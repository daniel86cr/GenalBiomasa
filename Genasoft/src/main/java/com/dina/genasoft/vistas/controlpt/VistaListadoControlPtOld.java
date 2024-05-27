/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.controlpt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TColumnasTablasEmpleado;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesBrixVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCajaVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCalibreVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesConfeccionVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesPenetromiaVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoVista;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TProductos;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.TablaGenerica;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.RolesEnum;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
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
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaListadoControlPtOld.NAME)
public class VistaListadoControlPtOld extends CustomComponent implements View ,Button.ClickListener {
    /** El nombre de la vista.*/
    public static final String                                                     NAME                   = "vLControlPtOld";
    /** Necesario para mostrar los controles de PT*/
    private BeanContainer<String, TControlProductoTerminadoVista>                  bcControlPt;
    /** Necesario para mostrar los controles de PT de pesajes de caja. */
    private BeanContainer<String, TControlProductoTerminadoPesajesCajaVista>       bcPesajesCajas;
    /** Necesario para mostrar los controles de PT de pesajes de calibre. */
    private BeanContainer<String, TControlProductoTerminadoPesajesCalibreVista>    bcPesajesCalibres;
    /** Necesario para mostrar los controles de PT de pesajes de confección. */
    private BeanContainer<String, TControlProductoTerminadoPesajesConfeccionVista> bcPesajesConfeccion;
    /** Necesario para mostrar los controles de PT de pesajes de BRIX. */
    private BeanContainer<String, TControlProductoTerminadoPesajesBrixVista>       bcPesajesBrix;
    /** Necesario para mostrar los controles de PT de pesajes de penetromia. */
    private BeanContainer<String, TControlProductoTerminadoPesajesPenetromiaVista> bcPesajesPenetromia;
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas                                                      contrVista;
    /** El boton para crear un control de PT.*/
    private Button                                                                 crearButton;
    /** El boton para crear un control de PT.*/
    private Button                                                                 modificarButton;
    /** El boton para crear una empresa.*/
    private Button                                                                 guardarColumnasTablaButton;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                                                                 pdfButton;
    /** La fecha 'desde' para filtrar controles de PT*/
    private DateField                                                              f1;
    /** La fecha 'hasta' para filtrar controles de PT*/
    private DateField                                                              f2;
    // Elementos para realizar busquedas    
    /** Para filtrar por el nombre del proveedor.*/
    private TextField                                                              fNumPedido;
    /** Para filtrar por el producto del tipo del control de PT. */
    private ComboBox                                                               fProductos;
    /** Para filtrar por el cliente del tipo del control de PT. */
    private ComboBox                                                               fClientes;
    /** Para filtrar por estado. */
    private ComboBox                                                               fEstados;
    /** Para filtrar por el producto del tipo del control de PT. */
    private ComboBox                                                               fEmpleados;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                                                                 appName;
    /** El ID del control de producto terminado seleccionado.*/
    private String                                                                 idSeleccionado;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger                                          log                    = org.slf4j.LoggerFactory.getLogger(VistaListadoControlPtOld.class);
    /** El usuario que está logado. */
    private Integer                                                                user                   = null;
    /** La fecha en que se inició sesión. */
    private Long                                                                   time                   = null;
    /** El filtro que se le aplica al container. */
    private FiltroContainer                                                        filter;
    /** No tengo ni puta idea para que sirve. */
    private final Label                                                            status                 = new Label("");
    /** Tabla para mostrar los controles de producto terminado del sistema. */
    private Table                                                                  tablaControlPt         = null;
    /** Tabla para mostrar la leyenda de colores de la tabla de control de producto terminado. */
    private Table                                                                  tablaColores           = null;
    /** Tabla para mostrar los pesajes de las cajas. */
    private Table                                                                  tablaPesajesCajas      = null;
    /** Tabla para mostrar los pesajes de los calibres. */
    private Table                                                                  tablaPesajesCalibres   = null;
    /** Tabla para mostrar los pesajes de las confecciones. */
    private Table                                                                  tablaPesajesConfeccion = null;
    /** Tabla para mostrar los valores de BRIX. */
    private Table                                                                  tablaBrix              = null;
    /** Tabla para mostrar los valores de penetromía. */
    private Table                                                                  tablaPenetromia        = null;
    /** Para indicar que se está filtrando por el campo nombre. */
    private final String                                                           NUM_PEDIDO             = "numPedido";
    /** El empleado que ha iniciado sesión en la aplicación . */
    private TEmpleados                                                             empleado;
    /** Los permisos del empleado que ha iniciado sesión . */
    private TPermisos                                                              permisos;
    /** Diccionario con las columnas que se van a mostrar en la tabla de control de producto terminado. */
    private Map<String, String>                                                    mColumnasTablaControlPt;
    /** Diccionario con las columnas que se van a mostrar en la tabla de control de producto terminado. */
    private Map<String, String>                                                    mColumnasIdsControlPt;
    /** Diccionario con las columnas que se van a mostrar en la tabla. */
    private Map<String, String>                                                    mColumnasGuardadoTablaControlPt;
    /** Contendrá el número de fotos que debe tener de palé. */
    @Value("${app.pt.fotos.pale}")
    private Integer                                                                appPtFotosPale;
    /** Contendrá el número de fotos que debe tener de cajas .*/
    @Value("${app.pt.fotos.cajas}")
    private Integer                                                                appPtFotosCajas;
    /** Contendrá el número de fotos que debe tener de etiquetas internas .*/
    @Value("${app.pt.fotos.etiqueta1}")
    private Integer                                                                appPtFotosEtiqueta1;
    /** Contendrá el número de fotos que debe tener de etiquetas externas .*/
    @Value("${app.pt.fotos.etiqueta2}")
    private Integer                                                                appPtFotosEtiqueta2;
    /** Contendrá el número de pesajes que se deben realizar en las confecciones .*/
    @Value("${app.pt.pesajes.confeccion}")
    private Integer                                                                appPtPesajesConfeccion;
    /** Contendrá el número de pesajes que se deben realizar en las cajas .*/
    @Value("${app.pt.pesajes.caja}")
    private Integer                                                                appPtPesajesCaja;
    /** Contendrá el número de pesajes que se deben realizar en los calibres .*/
    @Value("${app.pt.pesajes.calibre}")
    private Integer                                                                appPtPesajesCalibre;
    private List<TProductos>                                                       lProductos             = null;
    private List<TClientes>                                                        lClientes              = null;
    private List<TEmpleados>                                                       lEmpleados             = null;
    private VerticalLayout                                                         vTablaPesCaja;
    private VerticalLayout                                                         vTablaPesConf;
    private VerticalLayout                                                         vTablaPesCalibre;

