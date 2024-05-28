/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Daniel Carmona Romero
 * Vista del menu principal.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaMenuPrincipalAdministrador.NAME)
public class VistaMenuPrincipalAdministrador extends CustomComponent implements View ,Button.ClickListener {

    /** El nombre de la vista.*/
    public static final String            NAME = "menuAdmin";
    /** El empleado que está logueado.*/
    private TEmpleados                    empleado;
    /** Los permisos del empleado logueado.*/
    private TPermisos                     permisos;
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log  = org.slf4j.LoggerFactory.getLogger(VistaMenuPrincipalAdministrador.class);
    /** El usuario que está logado. */
    private Integer                       user = null;
    /** La fecha en que se inició sesión. */
    private Long                          time = null;

    /// Creamos los botones necesarios para cada entorno.
    /** Botón para mostrar el entorno de maestros.*/
    private Button                        bMaestros;
    /** Botón para mostrar la pantalla para el registro de pesajes.*/
    private Button                        bRegistroPesajes;
    /** Botón para mostrar el entorno de control de pesajes.*/
    private Button                        bPesajes;
    /** Botón para mostrar el entorno de facturación.*/
    private Button                        bFacturacion;

    @PostConstruct
    void init() {

        setSizeFull();
        // Establecemos el tamaño de la pantalla.
        setHeightUndefined();
        // Creamos los botones.
        crearBotones();
        // Los eventos de los botones.
        eventosBotones();

    }

