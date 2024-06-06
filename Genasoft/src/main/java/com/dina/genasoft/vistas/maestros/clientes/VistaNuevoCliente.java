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
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TMateriales;
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TRegistrosCambiosClientes;
import com.dina.genasoft.db.entity.TTransportistas;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
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
    /** Combobox para los estados.*/
    private ComboBox                      cbEstado;
    /** Combobox para los materiales.*/
    private ComboBox                      cbMateriales;
    /** Combobox para los operadores.*/
    private ComboBox                      cbOperadores;
    /** Combobox para los materiales.*/
    private ComboBox                      cbTransportistas;
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
    private TextField                     txtMatricula;
    /** La caja de texto para el nombre.*/
    private TextField                     txtRemolque;
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
    /** Muestra la información relacionada con los operadores del cliente.*/
    private Button                        transportistasButton;
    /** Container que mostrará los campos relacionado con la información principal del cliente. */
    private VerticalLayout                infoPrincipal      = null;
    /** Container que mostrará los campos relacionado con la información de materiales del cliente. */
    private VerticalLayout                infoMateriales     = null;
    /** Container que mostrará los campos relacionado con la información de operadores del cliente. */
    private VerticalLayout                infoOperadores     = null;
    /** Container que mostrará los campos relacionado con la información de operadores del cliente. */
    private VerticalLayout                infoTransportistas = null;
    /** ListSelect para añadir los materiales para los materiales. */
    private ListSelect                    lsMateriales;
    /** ListSelect para añadir los materiales para los operadores. */
    private ListSelect                    lsOperadores;
    /** ListSelect para añadir los materiales para los transportistas. */
    private ListSelect                    lsTransportistas;
    /** Lista con los materiales activos del sistema. */
    private List<TMateriales>             lMateriales;
    /** Lista con los operadores activos del sistema. */
    private List<TOperadores>             lOperadores;
    /** Lista con los operadores activos del sistema. */
    private List<TTransportistas>         lTransportistas;
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

                // Construimos el objeto cliente a partir de los datos introducidos en el formulario.
                construirBean();
                Integer id = contrVista.crearClienteRetornaId(cliente, user, time);
                if (id > 0) {
                    String result = Constants.OPERACION_OK;
                    result = contrVista.obtenerDescripcionCodigo(result);
                    Notification aviso = new Notification(result, Notification.Type.HUMANIZED_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());

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

                // Obtenemos los materiales activos del sistema.
                lMateriales = contrVista.obtenerMaterialesActivos(user, time);

                // Obtenemos los operadores activos del sistema.
                lOperadores = contrVista.obtenerOperadoresActivos(user, time);

                // Obtenemos los transportistas activos del sistema.
                lTransportistas = contrVista.obtenerTransportistasActivos(user, time);

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
                crearComponentes();

                // Generamos las partes de la intefaz.

                generaInformacionPrincipal();
                generaInformacionMateriales();
                generaInformacioOperadores();
                generaInformacioTransportistas();

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
                    principalButton.addStyleName("down");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("default");
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
                    infoOperadores.setVisible(false);
                    infoTransportistas.setVisible(false);
                    principalButton.setStyleName("default");
                    materialesButton.setStyleName("down");
                    operadoresButton.setStyleName("default");
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
                    infoTransportistas.setVisible(false);
                    principalButton.setStyleName("default");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("down");
                    transportistasButton.setStyleName("default");

                }
            }
        });

        transportistasButton = new Button("Operadores");
        transportistasButton.addStyleName("default");
        transportistasButton.addStyleName("down");

        transportistasButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!infoTransportistas.isVisible()) {
                    infoTransportistas.setVisible(true);
                    infoOperadores.setVisible(false);
                    infoMateriales.setVisible(false);
                    infoPrincipal.setVisible(false);
                    principalButton.setStyleName("default");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("default");
                    transportistasButton.setStyleName("down");

                }
            }
        });

        botonesMenu.addComponent(principalButton);
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
        //Los campos que componen un empleado.

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
        cbEstado.setValue(Constants.ACTIVO);
        cbEstado.setFilteringMode(FilteringMode.CONTAINS);
        cbEstado.setRequired(true);
        cbEstado.setNullSelectionAllowed(false);
        cbEstado.setWidth(appWidth, Sizeable.Unit.EM);

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
    private void generaInformacioTransportistas() {
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
                    lsTransportistas.addItem((TTransportistas) cbTransportistas.getValue());
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

}
