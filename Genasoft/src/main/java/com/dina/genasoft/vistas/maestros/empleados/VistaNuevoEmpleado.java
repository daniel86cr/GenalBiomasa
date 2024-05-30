/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.maestros.empleados;

import java.util.List;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TRoles;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
import com.vaadin.annotations.Theme;
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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Daniel Carmona Romero
 * Vista para crear un nuevo empleado.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaNuevoEmpleado.NAME)
public class VistaNuevoEmpleado extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;
    /** El nombre de la vista.*/
    public static final String            NAME     = "nuevoEmpleado";
    /** Para los campos que componen un empleado.*/
    private BeanFieldGroup<TEmpleados>    binder;
    /** El boton para crear el empleado.*/
    private Button                        crearButton;
    /** Combobox para los estados.*/
    private ComboBox                      cbEstado;
    /** Combobox para los roles. */
    private ComboBox                      cbRoles;
    /** El empleado a crear.*/
    private TEmpleados                    empNuevo;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                       appWidth;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log      = org.slf4j.LoggerFactory.getLogger(VistaNuevoEmpleado.class);
    // Los campos obligatorios
    /** La caja de texto para el nombre de usuario.*/
    private TextField                     txtNombreUsuario;
    /** La caja de texto para el código externo de empleado.*/
    private TextField                     txtIdExterno;
    /** La caja de texto para el nombre.*/
    private TextField                     txtNombre;
    /** La caja de texto para el DNI.*/
    private TextField                     txtDni;
    /** La caja de texto para el teléfono.*/
    private TextField                     txtTelefono;
    /** La caja de texto para la contraseña.*/
    private PasswordField                 txtPassword;
    /** La caja de texto para el correo electrónico.*/
    private TextField                     txtCorreoE;
    /** Los permisos del empleado actual. */
    private TPermisos                     permisos = null;
    /** El usuario que está logado. */
    private Integer                       user     = null;
    /** La fecha en que se inició sesión. */
    private Long                          time     = null;
    /** La caja de texto para el código de acceso del empleado.*/
    private TextField                     txtCodAcceso;
    private TEmpleados                    empleado;
    private List<TRoles>                  roles;

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(crearButton)) {
            // Creamos el evento para crear un nuevo empleado con los datos introducidos en el formulario
            try {
                if (validarCamposObligatorios()) {
                    Notification aviso = new Notification("Se debe informar los campos marcados con '*'", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                if (txtCodAcceso.getValue() != null && !txtCodAcceso.getValue().trim().isEmpty()) {
                    if (txtCodAcceso.getValue().trim().length() < 3) {
                        Notification aviso = new Notification("El campo '" + txtCodAcceso.getCaption() + "' no tiene la longitud correcta, como mínimo debe ser 3 caracteres", Notification.Type.WARNING_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        return;
                    }
                }

                if (txtCorreoE.getValue() != null && !txtCorreoE.getValue().isEmpty()) {
                    if (!Utils.comprobarDireccionCorreo(txtCorreoE.getValue())) {
                        Notification aviso = new Notification("No se ha indicado una dirección de correo electrónico válida", Notification.Type.WARNING_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        return;
                    }
                }

                // Construimos el objeto empleado a partir de los datos introducidos en el formulario.
                construirBean();
                String result = contrVista.crearEmpleado(empNuevo, user, time);
                if (result.equals(Constants.OPERACION_OK)) {
                    result = contrVista.obtenerDescripcionCodigo(result);
                    Notification aviso = new Notification(result, Notification.Type.HUMANIZED_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    inicializarCampos();
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

                binder = new BeanFieldGroup<>(TEmpleados.class);

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

                // Creamos los botones de la pantalla.
                crearBotones();

                //El fieldgroup no es un componente
                binder.setItemDataSource(new TEmpleados());
                // Obtenemos los roles activos.
                roles = Utils.generarListaGenerica();

                // Obtenemos los roles activos.
                roles = contrVista.obtenerRolesActivosSinMaster(user, time);

                // Creamos los combos de la pantalla.
                crearCombos();

                // Creamos los botones de la pantalla.
                crearBotones();

                // Creamos los componetes que conforman la pantalla.
                crearComponentes();

                Label texto = new Label("Nuevo empleado");
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
                HorizontalLayout imgNaturSoft = contrVista.logoGenaSoft();

                viewLayout.addComponent(imgNaturSoft);
                viewLayout.setComponentAlignment(imgNaturSoft, Alignment.TOP_RIGHT);
                viewLayout.addComponent(titulo);
                viewLayout.setComponentAlignment(titulo, Alignment.TOP_CENTER);

                HorizontalLayout body = new HorizontalLayout();
                body.setSpacing(true);
                body.setMargin(true);

                // Formulario con los campos que componen el empleado.
                VerticalLayout formulario1 = new VerticalLayout();
                formulario1.setSpacing(true);
                // Formulario con los campos que componen el empleado.
                VerticalLayout formulario2 = new VerticalLayout();
                formulario2.setSpacing(true);

                formulario1.addComponent(txtNombreUsuario);
                formulario1.setComponentAlignment(txtNombreUsuario, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtIdExterno);
                formulario1.setComponentAlignment(txtIdExterno, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtCodAcceso);
                formulario1.setComponentAlignment(txtCodAcceso, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtNombre);
                formulario1.setComponentAlignment(txtNombre, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtPassword);
                formulario1.setComponentAlignment(txtPassword, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtDni);
                formulario2.setComponentAlignment(txtDni, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtTelefono);
                formulario2.setComponentAlignment(txtTelefono, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtCorreoE);
                formulario2.setComponentAlignment(txtCorreoE, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(cbRoles);
                formulario2.setComponentAlignment(cbRoles, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(cbEstado);
                formulario2.setComponentAlignment(cbEstado, Alignment.MIDDLE_CENTER);
                body.addComponent(formulario1);
                body.setComponentAlignment(formulario1, Alignment.MIDDLE_LEFT);
                body.addComponent(formulario2);
                body.setComponentAlignment(formulario2, Alignment.MIDDLE_RIGHT);
                viewLayout.addComponent(body);
                viewLayout.addComponent(crearButton);
                viewLayout.setComponentAlignment(crearButton, Alignment.MIDDLE_CENTER);

                // Añadimos el logo de GenalSoft
                viewLayout.addComponent(contrVista.logoCliente());
                setCompositionRoot(viewLayout);
                // Establecemos el porcentaje de ratio para los layouts
                viewLayout.setExpandRatio(titulo, 0.1f);
                viewLayout.setMargin(true);
                viewLayout.setSpacing(true);

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
        crearButton = new Button("Crear empleado", this);
        crearButton.addStyleName("big");

    }

    /**
     * Método para crear los combos de la pantalla.
     */
    private void crearCombos() {
        // Combo box para el estado.
        cbEstado = new ComboBox();
        cbEstado.addStyleName("big");
        cbRoles = new ComboBox();
        cbRoles.setNewItemsAllowed(false);
        cbRoles.addStyleName("big");

    }

    /**
     * Método que nos crea los componetes que conforman la pantalla.
     */
    private void crearComponentes() {
        //Los campos que componen un empleado.

        // El nombre con el que se hace login. 
        txtNombreUsuario = (TextField) binder.buildAndBind("Nombre usuario:", "nombreUsuario");
        txtNombreUsuario.setNullRepresentation("");
        txtNombreUsuario.setRequired(true);
        txtNombreUsuario.setWidth(appWidth, Sizeable.Unit.EM);
        txtNombreUsuario.setMaxLength(245);

        // El código de empleado.
        txtIdExterno = (TextField) binder.buildAndBind("ID externo:", "idExterno");
        txtIdExterno.setNullRepresentation("");
        txtIdExterno.setWidth(appWidth, Sizeable.Unit.EM);
        txtIdExterno.setMaxLength(245);

        // El nombre
        txtNombre = (TextField) binder.buildAndBind("Nombre empleado:", "nombre");
        txtNombre.setNullRepresentation("");
        txtNombre.setRequired(true);
        txtNombre.setWidth(appWidth, Sizeable.Unit.EM);
        txtNombre.setMaxLength(245);

        // La contraseña
        txtPassword = (PasswordField) binder.buildAndBind("Contraseña:", "password", PasswordField.class);
        txtPassword.setNullRepresentation("");
        txtPassword.setRequired(true);
        txtPassword.setWidth(appWidth, Sizeable.Unit.EM);
        txtPassword.setMaxLength(245);

        // El Correo electrónico.
        txtCorreoE = (TextField) binder.buildAndBind("Email:", "email", TextField.class);
        txtCorreoE.setNullRepresentation("");
        txtCorreoE.setWidth(appWidth, Sizeable.Unit.EM);
        txtCorreoE.setMaxLength(100);

        // El DNI.
        txtDni = (TextField) binder.buildAndBind("DNI:", "dni", TextField.class);
        txtDni.setNullRepresentation("");
        txtDni.setWidth(appWidth, Sizeable.Unit.EM);
        txtDni.setMaxLength(45);

        // El número de teléfono
        txtTelefono = (TextField) binder.buildAndBind("Telefono:", "telefono");
        txtTelefono.setNullRepresentation("");
        txtTelefono.setWidth(appWidth, Sizeable.Unit.EM);
        txtTelefono.setMaxLength(45);

        // El código de acceso
        txtCodAcceso = new TextField("Código acceso: ");
        txtCodAcceso.setNullRepresentation("");
        txtCodAcceso.setWidth(appWidth, Sizeable.Unit.EM);
        txtCodAcceso.setMaxLength(45);

        // Los roles        
        cbRoles.addItems(roles);
        cbRoles.setCaption("Rol:");
        cbRoles.setFilteringMode(FilteringMode.CONTAINS);
        cbRoles.setRequired(true);
        cbRoles.setNewItemsAllowed(false);
        cbRoles.setNullSelectionAllowed(false);
        cbRoles.setWidth(appWidth, Sizeable.Unit.EM);

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
        empNuevo = new TEmpleados();
        empNuevo.setEstado(cbEstado.getValue().equals(Constants.ACTIVO) ? 1 : 0);
        empNuevo.setDni(txtDni.getValue() != null ? txtDni.getValue().trim().toUpperCase() : null);
        empNuevo.setEmail(txtCorreoE.getValue());
        empNuevo.setFechaCrea(Utils.generarFecha());
        empNuevo.setIdExterno(txtIdExterno.getValue() != null && !txtIdExterno.getValue().trim().isEmpty() ? txtIdExterno.getValue().toString().trim().toUpperCase() : null);

        empNuevo.setNombre(txtNombre.getValue().trim().toUpperCase());
        empNuevo.setNombreUsuario(txtNombreUsuario.getValue().trim().toLowerCase());

        empNuevo.setPassword(txtPassword.getValue());
        if (txtTelefono.getValue() != null) {
            empNuevo.setTelefono((Utils.formatearValorString(((String) txtTelefono.getValue()))));
        } else {
            empNuevo.setTelefono(null);
        }
        empNuevo.setUsuCrea(user);
        empNuevo.setIdRol(((TRoles) cbRoles.getValue()).getId());
        empNuevo.setCodigoAcceso(txtCodAcceso.getValue() != null && !txtCodAcceso.getValue().trim().isEmpty() ? txtCodAcceso.getValue().trim().toLowerCase() : null);
        empNuevo.setPais("ES");

    }

    /**
     * Método que es llamado para inicializar los valores de los componentes.
     */
    private void inicializarCampos() {
        txtDni.setValue(null);
        txtNombreUsuario.setValue(null);
        txtNombre.setValue(null);
        txtPassword.setValue(null);
        txtCorreoE.setValue(null);
        txtTelefono.setValue(null);
        txtIdExterno.clear();
        cbEstado.setValue(Constants.ACTIVO);
        txtCodAcceso.setValue(null);
    }

    /**
     * Comprobamos si ha informado los campos obligatorios.
     * @return true si no se cumple la validación
     */
    private boolean validarCamposObligatorios() {
        return !txtNombreUsuario.isValid() || !cbEstado.isValid() || !txtNombre.isValid() || !txtPassword.isValid() || !cbRoles.isValid();
    }
}
