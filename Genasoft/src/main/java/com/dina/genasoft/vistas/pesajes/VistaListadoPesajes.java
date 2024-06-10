/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.pesajes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TFacturas;
import com.dina.genasoft.db.entity.TLineasFactura;
import com.dina.genasoft.db.entity.TMateriales;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TPesajes;
import com.dina.genasoft.db.entity.TPesajesVista;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.TablaGenerica;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.PesajesEnum;
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

import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaListadoPesajes.NAME)
public class VistaListadoPesajes extends CustomComponent implements View ,Button.ClickListener {
    /** El nombre de la vista.*/
    public static final String                   NAME         = "vPesajes";
    /** Necesario para mostrar los pesajes*/
    private BeanContainer<String, TPesajesVista> bcPesajes;
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas                    contrVista;
    /** El boton para crear un pesaje.*/
    private Button                               crearButton;
    /** El boton para crear un pesaje.*/
    private Button                               modificarButton;
    /** El boton para eliminar un pesaje.*/
    private Button                               eliminarButton;
    /** El boton para generar facturas.*/
    private Button                               facturarButton;
    /** El boton para generar multifacturas.*/
    private Button                               facturar2Button;
    // Elementos para realizar busquedas
    /** Para filtrar por el nº de albarán del pesaje.*/
    private TextField                            fAlbaran;
    /** Para filtrar por la obra del pesaje.*/
    private TextField                            fObra;
    /** Para filtrar por el origen del pesaje.*/
    private TextField                            fOrigen;
    /** Para filtrar por el destino del pesaje.*/
    private TextField                            fDestino;
    /** Para filtrar por la matricula del pesaje.*/
    private TextField                            fMatricula;
    /** Para filtrar por el remolque del pesaje.*/
    private TextField                            fRemolque;
    /** Para filtrar por el cliente. */
    private ComboBox                             fClientes;
    /** Para filtrar por el material. */
    private ComboBox                             fMaterial;
    /** Para filtrar por estado. */
    private ComboBox                             fEstados;
    /** Para el filtrado, la fecha desde. */
    private DateField                            fDesde;
    /** Para el filtrado, la fecha hasta. */
    private DateField                            fHasta;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                               appName;
    /** El ID del empleado seleccionado.*/
    private String                               idSeleccionado;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger        log          = org.slf4j.LoggerFactory.getLogger(VistaListadoPesajes.class);
    /** El usuario que está logado. */
    private Integer                              user         = null;
    /** La fecha en que se inició sesión. */
    private Long                                 time         = null;
    /** El filtro que se le aplica al container. */
    private FiltroContainer                      filter;
    /** No tengo ni puta idea para que sirve. */
    private final Label                          status       = new Label("");
    /** Tabla para mostrar los empleados del sistema. */
    private Table                                tablaPesajes = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                                tablaColores = null;
    /** Para indicar que se está filtrando por el campo ALBARAN. */
    private final String                         ALBARAN      = "albaran";
    /** Para indicar que se está filtrando por el campo OBRA. */
    private final String                         OBRA         = "obra";
    /** Para indicar que se está filtrando por el campo ORIGEN. */
    private final String                         ORIGEN       = "origen";
    /** Para indicar que se está filtrando por el campo DESTINO. */
    private final String                         DESTINO      = "destino";
    /** Para indicar que se está filtrando por el campo MATRICULA. */
    private final String                         MATRICULA    = "matricula";
    /** Para indicar que se está filtrando por el campo REMOLQUE. */
    private final String                         REMOLQUE     = "remolque";
    private TEmpleados                           empleado;
    private TPermisos                            permisos;
    private List<TClientes>                      lClientes;
    private List<TMateriales>                    lMateriales;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                               pdfButton;

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

                bcPesajes = new BeanContainer<>(TPesajesVista.class);
                bcPesajes.setBeanIdProperty("id");

                Label texto = new Label("Pesajes");
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

