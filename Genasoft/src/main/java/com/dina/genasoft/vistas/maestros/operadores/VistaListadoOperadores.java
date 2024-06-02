/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.vistas.maestros.operadores;

import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import javax.annotation.PostConstruct;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TOperadoresVista;
import com.dina.genasoft.db.entity.TPermisos;
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
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

@SuppressWarnings("serial")
@Theme("Genal")
@UIScope
@SpringView(name = VistaListadoOperadores.NAME)
public class VistaListadoOperadores extends CustomComponent implements View ,Button.ClickListener {
    /** El nombre de la vista.*/
    public static final String                      NAME            = "vOperadores";
    /** Necesario para mostrar los operadores*/
    private BeanContainer<String, TOperadoresVista> bcOperadores;
    /** El controlador de las vistas. */
    @Autowired
    private ControladorVistas                       contrVista;
    /** El boton para crear un operador.*/
    private Button                                  crearButton;
    /** El boton para crear un operador.*/
    private Button                                  modificarButton;
    /** El boton para eliminar un operador.*/
    private Button                                  eliminarButton;
    // Elementos para realizar busquedas
    /** Para filtrar por el nombre del operador.*/
    private TextField                               fNombre;
    /** Para filtrar por el cif del operador.*/
    private TextField                               fCif;
    /** Para filtrar por la dirección del operador.*/
    private TextField                               fDireccion;
    /** Para filtrar por el código postal del operador.*/
    private TextField                               fCodigoPostal;
    /** Para filtrar por la ciudad del operador.*/
    private TextField                               fCiudad;
    /** Para filtrar por estado. */
    private ComboBox                                fEstados;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                                  appName;
    /** El ID del empleado seleccionado.*/
    private String                                  idSeleccionado;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger           log             = org.slf4j.LoggerFactory.getLogger(VistaListadoOperadores.class);
    /** El usuario que está logado. */
    private Integer                                 user            = null;
    /** La fecha en que se inició sesión. */
    private Long                                    time            = null;
    /** El filtro que se le aplica al container. */
    private FiltroContainer                         filter;
    /** No tengo ni puta idea para que sirve. */
    private final Label                             status          = new Label("");
    /** Tabla para mostrar los empleados del sistema. */
    private Table                                   tablaOperadores = null;
    /** Para indicar que se está filtrando por el campo NOMBRE. */
    private final String                            NOMBRE          = "nombre";
    /** Para indicar que se está filtrando por el campo CIF. */
    private final String                            CIF             = "cif";
    /** Para indicar que se está filtrando por el campo CP. */
    private final String                            CP              = "cp";
    /** Para indicar que se está filtrando por el campo DIRECCIÓN. */
    private final String                            DIRECCION       = "direccion";
    /** Para indicar que se está filtrando por el campo CIUDAD. */
    private final String                            CIUDAD          = "ciudad";
    private TEmpleados                              empleado;
    private TPermisos                               permisos;

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
        eventosBotones();
    }

    @Override
    public void enter(ViewChangeEvent event) {
        user = null;
        time = null;
        List<TOperadoresVista> lOperadores = Utils.generarListaGenerica();
        user = (Integer) getSession().getAttribute("user");
        if ((String) getSession().getAttribute("fecha") != null) {
            time = Long.parseLong((String) getSession().getAttribute("fecha"));
        }
        if (time != null) {
            try {
                idSeleccionado = null;

                bcOperadores = new BeanContainer<>(TOperadoresVista.class);
                bcOperadores.setBeanIdProperty("id");

                Label texto = new Label("Operadores");
                texto.setStyleName("tituloTamano18");
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

                if (!Utils.booleanFromInteger(permisos.getEntornoMaestros())) {
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
                HorizontalLayout imgGenaSoft = contrVista.logoGenaSoft();

                viewLayout.addComponent(imgGenaSoft);
                viewLayout.setComponentAlignment(imgGenaSoft, Alignment.TOP_RIGHT);
                viewLayout.addComponent(titulo);
                viewLayout.setComponentAlignment(titulo, Alignment.TOP_CENTER);

                crearTablaTransportistas(permisos);

                lOperadores = contrVista.obtenerTodosOperadoresVista(user, time);

                bcOperadores.removeAllItems();
                bcOperadores.addAll(lOperadores);

                Label tituloFiltrar = new Label("Filtrar");
                tituloFiltrar.setStyleName("tituloTamano12");

                // Creamos el componente filtro.
                VerticalLayout filtro = crearComponenteFiltro();

                // Creamos la botonera.
                HorizontalLayout botonera = crearBotonera(permisos);

                viewLayout.addComponent(filtro);
                viewLayout.addComponent(botonera);
                viewLayout.addComponent(tablaOperadores);
                // Añadimos el logo de GENAL BIOMASA
                viewLayout.addComponent(contrVista.logoCliente());
                setCompositionRoot(viewLayout);
                // Establecemos el porcentaje de ratio para los layouts
                viewLayout.setExpandRatio(titulo, 0.1f);
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
        crearButton = new Button("Crear operador", this);
        modificarButton = new Button("Editar operador", this);
        eliminarButton = new Button("Desactivar operador", this);
    }

    @Override
    public void buttonClick(ClickEvent event) {

    }

    /**
     * Este metodo nos crea el evento para cada botón.
     */
    private void eventosBotones() {

        // Evento para crear un nuevo empleado.
        crearButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                getUI().getNavigator().navigateTo(VistaNuevoOperador.NAME + "/" + user);
            }

        });

        // Evento para modificar un empleado.
        modificarButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                if (idSeleccionado != null && !idSeleccionado.contains(",")) {

                    getUI().getNavigator().navigateTo(VistaOperador.NAME + "/" + idSeleccionado);

                } else {
                    Notification aviso = new Notification("Se debe seleccionar un registro del listado", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }

        });

        // Evento para eliminar un empleado empleado.
        eliminarButton.addClickListener(new ClickListener() {

            public void buttonClick(ClickEvent event) {

                if (idSeleccionado != null) {

                    MessageBox.createQuestion().withCaption(appName).withMessage("¿Quieres desactivar el registro seleccionado?").withNoButton().withYesButton(() -> desactivarOperador(idSeleccionado), ButtonOption.caption("Sí")).open();

                } else {
                    Notification aviso = new Notification("Se debe seleccionar un registro del listado", Notification.Type.ASSISTIVE_NOTIFICATION);
                    aviso.setPosition(Position.MIDDLE_CENTER);
                    aviso.show(Page.getCurrent());
                }
            }

        });

    }

    /**
     * Método que nos realiza la desactivación del empleado.
     * @param idSeleccionado El Id del empleado a desactivar.
     */
    @SuppressWarnings("unchecked")
    private void desactivarOperador(String idSeleccionado) {

        try {
            if (idSeleccionado.contains(",")) {
                String[] ids = idSeleccionado.split(",");
                int i = 0;
                String result = "";
                while (i < ids.length) {
                    result = contrVista.desactivarOperador(Integer.valueOf(ids[i]), user, time);

                    result = contrVista.obtenerDescripcionCodigo(result);
                    Item articulo = tablaOperadores.getItem("" + ids[i]);
                    String est = "Desactivado";
                    if (articulo.getItemProperty("estado").getValue().equals("Activo")) {
                        est = "Desactivado";
                    }
                    articulo.getItemProperty("estado").setValue(est);
                    i++;
                }
                Notification aviso = new Notification(result, Notification.Type.ASSISTIVE_NOTIFICATION);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
            } else {
                String result = contrVista.desactivarOperador(Integer.valueOf(idSeleccionado), user, time);

                result = contrVista.obtenerDescripcionCodigo(result);
                Notification aviso = new Notification(result, Notification.Type.ASSISTIVE_NOTIFICATION);
                aviso.setPosition(Position.MIDDLE_CENTER);
                aviso.show(Page.getCurrent());
                Item articulo = tablaOperadores.getItem("" + idSeleccionado);
                String est = "Desactivado";
                if (articulo.getItemProperty("estado").getValue().equals("Activo")) {
                    est = "Desactivado";
                }
                articulo.getItemProperty("estado").setValue(est);
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
        }
    }

    /**
     * Método que se encaerga de crear y montar el grid.
     * @return El Grid con las columnas.
     */
    private void crearTablaTransportistas(TPermisos permisos) {
        tablaOperadores = new TablaGenerica(new Object[] { "nombre", "cif", "direccion", "codigoPostal", "provincia", "fechaCrea", "fechaModifica", "estado" }, new String[] { "Razón social", "CIF", "Dirección", "CP", "Provincia", "Fecha alta", "Fecha ult. Modificación", "Estado" }, bcOperadores);
        tablaOperadores.addStyleName("big striped");
        tablaOperadores.setPageLength(25);

        // Establecemos tamaño fijo en columnas específicas.

        tablaOperadores.setMultiSelect(true);
        tablaOperadores.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                @SuppressWarnings("unchecked")
                Set<String> values = (Set<String>) tablaOperadores.getValue();
                idSeleccionado = "";
                for (String v : values) {
                    if (v.isEmpty())
                        continue;
                    if (!idSeleccionado.isEmpty()) {
                        idSeleccionado = idSeleccionado + "," + v;
                    } else {
                        idSeleccionado = v;
                    }

                }
            }
        });

        tablaOperadores.addItemClickListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                idSeleccionado = (String) event.getItemId();
                if (event.isDoubleClick()) {
                    getUI().getNavigator().navigateTo(VistaOperador.NAME + "/" + idSeleccionado);
                }
            }
        });
    }

    /**
     * Clase para aplicar filtros al container.
     * @author Daniel Carmona Romero
     *
     */
    private class FiltroContainer implements Container.Filter {
        protected String propertyId;
        protected String regex;
        protected Label  status;

        public FiltroContainer(String propertyId, String regex, Label status) {
            this.propertyId = propertyId;
            this.regex = regex;
            this.status = status;
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
                boolean result = value.toLowerCase().matches(regex);
                status.setValue(""); // OK
                return result;
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
    private VerticalLayout crearComponenteFiltro() {

        // Campo para filtrar por el nombre
        fNombre = new TextField("");
        fNombre.setNullRepresentation("");
        fNombre.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcOperadores.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("nombre", filtro, status);
                    bcOperadores.addContainerFilter(filter);
                    aplicarFiltro2(NOMBRE);
                } else {
                    fNombre.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por el CIF
        fCif = new TextField("");
        fCif.setNullRepresentation("");
        fCif.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcOperadores.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("cif", filtro, status);
                    bcOperadores.addContainerFilter(filter);
                    aplicarFiltro2(CIF);
                } else {
                    fCif.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por la dirección
        fDireccion = new TextField("");
        fDireccion.setNullRepresentation("");
        fDireccion.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcOperadores.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("direccion", filtro, status);
                    bcOperadores.addContainerFilter(filter);
                    aplicarFiltro2(DIRECCION);
                } else {
                    fDireccion.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por el código postal
        fCodigoPostal = new TextField("");
        fCodigoPostal.setNullRepresentation("");
        fCodigoPostal.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcOperadores.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("cp", filtro, status);
                    bcOperadores.addContainerFilter(filter);
                    aplicarFiltro2(CP);
                } else {
                    fCodigoPostal.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por la ciudad
        fCiudad = new TextField("");
        fCiudad.setNullRepresentation("");
        fCiudad.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {

                // Remove old filter
                if (event.getText() != null && !event.getText().isEmpty()) {
                    // Set new filter for the "Name" column
                    bcOperadores.removeAllContainerFilters();
                    String[] params = event.getText().trim().split(" ");
                    String filtro = "";
                    for (int i = 0; i < params.length; i++) {
                        filtro = filtro + ".*(" + params[i] + ").*";
                    }
                    filtro = filtro.toLowerCase();
                    filter = new FiltroContainer("ciudad", filtro, status);
                    bcOperadores.addContainerFilter(filter);
                    aplicarFiltro2(CIUDAD);
                } else {
                    fCiudad.setValue(null);
                    aplicarFiltro();

                }

            }
        });

        // Campo para filtrar por estado.
        fEstados = new ComboBox();
        fEstados.addItem("Todos");
        fEstados.addItem(Constants.ACTIVO);
        fEstados.addItem(Constants.DESACTIVADO);
        fEstados.setValue("Todos");
        fEstados.setNullSelectionAllowed(false);
        fEstados.setFilteringMode(FilteringMode.CONTAINS);

        fEstados.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                aplicarFiltro();
            }
        });

        Label tituloFiltrar = new Label("Filtrar");
        tituloFiltrar.setStyleName("tituloTamano12");
        // Filtro cabecera
        VerticalLayout filtro = new VerticalLayout(tituloFiltrar);

        // Tabla de filtro
        Table table = new Table();
        table.addStyleName("big striped");
        // La cabecera de la tabla del filtro.
        table.addContainerProperty("Nombre", TextField.class, null);
        table.addContainerProperty("CIF", TextField.class, null);
        table.addContainerProperty("Dirección", TextField.class, null);
        table.addContainerProperty("Código postal", TextField.class, null);
        table.addContainerProperty("Provincia", TextField.class, null);
        table.addContainerProperty("Estado", ComboBox.class, null);
        table.setPageLength(table.size());

        // Los componentes que componen al filtro
        table.addItem(new Object[] { fNombre, fCif, fDireccion, fCodigoPostal, fCiudad, fEstados }, 1);

        filtro.addComponent(table);
        filtro.setSpacing(true);
        filtro.setMargin(true);
        return filtro;
    }

    /**
     * Método que nos aplica el filtro correspondiente.
     */
    private void aplicarFiltro() {
        boolean entra = false;
        bcOperadores.removeAllContainerFilters();
        if (fNombre.getValue() != null && !fNombre.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fNombre.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("nombre", filtro, status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }

        if (fDireccion.getValue() != null && !fDireccion.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fDireccion.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("referencia", filtro, status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }

        if (fCodigoPostal.getValue() != null && !fCodigoPostal.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fCodigoPostal.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("ler", filtro, status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }

        if (fCif.getValue() != null && !fCif.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fCif.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("cif", filtro, status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }

        if (fCiudad.getValue() != null && !fCiudad.getValue().isEmpty()) {
            // Set new filter for the "Name" column
            String[] params = fCiudad.getValue().split(" ");
            String filtro = "";
            for (int i = 0; i < params.length; i++) {
                filtro = filtro + ".*(" + params[i] + ").*";
            }
            filtro = filtro.toLowerCase();
            filter = new FiltroContainer("provincia", filtro, status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }

        if (!fEstados.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("estado", (String) ".*(" + fEstados.getValue().toString().toLowerCase() + ").*", status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }

        if (!entra) {
            bcOperadores.removeAllItems();
            try {
                bcOperadores.addAll(contrVista.obtenerTodosOperadoresVista(user, time));
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
     * Método que nos aplica el filtro correspondiente.
     */
    private void aplicarFiltro2(String filtro) {
        boolean entra = false;

        if (fNombre.getValue() != null && !fNombre.getValue().isEmpty() && !filtro.equals(NOMBRE)) {
            // Set new filter for the "Name" column
            String[] params = fNombre.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("nombre", filtr, status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }
        if (fNombre.getValue() != null && !fNombre.getValue().isEmpty()) {
            entra = true;
        }

        if (fDireccion.getValue() != null && !fDireccion.getValue().isEmpty() && !filtro.equals(DIRECCION)) {
            // Set new filter for the "Name" column
            String[] params = fDireccion.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("direccion", filtr, status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }
        if (fDireccion.getValue() != null && !fDireccion.getValue().isEmpty()) {
            entra = true;
        }

        if (fCodigoPostal.getValue() != null && !fCodigoPostal.getValue().isEmpty() && !filtro.equals(CP)) {
            // Set new filter for the "Name" column
            String[] params = fCodigoPostal.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("cp", filtr, status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }
        if (fCodigoPostal.getValue() != null && !fCodigoPostal.getValue().isEmpty()) {
            entra = true;
        }

        if (fCif.getValue() != null && !fCif.getValue().isEmpty() && !filtro.equals(CIF)) {
            // Set new filter for the "Name" column
            String[] params = fCif.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("cif", filtr, status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }
        if (fCodigoPostal.getValue() != null && !fCodigoPostal.getValue().isEmpty()) {
            entra = true;
        }

        if (fCiudad.getValue() != null && !fCiudad.getValue().isEmpty() && !filtro.equals(CIUDAD)) {
            // Set new filter for the "Name" column
            String[] params = fCif.getValue().split(" ");
            String filtr = "";
            for (int i = 0; i < params.length; i++) {
                filtr = filtr + ".*(" + params[i] + ").*";
            }
            filtr = filtr.toLowerCase();
            filter = new FiltroContainer("provincia", filtr, status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }
        if (fCiudad.getValue() != null && !fCiudad.getValue().isEmpty()) {
            entra = true;
        }

        if (!fEstados.getValue().equals("Todos")) {
            // Set new filter for the "Name" column
            filter = new FiltroContainer("estado", (String) ".*(" + fEstados.getValue().toString().toLowerCase() + ").*", status);
            bcOperadores.addContainerFilter(filter);
            entra = true;
        }

        if (!entra) {
            bcOperadores.removeAllItems();
            try {
                bcOperadores.addAll(contrVista.obtenerTodosOperadoresVista(user, time));
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
     * Método que nos crea el componente donde están los botones de la vista.
     * @param permisos Los permisos que tiene el empleado activo.
     * @return El componente con los botones a partir de los permisos que tiene el empleado.
     */
    private HorizontalLayout crearBotonera(TPermisos permisos) {
        //Botonera
        HorizontalLayout botonera = new HorizontalLayout();
        botonera.setSpacing(true);

        botonera.addComponent(crearButton);

        botonera.addComponent(modificarButton);

        botonera.addComponent(eliminarButton);

        botonera.setMargin(true);

        return botonera;
    }

}