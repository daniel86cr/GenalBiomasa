/**
x * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.controlpt;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TControlProductoTerminado;
import com.dina.genasoft.db.entity.TControlProductoTerminadoFotos;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesBrix;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCaja;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCalibre;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesConfeccion;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesPenetromia;
import com.dina.genasoft.db.entity.TDiametrosProducto;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TLineaControlProductoTerminado;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TProductoCalibre;
import com.dina.genasoft.db.entity.TProductos;
import com.dina.genasoft.db.entity.TVariedad;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.UploadImage;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.ProductoEnum;
import com.dina.genasoft.utils.enums.RolesEnum;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
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
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

/**
 * @author Daniel Carmona Romero
 * Vista para modificar/visualizar un control de PT.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaControlPt.NAME)
@Component
public class VistaControlPt extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas                           contrVista;
    /** El nombre de la vista.*/
    public static final String                          NAME                       = "vControlPt";
    /** El boton para crear el proveedor.*/
    private Button                                      nuevoButton;
    /** El boton para crear el proveedor.*/
    private Button                                      crearButton;
    /** El botón principal del segmento. Muestra la información principal del control de producto terminado.*/
    private Button                                      listadoButton;
    /** El botón principal del segmento. Muestra la información principal del control de producto terminado.*/
    private Button                                      principalButton;
    /** Muestra las fotos asociadas al control de producto terminado .*/
    private Button                                      fotosButton;
    /** Muestra las fotos asociadas al control de producto terminado .*/
    private Button                                      eliminarFotoCajaButton;
    /** Muestra las fotos asociadas al control de producto terminado .*/
    private Button                                      subirFotosButton;
    /** Muestra las fotos asociadas al control de producto terminado .*/
    private Button                                      firmarButton;
    /** Muestra las fotos asociadas al control de producto terminado .*/
    private Button                                      guardarFotoButton;
    /** Muestra las observaciones asociadas al control de producto terminado .*/
    private Button                                      cerrarSesionButton;
    /** Muestra las observaciones asociadas al control de producto terminado .*/
    private Button                                      observacionesButton;
    /** El boton para añadir pesajes al listado de cajas. */
    private Button                                      incluirPesajeCajaButton;
    /** El boton para eliminar pesajes del listado de cajas. */
    private Button                                      eliminarPesajeCajaButton;
    /** El boton para añadir pesajes al listado de confecciones. */
    private Button                                      incluirPesajeConfeccionButton;
    /** El boton para eliminar pesajes del listado de confecciones. */
    private Button                                      eliminarPesajeConfeccionButton;
    /** El boton para añadir calibres al listado. */
    private Button                                      incluirCalibreButton;
    /** El boton para eliminar calibres del listado. */
    private Button                                      eliminarCalibreButton;
    /** El boton para añadir penetromia al listado. */
    private Button                                      incluirPenetromiaButton;
    /** El boton para eliminar penetromia del listado. */
    private Button                                      eliminarPenetromiaButton;
    /** El boton para añadir birx al listado. */
    private Button                                      incluirBrixButton;
    /** El boton para eliminar brix del listado. */
    private Button                                      eliminarBrixButton;
    /** Combo para indicar la nave .*/
    private ComboBox                                    cbNumProducto;
    /** El boton para imprimir la hoja de ruta.*/
    private Button                                      pdfButton;
    /** Combo para indicar la nave .*/
    private ComboBox                                    cbFotoCajaBorrar;
    /** Combo para indicar la nave .*/
    private ComboBox                                    cbNaves;
    /** Combo para indicar la mesa .*/
    private ComboBox                                    cbMesas;
    /** Combo para indicar si es Flowpack .*/
    private ComboBox                                    cbFlowPack;
    /** Combo para indicar si es maduración .*/
    private ComboBox                                    cbMaduracion;
    /** Combo para indicar si es repaso .*/
    private ComboBox                                    cbRepaso;
    /** Combo para indicar si es repaso .*/
    private ComboBox                                    cbMango;
    /** Combo para indicar si es repaso .*/
    private ComboBox                                    cbMalla;
    /** Combo para indicar si es repaso .*/
    private ComboBox                                    cbCalibrador;
    /** Combo para indicar si es repaso .*/
    private ComboBox                                    cbMesaConfeccion;
    /** Combo para indicar si es repaso .*/
    private ComboBox                                    cbBio;
    /** Combo para indicar si tiene daños internos .*/
    private ComboBox                                    cbDInternos;
    /** Combo para indicar si tiene daños externos .*/
    private ComboBox                                    cbDInExternos;
    /** Combo para indicar si tiene contaminación física o no .*/
    private ComboBox                                    cbContFisica;
    /** Combo para indicar si tiene contaminación química o no .*/
    private ComboBox                                    cbContQuimica;
    /** Combo para indicar si tiene contaminación biológica o no .*/
    private ComboBox                                    cbContBiologica;
    /** Combo para indicar el producto .*/
    private ComboBox                                    cbProductos;
    /** Combo para indicar la variedad del producto .*/
    private ComboBox                                    cbVariedad;
    /** Combo para indicar el calibre del producto .*/
    private ComboBox                                    cbCalibre;
    /** Combo para indicar el diámetro del producto .*/
    private ComboBox                                    cbDiametro;
    /** Combo para indicar el cliente .*/
    private ComboBox                                    cbClientes;
    /** Combo para indicar el número de cajas del pedido .*/
    private ComboBox                                    cbCajasIni;
    /** Combo para indicar el número de cajas real .*/
    private ComboBox                                    cbCajasFin;
    /** Combo para indicar el tipo de foto a subir .*/
    private ComboBox                                    cbTipoFoto;
    /** Para indicar la fecha del control de producto terminado. */
    private DateField                                   fecha;
    /** El control de producto terminado a modificar. */
    private TControlProductoTerminado                   controlPt;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                                      appName;
    @Value("${docs.path.temp.control.pt}")
    private String                                      docsPathTempControlPt;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger               log                        = LoggerFactory.getLogger(VistaControlPt.class);
    /** El pedido. */
    private TextArea                                    txtObservaciones;
    /** El pedido. */
    private TextField                                   txtNumPedido;
    /** El peso teórico de cada caja. */
    private TextField                                   txtPesoTeoricoCaja;
    /** El peso teórico de cada confección. */
    private TextField                                   txtPesoTeoricoConf;
    /** El peso teórico de cada confección. */
    private TextField                                   txtOrigen;
    /** El peso real que se registra. */
    private TextField                                   txtPesoRealCaja;
    /** El peso real que se registra. */
    private TextField                                   txtPesoRealConf;
    /** El calibre que se registra. */
    private TextField                                   txtCalibre;
    /** La penetromía que se registra. */
    private TextField                                   txtPenetromia;
    /** El BRIX que se registra. */
    private TextField                                   txtBrix;
    /** Los daños internos que se registra. */
    private TextField                                   txtDInternos;
    /** Los daños externos que se registra. */
    private TextField                                   txtDExternos;
    /** Los pesos de cajas que se van registrando. */
    private Table                                       tablaPesosCajas;
    /** Los pesos de confección que se van registrando. */
    private Table                                       tablaPesosConf;
    /** Los calibres que se van registrando. */
    private Table                                       tablaPesosCalibres;
    /** Las penetromías que se van registrando. */
    private Table                                       tablaPesosPenetromias;
    /** Los valores BRIX que se van registrando. */
    private Table                                       tablaPesosBrix;
    /** El usuario que está logado. */
    private Integer                                     user                       = null;
    /** La fecha en que se inició sesión. */
    private Long                                        time                       = null;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                                     appWidth;
    /** Container que mostrará los campos relacionado con la información principal del control de producto terminado.*/
    private VerticalLayout                              indentificacionContainer   = null;
    /** Container que mostrará las fotos asociadas al control de producto terminado.*/
    private VerticalLayout                              observacionesContainer     = null;
    /** Container que mostrará las fotos asociadas al control de producto terminado.*/
    private VerticalLayout                              controlEtiquetadoContainer = null;
    /** Container que mostrará las fotos asociadas al control de producto terminado.*/
    private VerticalLayout                              subirFotosContainer        = null;
    /** El empleado que ha iniciado sesión en la aplicación . */
    private TEmpleados                                  empleado;
    /** Los permisos del empleado que ha iniciado sesión . */
    private TPermisos                                   permisos;
    private UploadImage                                 upload;
    private List<TClientes>                             lClientes;
    private List<TProductos>                            lProductos;
    private List<TVariedad>                             lVariedades;
    private List<TProductoCalibre>                      lCalibres;
    private List<TDiametrosProducto>                    lDiametros;
    private List<Integer>                               lNumeros;
    private Label                                       lblLineaEnvasado;
    private Label                                       lblIdentificacion;
    /** Contendrá el número de fotos que debe tener de palé. */
    @Value("${app.pt.fotos.pale}")
    private Integer                                     appPtFotosPale;
    /** Contendrá el número de fotos que debe tener de cajas .*/
    @Value("${app.pt.fotos.cajas}")
    private Integer                                     appPtFotosCajas;
    /** Contendrá el número de fotos que debe tener de etiquetas internas .*/
    @Value("${app.pt.fotos.etiqueta1}")
    private Integer                                     appPtFotosEtiqueta1;
    /** Contendrá el número de fotos que debe tener de etiquetas externas .*/
    @Value("${app.pt.fotos.etiqueta2}")
    private Integer                                     appPtFotosEtiqueta2;
    /** Contendrá el número de pesajes que se deben realizar en las confecciones .*/
    @Value("${app.pt.pesajes.confeccion}")
    private Integer                                     appPtPesajesConfeccion;
    /** Contendrá el número de pesajes que se deben realizar en las cajas .*/
    @Value("${app.pt.pesajes.caja}")
    private Integer                                     appPtPesajesCaja;
    /** Contendrá el número de pesajes que se deben realizar en los calibres .*/
    @Value("${app.pt.pesajes.calibre}")
    private Integer                                     appPtPesajesCalibre;
    /** Contendrá el número de pesajes que se deben realizar en las penetromías .*/
    @Value("${app.pt.pesajes.penetromia}")
    private Integer                                     appPtPesajesPenetromia;
    /** Contendrá el número de pesajes que se deben realizar en los BRIX .*/
    @Value("${app.pt.pesajes.brix}")
    private Integer                                     appPtPesajesBrix;
    /** El índice del elemento seleccionado del listado de pesajes de cajas. */
    private Integer                                     idSeleccionadoCajas;
    /** El índice del elemento seleccionado del listado de pesajes de cajas. */
    private Integer                                     idSeleccionadoConf;
    /** El índice del elemento seleccionado del listado de pesajes de calibre. */
    private Integer                                     idSeleccionadoCalibre;
    /** El índice del elemento seleccionado del listado de pesajes de penetromía. */
    private Integer                                     idSeleccionadoPenetromia;
    /** El índice del elemento seleccionado del listado de pesajes de Brix. */
    private Integer                                     idSeleccionadoBrix;
    private HorizontalLayout                            horPesCaja                 = null;
    private HorizontalLayout                            horPesConfeccion           = null;
    /** Nos indicará si se accede directamente a la parte de fotos. */
    private Boolean                                     mostrarFotos;
    private Table                                       tablaFotos;
    private Table                                       tablaFotos2;
    private Table                                       tablaFotos3;
    private Integer                                     cnt;
    private Label                                       lblLeyendaFotos;
    private String                                      indexProducto;
    private Map<String, TLineaControlProductoTerminado> mLineas;
    private Map<String, List<Double>>                   mLineasBrix;
    private Map<String, List<Double>>                   mLineasCajas;
    private Map<String, List<Double>>                   mLineasCalibre;
    private Map<String, List<Double>>                   mLineasConfeccion;
    private Map<String, List<Double>>                   mLineasPenetromia;
    private ComboBox                                    cbProductosObservaciones;
    private ComboBox                                    cbProductosControlEtiquetado;
    private ComboBox                                    cbProductosFotos;
    private Integer                                     indexFotoMax;

    public class NumeroPesaje {
        /** El índice dentro de la lista. */
        private Integer index;
        /** El valor. */
        private Double  valor;

        /**
         * @return the index
         */
        public Integer getIndex() {
            return index;
        }

        /**
         * @param index the index to set
         */
        public void setIndex(Integer index) {
            this.index = index;
        }

        /**
         * @return the valor
         */
        public Double getValor() {
            return valor;
        }

        /**
         * @param valor the valor to set
         */
        public void setValor(Double valor) {
            this.valor = valor;
        }

        @Override
        public String toString() {
            return "" + valor;
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(crearButton)) {
            accionModificarControlPt();
        } else if (event.getButton().equals(listadoButton)) {
            getUI().getNavigator().navigateTo(VistaListadoControlPt.NAME);
        } else if (event.getButton().equals(nuevoButton)) {
            getUI().getNavigator().navigateTo(VistaNuevoControlPt.NAME);
        } else if (event.getButton().equals(incluirPesajeCajaButton)) {
            if (txtPesoRealCaja.getValue() != null && !txtPesoRealCaja.getValue().isEmpty()) {
                Double val = Double.valueOf(0);
                NumeroPesaje valor = new NumeroPesaje();
                //valor.setIndex(lsPesosCajas.size());
                valor.setIndex(tablaPesosCajas.getPageLength());
                try {
                    val = Utils.formatearValorDouble2(txtPesoRealCaja.getValue());
                    valor.setValor(val);
                    //lsPesosCajas.addItem(valor);
                    Label lbl = new Label("" + valor);
                    tablaPesosCajas.addItem(new Object[] { lbl }, cnt);
                    tablaPesosCajas.setPageLength(tablaPesosCajas.getPageLength() + 1);
                    tablaPesosCajas.setCaption("Total: " + tablaPesosCajas.getPageLength() + "(" + appPtPesajesCaja + ")");
                    cnt++;
                    txtPesoRealCaja.setValue(null);
                } catch (NumberFormatException nfe) {
                    Notification aviso = new Notification("Se debe introducir un valor numérico válido.", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }

            }
        } else if (event.getButton().equals(eliminarPesajeCajaButton)) {
            if (idSeleccionadoCajas != null) {
                //lsPesosCajas.removeItem(idSeleccionadoCajas);
                tablaPesosCajas.removeItem(idSeleccionadoCajas);
                tablaPesosCajas.setPageLength(tablaPesosCajas.getPageLength() - 1);
                tablaPesosCajas.setCaption("Total: " + tablaPesosCajas.getPageLength() + "(" + appPtPesajesCaja + ")");
            } else {
                Notification aviso = new Notification("Se debe seleccionar el valor del listado para eliminarlo", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }
        } else if (event.getButton().equals(incluirPesajeConfeccionButton)) {
            if (txtPesoRealConf.getValue() != null && !txtPesoRealConf.getValue().isEmpty()) {
                Double val = Double.valueOf(0);
                NumeroPesaje valor = new NumeroPesaje();
                //valor.setIndex(lsPesosConf.size());
                valor.setIndex(tablaPesosConf.getPageLength());
                try {
                    val = Utils.formatearValorDouble2(txtPesoRealConf.getValue());
                    valor.setValor(val);
                    //lsPesosConf.addItem(valor);
                    Label lbl = new Label("" + valor);
                    tablaPesosConf.addItem(new Object[] { lbl }, cnt);
                    tablaPesosConf.setPageLength(tablaPesosConf.getPageLength() + 1);
                    tablaPesosConf.setCaption("Total: " + tablaPesosConf.getPageLength() + "(" + appPtPesajesConfeccion + ")");
                    cnt++;
                    txtPesoRealConf.setValue(null);
                } catch (NumberFormatException nfe) {
                    Notification aviso = new Notification("Se debe introducir un valor numérico válido.", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }

            }
        } else if (event.getButton().equals(eliminarPesajeConfeccionButton)) {
            if (idSeleccionadoConf != null) {
                //lsPesosCajas.removeItem(idSeleccionadoConf);
                tablaPesosConf.removeItem(idSeleccionadoConf);
                tablaPesosConf.setPageLength(tablaPesosConf.getPageLength() - 1);
                tablaPesosConf.setCaption("Total: " + tablaPesosConf.getPageLength() + "(" + appPtPesajesConfeccion + ")");
            } else {
                Notification aviso = new Notification("Se debe seleccionar el valor del listado para eliminarlo", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }
        } else if (event.getButton().equals(incluirCalibreButton)) {
            if (txtCalibre.getValue() != null && !txtCalibre.getValue().isEmpty()) {
                Double val = Double.valueOf(0);
                NumeroPesaje valor = new NumeroPesaje();
                //valor.setIndex(lsCalibres.size());
                valor.setIndex(tablaPesosConf.getPageLength());
                try {
                    val = Utils.formatearValorDouble2(txtCalibre.getValue());
                    valor.setValor(val);
                    //lsCalibres.addItem(valor);
                    Label lbl = new Label("" + valor);
                    tablaPesosCalibres.addItem(new Object[] { lbl }, cnt);
                    tablaPesosCalibres.setPageLength(tablaPesosCalibres.getPageLength() + 1);
                    tablaPesosCalibres.setCaption("Total: " + tablaPesosCalibres.getPageLength() + "(" + appPtPesajesCalibre + ")");
                    cnt++;
                    txtCalibre.setValue(null);
                } catch (NumberFormatException nfe) {
                    Notification aviso = new Notification("Se debe introducir un valor numérico válido.", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }

            }
        } else if (event.getButton().equals(eliminarCalibreButton)) {
            if (idSeleccionadoCalibre != null) {
                //lsCalibres.removeItem(idSeleccionadoCalibre);
                tablaPesosCalibres.removeItem(idSeleccionadoCalibre);
                tablaPesosCalibres.setPageLength(tablaPesosCalibres.getPageLength() - 1);
                tablaPesosCalibres.setCaption("Total: " + tablaPesosCalibres.getPageLength() + "(" + appPtPesajesCalibre + ")");
            } else {
                Notification aviso = new Notification("Se debe seleccionar el valor del listado para eliminarlo", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }
        } else if (event.getButton().equals(incluirPenetromiaButton)) {
            if (txtPenetromia.getValue() != null && !txtPenetromia.getValue().isEmpty()) {
                Double val = Double.valueOf(0);
                NumeroPesaje valor = new NumeroPesaje();
                //valor.setIndex(lsPenetromias.size());
                valor.setIndex(tablaPesosPenetromias.getPageLength());
                try {
                    val = Utils.formatearValorDouble2(txtPenetromia.getValue());
                    valor.setValor(val);
                    //lsPenetromias.addItem(valor);
                    Label lbl = new Label("" + valor);
                    tablaPesosPenetromias.addItem(new Object[] { lbl }, cnt);
                    tablaPesosPenetromias.setPageLength(tablaPesosPenetromias.getPageLength() + 1);
                    tablaPesosPenetromias.setCaption("Total: " + tablaPesosPenetromias.getPageLength() + "(" + appPtPesajesPenetromia + ")");
                    cnt++;
                    txtPenetromia.setValue(null);
                } catch (NumberFormatException nfe) {
                    Notification aviso = new Notification("Se debe introducir un valor numérico válido.", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }

            }
        } else if (event.getButton().equals(eliminarPenetromiaButton)) {
            if (idSeleccionadoPenetromia != null) {
                //lsPenetromias.removeItem(idSeleccionadoPenetromia);
                tablaPesosPenetromias.removeItem(idSeleccionadoPenetromia);
                tablaPesosPenetromias.setPageLength(tablaPesosPenetromias.getPageLength() - 1);
                tablaPesosPenetromias.setCaption("Total: " + tablaPesosPenetromias.getPageLength() + "(" + appPtPesajesPenetromia + ")");
            } else {
                Notification aviso = new Notification("Se debe seleccionar el valor del listado para eliminarlo", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }
        } else if (event.getButton().equals(incluirBrixButton)) {
            if (txtBrix.getValue() != null && !txtBrix.getValue().isEmpty()) {
                Double val = Double.valueOf(0);
                NumeroPesaje valor = new NumeroPesaje();
                //valor.setIndex(lsBrix.size());
                valor.setIndex(tablaPesosBrix.getPageLength());
                try {
                    val = Utils.formatearValorDouble2(txtBrix.getValue());
                    valor.setValor(val);
                    //lsBrix.addItem(valor);
                    Label lbl = new Label("" + valor);
                    tablaPesosBrix.addItem(new Object[] { lbl }, cnt);
                    tablaPesosBrix.setPageLength(tablaPesosBrix.getPageLength() + 1);
                    tablaPesosBrix.setCaption("Total: " + tablaPesosBrix.getPageLength() + "(" + appPtPesajesBrix + ")");
                    cnt++;
                    txtBrix.setValue(null);
                } catch (NumberFormatException nfe) {
                    Notification aviso = new Notification("Se debe introducir un valor numérico válido.", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }

            }
        } else if (event.getButton().equals(eliminarBrixButton)) {
            if (idSeleccionadoBrix != null) {
                //lsBrix.removeItem(idSeleccionadoBrix);
                tablaPesosBrix.removeItem(idSeleccionadoBrix);
                tablaPesosBrix.setPageLength(tablaPesosBrix.getPageLength() - 1);
                tablaPesosBrix.setCaption("Total: " + tablaPesosBrix.getPageLength() + "(" + appPtPesajesBrix + ")");
            } else {
                Notification aviso = new Notification("Se debe seleccionar el valor del listado para eliminarlo", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }
        } else if (event.getButton().equals(eliminarFotoCajaButton)) {
            if (!tablaFotos.getItemIds().isEmpty() && cbFotoCajaBorrar.getValue() != null) {
                try {
                    List<Integer> lIds = Utils.generarListaGenerica();
                    lIds.addAll((Collection<? extends Integer>) tablaFotos.getItemIds());

                    Integer indexFoto = Integer.valueOf(cbFotoCajaBorrar.getValue().toString().split("Nº")[1]);

                    Integer idFoto = indexFoto % 5;

                    if (indexFoto < 5) {
                        idFoto = indexFoto;
                    }
                    // Eliminamos la foto de palé.

                    // En función del valor del desplegable, cogemos la foto
                    String tipoFoto = cbFotoCajaBorrar.getValue().toString();

                    if (idFoto.equals(0)) {
                        idFoto = 5;
                    }

                    Integer fila = indexFoto % 5;

                    if (indexFoto < 5) {
                        fila = 0;
                    }

                    String col = "FOTO Nº " + idFoto;
                    Item it = null;
                    Property<?> prop = null;
                    Integer indexBorrar;
                    it = tablaFotos.getItem(fila + 1);

                    prop = it.getItemProperty(col);
                    indexBorrar = idFoto;

                    Image img = (Image) prop.getValue();
                    if (img.getId() == null) {
                        Notification aviso = new Notification("La imagen indicada no existe", Notification.Type.WARNING_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        return;
                    }
                    // En fuinción del valor puesto en el desplegable, cogemos la foto a eliminar.

                    TControlProductoTerminadoFotos foto = contrVista.obtenerFotoControlPtPorId(Integer.valueOf(img.getId()), user, time);

                    contrVista.eliminarFotoControlProductoTerminadoIdFoto(foto.getId(), user, time);
                    if (foto != null) {
                        // Eliminamos la foto de disco.
                        File f = new File(foto.getDescripcionFoto());
                        f.delete();
                    }

                    contrVista.modificarControlPt(controlPt, user, time);
                    cbFotoCajaBorrar.clear();

                    cargarFotosTablas(Integer.valueOf(cbProductosControlEtiquetado.getValue().toString()));

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
                Notification aviso = new Notification("No se ha identificado ninguna foto o no se ha seleccionado la foto a eliminar del desplegable", Notification.Type.WARNING_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            }
        } else if (event.getButton().equals(subirFotosButton)) {
            if (!subirFotosContainer.isVisible()) {
                subirFotosContainer.setVisible(true);
                controlEtiquetadoContainer.setVisible(false);
                observacionesContainer.setVisible(false);
                indentificacionContainer.setVisible(false);
                subirFotosButton.addStyleName("down");
                principalButton.setStyleName("default");
                observacionesButton.setStyleName("default");
                fotosButton.setStyleName("default");
                subirFotosButton.setStyleName("down");
                upload.setVisible(false);
                crearButton.setVisible(false);
                cbProductosFotos.removeAllItems();

                List<String> lNumeros = Utils.generarListaGenerica();
                lNumeros.addAll(mLineas.keySet());

                cbProductosFotos.addItems(lNumeros);
                if (lNumeros.size() == 1) {
                    cbProductosFotos.setValue(lNumeros.get(0));
                }
            }
        } else if (event.getButton().equals(guardarFotoButton)) {
            try {
                Integer ordenFoto = 0;
                String tipoFoto = "";
                if (cbProductosFotos.getValue() != null && !cbProductosFotos.getValue().toString().trim().isEmpty()) {
                    String index = cbProductosFotos.getValue().toString().trim();
                    if (cbTipoFoto.getValue() != null) {
                        if (cbTipoFoto.getValue().toString().contains("FOTO CAJA")) {
                            ordenFoto = mLineas.get(index).getIndFotoCaja();
                            controlPt.setIndFotoCaja(controlPt.getIndFotoCaja() + 1);
                            tipoFoto = "caja";
                        } else if (cbTipoFoto.getValue().toString().contains("FOTO PALET")) {
                            ordenFoto = mLineas.get(index).getIndFotoPale();
                            controlPt.setIndFotoPale(controlPt.getIndFotoPale() + 1);
                            tipoFoto = "pale";
                        } else if (cbTipoFoto.getValue().toString().contains("FOTO ETIQUETA INTERIOR")) {
                            ordenFoto = mLineas.get(index).getIndFotoEtiqueta();
                            controlPt.setIndFotoEtiqueta(controlPt.getIndFotoEtiqueta() + 1);
                            tipoFoto = "int";
                        } else if (cbTipoFoto.getValue().toString().contains("FOTO ETIQUETA EXTERIOR")) {
                            ordenFoto = mLineas.get(index).getIndFotoConfeccion();
                            controlPt.setIndFotoConfeccion(controlPt.getIndFotoConfeccion() + 1);
                            tipoFoto = "ext";
                        }
                        String descFoto = String.valueOf(cbTipoFoto.getValue());
                        // Open the file for writing.
                        String nombreFichero = docsPathTempControlPt + "CONTROL_PT/PT-" + controlPt.getId() + "_" + index + "_" + descFoto + "_" + ordenFoto + ".jpg";
                        File file = new File(nombreFichero);

                        if (file.exists()) {

                            TControlProductoTerminadoFotos foto = new TControlProductoTerminadoFotos();
                            foto.setDescripcionFoto(nombreFichero);
                            foto.setFechaFoto(Utils.generarFecha());
                            foto.setIdControlProductoTerminado(controlPt.getId());
                            foto.setOrdenFoto(ordenFoto);
                            foto.setIdLinea(mLineas.get(index).getId());
                            foto.setUsuFoto(user);

                            if (tipoFoto.equals("caja")) {
                                mLineas.get(index).setIndFotoCaja(mLineas.get(index).getIndFotoCaja() + 1);
                            } else if (tipoFoto.equals("pale")) {
                                mLineas.get(index).setIndFotoPale(mLineas.get(index).getIndFotoPale() + 1);
                            } else if (tipoFoto.equals("int")) {
                                mLineas.get(index).setIndFotoEtiqueta(mLineas.get(index).getIndFotoEtiqueta() + 1);
                            } else if (tipoFoto.equals("ext")) {
                                mLineas.get(index).setIndFotoConfeccion(mLineas.get(index).getIndFotoConfeccion() + 1);
                            }

                            // Guardamos el registro en t_control_producto_terminado_fotos
                            contrVista.crearFotoControlPt(foto, user, time);
                            Notification aviso = new Notification("Foto incluida correctamente", Notification.Type.HUMANIZED_MESSAGE);
                            aviso.setPosition(Position.MIDDLE_CENTER);
                            aviso.show(Page.getCurrent());

                            cbTipoFoto.setValue(null);
                            lblLeyendaFotos.setValue("");
                            contrVista.modificarLineaControlPt(mLineas.get(index), user, time);
                        } else {
                            Notification aviso = new Notification("No se ha identificado ninguna foto, es necesario darle al botón 'Adjuntar imagen' despues de haber seleccionado la foto", Notification.Type.WARNING_MESSAGE);
                            aviso.setPosition(Position.MIDDLE_CENTER);
                            aviso.show(Page.getCurrent());
                        }
                    } else {
                        Notification aviso = new Notification("No se ha identificado ninguna foto no se ha seleccionado el tipo de foto del desplegable", Notification.Type.WARNING_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                    }
                } else {
                    Notification aviso = new Notification("No se ha identificado el producto al que se le asocia la foto", Notification.Type.WARNING_MESSAGE);
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
        } else if (event.getButton().equals(cerrarSesionButton)) {
            getUI().getNavigator().navigateTo(VistaInicioSesion.NAME + "/cierre");
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
        time = null;
        user = (Integer) getSession().getAttribute("user");
        cnt = 0;
        if ((String) getSession().getAttribute("fecha") != null) {
            time = Long.parseLong((String) getSession().getAttribute("fecha"));
        }
        if (time != null) {
            try {
                indexFotoMax = 5;
                mLineas = new HashMap<String, TLineaControlProductoTerminado>();
                mLineasBrix = new HashMap<String, List<Double>>();
                mLineasCajas = new HashMap<String, List<Double>>();
                mLineasCalibre = new HashMap<String, List<Double>>();
                mLineasConfeccion = new HashMap<String, List<Double>>();
                mLineasPenetromia = new HashMap<String, List<Double>>();

                indexProducto = null;

                lblLeyendaFotos = new Label("");
                lblLeyendaFotos.setStyleName("tituloTamano16");
                // Creamos los botones de la pantalla.
                crearBotones();
                upload = new UploadImage();
                horPesCaja = new HorizontalLayout();
                horPesConfeccion = new HorizontalLayout();
                empleado = contrVista.obtenerEmpleadoPorId(user, user, time);

                permisos = contrVista.obtenerPermisosEmpleado(empleado, user, time);
                lNumeros = Utils.obtenerListaNumeros();

                if (permisos == null) {
                    Notification aviso = new Notification("No se ha podido identificar los permisos asignados, contacta con el administrador.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    return;
                }

                if (!Utils.booleanFromInteger(permisos.getModificarControlPt())) {
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
                    if (parametros.contains("_")) {
                        controlPt = contrVista.obtenerControlPtPorId(Integer.valueOf(parametros.split("_")[0]), user, time);
                        mostrarFotos = true;
                    } else {
                        controlPt = contrVista.obtenerControlPtPorId(Integer.valueOf(parametros), user, time);
                        mostrarFotos = false;
                    }
                } else {
                    parametros = null;
                }

                if (controlPt == null) {
                    Notification aviso = new Notification("No se ha encontrado el control de producto terminado.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getUI().getNavigator().navigateTo(VistaListadoControlPtOld.NAME);
                    return;
                }

                if (empleado.getIdRol().equals(RolesEnum.OPERARIO_CONTROL_PT.getValue())) {
                    if (!empleado.getId().equals(controlPt.getUsuCrea())) {
                        Notification aviso = new Notification("No se tienen permisos para modificar el conrol de producto terminado indicado.", Notification.Type.ERROR_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        getUI().getNavigator().navigateTo(VistaListadoControlPtOld.NAME);
                        return;
                    }
                }

                lClientes = contrVista.obtenerTodosClientes(user, time);

                lProductos = contrVista.obtenerTodosProductos(user, time);

                List<TLineaControlProductoTerminado> lLineas = contrVista.obtenerLineasControlProductoTerminado(controlPt.getId(), user, time);

                // Cargamos los datos en las listas de pesajes.
                List<TControlProductoTerminadoPesajesCaja> lPesCaja = Utils.generarListaGenerica();
                List<TControlProductoTerminadoPesajesConfeccion> lPesConf = Utils.generarListaGenerica();
                List<TControlProductoTerminadoPesajesCalibre> lPesCal = Utils.generarListaGenerica();
                List<TControlProductoTerminadoPesajesBrix> lPesBrix = Utils.generarListaGenerica();
                List<TControlProductoTerminadoPesajesPenetromia> lPesPen = Utils.generarListaGenerica();

                List<Double> lValores = Utils.generarListaGenerica();
                Integer cnt = 1;
                for (TLineaControlProductoTerminado l : lLineas) {
                    mLineas.put("" + cnt, l);

                    // PESAJES DE CAJAS
                    lPesCaja = contrVista.obtenerPesajesCajaIdLinea(l.getId(), user, time);
                    if (lPesCaja != null) {
                        for (TControlProductoTerminadoPesajesCaja pes : lPesCaja) {

                            lValores.add(pes.getValor());
                        }

                        mLineasCajas.put("" + cnt, lValores);
                    }

                    lValores = Utils.generarListaGenerica();

                    // PESAJES DE CONFECCIÓN
                    lPesConf = contrVista.obtenerPesajesConfeccionIdLineaControlPt(l.getId(), user, time);
                    if (lPesConf != null) {
                        for (TControlProductoTerminadoPesajesConfeccion pes : lPesConf) {

                            lValores.add(pes.getValor());
                        }

                        mLineasConfeccion.put("" + cnt, lValores);
                    }

                    lValores = Utils.generarListaGenerica();

                    // PESAJES DE CALIBRE
                    lPesCal = contrVista.obtenerPesajesCalibreIdLineaControlPt(l.getId(), user, time);
                    if (lPesCal != null) {
                        for (TControlProductoTerminadoPesajesCalibre pes : lPesCal) {

                            lValores.add(pes.getValor());
                        }

                        mLineasCalibre.put("" + cnt, lValores);
                    }

                    lValores = Utils.generarListaGenerica();

                    // PESAJES DE BRIX
                    lPesBrix = contrVista.obtenerPesajesBrixIdLinea(l.getId(), user, time);
                    if (lPesCal != null) {
                        for (TControlProductoTerminadoPesajesBrix pes : lPesBrix) {

                            lValores.add(pes.getValor());
                        }

                        mLineasBrix.put("" + cnt, lValores);
                    }

                    lValores = Utils.generarListaGenerica();

                    // PESAJES DE PENETROMÍA
                    lPesPen = contrVista.obtenerPesajesPenetromiaIdLinea(l.getId(), user, time);
                    if (lPesPen != null) {
                        for (TControlProductoTerminadoPesajesPenetromia pes : lPesPen) {

                            lValores.add(pes.getValor());
                        }

                        mLineasPenetromia.put("" + cnt, lValores);
                    }

                    lValores = Utils.generarListaGenerica();

                    cnt++;
                }

                crearTablaPesajes();

                crearComponentes();

                crearTablasImagenes();

                NumeroPesaje numPes = null;

                Label lbl;

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

            lblLineaEnvasado = new Label("LÍNEA DE ENVASADO");
            lblLineaEnvasado.setStyleName("tituloTamano18");

            lblIdentificacion = new Label("IDENTIFICACIÓN");
            lblIdentificacion.setStyleName("tituloTamano18");

            // Construimos los containers
            generaContenedorIdentificacion();
            generaContenedorContenedorCalidad();
            generaContenedorInformacionFotos();
            generaContenedorSubirFotos();

            indentificacionContainer.addComponent(observacionesContainer);

            Label texto = new Label("Control de producto terminado");
            texto.setStyleName("tituloTamano18");
            texto.setHeight(4, Sizeable.Unit.EM);

            // Creamos y añadimos el logo de Genasoft a la pantalla
            HorizontalLayout imgNaturSoft = null;

            // The view root layout
            VerticalLayout viewLayout = null;
            if (empleado.getIdRol().equals(RolesEnum.OPERARIO_CONTROL_PT.getValue())) {
                imgNaturSoft = contrVista.logoGenaSoft();
                viewLayout = new VerticalLayout();
            } else {
                imgNaturSoft = contrVista.logoGenaSoft(texto);
                viewLayout = new VerticalLayout(new Menu(permisos, user));
            }
            viewLayout.setSizeFull();
            viewLayout.setSpacing(true);
            if (!empleado.getIdRol().equals(RolesEnum.OPERARIO_CONTROL_PT.getValue())) {
                viewLayout.addComponent(imgNaturSoft);
            }

            //viewLayout.setComponentAlignment(imgNaturSoft, Alignment.TOP_CENTER);
            //viewLayout.addComponent(titulo);
            //viewLayout.setComponentAlignment(titulo, Alignment.TOP_CENTER);

            // Formulario con los campos que componen el cliente.
            VerticalLayout formulario = new VerticalLayout();
            formulario.setSpacing(true);

            HorizontalLayout btsPrinc = new HorizontalLayout();
            btsPrinc.setSpacing(true);
            btsPrinc.addComponent(nuevoButton);
            btsPrinc.addComponent(listadoButton);

            viewLayout.addComponent(btsPrinc);
            viewLayout.addComponent(crearBotonesMenu());
            if (empleado.getIdRol().equals(RolesEnum.OPERARIO_CONTROL_PT.getValue())) {
                HorizontalLayout hor = new HorizontalLayout();
                hor.setSpacing(true);
                hor.addComponent(crearButton);
                hor.addComponent(cerrarSesionButton);
                viewLayout.addComponent(hor);
            }
            viewLayout.addComponent(indentificacionContainer);
            viewLayout.addComponent(observacionesContainer);
            viewLayout.addComponent(controlEtiquetadoContainer);
            //viewLayout.addComponent(subirFotosContainer);
            if (!empleado.getIdRol().equals(RolesEnum.OPERARIO_CONTROL_PT.getValue())) {
                formulario.addComponent(crearButton);
                formulario.setComponentAlignment(crearButton, Alignment.MIDDLE_CENTER);
            }
            if (empleado.getIdRol().equals(RolesEnum.OPERARIO_CONTROL_PT.getValue())) {
                viewLayout.addComponent(imgNaturSoft);
            }
            viewLayout.addComponent(formulario);
            setCompositionRoot(viewLayout);
            // Establecemos el porcentaje de ratio para los layouts
            //viewLayout.setExpandRatio(titulo, 0.1f);

            viewLayout.setMargin(true);

            if (mostrarFotos) {
                buttonClick(new ClickEvent(subirFotosButton));
            }

            // Page.getCurrent().open("http://localhost:8080/ImagenesControlPt/cargaFotos.jsp?&usuario=" + user + "&idLinea=" + mLineas.get(indexProducto).getId() + "&idControlPt=" + mLineas.get(indexProducto).getIdControlPt(), "_blank", true);
            BrowserWindowOpener opener = new BrowserWindowOpener(new ExternalResource("http://192.168.0.40:8090/ImagenesControlPt/cargaFotos.jsp?&usuario=" + user + "&idLinea=" + mLineas.get(indexProducto).getId() + "&idControlPt="
                    + mLineas.get(indexProducto).getIdControlPt()));
            opener.setWindowName("_blank");
            opener.extend(subirFotosButton);

            BrowserWindowOpener opener2 = new BrowserWindowOpener(new ExternalResource("http://192.168.0.40:8090/ImagenesControlPt/firma.jsp?&usuario=" + user + "&idControlPt=" + mLineas.get(indexProducto).getIdControlPt(), "_blank"));
            opener2.setWindowName("_blank");
            opener2.extend(firmarButton);
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
        crearButton = new Button("Aplicar cambios", this);
        listadoButton = new Button("Volver al listado", this);
        nuevoButton = new Button("Crear nuevo control PT", this);

        guardarFotoButton = new Button("Guardar foto", this);

        incluirPesajeCajaButton = new Button(">>", this);
        eliminarPesajeCajaButton = new Button("<<", this);

        incluirPesajeConfeccionButton = new Button(">>", this);
        eliminarPesajeConfeccionButton = new Button("<<", this);

        incluirCalibreButton = new Button(">>", this);
        eliminarCalibreButton = new Button("<<", this);

        incluirPenetromiaButton = new Button(">>", this);
        eliminarPenetromiaButton = new Button("<<", this);

        incluirBrixButton = new Button(">>", this);
        eliminarBrixButton = new Button("<<", this);

        eliminarFotoCajaButton = new Button("Eliminar foto", this);

        cerrarSesionButton = new Button("Cerrar sesión", this);

    }

    /**
     * Método que nos genera el contenedor con los botones que conforma el segmento del menú de cliente. 
     * @return El contenedor con los botones.
     */
    private HorizontalLayout crearBotonesMenu() {
        HorizontalLayout botonesMenu = new HorizontalLayout();
        botonesMenu.setStyleName("segment");
        botonesMenu.addStyleName("segment-alternate");

        pdfButton = new Button("", this);
        pdfButton.setIcon(new ThemeResource("icons/pdf-32.ico"));
        pdfButton.setStyleName(BaseTheme.BUTTON_LINK);
        if (empleado.getIdRol().equals(RolesEnum.OPERARIO_CONTROL_PT.getValue())) {
            pdfButton.setVisible(false);
        }

        // Evento para eliminar un articulo articulo.
        pdfButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                Page.getCurrent().open("/exportarControlPtPdf?idEmpleado=" + user + "&idSeleccionado=" + controlPt.getId(), "_blank");

            }

        });

        // Los botones que componen el menú.
        principalButton = new Button("Identificación");
        principalButton.addStyleName("default");
        principalButton.addStyleName("first");
        principalButton.addStyleName("down");

        principalButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!indentificacionContainer.isVisible()) {
                    indentificacionContainer.setVisible(true);
                    controlEtiquetadoContainer.setVisible(false);
                    subirFotosContainer.setVisible(false);
                    observacionesContainer.setVisible(false);
                    //calidadContainer.setVisible(false);
                    principalButton.addStyleName("down");
                    principalButton.setStyleName("down");
                    fotosButton.setStyleName("default");
                    observacionesButton.setStyleName("default");
                    subirFotosButton.setStyleName("default");
                    cbTipoFoto.setValue(null);
                    crearButton.setVisible(true);
                    upload.setVisible(false);

                }
            }
        });

        observacionesButton = new Button("Observaciones");
        observacionesButton.addStyleName("default");

        observacionesButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!observacionesContainer.isVisible()) {
                    observacionesContainer.setVisible(true);
                    indentificacionContainer.setVisible(false);
                    controlEtiquetadoContainer.setVisible(false);
                    subirFotosContainer.setVisible(false);
                    principalButton.setStyleName("default");
                    fotosButton.setStyleName("default");
                    subirFotosButton.setStyleName("default");
                    observacionesButton.setStyleName("down");
                    cbTipoFoto.setValue(null);
                    upload.setVisible(false);
                    crearButton.setVisible(true);

                    cbProductosObservaciones.removeAllItems();

                    List<String> lNumeros = Utils.generarListaGenerica();
                    lNumeros.addAll(mLineas.keySet());

                    cbProductosObservaciones.addItems(lNumeros);
                    if (lNumeros.size() == 1) {
                        cbProductosObservaciones.setValue(lNumeros.get(0));
                    }

                }
            }
        });

        fotosButton = new Button("Control de etiquetado");
        fotosButton.addStyleName("default");

        fotosButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (!controlEtiquetadoContainer.isVisible()) {
                    controlEtiquetadoContainer.setVisible(true);
                    observacionesContainer.setVisible(false);
                    indentificacionContainer.setVisible(false);
                    subirFotosContainer.setVisible(false);
                    fotosButton.addStyleName("down");
                    principalButton.setStyleName("default");
                    observacionesButton.setStyleName("default");
                    subirFotosButton.setStyleName("default");
                    fotosButton.setStyleName("down");
                    upload.setVisible(false);
                    cbTipoFoto.setValue(null);
                    crearButton.setVisible(false);

                    cbProductosControlEtiquetado.removeAllItems();

                    List<String> lNumeros = Utils.generarListaGenerica();
                    lNumeros.addAll(mLineas.keySet());

                    cbProductosControlEtiquetado.addItems(lNumeros);
                    if (lNumeros.size() == 1) {
                        cbProductosControlEtiquetado.setValue(lNumeros.get(0));
                        cargarFotosTablas(1);
                    } else {
                        cargarFotosTablas(-1);
                    }

                }

            }

        });

        subirFotosButton = new Button("Incluir foto");
        subirFotosButton.addStyleName("default");

        subirFotosButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                /**
                if (!subirFotosContainer.isVisible()) {
                    subirFotosContainer.setVisible(true);
                    controlEtiquetadoContainer.setVisible(false);
                    observacionesContainer.setVisible(false);
                    indentificacionContainer.setVisible(false);
                    subirFotosButton.addStyleName("down");
                    principalButton.setStyleName("default");
                    observacionesButton.setStyleName("default");
                    fotosButton.setStyleName("default");
                    subirFotosButton.setStyleName("down");
                    upload.setVisible(false);
                    crearButton.setVisible(false);
                
                    cbProductosFotos.removeAllItems();
                
                    List<String> lNumeros = Utils.generarListaGenerica();
                    lNumeros.addAll(mLineas.keySet());
                
                    cbProductosFotos.addItems(lNumeros);
                    if (lNumeros.size() == 1) {
                        cbProductosFotos.setValue(lNumeros.get(0));
                    }
                }
                */
            }

        });

        firmarButton = new Button("Firmar");
        firmarButton.addStyleName("default");

        firmarButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {
                //Page.getCurrent().open("http://localhost:8080/ImagenesControlPt/firma.jsp?&usuario=" + user + "&idControlPt=" + mLineas.get(indexProducto).getIdControlPt(), "_blank", true);                
                /**
                if (!subirFotosContainer.isVisible()) {
                    subirFotosContainer.setVisible(true);
                    controlEtiquetadoContainer.setVisible(false);
                    observacionesContainer.setVisible(false);
                    indentificacionContainer.setVisible(false);
                    subirFotosButton.addStyleName("down");
                    principalButton.setStyleName("default");
                    observacionesButton.setStyleName("default");
                    fotosButton.setStyleName("default");
                    subirFotosButton.setStyleName("down");
                    upload.setVisible(false);
                    crearButton.setVisible(false);
                
                    cbProductosFotos.removeAllItems();
                
                    List<String> lNumeros = Utils.generarListaGenerica();
                    lNumeros.addAll(mLineas.keySet());
                
                    cbProductosFotos.addItems(lNumeros);
                    if (lNumeros.size() == 1) {
                        cbProductosFotos.setValue(lNumeros.get(0));
                    }
                }
                */
            }

        });

        botonesMenu.addComponent(principalButton);
        botonesMenu.addComponent(observacionesButton);
        botonesMenu.addComponent(fotosButton);
        botonesMenu.addComponent(subirFotosButton);
        botonesMenu.addComponent(firmarButton);

        botonesMenu.addComponent(pdfButton);

        // Retornamos el segmento de botones.
        return botonesMenu;
    }

    /**
       
    
    /**
     * Método que nos crea los componentes (cajas de texto)
     */
    private void crearComponentes() {
        DecimalFormat df = new DecimalFormat("####0.00");
        //Los campos que componen un empleado.         
        txtNumPedido = new TextField("Pedido: ");
        txtNumPedido.setRequired(true);
        txtNumPedido.setNullRepresentation("");
        txtNumPedido.setWidth(10, Sizeable.Unit.EM);
        txtNumPedido.setMaxLength(245);
        txtNumPedido.setValue(controlPt.getNumeroPedido());

        txtObservaciones = new TextArea("Observaciones: ");
        txtObservaciones.setNullRepresentation("");
        txtObservaciones.setMaxLength(3000);
        txtObservaciones.setWidth(appWidth * 2, Sizeable.Unit.EM);
        txtObservaciones.setHeight(appWidth, Sizeable.Unit.EM);
        txtObservaciones.setValue(controlPt.getObservaciones());

        fecha = new DateField("Fecha: ");
        fecha.setWidth(10, Sizeable.Unit.EM);
        fecha.setValue(controlPt.getFecha());

        cbClientes = new ComboBox("Cliente:");
        cbClientes.addItems(lClientes);
        cbClientes.setRequired(true);
        cbClientes.setNewItemsAllowed(true);
        cbClientes.setNullSelectionAllowed(false);
        cbClientes.setWidth(appWidth / 2, Sizeable.Unit.EM);
        cbClientes.setFilteringMode(FilteringMode.CONTAINS);
        if (controlPt.getIdCliente() != null && !controlPt.getIdCliente().equals(-1)) {
            for (TClientes cl : lClientes) {
                if (cl.getId().equals(controlPt.getIdCliente())) {
                    cbClientes.setValue(cl);
                    break;
                }
            }
        }

        // Peso teórico caja.
        txtPesoTeoricoCaja = new TextField("Peso teórico caja: ");
        txtPesoTeoricoCaja.setNullRepresentation("");
        txtPesoTeoricoCaja.setValue(df.format(controlPt.getPesoCaja()));
        txtPesoTeoricoCaja.setRequired(true);

        // Peso teórico confección.
        txtPesoTeoricoConf = new TextField("Peso teórico confección: ");
        txtPesoTeoricoConf.setNullRepresentation("");
        txtPesoTeoricoConf.setValue(df.format(controlPt.getPesoCaja()));
        txtPesoTeoricoConf.setRequired(true);

        // El orígen
        txtOrigen = new TextField("Orígen: ");
        txtOrigen.setNullRepresentation("");
        txtOrigen.setRequired(true);
        txtOrigen.setWidth(5, Sizeable.Unit.EM);

        // Peso real caja
        txtPesoRealCaja = new TextField("Peso real caja: ");
        txtPesoRealCaja.setNullRepresentation("");
        txtPesoRealCaja.setWidth(5, Sizeable.Unit.EM);
        txtPesoRealCaja.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                if (target.equals(txtPesoRealCaja)) {
                    buttonClick(new ClickEvent(incluirPesajeCajaButton));
                } else if (target.equals(txtPesoRealConf)) {
                    buttonClick(new ClickEvent(incluirPesajeConfeccionButton));
                } else if (target.equals(txtCalibre)) {
                    buttonClick(new ClickEvent(incluirCalibreButton));
                } else if (target.equals(txtPenetromia)) {
                    buttonClick(new ClickEvent(incluirPenetromiaButton));
                } else if (target.equals(txtBrix)) {
                    buttonClick(new ClickEvent(incluirBrixButton));
                }
            }
        });

        // Peso real confección
        txtPesoRealConf = new TextField("Peso real confección: ");
        txtPesoRealConf.setNullRepresentation("");
        txtPesoRealConf.setWidth(5, Sizeable.Unit.EM);
        txtPesoRealConf.addShortcutListener(new ShortcutListener("Shortcut Name2", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                buttonClick(new ClickEvent(incluirPesajeConfeccionButton));
            }
        });

        // El calibre
        txtCalibre = new TextField("Calibre: ");
        txtCalibre.setNullRepresentation("");
        txtCalibre.setWidth(5, Sizeable.Unit.EM);
        txtCalibre.addShortcutListener(new ShortcutListener("Shortcut Name3", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                buttonClick(new ClickEvent(incluirCalibreButton));
            }
        });

        // Penetromía
        txtPenetromia = new TextField("Penetromía: ");
        txtPenetromia.setNullRepresentation("");
        txtPenetromia.setWidth(5, Sizeable.Unit.EM);
        txtPenetromia.addShortcutListener(new ShortcutListener("Shortcut Name4", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                buttonClick(new ClickEvent(incluirPenetromiaButton));
            }
        });

        // Brix
        txtBrix = new TextField("ºBRIX: ");
        txtBrix.setNullRepresentation("");
        txtBrix.setWidth(5, Sizeable.Unit.EM);
        txtBrix.addShortcutListener(new ShortcutListener("Shortcut Name5", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                buttonClick(new ClickEvent(incluirBrixButton));
            }
        });

        // VARIEDAD
        cbVariedad = new ComboBox("Variedad: ");
        cbVariedad.setRequired(true);
        cbVariedad.setNullSelectionAllowed(false);
        cbVariedad.setWidth(appWidth / 2, Sizeable.Unit.EM);
        cbVariedad.setFilteringMode(FilteringMode.CONTAINS);

        // CALIBRE
        cbCalibre = new ComboBox("Calibre: ");
        cbCalibre.setNullSelectionAllowed(false);
        cbCalibre.setWidth(7, Sizeable.Unit.EM);
        cbCalibre.setFilteringMode(FilteringMode.CONTAINS);

        // DIAMETRO
        cbDiametro = new ComboBox("Diámetro: ");
        cbDiametro.setRequired(true);
        cbDiametro.setNullSelectionAllowed(false);
        cbDiametro.setWidth(14, Sizeable.Unit.EM);
        cbDiametro.setFilteringMode(FilteringMode.CONTAINS);
        cbDiametro.setVisible(false);

        // Los productos
        cbProductos = new ComboBox("Producto");
        cbProductos.addItems(lProductos);
        cbProductos.setRequired(true);
        cbProductos.setNullSelectionAllowed(false);
        cbProductos.setWidth(appWidth / 2, Sizeable.Unit.EM);
        cbProductos.setFilteringMode(FilteringMode.CONTAINS);
        cbProductos.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbProductos.getValue() != null) {
                    try {
                        // Obtenemos las variedades 
                        TProductos prod = (TProductos) cbProductos.getValue();
                        lVariedades = contrVista.obtenerVariedadesProducto(prod.getId(), user, time);

                        cbVariedad.removeAllItems();
                        cbVariedad.addItems(lVariedades);

                        if (lVariedades.size() == 1) {
                            cbVariedad.setValue(lVariedades.get(0));
                        }

                        cbCalibre.removeAllItems();
                        cbDiametro.removeAllItems();
                        if (prod.getTipo().equals(ProductoEnum.CALIBRE.getValue())) {
                            // Obtenemos los calibres
                            lCalibres = contrVista.obtenerNumerosCalibreProducto(prod.getId(), user, time);
                            cbCalibre.addItems(lCalibres);
                            if (lCalibres.size() == 1) {
                                cbCalibre.setValue(lCalibres.get(0));
                            }
                            cbCalibre.setVisible(true);
                            cbDiametro.setVisible(false);
                        } else {
                            lDiametros = contrVista.obtenerDiametrosProducto(prod.getId(), user, time);
                            cbDiametro.addItems(lDiametros);
                            if (lDiametros.size() == 1) {
                                cbDiametro.setValue(lDiametros.get(0));
                            }
                            cbDiametro.setVisible(true);
                            cbCalibre.setVisible(false);
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
                    cbVariedad.clear();
                    cbDiametro.clear();
                }
            }
        });

        // PRODUCTO
        if (controlPt.getIdProdudcto() != null && !controlPt.getIdProdudcto().equals(-1)) {
            for (TProductos prod : lProductos) {
                if (prod.getId().equals(controlPt.getIdProdudcto())) {
                    cbProductos.setValue(prod);
                    break;
                }
            }
        }

        // VARIEDAD
        if (controlPt.getIdVariedad() != null && !controlPt.getIdVariedad().equals(-1)) {
            for (TVariedad var : lVariedades) {
                if (var.getId().equals(controlPt.getIdVariedad())) {
                    cbVariedad.setValue(var);
                    break;
                }
            }
        }

        // CALIBRE
        if (controlPt.getIdCalibre() != null && !controlPt.getIdCalibre().isEmpty()) {
            for (TProductoCalibre cal : lCalibres) {
                if (cal.getCalibre().equals(controlPt.getIdCalibre())) {
                    cbCalibre.setValue(cal);
                    break;
                }
            }
        }

        // DIAMETRO
        if (controlPt.getIdDiametro() != null && !controlPt.getIdDiametro().equals(-1)) {
            for (TDiametrosProducto diam : lDiametros) {
                if (diam.getId().equals(controlPt.getIdDiametro())) {
                    cbDiametro.setValue(diam);
                    break;
                }
            }
        }

        // Línea de producción
        cbMesas = new ComboBox("Línea: ");
        cbMesas.addItem("Línea 1");
        cbMesas.addItem("Línea 2");
        cbMesas.setRequired(true);
        cbMesas.setNullSelectionAllowed(false);
        cbMesas.setWidth(5, Sizeable.Unit.EM);
        cbMesas.setFilteringMode(FilteringMode.CONTAINS);
        cbMesas.setVisible(false);

        cbMesas.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbMesas.getValue() != null) {
                    String val = (String) cbMesas.getValue();
                    if (val.equals("Línea 1")) {
                        cbBio.setValue("No");
                        cbBio.setEnabled(false);
                        txtPesoRealConf.setVisible(false);
                        txtPesoRealCaja.setVisible(true);
                        txtPesoTeoricoConf.setVisible(false);
                        txtPesoTeoricoCaja.setVisible(true);
                        horPesConfeccion.setVisible(false);
                        horPesCaja.setVisible(true);
                    } else {
                        cbBio.setEnabled(true);
                        txtPesoRealConf.setVisible(false);
                        txtPesoRealCaja.setVisible(false);
                        txtPesoTeoricoConf.setVisible(false);
                        txtPesoTeoricoCaja.setVisible(false);
                        horPesConfeccion.setVisible(false);
                        horPesCaja.setVisible(false);
                        if (val.equals("Malla") || val.contains("Flowpack")) {
                            txtPesoRealConf.setVisible(true);
                            txtPesoTeoricoConf.setVisible(true);
                            txtPesoTeoricoCaja.setVisible(false);
                            horPesConfeccion.setVisible(true);
                            horPesCaja.setVisible(false);
                        } else {
                            txtPesoRealConf.setVisible(false);
                            txtPesoRealCaja.setVisible(true);
                            txtPesoTeoricoConf.setVisible(false);
                            txtPesoTeoricoCaja.setVisible(true);
                            horPesConfeccion.setVisible(false);
                            horPesCaja.setVisible(true);
                        }
                        if (val.equals("Mango")) {
                            for (TProductos prod : lProductos) {
                                if (prod.getDescripcion().equals("MANGO")) {
                                    cbProductos.setValue(prod);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    cbBio.setEnabled(true);
                }
            }
        });

        // FlowPack
        cbFlowPack = new ComboBox("Flowpack: ");
        cbFlowPack.addItem("1");
        cbFlowPack.addItem("2");
        cbFlowPack.setRequired(true);
        cbFlowPack.setWidth(4, Sizeable.Unit.EM);
        cbFlowPack.setFilteringMode(FilteringMode.CONTAINS);
        cbFlowPack.setVisible(false);
        horPesConfeccion.setVisible(false);
        cbFlowPack.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbFlowPack.getValue() != null) {
                    String val = (String) cbFlowPack.getValue();
                    if (val != null) {
                        horPesCaja.setVisible(false);
                        txtPesoRealConf.setVisible(true);
                        txtPesoTeoricoConf.setVisible(true);
                        txtPesoTeoricoCaja.setVisible(false);
                        txtPesoRealCaja.setVisible(false);
                        horPesConfeccion.setVisible(true);
                    }
                } else {
                    horPesCaja.setVisible(true);
                    txtPesoTeoricoConf.setVisible(false);
                    txtPesoTeoricoCaja.setVisible(true);
                    txtPesoRealConf.setVisible(false);
                    txtPesoRealCaja.setVisible(true);
                    horPesConfeccion.setVisible(false);
                }
            }
        });

        if (controlPt.getIndFlowPack() != null && !controlPt.getIndFlowPack().equals(-1) && !controlPt.getIndFlowPack().equals(0)) {
            cbFlowPack.setValue("" + controlPt.getIndFlowPack());
        }

        // Calibrador
        cbCalibrador = new ComboBox("Calibrador: ");
        cbCalibrador.addItem("Sí");
        cbCalibrador.addItem("No");
        cbCalibrador.setRequired(true);
        cbCalibrador.setNullSelectionAllowed(false);
        cbCalibrador.setWidth(4, Sizeable.Unit.EM);
        cbCalibrador.setFilteringMode(FilteringMode.CONTAINS);
        cbCalibrador.setVisible(false);

        if (controlPt.getCalibrador() != null && !cbCalibrador.isEmpty()) {
            cbCalibrador.setValue(controlPt.getCalibrador());
        }

        // Mesa confección
        cbMesaConfeccion = new ComboBox("Mesa confección: ");
        cbMesaConfeccion.addItem("Sí");
        cbMesaConfeccion.addItem("No");
        cbMesaConfeccion.setRequired(true);
        cbMesaConfeccion.setNullSelectionAllowed(false);
        cbMesaConfeccion.setWidth(4, Sizeable.Unit.EM);
        cbMesaConfeccion.setFilteringMode(FilteringMode.CONTAINS);
        cbMesaConfeccion.setVisible(false);

        if (controlPt.getMesaConfeccion() != null && !controlPt.getMesaConfeccion().isEmpty()) {
            cbMesaConfeccion.setValue(controlPt.getMesaConfeccion());
        }

        // Maduración
        cbMaduracion = new ComboBox("¿Maduración?");
        cbMaduracion.addItem("Sí");
        cbMaduracion.addItem("No");
        cbMaduracion.setValue("No");
        cbMaduracion.setNullSelectionAllowed(false);
        cbMaduracion.setWidth(4, Sizeable.Unit.EM);
        cbMaduracion.setFilteringMode(FilteringMode.CONTAINS);
        cbMaduracion.setVisible(false);
        if (controlPt.getIndMaduracion() != null && !controlPt.getIndMaduracion().equals(-1)) {
            if (Utils.booleanFromInteger(controlPt.getIndMaduracion())) {
                cbMaduracion.setValue("Sí");
            } else {
                cbMaduracion.setValue("No");
            }
        }

        // Repaso
        cbRepaso = new ComboBox("Repaso: ");
        cbRepaso.addItem("Repaso 1");
        cbRepaso.addItem("Repaso 2");
        cbRepaso.setNullSelectionAllowed(false);
        cbRepaso.setWidth(7, Sizeable.Unit.EM);
        cbRepaso.setFilteringMode(FilteringMode.CONTAINS);
        cbRepaso.setVisible(false);
        if (controlPt.getIndRepaso() != null && !controlPt.getIndRepaso().equals(-1)) {
            if (Utils.booleanFromInteger(controlPt.getIndRepaso())) {
                cbRepaso.setValue("Repaso 1");
            } else {
                cbRepaso.setValue("Repaso 2");
            }
        }

        // Mango
        cbMango = new ComboBox("¿Mango?");
        cbMango.addItem("Sí");
        cbMango.addItem("No");
        cbMango.setValue("No");
        cbMango.setNullSelectionAllowed(false);
        cbMango.setWidth(4, Sizeable.Unit.EM);
        cbMango.setFilteringMode(FilteringMode.CONTAINS);
        cbMango.setVisible(false);

        if (controlPt.getIndMango() != null && !controlPt.getIndMango().equals(-1)) {
            if (Utils.booleanFromInteger(controlPt.getIndMango())) {
                cbMango.setValue("Sí");
            } else {
                cbMango.setValue("No");
            }
        }

        // Malla
        cbMalla = new ComboBox("¿Malla?");
        cbMalla.addItem("Sí");
        cbMalla.addItem("No");
        cbMalla.setValue("No");
        cbMalla.setNullSelectionAllowed(false);
        cbMalla.setWidth(4, Sizeable.Unit.EM);
        cbMalla.setFilteringMode(FilteringMode.CONTAINS);
        cbMalla.setVisible(false);

        cbMalla.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (cbMalla.getValue() != null) {
                    String val = (String) cbMalla.getValue();
                    if (val.equals("Sí")) {
                        txtPesoRealConf.setVisible(true);
                        txtPesoRealCaja.setVisible(false);
                    } else {
                        txtPesoRealConf.setVisible(false);
                        txtPesoRealCaja.setVisible(true);
                    }
                }
            }
        });

        if (controlPt.getIndMallas() != null && !controlPt.getIndMallas().equals(-1)) {
            if (Utils.booleanFromInteger(controlPt.getIndMallas())) {
                cbMalla.setValue("Sí");
            } else {
                cbMalla.setValue("No");
            }
        }

        cbCajasIni = new ComboBox("Cajas pedido: ");
        cbCajasIni.addItems(lNumeros);
        cbCajasIni.setRequired(true);
        cbCajasIni.setNullSelectionAllowed(false);
        cbCajasIni.setWidth(5, Sizeable.Unit.EM);
        cbCajasIni.setFilteringMode(FilteringMode.CONTAINS);
        cbCajasIni.setValue(controlPt.getNumCajasPedido().intValue());

        cbCajasFin = new ComboBox("Cajas real: ");
        cbCajasFin.setRequired(true);
        cbCajasFin.addItems(lNumeros);
        cbCajasFin.setWidth(5, Sizeable.Unit.EM);
        cbCajasFin.setNullSelectionAllowed(false);
        cbCajasFin.setFilteringMode(FilteringMode.CONTAINS);

        cbCajasFin.setValue(controlPt.getNumCajasReal().intValue());

        cbNumProducto = new ComboBox("Nº de producto");
        cbNumProducto.addItem("1");
        cbNumProducto.addItem("2");
        cbNumProducto.addItem("3");
        cbNumProducto.addItem("4");
        cbNumProducto.addItem("5");
        cbNumProducto.addItem("6");
        cbNumProducto.addItem("7");
        cbNumProducto.addItem("8");
        cbNumProducto.addItem("9");
        cbNumProducto.addItem("10");
        cbNumProducto.setWidth(5, Sizeable.Unit.EM);
        cbNumProducto.setFilteringMode(FilteringMode.CONTAINS);
        cbNumProducto.setNullSelectionAllowed(false);
        cbNumProducto.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                String val = (String) cbNumProducto.getValue();

                if (cbDiametro.isVisible() && cbDiametro.getValue() == null && !cbNumProducto.getValue().equals(indexProducto)) {
                    Notification aviso = new Notification("Se debe indicar el diámetro", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    cbNumProducto.setValue(indexProducto);
                    return;
                }

                // Guardamos los datos que se están visualizando
                guardarDatosLinea();
                indexProducto = val;

                cargarDatosLinea();

            }
        });

        // LA NAVE 
        cbNaves = new ComboBox("Nave: ");
        cbNaves.setRequired(true);
        cbNaves.addItem("Nave 1");
        cbNaves.addItem("Nave 2");
        cbNaves.addItem("Nave 3");
        cbNaves.setNullSelectionAllowed(false);
        cbNaves.setWidth(5, Sizeable.Unit.EM);
        cbNaves.setFilteringMode(FilteringMode.CONTAINS);
        cbNaves.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                String val = (String) cbNaves.getValue();
                if (val.equals("Nave 1")) {
                    // Metemos 10 valores en la tabla de penetromía solo si está vacía.                   
                    cbMesas.setVisible(true);
                    cbMaduracion.setVisible(false);
                    cbRepaso.setVisible(false);
                    cbMango.setVisible(false);
                    cbMalla.setVisible(false);
                    cbFlowPack.setVisible(false);
                    cbCalibrador.setVisible(false);
                    cbMesaConfeccion.setVisible(false);

                    cbMesas.removeAllItems();
                    cbMesas.addItem("Línea 1");
                    cbMesas.addItem("Línea 2");
                } else if (val.equals("Nave 2")) {
                    cbMesas.setVisible(true);
                    cbMaduracion.setVisible(false);
                    cbRepaso.setVisible(false);
                    cbMango.setVisible(false);
                    cbMalla.setVisible(false);
                    cbFlowPack.setVisible(false);
                    cbCalibrador.setVisible(false);
                    cbMesaConfeccion.setVisible(false);
                    cbMesas.removeAllItems();
                    cbMesas.addItem("Maduración");
                    cbMesas.addItem("Mango");
                    cbMesas.addItem("Repaso");
                    cbMesas.addItem("Flowpack 1");
                    cbMesas.addItem("Flowpack 2");
                    cbMesas.addItem("Malla");
                } else if (val.equals("Nave 3")) {
                    cbMesas.setVisible(true);
                    cbMalla.setVisible(false);
                    cbCalibrador.setVisible(false);
                    cbMesaConfeccion.setVisible(false);
                    cbMaduracion.setVisible(false);
                    cbRepaso.setVisible(false);
                    cbMango.setVisible(false);
                    cbFlowPack.setVisible(false);
                    cbMesas.removeAllItems();
                    cbMesas.addItem("Malla manual");
                    cbMesas.addItem("Calibrador");
                    cbMesas.addItem("Mesa confección");
                    cbMesas.addItem("Flowpack 3");
                }
            }
        });

        tablaPesosPenetromias.removeAllItems();
        tablaPesosPenetromias.setPageLength(0);

        cbBio = new ComboBox("¿BIO?");
        cbBio.addItem("Sí");
        cbBio.addItem("No");
        cbBio.setValue("No");
        cbBio.setRequired(true);
        cbBio.setNullSelectionAllowed(false);
        cbBio.setWidth(4, Sizeable.Unit.EM);
        cbBio.setFilteringMode(FilteringMode.CONTAINS);
        if (controlPt.getIndBio() != null && !cbBio.getValue().equals(-1)) {
            if (Utils.booleanFromInteger(controlPt.getIndBio())) {
                cbBio.setValue("Sí");
            } else {
                cbBio.setValue("No");
            }
        }

        cbProductosObservaciones = new ComboBox("Producto: ");
        cbProductosObservaciones.setNullSelectionAllowed(false);
        cbProductosObservaciones.setWidth(4, Sizeable.Unit.EM);
        cbProductosObservaciones.setRequired(true);

        cbProductosObservaciones.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                String index = (String) cbProductosObservaciones.getValue();
                if (mLineas.get(index) != null) {
                    txtObservaciones.setValue(mLineas.get(index).getObservaciones());
                }
            }
        });

        cbProductosControlEtiquetado = new ComboBox("Producto: ");
        cbProductosControlEtiquetado.setNullSelectionAllowed(false);
        cbProductosControlEtiquetado.setWidth(4, Sizeable.Unit.EM);
        cbProductosControlEtiquetado.setRequired(true);

        cbProductosControlEtiquetado.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                String index = (String) cbProductosControlEtiquetado.getValue();
                if (index != null) {
                    cargarFotosTablas(Integer.valueOf(index));
                }
            }
        });

        cbProductosFotos = new ComboBox("Producto: ");
        cbProductosFotos.setNullSelectionAllowed(false);
        cbProductosFotos.setWidth(4, Sizeable.Unit.EM);
        cbProductosFotos.setRequired(true);

        cbDInternos = new ComboBox("¿D.I?");
        cbDInternos.addItem("Sí");
        cbDInternos.addItem("No");
        cbDInternos.setValue("No");
        cbDInternos.setRequired(true);
        cbDInternos.setNullSelectionAllowed(false);
        cbDInternos.setWidth(4, Sizeable.Unit.EM);
        cbDInternos.setFilteringMode(FilteringMode.CONTAINS);
        cbDInternos.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                String val = (String) cbDInternos.getValue();
                if (val.equals("Sí")) {
                    txtDInternos.setVisible(true);
                    txtDInternos.setValue("5");
                } else {
                    txtDInternos.setVisible(false);
                    txtDInternos.setValue(null);
                }
            }
        });

        cbDInExternos = new ComboBox("¿D.E?");
        cbDInExternos.addItem("Sí");
        cbDInExternos.addItem("No");
        cbDInExternos.setValue("No");
        cbDInExternos.setRequired(true);
        cbDInExternos.setNullSelectionAllowed(false);
        cbDInExternos.setWidth(4, Sizeable.Unit.EM);
        cbDInExternos.setFilteringMode(FilteringMode.CONTAINS);
        cbDInExternos.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                String val = (String) cbDInExternos.getValue();
                if (val.equals("Sí")) {
                    txtDExternos.setValue("10");
                    txtDExternos.setVisible(true);
                } else {
                    txtDExternos.setVisible(false);
                    txtDExternos.setValue(null);
                }
            }
        });

        txtDInternos = new TextField("Valor D.I: ");
        txtDInternos.setNullRepresentation("");
        txtDInternos.setVisible(false);
        txtDInternos.setWidth(4, Sizeable.Unit.EM);
        if (!controlPt.getdInternos().equals(Double.valueOf(0))) {
            cbDInternos.setValue("Sí");
            txtDInternos.setValue(df.format(controlPt.getdInternos()));
        }

        txtDExternos = new TextField("Valor D.E: ");
        txtDExternos.setNullRepresentation("");
        txtDExternos.setWidth(4, Sizeable.Unit.EM);
        txtDExternos.setVisible(false);

        if (!controlPt.getdExternos().equals(Double.valueOf(0))) {
            cbDInExternos.setValue("Sí");
            txtDExternos.setValue(df.format(controlPt.getdExternos()));
        }

        if (Utils.booleanFromInteger(controlPt.getIndNave1())) {
            cbNaves.setValue("Nave 1");
        } else if (Utils.booleanFromInteger(controlPt.getIndNave2())) {
            cbNaves.setValue("Nave 2");
        } else if (Utils.booleanFromInteger(controlPt.getIndNave3())) {
            cbNaves.setValue("Nave 3");
        }

        cbMesas.setValue(controlPt.getLinea());

        // EL TIPO DE FOTO QUE SE VA A ADJUNTAR AL INFORME DE CONTROL DE PROTUCTO TERMINADO.
        cbTipoFoto = new ComboBox("Tipo de foto: ");
        cbTipoFoto.setRequired(true);
        cbTipoFoto.setNullSelectionAllowed(false);
        cbTipoFoto.addItem("FOTO PALET");
        cbTipoFoto.addItem("FOTO CAJA");
        cbTipoFoto.addItem("FOTO ETIQUETA INTERIOR");
        cbTipoFoto.addItem("FOTO ETIQUETA EXTERIOR");
        cbTipoFoto.setNullSelectionAllowed(false);
        cbTipoFoto.setWidth(appWidth, Sizeable.Unit.EM);
        cbTipoFoto.setFilteringMode(FilteringMode.CONTAINS);

        cbTipoFoto.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Integer orden = 0;
                String indexFoto = "";
                if (cbProductosFotos.getValue() == null) {
                    Notification aviso = new Notification("Se debe indicar el producto", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    cbTipoFoto.clear();
                    return;
                } else {
                    indexFoto = cbProductosFotos.getValue().toString().trim();
                }
                if (cbTipoFoto.getValue() != null) {
                    if (cbTipoFoto.getValue().equals("FOTO PALET")) {
                        orden = mLineas.get(indexFoto).getIndFotoPale();
                        lblLeyendaFotos.setValue("Fotos necesarias: " + appPtFotosPale + ", fotos incluidas: " + mLineas.get(indexFoto).getIndFotoPale());
                    } else if (cbTipoFoto.getValue().equals("FOTO CAJA")) {
                        orden = mLineas.get(indexFoto).getIndFotoCaja();
                        lblLeyendaFotos.setValue("Fotos necesarias: " + appPtFotosCajas + ", fotos incluidas: " + mLineas.get(indexFoto).getIndFotoCaja());
                    } else if (cbTipoFoto.getValue().equals("FOTO ETIQUETA INTERIOR")) {
                        orden = mLineas.get(indexFoto).getIndFotoEtiqueta();
                        lblLeyendaFotos.setValue("Fotos necesarias: " + appPtFotosEtiqueta1 + ", fotos incluidas: " + mLineas.get(indexFoto).getIndFotoEtiqueta());
                    } else if (cbTipoFoto.getValue().equals("FOTO ETIQUETA EXTERIOR")) {
                        orden = mLineas.get(indexFoto).getIndFotoConfeccion();
                        lblLeyendaFotos.setValue("Fotos necesarias: " + appPtFotosEtiqueta2 + ", fotos incluidas: " + mLineas.get(indexFoto).getIndFotoConfeccion());
                    }

                    upload.init("basic", controlPt.getId(), indexFoto, String.valueOf(cbTipoFoto.getValue()), docsPathTempControlPt, orden, user);

                    upload.setVisible(true);
                } else {
                    upload.setVisible(false);
                }
            }
        });

        // Contaminación física
        cbContFisica = new ComboBox("¿Contaminación física?");
        cbContFisica.addItem("Sí");
        cbContFisica.addItem("No");
        cbContFisica.setNullSelectionAllowed(false);
        cbContFisica.setRequired(true);
        cbContFisica.setFilteringMode(FilteringMode.CONTAINS);
        if (controlPt.getIndContaminaFisica() != null && !controlPt.getIndContaminaFisica().equals(-1)) {
            if (Utils.booleanFromInteger(controlPt.getIndContaminaFisica())) {
                cbContFisica.setValue("Sí");
            } else {
                cbContFisica.setValue("No");
            }
        } else {
            cbContFisica.setValue("No");
        }

        // Contaminación biológica
        cbContBiologica = new ComboBox("¿Contaminación biológica?");
        cbContBiologica.addItem("Sí");
        cbContBiologica.addItem("No");
        cbContBiologica.setNullSelectionAllowed(false);
        cbContBiologica.setRequired(true);
        cbContBiologica.setFilteringMode(FilteringMode.CONTAINS);
        if (controlPt.getIndContaminaBiologica() != null && !controlPt.getIndContaminaBiologica().equals(-1)) {
            if (Utils.booleanFromInteger(controlPt.getIndContaminaBiologica())) {
                cbContBiologica.setValue("Sí");
            } else {
                cbContBiologica.setValue("No");
            }
        } else {
            cbContBiologica.setValue("No");
        }

        // Contaminación química
        cbContQuimica = new ComboBox("¿Contaminación química?");
        cbContQuimica.addItem("Sí");
        cbContQuimica.addItem("No");
        cbContQuimica.setNullSelectionAllowed(false);
        cbContQuimica.setRequired(true);
        cbContQuimica.setFilteringMode(FilteringMode.CONTAINS);
        if (controlPt.getIndContaminaQuimica() != null && !controlPt.getIndContaminaQuimica().equals(-1)) {
            if (Utils.booleanFromInteger(controlPt.getIndContaminaQuimica())) {
                cbContQuimica.setValue("Sí");
            } else {
                cbContQuimica.setValue("No");
            }
        } else {
            cbContQuimica.setValue("No");
        }

        /**
        // LAS LISTAS CON LOS PESOS
        lsPesosCajas = new ListSelect("Valores (" + appPtPesajesCaja + ")");
        lsPesosCajas.setHeight(16, Sizeable.Unit.EM);
        lsPesosCajas.addValueChangeListener(new ValueChangeListener() {
        
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (event.getProperty().getValue() != null) {
                    idSeleccionadoCajas = (NumeroPesaje) event.getProperty().getValue();
                } else {
                    idSeleccionadoCajas = null;
                }
            }
        });
        lsPesosConf = new ListSelect("Valores (" + appPtPesajesConfeccion + ")");
        lsPesosConf.setHeight(16, Sizeable.Unit.EM);
        lsBrix = new ListSelect("Valores (" + appPtPesajesBrix + ")");
        lsBrix.setHeight(16, Sizeable.Unit.EM);
        lsCalibres = new ListSelect("Valores (" + appPtPesajesCalibre + ")");
        lsCalibres.setHeight(16, Sizeable.Unit.EM);
        lsPenetromias = new ListSelect("Valores (" + appPtPesajesPenetromia + ")");
        lsPenetromias.setHeight(16, Sizeable.Unit.EM);
        */
        //txtPesoTeoricoConf.setVisible(false);

        cbFotoCajaBorrar = new ComboBox();
        cbFotoCajaBorrar.addItem("Foto Nº1");
        cbFotoCajaBorrar.addItem("Foto Nº2");
        cbFotoCajaBorrar.addItem("Foto Nº3");
        cbFotoCajaBorrar.addItem("Foto Nº4");
        cbFotoCajaBorrar.addItem("Foto Nº5");
        cbFotoCajaBorrar.setFilteringMode(FilteringMode.CONTAINS);

        cbNumProducto.setValue("1");

    }

    /**
     * Método que nos valida que se hayan informado los valores obligatorios. <br>
     * Si no se informan correctamente, devuelve false 
     * @return True si son correctos, en caso contrario false.
     */
    private boolean validaCamposObligatorios() {
        // Comprobamos si ha informado los campos obligatorios.
        return !txtNumPedido.isValid() || !cbMesas.isValid() || !cbNaves.isValid() || (txtPesoRealCaja.isVisible() && !txtPesoRealCaja.isValid()) || (txtPesoRealConf.isVisible() && !txtPesoRealConf.isValid()) || !cbClientes.isValid();
    }

    /**
     * Método que nos nutre el contenedor de información principal con los componentes necesarios.
     */
    private void generaContenedorIdentificacion() {
        indentificacionContainer = new VerticalLayout();
        indentificacionContainer.setSpacing(true);
        indentificacionContainer.setMargin(true);

        HorizontalLayout info1 = new HorizontalLayout();
        info1.setSpacing(true);

        HorizontalLayout vLineaEnvasado = new HorizontalLayout();
        vLineaEnvasado.setSpacing(true);
        vLineaEnvasado.addComponent(cbNaves);
        vLineaEnvasado.addComponent(cbMesas);
        vLineaEnvasado.addComponent(cbFlowPack);
        vLineaEnvasado.addComponent(cbMaduracion);
        vLineaEnvasado.addComponent(cbRepaso);
        vLineaEnvasado.addComponent(cbMalla);
        vLineaEnvasado.addComponent(cbCalibrador);
        vLineaEnvasado.addComponent(cbMesaConfeccion);
        vLineaEnvasado.addComponent(cbBio);

        info1.addComponent(fecha);
        info1.addComponent(cbClientes);
        info1.addComponent(txtNumPedido);
        info1.addComponent(cbCajasIni);
        info1.addComponent(cbCajasFin);

        HorizontalLayout info2 = new HorizontalLayout();
        info2.setSpacing(true);

        info2.addComponent(cbNumProducto);
        info2.addComponent(cbProductos);
        info2.addComponent(cbVariedad);
        info2.addComponent(cbCalibre);
        info2.addComponent(cbDiametro);

        HorizontalLayout info3 = new HorizontalLayout();
        info3.setSpacing(true);

        info2.addComponent(txtPesoTeoricoCaja);
        info2.addComponent(txtPesoTeoricoConf);

        indentificacionContainer.addComponent(lblLineaEnvasado);
        indentificacionContainer.setComponentAlignment(lblLineaEnvasado, Alignment.TOP_CENTER);
        indentificacionContainer.addComponent(vLineaEnvasado);
        indentificacionContainer.addComponent(lblIdentificacion);
        indentificacionContainer.addComponent(info1);
        indentificacionContainer.addComponent(info2);
        indentificacionContainer.addComponent(info3);

        HorizontalLayout cal1 = new HorizontalLayout();
        cal1.setSpacing(true);

        cal1.addComponent(cbContFisica);
        cal1.addComponent(cbContQuimica);
        cal1.addComponent(cbContBiologica);
        cal1.addComponent(cbDInternos);
        cal1.addComponent(txtDInternos);
        cal1.addComponent(cbDInExternos);
        cal1.addComponent(txtDExternos);

        // PESAJES CAJA 
        VerticalLayout val1 = new VerticalLayout();
        val1.setSpacing(true);

        val1.addComponent(txtPesoRealCaja);

        HorizontalLayout val2 = new HorizontalLayout();
        val2.setSpacing(true);
        val2.addComponent(incluirPesajeCajaButton);
        val2.addComponent(eliminarPesajeCajaButton);

        val1.addComponent(val2);

        horPesCaja.setSpacing(true);
        horPesCaja.addComponent(val1);
        //horPesCaja.addComponent(lsPesosCajas);
        horPesCaja.addComponent(tablaPesosCajas);

        // PESAJES CONFECCIÓN 
        VerticalLayout val3 = new VerticalLayout();
        val3.setSpacing(true);

        val3.addComponent(txtPesoRealConf);

        HorizontalLayout val4 = new HorizontalLayout();
        val4.setSpacing(true);
        val4.addComponent(incluirPesajeConfeccionButton);
        val4.addComponent(eliminarPesajeConfeccionButton);

        val3.addComponent(val4);

        horPesConfeccion.setSpacing(true);
        horPesConfeccion.addComponent(val3);
        //horPesConfeccion.addComponent(lsPesosConf);
        horPesConfeccion.addComponent(tablaPesosConf);

        // CONTROL CALIBRE
        VerticalLayout val5 = new VerticalLayout();
        val5.setSpacing(true);

        val5.addComponent(txtCalibre);

        HorizontalLayout val6 = new HorizontalLayout();
        val6.setSpacing(true);
        val6.addComponent(incluirCalibreButton);
        val6.addComponent(eliminarCalibreButton);

        val5.addComponent(val6);

        HorizontalLayout hor3 = new HorizontalLayout();
        hor3.setSpacing(true);
        hor3.addComponent(val5);
        //hor3.addComponent(lsCalibres);
        hor3.addComponent(tablaPesosCalibres);

        // CONTROL PENETROMÍA
        VerticalLayout val7 = new VerticalLayout();
        val7.setSpacing(true);

        val7.addComponent(txtPenetromia);

        HorizontalLayout val8 = new HorizontalLayout();
        val8.setSpacing(true);
        val8.addComponent(incluirPenetromiaButton);
        val8.addComponent(eliminarPenetromiaButton);

        val7.addComponent(val8);
        HorizontalLayout hor4 = new HorizontalLayout();
        hor4.setSpacing(true);
        hor4.addComponent(val7);
        //hor4.addComponent(lsPenetromias);
        hor4.addComponent(tablaPesosPenetromias);

        // CONTROL BRIX
        VerticalLayout val9 = new VerticalLayout();
        val9.setSpacing(true);

        val9.addComponent(txtBrix);

        HorizontalLayout val10 = new HorizontalLayout();
        val10.setSpacing(true);
        val10.addComponent(incluirBrixButton);
        val10.addComponent(eliminarBrixButton);

        val9.addComponent(val10);

        HorizontalLayout hor5 = new HorizontalLayout();
        hor5.setSpacing(true);
        hor5.addComponent(val9);
        //hor5.addComponent(lsBrix);
        hor5.addComponent(tablaPesosBrix);

        HorizontalLayout pesajes = new HorizontalLayout();
        pesajes.setSpacing(true);

        VerticalLayout calidadContainer = new VerticalLayout();
        calidadContainer.setSpacing(true);
        calidadContainer.addComponent(cal1);
        pesajes.addComponent(horPesCaja);
        pesajes.addComponent(horPesConfeccion);
        pesajes.addComponent(hor3);
        pesajes.addComponent(hor4);
        pesajes.addComponent(hor5);
        calidadContainer.addComponent(pesajes);

        indentificacionContainer.addComponent(calidadContainer);

    }

    private void generaContenedorContenedorCalidad() {
        observacionesContainer = new VerticalLayout();
        observacionesContainer.setSpacing(true);
        observacionesContainer.setVisible(false);

        observacionesContainer.addComponent(cbProductosObservaciones);
        observacionesContainer.setComponentAlignment(cbProductosObservaciones, Alignment.TOP_CENTER);
        observacionesContainer.addComponent(txtObservaciones);
        observacionesContainer.setComponentAlignment(txtObservaciones, Alignment.TOP_CENTER);

    }

    /**
     * Método que nos nutre el contenedor de las fotos con los componentes necesarios.
     */

    private void generaContenedorSubirFotos() {
        subirFotosContainer = new VerticalLayout();
        subirFotosContainer.setSpacing(true);
        subirFotosContainer.setVisible(false);

        subirFotosContainer.addComponent(cbProductosFotos);
        subirFotosContainer.setComponentAlignment(cbProductosFotos, Alignment.TOP_CENTER);

        subirFotosContainer.addComponent(cbTipoFoto);
        subirFotosContainer.setComponentAlignment(cbTipoFoto, Alignment.TOP_CENTER);

        subirFotosContainer.addComponent(lblLeyendaFotos);
        subirFotosContainer.setComponentAlignment(lblLeyendaFotos, Alignment.TOP_CENTER);

        subirFotosContainer.addComponent(upload);
        subirFotosContainer.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);

        subirFotosContainer.addComponent(guardarFotoButton);
        subirFotosContainer.setComponentAlignment(guardarFotoButton, Alignment.TOP_CENTER);
    }

    /**
     * Método que nos nutre el contenedor de las fotos con los componentes necesarios.
     */
    private void generaContenedorInformacionFotos() {
        controlEtiquetadoContainer = new VerticalLayout();
        controlEtiquetadoContainer.setSpacing(true);
        controlEtiquetadoContainer.setVisible(false);

        HorizontalLayout tablasFotos = new HorizontalLayout();
        tablasFotos.setSpacing(true);

        HorizontalLayout borrCajas = new HorizontalLayout();
        borrCajas.setSpacing(true);
        borrCajas.addComponent(cbFotoCajaBorrar);
        borrCajas.addComponent(eliminarFotoCajaButton);

        HorizontalLayout btns = new HorizontalLayout();
        btns.setSpacing(true);

        controlEtiquetadoContainer.addComponent(cbProductosControlEtiquetado);
        controlEtiquetadoContainer.setComponentAlignment(cbProductosControlEtiquetado, Alignment.TOP_CENTER);

        controlEtiquetadoContainer.addComponent(tablaFotos);
        controlEtiquetadoContainer.setComponentAlignment(tablaFotos, Alignment.TOP_CENTER);
        controlEtiquetadoContainer.addComponent(borrCajas);
        controlEtiquetadoContainer.setComponentAlignment(borrCajas, Alignment.TOP_CENTER);

        controlEtiquetadoContainer.addComponent(tablasFotos);
        controlEtiquetadoContainer.setComponentAlignment(tablasFotos, Alignment.TOP_CENTER);

        controlEtiquetadoContainer.addComponent(btns);
        controlEtiquetadoContainer.setComponentAlignment(btns, Alignment.TOP_CENTER);

        controlEtiquetadoContainer.setSizeUndefined();
        controlEtiquetadoContainer.setSizeFull();
        controlEtiquetadoContainer.setWidthUndefined();

    }

    private void accionModificarControlPt() {
        // Creamos el evento para crear un nuevo cliente con los datos introducidos en el formulario        
        if (validaCamposObligatorios()) {
            Notification aviso = new Notification("Debe informar los campos marcados con '*'", Notification.Type.ASSISTIVE_NOTIFICATION);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
            return;
        }
        if (txtPesoTeoricoCaja.isVisible() && !txtPesoTeoricoCaja.isValid()) {
            Notification aviso = new Notification("Debe informar los campos marcados con '*'", Notification.Type.ASSISTIVE_NOTIFICATION);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
            return;
        }

        if (txtPesoTeoricoConf.isVisible() && !txtPesoTeoricoConf.isValid()) {
            Notification aviso = new Notification("Debe informar los campos marcados con '*'", Notification.Type.ASSISTIVE_NOTIFICATION);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
            return;
        }

        // Guardamos los datos de la línea en el diccionario
        guardarDatosLinea();

        if (txtObservaciones.getValue() != null && !txtObservaciones.getValue().trim().isEmpty() && cbProductosObservaciones.getValue() == null && mLineas.keySet().size() > 1) {
            Notification aviso = new Notification("Se debe indicar el producto para guardar las observaciones", Notification.Type.ASSISTIVE_NOTIFICATION);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
            return;
        }

        if (txtObservaciones.getValue() != null && !txtObservaciones.getValue().trim().isEmpty()) {
            String index = cbProductosObservaciones.getValue().toString();

            mLineas.get(index).setObservaciones(txtObservaciones.getValue().trim().toUpperCase());
        }

        try {
            // Comprobamos que los datos introducidos en las líneas sean correctos
            if (validaDatosLineas()) {

                List<String> lIds = Utils.generarListaGenerica();
                lIds.addAll(mLineas.keySet());
                String text = "";
                boolean entra = false;
                for (String id : lIds) {
                    entra = false;
                    // Comprobamos los pesajes
                    if (mLineasBrix.get(id).size() < appPtPesajesBrix) {
                        text = text + "- El producto Nº " + id + ", no tiene todos los pesajes completos. ";
                        entra = true;
                    }
                    if (mLineasCajas.get(id).size() < appPtPesajesCaja && !entra) {
                        text = text + "- El producto Nº " + id + ", no tiene todos los pesajes completos. ";
                        entra = true;
                    }
                    if (mLineasCalibre.get(id).size() < appPtPesajesCalibre && !entra) {
                        text = text + "- El producto Nº " + id + ", no tiene todos los pesajes completos. ";
                        entra = true;
                    }
                    if (tablaPesosConf.isVisible() && txtPesoTeoricoConf.isVisible()) {
                        if (mLineasConfeccion.get(id).size() < appPtPesajesConfeccion && !entra) {
                            text = text + "- El producto Nº " + id + ", no tiene todos los pesajes completos. ";
                            entra = true;
                        }
                    }
                    if (mLineasPenetromia.get(id).size() < appPtPesajesPenetromia && !entra) {
                        text = text + "- El producto Nº " + id + ", no tiene todos los pesajes completos. ";
                        entra = true;
                    }
                }

                if (!text.isEmpty()) {
                    text = text + "¿Quieres continuar con la modificación?";
                    MessageBox.createQuestion().withCaption(appName).withMessage(text).withNoButton().withYesButton(() ->

                    accionCrear(), ButtonOption.caption("Sí")).open();

                } else {
                    accionCrear();
                }

            }
        } catch (GenasoftException te) {
            Notification aviso = new Notification(te.getMessage(), Notification.Type.ERROR_MESSAGE);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
        }

    }

    /**
     * Método que nos nutre los valores para generar el objeto Control de producto terminado
     * @throws GenasoftException Si se produce algún error al nutrir el objeto T_CONTROL_PRODUCTO_TERMINADO
     */
    private void construirBean() throws GenasoftException {

        String calibrador = null;
        calibrador = cbMesas.getValue().toString();
        if (calibrador.equals("Calibrador")) {
            calibrador = "Sí";
        }

        controlPt.setCalibrador(calibrador);
        controlPt.setFecha(fecha.getValue());
        controlPt.setFechaModifica(Utils.generarFecha());
        controlPt.setIdCalibre(cbCalibre.getValue() != null ? ((TProductoCalibre) cbCalibre.getValue()).getCalibre() : null);
        controlPt.setIdDiametro(cbDiametro.getValue() != null ? ((TDiametrosProducto) cbDiametro.getValue()).getId() : null);

        Object obj = cbClientes.getValue();

        if (obj.getClass().equals(TControlProductoTerminado.class)) {
            TClientes cl = (TClientes) obj;
            controlPt.setIdCliente(cl.getId());
        } else {
            // Miramos si existe el cliente a partir de lo que ha introducido
            String nombre = obj.toString().trim().toUpperCase();
            TClientes cl = contrVista.obtenerClientePorNombre(nombre, user, time);
            if (cl == null) {
                // Creamos el cliente
                cl = new TClientes();
                cl.setDescripcion(nombre);
                cl.setUsuCrea(user);
                cl.setFechaCrea(Utils.generarFecha());
                cl.setEstado(1);
                contrVista.crearClienteRetornaId(cl, user, time);
            } else {
                controlPt.setIdCliente(cl.getId());
            }
        }

        controlPt.setIdProdudcto(((TProductos) cbProductos.getValue()).getId());
        controlPt.setIdVariedad(((TVariedad) cbVariedad.getValue()).getId());
        controlPt.setIndBio((cbBio.getValue().equals("Sí") ? 1 : 0));
        controlPt.setIndContaminaBiologica(cbContBiologica.getValue().toString().equals("Sí") ? 1 : 0);
        controlPt.setIndContaminaFisica(cbContFisica.getValue().toString().equals("Sí") ? 1 : 0);
        controlPt.setIndContaminaQuimica(cbContQuimica.getValue().toString().equals("Sí") ? 1 : 0);

        /// FLOWPACK
        Integer flowpack = 0;
        String value = cbMesas.getValue().toString();
        if (value.contains("Flowpack")) {
            if (value.contains("1")) {
                flowpack = 1;
            } else if (value.contains("2")) {
                flowpack = 2;
            } else if (value.contains("3")) {
                flowpack = 3;
            }
        }
        controlPt.setIndFlowPack(flowpack);

        // MADURACIÓN
        Integer maduracion = 0;
        value = cbMesas.getValue().toString();
        if (value.equals("Maduración")) {
            maduracion = 1;
        }

        controlPt.setIndMaduracion(maduracion);

        // MALLAS     
        Integer mallas = 0;
        value = cbMesas.getValue().toString();
        if (value.equals("Malla")) {
            mallas = 1;
        }

        controlPt.setIndMallas(mallas);

        // MANGO     
        Integer mango = 0;
        value = cbMesas.getValue().toString();
        if (value.equals("Mango")) {
            mango = 1;
        }

        controlPt.setIndMango(mango);
        controlPt.setIndMesa1(cbMesas.getValue() != null && cbMesas.getValue().equals("Línea 1") ? 1 : 0);
        controlPt.setIndMesa2(cbMesas.getValue() != null && cbMesas.getValue().equals("Línea 2") ? 1 : 0);
        controlPt.setIndNave1(cbNaves.getValue() != null && cbNaves.getValue().equals("Nave 1") ? 1 : 0);
        controlPt.setIndNave2(cbNaves.getValue() != null && cbNaves.getValue().equals("Nave 2") ? 1 : 0);
        controlPt.setIndNave3(cbNaves.getValue() != null && cbNaves.getValue().equals("Nave 3") ? 1 : 0);
        Integer repaso = 0;
        if (cbRepaso.getValue() != null) {
            repaso = cbRepaso.getValue().equals("Repaso 1") ? 1 : 2;
        }

        // REPASO
        value = cbMesas.getValue().toString();
        if (value.equals("Repaso")) {
            repaso = 1;
        }

        controlPt.setIndRepaso(repaso);

        // MESA CONFECCIÓN
        String mesaConfeccion = null;;
        value = cbMesas.getValue().toString();
        if (value.equals("Mesa confección")) {
            mesaConfeccion = "Sí";
        }

        controlPt.setMesaConfeccion(mesaConfeccion);

        Double val = Double.valueOf(0);

        try {
            val = Double.valueOf(cbCajasIni.getValue().toString());
            controlPt.setNumCajasPedido(val);
        } catch (Exception e) {
            throw new GenasoftException("No se ha informado el campo '" + cbCajasIni.getCaption() + "'");
        }

        try {
            val = Double.valueOf(cbCajasFin.getValue().toString());
            controlPt.setNumCajasReal(val);

        } catch (Exception e) {
            throw new GenasoftException("No se ha informado el campo '" + cbCajasFin.getCaption() + "'");
        }

        controlPt.setNumeroPedido(txtNumPedido.getValue());

        controlPt.setNumPesajesCajas(tablaPesosCajas.getItemIds().size());
        controlPt.setNumPesajesCalibres(tablaPesosCalibres.getItemIds().size());
        controlPt.setNumPesajesConfeccion(tablaPesosConf.getItemIds().size());
        controlPt.setObservaciones(txtObservaciones.getValue());

        String val2 = "";
        if (txtPesoTeoricoCaja.isVisible()) {
            try {
                val2 = txtPesoTeoricoCaja.getValue().trim();
                if (val2.contains(",")) {
                    val2 = val2.replaceAll(",", ".");
                }
                val = Double.valueOf(val2);
                controlPt.setPesoCaja(val);

            } catch (Exception e) {
                throw new GenasoftException("No se ha informado el campo '" + txtPesoTeoricoCaja.getCaption() + "' se debe introducir un valor numérico válido.");
            }
        } else {
            try {
                val2 = txtPesoTeoricoConf.getValue().trim();
                if (val2.contains(",")) {
                    val2 = val2.replaceAll(",", ".");
                }
                val = Double.valueOf(val2);
                controlPt.setPesoCaja(val);

            } catch (Exception e) {
                throw new GenasoftException("No se ha informado el campo '" + txtPesoTeoricoConf.getCaption() + "' se debe introducir un valor numérico válido.");
            }
        }

        controlPt.setUsuModifica(user);
        controlPt.setLinea(cbMesas.getValue().toString());

        // Daños internos
        if (txtDInternos.isVisible()) {
            val2 = txtDInternos.getValue().trim();
            if (val2.contains(",")) {
                val2 = val2.replaceAll(",", ".");
            }
            val = Double.valueOf(val2);
            controlPt.setdInternos(val);
        } else {
            controlPt.setdInternos(Double.valueOf(0));
        }

        // Daños externos
        if (txtDExternos.isVisible()) {
            val2 = txtDExternos.getValue().trim();
            if (val2.contains(",")) {
                val2 = val2.replaceAll(",", ".");
            }
            val = Double.valueOf(val2);
            controlPt.setdExternos(val);
        } else {
            controlPt.setdExternos(Double.valueOf(0));
        }

    }

    private void accionCrear() {
        try {
            // Construimos el objeto cliente a partir de los datos introducidos en el formulario.
            construirBean();
            String result = contrVista.modificarControlPt(controlPt, user, time);

            Notification aviso = null;

            if (result.equals(Constants.OPERACION_OK)) {
                aviso = new Notification(contrVista.obtenerDescripcionCodigo(result), Notification.Type.ASSISTIVE_NOTIFICATION);
            } else {
                aviso = new Notification(contrVista.obtenerDescripcionCodigo(result), Notification.Type.WARNING_MESSAGE);
            }

            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
            // Accedemos a la vista de edición para añadir las fotos.
            if (result.equals(Constants.OPERACION_OK)) {

                // Creamos las diferentes líneas que se han identificado
                List<String> lIndex = Utils.generarListaGenerica();
                lIndex.addAll(mLineas.keySet());

                List<Double> lValores = Utils.generarListaGenerica();

                TLineaControlProductoTerminado linea = null;
                Integer idLinea;
                Integer id = controlPt.getId();
                for (String indexL : lIndex) {
                    linea = mLineas.get(indexL);

                    if (linea.getId() == null || linea.getId().equals(-1)) {

                        linea.setIdControlPt(controlPt.getId());
                        linea.setFechaCrea(Utils.generarFecha());
                        linea.setUsuCrea(user);
                        linea.setEstado(0);

                        idLinea = contrVista.crearLineaControlPtRetornaId(linea, user, time);

                    } else {
                        idLinea = linea.getId();
                        contrVista.modificarLineaControlPt(linea, user, time);
                    }

                    // Creamos los diferentes pesajes que se hayan identificado.
                    if (idLinea > -1) {
                        // PESAJES DE BRIX
                        lValores = mLineasBrix.get(indexL);
                        if (lValores != null && !lValores.isEmpty()) {
                            // Guardamos los pesajes de BRIX
                            contrVista.crearPesajesBrixControlPt(id, idLinea, lValores, user, time);
                        }

                        // PESAJES DE CAJAS
                        lValores = mLineasCajas.get(indexL);
                        if (lValores != null && !lValores.isEmpty()) {
                            // Guardamos los pesajes de CAJAS
                            contrVista.crearPesajesCajasControlPt(id, idLinea, lValores, user, time);
                        }

                        // PESAJES DE CALIBRES
                        lValores = mLineasCalibre.get(indexL);
                        if (lValores != null && !lValores.isEmpty()) {
                            // Guardamos los pesajes de CALIBRES
                            contrVista.crearPesajesCalibreControlPt(id, idLinea, lValores, user, time);
                        }

                        // PESAJES DE CONFECCIÓN
                        lValores = mLineasConfeccion.get(indexL);
                        if (lValores != null && !lValores.isEmpty()) {
                            // Guardamos los pesajes de CONFECCIÓN
                            contrVista.crearPesajesConfeccionControlPt(id, idLinea, lValores, user, time);
                        }

                        // PESAJES DE PENETROMÍA
                        lValores = mLineasPenetromia.get(indexL);
                        if (lValores != null && !lValores.isEmpty()) {
                            // Guardamos los pesajes de PENETROMÍA
                            contrVista.crearPesajesPenetromiaControlPt(id, idLinea, lValores, user, time);
                        }
                    }
                }
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

    /**
     * Método que nos crea las tablas para mostrar las imágenes cargadas en el control de producto terminado.
     */
    private void crearTablasImagenes() {

        // TABLA PARA MOSTRAR LAS FOTOS DE CAJAS
        tablaFotos = new Table();
        tablaFotos.addStyleName("big striped");
        // La cabecera de la tabla del filtro.
        tablaFotos.addContainerProperty("FOTO Nº 1", Image.class, null);
        tablaFotos.addContainerProperty("FOTO Nº 2", Image.class, null);
        tablaFotos.addContainerProperty("FOTO Nº 3", Image.class, null);
        tablaFotos.addContainerProperty("FOTO Nº 4", Image.class, null);
        tablaFotos.addContainerProperty("FOTO Nº 5", Image.class, null);
        tablaFotos.setPageLength(1);

    }

    /**
     * Método que nos carga las fotos en las tablas para visualizarlas.
     */
    private void cargarFotosTablas(Integer indexFoto) {

        try {
            tablaFotos.removeAllItems();

            if (!indexFoto.equals(-1)) {
                // En primer lugar, limpiamos las tablas por si hay que incuir alguna).

                File file = null;
                Image image1 = null;
                FileResource fRes = null;
                List<TControlProductoTerminadoFotos> fotos = contrVista.obtenerImagenesIdLinea(mLineas.get("" + indexFoto).getId(), user, time);

                Integer imgsCaja = 0;
                Image imgs[] = new Image[5];
                imgs[0] = new Image();
                imgs[1] = new Image();
                imgs[2] = new Image();
                imgs[3] = new Image();
                imgs[4] = new Image();

                Integer cnt = 1;
                Integer imgsTotal = 0;
                for (TControlProductoTerminadoFotos foto : fotos) {
                    file = new File(foto.getDescripcionFoto());
                    fRes = new FileResource(file);
                    image1 = new Image(null, fRes);

                    if (image1 != null && file.exists()) {
                        image1.setWidth(380, Sizeable.Unit.PIXELS);
                        image1.setHeight(300, Sizeable.Unit.PIXELS);
                        image1.setId("" + foto.getId());

                        tablaFotos.addItem(new Object[] { image1 }, foto.getId());
                        imgs[imgsCaja] = image1;
                        imgsCaja++;
                        imgsTotal++;
                    }

                    if (imgsCaja.equals(indexFotoMax)) {
                        tablaFotos.addItem(new Object[] { imgs[0], imgs[1], imgs[2], imgs[3], imgs[4] }, cnt);
                        cnt++;
                        imgsCaja = 0;
                        imgs = new Image[5];
                        imgs[0] = new Image();
                        imgs[1] = new Image();
                        imgs[2] = new Image();
                        imgs[3] = new Image();
                        imgs[4] = new Image();
                    }
                }

                tablaFotos.addItem(new Object[] { imgs[0], imgs[1], imgs[2], imgs[3], imgs[4] }, cnt);
                imgs = new Image[5];
                imgs[0] = new Image();
                imgs[1] = new Image();
                imgs[2] = new Image();
                imgs[3] = new Image();
                imgs[4] = new Image();

                tablaFotos.setPageLength(cnt);

                cbFotoCajaBorrar.removeAllItems();
                int cnt2 = 0;
                while (cnt2 < imgsTotal) {
                    cbFotoCajaBorrar.addItem("FOTO Nº" + (cnt2 + 1));
                    cnt2++;
                }

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

    private void crearTablaPesajes() {
        // TABLA PARA MOSTRAR LOS PESAJES DE CAJAS
        tablaPesosCajas = new Table();
        tablaPesosCajas.addStyleName("big striped");
        // La cabecera de la tabla del filtro.
        tablaPesosCajas.addContainerProperty("Valores", Label.class, null);
        tablaPesosCajas.setPageLength(0);
        tablaPesosCajas.setSelectable(true);
        tablaPesosCajas.setCaption("Total: " + tablaPesosCajas.getPageLength() + "(" + appPtPesajesCaja + ")");
        tablaPesosCajas.addItemClickListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoCajas = (Integer) event.getItemId();

            }

        });

        tablaPesosCajas.setCellStyleGenerator(new Table.CellStyleGenerator() {

            Double peso = null;

            @SuppressWarnings("rawtypes")
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {

                if (txtPesoTeoricoCaja.getValue() != null && !txtPesoTeoricoCaja.getValue().trim().isEmpty()) {
                    try {
                        peso = Utils.formatearValorDouble(txtPesoTeoricoCaja.getValue().trim());
                    } catch (NumberFormatException e) {
                        peso = null;
                    }
                }

                Item it = tablaPesosCajas.getItem(itemId);
                Property prop = null;

                prop = it.getItemProperty("Valores");

                if (peso != null) {
                    Label lbl = (Label) prop.getValue();

                    Double val = Utils.formatearValorDouble2(lbl.getValue());
                    if (val > peso) {
                        return "green";
                    } else {
                        return "red";
                    }
                } else {
                    return "";
                }
            }
        });

        // TABLA PARA MOSTRAR LOS PESAJES DE CONFECCIONES
        tablaPesosConf = new Table();
        tablaPesosConf.addStyleName("big striped");
        // La cabecera de la tabla del filtro.
        tablaPesosConf.addContainerProperty("Valores", Label.class, null);
        tablaPesosConf.setPageLength(0);
        tablaPesosConf.setCaption("Total: " + tablaPesosConf.getPageLength() + "(" + appPtPesajesConfeccion + ")");
        tablaPesosConf.setSelectable(true);

        tablaPesosConf.addItemClickListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoConf = (Integer) event.getItemId();

            }

        });

        tablaPesosConf.setCellStyleGenerator(new Table.CellStyleGenerator() {

            Double peso = null;

            @SuppressWarnings("rawtypes")
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {

                if (txtPesoTeoricoConf.getValue() != null && !txtPesoTeoricoConf.getValue().trim().isEmpty()) {
                    try {
                        peso = Utils.formatearValorDouble(txtPesoTeoricoConf.getValue().trim());
                    } catch (NumberFormatException e) {
                        peso = null;
                    }
                }

                Item it = tablaPesosConf.getItem(itemId);
                Property prop = null;

                prop = it.getItemProperty("Valores");

                if (peso != null) {
                    Label lbl = (Label) prop.getValue();

                    Double val = Utils.formatearValorDouble2(lbl.getValue());
                    if (val > peso) {
                        return "green";
                    } else {
                        return "red";
                    }
                } else {
                    return "";
                }
            }
        });

        // TABLA PARA MOSTRAR LOS PESAJES DE CALIBRES
        tablaPesosCalibres = new Table();
        tablaPesosCalibres.addStyleName("big striped");
        // La cabecera de la tabla del filtro.
        tablaPesosCalibres.addContainerProperty("Valores", Label.class, null);
        tablaPesosCalibres.setPageLength(0);
        tablaPesosCalibres.setCaption("Total: " + tablaPesosCalibres.getPageLength() + "(" + appPtPesajesCalibre + ")");
        tablaPesosCalibres.setSelectable(true);
        tablaPesosCalibres.addItemClickListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoCalibre = (Integer) event.getItemId();

            }

        });

        tablaPesosCalibres.setCellStyleGenerator(new Table.CellStyleGenerator() {

            TProductoCalibre cal = null;

            @SuppressWarnings("rawtypes")
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {

                Item it = tablaPesosCalibres.getItem(itemId);
                Property prop = null;

                if (cbCalibre.getValue() != null) {
                    cal = (TProductoCalibre) cbCalibre.getValue();
                } else {
                    cal = null;
                }

                prop = it.getItemProperty("Valores");

                if (cal != null) {
                    Label lbl = (Label) prop.getValue();

                    Double val = Utils.formatearValorDouble2(lbl.getValue());
                    if (val >= cal.getRangoPesoIni() && val <= cal.getRangoPesoFin()) {
                        return "green";
                    } else {
                        return "red";
                    }
                } else {
                    return "";
                }
            }
        });

        // TABLA PARA MOSTRAR LOS PESAJES DE PENETROMÍA
        tablaPesosPenetromias = new Table();
        tablaPesosPenetromias.addStyleName("big striped");
        // La cabecera de la tabla del filtro.
        tablaPesosPenetromias.addContainerProperty("Valores", Label.class, null);
        tablaPesosPenetromias.setPageLength(0);
        tablaPesosPenetromias.setCaption("Total: " + tablaPesosPenetromias.getPageLength() + "(" + appPtPesajesPenetromia + ")");
        tablaPesosPenetromias.setSelectable(true);
        tablaPesosPenetromias.addItemClickListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoPenetromia = (Integer) event.getItemId();

            }

        });

        // TABLA PARA MOSTRAR LOS PESAJES DE BRIX
        tablaPesosBrix = new Table();
        tablaPesosBrix.addStyleName("big striped");
        // La cabecera de la tabla del filtro.
        tablaPesosBrix.addContainerProperty("Valores", Label.class, null);
        tablaPesosBrix.setPageLength(0);
        tablaPesosBrix.setCaption("Total: " + tablaPesosBrix.getPageLength() + "(" + appPtPesajesBrix + ")");
        tablaPesosBrix.setSelectable(true);
        tablaPesosBrix.addItemClickListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionadoBrix = (Integer) event.getItemId();

            }

        });
    }

    private void meterValoresPenetromia() {
        int cnt = 0;

        Double val = Double.valueOf(0);
        Label lbl = null;
        NumeroPesaje valor = null;

        while (cnt < appPtPesajesPenetromia) {
            valor = new NumeroPesaje();
            //valor.setIndex(lsPesosCajas.size());
            valor.setIndex(tablaPesosPenetromias.getPageLength());

            val = Double.valueOf(13);
            valor.setValor(val);
            //lsPesosCajas.addItem(valor);
            lbl = new Label("13");
            tablaPesosPenetromias.addItem(new Object[] { lbl }, cnt);
            tablaPesosPenetromias.setPageLength(tablaPesosPenetromias.getPageLength() + 1);
            tablaPesosPenetromias.setCaption("Total: " + tablaPesosPenetromias.getPageLength() + "(" + appPtPesajesPenetromia + ")");

            cnt++;
        }
    }

    /**
     * Método que nos guarda en el diccionario de líneas del control de PT los datos que se están mostrando en pantalla creando la línea de control de PT
     * para posteriormente crear las líneas que se hayan identificado
     */
    private void guardarDatosLinea() {

        if (indexProducto != null) {
            TLineaControlProductoTerminado linea = mLineas.get(indexProducto);
            // Comprobamos si existe o no la línea en el diccionario, si existe, cargamso la línea para guardar los cambios.
            if (linea != null) {

                // Nutrimos la línea con los datos que se están visualizando por pantalla
                linea = nutrirDatosLinea(linea);

                // Guardamos los pesajes
                nutrirPesajes();

                mLineas.put(indexProducto, linea);
            } else {
                linea = new TLineaControlProductoTerminado();

                // Nutrimos la línea con los datos que se están visualizando por pantalla
                linea = nutrirDatosLinea(linea);

                // Guardamos los pesajes
                nutrirPesajes();
                // Gurdamos los datos en el diccionario.
                mLineas.put(indexProducto, linea);
            }
        }
    }

    /**
     * Método que nos guarda en el diccionario de líneas del control de PT los datos que se están mostrando en pantalla creando la línea de control de PT
     * para posteriormente crear las líneas que se hayan identificado
     */
    private TLineaControlProductoTerminado nutrirDatosLinea(TLineaControlProductoTerminado linea) {

        linea.setFechaModifica(Utils.generarFecha());
        linea.setIdCalibre(cbCalibre.getValue() != null ? ((TProductoCalibre) cbCalibre.getValue()).getCalibre() : null);
        linea.setIdDiametro(cbDiametro.getValue() != null ? ((TDiametrosProducto) cbDiametro.getValue()).getId() : null);

        TProductos prod = ((TProductos) cbProductos.getValue());
        TVariedad var = (TVariedad) cbVariedad.getValue();

        linea.setIdProdudcto(prod != null ? prod.getId() : -1);
        linea.setIdVariedad(var != null ? var.getId() : -1);
        linea.setIndContaminaBiologica(cbContBiologica.getValue().toString().equals("Sí") ? 1 : 0);
        linea.setIndContaminaFisica(cbContFisica.getValue().toString().equals("Sí") ? 1 : 0);
        linea.setIndContaminaQuimica(cbContQuimica.getValue().toString().equals("Sí") ? 1 : 0);

        Double val = Double.valueOf(0);

        try {
            val = Double.valueOf(cbCajasIni.getValue().toString());
            linea.setNumCajasPedido(val);
        } catch (Exception e) {
            linea.setNumCajasPedido(Double.valueOf(-999));
        }

        try {
            val = Double.valueOf(cbCajasFin.getValue().toString());
            linea.setNumCajasReal(val);

        } catch (Exception e) {
            linea.setNumCajasReal(Double.valueOf(-999));
        }

        linea.setNumPesajesCajas(tablaPesosCajas.getItemIds().size());
        linea.setNumPesajesCalibres(tablaPesosCalibres.getItemIds().size());
        linea.setNumPesajesConfeccion(tablaPesosConf.getItemIds().size());

        //linea.setObservaciones(txtObservaciones.getValue());

        String val2 = "";
        if (txtPesoTeoricoCaja.isVisible()) {
            try {
                val2 = txtPesoTeoricoCaja.getValue().trim();
                if (val2.contains(",")) {
                    val2 = val2.replaceAll(",", ".");
                }
                val = Double.valueOf(val2);
                linea.setPesoCaja(val);

            } catch (Exception e) {
                linea.setPesoCaja(Double.valueOf(-999));
            }
        } else {
            try {
                val2 = txtPesoTeoricoConf.getValue().trim();
                if (val2.contains(",")) {
                    val2 = val2.replaceAll(",", ".");
                }
                val = Double.valueOf(val2);
                linea.setPesoCaja(val);

            } catch (Exception e) {
                linea.setPesoCaja(Double.valueOf(-999));
            }
        }
        linea.setUsuModifica(user);

        // Daños internos
        if (txtDInternos.isVisible()) {
            val2 = txtDInternos.getValue().trim();
            if (val2.contains(",")) {
                val2 = val2.replaceAll(",", ".");
            }
            val = Double.valueOf(val2);
            linea.setdInternos(val);
        } else {
            linea.setdInternos(Double.valueOf(0));
        }

        // Daños externos
        if (txtDExternos.isVisible()) {
            val2 = txtDExternos.getValue().trim();
            if (val2.contains(",")) {
                val2 = val2.replaceAll(",", ".");
            }
            val = Double.valueOf(val2);
            linea.setdExternos(val);
        } else {
            linea.setdExternos(Double.valueOf(0));
        }

        linea.setOrigen(txtOrigen.getValue() != null ? txtOrigen.getValue().trim().toUpperCase() : null);

        if (linea.getId() == null || linea.getId().equals(-1)) {
            linea.setIndFotoCaja(0);
            linea.setIndFotoConfeccion(0);
            linea.setIndFotoEtiqueta(0);
            linea.setIndFotoPale(0);
        }

        return linea;
    }

    @SuppressWarnings("unchecked")
    private void nutrirPesajes() {
        List<Double> lValores = Utils.generarListaGenerica();

        List<Integer> lVal = Utils.generarListaGenerica();

        // PESAJES DE BRIX
        lVal.addAll((List<Integer>) tablaPesosBrix.getItemIds());
        lValores = Utils.generarListaGenerica();
        Item it = null;
        Property<?> prop = null;
        Label lbl;

        if (lVal != null) {
            for (Integer pes : lVal) {
                it = tablaPesosBrix.getItem(pes);
                prop = it.getItemProperty("Valores");
                lbl = (Label) prop.getValue();

                lValores.add(Utils.formatearValorDouble2(lbl.getValue()));
            }

            mLineasBrix.put(indexProducto, lValores);
        }
        lVal = Utils.generarListaGenerica();
        lValores = Utils.generarListaGenerica();
        // PESAJES DE CAJAS
        lVal.addAll((List<Integer>) tablaPesosCajas.getItemIds());

        if (lVal != null) {
            for (Integer pes : lVal) {
                it = tablaPesosCajas.getItem(pes);
                prop = it.getItemProperty("Valores");
                lbl = (Label) prop.getValue();

                lValores.add(Utils.formatearValorDouble2(lbl.getValue()));
            }

            mLineasCajas.put(indexProducto, lValores);
        }
        lVal = Utils.generarListaGenerica();
        lValores = Utils.generarListaGenerica();
        // PESAJES DE CALIBRES
        lVal.addAll((List<Integer>) tablaPesosCalibres.getItemIds());
        if (lVal != null) {
            for (Integer pes : lVal) {
                it = tablaPesosCalibres.getItem(pes);
                prop = it.getItemProperty("Valores");
                lbl = (Label) prop.getValue();

                lValores.add(Utils.formatearValorDouble2(lbl.getValue()));
            }

            mLineasCalibre.put(indexProducto, lValores);
        }
        lVal = Utils.generarListaGenerica();
        lValores = Utils.generarListaGenerica();
        // PESAJES DE CONFECCIÓN
        lVal.addAll((List<Integer>) tablaPesosConf.getItemIds());
        if (lVal != null) {
            for (Integer pes : lVal) {
                it = tablaPesosConf.getItem(pes);
                prop = it.getItemProperty("Valores");
                lbl = (Label) prop.getValue();

                lValores.add(Utils.formatearValorDouble2(lbl.getValue()));
            }
        }
        mLineasConfeccion.put(indexProducto, lValores);
        lVal = Utils.generarListaGenerica();
        lValores = Utils.generarListaGenerica();
        // PESAJES DE PENETROMÍA
        lVal.addAll((List<Integer>) tablaPesosPenetromias.getItemIds());
        if (lVal != null) {
            for (Integer pes : lVal) {
                it = tablaPesosPenetromias.getItem(pes);
                prop = it.getItemProperty("Valores");
                lbl = (Label) prop.getValue();

                lValores.add(Utils.formatearValorDouble2(lbl.getValue()));
            }

            mLineasPenetromia.put(indexProducto, lValores);
        }
    }

    // Mostramos en pantalla los datos que están guardados en el diccionario.
    private void cargarDatosLinea() {

        TLineaControlProductoTerminado linea = mLineas.get(indexProducto);

        DecimalFormat df = new DecimalFormat("####0.00");

        if (linea != null) {

            txtPesoTeoricoCaja.setValue(linea.getPesoCaja() != null && !linea.getPesoCaja().equals(Double.valueOf(-999)) ? df.format(linea.getPesoCaja()) : null);
            txtPesoTeoricoConf.setValue(linea.getPesoCaja() != null && !linea.getPesoCaja().equals(Double.valueOf(-999)) ? df.format(linea.getPesoCaja()) : null);
            txtOrigen.setValue(linea.getOrigen());

            // PRODUCTO
            if (linea.getIdProdudcto() != null && !linea.getIdProdudcto().equals(-1)) {
                for (TProductos prod : lProductos) {
                    if (prod.getId().equals(linea.getIdProdudcto())) {
                        cbProductos.setValue(prod);
                        break;
                    }
                }
            } else {
                cbProductos.clear();
            }

            // VARIEDAD
            if (linea.getIdVariedad() != null && !linea.getIdVariedad().equals(-1)) {
                for (TVariedad var : lVariedades) {
                    if (var.getId().equals(linea.getIdVariedad())) {
                        cbVariedad.setValue(var);
                        break;
                    }
                }
            }

            // CALIBRE
            if (linea.getIdCalibre() != null && !linea.getIdCalibre().isEmpty()) {
                for (TProductoCalibre cal : lCalibres) {
                    if (cal.getCalibre().equals(linea.getIdCalibre())) {
                        cbCalibre.setValue(cal);
                        break;
                    }
                }
            }

            // DIAMETRO
            if (linea.getIdDiametro() != null && !linea.getIdDiametro().equals(-1)) {
                for (TDiametrosProducto diam : lDiametros) {
                    if (diam.getId().equals(linea.getIdDiametro())) {
                        cbDiametro.setValue(diam);
                        break;
                    }
                }
            }

            cbCajasIni.setValue(linea.getNumCajasPedido() != null ? linea.getNumCajasPedido().intValue() : null);

            cbCajasFin.setValue(linea.getNumCajasReal() != null ? linea.getNumCajasReal().intValue() : null);

            if (linea.getdInternos() != null && !linea.getdInternos().equals(Double.valueOf(0))) {
                cbDInternos.setValue("Sí");
                txtDInternos.setValue(df.format(linea.getdInternos()));
            }

            if (linea.getdExternos() != null && !linea.getdExternos().equals(Double.valueOf(0))) {
                cbDInExternos.setValue("Sí");
                txtDExternos.setValue(df.format(linea.getdExternos()));
            }

            if (linea.getIndContaminaFisica() != null && !linea.getIndContaminaFisica().equals(-1)) {
                if (Utils.booleanFromInteger(linea.getIndContaminaFisica())) {
                    cbContFisica.setValue("Sí");
                } else {
                    cbContFisica.setValue("No");
                }
            } else {
                cbContFisica.setValue("No");
            }

            if (linea.getIndContaminaBiologica() != null && !linea.getIndContaminaBiologica().equals(-1)) {
                if (Utils.booleanFromInteger(linea.getIndContaminaBiologica())) {
                    cbContBiologica.setValue("Sí");
                } else {
                    cbContBiologica.setValue("No");
                }
            } else {
                cbContBiologica.setValue("No");
            }

            if (linea.getIndContaminaQuimica() != null && !linea.getIndContaminaQuimica().equals(-1)) {
                if (Utils.booleanFromInteger(linea.getIndContaminaQuimica())) {
                    cbContQuimica.setValue("Sí");
                } else {
                    cbContQuimica.setValue("No");
                }
            } else {
                cbContQuimica.setValue("No");
            }

            // limpiamos las tablas y mostramos los valores
            tablaPesosBrix.removeAllItems();
            tablaPesosBrix.setPageLength(0);
            tablaPesosCajas.removeAllItems();
            tablaPesosCajas.setPageLength(0);
            tablaPesosCalibres.removeAllItems();
            tablaPesosCalibres.setPageLength(0);
            tablaPesosConf.removeAllItems();
            tablaPesosConf.setPageLength(0);
            tablaPesosPenetromias.removeAllItems();
            tablaPesosPenetromias.setPageLength(0);

            List<Double> lValores;

            lValores = mLineasCajas.get(indexProducto);

            NumeroPesaje numPes = null;
            Label lbl = null;
            int i = 1;

            if (lValores != null) {
                // PESAJES CAJAS
                for (Double pesC : lValores) {
                    numPes = new NumeroPesaje();
                    numPes.setIndex(i);
                    numPes.setValor(pesC);
                    lbl = new Label("" + pesC);
                    tablaPesosCajas.addItem(new Object[] { lbl }, cnt);
                    tablaPesosCajas.setCaption("Total: " + tablaPesosCajas.getPageLength() + "(" + appPtPesajesCaja + ")");
                    cnt++;
                }
                tablaPesosCajas.setPageLength(lValores.size());

            }

            lValores = mLineasConfeccion.get(indexProducto);

            if (lValores != null) {
                // PESAJES CONFECCIÓN
                for (Double pesC : lValores) {
                    numPes = new NumeroPesaje();
                    numPes.setIndex(i);
                    numPes.setValor(pesC);
                    lbl = new Label("" + pesC);
                    tablaPesosConf.addItem(new Object[] { lbl }, cnt);
                    tablaPesosConf.setPageLength(tablaPesosConf.getPageLength() + 1);
                    tablaPesosConf.setCaption("Total: " + tablaPesosConf.getPageLength() + "(" + appPtPesajesConfeccion + ")");
                    cnt++;
                }
            }
            lValores = mLineasCalibre.get(indexProducto);
            if (lValores != null) {
                // PESAJES CALIBRE
                for (Double pesC : lValores) {
                    numPes = new NumeroPesaje();
                    numPes.setIndex(i);
                    numPes.setValor(pesC);
                    lbl = new Label("" + pesC);
                    tablaPesosCalibres.addItem(new Object[] { lbl }, cnt);
                    tablaPesosCalibres.setPageLength(tablaPesosCalibres.getPageLength() + 1);
                    tablaPesosCalibres.setCaption("Total: " + tablaPesosCalibres.getPageLength() + "(" + appPtPesajesCalibre + ")");
                    cnt++;
                }
            }
            lValores = mLineasBrix.get(indexProducto);
            if (lValores != null) {
                // PESAJES BRIX
                for (Double pesC : lValores) {
                    numPes = new NumeroPesaje();
                    numPes.setIndex(i);
                    numPes.setValor(pesC);
                    lbl = new Label("" + pesC);
                    tablaPesosBrix.addItem(new Object[] { lbl }, cnt);
                    tablaPesosBrix.setPageLength(tablaPesosBrix.getPageLength() + 1);
                    tablaPesosBrix.setCaption("Total: " + tablaPesosBrix.getPageLength() + "(" + appPtPesajesBrix + ")");
                    cnt++;
                }
            }
            lValores = mLineasPenetromia.get(indexProducto);
            if (lValores != null) {
                // PESAJES PENETROMÍA
                for (Double pesC : lValores) {
                    numPes = new NumeroPesaje();
                    numPes.setIndex(i);
                    numPes.setValor(pesC);
                    lbl = new Label("" + pesC);
                    tablaPesosPenetromias.addItem(new Object[] { lbl }, cnt);
                    tablaPesosPenetromias.setPageLength(tablaPesosPenetromias.getPageLength() + 1);
                    tablaPesosPenetromias.setCaption("Total: " + tablaPesosPenetromias.getPageLength() + "(" + appPtPesajesPenetromia + ")");
                    cnt++;
                }
            }
            if (cbNaves.getValue() != null && cbNaves.getValue().toString().equals("Nave 1")) {
                if (tablaPesosPenetromias.getPageLength() == 0) {
                    meterValoresPenetromia();
                }
            }
        } else {

            txtPesoTeoricoCaja.setValue(null);
            txtPesoTeoricoConf.setValue(null);
            txtOrigen.setValue(null);

            cbProductos.clear();
            cbCalibre.clear();
            cbDiametro.clear();
            cbCajasIni.clear();
            cbCajasFin.clear();

            cbDInternos.setValue("No");
            cbDInExternos.setValue("No");
            cbContFisica.setValue("No");
            cbContBiologica.setValue("No");
            cbContQuimica.setValue("No");

            // limpiamos las tablas y mostramos los valores
            tablaPesosBrix.removeAllItems();
            tablaPesosBrix.setPageLength(0);
            tablaPesosCajas.removeAllItems();
            tablaPesosCajas.setPageLength(0);
            tablaPesosCalibres.removeAllItems();
            tablaPesosCalibres.setPageLength(0);
            tablaPesosConf.removeAllItems();
            tablaPesosConf.setPageLength(0);
            tablaPesosPenetromias.removeAllItems();
            tablaPesosPenetromias.setPageLength(0);

            if (cbNaves.getValue() != null && cbNaves.getValue().toString().equals("Nave 1")) {
                if (tablaPesosPenetromias.getPageLength() == 0) {
                    meterValoresPenetromia();
                }
            }

        }

    }

    /**
     * Método que nos comprueba si los datos introducidos de las líneas son correctos.
     * @return True si son correctos, false en caso contrario.
     * @throws GenasoftException 
     */
    private Boolean validaDatosLineas() throws GenasoftException {
        Boolean result = true;

        List<String> lIds = Utils.generarListaGenerica();

        lIds.addAll(mLineas.keySet());

        TLineaControlProductoTerminado linea;
        for (String index : lIds) {
            linea = mLineas.get(index);

            //if (linea.getIdDiametro() == null) {
            //    throw new GenasoftException("No se ha identificado el calibre o el diámetro en el producto Nº" + index);
            //}

            if (linea.getNumCajasPedido() == null || linea.getNumCajasPedido().equals(Double.valueOf(-999))) {
                throw new GenasoftException("No se ha identificado el nº de cajas del pedido en el producto Nº" + index);
            }

            if (linea.getNumCajasReal() == null || linea.getNumCajasReal().equals(Double.valueOf(-999))) {
                throw new GenasoftException("No se ha identificado el nº de cajas real en el producto Nº" + index);
            }

            if (linea.getIdProdudcto() == null || linea.getIdProdudcto().equals(-1)) {
                throw new GenasoftException("No se ha identificado el producto en el producto Nº" + index);
            }

            if (linea.getIdVariedad() == null || linea.getIdVariedad().equals(-1)) {
                throw new GenasoftException("No se ha identificado la variedad en el producto Nº" + index);
            }

            if (linea.getPesoCaja() == null || linea.getPesoCaja().equals(Double.valueOf(-999))) {
                throw new GenasoftException("No se ha identificado el peso teórico en el producto Nº" + index);
            }

            if (linea.getPesoCaja() == null || linea.getPesoCaja().equals(Double.valueOf(-999))) {
                throw new GenasoftException("No se ha identificado lel peso teórico en el producto Nº" + index);
            }

        }

        return result;
    }

}
