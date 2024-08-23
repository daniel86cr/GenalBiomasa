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
import com.dina.genasoft.db.entity.TOperacionActual;
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TPesajes;
import com.dina.genasoft.db.entity.TTransportistas;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.PesajesEnum;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
import com.dina.genasoft.vistas.maestros.clientes.VistaNuevoCliente;
import com.dina.genasoft.vistas.maestros.materiales.VistaNuevoMaterial;
import com.dina.genasoft.vistas.maestros.operadores.VistaNuevoOperador;
import com.dina.genasoft.vistas.maestros.transportistas.VistaNuevoTransportista;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ReadOnlyException;
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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

/**
 * @author Daniel Carmona Romero
 * Vista para registrar un nuevo pesaje.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaNuevoPesaje.NAME)
public class VistaNuevoPesaje extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;
    /** El nombre de la vista.*/
    public static final String            NAME     = "nuevoPesaje";
    /** El boton para registrar el pesaje.*/
    private Button                        crearButton;
    /** Combobox para los clientes.*/
    private ComboBox                      cbClientes;
    /** Combobox para los materiales.*/
    private ComboBox                      cbMateriales;
    /** Combobox para las direcciones.*/
    private ComboBox                      cbDirecciones;
    /** Combobox para los operadores.*/
    private ComboBox                      cbOperadores;
    /** Combobox para los operadores.*/
    private ComboBox                      cbTransportistas;
    /** Combobox para los operadores.*/
    private ComboBox                      cbMatriculas;
    /** Combobox para los operadores.*/
    private ComboBox                      cbRemolques;
    /** El pesaje a registrar.*/
    private TPesajes                      nPesaje;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                       appWidth;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log      = org.slf4j.LoggerFactory.getLogger(VistaNuevoPesaje.class);
    // Los campos obligatorios
    /** La caja de texto para la obra .*/
    private TextField                     txtAlbaran;
    /** La caja de texto para la obra .*/
    private TextField                     txtObra;
    /** La caja de texto para el origen.*/
    private TextField                     txtOrigen;
    /** La caja de texto para el destino.*/
    private TextField                     txtDestino;
    /** La caja de texto para los kilos brutos.*/
    private TextField                     txtKgsBrutos;
    /** La caja de texto para la tara.*/
    private TextField                     txtTara;
    /** La caja de texto para los kilos netos.*/
    private TextField                     txtKgsNetos;
    /** La caja de texto para las observaciones.*/
    private TextArea                      txtObservaciones;
    /** La fecha del pesaje. */
    private DateField                     fechaPesaje;
    /** Los permisos del empleado actual. */
    private TPermisos                     permisos = null;
    /** El usuario que está logado. */
    private Integer                       user     = null;
    /** La fecha en que se inició sesión. */
    private Long                          time     = null;
    private TEmpleados                    empleado;
    /** Los clientes activos del sistema.*/
    private List<TClientes>               lClientes;
    /** Los materiales activos del sistema.*/
    private List<TMateriales>             lMateriales;
    /** Los operadores activos del sistema.*/
    private List<TOperadores>             lOperadores;
    /** Los transportistas activos del sistema.*/
    private List<TTransportistas>         lTransportistas;
    /** Las matrículas asociadas al cliente.*/
    private List<String>                  lMatriculas;
    /** Los remolques asociados al cliente.*/
    private List<String>                  lRemolques;
    private Double                        bruto, tara, neto;
    private String                        name;

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(crearButton)) {
            // Creamos el evento para crear un nuevo transportista con los datos introducidos en el formulario
            try {
                if (validarCamposObligatorios()) {
                    Notification aviso = new Notification("Se debe informar los campos marcados con '*'", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                if (neto < Double.valueOf(0)) {
                    Notification aviso = new Notification("Los kilos brutos introducidos menos la tara da negativo", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                // Construimos el objeto transportista a partir de los datos introducidos en el formulario.
                construirBean();
                String result = contrVista.crearPesaje(nPesaje, user, time);
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

                empleado = contrVista.obtenerEmpleadoPorId(user, user, time);

                permisos = contrVista.obtenerPermisosEmpleado(empleado, user, time);

                lMateriales = Utils.generarListaGenerica();

                lTransportistas = Utils.generarListaGenerica();

                lMatriculas = Utils.generarListaGenerica();

                lRemolques = Utils.generarListaGenerica();

                bruto = Double.valueOf(0);
                tara = Double.valueOf(0);
                neto = Double.valueOf(0);

                if (permisos == null) {
                    Notification aviso = new Notification("No se ha podido identificar los permisos asignados, contacta con el administrador.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    return;
                }

                if (!Utils.booleanFromInteger(permisos.getCrearPesaje())) {
                    Notification aviso = new Notification("No se tienen permisos para acceder a la pantalla indicada", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                    return;
                }

                lClientes = contrVista.obtenerClientesActivos(user, time);

                // Creamos los botones de la pantalla.
                crearBotones();

                // Creamos los combos de la pantalla.
                crearCombos();

                // Creamos los botones de la pantalla.
                crearBotones();

                // Creamos los componetes que conforman la pantalla.
                crearComponentes();

                Label texto = new Label("Registro de pesaje");
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

                HorizontalLayout horKgs = new HorizontalLayout();
                horKgs.setSpacing(true);

                horKgs.addComponent(txtKgsBrutos);
                horKgs.addComponent(txtTara);

                HorizontalLayout horCl = new HorizontalLayout();
                horCl.setSpacing(true);

                horCl.addComponent(cbClientes);
                horCl.addComponent(cbDirecciones);

                // Formulario con los campos que componen el empleado.
                VerticalLayout formulario1 = new VerticalLayout();
                formulario1.setSpacing(true);

                VerticalLayout formulario2 = new VerticalLayout();
                formulario2.setSpacing(true);

                viewLayout.addComponent(fechaPesaje);
                viewLayout.setComponentAlignment(fechaPesaje, Alignment.MIDDLE_CENTER);
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
                //formulario2.addComponent(txtDestino);
                //formulario2.setComponentAlignment(txtDestino, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(cbMatriculas);
                formulario2.setComponentAlignment(cbMatriculas, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(cbRemolques);
                formulario2.setComponentAlignment(cbRemolques, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(cbTransportistas);
                formulario2.setComponentAlignment(cbTransportistas, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtKgsBrutos);
                formulario2.setComponentAlignment(txtKgsBrutos, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtTara);
                formulario2.setComponentAlignment(txtTara, Alignment.MIDDLE_CENTER);
                formulario2.addComponent(txtKgsNetos);
                formulario2.setComponentAlignment(txtKgsNetos, Alignment.MIDDLE_CENTER);
                body.addComponent(formulario1);
                body.setComponentAlignment(formulario1, Alignment.MIDDLE_CENTER);
                body.addComponent(formulario2);
                body.setComponentAlignment(formulario2, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(body);
                viewLayout.setComponentAlignment(body, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(txtObservaciones);
                viewLayout.setComponentAlignment(txtObservaciones, Alignment.MIDDLE_CENTER);
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
        crearButton = new Button("Registrar pesaje", this);
        crearButton.addStyleName("big");

    }

    /**
     * Método para crear los combos de la pantalla.
     */
    private void crearCombos() {
        // ComboBox para los clientes.
        cbClientes = new ComboBox();
        cbClientes.addStyleName("big");
        cbClientes.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbClientes.getValue() != null) {
                    if (cbClientes.getValue().getClass().equals(TClientes.class)) {
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
                            } else if (lOperadores.isEmpty()) {
                                Notification aviso = new Notification("No se han identificado operadores asignados al cliente seleccionado", Notification.Type.ERROR_MESSAGE);
                                aviso.setPosition(Position.MIDDLE_CENTER);
                                aviso.show(Page.getCurrent());
                            }

                            lTransportistas = contrVista.obtenerTransportistasAsignadosCliente(cl.getId(), user, time);
                            cbTransportistas.removeAllItems();
                            cbTransportistas.addItems(lTransportistas);
                            if (lTransportistas.size() == 1) {
                                cbTransportistas.setValue(lTransportistas.get(0));
                            } else if (lTransportistas.isEmpty()) {
                                Notification aviso = new Notification("No se han identificado transportistas asignados al cliente seleccionado", Notification.Type.ERROR_MESSAGE);
                                aviso.setPosition(Position.MIDDLE_CENTER);
                                aviso.show(Page.getCurrent());
                            }

                            lMatriculas = contrVista.obtenerMatriculasAsignadasCliente(cl.getId(), user, time);
                            cbMatriculas.removeAllItems();
                            cbMatriculas.addItems(lMatriculas);
                            if (lMatriculas.size() == 1) {
                                cbMatriculas.setValue(lMatriculas.get(0));
                            } else if (lMatriculas.isEmpty()) {
                                Notification aviso = new Notification("No se han identificado matrículas asignadas al cliente seleccionado", Notification.Type.ERROR_MESSAGE);
                                aviso.setPosition(Position.MIDDLE_CENTER);
                                aviso.show(Page.getCurrent());
                            }

                            lRemolques = contrVista.obtenerRemolquesAsignadosCliente(cl.getId(), user, time);
                            cbRemolques.removeAllItems();
                            cbRemolques.addItems(lRemolques);
                            if (lRemolques.size() == 1) {
                                cbRemolques.setValue(lRemolques.get(0));
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
                        try {
                            name = (String) cbClientes.getValue();
                            // Buscamos si existe el cliente por nombre
                            TClientes cl = contrVista.obtenerClientePorNombre(name.trim().toUpperCase(), user, time);
                            if (cl == null) {
                                if (name.contains(" ")) {
                                    name = name.replaceAll(" ", "_");
                                }
                                MessageBox.createQuestion().withCaption(appName).withMessage("¿Quieres crear un nuevo cliente?").withNoButton().withYesButton(() ->

                                Page.getCurrent().open("#!" + VistaNuevoCliente.NAME + "/" + user + "," + name, "_blank"), ButtonOption.caption("Sí")).open();
                                cbOperadores.clear();
                            } else {
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
                                } else if (lOperadores.isEmpty()) {
                                    Notification aviso = new Notification("No se han identificado operadores asignados al cliente seleccionado", Notification.Type.ERROR_MESSAGE);
                                    aviso.setPosition(Position.MIDDLE_CENTER);
                                    aviso.show(Page.getCurrent());
                                }

                                lTransportistas = contrVista.obtenerTransportistasAsignadosCliente(cl.getId(), user, time);
                                cbTransportistas.removeAllItems();
                                cbTransportistas.addItems(lTransportistas);
                                if (lTransportistas.size() == 1) {
                                    cbTransportistas.setValue(lTransportistas.get(0));
                                } else if (lTransportistas.isEmpty()) {
                                    Notification aviso = new Notification("No se han identificado transportistas asignados al cliente seleccionado", Notification.Type.ERROR_MESSAGE);
                                    aviso.setPosition(Position.MIDDLE_CENTER);
                                    aviso.show(Page.getCurrent());
                                }

                                lMatriculas = contrVista.obtenerMatriculasAsignadasCliente(cl.getId(), user, time);
                                cbMatriculas.removeAllItems();
                                cbMatriculas.addItems(lMatriculas);
                                if (lMatriculas.size() == 1) {
                                    cbMatriculas.setValue(lMatriculas.get(0));
                                } else if (lMatriculas.isEmpty()) {
                                    Notification aviso = new Notification("No se han identificado matrículas asignadas al cliente seleccionado", Notification.Type.ERROR_MESSAGE);
                                    aviso.setPosition(Position.MIDDLE_CENTER);
                                    aviso.show(Page.getCurrent());
                                }

                                lRemolques = contrVista.obtenerRemolquesAsignadosCliente(cl.getId(), user, time);
                                cbRemolques.removeAllItems();
                                cbRemolques.addItems(lRemolques);
                                if (lRemolques.size() == 1) {
                                    cbRemolques.setValue(lRemolques.get(0));
                                }
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
            }
        });

        // ComboBox para los materiales.
        cbMateriales = new ComboBox();
        cbMateriales.addStyleName("big");

        cbMateriales.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbMateriales.getValue() != null && !cbMateriales.getValue().getClass().equals(TMateriales.class)) {
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
        });

        // ComboBox para las direcciones
        cbDirecciones = new ComboBox("Dirección:");
        cbDirecciones.addStyleName("big");
        cbDirecciones.setFilteringMode(FilteringMode.CONTAINS);
        cbDirecciones.setRequired(true);
        cbDirecciones.setNullSelectionAllowed(false);
        cbDirecciones.setWidth(appWidth, Sizeable.Unit.EM);

        cbOperadores = new ComboBox("Operador:");
        cbOperadores.addStyleName("big");
        cbOperadores.setFilteringMode(FilteringMode.CONTAINS);
        cbOperadores.setRequired(true);
        cbOperadores.setNullSelectionAllowed(false);
        cbOperadores.setWidth(appWidth, Sizeable.Unit.EM);

        cbOperadores.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbOperadores.getValue() != null) {
                    if (!cbOperadores.getValue().getClass().equals(TOperadores.class)) {
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
            }
        });

        cbTransportistas = new ComboBox("Transportista:");
        cbTransportistas.addItems(lTransportistas);
        cbTransportistas.addStyleName("big");
        cbTransportistas.setFilteringMode(FilteringMode.CONTAINS);
        cbTransportistas.setRequired(true);
        cbTransportistas.setNullSelectionAllowed(false);
        cbTransportistas.setWidth(appWidth, Sizeable.Unit.EM);

        cbTransportistas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbTransportistas.getValue() != null) {
                    if (!cbTransportistas.getValue().getClass().equals(TTransportistas.class)) {
                        try {
                            name = (String) cbTransportistas.getValue();
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
            }
        });

        cbMatriculas = new ComboBox("Matrícula:");
        cbMatriculas.addItems(lMatriculas);
        cbMatriculas.addStyleName("big");
        cbMatriculas.setFilteringMode(FilteringMode.CONTAINS);
        cbMatriculas.setRequired(true);
        cbMatriculas.setNullSelectionAllowed(false);
        cbMatriculas.setNewItemsAllowed(true);
        cbMatriculas.setWidth(appWidth, Sizeable.Unit.EM);

        cbRemolques = new ComboBox("Remolque:");
        cbRemolques.addItems(lRemolques);
        cbRemolques.addStyleName("big");
        cbRemolques.setFilteringMode(FilteringMode.CONTAINS);
        cbRemolques.setNullSelectionAllowed(false);
        cbRemolques.setNewItemsAllowed(true);
        cbRemolques.setWidth(appWidth, Sizeable.Unit.EM);

    }

    /**
     * Método que nos crea los componetes que conforman la pantalla.
     */
    private void crearComponentes() {
        //Los campos que componen un pesaje.

        fechaPesaje = new DateField("Fecha pesaje");
        fechaPesaje.setValue(Utils.generarFecha());
        fechaPesaje.setRequired(true);
        fechaPesaje.setWidth(appWidth, Sizeable.Unit.EM);

        // El nº de albarán
        txtAlbaran = new TextField("Nº Albarán:");
        txtAlbaran.setNullRepresentation("");
        txtAlbaran.setRequired(true);
        txtAlbaran.setWidth(appWidth, Sizeable.Unit.EM);
        txtAlbaran.setMaxLength(445);

        // La obra
        txtObra = new TextField("Obra:");
        txtObra.setNullRepresentation("");
        txtObra.setWidth(appWidth, Sizeable.Unit.EM);
        txtObra.setMaxLength(445);

        // El origen
        txtOrigen = new TextField("Origen:");
        txtOrigen.setNullRepresentation("");
        txtOrigen.setWidth(appWidth, Sizeable.Unit.EM);
        txtOrigen.setMaxLength(445);

        // El destino
        txtDestino = new TextField("Destino:");
        txtDestino.setNullRepresentation("");
        txtDestino.setWidth(appWidth, Sizeable.Unit.EM);
        txtDestino.setMaxLength(445);

        // Los Kgs brutos.
        txtKgsBrutos = new TextField("Kilos brutos:");
        txtKgsBrutos.setNullRepresentation("");
        txtKgsBrutos.setWidth(appWidth, Sizeable.Unit.EM);
        txtKgsBrutos.setRequired(true);
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
        txtKgsNetos.setWidth(appWidth, Sizeable.Unit.EM);
        txtKgsNetos.setRequired(true);
        txtKgsNetos.setEnabled(false);

        // El cliente.
        cbClientes.setCaption("Cliente:");
        cbClientes.addItems(lClientes);
        cbClientes.setFilteringMode(FilteringMode.CONTAINS);
        cbClientes.setRequired(true);
        cbClientes.setNullSelectionAllowed(false);
        cbClientes.setWidth(appWidth, Sizeable.Unit.EM);

        // El cliente.
        cbMateriales.setCaption("Material:");
        cbMateriales.addItems(lMateriales);
        cbMateriales.setFilteringMode(FilteringMode.CONTAINS);
        cbMateriales.setRequired(true);
        cbMateriales.setNullSelectionAllowed(false);
        cbMateriales.setWidth(appWidth, Sizeable.Unit.EM);

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

        txtObservaciones = new TextArea("Observaciones:");
        txtObservaciones.setNullRepresentation("");
        txtObservaciones.setWidth(appWidth, Sizeable.Unit.EM);

    }

    /**
     * Método que es llamado para crear el objeto bean para la creación.
     */
    private void construirBean() throws GenasoftException {

        TMateriales mat = (TMateriales) cbMateriales.getValue();
        TClientes cl = (TClientes) cbClientes.getValue();
        TDireccionCliente dir = (TDireccionCliente) cbDirecciones.getValue();

        nPesaje = new TPesajes();
        nPesaje.setDescMaterial(mat.getDescripcion());
        nPesaje.setDestino(txtDestino.getValue() != null ? txtDestino.getValue().trim().toUpperCase() : "");
        nPesaje.setEstado(PesajesEnum.ALBARAN.getValue());
        nPesaje.setFechaCrea(Utils.generarFecha());
        nPesaje.setFechaPesaje(fechaPesaje.getValue());
        nPesaje.setIdCliente(cl.getId());
        nPesaje.setIdDireccion(dir.getId());
        nPesaje.setIdFactura(-1);
        nPesaje.setIdMaterial(mat.getId());
        try {
            nPesaje.setKgsBruto(Utils.formatearValorDouble(txtKgsBrutos.getValue().trim()));
            if (nPesaje.getKgsBruto() <= Double.valueOf(0)) {
                throw new GenasoftException("No se ha introducido un valor numérico válido en el campo: " + txtKgsBrutos.getCaption());
            }
        } catch (Exception e) {
            throw new GenasoftException("No se ha introducido un valor numérico válido en el campo: " + txtKgsBrutos.getCaption());
        }
        try {
            nPesaje.setKgsNeto(Utils.formatearValorDouble(txtKgsNetos.getValue().trim()));

            if (nPesaje.getKgsNeto() <= Double.valueOf(0)) {
                throw new GenasoftException("No se ha introducido un valor numérico válido en el campo: " + txtKgsNetos.getCaption());
            }
        } catch (Exception e) {
            throw new GenasoftException("No se ha introducido un valor numérico válido en el campo: " + txtKgsNetos.getCaption());
        }

        try {
            nPesaje.setTara(Utils.formatearValorDouble(txtTara.getValue().trim()));

            if (nPesaje.getTara() <= Double.valueOf(0)) {
                throw new GenasoftException("No se ha introducido un valor numérico válido en el campo: " + txtTara.getCaption());
            }
        } catch (Exception e) {
            throw new GenasoftException("No se ha introducido un valor numérico válido en el campo: " + txtTara.getCaption());
        }

        nPesaje.setLerMaterial(mat.getLer());
        nPesaje.setMatricula(cbMatriculas.getValue() != null ? cbMatriculas.getValue().toString().trim().toUpperCase() : "");
        //nPesaje.setNumeroAlbaran(contrVista.obtenerNumeroAlbaran("" + PesajesEnum.TIPO_GENERICO.getValue(), user, time));
        nPesaje.setObra(txtObra.getValue() != null ? txtObra.getValue().trim().toUpperCase() : "");
        nPesaje.setOrigen(txtOrigen.getValue() != null ? txtOrigen.getValue().trim().toUpperCase() : "");
        nPesaje.setRefMaterial(mat.getReferencia());
        nPesaje.setRemolque(cbRemolques.getValue() != null ? cbRemolques.getValue().toString().trim().toUpperCase() : "");
        nPesaje.setUsuCrea(user);
        nPesaje.setIdOperador(((TOperadores) cbOperadores.getValue()).getId());
        nPesaje.setIdTransportista(((TTransportistas) cbTransportistas.getValue()).getId());

        TIva iva = contrVista.obtenerIvaPorId(mat.getIva(), user, time);

        nPesaje.setBase(Utils.redondeoDecimales(2, mat.getPrecio() * nPesaje.getKgsNeto()));
        nPesaje.setIva(iva.getImporte());
        nPesaje.setIdIva(iva.getId());
        Double val = (nPesaje.getBase() * nPesaje.getIva()) / 100;
        val = Utils.redondeoDecimales(2, val + nPesaje.getBase());
        nPesaje.setImporte(val);
        nPesaje.setPrecioKg(mat.getPrecio());
        nPesaje.setIndFirmaCliente(PesajesEnum.SIN_FIRMA.getValue());
        nPesaje.setObservaciones(txtObservaciones.getValue());

    }

    /**
     * Método que es llamado para inicializar los valores de los componentes.
     * @throws GenasoftException 
     * @throws ReadOnlyException 
     */
    private void inicializarCampos() throws ReadOnlyException, GenasoftException {
        txtAlbaran.setValue(null);
        txtOrigen.setValue(null);
        cbMatriculas.removeAllItems();
        txtObra.setValue(null);
        cbClientes.setValue(Constants.ACTIVO);
        cbRemolques.removeAllItems();
        txtKgsBrutos.setValue(null);
        cbClientes.clear();
        cbMateriales.clear();
        cbDirecciones.removeAllItems();
        txtDestino.setValue(null);
        txtKgsBrutos.setValue(null);
        txtTara.setValue(null);
        cbOperadores.removeAllItems();
        cbTransportistas.removeAllItems();
        txtObservaciones.setValue(null);
    }

    /**
     * Comprobamos si ha informado los campos obligatorios.
     * @return true si no se cumple la validación
     */
    private boolean validarCamposObligatorios() {
        return !cbClientes.isValid() || !cbMateriales.isValid() || !txtKgsBrutos.isValid() || !cbMatriculas.isValid() || !fechaPesaje.isValid() || !cbDirecciones.isValid() || !fechaPesaje.isValid() || !cbTransportistas.isValid()
                || !cbOperadores.isValid();
    }
}
