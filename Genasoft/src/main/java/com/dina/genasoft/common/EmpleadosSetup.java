/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.common;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.db.entity.TAcceso;
import com.dina.genasoft.db.entity.TAccesoHis;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TEmpleadosVista;
import com.dina.genasoft.db.entity.TRegistrosCambiosEmpleados;
import com.dina.genasoft.db.entity.TRoles;
import com.dina.genasoft.db.mapper.TEmpleadosMapper;
import com.dina.genasoft.db.mapper.TRegistrosCambiosEmpleadosMapper;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Encriptacion;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.EmpleadoEnum;
import com.dina.genasoft.utils.enums.RolesEnum;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero
 * Clase que hace de 'facade' entre la lógica de empleados y la BD.
 */
@Component
@Slf4j
@Data
public class EmpleadosSetup implements Serializable {
    /** Serial ID de la aplicación Spring. */
    private static final long                serialVersionUID = 6316107860896774023L;
    /** Inyección de Spring para poder acceder a la capa de datos común.*/
    @Autowired
    private CommonSetup                      commonSetup;
    /** Inyección de Spring para poder acceder a la capa de roles*/
    @Autowired
    private RolesSetup                       rolesSetup;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger    log              = org.slf4j.LoggerFactory.getLogger(EmpleadosSetup.class);
    /** Inyección que se encarga de encriptar. */
    @Autowired
    private Encriptacion                     encriptacion;
    /** Inyección por Spring del mapper TEmpleadosMapper.*/
    @Autowired
    private TEmpleadosMapper                 tEmpleadosMapper;
    /** Inyección por Spring del mapper TRegistrosCambiosEmpleadosMapper.*/
    @Autowired
    private TRegistrosCambiosEmpleadosMapper tRegistrosCambiosEmpleadosMapper;
    @Value("${app.max.time.idle}")
    /** El tiempo máximo de inactividad.*/
    private String                           maxIdel;

