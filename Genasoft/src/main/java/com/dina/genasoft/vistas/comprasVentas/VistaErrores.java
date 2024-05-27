/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.comprasVentas;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.PatternSyntaxException;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TColumnasTablasEmpleado;
import com.dina.genasoft.db.entity.TComprasVista;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TLineasVentas;
import com.dina.genasoft.db.entity.TLineasVentasVista;
import com.dina.genasoft.db.entity.TPais;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TVentas;
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
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaErrores.NAME)
public class VistaErrores extends CustomComponent implements View ,Button.ClickListener {
    /** El nombre de la vista.*/
    public static final String                        NAME                     = "vErrores";
    /** Necesario para mostrar las compras. */
    private BeanContainer<String, TComprasVista>      bcCompras;
    /** Necesario para mostrar las compras. */
    private BeanContainer<String, TComprasVista>      bcNuevaCompra;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TVentasVista>       bcVentas;
    /** Necesario para mostrar las compras. */
    private BeanContainer<String, TComprasVista>      bcComprasTotales;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TVentasVista>       bcVentasTotales;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TLineasVentasVista> bcLineasTotales;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TLineasVentasVista> bcLineasVentas;
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas                         contrVista;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                                    quitarFiltrosButton;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                                    excelButton;
    /** El boton para aplicar los filtros indicados.*/
    private Button                                    aplicarFiltroButton;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                                    pdfButton;
    /** El boton para crear una empresa.*/
    private Button                                    guardarColumnasComprasButton;
    /** El boton para crear una empresa.*/
    private Button                                    guardarColumnasVentasButton;
    /** El boton para crear una empresa.*/
    private Button                                    guardarBasuraButton;
    /** El boton para guardar los cambios introducidos.*/
    private Button                                    incluirCompraButton;
    /** El boton para guardar los cambios introducidos.*/
    private Button                                    incluirVentaButton;
    /** El boton para guardar los cambios introducidos.*/
    private Button                                    borrarCompraButton;
    /** El boton para guardar los cambios introducidos.*/
    private Button                                    eliminarLineaCompraButton;
    /** El boton para guardar los cambios introducidos.*/
    private Button                                    eliminarLineaVentaButton;
    /** El boton para guardar los cambios introducidos.*/
    private Button                                    borrarVentaButton;
    /** El boton para guardar los cambios introducidos.*/
    private Button                                    guardarCompraButton;
    /** El boton para guardar los cambios introducidos.*/
    private Button                                    guardarVentaButton;
    /** El boton para guardar los cambios introducidos.*/
    private Button                                    guardarButton;
    /** El boton para guardar los cambios introducidos.*/
    private Button                                    corregirErroresButton;
    // Elementos para realizar busquedas
    /** Para filtrar por estado. */
    private ComboBox                                  fModificarCampos;
    /** Para filtrar por estado. */
    private ComboBox                                  fDatosMostrar;
    /** Para filtrar por estado. */
    private ComboBox                                  fMostrarTabla;
    /** Para indicar qué tipo de filtro queremos aplicar. */
    private ComboBox                                  fTipoFiltro;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                                    appName;
    /** El ID del cliente seleccionado.*/
    private String                                    idSeleccionadoCompras;
    /** El ID del cliente seleccionado.*/
    private String                                    idSeleccionadoVentas;
    /** El ID del cliente seleccionado.*/
    private String                                    idSeleccionadoLineaVentas;
    /** El ID del cliente seleccionado.*/
    private String                                    idSeleccionadoNCompra;
    /** El ID del cliente seleccionado.*/
    private String                                    idSeleccionadoNVenta;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger             log                      = org.slf4j.LoggerFactory.getLogger(VistaErrores.class);
    /** El usuario que está logado. */
    private Integer                                   user                     = null;
    /** La fecha en que se inició sesión. */
    private Long                                      time                     = null;
    /** El filtro que se le aplica al container. */
    private FiltroContainer                           filter;
    /** No tengo ni puta idea para que sirve. */
    private final Label                               status                   = new Label("");
    /** Tabla para mostrar las compras del sistema. */
    private Table                                     tablaCompras             = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                                     tablaColores             = null;
    /** Tabla para mostrar las compras del sistema. */
    private Table                                     tablaComprasCambios      = null;
    /** Tabla para mostrar las compras del sistema. */
    private Table                                     tablaNuevaCompra         = null;
    /** Tabla para mostrar las ventas del sistema. */
    private Table                                     tablaVentas              = null;
    /** Tabla para mostrar las ventas del sistema. */
    private Table                                     tablaLineasVentas        = null;
    /** Tabla para mostrar las ventas del sistema. */
    private Table                                     tablaVentasCambios       = null;
    /** Tabla para mostrar las ventas del sistema. */
    private Table                                     tablaVentasCambiosTodos  = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                                     tablaComprasTotales      = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                                     tablaVentasTotales       = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                                     tablaLineasVentasTotales = null;
    /** Para indicar que se está filtrando por el campo nombre. */
    public TreeMap<Date, List<TComprasVista>>         mCompras                 = null;
    /** Arbol con los resultados. */
    private TreeMap<Date, List<TVentasVista>>         mVentas                  = null;
    /** Etiqueta para mostrar el texto de las tablas de compras. */
    private Label                                     textoTablaCompras;
    /** Etiqueta para mostrar el texto de las tablas de compras. */
    private Label                                     textoTablaCompras2;
    /** Etiqueta para mostrar el texto de las tablas de ventas. */
    private Label                                     textoTablaVentas;
    /** Etiqueta para mostrar el texto de las tablas de ventas. */
    private Label                                     textoTablaLineasVentas;
    /** Etiqueta para mostrar el texto de las tablas de ventas. */
    private Label                                     textoTablaVentas2;
    /** Etiqueta para mostrar el texto de las tablas de ventas. */
    private Label                                     textoTablaLineasVentas2;
    private VerticalLayout                            tCompras;
    private VerticalLayout                            tVentas;
    private VerticalLayout                            tLineas;
    /** Diccionario con las columnas que se van a mostrar en la tabla. */
    private Map<String, String>                       mColumnasGuardadoCompras;
    /** Diccionario con las columnas que se van a mostrar en la tabla. */
    private Map<String, String>                       mColumnasGuardadoVentas;
    /** Diccionario con las columnas que se van a mostrar en la tabla de compras. */
    private Map<String, String>                       mColumnasCompras;
    /** Diccionario con las columnas que se van a mostrar en la tabla de ventas. */
    private Map<String, String>                       mColumnasVentas;
    /** Diccionario con las columnas que se van a mostrar en la tabla de compras. */
    private Map<String, String>                       mColumnasIdsCompras;
    /** Diccionario con las columnas que se van a mostrar en la tabla de compras. */
    private Map<String, String>                       mColumnasIdsComprasCambios;
    /** Diccionario con las columnas que se van a mostrar en la tabla de ventas. */
    private Map<String, String>                       mColumnasIdsVentas;
    /** ComboBox para ocultar columnas en la tabla de ventas. */
    private ComboBox                                  cbMostrarControles;
    /** ComboBox para ocultar columnas en la tabla de ventas. */
    private ComboBox                                  cbMostrarFiltro;
    /** ComboBox para ocultar columnas en la tabla de ventas. */
    private ComboBox                                  cbMostrarDatosGlobales;
    /** ComboBox para ocultar columnas en la tabla de ventas. */
    private ComboBox                                  cbMostrarCambios;
    /**********                                 FILTROS                                 **********/
    /** Para filtrar por nombre descriptivo en importación de compra.  */
    private ComboBox                                  cbNombresCompras;
    /** Para filtrar por nombre descriptivo en importación de venta.  */
    private ComboBox                                  cbCerradas;
    /** Para filtrar por nombre descriptivo en importación de venta.  */
    private ComboBox                                  cbNombresVentas;
    /** Para filtrar por familia.  */
    private ComboBox                                  cbFamiliasCompras;
    /** Para filtrar por familia.  */
    private ComboBox                                  cbFamiliasVentas;
    /** Para filtrar por país.  */
    private ComboBox                                  cbPaisesCompras;
    /** Para filtrar por país.  */
    private ComboBox                                  cbPaisesVentas;
    /** Para filtrar por Producto.  */
    private ComboBox                                  cbArticulosCompras;
    /** Para filtrar por Producto.  */
    private ComboBox                                  cbArticulosVentas;
    /** Para filtrar por el tipo de proveedor. */
    private ComboBox                                  cbGgnCompra;
    /** Para filtrar por el tipo de proveedor. */
    private ComboBox                                  cbGgnVenta;
    /** Para filtrar por clientes.  */
    private ComboBox                                  cbClientes;
    /** Para filtrar por proveedores en compras.  */
    private ComboBox                                  cbProveedoresCompras;
    /** Para filtrar por proveedores en ventas.  */
    private ComboBox                                  cbProveedoreVentas;
    /** Para filtrar por albarán de compra.  */
    private ComboBox                                  cbTipoErroresVenta;
    /** Para filtrar por albarán de compra.  */
    private ComboBox                                  cbAlbaranCompra;
    /** Para filtrar por albarán de venta.  */
    private ComboBox                                  cbAlbaranVenta;
    /** Para filtrar por albarán de venta.  */
    private ComboBox                                  cbPedidoVenta;
    /** Para filtrar por calidad de compra.  */
    private ComboBox                                  cbCalidadCompra;
    /** Para filtrar por calidad de venta.  */
    private ComboBox                                  cbCalidadVenta;
    /** Para filtrar por calidad de compra.  */
    private ComboBox                                  cbLoteCompra;
    /** Para filtrar por calidad de venta.  */
    private ComboBox                                  cbLoteVenta;
    /** Para filtrar por calidad de compra.  */
    private ComboBox                                  cbPartidaCompra;
    /** Para filtrar por basura.  */
    private ComboBox                                  cbBasuras;
    /** Para filtrar por basura.  */
    private ComboBox                                  cbVariedades;
    /** La fecha desde para los informes. */
    private DateField                                 fechaDesdeCompra;
    /** La fecha hasta para los informes. */
    private DateField                                 fechaHastaCompra;
    /** La fecha desde para los informes. */
    private DateField                                 fechaDesdeVenta;
    /** La fecha hasta para los informes. */
    private DateField                                 fechaHastaVenta;
    private HorizontalLayout                          cabeceraPantalla;
    private VerticalLayout                            filtro;
    /********* LISTAS PARA FILTROS *********/
    /** Lista con las nombres de importaciones en las compras. */
    private List<String>                              lNombresCompras;
    /** Lista con las nombres de importaciones en las ventas. */
    private List<String>                              lNombresVentas;
    /** Lista con las familias identificadas en las compras. */
    private List<String>                              lFamiliasCompras;
    /** Lista con las familias identificadas en las ventas. */
    private List<String>                              lFamiliasVentas;
    /** Lista con los paises identificadas en las compras. */
    private List<String>                              lPaisesCompras;
    /** Lista con los paises identificadas en las ventas. */
    private List<String>                              lPaisesVentas;
    /** Lista con los Productos identificadas en las compras. */
    private List<String>                              lArticulosCompras;
    /** Lista con los Productos identificadas en las ventas. */
    private List<String>                              lArticulosVentas;
    /** Lista con los clientes identificadas en las ventas. */
    private List<String>                              lClientes;
    /** Lista con los proveedores identificadas en las compras. */
    private List<String>                              lProveedoresCompras;
    /** Lista con los proveedores identificadas en las ventas. */
    private List<String>                              lProveedoresVentas;
    /** Lista con los albaranes identificadas en las compras. */
    private List<String>                              lAlbaranesCompras;
    /** Lista con los albaranes identificadas en las ventas. */
    private List<String>                              lAlbaranesVentas;
    /** Lista con los albaranes identificadas en las ventas. */
    private List<String>                              lPedidosVentas;
    /** Lista con las calidades identificadas en las compras. */
    private List<String>                              lCalidadCompras;
    /** Lista con las calidades identificadas en las ventas. */
    private List<String>                              lCalidadVentas;
    /** Lista con los lotes identificadas en las compras. */
    private List<String>                              lLotesCompras;
    /** Lista con los lotes identificadas en las ventas. */
    private List<String>                              lLotesVentas;
    /** Lista con las partidas identificadas en las compras. */
    private List<String>                              lPartidasCompras;
    /** Lista con las partidas identificadas en las compras. */
    private List<String>                              lVariedades;
    /********* Listas para mostrar los datos por lo que se está filtrando *********/
    /** Lista familias por los que está filtrando. */
    private ListSelect                                lsFamiliasCompras;
    /** Lista familias por los que está filtrando. */
    private ListSelect                                lsFamiliasVentas;
    /** Lista paises compra por los que está filtrando. */
    private ListSelect                                lsPaisesCompra;
    /** Lista paises venta por los que está filtrando. */
    private ListSelect                                lsPaisesVentas;
    /** Lista Productos por los que está filtrando. */
    private ListSelect                                lsArticulosCompras;
    /** Lista Productos por los que está filtrando. */
    private ListSelect                                lsArticulosVentas;
    /** Lista de clientes por los que está filtrando. */
    private ListSelect                                lsClientes;
    /** Lista de Nº de albaranes por los que está filtrando. */
    private ListSelect                                lsAlbaranesCompras;
    /** Lista de Nº de albaranes por los que está filtrando. */
    private ListSelect                                lsAlbaranesVentas;
    /** Lista de Nº de albaranes por los que está filtrando. */
    private ListSelect                                lsPedidosVentas;
    /** Lista de proveedores por los que está filtrando. */
    private ListSelect                                lsProveedoresCompras;
    /** Lista de proveedores por los que está filtrando. */
    private ListSelect                                lsProveedoresVentas;
    /** Lista de lotes por los que está filtrando. */
    private ListSelect                                lsLoteCompras;
    /** Lista de lotes por los que está filtrando. */
    private ListSelect                                lsLoteVentas;
    /** Lista de lotes por los que está filtrando. */
    private ListSelect                                lsPartidasCompra;
    /** Lista de calidades por los que está filtrando. */
    private ListSelect                                lsCalidadCompra;
    /** Lista de calidades por los que está filtrando. */
    private ListSelect                                lsCalidadVentas;
    /** Lista de calidades por los que está filtrando. */
    private ListSelect                                lsTiposErrores;
    /** Lista de calidades por los que está filtrando. */
    private ListSelect                                lsVariedades;
    private VerticalLayout                            vNuevasTablas;
    private Map<String, TComprasVista>                mComprasEliminadas;
    private Map<String, TVentasVista>                 mVentasEliminadas;
    private List<String>                              lIdsComprasModificadas;
    private List<String>                              lIdsVentasModificadas;
    private Map<Integer, TVentas>                     mVentasErroneas;
    private Map<Integer, TLineasVentas>               mLineasVentasErroneas;
    private List<Integer>                             lIds;
    private Label                                     tituloTablaColores;
    private TPermisos                                 permisos;
    /** Variables que nos indica la posición del diccionario de filtros donde se encuentra el valor a filtrar.*/
    private final Integer                             ALBARAN_COMPRA           = 1;
    private final Integer                             PARTIDA_COMPRA           = 2;
    private final Integer                             PEDIDO_VENTA             = 3;
    private final Integer                             ALBARAN_VENTA            = 4;
    private final Integer                             ARTICULO_COMPRA          = 5;
    private final Integer                             ARTICULO_VENTA           = 6;
    private final Integer                             FAMILIA_COMPRA           = 7;
    private final Integer                             FAMILIA_VENTA            = 8;
    private final Integer                             ORIGEN_COMPRA            = 9;
    private final Integer                             ORIGEN_VENTA             = 10;
    private final Integer                             PROVEEDOR_COMPRA         = 11;
    private final Integer                             PROVEEDOR_VENTA          = 12;
    private final Integer                             CLIENTE                  = 13;
    private final Integer                             LOTE_COMPRA              = 14;
    private final Integer                             LOTE_VENTA               = 15;
    private final Integer                             FECHA_DESDE_COMPRA       = 16;
    private final Integer                             FECHA_HASTA_COMPRA       = 17;
    private final Integer                             FECHA_DESDE_VENTA        = 18;
    private final Integer                             FECHA_HASTA_VENTA        = 19;
    private final Integer                             GGN                      = 20;
    private final Integer                             CALIDAD_COMPRA           = 21;
    private final Integer                             CALIDAD_VENTA            = 22;
    private TEmpleados                                empleado;
    private HorizontalLayout                          totales;
    private Label                                     filtrosAplicados;
    private HorizontalLayout                          botones;
    /** Contendrá los lotes de las compras que se están mostrando.*/
    private List<String>                              lLotesComprasSelecc;
    private Boolean                                   filtrandoVenta;

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
                idSeleccionadoNCompra = null;
                idSeleccionadoNVenta = null;
                idSeleccionadoVentas = null;
                idSeleccionadoLineaVentas = null;

                filtrandoVenta = false;

                bcCompras = new BeanContainer<>(TComprasVista.class);
                bcCompras.setBeanIdProperty("id");

                bcNuevaCompra = new BeanContainer<>(TComprasVista.class);
                bcNuevaCompra.setBeanIdProperty("id");

                bcVentas = new BeanContainer<>(TVentasVista.class);
                bcVentas.setBeanIdProperty("idExterno");

                bcLineasVentas = new BeanContainer<>(TLineasVentasVista.class);
                bcLineasVentas.setBeanIdProperty("id");

                bcComprasTotales = new BeanContainer<>(TComprasVista.class);
                bcComprasTotales.setBeanIdProperty("id");

                bcVentasTotales = new BeanContainer<>(TVentasVista.class);
                bcVentasTotales.setBeanIdProperty("id");

                bcLineasTotales = new BeanContainer<>(TLineasVentasVista.class);
                bcLineasTotales.setBeanIdProperty("id");

                mComprasEliminadas = new HashMap<String, TComprasVista>();
                mVentasEliminadas = new HashMap<String, TVentasVista>();

                lIdsComprasModificadas = Utils.generarListaGenerica();
                lIdsVentasModificadas = Utils.generarListaGenerica();

                Label texto = new Label("Comprobación trazabilidades");
                texto.setStyleName("tituloTamano16");
                // Incluimos en el layout los componentes

                empleado = contrVista.obtenerEmpleadoPorId(user, user, time);

                permisos = contrVista.obtenerPermisosEmpleado(empleado, user, time);

                if (lLotesComprasSelecc == null) {
                    lLotesComprasSelecc = Utils.generarListaGenerica();
                } else {
                    lLotesComprasSelecc.clear();
                }

