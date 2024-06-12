/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.common;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.DataBaseConnection;
import com.dina.genasoft.db.entity.TAcceso;
import com.dina.genasoft.db.entity.TAccesoHis;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TErrores;
import com.dina.genasoft.db.entity.TOperacionActual;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TTrace;
import com.dina.genasoft.db.entity.TTrace2;
import com.dina.genasoft.db.mapper.TAccesoHisMapper;
import com.dina.genasoft.db.mapper.TAccesoMapper;
import com.dina.genasoft.db.mapper.TErroresMapper;
import com.dina.genasoft.db.mapper.TOperacionActualMapper;
import com.dina.genasoft.db.mapper.TPermisosMapper;
import com.dina.genasoft.db.mapper.TTrace2Mapper;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.EnvioCorreo;
import com.dina.genasoft.utils.Telegram;
import com.dina.genasoft.utils.WhatsApp;
import com.dina.genasoft.utils.enums.EmpleadoEnum;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que se encarga de hacer de 'fachada' entre la capa de base de datos y la lógica de la aplicación.
 */
@Component
@Slf4j
@Data
public class CommonSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(CommonSetup.class);
    /** Inyección de Spring para poder acceder a la capa de datos de empleados.*/
    @Autowired
    private EmpleadosSetup                empleadosSetup;
    /** Serial ID de la aplicación Spring. */
    private static final long             serialVersionUID = -7194044566154533555L;
    /** Inyección por Spring del mapper TAccesosMapper.*/
    @Autowired
    private TAccesoMapper                 tAccesoMapper;
    /** Inyección por Spring del mapper TAccesosMapper.*/
    @Autowired
    private TAccesoHisMapper              tAccesoHisMapper;
    /** Inyección por Spring del mapper TPermisosMapper.*/
    @Autowired
    private TPermisosMapper               tPermisosMapper;
    /** Inyección por Spring del mapper TErroresMapper.*/
    @Autowired
    private TErroresMapper                tErroresMapper;
    /** Inyección por Spring del mapper TTrace2Mapper.*/
    @Autowired
    private TTrace2Mapper                 tTrace2Mapper;
    /** Inyección por Spring del mapper TOperacionActualMapper.*/
    @Autowired
    private TOperacionActualMapper        tOperacionActualMapper;
    /** Inyección por Spring del mapper Telegram.*/
    @Autowired
    private Telegram                      telegram;
    /** Inyección por Spring del mapper WhatsApp.*/
    @Autowired
    private WhatsApp                      whatsApp;
    /** Contendrá el ID del usuario del administrador para recibir notificaciones.*/
    @Value("${user.notificacions}")
    private String                        userNotifications;
    /** Contendrá el ID del usuario del administrador para recibir notificaciones.*/
    @Value("${user.notificacions2}")
    private String                        userNotifications2;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private EnvioCorreo                   envioCorreo;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.int.interval}")
    private Integer                       appIntInterval;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.int.max}")
    private Integer                       appIntMax;
    /** Para establecer conexión con la BD externa.*/
    @Autowired
    private DataBaseConnection            dbConection;

    /**
     * Nos comprueba si la sesión del empleado actual es correcta.
     * @param username El empleado.
     * @return true si es válida, en caso contrario false.
     */
    public boolean conexionValida(int idEmpleado, Timestamp fecha, int estadoAplicacion) {
        boolean result = false;

        if (estadoAplicacion == -1) {
            return false;
        } else {

            // Comprobamos el acceso.
            TAcceso acceso = tAccesoMapper.obtenerAccesoEmpleado(idEmpleado);

            String f1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fecha);

            if (acceso != null) {
                String f2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(acceso.getFecha());
                // Comprobamos si la hora es la misma que la del registro de BD.
                result = f1.equals(f2);
            }

            if (result) {
                result = empleadosSetup.obtenerEmpleadoPorId(idEmpleado).getEstado().equals(EmpleadoEnum.ACTIVO.getValue());
            }
        }

        // Retornamos el resultado.
        return result;
    }

    /**
     * Método que nos elimina el acceso existente del empleado.
     * @param idEmpleado El ID del empleado con acceso a eliminar.
     */
    public void eliminaAcceso(Integer idEmpleado) {
        tAccesoMapper.eliminarAccesoEmpleado(idEmpleado);
    }

    /**
     * Método que se encarga de modificar el acceso de empleado.
     * @param acceso El acceso a modificar
     * @return Los registros modificados.
     */
    public int modificarAccesoEmpleado(TAcceso acceso) {
        return tAccesoMapper.updateByPrimaryKey(acceso);
    }

    /**
     * Método que se encarga de crear el acceso de empleado.
     * @param acceso El acceso a crear
     * @return Los registros creados.
     */
    public int crearAccesoEmpleado(TAcceso acceso) {
        return tAccesoMapper.insert(acceso);
    }

    /**
     * Método que se encarga de crear el acceso de empleado.
     * @param acceso El acceso a crear
     * @return Los registros creados.
     */
    public int crearAccesoEmpleadoHis(TAccesoHis acceso) {
        return tAccesoHisMapper.insert(acceso);
    }

    /**
     * Método que nos retorna el acceso del empleado
     * @param idEmpleado El ID del empleado
     * @return El acceso asignado.
     */
    public TAcceso obtenerAccesoEmpleado(Integer idEmpleado) {
        return tAccesoMapper.obtenerAccesoEmpleado(idEmpleado);
    }

    /**
     * Método que nos retorna los permisos a partir del rol.
     * @param idRol El rol
     * @return Los permisos encontrados
     */
    public TPermisos obtenerPermisosPorRol(int idRol) {
        TPermisos permisos = tPermisosMapper.selectByPrimaryKey(idRol);
        // Retnornamos el permiso encontrado.
        return permisos;
    }

    /**
     * Método que nos retorna los permisos a partir del rol.
     * @param idRol El rol
     * @return Los permisos encontrados
     */
    public void crearPermiso(TPermisos record) {
        tPermisosMapper.insert(record);
    }

    /**
     * Método que nos retorna la descripción del código que nos pasan por parámetro.
     * @param codigo El código a buscar.
     * @return La descripción.
     */
    public String obtenerDescripcionCodigo(String codigo) {
        String result = "Error indeterminado.";
        TErrores err = tErroresMapper.selectByPrimaryKey(codigo);
        if (err != null) {
            result = err.getDescripcion();
        }
        // Retornamos la descripción.
        return result;
    }

    /**
     * Métdo para enviar notificaciones vía telegram.
     * @param texto El texto del mensaje.
     * @throws BrosException Si se produce algún error.
     */
    public void enviaNotificacionTelegramMasivo(String texto) throws GenasoftException {
        // Buscamos el empleado de las notificaciones
        TEmpleados empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications));
        telegram.enviarMensaje("+34" + empl.getTelefono(), texto);
        //empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications2));
        //telegram.enviarMensaje("+34" + empl.getTelefono(), texto);
    }

    /**
     * Métdo para enviar notificaciones vía telegram.
     * @param texto El texto del mensaje.
     * @throws BrosException Si se produce algún error.
     */
    public void enviaNotificacionWhatsAppMasivo(String texto) throws GenasoftException {
        // Buscamos el empleado de las notificaciones
        TEmpleados empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications));
        whatsApp.enviarMensaje("+34" + empl.getTelefono(), texto);
        //empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications2));
        //whatsApp.enviarMensaje("+34" + empl.getTelefono(), texto);
    }

    /**
     * Método que nos envía un correo electrónico a la dirección de correo especificado con el adjunto especificado.
     * @param email La dirección (o direcciones separadas por ;) para enviar la notificación.
     * @param titulo El título del correo.
     * @param cuerpo El cuerpo del mensaje.
     * @param adjunto El fichero adjunto (puede ser nulo.)
     * @return El resultado de la operación
     * @throws GenasoftException 
     * @throws NumberFormatException 
     */
    public String enviaNotificacionCorreo(String titulo, String cuerpo, String adjunto, Integer user) throws NumberFormatException, GenasoftException {
        String result = "Notificación enviada correctamente.";
        TEmpleados empl = null;
        try {
            empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(user));
            empl.setEmail("daniel86cr@gmail.com");
            //  if (empl != null && empl.getEmail() != null && !empl.getEmail().isEmpty()) {
            //envioCorreo.enviarCorreo(empl.getEmail(), titulo, cuerpo, adjunto);
            // }
        } catch (MailException e) {
            result = "Error al enviar la notificación, compruebe que la dirección de correo es correcta. Si el problema persiste, contacte con el administrador";
        } catch (Exception e) {

        }

        return result;
    }

    /**
     * Método que nos envía un correo electrónico a la dirección de correo especificado con el adjunto especificado.
     * @param email La dirección (o direcciones separadas por ;) para enviar la notificación.
     * @param titulo El título del correo.
     * @param cuerpo El cuerpo del mensaje.
     * @param adjunto El fichero adjunto (puede ser nulo.)
     * @return El resultado de la operación
     * @throws GenasoftException 
     * @throws NumberFormatException 
     */
    public String enviaNotificacionCorreoMasiva(String titulo, String cuerpo, String adjunto) throws NumberFormatException, GenasoftException {
        String result = "Notificación enviada correctamente.";
        TEmpleados empl = null;
        try {
            empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications));
            if (empl != null && empl.getEmail() != null && !empl.getEmail().isEmpty()) {
                envioCorreo.enviarCorreo(empl.getEmail(), titulo, cuerpo, adjunto);
            }
            empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications2));
            if (empl != null && empl.getEmail() != null && !empl.getEmail().isEmpty()) {
                envioCorreo.enviarCorreo(empl.getEmail(), titulo, cuerpo, adjunto);
            }
        } catch (MailException | InterruptedException e) {
            result = "Error al enviar la notificación, compruebe que la dirección de correo es correcta. Si el problema persiste, contacte con el administrador";
        } catch (Exception e) {

        }

        return result;
    }

    /**
     * Método que nos realiza la búsqueda de los pedidos existentes en el sistema externo.
     * @return Lista de pedidos externos {@link Ge07NlvPedidos}
     * @throws ConTransException Si se produce alguna excepción al realizarse la consulta.
     */

    public TTrace obtenerInformacionLicencia(String mac, String nombreCliente) throws GenasoftException {
        log.debug("Debug", " -> Obtener información licencia()");

        TTrace result = null;
        try {
            StringBuffer queryString = new StringBuffer("");
            PreparedStatement selectQuery = null;
            dbConection.getConnection();

            queryString.append("SELECT mac, cliente, last_check, validez, estado FROM t_trace where mac = ? and cliente = ?");

            selectQuery = dbConection.getConection().prepareStatement(queryString.toString());

            selectQuery.setString(1, mac.toUpperCase());
            selectQuery.setString(2, nombreCliente.toUpperCase());

            log.error("La query: " + selectQuery.toString());

            ResultSet rSet = selectQuery.executeQuery();

            // Recorremos los resultados encontrados y los añadimos a la lista final
            if (rSet.next()) {
                result = new TTrace();
                result.setMac(rSet.getString(1));
                result.setCliente(rSet.getString(2));
                result.setLastCheck(rSet.getDate(3));
                result.setValidez(rSet.getDate(4));
                result.setEstado(rSet.getInt(5));
            }

            rSet.close();
            selectQuery.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GenasoftException("SQLException  obtenerPedidosExternos(): " + e.getLocalizedMessage());
        } finally {
            if (dbConection.getConection() != null) {
                dbConection.releaseConnection();
            }
        }

        log.debug("Debug", " <- obtenerPedidosExternos()");

        // La información de la licencia.
        return result;
    }

    /**
     * Método que nos retorna el nº de instentos fallidos para comprobar la licencia.
     * @return El número de intentos fallidos.
     */
    public String guardarIntentos(TTrace2 record) {
        tTrace2Mapper.eliminarRegistros();
        tTrace2Mapper.insert(record);
        return Constants.OPERACION_OK;
    }

    /**
     * Método que nos realiza la búsqueda de los pedidos existentes en el sistema externo.
     * @return Lista de pedidos externos {@link Ge07NlvPedidos}
     * @throws ConTransException Si se produce alguna excepción al realizarse la consulta.
     */

    public void guardarInformacionLicencia(TTrace traza) throws GenasoftException {
        log.debug("Debug", " -> Guardar información licencia()");

        try {
            StringBuffer queryString = new StringBuffer("");
            PreparedStatement selectQuery = null;
            dbConection.getConnection();
            Timestamp t = new Timestamp(traza.getLastCheck().getTime());

            queryString.append("UPDATE t_trace set last_check = ? where mac = ? and cliente = ?");

            selectQuery = dbConection.getConection().prepareStatement(queryString.toString());

            selectQuery.setTimestamp(1, t);
            selectQuery.setString(2, traza.getMac());
            selectQuery.setString(3, traza.getCliente());

            selectQuery.executeUpdate();

            selectQuery.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GenasoftException("SQLException  guardarInformacionLicencia(): " + e.getLocalizedMessage());
        } finally {
            if (dbConection.getConection() != null) {
                dbConection.releaseConnection();
            }
        }

        log.debug("Debug", " <- Guardar información licencia()");
    }

    /**
     * Método que nos retorna el nº de instentos fallidos para comprobar la licencia.
     * @return El número de intentos fallidos.
     */
    public TTrace2 obtenerIntentosFallidosLicencia() {
        TTrace2 result = null;
        result = tTrace2Mapper.obtenerIntentos();
        if (result == null) {
            result = new TTrace2();
            result.setIntentos(0);
        }
        return result;
    }

    /**
     * Método que nos elimina la operación que el empleado ha hecho.
     * @param idEmpleado El id del empleado para eliminar la operación.
     */
    public void eliminarOperacionEmpleado(Integer idEmpleado) {
        tOperacionActualMapper.deleteByPrimaryKey(idEmpleado);
    }

    /**
     * Método que nos guarda la operación que el empleado está realizando.
     * @param idEmpleado El id del empleado para guardar la operación.
     */
    public void guardarOperacionEmpleado(TOperacionActual record) {
        tOperacionActualMapper.insert(record);
    }

    /**
     * Método que nos realiza la consulta de la operación por pantalla e ID entidad.
     * @param pantalla La pantalla donde se realiza la operación.
     * @param idEntidad El ID de la entidad que se está consultando (ID_PRESUPUESTO,  ID_FACTURA ...)
     * @return La operación encontrado.
     */
    public TOperacionActual obtenerOperacionEntidadPantalla(String pantalla, Integer idEntidad) {
        return tOperacionActualMapper.obtenerOperacionEntidadPantalla(pantalla, idEntidad);
    }

    /**
     * Método que nos retorna las operaciones más antiguas que la fecha pasada por parámetro.
     * @param min fecha maxima
     * @return Las operaciones encontradas
     */
    public List<TOperacionActual> obtenerOperacionesPorTiempo(Date max) {
        return tOperacionActualMapper.obtenerOperacionesPorTiempo(max);
    }

    /**
     * Método que nos registra en el sistema la operación que está realizando el empleado pasado por parámetro.
     * @param record El registro a guardar.
     * @return Si la operación se puede registrar, y por lo tanto realizar, el resultado será {@literal ConstantsMaestros.OPERACION_OK}. <br>
     * Si no se puede registear, quiere decir que hay un empleado realizando esa operación se retorna mensaje descriptivo.
     */
    public String registrarOperacionEmpleado(TOperacionActual record) {
        String result = "";

        if (record.getIdEntidad().equals(-1)) {
            // Si el id entidad es -1 puede ser un listado o alguna vista que no requiere bloqueo, en ese caso nos la trae al pairo si existe la misma operación con el otro empleado.
            // Eliminamos la operación del empleado, para que no haya más de 1.
            eliminarOperacionEmpleado(record.getIdEmpleado());
            // Registramos la operación del empleado
            guardarOperacionEmpleado(record);
        } else {
            TOperacionActual aux = obtenerOperacionEntidadPantalla(record.getPantalla(), record.getIdEntidad());
            if (aux != null) {
                if (!aux.getIdEmpleado().equals(record.getIdEmpleado())) {
                    result = obtenerDescripcionCodigo(Constants.BD_KO_OPERACION_BLOQUEADA);
                    TEmpleados empl = empleadosSetup.obtenerEmpleadoPorId(aux.getIdEmpleado());
                    result += " " + empl.getNombre() + " en " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(record.getFecha());
                } else {
                    tOperacionActualMapper.updateByPrimaryKey(record);
                }

            } else {
                // Eliminamos la operación del empleado, para que no haya más de 1.
                eliminarOperacionEmpleado(record.getIdEmpleado());
                // Registramos la operación del empleado
                guardarOperacionEmpleado(record);
            }
        }

        return result;
    }

}
