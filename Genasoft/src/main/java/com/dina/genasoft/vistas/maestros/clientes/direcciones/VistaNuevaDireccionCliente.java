/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.maestros.clientes.direcciones;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TDireccionCliente;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TOperacionActual;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
import com.dina.genasoft.vistas.maestros.clientes.VistaCliente;
import com.dina.genasoft.vistas.maestros.clientes.VistaListadoClientes;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Daniel Carmona Romero
 * Vista para crear un nuevo operador.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaNuevaDireccionCliente.NAME)
public class VistaNuevaDireccionCliente extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas                 contrVista;
    /** El nombre de la vista.*/
    public static final String                NAME     = "vNuevaDireccion";
    /** Para los campos que componen un operador.*/
    private BeanFieldGroup<TDireccionCliente> binder;
    /** El boton para crear el operador.*/
    private Button                            crearButton;
    /** El boton para volver al cliente.*/
    private Button                            listadoButton;
    /** Combobox para los estados.*/
    private ComboBox                          cbEstado;
    /** La dirección a crear.*/
    private TDireccionCliente                 direccion;
    /** El cliente para añadirle la nueva dirección.*/
    private TClientes                         cliente;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                            appName;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                           appWidth;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger     log      = org.slf4j.LoggerFactory.getLogger(VistaNuevaDireccionCliente.class);
    // Los campos obligatorios
    /** La caja de texto para la referencia .*/
    private TextField                         txtCodDireccion;
    /** La caja de texto para la dirección.*/
    private TextField                         txtDireccion;
    /** La caja de texto para el precio.*/
    private TextField                         txtCiudad;
    /** La caja de texto para el precio.*/
    private TextField                         txtCp;
    /** La caja de texto para el precio.*/
    private TextField                         txtProvincia;
    /** La caja de texto para el precio.*/
    private TextField                         txtPais;
    /** Los permisos del empleado actual. */
    private TPermisos                         permisos = null;
    /** El usuario que está logado. */
    private Integer                           user     = null;
    /** La fecha en que se inició sesión. */
    private Long                              time     = null;
    private TEmpleados                        empleado;

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(crearButton)) {
            // Creamos el evento para crear un nuevo operador con los datos introducidos en el formulario
            try {
                if (validarCamposObligatorios()) {
                    Notification aviso = new Notification("Se debe informar los campos marcados con '*'", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                // Construimos el objeto operador a partir de los datos introducidos en el formulario.
                construirBean();
                String result = contrVista.crearDireccionCliente(direccion, user, time);
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

        } else if (event.getButton().equals(listadoButton)) {
            getUI().getNavigator().navigateTo(VistaCliente.NAME + "/" + cliente.getId());
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
            String parametros = event.getParameters();

            try {

                if (parametros != null && !parametros.isEmpty()) {
                    try {
                        if (parametros.contains(",")) {
                            cliente = contrVista.obtenerClientePorId(Integer.valueOf(parametros.split(",")[0]), user, time);
                        } else {
                            cliente = contrVista.obtenerClientePorId(Integer.valueOf(parametros), user, time);
                        }
                    } catch (NumberFormatException nfe) {
                        throw new GenasoftException("No se ha encontrado al cliente seleccionado.");
                    }
                } else {
                    Notification aviso = new Notification("No se ha encontrado el cliente", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getUI().getNavigator().navigateTo(VistaListadoClientes.NAME);
                    return;
                }

                if (cliente == null) {
                    Notification aviso = new Notification("No se ha encontrado el cliente", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getUI().getNavigator().navigateTo(VistaListadoClientes.NAME);
                    return;
                }

                binder = new BeanFieldGroup<>(TDireccionCliente.class);

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

                if (!Utils.booleanFromInteger(permisos.getModificarCliente())) {
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
                binder.setItemDataSource(new TDireccionCliente());

                // Creamos los combos de la pantalla.
                crearCombos();

                // Creamos los botones de la pantalla.
                crearBotones();

                // Creamos los componetes que conforman la pantalla.
                crearComponentes();

                Label texto = new Label("Nueva dirección cliente");
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
                viewLayout.addComponent(listadoButton);

                HorizontalLayout body = new HorizontalLayout();
                body.setSpacing(true);
                body.setMargin(true);

                // Formulario con los campos que componen el empleado.
                VerticalLayout formulario1 = new VerticalLayout();
                formulario1.setSpacing(true);

                formulario1.addComponent(txtCodDireccion);
                formulario1.setComponentAlignment(txtCodDireccion, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtDireccion);
                formulario1.setComponentAlignment(txtDireccion, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtCp);
                formulario1.setComponentAlignment(txtCp, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtCiudad);
                formulario1.setComponentAlignment(txtCiudad, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtProvincia);
                formulario1.setComponentAlignment(txtProvincia, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtPais);
                formulario1.setComponentAlignment(txtPais, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(cbEstado);
                formulario1.setComponentAlignment(cbEstado, Alignment.MIDDLE_CENTER);
                body.addComponent(formulario1);
                body.setComponentAlignment(formulario1, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(body);
                viewLayout.setComponentAlignment(body, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(crearButton);
                viewLayout.setComponentAlignment(crearButton, Alignment.MIDDLE_CENTER);

                // Añadimos el logo de GenalSoft
                viewLayout.addComponent(contrVista.logoCliente());
                setCompositionRoot(viewLayout);
                // Establecemos el porcentaje de ratio para los layouts
                viewLayout.setExpandRatio(titulo, 0.1f);
                viewLayout.setMargin(true);
                viewLayout.setSpacing(true);

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
        crearButton = new Button("Crear dirección", this);
        crearButton.addStyleName("big");

        listadoButton = new Button("Volver al cliente", this);
        crearButton.addStyleName("big");

    }

    /**
     * Método para crear los combos de la pantalla.
     */
    private void crearCombos() {
        // Combo box para el estado.
        cbEstado = new ComboBox();
        cbEstado.addStyleName("big");
    }

    /**
     * Método que nos crea los componetes que conforman la pantalla.
     */
    private void crearComponentes() {
        //Los campos que componen un empleado.

        // La razón social.
        txtCodDireccion = (TextField) binder.buildAndBind("Código dirección:", "codDireccion");
        txtCodDireccion.setNullRepresentation("");
        txtCodDireccion.setWidth(appWidth, Sizeable.Unit.EM);
        txtCodDireccion.setMaxLength(445);
        txtCodDireccion.setRequired(true);

        // Dirección
        txtDireccion = (TextField) binder.buildAndBind("Dirección:", "direccion");
        txtDireccion.setNullRepresentation("");
        txtDireccion.setRequired(true);
        txtDireccion.setWidth(appWidth, Sizeable.Unit.EM);
        txtDireccion.setMaxLength(445);

        // El código postal.
        txtCp = (TextField) binder.buildAndBind("Código postal: ", "codigoPostal");
        txtCp.setNullRepresentation("");
        txtCp.setWidth(appWidth, Sizeable.Unit.EM);
        txtCp.setRequired(true);
        txtCp.setMaxLength(245);

        // Ciudad.
        txtCiudad = (TextField) binder.buildAndBind("Ciudad: ", "poblacion");
        txtCiudad.setNullRepresentation("");
        txtCiudad.setWidth(appWidth, Sizeable.Unit.EM);
        txtCiudad.setRequired(true);
        txtCiudad.setMaxLength(445);

        // Provincia.
        txtProvincia = (TextField) binder.buildAndBind("Provincia: ", "provincia");
        txtProvincia.setNullRepresentation("");
        txtProvincia.setWidth(appWidth, Sizeable.Unit.EM);
        txtProvincia.setRequired(true);
        txtProvincia.setMaxLength(445);

        // Provincia.
        txtProvincia = (TextField) binder.buildAndBind("País: ", "pais");
        txtProvincia.setNullRepresentation("");
        txtProvincia.setWidth(appWidth, Sizeable.Unit.EM);
        txtProvincia.setRequired(true);
        txtProvincia.setMaxLength(45);

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
        direccion = new TDireccionCliente();
        direccion.setDireccion(txtDireccion.getValue().trim().toUpperCase());
        direccion.setEstado(cbEstado.getValue().equals(Constants.ACTIVO) ? 1 : 0);
        direccion.setCodDireccion(txtCodDireccion.getValue().trim().toUpperCase());
        direccion.setCodigoPostal(txtCp.getValue().trim().toUpperCase());
        direccion.setPoblacion(txtCiudad.getValue().trim().toUpperCase());
        direccion.setProvincia(txtProvincia.getValue().trim().toUpperCase());
        direccion.setFechaCrea(Utils.generarFecha());
        direccion.setUsuCrea(user);
        direccion.setPais(txtPais.getValue().trim().toUpperCase());
        direccion.setFechaCrea(Utils.generarFecha());
        direccion.setIdCliente(cliente.getId());
    }

    /**
     * Método que es llamado para inicializar los valores de los componentes.
     */
    private void inicializarCampos() {
        txtDireccion.setValue(null);
        txtCp.setValue(null);
        txtCodDireccion.setValue(null);
        cbEstado.setValue(Constants.ACTIVO);
        txtCiudad.setValue(null);
        txtProvincia.setValue(null);
        txtPais.setValue(null);
    }

    /**
     * Comprobamos si ha informado los campos obligatorios.
     * @return true si no se cumple la validación
     */
    private boolean validarCamposObligatorios() {
        return !cbEstado.isValid() || !txtCodDireccion.isValid() || !txtDireccion.isValid() || !txtCp.isValid() || !txtProvincia.isValid() || !txtCiudad.isValid() || !txtPais.isValid();
    }
}
