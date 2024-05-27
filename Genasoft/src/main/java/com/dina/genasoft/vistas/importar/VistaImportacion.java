/**
ç * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.importar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.UploadFileEmail;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.vistas.Menu;
import com.dina.genasoft.vistas.VistaInicioSesion;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
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
 * daniel86cr@gmail.com 618.930.273
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaImportacion.NAME)
public class VistaImportacion extends CustomComponent implements View ,Button.ClickListener {
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;
    /** El nombre de la vista.*/
    public static final String            NAME                 = "vImportacion";
    /** El boton para crear el coste del proveedor.*/
    private Button                        importarFichero;
    /** La caja de texto para indicar nombre descriptivo.*/
    private TextField                     txtNombreImportacion;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** Contendrá el ancho de los componentes.*/
    @Value("${app.width}")
    private Integer                       appWidth;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log                  = org.slf4j.LoggerFactory.getLogger(VistaImportacion.class);
    // Los campos obligatorios
    private ComboBox                      cbTipoFichero;
    /** El usuario que está logado. */
    private Integer                       user                 = null;
    /** La fecha en que se inició sesión. */
    private Long                          time                 = null;
    @Value("${docs.path.temp}")
    private String                        docsPathTemp;
    private UploadFileEmail               upload;
    private TPermisos                     permisos;
    private TEmpleados                    empleado;
    private static final String           FICHERO_COMPRAS      = "Compras";
    private static final String           FICHERO_VENTAS       = "Movimientos ventas";
    private static final String           FICHERO_LOTES_VENTAS = "Trazabilidad ventas";
    private static final String           FICHERO_ALMACEN      = "Movimientos almacén";
    private DateField                     fecha;

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton().equals(importarFichero)) {

            // Enviamos el correo a todos los clientes que hayamos indicado
            try {
                if (contrVista.obtenerEjercicioActivo(user, time).equals(-1)) {
                    Notification aviso = new Notification("Se debe indicar el ejercicio con el que trabajar.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }
                if (fecha.getValue() == null) {
                    Notification aviso = new Notification("Se debe indicar la fecha para tener en cuenta las compras/ventas.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                }

                if (upload.getPath() == null || upload.getPath().isEmpty()) {
                    Notification aviso = new Notification("Se debe adjuntar previamente el fichero a importar", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                } else {
                    if (!upload.getPath().endsWith(".txt") && !upload.getPath().endsWith(".csv")) {
                        Notification aviso = new Notification("El formato admitido es *.txt o *.csv con campos delimitado por '\t' ", Notification.Type.WARNING_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                    } else {

                        String result = Constants.OPERACION_OK;

                        String nombre = txtNombreImportacion.getValue();

                        if (nombre == null || nombre.isEmpty()) {
                            nombre = "Fecha importación " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Utils.generarFecha());
                        }

                        // Se envía a todos los clientes activos
                        if (cbTipoFichero.getValue().equals(FICHERO_COMPRAS)) {
                            result = contrVista.importarFicheroCompras(upload.getPath(), nombre, user, time);
                            txtNombreImportacion.setValue(null);
                            // Cargamos las compras desde BD
                        } else if (cbTipoFichero.getValue().equals(FICHERO_VENTAS)) {
                            result = contrVista.importarFicheroVentas(upload.getPath(), nombre, user, time);
                            txtNombreImportacion.setValue(null);
                        } else if (cbTipoFichero.getValue().equals(FICHERO_LOTES_VENTAS)) {
                            result = contrVista.importarFicheroLotesVentas(upload.getPath(), nombre, new SimpleDateFormat("dd/MM/yyyy").format(fecha.getValue()), user, time);
                            txtNombreImportacion.setValue(null);
                        } else {
                            result = contrVista.importarFicheroMovimientosAlmacen(upload.getPath(), nombre, new SimpleDateFormat("dd/MM/yyyy").format(fecha.getValue()), user, time);
                            txtNombreImportacion.setValue(null);
                        }

                        // Eliminamos el fichero.
                        if (upload.getPath() != null) {
                            File f = new File(upload.getPath());
                            f.delete();
                            upload.setPath(null);
                        }

                        Notification aviso = new Notification(result, Notification.Type.ASSISTIVE_NOTIFICATION);
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
                getSession().setAttribute("user", null);
                getSession().setAttribute("fecha", null);
                // Redirigimos a la página de inicio.
                getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
            } catch (MyBatisSystemException e) {
                Notification aviso = new Notification("No se ha podido establecer conexión con la base de datos.", Notification.Type.ERROR_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                log.error("Error al obtener datos de base de datos ", e);
            } catch (Exception e) {
                Notification aviso = new Notification("Error al procesar los datos, contacte con el administrador: ." + e.getMessage(), Notification.Type.ERROR_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                log.error("Error al obtener datos de base de datos ", e);

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
        // Creamos los botones de la pantalla.
        crearBotones();

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

                fecha = new DateField("Fechas Compras/ventas: ");

                Calendar cal = Calendar.getInstance();

                cal.setTime(Utils.generarFecha());

                cal.add(Calendar.DAY_OF_YEAR, -60);

                fecha.setValue(cal.getTime());

                // Para indicar todos los clientes como destinatario.
                cbTipoFichero = new ComboBox();
                cbTipoFichero.setCaption("Tipo de fichero a importar");
                cbTipoFichero.setNullSelectionAllowed(false);
                cbTipoFichero.addItem(FICHERO_COMPRAS);
                cbTipoFichero.addItem(FICHERO_VENTAS);
                cbTipoFichero.addItem(FICHERO_LOTES_VENTAS);
                cbTipoFichero.addItem(FICHERO_ALMACEN);
                cbTipoFichero.setValue(FICHERO_COMPRAS);
                cbTipoFichero.setFilteringMode(FilteringMode.CONTAINS);

                cbTipoFichero.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        if (cbTipoFichero.getValue().equals(FICHERO_LOTES_VENTAS) || cbTipoFichero.getValue().equals(FICHERO_ALMACEN)) {
                            fecha.setVisible(true);
                        } else {
                            fecha.setVisible(false);
                            Calendar cal = Calendar.getInstance();

                            cal.setTime(Utils.generarFecha());

                            cal.add(Calendar.DAY_OF_YEAR, -60);
                            fecha.setValue(cal.getTime());
                        }
                    }
                });

                Label texto = new Label("Importación de ficheros");
                texto.setStyleName("tituloTamano18");
                //texto.setHeight(10, Sizeable.Unit.EM);
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

                if (!Utils.booleanFromInteger(permisos.getImportar())) {
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

                try {
                    upload = new UploadFileEmail();
                    upload.init("basic", docsPathTemp);
                } catch (Exception e) {

                }

                HorizontalLayout up = new HorizontalLayout();

                Label lbl1 = new Label("");
                lbl1.setHeight(15, Sizeable.Unit.EM);

                Label lbl2 = new Label("");
                lbl2.setHeight(3, Sizeable.Unit.EM);

                Label lbl3 = new Label("");
                lbl3.setHeight(3, Sizeable.Unit.EM);

                Label lbl4 = new Label(" ");
                lbl4.setHeight(3, Sizeable.Unit.EM);

                txtNombreImportacion = new TextField("Nombre importación");
                txtNombreImportacion.setNullRepresentation("");
                txtNombreImportacion.setWidth(appWidth, Sizeable.Unit.EM);
                txtNombreImportacion.setMaxLength(245);

                up.setSpacing(true);
                up.addComponent(upload);
                up.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
                //up.addComponent(txtNombreImportacion);
                //up.setComponentAlignment(txtNombreImportacion, Alignment.MIDDLE_CENTER);

                // Creamos y añadimos el logo de Genal Biomasa a la pantalla
                HorizontalLayout imgGenalBiomasa = contrVista.logoGenaSoft();

                viewLayout.addComponent(imgGenalBiomasa);
                viewLayout.setComponentAlignment(imgGenalBiomasa, Alignment.TOP_RIGHT);

                /**HorizontalLayout horas = new HorizontalLayout();
                horas.setSpacing(true);
                horas.addComponent(lblTiempoEstimado);
                horas.addComponent(lbHoraImportacion);
                */

                HorizontalLayout hor = new HorizontalLayout();
                hor.setSpacing(true);
                hor.addComponent(cbTipoFichero);
                hor.addComponent(fecha);

                viewLayout.addComponent(titulo);
                viewLayout.setComponentAlignment(titulo, Alignment.TOP_CENTER);
                viewLayout.addComponent(lbl1);
                viewLayout.addComponent(hor);
                viewLayout.setComponentAlignment(hor, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(lbl2);
                viewLayout.addComponent(up);
                viewLayout.setComponentAlignment(up, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(lbl3);
                //viewLayout.addComponent(lblTiempoEstimado);
                //viewLayout.addComponent(lbHoraImportacion);
                viewLayout.addComponent(importarFichero);
                viewLayout.setComponentAlignment(importarFichero, Alignment.MIDDLE_CENTER);
                viewLayout.addComponent(lbl4);

                ///viewLayout.addComponent(progressBar);
                //viewLayout.addComponent(status);

                // Añadimos el logo del cliente
                viewLayout.addComponent(contrVista.logoCliente());

                setCompositionRoot(viewLayout);
                // Establecemos el porcentaje de ratio para los layouts
                //viewLayout.setExpandRatio(titulo, 0.1f);
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
        importarFichero = new Button("Importar fichero", this);
    }

}
