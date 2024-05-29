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
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TEmpresas;
import com.dina.genasoft.db.entity.TFacturas;
import com.dina.genasoft.db.entity.TFacturasVista;
import com.dina.genasoft.db.entity.TRegistrosCambiosFacturas;
import com.dina.genasoft.db.mapper.TFacturasMapper;
import com.dina.genasoft.db.mapper.TRegistrosCambiosFacturasMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de facturas y la BD.
 */
@Component
@Slf4j
@Data
public class FacturasSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger   log              = org.slf4j.LoggerFactory.getLogger(FacturasSetup.class);
    /** Inyección por Spring del mapper TFacturasMapper.*/
    @Autowired
    private TFacturasMapper                 tFacturasMapper;
    /** Inyección por Spring del mapper TRegistrosCambiosFacturasMapper.*/
    @Autowired
    private TRegistrosCambiosFacturasMapper tRegistrosCambiosFacturasMapper;
    /** Inyección de Spring para poder acceder a la capa de datos de empleados. */
    @Autowired
    private EmpleadosSetup                  empleadosSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de empresas. */
    @Autowired
    private EmpresasSetup                   empresasSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de clientes. */
    @Autowired
    private ClientesSetup                   clientesSetup;
    private static final long               serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna la factura a partir del ID.
     * @param id El Id de la factura.
     * @return La factura encontrado.
     */
    public TFacturas obtenerFacturaPorId(Integer id) {
        return tFacturasMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos crea la factura y nos retorna el ID generado.
     * @param record La factura a crear.
     * @return El ID generado.
     */
    public int crearFacturaRetornaId(TFacturas record) {

        Integer id = -1;

        try {
            // Rellenamos los datos necesarios para crear el cliente.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("numeroFactura", record.getNumeroFactura());
            map.put("fechaFactura", record.getFechaFactura());
            map.put("empresa", record.getEmpresa());
            map.put("obra", record.getObra());
            map.put("idCliente", record.getIdCliente());
            map.put("idDireccion", record.getIdDireccion());
            map.put("base", record.getBase());
            map.put("descuento", record.getDescuento());
            map.put("subTotal", record.getSubtotal());
            map.put("totalNeto", record.getTotalNeto());
            map.put("total", record.getTotal());

            tFacturasMapper.insertRecord(map);

            id = (Integer) map.get("id");
        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_FACTURA + ", Error al registrar la factura: " + record.toString2() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return id;
    }

    /**
     * Método que nos registra en el sistema la factura pasada por parámetro.
     * @param p La factura a registrar
     * @return El resultado de la operación.
     */
    public String crearFactura(TFacturas record) {
        String result = Constants.OPERACION_OK;

        if (record.getId() != null && record.getId().equals(-1)) {
            // Es una modificación.
        } else {
            try {
                result = tFacturasMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_FACTURA;
            } catch (Exception e) {
                result = Constants.BD_KO_CREA_FACTURA;
                log.error(Constants.BD_KO_CREA_FACTURA + ", Error al crear el registro de creación de la factura: " + record.toString2() + ", ", e);
            }
        }

        return result;
    }

    /**
     * Método que nos registra en el sistema la factura pasada por parámetro.
     * @param p La factura a registrar
     * @return El resultado de la operación.
     */
    public String modificarFactura(TFacturas record) {
        String result = Constants.OPERACION_OK;

        if (record.getId() == null || record.getId().equals(-1)) {
            // Es una creación.
            crearFactura(record);
        } else {
            try {
                result = tFacturasMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_FACTURA;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_FACTURA;
                log.error(Constants.BD_KO_MODIF_FACTURA + ", Error al crear el registro de modificación de la factura: " + record.toString2() + ", ", e);
            }
        }

        return result;
    }

    /**
     * Método que se encarga de crear un nuevo registro de cambio de pesaje en el sistema.
     * @param record El registro de cambio de pesaje a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioFactura(TRegistrosCambiosFacturas record) {

        String result = Constants.OPERACION_OK;

        try {
            result = tRegistrosCambiosFacturasMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_FACTURA;
        } catch (Exception e) {
            result = Constants.BD_KO_MODIF_FACTURA;
            log.error(Constants.BD_KO_MODIF_FACTURA + ", Error al crear el registro de modificación de la factura: " + record.toString() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    public List<TFacturasVista> convertirFacturasVista(List<TFacturas> lFacturas) {
        List<TFacturasVista> lResult = Utils.generarListaGenerica();

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();
        List<TClientes> lClient = clientesSetup.obtenerTodosClientes();
        List<TEmpresas> lEmpr = empresasSetup.obtenerTodasEmpresas();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));
        // Nutrimos el diccionario con los clientes
        Map<Integer, String> mClientes = lClient.stream().collect(Collectors.toMap(TClientes::getId, TClientes::getNombre));
        // Nutrimos el diccionario con las empresas
        Map<Integer, String> mEmpresas = lEmpr.stream().collect(Collectors.toMap(TEmpresas::getId, TEmpresas::getNombre));

        String campo = "";

        TFacturasVista aux = null;

        for (TFacturas f : lFacturas) {
            aux = new TFacturasVista(f);

            // Empleados
            campo = mEmpleados.get(f.getUsuCrea());
            aux.setUsuCrea(campo != null ? campo : "N/D; id: " + f.getUsuCrea());

            if (f.getUsuModifica() != null) {
                campo = mEmpleados.get(f.getUsuModifica());
                aux.setUsuModifica(campo != null ? campo : "N/D; id: " + f.getUsuModifica());
            }

            // Cliente
            campo = mClientes.get(f.getIdCliente());
            aux.setIdCliente(campo != null ? campo : "N/D; ID: " + f.getIdCliente());

            // Empresa
            campo = mEmpresas.get(f.getEmpresa());
            aux.setEmpresa(campo != null ? campo : "N/D; ID: " + f.getEmpresa());

            lResult.add(aux);
        }

        return lResult;

    }

}
