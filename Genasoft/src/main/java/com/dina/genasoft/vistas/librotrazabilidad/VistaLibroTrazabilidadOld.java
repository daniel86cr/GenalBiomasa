/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.librotrazabilidad;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.PatternSyntaxException;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TColumnasTablasEmpleado;
import com.dina.genasoft.db.entity.TComprasVista;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPais;
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
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
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
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaLibroTrazabilidadOld.NAME)
public class VistaLibroTrazabilidadOld extends CustomComponent implements View ,Button.ClickListener {
    /** El nombre de la vista.*/
    public static final String                   NAME                = "vLibroTrazabilidadOld";
    /** Necesario para mostrar las compras. */
    private BeanContainer<String, TComprasVista> bcCompras;
    /** Necesario para mostrar las compras. */
    private BeanContainer<String, TComprasVista> bcNuevaCompra;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TVentasVista>  bcVentas;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TVentasVista>  bcNuevaVenta;
    /** Necesario para mostrar las compras. */
    private BeanContainer<String, TComprasVista> bcComprasTotales;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TVentasVista>  bcVentasTotales;
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas                    contrVista;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                               excelButton;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                               quitarFiltrosButton;
    /** El boton para crear una empresa.*/
    private Button                               guardarColumnasComprasButton;
    /** El boton para crear una empresa.*/
    private Button                               guardarColumnasVentasButton;
    // Elementos para realizar busquedas
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                               appName;
    /** El ID del cliente seleccionado.*/
    private String                               idSeleccionadoCompras;
    /** El ID del cliente seleccionado.*/
    private String                               idSeleccionadoVentas;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger        log                 = org.slf4j.LoggerFactory.getLogger(VistaLibroTrazabilidadOld.class);
    /** El usuario que está logado. */
    private Integer                              user                = null;
    /** La fecha en que se inició sesión. */
    private Long                                 time                = null;
    /** El filtro que se le aplica al container. */
    private FiltroContainer                      filter;
    /** No tengo ni puta idea para que sirve. */
    private final Label                          status              = new Label("");
    /** Tabla para mostrar las compras del sistema. */
    private Table                                tablaCompras        = null;
    /** Etiqueta para mostrar el texto de las tablas de compras. */
    private Label                                textoTablaCompras2;
    /** Tabla para mostrar las ventas del sistema. */
    private Table                                tablaVentas         = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                                tablaComprasTotales = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                                tablaVentasTotales  = null;
    /** Para indicar que se está filtrando por el campo nombre. */
    public TreeMap<Date, List<TComprasVista>>    mCompras            = null;
    /** Arbol con los resultados. */
    private TreeMap<Date, List<TVentasVista>>    mVentas             = null;
    /** Etiqueta para mostrar el texto de las tablas de compras. */
    private Label                                textoTablaCompras;
    /** Etiqueta para mostrar el texto de las tablas de ventas. */
    private Label                                textoTablaVentas;
    /** Etiqueta para mostrar el texto de las tablas de ventas. */
    private Label                                textoTablaVentas2;
    private VerticalLayout                       tCompras;
    private VerticalLayout                       tVentas;
    /** Diccionario con las columnas que se van a mostrar en la tabla. */
    private Map<String, String>                  mColumnasGuardadoCompras;
    /** Diccionario con las columnas que se van a mostrar en la tabla. */
    private Map<String, String>                  mColumnasGuardadoVentas;
    /** Diccionario con las columnas que se van a mostrar en la tabla de compras. */
    private Map<String, String>                  mColumnasCompras;
    /** Diccionario con las columnas que se van a mostrar en la tabla de ventas. */
    private Map<String, String>                  mColumnasVentas;
    /** Diccionario con las columnas que se van a mostrar en la tabla de compras. */
    private Map<String, String>                  mColumnasIdsCompras;
    /** Diccionario con las columnas que se van a mostrar en la tabla de ventas. */
    private Map<String, String>                  mColumnasIdsVentas;
    /** ComboBox para ocultar columnas en la tabla de ventas. */
    private ComboBox                             cbMostrarFiltro;
    /** ComboBox para ocultar columnas en la tabla de ventas. */
    private ComboBox                             cbMostrarDatosGlobales;
    /**********                                 FILTROS                                 **********/
    /** Para filtrar por nombre descriptivo en importación de compra.  */
    private ComboBox                             cbNombresCompras;
    /** Para filtrar por nombre descriptivo en importación de venta.  */
    private ComboBox                             cbNombresVentas;
    /** Para filtrar por familia.  */
    private ComboBox                             cbFamiliasCompras;
    /** Para filtrar por familia.  */
    private ComboBox                             cbFamiliasVentas;
    /** Para filtrar por país.  */
    private ComboBox                             cbPaisesCompras;
    /** Para filtrar por país.  */
    private ComboBox                             cbPaisesVentas;
    /** Para filtrar por artículo.  */
    private ComboBox                             cbArticulosCompras;
    /** Para filtrar por artículo.  */
    private ComboBox                             cbArticulosVentas;
    /** Para filtrar por el tipo de proveedor. */
    private ComboBox                             cbGgnCompra;
    /** Para filtrar por clientes.  */
    private ComboBox                             cbClientes;
    /** Para filtrar por proveedores en compras.  */
    private ComboBox                             cbProveedoresCompras;
    /** Para filtrar por proveedores en ventas.  */
    private ComboBox                             cbProveedoreVentas;
    /** Para filtrar por albarán de compra.  */
    private ComboBox                             cbAlbaranCompra;
    /** Para filtrar por albarán de venta.  */
    private ComboBox                             cbAlbaranVenta;
    /** Para filtrar por albarán de venta.  */
    private ComboBox                             cbPedidoVenta;
    /** Para filtrar por calidad de compra.  */
    private ComboBox                             cbCalidadCompra;
    /** Para filtrar por calidad de venta.  */
    private ComboBox                             cbCalidadVenta;
    /** Para filtrar por calidad de compra.  */
    private ComboBox                             cbLoteCompra;
    /** Para filtrar por calidad de venta.  */
    private ComboBox                             cbLoteVenta;
    /** Para filtrar por calidad de compra.  */
    private ComboBox                             cbPartidaCompra;
    /** La fecha desde para los informes. */
    private DateField                            fechaDesdeCompra;
    /** La fecha hasta para los informes. */
    private DateField                            fechaHastaCompra;
    /** La fecha desde para los informes. */
    private DateField                            fechaDesdeVenta;
    /** La fecha hasta para los informes. */
    private DateField                            fechaHastaVenta;
    private HorizontalLayout                     cabeceraPantalla;
    private VerticalLayout                       filtro;
    /********* LISTAS PARA FILTROS *********/
    /** Lista con las nombres de importaciones en las compras. */
    private List<String>                         lNombresCompras;
    /** Lista con las nombres de importaciones en las ventas. */
    private List<String>                         lNombresVentas;
    /** Lista con las familias identificadas en las compras. */
    private List<String>                         lFamiliasCompras;
    /** Lista con los paises identificadas en las compras. */
    private List<String>                         lPaisesCompras;
    /** Lista con los paises identificadas en las ventas. */
    private List<String>                         lPaisesVentas;
    /** Lista con los artículos identificadas en las compras. */
    private List<String>                         lArticulosCompras;
    /** Lista con los artículos identificadas en las ventas. */
    private List<String>                         lArticulosVentas;
    /** Lista con los clientes identificadas en las ventas. */
    private List<String>                         lClientes;
    /** Lista con los proveedores identificadas en las compras. */
    private List<String>                         lProveedoresCompras;
    /** Lista con los proveedores identificadas en las ventas. */
    private List<String>                         lProveedoresVentas;
    /** Lista con los albaranes identificadas en las compras. */
    private List<String>                         lAlbaranesCompras;
    /** Lista con los albaranes identificadas en las ventas. */
    private List<String>                         lAlbaranesVentas;
    /** Lista con los albaranes identificadas en las ventas. */
    private List<String>                         lPedidosVentas;
    /** Lista con las calidades identificadas en las compras. */
    private List<String>                         lCalidadCompras;
    /** Lista con las calidades identificadas en las ventas. */
    private List<String>                         lCalidadVentas;
    /** Lista con los lotes identificadas en las compras. */
    private List<String>                         lLotesCompras;
    /** Lista con los lotes identificadas en las ventas. */
    private List<String>                         lLotesVentas;
    /** Lista con las partidas identificadas en las compras. */
    private List<String>                         lPartidasCompras;
    /********* Listas para mostrar los datos por lo que se está filtrando *********/
    /** Lista familias por los que está filtrando. */
    private ListSelect                           lsFamiliasCompras;
    /** Lista familias por los que está filtrando. */
    private ListSelect                           lsFamiliasVentas;
    /** Lista paises compra por los que está filtrando. */
    private ListSelect                           lsPaisesCompra;
    /** Lista paises venta por los que está filtrando. */
    private ListSelect                           lsPaisesVentas;
    /** Lista artículos por los que está filtrando. */
    private ListSelect                           lsArticulosCompras;
    /** Lista artículos por los que está filtrando. */
    private ListSelect                           lsArticulosVentas;
    /** Lista de clientes por los que está filtrando. */
    private ListSelect                           lsClientes;
    /** Lista de Nº de albaranes por los que está filtrando. */
    private ListSelect                           lsAlbaranesCompras;
    /** Lista de Nº de albaranes por los que está filtrando. */
    private ListSelect                           lsAlbaranesVentas;
    /** Lista de Nº de albaranes por los que está filtrando. */
    private ListSelect                           lsPedidosVentas;
    /** Lista de proveedores por los que está filtrando. */
    private ListSelect                           lsProveedoresCompras;
    /** Lista de proveedores por los que está filtrando. */
    private ListSelect                           lsProveedoresVentas;
    /** Lista de lotes por los que está filtrando. */
    private ListSelect                           lsLoteCompras;
    /** Lista de lotes por los que está filtrando. */
    private ListSelect                           lsLoteVentas;
    /** Lista de lotes por los que está filtrando. */
    private ListSelect                           lsPartidasCompra;
    /** Lista de calidades por los que está filtrando. */
    private ListSelect                           lsCalidadCompra;
    /** Lista de calidades por los que está filtrando. */
    private ListSelect                           lsCalidadVentas;
    private Boolean                              primerFiltro;
    /** Variables que nos indica la posición del diccionario de filtros donde se encuentra el valor a filtrar.*/
    private final Integer                        ALBARAN_COMPRA      = 1;
    private final Integer                        PARTIDA_COMPRA      = 2;
    private final Integer                        PEDIDO_VENTA        = 3;
    private final Integer                        ALBARAN_VENTA       = 4;
    private final Integer                        ARTICULO_COMPRA     = 5;
    private final Integer                        ARTICULO_VENTA      = 6;
    private final Integer                        FAMILIA_COMPRA      = 7;
    private final Integer                        FAMILIA_VENTA       = 8;
    private final Integer                        ORIGEN_COMPRA       = 9;
    private final Integer                        ORIGEN_VENTA        = 10;
    private final Integer                        PROVEEDOR_COMPRA    = 11;
    private final Integer                        PROVEEDOR_VENTA     = 12;
    private final Integer                        CLIENTE             = 13;
    private final Integer                        LOTE_COMPRA         = 14;
    private final Integer                        LOTE_VENTA          = 15;
    private final Integer                        FECHA_DESDE_COMPRA  = 16;
    private final Integer                        FECHA_HASTA_COMPRA  = 17;
    private final Integer                        FECHA_DESDE_VENTA   = 18;
    private final Integer                        FECHA_HASTA_VENTA   = 19;
    private final Integer                        GGN                 = 20;
    private final Integer                        CALIDAD_COMPRA      = 21;
    private final Integer                        CALIDAD_VENTA       = 22;
    private TPermisos                            permisos;
    private TEmpleados                           empleado;
    private HorizontalLayout                     totales;
    private Label                                filtrosAplicados;

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
            filtrosAplicados = new Label("");
            filtrosAplicados.setStyleName("tituloTamano14");

