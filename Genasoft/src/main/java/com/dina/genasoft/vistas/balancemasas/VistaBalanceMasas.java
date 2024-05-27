/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.balancemasas;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.PatternSyntaxException;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TVentasVista;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.TablaGenerica;
import com.dina.genasoft.utils.Utils;
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
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaBalanceMasas.NAME)
public class VistaBalanceMasas extends CustomComponent implements View ,Button.ClickListener {
    /** El nombre de la vista.*/
    public static final String                  NAME                     = "vBalanceMasas";
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TVentasVista> bcMasasOrigen;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TVentasVista> bcMasasOrigenTotales;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TVentasVista> bcMasasOrigenTotalesCalidad;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TVentasVista> bcMasasGgnTotalesCalidad;
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas                   contrVista;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                              excelButton;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                              pdfButton;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                              generarBalanceButton;
    // Elementos para realizar busquedas
    /** Para filtrar por estado. */
    private ComboBox                            fTipoOrigen;
    /** Para filtrar por estado. */
    private ComboBox                            fOrigen;
    /** Para filtrar por estado. */
    private ComboBox                            fCalidad;
    /** Para filtrar por estado. */
    private ComboBox                            fArticulos;
    /** Para filtrar por estado. */
    private ComboBox                            fVariedades;
    /** Para filtrar por GGN. */
    private ComboBox                            fGGn;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                              appName;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger       log                      = org.slf4j.LoggerFactory.getLogger(VistaBalanceMasas.class);
    /** El usuario que está logado. */
    private Integer                             user                     = null;
    /** La fecha en que se inició sesión. */
    private Long                                time                     = null;
    /** Tabla para mostrar las compras del sistema. */
    private Table                               tablaMasasOrigen         = null;
    /** Tabla para mostrar las compras del sistema. */
    private Table                               tablaMasasOrigenCalidad  = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                               tablaMasasGgn            = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                               tablaMasasProducto       = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                               tablaMasasOrigenTotales  = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                               tablaMasasCalidadTotales = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                               tablaMasasGgnTotales     = null;
    /** Arbol con los resultados. */
    private TreeMap<Date, List<TVentasVista>>   mMasas                   = null;
    /** La fecha desde para los informes. */
    private DateField                           fechaDesdeBalance;
    /** La fecha hasta para los informes. */
    private DateField                           fechaHastaBalance;
    private HorizontalLayout                    cabeceraPantalla;
    /** El filtro que se le aplica al container. */
    private FiltroContainer                     filter;
    /** No tengo ni puta idea para que sirve. */
    private final Label                         status                   = new Label("");
    private TPermisos                           permisos;
    private TEmpleados                          empleado;
    private ComboBox                            cbTipoBalance;
    private Table                               table;
    private Table                               table12;

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