                if (!Utils.booleanFromInteger(permisos.getEntornoPesajes())) {
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
                HorizontalLayout imgGenaSoft = contrVista.logoGenaSoft();

                viewLayout.addComponent(imgGenaSoft);
                viewLayout.setComponentAlignment(imgGenaSoft, Alignment.TOP_RIGHT);
                viewLayout.addComponent(titulo);
                viewLayout.setComponentAlignment(titulo, Alignment.TOP_CENTER);

                // Creamos la tabla de colores
                crearTablaColores();
                // Creamos la tabla de pesajes
                crearTablaPesajes(permisos);

                lClientes = contrVista.obtenerClientesActivos(user, time);
                lMateriales = contrVista.obtenerMaterialesActivos(user, time);

                // Creamos el componente filtro.
                VerticalLayout filtro = crearComponenteFiltro();

                cambiarFechas();

                Label tituloFiltrar = new Label("Filtrar");
                tituloFiltrar.setStyleName("tituloTamano12");

                // Creamos la botonera.
                HorizontalLayout botonera = crearBotonera(permisos);

                viewLayout.addComponent(filtro);
                viewLayout.addComponent(botonera);
                if (Utils.booleanFromInteger(permisos.getCrearFactura())) {
                    viewLayout.addComponent(crearBotonera2());
                }
                Label tituloTablaColores = new Label("Leyenda colores");
                tituloTablaColores.setStyleName("tituloTamano12");

                viewLayout.addComponent(tituloTablaColores);
                viewLayout.addComponent(tablaColores);
                viewLayout.addComponent(tablaPesajes);
                // Añadimos el logo de GENAL BIOMASA
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
        crearButton = new Button("Registrar pesaje", this);
        modificarButton = new Button("Editar pesaje", this);
        eliminarButton = new Button("Anular pesaje", this);
        facturarButton = new Button("Generar factura", this);
        facturar2Button = new Button("Generar multifactura", this);

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

        // Evento para crear un nuevo empleado.
        crearButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                getUI().getNavigator().navigateTo(VistaNuevoPesaje.NAME + "/" + user);
            }

        });

        // Evento para modificar un empleado.
        modificarButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                if (idSeleccionado != null && !idSeleccionado.contains(",")) {

                    getUI().getNavigator().navigateTo(VistaPesaje.NAME + "/" + idSeleccionado);

                } else {
                    Notification aviso = new Notification("Se debe seleccionar un registro del listado", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }

        });

        // Evento para modificar un empleado.
        facturarButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                if (idSeleccionado != null) {

                    Label lbl = new Label("Se van a generar tantas facturas como pesajes seleccionados. ¿Quieres continuar?");
                    lbl.setStyleName("tituloTamano18Rojo");

                    DateField date;

                    if (idSeleccionado.contains(",")) {
                        date = new DateField("Fecha facturas: ");
                    } else {
                        date = new DateField("Fecha factura: ");
                    }
                    date.setValue(Utils.generarFecha());

                    VerticalLayout ver = new VerticalLayout();
                    ver.setSpacing(true);
                    if (idSeleccionado.contains(",")) {
                        ver.addComponent(lbl);
                    }
                    ver.addComponent(date);
                    ver.setComponentAlignment(date, Alignment.TOP_CENTER);
                    MessageBox.createQuestion().withCaption(appName).withMessage(ver).withNoButton().withYesButton(() ->

                    generarFactura(date.getValue()), ButtonOption.caption("Generar")).open();

                } else {
                    Notification aviso = new Notification("Se debe seleccionar al menos un registro del listado", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }

        });

        // Evento para modificar un empleado.
        facturar2Button.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                if (idSeleccionado != null && idSeleccionado.contains(",")) {

                    Label lbl = new Label("Al generar una multifactura, todos los registros deben ser del mismo cliente. ¿Quieres continuar?");
                    lbl.setStyleName("tituloTamano18Rojo");

                    DateField date;

                    date = new DateField("Fecha facturas: ");
                    date.setValue(Utils.generarFecha());

                    VerticalLayout ver = new VerticalLayout();
                    ver.setSpacing(true);
                    if (idSeleccionado.contains(",")) {
                        ver.addComponent(lbl);
                    }
                    ver.addComponent(date);
                    ver.setComponentAlignment(date, Alignment.TOP_CENTER);
                    MessageBox.createQuestion().withCaption(appName).withMessage(ver).withNoButton().withYesButton(() ->

                    generarMultiFactura(date.getValue()), ButtonOption.caption("Generar")).open();

                } else {
                    Notification aviso = new Notification("Se debe seleccionar al menos dos registros del listado", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }

        });

        // Evento para eliminar un empleado empleado.
        eliminarButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                if (idSeleccionado != null) {

                    MessageBox.createQuestion().withCaption(appName).withMessage("¿Quieres desactivar el registro seleccionado?").withNoButton().withYesButton(() -> anularPesaje(idSeleccionado), ButtonOption.caption("Sí")).open();

                } else {
                    Notification aviso = new Notification("Se debe seleccionar un registro del listado", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }

        });

        // Para generar el PDF de la factura
        pdfButton.addClickListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {

                if (idSeleccionado != null && !idSeleccionado.isEmpty()) {
                    Page.getCurrent().open("/albaranPesajes?&idPesaje=" + idSeleccionado, "_blank");
                } else {
                    Notification aviso = new Notification("Se debe seleccionar al menos un registro del listado", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }
        });

    }

    /**
     * Método que nos realiza la desactivación del empleado.
     * @param idSeleccionado El Id del empleado a desactivar.
     */
    @SuppressWarnings("unchecked")
    private void anularPesaje(String idSeleccionado) {

        try {
            if (idSeleccionado.contains(",")) {
                String[] ids = idSeleccionado.split(",");
                int i = 0;
                String result = "";
                while (i < ids.length) {
                    result = contrVista.desactivarPesaje(Integer.valueOf(ids[i]), user, time);

                    result = contrVista.obtenerDescripcionCodigo(result);
                    Item articulo = tablaPesajes.getItem("" + ids[i]);
                    String est = Constants.ANULADO;
                    if (articulo.getItemProperty("estado").getValue().equals("Activo")) {
                        est = Constants.ANULADO;
                    }
                    articulo.getItemProperty("estado").setValue(est);
                    i++;
                }
                Notification aviso = new Notification(result, Notification.Type.ASSISTIVE_NOTIFICATION);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            } else {
                String result = contrVista.desactivarPesaje(Integer.valueOf(idSeleccionado), user, time);

                result = contrVista.obtenerDescripcionCodigo(result);
                Notification aviso = new Notification(result, Notification.Type.ASSISTIVE_NOTIFICATION);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                Item articulo = tablaPesajes.getItem("" + idSeleccionado);
                String est = Constants.ANULADO;
                if (articulo.getItemProperty("estado").getValue().equals("Activo")) {
                    est = Constants.ANULADO;
                }
                articulo.getItemProperty("estado").setValue(est);
            }

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
        }
    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaPesajes(TPermisos permisos) {
        tablaPesajes = new TablaGenerica(new Object[] { "numeroAlbaran", "idCliente", "idDireccion", "idOperador", "idTransportista", "fechaPesaje", "obra", "origen", "idMaterial", "kgsBruto", "tara", "kgsNeto", "fechaCrea", "estado" }, new String[] { "Nº Albarán", "Cliente", "Dirección", "Operador", "Transportista", "Fecha", "Obra", "Origen", "Material", "Kilos brutos", "TARA", "Kilos netos", "Registrado por", "Estado" }, bcPesajes);
        tablaPesajes.addStyleName("big striped");
        tablaPesajes.setPageLength(25);

        // Establecemos tamaño fijo en columnas específicas.

        tablaPesajes.setMultiSelect(true);
        tablaPesajes.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                @SuppressWarnings("unchecked")
                Set<String> values = (Set<String>) tablaPesajes.getValue();
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

        tablaPesajes.addItemClickListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionado = (String) event.getItemId();
                if (event.isDoubleClick() && Utils.booleanFromInteger(permisos.getModificarPesaje())) {
                    getUI().getNavigator().navigateTo(VistaPesaje.NAME + "/" + idSeleccionado);
                }
            }
        });

        tablaPesajes.setCellStyleGenerator(new Table.CellStyleGenerator() {

            @SuppressWarnings("unchecked")
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                BeanItem<TPesajesVista> aux = (BeanItem<TPesajesVista>) source.getItem(itemId);

                TPesajesVista pedido = aux.getBean();
                if (pedido.getEstado().equals(Constants.FACTURADO)) {
                    return "green";
                } else if (pedido.getEstado().equals(Constants.ALBARAN)) {
                    return "orange";
                } else if (pedido.getEstado().equals(Constants.ANULADO)) {
                    return "gris_1";
                } else {
                    return "";
                }
            }
        });
    }

    /**
     * Clase para aplicar filtros al container.
     * @author Daniel Carmona Romero
     *
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
        fAlbaran = new TextField("");
        fAlbaran.setNullRepresentation("");
        fAlbaran.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcPesajes.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("numeroAlbaran", filtro, status);
                    bcPesajes.addContainerFilter(filter);
                    aplicarFiltro2(ALBARAN);
                } else {
                    fAlbaran.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por el CIF
        fObra = new TextField("");
        fObra.setNullRepresentation("");
        fObra.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcPesajes.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("obra", filtro, status);
                    bcPesajes.addContainerFilter(filter);
                    aplicarFiltro2(OBRA);
                } else {
                    fObra.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por la dirección
        fOrigen = new TextField("");
        fOrigen.setNullRepresentation("");
        fOrigen.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcPesajes.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("origen", filtro, status);
                    bcPesajes.addContainerFilter(filter);
                    aplicarFiltro2(ORIGEN);
                } else {
                    fOrigen.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por el código postal
        fDestino = new TextField("");
        fDestino.setNullRepresentation("");
        fDestino.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcPesajes.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("cp", filtro, status);
                    bcPesajes.addContainerFilter(filter);
                    aplicarFiltro2(ORIGEN);
                } else {
                    fDestino.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por la ciudad
        fMatricula = new TextField("");
        fMatricula.setNullRepresentation("");
        fMatricula.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcPesajes.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("matricula", filtro, status);
                    bcPesajes.addContainerFilter(filter);
                    aplicarFiltro2(MATRICULA);
                } else {
                    fMatricula.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por la ciudad
        fRemolque = new TextField("");
        fRemolque.setNullRepresentation("");
        fRemolque.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcPesajes.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("remolque", filtro, status);
                    bcPesajes.addContainerFilter(filter);
                    aplicarFiltro2(REMOLQUE);
                } else {
                    fRemolque.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por el cliente.
        fClientes = new ComboBox();
        fClientes.addItem("Todos");
        fClientes.addItems(lClientes);
        fClientes.setValue("Todos");
        fClientes.setNullSelectionAllowed(false);
        fClientes.setFilteringMode(FilteringMode.CONTAINS);

        fClientes.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltro();
            }
        });

        // Campo para filtrar por el cliente.
        fMaterial = new ComboBox();
        fMaterial.addItem("Todos");
        fMaterial.addItems(lMateriales);
        fMaterial.setValue("Todos");
        fMaterial.setNullSelectionAllowed(false);
        fMaterial.setFilteringMode(FilteringMode.CONTAINS);

        fMaterial.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltro();
            }
        });

        // Campo para filtrar por estado.
        fEstados = new ComboBox();
        fEstados.addItem("Todos");
        fEstados.addItem(Constants.ALBARAN);
        fEstados.addItem(Constants.FACTURADO);
        fEstados.addItem(Constants.ANULADO);
        fEstados.setValue("Todos");
        fEstados.setNullSelectionAllowed(false);
        fEstados.setFilteringMode(FilteringMode.CONTAINS);

        fEstados.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltro();
            }
        });

        // La fecha Desde
        fDesde = new DateField("");
        fDesde.setValue(Utils.obtenerPrimerDiaMesEnCurso());

        // La fecha Hasta
        fHasta = new DateField("");
        fHasta.setValue(Utils.obtenerUltimoDiaMesEnCurso());

        Label tituloFiltrar = new Label("Filtrar");
        tituloFiltrar.setStyleName("tituloTamano12");
        // Filtro cabecera
        VerticalLayout filtro = new VerticalLayout(tituloFiltrar);

        // Tabla de filtro
        Table table = new Table();
        table.addStyleName("big striped");
        // La cabecera de la tabla del filtro.
        table.addContainerProperty("Nº Albarán", TextField.class, null);
        table.addContainerProperty("Cliente", ComboBox.class, null);
        table.addContainerProperty("Material", ComboBox.class, null);
        table.addContainerProperty("Obra", TextField.class, null);
        table.addContainerProperty("Origen", TextField.class, null);
        table.addContainerProperty("Destino", TextField.class, null);
        table.addContainerProperty("Matrícula", TextField.class, null);
        table.addContainerProperty("Remolque", TextField.class, null);
        table.addContainerProperty("Estado", ComboBox.class, null);
        table.setPageLength(table.size());

        // Los componentes que componen al filtro
        table.addItem(new Object[] { fAlbaran, fClientes, fMaterial, fObra, fOrigen, fDestino, fMatricula, fRemolque, fEstados }, 1);

        // Fechas
        Table table2 = new Table();
        table2.addStyleName("big striped");
        table2.addContainerProperty("Fecha desde", DateField.class, null);
        table2.addContainerProperty("Fecha hasta", DateField.class, null);
        table2.addItem(new Object[] { fDesde, fHasta }, 1);
        table2.setPageLength(table.size());

        filtro.addComponent(table);
        filtro.addComponent(table2);
        filtro.setSpacing(true);
        filtro.setMargin(true);
        return filtro;
    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    private void aplicarFiltro() {
        boolean entra = false;
        bcPesajes.removeAllContainerFilters();
        if (fAlbaran.getValue() != null && !fAlbaran.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fAlbaran.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("nombre", filtro, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }

        if (fOrigen.getValue() != null && !fOrigen.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fOrigen.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("referencia", filtro, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }

        if (fDestino.getValue() != null && !fDestino.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fDestino.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("ler", filtro, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }

        if (fObra.getValue() != null && !fObra.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fObra.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("cif", filtro, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }

        if (fMatricula.getValue() != null && !fMatricula.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fMatricula.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("provincia", filtro, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }

        if (fRemolque.getValue() != null && !fRemolque.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fRemolque.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("remolque", filtro, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }

        if (!fClientes.getValue().equals("Todos")) {
            String nombreCliente = ((TClientes) fClientes.getValue()).getNombre().toLowerCase();
            if (nombreCliente.contains("(")) {
                nombreCliente = nombreCliente.replaceAll("\\(", " ");
            }
            if (nombreCliente.contains(")")) {
                nombreCliente = nombreCliente.replaceAll("\\)", " ");
            }
            // Set new filter for the "Name" column
            filter = new FiltroContainer("idCliente", (String) ".*(" + nombreCliente + ").*", status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }

        if (!fMaterial.getValue().equals("Todos")) {
            String nombreMaterial = ((TMateriales) fMaterial.getValue()).getDescripcion().toLowerCase();
            if (nombreMaterial.contains("(")) {
                nombreMaterial = nombreMaterial.replaceAll("\\(", " ");
            }
            if (nombreMaterial.contains(")")) {
                nombreMaterial = nombreMaterial.replaceAll("\\)", " ");
            }
            // Set new filter for the "Name" column
            filter = new FiltroContainer("idMaterial", (String) ".*(" + nombreMaterial + ").*", status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }

        if (!fEstados.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("estado", (String) ".*(" + fEstados.getValue().toString().toLowerCase() + ").*", status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }

        if (!entra) {
            bcPesajes.removeAllItems();
            try {
                if (fDesde.getValue() != null && fHasta.getValue() != null) {
                    String f1 = new SimpleDateFormat("dd/MM/yyyy").format(fDesde.getValue());
                    String f2 = new SimpleDateFormat("dd/MM/yyyy").format(fHasta.getValue());

                    bcPesajes.removeAllItems();
                    bcPesajes.addAll(contrVista.obtenerPesajesFechasVista(f1, f2, user, time));

                } else {
                    Notification aviso = new Notification("Se debe indica la fecha 'desde' y 'hasta' para mostrar resultados", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }

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
    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    private void aplicarFiltro2(String filtro) {
        boolean entra = false;

        if (fAlbaran.getValue() != null && !fAlbaran.getValue().isEmpty() && !filtro.equals(ALBARAN)) {
            // Set new filter for the "Name" column
            String[] params = fAlbaran.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("nombre", filtr, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }
        if (fAlbaran.getValue() != null && !fAlbaran.getValue().isEmpty()) {
            entra = true;
        }

        if (fOrigen.getValue() != null && !fOrigen.getValue().isEmpty() && !filtro.equals(ORIGEN)) {
            // Set new filter for the "Name" column
            String[] params = fOrigen.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("direccion", filtr, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }
        if (fOrigen.getValue() != null && !fOrigen.getValue().isEmpty()) {
            entra = true;
        }

        if (fDestino.getValue() != null && !fDestino.getValue().isEmpty() && !filtro.equals(DESTINO)) {
            // Set new filter for the "Name" column
            String[] params = fDestino.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("cp", filtr, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }
        if (fDestino.getValue() != null && !fDestino.getValue().isEmpty()) {
            entra = true;
        }

        if (fObra.getValue() != null && !fObra.getValue().isEmpty() && !filtro.equals(OBRA)) {
            // Set new filter for the "Name" column
            String[] params = fObra.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("cif", filtr, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }
        if (fDestino.getValue() != null && !fDestino.getValue().isEmpty()) {
            entra = true;
        }

        if (fMatricula.getValue() != null && !fMatricula.getValue().isEmpty() && !filtro.equals(MATRICULA)) {
            // Set new filter for the "Name" column
            String[] params = fMatricula.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("matricula", filtr, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }
        if (fMatricula.getValue() != null && !fMatricula.getValue().isEmpty()) {
            entra = true;
        }

        if (fRemolque.getValue() != null && !fRemolque.getValue().isEmpty() && !filtro.equals(REMOLQUE)) {
            // Set new filter for the "Name" column
            String[] params = fRemolque.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("remolque", filtr, status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }

        if (!fEstados.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("estado", (String) ".*(" + fEstados.getValue().toString().toLowerCase() + ").*", status);
            bcPesajes.addContainerFilter(filter);
            entra = true;
        }

        if (!entra) {
            try {
                if (fDesde.getValue() != null && fHasta.getValue() != null) {
                    String f1 = new SimpleDateFormat("dd/MM/yyyy").format(fDesde.getValue());
                    String f2 = new SimpleDateFormat("dd/MM/yyyy").format(fHasta.getValue());

                    bcPesajes.removeAllItems();
                    bcPesajes.addAll(contrVista.obtenerPesajesFechasVista(f1, f2, user, time));

                } else {
                    Notification aviso = new Notification("Se debe indica la fecha 'desde' y 'hasta' para mostrar resultados", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
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
    }

    private void cambiarFechas() {
        try {
            String f1, f2;

            if (fDesde.getValue() != null && fHasta.getValue() != null) {
                f1 = new SimpleDateFormat("dd/MM/yyyy").format(fDesde.getValue());
                f2 = new SimpleDateFormat("dd/MM/yyyy").format(fHasta.getValue());

                bcPesajes.removeAllItems();
                bcPesajes.addAll(contrVista.obtenerPesajesFechasVista(f1, f2, user, time));

            } else {
                Notification aviso = new Notification("Se debe indica la fecha 'desde' y 'hasta' para mostrar resultados", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }
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

    /**
     * Método que nos crea el componente donde están los botones de la vista.
     * @param permisos Los permisos que tiene el empleado activo.
     * @return El componente con los botones a partir de los permisos que tiene el empleado.
     */
    private HorizontalLayout crearBotonera(TPermisos permisos) {
        //Botonera
        HorizontalLayout botonera = new HorizontalLayout();
        botonera.setSpacing(true);
        botonera.addComponent(crearButton);
        botonera.addComponent(modificarButton);
        botonera.addComponent(eliminarButton);
        botonera.addComponent(pdfButton);
        botonera.setMargin(true);

        return botonera;
    }

    private HorizontalLayout crearBotonera2() {
        HorizontalLayout hor = new HorizontalLayout();
        hor.setSpacing(true);
        hor.setMargin(true);

        hor.addComponent(facturarButton);
        hor.addComponent(facturar2Button);

        return hor;
    }

    /**
     * Método que nos crea una tabla leyenda de colores.
     */
    @SuppressWarnings("unchecked")
    private void crearTablaColores() {
        tablaColores = new Table("");
        tablaColores.addContainerProperty("Estado", String.class, null);
        tablaColores.addContainerProperty("Estado2", String.class, null);
        tablaColores.addContainerProperty("Estado3", String.class, null);
        tablaColores.setSelectable(false);
        tablaColores.setCellStyleGenerator(new Table.CellStyleGenerator() {

            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                Item aux = (Item) source.getItem(itemId);

                if (propertyId == null) {
                    return "";
                }
                String estado = (String) aux.getItemProperty(propertyId).getValue();
                if (estado.equals(Constants.FACTURADO)) {
                    return "green";
                } else if (estado.equals(Constants.ALBARAN)) {
                    return "orange";
                } else if (estado.equals(Constants.ANULADO)) {
                    return "gris_1";
                } else {
                    return "";
                }
            }
        });
        tablaColores.addStyleName("striped");
        tablaColores.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
        tablaColores.setPageLength(0);

        // Estado documentado
        Object itemId = tablaColores.addItem();
        Item estado1 = tablaColores.getItem(itemId);
        estado1.getItemProperty("Estado").setValue(Constants.ALBARAN);
        estado1.getItemProperty("Estado2").setValue(Constants.FACTURADO);
        estado1.getItemProperty("Estado3").setValue(Constants.ANULADO);

    }

    /**
     * En este método se generan tantas facturas como diferentes registros se han seleccionado.
     */
    @SuppressWarnings("unchecked")
    private void generarFactura(Date fechaFactura) {
        if (fechaFactura != null) {
            try {
                if (!idSeleccionado.contains(",")) {
                    idSeleccionado.concat(",");
                }

                String ids[] = idSeleccionado.split(",");

                int cnt = 0;
                TPesajes p;
                TFacturas fac;
                TLineasFactura lFac;

                String texto = "";

                // Por cada registro seleccionado, creamos una factura
                while (cnt < ids.length) {

                    p = contrVista.obtenerPesajePorId(Integer.valueOf(ids[cnt]), user, time);
                    if (p != null) {
                        if (p.getEstado().equals(PesajesEnum.FACTURADO.getValue())) {
                            texto = "\n El pesaje con Nº de albarán " + p.getNumeroAlbaran() + " ya está facturado.";
                            cnt++;
                            continue;
                        }
                        if (p.getEstado().equals(PesajesEnum.ANULADO.getValue())) {
                            texto = "\n El pesaje con Nº de albarán " + p.getNumeroAlbaran() + " está anulado.";
                            cnt++;
                            continue;
                        }
                        fac = new TFacturas();
                        fac.setBase(p.getBase());
                        fac.setSubtotal(p.getBase());
                        fac.setDescuento(Double.valueOf(0));
                        fac.setEmpresa(1);
                        fac.setFechaCrea(Utils.generarFecha());
                        fac.setFechaFactura(fechaFactura);
                        fac.setIdCliente(p.getIdCliente());
                        fac.setIdDireccion(p.getIdDireccion());
                        fac.setObra(p.getObra());
                        fac.setSubtotal(p.getImporte() - p.getBase());
                        fac.setTotal(p.getImporte());
                        fac.setTotalNeto(p.getImporte());
                        fac.setUsuCrea(user);
                        fac.setId(contrVista.crearFacturaRetornaId(fac, user, time));
                        if (fac.getId() > 0) {
                            // Creamos la línea de la factura
                            lFac = new TLineasFactura();
                            lFac.setDescuento(Double.valueOf(0));
                            lFac.setIdFactura(fac.getId());
                            lFac.setIdPesaje(p.getId());
                            lFac.setImporte(fac.getTotal());
                            lFac.setIva(fac.getSubtotal());
                            lFac.setIdIva(p.getIdIva());
                            lFac.setKgsBrutos(p.getKgsBruto());
                            lFac.setKgsNetos(p.getKgsNeto());
                            lFac.setNumeroAlbaran(p.getNumeroAlbaran());
                            lFac.setTara(p.getTara());
                            lFac.setTotal(p.getImporte());
                            lFac.setIdMaterial(p.getIdMaterial());
                            lFac.setDescripcionMaterial(p.getDescMaterial());
                            lFac.setLerMarerial(p.getLerMaterial());
                            lFac.setReferenciaMaterial(p.getRefMaterial());
                            contrVista.crearLineaFactura(lFac, user, time);

                            // Marcamos el pesaje como facturado.
                            p.setEstado(PesajesEnum.FACTURADO.getValue());
                            p.setIdFactura(fac.getId());
                            contrVista.modificarPesaje(p, user, time);
                            Item articulo = tablaPesajes.getItem("" + ids[cnt]);
                            articulo.getItemProperty("estado").setValue(Constants.FACTURADO);

                        } else if (fac.getId().equals(-2)) {
                            Notification aviso = new Notification("No se ha podido generar el nº de factura, contacta con el administrador.", Notification.Type.ERROR_MESSAGE);
                            aviso.setPosition(Position.MIDDLE_CENTER);
                            aviso.show(Page.getCurrent());
                        } else {
                            Notification aviso = new Notification("No se ha podido generar la factura, contacta con el administrador", Notification.Type.ERROR_MESSAGE);
                            aviso.setPosition(Position.MIDDLE_CENTER);
                            aviso.show(Page.getCurrent());
                        }
                    }

                    cnt++;
                }
                if (texto.isEmpty()) {
                    Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(Constants.OPERACION_OK), Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                } else {
                    Notification aviso = new Notification(texto, Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }

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
            Notification aviso = new Notification("Se debe indicar la fecha de la factura", Notification.Type.ERROR_MESSAGE);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
        }
    }

    /**
     * En este método se generan tantas facturas como diferentes registros se han seleccionado.
     */
    @SuppressWarnings("unchecked")
    private void generarMultiFactura(Date fechaFactura) {
        if (fechaFactura != null) {
            try {
                if (!idSeleccionado.contains(",")) {
                    Notification aviso = new Notification("Se debe seleccionar al menos dos registros del listado.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                String ids[] = idSeleccionado.split(",");

                List<Integer> lIds = Utils.generarListaGenerica();
                int cnt = 0;
                while (cnt < ids.length) {
                    lIds.add(Integer.valueOf(ids[cnt]));
                    cnt++;
                }

                TFacturas fac;
                TLineasFactura lFac;

                String texto = "";

                List<TPesajes> lPesajes = contrVista.obtenerPesajesIds(lIds, user, time);

                cnt = 0;
                Double base = Double.valueOf(0);
                Double iva = Double.valueOf(0);
                Double total = Double.valueOf(0);

                Integer idCliente = null;
                Integer idDireccion = null;
                String numAlbaranes = "";
                String obra = "";
                for (TPesajes p : lPesajes) {
                    if (cnt == 0) {
                        idCliente = p.getIdCliente();
                        idDireccion = p.getIdDireccion();
                        obra = p.getObra();
                        numAlbaranes = numAlbaranes + p.getNumeroAlbaran() + ", ";
                    } else {
                        if (idCliente.equals(p.getIdCliente())) {
                            Notification aviso = new Notification("Todos los registros deben tener el mismo cliente", Notification.Type.ERROR_MESSAGE);
                            aviso.setPosition(Position.MIDDLE_CENTER);
                            aviso.show(Page.getCurrent());
                            return;
                        }
                    }
                    if (p.getEstado().equals(PesajesEnum.FACTURADO.getValue()) || p.getEstado().equals(PesajesEnum.ANULADO.getValue())) {
                        Notification aviso = new Notification("Existen pesajes facturados y/o anulados", Notification.Type.ERROR_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        return;
                    }
                    base = Utils.redondeoDecimales(2, base + p.getBase());
                    iva = Utils.redondeoDecimales(2, iva + p.getImporte() - p.getBase());
                    total = Utils.redondeoDecimales(2, total + p.getImporte());
                }

                // Por cada registro seleccionado, creamos una factura

                numAlbaranes = numAlbaranes.substring(0, numAlbaranes.length() - 2);

                fac = new TFacturas();
                fac.setBase(base);
                fac.setSubtotal(base);
                fac.setDescuento(Double.valueOf(0));
                fac.setEmpresa(1);
                fac.setFechaCrea(Utils.generarFecha());
                fac.setFechaFactura(fechaFactura);
                fac.setIdCliente(idCliente);
                fac.setIdDireccion(idDireccion);
                fac.setObra(obra);
                fac.setSubtotal(iva);
                fac.setTotal(total);
                fac.setTotalNeto(total);
                fac.setUsuCrea(user);
                fac.setId(contrVista.crearFacturaRetornaId(fac, user, time));
                for (TPesajes p : lPesajes) {
                    if (fac.getId() > 0) {
                        // Creamos la línea de la factura
                        lFac = new TLineasFactura();
                        lFac.setDescuento(Double.valueOf(0));
                        lFac.setIdFactura(fac.getId());
                        lFac.setIdPesaje(p.getId());
                        lFac.setImporte(fac.getTotal());
                        lFac.setIva(fac.getSubtotal());
                        lFac.setIdIva(p.getIdIva());
                        lFac.setKgsBrutos(p.getKgsBruto());
                        lFac.setKgsNetos(p.getKgsNeto());
                        lFac.setNumeroAlbaran(p.getNumeroAlbaran());
                        lFac.setTara(p.getTara());
                        lFac.setTotal(p.getImporte());
                        lFac.setIdMaterial(p.getIdMaterial());
                        lFac.setDescripcionMaterial(p.getDescMaterial());
                        lFac.setLerMarerial(p.getLerMaterial());
                        lFac.setReferenciaMaterial(p.getRefMaterial());
                        contrVista.crearLineaFactura(lFac, user, time);

                        // Marcamos el pesaje como facturado.
                        p.setEstado(PesajesEnum.FACTURADO.getValue());
                        p.setIdFactura(fac.getId());
                        contrVista.modificarPesaje(p, user, time);
                        Item articulo = tablaPesajes.getItem("" + p.getId());
                        articulo.getItemProperty("estado").setValue(Constants.FACTURADO);

                    } else if (fac.getId().equals(-2)) {
                        Notification aviso = new Notification("No se ha podido generar el nº de factura, contacta con el administrador.", Notification.Type.ERROR_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                    } else {
                        Notification aviso = new Notification("No se ha podido generar la factura, contacta con el administrador", Notification.Type.ERROR_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                    }

                }
                if (texto.isEmpty()) {
                    Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(Constants.OPERACION_OK), Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                } else {
                    Notification aviso = new Notification(texto, Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }

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
            Notification aviso = new Notification("Se debe indicar la fecha de la factura", Notification.Type.ERROR_MESSAGE);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
        }
    }

}
