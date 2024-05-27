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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import com.dina.genasoft.common.ClientesSetup;
import com.dina.genasoft.common.CommonSetup;
import com.dina.genasoft.common.ControlPtSetup;
import com.dina.genasoft.common.EmpleadosSetup;
import com.dina.genasoft.common.ImportadorSetup;
import com.dina.genasoft.common.ProductosSetup;
import com.dina.genasoft.common.ProveedoresSetup;
import com.dina.genasoft.common.RolesSetup;
import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TColumnasTablasEmpleado;
import com.dina.genasoft.db.entity.TCompras;
import com.dina.genasoft.db.entity.TComprasFict;
import com.dina.genasoft.db.entity.TComprasFictVista;
import com.dina.genasoft.db.entity.TComprasVista;
import com.dina.genasoft.db.entity.TControlProductoTerminado;
import com.dina.genasoft.db.entity.TControlProductoTerminadoFotos;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesBrix;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesBrixVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCaja;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCajaVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCalibre;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCalibreVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesConfeccion;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesConfeccionVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesPenetromia;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesPenetromiaVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoVista;
import com.dina.genasoft.db.entity.TDiametrosProducto;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TEmpleadosVista;
import com.dina.genasoft.db.entity.TLineaControlProductoTerminado;
import com.dina.genasoft.db.entity.TLineaControlProductoTerminadoVista;
import com.dina.genasoft.db.entity.TLineasVentas;
import com.dina.genasoft.db.entity.TLineasVentasVista;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TProductoCalibre;
import com.dina.genasoft.db.entity.TProductos;
import com.dina.genasoft.db.entity.TProductosVista;
import com.dina.genasoft.db.entity.TProveedores;
import com.dina.genasoft.db.entity.TProveedoresVista;
import com.dina.genasoft.db.entity.TRegistrosCambiosControlProductoTerminado;
import com.dina.genasoft.db.entity.TRegistrosCambiosEmpleados;
import com.dina.genasoft.db.entity.TRoles;
import com.dina.genasoft.db.entity.TVariedad;
import com.dina.genasoft.db.entity.TVentas;
import com.dina.genasoft.db.entity.TVentasFictVista;
import com.dina.genasoft.db.entity.TVentasVista;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.EnvioCorreo;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.EmpleadoEnum;
import com.dina.genasoft.utils.enums.ProductoEnum;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
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

    private static final long                       serialVersionUID               = 5264118420322183128L;
    /** Inyección de Spring para poder acceder a la capa de datos.*/
    @Autowired
    private Controller                              controller;
    /** Inyección de Spring para poder acceder a la capa de clientes.*/
    @Autowired
    private ClientesSetup                           clientesSetup;
    /** Inyección de Spring para poder acceder a la capa de Control PT.*/
    @Autowired
    private ControlPtSetup                          controlPtSetup;
    /** Inyección de Spring para poder acceder a la capa de datos.*/
    @Autowired
    private CommonSetup                             commonSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de empleados.*/
    @Autowired
    private EmpleadosSetup                          empleadosSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de importación.*/
    @Autowired
    private ImportadorSetup                         importadorSetup;
    /** Inyección de Spring para poder acceder a la capa de productos.*/
    @Autowired
    private ProductosSetup                          productosSetup;
    /** Inyección de Spring para poder acceder a la capa de proveedores.*/
    @Autowired
    private ProveedoresSetup                        proveedoresSetup;
    /** Inyección de Spring para poder acceder a la capa de roles.*/
    @Autowired
    private RolesSetup                              rolesSetup;
    /** Para enviar correos.*/
    @Autowired
    private EnvioCorreo                             envioCorreo;
    /** Contendrá el ID del usuario del administrador para recibir notificaciones.*/
    @Value("${user.notificacions}")
    private String                                  userNotifications;
    public Integer                                  estadoAplicacion;
    public Map<Integer, List<Integer>>              mTrazabilidadesComprasEmpleado;
    public Map<Integer, List<Integer>>              mComprasComprasVentasEmpleado;
    public Map<Integer, List<Double>>               mTotalesTrazabilidadesComprasEmpleado;
    public Map<Integer, List<Double>>               mTotalesTrazabilidadesVentasEmpleado;
    public Map<Integer, List<Double>>               mTotalesComprasComprasVentasEmpleado;
    public Map<Integer, List<Double>>               mTotalesVentasComprasVentasEmpleado;
    public Map<Integer, List<Integer>>              mTrazabilidadesVentasEmpleado;
    public Map<Integer, List<Integer>>              mComprasVentasVentasEmpleado;
    public Map<Integer, List<TVentasVista>>         mBalanceMasasExportar;
    public Map<Integer, List<TVentasVista>>         mBalanceTotalesMasasExportar;
    public List<String>                             lFiltrosBalanceMasas;
    public Map<Integer, Map<Integer, List<String>>> mFiltrosTrazabilidades;
    /** Contrendrá los resultados indexados por fecha de compras globales. */
    public TreeMap<Date, List<TComprasVista>>       mComprasGlobales               = null;
    /** Contrendrá los resultados indexados por fecha de compras globales. */
    public TreeMap<Date, List<TVentasVista>>        mVentasGlobales                = null;
    /** Contrendrá los resultados indexados por fecha de compras globales. */
    public TreeMap<String, List<TComprasVista>>     mComprasGlobalesPorAlbaranLote = null;
    /** Contrendrá los resultados indexados por fecha de compras globales. */
    public TreeMap<String, List<TVentasVista>>      mVentasGlobalesPorAlbaranLote  = null;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                                  appName;

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
            inicializarTodo(null);
            //importadorSetup.comprobarComprasVentas();
        }

        TEmpleados empleado = null;
        // Obtenemos el empleado de base de datos.
        empleado = empleadosSetup.obtenerEmpleado(username, password, fecha);

        // Retornamos el empleado encontrado.
        return empleado;
    }

    public void comprobarComprasVentas() throws GenasoftException {
        importadorSetup.comprobarComprasVentas();
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
    public Integer generarVentasDesdeCompra(TComprasVista compraVista, Date fechaCompra, String calidad, Integer userId, long time) throws GenasoftException {
        Integer result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.generarVentasDesdeCompra(compraVista, fechaCompra, calidad);
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
     * Método que se encarga de crear un nuevo empleado en el sistema.
     * @param record El empleado a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public Integer crearControlPtRetornaId(TControlProductoTerminado record, Integer userId, long time) throws GenasoftException {
        Integer result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = controlPtSetup.crearControlPtRetornaId(record);
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
    public Integer crearLineaControlPtRetornaId(TLineaControlProductoTerminado record, Integer userId, long time) throws GenasoftException {
        Integer result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = controlPtSetup.crearLineaControlPtRetornaId(record);
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
    public String modificarLineaControlPt(TLineaControlProductoTerminado record, Integer userId, long time) throws GenasoftException {
        String result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = controlPtSetup.modificarLineaControlPt(record);
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
    public String crearControlPt(TControlProductoTerminado record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = controlPtSetup.crearControlPt(record);
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
    public String crearPesajesCajasControlPt(Integer idControlPt, Integer idLinea, List<Double> lValores, Integer userId, long time) throws GenasoftException {
        String result = Constants.OPERACION_OK;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            controlPtSetup.guardarPesajesCajaControlPt(idControlPt, idLinea, lValores, userId);
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
    public String crearPesajesConfeccionControlPt(Integer idControlPt, Integer idLinea, List<Double> lValores, Integer userId, long time) throws GenasoftException {
        String result = Constants.OPERACION_OK;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            controlPtSetup.guardarPesajesConfeccionControlPt(idControlPt, idLinea, lValores, userId);
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
    public String crearPesajesCalibreControlPt(Integer idControlPt, Integer idLinea, List<Double> lValores, Integer userId, long time) throws GenasoftException {
        String result = Constants.OPERACION_OK;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            controlPtSetup.guardarPesajesCalibreControlPt(idControlPt, idLinea, lValores, userId);
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
    public String crearPesajesPenetromiaControlPt(Integer idControlPt, Integer idLinea, List<Double> lValores, Integer userId, long time) throws GenasoftException {
        String result = Constants.OPERACION_OK;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            controlPtSetup.guardarPesajesPenetromiaControlPt(idControlPt, idLinea, lValores, userId);
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
    public String crearPesajesBrixControlPt(Integer idControlPt, Integer idLinea, List<Double> lValores, Integer userId, long time) throws GenasoftException {
        String result = Constants.OPERACION_OK;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            controlPtSetup.guardarPesajesBrixControlPt(idControlPt, idLinea, lValores, userId);
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
    public TControlProductoTerminado obtenerControlPtPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TControlProductoTerminado result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = controlPtSetup.obtenerControlPtPorId(id);
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

    public TComprasFictVista obtenerCompraFicticiaVistaPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TComprasFictVista result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.obtenerCompraFicticiaVistaPorId(id);
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

    public TComprasFict obtenerCompraFicticiaPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TComprasFict result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.obtenerCompraFicticiaPorId(id);
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
    public String modificarControlPt(TControlProductoTerminado record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = controlPtSetup.modificarControlPt(record);
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
    public String crearFotoControlPt(TControlProductoTerminadoFotos record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = controlPtSetup.crearFotoControlPt(record);
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
    public String eliminarFotoControlProductoTerminadoIdFoto(Integer idFoto, Integer userId, long time) throws GenasoftException {
        String result = Constants.OPERACION_OK;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            controlPtSetup.eliminarFotoControlProductoTerminadoIdFoto(idFoto);
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
     * Método que se encarga de crear un nuevo producto en el sistema.
     * @param record El producto a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearProducto(TProductos record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = productosSetup.crearProducto(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del producto.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo producto en el sistema retornándonos el ID generado.
     * @param record El producto a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El ID del producto creado.
     */
    public Integer crearProductoRetornaId(TProductos record, Integer userId, long time) throws GenasoftException {
        Integer result = -1;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = productosSetup.crearProductoRetornaId(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del producto.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo proveedor en el sistema.
     * @param record El proveedor a crear.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String crearProveedor(TProveedores record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = proveedoresSetup.crearProveedor(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la inserción del proveedor.
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
     * Método que se encarga de modificar un producto en el sistema.
     * @param record El producto a modificar.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String modificarProducto(TProductos record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = productosSetup.modificarProducto(record);
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
     * Método que se encarga de modificar un proveedor en el sistema.
     * @param record El proveedor a modificar.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String modificarProveedor(TProveedores record, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = proveedoresSetup.modificarProveedor(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        // Retornamos el resultado de la modificación del proveedor.
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
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoFotos> obtenerImagenesIdControlPtDescripcion(Integer idControlPt, String descripcion, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoFotos> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerImagenesIdControlPtDescripcion(idControlPt, descripcion);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos las imagenes encontradas.
        return lResult;
    }

    /**
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoFotos> obtenerFotosIdControlPt(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoFotos> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerImagenesIdControlPt(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos las imagenes encontradas.
        return lResult;
    }

    /**
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoFotos> obtenerImagenesIdLinea(Integer idLinea, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoFotos> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerImagenesIdLinea(idLinea);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos las imagenes encontradas.
        return lResult;
    }

    /**
     * Método que nos retorna los clientes activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TControlProductoTerminadoFotos obtenerFotoControlPtPorId(Integer idFoto, Integer userId, long time) throws GenasoftException {
        TControlProductoTerminadoFotos result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = controlPtSetup.obtenerImagenControlPtPorId(idFoto);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos la imagen encontrada.
        return result;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TProductos> obtenerProductosActivos(Integer userId, long time) throws GenasoftException {
        List<TProductos> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = productosSetup.obtenerProductosActivos();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TProductos> obtenerTodosProductos(Integer userId, long time) throws GenasoftException {
        List<TProductos> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = productosSetup.obtenerTodosProductos();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TProductosVista> obtenerProductosActivosVista(Integer userId, long time) throws GenasoftException {
        List<TProductosVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = productosSetup.obtenerProductosActivosVista();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TProductosVista> obtenerTodosProductosVista(Integer userId, long time) throws GenasoftException {
        List<TProductosVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = productosSetup.obtenerTodosProductosVista();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
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
     * Método que nos retorna el proveedor a partir del nombre.
     * @param nombre El nombre por el que realizar la búsqueda.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El proveedor encontrado.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TProveedores obtenerProveedorPorNombre(String nombre, Integer userId, long time) throws GenasoftException {
        TProveedores result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = proveedoresSetup.obtenerProveedorPorNombre(nombre);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos el proveedor encontrado.
        return result;
    }

    /**
     * Método que nos importa el contenido del fichero pasado por parámetros con contenido de compras.
     * @param path El path que contiene el fichero.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la importación.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public String importarFicheroCompras(String path, String nombre, Integer userId, long time) throws GenasoftException {
        String result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.importarFicheroCompras(path, nombre, userId);
            reestablecerCompras();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos el proveedor encontrado.
        return result;
    }

    /**
     * Método que nos elimina la compra pasada por parámetros, guardando el registro en t_compras_his.
     * @param record El objeto a eliminar
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la importación.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public void eliminarCompra(TCompras record, Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            importadorSetup.eliminarCompra(record, userId);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

    }

    /**
     * Método que nos elimina la compra pasada por parámetros, guardando el registro en t_compras_his.
     * @param record El objeto a eliminar
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la importación.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public void eliminarCompraVista(TComprasVista record, Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            importadorSetup.eliminarCompraVista(record, userId);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

    }

    /**
     * Método que nos importa el contenido del fichero pasado por parámetros con contenido de ventas.
     * @param path El path que contiene el fichero.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la importación.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public String importarFicheroVentas(String path, String nombre, Integer userId, long time) throws GenasoftException {
        String result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.importarFicheroVentas(path, nombre, userId);
            //reestablecerCompras();
            //reestablecerVentas();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos el proveedor encontrado.
        return result;
    }

    /**
     * Método que nos importa el contenido del fichero pasado por parámetros con contenido de ventas.
     * @param path El path que contiene el fichero.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la importación.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public String importarFicheroLotesVentas(String path, String nombre, String fecha, Integer userId, long time) throws GenasoftException {
        String result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {

            String f1 = fecha + " 23:59:59";

            Date fecha1 = null;

            try {
                fecha1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(f1);
            } catch (ParseException e) {

            }

            result = importadorSetup.importarFicheroLotes(path, nombre, userId, fecha1);
            reestablecerCompras();
            reestablecerVentas();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos el proveedor encontrado.
        return result;
    }

    /**
     * Método que nos importa el contenido del fichero pasado por parámetros con contenido de ventas.
     * @param path El path que contiene el fichero.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la importación.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public String importarFicheroMovimientosAlmacen(String path, String nombre, String fecha, Integer userId, long time) throws GenasoftException {
        String result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {

            String f1 = fecha + " 23:59:59";

            Date fecha1 = null;

            try {
                fecha1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(f1);
            } catch (ParseException e) {

            }

            result = importadorSetup.importarFicheroMovimientosAlmacen(path, nombre, userId, fecha1);
            reestablecerCompras();
            reestablecerVentas();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos el proveedor encontrado.
        return result;
    }

    /**
     * Método que nos elimina la venta pasada por parámetros, guardando el registro en t_ventas_his.
     * @param record El objeto a eliminar
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la importación.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public void eliminarVenta(TVentas record, Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            importadorSetup.eliminarVenta(record, userId);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

    }

    /**
     * Método que nos elimina la venta pasada por parámetros, guardando el registro en t_ventas_his.
     * @param record El objeto a eliminar
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la importación.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public void eliminarVentaVista(TVentasVista record, Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            importadorSetup.eliminarVentaVista(record, userId);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

    }

    /**
     * Método que nos elimina la venta pasada por parámetros, guardando el registro en t_ventas_his.
     * @param record El objeto a eliminar
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la importación.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public void eliminarVentasIds(List<Integer> lIds, Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            importadorSetup.eliminarVentasIds(lIds);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

    }

    /**
     * Método que nos elimina la compra pasada por parámetros, guardando el registro en t_compras_his.
     * @param record El objeto a eliminar
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la importación.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public void eliminarComprasIds(List<Integer> lIds, Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            importadorSetup.eliminarComprasIds(lIds);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

    }

    /**
     * Método que nos retorna el proveedor a partir del ID.
     * @param id El ID por el que realizar la búsqueda.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El proveedor encontrado.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TProveedores obtenerProveedorPorId(Integer id, Integer userId, long time) throws GenasoftException {
        TProveedores result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = proveedoresSetup.obtenerProveedorPorId(id);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos el proveedor encontrado.
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
     * Método que nos retorna los proveedores existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los proveedores encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TProveedores> obtenerTodosProveedores(Integer userId, long time) throws GenasoftException {
        List<TProveedores> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = proveedoresSetup.obtenerTodosProveedores();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los proveedores encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los proveedores existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los proveedores encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TProveedoresVista> obtenerTodosProveedoresVista(Integer userId, long time) throws GenasoftException {
        List<TProveedoresVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = proveedoresSetup.obtenerProveedoresVista();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los proveedores encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los proveedores existentes en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los proveedores encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TreeMap<Date, List<TComprasVista>> obtenerTodasCompras(Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            if (mComprasGlobales == null || mComprasGlobales.keySet().isEmpty()) {
                cargarComprasGlobales();
            }
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return mComprasGlobales;
    }

    public TreeMap<Date, List<TVentasVista>> obtenerVentasGlobales(Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            //if (mVentasGlobales == null || mVentasGlobales.keySet().isEmpty()) {
            cargarVentasGlobales();
            //}
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return mVentasGlobales;
    }

    public TreeMap<Date, List<TVentasVista>> obtenerVentasGlobales2(Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            //if (mVentasGlobales == null || mVentasGlobales.keySet().isEmpty()) {
            cargarVentasGlobales();
            //}
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return mVentasGlobales;
    }

    public TreeMap<Date, List<TVentasVista>> obtenerVentasGlobales3(Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            //  if (mVentasGlobales == null || mVentasGlobales.keySet().isEmpty()) {
            cargarVentasGlobales3();
            //  }
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return mVentasGlobales;
    }

    public TreeMap<Date, List<TVentasVista>> obtenerBalancePorOrigen(Integer userId, long time) throws GenasoftException {
        TreeMap<Date, List<TVentasVista>> result = new TreeMap<Date, List<TVentasVista>>();
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.obtenerMasasOrigen();

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return result;
    }

    public List<TVentasVista> obtenerBalancePorProducto(String producto, String calidad, String origen, String ggn, String variedad, Date f1, Date f2, Integer userId, long time) throws GenasoftException {
        List<TVentasVista> result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.obtenerBalanceMasasProducto(producto, calidad, origen, ggn, variedad, f1, f2);

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return result;
    }

    public List<TVentasVista> obtenerBalancePorProducto2(String tipoBalance, Date f1, Date f2, Integer userId, long time) throws GenasoftException {
        List<TVentasVista> result = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.obtenerBalancePorProducto2(tipoBalance, f1, f2);

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return result;
    }

    public TreeMap<Date, List<TVentasVista>> obtenerBalancePorPais(Integer userId, long time) throws GenasoftException {
        TreeMap<Date, List<TVentasVista>> result = new TreeMap<Date, List<TVentasVista>>();
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.obtenerMasasPaises();

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return result;
    }

    public TreeMap<Date, List<TVentasVista>> obtenerBalancePorCalidad(Integer userId, long time) throws GenasoftException {
        TreeMap<Date, List<TVentasVista>> result = new TreeMap<Date, List<TVentasVista>>();
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.obtenerMasasCalidad();

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return result;
    }

    public TreeMap<Date, List<TVentasVista>> obtenerBalancePorGgn(Integer userId, long time) throws GenasoftException {
        TreeMap<Date, List<TVentasVista>> result = new TreeMap<Date, List<TVentasVista>>();
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.obtenerMasasGGN();

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return result;
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
        if (titulo != null && titulo.getClass().equals(Label.class) && titulo.length > 0) {
            Label lbl = (Label) titulo[0];
            imgGenalBiomasa.addComponent(lbl);
            imgGenalBiomasa.setComponentAlignment(lbl, Alignment.TOP_CENTER);
            imgGenalBiomasa.setExpandRatio(lbl, 1f);
        } else if (titulo != null && titulo.length > 0) {
            com.vaadin.ui.Component lbl = (com.vaadin.ui.Component) titulo[0];
            imgGenalBiomasa.addComponent(lbl);
            imgGenalBiomasa.setComponentAlignment(lbl, Alignment.TOP_CENTER);
            imgGenalBiomasa.setExpandRatio(lbl, 1f);
        }

        ComboBox cbEjercicios = new ComboBox();
        cbEjercicios.setFilteringMode(FilteringMode.CONTAINS);

        cbEjercicios.addItems(importadorSetup.getlEjercicios());
        cbEjercicios.setCaption("Ejercicio: ");
        if (commonSetup.getEjercicio() == null) {
            cbEjercicios.setValue("Todos");
        } else {
            cbEjercicios.setValue("" + commonSetup.getEjercicio());
        }
        cbEjercicios.setNullSelectionAllowed(false);
        cbEjercicios.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {

                VerticalLayout ver = new VerticalLayout();
                ver.setSpacing(true);

                MessageBox.createQuestion().withCaption(appName).withMessage("¿Quieres cambiar de ejercicio? Esta acción cargará los datos del ejercicio seleccionado, y llevará tiempo...").withNoButton().withYesButton(() -> {
                    try {
                        accionCambiarEjercicio((String) cbEjercicios.getValue());
                    } catch (GenasoftException e) {
                        e.printStackTrace();
                    }
                }, ButtonOption.caption("Sí")).open();
            }
        });

        imgGenalBiomasa.addComponent(cbEjercicios);
        imgGenalBiomasa.setComponentAlignment(cbEjercicios, Alignment.TOP_RIGHT);
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

    public TreeMap<Date, List<TComprasVista>> obtenerComprasGlobales(Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            cargarComprasGlobales();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return mComprasGlobales;
    }

    public TreeMap<Date, List<TComprasVista>> obtenerComprasGlobales2(Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {

            cargarComprasGlobales2();

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return mComprasGlobales;
    }

    public TreeMap<Date, List<TComprasVista>> obtenerCompras(Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            return importadorSetup.obtenerComprasFechas();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

    }

    public void reestablecerCompras() throws GenasoftException {
        commonSetup.mCompras.clear();
        commonSetup.mMasasArticulosOrigen.clear();
        commonSetup.mMasasArticulosPais.clear();
        commonSetup.mMasasArticulosCalidad.clear();
        commonSetup.mMasasGgnCalidad.clear();
        commonSetup.mMasasProductos.clear();
        //commonSetup.mComprasVista.clear();
        commonSetup.mComprasId.clear();
        commonSetup.mLoteCompraKgs.clear();
        //commonSetup.mComprasLineas.clear();
        //commonSetup.mLotesCalidad.clear();
        //commonSetup.mComprasKgs.clear();
        //commonSetup.mComprasProveedor.clear();
        //commonSetup.mComprasVentas.clear();
        //commonSetup.mLoteVentasCompras.clear();
        commonSetup.mLotesCompras.clear();
        commonSetup.mLotesComprasVista.clear();
        commonSetup.mProductosComprasDisponibles.clear();

        cargarComprasGlobales();
    }

    public void reestablecerVentas() throws GenasoftException {
        //commonSetup.mVentas.clear();
        //commonSetup.mVentasVista.clear();
        commonSetup.mVentasId.clear();
        //commonSetup.mVentasCompras.clear();
        //commonSetup.mLoteCompraVentas.clear();
        commonSetup.mLoteVentaProd.clear();
        commonSetup.mIdVentasErroneas.clear();
        commonSetup.mIdLineasVentasErroneas.clear();
        commonSetup.mIdLineasVentas.clear();
        cargarVentasGlobales();
    }

    public void cargarComprasGlobales() throws GenasoftException {
        importadorSetup.inicializarListasCompra();
        mComprasGlobales = importadorSetup.obtenerComprasFechas();
    }

    public void cargarComprasGlobales2() throws GenasoftException {
        importadorSetup.inicializarListasCompra();
        mComprasGlobales = importadorSetup.obtenerComprasFechas2();
    }

    public TreeMap<Date, List<TComprasVista>> forzarCargaCompras(Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            if (commonSetup.mCompras == null) {
                commonSetup.mCompras = new HashMap<String, TCompras>();
                commonSetup.mMasasArticulosOrigen = new HashMap<String, TVentasVista>();
                commonSetup.mMasasArticulosPais = new HashMap<String, TVentasVista>();
                commonSetup.mMasasArticulosCalidad = new HashMap<String, TVentasVista>();
                commonSetup.mMasasGgnCalidad = new HashMap<String, TVentasVista>();
                commonSetup.mMasasProductos = new HashMap<String, TVentasVista>();
                commonSetup.mComprasId = new HashMap<String, TComprasVista>();
                commonSetup.mVentasId = new HashMap<String, TVentasVista>();
                commonSetup.mLotesCompras = new HashMap<String, List<TCompras>>();
                commonSetup.mLoteCompraVentas = new HashMap<String, List<TVentasVista>>();
                commonSetup.mLoteVentaProd = new HashMap<String, List<TVentas>>();
                commonSetup.mLotesComprasVista = new HashMap<String, List<TComprasVista>>();
                commonSetup.mComprasDisponibles = new HashMap<String, TreeMap<Date, List<TCompras>>>();
                commonSetup.mLineasVentaDisponibles = new HashMap<String, TreeMap<Date, List<TLineasVentas>>>();
                commonSetup.mLoteCompraKgs = new HashMap<String, Double>();
                commonSetup.mProductosComprasDisponibles = new HashMap<String, Map<Integer, TCompras>>();
                commonSetup.mTodasCompras = new HashMap<Integer, TCompras>();
                //commonSetup.mProductosComprasDisponibles = new HashMap<String, List<Producto>>();
                commonSetup.mIdVentasErroneas = new HashMap<Integer, TVentas>();
                commonSetup.mIdLineasVentasErroneas = new HashMap<Double, List<TLineasVentas>>();
                commonSetup.mIdLineasVentas = new HashMap<Double, List<TLineasVentas>>();
            } else {
                commonSetup.mCompras.clear();
                commonSetup.mMasasArticulosOrigen.clear();
                commonSetup.mMasasArticulosPais.clear();
                commonSetup.mMasasArticulosCalidad.clear();
                commonSetup.mMasasGgnCalidad.clear();
                commonSetup.mMasasProductos.clear();
                commonSetup.mComprasId.clear();
                commonSetup.mLotesCompras.clear();
                commonSetup.mLotesComprasVista.clear();
                commonSetup.mComprasDisponibles.clear();
                commonSetup.mLineasVentaDisponibles.clear();
                commonSetup.mLoteCompraKgs.clear();
                commonSetup.mProductosComprasDisponibles.clear();
                commonSetup.mTodasCompras.clear();
                mComprasGlobales.clear();
                if (mComprasGlobalesPorAlbaranLote == null) {
                    mComprasGlobalesPorAlbaranLote = new TreeMap<String, List<TComprasVista>>();
                } else {
                    mComprasGlobalesPorAlbaranLote.clear();
                }
                mVentasGlobales.clear();
                if (mVentasGlobalesPorAlbaranLote == null) {
                    mVentasGlobalesPorAlbaranLote = new TreeMap<String, List<TVentasVista>>();
                } else {
                    mVentasGlobalesPorAlbaranLote.clear();
                }
            }

            importadorSetup.inicializarListasCompra();
            mComprasGlobales = importadorSetup.obtenerComprasFechas();

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return mComprasGlobales;
    }

    public TreeMap<Date, List<TVentasVista>> forzarCargaVentas(Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            if (commonSetup.mCompras == null) {
                commonSetup.mCompras = new HashMap<String, TCompras>();
                commonSetup.mMasasArticulosOrigen = new HashMap<String, TVentasVista>();
                commonSetup.mMasasArticulosPais = new HashMap<String, TVentasVista>();
                commonSetup.mMasasArticulosCalidad = new HashMap<String, TVentasVista>();
                commonSetup.mMasasGgnCalidad = new HashMap<String, TVentasVista>();
                commonSetup.mMasasProductos = new HashMap<String, TVentasVista>();
                commonSetup.mComprasId = new HashMap<String, TComprasVista>();
                commonSetup.mVentasId = new HashMap<String, TVentasVista>();
                commonSetup.mLotesCompras = new HashMap<String, List<TCompras>>();
                commonSetup.mLoteCompraVentas = new HashMap<String, List<TVentasVista>>();
                commonSetup.mLoteVentaProd = new HashMap<String, List<TVentas>>();
                commonSetup.mLotesComprasVista = new HashMap<String, List<TComprasVista>>();
                commonSetup.mComprasDisponibles = new HashMap<String, TreeMap<Date, List<TCompras>>>();
                commonSetup.mLineasVentaDisponibles = new HashMap<String, TreeMap<Date, List<TLineasVentas>>>();
                commonSetup.mLoteCompraKgs = new HashMap<String, Double>();
                commonSetup.mProductosComprasDisponibles = new HashMap<String, Map<Integer, TCompras>>();
                commonSetup.mTodasCompras = new HashMap<Integer, TCompras>();
                //commonSetup.mProductosComprasDisponibles = new HashMap<String, List<Producto>>();
                commonSetup.mIdVentasErroneas = new HashMap<Integer, TVentas>();
                commonSetup.mIdLineasVentasErroneas = new HashMap<Double, List<TLineasVentas>>();
                commonSetup.mIdLineasVentas = new HashMap<Double, List<TLineasVentas>>();
                mComprasGlobales.clear();
                if (mComprasGlobalesPorAlbaranLote == null) {
                    mComprasGlobalesPorAlbaranLote = new TreeMap<String, List<TComprasVista>>();
                } else {
                    mComprasGlobalesPorAlbaranLote.clear();
                }
                mVentasGlobales.clear();
                if (mVentasGlobalesPorAlbaranLote == null) {
                    mVentasGlobalesPorAlbaranLote = new TreeMap<String, List<TVentasVista>>();
                } else {
                    mVentasGlobalesPorAlbaranLote.clear();
                }
            } else {
                commonSetup.mVentasId.clear();
                //commonSetup.mLoteCompraVentas.clear();
                commonSetup.mLoteVentaProd.clear();
                commonSetup.mIdVentasErroneas.clear();
                commonSetup.mIdLineasVentasErroneas.clear();
                commonSetup.mIdLineasVentas.clear();
                commonSetup.mLineasVentaDisponibles.clear();

                mVentasGlobales.clear();
                if (mVentasGlobalesPorAlbaranLote == null) {
                    mVentasGlobalesPorAlbaranLote = new TreeMap<String, List<TVentasVista>>();
                } else {
                    mVentasGlobalesPorAlbaranLote.clear();
                }
            }
            importadorSetup.inicializarListasVenta();
            mVentasGlobales = importadorSetup.obtenerVentasFechas();

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return mVentasGlobales;
    }

    public Double obtenerProgresoCargaFichero(Integer userId, long time) throws GenasoftException {

        Double result = Double.valueOf(0);
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = importadorSetup.getProgreso();
            if (result == null) {
                result = Double.valueOf(0);
            }

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return result;
    }

    public void cargarVentasGlobales() throws GenasoftException {

        //importadorSetup.inicializarListasVenta();
        mVentasGlobales = importadorSetup.obtenerVentasFechas();
    }

    public void cargarVentasGlobales3() throws GenasoftException {

        //importadorSetup.inicializarListasVenta();
        mVentasGlobales = importadorSetup.obtenerVentasFechas3();
    }

    public void cargarVentasGlobales2() throws GenasoftException {

        mVentasGlobales = importadorSetup.obtenerVentasFechas();
    }

    public TreeMap<Date, List<TVentasVista>> obtenerVentasErroneas() {

        return importadorSetup.obtenerVentasErroneas();
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public void guardarComprasListadoTrazabilidades(List<Integer> lIdsCompras, Integer user, long time) throws GenasoftException {
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            if (mTrazabilidadesComprasEmpleado == null) {
                mTrazabilidadesComprasEmpleado = new HashMap<Integer, List<Integer>>();
            }
            mTrazabilidadesComprasEmpleado.put(user, lIdsCompras);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public void guardarComprasListadoComprasVentas(List<Integer> lIdsCompras, Integer user, long time) throws GenasoftException {
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            if (mComprasComprasVentasEmpleado == null) {
                mComprasComprasVentasEmpleado = new HashMap<Integer, List<Integer>>();
            }
            mComprasComprasVentasEmpleado.put(user, lIdsCompras);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public void guardarTotalesComprasListadoTrazabilidades(List<Double> lTotales, Integer user, long time) throws GenasoftException {
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            if (mTotalesTrazabilidadesComprasEmpleado == null) {
                mTotalesTrazabilidadesComprasEmpleado = new HashMap<Integer, List<Double>>();
            }
            mTotalesTrazabilidadesComprasEmpleado.put(user, lTotales);

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public void guardarTotalesComprasComprasVentas(List<Double> lTotales, Integer user, long time) throws GenasoftException {
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            if (mTotalesComprasComprasVentasEmpleado == null) {
                mTotalesComprasComprasVentasEmpleado = new HashMap<Integer, List<Double>>();
            }
            mTotalesComprasComprasVentasEmpleado.put(user, lTotales);

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
    }

    public List<Integer> obtenerListadoComprasTrazabilidadesIdEmpleado(Integer userId) throws GenasoftException {

        if (mTrazabilidadesComprasEmpleado.get(userId) == null) {
            return Utils.generarListaGenerica();
        } else {
            return mTrazabilidadesComprasEmpleado.get(userId);
        }

    }

    public List<Integer> obtenerListadoComprasComprasVentasIdEmpleado(Integer userId) throws GenasoftException {

        if (mComprasComprasVentasEmpleado.get(userId) == null) {
            return Utils.generarListaGenerica();
        } else {
            return mComprasComprasVentasEmpleado.get(userId);
        }

    }

    public List<Double> obtenerTotalesListadoComprasTrazabilidadesIdEmpleado(Integer userId) throws GenasoftException {

        if (mTotalesTrazabilidadesComprasEmpleado.get(userId) == null) {
            return Utils.generarListaGenerica();
        } else {
            return mTotalesTrazabilidadesComprasEmpleado.get(userId);
        }

    }

    public List<Double> obtenerTotalesListadoComprasComprasVentasIdEmpleado(Integer userId) throws GenasoftException {

        if (mTotalesComprasComprasVentasEmpleado.get(userId) == null) {
            return Utils.generarListaGenerica();
        } else {
            return mTotalesComprasComprasVentasEmpleado.get(userId);
        }

    }

    public List<TComprasVista> obtenerComprasAlbaranesLotesPartidas(List<String> lAlbaranes, List<String> lLotes, List<String> lPartidas, Integer user, long time) throws GenasoftException {
        List<TComprasVista> lResult = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            lResult = importadorSetup.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        return lResult;
    }

    public List<TComprasFictVista> obtenerComprasFictAlbaranesLotesPartidas(List<String> lAlbaranes, List<String> lLotes, List<String> lPartidas, Integer user, long time) throws GenasoftException {
        List<TComprasFictVista> lResult = null;
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            lResult = importadorSetup.obtenerComprasFictAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }

        return lResult;
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public void guardarBalanceMasas(List<TVentasVista> lVentas, List<String> lFiltros, Integer user, long time) throws GenasoftException {
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            if (mBalanceMasasExportar == null) {
                mBalanceMasasExportar = new HashMap<Integer, List<TVentasVista>>();
            }

            lFiltrosBalanceMasas = Utils.generarListaGenerica();

            mBalanceMasasExportar.put(user, lVentas);
            lFiltrosBalanceMasas.addAll(lFiltros);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public void guardarTotalesBalanceMasas(List<TVentasVista> lVentas, Integer user, long time) throws GenasoftException {
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            if (mBalanceTotalesMasasExportar == null) {
                mBalanceTotalesMasasExportar = new HashMap<Integer, List<TVentasVista>>();
            }

            mBalanceTotalesMasasExportar.put(user, lVentas);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public void guardarVentasListadoTrazabilidades(List<Integer> lIdsVentas, Integer user, long time) throws GenasoftException {
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            if (mTrazabilidadesVentasEmpleado == null) {
                mTrazabilidadesVentasEmpleado = new HashMap<Integer, List<Integer>>();
            }
            mTrazabilidadesVentasEmpleado.put(user, lIdsVentas);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public void guardarVentasListadoComprasVentas(List<Integer> lIdsVentas, Integer user, long time) throws GenasoftException {
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            if (mComprasVentasVentasEmpleado == null) {
                mComprasVentasVentasEmpleado = new HashMap<Integer, List<Integer>>();
            }
            mComprasVentasVentasEmpleado.put(user, lIdsVentas);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public void guardarTotalesVentasListadoTrazabilidades(List<Double> lTotales, Integer user, long time) throws GenasoftException {
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            if (mTotalesTrazabilidadesVentasEmpleado == null) {
                mTotalesTrazabilidadesVentasEmpleado = new HashMap<Integer, List<Double>>();
            }
            mTotalesTrazabilidadesVentasEmpleado.put(user, lTotales);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public void guardarTotalesVentasListadoComprasVentas(List<Double> lTotales, Integer user, long time) throws GenasoftException {
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            if (mTotalesVentasComprasVentasEmpleado == null) {
                mTotalesVentasComprasVentasEmpleado = new HashMap<Integer, List<Double>>();
            }
            mTotalesVentasComprasVentasEmpleado.put(user, lTotales);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
    }

    public void guardarFiltrosTrazabilidades(Map<Integer, List<String>> mFiltros, Integer user, long time) throws GenasoftException {
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(user, t, estadoAplicacion)) {
            if (mFiltrosTrazabilidades == null) {
                mFiltrosTrazabilidades = new HashMap<Integer, Map<Integer, List<String>>>();
            }
            mFiltrosTrazabilidades.put(user, mFiltros);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
    }

    public Map<Integer, List<String>> obtenerFiltroTrazabilidades(Integer userId) throws GenasoftException {

        if (mFiltrosTrazabilidades.get(userId) == null) {
            return new HashMap<Integer, List<String>>();
        } else {
            return mFiltrosTrazabilidades.get(userId);
        }
    }

    public List<Integer> obtenerListadoVentasTrazabilidadesIdEmpleado(Integer userId) throws GenasoftException {

        if (mTrazabilidadesVentasEmpleado.get(userId) == null) {
            return Utils.generarListaGenerica();
        } else {
            return mTrazabilidadesVentasEmpleado.get(userId);
        }
    }

    public List<Integer> obtenerListadoVentasComprasVentasIdEmpleado(Integer userId) throws GenasoftException {

        if (mComprasVentasVentasEmpleado.get(userId) == null) {
            return Utils.generarListaGenerica();
        } else {
            return mComprasVentasVentasEmpleado.get(userId);
        }
    }

    public List<TVentasVista> obtenerBalanceMasasExportar(Integer userId) throws GenasoftException {

        if (mBalanceMasasExportar.get(userId) == null) {
            return Utils.generarListaGenerica();
        } else {
            return mBalanceMasasExportar.get(userId);
        }
    }

    public List<TVentasVista> obtenerTotalesBalanceMasasExportar(Integer userId) throws GenasoftException {

        if (mBalanceTotalesMasasExportar.get(userId) == null) {
            return Utils.generarListaGenerica();
        } else {
            return mBalanceTotalesMasasExportar.get(userId);
        }
    }

    public List<String> obtenerFiltrosBalanceMasasExportar(Integer userId) throws GenasoftException {

        if (lFiltrosBalanceMasas.get(userId) == null) {
            return Utils.generarListaGenerica();
        } else {
            return lFiltrosBalanceMasas;
        }
    }

    public List<Double> obtenerTotalesListadoVentasTrazabilidadesIdEmpleado(Integer userId) throws GenasoftException {

        if (mTotalesTrazabilidadesVentasEmpleado.get(userId) == null) {
            return Utils.generarListaGenerica();
        } else {
            return mTotalesTrazabilidadesVentasEmpleado.get(userId);
        }

    }

    public List<Double> obtenerTotalesListadoVentasComprasVentasIdEmpleado(Integer userId) throws GenasoftException {

        if (mTotalesVentasComprasVentasEmpleado.get(userId) == null) {
            return Utils.generarListaGenerica();
        } else {
            return mTotalesVentasComprasVentasEmpleado.get(userId);
        }

    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public String guardarCamposTablaEmpleado(List<TColumnasTablasEmpleado> lCampos, Integer idEmpleado, String nombrePantalla, Integer idTabla, Integer user, long time) throws GenasoftException {
        String result = "";

        Timestamp t = new Timestamp(time);
        if (user != null && commonSetup.conexionValida(user, t, estadoAplicacion)) {
            result = empleadosSetup.guardarCamposTablaEmpleado(lCampos, idEmpleado, nombrePantalla, idTabla);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los roles encontrados.
        return result;
    }

    /**
     * Método que nos guarda en el sistema una nueva línea de compra pasada por parámetro.
     * @param record La nueva línea de compra a guardar en el sistema.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public String guardarLineaCompra(TComprasVista record, Integer user, long time) throws GenasoftException {
        String result = "";

        Timestamp t = new Timestamp(time);
        if (user != null && commonSetup.conexionValida(user, t, estadoAplicacion)) {
            result = importadorSetup.guardarCompraVista(record);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los roles encontrados.
        return result;
    }

    /**
     * Método que nos guarda en el sistema una nueva línea de venta pasada por parámetro.
     * @param record La nueva línea de venta a guardar en el sistema.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public String guardarLineaVenta(TVentasVista record, Boolean abrirLinea, Integer user, long time) throws GenasoftException {
        String result = "";

        Timestamp t = new Timestamp(time);
        if (user != null && commonSetup.conexionValida(user, t, estadoAplicacion)) {
            result = importadorSetup.guardarVentaVista(record, abrirLinea);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los roles encontrados.
        return result;
    }

    /**
     * Método que nos guarda en el sistema una nueva línea de venta pasada por parámetro.
     * @param record La nueva línea de venta a guardar en el sistema.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public String guardarLineaTrazabilidad(TLineasVentasVista record, Integer user, long time) throws GenasoftException {
        String result = "";

        Timestamp t = new Timestamp(time);
        if (user != null && commonSetup.conexionValida(user, t, estadoAplicacion)) {
            result = importadorSetup.guardarLineaTrazabilidadVentaVista(record, user);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los roles encontrados.
        return result;
    }

    /**
     * Método que nos guarda en el sistema una nueva línea de venta pasada por parámetro.
     * @param record La nueva línea de venta a guardar en el sistema.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El resultado de la operación.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public String guardarLineaVentaFict(TVentasFictVista record, Boolean abrirLinea, Integer user, long time) throws GenasoftException {
        String result = "";

        Timestamp t = new Timestamp(time);
        if (user != null && commonSetup.conexionValida(user, t, estadoAplicacion)) {
            result = importadorSetup.guardarVentaFictVista(record, abrirLinea);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los roles encontrados.
        return result;
    }

    /**
     * Método que nos retorna los roles activos en el sistema excluyendo el rol Master.
     * @param user El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los roles encontrados.
     * @throws BrosException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TColumnasTablasEmpleado> obtenerCamposPantallaTablaEmpleado(Integer idEmpleado, String nombrePantalla, Integer idTabla, Integer user, long time) throws GenasoftException {
        List<TColumnasTablasEmpleado> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (user != null && commonSetup.conexionValida(user, t, estadoAplicacion)) {
            lResult = empleadosSetup.obtenerCamposPantallaTablaEmpleado(idEmpleado, nombrePantalla, idTabla);
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
     * Método que nos retorna la lista de familias de compras identificadas.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Las familias de compras encontradas.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerFamiliasCompras(Integer userId, long time) throws GenasoftException {
        List<String> lFamilias = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lFamilias = commonSetup.getlFamiliasComprasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos las familias encontradas.
        return lFamilias;
    }

    /**
     * Método que nos retorna la lista de familias de ventas identificadas.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Las familias de ventas encontradas.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerFamiliasVentas(Integer userId, long time) throws GenasoftException {
        List<String> lFamilias = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lFamilias = commonSetup.getlFamiliasVentasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos las familias encontradas.
        return lFamilias;
    }

    /**
     * Método que nos retorna la lista de familias de ventas identificadas.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Las familias de ventas encontradas.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerVariedades(Integer userId, long time) throws GenasoftException {
        List<String> lFamilias = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lFamilias = commonSetup.getlVariedades();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos las familias encontradas.
        return lFamilias;
    }

    /**
     * Método que nos retorna la lista de familias de ventas identificadas.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Las familias de ventas encontradas.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TVariedad> obtenerVariedadesProducto(Integer idProducto, Integer userId, long time) throws GenasoftException {
        List<TVariedad> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = productosSetup.obtenerVariedadesProducto(idProducto);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos las familias encontradas.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de familias de ventas identificadas.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Las familias de ventas encontradas.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TProductoCalibre> obtenerNumerosCalibreProducto(Integer idProducto, Integer userId, long time) throws GenasoftException {
        List<TProductoCalibre> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = productosSetup.obtenerNumerosCalibreProducto(idProducto);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos las familias encontradas.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de familias de ventas identificadas.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Las familias de ventas encontradas.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TDiametrosProducto> obtenerDiametrosProducto(Integer idProducto, Integer userId, long time) throws GenasoftException {
        List<TDiametrosProducto> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = productosSetup.obtenerDiametrosProducto(idProducto);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos las familias encontradas.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de paises de compras identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los paises de compras encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerPaisesCompras(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlPaisesComprasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de paises de ventas identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los paises de ventas encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerPaisesVentas(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlPaisesVentasFin();
            if (lResult == null) {
                lResult = Utils.generarListaGenerica();
            }
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de artículos de compras identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los artículos de compras encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerArticulosCompras(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlArticulosComprasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de artículos de ventas identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los artículos de ventas encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerArticulosVentas(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlArticulosVentasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de clientes de ventas identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los clientes de ventas encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerClientes(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlClientesVentasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de proveedores de compras identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los proveedores de compras encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerProveedoresCompras(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlProveedoresComprasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de proveedores de ventas identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los proveedores de ventas encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerProveedoresVentas(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlProveedoresVentasComprasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de albaranes de compras identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los albaranes de compras encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerAlbaranesCompras(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlNumerosAlbaranComprasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de albaranes de compras identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los albaranes de compras encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerAlbaranesComprasFict(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = importadorSetup.obtenerNumerosAlbaranComprasFict();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de albaranes de ventas identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los albaranes de ventas encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerAlbaranesVentas(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlNumerosAlbaranVentasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de lotes de compras identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los lotes de compras encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerLotesCompras(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlLotesComprasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de lotes de ventas identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los lotes de ventas encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerLotesVentas(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlLotesVentasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de lotes de ventas identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los lotes de ventas encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerCodsPallets(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlCodsPallets();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de lotes de ventas identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los lotes de ventas encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerLotesPallets(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlLotesPallets();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de partidas de compras identificadas.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Las partidas de compras encontradas.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerPartidasCompras(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlPartidasComprasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de partidas de compras identificadas.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Las partidas de compras encontradas.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerLotesComprasFict(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = importadorSetup.obtenerLotesComprasFict();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de partidas de compras identificadas.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Las partidas de compras encontradas.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerPartidasComprasFict(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = importadorSetup.obtenerPartidasComprasFict();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de pedidos de ventas identificados.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los pedidos de venta encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerPedidosVentas(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlPedidosVentasFin();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de nombres descriptivos en importaciones de compras.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los nombres de importaciones de compras encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerNombresCompras(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlNombresImportacionesCompras();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna la lista de nombres descriptivos en importaciones de ventas.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los nombres de importaciones de venta encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<String> obtenerNombresVentas(Integer userId, long time) throws GenasoftException {
        List<String> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getlNombresImportacionesVentas();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna un diccionario con las ventas que son erróneas, en las que algo no cuadra.
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los nombres de importaciones de venta encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public Map<Integer, TVentas> obtenerVentasErroneas(Integer userId, long time) throws GenasoftException {
        Map<Integer, TVentas> lResult = new HashMap<Integer, TVentas>();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = commonSetup.getmIdVentasErroneas();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    /**
     * Método que nos corrige las ventas que están identificadas como erróneas.
     * @param userId El Usuario que ha iniciado sesión.
     * @param time la fecha en que se realiza la conexión
     * @return Nos retorna el
     * @throws GenasoftException
     */
    public Map<Integer, TVentas> corregirVentasErroneas(Integer maxLotes, Integer maxLotesGuacamole, Boolean indBio, Boolean soloBio, Boolean todos, Integer userId, long time) throws GenasoftException {
        Map<Integer, TVentas> lResult = new HashMap<Integer, TVentas>();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = importadorSetup.corregirVentasErroneas3(maxLotes, maxLotesGuacamole, indBio, soloBio, todos, false);
            commonSetup.mIdVentasErroneas.clear();
            commonSetup.mIdVentasErroneas = new HashMap<Integer, TVentas>(lResult);

        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los resultados encontrados.
        return lResult;
    }

    public TreeMap<String, List<TComprasVista>> obtenerComprasAlbaranCompra(List<String> lValues, Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            mComprasGlobalesPorAlbaranLote = importadorSetup.obtenerComprasAlbaranCompra(commonSetup.mLotesComprasVista, lValues);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los datos encontrados.
        return mComprasGlobalesPorAlbaranLote;
    }

    public TreeMap<String, List<TVentasVista>> obtenerVentasAlbaranCompra(List<String> lValues, Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            mVentasGlobalesPorAlbaranLote = importadorSetup.obtenerVentasIdLineaPedidoCompra(commonSetup.mLoteCompraVentas, lValues);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return mVentasGlobalesPorAlbaranLote;
    }

    public TreeMap<String, List<TVentasVista>> obtenerVentasAlbaranCompra2(List<String> lValues, Integer userId, long time) throws GenasoftException {

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            mVentasGlobalesPorAlbaranLote = importadorSetup.obtenerVentasIdLineaPedidoCompra2(commonSetup.mLoteCompraVentas, lValues);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return mVentasGlobalesPorAlbaranLote;
    }

    public List<TVentasFictVista> obtenerVentasFictAlbaranCompra(List<Integer> lValues, Integer userId, long time) throws GenasoftException {

        List<TVentasFictVista> lResult = Utils.generarListaGenerica();
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = importadorSetup.obtenerVentasFictIdLineaPedidoCompra(lValues);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return lResult;
    }

    public List<TLineasVentasVista> obtenerLineasVentaVista(Double idVenta, Integer userId, long time) throws GenasoftException {

        List<TLineasVentasVista> lResult = Utils.generarListaGenerica();
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = importadorSetup.obtenerLineasVentaVista(idVenta);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return lResult;
    }

    public List<TLineasVentasVista> obtenerLineasVentasLotesVista(List<String> lLotes, Integer userId, long time) throws GenasoftException {

        List<TLineasVentasVista> lResult = Utils.generarListaGenerica();
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = importadorSetup.obtenerLineasVentaLotesVista(lLotes);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los países encontrados.
        return lResult;
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
                    cambio.setDescripcion("Se desactiva el empleado.");
                    cambio.setUsuRegistro(userId);
                    cambio.setFecha(Utils.generarFecha());
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
     * Método que se encarga de eliminar el control de producto terminado en el sistema, creando el registro en las tablas *_his
     * @param idControlPt El ID del control de producto terminado a eliminar.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String eliminarControlPt(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            TControlProductoTerminado empl = controlPtSetup.obtenerControlPtPorId(idControlPt);
            if (empl != null) {
                controlPtSetup.eliminarControlProductoTerminado(idControlPt);

                // Creamos el registro de cambios sobre el control de producto terminado.
                TRegistrosCambiosControlProductoTerminado cambio = new TRegistrosCambiosControlProductoTerminado();
                cambio.setIdControlPt(empl.getId());
                cambio.setDescripcion("Se elimina el control de PT.");
                cambio.setUsuRegistro(userId);
                cambio.setFecha(Utils.generarFecha());
                controlPtSetup.crearRegistroCambioControlPt(cambio);
            } else {
                result = Constants.CONTROL_PT_NO_EXISTE;
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
     * Método que se encarga de eliminar el control de producto terminado en el sistema, creando el registro en las tablas *_his
     * @param idLinea El ID del control de producto terminado a eliminar.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String eliminarLineaControlPt(Integer idLinea, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = controlPtSetup.eliminarLineaControlPt(idLinea);
        }
        // Retornamos el resultado de la modificación del empleado.
        return result;
    }

    /**
     * Método que se encarga de modificar el producto en el sistema desactivándolo.
     * @param idProducto El ID del producto a desactivar.     
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return El código del resultado de la operación.
     */
    public String desactivarProducto(Integer idProducto, Integer userId, long time) throws GenasoftException {
        String result = "";
        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            TProductos prod = productosSetup.obtenerProductoPorId(idProducto);
            if (prod != null) {
                if (prod.getEstado().equals(ProductoEnum.ACTIVO.getValue())) {
                    prod.setEstado(ProductoEnum.DESACTIVADO.getValue());
                    prod.setUsuModifica(userId);
                    prod.setFechaModifica(Utils.generarFecha());
                    result = productosSetup.modificarProducto(prod);
                    /**
                    // Creamos el registro de cambios sobre el empleado.
                    TRegistrosCambiosEmpleados cambio = new TRegistrosCambiosEmpleados();
                    cambio.setIdEmpleado(empl.getId());
                    cambio.setDescripcion("Se desactiva el empleado.");
                    cambio.setUsuRegistro(userId);
                    cambio.setFecha(Utils.generarFecha());
                    empleadosSetup.crearRegistroCambioEmpleado(cambio);
                    */
                } else {
                    result = Constants.PRODUCTO_DESACTIVADO;
                }
            } else {
                result = Constants.PRODUCTO_NO_EXISTE;
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

    public void inicializarTodo(Integer year) throws GenasoftException {
        if (commonSetup.mCompras == null || year != null) {
            Utils.obtenerListaNumeros();
            commonSetup.mCompras = new HashMap<String, TCompras>();
            commonSetup.mMasasArticulosOrigen = new HashMap<String, TVentasVista>();
            commonSetup.mMasasArticulosPais = new HashMap<String, TVentasVista>();
            commonSetup.mMasasArticulosCalidad = new HashMap<String, TVentasVista>();
            commonSetup.mMasasGgnCalidad = new HashMap<String, TVentasVista>();
            commonSetup.mMasasProductos = new HashMap<String, TVentasVista>();
            //commonSetup.mComprasVista = new HashMap<String, TComprasVista>();
            commonSetup.mComprasId = new HashMap<String, TComprasVista>();
            //commonSetup.mVentas = new HashMap<String, TVentas>();
            //commonSetup.mVentasVista = new HashMap<String, TVentasVista>();
            commonSetup.mVentasId = new HashMap<String, TVentasVista>();
            //commonSetup.mComprasLineas = new HashMap<String, List<TCompras>>();
            //commonSetup.mLotesCalidad = new HashMap<String, List<String>>();
            //commonSetup.mComprasKgs = new HashMap<String, Double>();
            //commonSetup.mComprasProveedor = new HashMap<String, List<String>>();
            //commonSetup.mComprasVentas = new HashMap<String, List<TVentas>>();
            //commonSetup.mVentasCompras = new HashMap<String, List<TCompras>>();
            //commonSetup.mLoteVentasCompras = new HashMap<String, List<TCompras>>();
            commonSetup.mLotesCompras = new HashMap<String, List<TCompras>>();
            commonSetup.mLoteCompraVentas = new HashMap<String, List<TVentasVista>>();
            commonSetup.mLoteVentaProd = new HashMap<String, List<TVentas>>();
            commonSetup.mLotesComprasVista = new HashMap<String, List<TComprasVista>>();
            commonSetup.mLoteCompraKgs = new HashMap<String, Double>();
            commonSetup.mProductosComprasDisponibles = new HashMap<String, Map<Integer, TCompras>>();
            commonSetup.mTodasCompras = new HashMap<Integer, TCompras>();
            commonSetup.mLineasVentaDisponibles = new HashMap<String, TreeMap<Date, List<TLineasVentas>>>();
            //commonSetup.mProductosComprasDisponibles = new HashMap<String, List<Producto>>();
            commonSetup.mIdVentasErroneas = new HashMap<Integer, TVentas>();
            commonSetup.mIdLineasVentasErroneas = new HashMap<Double, List<TLineasVentas>>();
            commonSetup.mIdLineasVentas = new HashMap<Double, List<TLineasVentas>>();
            importadorSetup.inicializarListasCompra();
            importadorSetup.inicializarListasVenta();
            cargarComprasGlobales();
            cargarVentasGlobales();
        } else {
            commonSetup.mCompras.clear();
            commonSetup.mMasasArticulosOrigen.clear();
            commonSetup.mMasasArticulosPais.clear();
            commonSetup.mMasasArticulosCalidad.clear();
            commonSetup.mMasasGgnCalidad.clear();
            commonSetup.mMasasProductos.clear();
            //commonSetup.mComprasVista = new HashMap<String, TComprasVista>();
            commonSetup.mComprasId.clear();
            //commonSetup.mVentas = new HashMap<String, TVentas>();
            //commonSetup.mVentasVista = new HashMap<String, TVentasVista>();
            commonSetup.mVentasId.clear();
            //commonSetup.mComprasLineas = new HashMap<String, List<TCompras>>();
            //commonSetup.mLotesCalidad = new HashMap<String, List<String>>();
            //commonSetup.mComprasKgs = new HashMap<String, Double>();
            //commonSetup.mComprasProveedor = new HashMap<String, List<String>>();
            //commonSetup.mComprasVentas = new HashMap<String, List<TVentas>>();
            //commonSetup.mVentasCompras = new HashMap<String, List<TCompras>>();
            //commonSetup.mLoteVentasCompras = new HashMap<String, List<TCompras>>();
            commonSetup.mLotesCompras.clear();
            //commonSetup.mLoteCompraVentas.clear();
            commonSetup.mLoteVentaProd.clear();
            commonSetup.mLotesComprasVista.clear();
            commonSetup.mLoteCompraKgs.clear();
            commonSetup.mProductosComprasDisponibles.clear();
            commonSetup.mTodasCompras.clear();;
            commonSetup.mIdVentasErroneas.clear();
            commonSetup.mIdLineasVentasErroneas.clear();
            commonSetup.mLineasVentaDisponibles.clear();
            commonSetup.mIdLineasVentas.clear();
            mComprasGlobales.clear();
            if (mComprasGlobalesPorAlbaranLote == null) {
                mComprasGlobalesPorAlbaranLote = new TreeMap<String, List<TComprasVista>>();
            } else {
                mComprasGlobalesPorAlbaranLote.clear();
            }
            mVentasGlobales.clear();
            if (mVentasGlobalesPorAlbaranLote == null) {
                mVentasGlobalesPorAlbaranLote = new TreeMap<String, List<TVentasVista>>();
            } else {
                mVentasGlobalesPorAlbaranLote.clear();
            }
        }
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoVista> obtenerControlesPtVista(Date f1, Date f2, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerControlPtPorFechasVista(f1, f2);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TLineaControlProductoTerminadoVista> obtenerLineasControlProductoTerminadoVista(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TLineaControlProductoTerminadoVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerLineasControlProductoTerminadoVista(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TLineaControlProductoTerminado> obtenerLineasControlProductoTerminado(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TLineaControlProductoTerminado> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerLineasControlProductoTerminado(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoVista> obtenerControlesPtCreadosVista(Integer user, Date f1, Date f2, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerControlPtCreadosVista(user, f1, f2);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesBrixVista> obtenerPesajesBrixIdControlPtVista(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesBrixVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesBrixIdControlPtVista(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesBrixVista> obtenerPesajesBrixIdLineaVista(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesBrixVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesBrixIdLineaVista(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesBrix> obtenerPesajesBrixIdLinea(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesBrix> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesBrixIdLinea(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesBrix> obtenerPesajesBrixIdControlPt(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesBrix> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesBrixIdControlPt(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesCajaVista> obtenerPesajesCajaIdControlPtVista(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesCajaVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesCajaIdControlPtVista(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesCajaVista> obtenerPesajesCajaIdLineaVista(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesCajaVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesCajaIdLineaVista(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesCaja> obtenerPesajesCajaIdLinea(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesCaja> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesCajaIdLinea(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesCaja> obtenerPesajesCajaIdControlPt(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesCaja> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesCajaIdControlPt(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesCalibreVista> obtenerPesajesCalibreIdControlPtVista(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesCalibreVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesCalibreIdControlPtVista(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesCalibreVista> obtenerPesajesCalibreIdLineaControlPtVista(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesCalibreVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesCalibreIdLineaVista(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesCalibre> obtenerPesajesCalibreIdLineaControlPt(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesCalibre> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesCalibreIdLinea(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesCalibre> obtenerPesajesCalibreIdControlPt(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesCalibre> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesCalibreIdControlPt(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesConfeccionVista> obtenerPesajesConfeccionIdControlPtVista(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesConfeccionVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesConfeccionIdControlPtVista(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesConfeccionVista> obtenerPesajesConfeccionIdLineaControlPtVista(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesConfeccionVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesConfeccionIdLineaVista(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesConfeccion> obtenerPesajesConfeccionIdLineaControlPt(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesConfeccion> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesConfeccionIdLinea(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesConfeccion> obtenerPesajesConfeccionIdControlPt(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesConfeccion> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesConfeccionIdControlPt(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesPenetromiaVista> obtenerPesajesPenetromiaIdControlPtVista(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesPenetromiaVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesPenetromiaIdControlPtVista(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesPenetromiaVista> obtenerPesajesPenetromiaIdLineaVista(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesPenetromiaVista> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesPenetromiaIdLineaVista(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesPenetromia> obtenerPesajesPenetromiaIdLinea(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesPenetromia> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesPenetromiaIdLinea(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public List<TControlProductoTerminadoPesajesPenetromia> obtenerPesajesPenetromiaIdControlPt(Integer idControlPt, Integer userId, long time) throws GenasoftException {
        List<TControlProductoTerminadoPesajesPenetromia> lResult = Utils.generarListaGenerica();

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            lResult = controlPtSetup.obtenerPesajesPenetromiaIdControlPt(idControlPt);
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // Retornamos los productos encontrados.
        return lResult;
    }

    /**
     * Método que nos cambia de ejercicio en la herramienta por el indicado en parámetro.
     * @param ejercicio
     * @throws GenasoftException 
     */
    private void accionCambiarEjercicio(String ejercicio) throws GenasoftException {
        if (ejercicio.equals("Todos")) {
            commonSetup.setEjercicio(-1);
        } else {
            Integer year = Integer.valueOf(ejercicio.trim());
            commonSetup.setEjercicio(year);
        }

        inicializarTodo(commonSetup.getEjercicio());
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public Integer obtenerEjercicioActivo(Integer userId, long time) throws GenasoftException {
        Integer result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = commonSetup.getEjercicio();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // El ejercicio activo.
        return result;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TreeMap<Date, List<TComprasVista>> obtenerComprasEjercicioActivo(Integer userId, long time) throws GenasoftException {
        TreeMap<Date, List<TComprasVista>> result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = commonSetup.gettComprasEjercicio();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // El ejercicio activo.
        return result;
    }

    /**
     * Método que nos retorna los productos activos en el sistema
     * @param userId El usuario que está activo.
     * @param time El tiempo en milisegundos.
     * @return Los productos encontrados.
     * @throws GenasoftException Si se ha iniciado sesión en otro dispositivo.
     */
    public TreeMap<Date, List<TVentasVista>> obtenerVentasEjercicioActivo(Integer userId, long time) throws GenasoftException {
        TreeMap<Date, List<TVentasVista>> result = null;

        Timestamp t = new Timestamp(time);
        if (commonSetup.conexionValida(userId, t, estadoAplicacion)) {
            result = commonSetup.gettVentasEjercicio();
        } else {
            if (estadoAplicacion == null || estadoAplicacion.equals(-1)) {
                throw new GenasoftException(Constants.LICENCIA_NO_VALIDA);
            } else {
                throw new GenasoftException(Constants.SESION_INVALIDA);
            }
        }
        // El ejercicio activo.
        return result;
    }

}
