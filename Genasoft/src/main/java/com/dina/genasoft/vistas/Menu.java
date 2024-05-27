/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas;

import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.vistas.balancemasas.VistaBalanceMasas;
import com.dina.genasoft.vistas.comprasVentas.VistaErrores;
import com.dina.genasoft.vistas.comprasVentas.VistaTrazabilidades;
import com.dina.genasoft.vistas.controlpt.VistaListadoControlPt;
import com.dina.genasoft.vistas.empleados.VistaEmpleado;
import com.dina.genasoft.vistas.empleados.VistaListadoEmpleados;
import com.dina.genasoft.vistas.importar.VistaImportacion;
import com.dina.genasoft.vistas.librotrazabilidad.VistaLibroTrazabilidad;
import com.dina.genasoft.vistas.productos.VistaListadoProductos;
import com.dina.genasoft.vistas.proveedores.VistaListadoProveedores;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
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
public class Menu extends CustomComponent {
    /** Evento de cerrar sesión. */
    private MenuBar.Command cerrarSesion;
    /** Evento para ir al menú Administrador.. */
    private MenuBar.Command menuAdminCommand;
    /** Evento de mostrar los datos del empleado actual. */
    private MenuBar.Command misDatos;
    /** Evento que muestra la vista de las cajas. */
    private MenuBar.Command importarFicheros;
    /** Evento que muestra la vista de las cajas. */
    private MenuBar.Command comprasVentas;
    /** Evento que muestra la vista de las cajas. */
    private MenuBar.Command trazabilidades;
    /** Evento que muestra la vista de las cajas. */
    private MenuBar.Command listadoErrores;
    /** Evento que muestra la vista de los productos. */
    private MenuBar.Command listarProductos;
    /** Evento que muestra la vista del control de  producto terminado. */
    private MenuBar.Command listarPT;
    /** Evento que muestra la vista de los empleados. */
    private MenuBar.Command listarEmpleados;
    /** Evento que muestra la vista de los clientes. */
    private MenuBar.Command balanceMasas;
    /** Evento que muestra la vista de los clientes. */
    private MenuBar.Command listarClientes;
    /** Evento que muestra la vista de los proveedores. */
    private MenuBar.Command listarProveedores;
    /** Evento para realizar copias de seguridad. */
    private MenuBar.Command copiaSeguridad;
    /** Evento que muestra la vista para crear pedidos de envases. */
    private MenuBar.Command acercaDe;
    /** El Id del empleado logado. */
    private Integer         idEmpleado;
    @Value("${app.name}")
    private String          appName;
    /** Los permisos de los que se nutre el menú. */
    private TPermisos       permisos;

    public Menu(TPermisos perm, Integer userId) {
        super();
        permisos = perm;
        idEmpleado = userId;
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

        if (Utils.booleanFromInteger(permisos.getMenuListados())) {
            MenuItem listados = barmenu.addItem("Listados", null, null);
            if (Utils.booleanFromInteger(permisos.getListarClientes())) {
                listados.addItem("Clientes", new ThemeResource("icons/addUser-16.ico"), listarClientes);
            }
            if (Utils.booleanFromInteger(permisos.getListarControlPt())) {
                // Submenu item with a sub-submen         
                listados.addItem("Control PT", new ThemeResource("icons/report-16.ico"), listarPT);
            }
            if (Utils.booleanFromInteger(permisos.getListarEmpleados())) {
                // Submenu item with a sub-submen         
                listados.addItem("Empleados", new ThemeResource("icons/addUser-16.ico"), listarEmpleados);
            }
            if (Utils.booleanFromInteger(permisos.getListarProductos())) {
                // Submenu item with a sub-submen         
                listados.addItem("Productos", new ThemeResource("icons/addCaja-16.ico"), listarProductos);
            }

            if (Utils.booleanFromInteger(permisos.getListarProveedores())) {
                listados.addItem("Proveedores", new ThemeResource("icons/addUser-16.ico"), listarProveedores);
            }

        }
        if (Utils.booleanFromInteger(permisos.getMenuEntornos())) {
            MenuItem entornos = barmenu.addItem("Trazabilidades", null, null);
            if (Utils.booleanFromInteger(permisos.getImportar())) {
                entornos.addItem("Importar ficheros", new ThemeResource("icons/addUser-16.ico"), importarFicheros);
            }
            if (Utils.booleanFromInteger(permisos.getComprasVentas())) {
                entornos.addItem("Trazabilidades", new ThemeResource("icons/addUser-16.ico"), comprasVentas);
            }
            if (Utils.booleanFromInteger(permisos.getErrores())) {
                entornos.addItem("Revisión trazabilidades", new ThemeResource("icons/addUser-16.ico"), listadoErrores);
            }
            if (Utils.booleanFromInteger(permisos.getTrazabilidades())) {
                entornos.addItem("Libro de trazabilidades", new ThemeResource("icons/addUser-16.ico"), trazabilidades);
            }
            if (Utils.booleanFromInteger(permisos.getBalanceMasas())) {
                entornos.addItem("Balance de masas", new ThemeResource("icons/addUser-16.ico"), balanceMasas);
            }
        }

        if (Utils.booleanFromInteger(permisos.getCopiaSeguridad())) {
            MenuItem entornos = barmenu.addItem("Administración", null, null);
            if (Utils.booleanFromInteger(permisos.getCopiaSeguridad())) {
                entornos.addItem("Realizar copia de seguridad", new ThemeResource("icons/database-16.ico"), copiaSeguridad);
            }
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

        importarFicheros = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaImportacion.NAME + "/" + idEmpleado);
            }

        };

        listarProveedores = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaListadoProveedores.NAME + "/" + idEmpleado);
            }

        };

        listarEmpleados = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaListadoEmpleados.NAME + "/" + idEmpleado);
            }

        };

        listarProductos = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaListadoProductos.NAME + "/" + idEmpleado);
            }

        };

        balanceMasas = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaBalanceMasas.NAME + "/" + idEmpleado);
            }

        };

        comprasVentas = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaTrazabilidades.NAME + "/" + idEmpleado);
            }

        };

        listarPT = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaListadoControlPt.NAME + "/" + idEmpleado);
            }

        };

        listadoErrores = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaErrores.NAME + "/" + idEmpleado);
            }

        };

        trazabilidades = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                getUI().getNavigator().navigateTo(VistaLibroTrazabilidad.NAME + "/" + idEmpleado);
            }

        };

        copiaSeguridad = new MenuBar.Command() {

            public void menuSelected(MenuItem selectedItem) {

                try {

                    Thread.sleep(7 * 1000);

                    new ProcessBuilder("C:\\Trazabilidades\\MySQLBackups\\mysqlbackup.bat").start();

                    Notification aviso = new Notification("Copia realizada correctamente", Notification.Type.HUMANIZED_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                } catch (Exception e) {

                }
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
                Resource resource1 = new ThemeResource("logo/logo_genasoft.png");
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
