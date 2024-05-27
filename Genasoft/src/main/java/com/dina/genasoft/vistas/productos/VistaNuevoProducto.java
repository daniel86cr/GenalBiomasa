/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.productos;

import java.util.List;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TProductos;
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
import com.vaadin.server.ThemeResource;
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
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * @author Daniel Carmona Romero
 * Vista para crear un nuevo producto.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaNuevoProducto.NAME)
public class VistaNuevoProducto extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;
    /** El nombre de la vista.*/
    public static final String            NAME            = "nuevoProducto";
    /** Para los campos que componen un producto.*/
    private BeanFieldGroup<TProductos>    binder;
    /** El boton para crear el producto.*/
    private Button                        crearButton;
    /** El boton para inlcuir un calibre/diametro para asociar.*/
    private Button                        incluirButton;
    /** El boton para quitar un calibre/diametro para asociar.*/
    private Button                        quitarButton;
    /** Combobox para los estados.*/
    private ComboBox                      cbEstado;
    /** Combobox para los tipos de producto. */
    private ComboBox                      cbTipo;
    /** El producto a crear.*/
    private TProductos                    prodNuevo;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                       appWidth;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log             = org.slf4j.LoggerFactory.getLogger(VistaNuevoProducto.class);
    // Los campos obligatorios
    /** La caja de texto para el nombre del producto.*/
    private TextField                     txtDescripcion;
    /** Los permisos del empleado actual. */
    private TPermisos                     permisos        = null;
    /** El usuario que está logado. */
    private Integer                       user            = null;
    /** La fecha en que se inició sesión. */
    private Long                          time            = null;
    private TEmpleados                    empleado;
    /** Tabla para mostrar los calibres que tiene el producto. */
    private Table                         tablaCalibres   = null;
    /** Tabla para mostrar los calibres que tiene el producto. */
    private Table                         tablaDiametros  = null;
    /** Tabla para mostrar los calibres que tiene el producto. */
    private Table                         tablaVariedades = null;
    /** Lista para los calibres. */
    private ComboBox                      cbCalibres;
    /** Lista para los diámetros. */
    private ListSelect                    cbDiametros;
    /** Lista para los diámetros. */
    private ListSelect                    cbVariedades;

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(crearButton)) {
            // Creamos el evento para crear un nuevo producto con los datos introducidos en el formulario
            try {
                if (validarCamposObligatorios()) {
                    Notification aviso = new Notification("Se debe informar los campos marcados con '*'", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                // Construimos el objeto producto a partir de los datos introducidos en el formulario.
                construirBean();
                String result = contrVista.crearProducto(prodNuevo, user, time);
                if (result.equals(Constants.OPERACION_OK)) {
                    result = contrVista.obtenerDescripcionCodigo(result);
                    Notification aviso = new Notification(result, Notification.Type.ASSISTIVE_NOTIFICATION);
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

                binder = new BeanFieldGroup<>(TProductos.class);

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

                if (!Utils.booleanFromInteger(permisos.getCrearProductos())) {
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
                binder.setItemDataSource(new TProductos());

                // Creamos los combos de la pantalla.
                crearCombos();

                // Creamos los botones de la pantalla.
                crearBotones();

                // Creamos los componetes que conforman la pantalla.
                //crearComponentes();

                Label texto = new Label("Nuevo producto");
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

                formulario1.addComponent(txtDescripcion);
                formulario1.setComponentAlignment(txtDescripcion, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(cbTipo);
                formulario1.setComponentAlignment(cbTipo, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(cbEstado);
                formulario2.setComponentAlignment(cbEstado, Alignment.MIDDLE_CENTER);
                body.addComponent(formulario1);
                body.setComponentAlignment(formulario1, Alignment.MIDDLE_LEFT);
                body.addComponent(formulario2);
                body.setComponentAlignment(formulario2, Alignment.MIDDLE_RIGHT);
                viewLayout.addComponent(body);
                viewLayout.addComponent(crearButton);
                viewLayout.setComponentAlignment(crearButton, Alignment.MIDDLE_CENTER);

                // Añadimos el logo de Natur tropic
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

        incluirButton = new Button("", this);
        incluirButton.setIcon(new ThemeResource("icons/arrow-der-32.ico"));
        incluirButton.setStyleName(BaseTheme.BUTTON_LINK);

        quitarButton = new Button("", this);
        quitarButton.setIcon(new ThemeResource("icons/arrow-izq-32.ico"));
        quitarButton.setStyleName(BaseTheme.BUTTON_LINK);

    }

    /**
     * Método para crear los combos de la pantalla.
     */
    private void crearCombos() {
        // Combo box para el estado.
        cbEstado = new ComboBox();
        cbEstado.addStyleName("big");
        cbTipo = new ComboBox();
        cbTipo.setNewItemsAllowed(false);
        cbTipo.addStyleName("big");

    }

    /**
     * Método que nos crea los componetes que conforman la pantalla.
     */
    private void crearComponentes(String nombre, List<TRoles> roles) {
        //Los campos que componen un empleado.

        // El nombre con el que se hace login. 
        txtDescripcion = (TextField) binder.buildAndBind("Nombre usuario:", "nombreUsuario");
        txtDescripcion.setNullRepresentation("");
        txtDescripcion.setRequired(true);
        txtDescripcion.setWidth(appWidth, Sizeable.Unit.EM);
        txtDescripcion.setMaxLength(245);

        // Los roles        
        cbTipo.addItems(roles);
        cbTipo.setCaption("Rol:");
        cbTipo.setFilteringMode(FilteringMode.CONTAINS);
        cbTipo.setRequired(true);
        cbTipo.setWidth(appWidth, Sizeable.Unit.EM);

        // Los estados.
        cbEstado.setCaption("Estado:");
        cbEstado.addItem("Activado");
        cbEstado.addItem("Desactivado");
        cbEstado.setValue("Activado");
        cbEstado.setFilteringMode(FilteringMode.CONTAINS);
        cbEstado.setRequired(true);
        cbEstado.setNullSelectionAllowed(false);
        cbEstado.setWidth(appWidth, Sizeable.Unit.EM);

        ;
    }

    /**
     * Método que es llamado para crear el objeto bean para la creación.
     */
    private void construirBean() throws GenasoftException {
        prodNuevo = new TProductos();
        prodNuevo.setEstado(cbEstado.getValue().equals("Activado") ? 1 : 0);
        prodNuevo.setFechaCrea(Utils.generarFecha());
        prodNuevo.setDescripcion(txtDescripcion.getValue().trim().toUpperCase());
        prodNuevo.setUsuCrea(user);

    }

    /**
     * Método que es llamado para inicializar los valores de los componentes.
     */
    private void inicializarCampos() {
        cbTipo.clear();
        txtDescripcion.setValue(null);
        cbEstado.setValue("Activo");
    }

    /**
     * Comprobamos si ha informado los campos obligatorios.
     * @return true si no se cumple la validación
     */
    private boolean validarCamposObligatorios() {
        return !txtDescripcion.isValid() || !cbEstado.isValid() || !cbTipo.isValid();
    }
}
