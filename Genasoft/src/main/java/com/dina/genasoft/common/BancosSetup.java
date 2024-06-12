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
import com.dina.genasoft.db.entity.TBancos;
import com.dina.genasoft.db.entity.TBancosVista;
import com.dina.genasoft.db.entity.TRegistrosCambiosBancos;
import com.dina.genasoft.db.mapper.TBancosMapper;
import com.dina.genasoft.db.mapper.TRegistrosCambiosBancosMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de bancos y la BD.
 */
@Component
@Slf4j
@Data
public class BancosSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(BancosSetup.class);
    /** Inyección por Spring del mapper TEnvasesMapper.*/
    @Autowired
    private TBancosMapper                 tBancosMapper;
    /** Inyección por Spring del mapper TRegistrosCambiosBancoMapper.*/
    @Autowired
    private TRegistrosCambiosBancosMapper tRegistrosCambiosBancosMapper;
    /** Serial ID de la aplicación Spring. */
    private static final long             serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos realiza la búsqueda del banco a partir del ID.
     * @param id El ID del banco a buscar.
     * @return El banco encontrado.
     */
    public TBancos obtenerBancoPorId(Integer id) {
        return tBancosMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos realiza la búsqueda del banco a partir del nombre.
     * @param nombre El nombre por el cual buscar.
     * @return El banco encontrado.
     */
    public TBancos obtenerBancoPorNombre(String nombre) {
        return tBancosMapper.obtenerBancoPorNombre(nombre);
    }

    /**
     * Método que nos realiza la búsqueda de los bancos existentes en el sistema.
     * @return Los bancos encontrados.
     */
    public List<TBancos> obtenerTodosBancos() {
        return tBancosMapper.obtenerTodosBancos();
    }

    /**
     * Método que nos realiza la búsqueda de los bancos activos en el sistema.
     * @return Los bancos encontrados.
     */
    public List<TBancos> obtenerBancosActivos() {
        return tBancosMapper.obtenerBancosActivos();
    }

    /**
     * Método que nos retorna los bancos en formato vista.
     * @return Los bancos en formato vista.
     */
    public List<TBancosVista> obtenerBancosVista() {
        return convertirBancosVista(obtenerTodosBancos());
    }

    /**
     * Método que nos convierte la lista de envases {@link TBancos} a formato vista {@link TBancosVista}
     * @param lBancos los bancos a convertir 
     * @return Los bancos en formato vista.
     */
    private List<TBancosVista> convertirBancosVista(List<TBancos> lBancos) {
        List<TBancosVista> lResult = Utils.generarListaGenerica();

        TBancosVista aux = null;

        for (TBancos banco : lBancos) {
            aux = new TBancosVista(banco);

            lResult.add(aux);
        }

        return lResult;
    }

    /**
     * Método que nos crea un banco en el sistema.
     * @param record El banco a crear.
     * @return El resultado de la operación.
     */
    public String crearBanco(TBancos record) {
        String result = Constants.OPERACION_OK;
        record.setNombre(record.getNombre().trim().toUpperCase());

        TBancos aux = obtenerBancoPorNombre(record.getNombre());

        if (aux != null) {
            result = Constants.BANCO_EXISTE_NOMBRE;
        } else {
            try {
                result = tBancosMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREAR_BANCO;
            } catch (Exception e) {
                result = Constants.BD_KO_CREAR_BANCO;
                log.error("Error al crear el banco. ", e);
            }

        }
        // Retornamos el resultado de la operación.
        return result;

    }

    /**
     * Método que nos modifica un banco en el sistema.
     * @param record El banco a modificar.
     * @return El resultado de la operación.
     */
    public String modificarBanco(TBancos record) {
        String result = Constants.OPERACION_OK;
        record.setNombre(record.getNombre().trim().toUpperCase());

        TBancos aux = obtenerBancoPorNombre(record.getNombre());
        if (aux != null && aux.getId() != record.getId()) {
            result = Constants.BANCO_EXISTE_NOMBRE;
        } else {
            try {
                result = tBancosMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_BANCO;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_BANCO;
                log.error("Error al modificar el banco. ", e);
            }

        }
        // Retornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo registro de cambio de envase en el sistema.
     * @param record El registro de cambio de envase a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioBanco(TRegistrosCambiosBancos record) {

        String result = Constants.OPERACION_OK;

        try {
            result = tRegistrosCambiosBancosMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREAR_BANCO;
        } catch (Exception e) {
            result = Constants.BD_KO_CREAR_BANCO;
            log.error(Constants.BD_KO_CREAR_BANCO + ", Error al crear el registro de modificación del banco: " + record.toString() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo banco en el sistema y nos retorna el ID generado.
     * @param record El banco a crear.     
     * @return El Id del nuevo banco, si el resultado es -1 quiere decir que no se ha creado correctamente.
     */
    public Integer crearBancoRetornaId(TBancos record) {
        // El resultado de la operación.
        Integer result = -1;

        record.setNombre(record.getNombre().trim().toUpperCase());

        TBancos aux = obtenerBancoPorNombre(record.getNombre());

        if (aux != null) {
            result = aux.getId();
        } else {
            try {
                // Rellenamos los datos necesarios para crear la familia.
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", 0);
                map.put("nombre", record.getNombre());
                map.put("estado", record.getEstado());

                tBancosMapper.insertRecord(map);

                result = (Integer) map.get("id");
            } catch (Exception e) {
                result = -1;
                log.error(Constants.BD_KO_CREAR_BANCO + ", Error al crear el banco: " + record.toString() + ", ", e);
            }

        }
        // Retornamos el resultado de la operación.
        return result;
    }

}
