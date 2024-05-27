/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Table;

/**
 * @author Daniel Carmona Romero
 * Clase que implementa a una tabla genérica.
 */
@SuppressWarnings("serial")
public class TablaGenerica extends Table {

    public TablaGenerica(Object[] propiedades, String[] columnas, BeanContainer<?, ?> beanContainer) {
        super();

        if (propiedades.length != columnas.length) {
            throw new IllegalArgumentException("Error al mostrar los elementos en la tabla, contacte con el administrador");
        }
        // Definimos las propiedades que tendrá la tabla
        setSelectable(true);
        setImmediate(true);
        setSizeFull();
        setColumnCollapsingAllowed(true);
        setContainerDataSource(beanContainer);
        setVisibleColumns(propiedades);
        setMultiSelect(false);
        for (int a = 0; a < propiedades.length; a++) {
            setColumnHeader(propiedades[a], columnas[a]);
        }

    }
}
