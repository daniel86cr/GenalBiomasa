/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import com.dina.genasoft.common.ClientesSetup;
import com.dina.genasoft.common.CommonSetup;
import com.dina.genasoft.common.EmpleadosSetup;
import com.dina.genasoft.common.EmpresasSetup;
import com.dina.genasoft.common.FacturasSetup;
import com.dina.genasoft.common.MaterialesSetup;
import com.dina.genasoft.common.MonedasSetup;
import com.dina.genasoft.common.OperadoresSetup;
import com.dina.genasoft.common.PesajesSetup;
import com.dina.genasoft.common.RolesSetup;
import com.dina.genasoft.common.TransportistasSetup;
import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TEmpleadosVista;
import com.dina.genasoft.db.entity.TEmpresas;
import com.dina.genasoft.db.entity.TFacturas;
import com.dina.genasoft.db.entity.TMateriales;
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TRegistrosCambiosClientes;
import com.dina.genasoft.db.entity.TRegistrosCambiosEmpleados;
import com.dina.genasoft.db.entity.TRoles;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.EnvioCorreo;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.EmpleadoEnum;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero
 * Controlador para las vistas de la aplicación.
 */
@Component
@Slf4j
@Data
public class ControladorVistas implements Serializable {

    private static final long   serialVersionUID = 5264118420322183128L;
    /** Inyección de Spring para poder acceder a la capa de datos.*/
    @Autowired
    private Controller          controller;
    /** Inyección de Spring para poder acceder a la capa de clientes.*/
    @Autowired
    private ClientesSetup       clientesSetup;
    /** Inyección de Spring para poder acceder a la capa de datos.*/
    @Autowired
    private CommonSetup         commonSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de empresas.*/
    @Autowired
    private EmpresasSetup       empresasSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de materiales.*/
    @Autowired
    private MaterialesSetup     materialesSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de monedas.*/
    @Autowired
    private MonedasSetup        monedasSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de operadores.*/
    @Autowired
    private OperadoresSetup     operadoresSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de facturas.*/
    @Autowired
    private FacturasSetup       facturasSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de pesajes.*/
    @Autowired
    private PesajesSetup        pesajesSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de pesajes.*/
    @Autowired
    private TransportistasSetup transportistasSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de empleados.*/
    @Autowired
    private EmpleadosSetup      empleadosSetup;
    /** Inyección de Spring para poder acceder a la capa de roles.*/
    @Autowired
    private RolesSetup          rolesSetup;
    /** Para enviar correos.*/
    @Autowired
    private EnvioCorreo         envioCorreo;
    /** Contendrá el ID del usuario del administrador para recibir notificaciones.*/
    @Value("${user.notificacions}")
    private String              userNotifications;
    public Integer              estadoAplicacion;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String              appName;

    /**
     * Método que nos elimina el acceso existente del empleado.
     * @param idEmpleado El acceso del empleado a eliminar.
     */
    public void eliminaAcceso(Integer idEmpleado) {
        commonSetup.eliminaAcceso(idEmpleado);
    }

    /**
     * Método que es llamado para iniciar sesión.
     * @param username El nombre de usuario.
     * @param password Contraseña
     * @param La fecha con hora cuando hace login.
     * @return El empleado si se encuentra. <br>
     * <li> Si la contraseña no coincide, el valor de la contraseña es {@link EmpleadoEnum.PASS_INCORRECT} <br>
     * <li> Si está desactivado, el valor del campo activo es: {@link EmpleadoEnum.DESACTIVADO} <br>
     * <li> Si está en uso, el valor del campo nombre es: {@link Constants.EMPLEADO_EN_USO} <br>  
     * @throws GenasoftException Si se produce alguna excepción al realizar el inicio de sesión.
     */
    public TEmpleados iniciarSesion(String username, String password, Timestamp fecha) throws GenasoftException {
        if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
            controller.controlLicencia();
        }

        TEmpleados empleado = null;
        // Obtenemos el empleado de base de datos.
        empleado = empleadosSetup.obtenerEmpleado(username, password, fecha);