    /**
     * Este método nos sirve para realizar la validación de los permisos que tiene el empleado logueado.
     */
    @Override
    public void enter(ViewChangeEvent event) {
        user = null;
        time = null;
        user = (Integer) getSession().getAttribute("user");
        if (getSession().getAttribute("fecha") != null) {
            time = Long.parseLong((String) getSession().getAttribute("fecha"));
        }
        if (time != null) {
            try {

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

                if (!Utils.booleanFromInteger(permisos.getMenuPrincipal())) {
                    Notification aviso = new Notification("No se tienen permisos para acceder a la pantalla indicada", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                    return;
                }

                setCompositionRoot(crearInterfaz());

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

    @Override
    public void buttonClick(ClickEvent event) {

    }

    /**
     * Este metodo nos crea el evento para cada botón.
     */
    private void eventosBotones() {

        // Evento para acceder a la vista principal de pedidos.
        bMaestros.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                getUI().getNavigator().navigateTo(VistaMenuPrincipalTrazabilidades.NAME + "/" + empleado.getId());

            }
        });

        // Evento para crear un nuevo empleado.
        bRegistroPesajes.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                getUI().getNavigator().navigateTo(VistaMenuPrincipalControlProductoTerminado.NAME + "/" + empleado.getId());

            }
        });

        // Evento para crear un nuevo empleado.
        bPesajes.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                try {

                    Thread.sleep(7 * 1000);

                    new ProcessBuilder("C:\\Trazabilidades\\MySQLBackups\\mysqlbackup.bat").start();

                    Notification aviso = new Notification("Copia realizada correctamente", Notification.Type.HUMANIZED_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                } catch (Exception e) {

                }

            }
        });

    }

    /**
     * Método que nos crea los botones
     */
    private void crearBotones() {
        if (bMaestros == null) {
            bMaestros = new Button("Entorno trazabilidades", this);
            bMaestros.addStyleName("wide tall big");
            bMaestros.setWidth(15, Sizeable.Unit.EM);
            bMaestros.setHeight(4, Sizeable.Unit.EM);
            bRegistroPesajes = new Button("Entorno control de PT", this);
            bRegistroPesajes.addStyleName("wide tall big");
            bRegistroPesajes.setWidth(15, Sizeable.Unit.EM);
            bRegistroPesajes.setHeight(4, Sizeable.Unit.EM);
            bPesajes = new Button("Copia de seguridad", this);
            bPesajes.addStyleName("wide tall big");
            bPesajes.setWidth(15, Sizeable.Unit.EM);
            bPesajes.setHeight(4, Sizeable.Unit.EM);

        }
    }

    /**
     * Método que se encarga de presentar los componentes en la pantalla.
     */
    private VerticalLayout crearInterfaz() {
        Label texto = new Label("Menú principal");
        texto.setStyleName("tituloTamano18");
        texto.setHeight(2, Sizeable.Unit.EM);
        // Incluimos en el layout los componentes
        VerticalLayout titulo = new VerticalLayout(texto);

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(new MenuAdministrador(permisos, user));

        // Layout para los componentes.
        HorizontalLayout body = new HorizontalLayout();
        body.setSizeFull();
        body.setSpacing(true);
        body.setMargin(true);

        // Los botones
        VerticalLayout botones = incluirBotones();

        ComboBox cbEjercicio = new ComboBox();

        // Las imágenes
        VerticalLayout imgs = imagenes();
        botones.setWidth(50, Sizeable.Unit.PERCENTAGE);

        // Añadimos al container body la chicha y la limoná.
        body.addComponent(botones);
        body.setComponentAlignment(botones, Alignment.MIDDLE_CENTER);
        body.addComponent(imgs);
        body.setComponentAlignment(imgs, Alignment.MIDDLE_LEFT);

        // Creamos y añadimos el logo de Genal Biomasa a la pantalla
        HorizontalLayout imgGenalBiomasa = contrVista.logoGenaSoft();

        viewLayout.addComponent(imgGenalBiomasa);
        viewLayout.addComponent(imgGenalBiomasa);
        viewLayout.setComponentAlignment(imgGenalBiomasa, Alignment.TOP_RIGHT);
        viewLayout.addComponent(titulo);
        viewLayout.setComponentAlignment(titulo, Alignment.MIDDLE_RIGHT);
        viewLayout.addComponent(body);
        // Añadimos el logo del cliente
        //viewLayout.addComponent(contrVista.logoCliente());
        viewLayout.setExpandRatio(body, 0.1f);

        return viewLayout;
    }

    /**
     * Método que se encarga de colocar los botones en la pantalla.
     * @return El contenedor con los botones.
     */
    private VerticalLayout incluirBotones() {
        Label l1 = new Label(" ");
        l1.setHeight(7, Sizeable.Unit.EM);
        Label l2 = new Label(" ");
        l2.setHeight(3, Sizeable.Unit.EM);
        Label l3 = new Label(" ");
        l3.setHeight(3, Sizeable.Unit.EM);
        Label l4 = new Label(" ");
        l4.setHeight(3, Sizeable.Unit.EM);
        Label l5 = new Label(" ");
        l5.setHeight(3, Sizeable.Unit.EM);
        // Layout para los botones.
        VerticalLayout botones = new VerticalLayout();
        botones.setSpacing(true);

        botones.addComponent(l1);

        if (Utils.booleanFromInteger(permisos.getAppTrazabilidades())) {
            botones.addComponent(bMaestros);
            botones.setComponentAlignment(bMaestros, Alignment.MIDDLE_RIGHT);
            botones.addComponent(l5);
        }

        if (Utils.booleanFromInteger(permisos.getAppControlPt())) {
            botones.addComponent(bRegistroPesajes);
            botones.setComponentAlignment(bRegistroPesajes, Alignment.MIDDLE_RIGHT);
            botones.addComponent(l2);
        }

        if (Utils.booleanFromInteger(permisos.getCopiaSeguridad())) {
            botones.addComponent(bPesajes);
            botones.setComponentAlignment(bPesajes, Alignment.MIDDLE_RIGHT);
            botones.addComponent(l4);
        }

        return botones;
    }

    private VerticalLayout imagenes() {
        // Obtenemos las imágenes del contenedor
        Resource resource1 = new ThemeResource("logo/logo_principal.png");
        Resource resource2 = new ThemeResource("images/img2.png");
        Resource resource3 = new ThemeResource("images/img3.png");
        Resource resource4 = new ThemeResource("images/img4.png");
        Resource resource5 = new ThemeResource("images/img5.png");
        // Cargamos las imágenes desde el objeto Image.
        Image image1 = new Image(null, resource1);
        Image image2 = new Image(null, resource2);
        Image image3 = new Image(null, resource3);
        Image image4 = new Image(null, resource4);
        Image image5 = new Image(null, resource5);

        // Logo de TRAZABILIDADES[Natural TROPIC]
        VerticalLayout imgs = new VerticalLayout();
        imgs.setSpacing(true);
        image1.setWidth(40, Sizeable.Unit.EM);
        image2.setWidth(15, Sizeable.Unit.EM);
        image3.setWidth(15, Sizeable.Unit.EM);
        image4.setWidth(15, Sizeable.Unit.EM);
        image5.setWidth(15, Sizeable.Unit.EM);

        HorizontalLayout hor1 = new HorizontalLayout();
        hor1.setSpacing(true);

        HorizontalLayout hor2 = new HorizontalLayout();
        hor2.setSpacing(true);

        hor1.addComponent(image1);
        //hor1.addComponent(image2);
        //hor1.addComponent(image5);
        //hor2.addComponent(image3);
        //hor2.addComponent(image4);

        imgs.addComponent(hor1);
        imgs.setComponentAlignment(hor1, Alignment.MIDDLE_CENTER);
        //imgs.addComponent(hor2);
        //imgs.setComponentAlignment(hor2, Alignment.MIDDLE_CENTER);
        //imgs.addComponent(hor2);
        //imgs.setComponentAlignment(hor2, Alignment.MIDDLE_RIGHT);

        return imgs;
    }

}