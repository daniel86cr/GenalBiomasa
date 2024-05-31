/**
 * Aplicación Data Food.
 * http://www.brostel.es/
 * Copyright (C) 2019
 */
package com.dina.genasoft.common;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dina.genasoft.db.entity.TIva;
import com.dina.genasoft.db.entity.TIvaVista;
import com.dina.genasoft.db.mapper.TIvaMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de monedas y la BD.
 */
@Component
@Slf4j
@Data
public class MonedasSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(MonedasSetup.class);
    /** Inyección por Spring del mapper TIvaMapper.*/
    @Autowired
    private TIvaMapper                    tIvaMapper;

    /** Serial ID de la aplicación Spring. */
    private static final long             serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna el IVA a partir del ID.
     * @param id El Id por el que realizar la búsqueda
     * @return El IVA encontrado.
     */
    public TIva obtenerIvaPorId(Integer id) {
        return tIvaMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos busca el IVA a partir de la descripción.
     * @param descripcion La descripción por la cual realizar la búsqueda.
     * @return El IVA encontrado.
     */
    public TIva obtenerIvaPorDescripcion(String descripcion) {
        return tIvaMapper.obtenerIvaPorDescripcion(descripcion);
    }

    /**
     * Método que nos realiza la búsqueda de los diferentes IVA existentes en el sistema.    
     * @return Lista de IVA existentes en el sistema.
     */
    public List<TIva> obtenerTodosIva() {
        return tIvaMapper.obtenerTodosIva();
    }

    /**
     * Método que nos realiza la búsqueda de los diferentes IVA activos en el sistema.    
     * @return Lista de IVA activos en el sistema.
     */
    public List<TIva> obtenerIvaActivos() {
        return tIvaMapper.obtenerIvaActivos();
    }

    /**
     * Método que nos retorna los tipos de iva existentes en el sistema en formato String.
     * @return Los tipos de IVA existentes en formato String.
     */
    public List<TIvaVista> obtenerTiposIvaVista() {
        return convertirIva(obtenerTodosIva());
    }

    /**
     * Método que nos convierte la lista de IVA {@linkplain TIva} a IVA vista {@linkplain TIvaVista} 
     * @param lIva Lista de IVA a convertir
     * @return Lista de IVA en formato IVA vista {@linkplain TIvaVista} 
     */
    private List<TIvaVista> convertirIva(List<TIva> lIva) {
        List<TIvaVista> lResult = Utils.generarListaGenerica();
        TIvaVista aux = null;
        for (TIva iva : lIva) {
            aux = new TIvaVista(iva);
            lResult.add(aux);
        }
        // Retornamos la vista de tipos de IVA.
        return lResult;
    }

    public String obtenerValorIva(Integer idIVa) {
        return tIvaMapper.obtenerValorIva(idIVa);
    }

}