        // Retornamos el empleado encontrado.
        return empleado;
    }

    /**
     * Método que es llamado para iniciar sesión sin especificar password.
     * @param username El nombre de usuario.
     * @param La fecha con hora cuando hace login.
     * @return El empleado si se encuentra. <br>     
     * <li> Si está desactivado, el valor del campo activo es: {@link EmpleadoEnum.DESACTIVADO} <br>  
     * @throws GenasoftException Si se produce alguna excepción al realizar el inicio de sesión.
     * <b> SE RECOMIENDA QUE SOLO SE UTILICE ESTE MÉTODO EN CASOS MUY CONCRETOS, YA QUE NO HAY SEGURIDAD <b>
     */
    public TEmpleados iniciarSesionSinPassword(String username, Timestamp fecha) throws GenasoftException {

        if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
            throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
        }
        TEmpleados empleado = null;
        // Obtenemos el empleado de base de datos.
        empleado = empleadosSetup.obtenerEmpleadoSinPassword(username, fecha);

        // Retornamos el empleado encontrado.
        return empleado;
    }

    /**
     * Método que nos retorna el empleado por usuario.
     * @param id El ID del empleado
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El empleado si se encuentra en base de datos activo.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TEmpleados obtenerEmpleadoPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TEmpleados empleado = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            // Obtenemos el empleado de base de datos.
            empleado = empleadosSetup.obtenerEmpleadoPorId(id);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el empleado encontrado.
        return empleado;
    }

    /**
     * Método que nos retorna el empleado por usuario.
     * @param id El ID del empleado
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El empleado si se encuentra en base de datos activo.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TEmpleados> obtenerEmpleadosPorRolActivos(Integer idRol, Integer userId, long time) throws GenasoftException {
        List<TEmpleados> lResult = Utils.generarListaGenerica();
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            // Obtenemos el empleado de base de datos.
            lResult = empleadosSetup.obtenerEmpleadosPorRolActivos(idRol);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos los empleados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna el empleado por usuario.
     * @param id El ID del empleado
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El empleado si se encuentra en base de datos activo.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TEmpleados obtenerEmpleadoPorNombreUsuario(String username) {
        TEmpleados empleado = null;

        empleado = empleadosSetup.obtenerEmpleadoPorNombreUsuario(username);

        // Retornamos el empleado encontrado.
        return empleado;
    }

    /**
     * Método que se encarga de crear un nuevo empleado en el sistema.
     * @param record El empleado a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearEmpleado(TEmpleados record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = empleadosSetup.crearEmpleado(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del empleado.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo empleado en el sistema.
     * @param record El empleado a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearPermiso(TPermisos record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            commonSetup.crearPermiso(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del empleado.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public Integer crearClienteRetornaId(TClientes record, Integer userId, long time) throws GenasoftException {
        Integer result;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.crearClienteRetornaId(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del cliente.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearCliente(TClientes record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.crearCliente(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del cliente.
        return result;
    }

    /**
     * Método que se encarga de modificar un cliente en el sistema.
     * @param record El cliente a modificar.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String modificarCliente(TClientes record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.modificarCliente(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la modificación del cliente.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public Integer crearEmpresaRetornaId(TEmpresas record, Integer userId, long time) throws GenasoftException {
        Integer result;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = empresasSetup.crearEmpresaRetornaId(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del cliente.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearEmpresa(TEmpresas record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = empresasSetup.crearEmpresa(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del cliente.
        return result;
    }

    /**
     * Método que se encarga de modificar un cliente en el sistema.
     * @param record El cliente a modificar.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String modificarEmpresa(TEmpresas record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = empresasSetup.modificarEmpresa(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la modificación del cliente.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public Integer crearFacturaRetornaId(TFacturas record, Integer userId, long time) throws GenasoftException {
        Integer result;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = facturasSetup.crearFacturaRetornaId(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del cliente.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearFactura(TFacturas record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = facturasSetup.crearFactura(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del cliente.
        return result;
    }

    /**
     * Método que se encarga de modificar un cliente en el sistema.
     * @param record El cliente a modificar.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String modificarFactura(TFacturas record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = facturasSetup.modificarFactura(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la modificación del cliente.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public Integer crearMaterialRetornaId(TMateriales record, Integer userId, long time) throws GenasoftException {
        Integer result;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = materialesSetup.crearMaterialRetornaId(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del cliente.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearMaterial(TMateriales record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = materialesSetup.crearMaterial(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del cliente.
        return result;
    }

    /**
     * Método que se encarga de modificar un cliente en el sistema.
     * @param record El cliente a modificar.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String modificarMaterial(TMateriales record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = materialesSetup.modificarMaterial(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la modificación del cliente.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public Integer crearOperadorRetornaId(TOperadores record, Integer userId, long time) throws GenasoftException {
        Integer result;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = operadoresSetup.crearOperadorRetornaId(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del cliente.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearOperador(TOperadores record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = operadoresSetup.crearOperador(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del cliente.
        return result;
    }

    /**
     * Método que se encarga de modificar un cliente en el sistema.
     * @param record El cliente a modificar.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String modificarOperador(TOperadores record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = operadoresSetup.modificarOperador(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la modificación del cliente.
        return result;
    }

    /**
     * Método que se encarga de modificar el empleado en el sistema.
     * @param record El empleado a modificar.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String modificarEmpleado(TEmpleados record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = empleadosSetup.modificarEmpleado(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la modificación del empleado.
        return result;
    }

    /**
     * Método que nos retorna la descripción del código que nos pasan por parámetro.
     * @param codigo El código a buscar.
     * @return La descripción.
     */
    public String obtenerDescripcionCodigo(String codigo) {
        return commonSetup.obtenerDescripcionCodigo(codigo);
    }

    /**
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TClientes> obtenerClientesActivos(Integer userId, long time) throws GenasoftException {
        List<TClientes> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = clientesSetup.obtenerClientesActivos();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los clientes encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna el cliente a partir del nombre
     * @param nombre El nombre del cliente por el que realizar la búsqueda.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El cliente encontrado.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TClientes obtenerClientePorNombre(String nombre, Integer userId, long time) throws GenasoftException {
        TClientes result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.obtenerClientePorNombre(nombre);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos el cliente encontrado.
        return result;
    }

    /**
     * Método que nos retorna el cliente a partir del ID.
     * @param id El ID del cliente por el que realizar la búsqueda.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El cliente encontrado.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TClientes obtenerClientePorId(Integer id, Integer userId, long time) throws GenasoftException {
        TClientes result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.obtenerClientePorId(id);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos el cliente encontrado.
        return result;
    }

    /**
     * Método que nos retorna los clientes existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TClientes> obtenerTodosClientes(Integer userId, long time) throws GenasoftException {
        List<TClientes> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = clientesSetup.obtenerTodosClientes();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los clientes encontrados.
        return lResult;
    }

    /**
     * Método que nos añade el logo de Bostel a la pantalla.
     */
    @SuppressWarnings("serial")
    public HorizontalLayout logoGenaSoft(Object... titulo) {

        // Image link
        Link iconic = new Link("", new ExternalResource(""));
        iconic.setIcon(new ThemeResource("logo/appLogo3.png"));

        // Obtenemos la imagen desde el tema aplicado a la aplicación
        // Resource resource = new ThemeResource("logo/appLogo2.png");
        // Cargamos la imagen desde el objeto Image
        //  Image image = new Image(null, resource);

        // Logo de Brostel
        HorizontalLayout imgGenalBiomasa = new HorizontalLayout();

        imgGenalBiomasa.addComponent(iconic);
        imgGenalBiomasa.setComponentAlignment(iconic, Alignment.TOP_RIGHT);
        imgGenalBiomasa.setSizeFull();

        return imgGenalBiomasa;
    }

    /**
     * Método que nos añade el logo de Bostel a la pantalla.
     */
    public VerticalLayout logoCliente() {

        // Obtenemos la imagen desde el tema aplicado a la aplicación
        Resource resource = new ThemeResource("logo/appLogo.png");
        // Cargamos la imagen desde el objeto Image
        Image image = new Image(null, resource);

        Label l = new Label(" ");
        l.setHeight(2, Sizeable.Unit.EM);

        // Logo de TRAZABILIDADES[Natural TROPIC]
        VerticalLayout imgCliente = new VerticalLayout();
        imgCliente.addComponent(l);
        imgCliente.addComponent(image);
        imgCliente.setComponentAlignment(image, Alignment.TOP_CENTER);
        imgCliente.setWidth(100, Sizeable.Unit.PERCENTAGE);

        return imgCliente;
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
    public String envioCorreo(Integer idEmpleado, String titulo, String cuerpo, String adjunto, Integer userId, long time) throws NumberFormatException, GenasoftException {
        String result = "Notificación enviada correctamente.";
        TEmpleados empl = null;
        try {
            empl = obtenerEmpleadoPorId(idEmpleado, userId, time);
            if (empl != null && empl.getEmail() != null && !empl.getEmail().isEmpty()) {
                envioCorreo.enviarCorreo2(empl.getEmail(), titulo, cuerpo, adjunto);
            }
        } catch (MailException | InterruptedException e) {
            result = "Error al enviar la notificación, compruebe que la dirección de correo es correcta. Si el problema persiste, contacte con el administrador";
            //enviarTelegram(Integer.valueOf(userNotifications), "** ERROR TRAZABILIDADES [NATURAL TROPIC] *** \\n Error al enviar correo al destinatario. \\n ID empleado: " + idEmpleado + ", email:" + empl.getEmail(), userId, time);
            //enviarWhatsApp(Integer.valueOf(userNotifications), "** ERROR TRAZABILIDADES [NATURAL TROPIC] *** \\n Error al enviar correo al destinatario. \\n ID empleado: " + idEmpleado + ", email:" + empl.getEmail(), userId, time);
        } catch (Exception e) {

        }

        return result;
    }

    public void enviarNotificacion(String texto, String cuerpo) {
        try {
            commonSetup.enviaNotificacionCorreoMasiva(texto, cuerpo, null);
            commonSetup.enviaNotificacionWhatsAppMasivo(cuerpo);
        } catch (GenasoftException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Método que es llamado para comprobar los permisos del empleado.
     * @param empleado El empleado. 
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los permisos del empleado, si existen.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public TPermisos obtenerPermisosEmpleado(TEmpleados empleado, Integer userId, long time) throws GenasoftException {
        TPermisos permisos = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            permisos = commonSetup.obtenerPermisosPorRol(empleado.getId());
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos los permisos encontrados.
        return permisos;
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los roles encontrados.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TRoles> obtenerRolesActivosSinMaster(Integer userId, long time) throws GenasoftException {
        List<TRoles> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = rolesSetup.obtenerRolesActivosSinMaster();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los roles encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los roles existentes en el sistema excluyendo el rol Master.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los roles encontrados.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TRoles> obtenerTodosRoles(Integer userId, long time) throws GenasoftException {
        List<TRoles> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = rolesSetup.obtenerTodosRoles();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los roles encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los empleados dentro del sistema (Activo o no) todos los campos son String <br>
     * Esto quiere decir que el ID rol es el nombre del rol, el estado es Activo o No activo, etc.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Empleados existentes en el sistema.
     */
    public List<TEmpleadosVista> obtenerEmpleadosVista(Integer userId, long time) throws GenasoftException {
        List<TEmpleadosVista> empl = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            empl = empleadosSetup.obtenerEmpleadosVista();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retnornamos los empleados encontrados.
        return empl;
    }

    /**
     * Método que se encarga de modificar el empleado en el sistema desactivándolo..
     * @param idEmpleado El ID del empleado a desactivar.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String desactivarEmpleado(Integer idEmpleado, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            TEmpleados empl = empleadosSetup.obtenerEmpleadoPorId(idEmpleado);
            if (empl != null) {
                if (empl.getEstado().equals(EmpleadoEnum.ACTIVO.getValue())) {
                    empl.setEstado(EmpleadoEnum.DESACTIVADO.getValue());
                    empl.setUsuModifica(userId);
                    empl.setFechaModifica(Utils.generarFecha());
                    result = empleadosSetup.modificarEmpleado(empl);

                    // Creamos el registro de cambios sobre el empleado.
                    TRegistrosCambiosEmpleados cambio = new TRegistrosCambiosEmpleados();
                    cambio.setIdEmpleado(empl.getId());
                    cambio.setCambio("Se desactiva el empleado.");
                    cambio.setUsuCrea(userId);
                    cambio.setFechaCambio(Utils.generarFecha());
                    empleadosSetup.crearRegistroCambioEmpleado(cambio);
                } else {
                    result = Constants.EMPL_DESACTIVADO;
                }
            } else {
                result = Constants.EMPL_NO_EXISTE;
            }
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos el resultado de la modificación del empleado.
        return result;
    }

    /**
     * Método que se encarga de modificar el cliente en el sistema desactivándolo..
     * @param idCliente El ID del cliente a desactivar.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String desactivarCliente(Integer idCliente, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            TClientes cliente = clientesSetup.obtenerClientePorId(idCliente);
            if (cliente != null) {
                if (cliente.getEstado().equals(EmpleadoEnum.ACTIVO.getValue())) {
                    cliente.setEstado(EmpleadoEnum.DESACTIVADO.getValue());
                    cliente.setUsuModifica(userId);
                    cliente.setFechaModifica(Utils.generarFecha());
                    result = clientesSetup.modificarCliente(cliente);

                    // Creamos el registro de cambios sobre el cliente.
                    TRegistrosCambiosClientes cambio = new TRegistrosCambiosClientes();
                    cambio.setIdCliente(cliente.getId());
                    cambio.setCambio("Se desactiva el cliente.");
                    cambio.setUsuCrea(userId);
                    cambio.setFechaCambio(Utils.generarFecha());
                    clientesSetup.crearRegistroCambioCliente(cambio);
                } else {
                    result = Constants.CLIENTE_DESACTIVADO;
                }
            } else {
                result = Constants.CLIENTE_NO_EXISTE;
            }
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos el resultado de la modificación del empleado.
        return result;
    }

    /**
     * Método que nos guarda el registro de cambios del empleado.
     * @param record El registro a guardar
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioEmpleado(TRegistrosCambiosEmpleados record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = empleadosSetup.crearRegistroCambioEmpleado(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        return result;
    }

    /**
     * Método que nos guarda el registro de cambios del cliente.
     * @param record El registro a guardar
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioCliente(TRegistrosCambiosClientes record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.crearRegistroCambioCliente(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        return result;
    }

}