                if (permisos == null) {
                    Notification aviso = new Notification("No se ha podido identificar los permisos asignados, contacta con el administrador.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    return;
                }

                if (!Utils.booleanFromInteger(permisos.getComprasVentas())) {
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

                crearTablaColores();
                crearTablaCompras(permisos);
                crearTablaComprasCambios(permisos);
                crearTablaComprasTotales();
                crearTablaNuevaCompra();
                crearTablaVentas(permisos);
                crearTablaVentasCambios(permisos);
                crearTablaVentasCambiosTodos();
                crearTablaVentasTotales();
                crearTablaLineasVentasTotales();
                crearTablaLineasVentas();
                // Obtenemos los campos y en el orden establecido de la tabla de empleados.
                List<TColumnasTablasEmpleado> lColumnas = contrVista.obtenerCamposPantallaTablaEmpleado(user, NAME, Integer.valueOf(tablaCompras.getId()), user, time);

                // Nutrimos el diccionario para mostrar/ocultar las columnas
                nutrirDiccionarioCabeceraTablaCompras();

                // Nutrimos el diccionario para mostrar/ocultar las columnas
                nutrirDiccionarioCabeceraTablaVentas();

                mostrarColumnasTablaCompras(lColumnas);

                lColumnas = contrVista.obtenerCamposPantallaTablaEmpleado(user, NAME, Integer.valueOf(tablaComprasCambios.getId()), user, time);

                mCompras = contrVista.obtenerCompras(user, time);
                mVentas = contrVista.obtenerVentasErroneas();

                mVentasErroneas = contrVista.obtenerVentasErroneas(user, time);

                lIds = Utils.generarListaGenerica();
                lIds.addAll(mVentasErroneas.keySet());

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

                vNuevasTablas = new VerticalLayout();
                vNuevasTablas.setSpacing(true);
                vNuevasTablas.addComponent(tablaNuevaCompra);

                HorizontalLayout btnsCompra = new HorizontalLayout();
                btnsCompra.setSpacing(true);
                btnsCompra.addComponent(guardarCompraButton);
                btnsCompra.addComponent(borrarCompraButton);

                vNuevasTablas.addComponent(btnsCompra);

                HorizontalLayout btnsVenta = new HorizontalLayout();
                btnsVenta.setSpacing(true);
                btnsVenta.addComponent(guardarVentaButton);
                btnsVenta.addComponent(borrarVentaButton);

                vNuevasTablas.addComponent(btnsVenta);

                Label tituloFiltrar = new Label("Filtrar");
                tituloFiltrar.setStyleName("tituloTamano12");

                // Obtenemos los datos para crear la parte filtro.
                lFamiliasCompras = contrVista.obtenerFamiliasCompras(user, time);
                lFamiliasVentas = contrVista.obtenerFamiliasVentas(user, time);
                lPaisesCompras = contrVista.obtenerPaisesCompras(user, time);
                lPaisesVentas = contrVista.obtenerPaisesVentas(user, time);
                lArticulosCompras = contrVista.obtenerArticulosVentas(user, time);
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
                //lVariedades = contrVista.obtenerVariedades(user, time);

                lCalidadCompras = Utils.generarListaGenerica();
                lCalidadCompras.add("BIO");
                lCalidadCompras.add("CONVENCIONAL");

                lCalidadVentas = Utils.generarListaGenerica();
                lCalidadVentas.add("BIO");
                lCalidadVentas.add("CONVENCIONAL");

                botones = new HorizontalLayout();
                botones.setSpacing(true);

                // Creamos el componente filtro.
                crearComponenteFiltro();

                // Creamos la botonera.
                //HorizontalLayout botonera = crearBotonera(permisos);
                viewLayout.addComponent(cbMostrarControles);
                viewLayout.addComponent(cabeceraPantalla);
                viewLayout.addComponent(filtro);

                botones.addComponent(guardarColumnasComprasButton);
                botones.addComponent(guardarColumnasVentasButton);
                botones.addComponent(incluirCompraButton);
                botones.addComponent(incluirVentaButton);

                //HorizontalLayout botones2 = new HorizontalLayout();
                //botones2.setSpacing(true);
                //botones2.addComponent(incluirCompraButton);
                //botones2.addComponent(incluirVentaButton);

                viewLayout.addComponent(botones);
                //viewLayout.addComponent(botones2);

                tituloTablaColores = new Label("Leyenda colores");
                tituloTablaColores.setStyleName("tituloTamano12");
                viewLayout.addComponent(tituloTablaColores);
                viewLayout.addComponent(tablaColores);

                viewLayout.addComponent(vNuevasTablas);

                vNuevasTablas.setVisible(true);

                textoTablaCompras = new Label("Compras");
                textoTablaCompras.setStyleName("tituloTamano18");

                textoTablaCompras2 = new Label("Compras totales");
                textoTablaCompras2.setStyleName("tituloTamano14Center");

                textoTablaVentas = new Label("Ventas");
                textoTablaVentas.setStyleName("tituloTamano18");
                textoTablaVentas2 = new Label("Ventas totales");
                textoTablaVentas2.setStyleName("tituloTamano14Center");

                textoTablaLineasVentas = new Label("Trazabilidades");
                textoTablaLineasVentas.setStyleName("tituloTamano18");
                textoTablaLineasVentas2 = new Label("Trazabilidades totales");
                textoTablaLineasVentas2.setStyleName("tituloTamano14Center");

                HorizontalLayout hor1 = new HorizontalLayout();
                hor1.setSpacing(true);
                hor1.addComponent(guardarBasuraButton);
                hor1.addComponent(eliminarLineaCompraButton);
                hor1.addComponent(botones);

                tCompras = new VerticalLayout();
                tCompras.setSpacing(true);
                tCompras.addComponent(textoTablaCompras);
                tCompras.setComponentAlignment(textoTablaCompras, Alignment.TOP_CENTER);
                tCompras.addComponent(tablaComprasTotales);
                tCompras.setComponentAlignment(tablaComprasTotales, Alignment.TOP_CENTER);
                tCompras.addComponent(hor1);
                tCompras.addComponent(tablaCompras);
                tCompras.addComponent(tablaComprasCambios);

                tVentas = new VerticalLayout();
                tVentas.setSpacing(true);
                tVentas.addComponent(textoTablaVentas);
                tVentas.setComponentAlignment(textoTablaVentas, Alignment.TOP_CENTER);
                tVentas.addComponent(tablaVentasTotales);
                tVentas.setComponentAlignment(tablaVentasTotales, Alignment.TOP_CENTER);
                tVentas.addComponent(tablaVentas);
                tVentas.addComponent(tablaVentasCambiosTodos);
                tVentas.addComponent(tablaVentasCambios);

                tLineas = new VerticalLayout();
                tLineas.setSpacing(true);
                tLineas.addComponent(textoTablaLineasVentas);
                tLineas.setComponentAlignment(textoTablaLineasVentas, Alignment.TOP_CENTER);
                tLineas.addComponent(tablaLineasVentasTotales);
                tLineas.setComponentAlignment(tablaLineasVentasTotales, Alignment.TOP_CENTER);
                tLineas.addComponent(tablaLineasVentas);

                VerticalLayout totalesCompras = new VerticalLayout();
                totalesCompras.setSpacing(true);
                totalesCompras.addComponent(textoTablaCompras2);
                totalesCompras.addComponent(tablaComprasTotales);

                VerticalLayout totalesVentas = new VerticalLayout();
                totalesVentas.setSpacing(true);
                totalesVentas.addComponent(textoTablaVentas2);
                totalesVentas.addComponent(tablaVentasTotales);

                VerticalLayout totalesLineas = new VerticalLayout();
                totalesLineas.setSpacing(true);
                totalesLineas.addComponent(textoTablaLineasVentas2);
                totalesLineas.addComponent(tablaLineasVentasTotales);

                totales = new HorizontalLayout();
                totales.setSpacing(true);
                totales.addStyleName("posLayoutFixed");
                totales.addComponent(filtrosAplicados);
                totales.addComponent(totalesCompras);
                totales.addComponent(totalesVentas);
                totales.addComponent(totalesLineas);

                HorizontalLayout tablas = new HorizontalLayout();
                //tablas.addStyleName("posLayoutFixed");

                tablas.addComponent(tCompras);
                //tablaCompras.setWidth(150, Sizeable.Unit.EM);
                tablas.addComponent(tVentas);
                //tablaVentas.setWidth(150, Sizeable.Unit.EM);
                tablas.addComponent(tLineas);
                tablas.setSizeUndefined();

                HorizontalLayout horBot = new HorizontalLayout();
                horBot.setSpacing(true);
                //horBot.addComponent(aplicarFiltroButton);
                horBot.addComponent(quitarFiltrosButton);
                horBot.addComponent(guardarButton);
                horBot.addComponent(corregirErroresButton);
                horBot.addComponent(excelButton);
                //horBot.addComponent(pdfButton);

                VerticalLayout filtros = new VerticalLayout();
                filtros.setSpacing(true);
                filtros.addComponent(filtrosAplicados);
                filtros.addStyleName("posLayoutFixed");

                Label lbl = new Label(" ");
                lbl.setHeight(4, Sizeable.Unit.EM);

                Label lbl2 = new Label(" ");
                lbl2.setHeight(4, Sizeable.Unit.EM);

                //viewLayout.addComponent(tabTotales);
                viewLayout.addComponent(horBot);
                viewLayout.addComponent(filtros);
                viewLayout.addComponent(lbl2);
                viewLayout.addComponent(totales);
                viewLayout.addComponent(lbl);
                viewLayout.addComponent(tablas);

                /** VerticalLayout v = new VerticalLayout();
                v.setSpacing(true);
                v.addComponent(textoTablaLineasVentas);
                v.addComponent(tablaLineasVentasCambiosTodos);
                v.addComponent(tablaLineasVentas);
                v.addComponent(tablaLineasVentasCambios);
                
                tablas.addComponent(v);
                */
                // Añadimos el logo del cliente
                viewLayout.addComponent(contrVista.logoCliente());
                setCompositionRoot(viewLayout);
                // Establecemos el porcentaje de ratio para los layouts
                //viewLayout.setExpandRatio(titulo, 0.1f);
                viewLayout.setMargin(true);
                viewLayout.setSpacing(true);

                calcularTotales();
                tablaColores.setVisible(false);
                tituloTablaColores.setVisible(false);
                tablaNuevaCompra.setVisible(false);
                guardarCompraButton.setVisible(false);
                borrarCompraButton.setVisible(false);
                tablaComprasCambios.setVisible(false);
                tablaVentasCambiosTodos.setVisible(false);
                tablaVentasCambios.setVisible(false);
                textoTablaLineasVentas.setVisible(false);
                tablaLineasVentas.setVisible(false);

                guardarVentaButton.setVisible(false);
                borrarVentaButton.setVisible(false);

                cbMostrarDatosGlobales.setValue("Mostrar");
                cbMostrarCambios.setValue("Mostrar");
                fDatosMostrar.setVisible(false);
                fDatosMostrar.setValue("Todos");
                tablaLineasVentas.setVisible(false);

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
        aplicarFiltroButton = new Button("Aplicar filtros", this);
        incluirCompraButton = new Button("Añadir línea compra", this);
        borrarCompraButton = new Button("Borrar línea compra", this);
        incluirVentaButton = new Button("Añadir línea venta", this);
        borrarVentaButton = new Button("Borrar línea venta", this);
        eliminarLineaCompraButton = new Button("Eliminar línea listado compra", this);
        eliminarLineaVentaButton = new Button("Eliminar línea listado venta", this);
        eliminarLineaVentaButton.setVisible(false);
        guardarBasuraButton = new Button("Crear basura", this);
        guardarButton = new Button("Guardar cambios", this);
        quitarFiltrosButton = new Button("Quitar todos los filtros", this);
        guardarCompraButton = new Button("Guardar línea compra", this);
        guardarVentaButton = new Button("Guardar línea venta", this);
        corregirErroresButton = new Button("Corregir errores", this);
        corregirErroresButton.setVisible(false);

        excelButton = new Button("", this);
        excelButton.setIcon(new ThemeResource("icons/excel-32.ico"));
        excelButton.setStyleName(BaseTheme.BUTTON_LINK);

        pdfButton = new Button("", this);
        pdfButton.setIcon(new ThemeResource("icons/pdf-32.ico"));
        pdfButton.setStyleName(BaseTheme.BUTTON_LINK);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void buttonClick(ClickEvent event) {

        if (event.getButton().equals(guardarColumnasComprasButton)) {
            guardarOrdenColumnasCompras();
        } else if (event.getButton().equals(guardarColumnasVentasButton)) {
            guardarOrdenColumnasVentas();
        } else if (event.getButton().equals(aplicarFiltroButton)) {
            Boolean entra = false;
            if (aplicaFiltrosVentas()) {
                entra = true;
                aplicarFiltroVentas(true);
            }
            if (aplicaFiltrosCompras()) {
                aplicarFiltroCompras(!entra, entra);
                entra = true;
            }
            if (cbBasuras.getValue() != null && !cbBasuras.getValue().toString().equals("Todo") && !cbBasuras.getValue().toString().isEmpty()) {
                String filtr = "";

                if (cbBasuras.getValue().toString().equals("Sí")) {
                    filtr = ".*(Sí).*";
                } else {
                    filtr = ".*(No).*";
                }
                filter = new FiltroContainer("indBasura", filtr.toLowerCase(), status);
                bcVentas.addContainerFilter(filter);
            }

            aplicarFiltroLineasVentas();

            calcularTotales();

        } else if (event.getButton().equals(incluirCompraButton)) {
            if (!tablaNuevaCompra.isVisible()) {
                tablaNuevaCompra.setVisible(true);
                guardarCompraButton.setVisible(true);
                borrarCompraButton.setVisible(true);
            }
            TComprasVista aux = new TComprasVista();
            aux.setId("" + tablaNuevaCompra.getItemIds().size() + 1);
            bcNuevaCompra.addBean(aux);
            tablaNuevaCompra.setPageLength(tablaNuevaCompra.getItemIds().size());
        } else if (event.getButton().equals(guardarCompraButton)) {
            guardarLineaCompra();
        } else if (event.getButton().equals(borrarCompraButton)) {
            if (idSeleccionadoNCompra == null || idSeleccionadoNCompra.isEmpty()) {
                Notification aviso = new Notification("Se debe seleccionar el registro a eliminar de la tabla", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            } else {
                tablaNuevaCompra.removeItem(idSeleccionadoNCompra);
            }
        } else if (event.getButton().equals(borrarVentaButton)) {
            if (idSeleccionadoNVenta == null || idSeleccionadoNVenta.isEmpty()) {
                Notification aviso = new Notification("Se debe seleccionar el registro a eliminar de la tabla", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }
        } else if (event.getButton().equals(eliminarLineaCompraButton)) {
            if (idSeleccionadoCompras == null || idSeleccionadoCompras.isEmpty()) {
                Notification aviso = new Notification("Se deben seleccionar los registros a eliminar del listado de compras", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            } else {
                BeanItem<TComprasVista> bRes = null;
                TComprasVista res = null;

                String[] ids;
                if (idSeleccionadoCompras.contains(",")) {
                    ids = idSeleccionadoCompras.split(",");
                } else {
                    ids = new String[1];
                    ids[0] = new String();
                    ids[0] = idSeleccionadoCompras;
                }
                int cnt = 0;
                for (cnt = 0; cnt < ids.length; cnt++) {
                    bRes = (BeanItem<TComprasVista>) tablaCompras.getItem(ids[cnt]);
                    res = bRes.getBean();
                    mComprasEliminadas.put(res.getId(), res);
                    bcCompras.removeItem(res.getId());
                }

                calcularTotales();

            }
        } else if (event.getButton().equals(eliminarLineaVentaButton)) {
            if (idSeleccionadoVentas == null || idSeleccionadoLineaVentas.isEmpty()) {
                Notification aviso = new Notification("Se deben seleccionar los registros a eliminar del listado de ventas", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            } else {

                String[] ids;
                if (idSeleccionadoLineaVentas.contains(",")) {
                    ids = idSeleccionadoLineaVentas.split(",");
                } else {
                    ids = new String[1];
                    ids[0] = new String();
                    ids[0] = idSeleccionadoLineaVentas;
                }

                BeanItem<TVentasVista> bRes = null;
                TVentasVista res = null;

                int cnt = 0;
                for (cnt = 0; cnt < ids.length; cnt++) {
                    bRes = (BeanItem<TVentasVista>) tablaLineasVentas.getItem(ids[cnt]);
                    res = bRes.getBean();

                    mVentasEliminadas.put(res.getId(), res);
                    bcLineasVentas.removeItem(res.getId());
                }

                calcularTotales();

            }
        } else if (event.getButton().equals(guardarButton)) {
            Item it = null;
            Property<?> prop = null;
            ComboBox cBox = null;
            Boolean indPedido = false;
            Boolean indAlbaran = false;
            Boolean indCalibre = false;
            Boolean indTraza = false;
            Boolean indProv = false;
            Boolean indKgs = false;
            Boolean indVaried = false;
            Boolean indOrig = false;
            Boolean indCert = false;
            Boolean indProducto = false;

            it = tablaVentasCambiosTodos.getItem(1);

            // Cambiar todos los pedidos
            prop = it.getItemProperty("¿Cambiar todos pedidos?");
            cBox = (ComboBox) prop.getValue();
            if (cBox.getValue().equals("Sí")) {
                indPedido = true;
                cBox.setValue("No");
            }

            // Cambiar todos los albaranes
            prop = it.getItemProperty("¿Cambiar todos albaranes?");
            cBox = (ComboBox) prop.getValue();
            if (cBox.getValue().equals("Sí")) {
                indAlbaran = true;
                cBox.setValue("No");
            }

            // Cambiar todos los calibres
            prop = it.getItemProperty("¿Cambiar todos calibres?");
            cBox = (ComboBox) prop.getValue();
            if (cBox.getValue().equals("Sí")) {
                indCalibre = true;
                cBox.setValue("No");
            }

            // Cambiar todos los Kgs
            prop = it.getItemProperty("¿Cambiar todos kgs?");
            cBox = (ComboBox) prop.getValue();
            if (cBox.getValue().equals("Sí")) {
                indKgs = true;
                cBox.setValue("No");
            }

            // Cambiar todas las variedades
            prop = it.getItemProperty("¿Cambiar todas variedades?");
            cBox = (ComboBox) prop.getValue();
            if (cBox.getValue().equals("Sí")) {
                indVaried = true;
                cBox.setValue("No");
            }

            // Cambiar todos los orígenes
            prop = it.getItemProperty("¿Cambiar todos orígenes?");
            cBox = (ComboBox) prop.getValue();
            if (cBox.getValue().equals("Sí")) {
                indOrig = true;
                cBox.setValue("No");
            }

            // Cambiar todas las certificaciones
            prop = it.getItemProperty("¿Cambiar todas certificaciones?");
            cBox = (ComboBox) prop.getValue();
            if (cBox.getValue().equals("Sí")) {
                indCert = true;
                cBox.setValue("No");
            }

            // Cambiar todos los productos
            prop = it.getItemProperty("¿Cambiar todos productos?");
            cBox = (ComboBox) prop.getValue();
            if (cBox.getValue().equals("Sí")) {
                cBox.setValue("No");
                indProducto = true;
            }
            if (mComprasEliminadas.isEmpty() && mVentasEliminadas.isEmpty() && lIdsComprasModificadas.isEmpty() && lIdsVentasModificadas.isEmpty() && !indPedido && !indAlbaran && !indCalibre && !indTraza && !indProv && !indKgs && !indVaried
                    && !indOrig && !indCert && !indProducto) {
                Notification aviso = new Notification("No se han identificado cambios.", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            } else {

                List<Integer> lCertificaciones = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 999);

                ComboBox cbTotalLotesGuacamole = new ComboBox("Nº máximo de trazabilidades diferentes de guacamole: ");
                cbTotalLotesGuacamole.addItems(lCertificaciones);
                cbTotalLotesGuacamole.setValue(6);
                cbTotalLotesGuacamole.setFilteringMode(FilteringMode.CONTAINS);
                cbTotalLotesGuacamole.setNullSelectionAllowed(false);
                cbTotalLotesGuacamole.setNewItemsAllowed(false);

                ComboBox cbTotalLotes = new ComboBox("Nº máximo de trazabilidades diferentes por producto: ");
                cbTotalLotes.addItems(lCertificaciones);
                cbTotalLotes.setValue(999);
                cbTotalLotes.setFilteringMode(FilteringMode.CONTAINS);
                cbTotalLotes.setNullSelectionAllowed(false);
                cbTotalLotes.setNewItemsAllowed(false);

                ComboBox cbCertificaciones = new ComboBox("Certificación");
                cbCertificaciones.addItem("SOLO BIO");
                cbCertificaciones.addItem("SOLO CONVENCIONAL");
                cbCertificaciones.addItem("TODAS");
                cbCertificaciones.setValue("TODAS");
                cbCertificaciones.setNullSelectionAllowed(false);

                HorizontalLayout hor = new HorizontalLayout();
                hor.setSpacing(true);

                VerticalLayout ver = new VerticalLayout();
                ver.setSpacing(true);

                //hor.addComponent(cbTotalLotes);
                hor.addComponent(cbTotalLotesGuacamole);
                hor.addComponent(cbCertificaciones);

                Label lbl2 = new Label("Configuración correcciones");

                Label lbl = new Label("Después de aplicar cambios, se procederá a corregir errores ¿Continuar?");

                ver.addComponent(lbl2);
                ver.setComponentAlignment(lbl2, Alignment.TOP_CENTER);
                ver.addComponent(hor);
                ver.addComponent(lbl);

                MessageBox.createQuestion().withCaption(appName).withMessage(ver).withNoButton()
                          .withYesButton(() -> aplicarCambios((Integer) cbTotalLotes.getValue(), (Integer) cbTotalLotesGuacamole.getValue(), (String) cbCertificaciones.getValue()), ButtonOption.caption("Sí")).open();

            }
        } else if (event.getButton().equals(corregirErroresButton)) {
            if (!mVentasErroneas.values().isEmpty()) {
                List<Integer> lCertificaciones = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 999);

                ComboBox cbTotalLotesGuacamole = new ComboBox("Trazabilidades diferentes de guacamole: ");
                cbTotalLotesGuacamole.addItems(lCertificaciones);
                cbTotalLotesGuacamole.setValue(6);
                cbTotalLotesGuacamole.setFilteringMode(FilteringMode.CONTAINS);
                cbTotalLotesGuacamole.setNullSelectionAllowed(false);
                cbTotalLotesGuacamole.setNewItemsAllowed(false);

                ComboBox cbTotalLotes = new ComboBox("Trazabilidades diferentes por producto: ");
                cbTotalLotes.addItems(lCertificaciones);
                cbTotalLotes.setValue(999);
                cbTotalLotes.setFilteringMode(FilteringMode.CONTAINS);
                cbTotalLotes.setNullSelectionAllowed(false);
                cbTotalLotes.setNewItemsAllowed(false);

                ComboBox cbCertificaciones = new ComboBox("Certificación");
                cbCertificaciones.addItem("SOLO BIO");
                cbCertificaciones.addItem("SOLO CONVENCIONAL");
                cbCertificaciones.addItem("TODAS");
                cbCertificaciones.setValue("TODAS");
                cbCertificaciones.setNullSelectionAllowed(false);
                HorizontalLayout hor = new HorizontalLayout();
                hor.setSpacing(true);

                VerticalLayout ver = new VerticalLayout();
                ver.setSpacing(true);

                //hor.addComponent(cbTotalLotes);
                hor.addComponent(cbTotalLotesGuacamole);
                hor.addComponent(cbCertificaciones);

                Label lbl2 = new Label("Configuración correcciones");
                lbl2.setStyleName("tituloTamano16");

                Label lbl = new Label("Se han identificado " + mVentasErroneas.size() + " registros erróneos. Esta operación llevar varios minutos en completarse y no se puede deshacer.");
                Label lbl3 = new Label("¿Quieres continuar?");
                lbl3.setStyleName("tituloTamano16");
                ver.addComponent(lbl2);
                ver.setComponentAlignment(lbl2, Alignment.TOP_CENTER);
                ver.addComponent(hor);
                ver.addComponent(lbl);
                ver.addComponent(lbl3);

                MessageBox.createQuestion().withCaption(appName).withMessage(ver).withNoButton()
                          .withYesButton(() -> accionCorregir((Integer) cbTotalLotes.getValue(), (Integer) cbTotalLotesGuacamole.getValue(), (String) cbCertificaciones.getValue()), ButtonOption.caption("Sí")).open();

            } else {
                Notification aviso = new Notification("No se han identificado errores a corregir.", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }
        } else if (event.getButton().equals(guardarBasuraButton)) {
            if (idSeleccionadoCompras == null || idSeleccionadoCompras.isEmpty()) {
                Notification aviso = new Notification("Se deben seleccionar los registros para crear los nuevos registros de basura", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            } else {
                guardarLineasBasura();
            }
        } else if (event.getButton().equals(excelButton)) {
            if (fModificarCampos.getValue().equals("Solo lectura")) {

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
        } else if (event.getButton().equals(pdfButton)) {
            if (tablaVentas.isVisible() || tablaVentasCambios.isVisible()) {
                if ((!tablaVentasCambios.getItemIds().isEmpty() || !tablaVentas.getItemIds().isEmpty()) && idSeleccionadoVentas != null && !idSeleccionadoVentas.isEmpty()) {
                    try {
                        guardarRegistrosTablasExportacion();

                        Page.getCurrent().open("/exportarTrazabilidadesPdf?idEmpleado=" + user + "&idPedidos=" + idSeleccionadoVentas, "_blank");

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
                } else {
                    Notification aviso = new Notification("No se han identificado pedidos de venta a exportar", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }

            } else {
                Notification aviso = new Notification("Solo se permite la exportación en modo 'Solo lectura'", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }
        } else if (event.getButton().equals(quitarFiltrosButton)) {
            quitarTodosFiltros();
        }
    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaCompras(TPermisos permisos) {
        tablaCompras = new TablaGenerica(new Object[] { "nombreDescriptivo", "albaranFin", "fechaFin", "partidaFin", "cajasFin", "kgsBrutoFin", "loteFin", "pesoNetoFin", "kgsDisponibles", "productoFin", "variedadFin", "proveedorFin", "origenFin", "familiaFin", "ggnFin", "calidadFin", "calibreFin" }, new String[] { "Descripción importación", "Albarán", "Fecha", "Partida", "Nº de cajas", "Peso bruto", "Trazabilidad", "Peso neto", "Kgs disponibles", "Producto", "Variedad", "Entidad", "Orígen", "Plantilla de producto", "Global Gap", "Certificación", "Calibre" }, bcCompras);
        tablaCompras.addStyleName("big striped");
        tablaCompras.setEditable(false);
        tablaCompras.setMultiSelect(true);
        tablaCompras.setPageLength(40);
        tablaCompras.setId("1");
        tablaCompras.setColumnWidth("fechaFin", 70);
        /**tablaCompras.setColumnWidth("albaran", 200);
        tablaCompras.setColumnWidth("albaranFin", 200);
        tablaCompras.setColumnWidth("fecha", 200);
        tablaCompras.setColumnWidth("fechaFin", 200);
        tablaCompras.setColumnWidth("partida", 200);
        tablaCompras.setColumnWidth("partidaFin", 200);
        tablaCompras.setColumnWidth("cajas", 200);
        tablaCompras.setColumnWidth("cajasFin", 200);
        tablaCompras.setColumnWidth("kgsBruto", 200);
        tablaCompras.setColumnWidth("kgsBrutoFin", 200);
        tablaCompras.setColumnWidth("lote", 200);
        tablaCompras.setColumnWidth("loteFin", 200);
        tablaCompras.setColumnWidth("pesoNeto", 200);
        tablaCompras.setColumnWidth("pesoNetoFin", 200);
        tablaCompras.setColumnWidth("producto", 200);
        tablaCompras.setColumnWidth("productoFin", 200);
        tablaCompras.setColumnWidth("proveedor", 200);
        tablaCompras.setColumnWidth("proveedorFin", 200);
        tablaCompras.setColumnWidth("origen", 200);
        tablaCompras.setColumnWidth("origenFin", 200);
        tablaCompras.setColumnWidth("familia", 200);
        tablaCompras.setColumnWidth("familiaFin", 200);
        tablaCompras.setColumnWidth("ggn", 200);
        tablaCompras.setColumnWidth("ggnFin", 200);
        tablaCompras.setColumnWidth("Certificación", 200);
        tablaCompras.setColumnWidth("calidadFin", 200);
        */
        tablaCompras.setDragMode(TableDragMode.ROW);
        // Para mostrar/ocultar columnas de la tabla
        tablaCompras.setColumnCollapsingAllowed(true);
        // Para cambiar el orden las columnas
        tablaCompras.setColumnReorderingAllowed(true);

        // Establecemos tamaño fijo en columnas específicas.        
        tablaCompras.setMultiSelect(true);
        tablaCompras.addItemClickListener(new ItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoCompras = (String) event.getItemId();
                // Guardamos el ID del registro para guardar la modificación. Solo si está en modo edición
                if (tablaCompras.isEditable() && idSeleccionadoCompras != null && !idSeleccionadoCompras.isEmpty()) {
                    if (!lIdsComprasModificadas.contains(idSeleccionadoCompras)) {
                        lIdsComprasModificadas.add(idSeleccionadoCompras);
                    }
                }
                if (event.isDoubleClick()) {
                    tablaCompras.getItem(idSeleccionadoCompras);
                    BeanItem<TComprasVista> bRes = (BeanItem<TComprasVista>) tablaCompras.getItem(idSeleccionadoCompras);
                    TComprasVista res = bRes.getBean();
                    if (event.getPropertyId() != null) {
                        if (event.getPropertyId().equals("albaranFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("fechaFin")) {
                            fechaDesdeCompra.setValue(res.getFechaFin());

                        } else if (event.getPropertyId().equals("partidaFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("loteFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("productoFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("proveedorFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("origenFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("familiaFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("ggnFin")) {
                            cbGgnCompra.setValue(res.getGgnFin());
                        } else if (event.getPropertyId().equals("calidadFin")) {
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
                            comprobarFiltrosAplicados();
                        }

                    }

                }
            }
        });

        tablaCompras.addValueChangeListener(new Property.ValueChangeListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {

                Set<String> values = (Set<String>) tablaCompras.getValue();
                idSeleccionadoCompras = "";
                for (String v : values) {
                    if (v.isEmpty())
                        continue;
                    if (!idSeleccionadoCompras.isEmpty()) {
                        idSeleccionadoCompras = idSeleccionadoCompras + "," + v;
                    } else {
                        idSeleccionadoCompras = v;
                    }

                }
            }
        });

        tablaCompras.setTableFieldFactory(new TableFieldFactory() {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                TextField field = new TextField((String) propertyId);
                // If you want to disable edition on a column, use ReadOnly
                if (propertyId.toString().equals("kgsDisponibles")) {
                    field.setReadOnly(true);
                    return field;
                } else {
                    return field;
                }
            }
        });

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaComprasCambios(TPermisos permisos) {
        tablaComprasCambios = new TablaGenerica(new Object[] { "albaranFin", "fechaFin", "partidaFin", "pesoNeto", "pesoNetoFin", "kgsDisponibles", "cajasFin", "kgsBruto", "kgsBrutoFin", "loteFin", "variedadFin", "proveedorFin", "origenFin", "familiaFin", "ggn", "ggnFin", "calidadFin", "calibreFin" }, new String[] { "Albarán(fin)", "Fecha(fin)", "Partida(fin)", "Peso neto(ini)", "Peso neto(fin)", "Kgs disponibles", "Nº de cajas(fin)", "Peso bruto(ini)", "Peso bruto(fin)", "Trazabilidad(fin)", "Variedad(fin)", "Entidad(fin)", "Orígen(fin)", "Plantilla de producto(fin)", "Global Gap(ini)", "Global Gap(fin)", "Calidad(fin)", "Calibre(fin)" }, bcCompras);
        tablaComprasCambios.addStyleName("big striped");
        tablaComprasCambios.setEditable(false);
        tablaComprasCambios.setMultiSelect(true);
        tablaComprasCambios.setPageLength(40);
        tablaComprasCambios.setId("3");
        tablaComprasCambios.setColumnWidth("fechaFin", 70);
        tablaComprasCambios.setColumnWidth("calidadFin", 200);
        tablaComprasCambios.setColumnWidth("variedadFin", 200);
        tablaComprasCambios.setColumnWidth("proveedorFin", 200);

        tablaComprasCambios.setDragMode(TableDragMode.ROW);
        // Para mostrar/ocultar columnas de la tabla
        tablaComprasCambios.setColumnCollapsingAllowed(true);
        // Para cambiar el orden las columnas
        tablaComprasCambios.setColumnReorderingAllowed(true);

        // Establecemos tamaño fijo en columnas específicas.        
        tablaComprasCambios.setMultiSelect(true);
        tablaComprasCambios.addItemClickListener(new ItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoCompras = (String) event.getItemId();
                // Guardamos el ID del registro para guardar la modificación. Solo si está en modo edición
                if (tablaComprasCambios.isEditable() && idSeleccionadoCompras != null && !idSeleccionadoCompras.isEmpty()) {
                    if (!lIdsComprasModificadas.contains(idSeleccionadoCompras)) {
                        lIdsComprasModificadas.add(idSeleccionadoCompras);
                    }
                }
                if (event.isDoubleClick()) {
                    tablaComprasCambios.getItem(idSeleccionadoCompras);
                    BeanItem<TComprasVista> bRes = (BeanItem<TComprasVista>) tablaComprasCambios.getItem(idSeleccionadoCompras);
                    TComprasVista res = bRes.getBean();
                    if (event.getPropertyId() != null) {
                        if (event.getPropertyId().equals("albaranFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("fechaFin")) {

                            fechaDesdeCompra.setValue(res.getFechaFin());

                        } else if (event.getPropertyId().equals("partidaFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("loteFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("productoFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("proveedorFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("origenFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("familiaFin")) {
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("ggnFin")) {
                            cbGgnCompra.setValue(res.getGgnFin());
                        } else if (event.getPropertyId().equals("calidadFin")) {
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
                            comprobarFiltrosAplicados();
                        }
                    }
                }
            }
        });

        tablaComprasCambios.addValueChangeListener(new Property.ValueChangeListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {

                Set<String> values = (Set<String>) tablaComprasCambios.getValue();
                idSeleccionadoCompras = "";
                for (String v : values) {
                    if (v.isEmpty())
                        continue;
                    if (!idSeleccionadoCompras.isEmpty()) {
                        idSeleccionadoCompras = idSeleccionadoCompras + "," + v;
                    } else {
                        idSeleccionadoCompras = v;
                    }

                }
            }
        });

        tablaComprasCambios.setTableFieldFactory(new TableFieldFactory() {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                TextField field = new TextField((String) propertyId);
                if (propertyId.equals("calidadFin")) {
                    ComboBox cb = new ComboBox();
                    cb.setWidth(170, Sizeable.Unit.PIXELS);
                    cb.setNullSelectionAllowed(false);
                    cb.setNewItemsAllowed(false);
                    cb.addItem("BIO");
                    cb.addItem("CONVENCIONAL");
                    cb.setId(itemId.toString());
                    cb.setValue(itemId);

                    return cb;
                }
                // If you want to disable edition on a column, use ReadOnly
                if (!propertyId.toString().endsWith("Fin") || propertyId.toString().equals("kgsDisponibles") || propertyId.toString().contains("albar") || propertyId.toString().contains("fech") || propertyId.toString().contains("parti")
                        || propertyId.toString().contains("lote") || propertyId.toString().contains("produ")) {
                    field.setReadOnly(true);
                    return field;
                } else {
                    return field;
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

    private void crearTablaNuevaCompra() {
        tablaNuevaCompra = new TablaGenerica(new Object[] { "nombreDescriptivo", "albaran", "fecha", "partida", "cajas", "kgsBruto", "lote", "pesoNeto", "producto", "proveedor", "origen", "familia", "calidad" }, new String[] { "Descripción importación", "Albarán", "Fecha", "Partida", "Nº de cajas", "Peso bruto", "Trazabilidad", "Peso neto", "Producto", "Entidad", "Orígen", "Plantilla de producto", "Certificación" }, bcNuevaCompra);
        tablaNuevaCompra.addStyleName("big striped");
        tablaNuevaCompra.setEditable(true);
        tablaNuevaCompra.setPageLength(1);
        tablaNuevaCompra.addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoNCompra = (String) event.getItemId();
            }
        });
    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaVentas(TPermisos permisos) {
        tablaVentas = new TablaGenerica(new Object[] { "nombreDescriptivo", "pedidoFin", "albaranFin", "calibreFin", "idPaleFin", "numBultosFin", "numBultosPaleFin", "clienteFin", "fechaVentaFin", "kgsFin", "kgsNetosFin", "variedadFin", "origenFin", "calidadVentaFin", "confeccionFin", "familiaFin" }, new String[] { "Descripción importación", "Pedido de venta", "Albarán de venta", "Calibre", "ID de palé", "Bultos mov. venta", "Bultos por palé", "Cliente", "Fecha salida", "Kilos", "Peso neto teo. venta", "Variedad", "Orígen venta", "Certificación venta", "Confección", "Producto" }, bcVentas);
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
                if (tablaVentas.isEditable() && idSeleccionadoVentas != null && !idSeleccionadoVentas.isEmpty()) {
                    if (!lIdsVentasModificadas.contains(idSeleccionadoVentas)) {
                        lIdsVentasModificadas.add(idSeleccionadoVentas);
                    }
                }
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("fechaVentaFin")) {

                            fechaDesdeVenta.setValue(res.getFechaVentaFin());

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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
                        }
                    }

                }
            }
        });

        tablaVentas.addValueChangeListener(new Property.ValueChangeListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {

                Set<String> values = (Set<String>) tablaVentas.getValue();
                idSeleccionadoVentas = "";
                for (String v : values) {
                    if (v.isEmpty())
                        continue;
                    if (!idSeleccionadoVentas.isEmpty()) {
                        idSeleccionadoVentas = idSeleccionadoVentas + "," + v;
                    } else {
                        idSeleccionadoVentas = v;
                    }

                }
            }
        });

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaLineasVentas() {
        tablaLineasVentas = new TablaGenerica(new Object[] { "detalleError", "certificacionFin", "ggn", "kgsTrazadosFin", "loteFin", "referenciaCompraFin", "proveedorFin", "albaranCompraFin", "indBasura" }, new String[] { "Error", "Certificación", "GGN", "Kgs", "Lote", "Referencia compra", "Proveedor", "Albarán compra", "¿Basura?" }, bcLineasVentas);
        tablaLineasVentas.addStyleName("big striped");
        tablaLineasVentas.setEditable(false);
        tablaLineasVentas.setPageLength(40);
        tablaLineasVentas.setId("3");

        tablaLineasVentas.setDragMode(TableDragMode.ROW);
        // Para mostrar/ocultar columnas de la tabla
        tablaLineasVentas.setColumnCollapsingAllowed(true);
        // Para cambiar el orden las columnas
        tablaLineasVentas.setColumnReorderingAllowed(true);

        // Establecemos tamaño fijo en columnas específicas.        
        tablaLineasVentas.setMultiSelect(true);
        tablaLineasVentas.addItemClickListener(new ItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoLineaVentas = (String) event.getItemId();
                // Guardamos el ID del registro para guardar la modificación. Solo si está en modo edición
                if (tablaLineasVentas.isEditable() && idSeleccionadoLineaVentas != null && !idSeleccionadoLineaVentas.isEmpty()) {
                    if (!idSeleccionadoLineaVentas.contains(idSeleccionadoLineaVentas)) {
                        lIdsVentasModificadas.add(idSeleccionadoLineaVentas);
                    }
                }

                if (event.isDoubleClick()) {
                    tablaLineasVentas.getItem(idSeleccionadoLineaVentas);
                    BeanItem<TLineasVentasVista> bRes = (BeanItem<TLineasVentasVista>) tablaLineasVentas.getItem(idSeleccionadoLineaVentas);
                    TLineasVentasVista res = bRes.getBean();
                    if (event.getPropertyId() != null) {
                        if (event.getPropertyId().equals("loteFin")) {
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("certificacionFin")) {
                            List<String> lCalidades = (List<String>) lsCalidadVentas.getItemIds();

                            Boolean entra = false;
                            // Comprobamos si el albarán por el que quiere hacer el filtro, está ya en la lista de albaranes de compra.
                            for (String fam : lCalidades) {
                                if (fam.equals(res.getCertificacionFin())) {
                                    entra = true;
                                    break;
                                }
                            }
                            if (!entra) {
                                lsCalidadVentas.addItem(res.getCertificacionFin());
                            } else {
                                lsCalidadVentas.removeItem(res.getCertificacionFin());
                            }
                            comprobarFiltrosAplicados();
                        }
                    }

                }
            }
        });

        tablaLineasVentas.setCellStyleGenerator(new Table.CellStyleGenerator() {

            @SuppressWarnings("unchecked")
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                BeanItem<TLineasVentasVista> aux = (BeanItem<TLineasVentasVista>) source.getItem(itemId);

                TLineasVentasVista venta = aux.getBean();

                if (venta.getIndError().equals("Sí")) {
                    return "red";
                } else {
                    if (venta.getIndCambio().equals("Sí")) {
                        return "azul_8";
                    } else {
                        return "";
                    }
                }
            }
        });

        tablaLineasVentas.addValueChangeListener(new Property.ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {

                Set<String> values = (Set<String>) tablaLineasVentas.getValue();
                idSeleccionadoLineaVentas = "";
                for (String v : values) {
                    if (v.isEmpty())
                        continue;
                    if (!idSeleccionadoLineaVentas.isEmpty()) {
                        idSeleccionadoLineaVentas = idSeleccionadoLineaVentas + "," + v;
                    } else {
                        idSeleccionadoLineaVentas = v;
                    }

                }
            }

        });

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaVentasCambios(TPermisos permisos) {
        tablaVentasCambios = new TablaGenerica(new Object[] { "pedidoFin", "albaranFin", "calibre", "calibreFin", "clienteFin", "fechaVentaFin", "kgsNetos", "kgsNetosFin", "kgsEnvaseFin", "variedad", "variedadFin", "origen", "origenFin", "calidadVenta", "calidadVentaFin", "confeccionFin", "familia", "familiaFin" }, new String[] { "Pedido de venta(fin)", "Albarán de venta(fin)", "Calibre(ini)", "Calibre(fin)", "Cliente(fin)", "Fecha(fin)", "Kilos(ini)", "Kilos(fin)", "Kilos envase", "Variedad(ini)", "Variedad(fin)", "Orígen venta(ini)", "Orígen venta(fin)", "Certificación (ini)", "Certificación (fin)", "Confección(fin)", "Producto(ini)", "Producto(fin)" }, bcVentas);
        tablaVentasCambios.addStyleName("big striped");
        tablaVentasCambios.setEditable(false);
        tablaVentasCambios.setPageLength(40);
        tablaVentasCambios.setId("4");
        tablaVentasCambios.setColumnWidth("fechaVentaFin", 72);
        tablaVentasCambios.setColumnWidth("calidadVentaFin", 200);

        tablaVentasCambios.setDragMode(TableDragMode.ROW);
        // Para mostrar/ocultar columnas de la tabla
        tablaVentasCambios.setColumnCollapsingAllowed(true);
        // Para cambiar el orden las columnas
        tablaVentasCambios.setColumnReorderingAllowed(true);

        // Establecemos tamaño fijo en columnas específicas.        
        tablaVentasCambios.setMultiSelect(true);
        tablaVentasCambios.addItemClickListener(new ItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoVentas = (String) event.getItemId();
                // Guardamos el ID del registro para guardar la modificación. Solo si está en modo edición
                if (tablaVentas.isEditable() && idSeleccionadoVentas != null && !idSeleccionadoVentas.isEmpty()) {
                    if (!lIdsVentasModificadas.contains(idSeleccionadoVentas)) {
                        lIdsVentasModificadas.add(idSeleccionadoVentas);
                    }
                }
                textoTablaLineasVentas.setVisible(true);
                tLineas.setVisible(true);
                textoTablaLineasVentas.setVisible(true);
                tablaLineasVentas.setVisible(true);
                bcLineasVentas.removeAllItems();
                try {
                    BeanItem<TVentasVista> bRes = (BeanItem<TVentasVista>) tablaVentasCambios.getItem(idSeleccionadoVentas);
                    TVentasVista res = bRes.getBean();
                    bcLineasVentas.addAll(contrVista.obtenerLineasVentaVista(Double.valueOf(res.getIdExterno()), user, time));
                    calcularTotalesTrazabilidades();
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
                    tablaVentasCambios.getItem(idSeleccionadoVentas);
                    BeanItem<TVentasVista> bRes = (BeanItem<TVentasVista>) tablaVentasCambios.getItem(idSeleccionadoVentas);
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
                        } else if (event.getPropertyId().equals("fechaVentaFin")) {
                            fechaDesdeVenta.setValue(res.getFechaVentaFin());
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
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
                            comprobarFiltrosAplicados();
                        }
                    }
                }
            }
        });

        tablaVentasCambios.setTableFieldFactory(new TableFieldFactory() {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                TextField field = new TextField((String) propertyId);
                if (propertyId.equals("calidadVentaFin")) {
                    ComboBox cb = new ComboBox();
                    cb.setWidth(170, Sizeable.Unit.PIXELS);
                    cb.setNullSelectionAllowed(false);
                    cb.setNewItemsAllowed(false);
                    cb.addItem("BIO");
                    cb.addItem("CONVENCIONAL");
                    cb.setId(itemId.toString());
                    cb.setValue(itemId);

                    return cb;
                }
                // If you want to disable edition on a column, use ReadOnly
                if (!propertyId.toString().endsWith("Fin") || propertyId.toString().contains("client") || propertyId.toString().contains("fech") || propertyId.toString().contains("confeccionFin")) {
                    field.setReadOnly(true);
                    return field;
                } else {
                    return field;
                }
            }
        });

        tablaVentasCambios.addValueChangeListener(new Property.ValueChangeListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {

                Set<String> values = (Set<String>) tablaVentasCambios.getValue();
                idSeleccionadoVentas = "";
                for (String v : values) {
                    if (v.isEmpty())
                        continue;
                    if (!idSeleccionadoVentas.isEmpty()) {
                        idSeleccionadoVentas = idSeleccionadoVentas + "," + v;
                    } else {
                        idSeleccionadoVentas = v;
                    }

                }
            }
        });

        tablaVentasCambios.setCellStyleGenerator(new Table.CellStyleGenerator() {

            @SuppressWarnings("unchecked")
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                BeanItem<TVentasVista> aux = (BeanItem<TVentasVista>) source.getItem(itemId);

                TVentasVista venta = aux.getBean();

                Integer id = Integer.valueOf(venta.getId());

                if (lIds.contains(id)) {
                    return "red";
                } else {
                    venta.setError("no");
                    if (comprobarCambioVenta(venta)) {
                        return "azul_8";
                    } else {
                        return "";
                    }
                }
            }
        });
    }

