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
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TOperadoresVista;
import com.dina.genasoft.db.entity.TRegistrosCambiosOperadores;
import com.dina.genasoft.db.mapper.TOperadoresMapper;
import com.dina.genasoft.db.mapper.TRegistrosCambiosOperadoresMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de operadores y la BD.
 */
@Component
@Slf4j
@Data
public class OperadoresSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger     log              = org.slf4j.LoggerFactory.getLogger(OperadoresSetup.class);
    /** Inyección por Spring del mapper TOperadoresMapper.*/
    @Autowired
    private TOperadoresMapper                 tOperadoresMapper;
    /** Inyección por Spring del mapper TRegistrosCambiosClientesMapper. */
    @Autowired
    private TRegistrosCambiosOperadoresMapper tRegistrosCambiosOperadoresMapper;
    /** Inyección de Spring para poder acceder a la capa de datos de empleados. */
    @Autowired
    private EmpleadosSetup                    empleadosSetup;
    /** Serial ID de la aplicación Spring. */
    private static final long                 serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna el operador a partir del ID.
     * @param id El ID del operador
     * @return El operador encontrado.
     */
    public TOperadores obtenerOperadorPorId(Integer id) {
        TOperadores operador = tOperadoresMapper.selectByPrimaryKey(id);
        // Retnornamos el operador encontrado.
        return operador;
    }

    /**
     * Método que nos retorna el operador a partir del nombre.
     * @param nombre El nombre del operador
     * @return El operador encontrado.
     */
    public TOperadores obtenerOperadorPorNombre(String nombre) {
        return tOperadoresMapper.obtenerOperadorPorNombre(nombre.trim().toUpperCase());
    }

    /**
     * Método que nos retorna el operador a partir del nombre.
     * @param nombre El nombre del operador
     * @return El operador encontrado.
     */
    public TOperadores obtenerOperadorPorCif(String cif) {
        return tOperadoresMapper.obtenerOperadorPorCif(cif.trim().toUpperCase());
    }

    /**
     * Método que nos retorna los operadores existentes en el sistema.
     * @return Los operadores encontrados
     */
    public List<TOperadores> obtenerTodosOperadores() {
        return tOperadoresMapper.obtenerTodosOperadores();
    }

    /**
     * Método que nos retorna los operadores activos en el sistema.
     * @return Los operadores activos encontrados
     */
    public List<TOperadores> obtenerOperadoresActivos() {
        return tOperadoresMapper.obtenerOperadoresActivos();
    }

    /**
     * Método que nos retorna los clientes activos en el sistema.
     * @return Los clientes activos encontrados
     */
    public List<TOperadoresVista> obtenerOperadoresActivosVista() {
        return convertirOperadoresVista(tOperadoresMapper.obtenerOperadoresActivos());
    }

    /**
     * Método que nos crea el operador y nos retorna el ID generado.
     * @param record El operador terminado a crear.
     * @return El ID generado.
     */
    public int crearOperadorRetornaId(TOperadores record) {

        Integer id = -1;

        try {
            // Rellenamos los datos necesarios para crear el cliente.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("nombre", record.getNombre());
            map.put("razonSocial", record.getRazonSocial());
            map.put("cif", record.getCif());
            map.put("direccion", record.getDireccion());
            map.put("codigoPostal", record.getCodigoPostal());
            map.put("ciudad", record.getCiudad());
            map.put("provincia", record.getProvincia());
            map.put("pais", record.getPais());
            map.put("usuCrea", record.getUsuCrea());
            map.put("fechaCrea", record.getFechaCrea());
            map.put("usuModifica", record.getUsuModifica());
            map.put("fechaModifica", record.getFechaModifica());
            map.put("estado", record.getEstado());

            tOperadoresMapper.insertRecord(map);

            id = (Integer) map.get("id");
        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_OPERADOR + ", Error al crear el operador: " + record.toString2() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return id;
    }

    /**
     * Método que se encarga de crear el operador en el sistema.
     * @param operador El operador a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearOperador(TOperadores operador) {
        String result = Constants.OPERACION_OK;
        operador.setNombre(operador.getNombre().trim().toUpperCase());

        String res = existeOperador(operador);

        if (res != null) {
            return res;
        } else {

            try {
                result = tOperadoresMapper.insert(operador) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_OPERADOR;
            } catch (Exception e) {
                result = Constants.BD_KO_CREA_OPERADOR;
                log.error(Constants.BD_KO_CREA_OPERADOR + ", Error al crear el operador: " + operador.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de modificar el operador en el sistema.
     * @param record El operador a modificar.     
     * @return El código del resultado de la operación.
     */
    public String modificarOperador(TOperadores record) {
        String result = Constants.OPERACION_OK;
        record.setNombre(record.getNombre().trim().toUpperCase());

        String res = existeOperador(record);

        if (res != null) {
            return res;
        } else {

            try {
                result = tOperadoresMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_OPERADOR;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_OPERADOR;
                log.error(Constants.BD_KO_MODIF_OPERADOR + ", Error al modificar el operador: " + record.toString2() + ", ", e);
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
    private String existeOperador(TOperadores op) {
        String result = null;
        TOperadores aux = obtenerOperadorPorNombre(op.getNombre());
        if (aux == null || (op.getId() != null && op.getId().equals(aux.getId()))) {
            aux = obtenerOperadorPorCif(op.getCif());
            if (aux != null && (op.getId() != null && op.getId().equals(aux.getId()))) {
                result = Constants.OPERADOR_EXISTE_CIF;
            }
        } else {
            result = Constants.OPERADOR_EXISTE_NOMBRE;
        }

        return result;
    }

    /**
     * Método que se encarga de crear un nuevo registro de cambio de operador en el sistema.
     * @param record El registro de cambio de operador a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioOperador(TRegistrosCambiosOperadores record) {

        String result = Constants.OPERACION_OK;

        try {
            result = tRegistrosCambiosOperadoresMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_OPERADOR;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_OPERADOR;
            log.error(Constants.BD_KO_CREA_OPERADOR + ", Error al crear el registro de modificación del operador: " + record.toString() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de realizar la conversión de  {@linkplain TOperadores} a  {@linkplain TOperadoresVista}
     * @param lOperadores Lista de operadores a convertir
     * @return Lista de operadores en formato  {@linkplain TOperadoresVista}
     */
    private List<TOperadoresVista> convertirOperadoresVista(List<TOperadores> lOperadores) {
        List<TOperadoresVista> lResult = Utils.generarListaGenerica();

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));

        TOperadoresVista aux = null;
        String campo = "";

        for (TOperadores op : lOperadores) {
            aux = new TOperadoresVista(op);

            campo = mEmpleados.get(op.getUsuCrea());
            aux.setUsuCrea(campo != null ? campo : "N/D; id: " + op.getUsuCrea());

            if (op.getUsuModifica() != null) {
                campo = mEmpleados.get(op.getUsuModifica());
                aux.setUsuModifica(campo != null ? campo : "N/D; id: " + op.getUsuModifica());
            }

            lResult.add(aux);
        }

        return lResult;
    }

}