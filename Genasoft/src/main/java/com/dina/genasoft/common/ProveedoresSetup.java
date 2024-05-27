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

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.db.entity.TProveedores;
import com.dina.genasoft.db.entity.TProveedoresVista;
import com.dina.genasoft.db.mapper.TProveedoresMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de proveedores y la BD.
 */
@Component
@Slf4j
@Data
public class ProveedoresSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(ProveedoresSetup.class);
    /** Inyección por Spring del mapper TClientesMapper.*/
    @Autowired
    private TProveedoresMapper            tProveedoresMapper;
    private static final long             serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna el proveedor a partir del ID.
     * @param id El ID del proveedor
     * @return El proveedor encontrado.
     */
    public TProveedores obtenerProveedorPorId(Integer id) {
        return tProveedoresMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el proveedor a partir del nombre.
     * @param nombre El nombre del proveedor.
     * @return El proveedor encontrado.
     */
    public TProveedores obtenerProveedorPorNombre(String nombre) {
        return tProveedoresMapper.obtenerProveedorPorNombre(nombre);
    }

    /**
     * Método que nos retorna los proveedores existentes en el sistema.
     * @return Los proveedores encontrados.
     */
    public List<TProveedores> obtenerTodosProveedores() {
        return tProveedoresMapper.obtenerTodosProveedores();
    }

    /**
     * Método que nos retorna los proveedores existentes en el sistema en formato vista {@linkplain TProveedoresVista}
     * @return Los proveedores existentes en el sistema.
     */
    public List<TProveedoresVista> obtenerProveedoresVista() {
        return convertirProveedoresVista(obtenerTodosProveedores());
    }

    /**
     * Método que se encarga de realizar la conversión de  {@linkplain TProveedores} a  {@linkplain TProveedoresVista}
     * @param lProveedores Lista de proveedores a convertir
     * @return Lista de proveedores en formato  {@linkplain TProveedoresVista}
     */
    private List<TProveedoresVista> convertirProveedoresVista(List<TProveedores> lProveedores) {
        List<TProveedoresVista> lResult = Utils.generarListaGenerica();

        TProveedoresVista aux = null;

        for (TProveedores cl : lProveedores) {
            aux = new TProveedoresVista();
            aux.copiaDesdeProveedor(cl);

            lResult.add(aux);
        }

        return lResult;
    }

    /**
     * Método que nos crea un proveedor en el sistema.
     * @param record El provedor a crear.
     * @return El resultado de la operación.
     */
    public String crearProveedor(TProveedores record) {
        String result = Constants.OPERACION_OK;
        record.setDescripcion(record.getDescripcion().trim().toUpperCase());

        // Comprobamos si existe el proveedor con el mismo nombre o con el código EDI.
        TProveedores aux = obtenerProveedorPorNombre(record.getDescripcion());
        if (aux != null) {
            return Constants.PROVEEDOR_EXISTE_NOMBRE;
        }
        try {
            result = tProveedoresMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREAR_PROVEEDOR;
        } catch (Exception e) {
            result = Constants.BD_KO_CREAR_PROVEEDOR;
            log.error(Constants.BD_KO_CREAR_PROVEEDOR + ", Error al crear el proveedor: " + record.toString() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que nos modifica un proveedor en el sistema.
     * @param record El provedor a modificar.
     * @return El resultado de la operación.
     */
    public String modificarProveedor(TProveedores record) {
        String result = Constants.OPERACION_OK;
        record.setDescripcion(record.getDescripcion().trim().toUpperCase());

        // Comprobamos si existe el proveedor con el mismo nombre o con el código EDI.
        TProveedores aux = obtenerProveedorPorNombre(record.getDescripcion());

        if (aux != null && !aux.getId().equals(record.getId())) {
            return Constants.PROVEEDOR_EXISTE_NOMBRE;
        }

        try {
            result = tProveedoresMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIFICAR_PROVEEDOR;
        } catch (Exception e) {
            result = Constants.BD_KO_MODIFICAR_PROVEEDOR;
            log.error(Constants.BD_KO_MODIFICAR_PROVEEDOR + ", Error al modificar el proveedor: " + record.toString() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return result;

    }

}
