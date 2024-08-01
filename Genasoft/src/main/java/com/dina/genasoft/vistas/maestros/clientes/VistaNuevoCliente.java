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
import com.dina.genasoft.db.entity.TBancos;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TClientesMateriales;
import com.dina.genasoft.db.entity.TClientesOperadores;
import com.dina.genasoft.db.entity.TClientesTransportistas;
import com.dina.genasoft.db.entity.TClientesVehiculos;
import com.dina.genasoft.db.entity.TDireccionCliente;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TMateriales;
import com.dina.genasoft.db.entity.TOperacionActual;
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TRegistrosCambiosClientes;
import com.dina.genasoft.db.entity.TTransportistas;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.BancosEnum;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
import com.dina.genasoft.vistas.maestros.clientes.direcciones.VistaNuevaDireccionCliente;
import com.dina.genasoft.vistas.maestros.materiales.VistaNuevoMaterial;
import com.dina.genasoft.vistas.maestros.operadores.VistaNuevoOperador;
import com.dina.genasoft.vistas.maestros.transportistas.VistaNuevoTransportista;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

/**
 * @author Daniel Carmona Romero
 * Vista para crear un nuevo cliente.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaNuevoCliente.NAME)
public class VistaNuevoCliente extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;
    /** El nombre de la vista.*/
    public static final String            NAME               = "nuevoCliente";
    /** Para los campos que componen un cliente.*/
    private BeanFieldGroup<TClientes>     binder;
    /** El boton para crear el operador.*/
    private Button                        crearButton;
    /** El boton para crear el operador.*/
    private Button                        inlcuirMatriculaButton;
    /** El boton para crear el operador.*/
    private Button                        inlcuirRemolqueButton;
    /** Combobox para los estados.*/
    private ComboBox                      cbEstado;
    /** Combobox para los materiales.*/
    private ComboBox                      cbMateriales;
    /** Combobox para los operadores.*/
    private ComboBox                      cbOperadores;
    /** Combobox para los transportistas.*/
    private ComboBox                      cbTransportistas;
    /** Combobox para los bancos.*/
    private ComboBox                      cbBancos;
    /** El cliente a crear.*/
    private TClientes                     cliente;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                       appWidth;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log                = org.slf4j.LoggerFactory.getLogger(VistaNuevoCliente.class);
    // Los campos obligatorios
    /** La caja de texto para la referencia .*/
    private TextField                     txtRazonSocial;
    /** La caja de texto para el nombre.*/
    private TextField                     txtCif;
    /** La caja de texto para el nombre.*/
    private TextField                     txtDireccion;
    /** La caja de texto para el nombre.*/
    private TextField                     txtCp;
    /** La caja de texto para el nombre.*/
    private TextField                     txtPoblacion;
    /** La caja de texto para el nombre.*/
    private TextField                     txtProvincia;
    /** La caja de texto para el nombre.*/
    private TextField                     txtMatricula;
    /** La caja de texto para el nombre.*/
    private TextField                     txtRemolque;
    /** La caja de texto para el nombre.*/
    private TextField                     txtNumCuenta;
    /** Los permisos del empleado actual. */
    private TPermisos                     permisos           = null;
    /** El usuario que está logado. */
    private Integer                       user               = null;
    /** La fecha en que se inició sesión. */
    private Long                          time               = null;
    private TEmpleados                    empleado;
    /** Botones superiores. **/
    /** El botón principal del segmento. Muestra la información principal del cliente.*/
    private Button                        principalButton;
    /** Muestra la información relacionada con los materiales del cliente.*/
    private Button                        materialesButton;
    /** Muestra la información relacionada con los operadores del cliente.*/
    private Button                        operadoresButton;
    /** Muestra la información relacionada con los transportistas del cliente.*/
    private Button                        transportistasButton;
    /** Muestra la información relacionada con los vehiculos del cliente.*/
    private Button                        vehiculosButton;
    /** Container que mostrará los campos relacionado con la información principal del cliente. */
    private VerticalLayout                infoPrincipal      = null;
    /** Container que mostrará los campos relacionado con la información de materiales del cliente. */
    private VerticalLayout                infoMateriales     = null;
    /** Container que mostrará los campos relacionado con la información de operadores del cliente. */
    private VerticalLayout                infoOperadores     = null;
    /** Container que mostrará los campos relacionado con la información de operadores del cliente. */
    private VerticalLayout                infoTransportistas = null;
    /** Container que mostrará los campos relacionado con la información de los vehiculos del cliente. */
    private VerticalLayout                infoVehiculos      = null;
    /** ListSelect para añadir los materiales para los materiales. */
    private ListSelect                    lsMateriales;
    /** ListSelect para añadir los materiales para los operadores. */
    private ListSelect                    lsOperadores;
    /** ListSelect para añadir los materiales para los transportistas. */
    private ListSelect                    lsTransportistas;
    /** ListSelect para añadir los materiales para los transportistas. */
    private ListSelect                    lsMatriculas;
    /** ListSelect para añadir los materiales para los transportistas. */
    private ListSelect                    lsRemolques;
    /** Lista con los materiales activos del sistema. */
    private List<TMateriales>             lMateriales;
    /** Lista con los operadores activos del sistema. */
    private List<TOperadores>             lOperadores;
    /** Lista con los operadores activos del sistema. */
    private List<TTransportistas>         lTransportistas;
    /** Lista con los bancos activos del sistema. */
    private List<TBancos>                 lBancos;
    private String                        name;

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(crearButton)) {
            // Creamos el evento para crear un nuevo cliente con los datos introducidos en el formulario
            try {
                if (validarCamposObligatorios()) {
                    Notification aviso = new Notification("Se debe informar los campos marcados con '*'", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                if (lsMatriculas.size() < 1) {
                    Notification aviso = new Notification("Se debe informar al menos una matrícula ", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                // Construimos el objeto cliente a partir de los datos introducidos en el formulario.
                construirBean();
                Integer id = contrVista.crearClienteRetornaId(cliente, user, time);
                if (id > 0) {
                    String result = Constants.OPERACION_OK;
                    result = contrVista.obtenerDescripcionCodigo(result);
                    Notification aviso = new Notification(result, Notification.Type.HUMANIZED_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());

                    // Creamos la dirección
                    TDireccionCliente dir = new TDireccionCliente();
                    dir.setCodDireccion("Principal");
                    dir.setCodigoPostal(txtCp.getValue().trim().toUpperCase());
                    dir.setDireccion(txtDireccion.getValue().trim().toUpperCase());
                    dir.setEstado(1);
                    dir.setFechaCrea(Utils.generarFecha());
                    dir.setIdCliente(id);
                    dir.setPais("ES");
                    dir.setPoblacion(txtPoblacion.getValue().trim().toUpperCase());
                    dir.setProvincia(txtProvincia.getValue().trim().toUpperCase());
                    dir.setUsuCrea(user);
                    contrVista.crearDireccionCliente(dir, user, time);

                    // Asociamos los operadores que haya seleccionado
                    @SuppressWarnings("unchecked")
                    List<TOperadores> lOperadoresAsig = (List<TOperadores>) lsOperadores.getItemIds();
                    TClientesOperadores clOp = null;
                    TRegistrosCambiosClientes record = null;
                    for (TOperadores op : lOperadoresAsig) {
                        clOp = new TClientesOperadores();
                        clOp.setEstado(1);
                        clOp.setFechaCrea(Utils.generarFecha());
                        clOp.setIdCliente(id);
                        clOp.setIdOperador(op.getId());
                        clOp.setUsuCrea(user);
                        contrVista.asignarOperadorCliente(clOp, user, time);

                        record = new TRegistrosCambiosClientes();
                        record.setCambio("Se le asigna el operador: " + op.getNombre());
                        record.setFechaCambio(Utils.generarFecha());
                        record.setIdCliente(id);
                        record.setUsuCrea(user);

                        contrVista.crearRegistroCambioCliente(record, user, time);
                    }

                    // Asociamos los transportistas que haya seleccionado
                    @SuppressWarnings("unchecked")
                    List<TTransportistas> lTransportistasAsignados = (List<TTransportistas>) lsTransportistas.getItemIds();
                    TClientesTransportistas clTrans = null;
                    for (TTransportistas tr : lTransportistasAsignados) {
                        clTrans = new TClientesTransportistas();
                        clTrans.setEstado(1);
                        clTrans.setFechaCrea(Utils.generarFecha());
                        clTrans.setIdCliente(id);
                        clTrans.setIdTransportista(tr.getId());
                        clTrans.setUsuCrea(user);
                        contrVista.asignarTransportistaCliente(clTrans, user, time);

                        record = new TRegistrosCambiosClientes();
                        record.setCambio("Se le asigna el transportista: " + tr.getNombre());
                        record.setFechaCambio(Utils.generarFecha());
                        record.setIdCliente(id);
                        record.setUsuCrea(user);

                        contrVista.crearRegistroCambioCliente(record, user, time);
                    }

                    // Asociamos los materiales que haya seleccionado
                    @SuppressWarnings("unchecked")
                    List<TMateriales> lMaterialesAsig = (List<TMateriales>) lsMateriales.getItemIds();
                    TClientesMateriales clMat = null;
                    TRegistrosCambiosClientes record2 = null;
                    for (TMateriales mat : lMaterialesAsig) {
                        clMat = new TClientesMateriales();
                        clMat.setEstado(1);
                        clMat.setFechaCrea(Utils.generarFecha());
                        clMat.setIdCliente(id);
                        clMat.setIdMaterial(mat.getId());
                        clMat.setIva(mat.getIva());
                        clMat.setPrecioKg(mat.getPrecio());
                        clMat.setUsuCrea(user);
                        contrVista.asignarMaterialCliente(clMat, user, time);

                        record2 = new TRegistrosCambiosClientes();
                        record2.setCambio("Se le asigna el material: " + mat.getDescripcion());
                        record2.setFechaCambio(Utils.generarFecha());
                        record2.setIdCliente(id);
                        record2.setUsuCrea(user);

                        contrVista.crearRegistroCambioCliente(record2, user, time);
                    }

                    // Asociamos las matrículas introducidas
                    @SuppressWarnings("unchecked")
                    List<String> lMatriculas = (List<String>) lsMatriculas.getItemIds();
                    TClientesVehiculos clVeh = null;
                    for (String matricula : lMatriculas) {
                        clVeh = new TClientesVehiculos();
                        clVeh.setEstado(1);
                        clVeh.setFechaCrea(Utils.generarFecha());
                        clVeh.setIdCliente(id);
                        clVeh.setMatricula(matricula);
                        clVeh.setTipo(1);
                        clVeh.setUsuCrea(user);
                        contrVista.asignarVehiculoCliente(clVeh, user, time);

                        record2 = new TRegistrosCambiosClientes();
                        record2.setCambio("Se le asigna la matrícula: " + matricula);
                        record2.setFechaCambio(Utils.generarFecha());
                        record2.setIdCliente(id);
                        record2.setUsuCrea(user);

                        contrVista.crearRegistroCambioCliente(record2, user, time);
                    }

                    // Asociamos los remolques introducidos
                    @SuppressWarnings("unchecked")
                    List<String> lRemolques = (List<String>) lsRemolques.getItemIds();
                    clVeh = null;
                    for (String matricula : lRemolques) {
                        clVeh = new TClientesVehiculos();
                        clVeh.setEstado(1);
                        clVeh.setFechaCrea(Utils.generarFecha());
                        clVeh.setIdCliente(id);
                        clVeh.setMatricula(matricula);
                        clVeh.setTipo(2);
                        clVeh.setUsuCrea(user);
                        contrVista.asignarVehiculoCliente(clVeh, user, time);

                        record2 = new TRegistrosCambiosClientes();
                        record2.setCambio("Se le asigna el remolque: " + matricula);
                        record2.setFechaCambio(Utils.generarFecha());
                        record2.setIdCliente(id);
                        record2.setUsuCrea(user);

                        contrVista.crearRegistroCambioCliente(record2, user, time);
                    }

                    inicializarCampos();

                    MessageBox.createQuestion().withCaption(appName).withMessage("¿Quieres crear una dirección?").withNoButton().withYesButton(() ->

                    getUI().getNavigator().navigateTo(VistaNuevaDireccionCliente.NAME + "/" + id), ButtonOption.caption("Sí")).open();

                } else {
                    String result = contrVista.obtenerDescripcionCodigo(Constants.BD_KO_CREA_CLIENTE);
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

        } else if (event.getButton().equals(inlcuirMatriculaButton)) {
            if (txtMatricula.getValue() != null && !txtMatricula.getValue().trim().isEmpty()) {
                lsMatriculas.addItem(txtMatricula.getValue().trim().toUpperCase());
                txtMatricula.setValue(null);
            }
        } else if (event.getButton().equals(inlcuirRemolqueButton)) {
            if (txtRemolque.getValue() != null && !txtRemolque.getValue().trim().isEmpty()) {
                lsRemolques.addItem(txtRemolque.getValue().trim().toUpperCase());
                txtRemolque.setValue(null);
            }
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

                String parametros = event.getParameters();

                if (parametros != null && !parametros.isEmpty()) {
                    if (parametros.split(",").length == 2) {
                        parametros = parametros.split(",")[1];
                        if (parametros.contains("_")) {
                            parametros = parametros.replaceAll("_", " ");
                        }
                    } else {
                        parametros = null;
                    }
                } else {
                    parametros = null;
                }

                // Obtenemos los materiales activos del sistema.
                lMateriales = contrVista.obtenerMaterialesActivos(user, time);

                // Obtenemos los operadores activos del sistema.
                lOperadores = contrVista.obtenerOperadoresActivos(user, time);

                // Obtenemos los transportistas activos del sistema.
                lTransportistas = contrVista.obtenerTransportistasActivos(user, time);

                // Obtenemos los bancos activos del sistema
                lBancos = contrVista.obtenerBancosActivos(user, time);

                // Creamos los botones de la pantalla.
                crearBotones();

                //El fieldgroup no es un componente
                binder.setItemDataSource(new TClientes());

                // Creamos los combos de la pantalla.
                crearCombos();

                // Creamos los botones de la pantalla.
                crearBotones();

                // Creamos los botones superiores
                crearBotonesMenu();

                // Creamos los componetes que conforman la pantalla.
                crearComponentes(parametros);

                // Generamos las partes de la intefaz.

                generaInformacionPrincipal();
                generaInformacionMateriales();
                generaInformacioOperadores();
                generaInformacionTransportistas();
                generaInformacionVehiculos();

                Label texto = new Label("Nuevo cliente");
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

                viewLayout.addComponent(crearBotonesMenu());
                viewLayout.addComponent(infoPrincipal);
                viewLayout.setComponentAlignment(infoPrincipal, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(infoMateriales);
                viewLayout.setComponentAlignment(infoMateriales, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(infoOperadores);
                viewLayout.setComponentAlignment(infoOperadores, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(infoTransportistas);
                viewLayout.setComponentAlignment(infoTransportistas, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(infoVehiculos);
                viewLayout.setComponentAlignment(infoVehiculos, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(crearButton);
                viewLayout.setComponentAlignment(crearButton, Alignment.MIDDLE_CENTER);

                // Añadimos el logo de GenalSoft
                viewLayout.addComponent(contrVista.logoCliente());
                setCompositionRoot(viewLayout);
                // Establecemos el porcentaje de ratio para los layouts
                viewLayout.setExpandRatio(titulo, 0.1f);
                viewLayout.setMargin(true);
                viewLayout.setSpacing(true);

                infoPrincipal.setVisible(true);
                infoMateriales.setVisible(false);
                infoOperadores.setVisible(false);
                infoTransportistas.setVisible(false);
                infoVehiculos.setVisible(false);

                // Guardamos la operación en BD.
                TOperacionActual record = new TOperacionActual();
                record.setFecha(Utils.generarFecha());
                record.setIdEmpleado(user);
                record.setIdEntidad(-1);
                record.setPantalla(NAME);
                contrVista.registrarOperacionEmpleado(record, user, time);

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
        crearButton = new Button("Crear cliente", this);
        crearButton.addStyleName("big");

        inlcuirMatriculaButton = new Button("+", this);
        inlcuirMatriculaButton.addStyleName("big");

        inlcuirRemolqueButton = new Button("+", this);
        inlcuirRemolqueButton.addStyleName("big");

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

        // Infiormación principal
        principalButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!infoPrincipal.isVisible()) {
                    infoPrincipal.setVisible(true);
                    infoOperadores.setVisible(false);
                    infoMateriales.setVisible(false);
                    infoTransportistas.setVisible(false);
                    infoVehiculos.setVisible(false);
                    principalButton.addStyleName("down");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("default");
                    transportistasButton.setStyleName("default");
                    vehiculosButton.setStyleName("default");
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
                    infoOperadores.setVisible(false);
                    infoTransportistas.setVisible(false);
                    infoVehiculos.setVisible(false);
                    principalButton.setStyleName("default");
                    materialesButton.setStyleName("down");
                    operadoresButton.setStyleName("default");
                    transportistasButton.setStyleName("default");
                    vehiculosButton.setStyleName("default");
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
                    infoTransportistas.setVisible(false);
                    infoVehiculos.setVisible(false);
                    principalButton.setStyleName("default");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("down");
                    transportistasButton.setStyleName("default");
                    vehiculosButton.setStyleName("default");

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
                    infoVehiculos.setVisible(false);
                    principalButton.setStyleName("default");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("default");
                    transportistasButton.setStyleName("down");
                    vehiculosButton.setStyleName("default");

                }
            }
        });

        vehiculosButton = new Button("Vehículos");
        vehiculosButton.addStyleName("default");
        vehiculosButton.addStyleName("down");

        vehiculosButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!infoVehiculos.isVisible()) {
                    infoVehiculos.setVisible(true);
                    infoOperadores.setVisible(false);
                    infoMateriales.setVisible(false);
                    infoPrincipal.setVisible(false);
                    infoTransportistas.setVisible(false);
                    principalButton.setStyleName("default");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("default");
                    transportistasButton.setStyleName("default");
                    vehiculosButton.setStyleName("down");

                }
            }
        });

        botonesMenu.addComponent(principalButton);
        botonesMenu.addComponent(vehiculosButton);
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

        cbBancos = new ComboBox();
        cbBancos.addStyleName("big");
    }

    /**
     * Método que nos crea los componetes que conforman la pantalla.
     */
    private void crearComponentes(String nombre) {
        //Los campos que componen un empleado.

        // La razón social.
        txtRazonSocial = (TextField) binder.buildAndBind("Razón social:", "nombre");
        txtRazonSocial.setNullRepresentation("");
        txtRazonSocial.setWidth(appWidth, Sizeable.Unit.EM);
        txtRazonSocial.setMaxLength(445);
        txtRazonSocial.setRequired(true);
        if (nombre != null) {
            txtRazonSocial.setValue(nombre);
        }

        // El CIF
        txtCif = (TextField) binder.buildAndBind("CIF:", "cif");
        txtCif.setNullRepresentation("");
        txtCif.setRequired(true);
        txtCif.setWidth(appWidth, Sizeable.Unit.EM);
        txtCif.setMaxLength(45);

        // La dirección
        txtDireccion = new TextField("Dirección: ");
        txtDireccion.setNullRepresentation("");
        txtDireccion.setRequired(true);
        txtDireccion.setWidth(appWidth, Sizeable.Unit.EM);
        txtDireccion.setMaxLength(445);

        // El CP
        txtCp = new TextField("CP: ");
        txtCp.setNullRepresentation("");
        txtCp.setRequired(true);
        txtCp.setWidth(appWidth, Sizeable.Unit.EM);
        txtCp.setMaxLength(445);

        // La población
        txtPoblacion = new TextField("Población: ");
        txtPoblacion.setNullRepresentation("");
        txtPoblacion.setRequired(true);
        txtPoblacion.setWidth(appWidth, Sizeable.Unit.EM);
        txtPoblacion.setMaxLength(445);

        // La provincia
        txtProvincia = new TextField("Provincia: ");
        txtProvincia.setNullRepresentation("");
        txtProvincia.setRequired(true);
        txtProvincia.setWidth(appWidth, Sizeable.Unit.EM);
        txtProvincia.setMaxLength(445);

        // La matrícula
        txtMatricula = (TextField) binder.buildAndBind("Matrícula:", "matricula");
        txtMatricula.setNullRepresentation("");
        txtMatricula.setRequired(true);
        txtMatricula.setWidth(appWidth, Sizeable.Unit.EM);
        txtMatricula.setMaxLength(45);

        // Remolque
        txtRemolque = (TextField) binder.buildAndBind("Remolque:", "remolque");
        txtRemolque.setNullRepresentation("");
        txtRemolque.setWidth(appWidth, Sizeable.Unit.EM);
        txtRemolque.setMaxLength(45);

        // Los estados.
        cbEstado.setCaption("Estado:");
        cbEstado.addItem(Constants.ACTIVO);
        cbEstado.addItem(Constants.DESACTIVADO);
        cbEstado.setValue(Constants.ACTIVO);
        cbEstado.setFilteringMode(FilteringMode.CONTAINS);
        cbEstado.setRequired(true);
        cbEstado.setNullSelectionAllowed(false);
        cbEstado.setWidth(appWidth, Sizeable.Unit.EM);

        // El banco
        cbBancos.setCaption("Banco:");
        cbBancos.setFilteringMode(FilteringMode.CONTAINS);
        cbBancos.setNullSelectionAllowed(true);
        cbBancos.setWidth(appWidth, Sizeable.Unit.EM);
        cbBancos.addItems(lBancos);
        cbBancos.setNewItemsAllowed(true);

        // El número de cuenta
        txtNumCuenta = (TextField) binder.buildAndBind("Número de cuenta:", "numCuenta");
        txtNumCuenta.setNullRepresentation("");
        txtNumCuenta.setWidth(appWidth, Sizeable.Unit.EM);
        txtNumCuenta.setMaxLength(245);

    }

    /**
     * Método que es llamado para crear el objeto bean para la creación.
     */
    private void construirBean() throws GenasoftException {
        cliente = new TClientes();
        cliente.setEstado(cbEstado.getValue().equals(Constants.ACTIVO) ? 1 : 0);
        cliente.setNombre(txtRazonSocial.getValue().trim().toUpperCase());
        cliente.setCif(txtCif.getValue().trim().toUpperCase());
        cliente.setMatricula(txtMatricula.getValue() != null ? txtMatricula.getValue().trim().toUpperCase() : "");
        cliente.setRemolque(txtRemolque.getValue() != null ? txtRemolque.getValue().trim().toUpperCase() : "");
        cliente.setRazonSocial(txtRazonSocial.getValue().trim().toUpperCase());
        cliente.setFechaCrea(Utils.generarFecha());
        cliente.setUsuCrea(user);
        cliente.setFechaCrea(Utils.generarFecha());
        cliente.setNumCuenta(txtNumCuenta.getValue() != null ? txtNumCuenta.getValue().trim() : "");

        Object val = cbBancos.getValue();
        if (val != null) {
            if (val.getClass().equals(TBancos.class)) {
                cliente.setIdBanco(((TBancos) val).getId());
            } else {
                String nombre = val.toString();
                // Buscamos el banco por si existe
                TBancos b = contrVista.obtenerBancoPorNombre(nombre.trim().toUpperCase(), user, time);
                if (b == null) {
                    b = new TBancos();
                    b.setEstado(BancosEnum.ACTIVO.getValue());
                    b.setNombre(nombre.trim().toUpperCase());
                    cliente.setIdBanco(contrVista.crearBancoRetornaId(b, user, time));
                } else {
                    cliente.setIdBanco(b.getId());
                }
            }
        }
    }

    /**
     * Método que es llamado para inicializar los valores de los componentes.
     */
    private void inicializarCampos() {
        txtCif.setValue(null);
        txtRazonSocial.setValue(null);
        cbEstado.setValue(Constants.ACTIVO);
        txtRemolque.setValue(null);
        txtMatricula.setValue(null);
        cbBancos.clear();
        lsMatriculas.removeAllItems();
        lsRemolques.removeAllItems();
    }

    /**
     * Comprobamos si ha informado los campos obligatorios.
     * @return true si no se cumple la validación
     */
    private boolean validarCamposObligatorios() {
        return !cbEstado.isValid() || !txtCif.isValid() || !txtRazonSocial.isValid() || !txtDireccion.isValid() || !txtCp.isValid() || !txtPoblacion.isValid() || !txtProvincia.isValid();
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
        formulario1.addComponent(txtDireccion);
        formulario1.setComponentAlignment(txtDireccion, Alignment.MIDDLE_CENTER);
        formulario1.addComponent(txtPoblacion);
        formulario1.setComponentAlignment(txtPoblacion, Alignment.MIDDLE_CENTER);
        formulario1.addComponent(txtCp);
        formulario1.setComponentAlignment(txtCp, Alignment.MIDDLE_CENTER);
        formulario1.addComponent(txtProvincia);
        formulario1.setComponentAlignment(txtProvincia, Alignment.MIDDLE_CENTER);
        formulario1.addComponent(cbBancos);
        formulario1.setComponentAlignment(cbBancos, Alignment.MIDDLE_CENTER);
        formulario1.addComponent(txtNumCuenta);
        formulario1.setComponentAlignment(txtNumCuenta, Alignment.MIDDLE_CENTER);
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
        cbMateriales.setWidth(appWidth, Sizeable.Unit.EM);
        cbMateriales.addItems(lMateriales);

        cbMateriales.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbMateriales.getValue() != null) {
                    if (cbMateriales.getValue().getClass().equals(TMateriales.class)) {
                        lsMateriales.addItem((TMateriales) cbMateriales.getValue());
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
        lsMateriales.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsMateriales.getValue() != null) {
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

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbOperadores.getValue() != null) {
                    if (cbOperadores.getValue().getClass().equals(TOperadores.class)) {
                        lsOperadores.addItem((TOperadores) cbOperadores.getValue());
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
        lsOperadores.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsOperadores.getValue() != null) {
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
    private void generaInformacionTransportistas() {
        infoTransportistas = new VerticalLayout();
        infoTransportistas.setSpacing(true);
        infoTransportistas.setMargin(true);

        HorizontalLayout body = new HorizontalLayout();
        body.setSpacing(true);
        body.setMargin(true);

        cbTransportistas.setCaption("Transportistas:");
        cbTransportistas.setFilteringMode(FilteringMode.CONTAINS);
        cbTransportistas.setNullSelectionAllowed(false);
        cbTransportistas.setWidth(appWidth, Sizeable.Unit.EM);
        cbTransportistas.addItems(lTransportistas);

        cbTransportistas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbTransportistas.getValue() != null) {
                    if (cbTransportistas.getValue().getClass().equals(TTransportistas.class)) {
                        lsTransportistas.addItem((TTransportistas) cbTransportistas.getValue());
                        cbTransportistas.clear();
                    } else {
                        try {
                            name = (String) cbTransportistas.getValue();
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
        lsTransportistas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsTransportistas.getValue() != null) {
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

    /**
     * Método que nos crea la parte de la interfaz con la información principal de los clientes
     */
    private void generaInformacionVehiculos() {
        infoVehiculos = new VerticalLayout();
        infoVehiculos.setSpacing(true);
        infoVehiculos.setMargin(true);

        HorizontalLayout body1 = new HorizontalLayout();
        body1.setSpacing(true);
        body1.setMargin(true);

        HorizontalLayout body2 = new HorizontalLayout();
        body2.setSpacing(true);
        body2.setMargin(true);

        txtMatricula = new TextField("Matrícula: ");
        txtMatricula.setNullRepresentation("");
        txtMatricula.setWidth(appWidth, Sizeable.Unit.EM);
        txtMatricula.setMaxLength(245);
        txtMatricula.setRequired(true);

        txtRemolque = new TextField("Remolque: ");
        txtRemolque.setNullRepresentation("");
        txtRemolque.setWidth(appWidth, Sizeable.Unit.EM);
        txtRemolque.setMaxLength(245);
        txtRemolque.setRequired(true);

        lsMatriculas = new ListSelect("Matrículas asignadas");
        lsMatriculas.setWidth(appWidth, Sizeable.Unit.EM);
        lsMatriculas.removeAllItems();
        lsMatriculas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsMatriculas.getValue() != null) {
                    lsMatriculas.removeItem(lsMatriculas.getValue());
                }
            }
        });

        lsRemolques = new ListSelect("Remolques asignados");
        lsRemolques.setWidth(appWidth, Sizeable.Unit.EM);
        lsRemolques.removeAllItems();
        lsRemolques.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsRemolques.getValue() != null) {
                    lsRemolques.removeItem(lsRemolques.getValue());
                }
            }
        });

        body1.addComponent(txtMatricula);
        body1.setComponentAlignment(txtMatricula, Alignment.MIDDLE_CENTER);
        body1.addComponent(inlcuirMatriculaButton);
        body1.setComponentAlignment(inlcuirMatriculaButton, Alignment.MIDDLE_CENTER);
        body1.addComponent(lsMatriculas);
        body1.setComponentAlignment(lsMatriculas, Alignment.MIDDLE_CENTER);

        body2.addComponent(txtRemolque);
        body2.setComponentAlignment(txtRemolque, Alignment.MIDDLE_CENTER);
        body2.addComponent(inlcuirRemolqueButton);
        body2.setComponentAlignment(inlcuirRemolqueButton, Alignment.MIDDLE_CENTER);
        body2.addComponent(lsRemolques);
        body2.setComponentAlignment(lsRemolques, Alignment.MIDDLE_CENTER);

        infoVehiculos.addComponent(body1);
        infoVehiculos.setComponentAlignment(body1, Alignment.MIDDLE_CENTER);
        infoVehiculos.addComponent(body2);
        infoVehiculos.setComponentAlignment(body2, Alignment.MIDDLE_CENTER);
    }

}
