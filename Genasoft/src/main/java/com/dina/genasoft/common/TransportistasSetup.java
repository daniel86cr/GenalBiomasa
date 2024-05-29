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
import com.dina.genasoft.db.entity.TRegistrosCambiosTransportistas;
import com.dina.genasoft.db.entity.TTransportistas;
import com.dina.genasoft.db.entity.TTransportistasVista;
import com.dina.genasoft.db.mapper.TRegistrosCambiosTransportistasMapper;
import com.dina.genasoft.db.mapper.TTransportistasMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de transportistas y la BD.
 */
@Component
@Slf4j
@Data
public class TransportistasSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger         log              = org.slf4j.LoggerFactory.getLogger(TransportistasSetup.class);
    /** Inyección por Spring del mapper TOperadoresMapper.*/
    @Autowired
    private TTransportistasMapper                 tTransportistasMapper;
    /** Inyección por Spring del mapper tRegistrosCambiosTransportistasMapper. */
    @Autowired
    private TRegistrosCambiosTransportistasMapper tRegistrosCambiosTransportistasMapper;
    /** Inyección de Spring para poder acceder a la capa de datos de empleados. */
    @Autowired
    private EmpleadosSetup                        empleadosSetup;
    /** Serial ID de la aplicación Spring. */
    private static final long                     serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna el transportista a partir del ID.
     * @param id El ID del transportista
     * @return El operador transportista.
     */
    public TTransportistas obtenerTransportistaPorId(Integer id) {
        TTransportistas transportista = tTransportistasMapper.selectByPrimaryKey(id);
        // Retnornamos el transportista encontrado.
        return transportista;
    }

    /**
     * Método que nos retorna el transportista a partir del nombre.
     * @param nombre El nombre del transportista
     * @return El transportista encontrado.
     */
    public TTransportistas obtenerTransportistaPorNombre(String nombre) {
        return tTransportistasMapper.obtenerTransportistaPorNombre(nombre.trim().toUpperCase());
    }

    /**
     * Método que nos retorna el transportista a partir del nombre.
     * @param nombre El nombre del transportista
     * @return El transportista encontrado.
     */
    public TTransportistas obtenerTransportistaPorCif(String cif) {
        return tTransportistasMapper.obtenerTransportistaPorCif(cif.trim().toUpperCase());
    }

    /**
     * Método que nos retorna los transportistas existentes en el sistema.
     * @return Los transportistas encontrados
     */
    public List<TTransportistas> obtenerTodosTransportistas() {
        return tTransportistasMapper.obtenerTodosTransportistas();
    }

    /**
     * Método que nos retorna los transportistas existentes en el sistema.
     * @return Los transportistas encontrados
     */
    public List<TTransportistasVista> obtenerTodosTransportistasVista() {
        return convertirTransportistasVista(tTransportistasMapper.obtenerTodosTransportistas());
    }

    /**
     * Método que nos retorna los transportistas activos en el sistema.
     * @return Los transportistas activos encontrados
     */
    public List<TTransportistas> obtenerTransportistasActivos() {
        return tTransportistasMapper.obtenerTransportistasActivos();
    }

    /**
     * Método que nos retorna los transportistas activos en el sistema.
     * @return Los transportistas activos encontrados
     */
    public List<TTransportistasVista> obtenerOperadoresActivosVista() {
        return convertirTransportistasVista(tTransportistasMapper.obtenerTransportistasActivos());
    }

    /**
     * Método que nos crea el operador y nos retorna el ID generado.
     * @param record El operador terminado a crear.
     * @return El ID generado.
     */
    public int crearTransportistaRetornaId(TTransportistas record) {

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

            tTransportistasMapper.insertRecord(map);

            id = (Integer) map.get("id");
        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_TRANSPORTISTA + ", Error al crear el transportista: " + record.toString2() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return id;
    }

    /**
     * Método que se encarga de crear el transportista en el sistema.
     * @param operador El transportista a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearTransportista(TTransportistas operador) {
        String result = Constants.OPERACION_OK;
        operador.setNombre(operador.getNombre().trim().toUpperCase());

        String res = existeTransportista(operador);

        if (res != null) {
            return res;
        } else {

            try {
                result = tTransportistasMapper.insert(operador) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_TRANSPORTISTA;
            } catch (Exception e) {
                result = Constants.BD_KO_CREA_TRANSPORTISTA;
                log.error(Constants.BD_KO_CREA_TRANSPORTISTA + ", Error al crear el transportista: " + operador.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de modificar el transportista en el sistema.
     * @param record El transportista a modificar.     
     * @return El código del resultado de la operación.
     */
    public String modificarTransportista(TTransportistas record) {
        String result = Constants.OPERACION_OK;
        record.setNombre(record.getNombre().trim().toUpperCase());

        String res = existeTransportista(record);

        if (res != null) {
            return res;
        } else {

            try {
                result = tTransportistasMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_TRANSPORTISTA;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_TRANSPORTISTA;
                log.error(Constants.BD_KO_MODIF_TRANSPORTISTA + ", Error al modificar el transportista: " + record.toString2() + ", ", e);
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
    private String existeTransportista(TTransportistas op) {
        String result = null;
        TTransportistas aux = obtenerTransportistaPorNombre(op.getNombre());
        if (aux == null || (op.getId() != null && op.getId().equals(aux.getId()))) {
            aux = obtenerTransportistaPorCif(op.getCif());
            if (aux != null && (op.getId() != null && op.getId().equals(aux.getId()))) {
                result = Constants.TRANSPORTISTA_EXISTE_CIF;
            }
        } else {
            result = Constants.TRANSPORTISTA_EXISTE_NOMBRE;
        }

        return result;
    }

    /**
     * Método que se encarga de crear un nuevo registro de cambio de operador en el sistema.
     * @param record El registro de cambio de operador a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioTransportista(TRegistrosCambiosTransportistas record) {

        String result = Constants.OPERACION_OK;

        try {
            result = tRegistrosCambiosTransportistasMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_TRANSPORTISTA;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_TRANSPORTISTA;
            log.error(Constants.BD_KO_CREA_TRANSPORTISTA + ", Error al crear el registro de modificación del transportista: " + record.toString() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de realizar la conversión de  {@linkplain TTransportistas} a  {@linkplain TTransportistasVista}
     * @param lTransportistas Lista de transportistas a convertir
     * @return Lista de transportistas en formato  {@linkplain TTransportistasVista}
     */
    private List<TTransportistasVista> convertirTransportistasVista(List<TTransportistas> lTransportistas) {
        List<TTransportistasVista> lResult = Utils.generarListaGenerica();

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));

        TTransportistasVista aux = null;
        String campo = "";

        for (TTransportistas op : lTransportistas) {
            aux = new TTransportistasVista(op);

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