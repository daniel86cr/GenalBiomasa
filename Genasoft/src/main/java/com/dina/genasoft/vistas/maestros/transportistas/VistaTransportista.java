/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.maestros.transportistas;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TRegistrosCambiosTransportistas;
import com.dina.genasoft.db.entity.TTransportistas;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.TransportistaEnum;
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
 * Vista para mostrar/visualizar un transportista.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaTransportista.NAME)
public class VistaTransportista extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas               contrVista;
    /** El nombre de la vista.*/
    public static final String              NAME     = "vTransportista";
    /** Para los campos que componen un transportista.*/
    private BeanFieldGroup<TTransportistas> binder;
    /** El boton para crear el transportista.*/
    private Button                          modificarButton;
    /** El boton para volver al listado de transportistas.*/
    private Button                          listadoButton;
    /** Combobox para los estados.*/
    private ComboBox                        cbEstado;
    /** El transportista a modificar.*/
    private TTransportistas                 nTransportista;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                          appName;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                         appWidth;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger   log      = org.slf4j.LoggerFactory.getLogger(VistaTransportista.class);
    // Los campos obligatorios
    /** La caja de texto para la referencia .*/
    private TextField                       txtRazonSocial;
    /** La caja de texto para el nombre.*/
    private TextField                       txtCif;
    /** La caja de texto para el LER.*/
    private TextField                       txtDireccion;
    /** La caja de texto para el precio.*/
    private TextField                       txtCiudad;
    /** La caja de texto para el precio.*/
    private TextField                       txtCp;
    /** La caja de texto para el precio.*/
    private TextField                       txtProvincia;
    /** Los permisos del empleado actual. */
    private TPermisos                       permisos = null;
    /** El usuario que está logado. */
    private Integer                         user     = null;
    /** La fecha en que se inició sesión. */
    private Long                            time     = null;
    /** Contendrá los cambios que se aplican al transportista. */
    private String                          cambios;
    private TEmpleados                      empleado;

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(modificarButton)) {
            // Creamos el evento para modificar un nuevo transportista con los datos introducidos en el formulario
            try {
                if (validarCamposObligatorios()) {
                    Notification aviso = new Notification("Se debe informar los campos marcados con '*'", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                // Construimos el objeto transportista a partir de los datos introducidos en el formulario.
                construirBean();
                String result = contrVista.modificarTransportista(nTransportista, user, time);
                if (result.equals(Constants.OPERACION_OK)) {

                    // Si hay cambios, guardamos los cambios en el registro de cambios
                    if (!cambios.isEmpty()) {

                        TRegistrosCambiosTransportistas record = new TRegistrosCambiosTransportistas();
                        record.setCambio(cambios);
                        record.setFechaCambio(Utils.generarFecha());
                        record.setIdTransportista(nTransportista.getId());
                        record.setUsuCrea(user);

                        contrVista.crearRegistroCambioTransportista(record, user, time);
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
            getUI().getNavigator().navigateTo(VistaListadoTransportistas.NAME);
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
                binder = new BeanFieldGroup<>(TTransportistas.class);

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

                if (!Utils.booleanFromInteger(permisos.getModificarTransportista())) {
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

                String parametros = event.getParameters();

                if (parametros != null && !parametros.isEmpty()) {
                    nTransportista = contrVista.obtenerTransportistaPorId(Integer.valueOf(parametros), user, time);
                } else {
                    parametros = null;
                }

                if (nTransportista == null) {
                    Notification aviso = new Notification("No se ha encontrado el transportista.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getUI().getNavigator().navigateTo(VistaListadoTransportistas.NAME);
                    return;
                }

                binder.setItemDataSource(nTransportista);

                // Creamos los botones de la pantalla.
                crearBotones();

                // Creamos los componetes que conforman la pantalla.
                crearComponentes(parametros);

                Label texto = new Label(nTransportista.getNombre());
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

                // Formulario con los campos que componen el transportista.
                VerticalLayout formulario1 = new VerticalLayout();
                formulario1.setSpacing(true);
                // Formulario con los campos que componen el transportista.
                VerticalLayout formulario2 = new VerticalLayout();
                formulario2.setSpacing(true);

                Label lblSpace = new Label(" ");
                lblSpace.setHeight(5, Sizeable.Unit.EM);

                formulario1.addComponent(txtRazonSocial);
                formulario1.setComponentAlignment(txtRazonSocial, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtCif);
                formulario1.setComponentAlignment(txtCif, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtDireccion);
                formulario1.setComponentAlignment(txtDireccion, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtCp);
                formulario1.setComponentAlignment(txtCp, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtCiudad);
                formulario1.setComponentAlignment(txtCiudad, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtProvincia);
                formulario1.setComponentAlignment(txtProvincia, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(cbEstado);
                formulario1.setComponentAlignment(cbEstado, Alignment.MIDDLE_CENTER);
                body.addComponent(formulario1);
                body.setComponentAlignment(formulario1, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(listadoButton);
                viewLayout.addComponent(body);
                viewLayout.setComponentAlignment(body, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(lblSpace);
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
        listadoButton = new Button("Volver al listado", this);
        listadoButton.addStyleName("big");
    }

    /**
     * Método para crear los combos de la pantalla.
     */
    private void crearCombos() {
        // Combo box para el estado.
        cbEstado = new ComboBox();

    }

    /**
     * Método que nos crea los componetes que conforman la pantalla.
     */
    private void crearComponentes(String nombre) {
        //Los campos que componen un transportista.

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
        txtCiudad = (TextField) binder.buildAndBind("Ciudad: ", "ciudad");
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

        // Los estados.
        cbEstado.setCaption("Estado:");
        cbEstado.addItem(Constants.ACTIVO);
        cbEstado.addItem(Constants.DESACTIVADO);
        cbEstado.setValue(nTransportista.getEstado().equals(TransportistaEnum.ACTIVO.getValue()) ? Constants.ACTIVO : Constants.DESACTIVADO);
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
        if (!nTransportista.getEstado().equals(estado)) {
            cambios = "Se cambia el estado del transportista, pasa de " + nTransportista.getEstado() + " a " + estado;
        }
        nTransportista.setEstado(cbEstado.getValue().equals(Constants.ACTIVO) ? 1 : 0);
        String value = txtRazonSocial.getValue();

        if (value != null) {
            value = value.trim().toUpperCase();
        }
        if (value == null && nTransportista.getNombre() != null) {
            cambios = cambios + "\n Se le quita el nombre, antes tenia: " + nTransportista.getNombre();
        } else if (value != null && nTransportista.getNombre() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna una nuevo nombre, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nTransportista.getNombre())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia el nombre, antes tenia: " + nTransportista.getNombre() + " y ahora tiene: " + value;
        }

        nTransportista.setNombre(value);
        nTransportista.setRazonSocial(value);

        value = txtCif.getValue().trim().toUpperCase();

        if (value != null) {
            value = value.trim().toUpperCase();
        }

        if (value == null && nTransportista.getCif() != null) {
            cambios = cambios + "\n Se le quita el CIF, antes tenia: " + nTransportista.getCif();
        } else if (value != null && nTransportista.getCif() == null) {
            cambios = cambios + "\n Se le asigna un nuevo CIF,  antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nTransportista.getCif())) {
            cambios = cambios + "\n Se le cambia el CIF, antes tenia: " + nTransportista.getCif() + " y ahora tiene: " + value;
        }

        nTransportista.setCif(value);

        value = txtCiudad.getValue();

        if (value == null && nTransportista.getCiudad() != null) {
            cambios = cambios + "\n Se le quita la ciudad, antes tenia: " + nTransportista.getCiudad();
        } else if (value != null && nTransportista.getCiudad() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna una nueva ciudad, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nTransportista.getCiudad())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia la ciudad, antes tenia: " + nTransportista.getCiudad() + " y ahora tiene: " + value;
        }

        nTransportista.setCiudad(value);

        value = txtCp.getValue();

        if (value == null && nTransportista.getCodigoPostal() != null) {
            cambios = cambios + "\n Se le quita el codigo postal, antes tenia: " + nTransportista.getCodigoPostal();
        } else if (value != null && nTransportista.getCodigoPostal() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna un nuevo codigo postal, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nTransportista.getCodigoPostal())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia el codigo postal, antes tenia: " + nTransportista.getCodigoPostal() + " y ahora tiene: " + value;
        }

        nTransportista.setCodigoPostal(value);

        value = txtProvincia.getValue();

        if (value == null && nTransportista.getProvincia() != null) {
            cambios = cambios + "\n Se le quita la provincia, antes tenia: " + nTransportista.getProvincia();
        } else if (value != null && nTransportista.getProvincia() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna una nueva provincia, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nTransportista.getProvincia())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia la provincia, antes tenia: " + nTransportista.getProvincia() + " y ahora tiene: " + value;
        }

        nTransportista.setProvincia(value);

        value = txtDireccion.getValue();

        if (value == null && nTransportista.getDireccion() != null) {
            cambios = cambios + "\n Se le quita la dirección, antes tenia: " + nTransportista.getDireccion();
        } else if (value != null && nTransportista.getDireccion() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna una nueva dirección, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nTransportista.getDireccion())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia la dirección, antes tenia: " + nTransportista.getDireccion() + " y ahora tiene: " + value;
        }

        nTransportista.setDireccion(value);

        if (!cambios.isEmpty()) {
            nTransportista.setFechaModifica(Utils.generarFecha());
            nTransportista.setUsuModifica(user);
        }

    }

    /**
     * Comprobamos si ha informado los campos obligatorios.
     * @return true si no se cumple la validación
     */
    private boolean validarCamposObligatorios() {
        return !cbEstado.isValid() || !txtCif.isValid() || !txtRazonSocial.isValid() || !txtDireccion.isValid() || !txtCp.isValid() || !txtProvincia.isValid() || !txtCiudad.isValid();
    }

}
