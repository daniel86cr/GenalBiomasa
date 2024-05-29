/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.db.entity.TEmpresas;
import com.dina.genasoft.db.entity.TEmpresasVista;
import com.dina.genasoft.db.entity.TRegistrosCambiosEmpresas;
import com.dina.genasoft.db.mapper.TEmpresasMapper;
import com.dina.genasoft.db.mapper.TRegistrosCambiosEmpresasMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de empresas y la BD.
 */
@Component
@Slf4j
@Data
public class EmpresasSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger   log              = org.slf4j.LoggerFactory.getLogger(EmpresasSetup.class);
    /** Inyección por Spring del mapper TEmpresasMapper.*/
    @Autowired
    private TEmpresasMapper                 tEmpresasMapper;
    /** Inyección por Spring del mapper TRegistrosCambiosEmpresasMapper. */
    @Autowired
    private TRegistrosCambiosEmpresasMapper tRegistrosCambiosEmpresasMapper;
    /** Serial ID de la aplicación Spring. */
    private static final long               serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna la empresa a partir del ID.
     * @param id El ID de la empresa
     * @return La empresa encontrada.
     */
    public TEmpresas obtenerEmpresaPorId(Integer id) {
        TEmpresas empresa = tEmpresasMapper.selectByPrimaryKey(id);
        // Retnornamos la empresa encontrada.
        return empresa;
    }

    /**
     * Método que nos retorna la empresa a partir del nombre.
     * @param nombre El nombre de la empresa
     * @return La empresa encontrada.
     */
    public TEmpresas obtenerEmpresaPorNombre(String nombre) {
        return tEmpresasMapper.obtenerEmpresaPorNombre(nombre.trim().toUpperCase());
    }

    /**
     * Método que nos retorna las empresas existentes en el sistema.
     * @return La empresas encontradas
     */
    public List<TEmpresas> obtenerTodasEmpresas() {
        return tEmpresasMapper.obtenerTodasEmpresas();
    }

    /**
     * Método que nos retorna las empresas activas en el sistema.
     * @return Las empresas activas encontradas
     */
    public List<TEmpresas> obtenerEmpresasActivas() {
        return tEmpresasMapper.obtenerEmpresasActivas();
    }

    /**
     * Método que nos retorna las empresas activas en el sistema.
     * @return Las empresas activas encontradas
     */
    public List<TEmpresasVista> obtenerEmpresasActivasVista() {
        return convertirEmpresasVista(tEmpresasMapper.obtenerEmpresasActivas());
    }

    /**
     * Método que nos crea la empresa y nos retorna el ID generado.
     * @param record La empresa a crear.
     * @return El ID generado.
     */
    public int crearEmpresaRetornaId(TEmpresas record) {

        Integer id = -1;

        try {
            // Rellenamos los datos necesarios para crear el cliente.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("nombre", record.getNombre());
            map.put("estado", record.getEstado());

            tEmpresasMapper.insertRecord(map);

            id = (Integer) map.get("id");
        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_EMPRESA + ", Error al crear la empresa: " + record.toString2() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return id;
    }

    /**
     * Método que se encarga de crear la empresa en el sistema.
     * @param operador La empresa a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearEmpresa(TEmpresas empresa) {
        String result = Constants.OPERACION_OK;
        empresa.setNombre(empresa.getNombre().trim().toUpperCase());

        String res = existeEmpresa(empresa);

        if (res != null) {
            return res;
        } else {

            try {
                result = tEmpresasMapper.insert(empresa) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_EMPRESA;
            } catch (Exception e) {
                result = Constants.BD_KO_CREA_EMPRESA;
                log.error(Constants.BD_KO_CREA_EMPRESA + ", Error al crear la empresa: " + empresa.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de modificar la empresa en el sistema.
     * @param record La empresa a modificar.     
     * @return El código del resultado de la operación.
     */
    public String modificarEmpresa(TEmpresas record) {
        String result = Constants.OPERACION_OK;
        record.setNombre(record.getNombre().trim().toUpperCase());

        String res = existeEmpresa(record);

        if (res != null) {
            return res;
        } else {

            try {
                result = tEmpresasMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_EMPRESA;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_EMPRESA;
                log.error(Constants.BD_KO_MODIF_EMPRESA + ", Error al modificar la empresa: " + record.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que nos comprueba si existe el cliente en el sistema
     * @param op El cliente a comprobar
     * @return El código del resultado de la operación.
     */
    private String existeEmpresa(TEmpresas op) {
        String result = null;
        TEmpresas aux = obtenerEmpresaPorNombre(op.getNombre());
        if (aux == null || (op.getId() != null && op.getId().equals(aux.getId()))) {
        } else {
            result = Constants.EMPRESA_EXISTE_NOMBRE;
        }

        return result;
    }

    /**
     * Método que se encarga de crear un nuevo registro de cambio de operador en el sistema.
     * @param record El registro de cambio de operador a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioEmpresa(TRegistrosCambiosEmpresas record) {

        String result = Constants.OPERACION_OK;

        try {
            result = tRegistrosCambiosEmpresasMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_EMPRESA;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_EMPRESA;
            log.error(Constants.BD_KO_CREA_EMPRESA + ", Error al crear el registro de modificación de la empresa: " + record.toString() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de realizar la conversión de  {@linkplain TEmpresas} a  {@linkplain TEmpresasVista}
     * @param lEmpresas Lista de empresas a convertir
     * @return Lista de empresas en formato  {@linkplain TEmpresasVista}
     */
    private List<TEmpresasVista> convertirEmpresasVista(List<TEmpresas> lEmpresas) {
        List<TEmpresasVista> lResult = Utils.generarListaGenerica();

        TEmpresasVista aux = null;

        for (TEmpresas op : lEmpresas) {
            aux = new TEmpresasVista(op);

            lResult.add(aux);
        }

        return lResult;
    }

}