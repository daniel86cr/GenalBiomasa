/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas;

import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.vistas.facturas.VistaListadoFacturas;
import com.dina.genasoft.vistas.maestros.empleados.VistaEmpleado;
import com.dina.genasoft.vistas.pesajes.VistaListadoPesajes;
import com.dina.genasoft.vistas.pesajes.VistaNuevoPesaje;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

/**
 * @author Daniel Carmona Romero 
 * El menú superior de la aplicación.
 */

@UIScope
@SuppressWarnings("serial")
@SpringComponent
public class MenuAdministrador extends CustomComponent {
    /** Evento de cerrar sesión. */
    private MenuBar.Command cerrarSesion;
    /** Evento para ir al menú Administrador.. */
    private MenuBar.Command menuAdminCommand;
    /** Evento de mostrar los datos del empleado actual. */
    private MenuBar.Command misDatos;
    /** Evento que muestra la vista de las cajas. */
    private MenuBar.Command entornoMaestros;
    /** Evento que muestra la vista de las cajas. */
    private MenuBar.Command inventarioPesajes;
    /** Evento que muestra la vista de las cajas. */
    private MenuBar.Command listadoPesajes;
    /** Evento que muestra la vista de los productos. */
    private MenuBar.Command entornoFacturacion;
    /** Evento que muestra la vista de las cajas. */
    private MenuBar.Command registroPesajes;
    /** Evento que muestra la vista para crear pedidos de envases. */
    private MenuBar.Command acercaDe;
    /** El Id del empleado logado. */
    private Integer         idEmpleado;
    @Value("${app.name}")
    private String          appName;
    /** Los permisos de los que se nutre el menú. */
    private TPermisos       permisos;

    public MenuAdministrador(TPermisos perm, Integer userId) {
        super();
        idEmpleado = userId;
        permisos = perm;
        init();
    }

    private void init() {

        MenuBar barmenu = new MenuBar();

        // Creamos los eventos de los elementos del menú.
        eventosMenus();

        // Submenu item with a sub-submenu
        MenuItem inicio = barmenu.addItem("Inicio", null, null);
        inicio.addItem("Mis Datos", new ThemeResource("icons/addUser-16.ico"), misDatos);
        if (Utils.booleanFromInteger(permisos.getMenuPrincipal())) {
            inicio.addItem("Menú principal", new ThemeResource("icons/menu-16.ico"), menuAdminCommand);
        }
        inicio.addItem("Cerrar sesión", new ThemeResource("icons/logout-16.ico"), cerrarSesion);

        MenuItem listados = barmenu.addItem("Entornos", null, null);
        if (Utils.booleanFromInteger(permisos.getEntornoMaestros())) {
            listados.addItem("Entorno Maestros", new ThemeResource("icons/addUser-16.ico"), entornoMaestros);
        }
        if (Utils.booleanFromInteger(permisos.getCrearPesaje())) {
            listados.addItem("Registo Pesajes", new ThemeResource("icons/addUser-16.ico"), registroPesajes);
        }
        if (Utils.booleanFromInteger(permisos.getEntornoPesajes())) {
            listados.addItem("Inventario pesajes", new ThemeResource("icons/report-16.ico"), inventarioPesajes);
        }
        if (Utils.booleanFromInteger(permisos.getEntornoFacturacion())) {
            // Submenu item with a sub-submen         
            listados.addItem("Entorno facturación", new ThemeResource("icons/report-16.ico"), entornoFacturacion);
        }

        // Yet another top-level item
        MenuItem menuAcercaDe = barmenu.addItem("Acerca de", null, null);
        menuAcercaDe.addItem("Genasoft", new ThemeResource("icons/globe-16.ico"), acercaDe);

        this.setCompositionRoot(barmenu);
    }

    private void eventosMenus() {
        // Define a common menu command for all the menu items
        cerrarSesion = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {
                MessageBox.createQuestion().withCaption(appName).withMessage("¿Quieres cerrar sesión?").withNoButton().withYesButton(() ->

                cierraSesion(), ButtonOption.caption("Sí")).open();
            }

        };

        misDatos = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaEmpleado.NAME + "/" + idEmpleado);
            }

        };

        menuAdminCommand = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaMenuPrincipalAdministrador.NAME + "/" + idEmpleado);
            }

        };

        entornoMaestros = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaMenuPrincipalMaestros.NAME + "/" + idEmpleado);
            }

        };

        registroPesajes = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaNuevoPesaje.NAME + "/" + idEmpleado);
            }

        };

        inventarioPesajes = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaListadoPesajes.NAME + "/" + idEmpleado);
            }

        };

        entornoFacturacion = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaListadoFacturas.NAME + "/" + idEmpleado);
            }

        };

        acercaDe = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                final Window window = new Window("Genasoft");
                window.setWidth(200.0f, Unit.PIXELS);
                window.setHeight(300.0f, Unit.PIXELS);
                final FormLayout content = new FormLayout();
                window.setPositionX(400);
                window.setPositionY(60);

                // Obtenemos las imágenes del contenedor
                Resource resource1 = new ThemeResource("logo/appLogo3.png");
                // Cargamos las imágenes desde el objeto Image.
                Image image1 = new Image(null, resource1);
                // Logo de TRAZABILIDADES[Natural TROPIC]
                VerticalLayout imgs = new VerticalLayout();
                imgs.setSpacing(true);
                imgs.addComponent(image1);
                imgs.setComponentAlignment(image1, Alignment.MIDDLE_CENTER);

                Label lbl2 = new Label();
                lbl2.setValue("Genal Biomasa");
                lbl2.setStyleName("tituloTamano16");

                Label lbl3 = new Label();
                lbl3.setValue("Versión 1.0.000");
                lbl3.setStyleName("tituloTamano12");
                Label lbl1 = new Label();
                Label lbl11 = new Label();
                Label lbl12 = new Label();

                lbl1.setValue("DANIEL CARMONA ROMERO");
                lbl1.setWidth("150px");
                lbl1.setStyleName("tituloTamano12");
                lbl11.setValue("TEL +34 618.930.273");
                lbl11.setWidth("150px");
                lbl11.setStyleName("tituloTamano12");
                lbl12.setValue("daniel86cr@gmail.com");
                lbl12.setStyleName("tituloTamano12");

                imgs.addComponent(lbl2);
                imgs.addComponent(lbl12);
                imgs.addComponent(lbl3);
                imgs.addComponent(lbl1);
                imgs.addComponent(lbl11);

                content.setMargin(true);
                content.addComponent(imgs);
                content.setComponentAlignment(imgs, Alignment.TOP_CENTER);
                window.setContent(content);
                getUI().addWindow(window);
            }
        };

    }

    /**
     * Método que nos sirve para cerrar sesión del empleado activo.
     */
    private void cierraSesion() {
        getUI().getNavigator().navigateTo(VistaInicioSesion.NAME + "/cierre");
    }

}
