/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.EmpleadoEnum;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

/**
 * @author Daniel Carmona Romero
 * Vista inicial de la aplicación
 */
@Theme("Genal")
@UIScope
@SpringView(name = VistaInicioSesion.NAME)
public class VistaInicioSesion extends CustomComponent implements View ,Button.ClickListener {

    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;

    /** En Spring es necesario crear serial version UID. */
    private static final long             serialVersionUID = -8825520563162735069L;
    /** El nombre de la vista.*/
    public static final String            NAME             = "login";
    /** Caja de texto para el nombre de usuario para el login*/
    private TextField                     user;
    /** La caja de texto para la clave de acceso del usuario para el login*/
    private PasswordField                 password;
    /** El boton para hacer login.*/
    private Button                        loginButton;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(VistaInicioSesion.class);
    /** La fecha en que hace login.*/
    private Timestamp                     fecha            = null;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** El nombre de usuario. */
    private String                        username;
    /** El empleado que está haciendo inicio de sesión.*/
    private TEmpleados                    empleado         = null;

    @SuppressWarnings("serial")
    @PostConstruct
    void init() {
        setSizeFull();

        // Obtenemos la imagen desde el tema aplicado a la aplicación
        Resource res = new ThemeResource("logo/appLogo2.png");

        // Cargamos la imagen desde el objeto Image
        Image image = new Image(null, res);

        // Obtenemos la imagen desde el tema aplicado a la aplicación
        // Resource res2 = new ThemeResource("logo/appLogo2.png");

        // Image link
        Link iconic = new Link(null, new ExternalResource(" "));
        iconic.setIcon(new ThemeResource("logo/appLogo3.png"));

        // Cargamos la imagen desde el objeto Image
        //Image image2 = new Image(null, res2);

        // La caja de texto para el nombre de uauario
        user = new TextField("Usuario:");
        user.setWidth("300px");
        // Establecemos que debe informarse en la vista.
        user.setRequired(true);
        // El texto que se ve si no se introduce nada
        user.setInputPrompt("Nombre de usuario");

        user.setInvalidAllowed(false);

        // La caja de texto para la clave de acceso
        password = new PasswordField("Contraseña:");
        password.setInputPrompt("Contraseña");
        password.setWidth("300px");
        // Establecemos que debe informarse en la vista.
        password.setRequired(true);
        password.setValue("");
        password.setNullRepresentation("");
        password.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                buttonClick(new ClickEvent(password));
            }
        });
        // Create login button
        loginButton = new Button("Acceder", this);
        loginButton.addStyleName("big");
        Label texto = new Label("Iniciar sesión");
        texto.setStyleName("tituloTamano18");

        HorizontalLayout imgGenalBiomasa = new HorizontalLayout();
        imgGenalBiomasa.addComponent(iconic);
        imgGenalBiomasa.setComponentAlignment(iconic, Alignment.TOP_RIGHT);

        // Incluimos en el layout los componentes
        VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);
        fields.addComponent(image);
        fields.setComponentAlignment(image, Alignment.TOP_CENTER);
        fields.addComponent(texto);
        fields.addComponent(user);
        fields.addComponent(password);
        fields.addComponent(loginButton);
        fields.setComponentAlignment(loginButton, Alignment.BOTTOM_CENTER);
        fields.setSizeFull();
        fields.setSizeUndefined();

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(imgGenalBiomasa);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(imgGenalBiomasa, Alignment.TOP_RIGHT);
        viewLayout.addComponent(fields);
        viewLayout.setComponentAlignment(fields, Alignment.TOP_CENTER);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeEvent event) {

        if (event.getParameters() != null) {
            if (event.getParameters().equals("cierre")) {
                Integer empleado = (Integer) getUI().getSession().getAttribute("user");
                contrVista.eliminaAcceso(empleado);
                // Eliminamos de la sesion el empleado
                getSession().setAttribute("user", null);
            }
        }
        // Establecemos el foco en la caja de text del nombre de usuario en la vista
        if (user != null) {
            user.focus();
        }
    }

    @Override
    public void buttonClick(ClickEvent event) {

        //
        // Validate the fields using the navigator. By using validors for the
        // fields we reduce the amount of queries we have to use to the database
        // for wrongly entered passwords
        //
        if (!user.isValid() || !password.isValid()) {
            return;
        }
        username = user.getValue();

        if (username != null) {
            username = username.toLowerCase();
        }

        String password = this.password.getValue();

        empleado = null;

        boolean isValid = false;
        try {
            // Comprobamos si en la sesion tenemos la fecha de inicio de sesion
            if (getSession().getAttribute("fecha") != null) {
                long time = Long.parseLong((String) getSession().getAttribute("fecha"));
                String f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(time);
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                fecha = new Timestamp(format.parse(f).getTime());
            } else {
                // Sino hay fecha de inicio de sesión, generamos la fecha nueva.
                fecha = new Timestamp(Utils.generarFecha().getTime());
                String f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fecha);
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                fecha = new Timestamp(format.parse(f).getTime());
            }
            try {
                // Se comprueba que el usuario y password sean correctos.
                empleado = contrVista.iniciarSesion(username, password, fecha);

                // Si existe el empleado, comprobamos si el usuario y contraseña son correctos y esta activo.
                if (empleado != null) {
                    if (!empleado.getNombre().equals(Constants.EMPLEADO_EN_USO)) {
                        isValid = !empleado.getPassword().equals(String.valueOf(EmpleadoEnum.PASS_INCORRECT)) && empleado.getEstado() != EmpleadoEnum.DESACTIVADO.getValue();

                        if (!isValid && empleado.getPassword().equals(String.valueOf(EmpleadoEnum.PASS_INCORRECT))) {

                            Notification aviso = new Notification("Contraseña incorrecta", Notification.Type.WARNING_MESSAGE);
                            aviso.setPosition(Position.MIDDLE_CENTER);
                            aviso.show(Page.getCurrent());
                        } else if (!isValid) {
                            Notification aviso = new Notification("Nombre de usuario no encontrado", Notification.Type.WARNING_MESSAGE);
                            aviso.setPosition(Position.MIDDLE_CENTER);
                            aviso.show(Page.getCurrent());
                        }
                    } else {
                        MessageBox.createQuestion().withCaption(appName).withMessage("¿El empleado está en uso, quieres eliminar las sesiones activas? Se cerrarán todas las sesiones existentes").withNoButton().withYesButton(() ->

                        contrVista.eliminaAcceso(empleado.getId()), ButtonOption.caption("Sí")).open();
                    }

                } else {
                    Notification aviso = new Notification("Usuario y contraseña incorrectos", Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            } catch (GenasoftException tbe) {
                if (tbe.getMessage().equals(Constants.LICENCIA_NO_VALIDA)) {
                    Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(Constants.LICENCIA_NO_VALIDA), Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    return;
                } else if (tbe.getMessage().equals(Constants.EMPLEADO_EN_USO)) {
                    empleado = contrVista.obtenerEmpleadoPorNombreUsuario(username);
                    MessageBox.createQuestion().withCaption(appName).withMessage(contrVista.obtenerDescripcionCodigo(Constants.EMPLEADO_EN_USO)).withNoButton().withYesButton(() ->

                    contrVista.eliminaAcceso(empleado.getId()), ButtonOption.caption("Sí")).open();
                } else {
                    Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(tbe.getMessage()), Notification.Type.WARNING_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }
        } catch (MyBatisSystemException e) {
            Notification aviso = new Notification("No se ha podido establecer conexión con la base de datos.", Notification.Type.ERROR_MESSAGE);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
            log.error("Error al realizar login: ", e);
        } catch (Exception sqle) {
            Notification aviso = new Notification("Error de inconsistencia de datos, consulte con el administrador. Código error: DNE-004.", Notification.Type.ERROR_MESSAGE);
            aviso.setPosition(Position.MIDDLE_CENTER);
            aviso.show(Page.getCurrent());
            log.error("Error DNE-004: ", sqle);

        }

        if (isValid) {

            // Store the current user in the service session
            getSession().setAttribute("user", empleado.getId());
            getSession().setAttribute("fecha", String.valueOf(fecha.getTime()));

            try {
                // Si el usuario no es nulo es que está logueado, redirigimos a la vista del menu principal.
                if (empleado != null) {
                    TPermisos perm = contrVista.obtenerPermisosEmpleado(empleado, empleado.getId(), fecha.getTime());
                    if (perm != null) {
                        switch (empleado.getIdRol()) {
                            // Máster.
                            case 1:
                                // Refresh this view, should redirect to login view
                                getUI().getNavigator().navigateTo(VistaMenuPrincipalAdministrador.NAME + "/" + user);
                                break;
                            case 2:
                                // Refresh this view, should redirect to login view
                                getUI().getNavigator().navigateTo(VistaMenuPrincipalAdministrador.NAME + "/" + user);
                                break;
                            case 3:
                                // Refresh this view, should redirect to login view
                                getUI().getNavigator().navigateTo(VistaMenuPrincipalAdministrador.NAME + "/" + user);
                                break;
                            default:
                                Notification aviso = new Notification("No se ha especificado la pantalla inicial para el rol actual, contacta con el administrador.", Notification.Type.ERROR_MESSAGE);
                                aviso.setPosition(Position.MIDDLE_CENTER);
                                aviso.show(Page.getCurrent());
                                getSession().setAttribute("user", null);
                                getSession().setAttribute("fecha", null);
                                // Redirigimos a la página de inicio.
                                getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                        }
                    } else {
                        Notification aviso = new Notification("No se ha podido identificar los permisos asignados, contacta con el administrador.", Notification.Type.ERROR_MESSAGE);
                        aviso.setPosition(Position.MIDDLE_CENTER);
                        aviso.show(Page.getCurrent());
                        getSession().setAttribute("user", null);
                        getSession().setAttribute("fecha", null);
                        // Redirigimos a la página de inicio.
                        getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                    }

                } else {
                    // Sino está logueado, vamos a la pantalla de inicio de sesión.
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
                }
            } catch (MyBatisSystemException e) {
                Notification aviso = new Notification("No se ha podido establecer conexión con la base de datos.", Notification.Type.ERROR_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                log.error("Error al realizar login: ", e);
            } catch (Exception sqle) {
                Notification aviso = new Notification(contrVista.obtenerDescripcionCodigo(Constants.ERROR_GENERAL), Notification.Type.ERROR_MESSAGE);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                log.error(Constants.ERROR_GENERAL, sqle);

            }
        } else {
            // Wrong password clear the password field and refocuses it
            this.password.setValue(null);
            this.password.focus();

        }
    }
}