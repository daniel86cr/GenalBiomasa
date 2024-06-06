/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.maestros.clientes;

import java.util.List;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TClientesMateriales;
import com.dina.genasoft.db.entity.TClientesOperadores;
import com.dina.genasoft.db.entity.TClientesTransportistas;
import com.dina.genasoft.db.entity.TDireccionClienteVista;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TMateriales;
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TRegistrosCambiosClientes;
import com.dina.genasoft.db.entity.TTransportistas;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.TablaGenerica;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.OperadorEnum;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
import com.dina.genasoft.vistas.maestros.clientes.direcciones.VistaDireccionCliente;
import com.dina.genasoft.vistas.maestros.clientes.direcciones.VistaNuevaDireccionCliente;
import com.dina.genasoft.vistas.maestros.materiales.VistaNuevoMaterial;
import com.dina.genasoft.vistas.maestros.operadores.VistaNuevoOperador;
import com.dina.genasoft.vistas.maestros.transportistas.VistaNuevoTransportista;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

/**
 * @author Daniel Carmona Romero
 * Vista para mostrar/visualizar un cliente.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaCliente.NAME)
public class VistaCliente extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas                             contrVista;
    /** Necesario para mostrar las direccones de descarga del cliente. */
    private BeanContainer<String, TDireccionClienteVista> bcDirecciones;
    /** El nombre de la vista.*/
    public static final String                            NAME               = "vCliente";
    /** Para los campos que componen un cliente.*/
    private BeanFieldGroup<TClientes>                     binder;
    /** El boton para crear el operador.*/
    private Button                                        modificarButton;
    /** El boton para volver al listado de clientes.*/
    private Button                                        listadoButton;
    /** Combobox para los estados.*/
    private ComboBox                                      cbEstado;
    /** Combobox para los materiales.*/
    private ComboBox                                      cbMateriales;
    /** Combobox para los operadores.*/
    private ComboBox                                      cbOperadores;
    /** Combobox para los transportistas.*/
    private ComboBox                                      cbTransportistas;
    /** El cliente a modificar.*/
    private TClientes                                     nClientes;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                                        appName;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                                       appWidth;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger                 log                = org.slf4j.LoggerFactory.getLogger(VistaCliente.class);
    // Los campos obligatorios
    /** La caja de texto para la referencia .*/
    private TextField                                     txtRazonSocial;
    /** La caja de texto para el nombre.*/
    private TextField                                     txtCif;
    /** La caja de texto para el nombre.*/
    private TextField                                     txtMatricula;
    /** La caja de texto para el nombre.*/
    private TextField                                     txtRemolque;
    /** Los permisos del empleado actual. */
    private TPermisos                                     permisos           = null;
    /** El usuario que está logado. */
    private Integer                                       user               = null;
    /** La fecha en que se inició sesión. */
    private Long                                          time               = null;
    private TEmpleados                                    empleado;
    /** Botones superiores. **/
    /** El botón principal del segmento. Muestra la información principal del cliente.*/
    private Button                                        principalButton;
    /** El botón principal del segmento. Muestra la información de las direcciones del cliente.*/
    private Button                                        direccionesButton;
    /** Muestra la información relacionada con los materiales del cliente.*/
    private Button                                        materialesButton;
    /** Muestra la información relacionada con los operadores del cliente.*/
    private Button                                        operadoresButton;
    /** Muestra la información relacionada con los operadores del cliente.*/
    private Button                                        transportistasButton;
    /** El boton para crear la dirección de descarga del cliente.*/
    private Button                                        crearDireccionButton;
    /** El boton para desactivar la dirección de descarga del cliente.*/
    private Button                                        desactivarDireccionButton;
    /** Container que mostrará los campos relacionado con la información principal del cliente. */
    private VerticalLayout                                infoPrincipal      = null;
    /** Container que mostrará los campos relacionado con la información de materiales del cliente. */
    private VerticalLayout                                infoMateriales     = null;
    /** Container que mostrará los campos relacionado con la información de operadores del cliente. */
    private VerticalLayout                                infoOperadores     = null;
    /** Container que mostrará los campos relacionado con la información de direcciones del cliente. */
    private VerticalLayout                                infoDirecciones    = null;
    /** Container que mostrará los campos relacionado con la información de transportistas del cliente. */
    private VerticalLayout                                infoTransportistas = null;
    /** ListSelect para añadir los materiales para los materiales. */
    private ListSelect                                    lsMateriales;
    /** ListSelect para añadir los materiales para los materiales. */
    private ListSelect                                    lsOperadores;
    /** ListSelect para añadir los materiales para los transportistas. */
    private ListSelect                                    lsTransportistas;
    /** Lista con los materiales activos del sistema. */
    private List<TMateriales>                             lMateriales;
    /** Lista con los operadores activos del sistema. */
    private List<TOperadores>                             lOperadores;
    /** Lista con los operadores activos del sistema. */
    private List<TTransportistas>                         lTransportistas;
    /** Lista con los materiales activos del sistema. */
    private List<TMateriales>                             lMaterialesAsignados;
    /** Lista con los operadores activos del sistema. */
    private List<TOperadores>                             lOperadoresAsignados;
    /** Lista con los transportistas activos del sistema. */
    private List<TTransportistas>                         lTransportistasAsignados;
    private String                                        cambios;
    /** Tabla para mostrar las direcciones del cliente. */
    private Table                                         tablaDirecciones;
    private String                                        idDireccionSeleccionada;
    /** Lista de direcciones de descarga del cliente. */
    private List<TDireccionClienteVista>                  lDirecciones       = null;
    private List<Integer>                                 lIncluidosOperadores;
    private List<Integer>                                 lIncluidosMateriales;
    private List<Integer>                                 lIncluidosTransportistas;
    private List<TOperadores>                             lEliminadosOperadores;
    private List<TMateriales>                             lEliminadosMateriales;
    private List<TTransportistas>                         lEliminadosTransportistas;
    private String                                        name;

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(modificarButton)) {
            // Creamos el evento para modificar un nuevo cliente con los datos introducidos en el formulario
            try {
                if (validarCamposObligatorios()) {
                    Notification aviso = new Notification("Se debe informar los campos marcados con '*'", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                // Construimos el objeto operador a partir de los datos introducidos en el formulario.
                construirBean();
                String result = contrVista.modificarCliente(nClientes, user, time);
                if (result.equals(Constants.OPERACION_OK)) {

                    // Si hay cambios, guardamos los cambios en el registro de cambios
                    if (!cambios.isEmpty()) {

                        TRegistrosCambiosClientes record = new TRegistrosCambiosClientes();
                        record.setCambio(cambios);
                        record.setFechaCambio(Utils.generarFecha());
                        record.setIdCliente(nClientes.getId());
                        record.setUsuCrea(user);

                        contrVista.crearRegistroCambioCliente(record, user, time);
                    }

                    // Asociamos los operadores que haya seleccionado
                    @SuppressWarnings("unchecked")
                    List<TOperadores> lOperadoresAsig = (List<TOperadores>) lsOperadores.getItemIds();
                    TClientesOperadores clOp = null;
                    TRegistrosCambiosClientes record = null;
                    TRegistrosCambiosClientes record2 = null;
                    for (TOperadores op : lOperadoresAsig) {
                        clOp = new TClientesOperadores();
                        clOp.setEstado(1);
                        clOp.setFechaCrea(Utils.generarFecha());
                        clOp.setIdCliente(nClientes.getId());
                        clOp.setIdOperador(op.getId());
                        clOp.setUsuCrea(user);
                        contrVista.asignarOperadorCliente(clOp, user, time);

                        if (lIncluidosOperadores.contains(op.getId())) {
                            record = new TRegistrosCambiosClientes();
                            record.setCambio("Se le asigna el operador: " + op.getNombre());
                            record.setFechaCambio(Utils.generarFecha());
                            record.setIdCliente(nClientes.getId());
                            record.setUsuCrea(user);

                            contrVista.crearRegistroCambioCliente(record, user, time);
                        }
                    }

                    // Asociamos los materiales que haya seleccionado
                    @SuppressWarnings("unchecked")
                    List<TMateriales> lMaterialesAsig = (List<TMateriales>) lsMateriales.getItemIds();
                    TClientesMateriales clMat = null;

                    for (TMateriales mat : lMaterialesAsig) {
                        clMat = new TClientesMateriales();
                        clMat.setEstado(1);
                        clMat.setFechaCrea(Utils.generarFecha());
                        clMat.setIdCliente(nClientes.getId());
                        clMat.setIva(mat.getIva());
                        clMat.setPrecioKg(mat.getPrecio());
                        clMat.setIdMaterial(mat.getId());
                        clMat.setUsuCrea(user);
                        contrVista.asignarMaterialCliente(clMat, user, time);

                        if (lIncluidosMateriales.contains(mat.getId())) {
                            record2 = new TRegistrosCambiosClientes();
                            record2.setCambio("Se le asigna el material: " + mat.getDescripcion());
                            record2.setFechaCambio(Utils.generarFecha());
                            record2.setIdCliente(nClientes.getId());
                            record2.setUsuCrea(user);

                            contrVista.crearRegistroCambioCliente(record2, user, time);
                        }
                    }

                    // Asociamos los transportistas que haya seleccionado
                    @SuppressWarnings("unchecked")
                    List<TTransportistas> lTransportistasAsig = (List<TTransportistas>) lsTransportistas.getItemIds();
                    TClientesTransportistas clTrans = null;

                    for (TTransportistas mat : lTransportistasAsig) {
                        clTrans = new TClientesTransportistas();
                        clTrans.setEstado(1);
                        clTrans.setFechaCrea(Utils.generarFecha());
                        clTrans.setIdCliente(nClientes.getId());
                        clTrans.setIdTransportista(mat.getId());
                        clTrans.setUsuCrea(user);
                        contrVista.asignarTransportistaCliente(clTrans, user, time);

                        if (lIncluidosTransportistas.contains(mat.getId())) {
                            record2 = new TRegistrosCambiosClientes();
                            record2.setCambio("Se le asigna el transportista: " + mat.getNombre());
                            record2.setFechaCambio(Utils.generarFecha());
                            record2.setIdCliente(nClientes.getId());
                            record2.setUsuCrea(user);

                            contrVista.crearRegistroCambioCliente(record2, user, time);
                        }
                    }

                    // Desactivamos lo que ha quitado del listado de operadores.
                    for (TOperadores op : lEliminadosOperadores) {
                        clOp = new TClientesOperadores();
                        clOp.setEstado(0);
                        clOp.setFechaCrea(Utils.generarFecha());
                        clOp.setFechaModifica(Utils.generarFecha());
                        clOp.setIdCliente(nClientes.getId());
                        clOp.setIdOperador(op.getId());
                        clOp.setUsuCrea(user);
                        clOp.setUsuModifica(user);
                        contrVista.asignarOperadorCliente(clOp, user, time);

                        record = new TRegistrosCambiosClientes();
                        record.setCambio("Se le desactiva el operador: " + op.getNombre());
                        record.setFechaCambio(Utils.generarFecha());
                        record.setIdCliente(nClientes.getId());
                        record.setUsuCrea(user);

                        contrVista.crearRegistroCambioCliente(record, user, time);
                    }

                    // Desactivamos lo que ha quitado del listado de transportistas.
                    for (TTransportistas op : lEliminadosTransportistas) {
                        clTrans = new TClientesTransportistas();
                        clTrans.setEstado(0);
                        clTrans.setFechaCrea(Utils.generarFecha());
                        clTrans.setFechaModifica(Utils.generarFecha());
                        clTrans.setIdCliente(nClientes.getId());
                        clTrans.setIdTransportista(op.getId());
                        clTrans.setUsuCrea(user);
                        clTrans.setUsuModifica(user);
                        contrVista.asignarTransportistaCliente(clTrans, user, time);

                        record = new TRegistrosCambiosClientes();
                        record.setCambio("Se le desactiva el transportista: " + op.getNombre());
                        record.setFechaCambio(Utils.generarFecha());
                        record.setIdCliente(nClientes.getId());
                        record.setUsuCrea(user);

                        contrVista.crearRegistroCambioCliente(record, user, time);
                    }

                    result = contrVista.obtenerDescripcionCodigo(result);
                    Notification aviso = new Notification(result, Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                } else {
                    result = contrVista.obtenerDescripcionCodigo(result);
                    Notification aviso = new Notification(result, Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
                // Si la operación se ha completado con éxito, inicializamos los componentes de la pantalla.                
            } catch (GenasoftException te) {
                if (te.getMessage().equals(Constants.SESION_INVALIDA)) {
                    Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(te.getMessage()), Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    // Redirigimos a la página de inicio.
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                } else {
                    Notification aviso = new Notification(te.getMessage(), Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            } catch (MyBatisSystemException e) {
                Notification aviso = new Notification("No se ha podido establecer conexión con la base de datos.", Notification.Type.ERROR_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                log.error("Error al obtener datos de base de datos ", e);
            } catch (Exception e) {
                log.error("ERROR", e);
                Notification aviso = new Notification("Revise que los datos son correctos", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }

        } else if (event.getButton().equals(listadoButton)) {
            getUI().getNavigator().navigateTo(VistaListadoClientes.NAME);
        } else if (event.getButton().equals(crearDireccionButton)) {
            getUI().getNavigator().navigateTo(VistaNuevaDireccionCliente.NAME + "/" + nClientes.getId());
        }
    }

    /**
     * Se inicializan botones, eventos, etc.
     */
    @PostConstruct
    void init() {

        setSizeFull();
        // Establecemos el tamaño de la pantalla.
        setHeightUndefined();

    }

    @Override
    public void enter(ViewChangeEvent event) {
        user = null;
        time = null;

        user = (Integer) getSession().getAttribute("user");
        if ((String) getSession().getAttribute("fecha") != null) {
            time = Long.parseLong((String) getSession().getAttribute("fecha"));
        }

        if (time != null) {
            try {
                binder = new BeanFieldGroup<>(TClientes.class);

                empleado = contrVista.obtenerEmpleadoPorId(user, user, time);

                permisos = contrVista.obtenerPermisosEmpleado(empleado, user, time);

                bcDirecciones = new BeanContainer<>(TDireccionClienteVista.class);
                bcDirecciones.setBeanIdProperty("id");

                lIncluidosMateriales = Utils.generarListaGenerica();
                lIncluidosOperadores = Utils.generarListaGenerica();
                lIncluidosTransportistas = Utils.generarListaGenerica();
                lEliminadosMateriales = Utils.generarListaGenerica();
                lEliminadosOperadores = Utils.generarListaGenerica();
                lEliminadosTransportistas = Utils.generarListaGenerica();

                if (permisos == null) {
                    Notification aviso = new Notification("No se ha podido identificar los permisos asignados, contacta con el administrador.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    return;
                }

                if (!Utils.booleanFromInteger(permisos.getEntornoMaestros())) {
                    Notification aviso = new Notification("No se tienen permisos para acceder a la pantalla indicada", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                    return;
                }

                // Creamos los botones de la pantalla.
                crearBotones();
                // Creamos los combos de la pantalla.
                crearCombos();
                // Creamos los botones superiores
                crearBotonesMenu();
                // Creamos la tabla de direcciones
                crearTablaDirecciones();
                //El fieldgroup no es un componente

                // Obtenemos los operadores activos del sistema.
                lOperadores = contrVista.obtenerOperadoresActivos(user, time);

                // Obtenemos los transportistas activos del sistema.
                lTransportistas = contrVista.obtenerTransportistasActivos(user, time);

                String parametros = event.getParameters();

                if (parametros != null && !parametros.isEmpty()) {
                    nClientes = contrVista.obtenerClientePorId(Integer.valueOf(parametros), user, time);
                } else {
                    parametros = null;
                }

                if (nClientes == null) {
                    Notification aviso = new Notification("No se ha encontrado el cliente.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getUI().getNavigator().navigateTo(VistaListadoClientes.NAME);
                    return;
                }

                // Obtenemos los materiales activos del sistema.
                lMateriales = contrVista.obtenerMaterialesActivos(user, time);
                lMaterialesAsignados = contrVista.obtenerMaterialesAsignadosCliente(nClientes.getId(), user, time);
                lOperadoresAsignados = contrVista.obtenerOperadoresAsignadosCliente(nClientes.getId(), user, time);
                lDirecciones = contrVista.obtenerDireccionesClientePorIdClienteVista(nClientes.getId(), user, time);
                lTransportistasAsignados = contrVista.obtenerTransportistasAsignadosCliente(nClientes.getId(), user, time);

                binder.setItemDataSource(nClientes);
                // Creamos los combos de la pantalla.
                crearCombos();

                // Creamos los botones de la pantalla.
                crearBotones();

                // Creamos los botones superiores
                crearBotonesMenu();

                // Creamos los componetes que conforman la pantalla.
                crearComponentes();

                bcDirecciones.removeAllItems();
                bcDirecciones.addAll(lDirecciones);

                // Generamos las partes de la intefaz.
                generaInformacionPrincipal();
                generaInformacionMateriales();
                generaInformacioOperadores();
                generaInformacionDirecciones();
                generaInformacioTransportistas();

                Label texto = new Label(nClientes.getNombre());
                texto.setStyleName("tituloTamano18");
                texto.setHeight(4, Sizeable.Unit.EM);
                Label texto2 = new Label(" ");
                texto2.setStyleName("tituloTamano18");
                texto2.setHeight(4, Sizeable.Unit.EM);
                // Incluimos en el layout los componentes
                VerticalLayout titulo = new VerticalLayout(texto);

                // The view root layout
                VerticalLayout viewLayout = new VerticalLayout(new Menu(permisos, empleado.getId()));
                viewLayout.setSizeFull();
                // Creamos y añadimos el logo de Genasoft a la pantalla
                HorizontalLayout imgGenaSoft = contrVista.logoGenaSoft();

                viewLayout.addComponent(imgGenaSoft);
                viewLayout.setComponentAlignment(imgGenaSoft, Alignment.TOP_RIGHT);
                viewLayout.addComponent(titulo);
                viewLayout.setComponentAlignment(titulo, Alignment.TOP_CENTER);

                HorizontalLayout body = new HorizontalLayout();
                body.setSpacing(true);
                body.setMargin(true);

                // Formulario con los campos que componen el operador.
                VerticalLayout formulario1 = new VerticalLayout();
                formulario1.setSpacing(true);
                // Formulario con los campos que componen el operador.
                VerticalLayout formulario2 = new VerticalLayout();
                formulario2.setSpacing(true);

                Label lblSpace = new Label(" ");
                lblSpace.setHeight(5, Sizeable.Unit.EM);

                viewLayout.addComponent(crearBotonesMenu());
                viewLayout.addComponent(infoPrincipal);
                viewLayout.setComponentAlignment(infoPrincipal, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(infoDirecciones);
                viewLayout.setComponentAlignment(infoDirecciones, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(infoMateriales);
                viewLayout.setComponentAlignment(infoMateriales, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(infoOperadores);
                viewLayout.setComponentAlignment(infoOperadores, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(infoTransportistas);
                viewLayout.setComponentAlignment(infoTransportistas, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(modificarButton);
                viewLayout.setComponentAlignment(modificarButton, Alignment.MIDDLE_CENTER);

                // Añadimos el logo del cliente
                viewLayout.addComponent(contrVista.logoCliente());
                setCompositionRoot(viewLayout);
                // Establecemos el porcentaje de ratio para los layouts
                viewLayout.setExpandRatio(titulo, 0.1f);
                viewLayout.setMargin(true);
                viewLayout.setSpacing(true);

                infoPrincipal.setVisible(true);
                infoMateriales.setVisible(false);
                infoOperadores.setVisible(false);
                infoDirecciones.setVisible(false);
                infoTransportistas.setVisible(false);

            } catch (MyBatisSystemException e) {
                Notification aviso = new Notification("No se ha podido establecer conexión con la base de datos.", Notification.Type.ERROR_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                log.error("Error al obtener datos de base de datos ", e);
            } catch (GenasoftException tbe) {
                if (tbe.getMessage().equals(Constants.SESION_INVALIDA)) {
                    Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    // Redirigimos a la página de inicio.
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                } else {
                    Notification aviso = new Notification(tbe.getMessage(), Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }
        } else {
            getSession().setAttribute("user", null);
            getSession().setAttribute("fecha", null);
            // Redirigimos a la página de inicio.
            getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
        }
    }

    /**
     * Método que nos crea los botones que contiene la vista del menú.
     */
    private void crearBotones() {
        // Creamos los botones.
        modificarButton = new Button("Aplicar cambios", this);
        modificarButton.addStyleName("big");
        listadoButton = new Button("Volver al listado", this);
        listadoButton.addStyleName("big");

        // Direcciones
        crearDireccionButton = new Button("Crear dirección", this);
        crearDireccionButton.addStyleName("big");

        desactivarDireccionButton = new Button("Desactivar dirección", this);
        desactivarDireccionButton.addStyleName("big");
    }

    /**
     * Método que nos genera el contenedor con los botones que conforma el segmento del menú de cliente. 
     * @return El contenedor con los botones.
     */
    private HorizontalLayout crearBotonesMenu() {
        HorizontalLayout botonesMenu = new HorizontalLayout();
        botonesMenu.setStyleName("segment");
        botonesMenu.addStyleName("segment-alternate");

        // Los botones que componen el menú.
        principalButton = new Button("Información principal");
        principalButton.addStyleName("default");
        principalButton.addStyleName("first");
        principalButton.addStyleName("down");
        principalButton.setStyleName("down");
        // Infiormación principal
        principalButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!infoPrincipal.isVisible()) {
                    infoPrincipal.setVisible(true);
                    infoOperadores.setVisible(false);
                    infoMateriales.setVisible(false);
                    infoDirecciones.setVisible(false);
                    infoTransportistas.setVisible(false);
                    principalButton.setStyleName("down");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("default");
                    direccionesButton.setStyleName("default");
                    transportistasButton.setStyleName("default");
                }
            }
        });

        direccionesButton = new Button("Direcciones");
        direccionesButton.addStyleName("default");
        direccionesButton.addStyleName("down");

        // Infiormación de direcciones
        direccionesButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!infoDirecciones.isVisible()) {
                    infoDirecciones.setVisible(true);
                    infoPrincipal.setVisible(false);
                    infoOperadores.setVisible(false);
                    infoMateriales.setVisible(false);
                    infoTransportistas.setVisible(false);
                    direccionesButton.setStyleName("down");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("default");
                    principalButton.setStyleName("default");
                    transportistasButton.setStyleName("default");
                }
            }
        });

        materialesButton = new Button("Materiales");
        materialesButton.addStyleName("default");
        materialesButton.addStyleName("down");

        materialesButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!infoMateriales.isVisible()) {
                    infoMateriales.setVisible(true);
                    infoPrincipal.setVisible(false);
                    infoDirecciones.setVisible(false);
                    infoOperadores.setVisible(false);
                    infoTransportistas.setVisible(false);
                    principalButton.setStyleName("default");
                    materialesButton.setStyleName("down");
                    operadoresButton.setStyleName("default");
                    direccionesButton.setStyleName("default");
                    transportistasButton.setStyleName("default");
                }
            }
        });

        operadoresButton = new Button("Operadores");
        operadoresButton.addStyleName("default");
        operadoresButton.addStyleName("down");

        operadoresButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!infoOperadores.isVisible()) {
                    infoOperadores.setVisible(true);
                    infoMateriales.setVisible(false);
                    infoPrincipal.setVisible(false);
                    infoDirecciones.setVisible(false);
                    infoTransportistas.setVisible(false);
                    principalButton.setStyleName("default");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("down");
                    direccionesButton.setStyleName("default");
                    transportistasButton.setStyleName("default");
                }
            }
        });

        transportistasButton = new Button("Transportistas");
        transportistasButton.addStyleName("default");
        transportistasButton.addStyleName("down");

        transportistasButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!infoTransportistas.isVisible()) {
                    infoTransportistas.setVisible(true);
                    infoOperadores.setVisible(false);
                    infoMateriales.setVisible(false);
                    infoPrincipal.setVisible(false);
                    infoDirecciones.setVisible(false);
                    principalButton.setStyleName("default");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("default");
                    direccionesButton.setStyleName("default");
                    transportistasButton.setStyleName("down");

                }
            }
        });

        botonesMenu.addComponent(principalButton);
        botonesMenu.addComponent(direccionesButton);
        botonesMenu.addComponent(materialesButton);
        botonesMenu.addComponent(operadoresButton);
        botonesMenu.addComponent(transportistasButton);

        // Retornamos el segmento de botones.
        return botonesMenu;
    }

    /**
     * Método para crear los combos de la pantalla.
     */
    private void crearCombos() {
        // Combo box para el estado.
        cbEstado = new ComboBox();
        cbEstado.addStyleName("big");

        cbMateriales = new ComboBox();
        cbMateriales.addStyleName("big");

        cbOperadores = new ComboBox();
        cbOperadores.addStyleName("big");

        cbTransportistas = new ComboBox();
        cbTransportistas.addStyleName("big");
    }

    /**
     * Método que nos crea los componetes que conforman la pantalla.
     */
    private void crearComponentes() {
        //Los campos que componen un operador.

        // La razón social.
        txtRazonSocial = (TextField) binder.buildAndBind("Razón social:", "nombre");
        txtRazonSocial.setNullRepresentation("");
        txtRazonSocial.setWidth(appWidth, Sizeable.Unit.EM);
        txtRazonSocial.setMaxLength(445);
        txtRazonSocial.setRequired(true);

        // El CIF
        txtCif = (TextField) binder.buildAndBind("CIF:", "cif");
        txtCif.setNullRepresentation("");
        txtCif.setRequired(true);
        txtCif.setWidth(appWidth, Sizeable.Unit.EM);
        txtCif.setMaxLength(45);

        // La matrícula
        txtMatricula = (TextField) binder.buildAndBind("Matrícula:", "matricula");
        txtMatricula.setNullRepresentation("");
        txtMatricula.setRequired(true);
        txtMatricula.setWidth(appWidth, Sizeable.Unit.EM);
        txtMatricula.setMaxLength(45);

        // Remolque
        txtRemolque = (TextField) binder.buildAndBind("Remolque:", "remolque");
        txtRemolque.setNullRepresentation("");
        txtRemolque.setRequired(true);
        txtRemolque.setWidth(appWidth, Sizeable.Unit.EM);
        txtRemolque.setMaxLength(45);

        // Los estados.
        cbEstado.setCaption("Estado:");
        cbEstado.addItem(Constants.ACTIVO);
        cbEstado.addItem(Constants.DESACTIVADO);
        cbEstado.setValue(nClientes.getEstado().equals(OperadorEnum.ACTIVO.getValue()) ? Constants.ACTIVO : Constants.DESACTIVADO);
        cbEstado.setFilteringMode(FilteringMode.CONTAINS);
        cbEstado.setRequired(true);
        cbEstado.setNullSelectionAllowed(false);
        cbEstado.setWidth(appWidth, Sizeable.Unit.EM);

    }

    /**
     * Método que es llamado para crear el objeto bean para la creación.
     */
    private void construirBean() throws GenasoftException {
        cambios = "";
        Integer estado = cbEstado.getValue().equals(Constants.ACTIVO) ? 1 : 0;
        if (!nClientes.getEstado().equals(estado)) {
            cambios = "Se cambia el estado del operador, pasa de " + nClientes.getEstado() + " a " + estado;
        }
        nClientes.setEstado(cbEstado.getValue().equals(Constants.ACTIVO) ? 1 : 0);
        String value = txtRazonSocial.getValue();

        if (value != null) {
            value = value.trim().toUpperCase();
        }
        if (value == null && nClientes.getNombre() != null) {
            cambios = cambios + "\n Se le quita el nombre, antes tenia: " + nClientes.getNombre();
        } else if (value != null && nClientes.getNombre() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna una nuevo nombre, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nClientes.getNombre())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia el nombre, antes tenia: " + nClientes.getNombre() + " y ahora tiene: " + value;
        }

        nClientes.setNombre(value);
        nClientes.setRazonSocial(value);

        value = txtCif.getValue().trim().toUpperCase();

        if (value != null) {
            value = value.trim().toUpperCase();
        }

        if (value == null && nClientes.getCif() != null) {
            cambios = cambios + "\n Se le quita el CIF, antes tenia: " + nClientes.getCif();
        } else if (value != null && nClientes.getCif() == null) {
            cambios = cambios + "\n Se le asigna un nuevo CIF,  antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nClientes.getCif())) {
            cambios = cambios + "\n Se le cambia el CIF, antes tenia: " + nClientes.getCif() + " y ahora tiene: " + value;
        }

        nClientes.setCif(value);

        value = txtMatricula.getValue().trim().toUpperCase();

        if (value != null) {
            value = value.trim().toUpperCase();
        }

        if (value == null && nClientes.getMatricula() != null) {
            cambios = cambios + "\n Se le quita la matrícula, antes tenia: " + nClientes.getMatricula();
        } else if (value != null && nClientes.getMatricula() == null) {
            cambios = cambios + "\n Se le asigna una nueva matrícula,  antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nClientes.getMatricula())) {
            cambios = cambios + "\n Se le cambia la matrícula, antes tenia: " + nClientes.getMatricula() + " y ahora tiene: " + value;
        }

        nClientes.setMatricula(value);

        value = txtRemolque.getValue().trim().toUpperCase();

        if (value != null) {
            value = value.trim().toUpperCase();
        }

        if (value == null && nClientes.getRemolque() != null) {
            cambios = cambios + "\n Se le quita el remolque, antes tenia: " + nClientes.getRemolque();
        } else if (value != null && nClientes.getRemolque() == null) {
            cambios = cambios + "\n Se le asigna un nuev remolque,  antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nClientes.getRemolque())) {
            cambios = cambios + "\n Se le cambia el remolque, antes tenia: " + nClientes.getRemolque() + " y ahora tiene: " + value;
        }

        nClientes.setRemolque(value);

        if (!cambios.isEmpty()) {
            nClientes.setFechaModifica(Utils.generarFecha());
            nClientes.setUsuModifica(user);
        }

    }

    /**
     * Comprobamos si ha informado los campos obligatorios.
     * @return true si no se cumple la validación
     */
    private boolean validarCamposObligatorios() {
        return !cbEstado.isValid() || !txtCif.isValid() || !txtRazonSocial.isValid();
    }

    /**
     * Método que nos crea la parte de la interfaz con la información principal de los clientes
     */
    private void generaInformacionPrincipal() {
        infoPrincipal = new VerticalLayout();
        infoPrincipal.setSpacing(true);
        infoPrincipal.setMargin(true);

        HorizontalLayout body = new HorizontalLayout();
        body.setSpacing(true);
        body.setMargin(true);

        // Formulario con los campos que componen el empleado.
        VerticalLayout formulario1 = new VerticalLayout();
        formulario1.setSpacing(true);

        formulario1.addComponent(txtRazonSocial);
        formulario1.setComponentAlignment(txtRazonSocial, Alignment.MIDDLE_CENTER);
        formulario1.addComponent(txtCif);
        formulario1.setComponentAlignment(txtCif, Alignment.MIDDLE_CENTER);
        formulario1.addComponent(txtMatricula);
        formulario1.setComponentAlignment(txtMatricula, Alignment.MIDDLE_CENTER);
        formulario1.addComponent(txtRemolque);
        formulario1.setComponentAlignment(txtRemolque, Alignment.MIDDLE_CENTER);
        formulario1.addComponent(cbEstado);
        formulario1.setComponentAlignment(cbEstado, Alignment.MIDDLE_CENTER);
        body.addComponent(formulario1);
        body.setComponentAlignment(formulario1, Alignment.MIDDLE_CENTER);

        infoPrincipal.addComponent(body);
        infoPrincipal.setComponentAlignment(body, Alignment.MIDDLE_CENTER);
    }

    /**
     * Método que nos crea la parte de la interfaz con la información principal de los clientes
     */
    private void generaInformacionMateriales() {
        infoMateriales = new VerticalLayout();
        infoMateriales.setSpacing(true);
        infoMateriales.setMargin(true);

        HorizontalLayout body = new HorizontalLayout();
        body.setSpacing(true);
        body.setMargin(true);

        cbMateriales.setCaption("Material:");
        cbMateriales.setFilteringMode(FilteringMode.CONTAINS);
        cbMateriales.setNullSelectionAllowed(false);
        cbMateriales.setNewItemsAllowed(true);
        cbMateriales.setWidth(appWidth, Sizeable.Unit.EM);
        cbMateriales.addItems(lMateriales);

        cbMateriales.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbMateriales.getValue() != null) {
                    if (cbMateriales.getValue().getClass().equals(TMateriales.class)) {

                        Boolean entra = false;
                        List<TMateriales> lOperadoresAsig = (List<TMateriales>) lsMateriales.getItemIds();
                        for (TMateriales op : lOperadoresAsig) {
                            if (op.getId().equals(((TMateriales) cbMateriales.getValue()).getId())) {
                                entra = true;
                            }
                        }
                        if (!entra) {
                            lsMateriales.addItem((TMateriales) cbMateriales.getValue());
                            lIncluidosMateriales.add(((TMateriales) cbMateriales.getValue()).getId());
                        }

                        cbMateriales.clear();
                    } else {
                        try {
                            name = (String) cbMateriales.getValue();
                            TMateriales mat = contrVista.obtenerMaterialPorNombre(name.trim().toUpperCase(), user, time);
                            // Buscamos el material por si aca...
                            if (mat == null) {
                                if (name.contains(" ")) {
                                    name = name.replaceAll(" ", "_");
                                }
                                MessageBox.createQuestion().withCaption(appName).withMessage("¿Quieres crear un nuevo material?").withNoButton().withYesButton(() ->

                                Page.getCurrent().open("#!" + VistaNuevoMaterial.NAME + "/" + user + "," + name, "_blank"), ButtonOption.caption("Sí")).open();
                                cbMateriales.clear();
                            } else {
                                lsMateriales.addItem(mat);
                                lIncluidosMateriales.add(mat.getId());
                                cbMateriales.clear();
                            }
                        } catch (GenasoftException tbe) {
                            if (tbe.getMessage().equals(Constants.SESION_INVALIDA)) {
                                Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
                                aviso.setPosition(Position.MIDDLE_CENTER);
                                aviso.show(Page.getCurrent());
                                getSession().setAttribute("user", null);
                                getSession().setAttribute("fecha", null);
                                // Redirigimos a la página de inicio.
                                getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                            } else {
                                Notification aviso = new Notification(tbe.getMessage(), Notification.Type.ERROR_MESSAGE);
                                aviso.setPosition(Position.MIDDLE_CENTER);
                                aviso.show(Page.getCurrent());
                            }
                        }
                    }
                }
            }
        });

        lsMateriales = new ListSelect("Materiales asignados");
        lsMateriales.setWidth(appWidth, Sizeable.Unit.EM);
        lsMateriales.removeAllItems();

        for (TMateriales clMat : lMaterialesAsignados) {
            lsMateriales.addItem(clMat);
        }

        lsMateriales.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsMateriales.getValue() != null) {
                    lEliminadosMateriales.add((TMateriales) lsMateriales.getValue());
                    lsMateriales.removeItem(lsMateriales.getValue());
                }
            }
        });

        body.addComponent(cbMateriales);
        body.setComponentAlignment(cbMateriales, Alignment.MIDDLE_CENTER);
        body.addComponent(lsMateriales);
        body.setComponentAlignment(lsMateriales, Alignment.MIDDLE_CENTER);

        infoMateriales.addComponent(body);
        infoMateriales.setComponentAlignment(body, Alignment.MIDDLE_CENTER);
    }

    /**
     * Método que nos crea la parte de la interfaz con la información principal de los clientes
     */
    private void generaInformacioOperadores() {
        infoOperadores = new VerticalLayout();
        infoOperadores.setSpacing(true);
        infoOperadores.setMargin(true);

        HorizontalLayout body = new HorizontalLayout();
        body.setSpacing(true);
        body.setMargin(true);

        cbOperadores.setCaption("Operador:");
        cbOperadores.setFilteringMode(FilteringMode.CONTAINS);
        cbOperadores.setNullSelectionAllowed(false);
        cbOperadores.setWidth(appWidth, Sizeable.Unit.EM);
        cbOperadores.addItems(lOperadores);

        cbOperadores.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbOperadores.getValue() != null) {
                    if (cbOperadores.getValue().getClass().equals(TOperadores.class)) {
                        Boolean entra = false;
                        List<TOperadores> lOperadoresAsig = (List<TOperadores>) lsOperadores.getItemIds();
                        for (TOperadores op : lOperadoresAsig) {
                            if (op.getId().equals(((TOperadores) cbOperadores.getValue()).getId())) {
                                entra = true;
                            }
                        }
                        if (!entra) {
                            lsOperadores.addItem((TOperadores) cbOperadores.getValue());
                        }
                        cbOperadores.clear();
                    } else {
                        try {
                            name = (String) cbOperadores.getValue();
                            TOperadores mat = contrVista.obtenerOperadorPorNombre(name.trim().toUpperCase(), user, time);
                            // Buscamos el material por si aca...
                            if (mat == null) {
                                if (name.contains(" ")) {
                                    name = name.replaceAll(" ", "_");
                                }
                                MessageBox.createQuestion().withCaption(appName).withMessage("¿Quieres crear un nuevo operador?").withNoButton().withYesButton(() ->

                                Page.getCurrent().open("#!" + VistaNuevoOperador.NAME + "/" + user + "," + name, "_blank"), ButtonOption.caption("Sí")).open();
                                cbOperadores.clear();
                            } else {
                                lsOperadores.addItem(mat);
                                cbOperadores.clear();
                            }
                        } catch (GenasoftException tbe) {
                            if (tbe.getMessage().equals(Constants.SESION_INVALIDA)) {
                                Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
                                aviso.setPosition(Position.MIDDLE_CENTER);
                                aviso.show(Page.getCurrent());
                                getSession().setAttribute("user", null);
                                getSession().setAttribute("fecha", null);
                                // Redirigimos a la página de inicio.
                                getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                            } else {
                                Notification aviso = new Notification(tbe.getMessage(), Notification.Type.ERROR_MESSAGE);
                                aviso.setPosition(Position.MIDDLE_CENTER);
                                aviso.show(Page.getCurrent());
                            }
                        }
                    }
                }
            }
        });

        lsOperadores = new ListSelect("Operadores asignados");
        lsOperadores.setWidth(appWidth, Sizeable.Unit.EM);
        lsOperadores.removeAllItems();

        for (TOperadores clOp : lOperadoresAsignados) {
            lsOperadores.addItem(clOp);
        }

        lsOperadores.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsOperadores.getValue() != null) {
                    lEliminadosOperadores.add((TOperadores) lsOperadores.getValue());
                    lsOperadores.removeItem(lsOperadores.getValue());
                }
            }
        });

        body.addComponent(cbOperadores);
        body.setComponentAlignment(cbOperadores, Alignment.MIDDLE_CENTER);
        body.addComponent(lsOperadores);
        body.setComponentAlignment(lsOperadores, Alignment.MIDDLE_CENTER);

        infoOperadores.addComponent(body);
        infoOperadores.setComponentAlignment(body, Alignment.MIDDLE_CENTER);
    }

    /**
     * Método que nos crea la parte de la interfaz con la información principal de los clientes
     */
    private void generaInformacioTransportistas() {
        infoTransportistas = new VerticalLayout();
        infoTransportistas.setSpacing(true);
        infoTransportistas.setMargin(true);

        HorizontalLayout body = new HorizontalLayout();
        body.setSpacing(true);
        body.setMargin(true);

        cbTransportistas.setCaption("Transportista:");
        cbTransportistas.setFilteringMode(FilteringMode.CONTAINS);
        cbTransportistas.setNullSelectionAllowed(false);
        cbTransportistas.setWidth(appWidth, Sizeable.Unit.EM);
        cbTransportistas.addItems(lTransportistas);

        cbTransportistas.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbTransportistas.getValue() != null) {
                    if (cbTransportistas.getValue().getClass().equals(TTransportistas.class)) {
                        Boolean entra = false;
                        List<TTransportistas> lOperadoresAsig = (List<TTransportistas>) lsTransportistas.getItemIds();
                        for (TTransportistas op : lOperadoresAsig) {
                            if (op.getId().equals(((TTransportistas) cbTransportistas.getValue()).getId())) {
                                entra = true;
                            }
                        }
                        if (!entra) {
                            lsTransportistas.addItem((TTransportistas) cbTransportistas.getValue());
                        }
                        cbTransportistas.clear();
                    } else {
                        try {
                            name = (String) cbOperadores.getValue();
                            TTransportistas mat = contrVista.obtenerTransportistaPorNombre(name.trim().toUpperCase(), user, time);
                            // Buscamos el material por si aca...
                            if (mat == null) {
                                if (name.contains(" ")) {
                                    name = name.replaceAll(" ", "_");
                                }
                                MessageBox.createQuestion().withCaption(appName).withMessage("¿Quieres crear un nuevo transportista?").withNoButton().withYesButton(() ->

                                Page.getCurrent().open("#!" + VistaNuevoTransportista.NAME + "/" + user + "," + name, "_blank"), ButtonOption.caption("Sí")).open();
                                cbTransportistas.clear();
                            } else {
                                lsTransportistas.addItem(mat);
                                cbTransportistas.clear();
                            }
                        } catch (GenasoftException tbe) {
                            if (tbe.getMessage().equals(Constants.SESION_INVALIDA)) {
                                Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
                                aviso.setPosition(Position.MIDDLE_CENTER);
                                aviso.show(Page.getCurrent());
                                getSession().setAttribute("user", null);
                                getSession().setAttribute("fecha", null);
                                // Redirigimos a la página de inicio.
                                getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                            } else {
                                Notification aviso = new Notification(tbe.getMessage(), Notification.Type.ERROR_MESSAGE);
                                aviso.setPosition(Position.MIDDLE_CENTER);
                                aviso.show(Page.getCurrent());
                            }
                        }
                    }
                }
            }
        });

        lsTransportistas = new ListSelect("Transportistas asignados");
        lsTransportistas.setWidth(appWidth, Sizeable.Unit.EM);
        lsTransportistas.removeAllItems();

        for (TTransportistas clOp : lTransportistasAsignados) {
            lsTransportistas.addItem(clOp);
        }

        lsTransportistas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsTransportistas.getValue() != null) {
                    lEliminadosTransportistas.add((TTransportistas) lsTransportistas.getValue());
                    lsTransportistas.removeItem(lsTransportistas.getValue());
                }
            }
        });

        body.addComponent(cbTransportistas);
        body.setComponentAlignment(cbTransportistas, Alignment.MIDDLE_CENTER);
        body.addComponent(lsTransportistas);
        body.setComponentAlignment(lsTransportistas, Alignment.MIDDLE_CENTER);

        infoTransportistas.addComponent(body);
        infoTransportistas.setComponentAlignment(body, Alignment.MIDDLE_CENTER);
    }

    private void generaInformacionDirecciones() {

        infoDirecciones = new VerticalLayout();
        infoDirecciones.setSpacing(true);
        infoDirecciones.setMargin(true);

        HorizontalLayout botonera = new HorizontalLayout();
        botonera.setSpacing(true);

        botonera.addComponent(crearDireccionButton);
        botonera.addComponent(desactivarDireccionButton);

        botonera.setMargin(true);

        Label lblDirecciones = new Label("Direcciones");
        lblDirecciones.setStyleName("tituloTamano12");
        // Creamos el componente de filtro.
        infoDirecciones.addComponent(botonera);
        infoDirecciones.addComponent(lblDirecciones);
        infoDirecciones.addComponent(tablaDirecciones);

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaDirecciones() {
        tablaDirecciones = new TablaGenerica(new Object[] { "codDireccion", "direccion", "codigoPostal", "poblacion", "provincia", "fechaCrea", "estado" }, new String[] { "Código dirección", "Dirección", "Código postal", "Población", "Provincia", "Fecha alta", "Estado" }, bcDirecciones);
        tablaDirecciones.addStyleName("big striped");
        tablaDirecciones.setPageLength(20);

        // Establecemos tamaño fijo en columnas específicas.
        tablaDirecciones.setColumnWidth("referencia", 80);
        tablaDirecciones.setColumnWidth("estado", 70);

        tablaDirecciones.addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                idDireccionSeleccionada = (String) event.getItemId();

                if (event.isDoubleClick()) {
                    getUI().getNavigator().navigateTo(VistaDireccionCliente.NAME + "/" + idDireccionSeleccionada);
                }

            }
        });
    }

}
