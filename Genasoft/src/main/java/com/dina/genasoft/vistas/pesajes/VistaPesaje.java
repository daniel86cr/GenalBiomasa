/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.pesajes;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TDireccionCliente;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TIva;
import com.dina.genasoft.db.entity.TMateriales;
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TPesajes;
import com.dina.genasoft.db.entity.TRegistrosCambiosPesajes;
import com.dina.genasoft.db.entity.TTransportistas;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.PesajesEnum;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutListener;
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
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Daniel Carmona Romero
 * Vista para mostrar/visualizar el pesaje.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaPesaje.NAME)
public class VistaPesaje extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;
    /** El nombre de la vista.*/
    public static final String            NAME     = "vPesaje";
    /** El boton para crear el transportista.*/
    private Button                        modificarButton;
    /** El boton para volver al listado de pesajes.*/
    private Button                        listadoButton;
    /** Combobox para los clientes.*/
    private ComboBox                      cbClientes;
    /** Combobox para los clientes.*/
    private ComboBox                      cbEstados;
    /** Combobox para los materiales.*/
    private ComboBox                      cbMateriales;
    /** Combobox para las direcciones.*/
    private ComboBox                      cbDirecciones;
    /** Combobox para los operadores.*/
    private ComboBox                      cbOperadores;
    /** Combobox para los operadores.*/
    private ComboBox                      cbTransportistas;
    /** El pesaje a modificar.*/
    private TPesajes                      nPesajes;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                       appWidth;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log      = org.slf4j.LoggerFactory.getLogger(VistaPesaje.class);
    // Los campos obligatorios
    // Los campos obligatorios
    /** La caja de texto para la obra .*/
    private TextField                     txtAlbaran;
    /** La caja de texto para la obra .*/
    private TextField                     txtObra;
    /** La caja de texto para el origen.*/
    private TextField                     txtOrigen;
    /** La caja de texto para el destino.*/
    private TextField                     txtDestino;
    /** La caja de texto para la matrícula.*/
    private TextField                     txtMatricula;
    /** La caja de texto para el remolque.*/
    private TextField                     txtRemolque;
    /** La caja de texto para los kilos brutos.*/
    private TextField                     txtKgsBrutos;
    /** La caja de texto para la tara.*/
    private TextField                     txtTara;
    /** La caja de texto para los kilos netos.*/
    private TextField                     txtKgsNetos;
    /** La fecha del pesaje. */
    private DateField                     fechaPesaje;
    /** Los permisos del empleado actual. */
    private TPermisos                     permisos = null;
    /** El usuario que está logado. */
    private Integer                       user     = null;
    /** La fecha en que se inició sesión. */
    private Long                          time     = null;
    /** Contendrá los cambios que se aplican al transportista. */
    private String                        cambios;
    private TEmpleados                    empleado;
    /** Los clientes activos del sistema.*/
    private List<TClientes>               lClientes;
    /** Los materiales activos del sistema.*/
    private List<TMateriales>             lMateriales;
    /** Los operadores activos del sistema.*/
    private List<TOperadores>             lOperadores;
    /** Los transportistas activos del sistema.*/
    private List<TTransportistas>         lTransportistas;
    private Double                        bruto, tara, neto;
    private List<TDireccionCliente>       lTodasDirs;

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
                String result = contrVista.modificarPesaje(nPesajes, user, time);
                if (result.equals(Constants.OPERACION_OK)) {

                    // Si hay cambios, guardamos los cambios en el registro de cambios
                    if (!cambios.isEmpty()) {

                        TRegistrosCambiosPesajes record = new TRegistrosCambiosPesajes();
                        record.setCambio(cambios);
                        record.setFechaCambio(Utils.generarFecha());
                        record.setIdPesaje(nPesajes.getId());
                        record.setUsuCrea(user);

                        contrVista.crearRegistroCambioPesaje(record, user, time);
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
            getUI().getNavigator().navigateTo(VistaListadoPesajes.NAME);
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

                empleado = contrVista.obtenerEmpleadoPorId(user, user, time);

                permisos = contrVista.obtenerPermisosEmpleado(empleado, user, time);

                lClientes = contrVista.obtenerClientesActivos(user, time);

                lMateriales = contrVista.obtenerTodosMateriales(user, time);

                lTransportistas = contrVista.obtenerTransportistasActivos(user, time);

                if (permisos == null) {
                    Notification aviso = new Notification("No se ha podido identificar los permisos asignados, contacta con el administrador.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    return;
                }

                if (!Utils.booleanFromInteger(permisos.getModificarPesaje())) {
                    Notification aviso = new Notification("No se tienen permisos para acceder a la pantalla indicada", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                    return;
                }

                lMateriales = Utils.generarListaGenerica();

                bruto = Double.valueOf(0);
                tara = Double.valueOf(0);
                neto = Double.valueOf(0);

                // Creamos los botones de la pantalla.
                crearBotones();
                // Creamos los combos de la pantalla.
                crearCombos();
                //El fieldgroup no es un componente

                String parametros = event.getParameters();

                if (parametros != null && !parametros.isEmpty()) {
                    nPesajes = contrVista.obtenerPesajePorId(Integer.valueOf(parametros), user, time);
                } else {
                    parametros = null;
                }

                if (nPesajes == null) {
                    Notification aviso = new Notification("No se ha encontrado el pesaje.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getUI().getNavigator().navigateTo(VistaListadoPesajes.NAME);
                    return;
                }

                // Creamos los botones de la pantalla.
                crearBotones();

                // Creamos los componetes que conforman la pantalla.
                crearComponentes(parametros);

                Label texto = new Label(nPesajes.getNumeroAlbaran());
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

                formulario1.addComponent(cbClientes);
                formulario1.setComponentAlignment(cbClientes, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(cbDirecciones);
                formulario1.setComponentAlignment(cbDirecciones, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(cbMateriales);
                formulario1.setComponentAlignment(cbMateriales, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(cbOperadores);
                formulario1.setComponentAlignment(cbOperadores, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtObra);
                formulario1.setComponentAlignment(txtObra, Alignment.MIDDLE_CENTER);
                formulario1.addComponent(txtOrigen);
                formulario1.setComponentAlignment(txtOrigen, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtDestino);
                formulario2.setComponentAlignment(txtDestino, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtMatricula);
                formulario2.setComponentAlignment(txtMatricula, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtRemolque);
                formulario2.setComponentAlignment(txtRemolque, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(cbTransportistas);
                formulario2.setComponentAlignment(cbTransportistas, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtKgsBrutos);
                formulario2.setComponentAlignment(txtKgsBrutos, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtTara);
                formulario2.setComponentAlignment(txtTara, Alignment.MIDDLE_CENTER);
                body.addComponent(formulario1);
                body.setComponentAlignment(formulario1, Alignment.MIDDLE_CENTER);
                body.addComponent(formulario2);
                body.setComponentAlignment(formulario2, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(listadoButton);
                viewLayout.addComponent(fechaPesaje);
                viewLayout.setComponentAlignment(fechaPesaje, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(body);
                viewLayout.setComponentAlignment(body, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(body);
                viewLayout.setComponentAlignment(body, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(txtKgsNetos);
                viewLayout.setComponentAlignment(txtKgsNetos, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(cbEstados);
                viewLayout.setComponentAlignment(cbEstados, Alignment.MIDDLE_CENTER);
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

                if (nPesajes.getEstado().equals(PesajesEnum.FACTURADO.getValue())) {
                    Notification aviso = new Notification("El pesaje está facturado, no se permiten modificaciones.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    modificarButton.setVisible(false);
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

        cbEstados = new ComboBox();
        cbEstados.addStyleName("big");

        cbOperadores = new ComboBox("Operador:");
        cbOperadores.addStyleName("big");

        cbTransportistas = new ComboBox("Transportista:");
        cbTransportistas.addStyleName("big");

        // ComboBox para los clientes.
        cbClientes = new ComboBox();
        cbClientes.setEnabled(false);
        cbClientes.addStyleName("big");
        cbClientes.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbClientes.getValue() != null) {
                    try {
                        TClientes cl = (TClientes) cbClientes.getValue();
                        cbDirecciones.removeAllItems();
                        List<TDireccionCliente> lDirs = contrVista.obtenerDireccionesClientePorIdCliente(cl.getId(), user, time);
                        cbDirecciones.addItems(lDirs);
                        if (lDirs.size() == 1) {
                            cbDirecciones.setValue(lDirs.get(0));
                        } else if (lDirs.isEmpty()) {
                            Notification aviso = new Notification("No se han identificado direcciones del cliente seleccionado.", Notification.Type.ERROR_MESSAGE);
                            aviso.setPosition(Position.MIDDLE_CENTER);
                            aviso.show(Page.getCurrent());
                        }

                        lMateriales = contrVista.obtenerMaterialesAsignadosCliente(cl.getId(), user, time);
                        cbMateriales.removeAllItems();
                        cbMateriales.addItems(lMateriales);
                        if (lMateriales.size() == 1) {
                            cbMateriales.setValue(lMateriales.get(0));
                        } else if (lMateriales.isEmpty()) {
                            Notification aviso = new Notification("No se han identificado materiales asignados al cliente seleccionado", Notification.Type.ERROR_MESSAGE);
                            aviso.setPosition(Position.MIDDLE_CENTER);
                            aviso.show(Page.getCurrent());
                        }

                        lOperadores = contrVista.obtenerOperadoresAsignadosCliente(cl.getId(), user, time);
                        cbOperadores.removeAllItems();
                        cbOperadores.addItems(lOperadores);
                        if (lOperadores.size() == 1) {
                            cbOperadores.setValue(lOperadores.get(0));
                        } else if (lMateriales.isEmpty()) {
                            Notification aviso = new Notification("No se han identificado operadores asignados al cliente seleccionado", Notification.Type.ERROR_MESSAGE);
                            aviso.setPosition(Position.MIDDLE_CENTER);
                            aviso.show(Page.getCurrent());
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
                }
            }
        });

        // ComboBox para los materiales.
        cbMateriales = new ComboBox();
        cbMateriales.addStyleName("big");

        // ComboBox para las direcciones
        cbDirecciones = new ComboBox("Dirección:");

    }

    /**
     * Método que nos crea los componetes que conforman la pantalla.
     * @throws GenasoftException 
     */
    private void crearComponentes(String nombre) throws GenasoftException {
        DecimalFormat df = new DecimalFormat("#,##0.000");
        fechaPesaje = new DateField("Fecha pesaje");
        fechaPesaje.setValue(Utils.generarFecha());
        fechaPesaje.setRequired(true);
        fechaPesaje.setWidth(appWidth, Sizeable.Unit.EM);
        fechaPesaje.setValue(nPesajes.getFechaPesaje());

        // La obra.
        txtObra = new TextField("Obra:");
        txtObra.setNullRepresentation("");
        txtObra.setWidth(appWidth, Sizeable.Unit.EM);
        txtObra.setMaxLength(445);
        txtObra.setRequired(true);
        txtObra.setValue(nPesajes.getObra());

        // El nº de albarán
        txtAlbaran = new TextField("Nº Albarán:");
        txtAlbaran.setNullRepresentation("");
        txtAlbaran.setRequired(true);
        txtAlbaran.setWidth(appWidth, Sizeable.Unit.EM);
        txtAlbaran.setMaxLength(445);
        txtAlbaran.setValue(nPesajes.getNumeroAlbaran());

        // El origen
        txtOrigen = new TextField("Origen:");
        txtOrigen.setNullRepresentation("");
        txtOrigen.setRequired(true);
        txtOrigen.setWidth(appWidth, Sizeable.Unit.EM);
        txtOrigen.setMaxLength(445);
        txtOrigen.setValue(nPesajes.getOrigen());

        // El destino
        txtDestino = new TextField("Destino:");
        txtDestino.setNullRepresentation("");
        txtDestino.setWidth(appWidth, Sizeable.Unit.EM);
        txtDestino.setMaxLength(445);
        txtDestino.setValue(nPesajes.getDestino());

        // La matrícula.
        txtMatricula = new TextField("Matrícula");
        txtMatricula.setNullRepresentation("");
        txtMatricula.setRequired(true);
        txtMatricula.setWidth(appWidth, Sizeable.Unit.EM);
        txtMatricula.setMaxLength(445);
        txtMatricula.setValue(nPesajes.getMatricula());

        // El remolque.
        txtRemolque = new TextField("Remolque");
        txtRemolque.setNullRepresentation("");
        txtRemolque.setRequired(true);
        txtRemolque.setWidth(appWidth, Sizeable.Unit.EM);
        txtRemolque.setMaxLength(445);
        txtRemolque.setValue(nPesajes.getRemolque());

        // Los Kgs brutos.
        txtKgsBrutos = new TextField("Kilos brutos:");
        txtKgsBrutos.setNullRepresentation("");
        txtKgsBrutos.setWidth(appWidth, Sizeable.Unit.EM);
        txtKgsBrutos.setRequired(true);
        txtKgsBrutos.setValue(df.format(nPesajes.getKgsBruto()));
        txtKgsBrutos.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                DecimalFormat df = new DecimalFormat("#,##0.000");
                bruto = Double.valueOf(0);
                tara = Double.valueOf(0);
                neto = Double.valueOf(0);
                if (txtKgsBrutos.getValue() != null) {
                    try {
                        bruto = Utils.formatearValorDouble(txtKgsBrutos.getValue().trim());
                    } catch (Exception e) {
                        bruto = Double.valueOf(0);
                    }
                }
                if (txtTara.getValue() != null) {
                    try {
                        tara = Utils.formatearValorDouble(txtTara.getValue().trim());
                    } catch (Exception e) {
                        tara = Double.valueOf(0);
                    }
                }

                neto = bruto - tara;

                txtKgsNetos.setValue(df.format(neto));

            }
        });

        // La tara.
        txtTara = new TextField("Tara:");
        txtTara.setNullRepresentation("");
        txtTara.setWidth(appWidth, Sizeable.Unit.EM);
        txtTara.setValue(df.format(nPesajes.getTara()));
        txtTara.setRequired(true);

        txtTara.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                DecimalFormat df = new DecimalFormat("#,##0.000");
                bruto = Double.valueOf(0);
                tara = Double.valueOf(0);
                neto = Double.valueOf(0);
                if (txtKgsBrutos.getValue() != null) {
                    try {
                        bruto = Utils.formatearValorDouble(txtKgsBrutos.getValue().trim());
                    } catch (Exception e) {
                        bruto = Double.valueOf(0);
                    }
                }
                if (txtTara.getValue() != null) {
                    try {
                        tara = Utils.formatearValorDouble(txtTara.getValue().trim());
                    } catch (Exception e) {
                        tara = Double.valueOf(0);
                    }
                }

                neto = bruto - tara;

                if (neto < Double.valueOf(0)) {
                    Notification aviso = new Notification("Los kilos brutos introducidos menos la tara da negativo", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }

                txtKgsNetos.setValue(df.format(neto));

            }
        });

        // Los kgs netos.
        txtKgsNetos = new TextField("Kilos netos:");
        txtKgsNetos.setNullRepresentation("");
        txtKgsNetos.setWidth(appWidth / 2, Sizeable.Unit.EM);
        txtKgsNetos.setRequired(true);
        txtKgsNetos.setEnabled(false);
        txtKgsNetos.setValue(df.format(nPesajes.getKgsNeto()));

        // El cliente.
        cbClientes.setCaption("Cliente:");
        cbClientes.addItems(lClientes);
        cbClientes.setFilteringMode(FilteringMode.CONTAINS);
        cbClientes.setRequired(true);
        cbClientes.setNullSelectionAllowed(false);
        cbClientes.setWidth(appWidth, Sizeable.Unit.EM);
        for (TClientes cl : lClientes) {
            if (cl.getId().equals(nPesajes.getIdCliente())) {
                cbClientes.setValue(cl);
                lTodasDirs = contrVista.obtenerDireccionesClientePorIdCliente(cl.getId(), user, time);
                break;
            }
        }

        // El material.
        cbMateriales.setCaption("Material:");
        cbMateriales.addItems(lMateriales);
        cbMateriales.setFilteringMode(FilteringMode.CONTAINS);
        cbMateriales.setRequired(true);
        cbMateriales.setNullSelectionAllowed(false);
        cbMateriales.setWidth(appWidth, Sizeable.Unit.EM);
        for (TMateriales ml : lMateriales) {
            if (ml.getId().equals(nPesajes.getIdMaterial())) {
                cbMateriales.setValue(ml);
                break;
            }
        }

        // El operador.
        cbOperadores.setCaption("Operador:");
        cbOperadores.addItems(lOperadores);
        cbOperadores.setFilteringMode(FilteringMode.CONTAINS);
        cbOperadores.setRequired(true);
        cbOperadores.setNullSelectionAllowed(false);
        cbOperadores.setWidth(appWidth, Sizeable.Unit.EM);
        for (TOperadores ml : lOperadores) {
            if (ml.getId().equals(nPesajes.getIdOperador())) {
                cbOperadores.setValue(ml);
                break;
            }
        }

        // El transportista.
        cbTransportistas.setCaption("Transportista:");
        cbTransportistas.addItems(lTransportistas);
        cbTransportistas.setFilteringMode(FilteringMode.CONTAINS);
        cbTransportistas.setRequired(true);
        cbTransportistas.setNullSelectionAllowed(false);
        cbTransportistas.setWidth(appWidth, Sizeable.Unit.EM);
        for (TTransportistas ml : lTransportistas) {
            if (ml.getId().equals(nPesajes.getIdTransportista())) {
                cbTransportistas.setValue(ml);
                break;
            }
        }

        cbEstados.setCaption("Estado:");
        cbEstados.addItem(Constants.ALBARAN);
        cbEstados.addItem(Constants.ANULADO);
        Integer est = nPesajes.getEstado();
        if (est.equals(PesajesEnum.ALBARAN.getValue())) {
            cbEstados.setValue(Constants.ALBARAN);
        } else if (est.equals(PesajesEnum.FACTURADO.getValue())) {
            cbEstados.addItem(Constants.FACTURADO);
            cbEstados.setValue(Constants.FACTURADO);
            cbEstados.setEnabled(false);
        } else if (est.equals(PesajesEnum.ANULADO.getValue())) {
            cbEstados.setValue(Constants.ANULADO);
        }
        cbEstados.setFilteringMode(FilteringMode.CONTAINS);
        cbEstados.setRequired(true);
        cbEstados.setNullSelectionAllowed(false);
        cbEstados.setWidth(appWidth / 2, Sizeable.Unit.EM);

        cbDirecciones.addStyleName("big");
        cbDirecciones.setFilteringMode(FilteringMode.CONTAINS);
        cbDirecciones.setRequired(true);
        cbDirecciones.setNullSelectionAllowed(false);
        cbDirecciones.setWidth(appWidth, Sizeable.Unit.EM);
        for (TDireccionCliente dir : lTodasDirs) {
            if (dir.getId().equals(nPesajes.getIdDireccion())) {
                cbDirecciones.setValue(dir);
                break;
            }
        }

        // El 190 es el KEYCODE del punto "."
        txtKgsNetos.addShortcutListener(new ShortcutListener(null, 190, null) {
            private static final long serialVersionUID = 1L;

            @Override
            public void handleAction(Object sender, Object target) {

            }

        });

        txtKgsNetos.addShortcutListener(new ShortcutListener(null, 110, null) {
            private static final long serialVersionUID = 1L;

            @Override
            public void handleAction(Object sender, Object target) {

            }

        });

        // El 190 es el KEYCODE del punto "."
        txtKgsBrutos.addShortcutListener(new ShortcutListener(null, 190, null) {
            private static final long serialVersionUID = 1L;

            @Override
            public void handleAction(Object sender, Object target) {

            }

        });

        txtKgsBrutos.addShortcutListener(new ShortcutListener(null, 110, null) {
            private static final long serialVersionUID = 1L;

            @Override
            public void handleAction(Object sender, Object target) {

            }

        });

        // El 190 es el KEYCODE del punto "."
        txtTara.addShortcutListener(new ShortcutListener(null, 190, null) {
            private static final long serialVersionUID = 1L;

            @Override
            public void handleAction(Object sender, Object target) {

            }

        });

        txtTara.addShortcutListener(new ShortcutListener(null, 110, null) {
            private static final long serialVersionUID = 1L;

            @Override
            public void handleAction(Object sender, Object target) {

            }

        });
    }

    /**
     * Método que es llamado para crear el objeto bean para la creación.
     */
    private void construirBean() throws GenasoftException {
        cambios = "";
        DecimalFormat df = new DecimalFormat("#,##0.000");
        String est = (String) cbEstados.getValue();

        Integer estado = 0;

        if (est.equals(Constants.ALBARAN)) {
            estado = PesajesEnum.ALBARAN.getValue();
        } else if (est.equals(Constants.FACTURADO)) {
            estado = PesajesEnum.FACTURADO.getValue();
        } else if (est.equals(Constants.ANULADO)) {
            estado = PesajesEnum.ANULADO.getValue();
        }

        if (!nPesajes.getEstado().equals(estado)) {
            cambios = "Se cambia el estado del pesaje, pasa de " + nPesajes.getEstado() + " a " + estado;
        }
        nPesajes.setEstado(estado);

        String value = txtObra.getValue();

        if (value != null) {
            value = value.trim().toUpperCase();
        }
        if (value == null && nPesajes.getObra() != null) {
            cambios = cambios + "\n Se le quita la obra, antes tenia: " + nPesajes.getObra();
        } else if (value != null && nPesajes.getObra() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna una obra, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nPesajes.getObra())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia la obra, antes tenia: " + nPesajes.getObra() + " y ahora tiene: " + value;
        }

        nPesajes.setObra(value);

        value = txtOrigen.getValue();

        if (value != null) {
            value = value.trim().toUpperCase();
        }

        if (value == null && nPesajes.getOrigen() != null) {
            cambios = cambios + "\n Se le quita el origen, antes tenia: " + nPesajes.getOrigen();
        } else if (value != null && nPesajes.getOrigen() == null) {
            cambios = cambios + "\n Se le asigna un nuevo origen,  antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nPesajes.getOrigen())) {
            cambios = cambios + "\n Se le cambia el origen, antes tenia: " + nPesajes.getOrigen() + " y ahora tiene: " + value;
        }

        nPesajes.setOrigen(value);

        value = txtDestino.getValue();

        if (value == null && nPesajes.getDestino() != null) {
            cambios = cambios + "\n Se le quita el destino, antes tenia: " + nPesajes.getDestino();
        } else if (value != null && nPesajes.getDestino() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna un nuevo destino, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nPesajes.getDestino())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia el destino, antes tenia: " + nPesajes.getDestino() + " y ahora tiene: " + value;
        }

        nPesajes.setDestino(value);

        value = txtAlbaran.getValue();

        if (value == null && nPesajes.getNumeroAlbaran() != null) {
            cambios = cambios + "\n Se le quita el número de albarán, antes tenia: " + nPesajes.getNumeroAlbaran();
        } else if (value != null && nPesajes.getNumeroAlbaran() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna un nuevo número de albarán, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nPesajes.getNumeroAlbaran())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia el número de albarán, antes tenia: " + nPesajes.getNumeroAlbaran() + " y ahora tiene: " + value;
        }

        nPesajes.setNumeroAlbaran(value);

        value = txtMatricula.getValue();

        if (value == null && nPesajes.getMatricula() != null) {
            cambios = cambios + "\n Se le quita la matrícula, antes tenia: " + nPesajes.getMatricula();
        } else if (value != null && nPesajes.getMatricula() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna una nueva matrícula, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nPesajes.getMatricula())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia la matrícula, antes tenia: " + nPesajes.getMatricula() + " y ahora tiene: " + value;
        }

        nPesajes.setMatricula(value);

        value = txtRemolque.getValue();

        if (value == null && nPesajes.getRemolque() != null) {
            cambios = cambios + "\n Se le quita el remolque, antes tenia: " + nPesajes.getRemolque();
        } else if (value != null && nPesajes.getRemolque() == null) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le asigna un nuevo remolque, antes no tenía tenia, ahora tiene:  " + value;
        } else if (value != null && !value.equals(nPesajes.getRemolque())) {
            value = value.trim().toUpperCase();
            cambios = cambios + "\n Se le cambia el remolque, antes tenia: " + nPesajes.getRemolque() + " y ahora tiene: " + value;
        }

        nPesajes.setRemolque(value);

        Double val = Utils.formatearValorDouble(txtKgsBrutos.getValue().trim());

        if (!val.equals(nPesajes.getKgsBruto())) {
            cambios = cambios + "\n Se le cambian los kilos brutos, antes tenia: " + df.format(nPesajes.getKgsBruto()) + " y ahora tiene: " + df.format(val);
            nPesajes.setKgsBruto(val);
        }

        val = Utils.formatearValorDouble(txtTara.getValue().trim());

        if (!val.equals(nPesajes.getTara())) {
            cambios = cambios + "\n Se le cambian los kilos de TARA, antes tenia: " + df.format(nPesajes.getTara()) + " y ahora tiene: " + df.format(val);
            nPesajes.setTara(val);
        }

        val = Utils.formatearValorDouble(txtKgsNetos.getValue().trim());

        if (!val.equals(nPesajes.getKgsNeto())) {
            cambios = cambios + "\n Se le cambian los kilos de netos, antes tenia: " + df.format(nPesajes.getKgsNeto()) + " y ahora tiene: " + df.format(val);
            nPesajes.setKgsNeto(val);
        }

        TClientes cl = (TClientes) cbClientes.getValue();

        if (cl.getId().equals(nPesajes.getIdCliente())) {
            cambios = cambios + "\n Se le cambia el cliente antes tenia: " + nPesajes.getIdCliente() + " y ahora tiene: " + cl.getId();
            nPesajes.setIdCliente(cl.getId());
        }

        TDireccionCliente dir = (TDireccionCliente) cbDirecciones.getValue();

        if (dir.getId().equals(nPesajes.getIdDireccion())) {
            cambios = cambios + "\n Se le cambia la dirección del cliente antes tenia: " + nPesajes.getIdDireccion() + " y ahora tiene: " + dir.getId();
            nPesajes.setIdDireccion(cl.getId());
        }

        TMateriales mat = (TMateriales) cbMateriales.getValue();

        if (mat.getId().equals(nPesajes.getIdMaterial())) {
            cambios = cambios + "\n Se le cambia el material antes tenia: " + nPesajes.getIdMaterial() + " y ahora tiene: " + mat.getId();
            nPesajes.setIdMaterial(cl.getId());
            nPesajes.setLerMaterial(mat.getLer());
            nPesajes.setRefMaterial(mat.getReferencia());
        }

        TIva iva = contrVista.obtenerIvaPorId(mat.getIva(), user, time);

        nPesajes.setBase(Utils.redondeoDecimales(2, mat.getPrecio() * nPesajes.getKgsNeto()));

        nPesajes.setIva(iva.getImporte());
        val = (nPesajes.getBase() * nPesajes.getIva()) / 100;
        val = Utils.redondeoDecimales(2, val + nPesajes.getBase());
        nPesajes.setImporte(val);
        nPesajes.setPrecioKg(mat.getPrecio());

        if (!cambios.isEmpty()) {
            nPesajes.setFechaModifica(Utils.generarFecha());
            nPesajes.setUsuModifica(user);
        }

    }

    /**
     * Comprobamos si ha informado los campos obligatorios.
     * @return true si no se cumple la validación
     */
    private boolean validarCamposObligatorios() {
        return !cbClientes.isValid() || !cbMateriales.isValid() || !txtOrigen.isValid() || !txtObra.isValid() || !txtRemolque.isValid() || !txtKgsBrutos.isValid() || !txtMatricula.isValid() || !fechaPesaje.isValid() || !cbDirecciones.isValid()
                || !fechaPesaje.isValid();
    }

}
