/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.comprasVentas;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TComprasFictVista;
import com.dina.genasoft.db.entity.TComprasVista;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TVentasFictVista;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.TablaGenerica;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
import com.vaadin.annotations.Theme;
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
@SpringView(name = VistaComprasVentas.NAME)
public class VistaComprasVentas extends CustomComponent implements View ,Button.ClickListener {
    /** El nombre de la vista.*/
    public static final String                       NAME                = "vVentasCompras";
    /** Necesario para mostrar las compras. */
    private BeanContainer<String, TComprasFictVista> bcCompras;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TVentasFictVista>  bcVentas;
    /** Necesario para mostrar las compras. */
    private BeanContainer<String, TComprasFictVista> bcComprasTotales;
    /** Necesario para mostrar las ventas. */
    private BeanContainer<String, TVentasFictVista>  bcVentasTotales;
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas                        contrVista;
    /** El boton para aplicar los filtros indicados.*/
    private Button                                   aplicarFiltroButton;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                                   nuevaCompraButton;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                                   excelButton;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                                   pdfButton;
    /** El boton para guardar los cambios introducidos.*/
    private Button                                   guardarButton;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                                   quitarFiltrosButton;
    // Elementos para realizar busquedas
    /** Para filtrar por estado. */
    private ComboBox                                 fModificarCampos;
    /** Para filtrar por estado. */
    private ComboBox                                 fMostrarTabla;
    /** Para indicar qué tipo de filtro queremos aplicar. */
    private ComboBox                                 fTipoFiltro;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                                   appName;
    /** El ID del cliente seleccionado.*/
    private String                                   idSeleccionadoCompras;
    /** El ID del cliente seleccionado.*/
    private String                                   idSeleccionadoVentas;
    /** El ID del cliente seleccionado.*/
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger            log                 = org.slf4j.LoggerFactory.getLogger(VistaComprasVentas.class);
    /** El usuario que está logado. */
    private Integer                                  user                = null;
    /** La fecha en que se inició sesión. */
    private Long                                     time                = null;
    /** Tabla para mostrar las compras del sistema. */
    private Table                                    tablaCompras        = null;
    /** Tabla para mostrar las ventas del sistema. */
    private Table                                    tablaVentas         = null;
    /** Etiqueta para mostrar el texto de las tablas de compras. */
    private Label                                    textoTablaCompras;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                                    tablaComprasTotales = null;
    /** Tabla para mostrar los pedidos del sistema. */
    private Table                                    tablaVentasTotales  = null;
    /** Etiqueta para mostrar el texto de las tablas de ventas. */
    private Label                                    textoTablaVentas;
    /** Etiqueta para mostrar el texto de las tablas de ventas. */
    private Label                                    textoTablaVentas2;
    private VerticalLayout                           tCompras;
    private VerticalLayout                           tVentas;
    /**********                                 FILTROS                                 **********/
    /** Para filtrar por albarán de compra.  */
    private ComboBox                                 cbAlbaranCompra;
    /** Para filtrar por calidad de compra.  */
    private ComboBox                                 cbLoteCompra;
    /** Para filtrar por calidad de compra.  */
    private ComboBox                                 cbPartidaCompra;
    private HorizontalLayout                         cabeceraPantalla;
    private VerticalLayout                           filtro;
    /********* LISTAS PARA FILTROS *********/
    /** Lista con los albaranes identificadas en las compras. */
    private List<String>                             lAlbaranesCompras;
    /** Lista con los lotes identificadas en las compras. */
    private List<String>                             lLotesCompras;
    /** Lista con las partidas identificadas en las compras. */
    private List<String>                             lPartidasCompras;
    /********* Listas para mostrar los datos por lo que se está filtrando *********/
    /** Lista de Nº de albaranes por los que está filtrando. */
    private ListSelect                               lsAlbaranesCompras;
    /** Lista de lotes por los que está filtrando. */
    private ListSelect                               lsLoteCompras;
    /** Lista de lotes por los que está filtrando. */
    private ListSelect                               lsPartidasCompra;
    private TPermisos                                permisos;
    private TEmpleados                               empleado;
    private HorizontalLayout                         totales;
    private Label                                    filtrosAplicados;
    private HorizontalLayout                         botones;
    private List<String>                             lIdsVentasModificadas;

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
            List<TComprasFictVista> lCompras = Utils.generarListaGenerica();
            List<TVentasFictVista> lVentas = Utils.generarListaGenerica();
            try {
                idSeleccionadoCompras = null;

                lIdsVentasModificadas = Utils.generarListaGenerica();

                bcCompras = new BeanContainer<>(TComprasFictVista.class);
                bcCompras.setBeanIdProperty("id");

                bcVentas = new BeanContainer<>(TVentasFictVista.class);
                bcVentas.setBeanIdProperty("id");

                bcComprasTotales = new BeanContainer<>(TComprasFictVista.class);
                bcComprasTotales.setBeanIdProperty("id");

                bcVentasTotales = new BeanContainer<>(TVentasFictVista.class);
                bcVentasTotales.setBeanIdProperty("id");

                Label texto = new Label("Compras - Ventas");
                texto.setStyleName("tituloTamano16");
                // Incluimos en el layout los componentes

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

                if (!Utils.booleanFromInteger(permisos.getComprasVentasFict())) {
                    Notification aviso = new Notification("No se tienen permisos para acceder a la pantalla indicada", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                    return;
                }

                String parametros = event.getParameters();

                if (parametros != null && !parametros.isEmpty()) {
                    Integer idCompra = Integer.valueOf(parametros);

                    TComprasFictVista aux = contrVista.obtenerCompraFicticiaVistaPorId(idCompra, user, time);

                    if (aux != null) {
                        lCompras.add(aux);

                        List<String> lNumAlb = Utils.generarListaGenerica();
                        lNumAlb.add(aux.getAlbaranFin());

                        List<TComprasFictVista> lComprasAux = contrVista.obtenerComprasFictAlbaranesLotesPartidas(lNumAlb, null, null, user, time);

                        List<Integer> lIds = Utils.generarListaGenerica();
                        for (TComprasFictVista compAu : lComprasAux) {
                            lIds.add(Integer.valueOf(compAu.getId()));
                        }
                        lVentas = contrVista.obtenerVentasFictAlbaranCompra(lIds, user, time);
                    }
                } else {
                    parametros = null;
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

                bcCompras.removeAllItems();
                bcCompras.addAll(lCompras);

                bcVentas.removeAllItems();
                bcVentas.addAll(lVentas);

                Label tituloFiltrar = new Label("Filtrar");
                tituloFiltrar.setStyleName("tituloTamano12");

                lAlbaranesCompras = contrVista.obtenerAlbaranesComprasFict(user, time);
                lLotesCompras = contrVista.obtenerLotesComprasFict(user, time);
                lPartidasCompras = contrVista.obtenerPartidasComprasFict(user, time);

                botones = new HorizontalLayout();
                botones.setSpacing(true);

                // Creamos el componente filtro.
                crearComponenteFiltro();

                // Creamos la botonera.                
                viewLayout.addComponent(cabeceraPantalla);
                viewLayout.addComponent(filtro);

                viewLayout.addComponent(botones);

                HorizontalLayout hotTot = new HorizontalLayout();
                hotTot.setSpacing(true);
                hotTot.addComponent(tablaComprasTotales);
                hotTot.addComponent(tablaVentasTotales);
                hotTot.addStyleName("posLayoutFixed");

                textoTablaCompras = new Label("Compras");
                textoTablaCompras.setStyleName("tituloTamano18");

                textoTablaVentas = new Label("Ventas");
                textoTablaVentas.setStyleName("tituloTamano18");
                textoTablaVentas2 = new Label("Ventas totales");
                textoTablaVentas2.setStyleName("tituloTamano14Center");

                tCompras = new VerticalLayout();
                tCompras.setSpacing(true);
                tCompras.addComponent(textoTablaCompras);
                tCompras.setComponentAlignment(textoTablaCompras, Alignment.TOP_CENTER);
                tCompras.addComponent(tablaCompras);

                tVentas = new VerticalLayout();
                tVentas.setSpacing(true);
                tVentas.addComponent(textoTablaVentas);
                tVentas.setComponentAlignment(textoTablaVentas, Alignment.TOP_CENTER);
                tVentas.addComponent(tablaVentas);

                totales = new HorizontalLayout();
                totales.setSpacing(true);
                totales.addStyleName("posLayoutFixed");
                totales.addComponent(filtrosAplicados);

                HorizontalLayout tablas = new HorizontalLayout();
                //tablas.addStyleName("posLayoutFixed");

                tablas.addComponent(tCompras);
                //tablaCompras.setWidth(150, Sizeable.Unit.EM);
                tablas.addComponent(tVentas);
                //tablaVentas.setWidth(150, Sizeable.Unit.EM);
                tablas.setSizeUndefined();

                HorizontalLayout horBot = new HorizontalLayout();
                horBot.setSpacing(true);
                horBot.addComponent(aplicarFiltroButton);
                horBot.addComponent(quitarFiltrosButton);
                horBot.addComponent(guardarButton);
                horBot.addComponent(nuevaCompraButton);
                horBot.addComponent(excelButton);
                horBot.addComponent(pdfButton);

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
                viewLayout.addComponent(hotTot);
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
        quitarFiltrosButton = new Button("Quitar todos los filtros", this);
        nuevaCompraButton = new Button("Nueva compra desde venta", this);
        guardarButton = new Button("Aplicar cambios", this);
        aplicarFiltroButton = new Button("Aplicar filtros", this);
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

        if (event.getButton().equals(guardarButton)) {
            try {
                BeanItem<TVentasFictVista> bResV = null;
                TVentasFictVista resV = null;

                // Guardamos las modificaciones realizadas en las ventas.
                for (String idVenta : lIdsVentasModificadas) {
                    bResV = (BeanItem<TVentasFictVista>) tablaVentas.getItem(idVenta);
                    // Es posible que se haya eliminado.
                    if (bResV != null) {
                        resV = bResV.getBean();
                        // HAy que mirar la nueva lógica, abrir las lineas de venta en que se hayan corregido automaticamente.
                        contrVista.guardarLineaVentaFict(resV, true, user, time);
                    }
                }

                lIdsVentasModificadas.clear();
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
        } else if (event.getButton().equals(excelButton)) {
            if (fModificarCampos.getValue().equals("Solo lectura")) {

                if (tablaCompras.getItemIds().isEmpty() && tablaVentas.getItemIds().isEmpty()) {
                    Notification aviso = new Notification("No se han identificado registros a exportar", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                } else {
                    try {
                        guardarRegistrosTablasExportacion();

                        Page.getCurrent().open("/exportarComprasVentasExcel?idEmpleado=" + user, "_blank");

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
            if (tablaCompras.isVisible() || tablaVentas.isVisible()) {
                if (!tablaCompras.getItemIds().isEmpty() || !tablaVentas.getItemIds().isEmpty()) {
                    try {
                        guardarRegistrosTablasExportacion();

                        Page.getCurrent().open("/exportarTrazabilidadesFictPdf?idEmpleado=" + user + "&idPedidos=" + idSeleccionadoVentas, "_blank");

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
                    Notification aviso = new Notification("No se han identificado pedidos a exportar", Notification.Type.WARNING_MESSAGE);
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
        } else if (event.getButton().equals(nuevaCompraButton)) {
            getUI().getNavigator().navigateTo(VistaVentasDesdeCompra.NAME);
        } else if (event.getButton().equals(aplicarFiltroButton)) {
            if (lsAlbaranesCompras.getItemIds().isEmpty() && lsLoteCompras.getItemIds().isEmpty() && lsPartidasCompra.getItemIds().isEmpty()) {
                Notification aviso = new Notification("Se debe indicar al menos un campo para realizar la búsqueda de la compra", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                return;
            }
            try {

                List<String> lAlbaranes = Utils.generarListaGenerica();
                List<String> lLotes = Utils.generarListaGenerica();
                List<String> lPartidas = Utils.generarListaGenerica();

                lAlbaranes.addAll((Collection<? extends String>) lsAlbaranesCompras.getItemIds());
                lLotes.addAll((Collection<? extends String>) lsLoteCompras.getItemIds());
                lPartidas.addAll((Collection<? extends String>) lsPartidasCompra.getItemIds());

                if (lAlbaranes.isEmpty()) {
                    lAlbaranes = null;
                }

                if (lLotes.isEmpty()) {
                    lLotes = null;
                }

                if (lPartidas.isEmpty()) {
                    lPartidas = null;
                }

                List<TComprasFictVista> lCompras = contrVista.obtenerComprasFictAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas, user, time);

                bcCompras.removeAllItems();
                bcCompras.addAll(lCompras);

                cargarVentas();

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

        }
    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaCompras(TPermisos permisos) {
        tablaCompras = new TablaGenerica(new Object[] { "albaranFin", "fechaFin", "partidaFin", "cajasFin", "loteFin", "pesoNetoFin", "variedadFin", "proveedorFin", "origenFin", "familiaFin", "ggnFin", "calidadFin" }, new String[] { "Albarán", "Fecha", "Partida", "Nº de cajas", "Trazabilidad", "Peso neto", "Variedad", "Entidad", "Orígen", "Producto", "Global Gap", "Certificación" }, bcCompras);
        tablaCompras.addStyleName("big striped");
        tablaCompras.setEditable(false);
        tablaCompras.setMultiSelect(true);
        tablaCompras.setPageLength(30);
        tablaCompras.setId("1");
        tablaCompras.setColumnWidth("fechaFin", 70);

        tablaCompras.setDragMode(TableDragMode.ROW);
        // Para mostrar/ocultar columnas de la tabla
        tablaCompras.setColumnCollapsingAllowed(true);
        // Para cambiar el orden las columnas
        tablaCompras.setColumnReorderingAllowed(true);

        // Establecemos tamaño fijo en columnas específicas.        
        tablaCompras.setMultiSelect(true);

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

    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaVentas(TPermisos permisos) {
        tablaVentas = new TablaGenerica(new Object[] { "pedidoFin", "albaranFin", "calibreFin", "loteFin", "numBultosFin", "clienteFin", "fechaVentaFin", "kgsFin", "variedadFin", "origenFin", "calidadVentaFin", "familiaFin" }, new String[] { "Pedido de venta", "Albarán de venta", "Calibre", "Trazabilidad", "Bultos mov. venta", "Entidad", "Fecha salida", "Kilos", "Variedad", "Orígen venta", "Certificación venta", "Producto" }, bcVentas);
        tablaVentas.addStyleName("big striped");
        tablaVentas.setEditable(false);
        tablaVentas.setPageLength(30);
        tablaVentas.setId("2");
        tablaVentas.setMultiSelect(true);

        // Para mostrar/ocultar columnas de la tabla
        tablaVentas.setColumnCollapsingAllowed(true);
        // Para cambiar el orden las columnas
        tablaVentas.setColumnReorderingAllowed(true);

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
            }
        });
    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaComprasTotales() {
        tablaComprasTotales = new TablaGenerica(new Object[] { "pesoNeto" }, new String[] { "Kilos totales Compra(s)" }, bcComprasTotales);
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
    private void crearTablaVentasTotales() {
        tablaVentasTotales = new TablaGenerica(new Object[] { "kgsNetosFin", }, new String[] { "Kilos totales venta(s)" }, bcVentasTotales);
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

        fTipoFiltro = new ComboBox("Filtro");
        fTipoFiltro.addItem("Albarán compra");
        fTipoFiltro.addItem("Partida compra");
        fTipoFiltro.addItem("Trazabilidad compra");
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
                    } else if (fTipoFiltro.getValue().equals("Partida compra")) {
                        cbPartidaCompra.setVisible(true);
                        lsPartidasCompra.setVisible(true);
                    } else if (fTipoFiltro.getValue().equals("Trazabilidad compra")) {
                        cbLoteCompra.setVisible(true);
                        lsLoteCompras.setVisible(true);
                    }
                }
            }
        });

        fMostrarTabla = new ComboBox();
        fMostrarTabla.setCaption("Mostrar tabla:");
        fMostrarTabla.addItem("Todas");
        fMostrarTabla.addItem("Compras");
        fMostrarTabla.addItem("Ventas");
        fMostrarTabla.setValue("Todas");
        fMostrarTabla.setNullSelectionAllowed(false);
        fMostrarTabla.setFilteringMode(FilteringMode.CONTAINS);

        fMostrarTabla.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (fMostrarTabla.getValue().equals("Todas")) {
                    tCompras.setVisible(true);
                    tVentas.setVisible(true);
                    idSeleccionadoCompras = null;
                    idSeleccionadoVentas = null;

                } else if (fMostrarTabla.getValue().equals("Compras")) {
                    tCompras.setVisible(true);
                    tVentas.setVisible(false);
                    idSeleccionadoCompras = null;
                    idSeleccionadoVentas = null;

                } else if (fMostrarTabla.getValue().equals("Ventas")) {
                    tCompras.setVisible(false);
                    tVentas.setVisible(true);
                    idSeleccionadoCompras = null;
                    idSeleccionadoVentas = null;

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
                    tablaVentas.setEditable(false);
                } else if (fModificarCampos.getValue().equals("Edición")) {
                    //tablaCompras.setEditable(true);
                    tablaVentas.setEditable(true);
                }

            }
        });

        cabeceraPantalla.addComponent(fMostrarTabla);
        cabeceraPantalla.addComponent(fModificarCampos);

        cabeceraPantalla.setVisible(true);

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
                    comprobarFiltrosAplicados();
                }
            }
        });

        // Combobox con los albaranes

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
                    comprobarFiltrosAplicados();
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
                    comprobarFiltrosAplicados();
                }
            }
        });

        /************************************************************************************************************/

        // Añadimos los componentes de los diltros
        HorizontalLayout hComps = new HorizontalLayout();
        hComps.setSpacing(true);
        hComps.addComponent(cbAlbaranCompra);
        hComps.addComponent(lsAlbaranesCompras);
        hComps.addComponent(cbPartidaCompra);
        hComps.addComponent(lsPartidasCompra);
        hComps.addComponent(cbLoteCompra);
        hComps.addComponent(lsLoteCompras);

        ocultarComponentesFiltros();

        //filtro.addComponent(table);

        filtro.addComponent(fTipoFiltro);
        filtro.addComponent(hComps);

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

        BeanItem<TComprasFictVista> bRes = null;
        TComprasFictVista res = null;

        for (String id : lIds) {
            bRes = (BeanItem<TComprasFictVista>) tablaCompras.getItem(id);
            res = bRes.getBean();
            totalKgsDisponibles += res.getKgsDisponibles();
            totalKgsCompras += res.getPesoNetoFin();
        }

        // Calculamos los conceptos de ventas
        lIds = bcVentas.getItemIds();

        BeanItem<TVentasFictVista> bResV = null;
        TVentasFictVista resV = null;

        for (String id : lIds) {
            bResV = (BeanItem<TVentasFictVista>) tablaVentas.getItem(id);
            resV = bResV.getBean();

            totalBultosVentas += Integer.valueOf(resV.getNumBultosPaleFin());
            totalBultosPaleVentas += Integer.valueOf(resV.getNumBultosFin());
            totalKgsVentas += resV.getKgsFin();
            totalKgsNetosVentas += resV.getKgsNetosFin();
        }

        // Compras totales
        TComprasFictVista cTotales = new TComprasFictVista();
        cTotales.setId("1");
        //  if (!fDatosMostrar.getValue().equals("Errores")) {
        //     cTotales.setKgsDisponibles(totalKgsCompras - totalKgsVentas);
        // } else {
        cTotales.setKgsDisponibles(totalKgsDisponibles);
        //  }
        cTotales.setPesoNeto(totalKgsCompras);

        // Ventas totales
        TVentasFictVista vTotales = new TVentasFictVista();
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
     * Método que se encarga de cargar las ventas en función de las compras que se están mostrando
     * Hay que tener en cuenta lo siguiente:
     * - Las ventas se cargan a partir del ID de la linea de compra
     */
    @SuppressWarnings("unchecked")
    private void cargarVentas() {

        // Vamos a emplear un árbol con clave ID de linea de compra, y valor, listado de lineas de venta.
        // En primer lugar, obtenemos las líneas de compra que se están mostrando en el listado de compras.
        List<String> lBeansCompras = bcCompras.getItemIds();
        List<Integer> lCompras = Utils.generarListaGenerica();
        BeanItem<TComprasFictVista> bRes = null;
        TComprasFictVista res = null;
        for (String id : lBeansCompras) {
            bRes = (BeanItem<TComprasFictVista>) tablaCompras.getItem(id);
            res = bRes.getBean();
            if (!lCompras.contains(Integer.valueOf(res.getId()))) {
                lCompras.add(Integer.valueOf(res.getId()));
            }
        }
        try {

            // Obtenemos las líneas de venta a partir de las compras.
            bcVentas.removeAllItems();
            List<TVentasFictVista> lVentas = contrVista.obtenerVentasFictAlbaranCompra(lCompras, user, time);

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

    @SuppressWarnings("unchecked")
    private void guardarRegistrosTablasExportacion() throws GenasoftException {

        List<String> lIds = Utils.generarListaGenerica();
        List<Integer> lIdsRecords = Utils.generarListaGenerica();
        if ((tablaCompras.isVisible() && !tablaCompras.getItemIds().isEmpty())) {
            if (!tablaCompras.getItemIds().isEmpty()) {
                lIds.addAll((Collection<? extends String>) tablaCompras.getItemIds());
                for (String id : lIds) {
                    lIdsRecords.add(Integer.valueOf(id));
                }
            }
        }

        List<Double> lValores = Utils.generarListaGenerica();
        // Si se está visualizando los totales, lo guardamos.
        if (tablaComprasTotales.isVisible()) {
            // Obtenemos los totales.
            BeanItem<TComprasFictVista> bRes = null;
            TComprasFictVista res = null;
            bRes = (BeanItem<TComprasFictVista>) tablaComprasTotales.getItem("1");
            res = bRes.getBean();
            lValores.add(res.getPesoNeto());
            lValores.add(res.getKgsDisponibles());
        }

        // Guardamos la lista de compras que se está visualizando para construir posteriormente el fichero Excel/PDF
        contrVista.guardarComprasListadoComprasVentas(lIdsRecords, user, time);
        // Guardamos los totales
        contrVista.guardarTotalesComprasComprasVentas(lValores, user, time);

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
            BeanItem<TVentasFictVista> bRes = null;
            TVentasFictVista res = null;
            bRes = (BeanItem<TVentasFictVista>) tablaVentasTotales.getItem("1");
            res = bRes.getBean();
            lValores.add(Utils.formatearValorDouble(res.getNumBultos()));
            lValores.add(Utils.formatearValorDouble(res.getNumBultosPale()));
            lValores.add(res.getKgsNetosFin());
            lValores.add(res.getKgsNetos());
        }

        // Guardamos la lista de ventas que se está visualizando para construir posteriormente el fichero Excel/PDF
        contrVista.guardarVentasListadoComprasVentas(lIdsPedidos, user, time);
        // Guardamos los totales
        contrVista.guardarTotalesVentasListadoComprasVentas(lValores, user, time);
    }

    /**
     * Método que nos oculta los componentes de los filtros
     */
    private void ocultarComponentesFiltros() {

        cbPartidaCompra.setVisible(false);
        lsPartidasCompra.setVisible(false);
        cbAlbaranCompra.setVisible(false);
        lsAlbaranesCompras.setVisible(false);

        cbLoteCompra.setVisible(false);
        lsLoteCompras.setVisible(false);

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

        if (lsLoteCompras.getItemIds().size() != 0) {
            filtr = filtr.concat("- Trazabilidad compra: ");
            lFiltros = (List<String>) lsLoteCompras.getItemIds();
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

    /**
     * Método que nos inicializa los componentes de los filtros para quitar los valores que aplican filtros.
     */
    private void quitarTodosFiltros() {

        // ALBARANES
        lsAlbaranesCompras.removeAllItems();

        lsPartidasCompra.removeAllItems();

        // LOTES
        lsLoteCompras.removeAllItems();

        bcCompras.removeAllItems();

        bcVentas.removeAllItems();

        comprobarFiltrosAplicados();

    }

    @SuppressWarnings("unchecked")
    private void generarVentas(Object obj, Date fechaCompra) {
        try {
            List<String> lIds = Utils.generarListaGenerica();
            lIds.addAll(bcCompras.getItemIds());

            String calidad;

            if (obj == null) {
                calidad = null;
            } else {
                calidad = obj.toString();
            }

            BeanItem<TComprasVista> bRes = null;
            TComprasVista res = null;

            // Por cada línea de compra que se está mostrando, creamos la(s)ventas correspondientes.
            for (String id : lIds) {
                bRes = (BeanItem<TComprasVista>) tablaCompras.getItem(id);
                res = bRes.getBean();

                contrVista.generarVentasDesdeCompra(res, fechaCompra, calidad, user, time);
            }

            Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(Constants.OPERACION_OK), Notification.Type.HUMANIZED_MESSAGE);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
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
        }
    }

}
