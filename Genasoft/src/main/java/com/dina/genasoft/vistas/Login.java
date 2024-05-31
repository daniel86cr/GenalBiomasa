/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

/**
 * Clase que se encarga de cargar Vaadin para crear las vistas.
 * @author Daniel Carmona Romero.
 */
@SuppressWarnings("serial")
@Theme("Genal")
@SpringUI
@ViewScope
public class Login extends UI {

    //Aqui hacemos el new pero esto es una bean que solicitamos a Spring o un EJB que solicitamos 
    //al contenedor de EJBs  

    @Autowired
    private SpringViewProvider viewProvider;

    @RequestMapping("/login")
    @VaadinServletConfiguration(productionMode = false, ui = Login.class)
    public static class Servlet extends VaadinServlet {

        /**
         * En Spring es necesario crear serial version UID.
         */
        private static final long serialVersionUID = -1717172038889911332L;
    }

    @Override
    // Inicialiamos la vista de la aplicación.
    protected void init(VaadinRequest request) {
        // Cargamos la vista de login.
        Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);

    }

    //En este método se define una vista y los eventos que recibiremos de ella
    @SuppressWarnings("unused")
    private void vistaLogin() {

        //
        // Creamos la instancia del navegador Create a new instance of the navigator. 
        // El navegador se conectara automaticamente a esta vista.
        new Navigator(this, this);

        //
        // Inlcuimos la vista inicial a la que el usuario puede acceder.
        //
        getNavigator().addView(VistaInicioSesion.NAME, VistaInicioSesion.class);//

        //
        // Incluimos la vista principal de la aplicacion.
        //
        getNavigator().addView(VistaLogin.NAME, VistaLogin.class);

        //
        // Incluimos la vista del menu principal de la aplicacion.
        //
        getNavigator().addView(VistaMenuPrincipalAdministrador.NAME, VistaMenuPrincipalAdministrador.class);

    }

}
