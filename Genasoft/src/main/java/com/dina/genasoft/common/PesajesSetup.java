/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.common;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TDireccionCliente;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TNumeroAlbaran;
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TPesajes;
import com.dina.genasoft.db.entity.TPesajesVista;
import com.dina.genasoft.db.entity.TRegistrosCambiosPesajes;
import com.dina.genasoft.db.entity.TTransportistas;
import com.dina.genasoft.db.mapper.TNumeroAlbaranMapper;
import com.dina.genasoft.db.mapper.TPesajesMapper;
import com.dina.genasoft.db.mapper.TRegistrosCambiosPesajesMapper;
import com.dina.genasoft.utils.EnvioCorreo;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.PesajesEnum;

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
    /** Inyección de Spring para poder acceder a la capa de datos de operadores. */
    @Autowired
    private OperadoresSetup                operadoresSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de transportistas. */
    @Autowired
    private TransportistasSetup            transportistasSetup;
    /** Inyección por Spring del mapper TNumeroAlbaran.*/
    @Autowired
    private TNumeroAlbaranMapper           tNumeroAlbaranMapper;
    /** Contendrá el ID del usuario del administrador para recibir notificaciones.*/
    @Value("${user.notificacions}")
    private String                         userNotifications;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private EnvioCorreo                    envioCorreo;
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
     * Método que nos retorna el pesaje a partir del nº de albarán.
     * @param albaran El albaran del pesaje.
     * @return El Pesaje encontrado.
     */
    public TPesajes obtenerPesajePorAlbaran(String albaran) {
        return tPesajesMapper.obtenerPesajePorAlbaran(albaran);
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
     * Método que nos retorna los pesajes que están dentro de la factura con ID pasado por parámetro.
     * @param idFactura El ID de la factura a consultar
     * @return Los pesajes existentes en la factura.
     */
    public List<TPesajes> obtenerPesajesIds(List<Integer> lIds) {
        return tPesajesMapper.obtenerPesajesIds(lIds);
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

        // Buscamos el pesaje por nº de albarán por si ya existe uno con el mismo número.

        TPesajes aux = obtenerPesajePorAlbaran(record.getNumeroAlbaran());

        if (aux != null) {
            aux.setNumeroAlbaran(obtenerNumeroAlbaran("" + PesajesEnum.TIPO_GENERICO.getValue()));
        }

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
            map.put("idOperador", record.getIdOperador());
            map.put("idTransportista", record.getIdTransportista());
            map.put("iva", record.getIva());
            map.put("base", record.getBase());
            map.put("precioKg", record.getPrecioKg());
            map.put("importe", record.getImporte());
            map.put("indFirmaCliente", record.getIndFirmaCliente());
            map.put("idIva", record.getIdIva());

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
        List<TDireccionCliente> lDirs = clientesSetup.obtenerTodasDireccionesCliente();
        List<TOperadores> lOpers = operadoresSetup.obtenerTodosOperadores();
        List<TTransportistas> lTrans = transportistasSetup.obtenerTodosTransportistas();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));
        // Nutrimos el diccionario con los clientes
        Map<Integer, String> mClientes = lClient.stream().collect(Collectors.toMap(TClientes::getId, TClientes::getNombre));
        // Nutrimos el diccionario con las direcciones
        Map<Integer, String> mDirs = lDirs.stream().collect(Collectors.toMap(TDireccionCliente::getId, TDireccionCliente::getDireccion));
        // Nutrimos el diccionario con los operadores
        Map<Integer, String> mOpers = lOpers.stream().collect(Collectors.toMap(TOperadores::getId, TOperadores::getNombre));
        // Nutrimos el diccionario con los transportistas
        Map<Integer, String> mTrans = lTrans.stream().collect(Collectors.toMap(TTransportistas::getId, TTransportistas::getNombre));

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

            // Dirección
            campo = mDirs.get(p.getIdDireccion());
            aux.setIdDireccion(campo != null ? campo : "N/D; ID: " + p.getIdDireccion());

            // Operador
            if (p.getIdOperador() != null) {
                campo = mOpers.get(p.getIdOperador());
                aux.setIdOperador(campo != null ? campo : "N/D; ID: " + p.getIdOperador());
            }

            // Transportista
            if (p.getIdTransportista() != null) {
                campo = mTrans.get(p.getIdTransportista());
                aux.setIdTransportista(campo != null ? campo : "N/D; ID: " + p.getIdTransportista());
            }

            lResult.add(aux);
        }

        return lResult;
    }

    /**
     * Método que nos retorna el número de albaran que le corresponde
     * @param tipoPesaje
     * @return
     */
    public String obtenerNumeroAlbaran(String tipoPesaje) {
        String result = "-1";

        Calendar cal = Calendar.getInstance();

        cal.setTime(Utils.generarFecha());
        int year = cal.get(Calendar.YEAR);
        Boolean entra = false;
        if (tipoPesaje.equals("00")) {
            year = cal.get(Calendar.YEAR) % 100;
            entra = true;
        }

        TNumeroAlbaran nAlbaran = tNumeroAlbaranMapper.obtenerNumeroAlbaran(tipoPesaje, year);

        if (nAlbaran == null) {
            year = cal.get(Calendar.YEAR) % 100;
            nAlbaran = tNumeroAlbaranMapper.obtenerNumeroAlbaran(tipoPesaje, year);
        }

        if (nAlbaran == null) {

            year = cal.get(Calendar.YEAR) % 100;

            int yearAnterior = year - 1;
            // Buscamos el último número del año anterior
            TNumeroAlbaran aux = tNumeroAlbaranMapper.obtenerNumeroAlbaran(tipoPesaje, yearAnterior);
            if (aux == null) {
                log.error("No se ha podido determinar el número de albaran con los siguientes datos: tipo de pedido: " + tipoPesaje + ", año anterior: " + yearAnterior);

            }
            result = "0000";
            String resultado = "" + year;
            // if (entra) {
            //     resultado = resultado + year + "000000";
            // } else {
            resultado = resultado + "000001";
            // }
            result = resultado;
            nAlbaran = new TNumeroAlbaran();
            nAlbaran.setFechaUltimaConsulta(Utils.generarFecha());
            nAlbaran.setTipoPesaje(tipoPesaje);
            if (entra) {
                nAlbaran.setUltimoNumero("00000" + 2);
            } else {
                nAlbaran.setUltimoNumero("00000" + 2);
            }
            nAlbaran.setYearActual(year);
            int cont = 0;
            boolean insert = false;
            while (cont < 10) {
                if (tNumeroAlbaranMapper.insert(nAlbaran) == 1) {
                    cont = 10;
                    insert = true;
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cont++;
                }
            }
            if (!insert) {
                if (userNotifications != null) {
                    try {
                        // Buscamos el empleado de las notificaciones
                        TEmpleados empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications));
                        envioCorreo.enviarCorreo(empl.getEmail(), "GENASOFT ERROR - No se ha podido generar el número de albarán", "No se ha podido realizar el insert", resultado);
                    } catch (Exception e) {
                        log.error("Se ha producido un error al enviar la notificación al usuario: " + userNotifications);
                    }
                }
            }
        } else {
            String resultado = "" + year;
            resultado = resultado + nAlbaran.getUltimoNumero();
            result = resultado;
            String valor = String.valueOf(Integer.valueOf(nAlbaran.getUltimoNumero()) + 1);
            if (nAlbaran.getUltimoNumero().length() == 6) {
                while (valor.length() < 6) {
                    valor = "0" + valor;
                }
            } else {
                while (valor.length() < 4) {
                    valor = "0" + valor;
                }
            }
            nAlbaran.setUltimoNumero(valor);
            nAlbaran.setFechaUltimaConsulta(Utils.generarFecha());
            int cont = 0;
            boolean insert = false;
            while (cont < 10) {

                if (tNumeroAlbaranMapper.updateByPrimaryKey(nAlbaran) == 1) {
                    insert = true;
                    cont = 10;
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cont++;
                }
            }
            if (!insert) {
                if (userNotifications != null) {
                    try {
                        // Buscamos el empleado de las notificaciones
                        TEmpleados empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications));
                        envioCorreo.enviarCorreo(empl.getEmail(), "GENASOFT ERROR - No se ha podido generar el número de albarán", "No se ha podido realizar el insert", resultado);
                    } catch (Exception e) {
                        log.error("Se ha producido un error al enviar la notificación al usuario: " + userNotifications);
                    }
                }
            }
        }
        // Retornamos el número del albaran encontrado
        return result;
    }

}