            List<TComprasVista> lCompras = Utils.generarListaGenerica();
            List<TVentasVista> lVentas = Utils.generarListaGenerica();
            try {
                idSeleccionadoCompras = null;
                primerFiltro = true;
                bcCompras = new BeanContainer<>(TComprasVista.class);
                bcCompras.setBeanIdProperty("id");

                bcNuevaCompra = new BeanContainer<>(TComprasVista.class);
                bcNuevaCompra.setBeanIdProperty("id");

                bcVentas = new BeanContainer<>(TVentasVista.class);
                bcVentas.setBeanIdProperty("id");

                bcNuevaVenta = new BeanContainer<>(TVentasVista.class);
                bcNuevaVenta.setBeanIdProperty("id");

                bcComprasTotales = new BeanContainer<>(TComprasVista.class);
                bcComprasTotales.setBeanIdProperty("id");

                bcVentasTotales = new BeanContainer<>(TVentasVista.class);
                bcVentasTotales.setBeanIdProperty("id");

                Label texto = new Label("Libro");
                texto.setStyleName("tituloTamano16");
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

                if (!Utils.booleanFromInteger(permisos.getTrazabilidades())) {
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
                HorizontalLayout imgNaturSoft = contrVista.logoGenaSoft(texto);

                viewLayout.addComponent(imgNaturSoft);
                viewLayout.setComponentAlignment(imgNaturSoft, Alignment.TOP_RIGHT);
                //viewLayout.addComponent(titulo);
                //viewLayout.setComponentAlignment(titulo, Alignment.TOP_CENTER);

                crearTablaCompras(permisos);
                crearTablaComprasTotales();
                crearTablaVentas(permisos);
                crearTablaVentasTotales();

                // Obtenemos los campos y en el orden establecido de la tabla de empleados.
                List<TColumnasTablasEmpleado> lColumnas = contrVista.obtenerCamposPantallaTablaEmpleado(user, NAME, Integer.valueOf(tablaCompras.getId()), user, time);

                // Nutrimos el diccionario para mostrar/ocultar las columnas
                nutrirDiccionarioCabeceraTablaCompras();

                // Nutrimos el diccionario para mostrar/ocultar las columnas
                nutrirDiccionarioCabeceraTablaVentas();

                mostrarColumnasTablaCompras(lColumnas);

                lColumnas = contrVista.obtenerCamposPantallaTablaEmpleado(user, NAME, Integer.valueOf(tablaVentas.getId()), user, time);

                mostrarColumnasTablaVentas(lColumnas);

                mCompras = contrVista.obtenerComprasGlobales(user, time);
                mVentas = contrVista.obtenerVentasGlobales(user, time);

                List<Date> lFechas = Utils.generarListaGenerica();
                List<TComprasVista> lAux = Utils.generarListaGenerica();
                List<TVentasVista> lAux2 = Utils.generarListaGenerica();

                lFechas.addAll(mCompras.keySet());

                // Cargamos los datos de las compras
                for (Date key : lFechas) {
                    lAux = (mCompras.get(key));
                    for (TComprasVista res : lAux) {
                        lCompras.add(res);
                    }
                }

                // Cargamos los datos de las ventas
                lFechas = Utils.generarListaGenerica();
                lFechas.addAll(mVentas.keySet());

                // Cargamos los datos de las ventas
                for (Date key : lFechas) {
                    lAux2 = (mVentas.get(key));
                    for (TVentasVista res : lAux2) {
                        lVentas.add(res);
                    }
                }

                bcCompras.removeAllItems();
                bcCompras.addAll(lCompras);

                bcVentas.removeAllItems();
                bcVentas.addAll(lVentas);

                Label tituloFiltrar = new Label("Filtrar");
                tituloFiltrar.setStyleName("tituloTamano12");

                // Obtenemos los datos para crear la parte filtro.
                lFamiliasCompras = contrVista.obtenerFamiliasCompras(user, time);
                lPaisesCompras = contrVista.obtenerPaisesCompras(user, time);
                lPaisesVentas = contrVista.obtenerPaisesVentas(user, time);
                lArticulosCompras = contrVista.obtenerArticulosCompras(user, time);
                lArticulosVentas = contrVista.obtenerArticulosVentas(user, time);
                lClientes = contrVista.obtenerClientes(user, time);
                lProveedoresCompras = contrVista.obtenerProveedoresCompras(user, time);
                lProveedoresVentas = contrVista.obtenerProveedoresVentas(user, time);
                lAlbaranesCompras = contrVista.obtenerAlbaranesCompras(user, time);
                lAlbaranesVentas = contrVista.obtenerAlbaranesVentas(user, time);
                lLotesCompras = contrVista.obtenerLotesCompras(user, time);
                lLotesVentas = contrVista.obtenerLotesVentas(user, time);
                lPartidasCompras = contrVista.obtenerPartidasCompras(user, time);
                lPedidosVentas = contrVista.obtenerPedidosVentas(user, time);
                lNombresCompras = contrVista.obtenerNombresCompras(user, time);
                lNombresVentas = contrVista.obtenerNombresVentas(user, time);

                lCalidadCompras = Utils.generarListaGenerica();
                lCalidadCompras.add("A");
                lCalidadCompras.add("B");
                lCalidadCompras.add("C");

                lCalidadVentas = Utils.generarListaGenerica();
                lCalidadVentas.add("A");
                lCalidadVentas.add("B");
                lCalidadVentas.add("C");

                // Creamos el componente filtro.
                crearComponenteFiltro();

                // Creamos la botonera.
                //HorizontalLayout botonera = crearBotonera(permisos);                
                viewLayout.addComponent(cabeceraPantalla);
                viewLayout.addComponent(filtro);

                HorizontalLayout botones = new HorizontalLayout();
                botones.setSpacing(true);
                botones.addComponent(guardarColumnasComprasButton);
                botones.addComponent(guardarColumnasVentasButton);

                viewLayout.addComponent(botones);

                textoTablaCompras = new Label("Compras");
                textoTablaCompras.setStyleName("tituloTamano18");

                textoTablaCompras2 = new Label("Compras totales");
                textoTablaCompras2.setStyleName("tituloTamano14Center");

                textoTablaVentas = new Label("Ventas");
                textoTablaVentas.setStyleName("tituloTamano18");
                textoTablaVentas2 = new Label("Ventas totales");
                textoTablaVentas2.setStyleName("tituloTamano14Center");

                tCompras = new VerticalLayout();
                tCompras.setSpacing(true);
                tCompras.addComponent(textoTablaCompras);
                tCompras.setComponentAlignment(textoTablaCompras, Alignment.TOP_CENTER);
                tCompras.addComponent(tablaComprasTotales);
                tCompras.setComponentAlignment(tablaComprasTotales, Alignment.TOP_CENTER);
                tCompras.addComponent(tablaCompras);

                tVentas = new VerticalLayout();
                tVentas.setSpacing(true);
                tVentas.addComponent(textoTablaVentas);
                tVentas.setComponentAlignment(textoTablaVentas, Alignment.TOP_CENTER);
                tVentas.addComponent(tablaVentasTotales);
                tVentas.setComponentAlignment(tablaVentasTotales, Alignment.TOP_CENTER);
                tVentas.addComponent(tablaVentas);

                VerticalLayout totalesCompras = new VerticalLayout();
                totalesCompras.setSpacing(true);
                totalesCompras.addComponent(textoTablaCompras2);
                totalesCompras.addComponent(tablaComprasTotales);

                VerticalLayout totalesVentas = new VerticalLayout();
                totalesVentas.setSpacing(true);
                totalesVentas.addComponent(textoTablaVentas2);
                totalesVentas.addComponent(tablaVentasTotales);

                totales = new HorizontalLayout();
                totales.setSpacing(true);
                totales.addStyleName("posLayoutFixed");
                totales.addComponent(filtrosAplicados);
                totales.addComponent(totalesCompras);
                totales.addComponent(totalesVentas);
                totales.setVisible(false);

                HorizontalLayout tablas = new HorizontalLayout();

                tablas.addComponent(tCompras);
                //tablaCompras.setWidth(150, Sizeable.Unit.EM);
                tablas.addComponent(tVentas);
                //tablaVentas.setWidth(150, Sizeable.Unit.EM);
                tablas.setSizeUndefined();

                VerticalLayout filtros = new VerticalLayout();
                filtros.setSpacing(true);
                filtros.addComponent(filtrosAplicados);
                filtros.addStyleName("posLayoutFixed");

                HorizontalLayout horBot = new HorizontalLayout();
                horBot.setSpacing(true);
                horBot.addComponent(quitarFiltrosButton);
                horBot.addComponent(excelButton);
                viewLayout.addComponent(tablas);

                Label lbl = new Label(" ");
                lbl.setHeight(15, Sizeable.Unit.EM);

                Label lbl2 = new Label(" ");
                lbl2.setHeight(4, Sizeable.Unit.EM);

                //viewLayout.addComponent(tabTotales);
                viewLayout.addComponent(horBot);
                viewLayout.addComponent(filtros);
                viewLayout.addComponent(lbl2);
                viewLayout.addComponent(totales);
                viewLayout.addComponent(lbl);
                viewLayout.addComponent(tablas);

                // Añadimos el logo del cliente
                viewLayout.addComponent(contrVista.logoCliente());
                setCompositionRoot(viewLayout);
                // Establecemos el porcentaje de ratio para los layouts
                //viewLayout.setExpandRatio(titulo, 0.1f);
                viewLayout.setMargin(true);
                viewLayout.setSpacing(true);

                filter = new FiltroContainer("asdf", "aaaa", status);
                bcVentas.addContainerFilter(filter);
                bcCompras.addContainerFilter(filter);

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
        guardarColumnasComprasButton = new Button("Guardar columnas tabla compras", this);
        guardarColumnasVentasButton = new Button("Guardar columnas tabla ventas", this);
        quitarFiltrosButton = new Button("Quitar todos los filtros", this);
        excelButton = new Button("", this);
        excelButton.setIcon(new ThemeResource("icons/excel-32.ico"));
        excelButton.setStyleName(BaseTheme.BUTTON_LINK);

    }

    @Override
    public void buttonClick(ClickEvent event) {

        if (event.getButton().equals(guardarColumnasComprasButton)) {
            guardarOrdenColumnasCompras();
        } else if (event.getButton().equals(guardarColumnasVentasButton)) {
            guardarOrdenColumnasVentas();
        } else if (event.getButton().equals(excelButton)) {
            if (tablaCompras.isVisible() || tablaVentas.isVisible()) {

                if (tablaCompras.getItemIds().isEmpty() && tablaVentas.getItemIds().isEmpty()) {
                    Notification aviso = new Notification("No se han identificado registros a exportar", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                } else {
                    try {
                        guardarRegistrosTablasExportacion();

                        guardarFiltrosAplicados();

                        Page.getCurrent().open("/exportarTrazabilidadesExcel?idEmpleado=" + user, "_blank");

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
            } else {
                Notification aviso = new Notification("Solo se permite la exportación en modo 'Solo lectura'", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }
        }
    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaCompras(TPermisos permisos) {
        tablaCompras = new TablaGenerica(new Object[] { "nombreDescriptivo", "albaran", "fecha", "partida", "cajas", "kgsBruto", "lote", "pesoNeto", "producto", "proveedor", "origen", "familia", "ggn", "calidad" }, new String[] { "Descripción importación", "Albarán", "Fecha", "Partida", "Nº de cajas", "Peso bruto", "Trazabilidad", "Peso neto", "Producto", "Entidad", "Orígen", "Plantilla de producto", "Global Gap", "Calidad" }, bcCompras);
        tablaCompras.addStyleName("big striped");
        tablaCompras.setEditable(false);
        tablaCompras.setPageLength(40);
        tablaCompras.setId("1");
        tablaCompras.setColumnWidth("albaran", 200);
        tablaCompras.setColumnWidth("fecha", 200);
        tablaCompras.setColumnWidth("partida", 200);
        tablaCompras.setColumnWidth("cajas", 200);
        tablaCompras.setColumnWidth("kgsBruto", 200);
        tablaCompras.setColumnWidth("lote", 200);
        tablaCompras.setColumnWidth("pesoNeto", 200);
        tablaCompras.setColumnWidth("producto", 200);
        tablaCompras.setColumnWidth("proveedor", 200);
        tablaCompras.setColumnWidth("origen", 200);
        tablaCompras.setColumnWidth("familia", 200);
        tablaCompras.setColumnWidth("ggn", 200);
        tablaCompras.setColumnWidth("calidad", 200);
        tablaCompras.setDragMode(TableDragMode.ROW);
        // Para mostrar/ocultar columnas de la tabla
        tablaCompras.setColumnCollapsingAllowed(true);
        // Para cambiar el orden las columnas
        tablaCompras.setColumnReorderingAllowed(true);

        // Establecemos tamaño fijo en columnas específicas.        
        tablaCompras.setSelectable(false);
        tablaCompras.addItemClickListener(new ItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoCompras = (String) event.getItemId();
                // Guardamos el ID del registro para guardar la modificación. Solo si está en modo edición

                if (event.isDoubleClick()) {
                    tablaCompras.getItem(idSeleccionadoCompras);
                    BeanItem<TComprasVista> bRes = (BeanItem<TComprasVista>) tablaCompras.getItem(idSeleccionadoCompras);
                    TComprasVista res = bRes.getBean();
                    if (event.getPropertyId() != null) {
                        if (event.getPropertyId().equals("albaran")) {
                            List<String> lAlbaranes = (List<String>) lsAlbaranesCompras.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lAlbaranes) {
                                if (fam.equals(res.getAlbaranFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsAlbaranesCompras.addItem(res.getAlbaranFin());
                            } else {
                                lsAlbaranesCompras.removeItem(res.getAlbaranFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("fecha")) {

                            fechaDesdeCompra.setValue(res.getFechaFin());

                        } else if (event.getPropertyId().equals("partida")) {
                            List<String> lPartidas = (List<String>) lsPartidasCompra.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lPartidas) {
                                if (fam.equals(res.getPartidaFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsPartidasCompra.addItem(res.getPartidaFin());
                            } else {
                                lsPartidasCompra.removeItem(res.getPartidaFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("lote")) {
                            List<String> lLotes = (List<String>) lsLoteCompras.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lLotes) {
                                if (fam.equals(res.getLoteFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsLoteCompras.addItem(res.getLoteFin());
                            } else {
                                lsLoteCompras.removeItem(res.getLoteFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("producto")) {
                            List<String> lArticulos = (List<String>) lsArticulosCompras.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lArticulos) {
                                if (fam.equals(res.getProductoFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsArticulosCompras.addItem(res.getProductoFin());
                            } else {
                                lsArticulosCompras.removeItem(res.getProductoFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("proveedor")) {
                            List<String> lProveedores = (List<String>) lsProveedoresCompras.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lProveedores) {
                                if (fam.equals(res.getProveedorFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsProveedoresCompras.addItem(res.getProveedorFin());
                            } else {
                                lsProveedoresCompras.removeItem(res.getProveedorFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("origen")) {
                            List<String> lPaises = (List<String>) lsPaisesCompra.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lPaises) {
                                if (fam.equals(res.getOrigenFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsPaisesCompra.addItem(res.getOrigenFin());
                            } else {
                                lsPaisesCompra.removeItem(res.getOrigenFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("familia")) {
                            List<String> lFamilias = (List<String>) lsFamiliasCompras.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lFamilias) {
                                if (fam.equals(res.getFamiliaFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsFamiliasCompras.addItem(res.getFamiliaFin());
                            } else {
                                lsFamiliasCompras.removeItem(res.getFamiliaFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("ggn")) {
                            cbGgnCompra.setValue(res.getGgnFin());
                        } else if (event.getPropertyId().equals("calidad")) {
                            List<String> lCalidades = (List<String>) lsCalidadCompra.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lCalidades) {
                                if (fam.equals(res.getCalidadFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsCalidadCompra.addItem(res.getCalidadFin());
                            } else {
                                lsCalidadCompra.removeItem(res.getCalidadFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        }

                    }
                }
            }
        });

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaComprasTotales() {
        tablaComprasTotales = new TablaGenerica(new Object[] { "pesoNeto", "kgsDisponibles" }, new String[] { "Kilos totales", "Kgs disponibles" }, bcComprasTotales);
        //tablaComprasTotales = new TablaGenerica(new Object[] { "pesoNeto", "kgsDisponibles" }, new String[] { "Kilos totales", "Kgs Diponibles" }, bcComprasTotales);
        tablaComprasTotales.addStyleName("big strong");
        tablaComprasTotales.setPageLength(1);
        // Para mostrar/ocultar columnas de la tabla
        tablaComprasTotales.setColumnCollapsingAllowed(false);
        // Para cambiar el orden las columnas
        tablaComprasTotales.setColumnReorderingAllowed(false);
        tablaComprasTotales.setSelectable(false);
        tablaComprasTotales.setSortEnabled(false);
        tablaComprasTotales.setColumnWidth("pesoNeto", 200);

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaVentas(TPermisos permisos) {
        tablaVentas = new TablaGenerica(new Object[] { "nombreDescriptivo", "pedidoFin", "albaranFin", "calibreFin", "loteFin", "idPaleFin", "numBultosFin", "numBultosPaleFin", "proveedorFin", "clienteFin", "fechaVentaFin", "kgsFin", "kgsNetosFin", "variedadFin", "origenFin", "calidadVentaFin", "confeccionFin", "familiaFin" }, new String[] { "Descripción importación", "Pedido de venta", "Albarán de venta", "Calibre", "Trazabilidad", "ID de palé", "Bultos mov. venta", "Bultos por palé", "Productor", "Entidad", "Fecha salida", "Kilos", "Peso neto teo. venta", "Variedad", "Orígen venta", "Calidad venta", "Confección", "Producto" }, bcVentas);
        tablaVentas.addStyleName("big striped");
        tablaVentas.setEditable(false);
        tablaVentas.setPageLength(40);
        tablaVentas.setId("2");
        tablaVentas.setColumnWidth("pedidoFin", 200);
        tablaVentas.setColumnWidth("albaranFin", 200);
        tablaVentas.setColumnWidth("calibreFin", 200);
        tablaVentas.setColumnWidth("loteFin", 200);
        tablaVentas.setColumnWidth("idPaleFin", 200);
        tablaVentas.setColumnWidth("numBultosFin", 200);
        tablaVentas.setColumnWidth("numBultosPaleFin", 200);
        tablaVentas.setColumnWidth("proveedorFin", 200);
        tablaVentas.setColumnWidth("clienteFin", 200);
        tablaVentas.setColumnWidth("fechaVentaFin", 200);
        tablaVentas.setColumnWidth("kgsFin", 200);
        tablaVentas.setColumnWidth("kgsNetosFin", 200);
        tablaVentas.setColumnWidth("variedadFin", 200);
        tablaVentas.setColumnWidth("origenFin", 200);
        tablaVentas.setColumnWidth("calidadVentaFin", 200);
        tablaVentas.setColumnWidth("confeccionFin", 200);
        tablaVentas.setColumnWidth("familiaFin", 200);
        tablaVentas.setDragMode(TableDragMode.ROW);
        // Para mostrar/ocultar columnas de la tabla
        tablaVentas.setColumnCollapsingAllowed(true);
        // Para cambiar el orden las columnas
        tablaVentas.setColumnReorderingAllowed(true);

        // Establecemos tamaño fijo en columnas específicas.        
        tablaVentas.setMultiSelect(true);
        tablaVentas.addItemClickListener(new ItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoVentas = (String) event.getItemId();
                // Guardamos el ID del registro para guardar la modificación. Solo si está en modo edición
                if (event.isDoubleClick()) {
                    tablaVentas.getItem(idSeleccionadoVentas);
                    BeanItem<TVentasVista> bRes = (BeanItem<TVentasVista>) tablaVentas.getItem(idSeleccionadoVentas);
                    TVentasVista res = bRes.getBean();
                    if (event.getPropertyId() != null) {
                        if (event.getPropertyId().equals("albaranFin")) {
                            List<String> lAlbaranes = (List<String>) lsAlbaranesVentas.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lAlbaranes) {
                                if (fam.equals(res.getAlbaranFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsAlbaranesVentas.addItem(res.getAlbaranFin());
                            } else {
                                lsAlbaranesVentas.removeItem(res.getAlbaranFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("pedidoFin")) {
                            List<String> lAlbaranes = (List<String>) lsPedidosVentas.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lAlbaranes) {
                                if (fam.equals(res.getPedidoFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsPedidosVentas.addItem(res.getPedidoFin());
                            } else {
                                lsPedidosVentas.removeItem(res.getPedidoFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("fechaVentaFin")) {

                            fechaDesdeCompra.setValue(res.getFechaVentaFin());

                        } else if (event.getPropertyId().equals("loteFin")) {
                            List<String> lLotes = (List<String>) lsLoteVentas.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lLotes) {
                                if (fam.equals(res.getLoteFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsLoteVentas.addItem(res.getLoteFin());
                            } else {
                                lsLoteVentas.removeItem(res.getLoteFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("familiaFin")) {
                            List<String> lArticulos = (List<String>) lsArticulosVentas.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lArticulos) {
                                if (fam.equals(res.getFamiliaFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsArticulosVentas.addItem(res.getFamiliaFin());
                            } else {
                                lsArticulosVentas.removeItem(res.getFamiliaFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("proveedorFin")) {
                            List<String> lProveedores = (List<String>) lsProveedoresVentas.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lProveedores) {
                                if (fam.equals(res.getProveedorFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsProveedoresVentas.addItem(res.getProveedorFin());
                            } else {
                                lsProveedoresVentas.removeItem(res.getProveedorFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("clienteFin")) {
                            List<String> lProveedores = (List<String>) lsClientes.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lProveedores) {
                                if (fam.equals(res.getClienteFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsClientes.addItem(res.getClienteFin());
                            } else {
                                lsClientes.removeItem(res.getClienteFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("origenFin")) {
                            List<String> lPaises = (List<String>) lsPaisesVentas.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lPaises) {
                                if (fam.equals(res.getOrigenFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsPaisesVentas.addItem(res.getOrigenFin());
                            } else {
                                lsPaisesVentas.removeItem(res.getOrigenFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        } else if (event.getPropertyId().equals("calidadVentaFin")) {
                            List<String> lCalidades = (List<String>) lsCalidadVentas.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lCalidades) {
                                if (fam.equals(res.getCalidadVentaFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsCalidadVentas.addItem(res.getCalidadVentaFin());
                            } else {
                                lsCalidadVentas.removeItem(res.getCalidadVentaFin());
                            }
                            if (aplicaFiltros()) {
                                // Comprobamos si está filtrando por compras o ventas.
                                if (aplicaFiltrosVentas()) {
                                    aplicarFiltroVentas(true);
                                } else {
                                    aplicarFiltroCompras(true, false);
                                }
                            } else {
                                aplicarFiltroCompras(true, false);
                            }

                        }

                    }
                }
            }
        });
    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaVentasTotales() {
        tablaVentasTotales = new TablaGenerica(new Object[] { "kgsNetosFin", }, new String[] { "Kilos totales" }, bcVentasTotales);
        //tablaVentasTotales = new TablaGenerica(new Object[] { "numBultos", "numBultosPale", "kgsNetosFin", "kgsNetos" }, new String[] { "Bultos mov.venta totales", "Bultos por palé totales", "Kilos totales", "Kilos netos teo. venta totales" }, bcVentasTotales);
        tablaVentasTotales.addStyleName("big strong");
        tablaVentasTotales.setPageLength(1);
        // Para mostrar/ocultar columnas de la tabla
        tablaVentasTotales.setColumnCollapsingAllowed(false);
        // Para cambiar el orden las columnas
        tablaVentasTotales.setColumnReorderingAllowed(false);
        tablaVentasTotales.setSelectable(false);
        tablaVentasTotales.setSortEnabled(false);

        tablaVentasTotales.setColumnWidth("kgsNetos", 300);

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
     * Método que se encarga de construir el componente que conforma la parte del filtro
     * @return El componente que conforma la parte del filtro.
     */
    private void crearComponenteFiltro() {

        // Cabecera de la pantalla
        cabeceraPantalla = new HorizontalLayout();
        cabeceraPantalla.setSpacing(true);
        cabeceraPantalla.setMargin(true);

        // Contenedor de los componentes para filtrar 
        filtro = new VerticalLayout();
        filtro.setSpacing(true);
        filtro.setMargin(true);

        cbMostrarDatosGlobales = new ComboBox("Mostrar/Ocultar totales");
        cbMostrarDatosGlobales.setNullSelectionAllowed(false);
        cbMostrarDatosGlobales.addItem("Ocultar");
        cbMostrarDatosGlobales.addItem("Mostrar");

        cbMostrarDatosGlobales.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (((String) cbMostrarDatosGlobales.getValue()).equals("Mostrar")) {
                    tablaComprasTotales.setVisible(true);
                    tablaVentasTotales.setVisible(true);
                } else {
                    tablaComprasTotales.setVisible(false);
                    tablaVentasTotales.setVisible(false);
                }
            }
        });

        cbMostrarDatosGlobales.setValue("Mostrar");

        cbMostrarFiltro = new ComboBox("Mostrar/Ocultar filtros");
        cbMostrarFiltro.addItem("Ocultar");
        cbMostrarFiltro.addItem("Mostrar");
        cbMostrarFiltro.setNewItemsAllowed(false);
        cbMostrarFiltro.setNullSelectionAllowed(false);
        cbMostrarFiltro.setFilteringMode(FilteringMode.CONTAINS);
        cbMostrarFiltro.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (((String) cbMostrarFiltro.getValue()).equals("Mostrar")) {
                    filtro.setVisible(true);
                    tablaCompras.setPageLength(40);
                    tablaVentas.setPageLength(40);
                } else {
                    filtro.setVisible(false);
                    tablaCompras.setPageLength(70);
                    tablaVentas.setPageLength(70);
                }
            }
        });

        cbMostrarFiltro.setValue("Mostrar");

        cabeceraPantalla.addComponent(cbMostrarDatosGlobales);

        cabeceraPantalla.setVisible(true);

        // COMPONENTES PARA FILTRAR

        /************************************************** FAMILIAS **************************************************/
        // Combobox con las familias
        cbFamiliasCompras = new ComboBox();
        cbFamiliasCompras.addItems(lFamiliasCompras);
        cbFamiliasCompras.setFilteringMode(FilteringMode.CONTAINS);
        cbFamiliasCompras.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbFamiliasCompras.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsFamiliasCompras.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbFamiliasCompras.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsFamiliasCompras.addItem(cbFamiliasCompras.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbFamiliasCompras.clear();
                }
            }
        });

        // Lista de albaranes que se están filtrando
        lsFamiliasCompras = new ListSelect();
        lsFamiliasCompras.setWidth(9, Sizeable.Unit.EM);
        lsFamiliasCompras.setHeight(7, Sizeable.Unit.EM);
        lsFamiliasCompras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsFamiliasCompras.getValue() != null) {
                    lsFamiliasCompras.removeItem(lsFamiliasCompras.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        // Combobox con las familias
        cbFamiliasVentas = new ComboBox();
        cbFamiliasVentas.addItems(lFamiliasCompras);
        cbFamiliasVentas.setFilteringMode(FilteringMode.CONTAINS);
        cbFamiliasVentas.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbFamiliasVentas.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsFamiliasVentas.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbFamiliasVentas.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsFamiliasVentas.addItem(cbFamiliasVentas.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbFamiliasVentas.clear();
                }
            }
        });

        // Lista de albaranes que se están filtrando
        lsFamiliasVentas = new ListSelect();
        lsFamiliasVentas.setWidth(9, Sizeable.Unit.EM);
        lsFamiliasVentas.setHeight(7, Sizeable.Unit.EM);
        lsFamiliasVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsFamiliasVentas.getValue() != null) {
                    lsFamiliasVentas.removeItem(lsFamiliasVentas.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        /************************************************************************************************************/

        /************************************************** PAISES **************************************************/

        // Combobox con los paises
        cbPaisesCompras = new ComboBox();
        cbPaisesCompras.addItems(lPaisesCompras);
        cbPaisesCompras.setFilteringMode(FilteringMode.CONTAINS);
        cbPaisesCompras.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbPaisesCompras.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsPaisesCompra.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbPaisesCompras.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsPaisesCompra.addItem(cbPaisesCompras.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbPaisesCompras.clear();
                }
            }
        });

        // Lista de paises que se están filtrando
        lsPaisesCompra = new ListSelect();
        lsPaisesCompra.setWidth(7, Sizeable.Unit.EM);
        lsPaisesCompra.setHeight(7, Sizeable.Unit.EM);
        lsPaisesCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsPaisesCompra.getValue() != null) {
                    lsPaisesCompra.removeItem(lsPaisesCompra.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        // Combobox con los paises
        cbPaisesVentas = new ComboBox();
        cbPaisesVentas.addItems(lPaisesVentas);
        cbPaisesVentas.setFilteringMode(FilteringMode.CONTAINS);
        cbPaisesVentas.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbPaisesVentas.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsPaisesVentas.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbPaisesVentas.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsPaisesVentas.addItem(cbPaisesVentas.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbPaisesVentas.clear();
                }
            }
        });

        // Lista de paises que se están filtrando
        lsPaisesVentas = new ListSelect();
        lsPaisesVentas.setWidth(7, Sizeable.Unit.EM);
        lsPaisesVentas.setHeight(7, Sizeable.Unit.EM);
        lsPaisesVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsPaisesVentas.getValue() != null) {
                    lsPaisesVentas.removeItem(lsPaisesVentas.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });
        /************************************************************************************************************/

        /************************************************** ARTÍCULOS **************************************************/

        // Combobox con los artículos
        cbArticulosCompras = new ComboBox();
        cbArticulosCompras.addItems(lArticulosCompras);
        cbArticulosCompras.setFilteringMode(FilteringMode.CONTAINS);
        cbArticulosCompras.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbArticulosCompras.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsArticulosCompras.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbArticulosCompras.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsArticulosCompras.addItem(cbArticulosCompras.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbArticulosCompras.clear();
                }
            }
        });

        // Lista de artículos que se están filtrando
        lsArticulosCompras = new ListSelect();
        lsArticulosCompras.setWidth(20, Sizeable.Unit.EM);
        lsArticulosCompras.setHeight(7, Sizeable.Unit.EM);
        lsArticulosCompras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsArticulosCompras.getValue() != null) {
                    lsArticulosCompras.removeItem(lsArticulosCompras.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        // Combobox con los artículos
        cbArticulosVentas = new ComboBox();
        cbArticulosVentas.addItems(lArticulosVentas);
        cbArticulosVentas.setFilteringMode(FilteringMode.CONTAINS);
        cbArticulosVentas.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbArticulosVentas.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsArticulosVentas.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbArticulosVentas.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsArticulosVentas.addItem(cbArticulosVentas.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbArticulosVentas.clear();
                }
            }
        });

        // Lista de paises que se están filtrando
        lsArticulosVentas = new ListSelect();
        lsArticulosVentas.setWidth(20, Sizeable.Unit.EM);
        lsArticulosVentas.setHeight(7, Sizeable.Unit.EM);
        lsArticulosVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsArticulosVentas.getValue() != null) {
                    lsArticulosVentas.removeItem(lsArticulosVentas.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });
        /************************************************************************************************************/

        /************************************************** GGN **************************************************/

        // Combobox con los artículos
        cbGgnCompra = new ComboBox();
        cbGgnCompra.addItem("NO GGN");
        cbGgnCompra.addItem("GGN NACIONAL");
        cbGgnCompra.addItem("GGN IMPORTACION");
        cbGgnCompra.setFilteringMode(FilteringMode.CONTAINS);
        cbGgnCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {

                if (aplicaFiltros()) {
                    if (aplicaFiltrosVentas()) {
                        aplicarFiltroVentas(true);
                    } else {
                        aplicarFiltroCompras(true, false);
                    }
                } else {
                    cargarCompras(false);
                    aplicarFiltroCompras(true, false);
                }

            }
        });

        /************************************************** CLIENTES **************************************************/

        // Combobox con los clientes
        cbClientes = new ComboBox();
        cbClientes.addItems(lClientes);
        cbClientes.setFilteringMode(FilteringMode.CONTAINS);
        cbClientes.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbClientes.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsClientes.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbClientes.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsClientes.addItem(cbClientes.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbClientes.clear();
                }
            }
        });

        // Lista de clientes que se están filtrando
        lsClientes = new ListSelect();
        lsClientes.setWidth(9, Sizeable.Unit.EM);
        lsClientes.setHeight(7, Sizeable.Unit.EM);
        lsClientes.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsClientes.getValue() != null) {
                    lsClientes.removeItem(lsClientes.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        /************************************************************************************************************/

        /************************************************** PROVEEDORES **************************************************/

        // Combobox con los proveedores
        cbProveedoresCompras = new ComboBox();
        cbProveedoresCompras.addItems(lProveedoresCompras);
        cbProveedoresCompras.setFilteringMode(FilteringMode.CONTAINS);
        cbProveedoresCompras.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbProveedoresCompras.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsProveedoresCompras.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbProveedoresCompras.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsProveedoresCompras.addItem(cbProveedoresCompras.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbProveedoresCompras.clear();
                }
            }
        });

        // Lista de proveedores que se están filtrando
        lsProveedoresCompras = new ListSelect();
        lsProveedoresCompras.setWidth(9, Sizeable.Unit.EM);
        lsProveedoresCompras.setHeight(7, Sizeable.Unit.EM);
        lsProveedoresCompras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsProveedoresCompras.getValue() != null) {
                    lsProveedoresCompras.removeItem(lsProveedoresCompras.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        // Combobox con los proveedores
        cbProveedoreVentas = new ComboBox();
        cbProveedoreVentas.addItems(lProveedoresVentas);
        cbProveedoreVentas.setFilteringMode(FilteringMode.CONTAINS);
        cbProveedoreVentas.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbProveedoreVentas.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsProveedoresVentas.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbProveedoreVentas.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsProveedoresVentas.addItem(cbProveedoreVentas.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbProveedoreVentas.clear();
                }
            }
        });

        // Lista de proveedores que se están filtrando
        lsProveedoresVentas = new ListSelect();
        lsProveedoresVentas.setWidth(9, Sizeable.Unit.EM);
        lsProveedoresVentas.setHeight(7, Sizeable.Unit.EM);
        lsProveedoresVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsProveedoresVentas.getValue() != null) {
                    lsProveedoresVentas.removeItem(lsProveedoresVentas.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });
        /************************************************************************************************************/

        /************************************************** ALBARANES **************************************************/

        // Combobox con los albaranes
        cbAlbaranCompra = new ComboBox();
        cbAlbaranCompra.addItems(lAlbaranesCompras);
        cbAlbaranCompra.setFilteringMode(FilteringMode.CONTAINS);
        cbAlbaranCompra.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbAlbaranCompra.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsAlbaranesCompras.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbAlbaranCompra.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsAlbaranesCompras.addItem(cbAlbaranCompra.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbAlbaranCompra.clear();
                }
            }
        });

        // Lista de albaranes que se están filtrando
        lsAlbaranesCompras = new ListSelect();
        lsAlbaranesCompras.setWidth(7, Sizeable.Unit.EM);
        lsAlbaranesCompras.setHeight(7, Sizeable.Unit.EM);
        lsAlbaranesCompras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsAlbaranesCompras.getValue() != null) {
                    lsAlbaranesCompras.removeItem(lsAlbaranesCompras.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        // Combobox con los albaranes
        cbAlbaranVenta = new ComboBox();
        cbAlbaranVenta.addItems(lAlbaranesVentas);
        cbAlbaranVenta.setFilteringMode(FilteringMode.CONTAINS);
        cbAlbaranVenta.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbAlbaranVenta.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsAlbaranesVentas.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbAlbaranVenta.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsAlbaranesVentas.addItem(cbAlbaranVenta.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbAlbaranVenta.clear();
                }
            }
        });

        // Lista de albaranes que se están filtrando
        lsAlbaranesVentas = new ListSelect();
        lsAlbaranesVentas.setWidth(7, Sizeable.Unit.EM);
        lsAlbaranesVentas.setHeight(7, Sizeable.Unit.EM);
        lsAlbaranesVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsAlbaranesVentas.getValue() != null) {
                    lsAlbaranesVentas.removeItem(lsAlbaranesVentas.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        /************************************************************************************************************/

        /************************************************** PEDIDOS VENTA **************************************************/

        // Combobox con los pedidos de venta
        cbPedidoVenta = new ComboBox();
        cbPedidoVenta.addItems(lPedidosVentas);
        cbPedidoVenta.setFilteringMode(FilteringMode.CONTAINS);
        cbPedidoVenta.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbPedidoVenta.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsPedidosVentas.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbPedidoVenta.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsPedidosVentas.addItem(cbPedidoVenta.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbPedidoVenta.clear();
                }
            }
        });

        // Lista de pedidos de venta que se están filtrando
        lsPedidosVentas = new ListSelect();
        lsPedidosVentas.setWidth(7, Sizeable.Unit.EM);
        lsPedidosVentas.setHeight(7, Sizeable.Unit.EM);
        lsPedidosVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsPedidosVentas.getValue() != null) {
                    lsPedidosVentas.removeItem(lsPedidosVentas.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        /************************************************************************************************************/

        /************************************************** CALIDAD **************************************************/

        // Combobox con las calidades
        cbCalidadCompra = new ComboBox();
        cbCalidadCompra.addItems(lCalidadCompras);
        cbCalidadCompra.setFilteringMode(FilteringMode.CONTAINS);
        cbCalidadCompra.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbAlbaranCompra.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsCalidadCompra.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbCalidadCompra.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsCalidadCompra.addItem(cbCalidadCompra.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbCalidadCompra.clear();
                }
            }
        });

        // Lista de calidades que se están filtrando
        lsCalidadCompra = new ListSelect();
        lsCalidadCompra.setWidth(7, Sizeable.Unit.EM);
        lsCalidadCompra.setHeight(7, Sizeable.Unit.EM);
        lsCalidadCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsCalidadCompra.getValue() != null) {
                    lsCalidadCompra.removeItem(lsCalidadCompra.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        // Combobox con las calidades
        cbCalidadVenta = new ComboBox();
        cbCalidadVenta.addItems(lCalidadVentas);
        cbCalidadVenta.setFilteringMode(FilteringMode.CONTAINS);
        cbCalidadVenta.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbCalidadVenta.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsCalidadVentas.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbCalidadVenta.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsCalidadVentas.addItem(cbCalidadVenta.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbCalidadVenta.clear();
                }
            }
        });

        // Lista de calidades que se están filtrando
        lsCalidadVentas = new ListSelect();
        lsCalidadVentas.setWidth(7, Sizeable.Unit.EM);
        lsCalidadVentas.setHeight(7, Sizeable.Unit.EM);
        lsCalidadVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsCalidadVentas.getValue() != null) {
                    lsCalidadVentas.removeItem(lsCalidadVentas.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        /************************************************************************************************************/

        /************************************************** LOTE **************************************************/

        // Combobox con los lote
        cbLoteCompra = new ComboBox();
        cbLoteCompra.addItems(lLotesCompras);
        cbLoteCompra.setFilteringMode(FilteringMode.CONTAINS);
        cbLoteCompra.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbLoteCompra.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsLoteCompras.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbLoteCompra.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsLoteCompras.addItem(cbLoteCompra.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbLoteCompra.clear();
                }
            }
        });

        // Lista de lote que se están filtrando
        lsLoteCompras = new ListSelect();
        lsLoteCompras.setWidth(7, Sizeable.Unit.EM);
        lsLoteCompras.setHeight(7, Sizeable.Unit.EM);
        lsLoteCompras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsLoteCompras.getValue() != null) {
                    lsLoteCompras.removeItem(lsLoteCompras.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        // Combobox con los lote
        cbLoteVenta = new ComboBox();
        cbLoteVenta.addItems(lLotesVentas);
        cbLoteVenta.setFilteringMode(FilteringMode.CONTAINS);
        cbLoteVenta.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbLoteVenta.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsLoteVentas.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbLoteVenta.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsLoteVentas.addItem(cbLoteVenta.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbLoteVenta.clear();
                }
            }
        });

        // Lista de lote que se están filtrando
        lsLoteVentas = new ListSelect();
        lsLoteVentas.setWidth(7, Sizeable.Unit.EM);
        lsLoteVentas.setHeight(7, Sizeable.Unit.EM);
        lsLoteVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsLoteVentas.getValue() != null) {
                    lsLoteVentas.removeItem(lsLoteVentas.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        /************************************************************************************************************/

        /************************************************** PARTIDA COMPRA **************************************************/

        // Combobox con los lote
        cbPartidaCompra = new ComboBox();
        cbPartidaCompra.addItems(lPartidasCompras);
        cbPartidaCompra.setFilteringMode(FilteringMode.CONTAINS);
        cbPartidaCompra.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbPartidaCompra.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsPartidasCompra.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbPartidaCompra.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsPartidasCompra.addItem(cbPartidaCompra.getValue());
                        if (aplicaFiltros()) {
                            if (aplicaFiltrosVentas()) {
                                aplicarFiltroVentas(true);
                            } else {
                                aplicarFiltroCompras(true, false);
                            }
                        } else {
                            cargarCompras(false);
                            aplicarFiltroCompras(true, false);
                        }
                    }
                    cbPartidaCompra.clear();
                }
            }
        });

        // Lista de lote que se están filtrando
        lsPartidasCompra = new ListSelect();
        lsPartidasCompra.setWidth(7, Sizeable.Unit.EM);
        lsPartidasCompra.setHeight(7, Sizeable.Unit.EM);
        lsPartidasCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsPartidasCompra.getValue() != null) {
                    lsPartidasCompra.removeItem(lsPartidasCompra.getValue());
                    if (aplicaFiltros()) {
                        if (aplicaFiltrosVentas()) {
                            aplicarFiltroVentas(true);
                        } else {
                            aplicarFiltroCompras(true, false);
                        }
                    } else {
                        cargarCompras(false);
                        aplicarFiltroCompras(true, false);
                    }
                }
            }
        });

        /************************************************************************************************************/

        /************************************************** FECHAS **************************************************/
        fechaDesdeCompra = new DateField();
        fechaDesdeCompra.setWidth(9, Sizeable.Unit.EM);
        fechaDesdeCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                cambiarFechasCompras();
            }
        });

        // Fecha compra hasta.
        fechaHastaCompra = new DateField();
        fechaHastaCompra.setWidth(9, Sizeable.Unit.EM);
        fechaHastaCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                cambiarFechasCompras();
            }
        });

        // Fecha venta desde.
        fechaDesdeVenta = new DateField();
        fechaDesdeVenta.setWidth(9, Sizeable.Unit.EM);
        fechaDesdeVenta.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                cambiarFechasVentas();
            }
        });

        // Fecha venta hasta.
        fechaHastaVenta = new DateField();
        fechaHastaVenta.setWidth(9, Sizeable.Unit.EM);
        fechaHastaVenta.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                cambiarFechasVentas();
            }
        });

        /************************************************************************************************************/

        cbNombresCompras = new ComboBox();
        cbNombresCompras.addItem("Todos");
        cbNombresCompras.addItems(lNombresCompras);
        cbNombresCompras.setValue("Todos");
        cbNombresCompras.setNullSelectionAllowed(false);
        cbNombresCompras.setNewItemsAllowed(false);
        cbNombresCompras.setFilteringMode(FilteringMode.CONTAINS);
        cbNombresCompras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {

                if (aplicaFiltros()) {
                    if (aplicaFiltrosVentas()) {
                        aplicarFiltroVentas(true);
                    } else {
                        aplicarFiltroCompras(true, false);
                    }
                } else {
                    cargarCompras(false);
                    aplicarFiltroCompras(true, false);
                }

            }
        });

        cbNombresVentas = new ComboBox();
        cbNombresVentas.addItem("Todos");
        cbNombresVentas.addItems(lNombresVentas);
        cbNombresVentas.setValue("Todos");
        cbNombresVentas.setNullSelectionAllowed(false);
        cbNombresVentas.setNewItemsAllowed(false);
        cbNombresVentas.setFilteringMode(FilteringMode.CONTAINS);
        cbNombresVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {

                if (aplicaFiltros()) {
                    if (aplicaFiltrosVentas()) {
                        aplicarFiltroVentas(true);
                    } else {
                        aplicarFiltroCompras(true, false);
                    }
                } else {
                    cargarVentas();
                    aplicarFiltroVentas(true);
                }

            }
        });

        cabeceraPantalla.addComponent(cbMostrarFiltro);

        // Tabla de filtro
        Table table = new Table();
        table.addStyleName("small strong");
        // La cabecera de la tabla del filtro.
        //table.addContainerProperty("Descripción importación Compra", ComboBox.class, null);
        //table.addContainerProperty("Descripción importación Venta", ComboBox.class, null);
        table.addContainerProperty("Partida Compra", ComboBox.class, null);
        table.addContainerProperty("Filtro partida Compra", ListSelect.class, null);
        table.addContainerProperty("Nº Albarán Compra", ComboBox.class, null);
        table.addContainerProperty("Filtro albarán Compra", ListSelect.class, null);
        table.addContainerProperty("Nº Venta", ComboBox.class, null);
        table.addContainerProperty("Filtro venta", ListSelect.class, null);
        table.addContainerProperty("Albarán venta", ComboBox.class, null);
        table.addContainerProperty("Filtro Albarán venta", ListSelect.class, null);
        table.addContainerProperty("Artículo compra", ComboBox.class, null);
        table.addContainerProperty("Filtro artículo compra", ListSelect.class, null);
        table.addContainerProperty("Artículo venta", ComboBox.class, null);
        table.addContainerProperty("Filtro artículo venta", ListSelect.class, null);
        table.addContainerProperty("Familia compra", ComboBox.class, null);
        table.addContainerProperty("Filtro familia compra", ListSelect.class, null);
        table.addContainerProperty("Familia venta", ComboBox.class, null);
        table.addContainerProperty("Filtro familia venta", ListSelect.class, null);
        table.addContainerProperty("Orígen compra", ComboBox.class, null);
        table.addContainerProperty("Filtro orígen compra", ListSelect.class, null);
        table.addContainerProperty("Orígen venta", ComboBox.class, null);
        table.addContainerProperty("Filtro orígen venta", ListSelect.class, null);
        table.addContainerProperty("GGN", ComboBox.class, null);
        table.addContainerProperty("Calidad compra", ComboBox.class, null);
        table.addContainerProperty("Filtro calidad compra", ListSelect.class, null);
        table.addContainerProperty("Calidad venta", ComboBox.class, null);
        table.addContainerProperty("Filtro venta compra", ListSelect.class, null);
        table.addContainerProperty("Proveedor compra", ComboBox.class, null);
        table.addContainerProperty("Filtro proveedor compra", ListSelect.class, null);
        table.addContainerProperty("Proveedor venta", ComboBox.class, null);
        table.addContainerProperty("Filtro proveedor venta", ListSelect.class, null);
        table.addContainerProperty("Cliente", ComboBox.class, null);
        table.addContainerProperty("Filtro cliente", ListSelect.class, null);
        table.addContainerProperty("Lote entrada", ComboBox.class, null);
        table.addContainerProperty("Filtro lote compra", ListSelect.class, null);
        table.addContainerProperty("Lote salida", ComboBox.class, null);
        table.addContainerProperty("Filtro lote venta", ListSelect.class, null);

        // Los componentes que componen al filtro
        table.addItem(
                      new Object[] { cbPartidaCompra, lsPartidasCompra, cbAlbaranCompra, lsAlbaranesCompras, cbPedidoVenta, lsPedidosVentas, cbAlbaranVenta, lsAlbaranesVentas, cbArticulosCompras, lsArticulosCompras, cbArticulosVentas, lsArticulosVentas, cbFamiliasCompras, lsFamiliasCompras, cbFamiliasVentas, lsFamiliasVentas, cbPaisesCompras, lsPaisesCompra, cbPaisesVentas, lsPaisesVentas, cbGgnCompra, cbCalidadCompra, lsCalidadCompra, cbCalidadVenta, lsCalidadVentas, cbProveedoresCompras, lsProveedoresCompras, cbProveedoreVentas, lsProveedoresVentas, cbClientes, lsClientes, cbLoteCompra, lsLoteCompras, cbLoteVenta, lsLoteVentas },
                          1);
        table.setPageLength(table.size());

        table.setColumnWidth("Filtro partida Compra", 127);
        table.setColumnWidth("Filtro albarán Compra", 127);
        table.setColumnWidth("Filtro venta", 127);
        table.setColumnWidth("Filtro Albarán venta", 127);
        table.setColumnWidth("Filtro artículo compra", 380);
        table.setColumnWidth("Filtro artículo venta", 380);
        table.setColumnWidth("Filtro familia compra", 170);
        table.setColumnWidth("Filtro familia venta", 170);
        table.setColumnWidth("Filtro orígen compra", 127);
        table.setColumnWidth("Filtro orígen venta", 127);
        table.setColumnWidth("Filtro calidad compra", 127);
        table.setColumnWidth("Filtro calidad venta", 127);
        table.setColumnWidth("Filtro proveedor compra", 127);
        table.setColumnWidth("Filtro proveedor venta", 127);
        table.setColumnWidth("Filtro cliente", 127);
        table.setColumnWidth("Filtro lote compra", 127);
        table.setColumnWidth("Filtro lote venta", 127);

        Table table2 = new Table();
        table2.addStyleName("small strong");
        table2.addContainerProperty("Fecha compra desde", DateField.class, null);
        table2.addContainerProperty("Fecha compra hasta", DateField.class, null);
        table2.addContainerProperty("Fecha venta desde", DateField.class, null);
        table2.addContainerProperty("Fecha venta hasta", DateField.class, null);
        table2.addItem(new Object[] { fechaDesdeCompra, fechaHastaCompra, fechaDesdeVenta, fechaHastaVenta, }, 1);
        table2.setPageLength(table.size());

        filtro.addComponent(table);
        filtro.addComponent(table2);

    }

    /**
     * Método que nos nutre el diccionario con las columnas para mostrarlas/ocultarlas en función de los datos guardados en BD.
     */
    private void nutrirDiccionarioCabeceraTablaCompras() {

        mColumnasCompras = new HashMap<String, String>();
        mColumnasCompras.put("Descripción importación", "nombreDescriptivo");
        mColumnasCompras.put("Albarán", "albaran");
        mColumnasCompras.put("Fecha", "fecha");
        mColumnasCompras.put("Partida", "partida");
        mColumnasCompras.put("Nº de cajas", "cajas");
        mColumnasCompras.put("Peso bruto", "kgsBruto");
        mColumnasCompras.put("Trazabilidad", "lote");
        mColumnasCompras.put("Peso neto", "pesoNeto");
        mColumnasCompras.put("Producto", "producto");
        mColumnasCompras.put("Entidad", "proveedor");
        mColumnasCompras.put("Orígen", "origen");
        mColumnasCompras.put("Plantilla de producto", "familia");
        mColumnasCompras.put("Global Gap", "ggn");
        mColumnasCompras.put("Calidad", "calidad");
        mColumnasCompras.put("Kgs disponibles", "kgsDisponibles");

        mColumnasCompras = new HashMap<String, String>();
        mColumnasCompras.put("nombreDescriptivo", "nombreDescriptivo");
        mColumnasCompras.put("albaran", "albaran");
        mColumnasCompras.put("fecha", "fecha");
        mColumnasCompras.put("partida", "partida");
        mColumnasCompras.put("cajas", "cajas");
        mColumnasCompras.put("kgsBruto", "kgsBruto");
        mColumnasCompras.put("lote", "lote");
        mColumnasCompras.put("pesoNeto", "pesoNeto");
        mColumnasCompras.put("producto", "producto");
        mColumnasCompras.put("proveedor", "proveedor");
        mColumnasCompras.put("origen", "origen");
        mColumnasCompras.put("familia", "familia");
        mColumnasCompras.put("ggn", "ggn");
        mColumnasCompras.put("calidad", "calidad");
        mColumnasCompras.put("kgsDisponibles", "kgsDisponibles");

        mColumnasIdsCompras = new HashMap<String, String>();
        mColumnasIdsCompras.put("nombreDescriptivo", "Descripción importación");
        mColumnasIdsCompras.put("albaran", "Albarán");
        mColumnasIdsCompras.put("fecha", "Fecha");
        mColumnasIdsCompras.put("partida", "Partida");
        mColumnasIdsCompras.put("cajas", "Nº de cajas");
        mColumnasIdsCompras.put("kgsBruto", "Peso bruto");
        mColumnasIdsCompras.put("lote", "Trazabilidad");
        mColumnasIdsCompras.put("pesoNeto", "Peso neto");
        mColumnasIdsCompras.put("producto", "Producto");
        mColumnasIdsCompras.put("proveedor", "Entidad");
        mColumnasIdsCompras.put("origen", "Orígen");
        mColumnasIdsCompras.put("familia", "Plantilla de producto");
        mColumnasIdsCompras.put("ggn", "Global Gap");
        mColumnasIdsCompras.put("calidad", "Calidad");
        mColumnasIdsCompras.put("kgsDisponibles", "Kgs disponibles");

        mColumnasGuardadoCompras = new HashMap<String, String>();
        mColumnasGuardadoCompras.put("Descripción importación", "nombreDescriptivo");
        mColumnasGuardadoCompras.put("Albarán", "albaran");
        mColumnasGuardadoCompras.put("Fecha", "fecha");
        mColumnasGuardadoCompras.put("Partida", "partida");
        mColumnasGuardadoCompras.put("Nº de cajas", "cajas");
        mColumnasGuardadoCompras.put("Peso bruto", "kgsBruto");
        mColumnasGuardadoCompras.put("Trazabilidad", "lote");
        mColumnasGuardadoCompras.put("Peso neto", "pesoNeto");
        mColumnasGuardadoCompras.put("Producto", "producto");
        mColumnasGuardadoCompras.put("Entidad", "proveedor");
        mColumnasGuardadoCompras.put("Orígen", "origen");
        mColumnasGuardadoCompras.put("Plantilla de producto", "familia");
        mColumnasGuardadoCompras.put("Global Gap", "ggn");
        mColumnasGuardadoCompras.put("Calidad", "calidad");
        mColumnasGuardadoCompras.put("Kgs disponibles", "kgsDisponibles");
    }

    /**
     * Método que nos nutre el diccionario con las columnas para mostrarlas/ocultarlas en función de los datos guardados en BD.
     */
    private void nutrirDiccionarioCabeceraTablaVentas() {

        mColumnasVentas = new HashMap<String, String>();
        mColumnasVentas.put("Descripción importación", "nombreDescriptivo");
        mColumnasVentas.put("Pedido de venta", "pedido");
        mColumnasVentas.put("Albarán de venta", "albaran");
        mColumnasVentas.put("Calibre", "calibre");
        mColumnasVentas.put("Trazabilidad", "lote");
        mColumnasVentas.put("ID de palé", "idPale");
        mColumnasVentas.put("Bultos mov. venta", "numBultos");
        mColumnasVentas.put("Bultos por palé", "numBultosPale");
        mColumnasVentas.put("Productor", "proveedor");
        mColumnasVentas.put("Entidad", "cliente");
        mColumnasVentas.put("Fecha salida", "fechaVenta");
        mColumnasVentas.put("Kilos", "kgs");
        mColumnasVentas.put("Peso neto teo. venta", "kgsNetos");
        mColumnasVentas.put("Variedad", "variedad");
        mColumnasVentas.put("Orígen venta", "origen");
        mColumnasVentas.put("Calidad venta", "calidadVenta");
        mColumnasVentas.put("Confección", "confeccion");
        mColumnasVentas.put("Producto", "familia");

        mColumnasVentas = new HashMap<String, String>();
        mColumnasVentas.put("nombreDescriptivo", "nombreDescriptivo");
        mColumnasVentas.put("pedido", "pedido");
        mColumnasVentas.put("albaran", "albaran");
        mColumnasVentas.put("calibre", "calibre");
        mColumnasVentas.put("lote", "lote");
        mColumnasVentas.put("idPale", "idPale");
        mColumnasVentas.put("numBultos", "numBultos");
        mColumnasVentas.put("numBultosPale", "numBultosPale");
        mColumnasVentas.put("proveedor", "proveedor");
        mColumnasVentas.put("cliente", "cliente");
        mColumnasVentas.put("fechaVenta", "fechaVenta");
        mColumnasVentas.put("kgs", "kgs");
        mColumnasVentas.put("kgsNetos", "kgsNetos");
        mColumnasVentas.put("variedad", "variedad");
        mColumnasVentas.put("origen", "origen");
        mColumnasVentas.put("calidadVenta", "calidadVenta");
        mColumnasVentas.put("confeccion", "confeccion");
        mColumnasVentas.put("familia", "familia");

        mColumnasIdsVentas = new HashMap<String, String>();
        mColumnasIdsVentas.put("nombreDescriptivo", "Descripción importación");
        mColumnasIdsVentas.put("pedido", "Pedido de venta");
        mColumnasIdsVentas.put("albaran", "Albarán de venta");
        mColumnasIdsVentas.put("calibre", "Calibre");
        mColumnasIdsVentas.put("lote", "Trazabilidad");
        mColumnasIdsVentas.put("idPale", "ID de palé");
        mColumnasIdsVentas.put("numBultos", "Bultos mov. venta");
        mColumnasIdsVentas.put("numBultosPale", "Bultos por palé");
        mColumnasIdsVentas.put("proveedor", "Productor");
        mColumnasIdsVentas.put("cliente", "Entidad");
        mColumnasIdsVentas.put("fechaVenta", "Fecha salida");
        mColumnasIdsVentas.put("kgs", "Kilos");
        mColumnasIdsVentas.put("kgsNetos", "Peso neto teo. venta");
        mColumnasIdsVentas.put("variedad", "Variedad");
        mColumnasIdsVentas.put("origen", "Orígen venta");
        mColumnasIdsVentas.put("calidadVenta", "Calidad venta");
        mColumnasIdsVentas.put("confeccion", "Confección");
        mColumnasIdsVentas.put("familia", "Producto");

        mColumnasGuardadoVentas = new HashMap<String, String>();
        mColumnasGuardadoVentas.put("Descripción importación", "nombreDescriptivo");
        mColumnasGuardadoVentas.put("Pedido de venta", "pedido");
        mColumnasGuardadoVentas.put("Albarán de venta", "albaran");
        mColumnasGuardadoVentas.put("Calibre", "calibre");
        mColumnasGuardadoVentas.put("Trazabilidad", "lote");
        mColumnasGuardadoVentas.put("ID de palé", "idPale");
        mColumnasGuardadoVentas.put("Bultos mov. venta", "numBultos");
        mColumnasGuardadoVentas.put("Bultos por palé", "numBultosPale");
        mColumnasGuardadoVentas.put("Productor", "proveedor");
        mColumnasGuardadoVentas.put("Entidad", "cliente");
        mColumnasGuardadoVentas.put("Fecha salida", "fechaVenta");
        mColumnasGuardadoVentas.put("Kilos", "kgs");
        mColumnasGuardadoVentas.put("Peso neto teo. venta", "kgsNetos");
        mColumnasGuardadoVentas.put("Variedad", "variedad");
        mColumnasGuardadoVentas.put("Orígen venta", "origen");
        mColumnasGuardadoVentas.put("Calidad venta", "calidadVenta");
        mColumnasGuardadoVentas.put("Confección", "confeccion");
        mColumnasGuardadoVentas.put("Producto", "familia");
    }

    /**
     * Método que nos guarda las columnas y en qué orden se muestran los datos en la tabla de empresas.
     */
    private void guardarOrdenColumnasCompras() {
        String columnas[] = tablaCompras.getColumnHeaders();
        String columnasIdioma[] = new String[columnas.length];

        int aux = 0;
        // Nurimos el array con 
        while (aux < columnas.length) {
            columnasIdioma[aux] = new String(mColumnasGuardadoCompras.get(columnas[aux]));
            aux++;
        }

        List<TColumnasTablasEmpleado> lCols = Utils.generarListaGenerica();
        TColumnasTablasEmpleado col = null;
        if (columnas != null && columnas.length > 0) {
            // Vamos recorriendo la cabecera de la tabla para coger los campos.
            Integer cnt = 0;
            while (cnt < columnas.length) {
                if (tablaCompras.isColumnCollapsed(mColumnasGuardadoCompras.get(columnas[cnt]))) {
                    cnt++;
                    continue;
                }
                col = new TColumnasTablasEmpleado();
                col.setCampo(columnasIdioma[cnt]);
                col.setIdEmpleado(user);
                col.setIdTabla(Integer.valueOf(tablaCompras.getId()));
                col.setNombrePantalla(NAME);
                col.setOrdenCampo(cnt);
                lCols.add(col);
                cnt++;
            }
            try {

                // Guardamos las columnas y en el orden indicado.
                contrVista.guardarCamposTablaEmpleado(lCols, user, NAME, Integer.valueOf(tablaCompras.getId()), user, time);

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

    /**
     * Método que nos guarda las columnas y en qué orden se muestran los datos en la tabla de empresas.
     */
    private void guardarOrdenColumnasVentas() {
        String columnas[] = tablaVentas.getColumnHeaders();
        String columnasIdioma[] = new String[columnas.length];

        int aux = 0;
        // Nurimos el array con 
        while (aux < columnas.length) {
            columnasIdioma[aux] = new String(mColumnasGuardadoVentas.get(columnas[aux]));
            aux++;
        }

        List<TColumnasTablasEmpleado> lCols = Utils.generarListaGenerica();
        TColumnasTablasEmpleado col = null;
        if (columnas != null && columnas.length > 0) {
            // Vamos recorriendo la cabecera de la tabla para coger los campos.
            Integer cnt = 0;
            while (cnt < columnas.length) {
                if (tablaVentas.isColumnCollapsed(mColumnasGuardadoVentas.get(columnas[cnt]))) {
                    cnt++;
                    continue;
                }
                col = new TColumnasTablasEmpleado();
                col.setCampo(columnasIdioma[cnt]);
                col.setIdEmpleado(user);
                col.setIdTabla(Integer.valueOf(tablaVentas.getId()));
                col.setNombrePantalla(NAME);
                col.setOrdenCampo(cnt);
                lCols.add(col);
                cnt++;
            }
            try {

                // Guardamos las columnas y en el orden indicado.
                contrVista.guardarCamposTablaEmpleado(lCols, user, NAME, Integer.valueOf(tablaVentas.getId()), user, time);

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

    /**
     * Método que nos muestra las columnas según la configuración de las compras.
     * @param lColumnas Las columnas y en qué orden se van a mostrar.
     */
    private void mostrarColumnasTablaCompras(List<TColumnasTablasEmpleado> lColumnas) {

        List<String> lCamposTabla = Utils.generarListaGenerica();
        List<String> lCamposTablaIdioma = Utils.generarListaGenerica();
        lCamposTabla.addAll(mColumnasCompras.values());
        lCamposTablaIdioma.addAll(mColumnasCompras.values());

        if (!lColumnas.isEmpty()) {
            Object[] visibleColumns = new Object[mColumnasIdsCompras.size()];

            int i = 0;
            for (TColumnasTablasEmpleado col : lColumnas) {
                visibleColumns[i] = mColumnasCompras.get(col.getCampo());
                lCamposTabla.remove(col.getCampo());
                lCamposTablaIdioma.remove(col.getCampo());
                i++;
            }

            // Eliminamos los campos que no se identificaron.
            for (String campo : lCamposTablaIdioma) {
                visibleColumns[i] = mColumnasCompras.get(campo);
                tablaCompras.setColumnCollapsed(mColumnasCompras.get(campo), true);
                i++;
            }

            tablaCompras.setVisibleColumns(visibleColumns);
        }

    }

    /**
     * Método que nos muestra las columnas según la configuración de las ventas.
     * @param lColumnas Las columnas y en qué orden se van a mostrar.
     */
    private void mostrarColumnasTablaVentas(List<TColumnasTablasEmpleado> lColumnas) {

        List<String> lCamposTabla = Utils.generarListaGenerica();
        List<String> lCamposTablaIdioma = Utils.generarListaGenerica();
        lCamposTabla.addAll(mColumnasVentas.values());
        lCamposTablaIdioma.addAll(mColumnasVentas.values());

        if (!lColumnas.isEmpty()) {
            Object[] visibleColumns = new Object[mColumnasIdsVentas.size()];

            int i = 0;
            for (TColumnasTablasEmpleado col : lColumnas) {
                visibleColumns[i] = mColumnasVentas.get(col.getCampo());
                lCamposTabla.remove(col.getCampo());
                lCamposTablaIdioma.remove(col.getCampo());
                i++;
            }

            // Eliminamos los campos que no se identificaron.
            for (String campo : lCamposTablaIdioma) {
                visibleColumns[i] = mColumnasVentas.get(campo);
                tablaVentas.setColumnCollapsed(mColumnasVentas.get(campo), true);
                i++;
            }

            tablaVentas.setVisibleColumns(visibleColumns);
        }
    }

    /**
     * Método que nos sirve para calcular los totales de los datos que se muestran en pantalla.
     */
    @SuppressWarnings("unchecked")
    private void calcularTotales() {
        DecimalFormat df3 = new DecimalFormat("#,##0");

        // Compras
        Double totalKgsDisponibles = Double.valueOf(0);
        Double totalKgsCompras = Double.valueOf(0);

        // Ventas
        Double totalBultosVentas = Double.valueOf(0);
        Double totalBultosPaleVentas = Double.valueOf(0);
        Double totalKgsVentas = Double.valueOf(0);
        Double totalKgsNetosVentas = Double.valueOf(0);

        // Calculamos los conceptos de compras
        List<String> lIds = bcCompras.getItemIds();

        BeanItem<TComprasVista> bRes = null;
        TComprasVista res = null;

        for (String id : lIds) {
            bRes = (BeanItem<TComprasVista>) tablaCompras.getItem(id);
            res = bRes.getBean();
            totalKgsDisponibles += res.getKgsDisponibles();
            totalKgsCompras += res.getPesoNetoFin();
        }

        // Calculamos los conceptos de ventas
        lIds = bcVentas.getItemIds();

        BeanItem<TVentasVista> bResV = null;
        TVentasVista resV = null;

        for (String id : lIds) {
            bResV = (BeanItem<TVentasVista>) tablaVentas.getItem(id);
            resV = bResV.getBean();

            totalBultosVentas += Integer.valueOf(resV.getNumBultosPaleFin());
            totalBultosPaleVentas += Integer.valueOf(resV.getNumBultosFin());
            totalKgsVentas += resV.getKgsFin();
            totalKgsNetosVentas += resV.getKgsNetosFin();
        }

        // Compras totales
        TComprasVista cTotales = new TComprasVista();
        cTotales.setId("1");
        cTotales.setKgsDisponibles(totalKgsDisponibles);
        cTotales.setPesoNeto(totalKgsCompras);

        // Ventas totales
        TVentasVista vTotales = new TVentasVista();
        vTotales.setId("1");
        vTotales.setNumBultos(df3.format(totalBultosVentas.intValue()));
        vTotales.setNumBultosPale(df3.format(totalBultosPaleVentas.intValue()));
        vTotales.setKgsNetosFin(totalKgsVentas);
        vTotales.setKgsNetos(totalKgsNetosVentas);

        // Añadimos los registros de totales.
        bcComprasTotales.removeAllItems();
        bcComprasTotales.addBean(cTotales);

        bcVentasTotales.removeAllItems();
        bcVentasTotales.addBean(vTotales);

        comprobarFiltrosAplicados();
    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    @SuppressWarnings("unchecked")
    private void aplicarFiltroCompras(Boolean cargarVentas, Boolean filtrandoVentas) {

        bcCompras.removeAllContainerFilters();
        if (primerFiltro) {
            bcVentas.removeAllContainerFilters();
            primerFiltro = false;
        }
        boolean entra = false;
        if (((List<String>) lsFamiliasCompras.getItemIds()).size() != 0) {
            List<String> lFamilias = (List<String>) lsFamiliasCompras.getItemIds();
            String familias = "";
            for (String fam : lFamilias) {
                if (fam.contains("(")) {
                    fam = fam.replaceAll("\\(", "-");
                }
                if (fam.contains(")")) {
                    fam = fam.replaceAll("\\)", "-");
                }
                familias = familias + fam + "****";
            }
            // Set new filter for the "Name" column
            String[] params = familias.split("\\*\\*\\*\\*");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("familiaFin", filtro.toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsAlbaranesCompras.getItemIds()).size() != 0) {
            List<String> lAlbaranes = (List<String>) lsAlbaranesCompras.getItemIds();
            String albaranes = "";
            for (String albaran : lAlbaranes) {
                albaranes = albaranes + albaran + " ";
            }
            // Set new filter for the "Name" column
            String[] params = albaranes.split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("albaranFin", filtro.toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsPartidasCompra.getItemIds()).size() != 0) {
            List<String> lPartidas = (List<String>) lsPartidasCompra.getItemIds();
            String partidas = "";
            for (String partida : lPartidas) {
                partidas = partidas + partida + " ";
            }
            // Set new filter for the "Name" column
            String[] params = partidas.split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("partidaFin", filtro.toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
            entra = true;
        }

        if (((List<TPais>) lsPaisesCompra.getItemIds()).size() != 0) {
            List<String> lPaises = (List<String>) lsPaisesCompra.getItemIds();
            String paises = "";
            for (String pais : lPaises) {
                paises = paises + pais + " ";
            }
            // Set new filter for the "Name" column
            String[] params = paises.split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("origenFin", filtro.toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsProveedoresCompras.getItemIds()).size() != 0) {
            List<String> lProveedores = (List<String>) lsProveedoresCompras.getItemIds();
            String proveedores = "";
            for (String proveedor : lProveedores) {
                proveedores = proveedores + proveedor + "  ";
            }
            // Set new filter for the "Name" column
            String[] params = proveedores.split("  ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("proveedorFin", filtro.toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsLoteCompras.getItemIds()).size() != 0) {
            List<String> lLotes = (List<String>) lsLoteCompras.getItemIds();
            String lotes = "";
            for (String lote : lLotes) {
                lotes = lotes + lote + " ";
            }
            // Set new filter for the "Name" column
            String[] params = lotes.split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("loteFin", filtro.toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsArticulosCompras.getItemIds()).size() != 0) {
            List<String> lArticulos = (List<String>) lsArticulosCompras.getItemIds();
            String articulos = "";
            for (String articulo : lArticulos) {
                if (articulo.contains("(")) {
                    articulo = articulo.replaceAll("\\(", "-");
                }
                if (articulo.contains(")")) {
                    articulo = articulo.replaceAll("\\)", "-");
                }
                articulos = articulos + articulo + "****";
            }
            // Set new filter for the "Name" column
            String[] params = articulos.split("\\*\\*\\*\\*");
            // Set new filter for the "Name" column
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("productoFin", filtro.toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
            entra = true;
        }

        if (cbGgnCompra.getValue() != null && !cbGgnCompra.getValue().equals("Todos")) {
            filter = new FiltroContainer("ggnFin", ((String) cbGgnCompra.getValue()).toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
            entra = true;
        }

        if (cbNombresCompras.getValue() != null && !cbNombresCompras.getValue().equals("Todos")) {
            filter = new FiltroContainer("nombreDescriptivo", ((String) cbNombresCompras.getValue()).toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsCalidadCompra.getItemIds()).size() != 0) {
            List<String> lCalidades = (List<String>) lsCalidadCompra.getItemIds();
            String calidades = "";
            for (String calidad : lCalidades) {
                calidades = calidades + calidad + " ";
            }
            // Set new filter for the "Name" column
            String[] params = calidades.split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("calidadFin", filtro.toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
            entra = true;
        }

        if (cargarVentas) {
            cargarVentas();
        } else {
            if (!entra) {
                //cargarCompras();
            }
        }
        if (!aplicaFiltros()) {
            cargarCompras(false);
            cargarVentas();
            aplicarFiltroVentas(false);
        }

        if (!entra && !cargarVentas && fechaDesdeCompra.getValue() == null && fechaHastaCompra.getValue() == null && fechaDesdeVenta.getValue() == null && fechaHastaVenta.getValue() == null) {
            filter = new FiltroContainer("asdf", "aaaa", status);
            bcVentas.addContainerFilter(filter);
            bcCompras.addContainerFilter(filter);
            primerFiltro = true;
        }

        calcularTotales();

    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    @SuppressWarnings("unchecked")
    private void aplicarFiltroVentas(Boolean cargarCompras) {
        boolean entra = false;

        bcVentas.removeAllContainerFilters();
        if (primerFiltro) {
            bcCompras.removeAllContainerFilters();
            primerFiltro = false;
        }

        if (((List<String>) lsFamiliasVentas.getItemIds()).size() != 0) {
            List<String> lFamilias = (List<String>) lsFamiliasVentas.getItemIds();
            String familias = "";
            for (String fam : lFamilias) {
                if (fam.contains("(")) {
                    fam = fam.replaceAll("\\(", "-");
                }
                if (fam.contains(")")) {
                    fam = fam.replaceAll("\\)", "-");
                }
                familias = familias + fam + "****";
            }
            // Set new filter for the "Name" column
            String[] params = familias.split("\\*\\*\\*\\*");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("variedadFin", filtro.toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsArticulosVentas.getItemIds()).size() != 0) {
            List<String> lArticulos = (List<String>) lsArticulosVentas.getItemIds();
            String articulos = "";
            for (String art : lArticulos) {
                if (art.contains("(")) {
                    art = art.replaceAll("\\(", "-");
                }
                if (art.contains(")")) {
                    art = art.replaceAll("\\)", "-");
                }
                articulos = articulos + art + "****";
            }
            // Set new filter for the "Name" column
            String[] params = articulos.split("\\*\\*\\*\\*");
            // Set new filter for the "Name" column
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("familiaFin", filtro.toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
            entra = true;
        }

        if (((List<TPais>) lsPaisesVentas.getItemIds()).size() != 0) {
            List<String> lsPaises = (List<String>) lsPaisesVentas.getItemIds();
            String paises = "";
            for (String pais : lsPaises) {
                if (pais.contains("(")) {
                    pais = pais.replaceAll("\\(", "-");
                }
                if (pais.contains(")")) {
                    pais = pais.replaceAll("\\)", "-");
                }
                paises = paises + pais + "****";
            }
            // Set new filter for the "Name" column
            String[] params = paises.split("\\*\\*\\*\\*");
            // Set new filter for the "Name" column
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }

            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("origenFin", filtro.toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsClientes.getItemIds()).size() != 0) {
            List<String> lClientes = (List<String>) lsClientes.getItemIds();
            String clientes = "";
            for (String cliente : lClientes) {
                clientes = clientes + cliente + "  ";
            }
            // Set new filter for the "Name" column
            String[] params = clientes.split("  ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("clienteFin", filtro.toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsAlbaranesVentas.getItemIds()).size() != 0) {
            List<String> lAlbaranes = (List<String>) lsAlbaranesVentas.getItemIds();
            String albaranes = "";
            for (String fam : lAlbaranes) {
                albaranes = albaranes + fam + " ";
            }
            // Set new filter for the "Name" column
            String[] params = albaranes.split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("albaranFin", filtro.toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsLoteVentas.getItemIds()).size() != 0) {
            List<String> lLotes = (List<String>) lsLoteVentas.getItemIds();
            String lotes = "";
            for (String lote : lLotes) {
                lotes = lotes + lote + " ";
            }
            // Set new filter for the "Name" column
            String[] params = lotes.split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("loteFin", filtro.toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsProveedoresVentas.getItemIds()).size() != 0) {
            List<String> lProveedores = (List<String>) lsProveedoresVentas.getItemIds();
            String proveedores = "";
            for (String proveedor : lProveedores) {
                proveedores = proveedores + proveedor + " ";
            }
            // Set new filter for the "Name" column
            String[] params = proveedores.split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("proveedorFin", filtro.toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsCalidadVentas.getItemIds()).size() != 0) {
            List<String> lCalidades = (List<String>) lsCalidadVentas.getItemIds();
            String calidades = "";
            for (String calidad : lCalidades) {
                calidades = calidades + calidad + " ";
            }
            // Set new filter for the "Name" column
            String[] params = calidades.split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("calidadVentaFin", filtro.toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
            entra = true;
        }

        if (((List<String>) lsPedidosVentas.getItemIds()).size() != 0) {
            List<String> lPedidos = (List<String>) lsPedidosVentas.getItemIds();
            String pedidos = "";
            for (String pedido : lPedidos) {
                pedidos = pedidos + pedido + " ";
            }
            // Set new filter for the "Name" column
            String[] params = pedidos.split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + params[i] + "|";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("pedidoFin", filtro.toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
            entra = true;
        }

        if (cbNombresVentas.getValue() != null && !cbNombresVentas.getValue().equals("Todos")) {
            filter = new FiltroContainer("nombreDescriptivo", ((String) cbNombresVentas.getValue()).toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
            entra = true;
        }

        if (cargarCompras) {
            cargarCompras(true);
        }
        if (!entra && !cargarCompras && fechaDesdeCompra.getValue() == null && fechaHastaCompra.getValue() == null && fechaDesdeVenta.getValue() == null && fechaHastaVenta.getValue() == null) {
            filter = new FiltroContainer("asdf", "aaaa", status);
            bcVentas.addContainerFilter(filter);
            bcCompras.addContainerFilter(filter);
            primerFiltro = true;
        }

        calcularTotales();
    }

    /**
     * Método que nos indica si se están aplicando o no filtros.
     * @return true --> Si está aplicando algún filtro, false en caso contrario.
     */
    private boolean aplicaFiltros() {
        boolean result = true;

        // Comprobamos si está filtrando por algún campo.
        // Empezamos por las fechas
        if (fechaDesdeCompra.getValue() == null && fechaHastaCompra.getValue() == null && fechaDesdeVenta.getValue() == null || fechaHastaVenta.getValue() == null) {
            // Albranes 
            if (lsAlbaranesCompras.getItemIds().size() == 0 && lsAlbaranesVentas.getItemIds().size() == 0 && lsPartidasCompra.getItemIds().size() == 0 && lsPedidosVentas.getItemIds().size() == 0) {
                // Artículos
                if (lsArticulosCompras.getItemIds().size() == 0 && lsArticulosVentas.getItemIds().size() == 0) {
                    // Calidad
                    if (lsCalidadCompra.getItemIds().size() == 0 && lsCalidadVentas.getItemIds().size() == 0) {
                        // Cliente / Proveedor
                        if (lsClientes.getItemIds().size() == 0 && lsProveedoresCompras.getItemIds().size() == 0 && lsProveedoresVentas.getItemIds().size() == 0) {
                            // Familias
                            if (lsFamiliasCompras.getItemIds().size() == 0 && lsFamiliasVentas.getItemIds().size() == 0) {
                                // Lotes
                                if (lsLoteCompras.getItemIds().size() == 0 && lsLoteVentas.getItemIds().size() == 0) {
                                    // Paises
                                    if (lsPaisesCompra.getItemIds().size() == 0 && lsPaisesVentas.getItemIds().size() == 0) {
                                        result = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Retornamos el valor de la consulta de aplicación de filtros.
        return result;
    }

    /**
     * Método que nos indica si está filtrando por algún campo de venta.
     * @return True si está filtrando por algún campo de venta, false en caso contrario.
     */
    private boolean aplicaFiltrosVentas() {
        boolean result = true;
        // Comprobamos si está filtrando por algún campo.
        // Empezamos por las fechas
        if (fechaDesdeVenta.getValue() == null || fechaHastaVenta.getValue() == null) {
            // Albranes 
            if (lsAlbaranesVentas.getItemIds().size() == 0 && lsPedidosVentas.getItemIds().size() == 0) {
                // Artículos
                if (lsArticulosVentas.getItemIds().size() == 0) {
                    // Calidad
                    if (lsCalidadVentas.getItemIds().size() == 0) {
                        // Cliente / Proveedor
                        if (lsClientes.getItemIds().size() == 0 && lsProveedoresVentas.getItemIds().size() == 0) {
                            // Familias
                            if (lsFamiliasVentas.getItemIds().size() == 0) {
                                // Lotes
                                if (lsLoteVentas.getItemIds().size() == 0) {
                                    // Paises
                                    if (lsPaisesVentas.getItemIds().size() == 0) {
                                        result = false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Método que nos muestra las compras en función de las ventas que se están mostrando
     * Obtendremos los Ids de las líneas de compra de las ventas para mostrar las compras
     */
    private void cargarCompras(Boolean vieneDeVentas) {

        if (aplicaFiltros()) {

            // En primer lugar, obtenemos las líneas de venta que se están mostrando en el listado de ventas.
            List<String> lBeansVentas = bcVentas.getItemIds();
            List<String> lVentas = Utils.generarListaGenerica();
            BeanItem<TVentasVista> bRes = null;
            TVentasVista res = null;
            for (String id : lBeansVentas) {
                bRes = (BeanItem<TVentasVista>) bcVentas.getItem(id);
                res = bRes.getBean();
                if (!lVentas.contains(res.getLoteFin())) {
                    lVentas.add(res.getLoteFin());
                }
            }
            try {

                // Obtenemos las líneas de venta a partir de las compras.
                bcCompras.removeAllItems();
                TreeMap<String, List<TComprasVista>> mVentasAux = contrVista.obtenerComprasAlbaranCompra(lVentas, user, time);

                Collection<List<TComprasVista>> lValues = mVentasAux.values();

                List<TComprasVista> lCompras = Utils.generarListaGenerica();
                for (List<TComprasVista> lista : lValues) {

                    for (TComprasVista compra : lista) {
                        lCompras.add(compra);
                    }
                }
                bcCompras.addAll(lCompras);
                aplicarFiltroCompras(false, vieneDeVentas);

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
        } else {
            List<Date> lDates = Utils.generarListaGenerica();
            List<TComprasVista> lAux = Utils.generarListaGenerica();
            lDates.addAll(mCompras.keySet());
            List<TComprasVista> lCompras = Utils.generarListaGenerica();
            // Cargamos los datos de las compras
            for (Date key : lDates) {
                lAux = (mCompras.get(key));
                for (TComprasVista res : lAux) {
                    lCompras.add(res);
                }
            }

            // Cargamos los datos de las ventas
            lDates = Utils.generarListaGenerica();
            lDates.addAll(mVentas.keySet());

            bcCompras.removeAllItems();
            bcCompras.addAll(lCompras);

        }
    }

    /**
     * Método que se encarga de cargar las ventas en función de las compras que se están mostrando
     * Hay que tener en cuenta lo siguiente:
     * - Las ventas se cargan a partir del ID de la linea de compra
     */
    @SuppressWarnings("unchecked")
    private void cargarVentas() {

        // Vamos a emplear un árbol con clave ID de linea de compra, y valor, listado de lineas de venta.
        // En primer lugar, obtenemos las líneas de compra que se están mostrando en el listado de compras.
        List<String> lBeansCompras = bcCompras.getItemIds();
        List<String> lCompras = Utils.generarListaGenerica();
        BeanItem<TComprasVista> bRes = null;
        TComprasVista res = null;
        for (String id : lBeansCompras) {
            bRes = (BeanItem<TComprasVista>) tablaCompras.getItem(id);
            res = bRes.getBean();
            if (!lCompras.contains(res.getLoteFin())) {
                lCompras.add(res.getLoteFin());
            }
        }
        try {

            // Obtenemos las líneas de venta a partir de las compras.
            bcVentas.removeAllItems();
            TreeMap<String, List<TVentasVista>> mVentasAux = contrVista.obtenerVentasAlbaranCompra(lCompras, user, time);

            Collection<List<TVentasVista>> lValues = mVentasAux.values();

            List<TVentasVista> lVentas = Utils.generarListaGenerica();
            for (List<TVentasVista> lista : lValues) {

                for (TVentasVista venta : lista) {
                    lVentas.add(venta);
                }
            }
            bcVentas.addAll(lVentas);
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
     * Método para cambiar el rango de fechas para mostrar los datos de resultados.
     */
    private void cambiarFechasCompras() {
        try {
            Date f1 = fechaDesdeCompra.getValue();
            Date f2 = fechaHastaCompra.getValue();

            // Compras
            List<TComprasVista> lResultados = Utils.generarListaGenerica();

            if (f1 == null && f2 == null) {
                bcCompras.removeAllItems();

                mCompras = contrVista.obtenerComprasGlobales(user, time);

                List<Date> lDates = Utils.generarListaGenerica();
                List<TComprasVista> lAux = Utils.generarListaGenerica();
                lDates.addAll(mCompras.keySet());

                for (Date key : lDates) {
                    lAux = (mCompras.get(key));
                    for (TComprasVista res : lAux) {
                        lResultados.add(res);
                    }
                }
            } else {
                SortedMap<Date, List<TComprasVista>> mAux = null;
                if (f1 != null && f2 == null) {
                    // El true quiere decir que se incluye también el valor de f1
                    mAux = mCompras.tailMap(f1, true);
                } else if (f1 == null) {
                    mAux = mCompras.headMap(f2, true);
                } else {
                    mAux = mCompras.subMap(f1, true, f2, true);
                }

                List<Date> lFechas = Utils.generarListaGenerica();
                lFechas.addAll(mAux.keySet());
                List<TComprasVista> lAux = null;
                for (Date key : lFechas) {
                    lAux = mAux.get(key);
                    for (TComprasVista record : lAux) {
                        lResultados.add(record);
                    }
                }
            }
            bcCompras.removeAllItems();
            bcCompras.addAll(lResultados);
            // Comprobamos si está filtrando por compras o ventas.
            // if (aplicaFiltrosVentas()) {
            aplicarFiltroVentas(false);
            //  } else {
            //      aplicarFiltroCompras(false);
            //  }

            cargarVentas();

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
     * Método para cambiar el rango de fechas para mostrar los datos de resultados.
     */
    private void cambiarFechasVentas() {
        try {
            Date f1 = fechaDesdeVenta.getValue();
            Date f2 = fechaHastaVenta.getValue();

            // Ventas
            List<TVentasVista> lResultados = Utils.generarListaGenerica();

            if (f1 == null && f2 == null) {
                bcVentas.removeAllItems();

                mVentas = contrVista.obtenerVentasGlobales(user, time);

                List<Date> lDates = Utils.generarListaGenerica();
                List<TVentasVista> lAux = Utils.generarListaGenerica();
                lDates.addAll(mVentas.keySet());

                for (Date key : lDates) {
                    lAux = (mVentas.get(key));
                    for (TVentasVista res : lAux) {
                        lResultados.add(res);
                    }
                }
            } else {
                SortedMap<Date, List<TVentasVista>> mAux = null;
                if (f1 != null && f2 == null) {
                    // El true quiere decir que se incluye también el valor de f1
                    mAux = mVentas.tailMap(f1, true);
                } else if (f1 == null) {
                    mAux = mVentas.headMap(f2, true);
                } else {
                    mAux = mVentas.subMap(f1, true, f2, true);
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
            bcVentas.removeAllItems();
            bcVentas.addAll(lResultados);
            // Comprobamos si está filtrando por compras o ventas.
            if (aplicaFiltrosVentas()) {
                aplicarFiltroVentas(false);
            } else {
                aplicarFiltroCompras(false, true);
            }

            cargarCompras(true);

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

    @SuppressWarnings("unchecked")
    private void guardarRegistrosTablasExportacion() throws GenasoftException {

        List<String> lIds = Utils.generarListaGenerica();
        List<Integer> lIdsRecords = Utils.generarListaGenerica();
        if (tablaCompras.isVisible() && !tablaCompras.getItemIds().isEmpty()) {
            lIds.addAll((Collection<? extends String>) tablaCompras.getItemIds());
            for (String id : lIds) {
                lIdsRecords.add(Integer.valueOf(id));
            }
        }

        List<Double> lValores = Utils.generarListaGenerica();
        // Si se está visualizando los totales, lo guardamos.
        if (tablaComprasTotales.isVisible()) {
            // Obtenemos los totales.
            BeanItem<TComprasVista> bRes = null;
            TComprasVista res = null;
            bRes = (BeanItem<TComprasVista>) tablaComprasTotales.getItem("1");
            res = bRes.getBean();
            lValores.add(res.getPesoNeto());
            lValores.add(res.getKgsDisponibles());
        }

        // Guardamos la lista de compras que se está visualizando para construir posteriormente el fichero Excel/PDF
        contrVista.guardarComprasListadoTrazabilidades(lIdsRecords, user, time);
        // Guardamos los totales
        contrVista.guardarTotalesComprasListadoTrazabilidades(lValores, user, time);

        lIds.clear();

        if (tablaVentas.isVisible() && !tablaVentas.getItemIds().isEmpty()) {
            lIds.addAll((Collection<? extends String>) tablaVentas.getItemIds());
            List<Integer> lIdsCompras = Utils.generarListaGenerica();
            for (String id : lIds) {
                lIdsCompras.add(Integer.valueOf(id));
            }
        }

        lIds.addAll((Collection<? extends String>) tablaVentas.getItemIds());
        List<Integer> lIdsPedidos = Utils.generarListaGenerica();
        for (String id : lIds) {
            lIdsPedidos.add(Integer.valueOf(id));
        }

        lValores = Utils.generarListaGenerica();
        // Si se está visualizando los totales, lo guardamos.
        if (tablaVentasTotales.isVisible()) {
            // Obtenemos los totales.
            BeanItem<TVentasVista> bRes = null;
            TVentasVista res = null;
            bRes = (BeanItem<TVentasVista>) tablaVentasTotales.getItem("1");
            res = bRes.getBean();
            lValores.add(Double.valueOf(res.getNumBultos()));
            lValores.add(Double.valueOf(res.getNumBultosPale()));
            lValores.add(res.getKgs());
            lValores.add(res.getKgsNetos());
        }

        // Guardamos la lista de ventas que se está visualizando para construir posteriormente el fichero Excel/PDF
        contrVista.guardarVentasListadoTrazabilidades(lIdsPedidos, user, time);
        // Guardamos los totales
        contrVista.guardarTotalesVentasListadoTrazabilidades(lValores, user, time);

        // Guardamos los Filtros

    }

    @SuppressWarnings("unchecked")
    private void guardarFiltrosAplicados() throws GenasoftException {
        Map<Integer, List<String>> mFiltros = new HashMap<Integer, List<String>>();

        if (lsAlbaranesVentas.getItemIds().size() != 0) {
            mFiltros.put(PARTIDA_COMPRA, (List<String>) lsPartidasCompra.getItemIds());
        }
        if (lsAlbaranesCompras.getItemIds().size() != 0) {
            mFiltros.put(ALBARAN_COMPRA, (List<String>) lsAlbaranesCompras.getItemIds());
        }
        if (lsPedidosVentas.getItemIds().size() != 0) {
            mFiltros.put(PEDIDO_VENTA, (List<String>) lsPedidosVentas.getItemIds());
        }
        if (lsAlbaranesVentas.getItemIds().size() != 0) {
            mFiltros.put(ALBARAN_VENTA, (List<String>) lsAlbaranesVentas.getItemIds());
        }
        if (lsArticulosCompras.getItemIds().size() != 0) {
            mFiltros.put(ARTICULO_COMPRA, (List<String>) lsArticulosCompras.getItemIds());
        }
        if (lsArticulosVentas.getItemIds().size() != 0) {
            mFiltros.put(ARTICULO_VENTA, (List<String>) lsArticulosVentas.getItemIds());
        }
        if (lsFamiliasCompras.getItemIds().size() != 0) {
            mFiltros.put(FAMILIA_COMPRA, (List<String>) lsFamiliasCompras.getItemIds());
        }
        if (lsFamiliasVentas.getItemIds().size() != 0) {
            mFiltros.put(FAMILIA_VENTA, (List<String>) lsFamiliasVentas.getItemIds());
        }
        if (lsPaisesCompra.getItemIds().size() != 0) {
            mFiltros.put(ORIGEN_COMPRA, (List<String>) lsPaisesCompra.getItemIds());
        }
        if (lsPaisesVentas.getItemIds().size() != 0) {
            mFiltros.put(ORIGEN_VENTA, (List<String>) lsPaisesVentas.getItemIds());
        }
        if (lsProveedoresCompras.getItemIds().size() != 0) {
            mFiltros.put(PROVEEDOR_COMPRA, (List<String>) lsProveedoresCompras.getItemIds());
        }
        if (lsProveedoresVentas.getItemIds().size() != 0) {
            mFiltros.put(PROVEEDOR_VENTA, (List<String>) lsProveedoresVentas.getItemIds());
        }
        if (lsClientes.getItemIds().size() != 0) {
            mFiltros.put(CLIENTE, (List<String>) lsClientes.getItemIds());
        }
        if (lsLoteCompras.getItemIds().size() != 0) {
            mFiltros.put(LOTE_COMPRA, (List<String>) lsLoteCompras.getItemIds());
        }
        if (lsLoteVentas.getItemIds().size() != 0) {
            mFiltros.put(LOTE_VENTA, (List<String>) lsLoteVentas.getItemIds());
        }
        if (cbGgnCompra.getValue() != null) {
            List<String> lGgn = Utils.generarListaGenerica();
            lGgn.add((String) cbGgnCompra.getValue());
            mFiltros.put(GGN, lGgn);
        }
        if (lsCalidadCompra.getItemIds().size() != 0) {
            mFiltros.put(CALIDAD_COMPRA, (List<String>) lsCalidadCompra.getItemIds());
        }
        if (lsCalidadVentas.getItemIds().size() != 0) {
            mFiltros.put(CALIDAD_VENTA, (List<String>) lsCalidadVentas.getItemIds());
        }
        if (fechaDesdeCompra.getValue() != null) {
            List<String> lFechas = Utils.generarListaGenerica();
            lFechas.add(new SimpleDateFormat("dd/MM/yyyy").format(fechaDesdeCompra.getValue()));
            mFiltros.put(FECHA_DESDE_COMPRA, lFechas);
        }
        if (fechaHastaCompra.getValue() != null) {
            List<String> lFechas = Utils.generarListaGenerica();
            lFechas.add(new SimpleDateFormat("dd/MM/yyyy").format(fechaHastaCompra.getValue()));
            mFiltros.put(FECHA_HASTA_COMPRA, lFechas);
        }
        if (fechaDesdeVenta.getValue() != null) {
            List<String> lFechas = Utils.generarListaGenerica();
            lFechas.add(new SimpleDateFormat("dd/MM/yyyy").format(fechaDesdeVenta.getValue()));
            mFiltros.put(FECHA_DESDE_VENTA, lFechas);
        }
        if (fechaHastaVenta.getValue() != null) {
            List<String> lFechas = Utils.generarListaGenerica();
            lFechas.add(new SimpleDateFormat("dd/MM/yyyy").format(fechaHastaVenta.getValue()));
            mFiltros.put(FECHA_HASTA_VENTA, lFechas);
        }

        contrVista.guardarFiltrosTrazabilidades(mFiltros, user, time);

    }

    @SuppressWarnings("unchecked")
    private void comprobarFiltrosAplicados() {
        String filtr = "Filtros: ";
        List<String> lFiltros = Utils.generarListaGenerica();
        if (lsPartidasCompra.getItemIds().size() != 0) {
            filtr = filtr.concat("- Partida compra: ");
            lFiltros = (List<String>) lsPartidasCompra.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsAlbaranesCompras.getItemIds().size() != 0) {
            filtr = filtr.concat("- Albarán compra: ");
            lFiltros = (List<String>) lsAlbaranesCompras.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsPedidosVentas.getItemIds().size() != 0) {
            filtr = filtr.concat("- Pedido venta: ");
            lFiltros = (List<String>) lsPedidosVentas.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsAlbaranesVentas.getItemIds().size() != 0) {
            filtr = filtr.concat("- Albarán venta: ");
            lFiltros = (List<String>) lsAlbaranesVentas.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsArticulosCompras.getItemIds().size() != 0) {
            filtr = filtr.concat("- Producto compra: ");
            lFiltros = (List<String>) lsArticulosCompras.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsArticulosVentas.getItemIds().size() != 0) {
            filtr = filtr.concat("- Producto venta: ");
            lFiltros = (List<String>) lsArticulosVentas.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsFamiliasCompras.getItemIds().size() != 0) {
            filtr = filtr.concat("- Familia compra: ");
            lFiltros = (List<String>) lsFamiliasCompras.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsFamiliasVentas.getItemIds().size() != 0) {
            filtr = filtr.concat("- Familia venta: ");
            lFiltros = (List<String>) lsFamiliasVentas.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsPaisesCompra.getItemIds().size() != 0) {
            filtr = filtr.concat("- Orígen compra: ");
            lFiltros = (List<String>) lsPaisesCompra.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsPaisesVentas.getItemIds().size() != 0) {
            filtr = filtr.concat("- Orígen venta: ");
            lFiltros = (List<String>) lsPaisesVentas.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsProveedoresCompras.getItemIds().size() != 0) {
            filtr = filtr.concat("- Proveedor compra: ");
            lFiltros = (List<String>) lsProveedoresCompras.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsProveedoresVentas.getItemIds().size() != 0) {
            filtr = filtr.concat("- Proveedor venta: ");
            lFiltros = (List<String>) lsProveedoresVentas.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsClientes.getItemIds().size() != 0) {
            filtr = filtr.concat("- Cliente: ");
            lFiltros = (List<String>) lsClientes.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsLoteCompras.getItemIds().size() != 0) {
            filtr = filtr.concat("- Trazabilidad compra: ");
            lFiltros = (List<String>) lsLoteCompras.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsLoteVentas.getItemIds().size() != 0) {
            filtr = filtr.concat("- Trazabilidad venta: ");
            lFiltros = (List<String>) lsLoteVentas.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (cbGgnCompra.getValue() != null) {
            filtr = filtr.concat("- GGN: ");
            String filtro = (String) cbGgnCompra.getValue();
            filtro = filtro.concat(", ");
            filtr = filtr.concat(filtro);

        }
        if (lsCalidadCompra.getItemIds().size() != 0) {

            filtr = filtr.concat("- Certificación compra: ");
            lFiltros = (List<String>) lsCalidadCompra.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }
        if (lsCalidadVentas.getItemIds().size() != 0) {
            filtr = filtr.concat("- Certificación venta: ");
            lFiltros = (List<String>) lsCalidadVentas.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }

        if (filtr.equals("Filtros: ")) {
            filtr = "Sin filtros aplicados";
        } else {
            filtr = filtr.substring(0, filtr.length() - 2);
        }

        filtrosAplicados.setValue(filtr);
    }

}
