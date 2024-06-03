/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.maestros.materiales;

import java.util.List;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TIva;
import com.dina.genasoft.db.entity.TMateriales;
import com.dina.genasoft.db.entity.TPermisos;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Daniel Carmona Romero
 * Vista para crear un nuevo material.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaNuevoMaterial.NAME)
public class VistaNuevoMaterial extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;
    /** El nombre de la vista.*/
    public static final String            NAME      = "nuevoMaterial";
    /** Para los campos que componen un material.*/
    private BeanFieldGroup<TMateriales>   binder;
    /** El boton para crear el empleado.*/
    private Button                        crearButton;
    /** Combobox para los IVA.*/
    private ComboBox                      cbIva;
    /** Combobox para los estados.*/
    private ComboBox                      cbEstado;
    /** El material a crear.*/
    private TMateriales                   material;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                       appWidth;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log       = org.slf4j.LoggerFactory.getLogger(VistaNuevoMaterial.class);
    // Los campos obligatorios
    /** La caja de texto para la referencia .*/
    private TextField                     txtReferencia;
    /** La caja de texto para el nombre.*/
    private TextField                     txtNombre;
    /** La caja de texto para el LER.*/
    private TextField                     txtLer;
    /** La caja de texto para el precio.*/
    private TextField                     txtPrecio;
    /** Los permisos del empleado actual. */
    private TPermisos                     permisos  = null;
    /** El usuario que está logado. */
    private Integer                       user      = null;
    /** La fecha en que se inició sesión. */
    private Long                          time      = null;
    private TEmpleados                    empleado;
    private List<TIva>                    lIvas;
    private final Integer                 ID_IVA_10 = 16;

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(crearButton)) {
            // Creamos el evento para crear un nuevo material con los datos introducidos en el formulario
            try {
                if (validarCamposObligatorios()) {
                    Notification aviso = new Notification("Se debe informar los campos marcados con '*'", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                // Construimos el objeto material a partir de los datos introducidos en el formulario.
                construirBean();
                String result = contrVista.crearMaterial(material, user, time);
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

                binder = new BeanFieldGroup<>(TMateriales.class);

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

                if (!Utils.booleanFromInteger(permisos.getCrearMateria())) {
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
                binder.setItemDataSource(new TMateriales());
                // Obtenemos los tipos de IVA activos.
                lIvas = Utils.generarListaGenerica();

                // Obtenemos los tipos de IVA activos.
                lIvas = contrVista.obtenerTiposIvaActivos(user, time);

                // Creamos los combos de la pantalla.
                crearCombos();

                // Creamos los botones de la pantalla.
                crearBotones();

                // Creamos los componetes que conforman la pantalla.
                crearComponentes();

                Label texto = new Label("Nuevo material");
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

                // Formulario con los campos que componen el empleado.
                VerticalLayout formulario1 = new VerticalLayout();
                formulario1.setSpacing(true);

                formulario1.addComponent(txtReferencia);
                formulario1.setComponentAlignment(txtReferencia, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtNombre);
                formulario1.setComponentAlignment(txtNombre, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtLer);
                formulario1.setComponentAlignment(txtLer, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtPrecio);
                formulario1.setComponentAlignment(txtPrecio, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(cbIva);
                formulario1.setComponentAlignment(cbIva, Alignment.MIDDLE_CENTER);
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
        crearButton = new Button("Crear material", this);
        crearButton.addStyleName("big");

    }

    /**
     * Método para crear los combos de la pantalla.
     */
    private void crearCombos() {
        // Combo box para el estado.
        cbEstado = new ComboBox();
        cbEstado.addStyleName("big");
        cbIva = new ComboBox();
        cbIva.setNewItemsAllowed(false);
        cbIva.addStyleName("big");

    }

    /**
     * Método que nos crea los componetes que conforman la pantalla.
     */
    private void crearComponentes() {
        //Los campos que componen un empleado.

        // El código de empleado.
        txtReferencia = (TextField) binder.buildAndBind("Referencia:", "referencia");
        txtReferencia.setNullRepresentation("");
        txtReferencia.setWidth(appWidth, Sizeable.Unit.EM);
        txtReferencia.setMaxLength(1000);
        txtReferencia.setRequired(true);

        // El nombre
        txtNombre = (TextField) binder.buildAndBind("Nombre:", "descripcion");
        txtNombre.setNullRepresentation("");
        txtNombre.setRequired(true);
        txtNombre.setWidth(appWidth, Sizeable.Unit.EM);
        txtNombre.setMaxLength(1000);

        // LER
        txtLer = (TextField) binder.buildAndBind("LER:", "ler");
        txtLer.setNullRepresentation("");
        txtLer.setRequired(true);
        txtLer.setWidth(appWidth, Sizeable.Unit.EM);
        txtLer.setMaxLength(445);

        // El Precio.
        txtPrecio = (TextField) binder.buildAndBind("Precio Kg:", "precio");
        txtPrecio.setNullRepresentation("0");
        txtPrecio.setWidth(appWidth, Sizeable.Unit.EM);
        txtPrecio.setRequired(true);

        // El IVA        
        cbIva.addItems(lIvas);
        cbIva.setCaption("IVA:");
        cbIva.setFilteringMode(FilteringMode.CONTAINS);
        cbIva.setRequired(true);
        cbIva.setNewItemsAllowed(false);
        cbIva.setNullSelectionAllowed(false);
        cbIva.setWidth(appWidth, Sizeable.Unit.EM);
        for (TIva iva : lIvas) {
            if (iva.getId().equals(ID_IVA_10)) {
                cbIva.setValue(iva);
                break;
            }
        }

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
        material = new TMateriales();
        material.setEstado(cbEstado.getValue().equals(Constants.ACTIVO) ? 1 : 0);
        material.setDescripcion(txtNombre.getValue().trim().toUpperCase());
        material.setFechaCrea(Utils.generarFecha());
        material.setUsuCrea(user);
        material.setIdFamilia(1);
        material.setIva(((TIva) cbIva.getValue()).getId());
        material.setLer(txtLer.getValue() != null ? txtLer.getValue().trim().toUpperCase() : "");
        try {
            material.setPrecio(txtPrecio.getValue() != null ? Utils.formatearValorDouble(txtPrecio.getValue().trim()) : Double.valueOf(0));
        } catch (Exception e) {
            throw new GenasoftException("No se ha introducido un valor numérico correcto en el precio.");
        }
        material.setReferencia(txtReferencia.getValue().trim().toUpperCase());

    }

    /**
     * Método que es llamado para inicializar los valores de los componentes.
     */
    private void inicializarCampos() {
        txtLer.setValue(null);
        txtNombre.setValue(null);
        txtPrecio.setValue(null);
        txtReferencia.clear();
        cbEstado.setValue(Constants.ACTIVO);
        for (TIva iva : lIvas) {
            if (iva.getId().equals(ID_IVA_10)) {
                cbIva.setValue(iva);
                break;
            }
        }

    }

    /**
     * Comprobamos si ha informado los campos obligatorios.
     * @return true si no se cumple la validación
     */
    private boolean validarCamposObligatorios() {
        return !cbEstado.isValid() || !txtNombre.isValid() || !txtReferencia.isValid() || !txtLer.isValid() || !txtPrecio.isValid() || !cbIva.isValid();
    }
}
