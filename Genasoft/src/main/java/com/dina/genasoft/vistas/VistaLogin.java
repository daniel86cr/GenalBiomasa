/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;

import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.exception.GenasoftException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

@SpringView(name = VistaLogin.NAME)
public class VistaLogin extends CustomComponent implements View {

    /**
     * En Spring es necesario crear serial version UID.
     */
    private static final long             serialVersionUID = 2244539441403199584L;
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas             contrVista;
    /** El usuario que está logado. */
    private Integer                       user             = null;
    /** La fecha en que se inició sesión. */
    private Long                          time             = null;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(VistaLogin.class);
    public static final String            NAME             = "";

    Label                                 text             = new Label();

    Button                                logout           = new Button("Logout", new Button.ClickListener() {

                                                               /**
                                                                * En Spring es necesario crear serial version UID.
                                                                */
                                                               private static final long serialVersionUID = 6526480918806134844L;

                                                               @Override
                                                               public void buttonClick(ClickEvent event) {

                                                                   // "Logout" the user
                                                                   getSession().setAttribute("user", null);

                                                                   // Redirigimos a la vista que tiene asignada el rol
                                                                   getUI().getNavigator().navigateTo(NAME);
                                                               }
                                                           });

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
                TEmpleados empl = contrVista.obtenerEmpleadoPorId(user, user, time);

                // Si el usuario no es nulo es que está logueado, redirigimos a la vista del menu principal.
                if (empl != null) {

                    // Redirigimos a la vista que tiene asignada el rol
                    getUI().getNavigator().navigateTo(VistaMenuPrincipalAdministrador.NAME + "/" + user);
                } else {
                    Notification aviso = new Notification("No se ha podido identificar los permisos asignados, contacta con el administrador.", Notification.Type.ERROR_MESSAGE);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                    getSession().setAttribute("user", null);
                    getSession().setAttribute("fecha", null);
                    // Redirigimos a la página de inicio.
                    getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
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
            }
        } else

        {
            getSession().setAttribute("user", null);
            getSession().setAttribute("fecha", null);
            // Redirigimos a la página de inicio.
            getUI().getNavigator().navigateTo(VistaInicioSesion.NAME);
        }
    }
}
