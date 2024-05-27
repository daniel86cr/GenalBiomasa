/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.db.entity.TAbonos;
import com.dina.genasoft.db.entity.TCompras;
import com.dina.genasoft.db.entity.TComprasFict;
import com.dina.genasoft.db.entity.TComprasFictVista;
import com.dina.genasoft.db.entity.TComprasHis;
import com.dina.genasoft.db.entity.TComprasVista;
import com.dina.genasoft.db.entity.TDevoluciones;
import com.dina.genasoft.db.entity.TLineasDevoluciones;
import com.dina.genasoft.db.entity.TLineasVentas;
import com.dina.genasoft.db.entity.TLineasVentasHis;
import com.dina.genasoft.db.entity.TLineasVentasVista;
import com.dina.genasoft.db.entity.TMovimientosAlmacen;
import com.dina.genasoft.db.entity.TProveedores;
import com.dina.genasoft.db.entity.TVentas;
import com.dina.genasoft.db.entity.TVentasFict;
import com.dina.genasoft.db.entity.TVentasFictVista;
import com.dina.genasoft.db.entity.TVentasHis;
import com.dina.genasoft.db.entity.TVentasVista;
import com.dina.genasoft.db.mapper.TAbonosMapper;
import com.dina.genasoft.db.mapper.TComprasFictMapper;
import com.dina.genasoft.db.mapper.TComprasHisMapper;
import com.dina.genasoft.db.mapper.TDevolucionesMapper;
import com.dina.genasoft.db.mapper.TLineasDevolucionesMapper;
import com.dina.genasoft.db.mapper.TLineasVentasHisMapper;
import com.dina.genasoft.db.mapper.TMovimientosAlmacenMapper;
import com.dina.genasoft.db.mapper.TVentasFictMapper;
import com.dina.genasoft.db.mapper.TVentasHisMapper;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.CertificacionesEnum;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de compras/ventas y la BD.
 */
@Component
@Slf4j
@Data
public class ImportadorSetup implements Serializable {

    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log                          = org.slf4j.LoggerFactory.getLogger(ImportadorSetup.class);
    /** Inyección por Spring de la capa común de datos.*/
    @Autowired
    private CommonSetup                   commonSetup;
    /** Inyección por Spring de la capa común de compras.*/
    @Autowired
    private ComprasSetup                  comprasSetup;
    /** Inyección por Spring de la capa común de ventas.*/
    @Autowired
    private VentasSetup                   ventasSetup;
    /** Inyección por Spring de la capa común de líneas de ventas.*/
    @Autowired
    private LineasVentasSetup             lineasVentasSetup;
    /** Inyección por Spring de la capa de proveedores.*/
    @Autowired
    private ProveedoresSetup              proveedoresSetup;
    /** Inyección por Spring del mapper TAbonosMapper.*/
    @Autowired
    private TAbonosMapper                 tAbonosMapper;
    /** Inyección por Spring del mapper TComprasFictMapper.*/
    @Autowired
    private TComprasFictMapper            tComprasFictMapper;
    /** Inyección por Spring del mapper TComprasHisMapper.*/
    @Autowired
    private TComprasHisMapper             tComprasHisMapper;
    /** Inyección por Spring del mapper TDevolucionesMapper.*/
    @Autowired
    private TDevolucionesMapper           tDevolucionesMapper;
    /** Inyección por Spring del mapper TVentasFictMapper.*/
    @Autowired
    private TVentasFictMapper             tVentasFictMapper;
    /** Inyección por Spring del mapper TVentasHisMapper.*/
    @Autowired
    private TVentasHisMapper              tVentasHisMapper;
    /** Inyección por Spring del mapper TLineasDevolucionesMapper.*/
    @Autowired
    private TLineasDevolucionesMapper     tLineasDevolucionesMapper;
    /** Inyección por Spring del mapper TVentasHisMapper.*/
    @Autowired
    private TLineasVentasHisMapper        tLineasVentasHisMapper;
    /** Inyección por Spring del mapper TMovimientosAlmacenMapper.*/
    @Autowired
    private TMovimientosAlmacenMapper     tMovimientosAlmacenMapper;
    /** Contendrá el número de campos que debe tener el fichero de compras.*/
    @Value("${app.compras.fields}")
    private Integer                       appComprasFields;
    /** Contendrá el número de campos que debe tener el fichero de ventas.*/
    @Value("${app.ventas.fields}")
    private Integer                       appVentasFields;
    /** Contendrá el número de campos que debe tener el fichero de ventas.*/
    @Value("${app.trazabilidad.fields}")
    private Integer                       appLotesFields;
    /** Contendrá el número de campos que debe tener el fichero de ventas.*/
    @Value("${app.mov.alm.fields}")
    private Integer                       appMovAlmFields;
    private static final long             serialVersionUID             = 5701299788812594642L;
    /** Para identificar la importación */
    private String                        nombreImportacion;
    /** Lista con todas las variedades. */
    private List<String>                  lTodasVariedades;
    /**                                 COMPRAS                                 */
    /** La posición dentro del fichero de compras del campo Número de albarán. */
    private final Integer                 POS_ALBARAN_C                = 0;
    /** La posición dentro del fichero de compras del campo Fecha compra. */
    private final Integer                 POS_FECHA_C                  = 1;
    /** La posición dentro del fichero de compras del campo Partida de compras. */
    private final Integer                 POS_PARTIDA_C                = 2;
    /** La posición dentro del fichero de compras del campo centro de compras. */
    private final Integer                 POS_CENTRO_C                 = 3;
    /** La posición dentro del fichero de compras del campo cajas de compras. */
    private final Integer                 POS_CAJAS_C                  = 4;
    /** La posición dentro del fichero de compras del campo peso bruto de compras. */
    private final Integer                 POS_PESO_B_C                 = 5;
    /** La posición dentro del fichero de compras del campo tipo de compra de compras. */
    private final Integer                 POS_TIPO_COMPRA_C            = 6;
    /** La posición dentro del fichero de compras del campo fecha de calibración de compras. */
    private final Integer                 POS_FECHA_CALIB_C            = 7;
    /** La posición dentro del fichero de compras del campo trazabilidad de compras. */
    private final Integer                 POS_TRAZ_C                   = 8;
    /** La posición dentro del fichero de compras del campo peso neto de compras. */
    private final Integer                 POS_PESO_N_C                 = 9;
    /** La posición dentro del fichero de compras del campo finca neto de compras. */
    private final Integer                 POS_FINCA_C                  = 10;
    /** La posición dentro del fichero de compras del campo producto de compras. */
    private final Integer                 POS_PRODUCTO_C               = 11;
    /** La posición dentro del fichero de compras del campo proveedor de compras. */
    private final Integer                 POS_PROVEEDOR_C              = 12;
    /** La posición dentro del fichero de compras del campo aviso calidad de compras. */
    private final Integer                 POS_AVISO_CAL_C              = 13;
    /** La posición dentro del fichero de compras del campo orígen de compras. */
    private final Integer                 POS_ORIGEN_C                 = 14;
    /** La posición dentro del fichero de compras del campo família del artículo de compras. */
    private final Integer                 POS_FAMILIA_ART_C            = 15;
    /** La posición dentro del fichero de compras del campo calidad de compras. */
    private final Integer                 POS_CALIDAD__C               = 16;
    /** La posición dentro del fichero de compras del campo variedad de compras. */
    private final Integer                 POS_VARIEDAD__C              = 17;
    /** La posición dentro del fichero de compras del campo variedad de compras. */
    private final Integer                 POS_CALIBRE__C               = 18;
    /** La posición dentro del fichero de compras del campo ID EXTERNO de compras. */
    private final Integer                 POS_ID_EXTERNO_C             = 19;
    /**                                 VENTAS                                  */
    /** La posición dentro del fichero de ventas del campo Número de pedido. */
    private final Integer                 POS_PEDIDO_V                 = 0;
    /** La posición dentro del fichero de ventas del campo Número de albarán. */
    private final Integer                 POS_LINEA_PEDIDO_V           = 1;
    /** La posición dentro del fichero de ventas del campo Número de albarán. */
    private final Integer                 POS_ALBARAN_V                = 2;
    /** La posición dentro del fichero de ventas del campo calibre. */
    private final Integer                 POS_CALIBRE_V                = 3;
    /** La posición dentro del fichero de ventas del campo bultos. */
    private final Integer                 POS_BULTOS_V                 = 4;
    /** La posición dentro del fichero de ventas del campo id externo. */
    private final Integer                 POS_ID_EXTERNO_V             = 5;
    /** La posición dentro del fichero de ventas del campo proveedor. */
    private final Integer                 POS_CLIENTE_V                = 6;
    /** La posición dentro del fichero de ventas del campo fecha salida. */
    private final Integer                 POS_FECHA_SAL_V              = 7;
    /** La posición dentro del fichero de ventas del campo kgs netos. */
    private final Integer                 POS_KGS_NETO_V               = 8;
    /** La posición dentro del fichero de ventas del campo peso envase. */
    private final Integer                 POS_PESO_ENV_V               = 9;
    /** La posición dentro del fichero de ventas del campo variedad. */
    private final Integer                 POS_VARIEDAD_V               = 10;
    /** La posición dentro del fichero de ventas del campo variedad. */
    private final Integer                 POS_ORIGEN_V                 = 11;
    /** La posición dentro del fichero de ventas del campo calidad venta. */
    private final Integer                 POS_CALIDAD_VENTA_V          = 12;
    /** La posición dentro del fichero de ventas del campo lote movimiento compra. */
    private final Integer                 POS_LOTE_MOV_VENTA_V         = 13;
    /** La posición dentro del fichero de ventas del campo confección. */
    private final Integer                 POS_CONFECCION_V             = 14;
    /** La posición dentro del fichero de ventas del campo envase. */
    private final Integer                 POS_ENVASE_V                 = 15;
    /** La posición dentro del fichero de ventas del campo unidad consumo. */
    private final Integer                 POS_UND_CONSUMO_V            = 16;
    /** La posición dentro del fichero de ventas del campo línea de pedido - lote. */
    private final Integer                 POS_LINEA_PEDIDO_LOTE_V      = 17;
    /** La posición dentro del fichero de ventas del campo línea de pedido - lote caja. */
    private final Integer                 POS_LINEA_PEDIDO_LOTE_CAJA_V = 18;
    /** La posición dentro del fichero de ventas del campo familia. */
    private final Integer                 POS_FAMILIA_V2               = 19;
    /** La posición dentro del fichero de ventas del campo plantilla. */
    private final Integer                 POS_PLANTILLA_V              = 20;
    /** La posición dentro del fichero de ventas del campo fecha de venta. */
    private final Integer                 POS_FECHA_VENTA_V            = 22;
    /** La posición dentro del fichero de ventas del campo código cliente. */
    private final Integer                 POS_COD_CLIENTE_V            = 23;
    /** La posición dentro del fichero de ventas del campo centro descarga. */
    private final Integer                 POS_CENTRO_DESCARGA_V        = 24;
    /** La posición dentro del fichero de ventas del campo dirección descarga. */
    private final Integer                 POS_DIR_DESCARGA_V           = 25;
    /** La posición dentro del fichero de ventas del campo referencia. */
    private final Integer                 POS_REFERENCIA_V             = 26;
    /** La posición dentro del fichero de ventas del campo categoria. */
    private final Integer                 POS_CATEGORIA_V              = 27;
    /** La posición dentro del fichero de ventas del campo marca . */
    private final Integer                 POS_MARCA_V                  = 28;
    /** La posición dentro del fichero de ventas del campo unidades. */
    private final Integer                 POS_UNIDADES_V               = 29;
    /** La posición dentro del fichero de ventas del campo palet. */
    private final Integer                 POS_PALE_V                   = 30;
    /** La posición dentro del fichero de ventas del campo nº de palets. */
    private final Integer                 POS_N_PALE_V                 = 31;
    /** La posición dentro del fichero de ventas del campo dirección del cliente. */
    private final Integer                 POS_DIRECCION_CLIENTE_V      = 32;
    /** La posición dentro del fichero de ventas del campo CIF del cliente. */
    private final Integer                 POS_CIF_CLIENTE_V            = 33;
    /** La posición dentro del fichero de ventas del campo télefono del cliente. */
    private final Integer                 POS_TELEFONO_CLIENTE_V       = 34;
    /** La posición dentro del fichero de ventas del campo transportista. */
    private final Integer                 POS_TRANSPORTISTA_V          = 35;
    /** La posición dentro del fichero de ventas del campo transportista. */
    private final Integer                 POS_NOMBRE_PRODUCTO_V        = 36;
    /**                                 TRAZABILIDADES                                  */
    /** La posición dentro del fichero de ventas del campo trazabilidad. */
    private final Integer                 POS_ID_VENTA_TRAZ            = 0;
    private final Integer                 POS_PROVEEDOR_TRAZ           = 1;
    private final Integer                 POS_CERTIFICACION_TRAZ       = 2;
    private final Integer                 POS_REFERENCIA_COMPRA_TRAZ   = 3;
    private final Integer                 POS_ALBARAN_COMPRA_TRAZ      = 4;
    //private final Integer                 POS_KGS_TRAZADOS_TRAZ        = 5;
    private final Integer                 POS_KGS_TRAZABILIDAD_TRAZ    = 6;
    private final Integer                 POS_ID_TRAZ                  = 7;
    private final Integer                 POS_LOTE_TRAZ                = 8;
    //private final Integer                 POS_ID_PALE_TRAZ             = 9;
    private final Integer                 POS_BULTOS_PALE_TRAZ         = 10;
    //private final Integer                 POS_FAMILIA_TRAZ             = 11;
    //private final Integer                 POS_VARIEDAD_TRAZ            = 12;

    /** La posición dentro del fichero de ventas del campo trazabilidad. */
    //private final Integer                 POS_TRAZA_V                  = 3;
    /** La posición dentro del fichero de ventas del campo id Pale. */
    //private final Integer                 POS_ID_PALE_V                = 4;
    /** La posición dentro del fichero de ventas del campo bultos por pale. */
    //private final Integer                 POS_BULTOS_PALE_V            = 6;
    /** La posición dentro del fichero de ventas del campo cliente. */
    //private final Integer                 POS_PROVEEDOR_V              = 8;
    /** La posición dentro del fichero de ventas del campo kgs. */
    //private final Integer                 POS_KGS_V                    = 10;

    /** La posición dentro del fichero de ventas del campo calidad compra. */
    //private final Integer                 POS_CALIDAD_COMPRA_V         = 15;

    /** La posición dentro del fichero de ventas del campo referencia compra. */
    //private final Integer                 POS_REF_COMPRA_V             = 17;
    /** La posición dentro del fichero de ventas del campo albarán compra. */
    //private final Integer                 POS_ALBARAN_COMPRA_V         = 18;

    /** La posición dentro del fichero de ventas del campo familia. */
    //private final Integer                 POS_FAMILIA_V                = 25;
    /**                                 TRAZABILIDADES                                  */
    /** La posición dentro del fichero de movimientos de almacén del campo trazabilidad. */
    /** La posición dentro del fichero de ventas del campo familia. */
    private final Integer                 POS_MOV_ALM_ID               = 0;
    /** La posición dentro del fichero de ventas del campo familia. */
    private final Integer                 POS_MOV_ALM_ID_PALET         = 1;
    /** La posición dentro del fichero de ventas del campo familia. */
    private final Integer                 POS_MOV_ALM_ID_MOV_VENTA     = 2;
    private String                        textoCorreo;
    private Double                        progreso;
    /** CONSTANTES PARA IDENTIFICAR EL ERROR EN LA LÍNEA DE VENTA. */
    /** La certificación de la venta no coincide con la de la compra. */
    private final Integer                 ERROR_CERTIFICACION_VENTA    = 1;
    /** La certificación de la venta no coincide con la de la compra. */
    private final Integer                 ERROR_CERTIFICACION_COMPRA   = -1;
    /** Los kgs de la línea de venta sobrepasan los que quedan pendientes en la venta. */
    private final Integer                 ERROR_KGS_VENTA              = 2;
    /** Los kgs de la línea de venta sobrepasan los que quedan pendientes en la compra. */
    private final Integer                 ERROR_KGS_COMPRA             = -2;
    /** No existe la venta asociada a la línea. */
    private final Integer                 ERROR_VENTA_NO_EXISTE        = 3;
    /** No existe la compra asociada a la línea. */
    private final Integer                 ERROR_VENTA_NO_EXISTE_COMPRA = 4;
    private TLineasVentas                 lVenta;
    private final String                  SIN_LOTE                     = "S/L";
    public final List<String>             lVariedadesMango             = Collections.unmodifiableList(Arrays.asList("PALMER", "KENT", "OSTEEN", "KENT AVION", "TOMMY ATKINS", "KEITT"));
    public final List<String>             lVariedadesPitaya            = Collections.unmodifiableList(
                                                                                                      Arrays.asList(
                                                                                                                    "PITAYA CARNE BLANCA",
                                                                                                                        "PITAYA CARNE ROJA",
                                                                                                                        "AMARILLA",
                                                                                                                        "PITAYA ROJA CARNE ROJA",
                                                                                                                        "PITAYA ROJA CARNE BLANCA",
                                                                                                                        "PITAYA BLANCA CARNE BLANCA",
                                                                                                                        "PITAYA BLANCACARNE ROJA"));
    /** Se le aumenta el 60 respecto del Movimiento de venta. */
    private final Double                  PORCENTAJE_GUACAMOLE         = 1.6;
    /** Lista de objectos con los diferentes ejercicios disponibles. */
    private List<String>                  lEjercicios;

    /**
     * @return the progreso
     */
    public Double getProgreso() {
        return progreso;
    }

    /**
     * @param progreso the progreso to set
     */
    public void setProgreso(Double progreso) {
        this.progreso = progreso;
    }

    public List<String> getlEjercicios() {
        return lEjercicios;
    }

    /**
     * Método que nos importa el contenido del fichero pasado por parámetros con contenido de compras.
     * @param path El path que contiene el fichero.
     * @param nombre para identificar la importación.
     * @return El resultado de la importación.
     * @throws GenasoftException 
     */
    public String importarFicheroCompras(String path, String nombre, Integer user) throws GenasoftException {
        progreso = Double.valueOf(0);
        String result = "";

        Integer total = 0;
        Integer ok = 0;
        Integer abs = 0;
        Integer ko = 0;
        nombreImportacion = nombre.trim();
        // Para leer el fichero.
        BufferedReader br = null;
        FileReader fr = null;
        Date fechaCrea = Utils.generarFecha();
        TAbonos abono;

        textoCorreo = "                           RESULTADO IMPORTACIÓN COMPRAS " + nombre + "\n\n";

        // Empezamos con el primer fichero, el que contiene los productos.
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            String[] datosLinea;
            Integer ordenLinea = -1;

            //inicializarListasCompra();

            // El primer fichero debe tener 2 campos por línea, separadas por ","
            // Nutrimos nuestra lista de productos.
            // Asumimos que no podemos tener productos con Ids duplicados, ya que en BD esta casuística no se puede dar.
            while ((linea = br.readLine()) != null) {
                // La primera línea es la cabecera del fichero.
                if (ordenLinea.equals(-1)) {
                    ordenLinea++;
                    continue;
                }
                if (linea.contains("\t")) {
                    if (linea.split("\t").length != appComprasFields) {
                        linea = linea + " \t";
                    }
                    datosLinea = linea.split("\t");
                } else {
                    if (linea.split(";").length != appComprasFields) {
                        linea = linea + " ;";
                    }
                    datosLinea = linea.split(";");
                }

                total++;
                if (datosLinea.length < appComprasFields || datosLinea.length > appComprasFields) {
                    log.error("La linea con orden " + ordenLinea + " no tiene el numero de campos correctos, tiene " + datosLinea.length + " y el correcto es: " + appComprasFields);
                    textoCorreo += "La linea con orden " + ordenLinea + " no tiene el numero de campos correctos, tiene " + datosLinea.length + " y el correcto es: " + appComprasFields + "\n";
                    ko++;
                    ordenLinea++;
                    continue;
                }

                try {
                    TCompras compra = nutrirCompraDatosLinea(datosLinea, ordenLinea, user, fechaCrea);

                    if (compra.getPesoNetoFin().equals(Double.valueOf(0))) {
                        compra.setPesoNetoDisponible(Double.valueOf(0));
                        compra.setCerrada(1);
                    } else {
                        compra.setCerrada(0);
                    }
                    if (compra.getProductoFin().toLowerCase().trim().equals("varios") || compra.getFamiliaFin().toLowerCase().trim().equals("administracion") || compra.getPesoNeto().equals(Double.valueOf(0))) {
                        abono = new TAbonos();
                        abono.copiaDesdeCompra(compra);
                        crearAbonoRetonaId(abono);
                        textoCorreo += "Se crea un abono ya que el propducto es 'VARIOS' o la familia es 'ADMINISTRACION' o el peso_neto es cero en la linea con orden " + ordenLinea + "\n";
                        abs++;
                    } else {
                        if (comprasSetup.obtenerCompraPorEjercicioIdExterno(commonSetup.getEjercicio(), compra.getIdExterno()) == null) {
                            compra.setId(crearCompraRetornaId(compra, true));

                            if (compra.getId() < 0) {
                                log.error("La linea con orden " + ordenLinea + " no se ha podido crear, el ID es: " + compra.getId());
                                textoCorreo += "La linea con orden " + ordenLinea + " no se ha podido crear, el ID es: " + compra.getId() + "\n";
                                ko++;
                                ordenLinea++;
                                continue;
                            }

                            // Añadimos la compra en los diccionarios de compras.
                            //utrirDiccionariosCompras(compra);
                            ok++;
                        }
                    }

                } catch (GenasoftException te) {
                    result += result;
                    ko++;
                }

                ordenLinea++;

            }

            result = "Del total de líneas (" + total + ") \n - Correctamente se han importado: " + ok + " registros. \n - Líneas de abonos: " + abs + " registros. \n - Líneas KO: " + ko + " registros ";
            textoCorreo += "Del total de lineas (" + total + ") \n - Correctamente se han importado: " + ok + " registros. - Lineas de abonos: " + abs + " registros. \n - Líneas KO: " + ko + " registros ";
            String textoCorreo2 = "..:FICHERO DE COMPRAS:.. Del total de lineas (" + total + ") - Correctamente se han importado: " + ok + " registros. - Lineas de abonos: " + ko + " registros.";

            commonSetup.enviaNotificacionCorreo("[GENASOFT] RESULTADO IMPORTACIÓN FICHERO DE COMPRAS", textoCorreo, null, user);
            commonSetup.enviaNotificacionWhatsAppMasivo(textoCorreo2);

        } catch (FileNotFoundException e) {
            log.info("NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR: ", e);
            e.printStackTrace();
            result = "No se ha podido importar el fichero, no se ha subido correctamente.";
        } catch (IOException ioe) {
            log.info("NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR: ", ioe);
            ioe.printStackTrace();
            result = "No se ha podido importar el fichero, no se ha subido correctamente.";
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Retornamos el resultado de la importación.
        return result;

    }

    /**
     * Método que nos importa el contenido del fichero pasado por parámetros con contenido de ventas.
     * @param path El path que contiene el fichero.
     * @param nombre para identificar la importación.
     * @return El resultado de la importación.
     */
    public String importarFicheroVentas(String path, String nombre, Integer user) {

        String result = "";

        Integer total = 0;
        Integer ok = 0;
        Integer devs = 0;
        Integer ko = 0;
        nombreImportacion = nombre;
        // Para leer el fichero.
        BufferedReader br = null;
        FileReader fr = null;
        Date fechaCrea = Utils.generarFecha();
        TDevoluciones dev = null;
        Map<String, TVentas> mVentas = new HashMap<String, TVentas>();
        textoCorreo = "                           RESULTADO IMPORTACIÓN VENTAS " + nombre + "\n\n";
        //TVentas vAux = null;
        TVentas venta = null;
        // Empezamos con el primer fichero, el que contiene los productos.
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            String[] datosLinea;
            Integer ordenLinea = -1;

            //inicializarListasVenta();            
            // El primer fichero debe tener 2 campos por línea, separadas por ","
            // Nutrimos nuestra lista de productos.
            // Asumimos que no podemos tener productos con Ids duplicados, ya que en BD esta casuística no se puede dar.
            while ((linea = br.readLine()) != null) {
                // La primera línea es la cabecera del fichero.
                if (ordenLinea.equals(-1)) {
                    ordenLinea++;
                    continue;
                }
                if (linea.contains("\t")) {
                    if (linea.split("\t").length < appVentasFields || linea.split("\t").length == appVentasFields) {
                        linea = linea + " \t";
                    }
                    datosLinea = linea.split("\t");
                } else {
                    if (linea.split(";").length < appVentasFields || linea.split(";").length == appVentasFields) {
                        linea = linea + " ;";
                    }
                    datosLinea = linea.split(";");
                }

                total++;

                if (datosLinea.length < appVentasFields) {
                    log.error("La linea con orden " + ordenLinea + " no tiene el numero de campos correctos, tiene " + datosLinea.length + " y el correcto es: " + appVentasFields);
                    textoCorreo += "La linea con orden " + ordenLinea + " no tiene el numero de campos correctos, tiene " + datosLinea.length + " y el correcto es: " + appVentasFields + "\n";
                    ko++;
                    ordenLinea++;
                    continue;
                }

                try {

                    venta = nutrirVentaDatosLinea(datosLinea, ordenLinea, user, fechaCrea);
                    venta.setIndBasura(0);
                    venta.setNumTrazabilidades(0);

                    // Si tiene kgs negativos, asumimos que es una devolución.
                    if (venta.getKgsNetos() < Double.valueOf(0) || venta.getNumBultosFin() < 0 || venta.getKgsNetos().equals(Double.valueOf(0)) || venta.getFamiliaFin().toLowerCase().contains("caja")) {
                        dev = new TDevoluciones();
                        dev.copiaDesdeVenta(venta);
                        crearDevolucionRetornaId(dev);
                        textoCorreo += "Se crea una devolución ya que los kgs son negativos o cero en la linea con orden " + ordenLinea + "\n";
                        devs++;
                    } else {
                        if (venta.getGgn() == null) {
                            venta.setGgn("NO GG");
                        }
                        venta.setId(crearVentaRetornaId(venta, true, false));
                        if (venta.getId() < 0) {
                            log.error("La linea con orden " + ordenLinea + " no se ha podido crear, el ID es: " + venta.getId());
                            textoCorreo += "La linea con orden " + ordenLinea + " no se ha podido crear, el ID es: " + venta.getId() + "\n";
                            ko++;
                            ordenLinea++;
                            continue;
                        }
                        // Miramos si la familia es de un tipo especial que tiene 2 o más productos en una caja
                        if (venta.getFamilia().equals("CROWDFARM 45KG AGUCATE CHIRI KUMQ OVERHA")) {
                            // Tiene 2 Kgs de Aguacate (ya creado)
                            // 2 Kgs de chirimoya
                            venta.setFamiliaFin("CHIRIMOYA");
                            venta.setVariedadFin("FINO DE JETE");
                            venta.setKgsNetos(Double.valueOf(2));
                            venta.setId(null);
                            venta.setId(crearVentaRetornaId(venta, true, false));
                            // 0.5 kgs de kumquat
                            venta.setFamiliaFin("KUMQUAT");
                            venta.setVariedadFin("KUMQUAT");
                            venta.setKgsNetos(Double.valueOf(0.5));
                            venta.setId(null);
                            venta.setId(crearVentaRetornaId(venta, true, false));
                        } else if (venta.getFamilia().equals("CROWDFARM 4KG AGUACATE KUMQUAT OVERHARVE")) {
                            // Tiene 3 Kgs de Aguacate (ya creado)                           
                            // 1 kg de kumquat
                            venta.setFamiliaFin("KUMQUAT");
                            venta.setVariedadFin("KUMQUAT");
                            venta.setKgsNetos(Double.valueOf(1));
                            venta.setId(null);
                            venta.setId(crearVentaRetornaId(venta, true, false));
                        } else if (venta.getFamilia().equals("CROWDFARM 5KG MANGO-AGUACATE OVERHARVEST")) {
                            // Tiene 3 Kgs de Mango (ya creado)                           
                            // 2 kg de kumquat
                            venta.setFamiliaFin("AGUACATE");
                            venta.setVariedadFin("HASS");
                            venta.setKgsNetos(Double.valueOf(2));
                            venta.setId(null);
                            venta.setId(crearVentaRetornaId(venta, true, false));
                        }

                        //nutrirDiccionariosVentas(venta);

                        ok++;
                    }
                } catch (GenasoftException te) {
                    result += result;
                    ko++;
                }

                ordenLinea++;
            }

            result = "Del total de líneas (" + total + ") \n - Correctamente se han importado: " + ok + " registros. \n - Líneas de devoluciones: " + devs + " registros. \n - Líneas KO: " + ko + " registros ";
            textoCorreo += "Del total de lineas (" + total + ") \n - Correctamente se han importado: " + ok + " registros. - Lineas de devoluciones: " + devs + " registros. \n - Líneas KO: " + ko + " registros ";
            //String textoCorreo2 = "..:FICHERO DE VENTAS:.. Del total de lineas (" + total + ") - Correctamente se han importado: " + ok + " registros. - Lineas de devoluciones: " + ko + " registros.";

            // commonSetup.enviaNotificacionCorreo("[GENASOFT] RESULTADO IMPORTACIÓN FICHERO DE VENTAS", textoCorreo, null, user);
            // commonSetup.enviaNotificacionWhatsAppMasivo(textoCorreo2);
            mVentas.clear();

        } catch (FileNotFoundException e) {
            log.info("NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR: ", e);
            textoCorreo += "NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR " + e + "\n";
            e.printStackTrace();
            result = "No se ha podido importar el fichero, no se ha subido correctamente.";
        } catch (IOException ioe) {
            log.info("NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR: ", ioe);
            textoCorreo += "NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR " + ioe + "\n";
            ioe.printStackTrace();
            result = "No se ha podido importar el fichero, no se ha subido correctamente.";
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        // Retornamos el resultado de la importación.
        return result;
    }

    /**
     * Método que nos importa el contenido del fichero pasado por parámetros con contenido de ventas.
     * @param path El path que contiene el fichero.
     * @param nombre para identificar la importación.
     * @return El resultado de la importación.
     */
    public String importarFicheroLotes(String path, String nombre, Integer user, Date f1) throws GenasoftException {

        String result = "";

        Integer total = 0;
        Integer ok = 0;
        Integer devs = 0;
        Integer ko = 0;
        Integer erronea = 0;
        Integer ceros = 0;
        nombreImportacion = nombre;
        // Para leer el fichero.
        BufferedReader br = null;
        FileReader fr = null;
        Date fechaCrea = Utils.generarFecha();

        textoCorreo = "                           RESULTADO IMPORTACIÓN TRAZABILIDADES " + nombre + "\n\n";
        TVentas v = null;
        TDevoluciones d = null;
        TCompras c = null;
        TLineasDevoluciones lDev = null;
        //TLineasVentasHis lHis = null;
        // Empezamos con el primer fichero, el que contiene los productos.

        List<TVentas> lVentas = ventasSetup.obtenerVentasEjercicioFechas(commonSetup.getEjercicio(), f1, null);
        List<TCompras> lCompras = comprasSetup.obtenerComprasEjercicioFechas(commonSetup.getEjercicio(), f1, null);
        List<TDevoluciones> lDevoluciones = tDevolucionesMapper.obtenerTodasDevoluciones();
        Map<Double, TVentas> mVentas = lVentas.stream().collect(Collectors.toMap(TVentas::getIdExterno, Function.identity()));
        Map<String, TCompras> mCompras = lCompras.stream().collect(Collectors.toMap(TCompras::getLoteFin, Function.identity()));

        Map<Double, TDevoluciones> mDevoluciones = lDevoluciones.stream().collect(Collectors.toMap(TDevoluciones::getIdExterno, Function.identity()));
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            String[] datosLinea;
            Integer ordenLinea = -1;

            //inicializarListasVenta();            
            // El primer fichero debe tener 2 campos por línea, separadas por ","
            // Nutrimos nuestra lista de productos.
            // Asumimos que no podemos tener productos con Ids duplicados, ya que en BD esta casuística no se puede dar.
            while ((linea = br.readLine()) != null) {
                v = null;
                d = null;
                c = null;
                // La primera línea es la cabecera del fichero.
                if (ordenLinea.equals(-1)) {
                    ordenLinea++;
                    continue;
                }
                if (linea.contains("\t")) {
                    if (linea.split("\t").length < appLotesFields || linea.split("\t").length == appLotesFields) {
                        linea = linea + " \t";
                    }
                    datosLinea = linea.split("\t");
                } else {
                    if (linea.split(";").length < appLotesFields || linea.split(";").length == appLotesFields) {
                        linea = linea + " ;";
                    }
                    datosLinea = linea.split(";");
                }

                total++;

                if (datosLinea.length < appLotesFields) {
                    log.error("La linea con orden " + ordenLinea + " no tiene el numero de campos correctos, tiene " + linea.split("\t").length + " y el correcto es: " + appLotesFields);
                    textoCorreo += "La linea con orden " + ordenLinea + " no tiene el numero de campos correctos, tiene " + linea.split("\t").length + " y el correcto es: " + appLotesFields + "\n";
                    ko++;
                    ordenLinea++;
                    continue;
                }

                try {

                    lVenta = nutrirTrazabilidadesDatosLinea(datosLinea, ordenLinea, user, fechaCrea);

                    // Si los kgs son 0, no nos complicamos la vida, guardamos en el his y pasamos al siguiente registro.
                    if (lVenta.getKgsTrazadosFin().equals(Double.valueOf(0)) || lVenta.getKgsTrazabilidadFin().equals(Double.valueOf(0))) {
                        if (lVenta.getIdVenta() == null) {
                            lVenta.setIdVenta(-1);
                            lVenta.setVariedadFin("N/D");
                            lVenta.setVariedadIni("N/D");
                            lVenta.setFamiliaFin("N/D");
                            lVenta.setFamiliaIni("N/D");
                        }
                        if (tLineasVentasHisMapper.obtenerLineaVentaHisPorIdExterno(lVenta.getIdExterno()) == null) {
                            tLineasVentasHisMapper.crearLineaVentaHis(lVenta);
                        } else {
                            tLineasVentasHisMapper.modificarLineaVentaHis(lVenta);
                        }
                        ceros++;
                        continue;
                    }
                    c = mCompras.get(lVenta.getAlbaranCompraFin() + "_" + lVenta.getLoteFin());
                    if (c != null) {
                        lVenta.setLoteFin(c.getLoteFin());
                    }

                    // Si no tiene error, comprobamos si hay algún tipo de error con los datos de la compra/venta.
                    if (!Utils.booleanFromInteger(lVenta.getIndError())) {
                        v = mVentas.get(lVenta.getIdVentaExterno());

                        Integer res = validaLineaVentaErrorVenta(lVenta, v);
                        if (!res.equals(0)) {
                            lVenta.setIndError(1);
                            if (res.equals(ERROR_CERTIFICACION_VENTA)) {
                                lVenta.setDetalleError("CERTIFICACIÓN ERRONEA VENTA");
                            } else if (res.equals(ERROR_KGS_VENTA)) {
                                lVenta.setDetalleError("KGS SOBREPASAN VENTA");
                            } else if (res.equals(ERROR_VENTA_NO_EXISTE)) {
                                lVenta.setDetalleError("NO EXISTE VENTA");
                            }
                        } else {
                            // Hacemos lo propio con la compra.
                            res = validaLineaVentaErrorCompra(c);
                            if (!res.equals(0)) {
                                lVenta.setIndError(1);
                                if (res.equals(ERROR_CERTIFICACION_COMPRA)) {
                                    lVenta.setDetalleError("CERTIFICACIÓN ERRONEA COMPRA");
                                } else if (res.equals(ERROR_KGS_COMPRA)) {
                                    lVenta.setDetalleError("KGS SOBREPASAN COMPRA");
                                } else if (res.equals(ERROR_VENTA_NO_EXISTE_COMPRA)) {
                                    lVenta.setDetalleError("NO EXISTE COMPRA");
                                }
                            } else {
                                if (c.getVariedadFin().equals(v.getVariedadFin()) && c.getOrigenFin().equals(v.getOrigenFin())) {
                                    // Restamos los kgs de la compra.
                                    c.setPesoNetoDisponible(Utils.redondeoDecimales(2, c.getPesoNetoDisponible() - lVenta.getKgsTrazabilidadFin()));
                                    lVenta.setIndCerrada(1);
                                    lVenta.setDetalleError("");
                                    if (c.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                        c.setCerrada(1);
                                        // Quiere decir que las ventas están mal, no tenemos en cuenta la última línea de venta (la actual) y sin más.
                                    } else if (c.getPesoNetoDisponible() < Double.valueOf(0)) {
                                        c.setPesoNetoDisponible(Utils.redondeoDecimales(2, c.getPesoNetoDisponible() + lVenta.getKgsTrazabilidadFin()));
                                        lVenta.setIndCerrada(0);
                                        lVenta.setDetalleError("KGS SOBREPASAN COMPRA");
                                    }
                                    comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), c);
                                    commonSetup.mCompras.put("" + c.getId(), c);
                                    TComprasVista cVista = new TComprasVista();
                                    cVista.copiaDesdeCompra(c);
                                    commonSetup.mComprasId.put(cVista.getId(), cVista);
                                } else {
                                    lVenta.setIndError(1);
                                    lVenta.setDetalleError("VARIEDAD ERRONEA");
                                }
                            }
                        }
                    }

                    // Si tiene error quiere decir que la línea es incorrecta en la venta (bien por certificación, variedad, kgs de más, etc.)
                    if (Utils.booleanFromInteger(lVenta.getIndError())) {
                        // 1ª OPCIÓN
                        if (lVenta.getIdVenta() == null) {
                            lVenta.setIdVenta(-1);
                            lVenta.setVariedadFin("N/D");
                            lVenta.setVariedadIni("N/D");
                            lVenta.setFamiliaFin("N/D");
                            lVenta.setFamiliaIni("N/D");
                        }
                        // Si la venta es errónea, no suma a la venta ni resta a la compra, con lo cual, eliminamos el registro y lo metemos en el his.
                        if (tLineasVentasHisMapper.obtenerLineaVentaHisPorIdExterno(lVenta.getIdExterno()) == null) {
                            tLineasVentasHisMapper.crearLineaVentaHis(lVenta);
                        } else {
                            tLineasVentasHisMapper.modificarLineaVentaHis(lVenta);
                        }
                        erronea++;

                        // ESTO LO DEJAMOS POR SI LA PRIMERA SOLUCIÓN SE NOS VA DE MADRE
                        // 2ª OPCIÓN
                        /**
                        // Si existe la compra, la metemos en el diccionario de compras disponibles.
                        if (c != null) {
                            // Si es mango, nos la trae un poco al pairo la certificación
                            if (lVenta.getFamiliaFin().equals("MANGO") || lVenta.getFamiliaFin().equals("KUMQUAT") || lVenta.getFamiliaFin().equals("CHIRIMOYA")) {
                                if (commonSetup.mLineasVentaDisponibles.get(lVenta.getVariedadFin()) == null) {
                                    List<TLineasVentas> lVentasDisp = Utils.generarListaGenerica();
                                    lVentasDisp.add(lVenta);
                                    tLineasVentasDisponibles = new TreeMap<Date, List<TLineasVentas>>();
                                    tLineasVentasDisponibles.put(c.getFecha(), lVentasDisp);
                                    commonSetup.mLineasVentaDisponibles.put(lVenta.getVariedadFin(), tLineasVentasDisponibles);
                                } else {
                                    tLineasVentasDisponibles = commonSetup.mLineasVentaDisponibles.get(lVenta.getVariedadFin());
                                    List<TLineasVentas> lVentasDisp;
                                    if (tLineasVentasDisponibles.get(c.getFecha()) == null) {
                                        lVentasDisp = Utils.generarListaGenerica();
                                        lVentasDisp.add(lVenta);
                                        tLineasVentasDisponibles.put(c.getFecha(), lVentasDisp);
                                    } else {
                                        lVentasDisp = tLineasVentasDisponibles.get(c.getFecha());
                                        lVentasDisp.add(lVenta);
                                    }
                                    tLineasVentasDisponibles.put(c.getFecha(), lVentasDisp);
                                    commonSetup.mLineasVentaDisponibles.put(c.getVariedad(), tLineasVentasDisponibles);
                        
                                    //List<TCompras> lComprasDisp = commonSetup.mComprasDisponibles.get(compra.getVariedadFin());
                                }
                            } else {
                                if (commonSetup.mLineasVentaDisponibles.get(lVenta.getVariedadFin() + "-" + lVenta.getCertificacionFin()) == null) {
                                    List<TLineasVentas> lVentasDisp = Utils.generarListaGenerica();
                                    lVentasDisp.add(lVenta);
                                    tLineasVentasDisponibles = new TreeMap<Date, List<TLineasVentas>>();
                                    tLineasVentasDisponibles.put(c.getFecha(), lVentasDisp);
                                    commonSetup.mLineasVentaDisponibles.put(c.getVariedadFin() + "-" + c.getCalidadFin(), tLineasVentasDisponibles);
                                } else {
                                    tLineasVentasDisponibles = commonSetup.mLineasVentaDisponibles.get(lVenta.getVariedadFin() + "-" + lVenta.getCertificacionFin());
                                    List<TLineasVentas> lVentasDisp;
                                    if (tLineasVentasDisponibles.get(c.getFecha()) == null) {
                                        lVentasDisp = Utils.generarListaGenerica();
                                        lVentasDisp.add(lVenta);
                                        tLineasVentasDisponibles.put(c.getFecha(), lVentasDisp);
                                    } else {
                                        lVentasDisp = tLineasVentasDisponibles.get(c.getFecha());
                                        lVentasDisp.add(lVenta);
                                    }
                                    tLineasVentasDisponibles.put(c.getFecha(), lVentasDisp);
                                    commonSetup.mLineasVentaDisponibles.put(lVenta.getVariedadFin() + "-" + lVenta.getCertificacionFin(), tLineasVentasDisponibles);
                        
                                    //List<TCompras> lComprasDisp = commonSetup.mComprasDisponibles.get(compra.getVariedadFin());
                                }
                            }
                        }
                        */

                    } else {

                        // Antes de crear el registro, tenemos que mirar si la venta de la que depende es una devolución, en ese caso, creamos el registro de t_lineas_devoluciones y miramos si la compra existe
                        // Si existe la compra, tendremos que restar los kgs_fin y disponible, en caso de que queden negativos o 0, se pasa al his.

                        // Si la venta no existe, es una devolución.
                        if (v == null) {
                            d = mDevoluciones.get(lVenta.getIdVentaExterno());
                            if (d != null) {
                                // Al ser una importación, puede ser que sea una línea de venta normal (ya existente)
                                if (lineasVentasSetup.obtenerLineaVentaEjercicioPorIdExterno(commonSetup.getEjercicio(), lVenta.getIdExterno()) != null) {
                                    lDev = new TLineasDevoluciones();
                                    lDev.copiaDesdeLineaVenta(lVenta);
                                    // Miramos si existe la línea de devolución en el sistema, si existe, miramos si los kgs son los mismos, y si el lote es el mismo.
                                    TLineasDevoluciones lAux = tLineasDevolucionesMapper.obtenerLineaDevolucionPorIdExterno(lDev.getIdExterno());
                                    if (lAux != null) {
                                        // Miramos si el lote es el mismo, si es el mismo, miramos si los KGS han variado.
                                        if (lAux.getLoteFin().equals(lDev.getLoteFin())) {
                                            // Si coincide la compra y los kgs, actualizamos el registro y tan amigos.
                                            if (lAux.getKgsTrazabilidadFin().equals(lDev.getKgsTrazabilidadFin())) {
                                                lDev.setId(lAux.getId());
                                                tLineasDevolucionesMapper.updateByPrimaryKey(lDev);
                                            } else {
                                                // Si los kgs que nos vienen de nuevas, son mayores, tenemos que restar los kgs en la compra.
                                                if (lDev.getKgsTrazabilidadFin() > lAux.getKgsTrazabilidadFin()) {
                                                    c = mCompras.get(lDev.getAlbaranCompraFin() + "_" + lDev.getLoteFin());
                                                    if (c != null) {
                                                        Double diferencia = lDev.getKgsTrazabilidadFin() - lAux.getKgsTrazabilidadFin();
                                                        c.setPesoNetoFin(c.getPesoNetoFin() - diferencia);
                                                        c.setPesoNetoDisponible(Utils.redondeoDecimales(2, c.getPesoNetoDisponible() - diferencia));
                                                        // Si se da este escenario, eliminamos la compra.
                                                        if (c.getPesoNetoFin().equals(Double.valueOf(0))) {
                                                            TAbonos ab = new TAbonos();
                                                            ab.copiaDesdeCompra(c);
                                                            crearAbonoRetonaId(ab);
                                                            mCompras.remove(c.getAlbaranFin() + "_" + c.getLoteFin());
                                                        }
                                                        // Si los kgs disponibles son negativos, quiere decir que nos hemos "comido" de más en las ventas, tenemos que abrir las ventas y liberar kgs de la compra.
                                                        if (c.getPesoNetoDisponible() < Double.valueOf(0)) {
                                                            c.setCerrada(0);
                                                            // Reabrimos las ventas asociadas.
                                                            // Buscamos las ventas que están asociadas al lote de compra, y las reabrimos
                                                            List<TVentas> lVentas2 = ventasSetup.obtenerVentasEjercicioLoteFin(commonSetup.getEjercicio(), c.getLoteFin());
                                                            List<Integer> lIdsVentas = Utils.generarListaGenerica();
                                                            TVentasVista vVista = null;
                                                            if (lVentas != null) {
                                                                for (TVentas v2 : lVentas2) {
                                                                    if (Utils.booleanFromInteger(v2.getCerrada())) {
                                                                        vVista = new TVentasVista();
                                                                        vVista.copiaDesdeVenta(v2);
                                                                        // Metemos las ventas como erróneas
                                                                        commonSetup.mVentasId.put(vVista.getId(), vVista);
                                                                        if (!lIdsVentas.contains(Integer.valueOf(v2.getId()))) {
                                                                            lIdsVentas.add(v2.getId());
                                                                        }
                                                                    }
                                                                }
                                                                if (!lIdsVentas.isEmpty()) {
                                                                    ventasSetup.abrirVentasEjercicioIdsVentas(commonSetup.getEjercicio(), lIdsVentas);
                                                                    // Eliminamos las líneas con id de venta asociada a las ventas que hemos abierto.
                                                                    lineasVentasSetup.eliminarLineasVentasEjercicioIdsVentas(commonSetup.getEjercicio(), lIdsVentas);
                                                                }
                                                            }
                                                        }

                                                        comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), c);
                                                        commonSetup.mCompras.put("" + c.getId(), c);
                                                        TComprasVista cVista = new TComprasVista();
                                                        cVista.copiaDesdeCompra(c);
                                                        commonSetup.mComprasId.put(cVista.getId(), cVista);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    crearLineaDevolucionRetornaId(lDev);
                                    textoCorreo += "Se crea una devolución ya que los kgs son negativos o cero en la linea con orden " + ordenLinea + "\n";
                                    devs++;
                                    continue;
                                } else {
                                    lDev = new TLineasDevoluciones();
                                    lDev.copiaDesdeLineaVenta(lVenta);
                                    crearLineaDevolucionRetornaId(lDev);
                                    textoCorreo += "Se crea una devolución ya que los kgs son negativos o cero en la linea con orden " + ordenLinea + "\n";
                                    devs++;
                                    continue;
                                }
                            }
                        }

                        if (d == null) {
                            if (v != null) {
                                lVenta.setIdVenta(v.getId());
                                lVenta.setVariedadFin(v.getVariedadFin());
                                lVenta.setVariedadIni(v.getVariedadFin());
                                lVenta.setFamiliaFin(v.getFamiliaFin());
                                lVenta.setFamiliaIni(v.getFamiliaFin());
                                if (c != null) {
                                    lVenta.setLoteFin(c.getPartidaFin());
                                }

                            } else {
                                lVenta.setIdVenta(-1);
                            }
                            v.setKgsNetosFin(Utils.redondeoDecimales(2, v.getKgsNetosFin() + lVenta.getKgsTrazabilidadFin()));
                            v.setLoteFin(v.getLoteFin() + ", " + lVenta.getLoteFin());
                            v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                            if (v.getKgsNetosFin() > v.getKgsNetos()) {
                                v.setIndError(1);
                                if (Utils.booleanFromInteger(v.getCerrada())) {
                                    v.setCerrada(0);
                                    v.setGgn(null);
                                }
                                commonSetup.mIdVentasErroneas.put(v.getId(), v);
                            } else if (v.getKgsNetosFin().equals(v.getKgsNetos())) {
                                v.setCerrada(1);
                                v.setGgn(c.getGgnFin());
                                v.setIndError(0);
                                v.setGgn(c.getGgnFin());
                                commonSetup.mIdVentasErroneas.remove(v.getId());
                            } else {
                                if (!Utils.booleanFromInteger(v.getCerrada())) {
                                    commonSetup.mIdVentasErroneas.put(v.getId(), v);
                                }
                            }
                            guardarVentaAPelo(v);
                            lVenta.setId(crearTrazabilidadVentaRetornaId(lVenta, true, false));
                            if (lVenta.getId() < 0) {
                                log.error("La linea con orden " + ordenLinea + " no se ha podido crear, el ID es: " + lVenta.getId());
                                textoCorreo += "La linea con orden " + ordenLinea + " no se ha podido crear, el ID es: " + lVenta.getId() + "\n";
                                ko++;
                                ordenLinea++;
                                continue;
                            }

                            // Tenga o no tenga error la línea, la metemos en el diccionario que compone la venta
                            if (commonSetup.mIdLineasVentas.get(lVenta.getIdVentaExterno()) == null) {
                                List<TLineasVentas> lAux = Utils.generarListaGenerica();
                                lAux.add(lVenta);
                                commonSetup.mIdLineasVentas.put(lVenta.getIdVentaExterno(), lAux);
                            } else {
                                List<TLineasVentas> lAux = commonSetup.mIdLineasVentas.get(lVenta.getIdVentaExterno());
                                lAux.add(lVenta);
                                commonSetup.mIdLineasVentas.put(lVenta.getIdVentaExterno(), lAux);
                            }

                            // ESTA LOGICA SOLO APLICA SI ESTÁ LA OPCIÓN 2 DESCOMENTADA, YA QUE SI HAY ERROR SE METE EN EL HIS DEL TIRÓN.
                            /**
                            // Si tiene error, la metemos en el diccionario de errores.
                            if (Utils.booleanFromInteger(lVenta.getIndError())) {
                                if (v != null) {
                                    // Si tiene error, la metemos en el diccionario de errores.
                                    if (commonSetup.mIdLineasVentasErroneas.get(lVenta.getIdVentaExterno()) == null) {
                                        List<TLineasVentas> lAux = Utils.generarListaGenerica();
                                        lAux.add(lVenta);
                                        commonSetup.mIdLineasVentasErroneas.put(lVenta.getIdVentaExterno(), lAux);
                                    } else {
                                        List<TLineasVentas> lAux = commonSetup.mIdLineasVentasErroneas.get(lVenta.getIdVentaExterno());
                                        lAux.add(lVenta);
                                        commonSetup.mIdLineasVentasErroneas.put(lVenta.getIdVentaExterno(), lAux);
                                    }
                                    // Comprobamos si la venta está marcada con error de otra línea o la misma venta, si no tiene error, la marcamos.
                                    if (!Utils.booleanFromInteger(v.getIndError())) {
                                        v.setIndError(1);
                                        if (Utils.booleanFromInteger(v.getCerrada())) {
                                            v.setCerrada(0);
                                        }
                                        commonSetup.mIdVentasErroneas.put(v.getId(), v);
                                        guardarVentaAPelo(v);
                                    }
                                }
                            } else {
                                // Si no tiene error, sumamos los kgs de la línea venta.
                                v.setKgsNetosFin(Utils.redondeoDecimales(2, v.getKgsNetosFin() + lVenta.getKgsTrazabilidadFin()));
                                if (v.getKgsNetosFin() > v.getKgsNetos()) {
                                    v.setIndError(1);
                                    if (Utils.booleanFromInteger(v.getCerrada())) {
                                        v.setCerrada(0);
                                    }
                                    commonSetup.mIdVentasErroneas.put(v.getId(), v);
                                    guardarVentaAPelo(v);
                                } else if (v.getKgsNetosFin().equals(v.getKgsNetos())) {
                                    v.setCerrada(1);
                                    v.setIndError(0);
                                    commonSetup.mIdVentasErroneas.remove(v.getId());
                                    guardarVentaAPelo(v);
                                } else {
                                    if (!Utils.booleanFromInteger(v.getCerrada())) {
                                        commonSetup.mIdVentasErroneas.put(v.getId(), v);
                                        guardarVentaAPelo(v);
                                    }
                                }
                            }
                            */
                        }
                        ok++;
                    }

                } catch (GenasoftException te) {
                    result += result;
                    ko++;
                }

                ordenLinea++;
            }

            result = "Del total de lineas (" + total + ") \n - Correctamente se han importado: " + ok + " registros. - Lineas devoluciones: " + devs + " registros. \n - Líneas con Kgs 0: " + ceros + "\n - Líneas erróneas: " + erronea
                    + " registros - Líneas KO: " + ko + " registros ";
            textoCorreo += "Del total de lineas (" + total + ") \n - Correctamente se han importado: " + ok + " registros. - Lineas devoluciones: " + devs + " registros. \n - Líneas con Kgs 0: " + ceros + "\n - Líneas erróneas: " + erronea
                    + " registros - Líneas KO: " + ko + " registros ";
            //String textoCorreo2 = "..:FICHERO DE VENTAS:.. Del total de lineas (" + total + ") - Correctamente se han importado: " + ok + " registros. - Lineas de devoluciones: " + ko + " registros.";

            // commonSetup.enviaNotificacionCorreo("[GENASOFT] RESULTADO IMPORTACIÓN FICHERO DE VENTAS", textoCorreo, null, user);
            // commonSetup.enviaNotificacionWhatsAppMasivo(textoCorreo2);
            mVentas.clear();

        } catch (FileNotFoundException e) {
            log.info("NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR: ", e);
            textoCorreo += "NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR " + e + "\n";
            e.printStackTrace();
            result = "No se ha podido importar el fichero, no se ha subido correctamente.";
        } catch (IOException ioe) {
            log.info("NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR: ", ioe);
            textoCorreo += "NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR " + ioe + "\n";
            ioe.printStackTrace();
            result = "No se ha podido importar el fichero, no se ha subido correctamente.";
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        // Retornamos el resultado de la importación.
        return result;

    }

    /**
     * Método que nos importa el contenido del fichero pasado por parámetros con contenido de ventas.
     * @param path El path que contiene el fichero.
     * @param nombre para identificar la importación.
     * @return El resultado de la importación.
     * @throws GenasoftException 
     */
    public String importarFicheroMovimientosAlmacen(String path, String nombre, Integer user, Date fecha1) throws GenasoftException {

        String result = "";

        Integer total = 0;
        Integer ok = 0;
        Integer ko = 0;
        Integer noExiste = 0;
        nombreImportacion = nombre;
        // Para leer el fichero.
        BufferedReader br = null;
        FileReader fr = null;
        Date fechaCrea = Utils.generarFecha();

        textoCorreo = "                           RESULTADO IMPORTACIÓN MOVIMIENTOS ALMACÉN " + nombre + "\n\n";
        TVentas v = null;

        TMovimientosAlmacen mov = null;
        // Cargamos las ventas de 2 meses atrás.
        // Empezamos con el primer fichero, el que contiene los productos.        
        List<TVentas> lVentas = ventasSetup.obtenerVentasEjercicioFechas(commonSetup.getEjercicio(), fecha1, null);
        Map<Double, TVentas> mVentas = lVentas.stream().collect(Collectors.toMap(TVentas::getIdExterno, Function.identity()));
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            String[] datosLinea;
            Integer ordenLinea = -1;
            Boolean entra = false;
            //inicializarListasVenta();            
            // El primer fichero debe tener 2 campos por línea, separadas por ","
            // Nutrimos nuestra lista de productos.
            // Asumimos que no podemos tener productos con Ids duplicados, ya que en BD esta casuística no se puede dar.
            while ((linea = br.readLine()) != null) {
                v = null;
                // La primera línea es la cabecera del fichero.
                if (ordenLinea.equals(-1)) {
                    ordenLinea++;
                    continue;
                }
                if (linea.contains("\t")) {
                    if (linea.split("\t").length < appMovAlmFields || linea.split("\t").length == appMovAlmFields) {
                        linea = linea + " \t";
                    }
                    datosLinea = linea.split("\t");
                } else {
                    if (linea.split(";").length < appMovAlmFields || linea.split(";").length == appMovAlmFields) {
                        linea = linea + " ;";
                    }
                    datosLinea = linea.split(";");
                }

                total++;

                if (datosLinea.length < appMovAlmFields) {
                    log.error("La linea con orden " + ordenLinea + " no tiene el numero de campos correctos, tiene " + linea.split("\t").length + " y el correcto es: " + appMovAlmFields);
                    textoCorreo += "La linea con orden " + ordenLinea + " no tiene el numero de campos correctos, tiene " + linea.split("\t").length + " y el correcto es: " + appMovAlmFields + "\n";
                    ko++;
                    ordenLinea++;
                    continue;
                }

                try {
                    mov = nutrirMovimirntoAlmacenDatosLinea(datosLinea, ordenLinea, user, fechaCrea);

                    v = mVentas.get(mov.getIdMovimeintoVenta());
                    if (v != null) {
                        mov.setIdVenta(v.getId());
                        if (v.getIdPale() == null) {
                            v.setIdPale("" + mov.getIdPalet().intValue());
                            v.setIdPaleFin("" + mov.getIdPalet().intValue() + ",");
                        } else {
                            String aux = v.getIdPaleFin();
                            String[] values = aux.split(",");
                            int cnt = 0;
                            entra = false;
                            while (cnt < values.length) {
                                if (values[cnt].equals("" + mov.getIdPalet().intValue())) {
                                    entra = true;
                                }
                                cnt++;
                            }
                            if (!entra) {
                                v.setIdPaleFin(v.getIdPaleFin().concat("" + mov.getIdPalet().intValue()).concat(","));
                            }
                        }
                        v.setIdPaleFin("" + mov.getIdPalet().intValue());
                        ventasSetup.modificarVentaEjercicio(commonSetup.getEjercicio(), v);
                        if (tMovimientosAlmacenMapper.obtenerMovimientoAlmacenPorId(mov.getId()) == null) {
                            tMovimientosAlmacenMapper.insert(mov);
                        } else {
                            tMovimientosAlmacenMapper.updateByPrimaryKey(mov);
                        }
                        ok++;
                    } else {
                        noExiste++;
                    }
                } catch (GenasoftException e) {
                    ko++;
                }
            }

        } catch (FileNotFoundException e) {
            log.info("NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR: ", e);
            textoCorreo += "NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR " + e + "\n";
            e.printStackTrace();
            result = "No se ha podido importar el fichero, no se ha subido correctamente.";
        } catch (IOException ioe) {
            log.info("NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR: ", ioe);
            textoCorreo += "NO SE HA PODIDO IMPORTAR EL FICHERO, MOTIVO ERROR " + ioe + "\n";
            ioe.printStackTrace();
            result = "No se ha podido importar el fichero, no se ha subido correctamente.";
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        result = "Del total de líneas (" + total + ") \n - Correctamente se han importado: " + ok + " registros. \n - Ventas que no existen: " + noExiste + " registros \n  - Líneas KO: " + ko + " registros ";

        // Retornamos el resultado de la importación.
        return result;

    }

    /**
     * Método que nos guarda la compra pasada por parámetro.
     * @param compra La compra a guardar en el sistema.
     * @return 1 si se ha guardado correctamente, 0 si no se ha podido guardar.
     * @throws GenasoftException 
     */
    public Integer guardarCompraEjercicio(Integer ejercicio, TCompras compra) throws GenasoftException {
        Integer result = 0;

        TCompras aux = comprasSetup.obtenerCompraPorAlbaranLoteEjercicio(ejercicio, compra.getAlbaran(), compra.getLote());

        if (aux != null) {
            result = comprasSetup.modificarCompraEjercicio(ejercicio, compra);
            if (result.equals(1)) {
                result = 2;
            }
        } else {
            result = comprasSetup.crearCompraEjercicio(ejercicio, compra);
            result = 1;
        }

        return result;
    }

    /**
     * Método que nos guarda la venta pasada por parámetro.
     * @param venta La venta a guardar en el sistema.
     * @return 1 si se ha guardado correctamente, 0 si no se ha podido guardar.
     */
    public Integer guardarVenta(TVentas venta) {
        Integer result = 0;

        try {
            TVentas aux = ventasSetup.obtenerVentaEjercicioNumPedidoAlbaranLoteIdPale(commonSetup.getEjercicio(), venta.getPedido(), venta.getAlbaran(), venta.getLote(), venta.getLoteFin(), venta.getIdPaleFin());

            if (aux != null) {
                TVentas aux2 = ventasSetup.obtenerVentaEjercicioNumPedidoAlbaranLoteIdPale(commonSetup.getEjercicio(), venta.getPedidoFin(), venta.getAlbaranFin(), venta.getLote(), venta.getLoteFin(), venta.getIdPaleFin());
                if (aux2 != null) {
                    if (!aux.getId().equals(aux2.getId())) {
                        // Sumamos los kgs, y eliminamos el registro, ya que sino duplicará kgs.
                        venta.setKgsFin(aux.getKgsFin() + aux2.getKgsFin());
                        result = ventasSetup.modificarVentaEjercicio(commonSetup.getEjercicio(), venta);
                        // Eliminamos el registro antiguo
                        ventasSetup.eliminarVentaEjercicio(commonSetup.getEjercicio(), aux2.getId());
                    } else {
                        result = ventasSetup.modificarVentaEjercicio(commonSetup.getEjercicio(), venta);
                    }
                } else {
                    result = ventasSetup.modificarVentaEjercicio(commonSetup.getEjercicio(), venta);
                }
                if (result.equals(1)) {
                    result = 2;
                }
            } else {
                result = ventasSetup.crearVentaEjercicio(commonSetup.getEjercicio(), venta);
            }
        } catch (Exception e) {

        }

        return result;
    }

    private Integer guardarVentaAPelo(TVentas venta) {
        Integer result = 0;

        try {
            TVentas aux = ventasSetup.obtenerVentaEjercicioPorId(commonSetup.getEjercicio(), venta.getId());

            if (aux != null) {

                result = ventasSetup.modificarVentaEjercicio(commonSetup.getEjercicio(), venta);

                if (result.equals(1)) {
                    result = 2;
                }
            } else {
                result = ventasSetup.crearVentaEjercicio(commonSetup.getEjercicio(), venta);
            }
        } catch (Exception e) {

        }

        return result;
    }

    /**
     * Método que nos elimina el registro de la compra pasada por parámetro
     * @param compra El registro de compra a eliminar
     * @return 1 Si se elimina el registro, 0 si no se elimina.
     * @throws GenasoftException 
     */
    public void eliminarCompra(TCompras compra, Integer userId) throws GenasoftException {

        TComprasHis cHis = new TComprasHis();
        cHis.copiaDesdeCompra(compra);
        cHis.setUsuCrea(userId);
        cHis.setFechaCrea(Utils.generarFecha());

        // Primero guardamos el registro en el his, por lo que pueda pasar.....
        if (tComprasHisMapper.insert(cHis) == 1) {
            comprasSetup.eliminarCompraEjercicio(commonSetup.getEjercicio(), compra.getId());
        }
    }

    /**
     * Método que nos elimina el registro de la compra pasada por parámetro
     * @param compra El registro de compra a eliminar
     * @return 1 Si se elimina el registro, 0 si no se elimina.
     * @throws GenasoftException 
     */
    public void eliminarCompraVista(TComprasVista cVista, Integer userId) throws GenasoftException {

        TCompras compra = new TCompras();
        compra.copiaDesdeCompraVista(cVista);
        compra.setId(Integer.valueOf(cVista.getId()));
        TComprasHis cHis = new TComprasHis();
        cHis.copiaDesdeCompra(compra);
        cHis.setUsuCrea(userId);
        cHis.setFechaCrea(Utils.generarFecha());

        // Primero guardamos el registro en el his, por lo que pueda pasar.....
        if (tComprasHisMapper.insert(cHis) == 1) {
            comprasSetup.eliminarCompraEjercicio(commonSetup.getEjercicio(), compra.getId());
        }
    }

    /**
     * Método que nos elimina el registro de la venta pasada por parámetro
     * @param venta El registro de venta a eliminar
     * @return 1 Si se elimina el registro, 0 si no se elimina.
     * @throws GenasoftException 
     */
    public void eliminarVenta(TVentas venta, Integer userId) throws GenasoftException {

        TVentasHis vHis = new TVentasHis();
        vHis.copiaDesdeVenta(venta);
        vHis.setUsuCrea(userId);
        vHis.setFechaCrea(Utils.generarFecha());

        // Primero guardamos el registro en el his, por lo que pueda pasar.....
        if (tVentasHisMapper.insert(vHis) == 1) {
            ventasSetup.eliminarVentaEjercicio(commonSetup.getEjercicio(), venta.getId());
        }
    }

    /**
     * Método que nos elimina el registro de la venta pasada por parámetro
     * @param venta El registro de venta a eliminar
     * @return 1 Si se elimina el registro, 0 si no se elimina.
     * @throws GenasoftException 
     */
    public void eliminarVentaVista(TVentasVista vVista, Integer userId) throws GenasoftException {

        TVentas venta = new TVentas();
        venta.copiaDesdeVentaVista(vVista);
        venta.setId(Integer.valueOf(vVista.getId()));
        TVentasHis vHis = new TVentasHis();
        vHis.copiaDesdeVenta(venta);
        vHis.setUsuCrea(userId);
        vHis.setFechaCrea(Utils.generarFecha());

        // Primero guardamos el registro en el his, por lo que pueda pasar.....
        if (tVentasHisMapper.insert(vHis) == 1) {
            ventasSetup.eliminarVentaEjercicio(commonSetup.getEjercicio(), venta.getId());
        }
    }

    /**
     * Método que nos elimina las ventas con los ids pasado por parámetro
     * @param venta El registro de venta a eliminar
     * @return 1 Si se elimina el registro, 0 si no se elimina.
     * @throws GenasoftException 
     */
    public void eliminarVentasIds(List<Integer> lIdsVentas) throws GenasoftException {
        if (!lIdsVentas.isEmpty()) {
            // En primer lugar guardamos las ventas en t_ventas_his por si acaso....
            if (tVentasHisMapper.crearVentasHisVentasIds(lIdsVentas) > 0) {
                // Eliminamos las ventas pasadas por parámetro.
                ventasSetup.eliminarVentasEjercicioIdsVentas(commonSetup.getEjercicio(), lIdsVentas);
            }
        }
    }

    /**
     * Método que nos elimina las compras con los ids pasado por parámetro
     * @param venta El registro de venta a eliminar
     * @return 1 Si se elimina el registro, 0 si no se elimina.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public void eliminarComprasIds(List<Integer> lIdsCompras) throws GenasoftException {
        if (!lIdsCompras.isEmpty()) {
            // En primer lugar guardamos las ventas en t_ventas_his por si acaso....
            if (tComprasHisMapper.crearComprasHisComprasIds(lIdsCompras) > 0) {
                // Eliminamos las ventas pasadas por parámetro.
                comprasSetup.eliminarComprasEjercicioIds(commonSetup.getEjercicio(), lIdsCompras);
            }
        }
    }

    /**
     * Método que nos nutre el campo TCOMPRAS a partir del contenido del objeto pasado por parámetro.
     * @param datosLinea El objeto con los datos a nutrir.
     * @return El objeto TCompras con el contenido
     * @throws GenasoftException 
     */
    private TCompras nutrirCompraDatosLinea(String[] datosLinea, Integer ordenLinea, Integer user, Date fechaCrea) throws GenasoftException {
        TCompras compra = new TCompras();
        TProveedores prov = null;
        String campo = null;

        compra.setAlbaran(datosLinea[POS_ALBARAN_C].trim());
        compra.setAlbaranFin(compra.getAlbaran());
        try {
            if (datosLinea[POS_CAJAS_C] == null) {
                compra.setCajas(0);
            } else {
                compra.setCajas(Integer.valueOf(datosLinea[POS_CAJAS_C].trim()));
            }
        } catch (NumberFormatException nfe) {
            compra.setCajas(0);
            log.debug("El campo CAJAS de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ");
            textoCorreo += "El campo CAJAS de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO" + ordenLinea + "\n";
            //throw new TrazabilidadesException("El campo CAJAS de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE OMITE LA CARGA DE la linea \n ");
        }
        compra.setCajasFin(compra.getCajas());
        compra.setCalidad(datosLinea[POS_CALIDAD__C].trim());
        if (compra.getCalidad().toLowerCase().trim().equals("b")) {
            compra.setCalidad("BIO");
        } else if (compra.getCalidad().toLowerCase().trim().equals("c")) {
            compra.setCalidad("CONVENCIONAL");
        }
        compra.setCalidadFin(compra.getCalidad());
        compra.setCentro(datosLinea[POS_CENTRO_C].trim());
        compra.setCentroFin(compra.getCentro());

        campo = datosLinea[POS_FAMILIA_ART_C].trim();

        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ó");
            }
            if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
            if (campo.contains("”")) {
                campo = campo.replaceAll("”", "Ó");
            }
        }
        compra.setFamilia(campo.trim());
        if (compra.getFamilia() != null) {
            if (compra.getFamilia().toUpperCase().equals("NÓSPERO")) {
                compra.setFamilia("NÍSPERO");
            } else if (compra.getFamilia().toUpperCase().equals("N?SPERO")) {
                compra.setFamilia("NÍSPERO");
            } else if (compra.getFamilia().toUpperCase().equals("NISPERO")) {
                compra.setFamilia("NÍSPERO");
            }
        }
        if (compra.getFamilia().contains("MANGO")) {
            compra.setFamiliaFin("MANGO");
        } else {
            compra.setFamiliaFin(compra.getFamilia());
        }

        if (compra.getFamilia().contains("MARAC")) {
            compra.setFamiliaFin("MARACUYA");
        }

        try {

            compra.setFecha(new SimpleDateFormat("dd/MM/yyyy").parse(datosLinea[POS_FECHA_C].trim()));
        } catch (ParseException pe) {
            compra.setFecha(null);
            log.debug("El campo FECHA de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE DEJA EL CAMPO CON VALOR NULO \n ");
            textoCorreo += "El campo FECHA de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE DEJA EL CAMPO CON VALOR NULO \n ";
            //throw new TrazabilidadesException("El campo FECHA de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE OMITE LA CARGA DE la linea \n ");
        }
        compra.setFechaFin(compra.getFecha());
        try {
            compra.setFechaCalibrado(new SimpleDateFormat("dd/MM/YY").parse(datosLinea[POS_FECHA_CALIB_C].trim()));
        } catch (ParseException pe) {
            compra.setFechaCalibrado(null);

        }
        compra.setFechaCalibradoFin(compra.getFechaCalibrado());
        compra.setFechaCrea(fechaCrea);

        campo = datosLinea[POS_FINCA_C].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        compra.setFinca(campo);
        compra.setFincaFin(compra.getFinca());
        try {
            campo = datosLinea[POS_PESO_B_C].trim();
            if (campo.contains(".")) {
                campo = campo.replaceAll("\\.", ",");
            }
            compra.setKgsBruto(Utils.formatearValorDouble(campo));
        } catch (NumberFormatException nfe) {
            compra.setKgsBruto(Double.valueOf(0));
            //throw new TrazabilidadesException("El campo PESO BRUTO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE OMITE LA CARGA DE la linea \n ");            
            log.debug("El campo PESO BRUTO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ");
            textoCorreo += "El campo PESO BRUTO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ";
        }
        compra.setKgsBrutoFin(compra.getKgsBruto());
        compra.setLote(datosLinea[POS_TRAZ_C].trim());
        if (compra.getLote().trim().isEmpty()) {
            compra.setLote(SIN_LOTE);
        }

        compra.setNotasCalidad(datosLinea[POS_AVISO_CAL_C].trim());
        compra.setNotasCalidadFin(compra.getNotasCalidad());
        campo = datosLinea[POS_ORIGEN_C].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        compra.setOrigen(campo);
        compra.setOrigenFin(compra.getOrigen());
        compra.setPartida(datosLinea[POS_PARTIDA_C].trim());
        compra.setPartidaFin(compra.getPartida());
        campo = datosLinea[POS_PRODUCTO_C];
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        compra.setProducto(campo);
        compra.setProductoFin(compra.getProducto());

        campo = datosLinea[POS_PESO_N_C].trim();
        if (campo == null || campo.trim().isEmpty()) {
            compra.setPesoNeto(Double.valueOf(0));
        } else {
            try {
                if (campo.contains(".")) {
                    campo = campo.replaceAll("\\.", ",");
                }
                compra.setPesoNeto(Utils.formatearValorDouble(campo.trim()));
            } catch (NumberFormatException nfe) {
                compra.setPesoNeto(Double.valueOf(0));
                log.debug("Los Kgs netos de la linea: " + ordenLinea + " no tiene el valor correcto: " + campo + ", se establece el valor CERO");
                textoCorreo += "Los Kgs netos de la linea: " + ordenLinea + " no tiene el valor correcto: " + campo + ", se establece el valor CERO \n ";
            }
        }
        compra.setPesoNetoFin(compra.getPesoNeto());

        campo = datosLinea[POS_PROVEEDOR_C].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        if (campo.trim().toUpperCase().equals("GONZÑLEZ CALDERÑN, FRANCISCO MIGUEL")) {
            campo = "GONZÁLEZ CALDERÓN, FRANCISCO MIGUEL";
        } else if (campo.trim().toUpperCase().equals("DIAZ TAMAYO, PLACIDO JOAQUÑN")) {
            campo = "DIAZ TAMAYO, PLACIDO JOAQUÍN";
        } else if (campo.trim().toUpperCase().equals("RODRIGUEZ CENTURION, MÑ CARMEN")) {
            campo = "RODRIGUEZ CENTURION, Mª CARMEN";
        } else if (campo.trim().toUpperCase().equals("RAMÑREZ TELLO, MARÑA ÑNGELES")) {
            campo = "RAMÍREZ TELLO, MARÍA ÁNGELES";
        } else if (campo.trim().toUpperCase().equals("GARCIA ARAGÑN, RAÑL")) {
            campo = "GARCIA ARAGÓN, RAÚL";
        } else if (campo.trim().toUpperCase().equals("AGUILERA GONZÑLEZ, FRANCISCO")) {
            campo = "AGUILERA GONZÁLEZ, FRANCISCO";
        } else if (campo.trim().toUpperCase().equals("RECIO MARTÑN, AMPARO")) {
            campo = "RECIO MARTÍN, AMPARO";
        } else if (campo.trim().toUpperCase().equals("SERÑN GARCIA, JOSE")) {
            campo = "SERÓN GARCIA, JOSE";
        } else if (campo.trim().toUpperCase().equals("ALGARÑN GUTIERREZ, MARIA ISABEL")) {
            campo = "ALGARÍN GUTIERREZ, MARIA ISABEL";
        } else if (campo.trim().toUpperCase().equals("ORTEGA MARTÑN, MANUEL DAVID")) {
            campo = "ORTEGA MARTÍN, MANUEL DAVID";
        } else if (campo.trim().toUpperCase().equals("NT Ñ AGRICOLA ZAYAS")) {
            campo = "NT - AGRICOLA ZAYAS";
        } else if (campo.trim().toUpperCase().equals("NT Ñ VILLOSLADA")) {
            campo = "NT - VILLOSLADA";
        } else if (campo.trim().toUpperCase().equals("NT Ñ GISELA HOENER")) {
            campo = "NT - GISELA HOENER";
        } else if (campo.trim().toUpperCase().equals("NT Ñ AYALA ESPAÑA")) {
            campo = "NT - AYALA ESPAÑA";
        } else if (campo.trim().toUpperCase().equals("ALBA MARTÑN, MIGUEL")) {
            campo = "ALBA MUÑOZ, RAFAEL";
        } else if (campo.trim().toUpperCase().equals("ARAGÑEZ DELGADO, RAFAEL")) {
            campo = "ARAGÜEZ DELGADO, RAFAEL";
        } else if (campo.trim().toUpperCase().equals("ARROYO ALARCÑN, MARIA EMILIA")) {
            campo = "ARROYO ALARCÓN, MARIA EMILIA";
        } else if (campo.trim().toUpperCase().equals("CEBRERO RUIZ, FRANCISCO EUSEB")) {
            campo = "CEBRERO RUIZ, FRANCISCO EUSEBIO";
        } else if (campo.trim().toUpperCase().equals("DE LA MONJA KÑHNE, JESUS")) {
            campo = "DE LA MONJA KÜHNE, JESUS";
        } else if (campo.trim().toUpperCase().equals("FORTES  GÑMEZ, CRISTINA")) {
            campo = "FORTES  GÓMEZ, CRISTINA";
        } else if (campo.trim().toUpperCase().equals("FRUTÑCOLA DO VALE DO SAO FRANCISCO UNIPESSOAL, LDA")) {
            campo = "FRUTÍCOLA DO VALE DO SAO FRANCISCO UNIPESSOAL, LDA";
        } else if (campo.trim().toUpperCase().equals("GARCIA PEÑA, ALADINO JOSÑ")) {
            campo = "GARCIA PEÑA, ALADINO JOSÉ";
        } else if (campo.trim().toUpperCase().equals("GARCIOLO BARBERO, MARÑA CARMEN")) {
            campo = "GARCIOLO BARBERO, MARÍA CARMEN";
        } else if (campo.trim().toUpperCase().equals("GONZALEZ CALDERON, FRANCISCO")) {
            campo = "GONZÁLEZ CALDERÓN, FRANCISCO MIGUEL";
        } else if (campo.trim().toUpperCase().equals("MANAGÑ")) {
            campo = "MANAGÚ";
        } else if (campo.trim().toUpperCase().equals("MARFIL FERNÑNDEZ, MARIA DEL CARMEN")) {
            campo = "MARFIL FERNÁNDEZ, MARIA DEL CARMEN";
        } else if (campo.trim().toUpperCase().equals("MARIN HIDALGO, ADORACIÑN")) {
            campo = "MARIN HIDALGO, ADORACIÓN";
        } else if (campo.trim().toUpperCase().equals("MARTÑN TORRES, JUAN CARLOS")) {
            campo = "MARTIN TORRES, JUAN CARLOS";
        } else if (campo.trim().toUpperCase().equals("MELÑNDEZ ARIAS, RAFAEL")) {
            campo = "MELÉNDEZ ARIAS, RAFAEL";
        } else if (campo.trim().toUpperCase().equals("PLANTACIONES ECOLOGICAS PABÑN S.L.")) {
            campo = "PLANTACIONES ECOLOGICAS PABÓN S.L.";
        } else if (campo.trim().toUpperCase().equals("RECIO MARTIN, FRANCISCO JESÑS")) {
            campo = "RECIO MARTIN, FRANCISCO JESÚS";
        } else if (campo.trim().toUpperCase().equals("RUIZ MARÑN, FRANCISCO")) {
            campo = "RUIZ MARÍN, FRANCISCO";
        } else if (campo.trim().toUpperCase().equals("SANCHEZ CENTURIÑN, MARIA LUISA")) {
            campo = "SANCHEZ CENTURIÓN, MARIA LUISA";
        } else if (campo.trim().toUpperCase().equals("SÑNCHEZ GARCÑA, MÑ JOSEFA")) {
            campo = "SÁNCHEZ GARCÍA, Mª JOSEFA";
        } else if (campo.trim().toUpperCase().equals("TOME RICO, JOSE EUSEB")) {
            campo = "TOME RICO, JOSE EUSEBIO";
        } else if (campo.trim().toUpperCase().equals("VALDERRAMA MÑRQUEZ, JOSÑ")) {
            campo = "VALDERRAMA MÁRQUEZ, JOSÉ";
        }

        if (!campo.isEmpty()) {
            prov = proveedoresSetup.obtenerProveedorPorNombre(campo.trim().toUpperCase());
            if (prov == null) {
                prov = new TProveedores();
                prov.setDescripcion(campo.trim().toUpperCase());
                prov.setGgn("NO GG");
                prov.setEstado(1);
                proveedoresSetup.crearProveedor(prov);
            }
        }

        compra.setProveedor(campo.trim().toUpperCase());
        compra.setProveedorFin(compra.getProveedor());
        if (prov != null) {
            compra.setGgn(prov.getGgn());
        } else {
            compra.setGgn("NO GG");
        }
        compra.setGgnFin(compra.getGgn());
        compra.setTara(compra.getKgsBruto() - compra.getPesoNeto());
        compra.setTaraFin(compra.getTara());
        compra.setTipoCompra(datosLinea[POS_TIPO_COMPRA_C].trim());
        compra.setTipoCompraFin(compra.getTipoCompra());
        compra.setUsuCrea(user);
        campo = datosLinea[POS_VARIEDAD__C].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }
        compra.setVariedad(campo);
        compra.setVariedadFin(compra.getVariedad());
        compra.setNombreDescriptivo(nombreImportacion);
        compra.setPesoNetoDisponible(compra.getPesoNetoFin());

        compra.setCalibre(datosLinea[POS_CALIBRE__C].trim());
        if (compra.getCalibre().isEmpty()) {
            compra.setCalibre("VARIOS");
        }
        compra.setCalibreFin(compra.getCalibre());
        compra.setIdExterno(Double.valueOf(datosLinea[POS_ID_EXTERNO_C]));

        compra.setLoteFin(compra.getPartidaFin());

        return compra;
    }

    /**
     * Método que nos nutre el campo TVENTAS a partir del contenido del objeto pasado por parámetro.
     * @param datosLinea El objeto con los datos a nutrir.
     * @return El objeto TVentas con el contenido
     * @throws GenasoftException 
     */
    private TVentas nutrirVentaDatosLinea(String[] datosLinea, Integer ordenLinea, Integer user, Date fechaCrea) throws GenasoftException {
        TVentas venta = new TVentas();
        String campo = null;

        venta.setAlbaran(datosLinea[POS_ALBARAN_V].trim());
        venta.setAlbaranFin(venta.getAlbaran());

        venta.setLineaPedido(datosLinea[POS_LINEA_PEDIDO_V].trim());

        //venta.setAlbaranCompra(datosLinea[POS_ALBARAN_COMPRA_V]);
        //venta.setAlbaranCompraFin(venta.getAlbaranCompra());
        venta.setCalibre(datosLinea[POS_CALIBRE_V]);
        venta.setCalibreFin(venta.getCalibre());
        if (venta.getCalibreFin() == null || venta.getCalibreFin().trim().isEmpty()) {
            venta.setCalibreFin("VARIOS");
        }
        //venta.setCalidadCompra(datosLinea[POS_CALIDAD_COMPRA_V]);
        //if (venta.getCalidadCompra().toLowerCase().trim().equals("b")) {
        //    venta.setCalidadCompra("BIO");
        //} else if (venta.getCalidadCompra().toLowerCase().trim().equals("c")) {
        //    venta.setCalidadCompra("CONVENCIONAL");
        //}
        //venta.setCalidadCompraFin(venta.getCalidadCompra());
        venta.setCalidadVenta(datosLinea[POS_CALIDAD_VENTA_V]);
        if (venta.getCalidadVenta().toLowerCase().trim().equals("b")) {
            venta.setCalidadVenta("BIO");
        } else if (venta.getCalidadVenta().toLowerCase().trim().equals("c")) {
            venta.setCalidadVenta("CONVENCIONAL");
        }
        venta.setCalidadVentaFin(venta.getCalidadVenta());
        campo = datosLinea[POS_CLIENTE_V];
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        venta.setCliente(campo);
        venta.setClienteFin(venta.getCliente());
        venta.setConfeccion(datosLinea[POS_CONFECCION_V]);
        venta.setConfeccionFin(venta.getConfeccion());
        venta.setEnvase(datosLinea[POS_ENVASE_V]);
        venta.setEnvaseFin(venta.getEnvase());
        venta.setFamilia(datosLinea[POS_FAMILIA_V2]);
        if (venta.getFamilia() != null) {
            if (venta.getFamilia().toUpperCase().equals("NÓSPERO")) {
                venta.setFamiliaFin("NÍSPERO");
            } else if (venta.getFamilia().toUpperCase().equals("N?SPERO")) {
                venta.setFamiliaFin("NÍSPERO");
            } else if (venta.getFamilia().toUpperCase().equals("AVI?N MANGO")) {
                venta.setFamiliaFin("MANGO");
            } else if (venta.getFamilia().toUpperCase().equals("AVIN MANGO")) {
                venta.setFamiliaFin("MANGO");
            } else if (venta.getFamilia().toUpperCase().contains("MANGO")) {
                venta.setFamiliaFin("MANGO");
            } else if (venta.getFamilia().toUpperCase().equals("NISPERO")) {
                venta.setFamiliaFin("NÍSPERO");
            } else {
                venta.setFamiliaFin(venta.getFamilia());
            }
        }

        if (venta.getFamilia().contains("MANG")) {
            venta.setFamiliaFin("MANGO");
        }

        if (venta.getFamilia().contains("MARAC")) {
            venta.setFamiliaFin("MARACUYA");
        }

        venta.setFechaCrea(fechaCrea);
        try

        {
            venta.setFechaVenta(new SimpleDateFormat("dd/MM/yyyy").parse(datosLinea[POS_FECHA_VENTA_V].trim()));
        } catch (ParseException pe) {
            venta.setFechaVenta(null);
            log.debug("El campo FECHA de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE DEJA EL CAMPO CON VALOR NULO \n ");
            textoCorreo += "El campo FECHA de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE DEJA EL CAMPO CON VALOR NULO \n ";
            throw new GenasoftException("El campo FECHA de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE OMITE LA CARGA DE la linea \n ");
        }
        venta.setFechaVentaFin(venta.getFechaVenta());
        //venta.setIdPale(datosLinea[POS_ID_PALE_V]);
        //venta.setIdPaleFin(venta.getIdPale());

        /**try {
        venta.setKgsEnvase(Utils.formatearValorDouble(datosLinea[POS_PESO_ENV_V].trim()));
        } catch (NumberFormatException nfe) {
        venta.setKgsEnvase(Double.valueOf(0));
        //throw new TrazabilidadesException("El campo PESO BRUTO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE OMITE LA CARGA DE la linea \n ");
        log.debug("El campo PESO ENVASE de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ");
        textoCorreo += "El campo PESO ENVASE de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ";
        }
        venta.setKgsEnvaseFin(venta.getKgsEnvase());
        */

        /** try {
        campo = datosLinea[POS_KGS_NETO_V].trim();
        if (campo.contains(".")) {
            campo = campo.replaceAll("\\.", ",");
        }
        venta.setKgs(Utils.formatearValorDouble(campo));
        } catch (NumberFormatException nfe) {
        venta.setKgs(Double.valueOf(0));
        //throw new TrazabilidadesException("El campo PESO BRUTO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE OMITE LA CARGA DE la linea \n ");
        log.debug("El campo KILOS de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ");
        textoCorreo += "El campo KILOS de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ";
        }
        venta.setKgsFin(venta.getKgs());
        */
        try {
            campo = datosLinea[POS_KGS_NETO_V].trim();
            if (campo.contains(".")) {
                campo = campo.replaceAll("\\.", ",");
            }
            venta.setKgsNetos(Utils.formatearValorDouble(campo));
        } catch (NumberFormatException nfe) {
            venta.setKgsNetos(Double.valueOf(0));
            //throw new TrazabilidadesException("El campo PESO BRUTO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE OMITE LA CARGA DE la linea \n ");
            log.debug("El campo KILOS NETO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ");
            textoCorreo += "El campo KILOS NETO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ";
        }
        venta.setKgsNetosFin(Double.valueOf(0));
        venta.setLineaPedidoLote(datosLinea[POS_LINEA_PEDIDO_LOTE_V].trim());
        venta.setLineaPedidoLoteFin(venta.getLineaPedidoLote());
        venta.setLineaPedidoLoteCaja(datosLinea[POS_LINEA_PEDIDO_LOTE_CAJA_V].trim());
        venta.setLineaPedidoLoteCajaFin(venta.getLineaPedidoLoteCaja());

        venta.setNombreDescriptivo(nombreImportacion);
        try {
            venta.setNumBultos(Integer.valueOf(datosLinea[POS_BULTOS_V].trim()));
        } catch (NumberFormatException nfe) {
            venta.setNumBultos(0);
            log.debug("El campo BULTOS de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ");
            textoCorreo += "El campo BULTOS de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ";
        }
        venta.setNumBultosFin(venta.getNumBultos());

        campo = datosLinea[POS_ORIGEN_V].trim();

        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }
        venta.setOrigen(campo);
        venta.setOrigenFin(venta.getOrigen());
        venta.setPedido(datosLinea[POS_PEDIDO_V].trim());
        venta.setPedidoFin(venta.getPedido());

        venta.setUndConsumo(datosLinea[POS_UND_CONSUMO_V].trim());
        venta.setUndConsumoFin(venta.getUndConsumo());
        venta.setUsuCrea(user);
        venta.setVariedad(datosLinea[POS_VARIEDAD_V].trim());
        venta.setVariedadFin(venta.getVariedad());

        if (venta.getVariedadFin().equals("KENT AVION")) {
            venta.setFamiliaFin("MANGO");
        }

        venta.setCerrada(0);

        venta.setIndAutomatico(0);

        venta.setPlantilla(datosLinea[POS_PLANTILLA_V].trim());

        if (venta.getPlantilla() != null) {
            if (venta.getPlantilla().toUpperCase().equals("NÓSPERO")) {
                venta.setPlantillaFin("NÍSPERO");
            } else if (venta.getPlantilla().toUpperCase().equals("N?SPERO")) {
                venta.setPlantillaFin("NÍSPERO");
            } else if (venta.getPlantilla().toUpperCase().equals("AVI�N MANGO")) {
                venta.setPlantillaFin("MANGO");
            } else if (venta.getPlantilla().toUpperCase().equals("AVI?N MANGO")) {
                venta.setPlantillaFin("MANGO");
            } else if (venta.getPlantilla().toUpperCase().equals("AVIN MANGO")) {
                venta.setPlantillaFin("MANGO");
            } else if (venta.getPlantilla().toUpperCase().contains("MANGO")) {
                venta.setPlantillaFin("MANGO");
            } else if (venta.getPlantilla().toUpperCase().contains("MANGO")) {
                venta.setPlantillaFin("MANGO");
            } else if (venta.getFamilia().toUpperCase().equals("NISPERO")) {
                venta.setPlantillaFin("NÍSPERO");
            } else {
                venta.setPlantillaFin(venta.getPlantilla());
            }
        }

        try {
            venta.setFechaSalida(new SimpleDateFormat("dd/MM/yyyy").parse(datosLinea[POS_FECHA_SAL_V].trim()));
        } catch (ParseException pe) {
            venta.setFechaVenta(null);
            log.debug("El campo FECHA de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE DEJA EL CAMPO CON VALOR NULO \n ");
            textoCorreo += "El campo FECHA de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE DEJA EL CAMPO CON VALOR NULO \n ";
            //throw new TrazabilidadesException("El campo FECHA de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE OMITE LA CARGA DE la linea \n ");
        }
        venta.setFechaSalidaFin(venta.getFechaSalida());

        campo = datosLinea[POS_COD_CLIENTE_V].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        venta.setCodCliente(campo);

        venta.setCodClienteFin(venta.getCodCliente());

        campo = datosLinea[POS_CENTRO_DESCARGA_V].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        venta.setCentroDescarga(campo);

        venta.setCentroDescargaFin(venta.getCentroDescarga());

        campo = datosLinea[POS_DIR_DESCARGA_V].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        venta.setDirDescarga(campo);
        venta.setDirDescargaFin(venta.getDirDescarga());

        campo = datosLinea[POS_REFERENCIA_V].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        venta.setReferencia(campo);
        venta.setReferenciaFin(venta.getReferencia());

        campo = datosLinea[POS_CATEGORIA_V].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        venta.setCategoria(campo);
        venta.setCategoriaFin(venta.getCategoria());

        campo = datosLinea[POS_MARCA_V].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        venta.setMarca(campo);
        venta.setMarcaFin(venta.getMarca());

        try {
            venta.setUnidades(Integer.valueOf(datosLinea[POS_UNIDADES_V].trim()));
        } catch (Exception e) {
            venta.setUnidades(0);
        }
        venta.setUnidadesFin(venta.getUnidades());

        campo = datosLinea[POS_PALE_V].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        venta.setPale(campo);
        venta.setPaleFin(venta.getPale());

        venta.setnPale(datosLinea[POS_N_PALE_V].trim());
        venta.setnPaleFin(venta.getnPale());

        campo = datosLinea[POS_DIRECCION_CLIENTE_V].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        venta.setDireccionCliente(campo);
        venta.setDireccionClienteFin(venta.getDireccionCliente());

        campo = datosLinea[POS_CIF_CLIENTE_V].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        venta.setCifCliente(campo);
        venta.setCifClienteFin(venta.getCifCliente());

        venta.setTelefonoCliente(datosLinea[POS_TELEFONO_CLIENTE_V].trim());
        if (venta.getTelefonoCliente() == null) {
            venta.setTelefonoCliente("");
        }
        venta.setTelefonoClienteFin(venta.getTelefonoCliente());

        campo = datosLinea[POS_TRANSPORTISTA_V].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        venta.setTransportista(campo);
        venta.setTransportistaFin(venta.getTransportista());

        venta.setIdExterno(Double.valueOf(datosLinea[POS_ID_EXTERNO_V]));

        try {
            campo = datosLinea[POS_PESO_ENV_V].trim();
            if (campo.contains(".")) {
                campo = campo.replaceAll("\\.", ",");
            }
            venta.setKgsEnvase(Utils.formatearValorDouble(campo));
        } catch (NumberFormatException nfe) {
            venta.setKgsNetos(Double.valueOf(0));
            //throw new TrazabilidadesException("El campo PESO BRUTO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE OMITE LA CARGA DE la linea \n ");
            log.debug("El campo PESO NETO ENVASE de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ");
            textoCorreo += "El campo PESO NETO ENVASE de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ";
        }
        venta.setKgsEnvaseFin(venta.getKgsEnvase());

        venta.setLoteMovAlm(datosLinea[POS_LOTE_MOV_VENTA_V]);
        venta.setLoteMovAlmFi(venta.getLoteMovAlm());
        venta.setConfeccionFin(venta.getConfeccion());
        venta.setLote("");
        venta.setLoteFin("");
        venta.setCerrada(0);
        venta.setIndError(0);

        campo = datosLinea[POS_NOMBRE_PRODUCTO_V].trim().toUpperCase();

        if (campo.equals("AG HASS ESP C I CROWDFAR")) {
            venta.setKgsNetos(Double.valueOf(4));
            venta.setFamilia("AG HASS ESP C I CROWDFAR");
            venta.setFamiliaFin("AGUACATE");
            venta.setVariedad("HASS");
            venta.setVariedadFin("HASS");
        } else if (campo.equals("AG LAMB HASS ESP C I CROWDFAR")) {
            venta.setKgsNetos(Double.valueOf(19));
            venta.setFamilia("AG LAMB HASS ESP C I CROWDFAR");
            venta.setFamiliaFin("AGUACATE");
            venta.setVariedad("HASS");
            venta.setVariedadFin("HASS");
        } else if (campo.equals("CROWDFARM 2.5KG AGUACATE OVERHARVEST")) {
            venta.setKgsNetos(Double.valueOf(2.5));
            venta.setFamilia("CROWDFARM 2.5KG AGUACATE OVERHARVEST");
            venta.setFamiliaFin("AGUACATE");
            venta.setVariedad("HASS");
            venta.setVariedadFin("HASS");
        } else if (campo.equals("CROWDFARM 2.5KG AGUACATE OVERHARVEST CRO")) {
            venta.setKgsNetos(Double.valueOf(2.5));
            venta.setFamilia("CROWDFARM 2.5KG AGUACATE OVERHARVEST CRO");
            venta.setFamiliaFin("AGUACATE");
            venta.setVariedad("HASS");
            venta.setVariedadFin("HASS");
        } else if (campo.equals("CROWDFARM 25KG AGUACATE OVERHARVEST CROW")) {
            venta.setKgsNetos(Double.valueOf(2.5));
            venta.setFamilia("CROWDFARM 25KG AGUACATE OVERHARVEST CROW");
            venta.setFamiliaFin("AGUACATE");
            venta.setVariedad("HASS");
            venta.setVariedadFin("HASS");
        } else if (campo.equals("CROWDFARM 3KG MANGO OVERHARVEST")) {
            venta.setKgsNetos(Double.valueOf(3));
            venta.setFamilia("CROWDFARM 3KG MANGO OVERHARVEST");
            venta.setFamiliaFin("MANGO");
            venta.setVariedad("KENT");
            venta.setVariedadFin("KENT");
        } else if (campo.equals("CROWDFARM 45KG AGUCATE CHIRI KUMQ OVERHA")) {
            venta.setKgsNetos(Double.valueOf(2));
            venta.setFamilia("CROWDFARM 45KG AGUCATE CHIRI KUMQ OVERHA");
            venta.setFamiliaFin("AGUACATE");
            venta.setVariedad("HASS");
            venta.setVariedadFin("HASS");
        } else if (campo.equals("CROWDFARM 4KG AGUACATE KUMQUAT OVERHARVE")) {
            venta.setKgsNetos(Double.valueOf(3));
            venta.setFamilia("CROWDFARM 4KG AGUACATE KUMQUAT OVERHARVE");
            venta.setFamiliaFin("AGUACATE");
            venta.setVariedad("HASS");
            venta.setVariedadFin("HASS");
        } else if (campo.equals("CROWDFARM 4KG AGUACATE OVERHARVEST")) {
            venta.setKgsNetos(Double.valueOf(4));
            venta.setFamilia("CROWDFARM 4KG AGUACATE OVERHARVEST");
            venta.setFamiliaFin("AGUACATE");
            venta.setVariedad("HASS");
            venta.setVariedadFin("HASS");
        } else if (campo.equals("CROWDFARM 4KG AGUACATE OVERHARVEST CROWD")) {
            venta.setKgsNetos(Double.valueOf(4));
            venta.setFamilia("CROWDFARM 4KG AGUACATE OVERHARVEST CROWD");
            venta.setFamiliaFin("AGUACATE");
            venta.setVariedad("HASS");
            venta.setVariedadFin("HASS");
        } else if (campo.equals("CROWDFARM 5KG MANGO OVERHARVEST")) {
            venta.setKgsNetos(Double.valueOf(5));
            venta.setFamilia("CROWDFARM 5KG MANGO OVERHARVEST");
            venta.setFamiliaFin("MANGO");
            venta.setVariedad("KENT");
            venta.setVariedadFin("KENT");
        } else if (campo.equals("CROWDFARM 5KG MANGO-AGUACATE OVERHARVEST")) {
            venta.setKgsNetos(Double.valueOf(3));
            venta.setFamilia("CROWDFARM 5KG MANGO-AGUACATE OVERHARVEST");
            venta.setFamiliaFin("MANGO");
            venta.setVariedad("KENT");
            venta.setVariedadFin("KENT");
        }

        return venta;
    }

    /**
     * Método que nos nutre el campo TVENTAS a partir del contenido del objeto pasado por parámetro.
     * @param datosLinea El objeto con los datos a nutrir.
     * @return El objeto TVentas con el contenido
     * @throws GenasoftException 
     */
    private TLineasVentas nutrirTrazabilidadesDatosLinea(String[] datosLinea, Integer ordenLinea, Integer user, Date fechaCrea) throws GenasoftException {
        TLineasVentas linea = new TLineasVentas();
        TProveedores prov = null;
        String campo = null;

        linea.setAlbaranCompraIni(datosLinea[POS_ALBARAN_COMPRA_TRAZ]);
        linea.setAlbaranCompraFin(linea.getAlbaranCompraIni());

        campo = datosLinea[POS_PROVEEDOR_TRAZ].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        if (campo.trim().toUpperCase().equals("GONZÑLEZ CALDERÑN, FRANCISCO MIGUEL")) {
            campo = "GONZÁLEZ CALDERÓN, FRANCISCO MIGUEL";
        } else if (campo.trim().toUpperCase().equals("DIAZ TAMAYO, PLACIDO JOAQUÑN")) {
            campo = "DIAZ TAMAYO, PLACIDO JOAQUÍN";
        } else if (campo.trim().toUpperCase().equals("RODRIGUEZ CENTURION, MÑ CARMEN")) {
            campo = "RODRIGUEZ CENTURION, Mª CARMEN";
        } else if (campo.trim().toUpperCase().equals("RAMÑREZ TELLO, MARÑA ÑNGELES")) {
            campo = "RAMÍREZ TELLO, MARÍA ÁNGELES";
        } else if (campo.trim().toUpperCase().equals("GARCIA ARAGÑN, RAÑL")) {
            campo = "GARCIA ARAGÓN, RAÚL";
        } else if (campo.trim().toUpperCase().equals("AGUILERA GONZÑLEZ, FRANCISCO")) {
            campo = "AGUILERA GONZÁLEZ, FRANCISCO";
        } else if (campo.trim().toUpperCase().equals("RECIO MARTÑN, AMPARO")) {
            campo = "RECIO MARTÍN, AMPARO";
        } else if (campo.trim().toUpperCase().equals("SERÑN GARCIA, JOSE")) {
            campo = "SERÓN GARCIA, JOSE";
        } else if (campo.trim().toUpperCase().equals("ALGARÑN GUTIERREZ, MARIA ISABEL")) {
            campo = "ALGARÍN GUTIERREZ, MARIA ISABEL";
        } else if (campo.trim().toUpperCase().equals("ORTEGA MARTÑN, MANUEL DAVID")) {
            campo = "ORTEGA MARTÍN, MANUEL DAVID";
        } else if (campo.trim().toUpperCase().equals("NT Ñ AGRICOLA ZAYAS")) {
            campo = "NT - AGRICOLA ZAYAS";
        } else if (campo.trim().toUpperCase().equals("NT Ñ VILLOSLADA")) {
            campo = "NT - VILLOSLADA";
        } else if (campo.trim().toUpperCase().equals("NT Ñ GISELA HOENER")) {
            campo = "NT - GISELA HOENER";
        } else if (campo.trim().toUpperCase().equals("NT Ñ AYALA ESPAÑA")) {
            campo = "NT - AYALA ESPAÑA";
        } else if (campo.trim().toUpperCase().equals("ALBA MARTÑN, MIGUEL")) {
            campo = "ALBA MUÑOZ, RAFAEL";
        } else if (campo.trim().toUpperCase().equals("ARAGÑEZ DELGADO, RAFAEL")) {
            campo = "ARAGÜEZ DELGADO, RAFAEL";
        } else if (campo.trim().toUpperCase().equals("ARROYO ALARCÑN, MARIA EMILIA")) {
            campo = "ARROYO ALARCÓN, MARIA EMILIA";
        } else if (campo.trim().toUpperCase().equals("CEBRERO RUIZ, FRANCISCO EUSEB")) {
            campo = "CEBRERO RUIZ, FRANCISCO EUSEBIO";
        } else if (campo.trim().toUpperCase().equals("DE LA MONJA KÑHNE, JESUS")) {
            campo = "DE LA MONJA KÜHNE, JESUS";
        } else if (campo.trim().toUpperCase().equals("FORTES  GÑMEZ, CRISTINA")) {
            campo = "FORTES  GÓMEZ, CRISTINA";
        } else if (campo.trim().toUpperCase().equals("FRUTÑCOLA DO VALE DO SAO FRANCISCO UNIPESSOAL, LDA")) {
            campo = "FRUTÍCOLA DO VALE DO SAO FRANCISCO UNIPESSOAL, LDA";
        } else if (campo.trim().toUpperCase().equals("GARCIA PEÑA, ALADINO JOSÑ")) {
            campo = "GARCIA PEÑA, ALADINO JOSÉ";
        } else if (campo.trim().toUpperCase().equals("GARCIOLO BARBERO, MARÑA CARMEN")) {
            campo = "GARCIOLO BARBERO, MARÍA CARMEN";
        } else if (campo.trim().toUpperCase().equals("GONZALEZ CALDERON, FRANCISCO")) {
            campo = "GONZÁLEZ CALDERÓN, FRANCISCO MIGUEL";
        } else if (campo.trim().toUpperCase().equals("MANAGÑ")) {
            campo = "MANAGÚ";
        } else if (campo.trim().toUpperCase().equals("MARFIL FERNÑNDEZ, MARIA DEL CARMEN")) {
            campo = "MARFIL FERNÁNDEZ, MARIA DEL CARMEN";
        } else if (campo.trim().toUpperCase().equals("MARIN HIDALGO, ADORACIÑN")) {
            campo = "MARIN HIDALGO, ADORACIÓN";
        } else if (campo.trim().toUpperCase().equals("MARTÑN TORRES, JUAN CARLOS")) {
            campo = "MARTIN TORRES, JUAN CARLOS";
        } else if (campo.trim().toUpperCase().equals("MELÑNDEZ ARIAS, RAFAEL")) {
            campo = "MELÉNDEZ ARIAS, RAFAEL";
        } else if (campo.trim().toUpperCase().equals("PLANTACIONES ECOLOGICAS PABÑN S.L.")) {
            campo = "PLANTACIONES ECOLOGICAS PABÓN S.L.";
        } else if (campo.trim().toUpperCase().equals("RECIO MARTIN, FRANCISCO JESÑS")) {
            campo = "RECIO MARTIN, FRANCISCO JESÚS";
        } else if (campo.trim().toUpperCase().equals("RUIZ MARÑN, FRANCISCO")) {
            campo = "RUIZ MARÍN, FRANCISCO";
        } else if (campo.trim().toUpperCase().equals("SANCHEZ CENTURIÑN, MARIA LUISA")) {
            campo = "SANCHEZ CENTURIÓN, MARIA LUISA";
        } else if (campo.trim().toUpperCase().equals("SÑNCHEZ GARCÑA, MÑ JOSEFA")) {
            campo = "SÁNCHEZ GARCÍA, Mª JOSEFA";
        } else if (campo.trim().toUpperCase().equals("TOME RICO, JOSE EUSEB")) {
            campo = "TOME RICO, JOSE EUSEBIO";
        } else if (campo.trim().toUpperCase().equals("VALDERRAMA MÑRQUEZ, JOSÑ")) {
            campo = "VALDERRAMA MÁRQUEZ, JOSÉ";
        }

        if (!campo.isEmpty()) {
            prov = proveedoresSetup.obtenerProveedorPorNombre(campo.trim().toUpperCase());
            if (prov == null) {
                prov = new TProveedores();
                prov.setDescripcion(campo.trim().toUpperCase());
                prov.setGgn("NO GG");
                prov.setEstado(1);
                proveedoresSetup.crearProveedor(prov);
            }
        }

        linea.setProveedorIni(campo.trim().toUpperCase());
        linea.setProveedorFin(linea.getProveedorIni());
        if (prov != null) {
            linea.setGgn(prov.getGgn());
        } else {
            linea.setGgn("NO GG");
        }

        linea.setCertificacionIni(datosLinea[POS_CERTIFICACION_TRAZ]);
        if (linea.getCertificacionIni().toLowerCase().trim().equals("b")) {
            linea.setCertificacionIni("BIO");
        } else if (linea.getCertificacionIni().toLowerCase().trim().equals("c")) {
            linea.setCertificacionIni("CONVENCIONAL");
        }
        linea.setCertificacionFin(linea.getCertificacionIni());

        campo = datosLinea[POS_REFERENCIA_COMPRA_TRAZ].trim();
        if (campo == null || campo.isEmpty()) {
            campo = "";
        } else {
            if (campo.contains("�")) {
                campo = campo.replaceAll("�", "Ñ");
            } else if (campo.contains("-")) {
                campo = campo.replaceAll("-", "Ñ");
            }
        }

        linea.setReferenciaCompraIni(campo);
        linea.setReferenciaCompraFin(linea.getReferenciaCompraIni());

        try {
            campo = datosLinea[POS_KGS_TRAZABILIDAD_TRAZ].trim();
            if (campo.contains(".")) {
                campo = campo.replaceAll("\\.", ",");
            }
            linea.setKgsTrazabilidadIni(Utils.formatearValorDouble(campo));
        } catch (NumberFormatException nfe) {
            linea.setKgsTrazabilidadIni(Double.valueOf(0));
            //throw new TrazabilidadesException("El campo PESO BRUTO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE OMITE LA CARGA DE la linea \n ");
            log.debug("El campo KILOS NETO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ");
            textoCorreo += "El campo KILOS NETO de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ";
        }
        linea.setKgsTrazabilidadFin(linea.getKgsTrazabilidadIni());
        linea.setKgsTrazadosIni(linea.getKgsTrazabilidadIni());
        linea.setKgsTrazadosFin(linea.getKgsTrazabilidadFin());

        linea.setLoteIni(datosLinea[POS_LOTE_TRAZ].trim());
        if (linea.getLoteIni().trim().isEmpty()) {
            linea.setLoteIni(SIN_LOTE);
        }
        linea.setLoteFin(linea.getLoteIni());

        try {
            linea.setBultosPaleIni(Integer.valueOf(datosLinea[POS_BULTOS_PALE_TRAZ].trim()));
        } catch (NumberFormatException nfe) {
            linea.setBultosPaleIni(0);
            log.debug("El campo BULTOS de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ");
            textoCorreo += "El campo BULTOS de la linea con orden " + ordenLinea + ", No tiene el valor correcto. SE ESTABLECE VALOR CERO \n ";
        }

        linea.setIdVentaExterno(Double.valueOf(datosLinea[POS_ID_VENTA_TRAZ].trim()));
        linea.setIdExterno(Double.valueOf(datosLinea[POS_ID_TRAZ].trim()));

        linea.setBultosPaleFin(linea.getBultosPaleIni());
        linea.setIndBasura(0);
        linea.setIndError(0);
        linea.setIndCambio(0);
        linea.setIndCerrada(0);
        if (linea.getLoteIni() == null || linea.getLoteIni().isEmpty()) {
            linea.setIndError(1);
        }
        if (linea.getKgsTrazadosIni().equals(Double.valueOf(0)) || linea.getKgsTrazadosIni() < Double.valueOf(0)) {
            linea.setIndError(1);
        }
        /**
        linea.setFamiliaIni(datosLinea[POS_FAMILIA_TRAZ]);
        if (linea.getFamiliaIni() != null) {
            if (linea.getFamiliaIni().toUpperCase().equals("NÓSPERO")) {
                linea.setFamiliaFin("NÍSPERO");
            } else if (linea.getFamiliaIni().toUpperCase().equals("N?SPERO")) {
                linea.setFamiliaFin("NÍSPERO");
            } else if (linea.getFamiliaIni().toUpperCase().equals("AVI?N MANGO")) {
                linea.setFamiliaFin("MANGO");
            } else if (linea.getFamiliaIni().toUpperCase().equals("AVIN MANGO")) {
                linea.setFamiliaFin("MANGO");
            } else if (linea.getFamiliaIni().toUpperCase().contains("MANGO")) {
                linea.setFamiliaFin("MANGO");
            } else {
                linea.setFamiliaFin(linea.getFamiliaIni());
            }
        }
        
        linea.setVariedadIni(datosLinea[POS_VARIEDAD_TRAZ].trim());
        linea.setVariedadFin(linea.getVariedadIni());
        
        if (linea.getVariedadFin().equals("KENT AVION") && linea.getFamiliaFin() == null || linea.getFamiliaFin().trim().isEmpty()) {
            linea.setFamiliaFin("MANGO");
        }
        */
        return linea;

    }

    /**
     * Método que nos nutre el campo TVENTAS a partir del contenido del objeto pasado por parámetro.
     * @param datosLinea El objeto con los datos a nutrir.
     * @return El objeto TVentas con el contenido
     * @throws GenasoftException 
     */
    private TMovimientosAlmacen nutrirMovimirntoAlmacenDatosLinea(String[] datosLinea, Integer ordenLinea, Integer user, Date fechaCrea) throws GenasoftException {
        TMovimientosAlmacen mov = new TMovimientosAlmacen();

        mov.setId(Utils.formatearValorDouble(datosLinea[POS_MOV_ALM_ID]));
        mov.setIdMovimeintoVenta(Utils.formatearValorDouble(datosLinea[POS_MOV_ALM_ID_MOV_VENTA]));
        mov.setIdPalet(Utils.formatearValorDouble(datosLinea[POS_MOV_ALM_ID_PALET]));

        return mov;
    }

    /**
     * Método que nos retorna los resultados de compras ordenados por fecha
     * @param f1 'Fecha desde' para realizar la consulta.
     * @param f2 'Fecha hasta' para realizar la consulta
     * @return Los resultados encontrados.
     * @throws GenasoftException 
     */
    public TreeMap<Date, List<TComprasVista>> obtenerComprasFechas() throws GenasoftException {

        // Creamos el árbol para tener los datos estructurados por fechas
        TreeMap<Date, List<TComprasVista>> trMap = new TreeMap<Date, List<TComprasVista>>();

        //Buscamos las compras pendientes de facturar
        List<TComprasVista> lCompras = Utils.generarListaGenerica();

        //lCompras.addAll(commonSetup.mComprasId.values());

        // Si lCompras está vacía, es posible que se tengan que coger los datos de BD
        if (lCompras.isEmpty()) {
            cargarComprasBD();
            lCompras.addAll(commonSetup.mComprasId.values());
        }

        try {
            // Guardamos los resultados de compras sin facturar
            for (TComprasVista compra : lCompras) {
                // Lo metemos en el diccionario
                if (trMap.get(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha())) == null) {
                    List<TComprasVista> lResult = Utils.generarListaGenerica();
                    lResult.add(compra);
                    if (compra.getFecha() != null) {
                        trMap.put(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha()), lResult);
                    } else {
                        continue;
                    }

                } else {
                    List<TComprasVista> lResult = trMap.get(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha()));
                    lResult.add(compra);
                    // Ordenamos la lista por nº de albarán 
                    lResult.sort(new Comparator<TComprasVista>() {

                        @Override
                        public int compare(TComprasVista m1, TComprasVista m2) {
                            if (m1.getAlbaran() == m2.getAlbaran()) {
                                return 0;
                            }
                            return m1.getAlbaran().compareTo(m2.getAlbaran());
                        }

                    });
                    trMap.put(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha()), lResult);
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return trMap;
    }

    public void nutrirDiccionarioTodasComprasEjercicio(List<TComprasVista> lComprasVista) {
        commonSetup.setmComprasEjercicio(lComprasVista.stream().collect(Collectors.toMap(TComprasVista::getId, Function.identity())));

        rellenarArbolCompras();
    }

    public void nutrirDiccionarioTodasVentasEjercicio(List<TVentas> lVentas, List<TVentasVista> lVentasVista) {

        commonSetup.setmVentasEjercicio(lVentasVista.stream().collect(Collectors.toMap(TVentasVista::getId, Function.identity())));

        rellenarArbolVentas();

    }

    /**
     * Método que nos retorna los resultados de compras ordenados por fecha
     * @param f1 'Fecha desde' para realizar la consulta.
     * @param f2 'Fecha hasta' para realizar la consulta
     * @return Los resultados encontrados.
     * @throws GenasoftException 
     */
    public TreeMap<Date, List<TComprasVista>> obtenerComprasFechas2() throws GenasoftException {

        // Creamos el árbol para tener los datos estructurados por fechas
        TreeMap<Date, List<TComprasVista>> trMap = new TreeMap<Date, List<TComprasVista>>();

        //Buscamos las compras pendientes de facturar
        List<TComprasVista> lCompras = Utils.generarListaGenerica();

        //lCompras.addAll(commonSetup.mComprasId.values());

        // Si lCompras está vacía, es posible que se tengan que coger los datos de BD
        if (lCompras.isEmpty()) {
            cargarComprasBD2();
            lCompras.addAll(commonSetup.mComprasId.values());
        }

        try {
            // Guardamos los resultados de compras sin facturar
            for (TComprasVista compra : lCompras) {
                // Lo metemos en el diccionario
                if (trMap.get(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha())) == null) {
                    List<TComprasVista> lResult = Utils.generarListaGenerica();
                    lResult.add(compra);
                    if (compra.getFecha() != null) {
                        trMap.put(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha()), lResult);
                    } else {
                        continue;
                    }

                } else {
                    List<TComprasVista> lResult = trMap.get(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha()));
                    lResult.add(compra);
                    // Ordenamos la lista por nº de albarán 
                    lResult.sort(new Comparator<TComprasVista>() {

                        @Override
                        public int compare(TComprasVista m1, TComprasVista m2) {
                            if (m1.getAlbaran() == m2.getAlbaran()) {
                                return 0;
                            }
                            return m1.getAlbaran().compareTo(m2.getAlbaran());
                        }

                    });
                    trMap.put(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha()), lResult);
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return trMap;
    }

    /**
     * Método que nos carga las ventas en un tree ID-linea compra --> iComprasGlobales
     * @return
     */
    public TreeMap<String, List<TComprasVista>> obtenerComprasAlbaranCompra(Map<String, List<TComprasVista>> mCompras, List<String> lIdsCompras) {

        TreeMap<String, List<TComprasVista>> tResult = new TreeMap<String, List<TComprasVista>>();

        // Vamos bucando las compras.        
        for (String compr : lIdsCompras) {
            if (mCompras.get(compr) != null) {
                // Lo metemos en el diccionario
                if (tResult.get(compr) == null) {
                    List<TComprasVista> lResult = Utils.generarListaGenerica();
                    lResult.addAll(mCompras.get(compr));
                    lResult.sort(new Comparator<TComprasVista>() {
                        @Override
                        public int compare(TComprasVista m1, TComprasVista m2) {
                            if (m1.getAlbaranFin() == m2.getAlbaranFin()) {
                                return 0;
                            }
                            return m1.getAlbaranFin().compareTo(m2.getAlbaranFin());
                        }
                    });
                    tResult.put(compr, lResult);
                } else {
                    List<TComprasVista> lResult = tResult.get(compr);
                    lResult.addAll(mCompras.get(compr));
                    // Ordenamos la lista por nº de albarán 
                    lResult.sort(new Comparator<TComprasVista>() {

                        @Override
                        public int compare(TComprasVista m1, TComprasVista m2) {
                            if (m1.getAlbaranFin() == m2.getAlbaranFin()) {
                                return 0;
                            }
                            return m1.getAlbaranFin().compareTo(m2.getAlbaranFin());
                        }

                    });
                    tResult.put(compr, lResult);
                }
            }
        }

        // Retornamos el diccionario con los datos encontrados.
        return tResult;
    }

    /**
     * Método que nos carga las ventas en un tree ID-linea compra --> iComprasGlobales
     * @return
     */
    public TreeMap<String, List<TVentasVista>> obtenerVentasIdLineaPedidoCompra(Map<String, List<TVentasVista>> mVentas, List<String> lIdsCompras) {

        TreeMap<String, List<TVentasVista>> tResult = new TreeMap<String, List<TVentasVista>>();

        // Vamos bucando las ventas.        
        for (String compr : lIdsCompras) {
            if (mVentas.get(compr) != null) {
                // Lo metemos en el diccionario
                if (tResult.get(compr) == null) {
                    List<TVentasVista> lResult = Utils.generarListaGenerica();
                    lResult.addAll(mVentas.get(compr));
                    lResult.sort(new Comparator<TVentasVista>() {
                        @Override
                        public int compare(TVentasVista m1, TVentasVista m2) {
                            if (m1.getAlbaranFin() == m2.getAlbaranFin()) {
                                return 0;
                            }
                            return m1.getAlbaranFin().compareTo(m2.getAlbaranFin());
                        }
                    });
                    tResult.put(compr, lResult);
                } else {
                    List<TVentasVista> lResult = tResult.get(compr);
                    lResult.addAll(mVentas.get(compr));
                    // Ordenamos la lista por nº de albarán 
                    lResult.sort(new Comparator<TVentasVista>() {

                        @Override
                        public int compare(TVentasVista m1, TVentasVista m2) {
                            if (m1.getAlbaranFin() == m2.getAlbaranFin()) {
                                return 0;
                            }
                            return m1.getAlbaranFin().compareTo(m2.getAlbaranFin());
                        }

                    });
                    tResult.put(compr, lResult);
                }
            }
        }

        return tResult;

    }

    /**
     * Método que nos carga las ventas en un tree ID-linea compra --> iComprasGlobales
     * @return
     */
    public TreeMap<String, List<TVentasVista>> obtenerVentasIdLineaPedidoCompra2(Map<String, List<TVentasVista>> mVentas, List<String> lIdsCompras) {

        TreeMap<String, List<TVentasVista>> tResult = new TreeMap<String, List<TVentasVista>>();

        // Vamos bucando las ventas.        
        for (String compr : lIdsCompras) {
            if (mVentas.get(compr) != null) {
                // Lo metemos en el diccionario
                if (tResult.get(compr) == null) {
                    List<TVentasVista> lResult = Utils.generarListaGenerica();
                    lResult.addAll(mVentas.get(compr));
                    lResult.sort(new Comparator<TVentasVista>() {
                        @Override
                        public int compare(TVentasVista m1, TVentasVista m2) {
                            if (m1.getAlbaranFin() == m2.getAlbaranFin()) {
                                return 0;
                            }
                            return m1.getAlbaranFin().compareTo(m2.getAlbaranFin());
                        }
                    });
                    tResult.put(compr, lResult);
                } else {
                    List<TVentasVista> lResult = tResult.get(compr);
                    lResult.addAll(mVentas.get(compr));
                    // Ordenamos la lista por nº de albarán 
                    lResult.sort(new Comparator<TVentasVista>() {

                        @Override
                        public int compare(TVentasVista m1, TVentasVista m2) {
                            if (m1.getAlbaranFin() == m2.getAlbaranFin()) {
                                return 0;
                            }
                            return m1.getAlbaranFin().compareTo(m2.getAlbaranFin());
                        }

                    });
                    tResult.put(compr, lResult);
                }
            }
        }

        return tResult;

    }

    /**
     * Método que nos carga las ventas en un tree ID-linea compra --> iComprasGlobales
     * @return
     */
    public List<TVentasFictVista> obtenerVentasFictIdLineaPedidoCompra(List<Integer> lIdsCompras) {

        List<TVentasFictVista> lResult = Utils.generarListaGenerica();

        // Buscamos las ventas a partir de los IDS de las comras
        List<TVentasFict> lVentas = tVentasFictMapper.obtenerVentasFictIdsPedidoCompra(lIdsCompras);

        if (!lVentas.isEmpty()) {
            lResult = convertirVentasFictVista(lVentas);
        }

        return lResult;

    }

    /**
     * Método que nos carga las ventas en un tree ID-linea compra --> iComprasGlobales
     * @return
     * @throws GenasoftException 
     */
    public List<TLineasVentasVista> obtenerLineasVentaVista(Double idVenta) throws GenasoftException {

        List<TLineasVentasVista> lResult = Utils.generarListaGenerica();

        // Buscamos las ventas a partir de los IDS de las comras
        List<TLineasVentas> lVentas = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), idVenta);

        lResult = convertirLineasVentasVista(lVentas);

        return lResult;

    }

    /**
     * Método que nos carga las ventas en un tree ID-linea compra --> iComprasGlobales
     * @return
     * @throws GenasoftException 
     */
    public List<TLineasVentasVista> obtenerLineasVentaLotesVista(List<String> lLotes) throws GenasoftException {

        List<TLineasVentasVista> lResult = Utils.generarListaGenerica();

        if (!lLotes.isEmpty()) {
            // Buscamos las ventas a partir de los IDS de las comras
            List<TLineasVentas> lVentas = obtenerLineasVentaLotes(lLotes);

            lResult = convertirLineasVentasVista(lVentas);
        }
        return lResult;

    }

    /**
     * Método que nos carga las ventas en un tree ID-linea compra --> iComprasGlobales
     * @return
     * @throws GenasoftException 
     */
    public List<TLineasVentas> obtenerLineasVentaLotes(List<String> lLotes) throws GenasoftException {

        return lineasVentasSetup.obtenerLineasVentasEjercicioLotes(commonSetup.getEjercicio(), lLotes);

    }

    /**
     * Método que nos carga las ventas en un tree ID-linea compra --> iComprasGlobales
     * @return
     * @throws GenasoftException 
     */
    public List<TLineasVentas> obtenerLineasVentasEjercicioIdsVentas(List<Integer> lIds) throws GenasoftException {

        return lineasVentasSetup.obtenerLineasVentasEjercicioIdsVentas(commonSetup.getEjercicio(), lIds);

    }

    /**
     * Método que nos retorna los resultados de ventas ordenados por fecha
     * @param f1 'Fecha desde' para realizar la consulta.
     * @param f2 'Fecha hasta' para realizar la consulta
     * @return Los resultados encontrados.
     * @throws GenasoftException 
     */
    public TreeMap<Date, List<TVentasVista>> obtenerVentasFechas() throws GenasoftException {

        // Creamos el árbol para tener los datos estructurados por fechas
        TreeMap<Date, List<TVentasVista>> trMap = new TreeMap<Date, List<TVentasVista>>();

        //Buscamos las compras pendientes de facturar
        List<TVentasVista> llVentas = Utils.generarListaGenerica();

        llVentas.addAll(commonSetup.mVentasId.values());

        // Si no tenemos ventas en memoria, miramos si tenemos en BD
        if (llVentas.isEmpty()) {
            cargarVentasBD();
            llVentas.addAll(commonSetup.mVentasId.values());
        }

        // Guardamos los resultados de compras sin facturar
        for (TVentasVista compra : llVentas) {
            // Lo metemos en el diccionario
            if (trMap.get(compra.getFechaVenta()) == null) {
                List<TVentasVista> lResult = Utils.generarListaGenerica();
                lResult.add(compra);
                if (compra.getCerrada().toLowerCase().equals("sí")) {
                    compra.setIndError("No");
                    compra.setError("0");
                }
                if (compra.getFechaVenta() != null) {
                    trMap.put(compra.getFechaVenta(), lResult);
                } else {
                    continue;
                }

            } else {
                List<TVentasVista> lResult = trMap.get(compra.getFechaVenta());
                lResult.add(compra);
                // Ordenamos la lista por nº de albarán 
                lResult.sort(new Comparator<TVentasVista>() {

                    @Override
                    public int compare(TVentasVista m1, TVentasVista m2) {
                        if (m1.getAlbaran() == m2.getAlbaran()) {
                            return 0;
                        }
                        return m1.getAlbaran().compareTo(m2.getAlbaran());
                    }

                });
                trMap.put(compra.getFechaVenta(), lResult);
            }

        }

        return trMap;
    }

    /**
     * Método que nos retorna los resultados de ventas ordenados por fecha
     * @param f1 'Fecha desde' para realizar la consulta.
     * @param f2 'Fecha hasta' para realizar la consulta
     * @return Los resultados encontrados.
     * @throws GenasoftException 
     */
    public TreeMap<Date, List<TVentasVista>> obtenerVentasFechas3() throws GenasoftException {

        // Creamos el árbol para tener los datos estructurados por fechas
        TreeMap<Date, List<TVentasVista>> trMap = new TreeMap<Date, List<TVentasVista>>();

        //Buscamos las compras pendientes de facturar
        List<TVentasVista> llVentas = Utils.generarListaGenerica();

        llVentas.addAll(commonSetup.mVentasId.values());

        // Si no tenemos ventas en memoria, miramos si tenemos en BD
        if (llVentas.isEmpty()) {
            cargarVentasBD2();
            llVentas.addAll(commonSetup.mVentasId.values());
        }

        // Guardamos los resultados de compras sin facturar
        for (TVentasVista compra : llVentas) {
            // Lo metemos en el diccionario
            if (trMap.get(compra.getFechaVenta()) == null) {
                List<TVentasVista> lResult = Utils.generarListaGenerica();
                lResult.add(compra);
                if (compra.getCerrada().toLowerCase().equals("sí")) {
                    compra.setIndError("No");
                    compra.setError("0");
                }
                if (compra.getFechaVenta() != null) {
                    trMap.put(compra.getFechaVenta(), lResult);
                } else {
                    continue;
                }

            } else {
                List<TVentasVista> lResult = trMap.get(compra.getFechaVenta());
                lResult.add(compra);
                // Ordenamos la lista por nº de albarán 
                lResult.sort(new Comparator<TVentasVista>() {

                    @Override
                    public int compare(TVentasVista m1, TVentasVista m2) {
                        if (m1.getAlbaran() == m2.getAlbaran()) {
                            return 0;
                        }
                        return m1.getAlbaran().compareTo(m2.getAlbaran());
                    }

                });
                trMap.put(compra.getFechaVenta(), lResult);
            }

        }

        return trMap;
    }

    /**
     * Método que nos retorna los resultados de ventas ordenados por fecha
     * @param f1 'Fecha desde' para realizar la consulta.
     * @param f2 'Fecha hasta' para realizar la consulta
     * @return Los resultados encontrados.
     */
    public TreeMap<Date, List<TVentasVista>> obtenerVentasErroneas() {

        // Creamos el árbol para tener los datos estructurados por fechas
        TreeMap<Date, List<TVentasVista>> trMap = new TreeMap<Date, List<TVentasVista>>();

        //Buscamos las compras pendientes de facturar
        List<TVentas> llVentas1 = Utils.generarListaGenerica();

        //Buscamos las compras pendientes de facturar
        List<TVentasVista> llVentas = Utils.generarListaGenerica();

        llVentas1.addAll(commonSetup.mIdVentasErroneas.values());

        llVentas = convertirVentasVista(llVentas1);

        // Guardamos los resultados de compras sin facturar
        for (TVentasVista compra : llVentas) {
            // Lo metemos en el diccionario
            if (trMap.get(compra.getFechaVenta()) == null) {
                List<TVentasVista> lResult = Utils.generarListaGenerica();
                lResult.add(compra);
                if (compra.getFechaVenta() != null) {
                    trMap.put(compra.getFechaVenta(), lResult);
                } else {
                    continue;
                }

            } else {
                List<TVentasVista> lResult = trMap.get(compra.getFechaVenta());
                lResult.add(compra);
                // Ordenamos la lista por nº de albarán 
                lResult.sort(new Comparator<TVentasVista>() {

                    @Override
                    public int compare(TVentasVista m1, TVentasVista m2) {
                        if (m1.getAlbaran() == m2.getAlbaran()) {
                            return 0;
                        }
                        return m1.getAlbaran().compareTo(m2.getAlbaran());
                    }

                });
                trMap.put(compra.getFechaVenta(), lResult);
            }

        }

        return trMap;
    }

    /**
     * Método que nos retorna los resultados de ventas ordenados por fecha
     * @param f1 'Fecha desde' para realizar la consulta.
     * @param f2 'Fecha hasta' para realizar la consulta
     * @return Los resultados encontrados.
     * @throws GenasoftException 
     */
    public TreeMap<Date, List<TVentasVista>> obtenerMasasOrigen() throws GenasoftException {

        // Creamos el árbol para tener los datos estructurados por fechas
        TreeMap<Date, List<TVentasVista>> trMap = new TreeMap<Date, List<TVentasVista>>();

        //Buscamos las compras pendientes de facturar
        List<TVentasVista> lBalances = Utils.generarListaGenerica();

        lBalances.addAll(commonSetup.mMasasArticulosOrigen.values());

        // Si no tenemos ventas en memoria, miramos si tenemos en BD
        if (lBalances.isEmpty()) {
            cargarVentasBD();
            lBalances.addAll(commonSetup.mMasasArticulosOrigen.values());
        }

        // Guardamos los resultados de compras sin facturar
        for (TVentasVista compra : lBalances) {
            // Lo metemos en el diccionario
            if (trMap.get(compra.getFechaVenta()) == null) {
                List<TVentasVista> lResult = Utils.generarListaGenerica();
                lResult.add(compra);
                if (compra.getFechaVenta() != null) {
                    trMap.put(compra.getFechaVenta(), lResult);
                } else {
                    continue;
                }

            } else {
                List<TVentasVista> lResult = trMap.get(compra.getFechaVenta());
                lResult.add(compra);
                // Ordenamos la lista por nº de albarán 
                lResult.sort(new Comparator<TVentasVista>() {

                    @Override
                    public int compare(TVentasVista m1, TVentasVista m2) {
                        if (m1.getAlbaran() == m2.getAlbaran()) {
                            return 0;
                        }
                        return m1.getAlbaran().compareTo(m2.getAlbaran());
                    }

                });
                trMap.put(compra.getFechaVenta(), lResult);
            }

        }

        return trMap;
    }

    /**
     * Método que nos retorna los resultados de ventas ordenados por fecha
     * @param f1 'Fecha desde' para realizar la consulta.
     * @param f2 'Fecha hasta' para realizar la consulta
     * @return Los resultados encontrados.
     * @throws GenasoftException 
     */
    public TreeMap<Date, List<TVentasVista>> obtenerMasasPaises() throws GenasoftException {

        // Creamos el árbol para tener los datos estructurados por fechas
        TreeMap<Date, List<TVentasVista>> trMap = new TreeMap<Date, List<TVentasVista>>();

        //Buscamos las compras pendientes de facturar
        List<TVentasVista> lBalances = Utils.generarListaGenerica();

        lBalances.addAll(commonSetup.mMasasArticulosPais.values());

        // Si no tenemos ventas en memoria, miramos si tenemos en BD
        if (lBalances.isEmpty()) {
            cargarVentasBD();
            lBalances.addAll(commonSetup.mMasasArticulosPais.values());
        }

        // Guardamos los resultados de compras sin facturar
        for (TVentasVista compra : lBalances) {
            // Lo metemos en el diccionario
            if (trMap.get(compra.getFechaVenta()) == null) {
                List<TVentasVista> lResult = Utils.generarListaGenerica();
                lResult.add(compra);
                if (compra.getFechaVenta() != null) {
                    trMap.put(compra.getFechaVenta(), lResult);
                } else {
                    continue;
                }

            } else {
                List<TVentasVista> lResult = trMap.get(compra.getFechaVenta());
                lResult.add(compra);
                // Ordenamos la lista por nº de albarán 
                lResult.sort(new Comparator<TVentasVista>() {

                    @Override
                    public int compare(TVentasVista m1, TVentasVista m2) {
                        if (m1.getAlbaran() == m2.getAlbaran()) {
                            return 0;
                        }
                        return m1.getAlbaran().compareTo(m2.getAlbaran());
                    }

                });
                trMap.put(compra.getFechaVenta(), lResult);
            }

        }

        return trMap;
    }

    /**
     * Método que nos retorna los resultados de ventas ordenados por fecha
     * @param f1 'Fecha desde' para realizar la consulta.
     * @param f2 'Fecha hasta' para realizar la consulta
     * @return Los resultados encontrados.
     * @throws GenasoftException 
     */
    public TreeMap<Date, List<TVentasVista>> obtenerMasasCalidad() throws GenasoftException {

        // Creamos el árbol para tener los datos estructurados por fechas
        TreeMap<Date, List<TVentasVista>> trMap = new TreeMap<Date, List<TVentasVista>>();

        //Buscamos las compras pendientes de facturar
        List<TVentasVista> lBalances = Utils.generarListaGenerica();

        lBalances.addAll(commonSetup.mMasasArticulosCalidad.values());

        // Si no tenemos ventas en memoria, miramos si tenemos en BD
        if (lBalances.isEmpty()) {
            cargarVentasBD();
            lBalances.addAll(commonSetup.mMasasArticulosCalidad.values());
        }

        // Guardamos los resultados de compras sin facturar
        for (TVentasVista compra : lBalances) {
            // Lo metemos en el diccionario
            if (trMap.get(compra.getFechaVenta()) == null) {
                List<TVentasVista> lResult = Utils.generarListaGenerica();
                lResult.add(compra);
                if (compra.getFechaVenta() != null) {
                    trMap.put(compra.getFechaVenta(), lResult);
                } else {
                    continue;
                }

            } else {
                List<TVentasVista> lResult = trMap.get(compra.getFechaVenta());
                lResult.add(compra);
                // Ordenamos la lista por nº de albarán 
                lResult.sort(new Comparator<TVentasVista>() {

                    @Override
                    public int compare(TVentasVista m1, TVentasVista m2) {
                        if (m1.getAlbaran() == m2.getAlbaran()) {
                            return 0;
                        }
                        return m1.getAlbaran().compareTo(m2.getAlbaran());
                    }

                });
                trMap.put(compra.getFechaVenta(), lResult);
            }

        }

        return trMap;
    }

    /**
     * Método que nos retorna los resultados de ventas ordenados por fecha
     * @param f1 'Fecha desde' para realizar la consulta.
     * @param f2 'Fecha hasta' para realizar la consulta
     * @return Los resultados encontrados.
     * @throws GenasoftException 
     */
    public TreeMap<Date, List<TVentasVista>> obtenerMasasGGN() throws GenasoftException {

        // Creamos el árbol para tener los datos estructurados por fechas
        TreeMap<Date, List<TVentasVista>> trMap = new TreeMap<Date, List<TVentasVista>>();

        //Buscamos las compras pendientes de facturar
        List<TVentasVista> lBalances = Utils.generarListaGenerica();

        lBalances.addAll(commonSetup.mMasasGgnCalidad.values());

        // Si no tenemos ventas en memoria, miramos si tenemos en BD
        if (lBalances.isEmpty()) {
            cargarVentasBD();
            lBalances.addAll(commonSetup.mMasasGgnCalidad.values());
        }

        // Guardamos los resultados de compras sin facturar
        for (TVentasVista compra : lBalances) {
            // Lo metemos en el diccionario
            if (trMap.get(compra.getFechaVenta()) == null) {
                List<TVentasVista> lResult = Utils.generarListaGenerica();
                lResult.add(compra);
                if (compra.getFechaVenta() != null) {
                    trMap.put(compra.getFechaVenta(), lResult);
                } else {
                    continue;
                }

            } else {
                List<TVentasVista> lResult = trMap.get(compra.getFechaVenta());
                lResult.add(compra);
                // Ordenamos la lista por nº de albarán 
                lResult.sort(new Comparator<TVentasVista>() {

                    @Override
                    public int compare(TVentasVista m1, TVentasVista m2) {
                        if (m1.getAlbaran() == m2.getAlbaran()) {
                            return 0;
                        }
                        return m1.getAlbaran().compareTo(m2.getAlbaran());
                    }

                });
                trMap.put(compra.getFechaVenta(), lResult);
            }

        }

        return trMap;
    }

    private void nutrirListasCompra(TCompras compra) {

        // ALBARÁN
        // Lista con los números de albaran de compra
        if (compra.getAlbaran() != null && !compra.getAlbaran().isEmpty()) {
            if (!commonSetup.lNumerosAlbaranCompras.contains(compra.getAlbaran())) {
                commonSetup.lNumerosAlbaranCompras.add(compra.getAlbaran());
            }
        }
        // Lista con los números de albaran de compra fin
        if (compra.getAlbaranFin() != null && !compra.getAlbaranFin().isEmpty()) {
            if (!commonSetup.lNumerosAlbaranComprasFin.contains(compra.getAlbaranFin())) {
                commonSetup.lNumerosAlbaranComprasFin.add(compra.getAlbaranFin());
            }
        }

        // VARIEDAD

        // Lista con los números de albaran de compra
        if (compra.getVariedad() != null && !compra.getVariedad().isEmpty()) {
            if (!commonSetup.lFamiliasCompras.contains(compra.getVariedad())) {
                commonSetup.lFamiliasCompras.add(compra.getVariedad());
            }
        }
        // Lista con los números de albaran de compra fin
        if (compra.getVariedadFin() != null && !compra.getVariedadFin().isEmpty()) {
            if (!commonSetup.lFamiliasComprasFin.contains(compra.getVariedadFin())) {
                commonSetup.lFamiliasComprasFin.add(compra.getVariedadFin());
            }
        }

        // ARTÍCULO
        // Lista con los números de albaran de compra
        if (compra.getProducto() != null && !compra.getProducto().isEmpty()) {
            if (!commonSetup.lArticulosCompras.contains(compra.getProducto())) {
                commonSetup.lArticulosCompras.add(compra.getProducto());
            }
        }
        // Lista con los números de albaran de compra fin
        if (compra.getProductoFin() != null && !compra.getProductoFin().isEmpty()) {
            if (!commonSetup.lArticulosComprasFin.contains(compra.getProductoFin())) {
                commonSetup.lArticulosComprasFin.add(compra.getProductoFin());
            }
        }

        // ORÍGEN
        // Lista con los números de albaran de compra
        if (compra.getOrigen() != null && !compra.getOrigen().isEmpty()) {
            if (!commonSetup.lPaisesCompras.contains(compra.getOrigen())) {
                commonSetup.lPaisesCompras.add(compra.getOrigen());
            }
        }
        // Lista con los números de albaran de compra fin
        if (compra.getOrigenFin() != null && !compra.getOrigenFin().isEmpty()) {
            if (!commonSetup.lPaisesComprasFin.contains(compra.getOrigenFin())) {
                commonSetup.lPaisesComprasFin.add(compra.getOrigenFin());
            }
        }

        // PARTIDA
        // Lista con los números de albaran de compra
        if (compra.getPartida() != null && !compra.getPartida().isEmpty()) {
            if (!commonSetup.lPartidasCompras.contains(compra.getPartida())) {
                commonSetup.lPartidasCompras.add(compra.getPartida());
            }
        }
        // Lista con los números de albaran de compra fin
        if (compra.getPartidaFin() != null && !compra.getPartidaFin().isEmpty()) {
            if (!commonSetup.lPartidasComprasFin.contains(compra.getPartidaFin())) {
                commonSetup.lPartidasComprasFin.add(compra.getPartidaFin());
            }
        }

        // PROVEEDOR
        // Lista con los números de albaran de compra
        if (compra.getProveedor() != null && !compra.getProveedor().isEmpty()) {
            if (!commonSetup.lProveedoresCompras.contains(compra.getProveedor())) {
                commonSetup.lProveedoresCompras.add(compra.getProveedor());
            }
        }
        // Lista con los números de albaran de compra fin
        if (compra.getProveedorFin() != null && !compra.getProveedorFin().isEmpty()) {
            if (!commonSetup.lProveedoresComprasFin.contains(compra.getProveedorFin())) {
                commonSetup.lProveedoresComprasFin.add(compra.getProveedorFin());
            }
        }

        // LOTE
        // Lista con los números de albaran de compra
        if (compra.getLote() != null && !compra.getLote().isEmpty()) {
            if (!commonSetup.lLotesCompras.contains(compra.getLote())) {
                commonSetup.lLotesCompras.add(compra.getLote());
            }
        }
        // Lista con los números de albaran de compra fin
        if (compra.getLoteFin() != null && !compra.getLoteFin().isEmpty()) {
            if (!commonSetup.lLotesComprasFin.contains(compra.getLoteFin())) {
                commonSetup.lLotesComprasFin.add(compra.getLoteFin());
            }
        }

        // NOMBRE DESCRIPTIVO
        // Lista con los nombres descriptivos de importaciones de venta
        if (compra.getNombreDescriptivo() != null && !compra.getNombreDescriptivo().isEmpty()) {
            if (!commonSetup.lNombresImportacionesCompras.contains(compra.getNombreDescriptivo())) {
                commonSetup.lNombresImportacionesCompras.add(compra.getNombreDescriptivo());
            }
        }

        // Lista con los números de albaran de compra fin
        if (compra.getFamiliaFin() != null && !compra.getFamiliaFin().isEmpty()) {
            if (!commonSetup.lVariedades.contains(compra.getFamiliaFin())) {
                commonSetup.lVariedades.add(compra.getFamiliaFin());
            }
        }

        // Lista con los números de albaran de compra fin
        if (compra.getVariedadFin() != null && !compra.getVariedadFin().isEmpty()) {
            if (!commonSetup.lVariedadesProductos.contains(compra.getVariedadFin())) {
                commonSetup.lVariedadesProductos.add(compra.getVariedadFin());
            }
        }
    }

    private void nutrirListasVenta(TVentas venta) {

        if (commonSetup.lNumerosAlbaranVentas == null) {
            inicializarListasVenta();
        }

        // ALBARÁN
        // Lista con los números de albaran de compra
        if (venta.getAlbaran() != null && !venta.getAlbaran().isEmpty()) {
            if (!commonSetup.lNumerosAlbaranVentas.contains(venta.getAlbaran())) {
                commonSetup.lNumerosAlbaranVentas.add(venta.getAlbaran());
            }
        }
        // Lista con los números de albaran de compra fin
        if (venta.getAlbaranFin() != null && !venta.getAlbaranFin().isEmpty()) {
            if (!commonSetup.lNumerosAlbaranVentasFin.contains(venta.getAlbaranFin())) {
                commonSetup.lNumerosAlbaranVentasFin.add(venta.getAlbaranFin());
            }
        }

        // ALBARÁN COMPRA

        // Lista con los números de albaran de compra fin
        if (venta.getAlbaranCompra() != null && !venta.getAlbaranCompra().isEmpty()) {
            if (!commonSetup.lNumerosAlbaranVentasCompras.contains(venta.getAlbaranCompra())) {
                commonSetup.lNumerosAlbaranVentasCompras.add(venta.getAlbaranCompra());
            }
        }

        // Lista con los números de albaran de compra fin
        if (venta.getAlbaranCompraFin() != null && !venta.getAlbaranCompraFin().isEmpty()) {
            if (!commonSetup.lNumerosAlbaranVentasComprasFin.contains(venta.getAlbaranCompraFin())) {
                commonSetup.lNumerosAlbaranVentasComprasFin.add(venta.getAlbaranCompraFin());
            }
        }

        // VARIEDAD

        // Lista con los números de albaran de compra
        if (venta.getVariedad() != null && !venta.getVariedad().isEmpty()) {
            if (!commonSetup.lFamiliasVentas.contains(venta.getVariedad())) {
                commonSetup.lFamiliasVentas.add(venta.getVariedad());
            }
        }
        // Lista con los números de albaran de compra fin
        if (venta.getVariedadFin() != null && !venta.getVariedadFin().isEmpty()) {
            if (!commonSetup.lFamiliasVentasFin.contains(venta.getVariedadFin())) {
                commonSetup.lFamiliasVentasFin.add(venta.getVariedadFin());
            }
        }

        // Lista con los números de albaran de compra fin
        if (venta.getFamiliaFin() != null && !venta.getFamiliaFin().isEmpty()) {
            if (!commonSetup.lVariedades.contains(venta.getFamiliaFin())) {
                commonSetup.lVariedades.add(venta.getFamiliaFin());
            }
        }

        // ARTÍCULO
        // Lista con los números de albaran de compra
        if (venta.getFamilia() != null && !venta.getFamilia().isEmpty()) {
            if (!commonSetup.lArticulosVentas.contains(venta.getFamilia())) {
                commonSetup.lArticulosVentas.add(venta.getFamilia());
            }
        }
        // Lista con los números de albaran de compra fin
        if (venta.getFamiliaFin() != null && !venta.getFamiliaFin().isEmpty()) {
            if (!commonSetup.lArticulosVentasFin.contains(venta.getFamiliaFin())) {
                commonSetup.lArticulosVentasFin.add(venta.getFamiliaFin());
            }
        }

        // CLIENTE
        // Lista con los números de albaran de compra
        if (venta.getCliente() != null && !venta.getCliente().isEmpty()) {
            if (!commonSetup.lClientesVentas.contains(venta.getCliente())) {
                commonSetup.lClientesVentas.add(venta.getCliente());
            }
        }
        // Lista con los números de albaran de compra fin
        if (venta.getClienteFin() != null && !venta.getClienteFin().isEmpty()) {
            if (!commonSetup.lClientesVentasFin.contains(venta.getClienteFin())) {
                commonSetup.lClientesVentasFin.add(venta.getClienteFin());
            }
        }

        // ORÍGEN
        // Lista con los números de albaran de compra
        if (venta.getOrigen() != null && !venta.getOrigen().isEmpty()) {
            if (!commonSetup.lPaisesVentas.contains(venta.getOrigen())) {
                commonSetup.lPaisesVentas.add(venta.getOrigen());
            }
        }
        // Lista con los números de albaran de compra fin
        if (venta.getOrigenFin() != null && !venta.getOrigenFin().isEmpty()) {
            if (!commonSetup.lPaisesVentasFin.contains(venta.getOrigenFin())) {
                commonSetup.lPaisesVentasFin.add(venta.getOrigenFin());
            }
        }

        // PROVEEDOR
        // Lista con los números de albaran de compra
        if (venta.getProveedor() != null && !venta.getProveedor().isEmpty()) {
            if (!commonSetup.lProveedoresVentasCompras.contains(venta.getProveedor())) {
                commonSetup.lProveedoresVentasCompras.add(venta.getProveedor());
            }
        }
        // Lista con los números de albaran de compra fin
        if (venta.getProveedorFin() != null && !venta.getProveedorFin().isEmpty()) {
            if (!commonSetup.lProveedoresVentasComprasFin.contains(venta.getProveedorFin())) {
                commonSetup.lProveedoresVentasComprasFin.add(venta.getProveedorFin());
            }
        }

        // LOTE
        // Lista con los números de albaran de compra
        if (venta.getLote() != null && !venta.getLote().isEmpty()) {
            if (!commonSetup.lLotesVentas.contains(venta.getLote())) {
                commonSetup.lLotesVentas.add(venta.getLote());
            }
        }
        // Lista con los números de albaran de compra fin
        if (venta.getLoteFin() != null && !venta.getLoteFin().isEmpty()) {
            if (!commonSetup.lLotesVentasFin.contains(venta.getLoteFin())) {
                commonSetup.lLotesVentasFin.add(venta.getLoteFin());
            }
        }

        // PALLET
        // Lista con los códigos de pallet
        if (venta.getIdPaleFin() != null && !venta.getIdPaleFin().isEmpty()) {
            if (!commonSetup.lCodsPallets.contains(venta.getIdPaleFin())) {
                commonSetup.lCodsPallets.add(venta.getIdPaleFin());
            }
        }
        // Lista con los lotes de pallet
        if (venta.getLoteMovAlmFi() != null && !venta.getLoteMovAlmFi().isEmpty()) {
            if (!commonSetup.lLotesPallets.contains(venta.getLoteMovAlmFi())) {
                commonSetup.lLotesPallets.add(venta.getLoteMovAlmFi());
            }
        }
        // PEDIDO VENTA
        // Lista con los nº de pedido de venta
        if (venta.getPedido() != null && !venta.getPedido().isEmpty()) {
            if (!commonSetup.lPedidosVentas.contains(venta.getPedido())) {
                commonSetup.lPedidosVentas.add(venta.getPedido());
            }
        }
        // Lista con los números de albaran de compra fin
        if (venta.getPedidoFin() != null && !venta.getPedidoFin().isEmpty()) {
            if (!commonSetup.lPedidosVentasFin.contains(venta.getPedidoFin())) {
                commonSetup.lPedidosVentasFin.add(venta.getPedidoFin());
            }
        }

        // NOMBRE DESCRIPTIVO
        // Lista con los nombres descriptivos de importaciones de venta
        if (venta.getNombreDescriptivo() != null && !venta.getNombreDescriptivo().isEmpty()) {
            if (!commonSetup.lNombresImportacionesVentas.contains(venta.getNombreDescriptivo())) {
                commonSetup.lNombresImportacionesVentas.add(venta.getNombreDescriptivo());
            }
        }

    }

    public void inicializarListasCompra() {

        if (commonSetup.lNumerosAlbaranCompras == null) {
            commonSetup.lNumerosAlbaranCompras = Utils.generarListaGenerica();
        } else {
            commonSetup.lNumerosAlbaranCompras.clear();
        }
        if (commonSetup.lNumerosAlbaranComprasFin == null) {
            commonSetup.lNumerosAlbaranComprasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lNumerosAlbaranComprasFin.clear();
        }
        if (commonSetup.lFamiliasCompras == null) {
            commonSetup.lFamiliasCompras = Utils.generarListaGenerica();
        } else {
            commonSetup.lFamiliasCompras.clear();
        }
        if (commonSetup.lFamiliasComprasFin == null) {
            commonSetup.lFamiliasComprasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lFamiliasComprasFin.clear();
        }
        if (commonSetup.lArticulosCompras == null) {
            commonSetup.lArticulosCompras = Utils.generarListaGenerica();
        } else {
            commonSetup.lArticulosCompras.clear();
        }
        if (commonSetup.lArticulosComprasFin == null) {
            commonSetup.lArticulosComprasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lArticulosComprasFin.clear();
        }
        if (commonSetup.lPaisesCompras == null) {
            commonSetup.lPaisesCompras = Utils.generarListaGenerica();
        } else {
            commonSetup.lPaisesCompras.clear();
        }
        if (commonSetup.lPaisesComprasFin == null) {
            commonSetup.lPaisesComprasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lPaisesComprasFin.clear();
        }
        if (commonSetup.lPartidasCompras == null) {
            commonSetup.lPartidasCompras = Utils.generarListaGenerica();
        } else {
            commonSetup.lPartidasCompras.clear();
        }
        if (commonSetup.lPartidasComprasFin == null) {
            commonSetup.lPartidasComprasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lPartidasComprasFin.clear();
        }
        if (commonSetup.lProveedoresCompras == null) {
            commonSetup.lProveedoresCompras = Utils.generarListaGenerica();
        } else {
            commonSetup.lProveedoresCompras.clear();
        }
        if (commonSetup.lProveedoresComprasFin == null) {
            commonSetup.lProveedoresComprasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lProveedoresComprasFin.clear();
        }
        if (commonSetup.lLotesCompras == null) {
            commonSetup.lLotesCompras = Utils.generarListaGenerica();
        } else {
            commonSetup.lLotesCompras.clear();
        }
        if (commonSetup.lLotesComprasFin == null) {
            commonSetup.lLotesComprasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lLotesComprasFin.clear();
        }
        if (commonSetup.lNombresImportacionesCompras == null) {
            commonSetup.lNombresImportacionesCompras = Utils.generarListaGenerica();
        } else {
            commonSetup.lNombresImportacionesCompras.clear();
        }
        if (commonSetup.lVariedades == null) {
            commonSetup.lVariedades = Utils.generarListaGenerica();
        } else {
            commonSetup.lVariedades.clear();
        }
        if (commonSetup.lVariedadesProductos == null) {
            commonSetup.lVariedadesProductos = Utils.generarListaGenerica();
        } else {
            commonSetup.lVariedadesProductos.clear();
        }
    }

    public void inicializarListasVenta() {
        if (commonSetup.lNumerosAlbaranVentas == null) {
            commonSetup.lNumerosAlbaranVentas = Utils.generarListaGenerica();
        } else {
            commonSetup.lNumerosAlbaranVentas.clear();
        }
        if (commonSetup.lNumerosAlbaranVentasFin == null) {
            commonSetup.lNumerosAlbaranVentasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lNumerosAlbaranVentasFin.clear();
        }
        if (commonSetup.lNumerosAlbaranVentasCompras == null) {
            commonSetup.lNumerosAlbaranVentasCompras = Utils.generarListaGenerica();
        } else {
            commonSetup.lNumerosAlbaranVentasCompras.clear();
        }
        if (commonSetup.lNumerosAlbaranVentasComprasFin == null) {
            commonSetup.lNumerosAlbaranVentasComprasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lNumerosAlbaranVentasComprasFin.clear();
        }
        if (commonSetup.lFamiliasVentas == null) {
            commonSetup.lFamiliasVentas = Utils.generarListaGenerica();
        } else {
            commonSetup.lFamiliasVentas.clear();
        }
        if (commonSetup.lFamiliasVentasFin == null) {
            commonSetup.lFamiliasVentasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lFamiliasVentasFin.clear();
        }

        if (commonSetup.lArticulosVentas == null) {
            commonSetup.lArticulosVentas = Utils.generarListaGenerica();
        } else {
            commonSetup.lArticulosVentas.clear();
        }
        if (commonSetup.lArticulosVentasFin == null) {
            commonSetup.lArticulosVentasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lArticulosVentasFin.clear();
        }
        if (commonSetup.lClientesVentas == null) {
            commonSetup.lClientesVentas = Utils.generarListaGenerica();
        } else {
            commonSetup.lClientesVentas.clear();
        }
        if (commonSetup.lClientesVentasFin == null) {
            commonSetup.lClientesVentasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lClientesVentasFin.clear();
        }
        if (commonSetup.lLotesEntradaVentas == null) {
            commonSetup.lLotesEntradaVentas = Utils.generarListaGenerica();
        } else {
            commonSetup.lLotesEntradaVentas.clear();
        }
        if (commonSetup.lLotesEntradaVentasFin == null) {
            commonSetup.lLotesEntradaVentasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lLotesEntradaVentasFin.clear();
        }
        if (commonSetup.lLotesVentas == null) {
            commonSetup.lLotesVentas = Utils.generarListaGenerica();
        } else {
            commonSetup.lLotesVentas.clear();
        }
        if (commonSetup.lLotesVentasFin == null) {
            commonSetup.lLotesVentasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lLotesVentasFin.clear();
        }

        if (commonSetup.lCodsPallets == null) {
            commonSetup.lCodsPallets = Utils.generarListaGenerica();
        } else {
            commonSetup.lCodsPallets.clear();
        }

        if (commonSetup.lLotesPallets == null) {
            commonSetup.lLotesPallets = Utils.generarListaGenerica();
        } else {
            commonSetup.lLotesPallets.clear();
        }

        if (commonSetup.lPaisesVentas == null) {
            commonSetup.lPaisesVentas = Utils.generarListaGenerica();
        } else {
            commonSetup.lPaisesVentas.clear();
        }
        if (commonSetup.lPaisesVentasFin == null) {
            commonSetup.lPaisesVentasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lPaisesVentasFin.clear();
        }

        if (commonSetup.lPedidosVentas == null) {
            commonSetup.lPedidosVentas = Utils.generarListaGenerica();
        } else {
            commonSetup.lPedidosVentas.clear();
        }
        if (commonSetup.lPedidosVentasFin == null) {
            commonSetup.lPedidosVentasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lPedidosVentasFin.clear();
        }

        if (commonSetup.lProveedoresVentasCompras == null) {
            commonSetup.lProveedoresVentasCompras = Utils.generarListaGenerica();
        } else {
            commonSetup.lProveedoresVentasCompras.clear();
        }
        if (commonSetup.lProveedoresVentasComprasFin == null) {
            commonSetup.lProveedoresVentasComprasFin = Utils.generarListaGenerica();
        } else {
            commonSetup.lProveedoresVentasComprasFin.clear();
        }

        if (commonSetup.lNombresImportacionesVentas == null) {
            commonSetup.lNombresImportacionesVentas = Utils.generarListaGenerica();
        } else {
            commonSetup.lNombresImportacionesVentas.clear();
        }
    }

    private int crearCompraRetornaId(TCompras record, Boolean importa) throws GenasoftException {

        Integer id = -1;
        // Comprobamos que no exista en el sistema la familia a crear.
        //TCompras aux = tComprasMapper.obtenerCompraPorAlbaranLote(record.getAlbaranFin(), record.getLoteFin());
        TCompras aux = comprasSetup.obtenerCompraPorEjercicioIdExterno(commonSetup.getEjercicio(), record.getIdExterno());

        if (aux != null) {
            id = aux.getId();
            logicaModificacionCompra(aux, record);
            return id;
        } else {
            // Miramos si existe una compra con el mismo lote, en ese caso nos quedamos con la primera compra y restamos los kgs de la última compra.
            TCompras aux2 = comprasSetup.obtenerCompraPorEjercicioLote(commonSetup.getEjercicio(), record.getLoteFin());
            if (aux2 != null) {
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                if (record.getId() == null) {
                    record.setId(aux2.getId());
                }
                cal1.setTime(record.getFechaFin());
                cal2.setTime(record.getFechaFin());
                record.setId(aux2.getId());
                if (record.getPesoNetoFin() < aux2.getPesoNetoFin()) {
                    System.out.println("LOTE: " + record.getLoteFin() + ", KGS INI: " + aux2.getPesoNetoFin() + ", KGS FIN: " + record.getPesoNetoFin());
                }
                if (!aux2.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                    record.setPesoNetoDisponible(aux2.getPesoNetoDisponible());
                } else {
                    record.setPesoNetoDisponible(Double.valueOf(0));
                }
                comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), record);
                return 1;
                /**
                // Restamos los kgs de la compra nueva a la vieja.
                if (cal1.getTime().before(cal2.getTime())) {
                    if (aux2.getPesoNetoFin() < record.getPesoNetoFin()) {
                        record.setPesoNetoFin(record.getPesoNetoFin() - aux2.getPesoNetoFin());
                        record.setPesoNetoDisponible(Utils.redondeoDecimales(2, record.getPesoNetoDisponible() - aux2.getPesoNetoDisponible()));
                    } else {
                        record.setPesoNetoFin(aux2.getPesoNetoFin() - record.getPesoNetoFin());
                        record.setPesoNetoDisponible(Utils.redondeoDecimales(2, aux2.getPesoNetoDisponible() - record.getPesoNetoDisponible()));
                    }
                    if (record.getPesoNetoDisponible() < Double.valueOf(0)) {
                        List<TVentas> lVentas = tVentasMapper.obtenerVentasLoteFin(record.getLoteFin());
                        if (lVentas != null) {
                            for (TVentas v : lVentas) {
                                if (Utils.booleanFromInteger(v.getCerrada())) {
                                    v.setCerrada(0);
                                    v.setIndError(1);
                                    tVentasMapper.updateByPrimaryKey(v);
                                }
                            }
                        }
                    }
                
                    tComprasMapper.updateByPrimaryKey(record);
                    return record.getId();
                } else if (cal1.getTime().equals(cal2.getTime())) {
                    if (record.getAlbaranFin().compareTo(aux2.getAlbaranFin()) < 0) {
                        // El albarán del record es "menor" que el del aux2
                        if (aux2.getPesoNetoFin() < record.getPesoNetoFin()) {
                            record.setPesoNetoFin(record.getPesoNetoFin() - aux2.getPesoNetoFin());
                            record.setPesoNetoDisponible(Utils.redondeoDecimales(2, record.getPesoNetoDisponible() - aux2.getPesoNetoDisponible()));
                        } else {
                            record.setPesoNetoFin(aux2.getPesoNetoFin() - record.getPesoNetoFin());
                            record.setPesoNetoDisponible(Utils.redondeoDecimales(2, aux2.getPesoNetoDisponible() - record.getPesoNetoDisponible()));
                        }
                        if (record.getPesoNetoDisponible() < Double.valueOf(0)) {
                            List<TVentas> lVentas = tVentasMapper.obtenerVentasLoteFin(record.getLoteFin());
                            if (lVentas != null) {
                                for (TVentas v : lVentas) {
                                    if (Utils.booleanFromInteger(v.getCerrada())) {
                                        v.setCerrada(0);
                                        v.setIndError(1);
                                        tVentasMapper.updateByPrimaryKey(v);
                                    }
                                }
                            }
                        }
                        tComprasMapper.updateByPrimaryKey(record);
                        return record.getId();
                    } else {
                        if (record.getPesoNetoFin() < aux2.getPesoNetoFin()) {
                            aux2.setPesoNetoFin(aux2.getPesoNetoFin() - record.getPesoNetoFin());
                            aux2.setPesoNetoDisponible(Utils.redondeoDecimales(2, aux2.getPesoNetoDisponible() - record.getPesoNetoDisponible()));
                        } else {
                            aux2.setPesoNetoFin(record.getPesoNetoFin());
                            aux2.setPesoNetoDisponible(Utils.redondeoDecimales(2, record.getPesoNetoDisponible() - aux2.getPesoNetoDisponible()));
                        }
                        if (aux2.getPesoNetoDisponible() < Double.valueOf(0)) {
                            List<TVentas> lVentas = tVentasMapper.obtenerVentasLoteFin(aux2.getLoteFin());
                            if (lVentas != null) {
                                for (TVentas v : lVentas) {
                                    if (Utils.booleanFromInteger(v.getCerrada())) {
                                        v.setCerrada(0);
                                        v.setIndError(1);
                                        tVentasMapper.updateByPrimaryKey(v);
                                    }
                                }
                            }
                        }
                        tComprasMapper.updateByPrimaryKey(aux2);
                        return aux2.getId();
                    }
                } else {
                    if (record.getPesoNetoFin() < aux2.getPesoNetoFin()) {
                        aux2.setPesoNetoFin(aux2.getPesoNetoFin() - record.getPesoNetoFin());
                        aux2.setPesoNetoDisponible(Utils.redondeoDecimales(2, aux2.getPesoNetoDisponible() - record.getPesoNetoDisponible()));
                    } else {
                        aux2.setPesoNetoFin(record.getPesoNetoFin());
                        aux2.setPesoNetoDisponible(Utils.redondeoDecimales(2, record.getPesoNetoDisponible() - aux2.getPesoNetoDisponible()));
                    }
                    if (aux2.getPesoNetoDisponible() < Double.valueOf(0)) {
                        List<TVentas> lVentas = tVentasMapper.obtenerVentasLoteFin(aux2.getLoteFin());
                        if (lVentas != null) {
                            for (TVentas v : lVentas) {
                                if (Utils.booleanFromInteger(v.getCerrada())) {
                                    v.setCerrada(0);
                                    v.setIndError(1);
                                    tVentasMapper.updateByPrimaryKey(v);
                                }
                            }
                        }
                    }
                    tComprasMapper.updateByPrimaryKey(aux2);
                    return aux2.getId();
                    
                }
                */
            } else {
                try {
                    // Rellenamos los datos necesarios para crear la familia.
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", 0);
                    map.put("albaran", record.getAlbaran());
                    map.put("albaranFin", record.getAlbaranFin());
                    map.put("lote", record.getLote());
                    map.put("loteFin", record.getLoteFin());
                    map.put("nombreDescriptivo", record.getNombreDescriptivo());
                    map.put("partida", record.getPartida());
                    map.put("partidaFin", record.getPartidaFin());
                    map.put("fecha", record.getFecha());
                    map.put("fechaFin", record.getFechaFin());
                    map.put("centro", record.getCentro());
                    map.put("centroFin", record.getCentroFin());
                    map.put("cajas", record.getCajas());
                    map.put("cajasFin", record.getCajasFin());
                    map.put("kgsBruto", record.getKgsBruto());
                    map.put("kgsBrutoFin", record.getKgsBrutoFin());
                    map.put("tipoCompra", record.getTipoCompra());
                    map.put("tipoCompraFin", record.getTipoCompraFin());
                    map.put("fechaCalibrado", record.getFechaCalibrado());
                    map.put("fechaCalibradoFin", record.getFechaCalibradoFin());
                    map.put("pesoNeto", record.getPesoNeto());
                    map.put("pesoNetoFin", record.getPesoNetoFin());
                    map.put("tara", record.getTara());
                    map.put("taraFin", record.getTaraFin());
                    map.put("finca", record.getFinca());
                    map.put("fincaFin", record.getFincaFin());
                    map.put("producto", record.getProducto());
                    map.put("productoFin", record.getProductoFin());
                    map.put("proveedor", record.getProveedor());
                    map.put("proveedorFin", record.getProveedorFin());
                    map.put("notasCalidad", record.getNotasCalidad());
                    map.put("notasCalidadFin", record.getNotasCalidadFin());
                    map.put("origen", record.getOrigen());
                    map.put("origenFin", record.getOrigenFin());
                    map.put("familia", record.getFamilia());
                    map.put("familiaFin", record.getFamiliaFin());
                    map.put("calidad", record.getCalidad());
                    map.put("calidadFin", record.getCalidadFin());
                    map.put("variedad", record.getVariedad());
                    map.put("variedadFin", record.getVariedadFin());
                    map.put("usuCrea", record.getUsuCrea());
                    map.put("fechaCrea", record.getFechaCrea());
                    map.put("ggn", record.getGgn());
                    map.put("ggnFin", record.getGgnFin());
                    map.put("pesoNetoDisponible", record.getPesoNetoDisponible());
                    map.put("cerrada", record.getCerrada());
                    map.put("calibre", record.getCalibre());
                    map.put("calibreFin", record.getCalibreFin());
                    map.put("idExterno", record.getIdExterno());

                    id = comprasSetup.insertRecordEjercicio(commonSetup.getEjercicio(), map);

                } catch (Exception e) {
                    id = -1;
                    log.error(Constants.BD_KO_CREA_COMPRA + ", Error al crear la compra: " + record.toString2() + ", ", e);
                }

                // Retornamos el resultado de la operación.
                return id;
            }
        }

    }

    private int crearCompraFictRetornaId(TComprasFict record) {

        Integer id = -1;

        try {
            // Rellenamos los datos necesarios para crear la familia.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("albaran", record.getAlbaran());
            map.put("albaranFin", record.getAlbaranFin());
            map.put("lote", record.getLote());
            map.put("loteFin", record.getLoteFin());
            map.put("nombreDescriptivo", record.getNombreDescriptivo());
            map.put("partida", record.getPartida());
            map.put("partidaFin", record.getPartidaFin());
            map.put("fecha", record.getFecha());
            map.put("fechaFin", record.getFechaFin());
            map.put("centro", record.getCentro());
            map.put("centroFin", record.getCentroFin());
            map.put("cajas", record.getCajas());
            map.put("cajasFin", record.getCajasFin());
            map.put("kgsBruto", record.getKgsBruto());
            map.put("kgsBrutoFin", record.getKgsBrutoFin());
            map.put("tipoCompra", record.getTipoCompra());
            map.put("tipoCompraFin", record.getTipoCompraFin());
            map.put("fechaCalibrado", record.getFechaCalibrado());
            map.put("fechaCalibradoFin", record.getFechaCalibradoFin());
            map.put("pesoNeto", record.getPesoNeto());
            map.put("pesoNetoFin", record.getPesoNetoFin());
            map.put("tara", record.getTara());
            map.put("taraFin", record.getTaraFin());
            map.put("finca", record.getFinca());
            map.put("fincaFin", record.getFincaFin());
            map.put("producto", record.getProducto());
            map.put("productoFin", record.getProductoFin());
            map.put("proveedor", record.getProveedor());
            map.put("proveedorFin", record.getProveedorFin());
            map.put("notasCalidad", record.getNotasCalidad());
            map.put("notasCalidadFin", record.getNotasCalidadFin());
            map.put("origen", record.getOrigen());
            map.put("origenFin", record.getOrigenFin());
            map.put("familia", record.getFamilia());
            map.put("familiaFin", record.getFamiliaFin());
            map.put("calidad", record.getCalidad());
            map.put("calidadFin", record.getCalidadFin());
            map.put("variedad", record.getVariedad());
            map.put("variedadFin", record.getVariedadFin());
            map.put("usuCrea", record.getUsuCrea());
            map.put("fechaCrea", record.getFechaCrea());
            map.put("ggn", record.getGgn());
            map.put("ggnFin", record.getGgnFin());
            map.put("pesoNetoDisponible", record.getPesoNetoDisponible());
            map.put("cerrada", record.getCerrada());
            map.put("idCompra", record.getIdCompra());
            map.put("calibre", record.getCalibre());
            map.put("calibreFin", record.getCalibreFin());

            tComprasFictMapper.insertRecord(map);

            id = (Integer) map.get("id");
        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_COMPRA + ", Error al crear la compra: " + record.toString2() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return id;
    }

    private int crearAbonoRetonaId(TAbonos record) {

        Integer id = -1;
        // Comprobamos que no exista en el sistema la familia a crear.
        TAbonos aux = tAbonosMapper.obtenerAbonoPorAlbaranLote(record.getAlbaranFin(), record.getLoteFin());
        if (aux != null) {
            id = aux.getId();
            // Guardamos el registro
            record.setId(aux.getId());
            tAbonosMapper.updateByPrimaryKey(record);
        } else {
            try {
                // Rellenamos los datos necesarios para crear la familia.
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", 0);
                map.put("albaran", record.getAlbaran());
                map.put("albaranFin", record.getAlbaranFin());
                map.put("lote", record.getLote());
                map.put("loteFin", record.getLoteFin());
                map.put("nombreDescriptivo", record.getNombreDescriptivo());
                map.put("partida", record.getPartida());
                map.put("partidaFin", record.getPartidaFin());
                map.put("fecha", record.getFecha());
                map.put("fechaFin", record.getFechaFin());
                map.put("centro", record.getCentro());
                map.put("centroFin", record.getCentroFin());
                map.put("cajas", record.getCajas());
                map.put("cajasFin", record.getCajasFin());
                map.put("kgsBruto", record.getKgsBruto());
                map.put("kgsBrutoFin", record.getKgsBrutoFin());
                map.put("tipoCompra", record.getTipoCompra());
                map.put("tipoCompraFin", record.getTipoCompraFin());
                map.put("fechaCalibrado", record.getFechaCalibrado());
                map.put("fechaCalibradoFin", record.getFechaCalibradoFin());
                map.put("pesoNeto", record.getPesoNeto());
                map.put("pesoNetoFin", record.getPesoNetoFin());
                map.put("tara", record.getTara());
                map.put("taraFin", record.getTaraFin());
                map.put("finca", record.getFinca());
                map.put("fincaFin", record.getFincaFin());
                map.put("producto", record.getProducto());
                map.put("productoFin", record.getProductoFin());
                map.put("proveedor", record.getProveedor());
                map.put("proveedorFin", record.getProveedorFin());
                map.put("notasCalidad", record.getNotasCalidad());
                map.put("notasCalidadFin", record.getNotasCalidadFin());
                map.put("origen", record.getOrigen());
                map.put("origenFin", record.getOrigenFin());
                map.put("familia", record.getFamilia());
                map.put("familiaFin", record.getFamiliaFin());
                map.put("calidad", record.getCalidad());
                map.put("calidadFin", record.getCalidadFin());
                map.put("variedad", record.getVariedad());
                map.put("variedadFin", record.getVariedadFin());
                map.put("usuCrea", record.getUsuCrea());
                map.put("fechaCrea", record.getFechaCrea());
                map.put("ggn", record.getGgn());
                map.put("ggnFin", record.getGgnFin());
                map.put("pesoNetoDisponible", record.getPesoNetoDisponible());
                map.put("calibre", record.getCalibre());
                map.put("calibreFin", record.getCalibreFin());

                tAbonosMapper.insertRecord(map);

                id = (Integer) map.get("id");
            } catch (Exception e) {
                id = -1;
                log.error(Constants.BD_KO_CREA_COMPRA + ", Error al crear el abono: " + record.toString2() + ", ", e);
            }
        }
        // Retornamos el resultado de la operación.
        return id;
    }

    public String guardarCompraVista(TComprasVista record) throws GenasoftException {
        String result = Constants.OPERACION_OK;

        TCompras aux = new TCompras();
        aux.copiaDesdeCompraVista(record);

        aux.setId(crearCompraRetornaId(aux, false));

        result = aux.getId() > 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_COMPRA;

        if (result.equals(Constants.OPERACION_OK)) {
            if (record.getProveedor() != null && !record.getProveedor().isEmpty()) {
                TProveedores prov = proveedoresSetup.obtenerProveedorPorNombre(record.getProveedor().toUpperCase());
                if (prov == null) {
                    prov = new TProveedores();
                    prov.setDescripcion(record.getProveedor().toUpperCase());
                    prov.setGgn("NO GG");
                    prov.setEstado(1);
                    proveedoresSetup.crearProveedor(prov);
                }
            }

            //nutrirDiccionariosCompras(aux);
        }

        return result;
    }

    public String guardarVentaVista(TVentasVista record, Boolean abrirLinea) throws GenasoftException {
        String result = Constants.OPERACION_OK;

        TVentas aux = new TVentas();
        aux.copiaDesdeVentaVista(record);

        aux.setId(crearVentaRetornaId(aux, false, abrirLinea));

        result = aux.getId() > 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_COMPRA;

        if (result.equals(Constants.OPERACION_OK)) {
            if (record.getProveedor() != null && !record.getProveedor().isEmpty()) {
                TProveedores prov = proveedoresSetup.obtenerProveedorPorNombre(record.getProveedor().toUpperCase());
                if (prov == null) {
                    prov = new TProveedores();
                    prov.setDescripcion(record.getProveedor().toUpperCase());
                    prov.setGgn("NO GG");
                    prov.setEstado(1);
                    proveedoresSetup.crearProveedor(prov);
                }
            }

            //nutrirDiccionariosVentas(aux);
        }

        return result;
    }

    public String guardarLineaTrazabilidadVentaVista(TLineasVentasVista record, Integer user) throws GenasoftException {
        String result = Constants.OPERACION_OK;

        TLineasVentas aux = new TLineasVentas();
        try {
            aux.copiaDesdeLineaVista(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer auxCompEj = commonSetup.getEjercicio();
        Integer auxVentEj = commonSetup.getEjercicio();

        // Buscamos la compra que nos ha indicado 
        TCompras compr = comprasSetup.obtenerCompraPorEjercicioLote(commonSetup.getEjercicio(), aux.getLoteFin().trim().toUpperCase());

        if (compr == null) {
            compr = comprasSetup.obtenerCompraPorEjercicioLote(commonSetup.getEjercicio() - 1, aux.getLoteFin().trim().toUpperCase());
            auxCompEj = commonSetup.getEjercicio() - 1;
        }
        if (compr == null) {
            compr = comprasSetup.obtenerCompraPorEjercicioLote(commonSetup.getEjercicio() - 2, aux.getLoteFin().trim().toUpperCase());
            auxCompEj = commonSetup.getEjercicio() - 2;
        }
        if (compr == null) {
            compr = comprasSetup.obtenerCompraPorEjercicioLote(commonSetup.getEjercicio() + 1, aux.getLoteFin().trim().toUpperCase());
            auxCompEj = commonSetup.getEjercicio() + 1;
        }
        if (compr == null) {
            compr = comprasSetup.obtenerCompraPorEjercicioLote(commonSetup.getEjercicio() + 2, aux.getLoteFin().trim().toUpperCase());
            auxCompEj = commonSetup.getEjercicio() - 2;
        }

        if (compr == null) {
            throw new GenasoftException("No se ha encontrado la compra con el lote indicado");
        }

        aux.setAlbaranCompraFin(compr.getAlbaranFin());
        aux.setProveedorFin(compr.getProveedorFin());
        aux.setGgn(compr.getGgnFin());

        // Buscamos la venta
        TVentas v = ventasSetup.obtenerVentaEjercicioPorId(commonSetup.getEjercicio(), Integer.valueOf(record.getIdVenta()));

        if (v == null) {
            v = ventasSetup.obtenerVentaEjercicioPorId(commonSetup.getEjercicio() - 1, Integer.valueOf(record.getIdVenta()));
            auxVentEj = commonSetup.getEjercicio() - 1;
        }

        if (v == null) {
            throw new GenasoftException("Se debe seleccionar la venta del listado.");
        }

        aux.setIdVenta(v.getId());
        aux.setIdVentaExterno(v.getIdExterno());

        if (aux.getId() < 0) {
            aux.setUsuCrea(user);
            aux.setFechaCrea(Utils.generarFecha());
            aux.setId(crearTrazabilidadVentaRetornaIdAPelo(aux));
            // Si llegamos a este punto, quiere decir que se ha grabado bien la línea de venta, con lo cual, tendremos que sumar los kgs.
            v.setKgsNetosFin(v.getKgsNetosFin() + aux.getKgsTrazadosFin());
        } else {
            TLineasVentas lv = lineasVentasSetup.obtenerLineaVentaEjercicioPorId(auxVentEj, aux.getId());
            lineasVentasSetup.modificarLineaVentaEjercicio(commonSetup.getEjercicio(), aux);
            if (!lv.getKgsTrazadosFin().equals(aux.getKgsTrazadosFin())) {
                // Se han modificado los kgs, con lo cual, tendremos que subir o bajar los de la venta.
                Double diff = aux.getKgsTrazadosFin() - lv.getKgsTrazadosFin();
                // Si llegamos a este punto, quiere decir que se ha grabado bien la línea de venta, con lo cual, tendremos que sumar los kgs.
                v.setKgsNetosFin(v.getKgsNetosFin() + diff);
            }
        }

        if (v.getKgsNetos().equals(v.getKgsNetosFin()) && v.getCerrada().equals(0)) {
            v.setCerrada(1);
            v.setIndError(0);
            ventasSetup.modificarVentaEjercicio(auxVentEj, v);
            TVentasVista vVista = new TVentasVista();
            vVista.copiaDesdeVenta(v);
            vVista.setIndError("No");
            vVista.setError("0");
            commonSetup.mIdVentasErroneas.remove(v.getId());
            commonSetup.getmVentasEjercicio().put(vVista.getId(), vVista);

            rellenarArbolVentas();

            compr.setPesoNetoDisponible(compr.getPesoNetoDisponible() - aux.getKgsTrazadosFin());
            if (compr.getPesoNetoDisponible() <= compr.getPesoNetoFin() * 7 / 100) {
                compr.setCerrada(1);
            }
            // Guardamos la compra en el diccionario
            TComprasVista cVista2 = new TComprasVista();
            cVista2.copiaDesdeCompra(compr);

            commonSetup.mComprasId.put(cVista2.getId(), cVista2);
            comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), compr);
            commonSetup.getmCompras().put("" + compr.getId(), compr);

            rellenarArbolCompras();
        }

        result = aux.getId() > 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_COMPRA;

        return result;
    }

    public String guardarVentaFictVista(TVentasFictVista record, Boolean abrirLinea) {
        String result = Constants.OPERACION_OK;

        TVentasFict aux = new TVentasFict();
        aux.copiaDesdeVentaFictVista(record);

        aux.setId(Integer.valueOf(record.getId()));

        tVentasFictMapper.updateByPrimaryKey(aux);

        return result;
    }

    private int crearVentaRetornaId(TVentas record, Boolean importacion, Boolean abrirVenta) throws GenasoftException {

        Integer id = -1;
        // Comprobamos que no exista en el sistema la familia a crear.
        TVentas aux = null;

        if (record.getIndBasura() == null) {
            record.setIndBasura(0);
        }

        // SI ES BASURA, BUSCAMOS POR ALBARÁN DE COMPRA Y SUMAMOS LOS KGS.
        if (record.getVariedadFin().toLowerCase().contains("basura")) {
            try {
                // En este caso no buscamos por id externo ya que no existe en fruttec, y al ser basura, solo tendremos una línea de basura por lote dentro de la misma venta.
                aux = ventasSetup.obtenerVentaEjercicioNumPedidoAlbaranCompraLote(commonSetup.getEjercicio(), record.getPedidoFin(), record.getAlbaranCompraFin(), record.getLoteFin());
                //aux = tVentasMapper.obtenerVentaPorIdExterno(record.getIdExterno());
            } catch (Exception e) {
                log.error("Error");
                log.error("ERROR en la linea con los datos: PEDIDO_FIN: " + record.getPedidoFin() + ", ALBARAN_COMPRA_FIN: " + record.getAlbaranCompraFin() + ", LOTE_FIN" + record.getLoteFin());
                throw e;
            }
            if (aux != null) {
                record.setKgsFin(record.getKgsFin() + aux.getKgsFin());
                record.setKgs(record.getKgs() + aux.getKgs());
            }
            record.setCerrada(1);
        } else {
            // Si no viene de importación, miramos los 2 lotes, ya que el lote fin se ha podido mofificar desde la aplicación
            if (!importacion) {
                try {
                    //aux = tVentasMapper.obtenerVentaNumPedidoAlbaranLoteIdPale(record.getPedidoFin(), record.getAlbaranFin(), record.getLote(), record.getLoteFin(), record.getIdPaleFin());
                    aux = ventasSetup.obtenerVentaEjercicioPorIdExterno(commonSetup.getEjercicio(), record.getIdExterno());
                } catch (Exception e) {
                    log.error(
                              "ERROR en la linea con los datos: PEDIDO_FIN: " + record.getPedidoFin() + ", ALBARAN_FIN: " + record.getAlbaranFin() + ", LOTE " + record.getLote() + ", LOTE_FIN " + record.getLoteFin() + ", PALE_FIN "
                                      + record.getIdPaleFin());
                    throw e;
                }
            } else {
                try {
                    // Si viene desde importación, no miramos el lote fin, ya que se ha podido modificar desde la aplicación y es posible que duplique la linea                    
                    //aux = tVentasMapper.obtenerVentaNumPedidoAlbaranCompraLoteIdPale(record.getPedido(), record.getAlbaran(), record.getLote(), record.getIdPale());
                    aux = ventasSetup.obtenerVentaEjercicioPorIdExterno(commonSetup.getEjercicio(), record.getIdExterno());
                } catch (Exception e) {
                    log.error("Error");
                    log.error("ERROR en la linea con los datos: PEDIDO: " + record.getPedido() + ", ALBARAN_FIN: " + record.getAlbaranFin() + ", LOTE_FIN" + record.getLoteFin());
                    throw e;
                }
            }
        }
        if (aux != null) {
            id = aux.getId();
            // Si está cerrada no modificamos nada.
            if (Utils.booleanFromInteger(aux.getCerrada()) && !abrirVenta) {
                return id;
            } else if (Utils.booleanFromInteger(aux.getCerrada())) {
                record.setCerrada(0);
            }

            if (importacion) {
                if (!Utils.booleanFromInteger(aux.getCerrada())) {
                    record.setId(aux.getId());
                    ventasSetup.modificarVentaEjercicio(commonSetup.getEjercicio(), record);
                }
            } else {
                TCompras compra = null;
                // Miramos a ver qué ha cambiado, para ver si hay que abrir la compra o no.
                // Si se modifican los kgs a menores se abre la compra, independientemente de cla calidad, etc.
                if (!record.getKgsNetosFin().equals(aux.getKgsNetosFin()) && record.getKgsNetosFin() < aux.getKgsNetosFin() || !record.getCalidadVentaFin().equals(aux.getCalidadVentaFin())) {
                    // Si los kgs de la venta se modifica a mayores, no tocamos la compra.
                    if (Utils.booleanFromInteger(record.getCerrada())) {
                        record.setCerrada(0);
                    }
                    // Obtenemos las líneas de venta para eliminarlas y sumar los kgs a las compras
                    List<TLineasVentas> lVentas = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), record.getIdExterno());
                    // Por cada compra, sumamos kgs disponibles, y abrimos si corresponde.
                    for (TLineasVentas lv : lVentas) {
                        compra = comprasSetup.obtenerCompraPorEjercicioLote(commonSetup.getEjercicio(), lv.getLoteFin());
                        compra.setPesoNetoDisponible(compra.getPesoNetoDisponible() + lv.getKgsTrazadosFin());
                        if (Utils.booleanFromInteger(compra.getCerrada())) {
                            compra.setCerrada(0);
                        }
                        comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), compra);
                        // Eliminamos la línea de venta
                        lineasVentasSetup.eliminarLineaVentaEjercicio(commonSetup.getEjercicio(), lv.getId());
                        record.setLoteFin("");
                    }
                    // Dejamos los kgs finales a cero en la vneta
                    record.setKgsNetosFin(Double.valueOf(0));

                } else if (!record.getKgsNetosFin().equals(aux.getKgsNetosFin()) && record.getKgsNetosFin() > aux.getKgsNetosFin()) {
                    // Si los kgs de la venta se modifica a mayores, no tocamos la compra.
                    if (Utils.booleanFromInteger(record.getCerrada())) {
                        record.setCerrada(0);
                    }

                }
                // Guardamos el registro
                record.setId(aux.getId());
                ventasSetup.modificarVentaEjercicio(commonSetup.getEjercicio(), record);
                List<Integer> lIDs = Utils.generarListaGenerica();
                lIDs.addAll(commonSetup.mIdVentasErroneas.keySet());
                //if (!lIDs.contains(record.getId())) {
                commonSetup.mIdVentasErroneas.put(record.getId(), record);
                ///}
            }
        } else {
            try {
                // Rellenamos los datos necesarios para crear la familia.
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", 0);
                map.put("idExterno", record.getIdExterno());
                map.put("pedido", record.getPedido());
                map.put("pedidoFin", record.getPedidoFin());
                map.put("albaran", record.getAlbaran());
                map.put("nombreDescriptivo", record.getNombreDescriptivo());
                map.put("albaranFin", record.getAlbaranFin());
                map.put("calibre", record.getCalibre());
                map.put("calibreFin", record.getCalibreFin());
                map.put("lote", record.getLote());
                map.put("loteFin", record.getLoteFin());
                map.put("idPale", record.getIdPale());
                map.put("idPaleFin", record.getIdPaleFin());
                map.put("numBultos", record.getNumBultos());
                map.put("numBultosFin", record.getNumBultosFin());
                map.put("numBultosPale", record.getNumBultosPale());
                map.put("numBultosPaleFin", record.getNumBultosPaleFin());
                map.put("proveedor", record.getProveedor());
                map.put("proveedorFin", record.getProveedorFin());
                map.put("cliente", record.getCliente());
                map.put("clienteFin", record.getClienteFin());
                map.put("fechaVenta", record.getFechaVenta());
                map.put("fechaVentaFin", record.getFechaVentaFin());
                map.put("kgs", record.getKgs());
                map.put("kgsFin", record.getKgsFin());
                map.put("kgsTrazados", record.getKgsTrazados());
                map.put("kgsNetos", record.getKgsNetos());
                map.put("kgsNetosFin", record.getKgsNetosFin());
                map.put("kgsEnvase", record.getKgsEnvase());
                map.put("kgsEnvaseFin", record.getKgsEnvaseFin());
                map.put("variedad", record.getVariedad());
                map.put("variedadFin", record.getVariedadFin());
                map.put("origen", record.getOrigen());
                map.put("origenFin", record.getOrigenFin());
                map.put("calidadCompra", record.getCalidadCompra());
                map.put("calidadCompraFin", record.getCalidadCompraFin());
                map.put("calidadVenta", record.getCalidadVenta());
                map.put("calidadVentaFin", record.getCalidadVentaFin());
                map.put("referencia", record.getReferencia());
                map.put("referenciaFin", record.getReferenciaFin());
                map.put("albaranCompra", record.getAlbaranCompra());
                map.put("albaranCompraFin", record.getAlbaranCompraFin());
                map.put("loteMovAlm", record.getLoteMovAlm());
                map.put("loteMovAlmFi", record.getLoteMovAlmFi());
                map.put("confeccion", record.getConfeccion());
                map.put("confeccionFin", record.getConfeccionFin());
                map.put("envase", record.getEnvase());
                map.put("envaseFin", record.getEnvaseFin());
                map.put("undConsumo", record.getUndConsumo());
                map.put("undConsumoFin", record.getUndConsumoFin());
                map.put("lineaPedidoLote", record.getLineaPedidoLote());
                map.put("lineaPedidoLoteFin", record.getLineaPedidoLoteFin());
                map.put("lineaPedidoLoteCaja", record.getLineaPedidoLoteCaja());
                map.put("lineaPedidoLoteCajaFin", record.getLineaPedidoLoteCajaFin());
                map.put("familia", record.getFamilia());
                map.put("familiaFin", record.getFamiliaFin());
                map.put("usuCrea", record.getUsuCrea());
                map.put("fechaCrea", record.getFechaCrea());
                map.put("indNueva", record.getIndNueva());
                map.put("unidadesTarrinas", record.getUnidadesTarrinas());
                map.put("cerrada", record.getCerrada());
                map.put("indBasura", record.getIndBasura());
                map.put("ggn", record.getGgn());
                map.put("plantilla", record.getPlantilla());
                map.put("plantillaFin", record.getPlantillaFin());
                map.put("fechaSalida", record.getFechaSalida());
                map.put("fechaSalidaFin", record.getFechaSalidaFin());
                map.put("codCliente", record.getCodCliente());
                map.put("codClienteFin", record.getCodClienteFin());
                map.put("centroDescarga", record.getCentroDescarga());
                map.put("centroDescargaFin", record.getCentroDescargaFin());
                map.put("dirDescarga", record.getDirDescarga());
                map.put("dirDescargaFin", record.getDirDescargaFin());
                map.put("categoria", record.getCategoria());
                map.put("categoriaFin", record.getCategoriaFin());
                map.put("marca", record.getMarca());
                map.put("marcaFin", record.getMarcaFin());
                map.put("unidades", record.getUnidades());
                map.put("unidadesFin", record.getUnidadesFin());
                map.put("pale", record.getPale());
                map.put("paleFin", record.getPaleFin());
                map.put("nPale", record.getnPale());
                map.put("nPaleFin", record.getnPaleFin());
                map.put("direccionCliente", record.getDireccionCliente());
                map.put("direccionClienteFin", record.getDireccionClienteFin());
                map.put("cifCliente", record.getCifCliente());
                map.put("cifClienteFin", record.getCifClienteFin());
                map.put("telefonoCliente", record.getTelefonoCliente());
                map.put("telefonoClienteFin", record.getTelefonoClienteFin());
                map.put("transportista", record.getTransportista());
                map.put("transportistaFin", record.getTransportistaFin());
                map.put("indError", record.getIndError());
                map.put("numTrazabilidades", record.getNumTrazabilidades());
                map.put("lineaPedido", record.getLineaPedido());

                id = ventasSetup.insertRecordEjercicioVenta(commonSetup.getEjercicio(), map);

            } catch (Exception e) {
                id = -1;
                log.error(Constants.BD_KO_CREA_VENTA + ", Error al crear la venta: " + record.toString2() + ", ", e);
            }
        }
        // Retornamos el resultado de la operación.
        return id;
    }

    private int crearTrazabilidadVentaRetornaId(TLineasVentas record, Boolean importacion, Boolean abrirVenta) throws GenasoftException {

        Integer id = -1;
        // Comprobamos que no exista en el sistema la familia a crear.
        TLineasVentas aux = null;

        if (record.getIndBasura() == null) {
            record.setIndBasura(0);
        }

        Boolean existeLoteVenta = false;

        // SI ES BASURA, BUSCAMOS POR ALBARÁN DE COMPRA Y SUMAMOS LOS KGS.
        if (Utils.booleanFromInteger(record.getIndBasura())) {
            try {
                aux = lineasVentasSetup.obtenerLineaVentaBasuraEjercicioIdVentaLoteFin(commonSetup.getEjercicio(), record.getIdVentaExterno(), record.getLoteFin());
            } catch (Exception e) {
                log.error("Error");
                log.error("ERROR en la linea de ventas de basura con los datos: ID_LINEA_VENTA: " + record.getIdExterno() + ", LOTE_FIN: " + record.getLoteFin());
                throw new GenasoftException("ERROR en la linea de ventas de basura con los datos: ID_LINEA_VENTA: " + record.getIdExterno() + ", LOTE_FIN: " + record.getLoteFin());
            }
            if (aux != null) {
                record.setKgsTrazabilidadFin(record.getKgsTrazabilidadFin() + aux.getKgsTrazabilidadFin());
            }
        } else {
            // Si no viene de importación, miramos por ID, si tiene si tiene ID sumamos los kgs, si no, creamos
            if (!importacion) {
                if (record.getId() != null && !record.getId().equals(-1)) {
                    aux = lineasVentasSetup.obtenerLineaVentaEjercicioPorId(commonSetup.getEjercicio(), id);
                } else {
                    // Buscamos si existe línea de Venta en la venta y el lote, solo puede haber una linea de venta con el mismo lote
                    // si la hay, sumamos los kgs y tan amigos...
                    try {
                        aux = lineasVentasSetup.obtenerLineaVentaBasuraEjercicioIdVentaLoteFin(commonSetup.getEjercicio(), record.getIdVentaExterno(), record.getLoteFin());
                    } catch (Exception e) {
                    }
                    if (aux != null) {
                        existeLoteVenta = true;
                    }
                }
            } else {
                try {
                    if (!record.getIdExterno().equals(Double.valueOf(-1))) {
                        // Si viene desde importación, miramos si existe por cod_externo
                        aux = lineasVentasSetup.obtenerLineaVentaEjercicioPorIdExterno(commonSetup.getEjercicio(), record.getIdExterno());
                        if (aux == null) {
                            // Aunque venga de importaciçon hay que mirar si existe por lote y venta
                            aux = lineasVentasSetup.obtenerLineaVentaBasuraEjercicioIdVentaLoteFin(commonSetup.getEjercicio(), record.getIdVentaExterno(), record.getLoteFin());
                        }
                    }
                } catch (Exception e) {
                    log.error("Error");
                    log.error("ERROR en la linea con los datos: ID EXTERNO: " + record.getIdExterno());
                    throw e;
                }
            }
        }
        if (aux != null) {
            if (importacion) {
                // Si existe el registro, sumamos los kgs, guardamos la línea y sin más.
                aux.setKgsTrazabilidadFin(Utils.redondeoDecimales(2, aux.getKgsTrazabilidadFin() + record.getKgsTrazabilidadFin()));
                aux.setKgsTrazabilidadIni(Utils.redondeoDecimales(2, aux.getKgsTrazabilidadIni() + record.getKgsTrazabilidadIni()));
                aux.setKgsTrazadosFin(Utils.redondeoDecimales(2, aux.getKgsTrazadosFin() + record.getKgsTrazadosFin()));
                aux.setKgsTrazadosIni(Utils.redondeoDecimales(2, aux.getKgsTrazadosIni() + record.getKgsTrazadosIni()));
                lineasVentasSetup.modificarLineaVentaEjercicio(commonSetup.getEjercicio(), aux);
                return aux.getId();
            } else {
                // Si no viene de importación y existe lote dentro de la venta, sumamos los kgs y tan amigos
                if (existeLoteVenta) {
                    aux.setKgsTrazabilidadFin(Utils.redondeoDecimales(2, aux.getKgsTrazabilidadFin() + record.getKgsTrazabilidadFin()));
                    aux.setKgsTrazabilidadIni(Utils.redondeoDecimales(2, aux.getKgsTrazabilidadIni() + record.getKgsTrazabilidadIni()));
                    aux.setKgsTrazadosFin(Utils.redondeoDecimales(2, aux.getKgsTrazadosFin() + record.getKgsTrazadosFin()));
                    aux.setKgsTrazadosIni(Utils.redondeoDecimales(2, aux.getKgsTrazadosIni() + record.getKgsTrazadosIni()));
                }
                lineasVentasSetup.modificarLineaVentaEjercicio(commonSetup.getEjercicio(), aux);
            }
        } else {
            try {
                // Rellenamos los datos necesarios para crear la familia.
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", 0);
                map.put("idExterno", record.getIdExterno());
                map.put("idVentaExterno", record.getIdVentaExterno());
                map.put("idVenta", record.getIdVenta());
                map.put("proveedorIni", record.getProveedorIni());
                map.put("proveedorFin", record.getProveedorFin());
                map.put("certificacionIni", record.getCertificacionIni());
                map.put("certificacionFin", record.getCertificacionFin());
                map.put("referenciaCompraIni", record.getReferenciaCompraIni());
                map.put("referenciaCompraFin", record.getReferenciaCompraFin());
                map.put("kgsTrazabilidadIni", record.getKgsTrazadosIni());
                map.put("kgsTrazabilidadFin", record.getKgsTrazadosFin());
                map.put("kgsTrazadosIni", record.getKgsTrazadosIni());
                map.put("kgsTrazadosFin", record.getKgsTrazadosFin());
                map.put("albaranCompraIni", record.getAlbaranCompraIni());
                map.put("albaranCompraFin", record.getAlbaranCompraFin());
                map.put("loteIni", record.getLoteIni());
                map.put("loteFin", record.getLoteFin());
                map.put("bultosPaleIni", record.getBultosPaleIni());
                map.put("bultosPaleFin", record.getBultosPaleFin());
                map.put("indBasura", record.getIndBasura());
                map.put("indError", record.getIndError());
                map.put("indCambio", record.getIndCambio());
                map.put("indCerrada", record.getIndCerrada());
                map.put("detalleError", record.getDetalleError());
                map.put("variedadIni", record.getVariedadIni());
                map.put("variedadFin", record.getVariedadFin());
                map.put("familiaIni", record.getFamiliaIni());
                map.put("familiaFin", record.getFamiliaFin());
                map.put("ggn", record.getGgn());

                id = lineasVentasSetup.insertRecordEjercicioLinea(commonSetup.getEjercicio(), map);

            } catch (Exception e) {
                id = -1;
                log.error(Constants.BD_KO_CREA_VENTA + ", Error al crear la venta: " + record.toString2() + ", ", e);
            }
        }
        /**
        // Miramos la venta de la que depende, y miramos si los kgs son los que marca la venta, y si es así la cerramos y tan amigos.
        // Miramos los Kgs de todas las líneas de venta que no sea error.
        Double kgs = tLineasVentasMapper.obtenerKgsCorrectosVenta(record.getIdVenta());
        if (kgs == null) {
            kgs = Double.valueOf(0);
        }
        TVentas v = tVentasMapper.obtenerVentaPorIdExterno(record.getIdVenta());
        if (kgs.equals(v.getKgsNetosFin()) && !Utils.booleanFromInteger(v.getIndCerrada())) {
            v.setIndCerrada(1);
            tVentasMapper.updateByPrimaryKey(v);
        } else if (!kgs.equals(v.getKgsNetosFin()) && Utils.booleanFromInteger(v.getIndCerrada())) {
            v.setIndCerrada(0);
            tVentasMapper.updateByPrimaryKey(v);
        }
        */
        // Retornamos el resultado de la operación.
        return id;
    }

    private int crearTrazabilidadVentaRetornaIdAPelo(TLineasVentas record) throws GenasoftException {

        Integer id = -1;

        if (record.getIndBasura() == null) {
            record.setIndBasura(0);
        }

        try {
            // Rellenamos los datos necesarios para crear la familia.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("idExterno", record.getIdExterno());
            map.put("idVentaExterno", record.getIdVentaExterno());
            map.put("idVenta", record.getIdVenta());
            map.put("proveedorIni", record.getProveedorIni());
            map.put("proveedorFin", record.getProveedorFin());
            map.put("certificacionIni", record.getCertificacionIni());
            map.put("certificacionFin", record.getCertificacionFin());
            map.put("referenciaCompraIni", record.getReferenciaCompraIni());
            map.put("referenciaCompraFin", record.getReferenciaCompraFin());
            map.put("kgsTrazabilidadIni", record.getKgsTrazadosIni());
            map.put("kgsTrazabilidadFin", record.getKgsTrazadosFin());
            map.put("kgsTrazadosIni", record.getKgsTrazadosIni());
            map.put("kgsTrazadosFin", record.getKgsTrazadosFin());
            map.put("albaranCompraIni", record.getAlbaranCompraIni());
            map.put("albaranCompraFin", record.getAlbaranCompraFin());
            map.put("loteIni", record.getLoteIni());
            map.put("loteFin", record.getLoteFin());
            map.put("bultosPaleIni", record.getBultosPaleIni());
            map.put("bultosPaleFin", record.getBultosPaleFin());
            map.put("indBasura", record.getIndBasura());
            map.put("indError", record.getIndError());
            map.put("indCambio", record.getIndCambio());
            map.put("indCerrada", record.getIndCerrada());
            map.put("detalleError", record.getDetalleError());
            map.put("variedadIni", record.getVariedadIni());
            map.put("variedadFin", record.getVariedadFin());
            map.put("familiaIni", record.getFamiliaIni());
            map.put("familiaFin", record.getFamiliaFin());
            map.put("ggn", record.getGgn());

            id = lineasVentasSetup.insertRecordEjercicioLinea(commonSetup.getEjercicio(), map);

        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_VENTA + ", Error al crear la venta: " + record.toString2() + ", ", e);
        }

        /**
        // Miramos la venta de la que depende, y miramos si los kgs son los que marca la venta, y si es así la cerramos y tan amigos.
        // Miramos los Kgs de todas las líneas de venta que no sea error.
        Double kgs = tLineasVentasMapper.obtenerKgsCorrectosVenta(record.getIdVenta());
        if (kgs == null) {
            kgs = Double.valueOf(0);
        }
        TVentas v = tVentasMapper.obtenerVentaPorIdExterno(record.getIdVenta());
        if (kgs.equals(v.getKgsNetosFin()) && !Utils.booleanFromInteger(v.getIndCerrada())) {
            v.setIndCerrada(1);
            tVentasMapper.updateByPrimaryKey(v);
        } else if (!kgs.equals(v.getKgsNetosFin()) && Utils.booleanFromInteger(v.getIndCerrada())) {
            v.setIndCerrada(0);
            tVentasMapper.updateByPrimaryKey(v);
        }
        */
        // Retornamos el resultado de la operación.
        return id;
    }

    private int crearLineaDevolucionRetornaId(TLineasDevoluciones record) {

        Integer id = -1;
        // Comprobamos que no exista en el sistema la familia a crear.
        TLineasDevoluciones aux = null;

        if (record.getIndBasura() == null) {
            record.setIndBasura(0);
        }

        // Si no viene de importación, miramos por ID, si tiene si tiene ID sumamos los kgs, si no, creamos

        try {
            // Si viene desde importación, miramos si existe por cod_externo
            aux = tLineasDevolucionesMapper.obtenerLineaDevolucionPorIdExterno(record.getIdExterno());
        } catch (Exception e) {
            log.error("Error");
            log.error("ERROR en la linea con los datos: ID EXTERNO: " + record.getIdExterno());
            throw e;
        }

        if (aux != null) {
            // Si existe el registro, guardamos la línea y sin más.
            record.setId(aux.getId());
            record.setIdExterno(aux.getIdExterno());
            tLineasDevolucionesMapper.updateByPrimaryKey(record);
            return record.getId();
        } else {
            try {
                // Rellenamos los datos necesarios para crear la familia.
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", 0);
                map.put("idExterno", record.getIdExterno());
                map.put("idDevolucion", record.getIdDevolucion());
                map.put("proveedorIni", record.getProveedorIni());
                map.put("proveedorFin", record.getProveedorFin());
                map.put("certificacionIni", record.getCertificacionIni());
                map.put("certificacionFin", record.getCertificacionFin());
                map.put("referenciaCompraIni", record.getReferenciaCompraIni());
                map.put("referenciaCompraFin", record.getReferenciaCompraFin());
                map.put("kgsTrazabilidadIni", record.getKgsTrazadosIni());
                map.put("kgsTrazabilidadFin", record.getKgsTrazadosFin());
                map.put("kgsTrazadosIni", record.getKgsTrazadosIni());
                map.put("kgsTrazadosFin", record.getKgsTrazadosFin());
                map.put("albaranCompraIni", record.getAlbaranCompraIni());
                map.put("albaranCompraFin", record.getAlbaranCompraFin());
                map.put("loteIni", record.getLoteIni());
                map.put("loteFin", record.getLoteFin());
                map.put("bultosPaleIni", record.getBultosPaleIni());
                map.put("bultosPaleFin", record.getBultosPaleFin());
                map.put("indBasura", record.getIndBasura());
                map.put("indError", record.getIndError());
                map.put("indCambio", record.getIndCambio());
                map.put("indCerrada", record.getIndCerrada());

                tLineasDevolucionesMapper.insertRecord(map);

                id = (Integer) map.get("id");

            } catch (Exception e) {
                id = -1;
                log.error(Constants.BD_KO_CREA_VENTA + ", Error al crear la línea de devolucion: " + record.toString2() + ", ", e);
            }
        }
        /**
        // Miramos la venta de la que depende, y miramos si los kgs son los que marca la venta, y si es así la cerramos y tan amigos.
        // Miramos los Kgs de todas las líneas de venta que no sea error.
        Double kgs = tLineasVentasMapper.obtenerKgsCorrectosVenta(record.getIdVenta());
        if (kgs == null) {
            kgs = Double.valueOf(0);
        }
        TVentas v = tVentasMapper.obtenerVentaPorIdExterno(record.getIdVenta());
        if (kgs.equals(v.getKgsNetosFin()) && !Utils.booleanFromInteger(v.getIndCerrada())) {
            v.setIndCerrada(1);
            tVentasMapper.updateByPrimaryKey(v);
        } else if (!kgs.equals(v.getKgsNetosFin()) && Utils.booleanFromInteger(v.getIndCerrada())) {
            v.setIndCerrada(0);
            tVentasMapper.updateByPrimaryKey(v);
        }
        */
        // Retornamos el resultado de la operación.
        return id;
    }

    private int crearVentaFicticiaRetornaId(TVentasFict record) {

        Integer id = -1;

        try {
            // Rellenamos los datos necesarios para crear la familia.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("pedido", record.getPedido());
            map.put("pedidoFin", record.getPedidoFin());
            map.put("albaran", record.getAlbaran());
            map.put("nombreDescriptivo", record.getNombreDescriptivo());
            map.put("albaranFin", record.getAlbaranFin());
            map.put("calibre", record.getCalibre());
            map.put("calibreFin", record.getCalibreFin());
            map.put("lote", record.getLote());
            map.put("loteFin", record.getLoteFin());
            map.put("idPale", record.getIdPale());
            map.put("idPaleFin", record.getIdPaleFin());
            map.put("numBultos", record.getNumBultos());
            map.put("numBultosFin", record.getNumBultosFin());
            map.put("numBultosPale", record.getNumBultosPale());
            map.put("numBultosPaleFin", record.getNumBultosPaleFin());
            map.put("proveedor", record.getProveedor());
            map.put("proveedorFin", record.getProveedorFin());
            map.put("cliente", record.getCliente());
            map.put("clienteFin", record.getClienteFin());
            map.put("fechaVenta", record.getFechaVenta());
            map.put("fechaVentaFin", record.getFechaVentaFin());
            map.put("kgs", record.getKgs());
            map.put("kgsFin", record.getKgsFin());
            map.put("kgsNetos", record.getKgsNetos());
            map.put("kgsNetosFin", record.getKgsNetosFin());
            map.put("kgsEnvase", record.getKgsEnvase());
            map.put("kgsEnvaseFin", record.getKgsEnvaseFin());
            map.put("variedad", record.getVariedad());
            map.put("variedadFin", record.getVariedadFin());
            map.put("origen", record.getOrigen());
            map.put("origenFin", record.getOrigenFin());
            map.put("calidadCompra", record.getCalidadCompra());
            map.put("calidadCompraFin", record.getCalidadCompraFin());
            map.put("calidadVenta", record.getCalidadVenta());
            map.put("calidadVentaFin", record.getCalidadVentaFin());
            map.put("referencia", record.getReferencia());
            map.put("referenciaFin", record.getReferenciaFin());
            map.put("albaranCompra", record.getAlbaranCompra());
            map.put("albaranCompraFin", record.getAlbaranCompraFin());
            map.put("loteMovAlm", record.getLoteMovAlm());
            map.put("loteMovAlmFi", record.getLoteMovAlmFi());
            map.put("confeccion", record.getConfeccion());
            map.put("confeccionFin", record.getConfeccionFin());
            map.put("envase", record.getEnvase());
            map.put("envaseFin", record.getEnvaseFin());
            map.put("undConsumo", record.getUndConsumo());
            map.put("undConsumoFin", record.getUndConsumoFin());
            map.put("lineaPedidoLote", record.getLineaPedidoLote());
            map.put("lineaPedidoLoteFin", record.getLineaPedidoLoteFin());
            map.put("lineaPedidoLoteCaja", record.getLineaPedidoLoteCaja());
            map.put("lineaPedidoLoteCajaFin", record.getLineaPedidoLoteCajaFin());
            map.put("familia", record.getFamilia());
            map.put("familiaFin", record.getFamiliaFin());
            map.put("usuCrea", record.getUsuCrea());
            map.put("fechaCrea", record.getFechaCrea());
            map.put("indNueva", record.getIndNueva());
            map.put("unidadesTarrinas", record.getUnidadesTarrinas());
            map.put("cerrada", record.getCerrada());
            map.put("indBasura", record.getIndBasura());
            map.put("ggn", record.getGgn());
            map.put("idCompraFict", record.getIdCompraFict());
            map.put("plantilla", record.getPlantilla());
            map.put("plantillaFin", record.getPlantillaFin());
            map.put("fechaSalida", record.getFechaSalida());
            map.put("fechaSalidaFin", record.getFechaSalidaFin());
            map.put("codCliente", record.getCodCliente());
            map.put("codClienteFin", record.getCodClienteFin());
            map.put("centroDescarga", record.getCentroDescarga());
            map.put("centroDescargaFin", record.getCentroDescargaFin());
            map.put("dirDescarga", record.getDirDescarga());
            map.put("dirDescargaFin", record.getDirDescargaFin());
            map.put("categoria", record.getCategoria());
            map.put("categoriaFin", record.getCategoriaFin());
            map.put("marca", record.getMarca());
            map.put("marcaFin", record.getMarcaFin());
            map.put("unidades", record.getUnidades());
            map.put("unidadesFin", record.getUnidadesFin());
            map.put("pale", record.getPale());
            map.put("paleFin", record.getPaleFin());
            map.put("nPale", record.getnPale());
            map.put("nPaleFin", record.getnPaleFin());
            map.put("direccionCliente", record.getDireccionCliente());
            map.put("direccionClienteFin", record.getDireccionClienteFin());
            map.put("cifCliente", record.getCifCliente());
            map.put("cifClienteFin", record.getCifClienteFin());
            map.put("telefonoCliente", record.getTelefonoCliente());
            map.put("telefonoClienteFin", record.getTelefonoClienteFin());
            map.put("transportista", record.getTransportista());
            map.put("transportistaFin", record.getTransportistaFin());

            tVentasFictMapper.insertRecord(map);

            id = (Integer) map.get("id");
        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_VENTA + ", Error al crear la venta ficticia: " + record.toString2() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return id;
    }

    private int crearDevolucionRetornaId(TDevoluciones record) {

        Integer id = -1;
        // Comprobamos que no exista en el sistema la familia a crear.
        TDevoluciones aux = tDevolucionesMapper.obtenerDevolucionPorIdExterno(record.getIdExterno());
        if (aux != null) {
            id = aux.getId();
            // Guardamos el registro
            record.setId(aux.getId());
            tDevolucionesMapper.updateByPrimaryKey(record);
        } else {
            try {
                // Rellenamos los datos necesarios para crear la familia.
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", 0);
                map.put("idExterno", record.getIdExterno());
                map.put("pedido", record.getPedido());
                map.put("pedidoFin", record.getPedidoFin());
                map.put("albaran", record.getAlbaran());
                map.put("nombreDescriptivo", record.getNombreDescriptivo());
                map.put("albaranFin", record.getAlbaranFin());
                map.put("calibre", record.getCalibre());
                map.put("calibreFin", record.getCalibreFin());
                map.put("lote", record.getLote());
                map.put("loteFin", record.getLoteFin());
                map.put("idPale", record.getIdPale());
                map.put("idPaleFin", record.getIdPaleFin());
                map.put("numBultos", record.getNumBultos());
                map.put("numBultosFin", record.getNumBultosFin());
                map.put("numBultosPale", record.getNumBultosPale());
                map.put("numBultosPaleFin", record.getNumBultosPaleFin());
                map.put("proveedor", record.getProveedor());
                map.put("proveedorFin", record.getProveedorFin());
                map.put("cliente", record.getCliente());
                map.put("clienteFin", record.getClienteFin());
                map.put("fechaVenta", record.getFechaVenta());
                map.put("fechaVentaFin", record.getFechaVentaFin());
                map.put("kgs", record.getKgs());
                map.put("kgsFin", record.getKgsFin());
                map.put("kgsNetos", record.getKgsNetos());
                map.put("kgsNetosFin", record.getKgsNetosFin());
                map.put("kgsEnvase", record.getKgsEnvase());
                map.put("kgsEnvaseFin", record.getKgsEnvaseFin());
                map.put("variedad", record.getVariedad());
                map.put("variedadFin", record.getVariedadFin());
                map.put("origen", record.getOrigen());
                map.put("origenFin", record.getOrigenFin());
                map.put("calidadCompra", record.getCalidadCompra());
                map.put("calidadCompraFin", record.getCalidadCompraFin());
                map.put("calidadVenta", record.getCalidadVenta());
                map.put("calidadVentaFin", record.getCalidadVentaFin());
                map.put("referencia", record.getReferencia());
                map.put("referenciaFin", record.getReferenciaFin());
                map.put("albaranCompra", record.getAlbaranCompra());
                map.put("albaranCompraFin", record.getAlbaranCompraFin());
                map.put("loteMovAlm", record.getLoteMovAlm());
                map.put("loteMovAlmFi", record.getLoteMovAlmFi());
                map.put("confeccion", record.getConfeccion());
                map.put("confeccionFin", record.getConfeccionFin());
                map.put("envase", record.getEnvase());
                map.put("envaseFin", record.getEnvaseFin());
                map.put("undConsumo", record.getUndConsumo());
                map.put("undConsumoFin", record.getUndConsumoFin());
                map.put("lineaPedidoLote", record.getLineaPedidoLote());
                map.put("lineaPedidoLoteFin", record.getLineaPedidoLoteFin());
                map.put("lineaPedidoLoteCaja", record.getLineaPedidoLoteCaja());
                map.put("lineaPedidoLoteCajaFin", record.getLineaPedidoLoteCajaFin());
                map.put("familia", record.getFamilia());
                map.put("familiaFin", record.getFamiliaFin());
                map.put("usuCrea", record.getUsuCrea());
                map.put("fechaCrea", record.getFechaCrea());
                map.put("plantilla", record.getPlantilla());
                map.put("plantillaFin", record.getPlantillaFin());
                map.put("fechaSalida", record.getFechaSalida());
                map.put("fechaSalidaFin", record.getFechaSalidaFin());
                map.put("codCliente", record.getCodCliente());
                map.put("codClienteFin", record.getCodClienteFin());
                map.put("centroDescarga", record.getCentroDescarga());
                map.put("centroDescargaFin", record.getCentroDescargaFin());
                map.put("dirDescarga", record.getDirDescarga());
                map.put("dirDescargaFin", record.getDirDescargaFin());
                map.put("categoria", record.getCategoria());
                map.put("categoriaFin", record.getCategoriaFin());
                map.put("marca", record.getMarca());
                map.put("marcaFin", record.getMarcaFin());
                map.put("unidades", record.getUnidades());
                map.put("unidadesFin", record.getUnidadesFin());
                map.put("pale", record.getPale());
                map.put("paleFin", record.getPaleFin());
                map.put("nPale", record.getnPale());
                map.put("nPaleFin", record.getnPaleFin());
                map.put("direccionCliente", record.getDireccionCliente());
                map.put("direccionClienteFin", record.getDireccionClienteFin());
                map.put("cifCliente", record.getCifCliente());
                map.put("cifClienteFin", record.getCifClienteFin());
                map.put("telefonoCliente", record.getTelefonoCliente());
                map.put("telefonoClienteFin", record.getTelefonoClienteFin());
                map.put("transportista", record.getTransportista());
                map.put("transportistaFin", record.getTransportistaFin());

                tDevolucionesMapper.insertRecord(map);

                id = (Integer) map.get("id");
            } catch (Exception e) {
                id = -1;
                log.error(Constants.BD_KO_CREA_VENTA + ", Error al crear la devolución: " + record.toString2() + ", ", e);
            }
        }
        // Retornamos el resultado de la operación.
        return id;
    }

    /**
     * Método que nos carga las compras de BD.
     * @throws GenasoftException 
     */
    public void cargarComprasBD() throws GenasoftException {
        List<TCompras> lCompras = Utils.generarListaGenerica();
        List<TComprasVista> lComprasV = Utils.generarListaGenerica();

        lCompras = comprasSetup.obtenerTodasComprasEjercicio(commonSetup.getEjercicio());

        if (commonSetup.mComprasDisponibles == null) {
            commonSetup.mComprasDisponibles = new HashMap<String, TreeMap<Date, List<TCompras>>>();
        } else {
            commonSetup.mComprasDisponibles.clear();
        }

        // Nutrimos los diccionarios de compras.
        for (TCompras compra : lCompras) {
            nutrirDiccionariosCompras(compra);
        }

        lComprasV.addAll(commonSetup.mComprasId.values());

        nutrirDiccionarioTodasComprasEjercicio(lComprasV);
    }

    /**
     * Método que nos carga las compras de BD.
     * @throws GenasoftException 
     */
    public void cargarComprasBD2() throws GenasoftException {
        List<TCompras> lCompras = Utils.generarListaGenerica();

        lCompras = comprasSetup.obtenerTodasComprasEjercicio2(commonSetup.getEjercicio());

        if (commonSetup.mComprasDisponibles == null) {
            commonSetup.mComprasDisponibles = new HashMap<String, TreeMap<Date, List<TCompras>>>();
        } else {
            commonSetup.mComprasDisponibles.clear();
        }

        // Nutrimos los diccionarios de compras.
        for (TCompras compra : lCompras) {
            nutrirDiccionariosCompras(compra);
        }
    }

    /**
     * Método que nos carga las ventas de BD.
     * @throws GenasoftException 
     */
    public void cargarVentasBD() throws GenasoftException {
        List<TVentas> lVentas = ventasSetup.obtenerTodasVentasEjercicio(commonSetup.getEjercicio());
        List<TVentasVista> lVentasV = Utils.generarListaGenerica();

        // Nutrimos los diccionarios de ventas.
        for (TVentas venta : lVentas) {
            nutrirDiccionariosVentas(venta);
        }

        lVentasV.addAll(commonSetup.mVentasId.values());

        nutrirDiccionarioTodasVentasEjercicio(lVentas, lVentasV);

    }

    /**
     * Método que nos carga las ventas de BD.
     * @throws GenasoftException 
     */
    public void cargarVentasBD2() throws GenasoftException {
        List<TVentas> lVentas = ventasSetup.obtenerTodasVentasEjercicio2(commonSetup.getEjercicio());

        // Nutrimos los diccionarios de ventas.
        for (TVentas venta : lVentas) {
            nutrirDiccionariosVentas(venta);
        }
    }

    /**
     * Método que nos carga las lineas de venta que no tienen linea de venta asignada de BD.
     * Las líneas de venta correctas, se van cargando a medida que se pulsa sobre la venta.
     * @throws GenasoftException 
     */
    public void cargarLineasVentasBD() throws GenasoftException {
        List<TLineasVentas> lLineas = lineasVentasSetup.obtenerLineasEjercicioVentaSinVenta(commonSetup.getEjercicio());

        if (commonSetup.mLineasVentaDisponibles == null) {
            commonSetup.mLineasVentaDisponibles = new HashMap<String, TreeMap<Date, List<TLineasVentas>>>();
        }

        TLineasVentasHis lHis = null;

        TCompras compra = null;
        List<TCompras> lCompras = Utils.generarListaGenerica();

        lCompras = comprasSetup.obtenerTodasComprasEjercicio(commonSetup.getEjercicio());

        Map<String, TCompras> mCompras = lCompras.stream().collect(Collectors.toMap(TCompras::getLoteFin, Function.identity()));

        TreeMap<Date, List<TLineasVentas>> tLineasVentasDisponibles = new TreeMap<Date, List<TLineasVentas>>();
        for (TLineasVentas linea : lLineas) {
            compra = mCompras.get(linea.getLoteFin());
            if (compra == null) {
                if (linea.getLoteFin().equals(SIN_LOTE)) {
                    lHis = new TLineasVentasHis();
                    lHis.copiaDesdeLineaVenta(linea);
                    if (tLineasVentasHisMapper.selectByPrimaryKey(linea.getId()) == null) {
                        tLineasVentasHisMapper.insert(lHis);
                    } else {
                        tLineasVentasHisMapper.updateByPrimaryKey(lHis);
                    }
                    // Eliminamos la linea ya que sin compra....
                    lineasVentasSetup.eliminarLineaVentaEjercicio(commonSetup.getEjercicio(), linea.getId());
                }
                continue;
            }
            // Metemos la compra disponible en función primero de la variedad, y luego lo ordenamos por fecha

            // Si es mango, nos la trae un poco al pairo la certificación
            if (linea.getFamiliaFin().equals("MANGO") || linea.getFamiliaFin().equals("KUMQUAT") || linea.getFamiliaFin().equals("CHIRIMOYA") || linea.getFamiliaFin().equals("PITAYA") || linea.getFamiliaFin().equals("PAPAYA")
                    || linea.getFamiliaFin().equals("CHIRIMOYA") || linea.getFamiliaFin().contains("SPERO")) {
                if (commonSetup.mLineasVentaDisponibles.get(linea.getVariedadFin()) == null) {
                    List<TLineasVentas> lVentasDisp = Utils.generarListaGenerica();
                    lVentasDisp.add(linea);
                    tLineasVentasDisponibles = new TreeMap<Date, List<TLineasVentas>>();
                    tLineasVentasDisponibles.put(compra.getFecha(), lVentasDisp);
                    commonSetup.mLineasVentaDisponibles.put(linea.getVariedadFin(), tLineasVentasDisponibles);
                } else {
                    tLineasVentasDisponibles = commonSetup.mLineasVentaDisponibles.get(linea.getVariedadFin());
                    List<TLineasVentas> lVentasDisp;
                    if (tLineasVentasDisponibles.get(compra.getFecha()) == null) {
                        lVentasDisp = Utils.generarListaGenerica();
                        lVentasDisp.add(linea);
                        tLineasVentasDisponibles.put(compra.getFecha(), lVentasDisp);
                    } else {
                        lVentasDisp = tLineasVentasDisponibles.get(compra.getFecha());
                        lVentasDisp.add(linea);
                    }
                    tLineasVentasDisponibles.put(compra.getFecha(), lVentasDisp);
                    commonSetup.mLineasVentaDisponibles.put(compra.getVariedad(), tLineasVentasDisponibles);

                    //List<TCompras> lComprasDisp = commonSetup.mComprasDisponibles.get(compra.getVariedadFin());
                }
            } else {
                if (commonSetup.mLineasVentaDisponibles.get(compra.getVariedadFin() + "-" + compra.getCalidadFin()) == null) {
                    List<TLineasVentas> lVentasDisp = Utils.generarListaGenerica();
                    lVentasDisp.add(linea);
                    tLineasVentasDisponibles = new TreeMap<Date, List<TLineasVentas>>();
                    tLineasVentasDisponibles.put(compra.getFecha(), lVentasDisp);
                    commonSetup.mLineasVentaDisponibles.put(compra.getVariedadFin() + "-" + compra.getCalidadFin(), tLineasVentasDisponibles);
                } else {
                    tLineasVentasDisponibles = commonSetup.mLineasVentaDisponibles.get(compra.getVariedadFin() + "-" + compra.getCalidadFin());
                    List<TLineasVentas> lVentasDisp;
                    if (tLineasVentasDisponibles.get(compra.getFecha()) == null) {
                        lVentasDisp = Utils.generarListaGenerica();
                        lVentasDisp.add(linea);
                        tLineasVentasDisponibles.put(compra.getFecha(), lVentasDisp);
                    } else {
                        lVentasDisp = tLineasVentasDisponibles.get(compra.getFecha());
                        lVentasDisp.add(linea);
                    }
                    tLineasVentasDisponibles.put(compra.getFecha(), lVentasDisp);
                    commonSetup.mLineasVentaDisponibles.put(linea.getVariedadFin() + "-" + linea.getCertificacionFin(), tLineasVentasDisponibles);

                    //List<TCompras> lComprasDisp = commonSetup.mComprasDisponibles.get(compra.getVariedadFin());
                }
            }
        }

    }

    private void nutrirDiccionariosCompras(TCompras compra) {
        // Comprobamos si existe el registro, si existe, lo modificamos, si no, lo creamos.
        List<TCompras> lCompras = Utils.generarListaGenerica();
        List<TComprasVista> lComprasVista = Utils.generarListaGenerica();
        Double aux = null;
        Double kgsCompra = Double.valueOf(0);
        TComprasVista cVista = null;
        Map<Integer, TCompras> mComprasDisponibles;
        // DICCIONARIO DE COMPRAS
        // Si se cumple la condición quiere decir que ha hecho un inseert, si es 0 o 2 no generamos un nuevo ID.        

        if (Utils.booleanFromInteger(compra.getCerrada())) {
            compra.setPesoNetoDisponible(Double.valueOf(0));
        }

        TreeMap<Date, List<TCompras>> tComprasDisponibles;

        // Metemos la compra disponible en función primero de la variedad, y luego lo ordenamos por fecha
        if (!Utils.booleanFromInteger(compra.getCerrada())) {
            // Si es mango, nos la trae un poco al pairo la certificación

            if (compra.getFamiliaFin().equals("MANGO") || compra.getFamiliaFin().equals("KUMQUAT") || compra.getFamiliaFin().equals("CHIRIMOYA") || compra.getFamiliaFin().equals("PITAYA") || compra.getFamiliaFin().equals("PAPAYA")
                    || compra.getFamiliaFin().equals("CHIRIMOYA") || compra.getFamiliaFin().contains("SPERO")) {
                if (commonSetup.mComprasDisponibles.get(compra.getVariedadFin()) == null) {
                    List<TCompras> lComprasDisp = Utils.generarListaGenerica();
                    lComprasDisp.add(compra);
                    tComprasDisponibles = new TreeMap<Date, List<TCompras>>();
                    tComprasDisponibles.put(compra.getFechaFin(), lComprasDisp);
                    commonSetup.mComprasDisponibles.put(compra.getVariedadFin(), tComprasDisponibles);
                } else {
                    tComprasDisponibles = commonSetup.mComprasDisponibles.get(compra.getVariedadFin());
                    List<TCompras> lComprasDisp;
                    if (tComprasDisponibles.get(compra.getFechaFin()) == null) {
                        lComprasDisp = Utils.generarListaGenerica();
                        lComprasDisp.add(compra);
                        tComprasDisponibles.put(compra.getFechaFin(), lComprasDisp);
                    } else {
                        lComprasDisp = tComprasDisponibles.get(compra.getFechaFin());
                        lComprasDisp.add(compra);
                    }
                    tComprasDisponibles.put(compra.getFechaFin(), lComprasDisp);
                    commonSetup.mComprasDisponibles.put(compra.getVariedadFin(), tComprasDisponibles);

                    //List<TCompras> lComprasDisp = commonSetup.mComprasDisponibles.get(compra.getVariedadFin());
                }
            } else {
                if (commonSetup.mComprasDisponibles.get(compra.getVariedadFin() + "-" + compra.getCalidadFin()) == null) {
                    List<TCompras> lComprasDisp = Utils.generarListaGenerica();
                    lComprasDisp.add(compra);
                    tComprasDisponibles = new TreeMap<Date, List<TCompras>>();
                    tComprasDisponibles.put(compra.getFechaFin(), lComprasDisp);
                    commonSetup.mComprasDisponibles.put(compra.getVariedadFin() + "-" + compra.getCalidadFin(), tComprasDisponibles);
                } else {
                    tComprasDisponibles = commonSetup.mComprasDisponibles.get(compra.getVariedadFin() + "-" + compra.getCalidadFin());
                    List<TCompras> lComprasDisp;
                    if (tComprasDisponibles.get(compra.getFechaFin()) == null) {
                        lComprasDisp = Utils.generarListaGenerica();
                        lComprasDisp.add(compra);
                        tComprasDisponibles.put(compra.getFechaFin(), lComprasDisp);
                    } else {
                        lComprasDisp = tComprasDisponibles.get(compra.getFecha());
                        lComprasDisp.add(compra);
                    }
                    tComprasDisponibles.put(compra.getFechaFin(), lComprasDisp);
                    commonSetup.mComprasDisponibles.put(compra.getVariedadFin() + "-" + compra.getCalidadFin(), tComprasDisponibles);

                    //List<TCompras> lComprasDisp = commonSetup.mComprasDisponibles.get(compra.getVariedadFin());
                }
            }
        }

        cVista = new TComprasVista();
        cVista.copiaDesdeCompra(compra);
        try {
            commonSetup.mComprasId.put(cVista.getId(), cVista);
        } catch (NullPointerException npe) {
        }
        nutrirListasCompra(compra);

        commonSetup.mCompras.put(compra.getAlbaranFin() + "_" + compra.getLoteFin(), compra);

        //commonSetup.mComprasVista.put(cVista.getAlbaranFin() + "_" + cVista.getLoteFin(), cVista);

        // DICCIONARIO LOTE LINEAS COMPRA
        lCompras = commonSetup.mLotesCompras.get(compra.getLoteFin());
        if (lCompras == null) {
            lCompras = Utils.generarListaGenerica();
        }
        lCompras.add(compra);
        commonSetup.mLotesCompras.put(compra.getLoteFin(), lCompras);

        lComprasVista = commonSetup.mLotesComprasVista.get(compra.getLoteFin());
        if (lComprasVista == null) {
            lComprasVista = Utils.generarListaGenerica();
        }
        lComprasVista.add(cVista);
        commonSetup.mLotesComprasVista.put(compra.getLoteFin(), lComprasVista);

        String origen = compra.getOrigenFin().toLowerCase().equals("españa") ? "Nacional" : "No nacional";

        // Diccionario para el balance de masas por orígen
        TVentasVista vVistaAux = commonSetup.mMasasArticulosOrigen.get(compra.getFamiliaFin() + "_" + compra.getVariedadFin() + "_" + origen);

        DecimalFormat df = new DecimalFormat("#,##0.000");
        // Si existe el artículo en el diccionario, sumamos los Kgs de compras.
        if (vVistaAux != null) {
            vVistaAux.setKgs(compra.getPesoNetoFin() + vVistaAux.getKgs());
            if (vVistaAux.getKgsFin().equals(Double.valueOf(0))) {
                vVistaAux.setKgsEnvase("0,00%");
            } else {
                vVistaAux.setKgsEnvase(df.format(vVistaAux.getKgs() / vVistaAux.getKgsFin()) + "%");
            }
        } else {
            vVistaAux = new TVentasVista();
            vVistaAux.setId("" + commonSetup.mMasasArticulosOrigen.size());
            vVistaAux.setVariedad(compra.getVariedadFin());
            vVistaAux.setFamilia(compra.getFamiliaFin());
            vVistaAux.setKgs(compra.getPesoNetoFin());
            vVistaAux.setKgsEnvase("0,00%");
            vVistaAux.setOrigen(origen);
            vVistaAux.setKgsFin(Double.valueOf(0));
            vVistaAux.setCalidadCompra(compra.getCalidadFin());
            vVistaAux.setFechaVenta(compra.getFecha());
            vVistaAux.setFechaVentaFin(compra.getFecha());
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                                                                                                                      //
        //                                        DICCIONARIOS PARA LA CORRECCIÓN DE DATOS ERRÓNEOS                                             //
        //                                                                                                                                      //
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // KGS por lote de compra
        //
        kgsCompra = compra.getPesoNetoFin();
        aux = commonSetup.mLoteCompraKgs.get(compra.getLoteFin());
        if (aux != null) {
            aux += kgsCompra;
        } else {
            aux = kgsCompra;
        }

        // Guardamos los Kgs. del lote de compra.
        commonSetup.mLoteCompraKgs.put(compra.getLoteFin(), aux);

        // Producto-Calidad kgs disponibles
        //
        mComprasDisponibles = commonSetup.mProductosComprasDisponibles.get(compra.getVariedad());

        if (compra.getPesoNetoDisponible() > 0 && !Utils.booleanFromInteger(compra.getCerrada())) {
            if (mComprasDisponibles == null) {
                mComprasDisponibles = new HashMap<Integer, TCompras>();
            }
            mComprasDisponibles.put(compra.getId(), compra);
        }

        commonSetup.mProductosComprasDisponibles.put(compra.getVariedad(), mComprasDisponibles);

    }

    private void nutrirDiccionariosVentas(TVentas venta) {

        List<TVentas> lVentas = Utils.generarListaGenerica();

        TVentas vAux = null;
        TVentasVista vVista = null;

        // Comprobamos si existe el registro, si existe, lo modificamos, si no, lo creamos.
        vVista = new TVentasVista();
        vVista.copiaDesdeVenta(venta);
        nutrirListasVenta(venta);

        if (lTodasVariedades == null) {
            lTodasVariedades = Utils.generarListaGenerica();
        } else {
            lTodasVariedades.clear();
        }
        lTodasVariedades.addAll(commonSetup.mProductosComprasDisponibles.keySet());

        if (!Utils.redondeoDecimales(2, venta.getKgsNetosFin()).equals(Utils.redondeoDecimales(2, venta.getKgsNetos()))) {
            vAux = new TVentas();
            vAux.copiaDesdeVenta(venta);
            vAux.setId(venta.getId());
            // Indicamos con -1 en los kgs para indicar que los kgs están mal.
            vAux.setKgsFin(Double.valueOf(-1));
            vVista.setError("1");
            vVista.setIndError("Sí");
            commonSetup.mIdVentasErroneas.put(venta.getId(), vAux);
        }

        lVentas = commonSetup.mLoteVentaProd.get(venta.getAlbaranFin() + "-" + venta.getFamiliaFin() + venta.getConfeccionFin());
        if (lVentas == null) {
            lVentas = Utils.generarListaGenerica();
            lVentas.add(venta);
        } else {
            lVentas.add(venta);
        }
        commonSetup.mLoteVentaProd.put(venta.getAlbaranFin() + "-" + venta.getFamiliaFin() + venta.getConfeccionFin(), lVentas);
        //commonSetup.mLoteVentasCompras.put(venta.getLoteFin(), lComprasVentas);

        if (commonSetup.mIdVentasErroneas.get(venta.getId()) == null && !Utils.booleanFromInteger(venta.getCerrada())) {
            vVista.setCerrada("Sí");
            venta.setCerrada(1);
            guardarVentaAPelo(venta);
        }
        commonSetup.mVentasId.put(vVista.getId(), vVista);

    }

    /**
     * Método que nos corrige las ventas erróneas, creando lineas de venta para ello.
     * @param maxLotes Los lotes máximos (líneas) por cada venta.
     * @param indBio Si se tratan los BIO o no.
     * @param soloBio Si solo trata los BIO.
     * @return Diccionario con las ventas que no se han podido corregir.
     * @throws GenasoftException 
     */
    public Map<Integer, TVentas> corregirVentasErroneas3(Integer maxLotes, Integer maxLotesGuacamole, Boolean indBio, Boolean soloBio, Boolean todos, Boolean repite) throws GenasoftException {

        Map<Integer, TVentas> mResult = new HashMap<Integer, TVentas>();

        Boolean vuelve = false;

        mResult.putAll(commonSetup.mIdVentasErroneas);

        List<Integer> lIds = Utils.generarListaGenerica();
        lIds.addAll(commonSetup.mIdVentasErroneas.keySet());

        Boolean cambio;
        List<TCompras> lCompras = Utils.generarListaGenerica();

        TComprasVista cVista2 = null;
        TLineasVentas lNueva = null;
        Integer cnt = 0;
        List<String> lVariedades = Utils.generarListaGenerica();
        lVariedades.add("HASS");
        Double kgsComprobar = Double.valueOf(0);
        List<TLineasVentas> lLineasAux = null;
        List<TProveedores> lProveedores = proveedoresSetup.obtenerTodosProveedores();
        Boolean crea = false;
        TVentasVista vVista = null;
        // Nutrimos el diccionario con los empleados
        Map<String, String> mProveedores = lProveedores.stream().collect(Collectors.toMap(TProveedores::getDescripcion, TProveedores::getGgn));
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        List<Integer> lIdsComprasUpdate = Utils.generarListaGenerica();

        List<String> lVariedadesProducto = Utils.generarListaGenerica();

        List<Integer> lCalibresAguacate = Utils.generarListaGenerica();
        lCalibresAguacate.add(10);
        lCalibresAguacate.add(12);
        lCalibresAguacate.add(14);
        lCalibresAguacate.add(16);
        lCalibresAguacate.add(18);
        lCalibresAguacate.add(20);
        lCalibresAguacate.add(22);
        lCalibresAguacate.add(24);
        lCalibresAguacate.add(26);
        lCalibresAguacate.add(28);
        lCalibresAguacate.add(30);
        lCalibresAguacate.add(32);
        lCalibresAguacate.add(34);
        lCalibresAguacate.add(36);
        Integer indexCalibre;

        List<TVentas> lVentas = Utils.generarListaGenerica();
        lVentas.addAll(commonSetup.mIdVentasErroneas.values());

        // Ordenamos las ventas por fecha de venta
        lVentas.sort(new Comparator<TVentas>() {

            @Override
            public int compare(TVentas m1, TVentas m2) {
                if (m1.getFechaVentaFin().equals(m2.getFechaVentaFin())) {
                    if (m1.getPedidoFin().equals(m2.getPedidoFin())) {
                        return 0;
                    }
                    return m1.getPedidoFin().compareTo(m2.getPedidoFin());
                }
                return m1.getFechaVentaFin().compareTo(m2.getFechaVentaFin());
            }

        });

        // Por cada elemento del map, vamos mirando qué datos hay incorrectos, y vamos solucionando los errores.
        for (TVentas v : lVentas) {

            if (!todos) {
                // Si solo tenemos que tratar BIO y la venta no es Bio, pasamos a la siguiente.
                if (soloBio && !v.getCalidadVentaFin().equals(CertificacionesEnum.BIO.getValue())) {
                    continue;
                }

                // Si no queremos tratar BIO y la venta es BIO, pasamos a la siguiente.
                if (!indBio && v.getCalidadVentaFin().equals(CertificacionesEnum.BIO.getValue())) {
                    continue;
                }
            }

            if (Utils.booleanFromInteger(v.getCerrada())) {
                commonSetup.mIdVentasErroneas.remove(v.getId());
                mResult.remove(v.getId());
                v.setIndError(0);
                guardarVentaAPelo(v);
                TVentasVista vVista2 = new TVentasVista();
                vVista2.copiaDesdeVenta(v);
                vVista2.setIndError("No");
                vVista2.setError("0");
                commonSetup.mIdVentasErroneas.remove(v.getId());
                commonSetup.getmVentasEjercicio().put(vVista2.getId(), vVista2);
                continue;
            }

            cnt++;
            cambio = false;
            if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                kgsComprobar = v.getKgsNetos();
            } else {
                kgsComprobar = v.getKgsNetosFin();
            }

            cal.setTime(v.getFechaVenta());
            cal.add(Calendar.DAY_OF_YEAR, -20);
            cal2.setTime(v.getFechaVenta());
            cal2.add(Calendar.DAY_OF_YEAR, 0);

            crea = true;
            // LÓGICA DE GUACAMOLE
            if (v.getVariedadFin().toUpperCase().equals("SUAVE") || v.getFamiliaFin().toUpperCase().equals("GUACAMOLE")) {
                // Buscamos los productos con variedad de aguacate para hacer el guacamole                

                lVariedadesProducto.clear();
                lVariedadesProducto.add("HASS");
                lVariedadesProducto.add("LAMB HASS");
                lVariedadesProducto.add("SUAVE");
                lVariedadesProducto.add("FUERTE");
                // Buscamos las compras que cumplen con los requisitos., el origen es null porque en el guacamoles nos la trae al pairo.
                lCompras = comprasSetup.obtenerComprasEjercicioParametros2(commonSetup.getEjercicio(), cal.getTime(), cal2.getTime(), lVariedadesProducto, null, v.getCalidadVentaFin());

                for (TCompras cVista : lCompras) {

                    // Miramos que la fecha de compra no sea posterior a la fecha de venta.

                    // Miramos si hya líneas de ventas creadas, en ese caso, hay que restar el campo v.kgs - (suma ventas kgs_fin)
                    if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                        kgsComprobar = v.getKgsNetos();
                    } else {
                        kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                    }

                    // HAY QUE AÑADIRLE UN PORCENTAJE YA UE EL GUACAMOLE NO ES EL 100% DE LOS KGS SE CONVIERTEN EN GUACAMOLE

                    kgsComprobar = PORCENTAJE_GUACAMOLE * kgsComprobar;

                    if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotesGuacamole) {
                        v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                        lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                        v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                        v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                        cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                        if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                            cVista.setCerrada(1);
                        }
                        cambio = true;
                        v.setIndError(0);
                        v.setCerrada(1);
                        v.setGgn(cVista.getGgnFin());
                        // Guardamos la línea de venta (trazabilidad)
                        crearTrazabilidadVentaRetornaId(lNueva, false, false);
                        // Guardamos la compra en el diccionario
                        cVista2 = new TComprasVista();
                        cVista2.copiaDesdeCompra(cVista);
                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                        comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                        commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                        // Metemos la venta en la lista con el lote nuevo                                        
                        break;
                    } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotesGuacamole) {
                        // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                        if (Integer.valueOf(v.getNumTrazabilidades() + 1).equals(maxLotesGuacamole)) {
                            continue;
                        }
                        v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                        lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                        v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                        v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());
                        kgsComprobar = v.getKgsFin();
                        cambio = true;
                        // Guardamos la compra en el diccionario
                        cVista2 = new TComprasVista();
                        cVista2.copiaDesdeCompra(cVista);
                        cVista2.setKgsDisponibles(Double.valueOf(0));
                        cVista2.setCerrada("Sí");
                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                        // Cambiamos los Kgs disponibles
                        cVista.setPesoNetoDisponible(Double.valueOf(0));
                        cVista.setCerrada(1);
                        // Guardamos la línea de venta (trazabilidad)
                        crearTrazabilidadVentaRetornaId(lNueva, false, false);
                        cVista2 = new TComprasVista();
                        cVista2.copiaDesdeCompra(cVista);
                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                        comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                        commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                    } else {
                        if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                            lCompras.remove(cVista);
                            cVista.setPesoNetoDisponible(Double.valueOf(0));
                            cVista.setCerrada(1);
                            cVista2 = new TComprasVista();
                            cVista2.copiaDesdeCompra(cVista);
                            commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                            comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                            commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                        }
                    }
                }

                if (cambio) {
                    if (Utils.booleanFromInteger(v.getCerrada())) {
                        v.setIndError(0);
                        // Quitamos la venta del diccionario resultado.
                        mResult.remove(v.getId());
                        commonSetup.mIdVentasErroneas.remove(v.getId());
                        vVista = new TVentasVista();
                        vVista.copiaDesdeVenta(v);
                        vVista.setIndError("No");
                        vVista.setError("0");
                        // Metemos la actualización en el diccionario.
                        commonSetup.mVentasId.put(vVista.getId(), vVista);
                        commonSetup.getmVentasEjercicio().put(vVista.getId(), vVista);
                    }
                    guardarVentaAPelo(v);
                }
            } else {
                // Si no tenemos lote, buscamos por variedad

                if (v.getFamiliaFin().contains("MANGO")) {
                    lVariedadesProducto.clear();
                    lVariedadesProducto.add(v.getVariedadFin());
                    for (String var : lVariedadesMango) {
                        if (!lVariedadesProducto.contains(var)) {
                            lVariedadesProducto.add(var);
                        }
                    }
                } else {
                    lVariedadesProducto.clear();
                    lVariedadesProducto.add(v.getVariedadFin());
                    if (v.getVariedadFin().equals("HASS")) {
                        lVariedadesProducto.add("LAMB-HASS");
                    }
                    if (v.getVariedadFin().equals("AMARILLA") || v.getVariedadFin().toUpperCase().contains("PITAYA")) {
                        lVariedadesProducto.clear();
                        for (String var : lVariedadesPitaya) {
                            if (!lVariedadesProducto.contains(var)) {
                                lVariedadesProducto.add(var);
                            }
                        }
                    }
                }

                if (Utils.booleanFromInteger(v.getCerrada())) {
                    break;
                }
                // Buscamos la primera calidad que en contremos y tan amigos....                        
                if ((v.getFamiliaFin().equals("MANGO") || v.getFamiliaFin().equals("KUMQUAT") || v.getFamiliaFin().equals("CHIRIMOYA") || v.getFamiliaFin().equals("PITAYA") || v.getFamiliaFin().equals("PAPAYA")
                        || v.getFamiliaFin().equals("CHIRIMOYA") || v.getFamiliaFin().contains("SPERO"))) {

                    // Buscamos las compras 
                    // Buscamos las compras que cumplen con los requisitos, la calidad es null porque en MANGO, KUMQUAT, CHIRIMOYA, PITAYA o PAPAYA  nos la trae al pairo.

                    // En mango, el orígen nos la trae al pairo.
                    if (v.getFamiliaFin().equals("MANGO")) {
                        lCompras = comprasSetup.obtenerComprasEjercicioParametros2(commonSetup.getEjercicio(), cal.getTime(), cal2.getTime(), lVariedadesProducto, null, null);
                    } else {
                        lCompras = comprasSetup.obtenerComprasEjercicioParametros2(commonSetup.getEjercicio(), cal.getTime(), cal2.getTime(), lVariedadesProducto, v.getOrigenFin(), null);
                    }

                    for (TCompras cVista : lCompras) {
                        if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                            kgsComprobar = v.getKgsNetos();
                        } else {
                            kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                        }
                        if (!cVista.getOrigenFin().equals(v.getOrigenFin()) && v.getFamiliaFin().equals("MANGO")) {
                            continue;
                        }

                        if ((!v.getCalidadVentaFin().equals(cVista.getCalidadFin()) && cVista.getCalidadFin().equals("BIO")) || v.getCalidadVentaFin().equals(cVista.getCalidadFin())) {
                            // Miramos a ver si existe alguna línea creada
                            lLineasAux = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());
                            crea = true;
                            for (TLineasVentas v2 : lLineasAux) {
                                // Miramos que dentro de la misma línea de venta, no se mezclen BIO CON/SIN GGN
                                if (v.getCalidadVentaFin().equals("BIO") && !mProveedores.get(cVista.getProveedorFin()).equals(mProveedores.get(v2.getProveedorFin()))
                                        && (mProveedores.get(cVista.getProveedorFin()).equals("NO GG") || mProveedores.get(v2.getProveedorFin()).equals("NO GG"))) {
                                    crea = false;
                                    break;
                                }

                            }
                        } else {
                            crea = false;
                        }
                        if (!crea) {
                            continue;
                        }

                        if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotes) {
                            // EN ESTA CASUÍSTICA LA CALIDAD NO SE TOCA.
                            v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                            lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                            lNueva.setCertificacionIni(v.getCalidadVenta());
                            lNueva.setCertificacionFin(v.getCalidadVentaFin());
                            v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                            v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                            cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                            if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                cVista.setCerrada(1);
                            }
                            cambio = true;
                            v.setCerrada(1);
                            v.setIndError(0);
                            v.setGgn(cVista.getGgnFin());
                            // Guardamos la línea de venta (trazabilidad)
                            crearTrazabilidadVentaRetornaId(lNueva, false, false);
                            if (cVista.getPesoNetoDisponible() <= cVista.getPesoNetoFin() * 7 / 100) {
                                cVista.setCerrada(1);
                            }
                            // Guardamos la compra en el diccionario
                            cVista2 = new TComprasVista();
                            cVista2.copiaDesdeCompra(cVista);

                            commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                            comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                            commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                            break;
                        } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotes) {
                            // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                            if (Integer.valueOf(v.getNumTrazabilidades() + 1).equals(maxLotes)) {
                                continue;
                            }
                            // EN ESTA CASUÍSTICA LA CALIDAD NO SE TOCA.
                            v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                            lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                            lNueva.setCertificacionIni(v.getCalidadVenta());
                            lNueva.setCertificacionFin(v.getCalidadVentaFin());
                            v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                            v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());

                            kgsComprobar = v.getKgsFin();
                            cambio = true;
                            cVista.setPesoNetoDisponible(Double.valueOf(0));
                            cVista.setCerrada(1);
                            // Guardamos la línea de venta (trazabilidad)
                            crearTrazabilidadVentaRetornaId(lNueva, false, false);
                            if (cVista.getPesoNetoDisponible() <= cVista.getPesoNetoFin() * 7 / 100) {
                                cVista.setCerrada(1);
                            }
                            // Guardamos la compra en el diccionario
                            cVista2 = new TComprasVista();
                            cVista2.copiaDesdeCompra(cVista);
                            commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                            comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                            commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                        } else {
                            if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                cVista.setPesoNetoDisponible(Double.valueOf(0));
                                cVista.setCerrada(1);
                                // Guardamos la compra en el diccionario
                                cVista2 = new TComprasVista();
                                cVista2.copiaDesdeCompra(cVista);
                                commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                            }
                        }
                    }
                } else {
                    if (v.getFamiliaFin().toUpperCase().contains("ACHO")) {
                        lVariedadesProducto.clear();
                        lVariedadesProducto.add("KERMAN");
                        lVariedadesProducto.add("GRANO");
                        lVariedadesProducto.add("LOST HILL");
                        lVariedadesProducto.add("GOLDEN HILL");
                        lVariedadesProducto.add("SIRORA");
                        lCompras = comprasSetup.obtenerComprasEjercicioParametros2(commonSetup.getEjercicio(), cal.getTime(), cal2.getTime(), lVariedadesProducto, v.getOrigenFin(), null);
                    } else {
                        lCompras = comprasSetup.obtenerComprasEjercicioParametros2(commonSetup.getEjercicio(), cal.getTime(), cal2.getTime(), lVariedadesProducto, v.getOrigenFin(), v.getCalidadVentaFin());
                    }
                    for (TCompras cVista : lCompras) {
                        crea = true;
                        if (cVista.getCalidadFin().equals(v.getCalidadVentaFin()) && cVista.getOrigenFin().equals(v.getOrigenFin())) {

                            // Miramos si hya líneas de ventas creadas, en ese caso, hay que restar el campo v.kgs - (suma ventas kgs_fin)
                            if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                                kgsComprobar = v.getKgsNetos();
                            } else {
                                kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                            }
                            try {
                                if (!v.getCalibreFin().equals("VARIOS")) {
                                    if (!v.getCalibreFin().equals(cVista.getCalibreFin()) && !cVista.getCalibreFin().equals("VARIOS")) {
                                        try {
                                            indexCalibre = lCalibresAguacate.indexOf(Integer.valueOf(v.getCalibreFin()));
                                        } catch (Exception e) {
                                            continue;
                                        }
                                        // Si el calibre es el más pequeño, miramos el que está inmediatamente superior.
                                        if (indexCalibre.equals(0)) {
                                            if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1))) {
                                                continue;
                                            }
                                        } else if (indexCalibre.equals(lCalibresAguacate.size() - 1)) {
                                            // Si el calibre es el más pequeño, miramos el que está inmediatamente inferior.
                                            if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                continue;
                                            }
                                        } else {
                                            // Si no es el más pequeño ni el más grande, miramos 1 por encima y 1 por debajo.
                                            if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1)) && !Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                continue;
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                continue;
                            }

                            // Miramos a ver si existe alguna línea creada
                            lLineasAux = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());
                            crea = true;
                            for (TLineasVentas v2 : lLineasAux) {
                                // Miramos que dentro de la misma línea de venta, no se mezclen BIO CON/SIN GGN
                                if (v.getCalidadVentaFin().equals("BIO") && !mProveedores.get(cVista.getProveedorFin()).equals(mProveedores.get(v2.getProveedorFin()))
                                        && (mProveedores.get(cVista.getProveedorFin()).equals("NO GG") || mProveedores.get(v2.getProveedorFin()).equals("NO GG"))) {
                                    crea = false;
                                    break;
                                }
                                // Miramos también si es HASS o LAMB-HASS que todas sean iguales.
                                if (cVista.getVariedadFin().equals("LAMB-HASS") && !(v2.getVariedadFin().equals("LAMB-HASS"))) {
                                    crea = false;
                                    break;
                                }
                            }
                            if (!crea) {
                                continue;
                            }
                            if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotes) {
                                v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                if (v.getFamiliaFin().toUpperCase().contains("ACHO")) {
                                    v.setVariedadFin(cVista.getVariedadFin());
                                }
                                lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                                v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                                cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                                if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                    cVista.setCerrada(1);
                                }
                                cambio = true;
                                v.setCerrada(1);
                                v.setIndError(0);
                                v.setGgn(cVista.getGgnFin());
                                if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                    lNueva.setVariedadFin("LAMB-HASS");
                                    v.setVariedadFin("LAMB-HASS");
                                }
                                // Guardamos la línea de venta (trazabilidad)
                                crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                if (cVista.getPesoNetoDisponible() <= cVista.getPesoNetoFin() * 7 / 100) {
                                    cVista.setCerrada(1);
                                }
                                // Guardamos la compra en el diccionario
                                cVista2 = new TComprasVista();
                                cVista2.copiaDesdeCompra(cVista);
                                commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                                break;
                            } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotes) {
                                // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                                if ((v.getNumTrazabilidades() + 1) == maxLotes) {
                                    continue;
                                }
                                v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                                v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());
                                kgsComprobar = v.getKgsFin();
                                cambio = true;
                                if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                    lNueva.setVariedadFin("LAMB-HASS");
                                    v.setVariedadFin("LAMB-HASS");
                                }
                                cVista.setPesoNetoDisponible(Double.valueOf(0));
                                cVista.setCerrada(1);
                                // Metemos la venta en la lista con el lote nuevo                                            
                                // Guardamos la línea de venta (trazabilidad)
                                crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                if (cVista.getPesoNetoDisponible() <= cVista.getPesoNetoFin() * 7 / 100) {
                                    cVista.setCerrada(1);
                                }
                                // Guardamos la compra en el diccionario
                                cVista2 = new TComprasVista();
                                cVista2.copiaDesdeCompra(cVista);
                                commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                            } else {
                                if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                    cVista.setPesoNetoDisponible(Double.valueOf(0));
                                    cVista.setCerrada(1);
                                    // Guardamos la compra en el diccionario
                                    cVista2 = new TComprasVista();
                                    cVista2.copiaDesdeCompra(cVista);
                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                    comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                    commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                                }
                            }
                        }
                    }
                }

                Boolean isGgn = false;
                // Comprobamos si la venta es BIO y no está completa
                if (!Utils.booleanFromInteger(v.getCerrada()) && v.getKgsNetosFin() < v.getKgsNetos() && v.getCalidadVentaFin().equals("BIO") && !repite && lVariedadesProducto.contains("HASS")) {
                    vuelve = true;
                    // Eliminamos las líneas, eso quiere decir que probablemente
                    List<TLineasVentas> lLineasVentas = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());

                    TCompras record = null;

                    if (!lLineasVentas.isEmpty()) {
                        // Nos quedamos con la primera linea del GGN

                        // Por cada línea encontrada, liberamos los kgs de la compra.
                        for (TLineasVentas lv : lLineasVentas) {

                            record = comprasSetup.obtenerCompraPorEjercicioLote(commonSetup.getEjercicio(), lv.getLoteFin());

                            isGgn = !mProveedores.get(lv.getProveedorFin()).equals("NO GG");

                            record.setPesoNetoDisponible(record.getPesoNetoDisponible() + lv.getKgsTrazabilidadFin());
                            if (Utils.booleanFromInteger(record.getCerrada())) {
                                record.setCerrada(0);
                            }

                            if (!lIdsComprasUpdate.contains(record.getId())) {
                                lIdsComprasUpdate.add(record.getId());
                            }
                            comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), record);
                            commonSetup.getmCompras().put("" + record.getId(), record);

                            // Modificamos la compra en los diccionarios pertinentes.
                            TComprasVista cAux = new TComprasVista();

                            cAux.copiaDesdeCompra(record);

                            cAux.setIdExterno("" + record.getIdExterno().intValue());
                            commonSetup.mComprasId.put(cAux.getId(), cAux);
                            commonSetup.mCompras.put("" + record.getId(), record);

                            lineasVentasSetup.eliminarLineaVentaEjercicio(commonSetup.getEjercicio(), lv.getId());

                        }
                        v.setLoteFin("");
                        v.setKgsNetosFin(Double.valueOf(0));
                        v.setNumTrazabilidades(0);
                        vVista = new TVentasVista();
                        vVista.copiaDesdeVenta(v);
                        // Metemos la actualización en el diccionario.
                        commonSetup.mVentasId.put(vVista.getId(), vVista);

                        guardarVentaAPelo(v);

                        lCompras = comprasSetup.obtenerComprasEjercicioParametros2(commonSetup.getEjercicio(), cal.getTime(), cal2.getTime(), lVariedadesProducto, v.getOrigenFin(), v.getCalidadVentaFin());

                        for (TCompras cVista : lCompras) {
                            if (!isGgn.equals(mProveedores.get(cVista.getProveedorFin()).equals("NO GG"))) {
                                continue;
                            }
                            crea = true;

                            // Miramos si hya líneas de ventas creadas, en ese caso, hay que restar el campo v.kgs - (suma ventas kgs_fin)
                            if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                                kgsComprobar = v.getKgsNetos();
                            } else {
                                kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                            }
                            try {
                                if (!v.getCalibreFin().equals("VARIOS")) {
                                    if (!v.getCalibreFin().equals(cVista.getCalibreFin()) && !cVista.getCalibreFin().equals("VARIOS")) {
                                        try {
                                            indexCalibre = lCalibresAguacate.indexOf(Integer.valueOf(v.getCalibreFin()));
                                        } catch (Exception e) {
                                            continue;
                                        }
                                        // Si el calibre es el más pequeño, miramos el que está inmediatamente superior.
                                        if (indexCalibre.equals(0)) {
                                            if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1))) {
                                                continue;
                                            }
                                        } else if (indexCalibre.equals(lCalibresAguacate.size() - 1)) {
                                            // Si el calibre es el más pequeño, miramos el que está inmediatamente inferior.
                                            if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                continue;
                                            }
                                        } else {
                                            // Si no es el más pequeño ni el más grande, miramos 1 por encima y 1 por debajo.
                                            if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1)) && !Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                continue;
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                continue;
                            }

                            // Miramos a ver si existe alguna línea creada
                            lLineasAux = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());
                            crea = true;
                            for (TLineasVentas v2 : lLineasAux) {
                                // Miramos que dentro de la misma línea de venta, no se mezclen BIO CON/SIN GGN
                                if (v.getCalidadVentaFin().equals("BIO") && !mProveedores.get(cVista.getProveedorFin()).equals(mProveedores.get(v2.getProveedorFin()))
                                        && (mProveedores.get(cVista.getProveedorFin()).equals("NO GG") || mProveedores.get(v2.getProveedorFin()).equals("NO GG"))) {
                                    crea = false;
                                    break;
                                }
                                // Miramos también si es HASS o LAMB-HASS que todas sean iguales.
                                if (cVista.getVariedadFin().equals("LAMB-HASS") && !(v2.getVariedadFin().equals("LAMB-HASS"))) {
                                    crea = false;
                                    break;
                                }
                            }
                            if (!crea) {
                                continue;
                            }
                            if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotes) {
                                v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                                v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                                cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                                if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                    cVista.setCerrada(1);
                                }
                                cambio = true;
                                v.setCerrada(1);
                                v.setIndError(0);
                                v.setGgn(cVista.getGgnFin());
                                if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                    lNueva.setVariedadFin("LAMB-HASS");
                                    v.setVariedadFin("LAMB-HASS");
                                }
                                // Guardamos la línea de venta (trazabilidad)
                                crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                if (cVista.getPesoNetoDisponible() <= cVista.getPesoNetoFin() * 7 / 100) {
                                    cVista.setCerrada(1);
                                }
                                // Guardamos la compra en el diccionario
                                cVista2 = new TComprasVista();
                                cVista2.copiaDesdeCompra(cVista);
                                commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                                break;
                            } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotes) {
                                // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                                if ((v.getNumTrazabilidades() + 1) == maxLotes) {
                                    continue;
                                }
                                v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                                v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());
                                kgsComprobar = v.getKgsFin();
                                cambio = true;
                                if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                    lNueva.setVariedadFin("LAMB-HASS");
                                    v.setVariedadFin("LAMB-HASS");
                                }
                                cVista.setPesoNetoDisponible(Double.valueOf(0));
                                cVista.setCerrada(1);
                                // Metemos la venta en la lista con el lote nuevo                                            
                                // Guardamos la línea de venta (trazabilidad)
                                crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                if (cVista.getPesoNetoDisponible() <= cVista.getPesoNetoFin() * 7 / 100) {
                                    cVista.setCerrada(1);
                                }
                                // Guardamos la compra en el diccionario
                                cVista2 = new TComprasVista();
                                cVista2.copiaDesdeCompra(cVista);
                                commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                            } else {
                                if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                    cVista.setPesoNetoDisponible(Double.valueOf(0));
                                    cVista.setCerrada(1);
                                    // Guardamos la compra en el diccionario
                                    cVista2 = new TComprasVista();
                                    cVista2.copiaDesdeCompra(cVista);
                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                    comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                    commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                                }
                            }
                        }
                    }
                }
                if (!Utils.booleanFromInteger(v.getCerrada())) {
                    // Eliminamos las líneas, eso quiere decir que probablemente
                    List<TLineasVentas> lLineasVentas = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());

                    TCompras record = null;

                    if (!lLineasVentas.isEmpty()) {
                        // Nos quedamos con la primera linea del GGN

                        // Por cada línea encontrada, liberamos los kgs de la compra.
                        for (TLineasVentas lv : lLineasVentas) {

                            record = comprasSetup.obtenerCompraPorEjercicioLote(commonSetup.getEjercicio(), lv.getLoteFin());

                            isGgn = !mProveedores.get(lv.getProveedorFin()).equals("NO GG");

                            record.setPesoNetoDisponible(record.getPesoNetoDisponible() + lv.getKgsTrazabilidadFin());
                            if (Utils.booleanFromInteger(record.getCerrada())) {
                                record.setCerrada(0);
                            }

                            if (!lIdsComprasUpdate.contains(record.getId())) {
                                lIdsComprasUpdate.add(record.getId());
                            }

                            comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), record);
                            commonSetup.getmCompras().put("" + record.getId(), record);

                            // Modificamos la compra en los diccionarios pertinentes.
                            TComprasVista cAux = new TComprasVista();

                            cAux.copiaDesdeCompra(record);

                            cAux.setIdExterno("" + record.getIdExterno().intValue());
                            commonSetup.mComprasId.put(cAux.getId(), cAux);
                            commonSetup.mCompras.put("" + record.getId(), record);

                            lineasVentasSetup.eliminarLineaVentaEjercicio(commonSetup.getEjercicio(), lv.getId());

                        }
                        v.setLoteFin("");
                        v.setKgsNetosFin(Double.valueOf(0));
                        v.setNumTrazabilidades(0);
                        vVista = new TVentasVista();
                        vVista.copiaDesdeVenta(v);
                        // Metemos la actualización en el diccionario.
                        commonSetup.mVentasId.put(vVista.getId(), vVista);

                        guardarVentaAPelo(v);

                    }
                    // Si la venta no está cerrada miramos si podemos cerrarla sin importar el orígen
                    if (Utils.booleanFromInteger(v.getCerrada())) {
                        break;
                    }
                    // Buscamos la primera calidad que en contremos y tan amigos....                        
                    if ((v.getFamiliaFin().equals("MANGO") || v.getFamiliaFin().equals("KUMQUAT") || v.getFamiliaFin().equals("CHIRIMOYA") || v.getFamiliaFin().equals("PITAYA") || v.getFamiliaFin().equals("PAPAYA")
                            || v.getFamiliaFin().equals("CHIRIMOYA") || v.getFamiliaFin().contains("SPERO"))) {

                        // Buscamos las compras 
                        // Buscamos las compras que cumplen con los requisitos, la calidad es null porque en MANGO, KUMQUAT, CHIRIMOYA, PITAYA o PAPAYA  nos la trae al pairo.

                        // En esta vuelta, nos da igual el orígen.
                        if (v.getFamiliaFin().equals("MANGO")) {
                            lCompras = comprasSetup.obtenerComprasEjercicioParametros2(commonSetup.getEjercicio(), cal.getTime(), cal2.getTime(), lVariedadesProducto, null, null);
                        } else {
                            lCompras = comprasSetup.obtenerComprasEjercicioParametros2(commonSetup.getEjercicio(), cal.getTime(), cal2.getTime(), lVariedadesProducto, null, null);
                        }

                        for (TCompras cVista : lCompras) {
                            if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                                kgsComprobar = v.getKgsNetos();
                            } else {
                                kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                            }

                            if ((!v.getCalidadVentaFin().equals(cVista.getCalidadFin()) && cVista.getCalidadFin().equals("BIO")) || v.getCalidadVentaFin().equals(cVista.getCalidadFin())) {
                                // Miramos a ver si existe alguna línea creada
                                lLineasAux = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());
                                crea = true;
                                if (!lLineasAux.isEmpty()) {
                                    // Dentro de un mismo movimiento, no se pueden mezclar los orígenes.
                                    if (!cVista.getOrigenFin().equals(v.getOrigenFin())) {
                                        continue;
                                    }
                                }
                                for (TLineasVentas v2 : lLineasAux) {
                                    // Miramos que dentro de la misma línea de venta, no se mezclen BIO CON/SIN GGN
                                    if (v.getCalidadVentaFin().equals("BIO") && !mProveedores.get(cVista.getProveedorFin()).equals(mProveedores.get(v2.getProveedorFin()))
                                            && (mProveedores.get(cVista.getProveedorFin()).equals("NO GG") || mProveedores.get(v2.getProveedorFin()).equals("NO GG"))) {
                                        crea = false;
                                        break;
                                    }
                                }
                            } else {
                                crea = false;
                            }
                            if (!crea) {
                                continue;
                            }

                            if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotes) {
                                // EN ESTA CASUÍSTICA LA CALIDAD NO SE TOCA.
                                v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                v.setOrigenFin(cVista.getOrigenFin());
                                lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                                lNueva.setCertificacionIni(v.getCalidadVenta());
                                lNueva.setCertificacionFin(v.getCalidadVentaFin());
                                v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                                v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                                if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                    cVista.setCerrada(1);
                                }
                                cambio = true;
                                v.setCerrada(1);
                                v.setIndError(0);
                                v.setGgn(cVista.getGgnFin());
                                // Guardamos la línea de venta (trazabilidad)
                                crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                if (cVista.getPesoNetoDisponible() <= cVista.getPesoNetoFin() * 7 / 100) {
                                    cVista.setCerrada(1);
                                }
                                // Guardamos la compra en el diccionario
                                cVista2 = new TComprasVista();
                                cVista2.copiaDesdeCompra(cVista);
                                commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                                break;
                            } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotes) {
                                // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                                if (Integer.valueOf(v.getNumTrazabilidades() + 1).equals(maxLotes)) {
                                    continue;
                                }
                                // EN ESTA CASUÍSTICA LA CALIDAD NO SE TOCA.
                                v.setOrigenFin(cVista.getOrigenFin());
                                v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                                lNueva.setCertificacionIni(v.getCalidadVenta());
                                lNueva.setCertificacionFin(v.getCalidadVentaFin());
                                v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());

                                kgsComprobar = v.getKgsFin();
                                cambio = true;
                                // Cambiamos los Kgs disponibles                                
                                cVista.setPesoNetoDisponible(Double.valueOf(0));
                                cVista.setCerrada(1);
                                // Guardamos la línea de venta (trazabilidad)
                                crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                if (cVista.getPesoNetoDisponible() <= cVista.getPesoNetoFin() * 7 / 100) {
                                    cVista.setCerrada(1);
                                }
                                // Guardamos la compra en el diccionario
                                cVista2 = new TComprasVista();
                                cVista2.copiaDesdeCompra(cVista);
                                commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                            } else {
                                if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                    cVista.setPesoNetoDisponible(Double.valueOf(0));
                                    cVista.setCerrada(1);
                                    // Guardamos la compra en el diccionario
                                    cVista2 = new TComprasVista();
                                    cVista2.copiaDesdeCompra(cVista);
                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                    comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                    commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                                }
                            }
                        }
                    } else {
                        // En esta vuelta, el orígen nos la trae al pairo
                        lCompras = comprasSetup.obtenerComprasEjercicioParametros2(commonSetup.getEjercicio(), cal.getTime(), cal2.getTime(), lVariedadesProducto, null, v.getCalidadVentaFin());
                        for (TCompras cVista : lCompras) {
                            crea = true;
                            if (cVista.getCalidadFin().equals(v.getCalidadVentaFin())) {

                                // Miramos si hya líneas de ventas creadas, en ese caso, hay que restar el campo v.kgs - (suma ventas kgs_fin)
                                if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                                    kgsComprobar = v.getKgsNetos();
                                } else {
                                    kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                                }
                                try {
                                    if (!v.getCalibreFin().equals("VARIOS")) {
                                        if (!v.getCalibreFin().equals(cVista.getCalibreFin()) && !cVista.getCalibreFin().equals("VARIOS")) {
                                            try {
                                                indexCalibre = lCalibresAguacate.indexOf(Integer.valueOf(v.getCalibreFin()));
                                            } catch (Exception e) {
                                                continue;
                                            }
                                            // Si el calibre es el más pequeño, miramos el que está inmediatamente superior.
                                            if (indexCalibre.equals(0)) {
                                                if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1))) {
                                                    continue;
                                                }
                                            } else if (indexCalibre.equals(lCalibresAguacate.size() - 1)) {
                                                // Si el calibre es el más pequeño, miramos el que está inmediatamente inferior.
                                                if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                    continue;
                                                }
                                            } else {
                                                // Si no es el más pequeño ni el más grande, miramos 1 por encima y 1 por debajo.
                                                if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1)) && !Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                    continue;
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    continue;
                                }

                                // Miramos a ver si existe alguna línea creada
                                lLineasAux = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());
                                crea = true;
                                for (TLineasVentas v2 : lLineasAux) {
                                    // Miramos que dentro de la misma línea de venta, no se mezclen BIO CON/SIN GGN
                                    if (v.getCalidadVentaFin().equals("BIO") && !mProveedores.get(cVista.getProveedorFin()).equals(mProveedores.get(v2.getProveedorFin()))
                                            && (mProveedores.get(cVista.getProveedorFin()).equals("NO GG") || mProveedores.get(v2.getProveedorFin()).equals("NO GG"))) {
                                        crea = false;
                                        break;
                                    }
                                    // Miramos también si es HASS o LAMB-HASS que todas sean iguales.
                                    if (cVista.getVariedadFin().equals("LAMB-HASS") && !(v2.getVariedadFin().equals("LAMB-HASS"))) {
                                        crea = false;
                                        break;
                                    }
                                }
                                if (!crea) {
                                    continue;
                                }
                                if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotes) {
                                    v.setOrigenFin(cVista.getOrigenFin());
                                    v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                    lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                                    v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                    v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                                    cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                                    if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                        cVista.setCerrada(1);
                                    }
                                    cambio = true;
                                    v.setCerrada(1);
                                    v.setIndError(0);
                                    v.setGgn(cVista.getGgnFin());
                                    if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                        lNueva.setVariedadFin("LAMB-HASS");
                                        v.setVariedadFin("LAMB-HASS");
                                    }
                                    // Guardamos la línea de venta (trazabilidad)
                                    crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                    if (cVista.getPesoNetoDisponible() <= cVista.getPesoNetoFin() * 7 / 100) {
                                        cVista.setCerrada(1);
                                    }
                                    // Guardamos la compra en el diccionario
                                    cVista2 = new TComprasVista();
                                    cVista2.copiaDesdeCompra(cVista);
                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                    comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                    commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                                    break;
                                } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotes) {
                                    v.setOrigenFin(cVista.getOrigenFin());
                                    // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                                    if ((v.getNumTrazabilidades() + 1) == maxLotes) {
                                        continue;
                                    }
                                    v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                    lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                                    v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                    v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());
                                    kgsComprobar = v.getKgsFin();
                                    cambio = true;
                                    if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                        lNueva.setVariedadFin("LAMB-HASS");
                                        v.setVariedadFin("LAMB-HASS");
                                    }
                                    cVista.setPesoNetoDisponible(Double.valueOf(0));
                                    cVista.setCerrada(1);
                                    // Metemos la venta en la lista con el lote nuevo                                            
                                    // Guardamos la línea de venta (trazabilidad)
                                    crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                    if (cVista.getPesoNetoDisponible() <= cVista.getPesoNetoFin() * 7 / 100) {
                                        cVista.setCerrada(1);
                                    }
                                    // Guardamos la compra en el diccionario
                                    cVista2 = new TComprasVista();
                                    cVista2.copiaDesdeCompra(cVista);
                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                    comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                    commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                                } else {
                                    if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                        cVista.setPesoNetoDisponible(Double.valueOf(0));
                                        cVista.setCerrada(1);
                                        // Guardamos la compra en el diccionario
                                        cVista2 = new TComprasVista();
                                        cVista2.copiaDesdeCompra(cVista);
                                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                        comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), cVista);
                                        commonSetup.getmCompras().put("" + cVista.getId(), cVista);
                                    }
                                }
                            }
                        }
                    }
                }

            }
            if (cambio) {
                if (Utils.booleanFromInteger(v.getCerrada())) {
                    v.setIndError(0);
                    // Quitamos la venta del diccionario resultado.
                    mResult.remove(v.getId());
                    commonSetup.mIdVentasErroneas.remove(v.getId());
                    vVista = new TVentasVista();
                    vVista.copiaDesdeVenta(v);
                    vVista.setError("0");
                    vVista.setIndError("No");
                    // Metemos la actualización en el diccionario.
                    commonSetup.mVentasId.put(vVista.getId(), vVista);
                    commonSetup.getmVentasEjercicio().put(vVista.getId(), vVista);

                } else if (!v.getKgsFin().equals(v.getKgs())) {
                    v.setIndError(1);
                    mResult.put(v.getId(), v);
                    vVista = new TVentasVista();
                    vVista.copiaDesdeVenta(v);
                    vVista.setError("1");
                    vVista.setIndError("Sí");
                    commonSetup.mIdVentasErroneas.put(v.getId(), v);
                    // Metemos la actualización en el diccionario.
                    commonSetup.mVentasId.put(vVista.getId(), vVista);
                    commonSetup.getmVentasEjercicio().put(vVista.getId(), vVista);
                }
                guardarVentaAPelo(v);
            }
        }

        if (vuelve) {
            corregirVentasErroneas(maxLotes, maxLotesGuacamole, indBio, soloBio, todos, vuelve);
        }

        lVentas.addAll(mResult.values());
        commonSetup.mIdVentasErroneas.clear();
        for (TVentas vv : lVentas) {
            vv.setIndError(1);
            vVista = new TVentasVista();
            vVista.copiaDesdeVenta(vv);
            vVista.setError("1");
            vVista.setIndError("Sí");
            commonSetup.mIdVentasErroneas.put(vv.getId(), vv);
            // Metemos la actualización en el diccionario.
            commonSetup.mVentasId.put(vVista.getId(), vVista);
            commonSetup.getmVentasEjercicio().put(vVista.getId(), vVista);

        }

        rellenarArbolCompras();
        rellenarArbolVentas();

        // Comprobamos si hay ventas que tienen mas kgs que en compras.
        //comprobarComprasVentas();
        return mResult;
    }

    /**
     * Método que nos corrige las ventas erróneas, creando lineas de venta para ello.
     * @param maxLotes Los lotes máximos (líneas) por cada venta.
     * @param indBio Si se tratan los BIO o no.
     * @param soloBio Si solo trata los BIO.
     * @return Diccionario con las ventas que no se han podido corregir.
     * @throws GenasoftException 
     */
    public Map<Integer, TVentas> corregirVentasErroneas(Integer maxLotes, Integer maxLotesGuacamole, Boolean indBio, Boolean soloBio, Boolean todos, Boolean repite) throws GenasoftException {

        Map<Integer, TVentas> mResult = new HashMap<Integer, TVentas>();

        Boolean vuelve = false;

        mResult.putAll(commonSetup.mIdVentasErroneas);

        List<Integer> lIds = Utils.generarListaGenerica();
        lIds.addAll(commonSetup.mIdVentasErroneas.keySet());

        Boolean cambio;
        List<TCompras> lCompras = Utils.generarListaGenerica();
        List<TCompras> lCompras2 = Utils.generarListaGenerica();

        TComprasVista cVista2 = null;
        TLineasVentas lNueva = null;
        Integer cnt = 0;
        List<String> lVariedades = Utils.generarListaGenerica();
        lVariedades.add("HASS");
        Double kgsComprobar = Double.valueOf(0);
        List<Integer> lIdsCompras = Utils.generarListaGenerica();
        List<TLineasVentas> lLineasAux = null;
        List<TProveedores> lProveedores = proveedoresSetup.obtenerTodosProveedores();
        Boolean crea = false;
        TVentasVista vVista = null;
        // Nutrimos el diccionario con los empleados
        Map<String, String> mProveedores = lProveedores.stream().collect(Collectors.toMap(TProveedores::getDescripcion, TProveedores::getGgn));
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        List<Integer> lIdsComprasUpdate = Utils.generarListaGenerica();

        List<String> lVariedadesProducto = Utils.generarListaGenerica();

        List<Integer> lCalibresAguacate = Utils.generarListaGenerica();
        lCalibresAguacate.add(10);
        lCalibresAguacate.add(12);
        lCalibresAguacate.add(14);
        lCalibresAguacate.add(16);
        lCalibresAguacate.add(18);
        lCalibresAguacate.add(20);
        lCalibresAguacate.add(22);
        lCalibresAguacate.add(24);
        lCalibresAguacate.add(26);
        lCalibresAguacate.add(28);
        lCalibresAguacate.add(30);
        lCalibresAguacate.add(32);
        lCalibresAguacate.add(34);
        lCalibresAguacate.add(36);
        Integer indexCalibre;

        TreeMap<Date, List<TCompras>> tComprasDisp;
        SortedMap<Date, List<TCompras>> tComprasDispFechas;
        List<Date> lFechas = Utils.generarListaGenerica();

        List<TVentas> lVentas = Utils.generarListaGenerica();
        lVentas.addAll(commonSetup.mIdVentasErroneas.values());

        // Ordenamos las ventas por fecha de venta
        lVentas.sort(new Comparator<TVentas>() {

            @Override
            public int compare(TVentas m1, TVentas m2) {
                if (m1.getFechaVentaFin().equals(m2.getFechaVentaFin())) {
                    if (m1.getPedidoFin().equals(m2.getPedidoFin())) {
                        return 0;
                    }
                    return m1.getPedidoFin().compareTo(m2.getPedidoFin());
                }
                return m1.getFechaVentaFin().compareTo(m2.getFechaVentaFin());
            }

        });

        // Por cada elemento del map, vamos mirando qué datos hay incorrectos, y vamos solucionando los errores.
        for (TVentas v : lVentas) {
            if (!todos) {
                // Si solo tenemos que tratar BIO y la venta no es Bio, pasamos a la siguiente.
                if (soloBio && !v.getCalidadVentaFin().equals(CertificacionesEnum.BIO.getValue())) {
                    continue;
                }

                // Si no queremos tratar BIO y la venta es BIO, pasamos a la siguiente.
                if (!indBio && v.getCalidadVentaFin().equals(CertificacionesEnum.BIO.getValue())) {
                    continue;
                }
            }

            if (Utils.booleanFromInteger(v.getCerrada())) {
                commonSetup.mIdVentasErroneas.remove(v.getId());
                mResult.remove(v.getId());
                v.setIndError(0);
                guardarVentaAPelo(v);
                continue;
            }

            cnt++;
            cambio = false;
            if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                kgsComprobar = v.getKgsNetos();
            } else {
                kgsComprobar = v.getKgsNetosFin();
            }

            crea = true;
            // LÓGICA DE GUACAMOLE
            if (v.getVariedadFin().toUpperCase().equals("SUAVE") || v.getFamiliaFin().toUpperCase().equals("GUACAMOLE")) {
                // Buscamos los productos con variedad de aguacate para hacer el guacamole                
                for (String variedad : lVariedades) {
                    tComprasDisp = commonSetup.mComprasDisponibles.get(variedad + "-" + v.getCalidadVentaFin());
                    if (tComprasDisp != null && !tComprasDisp.isEmpty()) {
                        // En el guacamole cogemos todas las fechas excepto si la venta es anterior a la compra.
                        tComprasDispFechas = tComprasDisp.headMap(v.getFechaVenta(), true);
                        if (tComprasDispFechas != null && !tComprasDispFechas.isEmpty()) {
                            //lComprasDisp.clear();
                            lFechas.clear();
                            lFechas.addAll(tComprasDispFechas.keySet());
                            for (Date fecha : lFechas) {
                                if (Utils.booleanFromInteger(v.getCerrada())) {
                                    break;
                                }
                                lCompras = tComprasDispFechas.get(fecha);
                                lCompras2.clear();
                                if (lCompras != null) {
                                    lCompras2.addAll(lCompras);
                                    for (TCompras cVista : lCompras2) {
                                        if (cVista.getCalidad().equals(v.getCalidadVentaFin())) {
                                            // Si es guacamole solo cogemos de variedad hass, lamb hass, suave o fuerte
                                            if (cVista.getVariedadFin().equals("HASS") || cVista.getVariedadFin().equals("LAMB HASS") || cVista.getVariedadFin().equals("SUAVE") || cVista.getVariedadFin().equals("FUERTE")) {
                                                // Miramos que la fecha de compra no sea posterior a la fecha de venta.

                                                // Miramos si hya líneas de ventas creadas, en ese caso, hay que restar el campo v.kgs - (suma ventas kgs_fin)
                                                if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                                                    kgsComprobar = v.getKgsNetos();
                                                } else {
                                                    kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                                                }

                                                if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotesGuacamole) {
                                                    v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                    lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                                                    v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                                                    v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                    cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                                                    if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                        lIdsCompras.add(cVista.getId());
                                                        lCompras.remove(cVista);
                                                    }
                                                    cambio = true;
                                                    v.setIndError(0);
                                                    v.setCerrada(1);
                                                    v.setGgn(cVista.getGgnFin());
                                                    // Guardamos la línea de venta (trazabilidad)
                                                    crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                    // Guardamos la compra en el diccionario
                                                    cVista2 = new TComprasVista();
                                                    cVista2.copiaDesdeCompra(cVista);
                                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                    // Guardamso el ID de la compra para hacer update en BD cuando acabemos
                                                    if (!lIdsComprasUpdate.contains(cVista.getId())) {
                                                        lIdsComprasUpdate.add(cVista.getId());
                                                    }
                                                    // Metemos la venta en la lista con el lote nuevo                                        
                                                    break;
                                                } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotesGuacamole) {
                                                    // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                                                    if (Integer.valueOf(v.getNumTrazabilidades() + 1).equals(maxLotesGuacamole)) {
                                                        continue;
                                                    }
                                                    v.setVariedadFin(cVista.getVariedadFin());
                                                    v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                    lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                                                    v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                    v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());
                                                    kgsComprobar = v.getKgsFin();
                                                    cambio = true;
                                                    // Guardamos la compra en el diccionario
                                                    cVista2 = new TComprasVista();
                                                    cVista2.copiaDesdeCompra(cVista);
                                                    cVista2.setKgsDisponibles(Double.valueOf(0));
                                                    cVista2.setCerrada("Sí");
                                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                    // Cambiamos los Kgs disponibles
                                                    lCompras.remove(cVista);
                                                    cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                    cVista.setCerrada(1);
                                                    lIdsCompras.add(cVista.getId());
                                                    // Guardamos la línea de venta (trazabilidad)
                                                    crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                    cVista2 = new TComprasVista();
                                                    cVista2.copiaDesdeCompra(cVista);
                                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                } else {
                                                    if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                        lCompras.remove(cVista);
                                                        lIdsCompras.add(cVista.getId());
                                                        cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                        cVista.setCerrada(1);
                                                        cVista2 = new TComprasVista();
                                                        cVista2.copiaDesdeCompra(cVista);
                                                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                    }
                                                }
                                            } else {
                                                continue;
                                            }
                                        }
                                    }
                                }
                            }
                            if (cambio) {
                                break;
                            }
                        }
                    }
                }
                if (cambio) {
                    if (Utils.booleanFromInteger(v.getCerrada())) {
                        v.setIndError(0);
                        // Quitamos la venta del diccionario resultado.
                        mResult.remove(v.getId());
                        commonSetup.mIdVentasErroneas.remove(v.getId());
                        vVista = new TVentasVista();
                        vVista.copiaDesdeVenta(v);
                        vVista.setIndError("No");
                        vVista.setError("0");
                        // Metemos la actualización en el diccionario.
                        commonSetup.mVentasId.put(vVista.getId(), vVista);
                    }
                    guardarVentaAPelo(v);
                }
            } else {
                // Si no tenemos lote, buscamos por variedad
                if (v.getLote() == null || v.getLote().trim().isEmpty() || v.getLote().equals(SIN_LOTE)) {
                    if (v.getAlbaranFin() == null || v.getAlbaranFin().isEmpty()) {
                        continue;
                    }
                    if (v.getFamiliaFin().contains("MANGO")) {
                        lVariedadesProducto.clear();
                        lVariedadesProducto.add(v.getVariedadFin());
                        for (String var : lVariedadesMango) {
                            if (!lVariedadesProducto.contains(var)) {
                                lVariedadesProducto.add(var);
                            }
                        }
                    } else {
                        lVariedadesProducto.clear();
                        lVariedadesProducto.add(v.getVariedadFin());
                        if (v.getVariedadFin().equals("HASS")) {
                            lVariedadesProducto.add("LAMB-HASS");
                        }
                        if (v.getVariedadFin().equals("AMARILLA") || v.getVariedadFin().toUpperCase().contains("PITAYA")) {
                            lVariedadesProducto.clear();
                            for (String var : lVariedadesPitaya) {
                                if (!lVariedadesProducto.contains(var)) {
                                    lVariedadesProducto.add(var);
                                }
                            }
                        }
                    }
                    for (String var : lVariedadesProducto) {
                        if (Utils.booleanFromInteger(v.getCerrada())) {
                            break;
                        }
                        // Buscamos la primera calidad que en contremos y tan amigos....                        
                        if ((v.getFamiliaFin().equals("MANGO") || v.getFamiliaFin().equals("KUMQUAT") || v.getFamiliaFin().equals("CHIRIMOYA") || v.getFamiliaFin().equals("PITAYA") || v.getFamiliaFin().equals("PAPAYA")
                                || v.getFamiliaFin().equals("CHIRIMOYA") || v.getFamiliaFin().contains("SPERO"))) {
                            // En MANGO no guardamos en el diccionario la calidad así que solo buscamos por variedad.
                            tComprasDisp = commonSetup.mComprasDisponibles.get(var);
                            if (tComprasDisp != null && !tComprasDisp.isEmpty()) {
                                // La fecha de compra no puede ser superior al mes, así que...
                                cal.setTime(v.getFechaVenta());
                                if (v.getFamiliaFin().equals("CHIRIMOYA")) {
                                    cal.add(Calendar.DAY_OF_YEAR, -15);
                                } else if (v.getFamiliaFin().contains("SPERO")) {
                                    cal.add(Calendar.DAY_OF_YEAR, -15);
                                } else {
                                    cal.add(Calendar.DAY_OF_YEAR, -20);
                                }
                                cal2.setTime(v.getFechaVenta());
                                cal2.add(Calendar.DAY_OF_YEAR, +0);
                                // En el guacamole cogemos todas las fechas excepto si la venta es anterior a la compra.
                                tComprasDispFechas = tComprasDisp.subMap(cal.getTime(), cal2.getTime());
                                if (tComprasDispFechas != null && !tComprasDispFechas.isEmpty()) {
                                    //lComprasDisp.clear();
                                    lFechas.clear();
                                    lFechas.addAll(tComprasDispFechas.keySet());
                                    for (Date fecha : lFechas) {
                                        if (Utils.booleanFromInteger(v.getCerrada())) {
                                            break;
                                        }
                                        lCompras = tComprasDispFechas.get(fecha);
                                        lCompras2.clear();
                                        if (lCompras != null) {
                                            lCompras2.addAll(lCompras);
                                            for (TCompras cVista : lCompras2) {
                                                if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                                                    kgsComprobar = v.getKgsNetos();
                                                } else {
                                                    kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                                                }
                                                if (!cVista.getOrigenFin().equals(v.getOrigenFin()) && v.getFamiliaFin().equals("MANGO")) {
                                                    continue;
                                                }

                                                if ((!v.getCalidadVentaFin().equals(cVista.getCalidadFin()) && cVista.getCalidadFin().equals("BIO")) || v.getCalidadVentaFin().equals(cVista.getCalidadFin())) {
                                                    // Miramos a ver si existe alguna línea creada
                                                    lLineasAux = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());
                                                    crea = true;
                                                    for (TLineasVentas v2 : lLineasAux) {
                                                        // Miramos que dentro de la misma línea de venta, no se mezclen BIO CON/SIN GGN
                                                        if (v.getCalidadVentaFin().equals("BIO") && !mProveedores.get(cVista.getProveedorFin()).equals(mProveedores.get(v2.getProveedorFin()))
                                                                && (mProveedores.get(cVista.getProveedorFin()).equals("NO GG") || mProveedores.get(v2.getProveedorFin()).equals("NO GG"))) {
                                                            crea = false;
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    crea = false;
                                                }
                                                if (!crea) {
                                                    continue;
                                                }

                                                if (v.getFamiliaFin().equals("MANGO") && !lLineasAux.isEmpty() && !cVista.getVariedadFin().equals(v.getVariedadFin())) {
                                                    continue;
                                                }

                                                if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotes) {
                                                    // EN ESTA CASUÍSTICA LA CALIDAD NO SE TOCA.
                                                    v.setVariedadFin(cVista.getVariedadFin());
                                                    v.setOrigenFin(cVista.getOrigenFin());
                                                    v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                    lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                                                    lNueva.setCertificacionIni(v.getCalidadVenta());
                                                    lNueva.setCertificacionFin(v.getCalidadVentaFin());
                                                    v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                                                    v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                    cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                                                    if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                        lIdsCompras.add(cVista.getId());
                                                        lCompras.remove(cVista);
                                                    }
                                                    cambio = true;
                                                    v.setCerrada(1);
                                                    v.setIndError(0);
                                                    v.setGgn(cVista.getGgnFin());
                                                    // Guardamos la línea de venta (trazabilidad)
                                                    crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                    // Guardamos la compra en el diccionario
                                                    cVista2 = new TComprasVista();
                                                    cVista2.copiaDesdeCompra(cVista);
                                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                    // Guardamso el ID de la compra para hacer update en BD cuando acabemos
                                                    if (!lIdsComprasUpdate.contains(cVista.getId())) {
                                                        lIdsComprasUpdate.add(cVista.getId());
                                                    }
                                                    break;
                                                } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotes) {
                                                    // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                                                    if (Integer.valueOf(v.getNumTrazabilidades() + 1).equals(maxLotes)) {
                                                        continue;
                                                    }
                                                    v.setVariedadFin(cVista.getVariedadFin());
                                                    v.setOrigenFin(cVista.getOrigenFin());
                                                    // EN ESTA CASUÍSTICA LA CALIDAD NO SE TOCA.
                                                    v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                    lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                                                    lNueva.setCertificacionIni(v.getCalidadVenta());
                                                    lNueva.setCertificacionFin(v.getCalidadVentaFin());
                                                    v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                    v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());

                                                    kgsComprobar = v.getKgsFin();
                                                    cambio = true;
                                                    // Cambiamos los Kgs disponibles
                                                    lCompras.remove(cVista);
                                                    cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                    cVista.setCerrada(1);
                                                    lIdsCompras.add(cVista.getId());
                                                    // Guardamos la línea de venta (trazabilidad)
                                                    crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                    // Guardamos la compra en el diccionario
                                                    cVista2 = new TComprasVista();
                                                    cVista2.copiaDesdeCompra(cVista);
                                                    cVista2.setKgsDisponibles(Double.valueOf(0));
                                                    cVista2.setCerrada("Sí");
                                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                } else {
                                                    if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                        lCompras.remove(cVista);
                                                        lIdsCompras.add(cVista.getId());
                                                        cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                        cVista.setCerrada(1);
                                                        cVista2 = new TComprasVista();
                                                        cVista2.copiaDesdeCompra(cVista);
                                                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            tComprasDisp = commonSetup.mComprasDisponibles.get(var + "-" + v.getCalidadVentaFin());
                            if (tComprasDisp != null && !tComprasDisp.isEmpty()) {
                                // La fecha de compra no puede ser superior al mes, así que...
                                cal.setTime(v.getFechaVenta());
                                cal.add(Calendar.DAY_OF_YEAR, -20);
                                cal2.setTime(v.getFechaVenta());
                                cal2.add(Calendar.DAY_OF_YEAR, 0);
                                tComprasDispFechas = tComprasDisp.subMap(cal.getTime(), cal2.getTime());
                                if (tComprasDispFechas != null && !tComprasDispFechas.isEmpty()) {
                                    //lComprasDisp.clear();
                                    lFechas.clear();
                                    lFechas.addAll(tComprasDispFechas.keySet());
                                    for (Date fecha : lFechas) {
                                        if (Utils.booleanFromInteger(v.getCerrada())) {
                                            break;
                                        }
                                        lCompras = tComprasDispFechas.get(fecha);
                                        lCompras2.clear();
                                        if (lCompras != null) {
                                            lCompras2.addAll(lCompras);
                                            for (TCompras cVista : lCompras2) {
                                                crea = true;
                                                if (cVista.getCalidadFin().equals(v.getCalidadVentaFin()) && cVista.getOrigenFin().equals(v.getOrigenFin())) {

                                                    // Miramos si hya líneas de ventas creadas, en ese caso, hay que restar el campo v.kgs - (suma ventas kgs_fin)
                                                    if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                                                        kgsComprobar = v.getKgsNetos();
                                                    } else {
                                                        kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                                                    }
                                                    try {
                                                        if (!v.getCalibreFin().equals("VARIOS")) {
                                                            if (!v.getCalibreFin().equals(cVista.getCalibreFin()) && !cVista.getCalibreFin().equals("VARIOS")) {
                                                                try {
                                                                    indexCalibre = lCalibresAguacate.indexOf(Integer.valueOf(v.getCalibreFin()));
                                                                } catch (Exception e) {
                                                                    continue;
                                                                }
                                                                // Si el calibre es el más pequeño, miramos el que está inmediatamente superior.
                                                                if (indexCalibre.equals(0)) {
                                                                    if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1))) {
                                                                        continue;
                                                                    }
                                                                } else if (indexCalibre.equals(lCalibresAguacate.size() - 1)) {
                                                                    // Si el calibre es el más pequeño, miramos el que está inmediatamente inferior.
                                                                    if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                                        continue;
                                                                    }
                                                                } else {
                                                                    // Si no es el más pequeño ni el más grande, miramos 1 por encima y 1 por debajo.
                                                                    if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1))
                                                                            && !Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                                        continue;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        continue;
                                                    }

                                                    // Miramos a ver si existe alguna línea creada
                                                    lLineasAux = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());
                                                    crea = true;
                                                    for (TLineasVentas v2 : lLineasAux) {
                                                        // Miramos que dentro de la misma línea de venta, no se mezclen BIO CON/SIN GGN
                                                        if (v.getCalidadVentaFin().equals("BIO") && !mProveedores.get(cVista.getProveedorFin()).equals(mProveedores.get(v2.getProveedorFin()))
                                                                && (mProveedores.get(cVista.getProveedorFin()).equals("NO GG") || mProveedores.get(v2.getProveedorFin()).equals("NO GG"))) {
                                                            crea = false;
                                                            break;
                                                        }
                                                        // Miramos también si es HASS o LAMB-HASS que todas sean iguales.
                                                        if (cVista.getVariedadFin().equals("LAMB-HASS") && !(v2.getVariedadFin().equals("LAMB-HASS"))) {
                                                            crea = false;
                                                            break;
                                                        }
                                                    }
                                                    if (!crea) {
                                                        continue;
                                                    }
                                                    if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotes) {
                                                        v.setVariedadFin(cVista.getVariedadFin());
                                                        v.setOrigenFin(cVista.getOrigenFin());
                                                        v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                        lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                                                        v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                        v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                                                        cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                                                        if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                            lIdsCompras.add(cVista.getId());
                                                            lCompras.remove(cVista);
                                                        }
                                                        cambio = true;
                                                        v.setCerrada(1);
                                                        v.setIndError(0);
                                                        v.setGgn(cVista.getGgnFin());
                                                        if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                                            lNueva.setVariedadFin("LAMB-HASS");
                                                            v.setVariedadFin("LAMB-HASS");
                                                        }
                                                        // Guardamos la línea de venta (trazabilidad)
                                                        crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                        // Guardamos la compra en el diccionario
                                                        cVista2 = new TComprasVista();
                                                        cVista2.copiaDesdeCompra(cVista);
                                                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                        // Guardamso el ID de la compra para hacer update en BD cuando acabemos
                                                        if (!lIdsComprasUpdate.contains(cVista.getId())) {
                                                            lIdsComprasUpdate.add(cVista.getId());
                                                        }
                                                        break;
                                                    } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotes) {
                                                        // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                                                        if ((v.getNumTrazabilidades() + 1) == maxLotes) {
                                                            continue;
                                                        }
                                                        v.setVariedadFin(cVista.getVariedadFin());
                                                        v.setOrigenFin(cVista.getOrigenFin());
                                                        v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                        lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                                                        v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                        v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());
                                                        kgsComprobar = v.getKgsFin();
                                                        cambio = true;
                                                        if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                                            lNueva.setVariedadFin("LAMB-HASS");
                                                            v.setVariedadFin("LAMB-HASS");
                                                        }
                                                        // Cambiamos los Kgs disponibles
                                                        lCompras.remove(cVista);
                                                        cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                        cVista.setCerrada(1);
                                                        lIdsCompras.add(cVista.getId());
                                                        // Metemos la venta en la lista con el lote nuevo                                            
                                                        // Guardamos la línea de venta (trazabilidad)
                                                        crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                        // Guardamos la compra en el diccionario
                                                        cVista2 = new TComprasVista();
                                                        cVista2.copiaDesdeCompra(cVista);
                                                        cVista2.setKgsDisponibles(Double.valueOf(0));
                                                        cVista2.setCerrada("Sí");
                                                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                        cVista2 = new TComprasVista();
                                                        cVista2.copiaDesdeCompra(cVista);
                                                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                    } else {
                                                        if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                            lCompras.remove(cVista);
                                                            lIdsCompras.add(cVista.getId());
                                                            cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                            cVista.setCerrada(1);
                                                            cVista2 = new TComprasVista();
                                                            cVista2.copiaDesdeCompra(cVista);
                                                            commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Boolean isGgn = false;
                // Comprobamos si la venta es BIO y no está completa
                if (!Utils.booleanFromInteger(v.getCerrada()) && v.getKgsNetosFin() < v.getKgsNetos() && v.getCalidadVentaFin().equals("BIO") && !repite) {
                    vuelve = true;
                    // Eliminamos las líneas, eso quiere decir que probablemente
                    List<TLineasVentas> lLineasVentas = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());

                    TCompras record = null;
                    Boolean encuentraCompra = false;
                    if (!lLineasVentas.isEmpty()) {
                        // Nos quedamos con la primera linea del GGN

                        // Por cada línea encontrada, liberamos los kgs de la compra.
                        for (TLineasVentas lv : lLineasVentas) {
                            encuentraCompra = false;
                            record = comprasSetup.obtenerCompraPorEjercicioLote(commonSetup.getEjercicio(), lv.getLoteFin());

                            isGgn = !mProveedores.get(lv.getProveedorFin()).equals("NO GG");

                            record.setPesoNetoDisponible(record.getPesoNetoDisponible() + lv.getKgsTrazabilidadFin());
                            if (Utils.booleanFromInteger(record.getCerrada())) {
                                record.setCerrada(0);
                            }

                            if (lIdsCompras.contains(record.getId())) {
                                lIdsCompras.remove(record.getId());
                            }

                            if (!lIdsComprasUpdate.contains(record.getId())) {
                                lIdsComprasUpdate.add(record.getId());
                            }

                            // Modificamos la compra en los diccionarios pertinentes.
                            TComprasVista cAux = new TComprasVista();

                            cAux.copiaDesdeCompra(record);

                            cAux.setIdExterno("" + record.getIdExterno().intValue());
                            commonSetup.mComprasId.put(cAux.getId(), cAux);
                            commonSetup.mCompras.put("" + record.getId(), record);
                            // Diccionario de compras disponibles.
                            // Si es mango, nos la trae un poco al pairo la certificación.

                            if (commonSetup.mComprasDisponibles.get(record.getVariedadFin() + "-" + record.getCalidadFin()) == null) {
                                List<TCompras> lComprasDisp = Utils.generarListaGenerica();
                                lComprasDisp.add(record);
                                TreeMap<Date, List<TCompras>> tComprasDisponibles = new TreeMap<Date, List<TCompras>>();
                                tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                                commonSetup.mComprasDisponibles.put(record.getVariedadFin() + "-" + record.getCalidadFin(), tComprasDisponibles);
                            } else {
                                TreeMap<Date, List<TCompras>> tComprasDisponibles = commonSetup.mComprasDisponibles.get(record.getVariedadFin() + "-" + record.getCalidadFin());
                                List<TCompras> lComprasDisp;
                                if (tComprasDisponibles.get(record.getFecha()) == null) {
                                    lComprasDisp = Utils.generarListaGenerica();
                                    lComprasDisp.add(record);
                                    tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                                } else {
                                    // Buscamos la compra
                                    lComprasDisp = tComprasDisponibles.get(record.getFecha());
                                    for (TCompras compr : lComprasDisp) {
                                        if (compr.getId().equals(record.getId())) {
                                            encuentraCompra = true;
                                            compr.setPesoNetoDisponible(compr.getPesoNetoDisponible() + lv.getKgsTrazadosFin());
                                            break;
                                        }
                                    }
                                    if (!encuentraCompra) {
                                        lComprasDisp.add(record);
                                        tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                                        commonSetup.mComprasDisponibles.put(record.getVariedad() + "-" + record.getCalidadFin(), tComprasDisponibles);
                                    }
                                }

                            }

                            lineasVentasSetup.eliminarLineaVentaEjercicio(commonSetup.getEjercicio(), lv.getId());

                        }
                        v.setLoteFin("");
                        v.setKgsNetosFin(Double.valueOf(0));
                        v.setNumTrazabilidades(0);
                        vVista = new TVentasVista();
                        vVista.copiaDesdeVenta(v);
                        // Metemos la actualización en el diccionario.
                        commonSetup.mVentasId.put(vVista.getId(), vVista);

                        guardarVentaAPelo(v);
                        lVariedadesProducto.clear();
                        lVariedadesProducto.add(v.getVariedadFin());
                        if (v.getVariedadFin().equals("HASS")) {
                            lVariedadesProducto.add("LAMB-HASS");
                        }
                        if (v.getVariedadFin().equals("AMARILLA") || v.getVariedadFin().toUpperCase().contains("PITAYA")) {
                            lVariedadesProducto.clear();
                            for (String var : lVariedadesPitaya) {
                                if (!lVariedadesProducto.contains(var)) {
                                    lVariedadesProducto.add(var);
                                }
                            }
                        }

                        for (String var : lVariedadesProducto) {
                            // Intentamos nutrir las ventas teniendo en cuenta el GGN que no tenia antes.
                            tComprasDisp = commonSetup.mComprasDisponibles.get(var + "-" + v.getCalidadVentaFin());
                            if (tComprasDisp != null && !tComprasDisp.isEmpty()) {
                                // La fecha de compra no puede ser superior al mes, así que...
                                cal.setTime(v.getFechaVenta());
                                cal.add(Calendar.DAY_OF_YEAR, -20);
                                cal2.setTime(v.getFechaVenta());
                                cal2.add(Calendar.DAY_OF_YEAR, +0);
                                tComprasDispFechas = tComprasDisp.subMap(cal.getTime(), cal2.getTime());
                                if (tComprasDispFechas != null && !tComprasDispFechas.isEmpty()) {
                                    //lComprasDisp.clear();
                                    lFechas.clear();
                                    lFechas.addAll(tComprasDispFechas.keySet());
                                    for (Date fecha : lFechas) {
                                        if (Utils.booleanFromInteger(v.getCerrada())) {
                                            break;
                                        }
                                        lCompras = tComprasDispFechas.get(fecha);
                                        lCompras2.clear();
                                        if (lCompras != null) {
                                            lCompras2.addAll(lCompras);
                                            for (TCompras cVista : lCompras2) {
                                                if (!isGgn.equals(mProveedores.get(cVista.getProveedorFin()).equals("NO GG"))) {
                                                    continue;
                                                }
                                                crea = true;
                                                if (cVista.getCalidadFin().equals(v.getCalidadVentaFin()) && cVista.getOrigenFin().equals(v.getOrigenFin())) {

                                                    // Miramos si hya líneas de ventas creadas, en ese caso, hay que restar el campo v.kgs - (suma ventas kgs_fin)
                                                    if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                                                        kgsComprobar = v.getKgsNetos();
                                                    } else {
                                                        kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                                                    }
                                                    try {
                                                        if (!v.getCalibreFin().equals("VARIOS")) {
                                                            if (!v.getCalibreFin().equals(cVista.getCalibreFin()) && !cVista.getCalibreFin().equals("VARIOS")) {
                                                                try {
                                                                    indexCalibre = lCalibresAguacate.indexOf(Integer.valueOf(v.getCalibreFin()));
                                                                } catch (Exception e) {
                                                                    continue;
                                                                }
                                                                // Si el calibre es el más pequeño, miramos el que está inmediatamente superior.
                                                                if (indexCalibre.equals(0)) {
                                                                    if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1))) {
                                                                        continue;
                                                                    }
                                                                } else if (indexCalibre.equals(lCalibresAguacate.size() - 1)) {
                                                                    // Si el calibre es el más pequeño, miramos el que está inmediatamente inferior.
                                                                    if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                                        continue;
                                                                    }
                                                                } else {
                                                                    // Si no es el más pequeño ni el más grande, miramos 1 por encima y 1 por debajo.
                                                                    if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1))
                                                                            && !Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                                        continue;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        continue;
                                                    }

                                                    // Miramos a ver si existe alguna línea creada
                                                    lLineasAux = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());
                                                    crea = true;
                                                    for (TLineasVentas v2 : lLineasAux) {
                                                        // Miramos que dentro de la misma línea de venta, no se mezclen BIO CON/SIN GGN
                                                        if (v.getCalidadVentaFin().equals("BIO") && !mProveedores.get(cVista.getProveedorFin()).equals(mProveedores.get(v2.getProveedorFin()))
                                                                && (mProveedores.get(cVista.getProveedorFin()).equals("NO GG") || mProveedores.get(v2.getProveedorFin()).equals("NO GG"))) {
                                                            crea = false;
                                                            break;
                                                        }
                                                        // Miramos también si es HASS o LAMB-HASS que todas sean iguales.
                                                        if (cVista.getVariedadFin().equals("LAMB-HASS") && !(v2.getVariedadFin().equals("LAMB-HASS"))) {
                                                            crea = false;
                                                            break;
                                                        }
                                                    }
                                                    if (!crea) {
                                                        continue;
                                                    }
                                                    if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotes) {
                                                        v.setVariedadFin(cVista.getVariedadFin());
                                                        v.setOrigenFin(cVista.getOrigenFin());
                                                        v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                        lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                                                        v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                        v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                                                        cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                                                        if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                            lIdsCompras.add(cVista.getId());
                                                            lCompras.remove(cVista);
                                                        }
                                                        cambio = true;
                                                        v.setCerrada(1);
                                                        v.setIndError(0);
                                                        v.setGgn(cVista.getGgnFin());
                                                        if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                                            lNueva.setVariedadFin("LAMB-HASS");
                                                            v.setVariedadFin("LAMB-HASS");
                                                        }
                                                        // Guardamos la línea de venta (trazabilidad)
                                                        crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                        // Guardamos la compra en el diccionario
                                                        cVista2 = new TComprasVista();
                                                        cVista2.copiaDesdeCompra(cVista);
                                                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                        // Guardamso el ID de la compra para hacer update en BD cuando acabemos
                                                        if (!lIdsComprasUpdate.contains(cVista.getId())) {
                                                            lIdsComprasUpdate.add(cVista.getId());
                                                        }
                                                        break;
                                                    } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotes) {
                                                        // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                                                        if ((v.getNumTrazabilidades() + 1) == maxLotes) {
                                                            continue;
                                                        }
                                                        v.setVariedadFin(cVista.getVariedadFin());
                                                        v.setOrigenFin(cVista.getOrigenFin());
                                                        v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                        lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                                                        v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                        v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());
                                                        kgsComprobar = v.getKgsFin();
                                                        cambio = true;
                                                        if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                                            lNueva.setVariedadFin("LAMB-HASS");
                                                            v.setVariedadFin("LAMB-HASS");
                                                        }
                                                        // Cambiamos los Kgs disponibles
                                                        lCompras.remove(cVista);
                                                        cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                        cVista.setCerrada(1);
                                                        lIdsCompras.add(cVista.getId());
                                                        // Metemos la venta en la lista con el lote nuevo                                            
                                                        // Guardamos la línea de venta (trazabilidad)
                                                        crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                        // Guardamos la compra en el diccionario
                                                        cVista2 = new TComprasVista();
                                                        cVista2.copiaDesdeCompra(cVista);
                                                        cVista2.setKgsDisponibles(Double.valueOf(0));
                                                        cVista2.setCerrada("Sí");
                                                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                    } else {
                                                        if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                            lCompras.remove(cVista);
                                                            lIdsCompras.add(cVista.getId());
                                                            cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                            cVista.setCerrada(1);
                                                            cVista2 = new TComprasVista();
                                                            cVista2.copiaDesdeCompra(cVista);
                                                            commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                if (!Utils.booleanFromInteger(v.getCerrada())) {
                    // Eliminamos las líneas, eso quiere decir que probablemente
                    List<TLineasVentas> lLineasVentas = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());

                    TCompras record = null;
                    Boolean encuentraCompra = false;
                    if (!lLineasVentas.isEmpty()) {
                        // Nos quedamos con la primera linea del GGN

                        // Por cada línea encontrada, liberamos los kgs de la compra.
                        for (TLineasVentas lv : lLineasVentas) {
                            encuentraCompra = false;
                            record = comprasSetup.obtenerCompraPorEjercicioLote(commonSetup.getEjercicio(), lv.getLoteFin());

                            isGgn = !mProveedores.get(lv.getProveedorFin()).equals("NO GG");

                            record.setPesoNetoDisponible(record.getPesoNetoDisponible() + lv.getKgsTrazabilidadFin());
                            if (Utils.booleanFromInteger(record.getCerrada())) {
                                record.setCerrada(0);
                            }

                            if (lIdsCompras.contains(record.getId())) {
                                lIdsCompras.remove(record.getId());
                            }

                            if (!lIdsComprasUpdate.contains(record.getId())) {
                                lIdsComprasUpdate.add(record.getId());
                            }

                            // Modificamos la compra en los diccionarios pertinentes.
                            TComprasVista cAux = new TComprasVista();

                            cAux.copiaDesdeCompra(record);

                            cAux.setIdExterno("" + record.getIdExterno().intValue());
                            commonSetup.mComprasId.put(cAux.getId(), cAux);
                            commonSetup.mCompras.put("" + record.getId(), record);
                            // Diccionario de compras disponibles.
                            // Si es mango, nos la trae un poco al pairo la certificación.

                            if (record.getFamiliaFin().equals("MANGO") || record.getFamiliaFin().equals("KUMQUAT") || record.getFamiliaFin().equals("CHIRIMOYA") || record.getFamiliaFin().equals("PITAYA") || record.getFamiliaFin().equals("PAPAYA")
                                    || record.getFamiliaFin().equals("CHIRIMOYA") || record.getFamiliaFin().contains("SPERO")) {
                                if (commonSetup.mComprasDisponibles.get(record.getVariedadFin()) == null) {
                                    List<TCompras> lComprasDisp = Utils.generarListaGenerica();
                                    lComprasDisp.add(record);
                                    TreeMap<Date, List<TCompras>> tComprasDisponibles = new TreeMap<Date, List<TCompras>>();
                                    tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                                    commonSetup.mComprasDisponibles.put(record.getVariedadFin(), tComprasDisponibles);
                                } else {
                                    TreeMap<Date, List<TCompras>> tComprasDisponibles = commonSetup.mComprasDisponibles.get(record.getVariedadFin());
                                    List<TCompras> lComprasDisp;
                                    if (tComprasDisponibles.get(record.getFecha()) == null) {
                                        lComprasDisp = Utils.generarListaGenerica();
                                        lComprasDisp.add(record);
                                        tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                                    } else {
                                        // Buscamos la compra
                                        lComprasDisp = tComprasDisponibles.get(record.getFecha());
                                        for (TCompras compr : lComprasDisp) {
                                            if (compr.getId().equals(record.getId())) {
                                                encuentraCompra = true;
                                                compr.setPesoNetoDisponible(compr.getPesoNetoDisponible() + lv.getKgsTrazadosFin());
                                                break;
                                            }
                                        }
                                        if (!encuentraCompra) {
                                            lComprasDisp.add(record);
                                            tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                                            commonSetup.mComprasDisponibles.put(record.getVariedad(), tComprasDisponibles);
                                        }
                                    }

                                }

                            } else {
                                if (commonSetup.mComprasDisponibles.get(record.getVariedadFin() + "-" + record.getCalidadFin()) == null) {
                                    List<TCompras> lComprasDisp = Utils.generarListaGenerica();
                                    lComprasDisp.add(record);
                                    TreeMap<Date, List<TCompras>> tComprasDisponibles = new TreeMap<Date, List<TCompras>>();
                                    tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                                    commonSetup.mComprasDisponibles.put(record.getVariedadFin() + "-" + record.getCalidadFin(), tComprasDisponibles);
                                } else {
                                    TreeMap<Date, List<TCompras>> tComprasDisponibles = commonSetup.mComprasDisponibles.get(record.getVariedadFin() + "-" + record.getCalidadFin());
                                    List<TCompras> lComprasDisp;
                                    if (tComprasDisponibles.get(record.getFecha()) == null) {
                                        lComprasDisp = Utils.generarListaGenerica();
                                        lComprasDisp.add(record);
                                        tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                                    } else {
                                        // Buscamos la compra
                                        lComprasDisp = tComprasDisponibles.get(record.getFecha());
                                        for (TCompras compr : lComprasDisp) {
                                            if (compr.getAlbaranFin().equals(record.getAlbaranFin()) && compr.getLoteFin().equals(record.getLoteFin())) {
                                                encuentraCompra = true;
                                                compr.setPesoNetoDisponible(compr.getPesoNetoDisponible() + lv.getKgsTrazadosFin());
                                                break;
                                            }
                                        }
                                        if (!encuentraCompra) {
                                            lComprasDisp.add(record);
                                            tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                                            commonSetup.mComprasDisponibles.put(record.getVariedad() + "-" + record.getCalidadFin(), tComprasDisponibles);
                                        }
                                    }

                                }
                            }

                            lineasVentasSetup.eliminarLineaVentaEjercicio(commonSetup.getEjercicio(), lv.getId());

                        }
                        v.setLoteFin("");
                        v.setKgsNetosFin(Double.valueOf(0));
                        v.setNumTrazabilidades(0);
                        vVista = new TVentasVista();
                        vVista.copiaDesdeVenta(v);
                        // Metemos la actualización en el diccionario.
                        commonSetup.mVentasId.put(vVista.getId(), vVista);

                        guardarVentaAPelo(v);
                        if (v.getFamiliaFin().contains("MANGO")) {
                            lVariedadesProducto.clear();

                            for (String var : lVariedadesMango) {
                                if (!lVariedadesProducto.contains(var)) {
                                    lVariedadesProducto.add(var);
                                }
                            }
                        } else {
                            lVariedadesProducto.clear();
                            lVariedadesProducto.add(v.getVariedadFin());
                            if (v.getVariedadFin().equals("HASS")) {
                                lVariedadesProducto.add("LAMB-HASS");
                            }
                            if (v.getVariedadFin().equals("AMARILLA") || v.getVariedadFin().toUpperCase().contains("PITAYA")) {
                                lVariedadesProducto.clear();
                                for (String var : lVariedadesPitaya) {
                                    if (!lVariedadesProducto.contains(var)) {
                                        lVariedadesProducto.add(var);
                                    }
                                }
                            }
                        }
                    }
                    // Si la venta no está cerrada miramos si podemos cerrarla sin importar el orígen
                    for (String var : lVariedadesProducto) {
                        if (Utils.booleanFromInteger(v.getCerrada())) {
                            break;
                        }
                        // Buscamos la primera calidad que en contremos y tan amigos....                        
                        if ((v.getFamiliaFin().equals("MANGO") || v.getFamiliaFin().equals("KUMQUAT") || v.getFamiliaFin().equals("CHIRIMOYA") || v.getFamiliaFin().equals("PITAYA") || v.getFamiliaFin().equals("PAPAYA")
                                || v.getFamiliaFin().equals("CHIRIMOYA") || v.getFamiliaFin().contains("SPERO"))) {
                            // En MANGO no guardamos en el diccionario la calidad así que solo buscamos por variedad.

                            String varAnt = v.getVariedadFin();
                            String orAnt = v.getOrigenFin();

                            tComprasDisp = commonSetup.mComprasDisponibles.get(var);
                            if (tComprasDisp != null && !tComprasDisp.isEmpty()) {
                                // La fecha de compra no puede ser superior al mes, así que...
                                cal.setTime(v.getFechaVenta());
                                if (v.getFamiliaFin().equals("CHIRIMOYA")) {
                                    cal.add(Calendar.DAY_OF_YEAR, -15);
                                } else if (v.getFamiliaFin().contains("SPERO")) {
                                    cal.add(Calendar.DAY_OF_YEAR, -15);
                                } else {
                                    cal.add(Calendar.DAY_OF_YEAR, -20);
                                }
                                // En el guacamole cogemos todas las fechas excepto si la venta es anterior a la compra.
                                cal2.setTime(v.getFechaVenta());
                                cal2.add(Calendar.DAY_OF_YEAR, +0);
                                tComprasDispFechas = tComprasDisp.subMap(cal.getTime(), cal2.getTime());
                                if (tComprasDispFechas != null && !tComprasDispFechas.isEmpty()) {
                                    //lComprasDisp.clear();
                                    lFechas.clear();
                                    lFechas.addAll(tComprasDispFechas.keySet());
                                    for (Date fecha : lFechas) {
                                        if (Utils.booleanFromInteger(v.getCerrada())) {
                                            break;
                                        }
                                        lCompras = tComprasDispFechas.get(fecha);
                                        lCompras2.clear();
                                        if (lCompras != null) {
                                            lCompras2.addAll(lCompras);
                                            for (TCompras cVista : lCompras2) {
                                                // Esto es para no caer con la misma piedra
                                                if (cVista.getOrigenFin().equals(orAnt)) {
                                                    continue;
                                                }
                                                if (cVista.getVariedadFin().equals(varAnt)) {
                                                    continue;
                                                }
                                                if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                                                    kgsComprobar = v.getKgsNetos();
                                                } else {
                                                    kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                                                }

                                                if ((!v.getCalidadVentaFin().equals(cVista.getCalidadFin()) && cVista.getCalidadFin().equals("BIO")) || v.getCalidadVentaFin().equals(cVista.getCalidadFin())) {
                                                    // Miramos a ver si existe alguna línea creada
                                                    lLineasAux = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());
                                                    crea = true;
                                                    if (!lLineasAux.isEmpty()) {
                                                        // Dentro de un mismo movimiento, no se pueden mezclar los orígenes.
                                                        if (!cVista.getOrigenFin().equals(v.getOrigenFin())) {
                                                            continue;
                                                        }
                                                    }
                                                    for (TLineasVentas v2 : lLineasAux) {
                                                        // Miramos que dentro de la misma línea de venta, no se mezclen BIO CON/SIN GGN
                                                        if (v.getCalidadVentaFin().equals("BIO") && !mProveedores.get(cVista.getProveedorFin()).equals(mProveedores.get(v2.getProveedorFin()))
                                                                && (mProveedores.get(cVista.getProveedorFin()).equals("NO GG") || mProveedores.get(v2.getProveedorFin()).equals("NO GG"))) {
                                                            crea = false;
                                                            break;
                                                        }
                                                    }
                                                } else {
                                                    crea = false;
                                                }
                                                if (!crea) {
                                                    continue;
                                                }

                                                if (v.getFamiliaFin().equals("MANGO") && !lLineasAux.isEmpty() && !cVista.getVariedadFin().equals(v.getVariedadFin()) && !v.getOrigenFin().equals(cVista.getOrigenFin())) {
                                                    continue;
                                                }

                                                if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotes) {
                                                    v.setVariedadFin(cVista.getVariedadFin());
                                                    v.setOrigenFin(cVista.getOrigenFin());
                                                    // EN ESTA CASUÍSTICA LA CALIDAD NO SE TOCA.
                                                    v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                    v.setOrigenFin(cVista.getOrigenFin());
                                                    lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                                                    lNueva.setCertificacionIni(v.getCalidadVenta());
                                                    lNueva.setCertificacionFin(v.getCalidadVentaFin());
                                                    v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                                                    v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                    cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                                                    if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                        lIdsCompras.add(cVista.getId());
                                                        lCompras.remove(cVista);
                                                    }
                                                    cambio = true;
                                                    v.setCerrada(1);
                                                    v.setGgn(cVista.getGgnFin());
                                                    v.setIndError(0);
                                                    // Guardamos la línea de venta (trazabilidad)
                                                    crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                    // Guardamos la compra en el diccionario
                                                    cVista2 = new TComprasVista();
                                                    cVista2.copiaDesdeCompra(cVista);
                                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                    // Guardamso el ID de la compra para hacer update en BD cuando acabemos
                                                    if (!lIdsComprasUpdate.contains(cVista.getId())) {
                                                        lIdsComprasUpdate.add(cVista.getId());
                                                    }
                                                    break;
                                                } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotes) {
                                                    // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                                                    if (Integer.valueOf(v.getNumTrazabilidades() + 1).equals(maxLotes)) {
                                                        continue;
                                                    }
                                                    v.setVariedadFin(cVista.getVariedadFin());
                                                    // EN ESTA CASUÍSTICA LA CALIDAD NO SE TOCA.
                                                    v.setOrigenFin(cVista.getOrigenFin());
                                                    v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                    lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                                                    lNueva.setCertificacionIni(v.getCalidadVenta());
                                                    lNueva.setCertificacionFin(v.getCalidadVentaFin());
                                                    v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                    v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());

                                                    kgsComprobar = v.getKgsFin();
                                                    cambio = true;
                                                    // Cambiamos los Kgs disponibles
                                                    lCompras.remove(cVista);
                                                    cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                    cVista.setCerrada(1);
                                                    lIdsCompras.add(cVista.getId());
                                                    // Guardamos la línea de venta (trazabilidad)
                                                    crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                    // Guardamos la compra en el diccionario
                                                    cVista2 = new TComprasVista();
                                                    cVista2.copiaDesdeCompra(cVista);
                                                    cVista2.setKgsDisponibles(Double.valueOf(0));
                                                    cVista2.setCerrada("Sí");
                                                    commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                } else {
                                                    if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                        lCompras.remove(cVista);
                                                        lIdsCompras.add(cVista.getId());
                                                        cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                        cVista.setCerrada(1);
                                                        cVista2 = new TComprasVista();
                                                        cVista2.copiaDesdeCompra(cVista);
                                                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            tComprasDisp = commonSetup.mComprasDisponibles.get(var + "-" + v.getCalidadVentaFin());
                            if (tComprasDisp != null && !tComprasDisp.isEmpty()) {
                                // La fecha de compra no puede ser superior al mes, así que...
                                cal.setTime(v.getFechaVenta());
                                cal.add(Calendar.DAY_OF_YEAR, -20);
                                cal2.setTime(v.getFechaVenta());
                                cal2.add(Calendar.DAY_OF_YEAR, +0);
                                tComprasDispFechas = tComprasDisp.subMap(cal.getTime(), cal2.getTime());
                                if (tComprasDispFechas != null && !tComprasDispFechas.isEmpty()) {
                                    //lComprasDisp.clear();
                                    lFechas.clear();
                                    lFechas.addAll(tComprasDispFechas.keySet());
                                    for (Date fecha : lFechas) {
                                        if (Utils.booleanFromInteger(v.getCerrada())) {
                                            break;
                                        }
                                        lCompras = tComprasDispFechas.get(fecha);
                                        lCompras2.clear();
                                        if (lCompras != null) {
                                            lCompras2.addAll(lCompras);
                                            for (TCompras cVista : lCompras2) {
                                                crea = true;
                                                if (cVista.getCalidadFin().equals(v.getCalidadVentaFin())) {

                                                    // Miramos si hya líneas de ventas creadas, en ese caso, hay que restar el campo v.kgs - (suma ventas kgs_fin)
                                                    if (v.getKgsNetosFin().equals(Double.valueOf(0))) {
                                                        kgsComprobar = v.getKgsNetos();
                                                    } else {
                                                        kgsComprobar = Utils.redondeoDecimales(2, v.getKgsNetos() - v.getKgsNetosFin());
                                                    }
                                                    try {
                                                        if (!v.getCalibreFin().equals("VARIOS")) {
                                                            if (!v.getCalibreFin().equals(cVista.getCalibreFin()) && !cVista.getCalibreFin().equals("VARIOS")) {
                                                                try {
                                                                    indexCalibre = lCalibresAguacate.indexOf(Integer.valueOf(v.getCalibreFin()));
                                                                } catch (Exception e) {
                                                                    continue;
                                                                }
                                                                // Si el calibre es el más pequeño, miramos el que está inmediatamente superior.
                                                                if (indexCalibre.equals(0)) {
                                                                    if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1))) {
                                                                        continue;
                                                                    }
                                                                } else if (indexCalibre.equals(lCalibresAguacate.size() - 1)) {
                                                                    // Si el calibre es el más pequeño, miramos el que está inmediatamente inferior.
                                                                    if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                                        continue;
                                                                    }
                                                                } else {
                                                                    // Si no es el más pequeño ni el más grande, miramos 1 por encima y 1 por debajo.
                                                                    if (!Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre + 1))
                                                                            && !Integer.valueOf(cVista.getCalibreFin()).equals(lCalibresAguacate.get(indexCalibre - 1))) {
                                                                        continue;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        continue;
                                                    }

                                                    // Miramos a ver si existe alguna línea creada
                                                    lLineasAux = lineasVentasSetup.obtenerLineasVentasEjercicioIdPedido(commonSetup.getEjercicio(), v.getIdExterno());
                                                    crea = true;
                                                    for (TLineasVentas v2 : lLineasAux) {
                                                        // Miramos que dentro de la misma línea de venta, no se mezclen BIO CON/SIN GGN
                                                        if (v.getCalidadVentaFin().equals("BIO") && !mProveedores.get(cVista.getProveedorFin()).equals(mProveedores.get(v2.getProveedorFin()))
                                                                && (mProveedores.get(cVista.getProveedorFin()).equals("NO GG") || mProveedores.get(v2.getProveedorFin()).equals("NO GG"))) {
                                                            crea = false;
                                                            break;
                                                        }
                                                        // Miramos también si es HASS o LAMB-HASS que todas sean iguales.
                                                        if (cVista.getVariedadFin().equals("LAMB-HASS") && !(v2.getVariedadFin().equals("LAMB-HASS"))) {
                                                            crea = false;
                                                            break;
                                                        }
                                                    }
                                                    if (!crea) {
                                                        continue;
                                                    }
                                                    if (cVista.getPesoNetoDisponible() >= kgsComprobar && v.getNumTrazabilidades() < maxLotes) {
                                                        v.setVariedadFin(cVista.getVariedadFin());
                                                        v.setOrigenFin(cVista.getOrigenFin());
                                                        v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                        lNueva = nutrirLineaDesdeVentaCompra(v, cVista, kgsComprobar);
                                                        v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                        v.setKgsNetosFin(Utils.redondeoDecimales(2, (v.getKgsNetosFin() + kgsComprobar)));
                                                        cVista.setPesoNetoDisponible(Utils.redondeoDecimales(2, cVista.getPesoNetoDisponible() - kgsComprobar));
                                                        if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                            lIdsCompras.add(cVista.getId());
                                                            lCompras.remove(cVista);
                                                        }
                                                        cambio = true;
                                                        v.setCerrada(1);
                                                        v.setIndError(0);
                                                        v.setGgn(cVista.getGgnFin());
                                                        if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                                            lNueva.setVariedadFin("LAMB-HASS");
                                                            v.setVariedadFin("LAMB-HASS");
                                                        }
                                                        // Guardamos la línea de venta (trazabilidad)
                                                        crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                        // Guardamos la compra en el diccionario
                                                        cVista2 = new TComprasVista();
                                                        cVista2.copiaDesdeCompra(cVista);
                                                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                        // Guardamso el ID de la compra para hacer update en BD cuando acabemos
                                                        if (!lIdsComprasUpdate.contains(cVista.getId())) {
                                                            lIdsComprasUpdate.add(cVista.getId());
                                                        }
                                                        break;
                                                    } else if (cVista.getPesoNetoDisponible() > Double.valueOf(0) && v.getNumTrazabilidades() < maxLotes) {
                                                        v.setVariedadFin(cVista.getVariedadFin());
                                                        v.setOrigenFin(cVista.getOrigenFin());
                                                        // Si se cumple la condición, quiere decir que es el último lote del máximo, con lo cual faltarán kgs.
                                                        if ((v.getNumTrazabilidades() + 1) == maxLotes) {
                                                            continue;
                                                        }
                                                        v.setNumTrazabilidades(v.getNumTrazabilidades() + 1);
                                                        lNueva = nutrirLineaDesdeVentaCompra(v, cVista, cVista.getPesoNetoDisponible());
                                                        v.setLoteFin(v.getLoteFin() + ", " + lNueva.getLoteFin());
                                                        v.setKgsNetosFin(v.getKgsNetosFin() + cVista.getPesoNetoDisponible());
                                                        kgsComprobar = v.getKgsFin();
                                                        cambio = true;
                                                        if (cVista.getVariedadFin().equals("LAMB-HASS")) {
                                                            lNueva.setVariedadFin("LAMB-HASS");
                                                            v.setVariedadFin("LAMB-HASS");
                                                        }
                                                        // Cambiamos los Kgs disponibles
                                                        lCompras.remove(cVista);
                                                        cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                        cVista.setCerrada(1);
                                                        lIdsCompras.add(cVista.getId());
                                                        // Metemos la venta en la lista con el lote nuevo                                            
                                                        // Guardamos la línea de venta (trazabilidad)
                                                        crearTrazabilidadVentaRetornaId(lNueva, false, false);
                                                        // Guardamos la compra en el diccionario
                                                        cVista2 = new TComprasVista();
                                                        cVista2.copiaDesdeCompra(cVista);
                                                        cVista2.setKgsDisponibles(Double.valueOf(0));
                                                        cVista2.setCerrada("Sí");
                                                        commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                    } else {
                                                        if (cVista.getPesoNetoDisponible().equals(Double.valueOf(0))) {
                                                            lCompras.remove(cVista);
                                                            lIdsCompras.add(cVista.getId());
                                                            cVista.setPesoNetoDisponible(Double.valueOf(0));
                                                            cVista.setCerrada(1);
                                                            cVista2 = new TComprasVista();
                                                            cVista2.copiaDesdeCompra(cVista);
                                                            commonSetup.mComprasId.put(cVista2.getId(), cVista2);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (cambio) {
                if (Utils.booleanFromInteger(v.getCerrada())) {
                    v.setIndError(0);
                    // Quitamos la venta del diccionario resultado.
                    mResult.remove(v.getId());
                    commonSetup.mIdVentasErroneas.remove(v.getId());
                    vVista = new TVentasVista();
                    vVista.copiaDesdeVenta(v);
                    vVista.setError("0");
                    vVista.setIndError("No");
                    // Metemos la actualización en el diccionario.
                    commonSetup.mVentasId.put(vVista.getId(), vVista);
                    commonSetup.mVentasId.get("240");

                } else if (!v.getKgsFin().equals(v.getKgs())) {
                    v.setIndError(1);
                    mResult.put(v.getId(), v);
                    vVista = new TVentasVista();
                    vVista.copiaDesdeVenta(v);
                    vVista.setError("1");
                    vVista.setIndError("Sí");
                    commonSetup.mIdVentasErroneas.put(v.getId(), v);
                    // Metemos la actualización en el diccionario.
                    commonSetup.mVentasId.put(vVista.getId(), vVista);
                }
                guardarVentaAPelo(v);
            }
        }

        // Cerramos las compras que se han cerrado durante las correcciones realizadas
        if (!lIdsCompras.isEmpty()) {
            comprasSetup.cerrarComprasEjercicioKgsDisponiblesCero(commonSetup.getEjercicio(), lIdsCompras);
        }

        lIdsComprasUpdate.removeAll(lIdsCompras);

        TCompras compra;
        // Guardamos las compras que no han sido cerradas y han sido modificadas (peso disponible)        
        for (Integer idCompr : lIdsComprasUpdate) {
            compra = new TCompras();
            compra.copiaDesdeCompraVista(commonSetup.mComprasId.get("" + idCompr));
            compra.setId(idCompr);
            if (lIdsCompras.contains(idCompr)) {
                continue;
            } else {
                comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), compra);
            }

        }
        if (vuelve) {
            corregirVentasErroneas(maxLotes, maxLotesGuacamole, indBio, soloBio, todos, vuelve);
        }

        lVentas.addAll(mResult.values());
        commonSetup.mIdVentasErroneas.clear();
        for (TVentas vv : lVentas) {
            vv.setIndError(1);
            vVista = new TVentasVista();
            vVista.copiaDesdeVenta(vv);
            vVista.setError("1");
            vVista.setIndError("Sí");
            commonSetup.mIdVentasErroneas.put(vv.getId(), vv);
            // Metemos la actualización en el diccionario.
            commonSetup.mVentasId.put(vVista.getId(), vVista);
        }

        // Comprobamos si hay ventas que tienen mas kgs que en compras.
        //comprobarComprasVentas();
        return mResult;
    }

    /**
     * Método que nos retorna las compras a partir de los Ids.
     * @param ids Lista de IDs de las compras a buscar.
     * @return Las compras encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TCompras> obtenerComprasIds(List<Integer> lIds) throws GenasoftException {
        return comprasSetup.obtenerComprasEjercicioIds(commonSetup.getEjercicio(), lIds);
    }

    /**
     * Método que nos retorna las compras a partir de los Ids.
     * @param ids Lista de IDs de las compras a buscar.
     * @return Las compras encontradas.
     */
    public List<TComprasFict> obtenerComprasFictIds(List<Integer> lIds) {
        return tComprasFictMapper.obtenerComprasIds(lIds);
    }

    /**
     * Método que nos retorna las ventas a partir de los Ids.
     * @param ids Lista de IDs de las ventas a buscar.
     * @return Las ventas encontradas.
     * @throws GenasoftException 
     */
    public List<TVentas> obtenerVentasIds(List<Integer> lIds) throws GenasoftException {
        return ventasSetup.obtenerVentasEjercicioIds(commonSetup.getEjercicio(), lIds);
    }

    /**
     * Método que nos retorna las ventas a partir de los Ids.
     * @param ids Lista de IDs de las ventas a buscar.
     * @return Las ventas encontradas.
     */
    public List<TVentasFict> obtenerVentasFictIds(List<Integer> lIds) {
        return tVentasFictMapper.obtenerVentasIds(lIds);
    }

    /**
     * Método que nos retorna las líneas del albarán de venta pasado por parámetro, retorna las líneas sumando las cajas de las diferentes lineas que puede tener del mismo producto
     * @param albaran El albarán
     * @return Las líneas de venta encontradas. Éstas se hace un GROUP BY de familia_fin, variedad_fin, calibre_fin
     * @throws GenasoftException 
     */
    public List<TVentasVista> obtenerLineasVentaAlbaran(String albaran) throws GenasoftException {
        return convertirVentasVista(ventasSetup.obtenerLineasVentasEjercicio(commonSetup.getEjercicio(), albaran));
    }

    /**
     * Método que nos retorna las líneas del albarán de venta pasado por parámetro, retorna las líneas sumando las cajas de las diferentes lineas que puede tener del mismo producto
     * @param albaran El albarán
     * @return Las líneas de venta encontradas. Éstas se hace un GROUP BY de familia_fin, variedad_fin, calibre_fin
     * @throws GenasoftException 
     */
    public List<TVentasVista> obtenerTodasLineasVentaAlbaran(String albaran) throws GenasoftException {
        return convertirVentasVista(ventasSetup.obtenerTodasLineasVentasEjercicio(commonSetup.getEjercicio(), albaran));
    }

    /**
     * Método que nos retorna las líneas del albarán de venta pasado por parámetro, retorna las líneas sumando las cajas de las diferentes lineas que puede tener del mismo producto
     * @param albaran El albarán
     * @return Las líneas de venta encontradas. Éstas se hace un GROUP BY de familia_fin, variedad_fin, calibre_fin
     */
    public List<TVentasFictVista> obtenerTodasLineasVentaFictAlbaran(String albaran) {
        return convertirVentasFictVista(tVentasFictMapper.obtenerTodasLineasVentaAlbaran(albaran));
    }

    private List<TComprasVista> convertirComprasVista(List<TCompras> lCompras) {
        List<TComprasVista> lResult = Utils.generarListaGenerica();

        TComprasVista aux = null;

        for (TCompras compra : lCompras) {
            aux = new TComprasVista();

            aux.copiaDesdeCompra(compra);

            lResult.add(aux);
        }

        return lResult;

    }

    private List<TVentasVista> convertirVentasVista(List<TVentas> lVentas) {
        List<TVentasVista> lResult = Utils.generarListaGenerica();

        TVentasVista aux = null;

        for (TVentas venta : lVentas) {
            aux = new TVentasVista();

            aux.copiaDesdeVenta(venta);

            lResult.add(aux);
        }

        return lResult;

    }

    private List<TComprasFictVista> convertirComprasFictVista(List<TComprasFict> lCompras) {
        List<TComprasFictVista> lResult = Utils.generarListaGenerica();

        TComprasFictVista aux = null;

        for (TComprasFict compra : lCompras) {
            aux = new TComprasFictVista();

            aux.copiaDesdeCompra(compra);

            lResult.add(aux);
        }

        return lResult;
    }

    private List<TVentasFictVista> convertirVentasFictVista(List<TVentasFict> lVentas) {
        List<TVentasFictVista> lResult = Utils.generarListaGenerica();

        TVentasFictVista aux = null;

        for (TVentasFict venta : lVentas) {
            aux = new TVentasFictVista();

            aux.copiaDesdeVentaFic(venta);

            lResult.add(aux);
        }

        return lResult;

    }

    private List<TLineasVentasVista> convertirLineasVentasVista(List<TLineasVentas> lVentas) {
        List<TLineasVentasVista> lResult = Utils.generarListaGenerica();

        TLineasVentasVista aux = null;

        for (TLineasVentas venta : lVentas) {
            aux = new TLineasVentasVista();

            aux.copiaDesdeLinea(venta);

            lResult.add(aux);
        }

        return lResult;

    }

    public List<TVentasVista> obtenerBalanceMasasProducto(String producto, String calidad, String origen, String ggn, String variedad, Date f1, Date f2) throws GenasoftException {
        //Map<String, TVentasVista> mResult = new HashMap<String, TVentasVista>();

        List<TVentasVista> lResult = Utils.generarListaGenerica();
        DecimalFormat df = new DecimalFormat("#,##0.000");

        String org = origen.equals("Todos") ? null : origen;
        String prod = producto.equals("Todos") ? null : producto;
        String var = variedad.equals("Todas") ? null : variedad;
        String cal = calidad.equals("Todas") ? null : calidad;
        String gn = ggn.equals("Todos") ? null : ggn;

        //String origenAux = origen;
        //String calidadAux = calidad;
        //String ggnAux = ggn;

        // CLAVE: ID-ART_VARIEDAD_CLASIFICACION_ORIGEN_GGN
        Map<String, TVentasVista> mResultado = new HashMap<String, TVentasVista>();

        List<TCompras> lCompras = Utils.generarListaGenerica();

        if (org.equals("DESGLOSADO") && prod.equals("DESGLOSADO") && var.equals("DESGLOSADO") && cal.equals("DESGLOSADO") && gn.equals("DESGLOSADO")) {
            lCompras = comprasSetup.obtenerComprasBalanceMasasEjercicioTodosGroupBy(commonSetup.getEjercicio());
        }

        // Obtenemos las ventazs
        List<TVentas> lVentas = Utils.generarListaGenerica();

        if (org.equals("DESGLOSADO") && prod.equals("DESGLOSADO") && var.equals("DESGLOSADO") && cal.equals("DESGLOSADO") && gn.equals("DESGLOSADO")) {
            lVentas = ventasSetup.obtenerVentasEjercicioBalanceMasasTodosGroupBy(commonSetup.getEjercicio());
        }

        TVentasVista aux = null;

        // Nutrimos el diccionario con los datos que hemos ido recopilando, por cada registro de compra, deberá de haber uno de venta

        for (TCompras compra : lCompras) {
            if (compra == null) {
                continue;
            }
            // Buscamos si existe el registro en el diccionario de resultado
            aux = mResultado.get(compra.getFamiliaFin() + "_" + compra.getVariedadFin() + "_" + compra.getOrigenFin() + "_" + compra.getGgnFin());
            if (aux == null) {
                // Nutrimos los campos del balance de masas que hacen referencia a la compra.
                aux = new TVentasVista();
                aux.setId("" + mResultado.size() + 1);
                aux.setVariedad(compra.getVariedadFin());
                aux.setFamilia(compra.getFamiliaFin());
                aux.setKgsNetos(compra.getPesoNetoFin());
                aux.setKgsEnvase("0,00%");
                aux.setOrigen(compra.getOrigenFin());
                aux.setKgsFin(Double.valueOf(0));
                aux.setCalidadCompra(compra.getCalidadFin());
                aux.setAlbaran(compra.getGgnFin());
                aux.setFechaVenta(compra.getFechaFin());
                aux.setFechaVentaFin(compra.getFechaFin());
            } else {
                // Sumamos los kgs, y si hay kgs de ventas, calculamos el porcentaje.
                aux.setKgsNetos(aux.getKgsNetos() + compra.getPesoNetoFin());
                if (aux.getKgsNetosFin().equals(Double.valueOf(0))) {
                    aux.setKgsEnvase("0,00%");
                } else {
                    aux.setKgsEnvase(df.format(aux.getKgsNetos() / aux.getKgsNetosFin()) + "%");
                }
                if (ggn == null) {
                    aux.setAlbaran("TODAS");
                } else {
                    aux.setAlbaran(ggn);
                }
            }

            mResultado.put(compra.getFamiliaFin() + "_" + compra.getVariedadFin() + "_" + compra.getOrigenFin() + "_" + compra.getGgnFin(), aux);
        }

        // Hacemos lo propio con las ventas, en este caso, debería haber registro de compra.

        for (TVentas venta : lVentas) {
            if (venta == null) {
                continue;
            }

            aux = mResultado.get(venta.getFamiliaFin() + "_" + venta.getVariedadFin() + "_" + venta.getOrigenFin() + "_" + venta.getGgn());

            if (aux == null) {
                // Nutrimos los campos del balance de masas que hacen referencia a la compra.
                aux = new TVentasVista();
                aux.setId("" + mResultado.size() + 1);
                aux.setVariedad(venta.getVariedadFin());
                aux.setFamilia(venta.getFamiliaFin());
                aux.setKgsNetosFin(venta.getKgsNetosFin());
                aux.setKgsEnvase("0,00%");
                aux.setOrigen(venta.getOrigenFin());
                aux.setCalidadCompra(venta.getCalidadVentaFin());
                aux.setAlbaran(venta.getGgn());
                aux.setFechaVenta(venta.getFechaVentaFin());
                aux.setFechaVentaFin(venta.getFechaVentaFin());
            } else {
                // Sumamos los kgs, y si hay kgs de ventas, calculamos el porcentaje.
                if (aux.getKgsNetosFin() + venta.getKgsNetosFin() < aux.getKgsNetos()) {
                    aux.setKgsNetosFin(aux.getKgsNetosFin() + venta.getKgsNetosFin());
                } else {
                    aux.setKgsNetosFin(aux.getKgsNetos() - (aux.getKgsNetos() * 5 / 100));
                }
                if (aux.getKgsNetosFin() < Double.valueOf(0)) {
                    aux.setKgsNetosFin(aux.getKgsNetos() - (aux.getKgsNetos() * 7 / 100));
                }
                if (aux.getKgsNetosFin().equals(Double.valueOf(0))) {
                    aux.setKgsEnvase("0,00%");
                } else {
                    aux.setKgsEnvase(df.format(aux.getKgsNetos() / aux.getKgsNetosFin()) + "%");
                }
            }

            mResultado.put(venta.getFamiliaFin() + "_" + venta.getVariedadFin() + "_" + venta.getOrigenFin() + "_" + venta.getGgn(), aux);
        }

        lResult.addAll(mResultado.values());

        lResult.sort(new Comparator<TVentasVista>() {

            @Override
            public int compare(TVentasVista m1, TVentasVista m2) {
                if (m1.getFamilia() == m2.getFamilia()) {
                    return 0;
                }
                return m1.getFamilia().compareTo(m2.getFamilia());
            }

        });

        return lResult;
    }

    public List<TVentasVista> obtenerBalancePorProducto2(String tipoBalance, Date f1, Date f2) throws GenasoftException {

        List<TVentasVista> lResult = Utils.generarListaGenerica();
        DecimalFormat df = new DecimalFormat("#,##0.000");

        // En función del tipo de balance, haremos una consulta u otra.

        // CLAVE: ID-ART_VARIEDAD_CLASIFICACION_ORIGEN_GGN
        Map<String, TVentasVista> mResultado = new HashMap<String, TVentasVista>();

        List<TCompras> lCompras = Utils.generarListaGenerica();
        List<TVentas> lVentas = Utils.generarListaGenerica();

        TVentasVista aux = null;
        String origenAux = null;

        // Balance de masas por producto, certificación y nacional/importación
        if (tipoBalance.equals("Certificación-Nacional/importación")) {
            lCompras = comprasSetup.obtenerComprasCertificacionNacionalImportacionEjercicio(commonSetup.getEjercicio());
            lVentas = ventasSetup.obtenerVentasEjercicioCertificacionNacionalImportacion(commonSetup.getEjercicio());

            // Preparamos los datos
            for (TCompras compra : lCompras) {
                if (compra == null) {
                    continue;
                }

                origenAux = compra.getOrigenFin().equals("ESPAÑA") ? "Nacional" : "Importación";

                // Buscamos si existe el registro en el diccionario de resultado
                aux = mResultado.get(compra.getFamiliaFin() + "_" + compra.getCalidadFin() + "_" + origenAux);
                if (aux == null) {
                    // Nutrimos los campos del balance de masas que hacen referencia a la compra.
                    aux = new TVentasVista();
                    aux.setId("" + mResultado.size() + 1);
                    aux.setVariedad("Todas");
                    aux.setFamilia(compra.getFamiliaFin());
                    aux.setKgsNetos(compra.getPesoNetoFin());
                    aux.setKgsEnvase("0,00%");
                    aux.setOrigen(origenAux);
                    aux.setKgsFin(Double.valueOf(0));
                    aux.setCalidadCompra(compra.getCalidadFin());
                    aux.setAlbaran("Todas");
                    aux.setFechaVenta(compra.getFechaFin());
                    aux.setFechaVentaFin(compra.getFechaFin());
                } else {
                    // Sumamos los kgs, y si hay kgs de ventas, calculamos el porcentaje.
                    aux.setKgsNetos(aux.getKgsNetos() + compra.getPesoNetoFin());
                    if (aux.getKgsNetosFin().equals(Double.valueOf(0))) {
                        aux.setKgsEnvase("0,00%");
                    } else {
                        aux.setKgsEnvase(df.format(aux.getKgsNetos() / aux.getKgsNetosFin()) + "%");
                    }

                }

                mResultado.put(compra.getFamiliaFin() + "_" + compra.getCalidadFin() + "_" + origenAux, aux);
            }

            // Hacemos lo propio con las ventas, en este caso, debería haber registro de compra.

            for (TVentas venta : lVentas) {
                if (venta == null) {
                    continue;
                }

                origenAux = venta.getOrigenFin().equals("ESPAÑA") ? "Nacional" : "Importación";

                aux = mResultado.get(venta.getFamiliaFin() + "_" + venta.getCalidadVentaFin() + "_" + origenAux);

                if (aux == null) {
                    // Nutrimos los campos del balance de masas que hacen referencia a la compra.
                    aux = new TVentasVista();
                    aux.setId("" + mResultado.size() + 1);
                    aux.setVariedad("Todas");
                    aux.setFamilia(venta.getFamiliaFin());
                    aux.setKgsNetosFin(venta.getKgsNetosFin());
                    aux.setKgsEnvase("0,00%");
                    aux.setOrigen(origenAux);
                    aux.setCalidadCompra(venta.getCalidadVentaFin());
                    aux.setAlbaran("Todas");
                    aux.setFechaVenta(venta.getFechaVentaFin());
                    aux.setFechaVentaFin(venta.getFechaVentaFin());
                } else {
                    // Sumamos los kgs, y si hay kgs de ventas, calculamos el porcentaje.
                    if (aux.getKgsNetosFin() + venta.getKgsNetosFin() < aux.getKgsNetos()) {
                        aux.setKgsNetosFin(aux.getKgsNetosFin() + venta.getKgsNetosFin());
                    } else {
                        aux.setKgsNetosFin(aux.getKgsNetos() - (aux.getKgsNetos() * 5 / 100));
                    }
                    if (aux.getKgsNetosFin() < Double.valueOf(0)) {
                        aux.setKgsNetosFin(aux.getKgsNetos() - (aux.getKgsNetos() * 7 / 100));
                    }
                    if (aux.getKgsNetosFin().equals(Double.valueOf(0))) {
                        aux.setKgsEnvase("0,00%");
                    } else {
                        aux.setKgsEnvase(df.format(aux.getKgsNetos() / aux.getKgsNetosFin()) + "%");
                    }
                }

                mResultado.put(venta.getFamiliaFin() + "_" + venta.getCalidadVentaFin() + "_" + origenAux, aux);
            }

        } else if (tipoBalance.equals("Certificación-Origen")) {
            lCompras = comprasSetup.obtenerComprasCertificacionNacionalImportacionEjercicio(commonSetup.getEjercicio());
            lVentas = ventasSetup.obtenerVentasEjercicioCertificacionNacionalImportacion(commonSetup.getEjercicio());

            // Preparamos los datos
            for (TCompras compra : lCompras) {
                if (compra == null) {
                    continue;
                }

                origenAux = compra.getOrigenFin();

                // Buscamos si existe el registro en el diccionario de resultado
                aux = mResultado.get(compra.getFamiliaFin() + "_" + compra.getCalidadFin() + "_" + origenAux);
                if (aux == null) {
                    // Nutrimos los campos del balance de masas que hacen referencia a la compra.
                    aux = new TVentasVista();
                    aux.setId("" + mResultado.size() + 1);
                    aux.setVariedad("Todas");
                    aux.setFamilia(compra.getFamiliaFin());
                    aux.setKgsNetos(compra.getPesoNetoFin());
                    aux.setKgsEnvase("0,00%");
                    aux.setOrigen(origenAux);
                    aux.setKgsFin(Double.valueOf(0));
                    aux.setCalidadCompra(compra.getCalidadFin());
                    aux.setAlbaran("Todas");
                    aux.setFechaVenta(compra.getFechaFin());
                    aux.setFechaVentaFin(compra.getFechaFin());
                } else {
                    // Sumamos los kgs, y si hay kgs de ventas, calculamos el porcentaje.
                    aux.setKgsNetos(aux.getKgsNetos() + compra.getPesoNetoFin());
                    if (aux.getKgsNetosFin().equals(Double.valueOf(0))) {
                        aux.setKgsEnvase("0,00%");
                    } else {
                        aux.setKgsEnvase(df.format(aux.getKgsNetos() / aux.getKgsNetosFin()) + "%");
                    }

                }

                mResultado.put(compra.getFamiliaFin() + "_" + compra.getCalidadFin() + "_" + origenAux, aux);
            }

            // Hacemos lo propio con las ventas, en este caso, debería haber registro de compra.

            for (TVentas venta : lVentas) {
                if (venta == null) {
                    continue;
                }

                origenAux = venta.getOrigenFin();

                aux = mResultado.get(venta.getFamiliaFin() + "_" + venta.getCalidadVentaFin() + "_" + origenAux);

                if (aux == null) {
                    // Nutrimos los campos del balance de masas que hacen referencia a la compra.
                    aux = new TVentasVista();
                    aux.setId("" + mResultado.size() + 1);
                    aux.setVariedad("Todas");
                    aux.setFamilia(venta.getFamiliaFin());
                    aux.setKgsNetosFin(venta.getKgsNetosFin());
                    aux.setKgsEnvase("0,00%");
                    aux.setOrigen(origenAux);
                    aux.setCalidadCompra(venta.getCalidadVentaFin());
                    aux.setAlbaran("Todas");
                    aux.setFechaVenta(venta.getFechaVentaFin());
                    aux.setFechaVentaFin(venta.getFechaVentaFin());
                } else {
                    // Sumamos los kgs, y si hay kgs de ventas, calculamos el porcentaje.
                    if (aux.getKgsNetosFin() + venta.getKgsNetosFin() < aux.getKgsNetos()) {
                        aux.setKgsNetosFin(aux.getKgsNetosFin() + venta.getKgsNetosFin());
                    } else {
                        aux.setKgsNetosFin(aux.getKgsNetos() - (aux.getKgsNetos() * 5 / 100));
                    }
                    if (aux.getKgsNetosFin() < Double.valueOf(0)) {
                        aux.setKgsNetosFin(aux.getKgsNetos() - (aux.getKgsNetos() * 7 / 100));
                    }
                    if (aux.getKgsNetosFin().equals(Double.valueOf(0))) {
                        aux.setKgsEnvase("0,00%");
                    } else {
                        aux.setKgsEnvase(df.format(aux.getKgsNetos() / aux.getKgsNetosFin()) + "%");
                    }
                }

                mResultado.put(venta.getFamiliaFin() + "_" + venta.getCalidadVentaFin() + "_" + origenAux, aux);
            }

        } else if (tipoBalance.equals("Origen-Global Gap")) {
            lCompras = comprasSetup.obtenerComprasGlobalGapEjercicio(commonSetup.getEjercicio());
            lVentas = ventasSetup.obtenerVentasEjercicioGlobalGap(commonSetup.getEjercicio());

            // Preparamos los datos
            for (TCompras compra : lCompras) {
                if (compra == null) {
                    continue;
                }

                origenAux = compra.getOrigenFin();

                // Buscamos si existe el registro en el diccionario de resultado
                aux = mResultado.get(compra.getFamiliaFin() + "_" + compra.getGgnFin() + "_" + origenAux);
                if (aux == null) {
                    // Nutrimos los campos del balance de masas que hacen referencia a la compra.
                    aux = new TVentasVista();
                    aux.setId("" + mResultado.size() + 1);
                    aux.setVariedad("Todas");
                    aux.setFamilia(compra.getFamiliaFin());
                    aux.setKgsNetos(compra.getPesoNetoFin());
                    aux.setKgsEnvase("0,00%");
                    aux.setOrigen(origenAux);
                    aux.setKgsFin(Double.valueOf(0));
                    aux.setCalidadCompra(compra.getCalidadFin());
                    aux.setAlbaran("Todas");
                    aux.setFechaVenta(compra.getFechaFin());
                    aux.setFechaVentaFin(compra.getFechaFin());
                } else {
                    // Sumamos los kgs, y si hay kgs de ventas, calculamos el porcentaje.
                    aux.setKgsNetos(aux.getKgsNetos() + compra.getPesoNetoFin());
                    if (aux.getKgsNetosFin().equals(Double.valueOf(0))) {
                        aux.setKgsEnvase("0,00%");
                    } else {
                        aux.setKgsEnvase(df.format(aux.getKgsNetos() / aux.getKgsNetosFin()) + "%");
                    }

                }

                mResultado.put(compra.getFamiliaFin() + "_" + compra.getGgnFin() + "_" + origenAux, aux);
            }

            // Hacemos lo propio con las ventas, en este caso, debería haber registro de compra.

            for (TVentas venta : lVentas) {
                if (venta == null) {
                    continue;
                }

                origenAux = venta.getOrigenFin();

                aux = mResultado.get(venta.getFamiliaFin() + "_" + venta.getGgn() + "_" + origenAux);

                if (aux == null) {
                    // Nutrimos los campos del balance de masas que hacen referencia a la compra.
                    aux = new TVentasVista();
                    aux.setId("" + mResultado.size() + 1);
                    aux.setVariedad("Todas");
                    aux.setFamilia(venta.getFamiliaFin());
                    aux.setKgsNetosFin(venta.getKgsNetosFin());
                    aux.setKgsEnvase("0,00%");
                    aux.setOrigen(origenAux);
                    aux.setCalidadCompra(venta.getCalidadVentaFin());
                    aux.setAlbaran("Todas");
                    aux.setFechaVenta(venta.getFechaVentaFin());
                    aux.setFechaVentaFin(venta.getFechaVentaFin());
                } else {
                    // Sumamos los kgs, y si hay kgs de ventas, calculamos el porcentaje.
                    if (aux.getKgsNetosFin() + venta.getKgsNetosFin() < aux.getKgsNetos()) {
                        aux.setKgsNetosFin(aux.getKgsNetosFin() + venta.getKgsNetosFin());
                    } else {
                        aux.setKgsNetosFin(aux.getKgsNetos() - (aux.getKgsNetos() * 5 / 100));
                    }
                    if (aux.getKgsNetosFin() < Double.valueOf(0)) {
                        aux.setKgsNetosFin(aux.getKgsNetos() - (aux.getKgsNetos() * 7 / 100));
                    }
                    if (aux.getKgsNetosFin().equals(Double.valueOf(0))) {
                        aux.setKgsEnvase("0,00%");
                    } else {
                        aux.setKgsEnvase(df.format(aux.getKgsNetos() / aux.getKgsNetosFin()) + "%");
                    }
                }

                mResultado.put(venta.getFamiliaFin() + "_" + venta.getGgn() + "_" + origenAux, aux);
            }

        }

        lResult.addAll(mResultado.values());

        lResult.sort(new Comparator<TVentasVista>() {

            @Override
            public int compare(TVentasVista m1, TVentasVista m2) {
                if (m1.getFamilia() == m2.getFamilia()) {
                    return 0;
                }
                return m1.getFamilia().compareTo(m2.getFamilia());
            }

        });

        return lResult;
    }

    /**
     * Método que nos comprueba que los kgs de ventas son mayores que los de compras.
     * @throws GenasoftException 
     */
    public void comprobarComprasVentas() throws GenasoftException {

        // Obtenemos todas las compras
        String result = "";
        List<TCompras> lCompras = comprasSetup.obtenerTodasComprasEjercicio(commonSetup.getEjercicio());
        DecimalFormat df = new DecimalFormat("#,##0.000");
        Double ventas;
        Integer cnt = 0;
        for (TCompras compra : lCompras) {
            ventas = ventasSetup.obtenerKgsLoteEjercicio(commonSetup.getEjercicio(), compra.getLoteFin());
            if (ventas == null) {
                continue;
            }
            if (Utils.redondeoDecimales(2, ventas) > Utils.redondeoDecimales(2, compra.getPesoNeto())) {
                result = result + "Los Kgs del lote de compra: " + compra.getLoteFin() + " son mayores en las ventas. Kgs compras: " + df.format(compra.getPesoNeto()) + ", Kgs ventas: " + df.format(ventas) + "\n ";
                cnt++;
            }
        }

        if (!result.isEmpty()) {
            try {
                commonSetup.enviaNotificacionCorreo("Kgs MAL Genasoft, TOTAL " + cnt, result, null, 1);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (GenasoftException e) {
                e.printStackTrace();
            }
        } else {
            try {
                commonSetup.enviaNotificacionCorreo("Kgs Genasoft, TOTAL " + cnt, "NO SE HAN DETECTADO KGS DE VENTA SUPERIOR A KGS DE COMPRA", null, 1);
                commonSetup.enviaNotificacionWhatsAppMasivo("NO SE HAN DETECTADO KGS DE VENTA SUPERIOR A KGS DE COMPRA");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (GenasoftException e) {
                e.printStackTrace();
            }
        }

    }

    public List<TComprasVista> obtenerComprasAlbaranesLotesPartidas(List<String> lAlbaranes, List<String> lLotes, List<String> lPartidas) throws GenasoftException {
        List<TComprasVista> lResult = null;

        List<TCompras> lCompras = comprasSetup.obtenerComprasAlbaranesLotesPartidasEjercicio(commonSetup.getEjercicio(), lAlbaranes, lLotes, lPartidas);

        lResult = convertirComprasVista(lCompras);

        return lResult;
    }

    public List<TComprasFictVista> obtenerComprasFictAlbaranesLotesPartidas(List<String> lAlbaranes, List<String> lLotes, List<String> lPartidas) {
        List<TComprasFictVista> lResult = null;

        List<TComprasFict> lCompras = tComprasFictMapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);

        lResult = convertirComprasFictVista(lCompras);

        return lResult;
    }

    public TComprasFictVista obtenerCompraFicticiaVistaPorId(Integer id) {
        List<TComprasFictVista> lResult = Utils.generarListaGenerica();

        TComprasFict aux = obtenerCompraFicticiaPorId(id);

        if (aux != null) {
            List<TComprasFict> lResult2 = Utils.generarListaGenerica();
            lResult2.add(aux);
            lResult = convertirComprasFictVista(lResult2);

            return lResult.get(0);
        } else {
            return null;
        }

    }

    public TComprasFict obtenerCompraFicticiaPorId(Integer id) {
        return tComprasFictMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna las líneas de venta a partir del lote fin
     * @param lote El lote por el que realizar la consulta
     * @return Las líneas de venta encontradas.
     * @throws GenasoftException 
     */
    public List<TVentas> obtenerVentasProductoBultosKg(String producto, String variedad, Integer bultos, Double kgs, String calidad, Date fecha, Date fecha2) throws GenasoftException {
        return ventasSetup.obtenerVentasEjercicioProductoBultosKg(commonSetup.getEjercicio(), producto, variedad, bultos, kgs, calidad, fecha, fecha2);
    }

    /**
     * Método que nos retorna las líneas de venta a partir del lote fin
     * @param lote El lote por el que realizar la consulta
     * @return Las líneas de venta encontradas.
     * @throws GenasoftException 
     */
    public List<TVentas> obtenerVentasProductoKg(String producto, String variedad, Double kgs, String calidad, Date fecha, Date fecha2) throws GenasoftException {
        return ventasSetup.obtenerVentasProductoKg(commonSetup.getEjercicio(), producto, variedad, kgs, calidad, fecha, fecha2);
    }

    /**
     * Método que nos genera las ventas a partir de la compra pasada por parámetro.
     * @param compraVista Los datos de la venta.
     * @param fechaCompra La fecha de la compra ficticia.
     * @param calidad La calidad de la compra ficticia.
     * @return El resultado de la opeeración.
     * @throws GenasoftException 
     */
    public Integer generarVentasDesdeCompra(TComprasVista compraVista, Date fechaCompra, String calidad) throws GenasoftException {
        Integer result = -1;

        Date fCompra = null;
        Date fCompra2 = null;
        String cal = null;
        // En primer lugar, creamos el objeto compra_fict con los datos de la compra reales.
        TCompras cAux = new TCompras();
        cAux.copiaDesdeCompraVista(compraVista);

        if (fechaCompra == null) {
            fCompra = cAux.getFechaFin();
        } else {
            fCompra = fechaCompra;
        }
        if (calidad == null) {
            cal = cAux.getCalidadFin();
        } else {
            cal = calidad;
        }

        // La fecha hasta de las ventas no debe superar los 31 días naturales
        Calendar calend = Calendar.getInstance();
        calend.setTime(fCompra);

        calend.add(Calendar.DAY_OF_YEAR, 31);

        fCompra2 = calend.getTime();

        // Buscamos las ventas a partir del producto, bultos, kgs calidad y fecha
        List<TVentas> lVentas = obtenerVentasProductoBultosKg(cAux.getFamiliaFin(), cAux.getVariedadFin(), cAux.getCajasFin(), cAux.getPesoNetoFin(), cal, fCompra, fCompra2);

        if (!lVentas.isEmpty()) {
            TComprasFict cFict = new TComprasFict();
            cFict.copiaDesdeCompra(cAux);
            // Seteamos los datos que nos viene por parámetro (fecha y calidad)            
            cFict.setFechaFin(fCompra);
            cFict.setCalidadFin(cal);
            cFict.setId(crearCompraFictRetornaId(cFict));

            // Una vez tenemos creada la compra, creamos las ventas
            if (cFict.getId() >= 1) {
                result = cFict.getId();
                TVentasFict vFict = null;
                vFict = new TVentasFict();
                vFict.copiaDesdeVenta(lVentas.get(0));

                // Seteamos los datos de la compra
                vFict.setIdCompraFict(cFict.getId());
                vFict.setKgsFin(cFict.getPesoNetoFin());
                vFict.setLoteFin(cFict.getLoteFin());
                vFict.setAlbaranCompraFin(cFict.getAlbaranFin());
                vFict.setOrigenFin(cFict.getOrigenFin());
                vFict.setProveedorFin(cFict.getProveedorFin());
                // Creamos la venta ficticia con los datos de la venta real y los datos de la compra                            
                if (crearVentaFicticiaRetornaId(vFict) < 1) {
                    return -1;
                }
            } else {
                result = -1;
            }

        } else {
            // Si no encontramos ventas, buscamos las ventas a partir del producto, kgs calidad y fecha
            if (lVentas.isEmpty()) {
                Double kgsAux = Double.valueOf(0);
                lVentas = obtenerVentasProductoKg(cAux.getFamiliaFin(), cAux.getVariedadFin(), cAux.getPesoNetoFin(), cal, fCompra, fCompra2);

                TComprasFict cFict = new TComprasFict();
                TVentasFict vFict = null;
                if (!lVentas.isEmpty()) {

                    cFict.copiaDesdeCompra(cAux);
                    // Seteamos los datos que nos viene por parámetro (fecha y calidad)            
                    cFict.setFechaFin(fCompra);
                    cFict.setCalidadFin(cal);
                    cFict.setId(crearCompraFictRetornaId(cFict));

                    // Una vez tenemos creada la compra, creamos las ventas
                    if (cFict.getId() >= 1) {
                        result = cFict.getId();
                        for (TVentas v : lVentas) {
                            if (kgsAux >= cAux.getPesoNetoFin()) {
                                break;
                            }
                            vFict = new TVentasFict();
                            vFict.copiaDesdeVenta(v);
                            if (vFict.getKgsFin() > (kgsAux + cFict.getPesoNetoFin())) {
                                vFict.setKgsFin(kgsAux);
                            }
                            // Seteamos los datos de la compra
                            vFict.setIdCompraFict(cFict.getId());
                            vFict.setLoteFin(cFict.getLoteFin());
                            vFict.setAlbaranCompraFin(cFict.getAlbaranFin());
                            vFict.setOrigenFin(cFict.getOrigenFin());
                            vFict.setProveedorFin(cFict.getProveedorFin());
                            // Creamos la venta ficticia con los datos de la venta real y los datos de la compra                            
                            if (crearVentaFicticiaRetornaId(vFict) < 1) {
                                return -1;
                            }
                            kgsAux = kgsAux + vFict.getKgsFin();
                        }

                    } else {
                        result = -1;
                    }
                }
            }

        }

        if (lVentas.isEmpty()) {
            result = -2;
        }

        return result;

    }

    /**
     * @return the lNumerosAlbaranCompras
     */
    public List<String> obtenerNumerosAlbaranComprasFict() {
        return tComprasFictMapper.obtenerNumerosAlbaranCompras();
    }

    /**
     * @return the lNumerosAlbaranCompras
     */
    public List<String> obtenerLotesComprasFict() {
        return tComprasFictMapper.obtenerLotesCompras();
    }

    /**
     * @return the lNumerosAlbaranCompras
     */
    public List<String> obtenerPartidasComprasFict() {
        return tComprasFictMapper.obtenerPartidasCompras();
    }

    public List<String> obtenerNumAlbaranIdsPedidos(List<Integer> lIdsPedidos) throws GenasoftException {
        return ventasSetup.obtenerNumAlbaranEjercicioIdsPedidos(commonSetup.getEjercicio(), lIdsPedidos);
    }

    public List<String> obtenerNumAlbaranIdsPedidosFict(List<Integer> lIdsPedidos) {
        return tVentasFictMapper.obtenerNumAlbaranIdsPedidos(lIdsPedidos);
    }

    /**
     * Método que nos comprueba si la línea de venta tiene algún campo referente a la venta que no corresponda.
     * @param linea Los datos de la línea de venta a comprobar
     * @param venta La venta a comprobar.
     * @return 0 Si no hay error
     * 1 --> Error en la certificación.
     * 2 --> Los kgs de la línea superan los kgs "disponibles" en la venta.
     * 3 --> No existe la venta.
     */
    private Integer validaLineaVentaErrorVenta(TLineasVentas linea, TVentas venta) {
        Integer result = 0;
        if (venta != null) {
            // Se da si la línea es calidad C y la venta B.
            //if (!linea.getCertificacionFin().equals(venta.getCalidadVentaFin()) && venta.getCalidadVentaFin().equals(CertificacionesEnum.BIO.getValue())) {
            if (!linea.getCertificacionFin().equals(venta.getCalidadVentaFin())) {
                result = ERROR_CERTIFICACION_VENTA;
            } else {
                // Aquí partimos de que la venta es B o es C y la venta puede ser B (esto es correcto)
                if ((venta.getKgsNetosFin() + linea.getKgsTrazabilidadFin()) > venta.getKgsNetos()) {
                    result = ERROR_KGS_VENTA;
                }
            }
        } else {
            result = ERROR_VENTA_NO_EXISTE;
        }

        return result;
    }

    /**
     * Método que nos comprueba si los datos de la línea y la compra son correctos
     * @param linea Los datos de la línea de venta a comprobar. 
     * @param compra Los datos de la compra a comprobar.
     * @return true si los datos no son correctos
     */
    private Integer validaLineaVentaErrorCompra(TCompras compra) {
        Integer result = 0;

        if (compra != null) {
            // Comprobamos si la calidad corresponde.
            if (!lVenta.getCertificacionFin().equals(compra.getCalidadFin())) {
                result = ERROR_CERTIFICACION_COMPRA;
            } else {
                if (lVenta.getKgsTrazabilidadFin() > compra.getPesoNetoDisponible()) {
                    return ERROR_KGS_COMPRA;
                } else {
                    if (!lVenta.getAlbaranCompraFin().equals(compra.getAlbaranFin())) {
                        lVenta.setAlbaranCompraFin(compra.getAlbaranFin());
                        lVenta.setIndCambio(1);
                    }
                    if (!lVenta.getProveedorFin().equals(compra.getProveedorFin())) {
                        lVenta.setProveedorFin(compra.getProveedorFin());
                        lVenta.setIndCambio(1);
                    }
                }
            }
        } else {
            result = ERROR_VENTA_NO_EXISTE_COMPRA;
        }

        return result;
    }

    private TLineasVentas nutrirLineaDesdeVentaCompra(TVentas v, TCompras c, Double kgs) {
        TLineasVentas result = new TLineasVentas();
        result.setAlbaranCompraIni(c.getAlbaranFin());
        result.setAlbaranCompraFin(c.getAlbaranFin());
        result.setBultosPaleFin(v.getNumBultosPaleFin() != null ? v.getNumBultosPaleFin() : -1);
        result.setBultosPaleIni(v.getNumBultosPaleFin() != null ? v.getNumBultosPale() : -1);
        result.setCertificacionFin(c.getCalidadFin());
        result.setCertificacionIni(c.getCalidadFin());
        result.setDetalleError("");
        result.setGgn(c.getGgnFin());
        result.setIdExterno(Double.valueOf(-1));
        result.setIdVenta(v.getId());
        result.setIdVentaExterno(v.getIdExterno());
        result.setIndBasura(Utils.intFromBoolean(false));
        result.setIndCambio(Utils.intFromBoolean(false));
        result.setIndCerrada(Utils.intFromBoolean(true));
        result.setIndError(Utils.intFromBoolean(false));
        result.setKgsTrazabilidadFin(kgs);
        result.setKgsTrazabilidadIni(kgs);
        result.setKgsTrazadosFin(kgs);
        result.setKgsTrazadosIni(kgs);
        result.setLoteFin(c.getPartidaFin());
        result.setLoteIni(c.getLoteFin());
        result.setProveedorFin(c.getProveedorFin());
        result.setProveedorIni(c.getProveedorFin());
        result.setReferenciaCompraFin("");
        result.setReferenciaCompraIni("");
        result.setVariedadFin(c.getVariedadFin());
        result.setVariedadIni(v.getVariedadFin());
        result.setFamiliaFin(c.getVariedadFin());
        result.setFamiliaIni(v.getFamiliaFin());

        return result;

    }

    private void logicaModificacionCompra(TCompras aux, TCompras record) throws GenasoftException {

        if (lTodasVariedades == null) {
            lTodasVariedades = Utils.generarListaGenerica();
        } else {
            lTodasVariedades.clear();
        }
        lTodasVariedades.addAll(commonSetup.mProductosComprasDisponibles.keySet());

        // Miramos qué ha cambiado
        if (!record.getPesoNetoFin().equals(aux.getPesoNetoFin())) {
            // Si el cambio es a mayores, aumentamos el disponible y abrimos la compra, sin que afecte a las ventas asociadas.
            if (record.getPesoNetoFin() > aux.getPesoNetoFin()) {
                // Abrimos la compra.
                record.setCerrada(0);
                record.setPesoNetoDisponible(Utils.redondeoDecimales(2, record.getPesoNetoDisponible() + (record.getPesoNetoFin() - aux.getPesoNetoFin())));
            } else {
                // Si es a menores, y la compra está cerrada tenemos que reabrir las ventas asociadas, abrir la compra, y recalcular los kgs disponibles
                // Abrimos la compra
                if (Utils.booleanFromInteger(aux.getCerrada())) {
                    record.setCerrada(0);
                    // Reabrimos las ventas asociadas.
                    // Buscamos las ventas que están asociadas al lote de compra, y las reabrimos
                    List<TVentas> lVentas = ventasSetup.obtenerVentasEjercicioLoteFin(commonSetup.getEjercicio(), record.getLoteFin());
                    List<Integer> lIdsVentas = Utils.generarListaGenerica();
                    TVentasVista vVista = null;
                    if (lVentas != null) {
                        for (TVentas v : lVentas) {
                            if (Utils.booleanFromInteger(v.getCerrada())) {
                                vVista = new TVentasVista();
                                vVista.copiaDesdeVenta(v);
                                // Metemos las ventas como erróneas
                                commonSetup.mVentasId.put(vVista.getId(), vVista);
                                if (!lIdsVentas.contains(Integer.valueOf(v.getId()))) {
                                    lIdsVentas.add(v.getId());
                                }
                            }
                        }
                        if (!lIdsVentas.isEmpty()) {
                            ventasSetup.abrirVentasEjercicioIdsVentas(commonSetup.getEjercicio(), lIdsVentas);
                            // Eliminamos las líneas con id de venta asociada a las ventas que hemos abierto.
                            lineasVentasSetup.eliminarLineasVentasEjercicioIdsVentas(commonSetup.getEjercicio(), lIdsVentas);
                        }
                    }

                } else {
                    // Miramos si la diferencia de Kgs se "come" alguna venta, en ese caso, abrimos las ventas.
                    if (record.getPesoNetoDisponible() < aux.getPesoNetoDisponible()) {
                        // Reabrimos las ventas asociadas.
                        // Buscamos las ventas que están asociadas al lote de compra, y las reabrimos
                        List<TVentas> lVentas = ventasSetup.obtenerVentasEjercicioLoteFin(commonSetup.getEjercicio(), record.getLoteFin());
                        List<Integer> lIdsVentas = Utils.generarListaGenerica();
                        TVentasVista vVista = null;
                        if (lVentas != null) {
                            for (TVentas v : lVentas) {
                                if (Utils.booleanFromInteger(v.getCerrada())) {
                                    vVista = new TVentasVista();
                                    vVista.copiaDesdeVenta(v);
                                    // Metemos las ventas como erróneas
                                    commonSetup.mVentasId.put(vVista.getId(), vVista);
                                    if (!lIdsVentas.contains(Integer.valueOf(v.getId()))) {
                                        lIdsVentas.add(v.getId());
                                    }
                                }
                            }
                            if (!lIdsVentas.isEmpty()) {
                                ventasSetup.abrirVentasEjercicioIdsVentas(commonSetup.getEjercicio(), lIdsVentas);
                                // Eliminamos las líneas con id de venta asociada a las ventas que hemos abierto.
                                lineasVentasSetup.eliminarLineasVentasEjercicioIdsVentas(commonSetup.getEjercicio(), lIdsVentas);
                            }
                        }
                    }
                }
            }

            // Modificamos la compra en los diccionarios pertinentes.
            TComprasVista cAux = new TComprasVista();
            record.setId(aux.getId());
            record.setIdExterno(aux.getIdExterno());
            cAux.copiaDesdeCompra(record);

            cAux.setIdExterno("" + aux.getIdExterno().intValue());
            commonSetup.mComprasId.put(cAux.getId(), cAux);
            commonSetup.mCompras.put("" + record.getId(), record);
            Boolean encuentraCompra = false;
            // Diccionario de compras disponibles.
            // Si es mango, nos la trae un poco al pairo la certificación.
            if (record.getFamiliaFin().equals("MANGO") || record.getFamiliaFin().equals("KUMQUAT") || record.getFamiliaFin().equals("CHIRIMOYA") || record.getFamiliaFin().equals("PITAYA") || record.getFamiliaFin().equals("PAPAYA")
                    || record.getFamiliaFin().equals("CHIRIMOYA") || record.getFamiliaFin().contains("SPERO")) {
                if (commonSetup.mComprasDisponibles.get(record.getVariedadFin()) == null) {
                    List<TCompras> lComprasDisp = Utils.generarListaGenerica();
                    lComprasDisp.add(record);
                    TreeMap<Date, List<TCompras>> tComprasDisponibles = new TreeMap<Date, List<TCompras>>();
                    tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                    commonSetup.mComprasDisponibles.put(record.getVariedadFin(), tComprasDisponibles);
                } else {
                    TreeMap<Date, List<TCompras>> tComprasDisponibles = commonSetup.mComprasDisponibles.get(record.getVariedadFin());
                    List<TCompras> lComprasDisp;
                    if (tComprasDisponibles.get(record.getFecha()) == null) {
                        lComprasDisp = Utils.generarListaGenerica();
                        lComprasDisp.add(record);
                        tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                    } else {
                        // Buscamos la compra
                        lComprasDisp = tComprasDisponibles.get(record.getFecha());
                        for (TCompras compr : lComprasDisp) {
                            if (compr.getId().equals(record.getId())) {
                                encuentraCompra = true;
                                compr.setPesoNetoDisponible(record.getPesoNetoDisponible());
                                break;
                            }
                        }
                        if (!encuentraCompra) {
                            lComprasDisp.add(record);
                            tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                            commonSetup.mComprasDisponibles.put(record.getVariedad(), tComprasDisponibles);
                        }
                    }
                }

            } else {
                if (commonSetup.mComprasDisponibles.get(record.getVariedadFin() + "-" + record.getCalidadFin()) == null) {
                    List<TCompras> lComprasDisp = Utils.generarListaGenerica();
                    lComprasDisp.add(record);
                    TreeMap<Date, List<TCompras>> tComprasDisponibles = new TreeMap<Date, List<TCompras>>();
                    tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                    commonSetup.mComprasDisponibles.put(record.getVariedadFin() + "-" + record.getCalidadFin(), tComprasDisponibles);
                } else {
                    TreeMap<Date, List<TCompras>> tComprasDisponibles = commonSetup.mComprasDisponibles.get(record.getVariedadFin() + "-" + record.getCalidadFin());
                    List<TCompras> lComprasDisp;
                    if (tComprasDisponibles.get(record.getFecha()) == null) {
                        lComprasDisp = Utils.generarListaGenerica();
                        lComprasDisp.add(record);
                        tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                    } else {
                        // Buscamos la compra
                        lComprasDisp = tComprasDisponibles.get(record.getFecha());
                        for (TCompras compr : lComprasDisp) {
                            if (compr.getId().equals(record.getId())) {
                                encuentraCompra = true;
                                compr.setPesoNetoDisponible(record.getPesoNetoDisponible());
                                break;
                            }
                        }
                        if (!encuentraCompra) {
                            lComprasDisp.add(record);
                            tComprasDisponibles.put(record.getFecha(), lComprasDisp);
                            commonSetup.mComprasDisponibles.put(record.getVariedad() + "-" + record.getCalidadFin(), tComprasDisponibles);
                        }
                    }

                }
            }

        } else {
            // Si cambian la calidad, proveedor o variedad, reabrimos las ventas
            if (!record.getCalidadFin().equals(aux.getCalidadFin()) || !record.getVariedadFin().equals(aux.getVariedadFin()) || !record.getProveedorFin().equals(aux.getProveedorFin())) {
                List<Integer> lIdsVentas = Utils.generarListaGenerica();
                if (!record.getCalidadFin().equals(aux.getCalidadFin()) || !record.getVariedadFin().equals(aux.getVariedadFin())) {
                    record.setCerrada(0);
                    record.setPesoNetoDisponible(record.getPesoNetoFin());
                    List<TVentas> lVentas = ventasSetup.obtenerVentasEjercicioLoteFin(commonSetup.getEjercicio(), record.getLoteFin());
                    TVentasVista vVista;
                    if (lVentas != null) {
                        for (TVentas v : lVentas) {
                            if (Utils.booleanFromInteger(v.getCerrada())) {
                                vVista = new TVentasVista();
                                vVista.copiaDesdeVenta(v);
                                // Metemos las ventas como erróneas
                                commonSetup.mVentasId.put(vVista.getId(), vVista);
                                if (!lIdsVentas.contains(Integer.valueOf(v.getId()))) {
                                    lIdsVentas.add(v.getId());
                                }
                            }
                        }
                        if (!lIdsVentas.isEmpty()) {
                            ventasSetup.abrirVentasEjercicioIdsVentas(commonSetup.getEjercicio(), lIdsVentas);
                            // Eliminamos las líneas con id de venta asociada a las ventas que hemos abierto.
                            lineasVentasSetup.eliminarLineasVentasEjercicioIdsVentas(commonSetup.getEjercicio(), lIdsVentas);
                        }
                    }
                } else {
                    // SOLO CAMBIA EL PROVEEDOR CON LO CUAL ÚNICAMENTE MODIFICAMOS EL PROVEEDOR_FINAL EN LAS LINEAS DE VENTAS Y TAN AMIGOS...
                    List<String> lLotes = Utils.generarListaGenerica();
                    lLotes.add(record.getLoteFin());
                    List<TLineasVentas> lLineas = lineasVentasSetup.obtenerLineasVentasEjercicioLotes(commonSetup.getEjercicio(), lTodasVariedades);
                    for (TLineasVentas v : lLineas) {
                        v.setProveedorFin(record.getProveedorFin());
                        lineasVentasSetup.modificarLineaVentaEjercicio(commonSetup.getEjercicio(), v);
                    }

                }
            }
        }
        // Guardamos el registro
        record.setId(aux.getId());
        comprasSetup.modificarCompraEjercicio(commonSetup.getEjercicio(), record);
    }

    /**
     * Método que nos carga los diferentes años en los que hay datos.
     */
    public void obtenerEjerciciosDisponibles(Integer ejercicio) {

        lEjercicios = Utils.generarListaGenerica();
        lEjercicios.add("Todos");

        if (ejercicio > 2022) {
            lEjercicios.add("2022");
        }
        if (ejercicio >= 2023) {
            lEjercicios.add("2023");
        }
        if (ejercicio >= 2024) {
            lEjercicios.add("2024");
        }
        if (ejercicio >= 2025) {
            lEjercicios.add("2025");
        }
        if (ejercicio >= 2026) {
            lEjercicios.add("2026");
        }
        if (ejercicio >= 2027) {
            lEjercicios.add("2027");
        }
        if (ejercicio >= 2028) {
            lEjercicios.add("2028");
        }
        if (ejercicio >= 2029) {
            lEjercicios.add("2029");
        }
        if (ejercicio >= 2030) {
            lEjercicios.add("2030");
        }
    }

    public void cargarDatosFruttec(Date fecha) {

    }

    private void rellenarArbolCompras() {

        Map<String, TComprasVista> mCompras = commonSetup.getmComprasEjercicio();

        List<TComprasVista> lComprasVista = Utils.generarListaGenerica();

        lComprasVista.addAll(mCompras.values());

        // Creamos el árbol para tener los datos estructurados por fechas
        TreeMap<Date, List<TComprasVista>> trMap = new TreeMap<Date, List<TComprasVista>>();
        try {
            // Guardamos los resultados de compras sin facturar
            for (TComprasVista compra : lComprasVista) {
                // Lo metemos en el diccionario
                if (trMap.get(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha())) == null) {
                    List<TComprasVista> lResult = Utils.generarListaGenerica();
                    lResult.add(compra);
                    if (compra.getFecha() != null) {
                        trMap.put(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha()), lResult);
                    } else {
                        continue;
                    }

                } else {
                    List<TComprasVista> lResult = trMap.get(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha()));
                    lResult.add(compra);
                    // Ordenamos la lista por nº de albarán 
                    lResult.sort(new Comparator<TComprasVista>() {

                        @Override
                        public int compare(TComprasVista m1, TComprasVista m2) {
                            if (m1.getAlbaran() == m2.getAlbaran()) {
                                return 0;
                            }
                            return m1.getAlbaran().compareTo(m2.getAlbaran());
                        }

                    });
                    trMap.put(new SimpleDateFormat("dd/MM/yyyy").parse(compra.getFecha()), lResult);
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        commonSetup.settComprasEjercicio(trMap);
    }

    private void rellenarArbolVentas() {

        TreeMap<Date, List<TVentasVista>> trMap = new TreeMap<Date, List<TVentasVista>>();

        Map<String, TVentasVista> mVentas = commonSetup.getmVentasEjercicio();

        List<TVentasVista> lVentasVista = Utils.generarListaGenerica();

        lVentasVista.addAll(mVentas.values());

        // Guardamos los resultados de compras sin facturar
        for (TVentasVista compra : lVentasVista) {
            // Lo metemos en el diccionario
            if (trMap.get(compra.getFechaVenta()) == null) {
                List<TVentasVista> lResult = Utils.generarListaGenerica();
                lResult.add(compra);
                if (compra.getCerrada().toLowerCase().equals("sí")) {
                    compra.setIndError("No");
                    compra.setError("0");
                }
                if (compra.getFechaVenta() != null) {
                    trMap.put(compra.getFechaVenta(), lResult);
                } else {
                    continue;
                }

            } else {
                List<TVentasVista> lResult = trMap.get(compra.getFechaVenta());
                lResult.add(compra);
                // Ordenamos la lista por nº de albarán 
                lResult.sort(new Comparator<TVentasVista>() {

                    @Override
                    public int compare(TVentasVista m1, TVentasVista m2) {
                        if (m1.getAlbaran() == m2.getAlbaran()) {
                            return 0;
                        }
                        return m1.getAlbaran().compareTo(m2.getAlbaran());
                    }

                });
                trMap.put(compra.getFechaVenta(), lResult);
            }
        }

        commonSetup.settVentasEjercicio(trMap);

    }

    /**
    public List<PtdAlm> obtenerPartidasComprasFruttec(Date fecha1, Date fecha2) {
    
    }
    */
}