    /**
     * Se inicializan botones, eventos, etc.
     */
    @PostConstruct
    void init() {

        setSizeFull();
        // Establecemos el tamaño de la pantalla.
        setHeightUndefined();
        // Creamos los botones de la pantalla.
        crearBotones();
        // Se añaden eventos a los botones
        eventosBotones();
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
                idSeleccionado = null;
                lProductos = Utils.generarListaGenerica();
                lClientes = Utils.generarListaGenerica();
                lEmpleados = Utils.generarListaGenerica();
                bcControlPt = new BeanContainer<>(TControlProductoTerminadoVista.class);
                bcControlPt.setBeanIdProperty("id");

                bcPesajesCajas = new BeanContainer<>(TControlProductoTerminadoPesajesCajaVista.class);
                bcPesajesCajas.setBeanIdProperty("id");

                bcPesajesCalibres = new BeanContainer<>(TControlProductoTerminadoPesajesCalibreVista.class);
                bcPesajesCalibres.setBeanIdProperty("id");

                bcPesajesConfeccion = new BeanContainer<>(TControlProductoTerminadoPesajesConfeccionVista.class);
                bcPesajesConfeccion.setBeanIdProperty("id");

                bcPesajesBrix = new BeanContainer<>(TControlProductoTerminadoPesajesBrixVista.class);
                bcPesajesBrix.setBeanIdProperty("id");

                bcPesajesPenetromia = new BeanContainer<>(TControlProductoTerminadoPesajesPenetromiaVista.class);
                bcPesajesPenetromia.setBeanIdProperty("id");

                Label texto = new Label("Listado Control de Producto Terminado");
                texto.setStyleName("tituloTamano18");
                // Incluimos en el layout los componentes
                VerticalLayout titulo = new VerticalLayout(texto);

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

                if (!Utils.booleanFromInteger(permisos.getListarControlPt())) {
                    Notification aviso = new Notification("No se tienen permisos para acceder a la pantalla indicada", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                    return;
                }

                // The view root layout
                VerticalLayout viewLayout = new VerticalLayout(new Menu(permisos, user));
                viewLayout.setSizeFull();

                // Creamos y añadimos el logo de Brostel a la pantalla
                HorizontalLayout imgNaturSoft = contrVista.logoGenaSoft();

                viewLayout.addComponent(imgNaturSoft);
                viewLayout.setComponentAlignment(imgNaturSoft, Alignment.TOP_RIGHT);
                viewLayout.addComponent(titulo);
                viewLayout.setComponentAlignment(titulo, Alignment.TOP_CENTER);

                // Creamos la tabla de colores
                crearTablaColores();

                crearTablaControlPt(permisos);

                crearTablaPesajesCajas();
                crearTablaPesajesCalibres();
                crearTablaPesajesConfecciones();
                crearTablaPesajesBrix();
                crearTablaPesajesPenetromia();

                Label tituloFiltrar = new Label("Filtrar");
                tituloFiltrar.setStyleName("tituloTamano16");

                lProductos = contrVista.obtenerTodosProductos(user, time);
                lClientes = contrVista.obtenerTodosClientes(user, time);
                lEmpleados = contrVista.obtenerEmpleadosPorRolActivos(RolesEnum.OPERARIO_CONTROL_PT.getValue(), user, time);

                // Obtenemos los campos y en el orden establecido de la tabla de empleados.
                List<TColumnasTablasEmpleado> lColumnas = contrVista.obtenerCamposPantallaTablaEmpleado(user, NAME, Integer.valueOf(tablaControlPt.getId()), user, time);

                nutrirDiccionarioCabeceraTablaControlPt();

                mostrarColumnasTablaControlPt(lColumnas);

                // Creamos el componente filtro.
                VerticalLayout filtro = crearComponenteFiltro();

                bcControlPt.addAll(contrVista.obtenerControlesPtVista(f1.getValue(), f2.getValue(), user, time));

                // Creamos la botonera.
                HorizontalLayout botonera = crearBotonera(permisos);

                viewLayout.addComponent(filtro);
                viewLayout.addComponent(botonera);
                viewLayout.addComponent(tablaColores);
                viewLayout.addComponent(tablaControlPt);

                HorizontalLayout subTablas = new HorizontalLayout();
                subTablas.setSpacing(true);

                ////////////////////////////////////// Definición de contenedores para mostrar las tablas de pesajes

                // PESAJE DE CAJAS
                Label tituloPesadoCajas = new Label("PESADO CAJAS REAL");
                tituloPesadoCajas.setStyleName("tituloTamano16");

                vTablaPesCaja = new VerticalLayout();
                vTablaPesCaja.setSpacing(true);
                //vTablaPesCaja.addComponent(tituloPesadoCajas);
                vTablaPesCaja.addComponent(tablaPesajesCajas);

                // PESAJE DE CONFECCIONES
                Label tituloPesadoConfecciones = new Label("PESADO CONFECCIÓN REAL");
                tituloPesadoConfecciones.setStyleName("tituloTamano16");

                vTablaPesConf = new VerticalLayout();
                vTablaPesConf.setSpacing(true);
                //vTablaPesConf.addComponent(tituloPesadoConfecciones);
                vTablaPesConf.addComponent(tablaPesajesConfeccion);

                // PESAJE DE CALIBRES
                Label tituloPesadoCalibres = new Label("CONTROL CALIBRE");
                tituloPesadoCalibres.setStyleName("tituloTamano16");

                vTablaPesCalibre = new VerticalLayout();
                vTablaPesCalibre.setSpacing(true);
                //vTablaPesCalibre.addComponent(tituloPesadoCalibres);
                vTablaPesCalibre.addComponent(tablaPesajesCalibres);

                vTablaPesCaja.setVisible(false);
                vTablaPesConf.setVisible(false);
                vTablaPesCalibre.setVisible(false);

                subTablas.addComponent(vTablaPesCaja);
                subTablas.addComponent(vTablaPesConf);
                subTablas.addComponent(vTablaPesCalibre);

                // BRIX
                Label tituloBrix = new Label("ºBRIX");
                tituloBrix.setStyleName("tituloTamano16");

                VerticalLayout vBrix = new VerticalLayout();
                vBrix.setSpacing(true);
                //vBrix.addComponent(tituloBrix);
                vBrix.addComponent(tablaBrix);

                // PENETROMÍA
                Label tituloPenetromia = new Label("PENETROMÍA");
                tituloPenetromia.setStyleName("tituloTamano16");

                VerticalLayout vPenetromia = new VerticalLayout();
                vPenetromia.setSpacing(true);
                //vPenetromia.addComponent(tituloPenetromia);
                vPenetromia.addComponent(tablaPenetromia);

                subTablas.addComponent(vPenetromia);
                subTablas.addComponent(vBrix);
                viewLayout.addComponent(subTablas);
                viewLayout.setComponentAlignment(subTablas, Alignment.MIDDLE_CENTER);
                // Añadimos el logo del cliente
                viewLayout.addComponent(contrVista.logoCliente());
                setCompositionRoot(viewLayout);
                // Establecemos el porcentaje de ratio para los layouts
                viewLayout.setExpandRatio(titulo, 0.1f);
                viewLayout.setMargin(true);
                viewLayout.setSpacing(true);

            } catch (GenasoftException tbe) {
                log.error("La sesión es inválida, se ha iniciado sesión en otro dispositivo.");
                // Si no se encuentran permisos con el rol especificado, informamos al empleado y cerramos sesión.
                Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                getSession().setAttribute("user", null);
                getSession().setAttribute("fecha", null);
                // Redirigimos a la página de inicio.
                getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
            } catch (MyBatisSystemException e) {
                Notification aviso = new Notification("No se ha podido establecer conexión con la base de datos.", Notification.Type.ERROR_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                log.error("Error al obtener datos de base de datos ", e);
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
        crearButton = new Button("Crear Control PT", this);
        //crearButton.setVisible(false);
        modificarButton = new Button("Editar Control PT", this);
        guardarColumnasTablaButton = new Button("Guardar orden columnas", this);

        pdfButton = new Button("", this);
        pdfButton.setIcon(new ThemeResource("icons/pdf-32.ico"));
        pdfButton.setStyleName(BaseTheme.BUTTON_LINK);
    }

    @Override
    public void buttonClick(ClickEvent event) {

    }

    /**
     * Este metodo nos crea el evento para cada botón.
     */
    private void eventosBotones() {

        // Evento para crear un nuevo cliente.
        guardarColumnasTablaButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                guardarOrdenColumnasTablaControlPt();

            }
        });

