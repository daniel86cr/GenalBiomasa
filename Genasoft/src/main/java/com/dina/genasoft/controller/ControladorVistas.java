/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.dina.genasoft.db.entity.TClientesMateriales;
import com.dina.genasoft.db.entity.TClientesOperadores;
import com.dina.genasoft.db.entity.TClientesVista;
import com.dina.genasoft.db.entity.TDireccionCliente;
import com.dina.genasoft.db.entity.TDireccionClienteVista;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TEmpleadosVista;
import com.dina.genasoft.db.entity.TEmpresas;
import com.dina.genasoft.db.entity.TFacturas;
import com.dina.genasoft.db.entity.TFacturasVista;
import com.dina.genasoft.db.entity.TIva;
import com.dina.genasoft.db.entity.TIvaVista;
import com.dina.genasoft.db.entity.TLineasFactura;
import com.dina.genasoft.db.entity.TLineasFacturaVista;
import com.dina.genasoft.db.entity.TMateriales;
import com.dina.genasoft.db.entity.TMaterialesVista;
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TOperadoresVista;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TPesajes;
import com.dina.genasoft.db.entity.TPesajesVista;
import com.dina.genasoft.db.entity.TRegistrosCambiosClientes;
import com.dina.genasoft.db.entity.TRegistrosCambiosDireccionCliente;
import com.dina.genasoft.db.entity.TRegistrosCambiosEmpleados;
import com.dina.genasoft.db.entity.TRegistrosCambiosEmpresas;
import com.dina.genasoft.db.entity.TRegistrosCambiosFacturas;
import com.dina.genasoft.db.entity.TRegistrosCambiosMateriales;
import com.dina.genasoft.db.entity.TRegistrosCambiosOperadores;
import com.dina.genasoft.db.entity.TRegistrosCambiosPesajes;
import com.dina.genasoft.db.entity.TRegistrosCambiosTransportistas;
import com.dina.genasoft.db.entity.TRoles;
import com.dina.genasoft.db.entity.TTransportistas;
import com.dina.genasoft.db.entity.TTransportistasVista;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.EnvioCorreo;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.ClienteEnum;
import com.dina.genasoft.utils.enums.EmpleadoEnum;
import com.dina.genasoft.utils.enums.EmpresaEnum;
import com.dina.genasoft.utils.enums.MaterialEnum;
import com.dina.genasoft.utils.enums.OperadorEnum;
import com.dina.genasoft.utils.enums.PesajesEnum;
import com.dina.genasoft.utils.enums.TransportistaEnum;
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
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearDireccionCliente(TDireccionCliente record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.crearDireccionCliente(record);
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
    public String modificarDireccionCliente(TDireccionCliente record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.modificarDireccionCliente(record);
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
    public TDireccionCliente obtenerDireccionClientePorId(Integer id, Integer userId, long time) throws GenasoftException {
        TDireccionCliente result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.obtenerDireccionClientePorId(id);
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
    public List<TDireccionCliente> obtenerDireccionesClientePorIdCliente(Integer idCliente, Integer userId, long time) throws GenasoftException {
        List<TDireccionCliente> result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.obtenerDireccionesClientePorIdCliente(idCliente);
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
    public List<TDireccionClienteVista> obtenerDireccionesClientePorIdClienteVista(Integer idCliente, Integer userId, long time) throws GenasoftException {
        List<TDireccionClienteVista> result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.obtenerDireccionesClientePorIdClienteVista(idCliente);
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
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearLineaFactura(TLineasFactura record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = facturasSetup.crearLineaFactura(record);
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
     * Método que se encarga de crear un nuevo cliente en el sistema.
     * @param record El cliente a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public Integer crearPesajeRetornaId(TPesajes record, Integer userId, long time) throws GenasoftException {
        Integer result;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = pesajesSetup.crearPesajeRetornaId(record);
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
    public String crearPesaje(TPesajes record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = pesajesSetup.crearPesaje(record);
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
    public String modificarPesaje(TPesajes record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = pesajesSetup.modificarPesaje(record);
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
    public Integer crearTransportistaRetornaId(TTransportistas record, Integer userId, long time) throws GenasoftException {
        Integer result;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = transportistasSetup.crearTransportistaRetornaId(record);
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
    public String crearTransportista(TTransportistas record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = transportistasSetup.crearTransportista(record);
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
    public String modificarTransportista(TTransportistas record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = transportistasSetup.modificarTransportista(record);
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
     * Método que nos retorna los clientes existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TClientesVista> obtenerTodosClientesVista(Integer userId, long time) throws GenasoftException {
        List<TClientesVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = clientesSetup.obtenerTodosClientesVista();
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
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TEmpresas> obtenerEmpresasActivas(Integer userId, long time) throws GenasoftException {
        List<TEmpresas> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = empresasSetup.obtenerEmpresasActivas();
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
    public TEmpresas obtenerEmpresaPorNombre(String nombre, Integer userId, long time) throws GenasoftException {
        TEmpresas result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = empresasSetup.obtenerEmpresaPorNombre(nombre);
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
    public TEmpresas obtenerEmpresaPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TEmpresas result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = empresasSetup.obtenerEmpresaPorId(id);
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
    public List<TEmpresas> obtenerTodasEmpresas(Integer userId, long time) throws GenasoftException {
        List<TEmpresas> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = empresasSetup.obtenerTodasEmpresas();
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
    public TFacturas obtenerFacturaPorNumeroFactura(String nombre, Integer userId, long time) throws GenasoftException {
        TFacturas result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = facturasSetup.obtenerFacturaPorNumeroFactura(nombre);
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
    public TFacturas obtenerFacturaPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TFacturas result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = facturasSetup.obtenerFacturaPorId(id);
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
    public TLineasFactura obtenerLineaFacturaPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TLineasFactura result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = facturasSetup.obtenerLineaFacturaPorId(id);
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
    public List<TFacturas> obtenerFacturasFechas(String f1, String f2, Integer userId, long time) throws GenasoftException {
        List<TFacturas> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {

            Date fecha1;
            Date fecha2;
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            try {
                fecha1 = df.parse(f1 + " 00:00:00");

                fecha2 = df.parse(f2 + " 23:59:59");
            } catch (ParseException e) {
                throw new GenasoftException(Constants.FORMATO_DATETIME_INCORRECTO);
            }

            lResult = facturasSetup.obtenerFacturasFechas(fecha1, fecha2);
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
     * Método que nos retorna los clientes existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TLineasFactura> obtenerLineasFacturaPorIdFactura(Integer idFactura, Integer userId, long time) throws GenasoftException {
        List<TLineasFactura> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {

            lResult = facturasSetup.obtenerLineasFacturaPorIdFactura(idFactura);
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
     * Método que nos retorna los clientes existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TLineasFacturaVista> obtenerLineasFacturaPorIdFacturaVista(Integer idFactura, Integer userId, long time) throws GenasoftException {
        List<TLineasFacturaVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {

            lResult = facturasSetup.obtenerLineasFacturaPorIdFacturaVista(idFactura);
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
     * Método que nos retorna los clientes existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TFacturasVista> obtenerFacturasFechasVista(String f1, String f2, Integer userId, long time) throws GenasoftException {
        List<TFacturasVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {

            Date fecha1;
            Date fecha2;
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            try {
                fecha1 = df.parse(f1 + " 00:00:00");

                fecha2 = df.parse(f2 + " 23:59:59");
            } catch (ParseException e) {
                throw new GenasoftException(Constants.FORMATO_DATETIME_INCORRECTO);
            }

            lResult = facturasSetup.obtenerFacturasFechasVista(fecha1, fecha2);
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
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TMateriales> obtenerMaterialesActivos(Integer userId, long time) throws GenasoftException {
        List<TMateriales> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = materialesSetup.obtenerMaterialesActivos();
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
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TMateriales> obtenerMaterialesAsignadosCliente(Integer idCliente, Integer userId, long time) throws GenasoftException {
        List<TMateriales> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = materialesSetup.obtenerMaterialesAsignadosCliente(idCliente);
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
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TOperadores> obtenerOperadoresAsignadosCliente(Integer idCliente, Integer userId, long time) throws GenasoftException {
        List<TOperadores> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = operadoresSetup.obtenerOperadoresAsignadosCliente(idCliente);
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
    public TMateriales obtenerMaterialPorNombre(String nombre, Integer userId, long time) throws GenasoftException {
        TMateriales result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = materialesSetup.obtenerMaterialPorNombre(nombre);
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
    public TMateriales obtenerMaterialPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TMateriales result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = materialesSetup.obtenerMaterialPorId(id);
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
    public List<TMateriales> obtenerTodosMateriales(Integer userId, long time) throws GenasoftException {
        List<TMateriales> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = materialesSetup.obtenerTodosMateriales();
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
     * Método que nos retorna los clientes existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TMaterialesVista> obtenerTodosMaterialesVista(Integer userId, long time) throws GenasoftException {
        List<TMaterialesVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = materialesSetup.obtenerTodosMaterialesVista();
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
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TIva> obtenerTiposIvaActivos(Integer userId, long time) throws GenasoftException {
        List<TIva> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = monedasSetup.obtenerIvaActivos();
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
    public TIva obtenerIvaPorDescripcion(String nombre, Integer userId, long time) throws GenasoftException {
        TIva result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = monedasSetup.obtenerIvaPorDescripcion(nombre);
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
    public TIva obtenerIvaPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TIva result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = monedasSetup.obtenerIvaPorId(id);
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
    public String asignarOperadorCliente(TClientesOperadores record, Integer userId, long time) throws GenasoftException {
        String result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.asignarOperadorCliente(record);
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
    public String modificarOperadorCliente(TClientesOperadores record, Integer userId, long time) throws GenasoftException {
        String result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.modificarOperadorCliente(record);
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
    public String asignarMaterialCliente(TClientesMateriales record, Integer userId, long time) throws GenasoftException {
        String result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.asignarMaterialCliente(record);
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
    public String modificarMaterialCliente(TClientesMateriales record, Integer userId, long time) throws GenasoftException {
        String result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.modificarMaterialCliente(record);
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
    public List<TIva> obtenerTodosTipoIva(Integer userId, long time) throws GenasoftException {
        List<TIva> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = monedasSetup.obtenerTodosIva();
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
     * Método que nos retorna los clientes existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TIvaVista> obtenerTodosTipoIvaVista(Integer userId, long time) throws GenasoftException {
        List<TIvaVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = monedasSetup.obtenerTiposIvaVista();
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
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TOperadores> obtenerOperadoresActivos(Integer userId, long time) throws GenasoftException {
        List<TOperadores> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = operadoresSetup.obtenerOperadoresActivos();
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
    public TOperadores obtenerOperadorPorNombre(String nombre, Integer userId, long time) throws GenasoftException {
        TOperadores result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = operadoresSetup.obtenerOperadorPorNombre(nombre);
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
    public TOperadores obtenerOperadorPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TOperadores result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = operadoresSetup.obtenerOperadorPorId(id);
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
    public List<TOperadores> obtenerTodosOperadores(Integer userId, long time) throws GenasoftException {
        List<TOperadores> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = operadoresSetup.obtenerTodosOperadores();
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
     * Método que nos retorna los clientes existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TOperadoresVista> obtenerTodosOperadoresVista(Integer userId, long time) throws GenasoftException {
        List<TOperadoresVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = operadoresSetup.obtenerTodosOperadoresVista();
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
    public TPesajes obtenerPesajePorNumeroAlbaran(String albaran, Integer userId, long time) throws GenasoftException {
        TPesajes result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = pesajesSetup.obtenerPesajePorAlbaran(albaran);
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
    public TPesajes obtenerPesajePorId(Integer id, Integer userId, long time) throws GenasoftException {
        TPesajes result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = pesajesSetup.obtenerPesajePorId(id);
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
    public List<TPesajes> obtenerPesajesFechas(String f1, String f2, Integer userId, long time) throws GenasoftException {
        List<TPesajes> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {

            Date fecha1;
            Date fecha2;
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            try {
                fecha1 = df.parse(f1 + " 00:00:00");

                fecha2 = df.parse(f2 + " 23:59:59");
            } catch (ParseException e) {
                throw new GenasoftException(Constants.FORMATO_DATETIME_INCORRECTO);
            }

            lResult = pesajesSetup.obtenerPesajesFechas(fecha1, fecha2);
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
     * Método que nos retorna los clientes existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TPesajesVista> obtenerPesajesFechasVista(String f1, String f2, Integer userId, long time) throws GenasoftException {
        List<TPesajesVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {

            Date fecha1;
            Date fecha2;
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            try {
                fecha1 = df.parse(f1 + " 00:00:00");

                fecha2 = df.parse(f2 + " 23:59:59");
            } catch (ParseException e) {
                throw new GenasoftException(Constants.FORMATO_DATETIME_INCORRECTO);
            }

            lResult = pesajesSetup.obtenerPesajesFechasVista(fecha1, fecha2);
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
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TTransportistas> obtenerTransportistasActivos(Integer userId, long time) throws GenasoftException {
        List<TTransportistas> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = transportistasSetup.obtenerTransportistasActivos();
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
    public TTransportistas obtenerTransportistaPorNombre(String nombre, Integer userId, long time) throws GenasoftException {
        TTransportistas result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = transportistasSetup.obtenerTransportistaPorNombre(nombre);
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
     * Método que nos retorna el cliente a partir del nombre
     * @param nombre El nombre del cliente por el que realizar la búsqueda.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El cliente encontrado.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TOperadores obtenerOperadorPorCif(String nombre, Integer userId, long time) throws GenasoftException {
        TOperadores result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = operadoresSetup.obtenerOperadorPorCif(nombre);
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
     * Método que nos retorna el cliente a partir del nombre
     * @param nombre El nombre del cliente por el que realizar la búsqueda.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El cliente encontrado.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TTransportistas obtenerTransportistaPorCif(String nombre, Integer userId, long time) throws GenasoftException {
        TTransportistas result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = transportistasSetup.obtenerTransportistaPorCif(nombre);
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
    public TTransportistas obtenerTransportistaPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TTransportistas result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = transportistasSetup.obtenerTransportistaPorId(id);
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
    public List<TTransportistas> obtenerTodosTransportistas(Integer userId, long time) throws GenasoftException {
        List<TTransportistas> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = transportistasSetup.obtenerTodosTransportistas();
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
     * Método que nos retorna los clientes existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TTransportistasVista> obtenerTodosTransportistasVista(Integer userId, long time) throws GenasoftException {
        List<TTransportistasVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = transportistasSetup.obtenerTodosTransportistasVista();
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
     * Método que nos añade el logo de la aplicación a la pantalla.
     */
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
        Resource resource = new ThemeResource("logo/appLogo2.png");
        // Cargamos la imagen desde el objeto Image
        Image image = new Image(null, resource);

        Label l = new Label(" ");
        l.setHeight(2, Sizeable.Unit.EM);

        // Logo de TRAZABILIDADES[GENAL BIOMASA]
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
            //enviarTelegram(Integer.valueOf(userNotifications), "** ERROR TRAZABILIDADES [GENAL BIOMASA] *** \\n Error al enviar correo al destinatario. \\n ID empleado: " + idEmpleado + ", email:" + empl.getEmail(), userId, time);
            //enviarWhatsApp(Integer.valueOf(userNotifications), "** ERROR TRAZABILIDADES [GENAL BIOMASA] *** \\n Error al enviar correo al destinatario. \\n ID empleado: " + idEmpleado + ", email:" + empl.getEmail(), userId, time);
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
                if (cliente.getEstado().equals(ClienteEnum.ACTIVO.getValue())) {
                    cliente.setEstado(ClienteEnum.DESACTIVADO.getValue());
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
     * Método que se encarga de modificar el cliente en el sistema desactivándolo..
     * @param idEmpresa El ID del cliente a desactivar.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String desactivarEmpresa(Integer idEmpresa, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            TEmpresas cliente = empresasSetup.obtenerEmpresaPorId(idEmpresa);
            if (cliente != null) {
                if (cliente.getEstado().equals(EmpresaEnum.ACTIVO.getValue())) {
                    cliente.setEstado(EmpresaEnum.DESACTIVADO.getValue());
                    result = empresasSetup.modificarEmpresa(cliente);

                    // Creamos el registro de cambios sobre el cliente.
                    TRegistrosCambiosEmpresas cambio = new TRegistrosCambiosEmpresas();
                    cambio.setIdEmpresa(cliente.getId());
                    cambio.setCambio("Se desactiva la empresa.");
                    cambio.setUsuCrea(userId);
                    cambio.setFechaCambio(Utils.generarFecha());
                    empresasSetup.crearRegistroCambioEmpresa(cambio);
                } else {
                    result = Constants.EMPRESA_DESACTIVADA;
                }
            } else {
                result = Constants.EMPRESA_NO_EXISTE;
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
     * @param idMaterial El ID del cliente a desactivar.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String desactivarMaterial(Integer idMaterial, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            TMateriales material = materialesSetup.obtenerMaterialPorId(idMaterial);
            if (material != null) {
                if (material.getEstado().equals(MaterialEnum.ACTIVO.getValue())) {
                    material.setEstado(MaterialEnum.DESACTIVADO.getValue());
                    material.setUsuModifica(userId);
                    material.setFechaModifica(Utils.generarFecha());
                    result = materialesSetup.modificarMaterial(material);

                    // Creamos el registro de cambios sobre el material.
                    TRegistrosCambiosMateriales cambio = new TRegistrosCambiosMateriales();
                    cambio.setIdMaterial(material.getId());
                    cambio.setCambio("Se desactiva el material.");
                    cambio.setUsuCrea(userId);
                    cambio.setFechaCambio(Utils.generarFecha());
                    materialesSetup.crearRegistroCambioMaterial(cambio);
                } else {
                    result = Constants.MATERIAL_DESACTIVADO;
                }
            } else {
                result = Constants.MATERIAL_NO_EXISTE;
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
     * @param idOperador El ID del cliente a desactivar.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String desactivarOperador(Integer idOperador, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            TOperadores operador = operadoresSetup.obtenerOperadorPorId(idOperador);
            if (operador != null) {
                if (operador.getEstado().equals(OperadorEnum.ACTIVO.getValue())) {
                    operador.setEstado(OperadorEnum.DESACTIVADO.getValue());
                    operador.setUsuModifica(userId);
                    operador.setFechaModifica(Utils.generarFecha());
                    result = operadoresSetup.modificarOperador(operador);

                    // Creamos el registro de cambios sobre el material.
                    TRegistrosCambiosOperadores cambio = new TRegistrosCambiosOperadores();
                    cambio.setIdOperador(operador.getId());
                    cambio.setCambio("Se desactiva el operador.");
                    cambio.setUsuCrea(userId);
                    cambio.setFechaCambio(Utils.generarFecha());
                    operadoresSetup.crearRegistroCambioOperador(cambio);
                } else {
                    result = Constants.MATERIAL_DESACTIVADO;
                }
            } else {
                result = Constants.MATERIAL_NO_EXISTE;
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
     * @param idPesaje El ID del cliente a desactivar.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String desactivarPesaje(Integer idPesaje, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            TPesajes pesaje = pesajesSetup.obtenerPesajePorId(idPesaje);
            if (pesaje != null) {
                if (pesaje.getEstado().equals(PesajesEnum.ALBARAN.getValue())) {
                    pesaje.setEstado(PesajesEnum.ANULADO.getValue());
                    pesaje.setUsuModifica(userId);
                    pesaje.setFechaModifica(Utils.generarFecha());
                    result = pesajesSetup.modificarPesaje(pesaje);

                    // Creamos el registro de cambios sobre el material.
                    TRegistrosCambiosPesajes cambio = new TRegistrosCambiosPesajes();
                    cambio.setIdPesaje(pesaje.getId());
                    cambio.setCambio("Se anula el pesaje.");
                    cambio.setUsuCrea(userId);
                    cambio.setFechaCambio(Utils.generarFecha());
                    pesajesSetup.crearRegistroCambioPesaje(cambio);
                } else {
                    result = Constants.PESAJE_DESACTIVADO;
                }
            } else {
                result = Constants.PESAJE_NO_EXISTE;
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
     * @param idPesaje El ID del cliente a desactivar.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String desactivarTransportista(Integer idPesaje, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            TTransportistas transportista = transportistasSetup.obtenerTransportistaPorId(idPesaje);
            if (transportista != null) {
                if (transportista.getEstado().equals(TransportistaEnum.ACTIVO.getValue())) {
                    transportista.setEstado(TransportistaEnum.DESACTIVADO.getValue());
                    transportista.setUsuModifica(userId);
                    transportista.setFechaModifica(Utils.generarFecha());
                    result = transportistasSetup.modificarTransportista(transportista);

                    // Creamos el registro de cambios sobre el material.
                    TRegistrosCambiosTransportistas cambio = new TRegistrosCambiosTransportistas();
                    cambio.setIdTransportista(transportista.getId());
                    cambio.setCambio("Se anula el transportista.");
                    cambio.setUsuCrea(userId);
                    cambio.setFechaCambio(Utils.generarFecha());
                    transportistasSetup.crearRegistroCambioTransportista(cambio);
                } else {
                    result = Constants.TRANSPORTISTA_DESACTIVADO;
                }
            } else {
                result = Constants.TRANSPORTISTA_NO_EXISTE;
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

    /**
     * Método que nos guarda el registro de cambios del cliente.
     * @param record El registro a guardar
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioEmpresa(TRegistrosCambiosEmpresas record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = empresasSetup.crearRegistroCambioEmpresa(record);
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
    public String crearRegistroCambioFactura(TRegistrosCambiosFacturas record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = facturasSetup.crearRegistroCambioFactura(record);
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
    public String crearRegistroCambioMaterial(TRegistrosCambiosMateriales record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = materialesSetup.crearRegistroCambioMaterial(record);
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
    public String crearRegistroCambioOperador(TRegistrosCambiosOperadores record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = operadoresSetup.crearRegistroCambioOperador(record);
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
    public String crearRegistroCambioDireccionCliente(TRegistrosCambiosDireccionCliente record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = clientesSetup.crearRegistroCambioDireccionCliente(record);
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
    public String crearRegistroCambioPesaje(TRegistrosCambiosPesajes record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = pesajesSetup.crearRegistroCambioPesaje(record);
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
    public String crearRegistroCambioTransportista(TRegistrosCambiosTransportistas record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = transportistasSetup.crearRegistroCambioTransportista(record);
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
     * Método que se encarga de obtener el número de albarán que le corresponde al nuevo pedido
     * @param tipo El tipo de pedido a crear.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El número de albarán adjundicado.
     */
    public String obtenerNumeroAlbaran(String tipo, Integer userId, long time) throws GenasoftException {
        String result = "-1";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = pesajesSetup.obtenerNumeroAlbaran(tipo);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos el número del albarán
        return result;
    }

}
