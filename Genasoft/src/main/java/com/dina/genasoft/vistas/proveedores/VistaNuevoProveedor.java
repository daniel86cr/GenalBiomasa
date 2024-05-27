/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.proveedores;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TProveedores;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
import com.vaadin.annotations.Theme;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Daniel Carmona Romero
 * Vista para crear un nuevo proveedor.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaNuevoProveedor.NAME)
public class VistaNuevoProveedor extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;
    /** El nombre de la vista.*/
    public static final String            NAME = "nuevoProveedor";
    /** El boton para crear el proveedor.*/
    private Button                        crearButton;
    /** Combo para indicar el GGN.*/
    private ComboBox                      cbGGns;
    /** La ruta a registrar.*/
    private TProveedores                  proveedor;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log  = LoggerFactory.getLogger(VistaNuevoProveedor.class);
    // Los campos que componen la recepción del pallet.
    /** El pedido. */
    private TextField                     txtNombre;
    /** El usuario que está logado. */
    private Integer                       user = null;
    /** La fecha en que se inició sesión. */
    private Long                          time = null;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                       appWidth;
    /** El empleado que ha iniciado sesión en la aplicación . */
    private TEmpleados                    empleado;
    /** Los permisos del empleado que ha iniciado sesión . */
    private TPermisos                     permisos;

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(crearButton)) {
            // Creamos el evento para crear un nuevo cliente con los datos introducidos en el formulario
            try {
                if (validaCamposObligatorios()) {
                    Notification aviso = new Notification("Debe informar los campos marcados con '*'", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }
                // Construimos el objeto cliente a partir de los datos introducidos en el formulario.
                contruirBean();
                contrVista.crearProveedor(proveedor, user, time);
                Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(Constants.OPERACION_OK), Notification.Type.HUMANIZED_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());

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
        time = null;
        user = (Integer) getSession().getAttribute("user");
        if ((String) getSession().getAttribute("fecha") != null) {
            time = Long.parseLong((String) getSession().getAttribute("fecha"));
        }
        if (time != null) {
            try {
                // Creamos los botones de la pantalla.
                crearBotones();
                // Creamos los componentes
                crearComponentes();

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

                if (!Utils.booleanFromInteger(permisos.getModificarProveedor())) {
                    Notification aviso = new Notification("No se tienen permisos para acceder a la pantalla indicada", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                    return;
                }

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

            Label texto = new Label("Nuevo proveedor");
            texto.setStyleName("tituloTamano18");
            // Incluimos en el layout los componentes
            VerticalLayout titulo = new VerticalLayout(texto);

            // The view root layout
            VerticalLayout viewLayout = new VerticalLayout(new Menu(permisos, user));
            viewLayout.setSizeFull();
            viewLayout.addComponent(titulo);
            viewLayout.setComponentAlignment(titulo, Alignment.TOP_CENTER);

            // Formulario con los campos que componen el cliente.
            VerticalLayout formulario = new VerticalLayout();
            formulario.setSpacing(true);
            formulario.addComponent(txtNombre);
            formulario.setComponentAlignment(txtNombre, Alignment.MIDDLE_CENTER);
            formulario.addComponent(cbGGns);
            formulario.setComponentAlignment(cbGGns, Alignment.MIDDLE_CENTER);
            formulario.addComponent(crearButton);
            formulario.setComponentAlignment(crearButton, Alignment.MIDDLE_CENTER);
            viewLayout.addComponent(formulario);
            setCompositionRoot(viewLayout);
            // Establecemos el porcentaje de ratio para los layouts
            viewLayout.setExpandRatio(titulo, 0.1f);

            viewLayout.setMargin(true);
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
        crearButton = new Button("Aplicar cambios", this);
    }

    /**
    
    /**
     * Método que es llamado para crear el objeto bean para la creación.
     * @throws ParseException 
     */
    private void contruirBean() throws GenasoftException {
        proveedor = new TProveedores();
        proveedor.setDescripcion(txtNombre.getValue().trim().toUpperCase());
        proveedor.setEstado(1);
        proveedor.setGgn(cbGGns.getValue().toString());
    }

    /**
     * Método que nos crea los componentes (cajas de texto)
     */
    private void crearComponentes() {

        //Los campos que componen un empleado.         
        txtNombre = new TextField("Nombre: ");
        txtNombre.setRequired(true);
        txtNombre.setNullRepresentation("");
        txtNombre.setWidth(appWidth, Sizeable.Unit.EM);
        txtNombre.setMaxLength(245);

        // GGN 
        cbGGns = new ComboBox();
        cbGGns.setRequired(true);
        cbGGns.addItem("NO GGN");
        cbGGns.addItem("GGN NACIONAL");
        cbGGns.addItem("GGN IMPORTACION");
        cbGGns.setValue("No GGN");
        cbGGns.setNullSelectionAllowed(false);
        cbGGns.setWidth(appWidth, Sizeable.Unit.EM);
        cbGGns.setFilteringMode(FilteringMode.CONTAINS);

    }

    /**
     * Método que nos valida que se hayan informado los valores obligatorios. <br>
     * Si no se informan correctamente, devuelve false 
     * @return True si son correctos, en caso contrario false.
     */
    private boolean validaCamposObligatorios() {
        // Comprobamos si ha informado los campos obligatorios.
        return !txtNombre.isValid() || !cbGGns.isValid();
    }
}
