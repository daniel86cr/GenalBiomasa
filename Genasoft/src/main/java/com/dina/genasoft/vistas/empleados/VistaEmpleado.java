/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.empleados;

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
import com.dina.genasoft.utils.enums.EmpleadoEnum;
import com.dina.genasoft.utils.enums.RolesEnum;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
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
 * Vista para mostrar/visualizar un empleado.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaEmpleado.NAME)
public class VistaEmpleado extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;
    /** El nombre de la vista.*/
    public static final String            NAME     = "vEmpleado";
    /** Para los campos que componen un empleado.*/
    private BeanFieldGroup<TEmpleados>    binder;
    /** El boton para crear el empleado.*/
    private Button                        modificarButton;
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
    private static final org.slf4j.Logger log      = org.slf4j.LoggerFactory.getLogger(VistaEmpleado.class);
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
    /** La caja de texto para el código de acceso del empleado.*/
    private TextField                     txtCodAcceso;
    /** Los permisos del empleado actual. */
    private TPermisos                     permisos = null;
    /** El usuario que está logado. */
    private Integer                       user     = null;
    /** La fecha en que se inició sesión. */
    private Long                          time     = null;
    /** Contendrá los cambios que se aplican al empleado. */
    private String                        cambios;
    /** La caja de texto para el 'campo' nave del empleado de rol Operario control de PT.*/
    private ComboBox                      cbNaves;
    /** La caja de texto para el 'linea' del empleado de rol Operario control de PT.*/
    private ComboBox                      cbLinea;
    private TEmpleados                    empleado;

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(modificarButton)) {
            // Creamos el evento para modificar un nuevo empleado con los datos introducidos en el formulario
            try {
                if (validarCamposObligatorios()) {
                    Notification aviso = new Notification("Se debe informar los campos marcados con '*'", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                if (txtCorreoE.getValue() != null && !txtCorreoE.getValue().isEmpty()) {
                    if (!Utils.comprobarDireccionCorreo(txtCorreoE.getValue())) {
                        Notification aviso = new Notification("No se ha indicado una dirección de correo electrónico válida", Notification.Type.ASSISTIVE_NOTIFICATION);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        return;
                    }
                }

                if (txtCodAcceso.getValue() != null && !txtCodAcceso.getValue().trim().isEmpty()) {
                    if (txtCodAcceso.getValue().trim().length() < 3) {
                        Notification aviso = new Notification("El campo '" + txtCodAcceso.getCaption() + "' no tiene la longitud correcta, como mínimo debe ser 3 caracteres", Notification.Type.ASSISTIVE_NOTIFICATION);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        return;
                    }
                }

                // Construimos el objeto empleado a partir de los datos introducidos en el formulario.
                construirBean();
                String result = contrVista.modificarEmpleado(empNuevo, user, time);
                if (result.equals(Constants.OPERACION_OK)) {

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

                if (!Utils.booleanFromInteger(permisos.getModificarEmpleado())) {
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
                //El fieldgroup no es un componente

                // Obtenemos los roles activos.
                List<TRoles> roles = Utils.generarListaGenerica();

                String parametros = event.getParameters();

                if (parametros != null && !parametros.isEmpty()) {
                    empNuevo = contrVista.obtenerEmpleadoPorId(Integer.valueOf(parametros), user, time);
                } else {
                    parametros = null;
                }

                if (empNuevo == null) {
                    Notification aviso = new Notification("No se ha encontrado el empleado.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getUI().getNavigator().navigateTo(VistaListadoEmpleados.NAME);
                    return;
                }

                binder.setItemDataSource(empNuevo);

                // Obtenemos los roles activos.
                roles = contrVista.obtenerRolesActivosSinMaster(user, time);

                // Creamos los botones de la pantalla.
                crearBotones();

                // Creamos los componetes que conforman la pantalla.
                crearComponentes(parametros, roles);

                Label texto = new Label(empNuevo.getNombre());
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
                formulario1.addComponent(txtDni);
                formulario1.setComponentAlignment(txtDni, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtTelefono);
                formulario2.setComponentAlignment(txtTelefono, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtCorreoE);
                formulario2.setComponentAlignment(txtCorreoE, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(cbRoles);
                formulario2.setComponentAlignment(cbRoles, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(cbNaves);
                formulario2.setComponentAlignment(cbNaves, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(cbLinea);
                formulario2.setComponentAlignment(cbLinea, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(cbEstado);
                formulario2.setComponentAlignment(cbEstado, Alignment.MIDDLE_CENTER);
                body.addComponent(formulario1);
                body.setComponentAlignment(formulario1, Alignment.MIDDLE_LEFT);
                body.addComponent(formulario2);
                body.setComponentAlignment(formulario2, Alignment.MIDDLE_RIGHT);
                viewLayout.addComponent(body);
                viewLayout.addComponent(modificarButton);
                viewLayout.setComponentAlignment(modificarButton, Alignment.MIDDLE_CENTER);

                // Añadimos el logo del cliente
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
        modificarButton = new Button("Aplicar cambios", this);
        modificarButton.addStyleName("big");
    }

    /**
     * Método para crear los combos de la pantalla.
     */
    private void crearCombos() {
        // Combo box para el estado.
        cbEstado = new ComboBox();
        cbRoles = new ComboBox();

    }

    /**
     * Método que nos crea los componetes que conforman la pantalla.
     */
    private void crearComponentes(String nombre, List<TRoles> roles) {

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
        txtNombre.setEnabled(false);

        // El código de acceso
        txtCodAcceso = (TextField) binder.buildAndBind("Código de acceso:", "codigoAcceso");
        txtCodAcceso.setNullRepresentation("");
        txtCodAcceso.setWidth(appWidth, Sizeable.Unit.EM);
        txtCodAcceso.setMaxLength(245);

        // La contraseña
        txtPassword = (PasswordField) binder.buildAndBind("Contraseña:", "password", PasswordField.class);
        txtPassword.setNullRepresentation("");
        txtPassword.setRequired(true);
        txtPassword.setWidth(appWidth, Sizeable.Unit.EM);
        txtPassword.setMaxLength(245);
        if (empleado.getIdRol().equals(1) || empleado.getId().equals(empNuevo.getId())) {
            txtPassword.setEnabled(true);
        } else {
            if (empleado.getIdRol().equals(2) && empNuevo.getIdRol() > 2) {
                txtPassword.setEnabled(true);
            } else {
                txtPassword.setEnabled(false);
                txtPassword.addStyleName("visible");
                txtPassword.addStyleName("deshabilitado");
            }
        }

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

        // El campo nave
        cbNaves = new ComboBox("Nave: ");
        cbNaves.setRequired(true);
        cbNaves.addItem("Nave 1");
        cbNaves.addItem("Nave 2");
        cbNaves.addItem("Nave 3");
        cbNaves.setNullSelectionAllowed(false);
        cbNaves.setNewItemsAllowed(false);
        cbNaves.setWidth(appWidth, Sizeable.Unit.EM);
        cbNaves.setFilteringMode(FilteringMode.CONTAINS);
        cbNaves.setVisible(false);

        cbNaves.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                String val = (String) cbNaves.getValue();
                if (val.equals("Nave 1")) {
                    cbLinea.setVisible(true);
                    cbLinea.removeAllItems();
                    cbLinea.addItem("Línea 1");
                    cbLinea.addItem("Línea 2");
                } else if (val.equals("Nave 2")) {
                    cbLinea.setVisible(true);
                    cbLinea.removeAllItems();
                    cbLinea.addItem("Maduración");
                    cbLinea.addItem("Mango");
                    cbLinea.addItem("Repaso");
                    cbLinea.addItem("Flowpack 1");
                    cbLinea.addItem("Flowpack 2");
                    cbLinea.addItem("Malla");
                } else if (val.equals("Nave 3")) {
                    cbLinea.setVisible(true);
                    cbLinea.removeAllItems();
                    cbLinea.addItem("Malla manual");
                    cbLinea.addItem("Calibrador");
                    cbLinea.addItem("Mesa confección");
                    cbLinea.addItem("Flowpack 3");
                }
            }
        });

        // El campo línea
        cbLinea = new ComboBox("Línea: ");
        cbLinea.addItem("Línea 1");
        cbLinea.addItem("Línea 2");
        cbLinea.setRequired(true);
        cbLinea.setNullSelectionAllowed(false);
        cbLinea.setNewItemsAllowed(false);
        cbLinea.setWidth(appWidth, Sizeable.Unit.EM);
        cbLinea.setFilteringMode(FilteringMode.CONTAINS);
        cbLinea.setVisible(false);

        if (empNuevo.getNave() != null) {
            cbNaves.setValue(empNuevo.getNave());
        }
        if (empNuevo.getLinea() != null) {
            cbLinea.setValue(empNuevo.getLinea());
        }

        // Los roles        
        cbRoles.addItems(roles);
        cbRoles.setCaption("Rol:");
        cbRoles.setFilteringMode(FilteringMode.CONTAINS);
        cbRoles.setRequired(true);
        cbRoles.setNullSelectionAllowed(false);
        cbRoles.setNewItemsAllowed(false);
        cbRoles.setWidth(appWidth, Sizeable.Unit.EM);

        cbRoles.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                TRoles val = (TRoles) cbRoles.getValue();
                if (val.getId().equals(RolesEnum.OPERARIO_CONTROL_PT.getValue())) {
                    cbLinea.setVisible(true);
                    cbNaves.setVisible(true);
                } else {
                    cbLinea.setVisible(false);
                    cbNaves.setVisible(false);
                }
            }
        });

        if (empleado.getId().equals(empNuevo.getId())) {
            cbRoles.setEnabled(false);
        }
        for (TRoles rol : roles) {
            if (rol.getId().equals(empNuevo.getIdRol())) {
                cbRoles.setValue(rol);
                break;
            }
        }

        // Los estados.
        cbEstado.setCaption("Estado:");
        cbEstado.addItem("Activado");
        cbEstado.addItem("Desactivado");
        cbEstado.setValue(empNuevo.getEstado().equals(EmpleadoEnum.ACTIVO.getValue()) ? "Activado" : "Desactivado");
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
        Integer estado = cbEstado.getValue().equals("Activado") ? 1 : 0;
        if (!empNuevo.getEstado().equals(estado)) {
            cambios = "Se cambia el estado del empleado, de " + empNuevo.getEstado() + " a " + estado;
        }
        empNuevo.setEstado(cbEstado.getValue().equals("Activado") ? 1 : 0);
        String value = txtDni.getValue();
        if (value != null) {
            value = value.trim().toUpperCase();
            if (value != null && empNuevo.getDni() == null || value == null || value.isEmpty() && empNuevo.getDni() != null) {
                if (value == null || value.isEmpty()) {
                    cambios = cambios + " Se le quita el DNI al empleado. Antes tenia: " + empNuevo.getDni();
                } else {
                    cambios = cambios + " Se le asigna un nuevo DNI, antes no tenía: " + value;
                }
            }
        }
        if (value != null) {
            if (!value.equals(empNuevo.getDni())) {
                cambios = cambios + " Se cambia el DNI; Anterior: " + empNuevo.getDni() + "Nuevo: " + value.toUpperCase();
            }

        }
        empNuevo.setDni(txtDni.getValue() != null ? txtDni.getValue().trim().toUpperCase() : null);

        value = txtCodAcceso.getValue();
        if (value != null) {
            value = value.trim().toUpperCase();
            if (value != null && empNuevo.getCodigoAcceso() == null || value == null || value.isEmpty() && empNuevo.getCodigoAcceso() != null) {
                if (value == null || value.isEmpty()) {
                    cambios = cambios + " Se le quita el código de acceso al empleado. Antes tenia: " + empNuevo.getCodigoAcceso();
                } else {
                    cambios = cambios + " Se le asigna un nuevo código de acceso, antes no tenía: " + value;
                }
            }
        }
        if (value != null) {
            if (!value.equals(empNuevo.getCodigoAcceso())) {
                cambios = cambios + " Se cambia el código de acceso; Anterior: " + empNuevo.getCodigoAcceso() + "Nuevo: " + value.toUpperCase();
            }

        }
        empNuevo.setCodigoAcceso(value);

        value = txtCorreoE.getValue();
        if (value != null && empNuevo.getEmail() == null || value == null || value.isEmpty() && empNuevo.getEmail() != null) {
            if (value == null || value.isEmpty()) {
                cambios = cambios + " Se le quita el correo electrónico al empleado. Antes tenia: " + empNuevo.getEmail();
            } else {
                cambios = cambios + " Se le asigna un nuevo correo electrónico, antes no tenía: " + value;
            }
        }
        if (value != null) {
            if (!value.equals(empNuevo.getEmail())) {
                cambios = cambios + " Se cambia el correo electrónico; Anterior: " + empNuevo.getEmail() + "Nuevo: " + value;
            }

        }
        empNuevo.setEmail(txtCorreoE.getValue());
        empNuevo.setFechaModifica(Utils.generarFecha());
        value = txtIdExterno.getValue();
        if (value != null) {
            value = value.trim().toUpperCase();
            if (value != null && empNuevo.getIdExterno() == null || value == null || value.isEmpty() && empNuevo.getIdExterno() != null) {
                if (value == null || value.isEmpty()) {
                    cambios = cambios + " Se le quita el ID externo al empleado. Antes tenia: " + empNuevo.getIdExterno();
                } else {
                    cambios = cambios + " Se le asigna un nuevo ID Externo, antes no tenía: " + value;
                }
            }
            if (value != null) {
                if (!value.equals(empNuevo.getEmail())) {
                    cambios = cambios + " Se cambia el correo electrónico; Anterior: " + empNuevo.getIdExterno() + "Nuevo: " + value;
                }

            }
            value = txtIdExterno.getValue();
            if (value != null && empNuevo.getIdExterno() == null || value == null || value.isEmpty() && empNuevo.getIdExterno() != null) {
                if (value == null || value.isEmpty()) {
                    cambios = cambios + " Se le quita el ID externo al empleado. Antes tenia: " + empNuevo.getIdExterno();
                } else {
                    cambios = cambios + " Se le asigna un nuevo ID externo, antes no tenía: " + value;
                }
            }
            if (value != null) {
                if (!value.equals(empNuevo.getEmail())) {
                    cambios = cambios + " Se cambia el ID externo; Anterior: " + empNuevo.getIdExterno() + "Nuevo: " + value;
                }

            }
        }
        empNuevo.setIdExterno(txtIdExterno.getValue() != null && !txtIdExterno.getValue().trim().isEmpty() ? txtIdExterno.getValue().toString().trim().toUpperCase() : null);

        empNuevo.setNombre(txtNombre.getValue().toUpperCase());
        empNuevo.setNombreUsuario(txtNombreUsuario.getValue().toLowerCase());

        empNuevo.setIdRol(((TRoles) cbRoles.getValue()).getId());

        if (!empNuevo.getPassword().equals(txtPassword.getValue())) {
            cambios = cambios + " Se cambia la contraseña";
        }
        empNuevo.setPassword(txtPassword.getValue());
        if (txtTelefono.getValue() != null) {
            if (empNuevo.getTelefono() != null && !empNuevo.getTelefono().equals(txtTelefono.getValue())) {
                cambios = cambios + " Se le asigna un nuevo teléfono: " + txtTelefono.getValue() + "antes tenía: " + empNuevo.getTelefono();
            } else if (empNuevo.getTelefono() == null) {
                cambios = cambios + " Se le asigna un nuevo teléfono: " + txtTelefono.getValue() + "antes no tenía.";
            }
            empNuevo.setTelefono((Utils.formatearValorNumericoString(((String) txtTelefono.getValue()))));
        } else {
            if (empNuevo.getTelefono() != null) {
                cambios = cambios + " Se le quita el teléfono, antes tenía: " + empNuevo.getTelefono();
            }
            empNuevo.setTelefono(null);
        }
        empNuevo.setUsuModifica(user);

        if (cbNaves.isVisible()) {
            if (empNuevo.getNave() == null) {
                cambios = cambios + " Se le añade el campo Nave, antes no tenia, ahora pasa a tener: " + cbNaves.getValue().toString();
            } else {
                if (!empNuevo.getNave().equals(cbNaves.getValue().toString())) {
                    cambios = cambios + " Se le cambia el campo Nave, antes tenia: " + empNuevo.getNave() + ", ahora pasa a tener: " + cbNaves.getValue().toString();
                }
            }
            empNuevo.setNave(cbNaves.getValue().toString());
        } else {
            if (empNuevo.getNave() != null) {
                cambios = cambios + " Se le quita el campo Nave, antes tenía: " + empNuevo.getNave();
            }
            empNuevo.setNave(null);
        }

        if (cbLinea.isVisible()) {
            if (empNuevo.getLinea() == null) {
                cambios = cambios + " Se le añade el campo Línea, antes no tenia, ahora pasa a tener: " + cbLinea.getValue().toString();
            } else {
                if (!empNuevo.getLinea().equals(cbLinea.getValue().toString())) {
                    cambios = cambios + " Se le cambia el campo Línea, antes tenia: " + empNuevo.getLinea() + ", ahora pasa a tener: " + cbLinea.getValue().toString();
                }
            }
            empNuevo.setLinea(cbLinea.getValue().toString());
        } else {
            if (empNuevo.getLinea() != null) {
                cambios = cambios + " Se le quita el campo Línea, antes tenía: " + empNuevo.getLinea();
            }
            empNuevo.setLinea(null);
        }

    }

    /**
     * Comprobamos si ha informado los campos obligatorios.
     * @return true si no se cumple la validación
     */
    private boolean validarCamposObligatorios() {
        return !txtNombreUsuario.isValid() || !cbEstado.isValid() || !txtNombre.isValid() || !txtPassword.isValid() || !cbRoles.isValid();
    }

}
