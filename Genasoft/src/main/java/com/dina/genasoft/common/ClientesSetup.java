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
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TClientesVista;
import com.dina.genasoft.db.mapper.TClientesMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de clientes y la BD.
 */
@Component
@Slf4j
@Data
public class ClientesSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(ClientesSetup.class);
    /** Inyección por Spring del mapper TClientesMapper.*/
    @Autowired
    private TClientesMapper               tClientesMapper;
    /** Inyección de Spring para poder acceder a la capa de datos de empleados.*/
    @Autowired
    private EmpleadosSetup                empleadosSetup;
    /** Serial ID de la aplicación Spring. */
    private static final long             serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna el cliente a partir del ID.
     * @param id El ID del cliente
     * @return El cliente encontrado.
     */
    public TClientes obtenerClientePorId(Integer id) {
        TClientes cliente = tClientesMapper.selectByPrimaryKey(id);
        // Retnornamos el cliente encontrado.
        return cliente;
    }

    /**
     * Método que nos retorna el cliente a partir del nombre.
     * @param nombre El nombre del cliente
     * @return El cliente encontrado.
     */
    public TClientes obtenerClientePorNombre(String nombre) {
        return tClientesMapper.obtenerClientePorNombre(nombre.trim().toUpperCase());
    }

    /**
     * Método que nos retorna los clientes existentes en el sistema.
     * @return Los clientes encontrados
     */
    public List<TClientes> obtenerTodosClientes() {
        return tClientesMapper.obtenerTodosClientes();
    }

    /**
     * Método que nos retorna los clientes activos en el sistema.
     * @return Los clientes activos encontrados
     */
    public List<TClientes> obtenerClientesActivos() {
        return tClientesMapper.obtenerClientesActivos();
    }

    /**
     * Método que nos retorna los clientes activos en el sistema.
     * @return Los clientes activos encontrados
     */
    public List<TClientesVista> obtenerClientesActivosVista() {
        return convertirClientesVista(tClientesMapper.obtenerClientesActivos());
    }

    /**
     * Método que nos crea el control de producto terminado y nos retorna el ID generado.
     * @param record El control de producto terminado a crear.
     * @return El ID generado.
     */
    public int crearClienteRetornaId(TClientes record) {

        Integer id = -1;

        try {
            // Rellenamos los datos necesarios para crear la familia.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("descripcion", record.getDescripcion());
            map.put("estado", record.getEstado());
            map.put("usuCrea", record.getUsuCrea());
            map.put("fechaCrea", record.getFechaCrea());
            map.put("usuModifica", record.getUsuModifica());
            map.put("fechaModifica", record.getFechaModifica());
            tClientesMapper.insertRecord(map);

            id = (Integer) map.get("id");
        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_CLIENTE + ", Error al crear el cliente: " + record.toString2() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return id;
    }

    /**
     * Método que se encarga de crear el cliente en el sistema.
     * @param cliente El cliente a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearCliente(TClientes cliente) {
        String result = Constants.OPERACION_OK;
        cliente.setDescripcion(cliente.getDescripcion().trim().toUpperCase());

        String res = existeCliente(cliente);

        if (res != null) {
            return res;
        } else {

            try {
                result = tClientesMapper.insert(cliente) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_CLIENTE;
            } catch (Exception e) {
                result = Constants.BD_KO_CREA_CLIENTE;
                log.error(Constants.BD_KO_CREA_CLIENTE + ", Error al crear el cliente: " + cliente.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de modificar el cliente en el sistema.
     * @param record El cliente a modificar.     
     * @return El código del resultado de la operación.
     */
    public String modificarCliente(TClientes record) {
        String result = Constants.OPERACION_OK;
        record.setDescripcion(record.getDescripcion().trim().toUpperCase());

        String res = existeCliente(record);

        if (res != null) {
            return res;
        } else {

            try {
                result = tClientesMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_CLIENTE;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_CLIENTE;
                log.error(Constants.BD_KO_MODIF_CLIENTE + ", Error al modificar el cliente: " + record.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de realizar la conversión de  {@linkplain TClientes} a  {@linkplain TClientesVista}
     * @param lClientes Lista de clientes a convertir
     * @return Lista de clientes en formato  {@linkplain TClientesVista}
     */
    private List<TClientesVista> convertirClientesVista(List<TClientes> lClientes) {
        List<TClientesVista> lResult = Utils.generarListaGenerica();

        TClientesVista aux = null;

        for (TClientes cl : lClientes) {
            aux = new TClientesVista();
            aux.copiaDesdeCliente(cl);

            lResult.add(aux);
        }

        return lResult;
    }

    /**
     * Método que nos comprueba si existe el cliente en el sistema
     * @param cl El cliente a comprobar
     * @return El código del resultado de la operación.
     */
    private String existeCliente(TClientes cl) {
        String result = null;
        TClientes aux = obtenerClientePorNombre(cl.getDescripcion());
        if (aux == null || (cl.getId() != null && cl.getId().equals(aux.getId()))) {

        } else {
            result = Constants.CLIENTE_EXISTE_NOMBRE;
        }

        return result;
    }

}