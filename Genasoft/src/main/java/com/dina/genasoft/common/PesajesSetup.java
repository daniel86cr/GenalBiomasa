/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.common;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TPesajes;
import com.dina.genasoft.db.entity.TPesajesVista;
import com.dina.genasoft.db.entity.TRegistrosCambiosPesajes;
import com.dina.genasoft.db.mapper.TPesajesMapper;
import com.dina.genasoft.db.mapper.TRegistrosCambiosPesajesMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de pesajes y la BD.
 */
@Component
@Slf4j
@Data
public class PesajesSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger  log              = org.slf4j.LoggerFactory.getLogger(PesajesSetup.class);
    /** Inyección por Spring del mapper TPesajesMapper.*/
    @Autowired
    private TPesajesMapper                 tPesajesMapper;
    /** Inyección por Spring del mapper TRegistrosCambiosPesajesMapper.*/
    @Autowired
    private TRegistrosCambiosPesajesMapper tRegistrosCambiosPesajesMapper;
    /** Inyección de Spring para poder acceder a la capa de datos de empleados. */
    @Autowired
    private EmpleadosSetup                 empleadosSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de clientes. */
    @Autowired
    private ClientesSetup                  clientesSetup;
    private static final long              serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna el pesaje a partir del ID.
     * @param id El Id del pesaje.
     * @return El Pesaje encontrado.
     */
    public TPesajes obtenerPesajePorId(Integer id) {
        return tPesajesMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna los pesajes que están dentro de la factura con ID pasado por parámetro.
     * @param idFactura El ID de la factura a consultar
     * @return Los pesajes existentes en la factura.
     */
    public List<TPesajes> obtenerPesajesFactura(Integer idFactura) {
        return tPesajesMapper.obtenerPesajesFactura(idFactura);
    }

    /**
     * Método que nos retorna los pesajes que tienen como fecha las comprendidas por las de los parámetros.
     * @param fecha1 Fecha desde
     * @param fecha1 Fecha Hasta
     * @return Los pesajes existentes en la factura.
     */
    public List<TPesajes> obtenerPesajesFechas(Date fecha1, Date fecha2) {
        return tPesajesMapper.obtenerPesajesFechas(fecha1, fecha2);
    }

    /**
     * Método que nos retorna los pesajes que tienen como fecha las comprendidas por las de los parámetros.
     * @param fecha1 Fecha desde
     * @param fecha1 Fecha Hasta
     * @return Los pesajes existentes en la factura.
     */
    public List<TPesajesVista> obtenerPesajesFechasVista(Date fecha1, Date fecha2) {
        return convertirPesajesVista(obtenerPesajesFechas(fecha1, fecha2));
    }

    /**
     * Método que nos crea el control de producto terminado y nos retorna el ID generado.
     * @param record El control de producto terminado a crear.
     * @return El ID generado.
     */
    public int crearPesajeRetornaId(TPesajes record) {

        Integer id = -1;

        try {
            // Rellenamos los datos necesarios para crear el cliente.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("numeroAlbaran", record.getNumeroAlbaran());
            map.put("idCliente", record.getIdCliente());
            map.put("idDireccion", record.getIdDireccion());
            map.put("fechaPesaje", record.getFechaPesaje());
            map.put("obra", record.getObra());
            map.put("destino", record.getDestino());
            map.put("idMaterial", record.getIdMaterial());
            map.put("refMaterial", record.getRefMaterial());
            map.put("lerMaterial", record.getLerMaterial());
            map.put("descMaterial", record.getDescMaterial());
            map.put("kgsBruto", record.getKgsBruto());
            map.put("kgsNeto", record.getKgsNeto());
            map.put("tara", record.getTara());
            map.put("usuCrea", record.getUsuCrea());
            map.put("fechaCrea", record.getFechaCrea());
            map.put("usuModifica", record.getUsuModifica());
            map.put("fechaModifica", record.getFechaModifica());
            map.put("idFactura", record.getIdFactura());
            map.put("estado", record.getEstado());

            tPesajesMapper.insertRecord(map);

            id = (Integer) map.get("id");
        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_PESAJE + ", Error al registrar el pesaje: " + record.toString2() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return id;
    }

    /**
     * Método que nos registra en el sistema el pesaje pasado por parámetro.
     * @param p El pesaje a registrar
     * @return El resultado de la operación.
     */
    public String crearPesaje(TPesajes record) {
        String result = Constants.OPERACION_OK;

        if (record.getId() != null && record.getId().equals(-1)) {
            // Es una modificación.
        } else {
            try {
                result = tPesajesMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_PESAJE;
            } catch (Exception e) {
                result = Constants.BD_KO_CREA_PESAJE;
                log.error(Constants.BD_KO_CREA_PESAJE + ", Error al crear el registro de creación del pesaje: " + record.toString2() + ", ", e);
            }
        }

        return result;
    }

    /**
     * Método que nos registra en el sistema el pesaje pasado por parámetro.
     * @param p El pesaje a registrar
     * @return El resultado de la operación.
     */
    public String modificarPesaje(TPesajes record) {
        String result = Constants.OPERACION_OK;

        if (record.getId() == null || record.getId().equals(-1)) {
            // Es una creación.
            crearPesaje(record);
        } else {
            try {
                result = tPesajesMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_PESAJE;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_PESAJE;
                log.error(Constants.BD_KO_MODIF_PESAJE + ", Error al crear el registro de modificación del pesaje: " + record.toString2() + ", ", e);
            }
        }

        return result;
    }

    /**
     * Método que se encarga de crear un nuevo registro de cambio de pesaje en el sistema.
     * @param record El registro de cambio de pesaje a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioPesaje(TRegistrosCambiosPesajes record) {

        String result = Constants.OPERACION_OK;

        try {
            result = tRegistrosCambiosPesajesMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_PESAJE;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_EMPL;
            log.error(Constants.BD_KO_CREA_EMPL + ", Error al crear el registro de modificación del pesaje: " + record.toString() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    private List<TPesajesVista> convertirPesajesVista(List<TPesajes> lPesajes) {
        List<TPesajesVista> lResult = Utils.generarListaGenerica();

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();
        List<TClientes> lClient = clientesSetup.obtenerTodosClientes();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));
        // Nutrimos el diccionario con los clientes
        Map<Integer, String> mClientes = lClient.stream().collect(Collectors.toMap(TClientes::getId, TClientes::getNombre));

        TPesajesVista aux = null;
        String campo = "";

        for (TPesajes p : lPesajes) {
            aux = new TPesajesVista(p);

            // Empleados
            campo = mEmpleados.get(p.getUsuCrea());
            aux.setUsuCrea(campo != null ? campo : "N/D; id: " + p.getUsuCrea());

            if (p.getUsuModifica() != null) {
                campo = mEmpleados.get(p.getUsuModifica());
                aux.setUsuModifica(campo != null ? campo : "N/D; id: " + p.getUsuModifica());
            }

            // Cliente
            campo = mClientes.get(p.getIdCliente());
            aux.setIdCliente(campo != null ? campo : "N/D; ID: " + p.getIdCliente());

            lResult.add(aux);
        }

        return lResult;
    }

}
