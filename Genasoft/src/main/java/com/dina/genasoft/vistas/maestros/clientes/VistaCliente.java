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
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TMateriales;
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TRegistrosCambiosClientes;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.OperadorEnum;
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
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

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
    private ControladorVistas             contrVista;
    /** El nombre de la vista.*/
    public static final String            NAME           = "vCliente";
    /** Para los campos que componen un cliente.*/
    private BeanFieldGroup<TClientes>     binder;
    /** El boton para crear el operador.*/
    private Button                        modificarButton;
    /** El boton para volver al listado de clientes.*/
    private Button                        listadoButton;
    /** Combobox para los estados.*/
    private ComboBox                      cbEstado;
    /** Combobox para los materiales.*/
    private ComboBox                      cbMateriales;
    /** Combobox para los materiales.*/
    private ComboBox                      cbOperadores;
    /** El cliente a modificar.*/
    private TClientes                     nClientes;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                       appWidth;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log            = org.slf4j.LoggerFactory.getLogger(VistaCliente.class);
    // Los campos obligatorios
    /** La caja de texto para la referencia .*/
    private TextField                     txtRazonSocial;
    /** La caja de texto para el nombre.*/
    private TextField                     txtCif;
    /** Los permisos del empleado actual. */
    private TPermisos                     permisos       = null;
    /** El usuario que está logado. */
    private Integer                       user           = null;
    /** La fecha en que se inició sesión. */
    private Long                          time           = null;
    private TEmpleados                    empleado;
    /** Botones superiores. **/
    /** El botón principal del segmento. Muestra la información principal del cliente.*/
    private Button                        principalButton;
    /** Muestra la información relacionada con los materiales del cliente.*/
    private Button                        materialesButton;
    /** Muestra la información relacionada con los operadores del cliente.*/
    private Button                        operadoresButton;
    /** El boton para crear la dirección de descarga del cliente.*/
    private Button                        crearDireccionButton;
    /** Container que mostrará los campos relacionado con la información principal del cliente. */
    private VerticalLayout                infoPrincipal  = null;
    /** Container que mostrará los campos relacionado con la información de materiales del cliente. */
    private VerticalLayout                infoMateriales = null;
    /** Container que mostrará los campos relacionado con la información de operadores del cliente. */
    private VerticalLayout                infoOperadores = null;
    /** ListSelect para añadir los materiales para los materiales. */
    private ListSelect                    lsMateriales;
    /** ListSelect para añadir los materiales para los materiales. */
    private ListSelect                    lsOperadores;
    /** Lista con los materiales activos del sistema. */
    private List<TMateriales>             lMateriales;
    /** Lista con los operadores activos del sistema. */
    private List<TOperadores>             lOperadores;
    /** Lista con los materiales activos del sistema. */
    private List<TMateriales>             lMaterialesAsignados;
    /** Lista con los operadores activos del sistema. */
    private List<TOperadores>             lOperadoresAsignados;
    private String                        cambios;

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

                // Creamos los botones de la pantalla.
                crearBotones();
                // Creamos los combos de la pantalla.
                crearCombos();

                // Creamos los botones superiores
                crearBotonesMenu();
                //El fieldgroup no es un componente

                // Obtenemos los materiales activos del sistema.
                lMateriales = contrVista.obtenerMaterialesActivos(user, time);
                lMaterialesAsignados = contrVista.obtenerMaterialesAsignadosCliente(nClientes.getId(), user, time);
                lOperadoresAsignados = contrVista.obtenerOperadoresAsignadosCliente(nClientes.getId(), user, time);

                // Obtenemos los operadores activos del sistema.
                lOperadores = contrVista.obtenerOperadoresActivos(user, time);

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

                binder.setItemDataSource(nClientes);
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

        // Direcciones
        crearDireccionButton = new Button("Crear dirección", this);
        crearDireccionButton.addStyleName("big");
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
                    principalButton.addStyleName("down");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("default");
                    modificarButton.setVisible(true);
                }
            }
        });

        materialesButton = new Button("Materiales");
        materialesButton.addStyleName("default");

        materialesButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!infoMateriales.isVisible()) {
                    infoMateriales.setVisible(true);
                    infoPrincipal.setVisible(false);
                    infoOperadores.setVisible(false);
                    principalButton.addStyleName("default");
                    materialesButton.setStyleName("down");
                    operadoresButton.setStyleName("default");
                    modificarButton.setVisible(false);
                }
            }
        });

        operadoresButton = new Button("Operadores");
        operadoresButton.addStyleName("default");

        operadoresButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!infoOperadores.isVisible()) {
                    infoOperadores.setVisible(true);
                    infoMateriales.setVisible(false);
                    infoPrincipal.setVisible(false);
                    principalButton.addStyleName("default");
                    materialesButton.setStyleName("default");
                    operadoresButton.setStyleName("down");
                    modificarButton.setVisible(false);

                }
            }
        });

        botonesMenu.addComponent(principalButton);
        botonesMenu.addComponent(materialesButton);
        botonesMenu.addComponent(operadoresButton);

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

        value = txtCiudad.getValue();

        if (value == null && nClientes.getCiudad() != null) {
            cambios = cambios + "\n Se le quita la ciudad, antes tenia: " + nClientes.getCiudad();
        } else if (value != null && nClientes.getCiudad() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna una nueva ciudad, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nClientes.getCiudad())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia la ciudad, antes tenia: " + nClientes.getCiudad() + " y ahora tiene: " + value;
        }

        nClientes.setCiudad(value);

        value = txtCp.getValue();

        if (value == null && nClientes.getCodigoPostal() != null) {
            cambios = cambios + "\n Se le quita el codigo postal, antes tenia: " + nClientes.getCodigoPostal();
        } else if (value != null && nClientes.getCodigoPostal() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna un nuevo codigo postal, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nClientes.getCodigoPostal())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia el codigo postal, antes tenia: " + nClientes.getCodigoPostal() + " y ahora tiene: " + value;
        }

        nClientes.setCodigoPostal(value);

        value = txtProvincia.getValue();

        if (value == null && nClientes.getProvincia() != null) {
            cambios = cambios + "\n Se le quita la provincia, antes tenia: " + nClientes.getProvincia();
        } else if (value != null && nClientes.getProvincia() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna una nueva provincia, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nClientes.getProvincia())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia la provincia, antes tenia: " + nClientes.getProvincia() + " y ahora tiene: " + value;
        }

        nClientes.setProvincia(value);

        value = txtDireccion.getValue();

        if (value == null && nClientes.getDireccion() != null) {
            cambios = cambios + "\n Se le quita la dirección, antes tenia: " + nClientes.getDireccion();
        } else if (value != null && nClientes.getDireccion() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna una nueva dirección, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nClientes.getDireccion())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia la dirección, antes tenia: " + nClientes.getDireccion() + " y ahora tiene: " + value;
        }

        nClientes.setDireccion(value);

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
        return !cbEstado.isValid() || !txtCif.isValid() || !txtRazonSocial.isValid() || !txtDireccion.isValid() || !txtCp.isValid() || !txtProvincia.isValid() || !txtCiudad.isValid();
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
        formulario1.addComponent(cbEstado);
        formulario1.setComponentAlignment(cbEstado, Alignment.MIDDLE_CENTER);
        body.addComponent(formulario1);
        body.setComponentAlignment(formulario1, Alignment.MIDDLE_CENTER);

        infoPrincipal.addComponent(body);
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
                    lsMateriales.addItem((TMateriales) cbMateriales.getValue());
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
                if (cbMateriales.getValue() != null) {
                    lsMateriales.addItem((TOperadores) cbOperadores.getValue());
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
    }

}