        // Evento para crear un nuevo cliente.
        crearButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                getUI().getNavigator().navigateTo(VistaNuevoControlPtOld.NAME + "/" + user);

            }
        });

        // Evento para eliminar un articulo articulo.
        modificarButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                if (idSeleccionado != null && !idSeleccionado.contains(",")) {

                    getUI().getNavigator().navigateTo(VistaControlPtOld.NAME + "/" + idSeleccionado);

                } else {
                    Notification aviso = new Notification("Se debe seleccionar un registro del listado", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }

        });

        // Evento para eliminar un articulo articulo.
        pdfButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                if (idSeleccionado != null) {

                    Page.getCurrent().open("/exportarControlPtPdf?idEmpleado=" + user + "&idSeleccionado=" + idSeleccionado, "_blank");

                } else {
                    Notification aviso = new Notification("Se debe seleccionar al menos un registro del listado", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }

        });

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaControlPt(TPermisos permisos) {
        tablaControlPt = new TablaGenerica(new Object[] { "id", "fecha", "indNave1", "indNave2", "indNave3", "linea", "indBio", "idCliente", "numeroPedido", "numCajasPedido", "numCajasReal", "idProdudcto", "idVariedad", "idCalibre", "pesoCaja", "indContaminaFisica", "indContaminaQuimica", "indContaminaBiologica", "usuCrea", "fechaCrea", "observaciones" }, new String[] { "Nº Control PT", "Fecha control PT", "¿Nave 1?", "¿Nave 2?", "¿Nave 3?", "Línea", "¿BIO?", "Cliente", "Nº Pedido", "Nº Cajas pedido", "Nº Cajas real", "Producto", "Variedad", "Calibre", "Peso caja/confección teórico", "Contaminación física", "Contaminación química", "Contaminación biológica", "Registrado por", "Fecha creación", "Observaciones" }, bcControlPt);
        tablaControlPt.addStyleName("big striped");
        tablaControlPt.setPageLength(20);
        tablaControlPt.setId("1");

        // Establecemos tamaño fijo en columnas específicas.        
        tablaControlPt.setMultiSelect(false);
        /** tablaControlPt.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                @SuppressWarnings("unchecked")
                Set<String> values = (Set<String>) tablaControlPt.getValue();
                idSeleccionado = "";
                for (String v : values) {
                    if (v.isEmpty())
                        continue;
                    if (!idSeleccionado.isEmpty()) {
                        idSeleccionado = idSeleccionado + "," + v;
                    } else {
                        idSeleccionado = v;
                    }
        
                }
            }
        });
        */
        tablaControlPt.addItemClickListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionado = (String) event.getItemId();
                try {
                    // Cargamos los datos de los pesajes, en función de si es caja, calibre o confección.
                    @SuppressWarnings("unchecked")
                    BeanItem<TControlProductoTerminadoVista> aux = (BeanItem<TControlProductoTerminadoVista>) tablaControlPt.getItem(idSeleccionado);

                    TControlProductoTerminadoVista cPt = aux.getBean();
                    // Si se cumple la condición es por confección
                    if (cPt.getIndFlowPack().equals("1") || cPt.getIndFlowPack().equals("2") || cPt.getIndFlowPack().equals("3") || cPt.getIdProdudcto().contains("MANGO")) {
                        vTablaPesConf.setVisible(true);
                        bcPesajesConfeccion.removeAllItems();
                        bcPesajesConfeccion.addAll(contrVista.obtenerPesajesConfeccionIdControlPtVista(Integer.valueOf(idSeleccionado), user, time));
                        vTablaPesCaja.setVisible(false);
                        vTablaPesCalibre.setVisible(true);
                        bcPesajesCalibres.removeAllItems();
                        bcPesajesCalibres.addAll(contrVista.obtenerPesajesCalibreIdControlPtVista(Integer.valueOf(idSeleccionado), user, time));
                    } else {
                        vTablaPesCaja.setVisible(true);
                        vTablaPesCalibre.setVisible(true);
                        vTablaPesConf.setVisible(false);
                        bcPesajesCajas.removeAllItems();
                        bcPesajesCajas.addAll(contrVista.obtenerPesajesCajaIdControlPtVista(Integer.valueOf(idSeleccionado), user, time));

                        bcPesajesCalibres.removeAllItems();
                        bcPesajesCalibres.addAll(contrVista.obtenerPesajesCalibreIdControlPtVista(Integer.valueOf(idSeleccionado), user, time));
                    }

                    bcPesajesPenetromia.removeAllItems();
                    bcPesajesPenetromia.addAll(contrVista.obtenerPesajesPenetromiaIdControlPtVista(Integer.valueOf(idSeleccionado), user, time));

                    bcPesajesBrix.removeAllItems();
                    bcPesajesBrix.addAll(contrVista.obtenerPesajesBrixIdControlPtVista(Integer.valueOf(idSeleccionado), user, time));

                } catch (GenasoftException tbe) {
                    log.error("La sesión es inválida, se ha iniciado sesión en otro dispositivo.");
                    // Si no se encuentran permisos con el rol especificado, informamos al empleado y cerramos sesión.                
                    Notification aviso = new Notification(tbe.getMessage(), Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    // Redirigimos a la página de inicio.
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                } catch (MyBatisSystemException e) {
                    Notification aviso = new Notification("No se ha podido establecer conexión con BD.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    log.error("Error al obtener datos de base de datos ", e);
                }

                if (event.isDoubleClick()) {
                    getUI().getNavigator().navigateTo(VistaControlPtOld.NAME + "/" + idSeleccionado);
                }
            }

        });

        tablaControlPt.setMultiSelect(true);
        tablaControlPt.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                @SuppressWarnings("unchecked")
                Set<String> values = (Set<String>) tablaControlPt.getValue();
                idSeleccionado = "";
                for (String v : values) {
                    if (v.isEmpty())
                        continue;
                    if (!idSeleccionado.isEmpty()) {
                        idSeleccionado = idSeleccionado + "," + v;
                    } else {
                        idSeleccionado = v;
                    }

                }
            }
        });

        tablaControlPt.setCellStyleGenerator(new Table.CellStyleGenerator() {

            @SuppressWarnings("unchecked")
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                BeanItem<TControlProductoTerminadoVista> aux = (BeanItem<TControlProductoTerminadoVista>) source.getItem(itemId);

                TControlProductoTerminadoVista cPt = aux.getBean();
                if (cPt.getEstado().equals("Incompleto")) {
                    return "orange";
                } else {
                    return "green";
                }
            }
        });
    }

    /**
     * Método que nos crea una tabla leyenda de colores.
     */
    @SuppressWarnings("unchecked")
    private void crearTablaColores() {
        tablaColores = new Table("");
        tablaColores.addContainerProperty("Estado", String.class, null);
        tablaColores.addContainerProperty("Estado2", String.class, null);
        tablaColores.setSelectable(false);
        tablaColores.setCellStyleGenerator(new Table.CellStyleGenerator() {

            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                Item aux = (Item) source.getItem(itemId);

                if (propertyId == null) {
                    return "";
                }
                String estado = (String) aux.getItemProperty(propertyId).getValue();
                if (estado.equals("Incompleto")) {
                    return "orange";
                } else {
                    return "green";
                }
            }
        });
        tablaColores.addStyleName("striped");
        tablaColores.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
        tablaColores.setPageLength(0);

        // Estado documentado
        Object itemId = tablaColores.addItem();
        Item estado1 = tablaColores.getItem(itemId);
        estado1.getItemProperty("Estado").setValue("Incompleto");
        estado1.getItemProperty("Estado2").setValue("Completo");
    }

    private void crearTablaPesajesCajas() {
        tablaPesajesCajas = new TablaGenerica(new Object[] { "orden", "valor" }, new String[] { "#", "Peso caja" }, bcPesajesCajas);
        tablaPesajesCajas.addStyleName("big striped");
        tablaPesajesCajas.setPageLength(10);
        tablaPesajesCajas.setId("2");

        tablaPesajesCajas.setCellStyleGenerator(new Table.CellStyleGenerator() {

            Double peso = null;

            @SuppressWarnings({ "unchecked" })
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {

                if (idSeleccionado.contains(",")) {
                    return "";
                }
                BeanItem<TControlProductoTerminadoVista> aux2 = (BeanItem<TControlProductoTerminadoVista>) tablaControlPt.getItem(idSeleccionado);

                TControlProductoTerminadoVista cPt2 = aux2.getBean();

                if (cPt2.getPesoCaja() != null && !cPt2.getPesoCaja().trim().isEmpty()) {
                    try {
                        peso = Utils.formatearValorDouble(cPt2.getPesoCaja());
                    } catch (NumberFormatException e) {
                        peso = null;
                    }
                }

                BeanItem<TControlProductoTerminadoPesajesCajaVista> aux = (BeanItem<TControlProductoTerminadoPesajesCajaVista>) source.getItem(itemId);

                TControlProductoTerminadoPesajesCajaVista cPt = aux.getBean();
                if (peso != null) {

                    if (Utils.formatearValorDouble2(cPt.getValor()) > peso) {
                        return "green";
                    } else {
                        return "red";
                    }
                } else {
                    return "";
                }
            }
        });

    }

    private void crearTablaPesajesCalibres() {
        tablaPesajesCalibres = new TablaGenerica(new Object[] { "orden", "valor" }, new String[] { "#", "Valor calibre" }, bcPesajesCalibres);
        tablaPesajesCalibres.addStyleName("big striped");
        tablaPesajesCalibres.setPageLength(10);
        tablaPesajesCalibres.setId("3");

        tablaPesajesCalibres.setCellStyleGenerator(new Table.CellStyleGenerator() {

            @SuppressWarnings("unchecked")
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                BeanItem<TControlProductoTerminadoPesajesCalibreVista> aux = (BeanItem<TControlProductoTerminadoPesajesCalibreVista>) source.getItem(itemId);

                BeanItem<TControlProductoTerminadoVista> aux2 = (BeanItem<TControlProductoTerminadoVista>) tablaControlPt.getItem(idSeleccionado);

                TControlProductoTerminadoVista cPt2 = aux2.getBean();

                String[] cal = cPt2.getIdCalibre().split(",");

                Integer min = Integer.valueOf(cal[0].split("\\(")[1].trim());
                Integer max = Integer.valueOf(cal[1].split("\\)")[0].trim());

                TControlProductoTerminadoPesajesCalibreVista cPt = aux.getBean();
                if (Utils.formatearValorDouble2(cPt.getValor()) >= min && Utils.formatearValorDouble2(cPt.getValor()) <= max) {
                    return "green";
                } else {
                    return "red";
                }
            }
        });

    }

    private void crearTablaPesajesConfecciones() {
        tablaPesajesConfeccion = new TablaGenerica(new Object[] { "orden", "valor" }, new String[] { "#", "Peso confección" }, bcPesajesConfeccion);
        tablaPesajesConfeccion.addStyleName("big striped");
        tablaPesajesConfeccion.setPageLength(10);
        tablaPesajesConfeccion.setId("4");

        tablaPesajesConfeccion.setCellStyleGenerator(new Table.CellStyleGenerator() {

            Double peso = null;

            @SuppressWarnings({ "unchecked" })
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {

                BeanItem<TControlProductoTerminadoVista> aux2 = (BeanItem<TControlProductoTerminadoVista>) tablaControlPt.getItem(idSeleccionado);

                TControlProductoTerminadoVista cPt2 = aux2.getBean();

                if (cPt2.getPesoCaja() != null && !cPt2.getPesoCaja().trim().isEmpty()) {
                    try {
                        peso = Utils.formatearValorDouble(cPt2.getPesoCaja());
                    } catch (NumberFormatException e) {
                        peso = null;
                    }
                }

                BeanItem<TControlProductoTerminadoPesajesConfeccionVista> aux = (BeanItem<TControlProductoTerminadoPesajesConfeccionVista>) source.getItem(itemId);

                TControlProductoTerminadoPesajesConfeccionVista cPt = aux.getBean();
                if (peso != null) {

                    if (Utils.formatearValorDouble2(cPt.getValor()) > peso) {
                        return "green";
                    } else {
                        return "red";
                    }
                } else {
                    return "";
                }
            }
        });

    }

    private void crearTablaPesajesBrix() {
        tablaBrix = new TablaGenerica(new Object[] { "orden", "valor" }, new String[] { "#", "Valor ºBrix" }, bcPesajesBrix);
        tablaBrix.addStyleName("big striped");
        tablaBrix.setPageLength(10);
        tablaBrix.setId("5");

    }

    private void crearTablaPesajesPenetromia() {
        tablaPenetromia = new TablaGenerica(new Object[] { "orden", "valor" }, new String[] { "#", "Valor penetromía" }, bcPesajesPenetromia);
        tablaPenetromia.addStyleName("big striped");
        tablaPenetromia.setPageLength(10);
        tablaPenetromia.setId("5");

    }

    /**
     * Clase para aplicar filtros al container.
     * @author Daniel Carmona Romero
     */
    private class FiltroContainer implements Container.Filter {
        protected String propertyId;
        protected String regex;
        protected Label  status;

        public FiltroContainer(String propertyId, String regex, Label status) {
            this.propertyId = propertyId;
            this.regex = regex;
            this.status = status;
        }

        /** Apply the filter on an item to check if it passes. */
        @Override
        public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
            // Acquire the relevant property from the item object
            Property<?> p = item.getItemProperty(propertyId);

            // Should always check validity
            if (p == null || !p.getType().equals(String.class))
                return false;
            String value = (String) p.getValue();

            // Pass all if regex not given
            if (regex.isEmpty()) {
                status.setValue("Empty filter");
                return true;
            }

            // The actual filter logic + error handling
            try {
                boolean result = value.toLowerCase().matches(regex);
                status.setValue(""); // OK
                return result;
            } catch (PatternSyntaxException e) {
                status.setValue("Invalid pattern");
                return false;
            }
        }

        /** Tells if this filter works on the given property. */
        @Override
        public boolean appliesToProperty(Object propertyId) {
            return propertyId != null && propertyId.equals(this.propertyId);
        }
    }

    /**
     * Método que se encarga de construir el componente que conforma la parte del filtro
     * @return El componente que conforma la parte del filtro.
     */
    private VerticalLayout crearComponenteFiltro() {

        // Campo para filtrar por el nombre
        fNumPedido = new TextField("");
        fNumPedido.setNullRepresentation("");
        fNumPedido.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcControlPt.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("numeroPedido", filtro, status);
                    bcControlPt.addContainerFilter(filter);
                    aplicarFiltro2(NUM_PEDIDO);
                } else {
                    fNumPedido.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por producto.
        fProductos = new ComboBox();
        fProductos.addItem("Todos");
        fProductos.addItems(lProductos);
        fProductos.setValue("Todos");
        fProductos.setNullSelectionAllowed(false);
        fProductos.setFilteringMode(FilteringMode.CONTAINS);
        fProductos.setValue("Todos");
        fProductos.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltro();
            }
        });

        // Campo para filtrar por cliente.
        fClientes = new ComboBox();
        fClientes.addItem("Todos");
        fClientes.addItems(lClientes);
        fClientes.setValue("Todos");
        fClientes.setNullSelectionAllowed(false);
        fClientes.setFilteringMode(FilteringMode.CONTAINS);
        fClientes.setValue("Todos");
        fClientes.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltro();
            }
        });

        // Campo para filtrar por operario.
        fEmpleados = new ComboBox();
        fEmpleados.addItem("Todos");
        fEmpleados.addItems(lEmpleados);
        fEmpleados.setValue("Todos");
        fEmpleados.setNullSelectionAllowed(false);
        fEmpleados.setFilteringMode(FilteringMode.CONTAINS);
        fEmpleados.setValue("Todos");
        fEmpleados.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltro();
            }
        });

        // Campo para filtrar por estado.
        fEstados = new ComboBox();
        fEstados.addItem("Todos");
        fEstados.addItem("Completo");
        fEstados.addItem("Incompleto");
        fEstados.setValue("Todos");
        fEstados.setNullSelectionAllowed(false);
        fEstados.setFilteringMode(FilteringMode.CONTAINS);

        fEstados.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltro();
            }
        });

        f1 = new DateField();
        f1.setValue(Utils.obtenerPrimerDiaMesEnCurso());
        f1.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                cambiarFechas();
            }
        });

        f2 = new DateField();
        f2.setValue(Utils.obtenerUltimoDiaMesEnCurso());
        f2.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                cambiarFechas();
            }
        });

        Label tituloFiltrar = new Label("Filtrar");
        tituloFiltrar.setStyleName("tituloTamano12");
        // Filtro cabecera
        VerticalLayout filtro = new VerticalLayout(tituloFiltrar);

        // Tabla de filtro
        Table table = new Table();
        table.addStyleName("big striped");
        // La cabecera de la tabla del filtro.
        table.addContainerProperty("Nº Pedido", TextField.class, null);
        table.addContainerProperty("Cliente", ComboBox.class, null);
        table.addContainerProperty("Producto", ComboBox.class, null);
        table.addContainerProperty("Operario", ComboBox.class, null);
        table.addContainerProperty("Estado", ComboBox.class, null);
        table.setPageLength(table.size());

        // Los componentes que componen al filtro
        table.addItem(new Object[] { fNumPedido, fClientes, fProductos, fEmpleados, fEstados }, 1);
        filtro.addComponent(table);

        HorizontalLayout fechas = new HorizontalLayout();
        fechas.setSpacing(true);
        fechas.addComponent(f1);
        fechas.addComponent(f2);

        filtro.addComponent(fechas);
        filtro.setSpacing(true);
        filtro.setMargin(true);
        return filtro;
    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    private void aplicarFiltro() {
        boolean entra = false;
        bcControlPt.removeAllContainerFilters();
        if (fNumPedido.getValue() != null && !fNumPedido.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fNumPedido.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("numeroPedido", filtro, status);
            bcControlPt.addContainerFilter(filter);
            entra = true;
        }

        if (!fProductos.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("idProducto", (String) ".*(" + fProductos.getValue().toString().toLowerCase() + ").*", status);
            bcControlPt.addContainerFilter(filter);
            entra = true;
        }

        if (!fClientes.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("idCliente", (String) ".*(" + fProductos.getValue().toString().toLowerCase() + ").*", status);
            bcControlPt.addContainerFilter(filter);
            entra = true;
        }

        if (!fEmpleados.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("usuCrea", (String) ".*(" + fEmpleados.getValue().toString().toLowerCase() + ").*", status);
            bcControlPt.addContainerFilter(filter);
            entra = true;
        }

        if (!fEstados.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("estado", (String) ".*(" + fEstados.getValue().toString().toLowerCase() + ").*", status);
            bcControlPt.addContainerFilter(filter);
            entra = true;
        }

        if (!entra) {
            cambiarFechas();
        }
    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    private void aplicarFiltro2(String filtro) {
        boolean entra = false;
        if (fNumPedido.getValue() != null && !fNumPedido.getValue().isEmpty() && !filtro.equals(NUM_PEDIDO)) {
            // Set new filter for the "Name" column
            String[] params = fNumPedido.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("descripcion", filtr, status);
            bcControlPt.addContainerFilter(filter);
            entra = true;
        }
        if (fNumPedido.getValue() != null && !fNumPedido.getValue().isEmpty()) {
            entra = true;
        }

        if (!fProductos.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("idProducto", (String) ".*(" + fProductos.getValue().toString().toLowerCase() + ").*", status);
            bcControlPt.addContainerFilter(filter);
            entra = true;
        }

        if (!fClientes.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("idCliente", (String) ".*(" + fProductos.getValue().toString().toLowerCase() + ").*", status);
            bcControlPt.addContainerFilter(filter);
            entra = true;
        }

        if (!fEmpleados.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("usuCrea", (String) ".*(" + fEmpleados.getValue().toString().toLowerCase() + ").*", status);
            bcControlPt.addContainerFilter(filter);
            entra = true;
        }

        if (!fEstados.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("estado", (String) ".*(" + fEstados.getValue().toString().toLowerCase() + ").*", status);
            bcControlPt.addContainerFilter(filter);
            entra = true;
        }

        if (!entra) {
            cambiarFechas();
        }

    }

    /**
     * Método que nos crea el componente donde están los botones de la vista.
     * @param permisos Los permisos que tiene el empleado activo.
     * @return El componente con los botones a partir de los permisos que tiene el cliente.
     */
    private HorizontalLayout crearBotonera(TPermisos permisos) {
        //Botonera
        HorizontalLayout botonera = new HorizontalLayout();
        botonera.setSpacing(true);
        botonera.addComponent(crearButton);
        botonera.addComponent(modificarButton);
        botonera.addComponent(pdfButton);

        botonera.setMargin(true);

        return botonera;
    }

    /**
     * Método que nos realiza el cambio de las fechas para la carga de datos
     */
    private void cambiarFechas() {
        bcControlPt.removeAllItems();
        try {
            bcControlPt.addAll(contrVista.obtenerControlesPtVista(f1.getValue(), f2.getValue(), user, time));
        } catch (GenasoftException tbe) {
            log.error("La sesión es inválida, se ha iniciado sesión en otro dispositivo.");
            // Si no se encuentran permisos con el rol especificado, informamos al empleado y cerramos sesión.
            Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
            getSession().setAttribute("user", null);
            getSession().setAttribute("fecha", null);
            // Redirigimos a la página de inicio.
            getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
        } catch (MyBatisSystemException e) {
            Notification aviso = new Notification("No se ha podido establecer conexión con la base de datos.", Notification.Type.ERROR_MESSAGE);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
            log.error("Error al obtener datos de base de datos ", e);
        }
    }

    private void nutrirDiccionarioCabeceraTablaControlPt() {

        mColumnasTablaControlPt = new HashMap<String, String>();
        mColumnasTablaControlPt.put("Nº Control", "id");
        mColumnasTablaControlPt.put("Fecha control PT", "fecha");
        mColumnasTablaControlPt.put("Cliente", "idCliente");
        mColumnasTablaControlPt.put("Nº Pedido", "numeroPedido");
        mColumnasTablaControlPt.put("Nº Cajas pedido", "numCajasPedido");
        mColumnasTablaControlPt.put("Nº Cajas real", "numCajasReal");
        mColumnasTablaControlPt.put("Producto", "idProducto");
        mColumnasTablaControlPt.put("Variedad", "idVariedad");
        mColumnasTablaControlPt.put("Calibre", "idCalibre");
        mColumnasTablaControlPt.put("Peso caja/confección teórico", "pesoCaja");
        mColumnasTablaControlPt.put("Contaminación física", "indContaminaFisica");
        mColumnasTablaControlPt.put("Contaminación química", "indContaminaQuimica");
        mColumnasTablaControlPt.put("Contaminación biológica", "indContaminaBiologica");
        mColumnasTablaControlPt.put("Observaciones", "observaciones");

        mColumnasTablaControlPt = new HashMap<String, String>();
        mColumnasTablaControlPt.put("id", "id");
        mColumnasTablaControlPt.put("fecha", "fecha");
        mColumnasTablaControlPt.put("idCliente", "idCliente");
        mColumnasTablaControlPt.put("numeroPedido", "numeroPedido");
        mColumnasTablaControlPt.put("numCajasPedido", "numCajasPedido");
        mColumnasTablaControlPt.put("numCajasReal", "numCajasReal");
        mColumnasTablaControlPt.put("idProducto", "idProducto");
        mColumnasTablaControlPt.put("idVariedad", "idVariedad");
        mColumnasTablaControlPt.put("idCalibre", "idCalibre");
        mColumnasTablaControlPt.put("pesoCaja", "pesoCaja");
        mColumnasTablaControlPt.put("indContaminaFisica", "indContaminaFisica");
        mColumnasTablaControlPt.put("indContaminaQuimica", "indContaminaQuimica");
        mColumnasTablaControlPt.put("indContaminaBiologica", "indContaminaBiologica");
        mColumnasTablaControlPt.put("observaciones", "observaciones");

        mColumnasIdsControlPt = new HashMap<String, String>();
        mColumnasIdsControlPt.put("id", "Nº Control");
        mColumnasIdsControlPt.put("fecha", "Fecha control PT");
        mColumnasIdsControlPt.put("idCliente", "Cliente");
        mColumnasIdsControlPt.put("numeroPedido", "Nº Pedido");
        mColumnasIdsControlPt.put("numCajasPedido", "Nº Cajas pedido");
        mColumnasIdsControlPt.put("numCajasReal", "Nº Cajas real");
        mColumnasIdsControlPt.put("idProducto", "Producto");
        mColumnasIdsControlPt.put("idVariedad", "Variedad");
        mColumnasIdsControlPt.put("idCalibre", "Calibre");
        mColumnasIdsControlPt.put("pesoCaja", "Peso caja/confección teórico");
        mColumnasIdsControlPt.put("indContaminaFisica", "Contaminación física");
        mColumnasIdsControlPt.put("indContaminaQuimica", "Contaminación química");
        mColumnasIdsControlPt.put("indContaminaBiologica", "Contaminación biológica");
        mColumnasIdsControlPt.put("observaciones", "Observaciones");

        mColumnasGuardadoTablaControlPt = new HashMap<String, String>();
        mColumnasGuardadoTablaControlPt.put("Nº Control", "id");
        mColumnasGuardadoTablaControlPt.put("Fecha control PT", "fecha");
        mColumnasGuardadoTablaControlPt.put("Cliente", "idCliente");
        mColumnasGuardadoTablaControlPt.put("Nº Pedido", "numeroPedido");
        mColumnasGuardadoTablaControlPt.put("Nº Cajas pedido", "numCajasPedido");
        mColumnasGuardadoTablaControlPt.put("Nº Cajas real", "numCajasReal");
        mColumnasGuardadoTablaControlPt.put("Producto", "idProducto");
        mColumnasGuardadoTablaControlPt.put("Variedad", "idVariedad");
        mColumnasGuardadoTablaControlPt.put("Calibre", "idCalibre");
        mColumnasGuardadoTablaControlPt.put("Peso caja/confección teórico", "pesoCaja");
        mColumnasGuardadoTablaControlPt.put("Contaminación física", "indContaminaFisica");
        mColumnasGuardadoTablaControlPt.put("Contaminación química", "indContaminaQuimica");
        mColumnasGuardadoTablaControlPt.put("Contaminación biológica", "indContaminaBiologica");
        mColumnasGuardadoTablaControlPt.put("Observaciones", "observaciones");
    }

    /**
     * Método que nos muestra las columnas según la configuración del control de producto terminado.
     * @param lColumnas Las columnas y en qué orden se van a mostrar.
     */
    private void mostrarColumnasTablaControlPt(List<TColumnasTablasEmpleado> lColumnas) {

        List<String> lCamposTabla = Utils.generarListaGenerica();
        List<String> lCamposTablaIdioma = Utils.generarListaGenerica();
        lCamposTabla.addAll(mColumnasTablaControlPt.values());
        lCamposTablaIdioma.addAll(mColumnasTablaControlPt.values());

        if (!lColumnas.isEmpty()) {
            Object[] visibleColumns = new Object[mColumnasIdsControlPt.size()];

            int i = 0;
            for (TColumnasTablasEmpleado col : lColumnas) {
                visibleColumns[i] = mColumnasTablaControlPt.get(col.getCampo());
                lCamposTabla.remove(col.getCampo());
                lCamposTablaIdioma.remove(col.getCampo());
                i++;
            }

            // Eliminamos los campos que no se identificaron.
            for (String campo : lCamposTablaIdioma) {
                visibleColumns[i] = mColumnasTablaControlPt.get(campo);
                tablaControlPt.setColumnCollapsed(mColumnasTablaControlPt.get(campo), true);
                i++;
            }

            tablaControlPt.setVisibleColumns(visibleColumns);
        }

    }

    /**
     * Método que nos guarda las columnas y en qué orden se muestran los datos en la tabla de empresas.
     */
    private void guardarOrdenColumnasTablaControlPt() {

        // Tabla Control de producto terminado                      a pelo.
        String columnas[] = tablaControlPt.getColumnHeaders();
        String columnasIdioma[] = new String[columnas.length];

        int aux = 0;
        // Nurimos el array con 
        while (aux < columnas.length) {
            columnasIdioma[aux] = new String(mColumnasGuardadoTablaControlPt.get(columnas[aux]));
            aux++;
        }

        List<TColumnasTablasEmpleado> lCols = Utils.generarListaGenerica();
        TColumnasTablasEmpleado col = null;
        if (columnas != null && columnas.length > 0) {
            // Vamos recorriendo la cabecera de la tabla para coger los campos.
            Integer cnt = 0;
            while (cnt < columnas.length) {
                if (tablaControlPt.isColumnCollapsed(mColumnasGuardadoTablaControlPt.get(columnas[cnt]))) {
                    cnt++;
                    continue;
                }
                col = new TColumnasTablasEmpleado();
                col.setCampo(columnasIdioma[cnt]);
                col.setIdEmpleado(user);
                col.setIdTabla(Integer.valueOf(tablaControlPt.getId()));
                col.setNombrePantalla(NAME);
                col.setOrdenCampo(cnt);
                lCols.add(col);
                cnt++;
            }
            try {

                // Guardamos las columnas y en el orden indicado.
                contrVista.guardarCamposTablaEmpleado(lCols, user, NAME, Integer.valueOf(tablaControlPt.getId()), user, time);

                Notification aviso = new Notification("Datos guardados correctamente", Notification.Type.HUMANIZED_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            } catch (GenasoftException tbe) {
                log.error("La sesión es inválida, se ha iniciado sesión en otro dispositivo.");
                // Si no se encuentran permisos con el rol especificado, informamos al empleado y cerramos sesión.                
                Notification aviso = new Notification(tbe.getMessage(), Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                getSession().setAttribute("user", null);
                getSession().setAttribute("fecha", null);
                // Redirigimos a la página de inicio.
                getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
            } catch (MyBatisSystemException e) {
                Notification aviso = new Notification("No se ha podido establecer conexión con BD.", Notification.Type.ERROR_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                log.error("Error al obtener datos de base de datos ", e);
            }
        }

    }

}
