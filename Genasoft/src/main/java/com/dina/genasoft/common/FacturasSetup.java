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
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TEmpresas;
import com.dina.genasoft.db.entity.TFacturas;
import com.dina.genasoft.db.entity.TFacturasVista;
import com.dina.genasoft.db.entity.TLineasFactura;
import com.dina.genasoft.db.entity.TLineasFacturaVista;
import com.dina.genasoft.db.entity.TNumeroAlbaran;
import com.dina.genasoft.db.entity.TRegistrosCambiosFacturas;
import com.dina.genasoft.db.mapper.TFacturasMapper;
import com.dina.genasoft.db.mapper.TLineasFacturaMapper;
import com.dina.genasoft.db.mapper.TNumeroAlbaranMapper;
import com.dina.genasoft.db.mapper.TRegistrosCambiosFacturasMapper;
import com.dina.genasoft.utils.EnvioCorreo;
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
    /** Inyección por Spring del mapper TLineasFacturaMapper.*/
    @Autowired
    private TLineasFacturaMapper            tLineasFacturaMapper;
    /** Inyección por Spring del mapper TNumeroAlbaran.*/
    @Autowired
    private TNumeroAlbaranMapper            tNumeroAlbaranMapper;
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
    /** Contendrá el ID del usuario del administrador para recibir notificaciones.*/
    @Value("${user.notificacions}")
    private String                          userNotifications;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private EnvioCorreo                     envioCorreo;
    private static final long               serialVersionUID = 5701299788812594642L;

    /**
     * Método que nos retorna la factura a partir del ID.
     * @param id El Id de la factura.
     * @return La factura encontrada.
     */
    public TFacturas obtenerFacturaPorId(Integer id) {
        return tFacturasMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna la línea de la factura a partir del ID.
     * @param id El Id de la línea de la factura.
     * @return La línea de la factura encontrada.
     */
    public TLineasFactura obtenerLineaFacturaPorId(Integer id) {
        return tLineasFacturaMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna la factura a partir del número de factura.
     * @param numeroFactura El nº de la factura.
     * @return La factura encontrada.
     */
    public TFacturas obtenerFacturaPorNumeroFactura(String numeroFactura) {
        return tFacturasMapper.obtenerFacturaPorNumeroFactura(numeroFactura);
    }

    /**
     * Método que nos retorna las facturas con el campo fecha_factura entre las fechas pasasas por parámetro
     * @param fec1 Fecha desde
     * @param fec2 Fecha hasta
     * @return Las facturas encontradas.
     */
    public List<TFacturas> obtenerFacturasFechas(Date fec1, Date fec2) {
        return tFacturasMapper.obtenerFacturasFechas(fec1, fec2);
    }

    /**
     * Método que nos retorna las facturas con el campo fecha_factura entre las fechas pasasas por parámetro
     * @param fec1 Fecha desde
     * @param fec2 Fecha hasta
     * @return Las facturas encontradas.
     */
    public List<TLineasFactura> obtenerLineasFacturaPorIdFactura(Integer idFactura) {
        return tLineasFacturaMapper.obtenerLineasFacturaPorIdFactura(idFactura);
    }

    /**
     * Método que nos retorna las facturas con el campo fecha_factura entre las fechas pasasas por parámetro
     * @param fec1 Fecha desde
     * @param fec2 Fecha hasta
     * @return Las facturas encontradas.
     */
    public List<TLineasFacturaVista> obtenerLineasFacturaPorIdFacturaVista(Integer idFactura) {
        return convertirLineasFacturaVista(obtenerLineasFacturaPorIdFactura(idFactura));
    }

    /**
     * Método que nos retorna las facturas en formato vista con el campo fecha_factura entre las fechas pasasas por parámetro
     * @param fec1 Fecha desde
     * @param fec2 Fecha hasta
     * @return Las facturas encontradas.
     */
    public List<TFacturasVista> obtenerFacturasFechasVista(Date fec1, Date fec2) {
        return convertirFacturasVista(tFacturasMapper.obtenerFacturasFechas(fec1, fec2));
    }

    /**
     * Método que nos crea la factura y nos retorna el ID generado.
     * @param record La factura a crear.
     * @return El ID generado.
     */
    public int crearFacturaRetornaId(TFacturas record) {

        Integer id = -1;

        // Generamos el nº de factura.
        record.setNumeroFactura(obtenerNumeroFactura("V"));

        if (obtenerFacturaPorNumeroFactura(record.getNumeroFactura()) != null) {
            record.setNumeroFactura(obtenerNumeroFactura("V"));
        }
        if (obtenerFacturaPorNumeroFactura(record.getNumeroFactura()) != null) {
            record.setNumeroFactura(obtenerNumeroFactura("V"));
        }
        if (obtenerFacturaPorNumeroFactura(record.getNumeroFactura()) != null) {
            return -2;
        }

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
            map.put("subtotal", record.getSubtotal());
            map.put("totalNeto", record.getTotalNeto());
            map.put("total", record.getTotal());
            map.put("usuCrea", record.getUsuCrea());
            map.put("fechaCrea", record.getFechaCrea());
            map.put("usuModifica", record.getUsuModifica());
            map.put("fechaModifica", record.getFechaModifica());

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
            modificarFactura(record);
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
    public String crearLineaFactura(TLineasFactura record) {
        String result = Constants.OPERACION_OK;

        if (record.getId() != null && record.getId().equals(-1)) {
            // Es una modificación.
        } else {
            try {
                result = tLineasFacturaMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_FACTURA;
            } catch (Exception e) {
                result = Constants.BD_KO_CREA_FACTURA;
                log.error(Constants.BD_KO_CREA_FACTURA + ", Error al crear el registro de creación de la factura: " + record.toString() + ", ", e);
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

    private List<TLineasFacturaVista> convertirLineasFacturaVista(List<TLineasFactura> lLineas) {
        List<TLineasFacturaVista> lResult = Utils.generarListaGenerica();

        TLineasFacturaVista aux = null;

        for (TLineasFactura lf : lLineas) {
            aux = new TLineasFacturaVista(lf);

            lResult.add(aux);
        }

        return lResult;
    }

    /**
     * Método que nos retorna el número de albaran que le corresponde
     * @param tipoPesaje
     * @return
     */
    public String obtenerNumeroFactura(String tipoPesaje) {
        String result = "-1";

        Calendar cal = Calendar.getInstance();

        cal.setTime(Utils.generarFecha());
        int year = cal.get(Calendar.YEAR);
        Boolean entra = false;

        year = cal.get(Calendar.YEAR) % 100;
        entra = true;

        tipoPesaje = tipoPesaje + year;

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
            String resultado = "";
            // if (entra) {
            //     resultado = resultado + year + "000000";
            // } else {
            resultado = resultado + "00001";
            // }
            result = tipoPesaje + "-" + resultado;
            nAlbaran = new TNumeroAlbaran();
            nAlbaran.setFechaUltimaConsulta(Utils.generarFecha());
            nAlbaran.setTipoPesaje(tipoPesaje);
            if (entra) {
                nAlbaran.setUltimoNumero("0000" + 2);
            } else {
                nAlbaran.setUltimoNumero("0000" + 2);
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
            String resultado = "";
            resultado = resultado + nAlbaran.getUltimoNumero();
            result = tipoPesaje + "-" + resultado;
            String valor = String.valueOf(Integer.valueOf(nAlbaran.getUltimoNumero()) + 1);
            if (nAlbaran.getUltimoNumero().length() == 6) {
                while (valor.length() < 6) {
                    valor = "0" + valor;
                }
            } else {
                while (valor.length() < 5) {
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

    public void eliminarLineaFactura(Integer id) {
        tLineasFacturaMapper.deleteByPrimaryKey(id);
    }

    public void eliminarFactura(Integer id) {
        tFacturasMapper.deleteByPrimaryKey(id);
    }

}