    /**
     * Método que nos retorna el empleado por usuario y contraseña.
     * @param username El nombre del usuario
     * @param password La contraseña.
     * @return El empleado si se encuentra con las credenciales facilitadas.
     * @throws TransTrazabilidadesException Si el empleado está en uso
     */
    @SuppressWarnings("static-access")
    public TEmpleados obtenerEmpleado(String username, String password, Timestamp fechaInicioSesion) throws GenasoftException {
        TEmpleados empl = tEmpleadosMapper.obtenerEmpleadoPorNombreUsuario(username.toLowerCase());
        // Si existe el empleado, comprobamos si está activo y coinciden las credenciales.
        if (empl != null) {

            String pass = encriptacion.getStringMessageDigest(password, encriptacion.SHA1);
            if (!empl.getPassword().equals(pass)) {
                // Estableciendo la contraseña a nulo indicamos que no es correcta.
                throw new GenasoftException(Constants.EMPLADO_CONTRASEÑA_INCORRECTA);
            } else if (empl.getEstado() == EmpleadoEnum.DESACTIVADO.getValue()) {
                throw new GenasoftException(Constants.BD_KO_EMPLEADO_NO_EXISTE);
            } else {
                // Comprobamos el acceso.
                TAcceso acceso = commonSetup.obtenerAccesoEmpleado(empl.getId());
                TAccesoHis accHis = null;
                if (acceso != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date(acceso.getFechaUltAcceso().getTime()));
                    // El timpo maximo de inactividad.
                    int minutos = Integer.valueOf(maxIdel);
                    cal.add(Calendar.MINUTE, minutos);
                    // Se modifica el acceso por inactividad.
                    if (cal.getTime().before(Utils.generarFecha())) {
                        acceso.setFecha(fechaInicioSesion);
                        acceso.setFechaUltAcceso(fechaInicioSesion);
                        commonSetup.modificarAccesoEmpleado(acceso);
                        // Acceso HIS
                        accHis = new TAccesoHis();
                        accHis.setFecha(fechaInicioSesion);
                        accHis.setFechaAcceso(fechaInicioSesion);
                        accHis.setIdEmpleado(empl.getId());
                        commonSetup.crearAccesoEmpleadoHis(accHis);
                    } else {
                        // No es el mismo empleado.
                        if (!acceso.getFecha().equals(fechaInicioSesion)) {
                            throw new GenasoftException(Constants.EMPLEADO_EN_USO);
                        }
                    }
                } else {
                    acceso = new TAcceso();
                    acceso.setFecha(fechaInicioSesion);
                    acceso.setFechaUltAcceso(fechaInicioSesion);
                    acceso.setIdEmpleado(empl.getId());
                    commonSetup.crearAccesoEmpleado(acceso);
                    // Acceso HIS
                    accHis = new TAccesoHis();
                    accHis.setFecha(fechaInicioSesion);
                    accHis.setFechaAcceso(fechaInicioSesion);
                    accHis.setIdEmpleado(empl.getId());
                    commonSetup.crearAccesoEmpleadoHis(accHis);

                }
            }
        } else {
            throw new GenasoftException(Constants.BD_KO_EMPLEADO_NO_EXISTE);
        }
        // Retnornamos el empleado encontrado
        return empl;
    }

    /**
     * Método que nos retorna el empleado por usuario y contraseña.
     * @param username El nombre del usuario
     * @param password La contraseña.
     * @return El empleado si se encuentra con las credenciales facilitadas.
     * @throws TransTrazabilidadesException Si el empleado está en uso
     */
    public TEmpleados obtenerEmpleadoSinPassword(String username, Timestamp fechaInicioSesion) throws GenasoftException {
        TEmpleados empl = tEmpleadosMapper.obtenerEmpleadoPorNombreUsuario(username);
        // Si existe el empleado, comprobamos si está activo y coinciden las credenciales.
        if (empl != null) {

            if (empl.getEstado().equals(EmpleadoEnum.DESACTIVADO.getValue())) {
                // Indicamos que no está activo el empleado.
                empl.setEstado(EmpleadoEnum.DESACTIVADO.getValue());
            } else {
                // Comprobamos el acceso.
                TAcceso acceso = commonSetup.obtenerAccesoEmpleado(empl.getId());
                if (acceso != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date(acceso.getFechaUltAcceso().getTime()));
                    // El timpo maximo de inactividad.
                    int minutos = Integer.valueOf(maxIdel);
                    cal.add(Calendar.MINUTE, minutos);
                    // Se modifica el acceso por inactividad.
                    if (cal.getTime().before(Utils.generarFecha())) {
                        acceso.setFecha(fechaInicioSesion);
                        acceso.setFechaUltAcceso(fechaInicioSesion);
                        commonSetup.modificarAccesoEmpleado(acceso);
                    } else {
                        // No es el mismo empleado.
                        if (!acceso.getFecha().equals(fechaInicioSesion)) {
                            commonSetup.eliminaAcceso(empl.getId());
                            acceso = new TAcceso();
                            acceso.setFecha(fechaInicioSesion);
                            acceso.setFechaUltAcceso(fechaInicioSesion);
                            acceso.setIdEmpleado(empl.getId());
                            commonSetup.crearAccesoEmpleado(acceso);
                        }
                    }
                } else {
                    acceso = new TAcceso();
                    acceso.setFecha(fechaInicioSesion);
                    acceso.setFechaUltAcceso(fechaInicioSesion);
                    acceso.setIdEmpleado(empl.getId());
                    commonSetup.crearAccesoEmpleado(acceso);
                }
            }
        }
        // Retnornamos el empleado encontrado
        return empl;
    }

    /**
     * Método que se encarga de crear un nuevo empleado en el sistema.
     * @param record El empleado a crear.     
     * @return El código del resultado de la operación.
     */
    @SuppressWarnings("static-access")
    public String crearEmpleado(TEmpleados record) {
        record.setNombreUsuario(record.getNombreUsuario().toLowerCase());
        if (record.getDni() != null) {
            record.setDni(record.getDni().trim().toUpperCase());
        }
        if (record.getIdExterno() != null) {
            record.setIdExterno(record.getIdExterno().trim().toUpperCase());
        }
        if (record.getCodigoAcceso() != null) {
            record.setCodigoAcceso(record.getCodigoAcceso().trim().toUpperCase());
        }
        record.setNombre(record.getNombre().trim().toUpperCase());
        // El resultado de la operación.
        String result = Constants.OPERACION_OK;

        // Comprobamos si existe el empleado
        String aux = existeEmpleado(record);

        if (record.getCodigoAcceso() != null && !record.getCodigoAcceso().isEmpty()) {
            TEmpleados aux2 = tEmpleadosMapper.obtenerEmpleadoPorCodAcceso(record.getCodigoAcceso());
            if (aux2 != null) {
                return Constants.BD_KO_CREAR_EMPLEADO_EXISTE_COD_ACCESO;
            }
        }

        // Comprobamos, en caso de que tenga código de acceso, si está en otro usuario
        if (aux == null) {
            /**   if (record.getCodigoAcceso() != null && !record.getCodigoAcceso().trim().isEmpty()) {
                if (obtenerEmpleadoPorCodAcceso(record.getCodigoAcceso().trim().toLowerCase()) != null) {
                    aux = Constants.EMPL_EXISTE_COD_ACCESO;
                }
            }
            */
        }

        if (aux != null) {
            result = aux;
        } else {
            try {
                // Al crear el empleado, hay que encriptar la contraseña.
                String pAux = encriptacion.getStringMessageDigest(record.getPassword(), encriptacion.SHA1);
                record.setPassword(pAux);
                result = tEmpleadosMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_EMPL;
            } catch (Exception e) {
                result = Constants.BD_KO_CREA_EMPL;
                log.error(Constants.BD_KO_CREA_EMPL + ", Error al crear el empleado: " + record.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de modificar el empleado en el sistema.
     * @param record El empleado a modificar.     
     * @return El código del resultado de la operación.
     */
    @SuppressWarnings("static-access")
    public String modificarEmpleado(TEmpleados record) {
        record.setNombreUsuario(record.getNombreUsuario().trim().toLowerCase());
        if (record.getDni() != null) {
            record.setDni(record.getDni().trim().toUpperCase());
        }
        if (record.getIdExterno() != null) {
            record.setIdExterno(record.getIdExterno().trim().toUpperCase());
        }
        record.setNombre(record.getNombre().trim().toUpperCase());
        // El resultado de la operación.
        String result = Constants.OPERACION_OK;

        // Comprobamos si existe el empleado
        TEmpleados aux = tEmpleadosMapper.selectByPrimaryKey(record.getId());

        if (aux == null) {
            result = Constants.EMPL_NO_EXISTE;
        } else {
            try {
                // Comprobamos si la contraseña ha cambiado, en tal caso, le asignamos la nueva.
                if (!aux.getPassword().equals(record.getPassword())) {
                    String pAux = encriptacion.getStringMessageDigest(record.getPassword(), encriptacion.SHA1);
                    record.setPassword(pAux);
                }

                // Comprobamos si los campos que son únicos están en uso.
                aux = tEmpleadosMapper.obtenerEmpleadoPorNombreUsuario(record.getNombreUsuario());
                if (aux != null && !aux.getId().equals(record.getId())) {
                    return Constants.EMPL_EXISTE_NOMBRE_USUARIO;
                }
                /**aux = tEmpleadosMapper.obtenerEmpleadoPorDni(record.getDni());
                if (aux != null && !aux.getId().equals(record.getId())) {
                    return Constants.EMPL_EXISTE_DNI;
                }
                */
                aux = tEmpleadosMapper.obtenerEmpleadoPorIdExterno(record.getIdExterno());
                if (aux != null && !aux.getId().equals(record.getId())) {
                    return Constants.EMPL_EXISTE_COD_EXTERNO;
                }

                aux = tEmpleadosMapper.obtenerEmpleadoPorNombre(record.getNombre());
                if (aux != null && !aux.getId().equals(record.getId())) {
                    return Constants.EMPL_EXISTE_NOMBRE;
                }
                // Comprobamos si existe el código de acceso del empleado en otro empleado
                if (record.getCodigoAcceso() != null && !record.getCodigoAcceso().trim().toLowerCase().isEmpty()) {
                    aux = tEmpleadosMapper.obtenerEmpleadoPorCodAcceso(record.getCodigoAcceso().trim().toLowerCase());
                    if (aux != null && !aux.getId().equals(record.getId())) {
                        return Constants.BD_KO_CREAR_EMPLEADO_EXISTE_COD_ACCESO;
                    }
                }

                result = tEmpleadosMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_EMPL;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_EMPL;
                log.error(Constants.BD_KO_MODIF_EMPL + ", Error al modificar el empleado: " + record.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que nos comprueba si existe el empleado en BD.
     * @param record El empleado a consultar.
     * @return True si existe, false en caso de no existir.
     * <br> Los campos por los que se realiza la consulta son: 
     * <li> Nombre de usuario </li>
     * <li> Nombre </li>
     * <li> DNI </li>
     * <li> ID Externo </li>
     */
    private String existeEmpleado(TEmpleados record) {

        TEmpleados aux = null;

        // Buscamos si existe un empleado con los datos únicos.        
        aux = tEmpleadosMapper.obtenerEmpleadoPorNombreUsuario(record.getNombreUsuario());
        if (aux == null) {
            aux = tEmpleadosMapper.obtenerEmpleadoPorNombre(record.getNombre());
            if (aux != null) {
                return Constants.EMPL_EXISTE_NOMBRE;
            }
        } else {
            return Constants.EMPL_EXISTE_NOMBRE_USUARIO;
        }
        // Retornamos el resultado.
        return null;
    }

    /**
     * Método que nos retorna el empleado por usuario.
     * @param id El ID del usuario
     * @return El empleado encontrado.
     */
    public TEmpleados obtenerEmpleadoPorId(Integer id) {
        return tEmpleadosMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el empleado por nombre de usuario.
     * @param username El nombre de usuario del emopleado.
     * @return El empleado encontrado.
     */
    public TEmpleados obtenerEmpleadoPorNombreUsuario(String username) {
        return tEmpleadosMapper.obtenerEmpleadoPorNombreUsuario(username);

    }

    /**
     * Método que nos retorna los empleados dentro del sistema (Activo o no) todos los campos son String <br>
     * Esto quiere decir que el ID rol es el nombre del rol, el estado es Activo o No activo, etc..
     * @return Empleados existentes en el sistema.
     */
    public List<TEmpleadosVista> obtenerEmpleadosVista() {
        // Buscamos los empleados y lo retornamos en formato TEmpleadosVista.
        return convertirEmpleadosVista(tEmpleadosMapper.obtenerTodosEmpleados());

    }

    /**
     * Método que nos retorna todos los empleados existentes en el sistema (activos o no)
     * @return Lista de empleados encontrados.
     */
    public List<TEmpleados> obtenerTodosEmpleados() {
        return tEmpleadosMapper.obtenerTodosEmpleados();
    }

    /**
     * Método que nos retorna todos los empleados existentes en el sistema (activos o no)
     * @return Lista de empleados encontrados.
     */
    public List<TEmpleados> obtenerEmpleadosPorRolActivos(Integer idRol) {
        return tEmpleadosMapper.obtenerEmpleadosPorRolActivos(idRol);
    }

    /**
     * Método que se encarega de convertir los datos de empleado {@link TEmpleados} a formato {@link TEmpleadosVista}
     * @param lEmpleados Lista de empleados a convertir.
     * @return Lista de empleados convertida a {@link TEmpleadosVista}
     */
    public List<TEmpleadosVista> convertirEmpleadosVista(List<TEmpleados> lEmpleados) {
        // Los empleados con formato vista.
        List<TEmpleadosVista> lResult = Utils.generarListaGenerica();
        TEmpleadosVista aux = null;
        String rol = null;
        String empl = null;
        String pais = null;

        List<TEmpleados> lEmpl = obtenerTodosEmpleados();
        List<TRoles> lRoles = rolesSetup.obtenerTodosRoles();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));

        // Nutrimos el diccionario con los roles
        Map<Integer, String> mRoles = lRoles.stream().collect(Collectors.toMap(TRoles::getId, TRoles::getDescripcion));

        for (TEmpleados empleado : lEmpleados) {
            // Los usuarios Máster no se muestran en el listado.
            if (empleado.getIdRol().equals(RolesEnum.MASTER.getValue())) {
                continue;
            }

            aux = new TEmpleadosVista(empleado);
            // Realizamos la copia de los campos
            rol = mRoles.get(empleado.getIdRol());
            aux.setIdRol(rol != null ? rol : "Rol no encontrado: " + empleado.getIdRol());
            //pais = mPaises.get(empleado.getPais());
            aux.setPais(pais != null ? pais : "País no encontrado: " + empleado.getPais());
            empl = mEmpleados.get(empleado.getUsuCrea());
            aux.setUsuCrea(empl != null ? empl : "Empleado no encontrado: " + empleado.getUsuCrea());
            if (empleado.getUsuModifica() != null) {
                empl = mEmpleados.get(empleado.getUsuModifica());
                aux.setUsuModifica(empl != null ? empl : "Empleado no encontrado: " + empleado.getUsuModifica());
            }

            // Añadimos a la lista final.
            lResult.add(aux);
        }
        // Retnornamos los empleados en formato vista.
        return lResult;

    }

    /**
     * Método que se encarga de crear un nuevo registro de cambio de empleado en el sistema.
     * @param record El registro de cambio de empleado a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioEmpleado(TRegistrosCambiosEmpleados record) {

        String result = Constants.OPERACION_OK;

        try {
            result = tRegistrosCambiosEmpleadosMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_EMPL;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_EMPL;
            log.error(Constants.BD_KO_CREA_EMPL + ", Error al crear el registro de modificación del empleado: " + record.toString() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

}
