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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TMateriales;
import com.dina.genasoft.db.entity.TMaterialesVista;
import com.dina.genasoft.db.entity.TRegistrosCambiosMateriales;
import com.dina.genasoft.db.mapper.TMaterialesMapper;
import com.dina.genasoft.db.mapper.TRegistrosCambiosMaterialesMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de materiales y la BD.
 */
@Component
@Slf4j
@Data
public class MaterialesSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger     log              = org.slf4j.LoggerFactory.getLogger(MaterialesSetup.class);
    /** Inyección por Spring del mapper TMaterialesMapper.*/
    @Autowired
    private TMaterialesMapper                 tMaterialesMapper;
    /** Inyección por Spring del mapper TRegistrosCambiosMateriales. */
    @Autowired
    private TRegistrosCambiosMaterialesMapper tRegistrosCambiosMaterialesMapper;
    /** Inyección de Spring para poder acceder a la capa de datos de materiales. */
    @Autowired
    private EmpleadosSetup                    empleadosSetup;
    /** Serial ID de la aplicación Spring. */
    private static final long                 serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna el material a partir del ID.
     * @param id El ID del material
     * @return El material encontrado.
     */
    public TMateriales obtenerMaterialPorId(Integer id) {
        TMateriales material = tMaterialesMapper.selectByPrimaryKey(id);
        // Retnornamos el material encontrado.
        return material;
    }

    /**
     * Método que nos retorna el material a partir del nombre.
     * @param nombre El nombre del material
     * @return El material encontrado.
     */
    public TMateriales obtenerMaterialPorNombre(String nombre) {
        return tMaterialesMapper.obtenerMaterialPorNombre(nombre.trim().toUpperCase());
    }

    /**
     * Método que nos retorna los materiales existentes en el sistema.
     * @return Los materiales encontrados
     */
    public List<TMateriales> obtenerTodosMateriales() {
        return tMaterialesMapper.obtenerTodosMateriales();
    }

    /**
     * Método que nos retorna los materiales activos en el sistema.
     * @return Los materiales activos encontrados
     */
    public List<TMateriales> obtenerMaterialesActivos() {
        return tMaterialesMapper.obtenerMaterialesActivos();
    }

    /**
     * Método que nos retorna los materiales activos en el sistema.
     * @return Los materiales activos encontrados
     */
    public List<TMaterialesVista> obtenerMaterialesActivosVista() {
        return convertirMaterialesVista(tMaterialesMapper.obtenerMaterialesActivos());
    }

    /**
     * Método que nos crea el material y nos retorna el ID generado.
     * @param record El material a crear.
     * @return El ID generado.
     */
    public int crearMaterialRetornaId(TMateriales record) {

        Integer id = -1;

        try {
            // Rellenamos los datos necesarios para crear el cliente.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("referencia", record.getReferencia());
            map.put("descripcion", record.getDescripcion());
            map.put("ler", record.getLer());
            map.put("idFamilia", record.getIdFamilia());
            map.put("precio", record.getPrecio());
            map.put("iva", record.getIva());
            map.put("usuCrea", record.getUsuCrea());
            map.put("fechaCrea", record.getFechaCrea());
            map.put("usuModifica", record.getUsuModifica());
            map.put("fechaModifica", record.getFechaModifica());
            map.put("estado", record.getEstado());

            tMaterialesMapper.insertRecord(map);

            id = (Integer) map.get("id");
        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_MATERIAL + ", Error al crear el material: " + record.toString2() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return id;
    }

    /**
     * Método que se encarga de crear el material en el sistema.
     * @param material El material a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearMaterial(TMateriales material) {
        String result = Constants.OPERACION_OK;
        material.setDescripcion(material.getDescripcion().trim().toUpperCase());

        String res = existeMaterial(material);

        if (res != null) {
            return res;
        } else {

            try {
                result = tMaterialesMapper.insert(material) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_MATERIAL;
            } catch (Exception e) {
                result = Constants.BD_KO_CREA_MATERIAL;
                log.error(Constants.BD_KO_CREA_MATERIAL + ", Error al crear el material: " + material.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de modificar el material en el sistema.
     * @param record El material a modificar.     
     * @return El código del resultado de la operación.
     */
    public String modificarMaterial(TMateriales record) {
        String result = Constants.OPERACION_OK;
        record.setDescripcion(record.getDescripcion().trim().toUpperCase());

        String res = existeMaterial(record);

        if (res != null) {
            return res;
        } else {

            try {
                result = tMaterialesMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_MATERIAL;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_MATERIAL;
                log.error(Constants.BD_KO_MODIF_MATERIAL + ", Error al modificar el material: " + record.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que nos comprueba si existe el material en el sistema
     * @param cl El material a comprobar
     * @return El código del resultado de la operación.
     */
    private String existeMaterial(TMateriales cl) {
        String result = null;
        TMateriales aux = obtenerMaterialPorNombre(cl.getDescripcion());
        if (aux == null || (cl.getId() != null && cl.getId().equals(aux.getId()))) {
        } else {
            result = Constants.MATERIAL_EXISTE_NOMBRE;
        }

        return result;
    }

    /**
     * Método que se encarga de crear un nuevo registro de cambio de material en el sistema.
     * @param record El registro de cambio de material a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioMaterial(TRegistrosCambiosMateriales record) {

        String result = Constants.OPERACION_OK;

        try {
            result = tRegistrosCambiosMaterialesMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_MATERIAL;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_MATERIAL;
            log.error(Constants.BD_KO_CREA_MATERIAL + ", Error al crear el registro de modificación del material: " + record.toString() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de realizar la conversión de  {@linkplain TMateriales} a  {@linkplain TMaterialesVista}
     * @param lMateriales Lista de materiales a convertir
     * @return Lista de materiales en formato  {@linkplain TMaterialesVista}
     */
    private List<TMaterialesVista> convertirMaterialesVista(List<TMateriales> lMateriales) {
        List<TMaterialesVista> lResult = Utils.generarListaGenerica();

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));

        TMaterialesVista aux = null;
        String campo = "";

        for (TMateriales cl : lMateriales) {
            aux = new TMaterialesVista(cl);

            campo = mEmpleados.get(cl.getUsuCrea());
            aux.setUsuCrea(campo != null ? campo : "N/D; id: " + cl.getUsuCrea());

            if (cl.getUsuModifica() != null) {
                campo = mEmpleados.get(cl.getUsuModifica());
                aux.setUsuModifica(campo != null ? campo : "N/D; id: " + cl.getUsuModifica());
            }

            lResult.add(aux);
        }

        return lResult;
    }

}