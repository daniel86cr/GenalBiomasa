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
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TClientesMateriales;
import com.dina.genasoft.db.entity.TClientesMaterialesVista;
import com.dina.genasoft.db.entity.TClientesOperadores;
import com.dina.genasoft.db.entity.TClientesOperadoresVista;
import com.dina.genasoft.db.entity.TClientesTransportistas;
import com.dina.genasoft.db.entity.TClientesTransportistasVista;
import com.dina.genasoft.db.entity.TClientesVista;
import com.dina.genasoft.db.entity.TDireccionCliente;
import com.dina.genasoft.db.entity.TDireccionClienteVista;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TIva;
import com.dina.genasoft.db.entity.TMateriales;
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TRegistrosCambiosClientes;
import com.dina.genasoft.db.entity.TTransportistas;
import com.dina.genasoft.db.mapper.TClientesMapper;
import com.dina.genasoft.db.mapper.TClientesMaterialesMapper;
import com.dina.genasoft.db.mapper.TClientesOperadoresMapper;
import com.dina.genasoft.db.mapper.TClientesTransportistasMapper;
import com.dina.genasoft.db.mapper.TDireccionClienteMapper;
import com.dina.genasoft.db.mapper.TRegistrosCambiosClientesMapper;
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
    private static final org.slf4j.Logger   log              = org.slf4j.LoggerFactory.getLogger(ClientesSetup.class);
    /** Inyección por Spring del mapper TClientesMapper.*/
    @Autowired
    private TClientesMapper                 tClientesMapper;
    /** Inyección por Spring del mapper TClientesOperadores.*/
    @Autowired
    private TClientesOperadoresMapper       tClientesOperadoresMapper;
    /** Inyección por Spring del mapper TClientesTransportistasMapper.*/
    @Autowired
    private TClientesTransportistasMapper   tClientesTransportistasMapper;
    /** Inyección por Spring del mapper TClientesMaterialesMapper.*/
    @Autowired
    private TClientesMaterialesMapper       tClientesMaterialesMapper;
    /** Inyección por Spring del mapper TDireccionClienteMapper.*/
    @Autowired
    private TDireccionClienteMapper         tDireccionClienteMapper;
    /** Inyección por Spring del mapper TRegistrosCambiosClientesMapper. */
    @Autowired
    private TRegistrosCambiosClientesMapper tRegistrosCambiosClientesMapper;
    /** Inyección de Spring para poder acceder a la capa de datos de empleados. */
    @Autowired
    private EmpleadosSetup                  empleadosSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de operadores. */
    @Autowired
    private OperadoresSetup                 operadoresSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de materiales. */
    @Autowired
    private MaterialesSetup                 materialesSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de monedas. */
    @Autowired
    private MonedasSetup                    monedasSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de transportistas. */
    @Autowired
    private TransportistasSetup             transportistasSetup;
    /** Serial ID de la aplicación Spring. */
    private static final long               serialVersionUID = 5701299788812594642L;

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
     * Método que nos retorna la dirección de cliente a partir del ID.
     * @param id El ID de la dirección de cliente.
     * @return La dirección de cliente encontrada.
     */
    public TDireccionCliente obtenerDireccionClientePorId(Integer id) {
        return tDireccionClienteMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna las direcciones de cliente a partir del ID.
     * @param id El ID de la dirección de cliente.
     * @return Las direcciones de cliente encontradas.
     */
    public List<TDireccionCliente> obtenerDireccionesClientePorIdCliente(Integer idCliente) {
        return tDireccionClienteMapper.obtenerDireccionesClientePorIdCliente(idCliente);
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
     * Método que nos retorna el cliente a partir del nombre.
     * @param nombre El nombre del cliente
     * @return El cliente encontrado.
     */
    public TClientes obtenerClientePorCif(String cif) {
        return tClientesMapper.obtenerClientePorCif(cif.trim().toUpperCase());
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
            // Rellenamos los datos necesarios para crear el cliente.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("nombre", record.getNombre());
            map.put("razonSocial", record.getRazonSocial());
            map.put("cif", record.getCif());
            map.put("usuCrea", record.getUsuCrea());
            map.put("fechaCrea", record.getFechaCrea());
            map.put("usuModifica", record.getUsuModifica());
            map.put("fechaModifica", record.getFechaModifica());
            map.put("estado", record.getEstado());

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
        cliente.setNombre(cliente.getNombre().trim().toUpperCase());

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
     * Método que se encarga de crear el cliente en el sistema.
     * @param dirCliente El cliente a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearDireccionCliente(TDireccionCliente dirCliente) {
        String result = "";
        try {
            result = tDireccionClienteMapper.insert(dirCliente) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_DIR_CLIENTE;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_DIR_CLIENTE;
            log.error(Constants.BD_KO_CREA_DIR_CLIENTE + ", Error al crear la dirección del cliente: " + dirCliente.toString2() + ", ", e);
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
        record.setNombre(record.getNombre().trim().toUpperCase());

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
     * Método que nos comprueba si existe el cliente en el sistema
     * @param cl El cliente a comprobar
     * @return El código del resultado de la operación.
     */
    private String existeCliente(TClientes cl) {
        String result = null;
        TClientes aux = obtenerClientePorNombre(cl.getNombre());
        if (aux == null || (cl.getId() != null && cl.getId().equals(aux.getId()))) {
            aux = obtenerClientePorCif(cl.getCif());
            if (aux != null && (cl.getId() != null && cl.getId().equals(aux.getId()))) {
                result = Constants.CLIENTE_EXISTE_CIF;
            }
        } else {
            result = Constants.CLIENTE_EXISTE_NOMBRE;
        }

        return result;
    }

    /**
     * Método que se encarga de crear un nuevo registro de cambio de empleado en el sistema.
     * @param record El registro de cambio de empleado a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioCliente(TRegistrosCambiosClientes record) {

        String result = Constants.OPERACION_OK;

        try {
            result = tRegistrosCambiosClientesMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_CLIENTE;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_CLIENTE;
            log.error(Constants.BD_KO_CREA_CLIENTE + ", Error al crear el registro de modificación del cliente: " + record.toString() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que nos retorna las asociaciones de cliente - operadores asociados al cliente.
     * @param idCliente El Id del cliente para realizar la consulta.
     * @return Los registros encontrados.
     */
    public List<TClientesOperadores> obtenerOperadoresAsociadosCliente(Integer idCliente) {
        return tClientesOperadoresMapper.obtenerOperadoresAsociadosCliente(idCliente);
    }

    /**
     * Método que nos retorna la asociación cliente-operador
     * @param idCliente El ID del cliente 
     * @param idOperador El ID del operador
     * @return El resultado de la consulta.
     */
    public TClientesOperadores obtenerClienteOperador(Integer idCliente, Integer idOperador) {
        return tClientesOperadoresMapper.selectByPrimaryKey(idCliente, idOperador);
    }

    /**
     * Método que nos retorna las asociaciones de cliente - transportista asociados al cliente.
     * @param idCliente El Id del cliente para realizar la consulta.
     * @return Los registros encontrados.
     */
    public List<TClientesTransportistas> obtenerTransportistasAsociadosCliente(Integer idCliente) {
        return tClientesTransportistasMapper.obtenerTransportistasAsociadosCliente(idCliente);
    }

    /**
     * Método que nos retorna las asociaciones de cliente - transportista asociados al cliente.
     * @param idCliente El Id del cliente para realizar la consulta.
     * @return Los registros encontrados.
     */
    public List<TClientesTransportistasVista> obtenerTransportistasAsociadosClienteVista(Integer idCliente) {
        return convertirClientesTransportistasVista(obtenerTransportistasAsociadosCliente(idCliente));
    }

    /**
     * Método que nos retorna la asociación cliente-transportista
     * @param idCliente El ID del cliente 
     * @param idTransportista El ID del transportista
     * @return El resultado de la consulta.
     */
    public TClientesTransportistas obtenerClienteTransportista(Integer idCliente, Integer idTransportista) {
        return tClientesTransportistasMapper.selectByPrimaryKey(idCliente, idTransportista);
    }

    /**
     * Método que nos retorna las asociaciones de cliente - transportista asociados al cliente.
     * @param idCliente El Id del cliente para realizar la consulta.
     * @return Los registros encontrados.
     */
    public List<TClientesMateriales> obtenerMaterialesAsociadosCliente(Integer idCliente) {
        return tClientesMaterialesMapper.obtenerMaterialesAsociadosCliente(idCliente);
    }

    /**
     * Método que nos retorna las asociaciones de cliente - transportista asociados al cliente.
     * @param idCliente El Id del cliente para realizar la consulta.
     * @return Los registros encontrados.
     */
    public List<TClientesMaterialesVista> obtenerMaterialesAsociadosClienteVista(Integer idCliente) {
        return convertirClientesMaterialesVista(obtenerMaterialesAsociadosCliente(idCliente));
    }

    /**
     * Método que nos retorna la asociación cliente-transportista
     * @param idCliente El ID del cliente 
     * @param idTransportista El ID del transportista
     * @return El resultado de la consulta.
     */
    public TClientesMateriales obtenerClienteMaterial(Integer idCliente, Integer idMaterial) {
        return tClientesMaterialesMapper.selectByPrimaryKey(idCliente, idMaterial);
    }

    /**
     * Método que se encarga de realizar la conversión de  {@linkplain TClientes} a  {@linkplain TClientesVista}
     * @param lClientes Lista de clientes a convertir
     * @return Lista de clientes en formato  {@linkplain TClientesVista}
     */
    private List<TClientesVista> convertirClientesVista(List<TClientes> lClientes) {
        List<TClientesVista> lResult = Utils.generarListaGenerica();

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));

        TClientesVista aux = null;
        String campo = "";

        for (TClientes cl : lClientes) {
            aux = new TClientesVista(cl);

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

    /**
     * Método que se encarga de convertir los clientes-operador en objeto vista
     * @param lList Lista con los objetos a convertir.
     * @return Lista resultado
     */
    public List<TClientesOperadoresVista> convertirClientesOperadoresVista(List<TClientesOperadores> lList) {
        List<TClientesOperadoresVista> lResult = Utils.generarListaGenerica();

        TClientesOperadoresVista aux = null;

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();
        List<TClientes> lClient = obtenerTodosClientes();
        List<TOperadores> lOper = operadoresSetup.obtenerTodosOperadores();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));
        // Nutrimos el diccionario con los clientes
        Map<Integer, String> mClientes = lClient.stream().collect(Collectors.toMap(TClientes::getId, TClientes::getNombre));
        // Nutrimos el diccionario con los operadores
        Map<Integer, String> mOperadores = lOper.stream().collect(Collectors.toMap(TOperadores::getId, TOperadores::getNombre));

        String campo = "";

        for (TClientesOperadores cl : lList) {
            aux = new TClientesOperadoresVista(cl);

            // Empleados
            campo = mEmpleados.get(cl.getUsuCrea());
            aux.setUsuCrea(campo != null ? campo : "N/D; id: " + cl.getUsuCrea());

            if (cl.getUsuModifica() != null) {
                campo = mEmpleados.get(cl.getUsuModifica());
                aux.setUsuModifica(campo != null ? campo : "N/D; id: " + cl.getUsuModifica());
            }

            // Cliente
            campo = mClientes.get(cl.getIdCliente());
            aux.setNombreCliente(campo != null ? campo : "N/D; ID: " + cl.getIdCliente());

            // Operador
            campo = mOperadores.get(cl.getIdOperador());
            aux.setNombreOperador(campo != null ? campo : "N/D; ID: " + cl.getIdOperador());

            lResult.add(aux);

        }

        return lResult;
    }

    /**
     * Método que se encarga de convertir los clientes-operador en objeto vista
     * @param lList Lista con los objetos a convertir.
     * @return Lista resultado
     */
    public List<TClientesTransportistasVista> convertirClientesTransportistasVista(List<TClientesTransportistas> lList) {
        List<TClientesTransportistasVista> lResult = Utils.generarListaGenerica();

        TClientesTransportistasVista aux = null;

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();
        List<TClientes> lClient = obtenerTodosClientes();
        List<TTransportistas> lOper = transportistasSetup.obtenerTodosTransportistas();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));
        // Nutrimos el diccionario con los clientes
        Map<Integer, String> mClientes = lClient.stream().collect(Collectors.toMap(TClientes::getId, TClientes::getNombre));
        // Nutrimos el diccionario con los transportistas
        Map<Integer, String> mTransportistas = lOper.stream().collect(Collectors.toMap(TTransportistas::getId, TTransportistas::getNombre));

        String campo = "";

        for (TClientesTransportistas cl : lList) {
            aux = new TClientesTransportistasVista(cl);

            // Empleados
            campo = mEmpleados.get(cl.getUsuCrea());
            aux.setUsuCrea(campo != null ? campo : "N/D; id: " + cl.getUsuCrea());

            if (cl.getUsuModifica() != null) {
                campo = mEmpleados.get(cl.getUsuModifica());
                aux.setUsuModifica(campo != null ? campo : "N/D; id: " + cl.getUsuModifica());
            }

            // Cliente
            campo = mClientes.get(cl.getIdCliente());
            aux.setNombreCliente(campo != null ? campo : "N/D; ID: " + cl.getIdCliente());

            // Transportista
            campo = mTransportistas.get(cl.getIdTransportista());
            aux.setNombreTransportista(campo != null ? campo : "N/D; ID: " + cl.getIdTransportista());

            lResult.add(aux);

        }

        return lResult;
    }

    /**
     * Método que se encarga de convertir los clientes-operador en objeto vista
     * @param lList Lista con los objetos a convertir.
     * @return Lista resultado
     */
    public List<TClientesMaterialesVista> convertirClientesMaterialesVista(List<TClientesMateriales> lList) {
        List<TClientesMaterialesVista> lResult = Utils.generarListaGenerica();

        TClientesMaterialesVista aux = null;

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();
        List<TClientes> lClient = obtenerTodosClientes();
        List<TMateriales> lMats = materialesSetup.obtenerTodosMateriales();
        List<TIva> lIvas = monedasSetup.obtenerTodosIva();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));
        // Nutrimos el diccionario con los clientes
        Map<Integer, String> mClientes = lClient.stream().collect(Collectors.toMap(TClientes::getId, TClientes::getNombre));
        // Nutrimos el diccionario con los materiales
        Map<Integer, String> mMateriales = lMats.stream().collect(Collectors.toMap(TMateriales::getId, TMateriales::getDescripcion));
        // Nutrimos el diccionario con los diferentes tipos de IVA
        Map<Integer, String> mIvas = lIvas.stream().collect(Collectors.toMap(TIva::getId, TIva::getDescripcion));

        String campo = "";

        for (TClientesMateriales cl : lList) {
            aux = new TClientesMaterialesVista(cl);

            // Empleados
            campo = mEmpleados.get(cl.getUsuCrea());
            aux.setUsuCrea(campo != null ? campo : "N/D; id: " + cl.getUsuCrea());

            if (cl.getUsuModifica() != null) {
                campo = mEmpleados.get(cl.getUsuModifica());
                aux.setUsuModifica(campo != null ? campo : "N/D; id: " + cl.getUsuModifica());
            }

            // Cliente
            campo = mClientes.get(cl.getIdCliente());
            aux.setNombreCliente(campo != null ? campo : "N/D; ID: " + cl.getIdCliente());

            // Material
            campo = mMateriales.get(cl.getIdMaterial());
            aux.setNombreMaterial(campo != null ? campo : "N/D; ID: " + cl.getIdMaterial());

            // IVA
            campo = mIvas.get(cl.getIva());
            aux.setDescripcionIva(campo != null ? campo : "N/D; ID: " + cl.getIva());

            lResult.add(aux);

        }

        return lResult;
    }

    /**
     * Método que se encarga de convertir los clientes-operador en objeto vista
     * @param lList Lista con los objetos a convertir.
     * @return Lista resultado
     */
    public List<TDireccionClienteVista> convertirDireccionClientesVista(List<TDireccionCliente> lList) {
        List<TDireccionClienteVista> lResult = Utils.generarListaGenerica();

        TDireccionClienteVista aux = null;

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();
        List<TClientes> lClient = obtenerTodosClientes();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));
        // Nutrimos el diccionario con los clientes
        Map<Integer, String> mClientes = lClient.stream().collect(Collectors.toMap(TClientes::getId, TClientes::getNombre));

        String campo = "";

        for (TDireccionCliente cl : lList) {
            aux = new TDireccionClienteVista(cl);

            // Empleados
            campo = mEmpleados.get(cl.getUsuCrea());
            aux.setUsuCrea(campo != null ? campo : "N/D; id: " + cl.getUsuCrea());

            if (cl.getUsuModifica() != null) {
                campo = mEmpleados.get(cl.getUsuModifica());
                aux.setUsuModifica(campo != null ? campo : "N/D; id: " + cl.getUsuModifica());
            }

            // Cliente
            campo = mClientes.get(cl.getIdCliente());
            aux.setIdCliente(campo != null ? campo : "N/D; ID: " + cl.getIdCliente());

            lResult.add(aux);

        }

        return lResult;
    }

}