    private void crearTablaVentasCambiosTodos() {
        tablaVentasCambiosTodos = new Table();
        tablaVentasCambiosTodos.setPageLength(1);
        tablaVentasCambiosTodos.addStyleName("small strong");

        tablaVentasCambiosTodos.addContainerProperty("¿Cambiar todos pedidos?", ComboBox.class, null);
        tablaVentasCambiosTodos.addContainerProperty("¿Cambiar todos albaranes?", ComboBox.class, null);
        tablaVentasCambiosTodos.addContainerProperty("¿Cambiar todos calibres?", ComboBox.class, null);
        tablaVentasCambiosTodos.addContainerProperty("                                 ", Label.class, null);
        tablaVentasCambiosTodos.addContainerProperty("¿Cambiar todos kgs?", ComboBox.class, null);
        tablaVentasCambiosTodos.addContainerProperty("             ", Label.class, null);
        tablaVentasCambiosTodos.addContainerProperty("¿Cambiar todas variedades?", ComboBox.class, null);
        tablaVentasCambiosTodos.addContainerProperty("         ", Label.class, null);
        tablaVentasCambiosTodos.addContainerProperty("¿Cambiar todos orígenes?", ComboBox.class, null);
        tablaVentasCambiosTodos.addContainerProperty("¿Cambiar todas certificaciones?", ComboBox.class, null);
        tablaVentasCambiosTodos.addContainerProperty("           ", Label.class, null);
        tablaVentasCambiosTodos.addContainerProperty("¿Cambiar todos productos?", ComboBox.class, null);

        ComboBox cbPedidos = new ComboBox();
        cbPedidos.setNullSelectionAllowed(false);
        cbPedidos.setWidth(9, Sizeable.Unit.EM);
        cbPedidos.addItem("Sí");
        cbPedidos.addItem("No");
        cbPedidos.setValue("No");
        cbPedidos.setFilteringMode(FilteringMode.CONTAINS);

        ComboBox cbAlbaranes = new ComboBox();
        cbAlbaranes.setNullSelectionAllowed(false);
        cbAlbaranes.setWidth(9, Sizeable.Unit.EM);
        cbAlbaranes.addItem("Sí");
        cbAlbaranes.addItem("No");
        cbAlbaranes.setValue("No");
        cbAlbaranes.setFilteringMode(FilteringMode.CONTAINS);

        Label lblCalibre = new Label("");

        ComboBox cbCalibres = new ComboBox();
        cbCalibres.setNullSelectionAllowed(false);
        cbCalibres.setWidth(9, Sizeable.Unit.EM);
        cbCalibres.addItem("Sí");
        cbCalibres.addItem("No");
        cbCalibres.setValue("No");
        cbCalibres.setFilteringMode(FilteringMode.CONTAINS);

        Label lblCliente = new Label("");
        Label lblFechaSalida = new Label("");
        Label lblKgs = new Label("");

        ComboBox cbKgs = new ComboBox();
        cbKgs.setNullSelectionAllowed(false);
        cbKgs.setWidth(9, Sizeable.Unit.EM);
        cbKgs.addItem("Sí");
        cbKgs.addItem("No");
        cbKgs.setValue("No");
        cbKgs.setFilteringMode(FilteringMode.CONTAINS);

        Label lblVariedad = new Label("");
        lblVariedad.setWidth(32, Sizeable.Unit.EM);
        ComboBox cbVariedades = new ComboBox();
        cbVariedades.setNullSelectionAllowed(false);
        cbVariedades.setWidth(11, Sizeable.Unit.EM);
        cbVariedades.addItem("Sí");
        cbVariedades.addItem("No");
        cbVariedades.setValue("No");
        cbVariedades.setFilteringMode(FilteringMode.CONTAINS);

        Label lblOrigen = new Label("");
        lblOrigen.setWidth(8, Sizeable.Unit.EM);
        ComboBox cbOrigenes = new ComboBox();
        cbOrigenes.setNullSelectionAllowed(false);
        cbOrigenes.setWidth(16, Sizeable.Unit.EM);
        cbOrigenes.addItem("Sí");
        cbOrigenes.addItem("No");
        cbOrigenes.setValue("No");
        cbOrigenes.setFilteringMode(FilteringMode.CONTAINS);

        Label lblCertificacion = new Label("");

        ComboBox cbCertificaciones = new ComboBox();
        cbCertificaciones.setNullSelectionAllowed(false);
        cbCertificaciones.setWidth(15, Sizeable.Unit.EM);
        cbCertificaciones.addItem("Sí");
        cbCertificaciones.addItem("No");
        cbCertificaciones.setValue("No");
        cbCertificaciones.setFilteringMode(FilteringMode.CONTAINS);

        Label lblProducto = new Label("");
        lblProducto.setWidth(14, Sizeable.Unit.EM);
        ComboBox cbProductos = new ComboBox();
        cbProductos.setNullSelectionAllowed(false);
        cbProductos.setWidth(13, Sizeable.Unit.EM);
        cbProductos.addItem("Sí");
        cbProductos.addItem("No");
        cbProductos.setValue("No");
        cbProductos.setFilteringMode(FilteringMode.CONTAINS);

        tablaVentasCambiosTodos.addItem(new Object[] { cbPedidos, cbAlbaranes, cbCalibres, lblVariedad, cbKgs, lblOrigen, cbVariedades, lblCertificacion, cbOrigenes, cbCertificaciones, lblProducto, cbProductos }, 1);

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaVentasTotales() {
        tablaVentasTotales = new TablaGenerica(new Object[] { "kgsNetos", "kgsNetosFin", }, new String[] { "Kilos iniciales", "Kilos trazados" }, bcVentasTotales);
        //tablaVentasTotales = new TablaGenerica(new Object[] { "numBultos", "numBultosPale", "kgsNetosFin", "kgsNetos" }, new String[] { "Bultos mov.venta totales", "Bultos por palé totales", "Kilos totales", "Kilos netos teo. venta totales" }, bcVentasTotales);
        tablaVentasTotales.addStyleName("big strong");
        tablaVentasTotales.setPageLength(1);
        // Para mostrar/ocultar columnas de la tabla
        tablaVentasTotales.setColumnCollapsingAllowed(false);
        // Para cambiar el orden las columnas
        tablaVentasTotales.setColumnReorderingAllowed(false);
        tablaVentasTotales.setSelectable(false);
        tablaVentasTotales.setSortEnabled(false);

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaLineasVentasTotales() {
        tablaLineasVentasTotales = new TablaGenerica(new Object[] { "id", "kgsTrazadosFin", }, new String[] { "Nº trazabilidades", "Kilos trazados" }, bcLineasTotales);
        //tablaVentasTotales = new TablaGenerica(new Object[] { "numBultos", "numBultosPale", "kgsNetosFin", "kgsNetos" }, new String[] { "Bultos mov.venta totales", "Bultos por palé totales", "Kilos totales", "Kilos netos teo. venta totales" }, bcVentasTotales);
        tablaLineasVentasTotales.addStyleName("big strong");
        tablaLineasVentasTotales.setPageLength(1);
        // Para mostrar/ocultar columnas de la tabla
        tablaLineasVentasTotales.setColumnCollapsingAllowed(false);
        // Para cambiar el orden las columnas
        tablaLineasVentasTotales.setColumnReorderingAllowed(false);
        tablaLineasVentasTotales.setSelectable(false);
        tablaLineasVentasTotales.setSortEnabled(false);

        tablaLineasVentasTotales.setColumnWidth("kgsTrazadosFin", 300);

    }

    /**
     * Clase para aplicar filtros al container.
     * @author Daniel Carmona Romero
     */
    private class FiltroContainer implements Container.Filter {
        protected String       propertyId;
        protected String       regex;
        protected Label        status;
        protected List<String> lotesVenta;
        protected List<String> lotesCompra;
        protected String[]     aux;
        protected Integer      cnt;

        public FiltroContainer(String propertyId, String regex, Label status) {
            this.propertyId = propertyId;
            this.regex = regex;
            this.status = status;
            this.lotesVenta = Utils.generarListaGenerica();
            this.lotesCompra = Utils.generarListaGenerica();
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
                // Si filtramos por el campo lote, customizamos el aplicar filtro.
                if (propertyId.equals("loteFin") && filtrandoVenta) {
                    // Obtenemos el campo "lote_fin" de la venta y lo pasamos a una lista para comprobar si "contains" el/los lote(s) que se están buscando
                    aux = value.split(",");
                    cnt = 1;
                    this.lotesCompra.clear();
                    this.lotesVenta.clear();
                    while (cnt < aux.length) {
                        this.lotesVenta.add(aux[cnt].trim().toLowerCase());
                        cnt++;
                    }
                    aux = regex.split("\\|");
                    cnt = 0;
                    while (cnt < aux.length) {
                        this.lotesCompra.add(aux[cnt]);
                        cnt++;
                    }
                    boolean result = false;

                    for (String val : this.lotesCompra) {
                        if (this.lotesVenta.contains(val)) {
                            status.setValue("");
                            result = true;
                            break;
                        }
                    }
                    if (!result) {
                        return false;
                    }
                    status.setValue(""); // OK
                    return result;
                } else {
                    boolean result = value.toLowerCase().matches(regex);
                    if (value.isEmpty() && !regex.isEmpty()) {
                        return false;
                    }
                    status.setValue(""); // OK
                    return result;
                }
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

        cbMostrarControles = new ComboBox("Mostrar componentes");
        cbMostrarControles.setNullSelectionAllowed(false);
        cbMostrarControles.addItem("Ocultar");
        cbMostrarControles.addItem("Mostrar");
        cbMostrarControles.setNewItemsAllowed(false);
        cbMostrarControles.setFilteringMode(FilteringMode.CONTAINS);
        cbMostrarControles.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (((String) cbMostrarControles.getValue()).equals("Mostrar")) {
                    cabeceraPantalla.setVisible(true);
                    botones.setVisible(true);
                } else {
                    cabeceraPantalla.setVisible(false);
                    botones.setVisible(false);
                }
            }
        });

        cbMostrarControles.setVisible(true);
        cbMostrarControles.setVisible(false);

        cbMostrarDatosGlobales = new ComboBox("Mostrar/Ocultar totales");
        cbMostrarDatosGlobales.setNullSelectionAllowed(false);
        cbMostrarDatosGlobales.addItem("Ocultar");
        cbMostrarDatosGlobales.addItem("Mostrar");

        cbMostrarDatosGlobales.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (((String) cbMostrarDatosGlobales.getValue()).equals("Mostrar")) {
                    totales.setVisible(true);
                } else {
                    totales.setVisible(false);
                }
            }
        });

        cbMostrarCambios = new ComboBox("Mostrar/Ocultar datos erróneos/cambios");
        cbMostrarCambios.setNullSelectionAllowed(false);
        cbMostrarCambios.addItem("Ocultar");
        cbMostrarCambios.addItem("Mostrar");
        cbMostrarCambios.setValue("Ocultar");

        cbMostrarCambios.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (((String) cbMostrarCambios.getValue()).equals("Mostrar")) {
                    tablaColores.setVisible(true);
                    tituloTablaColores.setVisible(true);
                    tablaComprasCambios.setVisible(true);
                    tablaVentasCambios.setVisible(true);
                    tablaCompras.setVisible(false);
                    guardarBasuraButton.setVisible(true);
                    eliminarLineaCompraButton.setVisible(true);
                    tablaVentas.setVisible(false);
                    fDatosMostrar.setVisible(true);
                    corregirErroresButton.setVisible(true);
                    idSeleccionadoCompras = null;
                } else {
                    // Quitamos los filtros de errores que pued haber
                    mostrarTodos();
                    tablaCompras.setVisible(true);
                    guardarBasuraButton.setVisible(true);
                    eliminarLineaCompraButton.setVisible(true);
                    tablaVentas.setVisible(true);
                    tablaColores.setVisible(false);
                    tituloTablaColores.setVisible(false);
                    tablaComprasCambios.setVisible(false);
                    tablaVentasCambios.setVisible(false);
                    fDatosMostrar.setVisible(false);
                    corregirErroresButton.setVisible(false);
                }
            }
        });

        cbMostrarCambios.setVisible(false);

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
        cbMostrarFiltro.setVisible(false);

        fMostrarTabla = new ComboBox();
        fMostrarTabla.setCaption("Mostrar tabla:");
        fMostrarTabla.addItem("Todas");
        fMostrarTabla.addItem("Compras");
        fMostrarTabla.addItem("Ventas");
        fMostrarTabla.addItem("Trazabilidades");
        fMostrarTabla.setValue("Todas");
        fMostrarTabla.setNullSelectionAllowed(false);
        fMostrarTabla.setFilteringMode(FilteringMode.CONTAINS);

        fMostrarTabla.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (fMostrarTabla.getValue().equals("Todas")) {
                    tCompras.setVisible(true);
                    tVentas.setVisible(true);
                    tLineas.setVisible(true);
                    idSeleccionadoCompras = null;
                    idSeleccionadoVentas = null;
                    guardarBasuraButton.setVisible(true);
                    eliminarLineaCompraButton.setVisible(true);
                } else if (fMostrarTabla.getValue().equals("Compras")) {
                    tCompras.setVisible(true);
                    tVentas.setVisible(false);
                    tLineas.setVisible(false);
                    idSeleccionadoCompras = null;
                    idSeleccionadoVentas = null;
                    guardarBasuraButton.setVisible(true);
                    eliminarLineaCompraButton.setVisible(false);
                } else if (fMostrarTabla.getValue().equals("Ventas")) {
                    tCompras.setVisible(false);
                    tVentas.setVisible(true);
                    tLineas.setVisible(false);
                    idSeleccionadoCompras = null;
                    idSeleccionadoVentas = null;
                    guardarBasuraButton.setVisible(false);
                    eliminarLineaCompraButton.setVisible(true);
                } else if (fMostrarTabla.getValue().equals("Trazabilidades")) {
                    tCompras.setVisible(false);
                    tVentas.setVisible(false);
                    tLineas.setVisible(true);
                    idSeleccionadoCompras = null;
                    idSeleccionadoVentas = null;
                    idSeleccionadoLineaVentas = null;
                    guardarBasuraButton.setVisible(false);
                    eliminarLineaCompraButton.setVisible(true);
                    textoTablaLineasVentas.setVisible(true);
                    if (fModificarCampos.getValue().toString().equals("Solo lectura")) {
                        tablaLineasVentas.setVisible(true);
                    } else {
                        tablaLineasVentas.setVisible(true);
                    }
                }
            }
        });

        fTipoFiltro = new ComboBox("Filtro");
        fTipoFiltro.addItem("Albarán compra");
        fTipoFiltro.addItem("Albarán venta");
        fTipoFiltro.addItem("Basura");
        fTipoFiltro.addItem("Certificación compra");
        fTipoFiltro.addItem("Certificación venta");
        fTipoFiltro.addItem("Cliente");
        fTipoFiltro.addItem("Familia compra");
        fTipoFiltro.addItem("Familia venta");
        fTipoFiltro.addItem("GGN");
        fTipoFiltro.addItem("Origen compra");
        fTipoFiltro.addItem("Origen venta");
        fTipoFiltro.addItem("Partida compra");
        fTipoFiltro.addItem("Partida venta");
        fTipoFiltro.addItem("Pedido compra");
        fTipoFiltro.addItem("Pedido venta");
        fTipoFiltro.addItem("Producto compra");
        fTipoFiltro.addItem("Producto venta");
        fTipoFiltro.addItem("Proveedor compra");
        fTipoFiltro.addItem("Proveedor venta");
        fTipoFiltro.addItem("Trazabilidad compra");
        fTipoFiltro.addItem("Trazabilidad venta");
        fTipoFiltro.addItem("Tipo error");
        fTipoFiltro.setNullSelectionAllowed(true);
        fTipoFiltro.setFilteringMode(FilteringMode.CONTAINS);

        fTipoFiltro.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                // Ocultamos todos los componentes para realizar búsquedas, y solo mostraremos el que corresponda
                ocultarComponentesFiltros();
                if (fTipoFiltro.getValue() != null) {
                    if (fTipoFiltro.getValue().equals("Albarán compra")) {
                        cbAlbaranCompra.setVisible(true);
                        lsAlbaranesCompras.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Albarán venta")) {
                        cbAlbaranVenta.setVisible(true);
                        lsAlbaranesVentas.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Basura")) {
                        cbBasuras.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Certificación compra")) {
                        cbCalidadCompra.setVisible(true);
                        lsCalidadCompra.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Certificación venta")) {
                        cbCalidadVenta.setVisible(true);
                        lsCalidadVentas.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Cliente")) {
                        cbClientes.setVisible(true);
                        lsClientes.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Familia compra")) {
                        cbFamiliasCompras.setVisible(true);
                        lsFamiliasCompras.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Familia venta")) {
                        cbFamiliasVentas.setVisible(true);
                        lsFamiliasVentas.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("GGN")) {
                        cbGgnCompra.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Origen compra")) {
                        cbPaisesCompras.setVisible(true);
                        lsPaisesCompra.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Origen venta")) {
                        cbPaisesVentas.setVisible(true);
                        lsPaisesVentas.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Partida compra")) {
                        cbPartidaCompra.setVisible(true);
                        lsPartidasCompra.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Pedido venta")) {
                        cbPedidoVenta.setVisible(true);
                        lsPedidosVentas.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Producto compra")) {
                        cbArticulosCompras.setVisible(true);
                        lsArticulosCompras.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Producto venta")) {
                        cbArticulosVentas.setVisible(true);
                        lsArticulosVentas.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Proveedor compra")) {
                        cbProveedoresCompras.setVisible(true);
                        lsProveedoresCompras.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Proveedor venta")) {
                        cbProveedoreVentas.setVisible(true);
                        lsProveedoresVentas.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Trazabilidad compra")) {
                        cbLoteCompra.setVisible(true);
                        lsLoteCompras.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Trazabilidad venta")) {
                        cbLoteVenta.setVisible(true);
                        lsLoteVentas.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Tipo error")) {
                        cbTipoErroresVenta.setVisible(true);
                        lsTiposErrores.setVisible(true);
                    }
                }
            }
        });

        fModificarCampos = new ComboBox();
        fModificarCampos.setCaption("Modo:");
        fModificarCampos.addItem("Solo lectura");
        fModificarCampos.addItem("Edición");
        fModificarCampos.setValue("Solo lectura");
        fModificarCampos.setNullSelectionAllowed(false);
        fModificarCampos.setFilteringMode(FilteringMode.CONTAINS);

        fModificarCampos.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (fModificarCampos.getValue().equals("Solo lectura")) {
                    tablaCompras.setEditable(false);
                    tablaComprasCambios.setEditable(false);
                    tablaVentasCambios.setEditable(false);
                    tablaVentas.setEditable(false);
                    tablaVentasCambiosTodos.setVisible(false);
                    tablaLineasVentas.setEditable(true);
                } else if (fModificarCampos.getValue().equals("Edición")) {
                    tablaCompras.setEditable(true);
                    tablaVentas.setEditable(true);
                    tablaLineasVentas.setEditable(true);
                    tablaComprasCambios.setEditable(true);
                    tablaVentasCambios.setEditable(true);
                    tablaVentasCambiosTodos.setVisible(true);
                }

            }
        });

        fDatosMostrar = new ComboBox();
        fDatosMostrar.setCaption("Datos a mostrar");
        fDatosMostrar.addItem("Todos");
        fDatosMostrar.addItem("Correcto");
        fDatosMostrar.addItem("Errores");
        fDatosMostrar.setValue("Todos");
        fDatosMostrar.setNullSelectionAllowed(false);
        fDatosMostrar.setFilteringMode(FilteringMode.CONTAINS);
        fDatosMostrar.setVisible(false);
        fDatosMostrar.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (fDatosMostrar.getValue().equals("Correcto")) {
                    mostrarCorrectos();
                } else if (fDatosMostrar.getValue().equals("Errores")) {
                    mostrarErrores();
                } else {
                    mostrarTodos();
                }

            }
        });

        fDatosMostrar.setVisible(false);

        cabeceraPantalla.addComponent(cbMostrarDatosGlobales);
        cabeceraPantalla.addComponent(cbMostrarCambios);
        cabeceraPantalla.addComponent(fDatosMostrar);

        cabeceraPantalla.addComponent(fMostrarTabla);
        cabeceraPantalla.addComponent(fModificarCampos);

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
                    }
                    comprobarFiltrosAplicados();
                    cbFamiliasCompras.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de albaranes que se están filtrando
        lsFamiliasCompras = new ListSelect();
        lsFamiliasCompras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsFamiliasCompras.getValue() != null) {
                    lsFamiliasCompras.removeItem(lsFamiliasCompras.getValue());
                }
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbFamiliasVentas.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de albaranes que se están filtrando
        lsFamiliasVentas = new ListSelect();
        lsFamiliasVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsFamiliasVentas.getValue() != null) {
                    lsFamiliasVentas.removeItem(lsFamiliasVentas.getValue());
                }
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbPaisesCompras.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de paises que se están filtrando
        lsPaisesCompra = new ListSelect();
        lsPaisesCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsPaisesCompra.getValue() != null) {
                    lsPaisesCompra.removeItem(lsPaisesCompra.getValue());
                }
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbPaisesVentas.clear();
                    fTipoFiltro.clear();
                }
                comprobarFiltrosAplicados();
            }
        });

        // Lista de paises que se están filtrando
        lsPaisesVentas = new ListSelect();
        lsPaisesVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsPaisesVentas.getValue() != null) {
                    lsPaisesVentas.removeItem(lsPaisesVentas.getValue());
                }
            }
        });
        /************************************************************************************************************/

        /************************************************** Tipo errores ********************************************/

        // Combobox con los Productos
        cbTipoErroresVenta = new ComboBox();
        cbTipoErroresVenta.addItem("CERTIFICACIÓN ERRONEA VENTA");
        cbTipoErroresVenta.addItem("KGS SOBREPASAN VENTA");
        cbTipoErroresVenta.addItem("NO EXISTE VENTA");
        cbTipoErroresVenta.addItem("CERTIFICACIÓN ERRONEA COMPRA");
        cbTipoErroresVenta.addItem("KGS SOBREPASAN COMPRA");
        cbTipoErroresVenta.addItem("NO EXISTE COMPRA");
        cbTipoErroresVenta.setFilteringMode(FilteringMode.CONTAINS);
        cbTipoErroresVenta.addValueChangeListener(new ValueChangeListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbTipoErroresVenta.getValue() != null) {
                    List<String> lAlbar = (List<String>) lsTiposErrores.getItemIds();
                    Boolean entra = false;
                    for (String al : lAlbar) {
                        if (al.equals((String) cbTipoErroresVenta.getValue())) {
                            entra = true;
                            break;
                        }
                    }

                    if (!entra) {
                        lsTiposErrores.addItem(cbTipoErroresVenta.getValue());
                    }
                    comprobarFiltrosAplicados();
                    cbTipoErroresVenta.clear();
                    lsTiposErrores.clear();
                }
            }
        });

        // Lista de Productos que se están filtrando
        lsTiposErrores = new ListSelect();
        lsTiposErrores.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsTiposErrores.getValue() != null) {
                    lsTiposErrores.removeItem(lsTiposErrores.getValue());
                }
                comprobarFiltrosAplicados();
            }
        });

        /************************************************************************************************************/

        /************************************************** Productos **************************************************/

        // Combobox con los Productos
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
                    }
                    comprobarFiltrosAplicados();
                    cbArticulosCompras.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de Productos que se están filtrando
        lsArticulosCompras = new ListSelect();
        lsArticulosCompras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsArticulosCompras.getValue() != null) {
                    lsArticulosCompras.removeItem(lsArticulosCompras.getValue());
                }
                comprobarFiltrosAplicados();
            }
        });

        // Combobox con los Productos
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
                    }
                    comprobarFiltrosAplicados();
                    cbArticulosVentas.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de paises que se están filtrando
        lsArticulosVentas = new ListSelect();
        lsArticulosVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsArticulosVentas.getValue() != null) {
                    lsArticulosVentas.removeItem(lsArticulosVentas.getValue());
                }
                comprobarFiltrosAplicados();
            }
        });
        /************************************************************************************************************/

        /************************************************** GGN **************************************************/

        // Combobox con los Productos
        cbGgnCompra = new ComboBox();
        cbGgnCompra.addItem("NO GGN");
        cbGgnCompra.addItem("GGN NACIONAL");
        cbGgnCompra.addItem("GGN IMPORTACION");
        cbGgnCompra.setFilteringMode(FilteringMode.CONTAINS);
        cbGgnCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                comprobarFiltrosAplicados();
                fTipoFiltro.clear();
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

                    }
                    comprobarFiltrosAplicados();
                    cbClientes.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de clientes que se están filtrando
        lsClientes = new ListSelect();
        lsClientes.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsClientes.getValue() != null) {
                    lsClientes.removeItem(lsClientes.getValue());
                }
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbProveedoresCompras.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de proveedores que se están filtrando
        lsProveedoresCompras = new ListSelect();
        lsProveedoresCompras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsProveedoresCompras.getValue() != null) {
                    lsProveedoresCompras.removeItem(lsProveedoresCompras.getValue());
                }
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbProveedoreVentas.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de proveedores que se están filtrando
        lsProveedoresVentas = new ListSelect();
        lsProveedoresVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsProveedoresVentas.getValue() != null) {
                    lsProveedoresVentas.removeItem(lsProveedoresVentas.getValue());
                }
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbAlbaranCompra.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de albaranes que se están filtrando
        lsAlbaranesCompras = new ListSelect();
        lsAlbaranesCompras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsAlbaranesCompras.getValue() != null) {
                    lsAlbaranesCompras.removeItem(lsAlbaranesCompras.getValue());
                }
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbAlbaranVenta.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de albaranes que se están filtrando
        lsAlbaranesVentas = new ListSelect();
        lsAlbaranesVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsAlbaranesVentas.getValue() != null) {
                    lsAlbaranesVentas.removeItem(lsAlbaranesVentas.getValue());
                }
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbPedidoVenta.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de pedidos de venta que se están filtrando
        lsPedidosVentas = new ListSelect();
        lsPedidosVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsPedidosVentas.getValue() != null) {
                    lsPedidosVentas.removeItem(lsPedidosVentas.getValue());
                }
                comprobarFiltrosAplicados();
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
                if (cbCalidadCompra.getValue() != null) {
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
                    }
                    comprobarFiltrosAplicados();
                    cbCalidadCompra.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de calidades que se están filtrando
        lsCalidadCompra = new ListSelect();
        lsCalidadCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsCalidadCompra.getValue() != null) {
                    lsCalidadCompra.removeItem(lsCalidadCompra.getValue());
                }
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbCalidadVenta.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de calidades que se están filtrando
        lsCalidadVentas = new ListSelect();
        lsCalidadVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsCalidadVentas.getValue() != null) {
                    lsCalidadVentas.removeItem(lsCalidadVentas.getValue());
                }
                comprobarFiltrosAplicados();
            }
        });

        /************************************************************************************************************/

        /************************************************** BASURA **************************************************/

        // Combobox con las calidades
        cbBasuras = new ComboBox();
        cbBasuras.addItem("Sí");
        cbBasuras.addItem("No");
        cbBasuras.addItem("Todo");
        cbBasuras.setNullSelectionAllowed(false);
        cbBasuras.setFilteringMode(FilteringMode.CONTAINS);
        cbBasuras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbCalidadVenta.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de calidades que se están filtrando
        lsCalidadVentas = new ListSelect();
        lsCalidadVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsCalidadVentas.getValue() != null) {
                    lsCalidadVentas.removeItem(lsCalidadVentas.getValue());
                }
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbLoteCompra.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de lote que se están filtrando
        lsLoteCompras = new ListSelect();
        lsLoteCompras.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsLoteCompras.getValue() != null) {
                    lsLoteCompras.removeItem(lsLoteCompras.getValue());
                }
                comprobarFiltrosAplicados();
            }
        });

        // Combobox con los lote
        cbLoteVenta = new ComboBox();
        cbLoteVenta.addItems(lLotesCompras);
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
                    }
                    comprobarFiltrosAplicados();
                    cbLoteVenta.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de lote que se están filtrando
        lsLoteVentas = new ListSelect();
        lsLoteVentas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsLoteVentas.getValue() != null) {
                    lsLoteVentas.removeItem(lsLoteVentas.getValue());
                }
                comprobarFiltrosAplicados();
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
                    }
                    comprobarFiltrosAplicados();
                    cbPartidaCompra.clear();
                    fTipoFiltro.clear();
                }
            }
        });

        // Lista de lote que se están filtrando
        lsPartidasCompra = new ListSelect();
        lsPartidasCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (lsPartidasCompra.getValue() != null) {
                    lsPartidasCompra.removeItem(lsPartidasCompra.getValue());
                }
                comprobarFiltrosAplicados();
            }
        });

        /************************************************************************************************************/

        /************************************************** FECHAS **************************************************/
        fechaDesdeCompra = new DateField();
        fechaDesdeCompra.setWidth(9, Sizeable.Unit.EM);
        fechaDesdeCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                fDatosMostrar.setValue("Todos");
                cambiarFechasCompras();
            }
        });

        // Fecha compra hasta.
        fechaHastaCompra = new DateField();
        fechaHastaCompra.setWidth(9, Sizeable.Unit.EM);
        fechaHastaCompra.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                fDatosMostrar.setValue("Todos");
                cambiarFechasCompras();
            }
        });

        // Fecha venta desde.
        fechaDesdeVenta = new DateField();
        fechaDesdeVenta.setWidth(9, Sizeable.Unit.EM);
        fechaDesdeVenta.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                fDatosMostrar.setValue("Todos");
                cambiarFechasVentas();
            }
        });

        // Fecha venta hasta.
        fechaHastaVenta = new DateField();
        fechaHastaVenta.setWidth(9, Sizeable.Unit.EM);
        fechaHastaVenta.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                fDatosMostrar.setValue("Todos");
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

        cbCerradas = new ComboBox();
        cbCerradas.addItem("Todas");
        cbCerradas.addItems("Sí");
        cbCerradas.addItem("No");
        cbCerradas.setValue("Todas");
        cbCerradas.setNullSelectionAllowed(false);
        cbCerradas.setNewItemsAllowed(false);
        cbCerradas.setFilteringMode(FilteringMode.CONTAINS);

        cbNombresVentas = new ComboBox();
        cbNombresVentas.addItem("Todos");
        cbNombresVentas.addItems(lNombresVentas);
        cbNombresVentas.setValue("Todos");
        cbNombresVentas.setNullSelectionAllowed(false);
        cbNombresVentas.setNewItemsAllowed(false);
        cbNombresVentas.setFilteringMode(FilteringMode.CONTAINS);

        cabeceraPantalla.addComponent(cbMostrarFiltro);

        cbMostrarControles.setValue("Mostrar");

        // Añadimos los componentes de los diltros
        HorizontalLayout hComps = new HorizontalLayout();
        hComps.setSpacing(true);
        hComps.addComponent(cbAlbaranCompra);
        hComps.addComponent(lsAlbaranesCompras);
        hComps.addComponent(cbAlbaranVenta);
        hComps.addComponent(lsAlbaranesVentas);
        hComps.addComponent(cbBasuras);
        hComps.addComponent(cbCalidadCompra);
        hComps.addComponent(lsCalidadCompra);
        hComps.addComponent(cbCalidadVenta);
        hComps.addComponent(lsCalidadVentas);
        hComps.addComponent(cbClientes);
        hComps.addComponent(lsClientes);
        hComps.addComponent(cbFamiliasCompras);
        hComps.addComponent(lsFamiliasCompras);
        hComps.addComponent(cbFamiliasVentas);
        hComps.addComponent(lsFamiliasVentas);
        hComps.addComponent(cbGgnCompra);
        hComps.addComponent(cbPaisesCompras);
        hComps.addComponent(lsPaisesCompra);
        hComps.addComponent(cbPaisesVentas);
        hComps.addComponent(lsPaisesVentas);
        hComps.addComponent(cbPartidaCompra);
        hComps.addComponent(lsPartidasCompra);
        hComps.addComponent(cbPedidoVenta);
        hComps.addComponent(lsPedidosVentas);
        hComps.addComponent(cbArticulosCompras);
        hComps.addComponent(lsArticulosCompras);
        hComps.addComponent(cbArticulosVentas);
        hComps.addComponent(lsArticulosVentas);
        hComps.addComponent(cbProveedoresCompras);
        hComps.addComponent(lsProveedoresCompras);
        hComps.addComponent(cbProveedoreVentas);
        hComps.addComponent(lsProveedoresVentas);
        hComps.addComponent(cbLoteCompra);
        hComps.addComponent(lsLoteCompras);
        hComps.addComponent(cbLoteVenta);
        hComps.addComponent(lsLoteVentas);
        hComps.addComponent(cbTipoErroresVenta);
        hComps.addComponent(lsTiposErrores);

        ocultarComponentesFiltros();

        Table table2 = new Table();
        table2.addStyleName("small strong");
        table2.addContainerProperty("Fecha compra desde", DateField.class, null);
        table2.addContainerProperty("Fecha compra hasta", DateField.class, null);
        table2.addContainerProperty("Fecha venta desde", DateField.class, null);
        table2.addContainerProperty("Fecha venta hasta", DateField.class, null);
        table2.addItem(new Object[] { fechaDesdeCompra, fechaHastaCompra, fechaDesdeVenta, fechaHastaVenta, }, 1);
        table2.setPageLength(1);

        //filtro.addComponent(table);
        filtro.addComponent(fTipoFiltro);
        filtro.addComponent(hComps);
        filtro.addComponent(aplicarFiltroButton);
        filtro.addComponent(table2);

        cbBasuras.setValue("No");

    }

    /**
     * Método que nos nutre el diccionario con las columnas para mostrarlas/ocultarlas en función de los datos guardados en BD.
     */
    private void nutrirDiccionarioCabeceraTablaCompras() {

        mColumnasCompras = new HashMap<String, String>();
        mColumnasCompras.put("Descripción importación", "nombreDescriptivo");
        mColumnasCompras.put("Albarán", "albaranFin");
        mColumnasCompras.put("Fecha", "fechaFin");
        mColumnasCompras.put("Partida", "partidaFin");
        mColumnasCompras.put("Nº de cajas", "cajasFin");
        mColumnasCompras.put("Peso bruto", "kgsBrutoFin");
        mColumnasCompras.put("Trazabilidad", "loteFin");
        mColumnasCompras.put("Peso neto", "pesoNetoFin");
        mColumnasCompras.put("Kgs disponibles", "kgsDisponibles");
        mColumnasCompras.put("Producto", "productoFin");
        mColumnasCompras.put("Variedad", "variedadFin");
        mColumnasCompras.put("Entidad", "proveedorFin");
        mColumnasCompras.put("Orígen", "origenFin");
        mColumnasCompras.put("Plantilla de producto", "familiaFin");
        mColumnasCompras.put("Global Gap", "ggnFin");
        mColumnasCompras.put("Certificación", "calidadFin");

        mColumnasCompras = new HashMap<String, String>();
        mColumnasCompras.put("nombreDescriptivo", "nombreDescriptivo");
        mColumnasCompras.put("albaranFin", "albaranFin");
        mColumnasCompras.put("fechaFin", "fechaFin");
        mColumnasCompras.put("partidaFin", "partidaFin");
        mColumnasCompras.put("cajasFin", "cajasFin");
        mColumnasCompras.put("kgsBrutoFin", "kgsBrutoFin");
        mColumnasCompras.put("loteFin", "loteFin");
        mColumnasCompras.put("pesoNetoFin", "pesoNetoFin");
        mColumnasCompras.put("kgsDisponibles", "kgsDisponibles");
        mColumnasCompras.put("productoFin", "productoFin");
        mColumnasCompras.put("variedadFin", "variedadFin");
        mColumnasCompras.put("proveedorFin", "proveedorFin");
        mColumnasCompras.put("origenFin", "origenFin");
        mColumnasCompras.put("familiaFin", "familiaFin");
        mColumnasCompras.put("ggnFin", "ggnFin");
        mColumnasCompras.put("calidadFin", "calidadFin");

        mColumnasIdsCompras = new HashMap<String, String>();
        mColumnasIdsCompras.put("nombreDescriptivo", "Descripción importación");
        mColumnasIdsCompras.put("albaranFin", "Albarán");
        mColumnasIdsCompras.put("fechaFin", "Fecha");
        mColumnasIdsCompras.put("partidaFin", "Partida");
        mColumnasIdsCompras.put("cajasFin", "Nº de cajas");
        mColumnasIdsCompras.put("kgsBrutoFin", "Peso bruto");
        mColumnasIdsCompras.put("loteFin", "Trazabilidad");
        mColumnasIdsCompras.put("pesoNetoFin", "Peso neto");
        mColumnasIdsCompras.put("kgsDisponibles", "Kgs disponibles");
        mColumnasIdsCompras.put("productoFin", "Producto");
        mColumnasIdsCompras.put("variedadFin", "Variedad");
        mColumnasIdsCompras.put("proveedorFin", "Entidad");
        mColumnasIdsCompras.put("origenFin", "Orígen");
        mColumnasIdsCompras.put("familiaFin", "Plantilla de producto");
        mColumnasIdsCompras.put("ggnFin", "Global Gap");
        mColumnasIdsCompras.put("calidadFin", "Certificación");
        // fin        
        mColumnasIdsComprasCambios = new HashMap<String, String>();
        mColumnasIdsComprasCambios.put("albaranFin", "Albarán(fin)");
        mColumnasIdsComprasCambios.put("fechaFin", "Fecha(fin)");
        mColumnasIdsComprasCambios.put("partidaFin", "Partida(fin)");
        mColumnasIdsComprasCambios.put("cajasFin", "Nº de cajas(fin)");
        mColumnasIdsComprasCambios.put("kgsBrutoFin", "Peso bruto(fin)");
        mColumnasIdsComprasCambios.put("loteFin", "Trazabilidad(fin)");
        mColumnasIdsComprasCambios.put("pesoNetoFin", "Peso neto(fin)");
        mColumnasIdsComprasCambios.put("kgsDisponibles", "Kgs disponibles");
        mColumnasIdsComprasCambios.put("productoFin", "Producto(fin)");
        mColumnasIdsComprasCambios.put("variedadFin", "Variedad(fin)");
        mColumnasIdsComprasCambios.put("proveedorFin", "Entidad(fin)");
        mColumnasIdsComprasCambios.put("origenFin", "Orígen(fin)");
        mColumnasIdsComprasCambios.put("familiaFin", "Plantilla de producto(fin)");
        mColumnasIdsComprasCambios.put("ggnFin", "Global Gap(fin)");
        mColumnasIdsComprasCambios.put("calidadFin", "Certificación(fin)");
        // ini
        mColumnasIdsComprasCambios.put("albaran", "Albarán(ini)");
        mColumnasIdsComprasCambios.put("fecha", "Fecha(ini)");
        mColumnasIdsComprasCambios.put("partida", "Partida(ini)");
        mColumnasIdsComprasCambios.put("cajas", "Nº de cajas(ini)");
        mColumnasIdsComprasCambios.put("kgsBruto", "Peso bruto(ini)");
        mColumnasIdsComprasCambios.put("lote", "Trazabilidad(ini)");
        mColumnasIdsComprasCambios.put("pesoNeto", "Peso neto(ini)");
        mColumnasIdsComprasCambios.put("producto", "Producto(ini)");
        mColumnasIdsComprasCambios.put("variedad", "Variedad(ini)");
        mColumnasIdsComprasCambios.put("proveedor", "Entidad(ini)");
        mColumnasIdsComprasCambios.put("origen", "Orígen(ini)");
        mColumnasIdsComprasCambios.put("familia", "Plantilla de producto(ini)");
        mColumnasIdsComprasCambios.put("ggn", "Global Gap(ini)");
        mColumnasIdsComprasCambios.put("calidad", "Certificación(ini)");

        mColumnasGuardadoCompras = new HashMap<String, String>();
        mColumnasGuardadoCompras.put("Descripción importación", "nombreDescriptivo");
        mColumnasGuardadoCompras.put("Albarán", "albaranFin");
        mColumnasGuardadoCompras.put("Fecha", "fechaFin");
        mColumnasGuardadoCompras.put("Partida", "partidaFin");
        mColumnasGuardadoCompras.put("Nº de cajas", "cajasFin");
        mColumnasGuardadoCompras.put("Peso bruto", "kgsBrutoFin");
        mColumnasGuardadoCompras.put("Trazabilidad", "loteFin");
        mColumnasGuardadoCompras.put("Peso neto", "pesoNetoFin");
        mColumnasGuardadoCompras.put("Kgs disponibles", "kgsDisponibles");
        mColumnasGuardadoCompras.put("Producto", "productoFin");
        mColumnasGuardadoCompras.put("Variedad", "variedadFin");
        mColumnasGuardadoCompras.put("Entidad", "proveedorFin");
        mColumnasGuardadoCompras.put("Orígen", "origenFin");
        mColumnasGuardadoCompras.put("Plantilla de producto", "familiaFin");
        mColumnasGuardadoCompras.put("Global Gap", "ggnFin");
        mColumnasGuardadoCompras.put("Certificación", "calidadFin");

        // Tabla cambios
        mColumnasGuardadoCompras.put("Nº de cajas(ini)", "cajas");
        mColumnasGuardadoCompras.put("Peso bruto(ini)", "kgsBruto");
        mColumnasGuardadoCompras.put("Trazabilidad(ini)", "lote");
        mColumnasGuardadoCompras.put("Peso neto(ini)", "pesoNeto");
        mColumnasGuardadoCompras.put("Variedad(ini)", "variedad");
        mColumnasGuardadoCompras.put("Entidad(ini)", "proveedor");
        mColumnasGuardadoCompras.put("Orígen(ini)", "origen");
        mColumnasGuardadoCompras.put("Plantilla de producto(ini)", "familia");
        mColumnasGuardadoCompras.put("Global Gap(ini)", "ggn");
        mColumnasGuardadoCompras.put("Calidad(ini)", "Certificación");
        mColumnasGuardadoCompras.put("Albarán(fin)", "albaranFin");
        mColumnasGuardadoCompras.put("Fecha(fin)", "fechaFin");
        mColumnasGuardadoCompras.put("Partida(fin)", "partidaFin");
        mColumnasGuardadoCompras.put("Nº de cajas(fin)", "cajasFin");
        mColumnasGuardadoCompras.put("Peso bruto(fin)", "kgsBrutoFin");
        mColumnasGuardadoCompras.put("Trazabilidad(fin)", "loteFin");
        mColumnasGuardadoCompras.put("Peso neto(fin)", "pesoNetoFin");
        mColumnasGuardadoCompras.put("Producto(fin)", "productoFin");
        mColumnasGuardadoCompras.put("Variedad(fin)", "variedadFin");
        mColumnasGuardadoCompras.put("Entidad(fin)", "proveedorFin");
        mColumnasGuardadoCompras.put("Orígen(fin)", "origenFin");
        mColumnasGuardadoCompras.put("Plantilla de producto(fin)", "familiaFin");
        mColumnasGuardadoCompras.put("Global Gap(fin)", "ggnFin");
        mColumnasGuardadoCompras.put("Calidad(fin)", "calidadFin");

        mColumnasGuardadoCompras.put("albaran", "Albarán(ini)");
        mColumnasGuardadoCompras.put("fecha", "Fecha(ini)");
        mColumnasGuardadoCompras.put("partida", "Partida(ini)");
        mColumnasGuardadoCompras.put("cajas", "Nº de cajas(ini)");
        mColumnasGuardadoCompras.put("kgsBruto", "Peso bruto(ini)");
        mColumnasGuardadoCompras.put("lote", "Trazabilidad(ini)");
        mColumnasGuardadoCompras.put("pesoNeto", "Peso neto(ini)");
        mColumnasGuardadoCompras.put("producto", "Producto(ini)");
        mColumnasGuardadoCompras.put("variedad", "Variedad(ini)");
        mColumnasGuardadoCompras.put("proveedor", "Entidad(ini)");
        mColumnasGuardadoCompras.put("origen", "Orígen(ini)");
        mColumnasGuardadoCompras.put("familia", "Plantilla de producto(ini)");
        mColumnasGuardadoCompras.put("ggn", "Global Gap(ini)");
        mColumnasGuardadoCompras.put("Certificación", "Calidad(ini)");
        mColumnasGuardadoCompras.put("albaranFin", "Albarán(fin)");
        mColumnasGuardadoCompras.put("fechaFin", "Fecha(fin)");
        mColumnasGuardadoCompras.put("partidaFin", "Partida(fin)");
        mColumnasGuardadoCompras.put("cajasFin", "Nº de cajas(fin)");
        mColumnasGuardadoCompras.put("kgsBrutoFin", "Peso bruto(fin)");
        mColumnasGuardadoCompras.put("loteFin", "Trazabilidad(fin)");
        mColumnasGuardadoCompras.put("pesoNetoFin", "Peso neto(fin)");
        mColumnasGuardadoCompras.put("productoFin", "Producto(fin)");
        mColumnasGuardadoCompras.put("variedadFin", "Variedad(fin)");
        mColumnasGuardadoCompras.put("proveedorFin", "Entidad(fin)");
        mColumnasGuardadoCompras.put("origenFin", "Orígen(fin)");
        mColumnasGuardadoCompras.put("familiaFin", "Plantilla de producto(fin)");
        mColumnasGuardadoCompras.put("ggnFin", "Global Gap(fin)");
        mColumnasGuardadoCompras.put("calidadFin", "Calidad(fin)");
    }

    /**
     * Método que nos nutre el diccionario con las columnas para mostrarlas/ocultarlas en función de los datos guardados en BD.
     */
    private void nutrirDiccionarioCabeceraTablaVentas() {

        mColumnasVentas = new HashMap<String, String>();
        mColumnasVentas.put("Descripción importación", "nombreDescriptivo");
        mColumnasVentas.put("Pedido de venta", "pedidoFin");
        mColumnasVentas.put("Albarán de venta", "albaranFin");
        mColumnasVentas.put("Calibre", "calibreFin");
        mColumnasVentas.put("Trazabilidad", "loteFin");
        mColumnasVentas.put("ID de palé", "idPaleFin");
        mColumnasVentas.put("Bultos mov. venta", "numBultosFin");
        mColumnasVentas.put("Bultos por palé", "numBultosPaleFin");
        mColumnasVentas.put("Productor", "proveedorFin");
        mColumnasVentas.put("Entidad", "clienteFin");
        mColumnasVentas.put("Fecha salida", "fechaVentaFin");
        mColumnasVentas.put("Kilos", "kgsFin");
        mColumnasVentas.put("Peso neto teo. venta", "kgsNetosFin");
        mColumnasVentas.put("Variedad", "variedadFin");
        mColumnasVentas.put("Orígen venta", "origenFin");
        mColumnasVentas.put("Certificación venta", "calidadVentaFin");
        mColumnasVentas.put("Confección", "confeccionFin");
        mColumnasVentas.put("Producto", "familiaFin");

        mColumnasVentas = new HashMap<String, String>();
        mColumnasVentas.put("nombreDescriptivo", "nombreDescriptivo");
        mColumnasVentas.put("pedidoFin", "pedidoFin");
        mColumnasVentas.put("albaranFin", "albaranFin");
        mColumnasVentas.put("calibreFin", "calibreFin");
        mColumnasVentas.put("loteFin", "loteFin");
        mColumnasVentas.put("idPaleFin", "idPaleFin");
        mColumnasVentas.put("numBultosFin", "numBultosFin");
        mColumnasVentas.put("numBultosPaleFin", "numBultosPaleFin");
        mColumnasVentas.put("proveedorFin", "proveedorFin");
        mColumnasVentas.put("clienteFin", "clienteFin");
        mColumnasVentas.put("fechaVentaFin", "fechaVentaFin");
        mColumnasVentas.put("kgsFin", "kgsFin");
        mColumnasVentas.put("kgsNetosFin", "kgsNetosFin");
        mColumnasVentas.put("variedadFin", "variedadFin");
        mColumnasVentas.put("origenFin", "origenFin");
        mColumnasVentas.put("calidadVentaFin", "calidadVentaFin");
        mColumnasVentas.put("confeccionFin", "confeccionFin");
        mColumnasVentas.put("familiaFin", "familiaFin");

        mColumnasIdsVentas = new HashMap<String, String>();
        mColumnasIdsVentas.put("nombreDescriptivo", "Descripción importación");
        mColumnasIdsVentas.put("pedidoFin", "Pedido de venta");
        mColumnasIdsVentas.put("albaranFin", "Albarán de venta");
        mColumnasIdsVentas.put("calibreFin", "Calibre");
        mColumnasIdsVentas.put("loteFin", "Trazabilidad");
        mColumnasIdsVentas.put("idPaleFin", "ID de palé");
        mColumnasIdsVentas.put("numBultosFin", "Bultos mov. venta");
        mColumnasIdsVentas.put("numBultosPaleFin", "Bultos por palé");
        mColumnasIdsVentas.put("proveedorFin", "Productor");
        mColumnasIdsVentas.put("clienteFin", "Entidad");
        mColumnasIdsVentas.put("fechaVentaFin", "Fecha salida");
        mColumnasIdsVentas.put("kgsFin", "Kilos");
        mColumnasIdsVentas.put("kgsNetosFin", "Peso neto teo. venta");
        mColumnasIdsVentas.put("variedadFin", "Variedad");
        mColumnasIdsVentas.put("origenFin", "Orígen venta");
        mColumnasIdsVentas.put("calidadVentaFin", "Certificación venta");
        mColumnasIdsVentas.put("confeccionFin", "Confección");
        mColumnasIdsVentas.put("familiaFin", "Producto");

        mColumnasGuardadoVentas = new HashMap<String, String>();
        mColumnasGuardadoVentas.put("Descripción importación", "nombreDescriptivo");
        mColumnasGuardadoVentas.put("Pedido de venta", "pedidoFin");
        mColumnasGuardadoVentas.put("Albarán de venta", "albaranFin");
        mColumnasGuardadoVentas.put("Calibre", "calibreFin");
        mColumnasGuardadoVentas.put("Trazabilidad", "loteFin");
        mColumnasGuardadoVentas.put("ID de palé", "idPaleFin");
        mColumnasGuardadoVentas.put("Bultos mov. venta", "numBultosFin");
        mColumnasGuardadoVentas.put("Bultos por palé", "numBultosPaleFin");
        mColumnasGuardadoVentas.put("Productor", "proveedorFin");
        mColumnasGuardadoVentas.put("Entidad", "clienteFin");
        mColumnasGuardadoVentas.put("Fecha salida", "fechaVentaFin");
        mColumnasGuardadoVentas.put("Kilos", "kgsFin");
        mColumnasGuardadoVentas.put("Peso neto teo. venta", "kgsNetosFin");
        mColumnasGuardadoVentas.put("Variedad", "variedadFin");
        mColumnasGuardadoVentas.put("Orígen venta", "origenFin");
        mColumnasGuardadoVentas.put("Certificación venta", "calidadVentaFin");
        mColumnasGuardadoVentas.put("Confección", "confeccionFin");
        mColumnasGuardadoVentas.put("Producto", "familiaFin");
        // Cambios
        mColumnasGuardadoVentas.put("Pedido de venta(ini)", "pedido");
        mColumnasGuardadoVentas.put("Albarán de venta(ini)", "albaran");
        mColumnasGuardadoVentas.put("Calibre(ini)", "calibre");
        mColumnasGuardadoVentas.put("Trazabilidad(ini)", "lote");
        mColumnasGuardadoVentas.put("ID de palé(ini)", "idPale");
        mColumnasGuardadoVentas.put("Bultos mov. venta(ini)", "numBultos");
        mColumnasGuardadoVentas.put("Bultos por palé(ini)", "numBultosPale");
        mColumnasGuardadoVentas.put("Productor(ini)", "proveedor");
        mColumnasGuardadoVentas.put("Entidad(ini)", "cliente");
        mColumnasGuardadoVentas.put("Fecha salida(ini)", "fechaVenta");
        mColumnasGuardadoVentas.put("Kilos(ini)", "kgs");
        mColumnasGuardadoVentas.put("Peso neto teo. venta(ini)", "kgsNetos");
        mColumnasGuardadoVentas.put("Variedad(ini)", "variedad");
        mColumnasGuardadoVentas.put("Orígen venta(ini)", "origen");
        mColumnasGuardadoVentas.put("Certificación (ini)", "calidadVenta");
        mColumnasGuardadoVentas.put("Confección(ini)", "confeccion");
        mColumnasGuardadoVentas.put("Producto(ini)", "familia");
        mColumnasGuardadoVentas.put("Pedido de venta(fin)", "pedidoFin");
        mColumnasGuardadoVentas.put("Albarán de venta(fin)", "albaranFin");
        mColumnasGuardadoVentas.put("Calibre(fin)", "calibreFin");
        mColumnasGuardadoVentas.put("Trazabilidad(fin)", "loteFin");
        mColumnasGuardadoVentas.put("ID de palé(fin)", "idPaleFin");
        mColumnasGuardadoVentas.put("Bultos mov. venta(fin)", "numBultosFin");
        mColumnasGuardadoVentas.put("Bultos por palé(fin)", "numBultosPaleFin");
        mColumnasGuardadoVentas.put("Productor(fin)", "proveedorFin");
        mColumnasGuardadoVentas.put("Entidad(fin)", "clienteFin");
        mColumnasGuardadoVentas.put("Fecha(fin)", "fechaVentaFin");
        mColumnasGuardadoVentas.put("Kilos(fin)", "kgsFin");
        mColumnasGuardadoVentas.put("Peso neto teo. venta(fin)", "kgsNetosFin");
        mColumnasGuardadoVentas.put("Variedad(fin)", "variedadFin");
        mColumnasGuardadoVentas.put("Orígen venta(fin)", "origenFin");
        mColumnasGuardadoVentas.put("Certificación (fin)", "calidadVentaFin");
        mColumnasGuardadoVentas.put("Confección(fin)", "confeccionFin");
        mColumnasGuardadoVentas.put("Producto(fin)", "familiaFin");

    }

    /**
     * Método que nos guarda las columnas y en qué orden se muestran los datos en la tabla de empresas.
     */
    private void guardarOrdenColumnasCompras() {

        // Tabla compras a pelo.
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

        // Tabla compras cambios
        String columnas2[] = tablaComprasCambios.getColumnHeaders();
        String columnasIdioma2[] = new String[columnas2.length];

        aux = 0;
        // Nutrimos el array con 
        while (aux < columnas2.length) {
            columnasIdioma2[aux] = new String(mColumnasGuardadoCompras.get(columnas2[aux]));
            aux++;
        }

        lCols = Utils.generarListaGenerica();
        col = null;
        if (columnas2 != null && columnas2.length > 0) {
            // Vamos recorriendo la cabecera de la tabla para coger los campos.
            Integer cnt = 0;
            while (cnt < columnas2.length) {
                if (tablaCompras.isColumnCollapsed(mColumnasGuardadoCompras.get(columnas2[cnt]))) {
                    cnt++;
                    continue;
                }
                col = new TColumnasTablasEmpleado();
                col.setCampo(columnasIdioma2[cnt]);
                col.setIdEmpleado(user);
                col.setIdTabla(Integer.valueOf(tablaComprasCambios.getId()));
                col.setNombrePantalla(NAME);
                col.setOrdenCampo(cnt);
                lCols.add(col);
                cnt++;
            }
            try {

                // Guardamos las columnas y en el orden indicado.
                contrVista.guardarCamposTablaEmpleado(lCols, user, NAME, Integer.valueOf(tablaComprasCambios.getId()), user, time);

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

        // Tabla ventas a pelo.
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

        // Tabla ventas cambios
        String columnas2[] = tablaVentasCambios.getColumnHeaders();
        String columnasIdioma2[] = new String[columnas2.length];

        aux = 0;
        // Nurimos el array con 
        while (aux < columnas2.length) {
            columnasIdioma2[aux] = new String(mColumnasGuardadoVentas.get(columnas2[aux]));
            aux++;
        }

        lCols = Utils.generarListaGenerica();
        col = null;
        if (columnas2 != null && columnas2.length > 0) {
            // Vamos recorriendo la cabecera de la tabla para coger los campos.
            Integer cnt = 0;
            while (cnt < columnas2.length) {
                if (tablaVentas.isColumnCollapsed(mColumnasGuardadoVentas.get(columnas2[cnt]))) {
                    cnt++;
                    continue;
                }
                col = new TColumnasTablasEmpleado();
                col.setCampo(columnasIdioma2[cnt]);
                col.setIdEmpleado(user);
                col.setIdTabla(Integer.valueOf(tablaVentasCambios.getId()));
                col.setNombrePantalla(NAME);
                col.setOrdenCampo(cnt);
                lCols.add(col);
                cnt++;
            }
            try {

                // Guardamos las columnas y en el orden indicado.
                contrVista.guardarCamposTablaEmpleado(lCols, user, NAME, Integer.valueOf(tablaVentasCambios.getId()), user, time);

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
     * Método que nos muestra las columnas según la configuración de las compras.
     * @param lColumnas Las columnas y en qué orden se van a mostrar.
     */
    private void mostrarColumnasTablaComprasCambios(List<TColumnasTablasEmpleado> lColumnas) {

        List<String> lCamposTabla = Utils.generarListaGenerica();
        List<String> lCamposTablaIdioma = Utils.generarListaGenerica();
        lCamposTabla.addAll(mColumnasGuardadoCompras.values());
        lCamposTablaIdioma.addAll(mColumnasGuardadoCompras.values());

        if (!lColumnas.isEmpty()) {
            Object[] visibleColumns = new Object[mColumnasIdsComprasCambios.size()];

            int i = 0;
            for (TColumnasTablasEmpleado col : lColumnas) {
                visibleColumns[i] = mColumnasGuardadoCompras.get(col.getCampo());
                lCamposTabla.remove(col.getCampo());
                lCamposTablaIdioma.remove(col.getCampo());
                i++;
            }

            // Eliminamos los campos que no se identificaron.
            for (String campo : lCamposTablaIdioma) {
                visibleColumns[i] = mColumnasGuardadoCompras.get(campo);
                tablaComprasCambios.setColumnCollapsed(mColumnasGuardadoCompras.get(campo), true);
                i++;
            }

            tablaComprasCambios.setVisibleColumns(visibleColumns);
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
     * Método que nos muestra las columnas según la configuración de las ventas.
     * @param lColumnas Las columnas y en qué orden se van a mostrar.
     */
    private void mostrarColumnasTablaVentasCambios(List<TColumnasTablasEmpleado> lColumnas) {

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
                tablaVentasCambios.setColumnCollapsed(mColumnasVentas.get(campo), true);
                i++;
            }

            tablaVentasCambios.setVisibleColumns(visibleColumns);
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

            totalKgsVentas += resV.getKgsNetosFin();
            totalKgsNetosVentas += resV.getKgsNetos();
        }

        // Compras totales
        TComprasVista cTotales = new TComprasVista();
        cTotales.setId("1");
        //  if (!fDatosMostrar.getValue().equals("Errores")) {
        //     cTotales.setKgsDisponibles(totalKgsCompras - totalKgsVentas);
        // } else {
        cTotales.setKgsDisponibles(totalKgsDisponibles);
        //  }
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

        calcularTotalesTrazabilidades();
    }

    @SuppressWarnings("unchecked")
    private void calcularTotalesTrazabilidades() {

        // Líneas
        Double totalKgsTrazados = Double.valueOf(0);
        Integer trazabilidades = 0;

        List<String> lIds = bcLineasVentas.getItemIds();
        List<TLineasVentasVista> lLineas = Utils.generarListaGenerica();

        BeanItem<TLineasVentasVista> bResL = null;
        TLineasVentasVista resL = null;

        // Si está filtrando por algo, mostramos las trazabilidades de ese algo
        if (!lLotesComprasSelecc.isEmpty()) {
            bcLineasVentas.removeAllItems();
            if (lLotesComprasSelecc.isEmpty()) {
                cargarLotesCompras();
            }
            try {

                lLineas = contrVista.obtenerLineasVentasLotesVista(lLotesComprasSelecc, user, time);
                bcLineasVentas.addAll(lLineas);
                tablaLineasVentas.setVisible(true);
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

        for (String id : lIds) {
            bResL = (BeanItem<TLineasVentasVista>) tablaLineasVentas.getItem(id);
            resL = bResL.getBean();

            trazabilidades++;
            totalKgsTrazados += resL.getKgsTrazadosFin();

        }

        // Líneas totales
        TLineasVentasVista lTotales = new TLineasVentasVista();
        lTotales.setId("" + trazabilidades);
        lTotales.setKgsTrazadosFin(totalKgsTrazados);

        bcLineasTotales.removeAllItems();
        bcLineasTotales.addBean(lTotales);

    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    @SuppressWarnings("unchecked")
    private void aplicarFiltroCompras(Boolean cargarVentas, Boolean permiteCarga) {
        bcCompras.removeAllContainerFilters();
        filtrandoVenta = false;
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
            filter = new FiltroContainer("variedadFin", filtro.toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
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
            bcCompras.addContainerFilter(filter);
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
            filter = new FiltroContainer("familiaFin", filtro.toLowerCase(), status);
            bcCompras.addContainerFilter(filter);

        }

        if (cbGgnCompra.getValue() != null && !cbGgnCompra.getValue().equals("Todos")) {
            filter = new FiltroContainer("ggnFin", ((String) cbGgnCompra.getValue()).toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
        }

        if (cbNombresCompras.getValue() != null && !cbNombresCompras.getValue().equals("Todos")) {
            filter = new FiltroContainer("nombreDescriptivo", ((String) cbNombresCompras.getValue()).toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
        }

        if (cbCerradas.getValue() != null && !cbCerradas.getValue().equals("Todas")) {
            filter = new FiltroContainer("cerrada", ((String) cbCerradas.getValue()).toLowerCase(), status);
            bcCompras.addContainerFilter(filter);
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
        }

        if (cargarVentas) {
            cargarLotesCompras();
            aplicarFiltroVentas(false);
        }

        calcularTotales();

    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    @SuppressWarnings("unchecked")
    private void aplicarFiltroVentas(Boolean cargarCompras) {
        bcVentas.removeAllContainerFilters();
        filtrandoVenta = true;
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
        }

        if (!lLotesComprasSelecc.isEmpty()) {
            String lotes = "";
            for (String lote : lLotesComprasSelecc) {
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
        }

        if (cbBasuras.getValue() != null && !cbBasuras.getValue().toString().equals("Todo") && !cbBasuras.getValue().toString().isEmpty()) {
            String filtr = "";

            if (cbBasuras.getValue().toString().equals("Sí")) {
                filtr = ".*(Sí).*";
            } else {
                filtr = ".*(No).*";
            }
            filter = new FiltroContainer("indBasura", filtr.toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
        }

        if (cbNombresVentas.getValue() != null && !cbNombresVentas.getValue().equals("Todos")) {
            filter = new FiltroContainer("nombreDescriptivo", ((String) cbNombresVentas.getValue()).toLowerCase(), status);
            bcVentas.addContainerFilter(filter);
        }

        // Aplicamos filtros en las compra en función de las ventas que se están mostrando.
        if (cargarCompras) {
            aplicarFiltroCompras(false, false);
        }
    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    @SuppressWarnings("unchecked")
    private void aplicarFiltroLineasVentas() {
        bcLineasVentas.removeAllContainerFilters();

        Boolean entra = false;
        if (((List<String>) lsTiposErrores.getItemIds()).size() != 0) {
            List<String> lArticulos = (List<String>) lsTiposErrores.getItemIds();
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
            filter = new FiltroContainer("detalleError", filtro.toLowerCase(), status);
            bcLineasVentas.addContainerFilter(filter);
            entra = true;
        }

        if (entra) {
            tLineas.setVisible(true);
        } else {
            tLineas.setVisible(false);
        }

    }

    /**
     * Método que nos indica si se están aplicando o no filtros.
     * @return true --> Si está aplicando algún filtro, false en caso contrario.
     */
    private boolean aplicaFiltros() {
        boolean result = true;

        // Comprobamos si está filtrando por algún campo.
        // Empezamos por las fechas
        if (fechaDesdeCompra.getValue() == null && fechaHastaCompra.getValue() == null && fechaDesdeVenta.getValue() == null && fechaHastaVenta.getValue() == null) {
            // Albranes 
            if (lsAlbaranesCompras.getItemIds().size() == 0 && lsAlbaranesVentas.getItemIds().size() == 0 && lsPartidasCompra.getItemIds().size() == 0 && lsPedidosVentas.getItemIds().size() == 0) {
                // Productos
                if (lsArticulosCompras.getItemIds().size() == 0 && lsArticulosVentas.getItemIds().size() == 0) {
                    // Calidad
                    if (lsCalidadCompra.getItemIds().size() == 0 && lsCalidadVentas.getItemIds().size() == 0) {
                        // Cliente / Proveedor
                        if (lsClientes.getItemIds().size() == 0 && lsProveedoresCompras.getItemIds().size() == 0 && lsProveedoresVentas.getItemIds().size() == 0) {
                            // Familias
                            if (lsFamiliasCompras.getItemIds().size() == 0 && lsFamiliasVentas.getItemIds().size() == 0) {
                                // Lotes
                                if (lsLoteCompras.getItemIds().size() == 0 && lsLoteVentas.getItemIds().size() == 0) {
                                    // Basura
                                    if (cbBasuras.getValue() == null || cbBasuras.getValue().toString().isEmpty()) {
                                        // Paises
                                        if (lsPaisesCompra.getItemIds().size() == 0 && lsPaisesVentas.getItemIds().size() == 0) {
                                            // Compras cerradas
                                            if (cbCerradas.getValue().equals("Todas")) {
                                                return false;
                                            }
                                        }
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
     * Método que nos indica si está filtrando por algún campo de compra.
     * @return True si está filtrando por algún campo de compra, false en caso contrario.
     */
    private boolean aplicaFiltrosCompras() {
        boolean result = true;
        // Comprobamos si está filtrando por algún campo.
        // Empezamos por las fechas
        if (fechaDesdeCompra.getValue() == null && fechaHastaCompra.getValue() == null) {
            // Albranes 
            if (lsAlbaranesCompras.getItemIds().size() == 0 && lsPartidasCompra.getItemIds().size() == 0) {
                // Productos
                if (lsArticulosCompras.getItemIds().size() == 0) {
                    // Calidad
                    if (lsCalidadCompra.getItemIds().size() == 0) {
                        // Proveedor
                        if (lsProveedoresCompras.getItemIds().size() == 0) {
                            // Familias
                            if (lsFamiliasCompras.getItemIds().size() == 0) {
                                // Lotes
                                if (lsLoteCompras.getItemIds().size() == 0) {
                                    // Paises
                                    if (lsPaisesCompra.getItemIds().size() == 0) {
                                        return false;
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
     * Método que nos indica si está filtrando por algún campo de venta.
     * @return True si está filtrando por algún campo de venta, false en caso contrario.
     */
    private boolean aplicaFiltrosVentas() {
        boolean result = true;
        // Comprobamos si está filtrando por algún campo.
        // Empezamos por las fechas
        if (fechaDesdeVenta.getValue() == null && fechaHastaVenta.getValue() == null) {
            // Albranes 
            if (lsAlbaranesVentas.getItemIds().size() == 0 && lsPedidosVentas.getItemIds().size() == 0) {
                // Productos
                if (lsArticulosVentas.getItemIds().size() == 0) {
                    // Calidad
                    if (lsCalidadVentas.getItemIds().size() == 0) {
                        // Cliente / Proveedor
                        if (lsClientes.getItemIds().size() == 0 && lsProveedoresVentas.getItemIds().size() == 0) {
                            // Familias
                            if (lsFamiliasVentas.getItemIds().size() == 0) {
                                // Lotes
                                //if (lsLoteVentas.getItemIds().size() == 0) {
                                // Paises
                                if (lsPaisesVentas.getItemIds().size() == 0) {
                                    result = false;
                                    //  }
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
    private void cargarCompras() {

        if (aplicaFiltrosVentas()) {

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

                mCompras = contrVista.obtenerCompras(user, time);

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

            cargarVentas();
            aplicarFiltroCompras(false, true);
            // Comprobamos si está filtrando por compras o ventas.
            if (aplicaFiltrosVentas()) {
                aplicarFiltroVentas(false);
                //  } else {
                //       aplicarFiltroCompras(false);
            }

            aplicarFiltroLineasVentas();

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
     * Método para cambiar el rango de fechas para mostrar los datos de resultados.
     */
    private void cambiarFechasVentas() {

        Date f1 = fechaDesdeVenta.getValue();
        Date f2 = fechaHastaVenta.getValue();

        // Ventas
        List<TVentasVista> lResultados = Utils.generarListaGenerica();

        if (f1 == null && f2 == null) {
            bcVentas.removeAllItems();

            mVentas = contrVista.obtenerVentasErroneas();

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

        aplicarFiltroLineasVentas();

        cargarCompras();

    }

    /**
     * Método que nos guarda en el sistema una nueva línea de compra a manija.
     */
    @SuppressWarnings("unchecked")
    private void guardarLineaCompra() {
        List<String> lIdsNuevos = bcNuevaCompra.getItemIds();
        try {

            Integer cnt = 1;

            List<String> lResults = Utils.generarListaGenerica();
            for (String id : lIdsNuevos) {
                tablaNuevaCompra.getItem(id);
                BeanItem<TComprasVista> bRes = (BeanItem<TComprasVista>) tablaNuevaCompra.getItem(id);
                TComprasVista res = bRes.getBean();

                // Comprobamos que se hayan introducido correctamente los datos fechas, números, etc.
                if (res.getFecha() != null && !res.getFecha().isEmpty()) {
                    try {
                        new SimpleDateFormat("dd/MM/yyyy").parse(res.getFecha());
                    } catch (ParseException e) {
                        Notification aviso = new Notification("El campo 'fecha' de la línea " + cnt + ", no tiene el valor correcto, el formato de fecha es 'dd/MM/yyyy", Notification.Type.ERROR_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        break;
                    }
                } else {
                    res.setFecha(new SimpleDateFormat("dd/MM/yyyy").format(Utils.generarFecha()));
                }

                if (res.getFechaCalibrado() != null && !res.getFechaCalibrado().isEmpty()) {
                    try {
                        new SimpleDateFormat("dd/MM/yyyy").parse(res.getFechaCalibrado());
                    } catch (ParseException e) {
                        Notification aviso = new Notification("El campo 'fecha' de la línea " + cnt + ", no tiene el valor correcto, el formato de fecha es 'dd/MM/yyyy", Notification.Type.ERROR_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        break;
                    }
                }

                if (res.getCajas() != null && !res.getCajas().isEmpty()) {
                    // Comprobamos que los números sean números
                    try {
                        Integer.valueOf(res.getCajas());
                    } catch (NumberFormatException e) {
                        Notification aviso = new Notification("El campo 'cajas' de la línea " + cnt + ", no tiene el valor correcto, el valor debe ser numérico sin decimales", Notification.Type.ERROR_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        break;
                    }
                }
                if (res.getKgsBruto() != null && !res.getKgsBruto().isEmpty()) {
                    try {
                        Utils.formatearValorDouble2(res.getKgsBruto());
                    } catch (NumberFormatException e) {
                        Notification aviso = new Notification("El campo 'peso bruto' de la línea " + cnt + ", no tiene el valor correcto, el valor debe ser numérico", Notification.Type.ERROR_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        break;
                    }

                }
                if (res.getTara() != null && !res.getTara().isEmpty()) {
                    try {
                        Utils.formatearValorDouble2(res.getTara());
                    } catch (NumberFormatException e) {
                        Notification aviso = new Notification("El campo 'tara' de la línea " + cnt + ", no tiene el valor correcto, el valor debe ser numérico", Notification.Type.ERROR_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        break;
                    }
                }

                if (res.getNombreDescriptivo() == null || res.getNombreDescriptivo().isEmpty()) {
                    res.setNombreDescriptivo("Creación línea compra manual. Fecha: " + new SimpleDateFormat("dd/MM/yyyy").format(Utils.generarFecha()));
                }

                res.setCentro("NATURAL TROPIC, S.L.");
                res.setUsuCrea("" + user);
                res.setFechaCrea(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Utils.generarFecha()));
                String result = contrVista.guardarLineaCompra(res, user, time);

                if (!lResults.contains(result)) {
                    lResults.add(result);
                }

                cnt++;
            }
            if (lResults.size() > 1) {
                Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo("Hay líneas que no se han podido guardar, comprueba que los datos son correctos."), Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            } else {
                if (lResults.get(0).equals(Constants.OPERACION_OK)) {
                    Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(lResults.get(0)), Notification.Type.HUMANIZED_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    bcNuevaCompra.removeAllItems();
                    tablaNuevaCompra.setVisible(false);
                    guardarCompraButton.setVisible(false);
                    borrarCompraButton.setVisible(false);
                    contrVista.forzarCargaCompras(user, time);
                    contrVista.forzarCargaVentas(user, time);
                    Page.getCurrent().reload();
                } else {
                    Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo("La operación no ha podido completarse, comprueba que los datos son correctos."), Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }

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
     * Método que nos guarda en el sistema una nueva línea de compra a manija.
     */
    @SuppressWarnings("unchecked")
    private void guardarLineasBasura() {

        DecimalFormat df = new DecimalFormat("#,##0");
        BeanItem<TComprasVista> bRes = null;
        TComprasVista res = null;

        String[] ids;
        if (idSeleccionadoCompras.contains(",")) {
            ids = idSeleccionadoCompras.split(",");
        } else {
            ids = new String[1];
            ids[0] = new String();
            ids[0] = idSeleccionadoCompras;
        }
        int cnt = 0;

        TVentasVista lBasura = null;

        try {
            for (cnt = 0; cnt < ids.length; cnt++) {
                bRes = (BeanItem<TComprasVista>) tablaCompras.getItem(ids[cnt]);
                res = bRes.getBean();

                if (res.getKgsDisponibles() > Double.valueOf(0) && res.getCerrada().equals("No")) {
                    // Cogemos los datos para crear la línea de basura
                    lBasura = new TVentasVista();
                    lBasura.setAlbaranCompra(res.getAlbaranFin());
                    lBasura.setAlbaranCompraFin(res.getAlbaranFin());
                    lBasura.setKgsNetos(res.getKgsDisponibles());
                    lBasura.setFechaVenta(Utils.generarFecha());
                    lBasura.setFechaVentaFin(Utils.generarFecha());
                    lBasura.setFamilia(res.getFamiliaFin());
                    lBasura.setFamiliaFin(res.getFamiliaFin());
                    lBasura.setCalibre("VARIOS");
                    lBasura.setCalibreFin("VARIOS");
                    lBasura.setCalidadVenta(res.getCalidadFin());
                    lBasura.setCalidadVentaFin(res.getCalidadFin());
                    lBasura.setFechaCrea(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Utils.generarFecha()));
                    lBasura.setUsuCrea("" + user);
                    lBasura.setKgs(res.getKgsDisponibles());
                    lBasura.setKgsFin(res.getKgsDisponibles());
                    lBasura.setLote(res.getLoteFin());
                    lBasura.setLoteFin(res.getLoteFin());
                    lBasura.setNombreDescriptivo(res.getNombreDescriptivo());
                    lBasura.setOrigen(res.getOrigenFin());
                    lBasura.setOrigenFin(res.getOrigenFin());
                    lBasura.setProveedor(res.getProveedorFin());
                    lBasura.setProveedorFin(res.getProveedorFin());
                    lBasura.setVariedad("BASURA " + res.getFamiliaFin());
                    lBasura.setVariedadFin("BASURA " + res.getFamiliaFin());
                    lBasura.setNumBultos("0");
                    lBasura.setNumBultosFin("0");
                    lBasura.setNumBultosPale("0");
                    lBasura.setNumBultosPaleFin("0");
                    lBasura.setCerrada("Sí");
                    lBasura.setIndBasura("Sí");

                    // Guardamos la nueva línea de basura
                    contrVista.guardarLineaVenta(lBasura, false, user, time);
                }
                // Guardamos la línea de compra con los nuevos kgs disponibles.
                res.setKgsDisponibles(Double.valueOf(0));
                res.setCerrada("Sí");
                contrVista.guardarLineaCompra(res, user, time);
            }
            if (cnt > 0) {
                contrVista.forzarCargaCompras(user, time);
                contrVista.forzarCargaVentas(user, time);
                Page.getCurrent().reload();
            }

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
                if (estado.equals("Erróneo")) {
                    return "red";
                } else if (estado.equals("Corregido")) {
                    return "azul_8";
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
        estado1.getItemProperty("Estado").setValue("Erróneo");
        estado1.getItemProperty("Estado2").setValue("Corregido");

    }

    private Boolean comprobarCambioVenta(TVentasVista v) {
        Boolean result = false;

        if (!v.getAlbaran().equals(v.getAlbaranFin())) {
            return true;
        }
        if (!v.getAlbaranCompra().equals(v.getAlbaranCompra())) {
            return true;
        }
        if (!v.getCalibre().equals(v.getCalibreFin())) {
            return true;
        }
        if (!v.getCalidadCompra().equals(v.getCalidadCompraFin())) {
            return true;
        }
        if (!v.getCalidadVenta().equals(v.getCalidadVentaFin())) {
            return true;
        }
        if (!v.getCliente().equals(v.getClienteFin())) {
            return true;
        }
        if (!v.getConfeccion().equals(v.getConfeccionFin())) {
            return true;
        }
        if (!v.getEnvase().equals(v.getEnvaseFin())) {
            return true;
        }
        if (!v.getFamilia().equals(v.getFamiliaFin())) {
            return true;
        }
        if (!v.getFechaVenta().equals(v.getFechaVentaFin())) {
            return true;
        }
        if (!v.getIdPale().equals(v.getIdPaleFin())) {
            return true;
        }
        if (!v.getKgs().equals(v.getKgsFin()) && !v.getKgs().equals(Double.valueOf(0))) {
            return true;
        }
        if (!v.getKgsEnvase().equals(v.getKgsEnvaseFin())) {
            return true;
        }
        if (!v.getKgsNetos().equals(v.getKgsNetosFin())) {
            return true;
        }
        if (!v.getLineaPedidoLote().equals(v.getLineaPedidoLoteFin())) {
            return true;
        }
        if (!v.getLineaPedidoLoteCaja().equals(v.getLineaPedidoLoteCajaFin())) {
            return true;
        }
        if (!v.getLote().equals(v.getLoteFin())) {
            return true;
        }
        if (!v.getLoteMovAlm().equals(v.getLoteMovAlmFi())) {
            return true;
        }
        if (!v.getNumBultos().equals(v.getNumBultosFin())) {
            return true;
        }
        if (!v.getNumBultosPale().equals(v.getNumBultosPaleFin())) {
            return true;
        }
        if (!v.getOrigen().equals(v.getOrigenFin())) {
            return true;
        }
        if (!v.getPedido().equals(v.getPedidoFin())) {
            return true;
        }
        if (!v.getProveedor().equals(v.getProveedorFin())) {
            return true;
        }
        if (!v.getReferencia().equals(v.getReferenciaFin())) {
            return true;
        }
        if (!v.getUndConsumo().equals(v.getUndConsumoFin())) {
            return true;
        }
        if (!v.getVariedad().equals(v.getVariedadFin())) {
            return true;
        }
        return result;
    }

    /**
     * En éste metodo se miran que los campos albaran, kgs y calidad no sean iguales
     * @return true si alguno de los campos no es igual
     */
    private Boolean compruebaDatosErroneos(TVentasVista v) {
        Boolean result = false;

        result = !v.getAlbaranCompra().equals(v.getAlbaranCompraFin()) || !v.getKgs().equals(v.getKgsFin()) || !v.getCalidadVenta().equals(v.getCalidadVentaFin()) || !v.getProveedor().equals(v.getProveedorFin());

        return result;
    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    private void mostrarCorrectos() {

        //filter = new FiltroContainer("error", "0", status);
        //bcCompras.addContainerFilter(filter);
        if (aplicaFiltrosVentas()) {
            aplicarFiltroVentas(false);
        } else {
            if (aplicaFiltrosCompras()) {
                aplicarFiltroCompras(false, true);
            }
        }

        aplicarFiltroLineasVentas();

        filter = new FiltroContainer("error", "0", status);
        bcVentas.addContainerFilter(filter);

        calcularTotales();

    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */

    private void mostrarErrores() {

        if (aplicaFiltrosVentas()) {
            aplicarFiltroVentas(false);
        } else {
            if (aplicaFiltrosCompras()) {
                aplicarFiltroCompras(false, true);
            }
        }

        aplicarFiltroLineasVentas();

        // Qiuiamos el de mostrar correctos, por si lo tiene activado.
        //filter = new FiltroContainer("error", "0", status);
        //bcCompras.removeContainerFilter(filter);
        filter = new FiltroContainer("error", "0", status);
        bcVentas.removeContainerFilter(filter);

        //filter = new FiltroContainer("error", "1", status);
        //bcCompras.addContainerFilter(filter);
        filter = new FiltroContainer("error", "1", status);
        bcVentas.addContainerFilter(filter);

        calcularTotales();

    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    private void mostrarTodos() {

        bcVentas.removeAllContainerFilters();
        bcCompras.removeAllContainerFilters();

        if (aplicaFiltrosVentas()) {
            aplicarFiltroVentas(false);
        } else {
            if (aplicaFiltrosCompras()) {
                aplicarFiltroCompras(false, true);
            }
        }

        calcularTotales();

    }

    private void accionCorregir(Integer numLotesMax, Integer numLotesGuacaMax, String opcion) {

        // Vamos al lío...
        try {

            Boolean soloBio = false;
            Boolean noBio = false;
            Boolean todos = false;

            if (opcion.equals("SOLO BIO")) {
                soloBio = true;
            } else if (opcion.equals("SOLO CONVENCIONAL")) {
                noBio = true;
            } else {
                todos = true;
            }

            Integer erroresAnteriores = mVentasErroneas.size();
            mVentasErroneas = contrVista.corregirVentasErroneas(numLotesMax, numLotesGuacaMax, soloBio, noBio, todos, user, time);

            //contrVista.inicializarTodo();

            //mCompras = contrVista.obtenerComprasGlobales(user, time);
            //mVentas = contrVista.obtenerVentasGlobales(user, time);

            //  contrVista.corregirVentasErroneas(user, time);

            // Vaciamos todo.
            mComprasEliminadas.clear();
            mVentasEliminadas.clear();
            lIdsComprasModificadas.clear();

            lIdsVentasModificadas.clear();

            //contrVista.inicializarTodo();

            bcCompras.removeAllItems();

            bcVentas.removeAllItems();

            mCompras = contrVista.obtenerCompras(user, time);

            mVentas = contrVista.obtenerVentasErroneas();

            bcCompras.removeAllItems();
            bcVentas.removeAllItems();

            List<Date> lFechas = Utils.generarListaGenerica();
            List<TComprasVista> lAux = Utils.generarListaGenerica();
            List<TVentasVista> lAux2 = Utils.generarListaGenerica();
            List<TComprasVista> lCompras = Utils.generarListaGenerica();
            List<TVentasVista> lVentas = Utils.generarListaGenerica();
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

            bcCompras.addAll(lCompras);;
            bcVentas.addAll(lVentas);

            calcularTotales();

            lIds.clear();
            lIds.addAll(mVentasErroneas.keySet());
            if (mVentasErroneas.isEmpty()) {
                Notification aviso = new Notification("Se han solucionado todos los errores identificados.", Notification.Type.HUMANIZED_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                contrVista.enviarNotificacion("[GENASOFT] CORRECCION ERRORES", "Se han solucionado todos los errores identificados.");
            } else {
                Notification aviso = new Notification("De los errores que habían " + erroresAnteriores + ", ha habido un total de " + mVentasErroneas.size() + " errores que no se han podido solucionar", Notification.Type.HUMANIZED_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                contrVista.enviarNotificacion("[GENASOFT] CORRECCION ERRORES", "De los errores que habian " + erroresAnteriores + ", ha habido un total de " + mVentasErroneas.size() + " errores que no se han podido solucionar");
            }

            fDatosMostrar.setValue("Todos");
            fModificarCampos.setValue("Solo lectura");

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

    @SuppressWarnings("unchecked")
    private void guardarRegistrosTablasExportacion() throws GenasoftException {

        List<String> lIds = Utils.generarListaGenerica();
        List<Integer> lIdsRecords = Utils.generarListaGenerica();
        if ((tablaCompras.isVisible() && !tablaCompras.getItemIds().isEmpty()) || (tablaComprasCambios.isVisible() && !tablaComprasCambios.getItemIds().isEmpty())) {
            if (!tablaCompras.getItemIds().isEmpty()) {
                lIds.addAll((Collection<? extends String>) tablaCompras.getItemIds());
                for (String id : lIds) {
                    lIdsRecords.add(Integer.valueOf(id));
                }
            } else {
                lIds.addAll((Collection<? extends String>) tablaComprasCambios.getItemIds());
                for (String id : lIds) {
                    lIdsRecords.add(Integer.valueOf(id));
                }
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
            lValores.add(Utils.formatearValorDouble(res.getNumBultos()));
            lValores.add(Utils.formatearValorDouble(res.getNumBultosPale()));
            lValores.add(res.getKgsNetosFin());
            lValores.add(res.getKgsNetos());
        }

        // Guardamos la lista de ventas que se está visualizando para construir posteriormente el fichero Excel/PDF
        contrVista.guardarVentasListadoTrazabilidades(lIdsPedidos, user, time);
        // Guardamos los totales
        contrVista.guardarTotalesVentasListadoTrazabilidades(lValores, user, time);
    }

    @SuppressWarnings("unchecked")
    private void guardarFiltrosAplicados() throws GenasoftException {
        Map<Integer, List<String>> mFiltros = new HashMap<Integer, List<String>>();

        if (lsPartidasCompra.getItemIds().size() != 0) {
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

    /**
     * Método que nos inicializa los componentes de los filtros para quitar los valores que aplican filtros.
     */
    private void quitarTodosFiltros() {
        try {
            // FECHAS
            fechaDesdeCompra.setValue(null);
            fechaHastaCompra.setValue(null);
            fechaDesdeVenta.setValue(null);
            fechaHastaVenta.setValue(null);

            // ALBARANES
            lsAlbaranesCompras.removeAllItems();
            lsAlbaranesVentas.removeAllItems();
            lsPartidasCompra.removeAllItems();
            lsPedidosVentas.removeAllItems();

            // ProductoS
            lsArticulosCompras.removeAllItems();
            lsArticulosVentas.removeAllItems();

            // CALIDAD
            lsCalidadCompra.removeAllItems();
            lsCalidadVentas.removeAllItems();

            // CLIENTE/PROVEEDOR
            lsClientes.removeAllItems();
            lsProveedoresCompras.removeAllItems();
            lsProveedoresVentas.removeAllItems();

            // FAMILIAS
            lsFamiliasCompras.removeAllItems();
            lsFamiliasVentas.removeAllItems();

            // LOTES
            lsLoteCompras.removeAllItems();
            lsLoteVentas.removeAllItems();

            // BASURA
            cbBasuras.setValue("Todos");

            // PAISES
            lsPaisesCompra.removeAllItems();
            cambiarFechasCompras();
            cambiarFechasVentas();
            mCompras = contrVista.obtenerCompras(user, time);
            mVentas = contrVista.obtenerVentasErroneas();

            lIds = Utils.generarListaGenerica();
            lIds.addAll(mVentasErroneas.keySet());

            List<Date> lFechas = Utils.generarListaGenerica();
            List<TComprasVista> lAux = Utils.generarListaGenerica();
            List<TVentasVista> lAux2 = Utils.generarListaGenerica();

            lFechas.addAll(mCompras.keySet());

            List<TComprasVista> lCompras = Utils.generarListaGenerica();
            List<TVentasVista> lVentas = Utils.generarListaGenerica();

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

            bcLineasVentas.removeAllItems();

            fDatosMostrar.setValue("Todos");

            comprobarFiltrosAplicados();

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

        if (lsTiposErrores.getItemIds().size() != 0) {
            filtr = filtr.concat("- Tipos errores venta: ");
            lFiltros = (List<String>) lsTiposErrores.getItemIds();
            for (String filtro : lFiltros) {
                filtr = filtr.concat(filtro).concat(", ");
            }
        }

        String basura = cbBasuras.getValue().toString();

        basura = "Basura: " + basura;

        filtr = filtr.concat(basura).concat(", ");

        if (filtr.equals("Filtros: ")) {
            filtr = "Sin filtros aplicados";
        } else {
            filtr = filtr.substring(0, filtr.length() - 2);
        }

        filtrosAplicados.setValue(filtr);

    }

    /**
     * Método que nos oculta los componentes de los filtros
     */
    private void ocultarComponentesFiltros() {
        cbCerradas.setVisible(false);
        cbBasuras.setVisible(false);
        cbPartidaCompra.setVisible(false);
        lsPartidasCompra.setVisible(false);
        cbAlbaranCompra.setVisible(false);
        lsAlbaranesCompras.setVisible(false);
        cbPedidoVenta.setVisible(false);
        lsPedidosVentas.setVisible(false);
        cbAlbaranVenta.setVisible(false);
        lsAlbaranesVentas.setVisible(false);
        cbArticulosCompras.setVisible(false);
        lsArticulosCompras.setVisible(false);
        cbArticulosVentas.setVisible(false);
        lsArticulosVentas.setVisible(false);
        cbFamiliasCompras.setVisible(false);
        lsFamiliasCompras.setVisible(false);
        cbFamiliasVentas.setVisible(false);
        lsFamiliasVentas.setVisible(false);
        cbPaisesCompras.setVisible(false);
        lsPaisesCompra.setVisible(false);
        cbPaisesVentas.setVisible(false);
        lsPaisesVentas.setVisible(false);
        cbGgnCompra.setVisible(false);
        cbCalidadCompra.setVisible(false);
        lsCalidadCompra.setVisible(false);
        cbCalidadVenta.setVisible(false);
        lsCalidadVentas.setVisible(false);
        cbProveedoresCompras.setVisible(false);
        lsProveedoresCompras.setVisible(false);
        cbProveedoreVentas.setVisible(false);
        lsProveedoresVentas.setVisible(false);
        cbClientes.setVisible(false);
        lsClientes.setVisible(false);
        cbLoteCompra.setVisible(false);
        lsLoteCompras.setVisible(false);
        cbLoteVenta.setVisible(false);
        lsLoteVentas.setVisible(false);
        cbTipoErroresVenta.setVisible(false);
        lsTiposErrores.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    private void aplicarCambios(Integer numLotesMax, Integer numLotesGuacaMax, String opcion) {

        Item it = null;
        Property<?> prop = null;
        ComboBox cBox = null;
        Boolean indPedido = false;
        Boolean indAlbaran = false;
        Boolean indCalibre = false;
        Boolean indTraza = false;
        Boolean indProv = false;
        Boolean indKgs = false;
        Boolean indVaried = false;
        Boolean indOrig = false;
        Boolean indCert = false;
        Boolean indProducto = false;

        it = tablaVentasCambiosTodos.getItem(1);

        // Cambiar todos los pedidos
        prop = it.getItemProperty("¿Cambiar todos pedidos?");
        cBox = (ComboBox) prop.getValue();
        if (cBox.getValue().equals("Sí")) {
            indPedido = true;
            cBox.setValue("No");
        }

        // Cambiar todos los albaranes
        prop = it.getItemProperty("¿Cambiar todos albaranes?");
        cBox = (ComboBox) prop.getValue();
        if (cBox.getValue().equals("Sí")) {
            indAlbaran = true;
            cBox.setValue("No");
        }

        // Cambiar todos los calibres
        prop = it.getItemProperty("¿Cambiar todos calibres?");
        cBox = (ComboBox) prop.getValue();
        if (cBox.getValue().equals("Sí")) {
            indCalibre = true;
            cBox.setValue("No");
        }

        // Cambiar todos los Kgs
        prop = it.getItemProperty("¿Cambiar todos kgs?");
        cBox = (ComboBox) prop.getValue();
        if (cBox.getValue().equals("Sí")) {
            indKgs = true;
            cBox.setValue("No");
        }

        // Cambiar todas las variedades
        prop = it.getItemProperty("¿Cambiar todas variedades?");
        cBox = (ComboBox) prop.getValue();
        if (cBox.getValue().equals("Sí")) {
            indVaried = true;
            cBox.setValue("No");
        }

        // Cambiar todos los orígenes
        prop = it.getItemProperty("¿Cambiar todos orígenes?");
        cBox = (ComboBox) prop.getValue();
        if (cBox.getValue().equals("Sí")) {
            indOrig = true;
            cBox.setValue("No");
        }

        // Cambiar todas las certificaciones
        prop = it.getItemProperty("¿Cambiar todas certificaciones?");
        cBox = (ComboBox) prop.getValue();
        if (cBox.getValue().equals("Sí")) {
            indCert = true;
            cBox.setValue("No");
        }

        // Cambiar todos los productos
        prop = it.getItemProperty("¿Cambiar todos productos?");
        cBox = (ComboBox) prop.getValue();
        if (cBox.getValue().equals("Sí")) {
            cBox.setValue("No");
            indProducto = true;
        }
        // Empezaremos por lo fácil, cogemos los registros de los diccionarios que han sido eliminados.
        try {

            List<String> lIds = Utils.generarListaGenerica();

            // Eliminamos las compras
            lIds.addAll(mComprasEliminadas.keySet());

            List<Integer> lIdsComprasEliminadas = Utils.generarListaGenerica();
            for (String key : lIds) {
                lIdsComprasEliminadas.add(Integer.valueOf(key));
            }

            lIds.clear();
            // Eliminamos las ventas
            lIds.addAll(mVentasEliminadas.keySet());

            List<Integer> lIdsVentasEliminadas = Utils.generarListaGenerica();
            for (String key : lIds) {
                lIdsVentasEliminadas.add(Integer.valueOf(key));
            }

            if (!lIdsVentasEliminadas.isEmpty()) {
                contrVista.eliminarVentasIds(lIdsVentasEliminadas, user, time);
            }

            BeanItem<TComprasVista> bRes = null;
            TComprasVista res = null;

            // Guardamos las modificaciones realizadas en las compras.
            for (String idCompra : lIdsComprasModificadas) {
                bRes = (BeanItem<TComprasVista>) tablaCompras.getItem(idCompra);
                // Es posible que se haya eliminado.
                if (bRes != null) {
                    res = bRes.getBean();
                    contrVista.guardarLineaCompra(res, user, time);
                }
            }

            BeanItem<TVentasVista> bResV = null;
            TVentasVista resV = null;

            // Miramos si ha marcado algún combo en la tabla de ventas cambios todos
            // En ese caso, no hay registros modificados, cogemos el primer registro de la tabla de ventas (que se está mostrando) en función de/l combo(s) indicados, vamos cambiando

            // Si algún ComboBox está marcado como si, nos fijamos en el primer registro para ir aplicando cambios en cascada.
            if (indPedido || indAlbaran || indCalibre || indTraza || indProv || indKgs || indVaried || indOrig || indCert || indProducto) {
                // Cogemos el primer registro como referencia, para aplicar cambios ae.
                lIds = bcVentas.getItemIds();

                bResV = bcVentas.getItem(lIds.get(0));
                TVentasVista vVista = bResV.getBean();
                Boolean primero = true;
                for (String idVenta : lIds) {
                    bResV = (BeanItem<TVentasVista>) tablaVentas.getItem(idVenta);
                    // Es posible que se haya eliminado.

                    // El primer registro ya lo tenemos nutrido.
                    if (primero) {
                        primero = false;
                        vVista.setCerrada("No");
                        contrVista.guardarLineaVenta(vVista, true, user, time);
                        continue;
                    } else {
                        if (bResV != null) {
                            primero = false;
                            resV = bResV.getBean();
                            // Aplicamos los cambios que ha indicado desde la vista
                            if (indPedido) {
                                resV.setPedidoFin(vVista.getPedidoFin());
                            }
                            if (indAlbaran) {
                                resV.setAlbaranFin(vVista.getAlbaranFin());
                            }
                            if (indCalibre) {
                                resV.setCalibreFin(vVista.getCalibreFin());
                            }
                            if (indKgs) {
                                resV.setKgsFin(vVista.getKgsFin());
                            }
                            if (indVaried) {
                                resV.setVariedadFin(vVista.getVariedadFin());
                            }
                            if (indOrig) {
                                resV.setOrigenFin(vVista.getOrigenFin());
                            }
                            if (indCert) {
                                resV.setCalidadVentaFin(vVista.getCalidadVentaFin());
                            }
                            if (indProducto) {
                                resV.setFamiliaFin(vVista.getFamiliaFin());
                            }
                            contrVista.guardarLineaVenta(resV, true, user, time);
                        } else {
                            continue;
                        }
                    }
                }
            } else {

                // Guardamos las modificaciones realizadas en las ventas.
                for (String idVenta : lIdsVentasModificadas) {
                    bResV = (BeanItem<TVentasVista>) tablaVentas.getItem(idVenta);
                    // Es posible que se haya eliminado.
                    if (bResV != null) {
                        resV = bResV.getBean();
                        // HAy que mirar la nueva lógica, abrir las lineas de venta en que se hayan corregido automaticamente.
                        contrVista.guardarLineaVenta(resV, true, user, time);
                    }
                }
            }

            // Vaciamos todo.
            mComprasEliminadas.clear();
            mVentasEliminadas.clear();
            lIdsComprasModificadas.clear();
            lIdsVentasModificadas.clear();

            //contrVista.reestablecerCompras();
            //contrVista.reestablecerVentas();

            mCompras = contrVista.obtenerCompras(user, time);
            mVentas = contrVista.obtenerVentasErroneas();

            accionCorregir(numLotesMax, numLotesGuacaMax, opcion);

            calcularTotales();
            //Notification aviso = new Notification("Cambios aplicados correctamente", Notification.Type.HUMANIZED_MESSAGE);
            //aviso.setPosition(Position.MIDDLE_CENTER);
            //aviso.show(Page.getCurrent());

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

    @SuppressWarnings("unchecked")
    private void cargarLotesCompras() {
        lLotesComprasSelecc.clear();

        List<String> lIdsCompras = Utils.generarListaGenerica();

        lIdsCompras.addAll(bcCompras.getItemIds());

        BeanItem<TComprasVista> bRes = null;
        TComprasVista res = null;

        for (String idC : lIdsCompras) {
            bRes = (BeanItem<TComprasVista>) tablaCompras.getItem(idC);
            res = bRes.getBean();

            if (!lLotesComprasSelecc.contains(res.getLoteFin())) {
                lLotesComprasSelecc.add(res.getLoteFin());
            }
        }

    }

}