            List<TVentasVista> lVentas = Utils.generarListaGenerica();
            try {

                bcMasasOrigen = new BeanContainer<>(TVentasVista.class);
                bcMasasOrigen.setBeanIdProperty("id");

                bcMasasOrigenTotales = new BeanContainer<>(TVentasVista.class);
                bcMasasOrigenTotales.setBeanIdProperty("id");

                bcMasasOrigenTotalesCalidad = new BeanContainer<>(TVentasVista.class);
                bcMasasOrigenTotalesCalidad.setBeanIdProperty("id");

                bcMasasGgnTotalesCalidad = new BeanContainer<>(TVentasVista.class);
                bcMasasGgnTotalesCalidad.setBeanIdProperty("id");

                Label texto = new Label("Balance de masas");
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

                if (!Utils.booleanFromInteger(permisos.getBalanceMasas())) {
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

                crearTablaMasasOrigen(permisos);
                crearTablaMasasCalidad(permisos);
                crearTablaMasasGgn(permisos);
                crearTablaMasasProductos(permisos);
                crearTablaMasasOrigenTotales();
                crearTablaMasasCalidadTotales();
                crearTablaMasasGgnTotales();

                //mMasas = contrVista.obtenerBalancePorOrigen(user, time);

                List<Date> lFechas = Utils.generarListaGenerica();
                List<TVentasVista> lAux2 = Utils.generarListaGenerica();

                List<String> lArticulos = contrVista.obtenerVariedades(user, time);
                List<String> lFamilias = contrVista.obtenerFamiliasCompras(user, time);

                //  lFechas.addAll(mMasas.keySet());

                // Cargamos los datos de las ventas
                for (Date key : lFechas) {
                    lAux2 = (mMasas.get(key));
                    for (TVentasVista res : lAux2) {
                        lVentas.add(res);
                    }
                }

                lVentas.sort(new Comparator<TVentasVista>() {

                    @Override
                    public int compare(TVentasVista m1, TVentasVista m2) {
                        if (m1.getVariedad() == m2.getVariedad()) {
                            return 0;
                        }
                        return m1.getVariedad().compareTo(m2.getVariedad());
                    }

                });

                bcMasasOrigen.removeAllItems();
                bcMasasOrigen.addAll(lVentas);

                tablaMasasOrigenCalidad.setVisible(false);
                tablaMasasGgn.setVisible(false);
                tablaMasasProducto.setVisible(false);
                tablaMasasOrigenTotales.setVisible(true);
                tablaMasasCalidadTotales.setVisible(false);
                tablaMasasGgnTotales.setVisible(false);

                Label tituloFiltrar = new Label("Filtrar");
                tituloFiltrar.setStyleName("tituloTamano12");

                // Creamos el componente filtro.
                crearComponenteFiltro(lArticulos, lFamilias);

                // Creamos la botonera.
                viewLayout.addComponent(cabeceraPantalla);
                viewLayout.addComponent(generarBalanceButton);

                HorizontalLayout horBot = new HorizontalLayout();
                horBot.setSpacing(true);
                horBot.addComponent(excelButton);
                //horBot.addComponent(pdfButton);

                //viewLayout.addComponent(tabTotales);
                viewLayout.addComponent(horBot);
                viewLayout.addComponent(tablaMasasOrigenTotales);
                viewLayout.addComponent(tablaMasasCalidadTotales);
                viewLayout.addComponent(tablaMasasGgnTotales);
                viewLayout.addComponent(tablaMasasProducto);
                viewLayout.addComponent(tablaMasasOrigen);
                viewLayout.addComponent(tablaMasasOrigenCalidad);
                viewLayout.addComponent(tablaMasasGgn);

                // Añadimos el logo del cliente
                viewLayout.addComponent(contrVista.logoCliente());
                setCompositionRoot(viewLayout);
                // Establecemos el porcentaje de ratio para los layouts
                viewLayout.setExpandRatio(titulo, 0.1f);
                viewLayout.setMargin(true);
                viewLayout.setSpacing(true);

                bcMasasOrigen.removeAllItems();
                //bcMasasOrigen.addAll(contrVista.obtenerBalancePorProducto("Todos", "Todas", "Todos", "Todos", "Todas", null, null, user, time));

                tablaMasasProducto.setVisible(true);
                tablaMasasOrigenCalidad.setVisible(false);
                tablaMasasCalidadTotales.setVisible(false);
                tablaMasasOrigen.setVisible(false);
                tablaMasasOrigenTotales.setVisible(true);
                tablaMasasGgnTotales.setVisible(false);
                tablaMasasGgn.setVisible(false);
                calcularTotales();
                generarBalanceButton.setVisible(true);

                fOrigen.setEnabled(true);
                fCalidad.setEnabled(true);
                fOrigen.addItem("DESGLOSADO");
                fCalidad.addItem("DESGLOSADO");

                calcularTotales();

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
        generarBalanceButton = new Button("Generar balance", this);

        excelButton = new Button("", this);
        excelButton.setIcon(new ThemeResource("icons/excel-32.ico"));
        excelButton.setStyleName(BaseTheme.BUTTON_LINK);

        pdfButton = new Button("", this);
        pdfButton.setIcon(new ThemeResource("icons/pdf-32.ico"));
        pdfButton.setStyleName(BaseTheme.BUTTON_LINK);

    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(generarBalanceButton)) {

            Date f1 = fechaDesdeBalance.getValue();
            Date f2 = fechaHastaBalance.getValue();

            // Es la lógica antigua
            if (table.isVisible()) {

                String origen = (String) fOrigen.getValue();
                String producto = (String) fArticulos.getValue();
                String variedad = (String) fVariedades.getValue();
                String certificacion = (String) fCalidad.getValue();
                String ggn = (String) fGGn.getValue();

                bcMasasOrigen.removeAllItems();

                try {
                    bcMasasOrigen.addAll(contrVista.obtenerBalancePorProducto(producto, certificacion, origen, ggn, variedad, f1, f2, user, time));
                } catch (GenasoftException tbe) {
                    log.error("La sesión es inválida, se ha iniciado sesión en otro dispositivo.");
                    // Si no se encuentran permisos con el rol especificado, informamos al empleado y cerramos sesión.
                    Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("userInf", null);
                    getSession().setAttribute("fechaInf", null);
                    // Redirigimos a la página de inicio.
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                } catch (MyBatisSystemException e) {
                    Notification aviso = new Notification("No se ha podido establecer conexión con la base de datos.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    log.error("Error al obtener datos de base de datos ", e);
                    return;
                }
            } else {
                try {
                    String tipoBalance = cbTipoBalance.getValue().toString();

                    bcMasasOrigen.removeAllItems();
                    bcMasasOrigen.addAll(contrVista.obtenerBalancePorProducto2(tipoBalance, f1, f2, user, time));
                } catch (GenasoftException tbe) {
                    log.error("La sesión es inválida, se ha iniciado sesión en otro dispositivo.");
                    // Si no se encuentran permisos con el rol especificado, informamos al empleado y cerramos sesión.
                    Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("userInf", null);
                    getSession().setAttribute("fechaInf", null);
                    // Redirigimos a la página de inicio.
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                } catch (MyBatisSystemException e) {
                    Notification aviso = new Notification("No se ha podido establecer conexión con la base de datos.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    log.error("Error al obtener datos de base de datos ", e);
                    return;
                }
            }
            tablaMasasProducto.setVisible(true);
            tablaMasasOrigenCalidad.setVisible(false);
            tablaMasasCalidadTotales.setVisible(false);
            tablaMasasOrigen.setVisible(false);
            tablaMasasOrigenTotales.setVisible(true);
            tablaMasasGgnTotales.setVisible(false);
            tablaMasasGgn.setVisible(false);
            calcularTotales();

        } else if (event.getButton().equals(excelButton)) {

            try {
                guardarRegistrosTablasExportacion();

                Page.getCurrent().open("/exportarLibroMasasExcel?idEmpleado=" + user, "_blank");

            } catch (GenasoftException tbe) {
                log.error("La sesión es inválida, se ha iniciado sesión en otro dispositivo.");
                // Si no se encuentran permisos con el rol especificado, informamos al articulo y cerramos sesión.
                Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                getSession().setAttribute("user", null);
                getSession().setAttribute("fecha", null);
                // Redirigimos a la página de inicio.
                getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
            }
        } else if (event.getButton().equals(pdfButton)) {
            try {
                guardarRegistrosTablasExportacion();

                Page.getCurrent().open("/exportarLibroMasasPdf?idEmpleado=" + user, "_blank");

            } catch (GenasoftException tbe) {
                log.error("La sesión es inválida, se ha iniciado sesión en otro dispositivo.");
                // Si no se encuentran permisos con el rol especificado, informamos al articulo y cerramos sesión.
                Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                getSession().setAttribute("user", null);
                getSession().setAttribute("fecha", null);
                // Redirigimos a la página de inicio.
                getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
            }
        }

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaMasasOrigen(TPermisos permisos) {
        tablaMasasOrigen = new TablaGenerica(new Object[] { "familia", "variedad", "origen", "kgs", "kgsFin", "kgsEnvase" }, new String[] { "Producto", "Variedad", "Orígen", "Compras(kg)", "Ventas(kg)", "Stock(kg)" }, bcMasasOrigen);
        tablaMasasOrigen.addStyleName("big striped");
        tablaMasasOrigen.setPageLength(15);
        tablaMasasOrigen.setId("1");
        tablaMasasOrigen.setColumnCollapsingAllowed(false);
        tablaMasasOrigen.setColumnReorderingAllowed(false);
        tablaMasasOrigen.setSelectable(true);
        tablaMasasOrigen.setSortEnabled(true);

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaMasasCalidad(TPermisos permisos) {
        tablaMasasOrigenCalidad = new TablaGenerica(new Object[] { "familia", "variedad", "calidadCompra", "kgs", "kgsFin", "kgsEnvase" }, new String[] { "Producto", "Variedad", "Certificación", "Compras(kg)", "Ventas(kg)", "Stock(kg)" }, bcMasasOrigen);
        tablaMasasOrigenCalidad.addStyleName("big striped");
        tablaMasasOrigenCalidad.setPageLength(15);
        tablaMasasOrigenCalidad.setId("1");
        tablaMasasOrigenCalidad.setColumnCollapsingAllowed(false);
        tablaMasasOrigenCalidad.setColumnReorderingAllowed(false);
        tablaMasasOrigenCalidad.setSelectable(true);
        tablaMasasOrigenCalidad.setSortEnabled(true);

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaMasasGgn(TPermisos permisos) {
        tablaMasasGgn = new TablaGenerica(new Object[] { "familia", "variedad", "calidadCompra", "albaran", "kgs", "kgsFin", "kgsEnvase" }, new String[] { "Producto", "Variedad", "Certificación", "GGN", "Compras(kg)", "Ventas(kg)", "Stock(kg)" }, bcMasasOrigen);
        tablaMasasGgn.addStyleName("big striped");
        tablaMasasGgn.setPageLength(15);
        tablaMasasGgn.setId("1");
        tablaMasasGgn.setColumnCollapsingAllowed(false);
        tablaMasasGgn.setColumnReorderingAllowed(false);
        tablaMasasGgn.setSelectable(true);
        tablaMasasGgn.setSortEnabled(true);

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaMasasProductos(TPermisos permisos) {
        tablaMasasProducto = new TablaGenerica(new Object[] { "familia", "variedad", "calidadCompra", "origen", "albaran", "kgsNetos", "kgsNetosFin", "kgsEnvase" }, new String[] { "Producto", "Variedad", "Certificación", "Orígen", "GGN", "Compras(kg)", "Ventas(kg)", "Stock(kg)" }, bcMasasOrigen);
        tablaMasasProducto.addStyleName("big striped");
        tablaMasasProducto.setPageLength(15);
        tablaMasasProducto.setId("1");
        tablaMasasProducto.setColumnCollapsingAllowed(false);
        tablaMasasProducto.setColumnReorderingAllowed(false);
        tablaMasasProducto.setSelectable(true);
        tablaMasasProducto.setSortEnabled(true);

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaMasasOrigenTotales() {
        tablaMasasOrigenTotales = new TablaGenerica(new Object[] { "kgs", "kgsFin", "kgsEnvase" }, new String[] { "Compras(kg)", "Ventas(kg)", "Stock(kg)" }, bcMasasOrigenTotales);
        tablaMasasOrigenTotales.addStyleName("big strong");
        tablaMasasOrigenTotales.setPageLength(1);
        // Para mostrar/ocultar columnas de la tabla
        tablaMasasOrigenTotales.setColumnCollapsingAllowed(false);
        // Para cambiar el orden las columnas
        tablaMasasOrigenTotales.setColumnReorderingAllowed(false);
        tablaMasasOrigenTotales.setSelectable(true);
        tablaMasasOrigenTotales.setSortEnabled(true);

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaMasasCalidadTotales() {
        tablaMasasCalidadTotales = new TablaGenerica(new Object[] { "lote", "loteFin", "albaran", "albaranFin", "calibre", "calibreFin" }, new String[] { "Compras(kg) (Calidad BIO)", "Compras(kg) (Calidad CONVENCIONAL)", "Ventas(kg) (Calidad BIO)", "Ventas(kg) (Calidad CONVENCIONAL)", "Stock(kg) (Calidad BIO)", "Stock(kg) (Calidad CONVENCIONAL)" }, bcMasasOrigenTotalesCalidad);
        tablaMasasCalidadTotales.addStyleName("big strong");
        tablaMasasCalidadTotales.setPageLength(1);
        // Para mostrar/ocultar columnas de la tabla
        tablaMasasCalidadTotales.setColumnCollapsingAllowed(false);
        // Para cambiar el orden las columnas
        tablaMasasCalidadTotales.setColumnReorderingAllowed(false);
        tablaMasasCalidadTotales.setSelectable(true);
        tablaMasasCalidadTotales.setSortEnabled(true);

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaMasasGgnTotales() {
        tablaMasasGgnTotales = new TablaGenerica(new Object[] { "lote", "loteFin", "albaran", "albaranFin", "calibre", "calibreFin" }, new String[] { "Compras(kg) (Calidad BIO)", "Compras(kg) (Calidad CONVENCIONAL)", "Ventas(kg) (Calidad BIO)", "Ventas(kg) (Calidad CONVENCIONAL)", "Stock(kg) (Calidad BIO)", "Stock(kg) (Calidad CONVENCIONAL)" }, bcMasasGgnTotalesCalidad);
        tablaMasasGgnTotales.addStyleName("big strong");
        tablaMasasGgnTotales.setPageLength(1);
        // Para mostrar/ocultar columnas de la tabla
        tablaMasasGgnTotales.setColumnCollapsingAllowed(false);
        // Para cambiar el orden las columnas
        tablaMasasGgnTotales.setColumnReorderingAllowed(false);
        tablaMasasGgnTotales.setSelectable(false);
        tablaMasasGgnTotales.setSortEnabled(false);

    }

    /**
     * Método que se encarga de construir el componente que conforma la parte del filtro
     * @return El componente que conforma la parte del filtro.
     */
    private void crearComponenteFiltro(List<String> lArticulos, List<String> lFamilias) {

        // Cabecera de la pantalla
        cabeceraPantalla = new HorizontalLayout();
        cabeceraPantalla.setSpacing(true);
        cabeceraPantalla.setMargin(true);

        cbTipoBalance = new ComboBox();
        cbTipoBalance.addItem("Certificación-Nacional/importación");
        cbTipoBalance.addItem("Certificación-Origen");
        cbTipoBalance.addItem("Origen-Global Gap");
        cbTipoBalance.setValue("Certificación-Nacional/importación");
        cbTipoBalance.setNullSelectionAllowed(false);
        cbTipoBalance.setNewItemsAllowed(false);
        cbTipoBalance.setWidth(17, Sizeable.Unit.EM);

        fTipoOrigen = new ComboBox();
        fTipoOrigen.addItem("Nacional/No nacional");
        fTipoOrigen.addItem("Por producto");
        fTipoOrigen.addItem("Por paises");
        fTipoOrigen.addItem("Por calidad");
        fTipoOrigen.addItem("Por ggn");
        fTipoOrigen.setValue("Por producto");
        fTipoOrigen.setNullSelectionAllowed(false);
        fTipoOrigen.setEnabled(false);
        fTipoOrigen.setFilteringMode(FilteringMode.CONTAINS);
        fTipoOrigen.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (fTipoOrigen.getValue().equals("Nacional/No nacional")) {
                    cambiarFechas();
                    calcularTotales();
                    fCalidad.setEnabled(false);
                    fGGn.setEnabled(false);
                    fCalidad.setValue("Todos");
                    fGGn.setValue("Todos");
                    fOrigen.setEnabled(false);
                    fOrigen.removeItem("DESGLOSADO");
                    fCalidad.removeItem("DESGLOSADO");
                    fGGn.removeAllItems();
                    fGGn.addItem("NO GG");
                    fGGn.addItem("GGN NACIONAL");
                    fGGn.addItem("GGN IMPORTACIÓN");
                    fGGn.addItem("Todos");
                    fGGn.setValue("Todos");
                } else if (fTipoOrigen.getValue().equals("Por paises")) {
                    cambiarFechas();
                    calcularTotales();
                    fCalidad.setEnabled(false);
                    fGGn.setEnabled(false);
                    fCalidad.setValue("Todos");
                    fGGn.setValue("Todos");
                    fOrigen.setEnabled(true);
                    fOrigen.setValue("Todos");
                    fOrigen.removeItem("DESGLOSADO");
                    fCalidad.removeItem("DESGLOSADO");
                    fGGn.removeAllItems();
                    fGGn.addItem("NO GG");
                    fGGn.addItem("GGN NACIONAL");
                    fGGn.addItem("GGN IMPORTACIÓN");
                    fGGn.addItem("Todos");
                    fGGn.setValue("Todos");
                } else if (fTipoOrigen.getValue().equals("Por ggn")) {
                    cambiarFechas();
                    calcularTotales();
                    fCalidad.setEnabled(false);
                    fGGn.setEnabled(false);
                    fCalidad.setValue("Todos");
                    fGGn.setEnabled(true);
                    fGGn.setValue("Todos");
                    fOrigen.setEnabled(true);
                    fOrigen.setValue("Todos");
                    fOrigen.removeItem("DESGLOSADO");
                    fCalidad.removeItem("DESGLOSADO");
                    fGGn.removeAllItems();
                    fGGn.addItem("NO GG");
                    fGGn.addItem("GGN NACIONAL");
                    fGGn.addItem("GGN IMPORTACIÓN");
                    fGGn.addItem("Todos");
                    fGGn.setValue("Todos");
                } else if (fTipoOrigen.getValue().equals("Por producto")) {
                    cambiarFechas();
                    calcularTotales();
                    fVariedades.setEnabled(false);
                    fVariedades.setValue("Todas");
                    fCalidad.setEnabled(true);
                    fCalidad.setValue("Todos");
                    fGGn.setEnabled(true);
                    fGGn.setValue("Todos");
                    fOrigen.setEnabled(true);
                    fOrigen.setValue("Todos");
                    fOrigen.addItem("DESGLOSADO");
                    fCalidad.addItem("DESGLOSADO");
                    fGGn.removeAllItems();
                    fGGn.addItem("Todos");
                    fGGn.addItem("Sí");
                    fGGn.addItem("No");
                    fGGn.setValue("Todos");
                } else {
                    cambiarFechas();
                    calcularTotales();
                    fCalidad.setEnabled(true);
                    fCalidad.setValue("Todos");
                    fGGn.setEnabled(false);
                    fGGn.setValue("Todos");
                    fOrigen.setEnabled(true);
                    fOrigen.setValue("Todos");
                    fOrigen.removeItem("DESGLOSADO");
                    fCalidad.removeItem("DESGLOSADO");
                    fGGn.removeAllItems();
                    fGGn.addItem("NO GG");
                    fGGn.addItem("GGN NACIONAL");
                    fGGn.addItem("GGN IMPORTACIÓN");
                    fGGn.addItem("Todos");
                    fGGn.setValue("Todos");
                }
            }
        });

        fOrigen = new ComboBox();
        fOrigen.addItem("Todos");
        fOrigen.addItem("DESGLOSADO");
        fOrigen.addItem("DESGLOSADO POR PAIS");
        fOrigen.addItem("DESGLOSADO NACIONAL/NO NACIONAL");
        fOrigen.addItem("NACIONAL");
        fOrigen.addItem("NO NACIONAL");
        fOrigen.setValue("Todos");
        fOrigen.setNullSelectionAllowed(false);
        fOrigen.setFilteringMode(FilteringMode.CONTAINS);
        fOrigen.setEnabled(false);
        fOrigen.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (!fTipoOrigen.getValue().equals("Por producto") && !fOrigen.getValue().equals("DESGLOSADO") && !fCalidad.getValue().equals("DESGLOSADO")) {
                    cambiarFechas();
                    calcularTotales();
                } else if (fTipoOrigen.getValue().equals("Por producto") && !fOrigen.getValue().equals("DESGLOSADO") && !fCalidad.getValue().equals("DESGLOSADO")) {
                    aplicarFiltroCalidades();
                }
            }
        });

        fArticulos = new ComboBox();
        fArticulos.addItem("Todos");
        fArticulos.addItem("DESGLOSADO");
        fArticulos.addItems(lArticulos);
        fArticulos.setValue("Todos");
        fArticulos.setNullSelectionAllowed(false);
        fArticulos.setFilteringMode(FilteringMode.CONTAINS);
        fArticulos.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltroCalidades();
            }
        });

        fVariedades = new ComboBox();
        fVariedades.addItem("Todas");
        fVariedades.addItem("DESGLOSADO");
        fVariedades.addItems(lFamilias);
        fVariedades.setValue("Todas");
        fVariedades.setNullSelectionAllowed(false);
        fVariedades.setFilteringMode(FilteringMode.CONTAINS);
        fVariedades.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltroCalidades();
            }
        });

        fCalidad = new ComboBox();
        fCalidad.addItem("Todas");
        fCalidad.addItem("DESGLOSADO");
        fCalidad.addItem("BIO");
        fCalidad.addItem("CONVENCIONAL");
        fCalidad.setValue("Todas");
        fCalidad.setEnabled(false);
        fCalidad.setNullSelectionAllowed(false);
        fCalidad.setFilteringMode(FilteringMode.CONTAINS);
        fCalidad.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltroCalidades();
            }
        });

        fGGn = new ComboBox();
        fGGn.addItem("Todos");
        fGGn.addItem("DESGLOSADO");
        fGGn.addItem("NO GG");
        fGGn.addItem("GGN NACIONAL");
        fGGn.addItem("GGN IMPORTACIÓN");
        fGGn.setValue("Todos");
        fGGn.setNullSelectionAllowed(false);
        fGGn.setFilteringMode(FilteringMode.CONTAINS);
        fGGn.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltroCalidades();
            }
        });

        cabeceraPantalla.setVisible(true);

        /************************************************************************************************************/

        /************************************************** FECHAS **************************************************/
        fechaDesdeBalance = new DateField();
        fechaDesdeBalance.setWidth(9, Sizeable.Unit.EM);
        fechaDesdeBalance.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                cambiarFechas();
            }
        });

        // Fecha compra hasta.
        fechaHastaBalance = new DateField();
        fechaHastaBalance.setWidth(9, Sizeable.Unit.EM);
        fechaHastaBalance.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                cambiarFechas();
            }
        });

        // Tabla de filtro
        table = new Table();
        table.addStyleName("small strong");
        // La cabecera de la tabla del filtro.
        table.addContainerProperty("Tipo orígen", ComboBox.class, null);
        table.addContainerProperty("Orígen", ComboBox.class, null);
        table.addContainerProperty("Producto", ComboBox.class, null);
        table.addContainerProperty("Variedad", ComboBox.class, null);
        table.addContainerProperty("Certificación", ComboBox.class, null);
        table.addContainerProperty("GGN", ComboBox.class, null);

        table12 = new Table();
        table12.addStyleName("small strong");
        table12.addContainerProperty("Por producto", ComboBox.class, null);

        // Los componentes que componen al filtro
        table.addItem(new Object[] { fTipoOrigen, fOrigen, fArticulos, fVariedades, fCalidad, fGGn }, 1);
        table.setPageLength(table.size());

        // Los componentes que componen al filtro
        table12.addItem(new Object[] { cbTipoBalance }, 1);
        table12.setPageLength(table.size());

        Table table2 = new Table();
        table2.addStyleName("small strong");
        table2.addContainerProperty("Fecha desde", DateField.class, null);
        table2.addContainerProperty("Fecha hasta", DateField.class, null);
        table2.addItem(new Object[] { fechaDesdeBalance, fechaHastaBalance }, 1);
        table2.setPageLength(table.size());

        table.setVisible(false);

        cabeceraPantalla.addComponent(table);
        cabeceraPantalla.addComponent(table12);
        cabeceraPantalla.addComponent(table2);

    }

    /**
     * Método que nos sirve para calcular los totales de los datos que se muestran en pantalla.
     */
    @SuppressWarnings("unchecked")
    private void calcularTotales() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        TVentasVista vTotales = new TVentasVista();
        if (tablaMasasOrigen.isVisible() || tablaMasasProducto.isVisible()) {
            // Masas
            Double totalCompras = Double.valueOf(0);
            Double totalVentas = Double.valueOf(0);
            Double stock = Double.valueOf(0);

            // Calculamos los conceptos de compras
            List<String> lIds = bcMasasOrigen.getItemIds();

            BeanItem<TVentasVista> bResV = null;
            TVentasVista resV = null;

            for (String id : lIds) {
                bResV = (BeanItem<TVentasVista>) tablaMasasOrigen.getItem(id);
                resV = bResV.getBean();
                totalCompras += resV.getKgsNetos();
                totalVentas += resV.getKgsNetosFin();
            }

            if (!totalVentas.equals(Double.valueOf(0))) {
                stock = totalCompras / totalVentas;
            }

            // Ventas totales

            vTotales.setId("1");
            vTotales.setVariedad("Totales ");
            vTotales.setOrigen(" ");
            vTotales.setKgs(totalCompras);
            vTotales.setKgsFin(totalVentas);
            vTotales.setKgsEnvase(df.format(stock));

            // Añadimos los registros de totales.

            bcMasasOrigenTotales.removeAllItems();
            bcMasasOrigenTotales.addBean(vTotales);
        } else if (tablaMasasOrigenCalidad.isVisible()) {
            // Va por calidad
            Double totalComprasB = Double.valueOf(0);
            Double totalVentasB = Double.valueOf(0);
            Double stockB = Double.valueOf(0);
            Double totalComprasC = Double.valueOf(0);
            Double totalVentasC = Double.valueOf(0);
            Double stockC = Double.valueOf(0);

            // Calculamos los conceptos de compras
            List<String> lIds = bcMasasOrigen.getItemIds();

            BeanItem<TVentasVista> bResV = null;
            TVentasVista resV = null;

            for (String id : lIds) {
                bResV = (BeanItem<TVentasVista>) tablaMasasOrigenCalidad.getItem(id);
                resV = bResV.getBean();
                if (resV.getCalidadCompra().equals("BIO")) {
                    totalComprasB += resV.getKgs();
                    totalVentasB += resV.getKgsFin();
                } else if (resV.getCalidadCompra().equals("CONVENCIONAL")) {
                    totalComprasC += resV.getKgs();
                    totalVentasC += resV.getKgsFin();
                }
            }

            if (!totalVentasB.equals(Double.valueOf(0))) {
                stockB = totalComprasB / totalVentasB;
            }
            if (!totalVentasC.equals(Double.valueOf(0))) {
                stockC = totalComprasC / totalVentasC;
            }

            // Ventas totales

            vTotales.setId("1");
            vTotales.setVariedad("Totales ");
            vTotales.setOrigen(" ");
            vTotales.setLote(df.format(totalComprasB));
            vTotales.setLoteFin(df.format(totalComprasC));
            vTotales.setAlbaran(df.format(totalVentasB));
            vTotales.setAlbaranFin(df.format(totalVentasC));
            vTotales.setCalibre(df.format(stockB));
            vTotales.setCalibreFin(df.format(stockC));

            // Añadimos los registros de totales.

            bcMasasOrigenTotalesCalidad.removeAllItems();
            bcMasasOrigenTotalesCalidad.addBean(vTotales);

        } else {
            // Va por GGN
            Double totalComprasB = Double.valueOf(0);
            Double totalComprasBImportado = Double.valueOf(0);
            Double totalComprasBNacional = Double.valueOf(0);
            Double totalVentasB = Double.valueOf(0);
            Double totalVentasBImportado = Double.valueOf(0);
            Double totalVentasBNacional = Double.valueOf(0);
            Double stockB = Double.valueOf(0);
            Double stockBImportado = Double.valueOf(0);
            Double stockBNacional = Double.valueOf(0);
            Double totalComprasC = Double.valueOf(0);
            Double totalComprasCImportado = Double.valueOf(0);
            Double totalComprasCNacional = Double.valueOf(0);
            Double totalVentasC = Double.valueOf(0);
            Double totalVentasCImportado = Double.valueOf(0);
            Double totalVentasCNacional = Double.valueOf(0);
            Double stockC = Double.valueOf(0);
            Double stockCImportado = Double.valueOf(0);
            Double stockCNacional = Double.valueOf(0);

            // Calculamos los conceptos de compras
            List<String> lIds = bcMasasOrigen.getItemIds();

            BeanItem<TVentasVista> bResV = null;
            TVentasVista resV = null;

            for (String id : lIds) {
                bResV = (BeanItem<TVentasVista>) tablaMasasGgn.getItem(id);
                resV = bResV.getBean();
                if (resV.getCalidadCompra().equals("BIO")) {
                    if (resV.getAlbaran().equals("NO GGN")) {
                        totalComprasB += resV.getKgs();
                        totalVentasB += resV.getKgsFin();
                    } else if (resV.getAlbaran().equals("GGN NACIONAL")) {
                        totalComprasBNacional += resV.getKgs();
                        totalVentasBNacional += resV.getKgsFin();
                    } else {
                        totalComprasBImportado += resV.getKgs();
                        totalVentasBImportado += resV.getKgsFin();
                    }
                } else if (resV.getCalidadCompra().equals("CONVENCIONAL")) {
                    if (resV.getAlbaran().equals("NO GGN")) {
                        totalComprasC += resV.getKgs();
                        totalVentasC += resV.getKgsFin();
                    } else if (resV.getAlbaran().equals("GGN NACIONAL")) {
                        totalComprasCNacional += resV.getKgs();
                        totalVentasCNacional += resV.getKgsFin();
                    } else {
                        totalComprasCImportado += resV.getKgs();
                        totalVentasCImportado += resV.getKgsFin();
                    }

                }
            }

            if (!totalVentasB.equals(Double.valueOf(0))) {
                stockB = totalComprasB / totalVentasB;
            }

            if (!totalVentasBImportado.equals(Double.valueOf(0))) {
                stockBImportado = totalComprasBImportado / totalVentasBImportado;
            }
            if (!totalVentasBNacional.equals(Double.valueOf(0))) {
                stockBNacional = totalComprasBNacional / totalVentasBNacional;
            }
            if (!totalVentasC.equals(Double.valueOf(0))) {
                stockC = totalComprasC / totalVentasC;
            }
            if (!totalVentasCImportado.equals(Double.valueOf(0))) {
                stockCImportado = totalComprasCImportado / totalVentasCImportado;
            }
            if (!totalVentasCNacional.equals(Double.valueOf(0))) {
                stockCNacional = totalComprasCNacional / totalVentasCNacional;
            }

            // Ventas totales

            vTotales.setId("1");
            vTotales.setVariedad("Totales ");
            vTotales.setOrigen(" ");
            // NO GGN
            vTotales.setLote(df.format(totalComprasB));
            vTotales.setLoteFin(df.format(totalComprasC));
            vTotales.setAlbaran(df.format(totalVentasB));
            vTotales.setAlbaranFin(df.format(totalVentasC));
            vTotales.setCalibre(df.format(stockB));
            vTotales.setCalibreFin(df.format(stockC));
            // GGN IMPORTADO            
            vTotales.setCalidadCompra(df.format(totalComprasBImportado));
            vTotales.setCalidadCompraFin(df.format(totalComprasCImportado));
            vTotales.setCalidadVenta(df.format(totalVentasBImportado));
            vTotales.setCalidadVentaFin(df.format(totalVentasCImportado));
            vTotales.setCliente(df.format(stockBImportado));
            vTotales.setClienteFin(df.format(stockCImportado));
            // GGN NACIONAL
            vTotales.setConfeccion(df.format(totalComprasBNacional));
            vTotales.setConfeccionFin(df.format(totalComprasCNacional));
            vTotales.setEnvase(df.format(totalVentasBNacional));
            vTotales.setEnvaseFin(df.format(totalVentasCNacional));
            vTotales.setFamilia(df.format(stockBNacional));
            vTotales.setFamiliaFin(df.format(stockCNacional));

            // Añadimos los registros de totales.

            bcMasasGgnTotalesCalidad.removeAllItems();
            bcMasasGgnTotalesCalidad.addBean(vTotales);
        }

    }

    /**
     * Método para cambiar el rango de fechas para mostrar los datos de resultados.
     */
    private void cambiarFechas() {
        try {
            Date f1 = fechaDesdeBalance.getValue();
            Date f2 = fechaHastaBalance.getValue();

            // Compras
            List<TVentasVista> lResultados = Utils.generarListaGenerica();

            if (f1 == null && f2 == null) {
                bcMasasOrigen.removeAllItems();

                if (fTipoOrigen.getValue().equals("Nacional/No nacional")) {
                    mMasas = contrVista.obtenerBalancePorOrigen(user, time);
                    tablaMasasOrigenCalidad.setVisible(false);
                    tablaMasasCalidadTotales.setVisible(false);
                    tablaMasasGgnTotales.setVisible(false);
                    tablaMasasOrigen.setVisible(true);
                    tablaMasasOrigenTotales.setVisible(true);
                    tablaMasasGgn.setVisible(false);
                    tablaMasasProducto.setVisible(false);
                    generarBalanceButton.setVisible(false);
                } else if (fTipoOrigen.getValue().equals("Por paises")) {
                    mMasas = contrVista.obtenerBalancePorPais(user, time);
                    tablaMasasOrigenCalidad.setVisible(false);
                    tablaMasasCalidadTotales.setVisible(false);
                    tablaMasasOrigen.setVisible(true);
                    tablaMasasOrigenTotales.setVisible(true);
                    tablaMasasGgnTotales.setVisible(false);
                    tablaMasasGgn.setVisible(false);
                    tablaMasasProducto.setVisible(false);
                    generarBalanceButton.setVisible(false);
                } else if (fTipoOrigen.getValue().equals("Por calidad")) {
                    mMasas = contrVista.obtenerBalancePorCalidad(user, time);
                    tablaMasasOrigenCalidad.setVisible(true);
                    tablaMasasCalidadTotales.setVisible(true);
                    tablaMasasOrigen.setVisible(false);
                    tablaMasasOrigenTotales.setVisible(false);
                    tablaMasasGgnTotales.setVisible(false);
                    tablaMasasGgn.setVisible(false);
                    tablaMasasProducto.setVisible(false);
                    generarBalanceButton.setVisible(false);
                } else if (fTipoOrigen.getValue().equals("Por producto")) {
                    generarBalanceButton.setVisible(true);
                    return;
                } else {
                    mMasas = contrVista.obtenerBalancePorGgn(user, time);
                    tablaMasasOrigenCalidad.setVisible(false);
                    tablaMasasCalidadTotales.setVisible(false);
                    tablaMasasOrigen.setVisible(false);
                    tablaMasasOrigenTotales.setVisible(false);
                    tablaMasasGgnTotales.setVisible(false);
                    tablaMasasGgn.setVisible(true);
                    tablaMasasProducto.setVisible(false);
                    generarBalanceButton.setVisible(false);
                }
                List<Date> lDates = Utils.generarListaGenerica();
                List<TVentasVista> lAux = Utils.generarListaGenerica();
                lDates.addAll(mMasas.keySet());

                for (Date key : lDates) {
                    lAux = (mMasas.get(key));
                    for (TVentasVista res : lAux) {
                        lResultados.add(res);
                    }
                }
            } else {
                bcMasasOrigen.removeAllItems();
                if (fTipoOrigen.getValue().equals("Nacional/No nacional")) {
                    mMasas = contrVista.obtenerBalancePorOrigen(user, time);
                    tablaMasasOrigenCalidad.setVisible(false);
                    tablaMasasCalidadTotales.setVisible(false);
                    tablaMasasGgnTotales.setVisible(false);
                    tablaMasasOrigen.setVisible(true);
                    tablaMasasOrigenTotales.setVisible(true);
                    tablaMasasProducto.setVisible(false);
                    generarBalanceButton.setVisible(false);
                } else if (fTipoOrigen.getValue().equals("Por paises")) {
                    mMasas = contrVista.obtenerBalancePorPais(user, time);
                    tablaMasasOrigenCalidad.setVisible(false);
                    tablaMasasCalidadTotales.setVisible(false);
                    tablaMasasOrigen.setVisible(true);
                    tablaMasasOrigenTotales.setVisible(true);
                    tablaMasasGgnTotales.setVisible(false);
                    tablaMasasProducto.setVisible(false);
                    generarBalanceButton.setVisible(false);
                } else if (fTipoOrigen.getValue().equals("Por calidad")) {
                    mMasas = contrVista.obtenerBalancePorCalidad(user, time);
                    tablaMasasOrigenCalidad.setVisible(true);
                    tablaMasasCalidadTotales.setVisible(true);
                    tablaMasasOrigen.setVisible(false);
                    tablaMasasOrigenTotales.setVisible(false);
                    tablaMasasGgnTotales.setVisible(false);
                    tablaMasasProducto.setVisible(false);
                } else if (fTipoOrigen.getValue().equals("Por producto")) {
                    generarBalanceButton.setVisible(true);
                    return;
                } else {
                    mMasas = contrVista.obtenerBalancePorGgn(user, time);
                    tablaMasasOrigenCalidad.setVisible(false);
                    tablaMasasCalidadTotales.setVisible(false);
                    tablaMasasOrigen.setVisible(false);
                    tablaMasasOrigenTotales.setVisible(false);
                    tablaMasasGgnTotales.setVisible(true);
                    tablaMasasGgn.setVisible(true);
                    tablaMasasProducto.setVisible(false);
                    generarBalanceButton.setVisible(false);
                }

                SortedMap<Date, List<TVentasVista>> mAux = null;
                if (f1 != null && f2 == null) {
                    // El true quiere decir que se incluye también el valor de f1
                    mAux = mMasas.tailMap(f1, true);
                } else if (f1 == null) {
                    mAux = mMasas.headMap(f2, true);
                } else {
                    mAux = mMasas.subMap(f1, true, f2, true);
                }

                List<Date> lFechas = Utils.generarListaGenerica();
                lFechas.addAll(mAux.keySet());
                List<TVentasVista> lAux = null;
                for (Date key : lFechas) {
                    lAux = mAux.get(key);
                    for (TVentasVista record : lAux) {
                        lResultados.add(record);
                    }
                }
            }

            if (tablaMasasProducto.isVisible()) {
                lResultados.sort(new Comparator<TVentasVista>() {

                    @Override
                    public int compare(TVentasVista m1, TVentasVista m2) {
                        if (m1.getFamilia() == m2.getFamilia()) {
                            return 0;
                        }
                        return m1.getFamilia().compareTo(m2.getFamilia());
                    }

                });
            } else {
                lResultados.sort(new Comparator<TVentasVista>() {

                    @Override
                    public int compare(TVentasVista m1, TVentasVista m2) {
                        if (m1.getVariedad() == m2.getVariedad()) {
                            return 0;
                        }
                        return m1.getVariedad().compareTo(m2.getVariedad());
                    }

                });
            }
            bcMasasOrigen.removeAllItems();
            bcMasasOrigen.addAll(lResultados);

            calcularTotales();

        } catch (GenasoftException tbe) {
            log.error("La sesión es inválida, se ha iniciado sesión en otro dispositivo.");
            // Si no se encuentran permisos con el rol especificado, informamos al empleado y cerramos sesión.
            Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
            getSession().setAttribute("userInf", null);
            getSession().setAttribute("fechaInf", null);
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
                if (value.isEmpty() && !regex.isEmpty()) {
                    return false;
                }
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
     * Método que nos aplica el filtro correspondiente.
     */
    private void aplicarFiltroCalidades() {
        bcMasasOrigen.removeAllContainerFilters();

        if (!fTipoOrigen.getValue().equals("Por producto")) {
            if (fCalidad.isEnabled() && fCalidad.getValue() != null && !fCalidad.getValue().equals("Todas")) {
                filter = new FiltroContainer("calidadCompra", ((String) fCalidad.getValue()).toLowerCase(), status);
                bcMasasOrigen.addContainerFilter(filter);
            }
        } else {
            if (!fCalidad.getValue().equals("Todos") && !fCalidad.getValue().equals("DESGLOSADO")) {
                String nombreRol = ((String) fCalidad.getValue()).toLowerCase();
                if (nombreRol.contains("(")) {
                    nombreRol = nombreRol.replaceAll("\\(", " ");
                }
                if (nombreRol.contains(")")) {
                    nombreRol = nombreRol.replaceAll("\\)", " ");
                }
                // Set new filter for the "Name" column
                filter = new FiltroContainer("calidadCompra", nombreRol, status);
                bcMasasOrigen.addContainerFilter(filter);
            }
        }

        if (!fTipoOrigen.getValue().equals("Por producto")) {
            if (fGGn.isEnabled() && fGGn.getValue() != null && !fGGn.getValue().equals("Todos")) {
                filter = new FiltroContainer("albaran", ((String) fGGn.getValue()).toLowerCase(), status);
                bcMasasOrigen.addContainerFilter(filter);
            }
        }

        if (!fArticulos.getValue().equals("Todos") && !fArticulos.getValue().equals("DESGLOSADO")) {
            String nombreRol = ((String) fArticulos.getValue()).toLowerCase();
            if (nombreRol.contains("(")) {
                nombreRol = nombreRol.replaceAll("\\(", " ");
            }
            if (nombreRol.contains(")")) {
                nombreRol = nombreRol.replaceAll("\\)", " ");
            }
            // Set new filter for the "Name" column
            filter = new FiltroContainer("familia", (String) ".*(" + nombreRol + ").*", status);
            bcMasasOrigen.addContainerFilter(filter);
        }

        if (!fVariedades.getValue().equals("Todas") && !fVariedades.getValue().equals("DESGLOSADO")) {
            String nombreRol = ((String) fVariedades.getValue()).toLowerCase();
            if (nombreRol.contains("(")) {
                nombreRol = nombreRol.replaceAll("\\(", " ");
            }
            if (nombreRol.contains(")")) {
                nombreRol = nombreRol.replaceAll("\\)", " ");
            }
            // Set new filter for the "Name" column
            filter = new FiltroContainer("variedad", (String) ".*(" + nombreRol + ").*", status);
            bcMasasOrigen.addContainerFilter(filter);
        }

        if (fTipoOrigen.getValue().equals("Por producto")) {
            if (!fOrigen.getValue().equals("Todos") && !fOrigen.getValue().equals("DESGLOSADO")) {
                String nombreRol = ((String) fOrigen.getValue()).toLowerCase();
                if (nombreRol.contains("(")) {
                    nombreRol = nombreRol.replaceAll("\\(", " ");
                }
                if (nombreRol.contains(")")) {
                    nombreRol = nombreRol.replaceAll("\\)", " ");
                }
                // Set new filter for the "Name" column
                filter = new FiltroContainer("origen", nombreRol, status);
                bcMasasOrigen.addContainerFilter(filter);
            }
        }

        calcularTotales();
    }

    @SuppressWarnings("unchecked")
    private void guardarRegistrosTablasExportacion() throws GenasoftException {

        List<TVentasVista> lVentas = Utils.generarListaGenerica();
        List<String> lFiltros = Utils.generarListaGenerica();

        // POS 1 tipo de informe
        lFiltros.add((String) fTipoOrigen.getValue());
        // POS 2 PRODUCTO
        lFiltros.add((String) fArticulos.getValue());
        // POS 3 CALIDAD
        if (fCalidad.isEnabled()) {
            lFiltros.add((String) fCalidad.getValue());
        } else {
            lFiltros.add("");
        }
        // POS 4 GGN1
        if (fGGn.isEnabled()) {
            lFiltros.add((String) fGGn.getValue());
        } else {
            lFiltros.add("");
        }
        // POS 5 FECHA DESDE
        if (fechaDesdeBalance.getValue() != null) {
            lFiltros.add(new SimpleDateFormat("dd/MM/yyyy").format(fechaDesdeBalance.getValue()));
        } else {
            lFiltros.add("");
        }
        // POS 6 FECHA HASTA
        if (fechaHastaBalance.getValue() != null) {
            lFiltros.add(new SimpleDateFormat("dd/MM/yyyy").format(fechaHastaBalance.getValue()));
        } else {
            lFiltros.add("");
        }

        List<TVentasVista> lTotales = Utils.generarListaGenerica();

        // Se está mostrando las masas por calidad.
        if (tablaMasasOrigenCalidad.isVisible()) {
            List<String> lIds = Utils.generarListaGenerica();
            lIds.addAll(bcMasasOrigen.getItemIds());

            BeanItem<TVentasVista> bRes = null;
            TVentasVista res = null;

            for (String id : lIds) {
                bRes = (BeanItem<TVentasVista>) tablaMasasOrigenCalidad.getItem(id);
                res = bRes.getBean();
                lVentas.add(res);
            }
            // Los totales
            lIds = Utils.generarListaGenerica();
            lIds.addAll(bcMasasOrigenTotalesCalidad.getItemIds());

            bRes = null;
            res = null;

            for (String id : lIds) {
                bRes = (BeanItem<TVentasVista>) tablaMasasCalidadTotales.getItem(id);
                res = bRes.getBean();
                lTotales.add(res);
            }

        } else if (tablaMasasGgn.isVisible()) {
            List<String> lIds = Utils.generarListaGenerica();
            lIds.addAll(bcMasasOrigen.getItemIds());

            BeanItem<TVentasVista> bRes = null;
            TVentasVista res = null;

            for (String id : lIds) {
                bRes = (BeanItem<TVentasVista>) tablaMasasGgn.getItem(id);
                res = bRes.getBean();
                lVentas.add(res);
            }
        } else if (tablaMasasProducto.isVisible()) {
            List<String> lIds = Utils.generarListaGenerica();
            lIds.addAll(bcMasasOrigen.getItemIds());

            BeanItem<TVentasVista> bRes = null;
            TVentasVista res = null;

            for (String id : lIds) {
                bRes = (BeanItem<TVentasVista>) tablaMasasProducto.getItem(id);
                res = bRes.getBean();
                lVentas.add(res);
            }

            // Los totales
            lIds = Utils.generarListaGenerica();
            lIds.addAll(bcMasasOrigenTotales.getItemIds());

            bRes = null;
            res = null;

            for (String id : lIds) {
                bRes = (BeanItem<TVentasVista>) tablaMasasOrigenTotales.getItem(id);
                res = bRes.getBean();
                lTotales.add(res);
            }

        } else if (tablaMasasOrigen.isVisible()) {
            List<String> lIds = Utils.generarListaGenerica();
            lIds.addAll(bcMasasOrigen.getItemIds());

            BeanItem<TVentasVista> bRes = null;
            TVentasVista res = null;

            for (String id : lIds) {
                bRes = (BeanItem<TVentasVista>) tablaMasasOrigen.getItem(id);
                res = bRes.getBean();
                lVentas.add(res);
            }
            // Los totales
            lIds = Utils.generarListaGenerica();
            lIds.addAll(bcMasasOrigenTotales.getItemIds());

            bRes = null;
            res = null;

            for (String id : lIds) {
                bRes = (BeanItem<TVentasVista>) tablaMasasOrigenTotales.getItem(id);
                res = bRes.getBean();
                lTotales.add(res);
            }
        }

        // Guardamos la lista de ventas que se está visualizando para construir posteriormente el fichero Excel/PDF
        contrVista.guardarBalanceMasas(lVentas, lFiltros, user, time);
        // Guardamos los totales
        contrVista.guardarTotalesBalanceMasas(lTotales, user, time);

    }

}
