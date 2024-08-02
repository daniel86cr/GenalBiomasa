/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.common;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dina.genasoft.db.entity.TRoles;
import com.dina.genasoft.db.mapper.TRolesMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de roles y la BD.
 */
@Component
@Slf4j
@Data
public class RolesSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(RolesSetup.class);
    /** Inyección por Spring del mapper TRolesMapper.*/
    @Autowired
    private TRolesMapper                  tRolesMapper;
    private static final long             serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna el rol a partir del ID.
     * @param id El ID del rol
     * @return El rol encontrado.
     */
    public TRoles obtenerRolPorId(Integer id) {
        return tRolesMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el rol a partir del nombre.
     * @param nombre El nombre del rol.
     * @return El rol encontrado.
     */
    public TRoles obtenerRolPorNombre(String nombre) {
        return tRolesMapper.obtenerRolPorNombre(nombre);
    }

    /**
     * Método que nos realiza la búsqueda de los roles activos existentes en el sistema excluyendo el rol Master.
     * @return Los roles encontrados.
     */
    public List<TRoles> obtenerRolesActivosSinMaster() {
        return tRolesMapper.obtenerRolesActivosSinMaster();
    }

    /**
     * Método que nos realiza la búsqueda de los roles activos existentes en el sistema excluyendo el rol Master.
     * @return Los roles encontrados.
     */
    public List<TRoles> obtenerTodosRoles() {
        return tRolesMapper.obtenerTodosRoles();
    }

}
