/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.common;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.controller.DataBaseConnection;
import com.dina.genasoft.db.entity.TAcceso;
import com.dina.genasoft.db.entity.TAccesoHis;
import com.dina.genasoft.db.entity.TCompras;
import com.dina.genasoft.db.entity.TComprasVista;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TErrores;
import com.dina.genasoft.db.entity.TLineasVentas;
import com.dina.genasoft.db.entity.TPermisos;
import com.dina.genasoft.db.entity.TTrace;
import com.dina.genasoft.db.entity.TTrace2;
import com.dina.genasoft.db.entity.TVentas;
import com.dina.genasoft.db.entity.TVentasVista;
import com.dina.genasoft.db.mapper.TAccesoHisMapper;
import com.dina.genasoft.db.mapper.TAccesoMapper;
import com.dina.genasoft.db.mapper.TErroresMapper;
import com.dina.genasoft.db.mapper.TPermisosMapper;
import com.dina.genasoft.db.mapper.TTrace2Mapper;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.EnvioCorreo;
import com.dina.genasoft.utils.Telegram;
import com.dina.genasoft.utils.WhatsApp;
import com.dina.genasoft.utils.enums.EmpleadoEnum;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que se encarga de hacer de 'fachada' entre la capa de base de datos y la lógica de la aplicación.
 */
@Component
@Slf4j
@Data
public class CommonSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger                  log              = org.slf4j.LoggerFactory.getLogger(CommonSetup.class);
    /** Inyección de Spring para poder acceder a la capa de datos de empleados.*/
    @Autowired
    private EmpleadosSetup                                 empleadosSetup;
    /** Serial ID de la aplicación Spring. */
    private static final long                              serialVersionUID = -7194044566154533555L;
    /** Inyección por Spring del mapper TAccesosMapper.*/
    @Autowired
    private TAccesoMapper                                  tAccesoMapper;
    /** Inyección por Spring del mapper TAccesosMapper.*/
    @Autowired
    private TAccesoHisMapper                               tAccesoHisMapper;
    /** Inyección por Spring del mapper TPermisosMapper.*/
    @Autowired
    private TPermisosMapper                                tPermisosMapper;
    /** Inyección por Spring del mapper TErroresMapper.*/
    @Autowired
    private TErroresMapper                                 tErroresMapper;
    /** Inyección por Spring del mapper Telegram.*/
    @Autowired
    private Telegram                                       telegram;
    /** Inyección por Spring del mapper WhatsApp.*/
    @Autowired
    private WhatsApp                                       whatsApp;
    /** Contendrá el ID del usuario del administrador para recibir notificaciones.*/
    @Value("${user.notificacions}")
    private String                                         userNotifications;
    /** Contendrá el ID del usuario del administrador para recibir notificaciones.*/
    @Value("${user.notificacions2}")
    private String                                         userNotifications2;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private EnvioCorreo                                    envioCorreo;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.int.interval}")
    private Integer                                        appIntInterval;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.int.max}")
    private Integer                                        appIntMax;
    /** Diccionario con el ID (compra.getAlbaran() + "_" + compra.getLote()) de la línea de compra y la linea de compra. */
    public Map<String, TCompras>                           mCompras;
    /** Diccionario con el ID (compra.getAlbaran() + "_" + compra.getLote()) de la línea de compra y la linea de compra. */
    //public Map<String, TComprasVista>      mComprasVista;
    /** Diccionario con el ID de la línea de compra y la linea de compra. */
    public Map<String, TComprasVista>                      mComprasId;
    /** Diccionario con el ID (venta.getPedido() + "_" + venta.getAlbaran() + "_" + venta.getLote()) de la línea de venta y la linea de venta. */
    //public Map<String, TVentas>            mVentas;
    /** Diccionario con el ID (venta.getPedido() + "_" + venta.getAlbaran() + "_" + venta.getLote()) de la línea de venta y la linea de venta. */
    //public Map<String, TVentasVista>       mVentasVista;
    /** Diccionario con el ID  de la línea de venta y la linea de venta. */
    public Map<String, TVentasVista>                       mVentasId;
    /** Diccionario con albarán_compra lineas compras. */
    // public Map<String, List<TCompras>>     mComprasLineas;
    /** Diccionario con lote calidad. */
    //public Map<String, List<String>>        mLotesCalidad;
    /** Diccionario con lote calidad. */
    public Map<String, List<TCompras>>                     mLotesCompras;
    /** Diccionario con lote calidad. */
    public Map<String, List<TComprasVista>>                mLotesComprasVista;
    /** Diccionario de Variedad - Calidad y TreeMap de las compras con kgs disponibles por fecha. */
    public Map<String, TreeMap<Date, List<TCompras>>>      mComprasDisponibles;
    /** Diccionario de Variedad - Calidad y TreeMap de las lineas de ventas con kgs disponibles por fecha. */
    public Map<String, TreeMap<Date, List<TLineasVentas>>> mLineasVentaDisponibles;
    /** Diccionario con albarán_compra kgs. */
    //public Map<String, Double>              mComprasKgs;
    /** Diccionario con albarán_compra proveedor. */
    //public Map<String, List<String>>        mComprasProveedor;
    /** Diccionario con albarán_compra lineas venta */
    //public Map<String, List<TVentas>>      mComprasVentas;
    /** Diccionario con albarán_compra lineas venta */
    //public Map<String, List<TVentas>>      mComprasVentasVista;
    /** Diccionario con albarán_venta lineas compra. */
    //public Map<String, List<TCompras>>     mVentasCompras;
    /** Diccionario con lote_venta lineas compra. */
    //public Map<String, List<TCompras>>     mLoteVentasCompras;
    /** Diccionario con lote_venta lineas compra. */
    public Map<String, List<TVentasVista>>                 mLoteCompraVentas;
    /** Diccionario con clave [num_albaran-producto-confeccion] nos da las diferentes líneas (lotes) dentro del albarán de venta. */
    public Map<String, List<TVentas>>                      mLoteVentaProd;
    /** Diccionario con las masas de compra. */
    public Map<String, TVentasVista>                       mMasasArticulosOrigen;
    /** Diccionario con las masas de compra. */
    public Map<String, TVentasVista>                       mMasasArticulosPais;
    /** Diccionario con las masas de compra. */
    public Map<String, TVentasVista>                       mMasasArticulosCalidad;
    /** Diccionario con las masas de compra es por articulo ggn. */
    public Map<String, TVentasVista>                       mMasasGgnCalidad;
    /** Diccionario con las masas por producto. */
    public Map<String, TVentasVista>                       mMasasProductos;
    /********************************************* DICCIONARIOS PARA COMPROBAR DATOS CORRECTOS *********************************************/
    /**Diccionario que nos guarda por cada lote de compra, cuantos Kgs hay disponibles*/
    public Map<String, Double>                             mLoteCompraKgs;
    /** DICCIONARIO CON CLAVE VARIEDAD Y VALOR DICCIONARIO CON CLAVE ID DE COMPRA VALOR COMPRA
        Esto es últil para acceder directamente a la compra a traves del ID para sumar/restar kg del disponible,
        Este diccionario lo podemos convertir en una lista con values() y ordenarla por fecha para corregir los errores. 
     */
    public Map<Integer, TCompras>                          mTodasCompras;
    public Map<String, Map<Integer, TCompras>>             mProductosComprasDisponibles;
    /** Diccionario que nos guarda por producto, cuantos productos de compra hay disponinles. */
    //public Map<String, List<Producto>>      mProductosComprasDisponibles;
    /** Diccionario con id venta (el campo id) y la venta que no tiene los datos correctos.*/
    public Map<Integer, TVentas>                           mIdVentasErroneas;
    /** Diccionario con el ID de la venta y las líneas que son erroneas. */
    public Map<Double, List<TLineasVentas>>                mIdLineasVentasErroneas;
    /** Diccionario con el ID de la venta y las líneas que componen la venta. */
    public Map<Double, List<TLineasVentas>>                mIdLineasVentas;
    public List<String>                                    lNombresImportacionesCompras;
    public List<String>                                    lNombresImportacionesVentas;
    public List<String>                                    lNumerosAlbaranCompras;
    public List<String>                                    lNumerosAlbaranVentas;
    public List<String>                                    lNumerosAlbaranVentasCompras;
    public List<String>                                    lNumerosAlbaranComprasFin;
    public List<String>                                    lNumerosAlbaranVentasFin;
    public List<String>                                    lNumerosAlbaranVentasComprasFin;
    public List<String>                                    lFamiliasCompras;
    public List<String>                                    lFamiliasComprasFin;
    public List<String>                                    lFamiliasVentas;
    public List<String>                                    lFamiliasVentasFin;
    public List<String>                                    lVariedades;
    public List<String>                                    lVariedadesProductos;
    public List<String>                                    lArticulosCompras;
    public List<String>                                    lArticulosComprasFin;
    public List<String>                                    lArticulosVentas;
    public List<String>                                    lArticulosVentasFin;
    public List<String>                                    lClientesVentas;
    public List<String>                                    lClientesVentasFin;
    public List<String>                                    lPaisesCompras;
    public List<String>                                    lPaisesComprasFin;
    public List<String>                                    lPartidasCompras;
    public List<String>                                    lPartidasComprasFin;
    public List<String>                                    lPedidosVentas;
    public List<String>                                    lPedidosVentasFin;
    public List<String>                                    lProveedoresCompras;
    public List<String>                                    lProveedoresComprasFin;
    public List<String>                                    lPaisesVentas;
    public List<String>                                    lPaisesVentasFin;
    public List<String>                                    lProveedoresVentasCompras;
    public List<String>                                    lProveedoresVentasComprasFin;
    public List<String>                                    lLotesCompras;
    public List<String>                                    lLotesComprasFin;
    public List<String>                                    lLotesEntradaVentas;
    public List<String>                                    lLotesEntradaVentasFin;
    public List<String>                                    lLotesVentas;
    public List<String>                                    lLotesVentasFin;
    public List<String>                                    lCodsPallets;
    public List<String>                                    lLotesPallets;
    /** Para establecer conexión con la BD externa.*/
    @Autowired
    private DataBaseConnection                             dbConection;
    /** Inyección por Spring del mapper TTrace2Mapper.*/
    @Autowired
    private TTrace2Mapper                                  tTrace2Mapper;
    private Integer                                        ejercicio;
    /** Diccionario de Variedad - Calidad y TreeMap de las compras con kgs disponibles por fecha. */
    private Map<String, TComprasVista>                     mComprasEjercicio;
    /** Diccionario de Variedad - Calidad y TreeMap de las compras con kgs disponibles por fecha. */
    private Map<String, TVentasVista>                      mVentasEjercicio;
    /** Diccionario de Variedad - Calidad y TreeMap de las compras con kgs disponibles por fecha. */
    private TreeMap<Date, List<TComprasVista>>             tComprasEjercicio;
    /** Diccionario de Variedad - Calidad y TreeMap de las compras con kgs disponibles por fecha. */
    private TreeMap<Date, List<TVentasVista>>              tVentasEjercicio;

    /**
     * @return the tComprasEjercicio
     */
    public TreeMap<Date, List<TComprasVista>> gettComprasEjercicio() {
        return tComprasEjercicio;
    }

    /**
     * @param tComprasEjercicio the tComprasEjercicio to set
     */
    public void settComprasEjercicio(TreeMap<Date, List<TComprasVista>> tComprasEjercicio) {
        this.tComprasEjercicio = tComprasEjercicio;
    }

    /**
     * @return the tVentasEjercicio
     */
    public TreeMap<Date, List<TVentasVista>> gettVentasEjercicio() {
        return tVentasEjercicio;
    }

    /**
     * @param tVentasEjercicio the tVentasEjercicio to set
     */
    public void settVentasEjercicio(TreeMap<Date, List<TVentasVista>> tVentasEjercicio) {
        this.tVentasEjercicio = tVentasEjercicio;
    }

    /**
     * @return the mComprasEjercicio
     */
    public Map<String, TComprasVista> getmComprasEjercicio() {
        return mComprasEjercicio;
    }

    /**
     * @param mComprasEjercicio the mComprasEjercicio to set
     */
    public void setmComprasEjercicio(Map<String, TComprasVista> mComprasEjercicio) {
        this.mComprasEjercicio = mComprasEjercicio;
    }

    /**
     * @return the mVentasEjercicio
     */
    public Map<String, TVentasVista> getmVentasEjercicio() {
        return mVentasEjercicio;
    }

    /**
     * @param mVentasEjercicio the mVentasEjercicio to set
     */
    public void setmVentasEjercicio(Map<String, TVentasVista> mVentasEjercicio) {
        this.mVentasEjercicio = mVentasEjercicio;
    }

    /**
     * @return the ejercicio
     */
    public Integer getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the mComprasId
     */
    public Map<String, TComprasVista> getmComprasId() {
        return mComprasId;
    }

    /**
     * @param mComprasId the mComprasId to set
     */
    public void setmComprasId(Map<String, TComprasVista> mComprasId) {
        this.mComprasId = mComprasId;
    }

    /**
     * @return the mVentasId
     */
    public Map<String, TVentasVista> getmVentasId() {
        return mVentasId;
    }

    /**
     * @param mVentasId the mVentasId to set
     */
    public void setmVentasId(Map<String, TVentasVista> mVentasId) {
        this.mVentasId = mVentasId;
    }

    /**
     * @return the mCompras
     */
    public Map<String, TCompras> getmCompras() {
        return mCompras;
    }

    /**
     * @return the mComprasCalidad
     */
    //  public Map<String, List<String>> getmLotesCalidad() {
    //      return mLotesCalidad;
    ///  }

    /**
     * @param mComprasCalidad the mComprasCalidad to set
     */
    // public void setmLotesCalidad(Map<String, List<String>> mLotesCalidad) {
    ///     this.mLotesCalidad = mLotesCalidad;
    // }

    /**
     * @return the mComprasKgs
     */
    //public Map<String, Double> getmComprasKgs() {
    //    return mComprasKgs;
    // }

    /**
     * @return the mComprasProveedor
     */
    //public Map<String, List<String>> getmComprasProveedor() {
    //    return mComprasProveedor;
    //}

    /**
     * @return the lNumerosAlbaranCompras
     */
    public List<String> getlNumerosAlbaranCompras() {
        return lNumerosAlbaranCompras;
    }

    /**
     * @param lNumerosAlbaranCompras the lNumerosAlbaranCompras to set
     */
    public void setlNumerosAlbaranCompras(List<String> lNumerosAlbaranCompras) {
        this.lNumerosAlbaranCompras = lNumerosAlbaranCompras;
    }

    /**
     * @return the lNumerosAlbaranVentas
     */
    public List<String> getlNumerosAlbaranVentas() {
        return lNumerosAlbaranVentas;
    }

    /**
     * @param lNumerosAlbaranVentas the lNumerosAlbaranVentas to set
     */
    public void setlNumerosAlbaranVentas(List<String> lNumerosAlbaranVentas) {
        this.lNumerosAlbaranVentas = lNumerosAlbaranVentas;
    }

    /**
     * @return the lNumerosAlbaranCompras
     */
    public List<String> getlNumerosAlbaranComprasFin() {
        return lNumerosAlbaranComprasFin;
    }

    /**
     * @param lNumerosAlbaranCompras the lNumerosAlbaranCompras to set
     */
    public void setlNumerosAlbaranComprasFin(List<String> lNumerosAlbaranComprasFin) {
        this.lNumerosAlbaranComprasFin = lNumerosAlbaranComprasFin;
    }

    /**
     * @return the lNumerosAlbaranVentas
     */
    public List<String> getlNumerosAlbaranVentasFin() {
        return lNumerosAlbaranVentasFin;
    }

    /**
     * @param lNumerosAlbaranVentas the lNumerosAlbaranVentas to set
     */
    public void setlNumerosAlbaranVentasFin(List<String> lNumerosAlbaranVentasFin) {
        this.lNumerosAlbaranVentasFin = lNumerosAlbaranVentasFin;
    }

    /**
     * @return the lFamiliasCompras
     */
    public List<String> getlFamiliasCompras() {
        return lFamiliasCompras;
    }

    /**
     * @param lFamiliasCompras the lFamiliasCompras to set
     */
    public void setlFamiliasCompras(List<String> lFamiliasCompras) {
        this.lFamiliasCompras = lFamiliasCompras;
    }

    /**
     * @return the lFamiliasComprasFin
     */
    public List<String> getlFamiliasComprasFin() {
        return lFamiliasComprasFin;
    }

    /**
     * @param lFamiliasComprasFin the lFamiliasComprasFin to set
     */
    public void setlFamiliasComprasFin(List<String> lFamiliasComprasFin) {
        this.lFamiliasComprasFin = lFamiliasComprasFin;
    }

    /**
     * @return the lFamiliasVentas
     */
    public List<String> getlFamiliasVentas() {
        return lFamiliasVentas;
    }

    /**
     * @param lFamiliasVentas the lFamiliasVentas to set
     */
    public void setlFamiliasVentas(List<String> lFamiliasVentas) {
        this.lFamiliasVentas = lFamiliasVentas;
    }

    /**
     * @return the lFamiliasVentasFin
     */
    public List<String> getlFamiliasVentasFin() {
        return lFamiliasVentasFin;
    }

    /**
     * @param lFamiliasVentasFin the lFamiliasVentasFin to set
     */
    public void setlFamiliasVentasFin(List<String> lFamiliasVentasFin) {
        this.lFamiliasVentasFin = lFamiliasVentasFin;
    }

    /**
     * @return the lArticulosCompras
     */
    public List<String> getlArticulosCompras() {
        return lArticulosCompras;
    }

    /**
     * @param lArticulosCompras the lArticulosCompras to set
     */
    public void setlArticulosCompras(List<String> lArticulosCompras) {
        this.lArticulosCompras = lArticulosCompras;
    }

    /**
     * @return the lArticulosComprasFin
     */
    public List<String> getlArticulosComprasFin() {
        return lArticulosComprasFin;
    }

    /**
     * @param lArticulosComprasFin the lArticulosComprasFin to set
     */
    public void setlArticulosComprasFin(List<String> lArticulosComprasFin) {
        this.lArticulosComprasFin = lArticulosComprasFin;
    }

    /**
     * @return the lArticulosVentas
     */
    public List<String> getlArticulosVentas() {
        return lArticulosVentas;
    }

    /**
     * @param lArticulosVentas the lArticulosVentas to set
     */
    public void setlArticulosVentas(List<String> lArticulosVentas) {
        this.lArticulosVentas = lArticulosVentas;
    }

    /**
     * @return the lArticulosVentasFin
     */
    public List<String> getlArticulosVentasFin() {
        return lArticulosVentasFin;
    }

    /**
     * @param lArticulosVentasFin the lArticulosVentasFin to set
     */
    public void setlArticulosVentasFin(List<String> lArticulosVentasFin) {
        this.lArticulosVentasFin = lArticulosVentasFin;
    }

    /**
     * @return the lClientesVentas
     */
    public List<String> getlClientesVentas() {
        return lClientesVentas;
    }

    /**
     * @param lClientesVentas the lClientesVentas to set
     */
    public void setlClientesVentas(List<String> lClientesVentas) {
        this.lClientesVentas = lClientesVentas;
    }

    /**
     * @return the lClientesVentasFin
     */
    public List<String> getlClientesVentasFin() {
        return lClientesVentasFin;
    }

    /**
     * @param lClientesVentasFin the lClientesVentasFin to set
     */
    public void setlClientesVentasFin(List<String> lClientesVentasFin) {
        this.lClientesVentasFin = lClientesVentasFin;
    }

    /**
     * @return the lProveedoresCompras
     */
    public List<String> getlProveedoresCompras() {
        return lProveedoresCompras;
    }

    /**
     * @param lProveedoresCompras the lProveedoresCompras to set
     */
    public void setlProveedoresCompras(List<String> lProveedoresCompras) {
        this.lProveedoresCompras = lProveedoresCompras;
    }

    /**
     * @return the lProveedoresComprasFin
     */
    public List<String> getlProveedoresComprasFin() {
        return lProveedoresComprasFin;
    }

    /**
     * @param lProveedoresComprasFin the lProveedoresComprasFin to set
     */
    public void setlProveedoresComprasFin(List<String> lProveedoresComprasFin) {
        this.lProveedoresComprasFin = lProveedoresComprasFin;
    }

    /**
     * @return the lLotesCompras
     */
    public List<String> getlLotesCompras() {
        return lLotesCompras;
    }

    /**
     * @param lLotesCompras the lLotesCompras to set
     */
    public void setlLotesCompras(List<String> lLotesCompras) {
        this.lLotesCompras = lLotesCompras;
    }

    /**
     * @return the lLotesComprasFin
     */
    public List<String> getlLotesComprasFin() {
        return lLotesComprasFin;
    }

    /**
     * @param lLotesComprasFin the lLotesComprasFin to set
     */
    public void setlLotesComprasFin(List<String> lLotesComprasFin) {
        this.lLotesComprasFin = lLotesComprasFin;
    }

    /**
     * @return the lLotesEntradaVentas
     */
    public List<String> getlLotesEntradaVentas() {
        return lLotesEntradaVentas;
    }

    /**
     * @param lLotesEntradaVentas the lLotesEntradaVentas to set
     */
    public void setlLotesEntradaVentas(List<String> lLotesEntradaVentas) {
        this.lLotesEntradaVentas = lLotesEntradaVentas;
    }

    /**
     * @return the lLotesEntradaVentasFin
     */
    public List<String> getlLotesEntradaVentasFin() {
        return lLotesEntradaVentasFin;
    }

    /**
     * @param lLotesEntradaVentasFin the lLotesEntradaVentasFin to set
     */
    public void setlLotesEntradaVentasFin(List<String> lLotesEntradaVentasFin) {
        this.lLotesEntradaVentasFin = lLotesEntradaVentasFin;
    }

    /**
     * @return the lLotesVentas
     */
    public List<String> getlLotesVentas() {
        return lLotesVentas;
    }

    /**
     * @param lLotesVentas the lLotesVentas to set
     */
    public void setlLotesVentas(List<String> lLotesVentas) {
        this.lLotesVentas = lLotesVentas;
    }

    /**
     * @return the lLotesVentasFin
     */
    public List<String> getlLotesVentasFin() {
        return lLotesVentasFin;
    }

    /**
     * @param lLotesVentasFin the lLotesVentasFin to set
     */
    public void setlLotesVentasFin(List<String> lLotesVentasFin) {
        this.lLotesVentasFin = lLotesVentasFin;
    }

    /**
     * @return the lCodsPallets
     */
    public List<String> getlCodsPallets() {
        return lCodsPallets;
    }

    /**
     * @param lCodsPallets the lCodsPallets to set
     */
    public void setlCodsPallets(List<String> lCodsPallets) {
        this.lCodsPallets = lCodsPallets;
    }

    /**
     * @return the lLotesPallets
     */
    public List<String> getlLotesPallets() {
        return lLotesPallets;
    }

    /**
     * @param lLotesPallets the lLotesPallets to set
     */
    public void setlLotesPallets(List<String> lLotesPallets) {
        this.lLotesPallets = lLotesPallets;
    }

    /**
     * @return the lPaisesCompras
     */
    public List<String> getlPaisesCompras() {
        return lPaisesCompras;
    }

    /**
     * @param lPaisesCompras the lPaisesCompras to set
     */
    public void setlPaisesCompras(List<String> lPaisesCompras) {
        this.lPaisesCompras = lPaisesCompras;
    }

    /**
     * @return the lPaisesComprasFin
     */
    public List<String> getlPaisesComprasFin() {
        return lPaisesComprasFin;
    }

    /**
     * @param lPaisesComprasFin the lPaisesComprasFin to set
     */
    public void setlPaisesComprasFin(List<String> lPaisesComprasFin) {
        this.lPaisesComprasFin = lPaisesComprasFin;
    }

    /**
     * 
     * @return the lPaisesVentas
     */
    public List<String> getlPaisesVentas() {
        return lPaisesVentas;
    }

    /**
     * @param lPaisesVentas the lPaisesVentas to set
     */
    public void setlPaisesVentas(List<String> lPaisesVentas) {
        this.lPaisesVentas = lPaisesVentas;
    }

    /**
     * @return the lPaisesVentasFin
     */
    public List<String> getlPaisesVentasFin() {
        return lPaisesVentasFin;
    }

    /**
     * @param lPaisesVentasFin the lPaisesVentasFin to set
     */
    public void setlPaisesVentasFin(List<String> lPaisesVentasFin) {
        this.lPaisesVentasFin = lPaisesVentasFin;
    }

    /**
     * @return the lNumerosAlbaranVentasCompras
     */
    public List<String> getlNumerosAlbaranVentasCompras() {
        return lNumerosAlbaranVentasCompras;
    }

    /**
     * @param lNumerosAlbaranVentasCompras the lNumerosAlbaranVentasCompras to set
     */
    public void setlNumerosAlbaranVentasCompras(List<String> lNumerosAlbaranVentasCompras) {
        this.lNumerosAlbaranVentasCompras = lNumerosAlbaranVentasCompras;
    }

    /**
     * @return the lNumerosAlbaranVentasComprasFin
     */
    public List<String> getlNumerosAlbaranVentasComprasFin() {
        return lNumerosAlbaranVentasComprasFin;
    }

    /**
     * @param lNumerosAlbaranVentasComprasFin the lNumerosAlbaranVentasComprasFin to set
     */
    public void setlNumerosAlbaranVentasComprasFin(List<String> lNumerosAlbaranVentasComprasFin) {
        this.lNumerosAlbaranVentasComprasFin = lNumerosAlbaranVentasComprasFin;
    }

    /**
     * @return the lProveedoresVentasCompras
     */
    public List<String> getlProveedoresVentasCompras() {
        return lProveedoresVentasCompras;
    }

    /**
     * @param lProveedoresVentasCompras the lProveedoresVentasCompras to set
     */
    public void setlProveedoresVentasCompras(List<String> lProveedoresVentasCompras) {
        this.lProveedoresVentasCompras = lProveedoresVentasCompras;
    }

    /**
     * @return the lProveedoresVentasComprasFin
     */
    public List<String> getlProveedoresVentasComprasFin() {
        return lProveedoresVentasComprasFin;
    }

    /**
     * @param lProveedoresVentasComprasFin the lProveedoresVentasComprasFin to set
     */
    public void setlProveedoresVentasComprasFin(List<String> lProveedoresVentasComprasFin) {
        this.lProveedoresVentasComprasFin = lProveedoresVentasComprasFin;
    }

    /**
     * @return the lPartidasCompras
     */
    public List<String> getlPartidasCompras() {
        return lPartidasCompras;
    }

    /**
     * @param lPartidasCompras the lPartidasCompras to set
     */
    public void setlPartidasCompras(List<String> lPartidasCompras) {
        this.lPartidasCompras = lPartidasCompras;
    }

    /**
     * @return the lPartidasComprasFin
     */
    public List<String> getlPartidasComprasFin() {
        return lPartidasComprasFin;
    }

    /**
     * @param lPartidasComprasFin the lPartidasComprasFin to set
     */
    public void setlPartidasComprasFin(List<String> lPartidasComprasFin) {
        this.lPartidasComprasFin = lPartidasComprasFin;
    }

    /**
     * @return the lPedidosVentas
     */
    public List<String> getlPedidosVentas() {
        return lPedidosVentas;
    }

    /**
     * @param lPedidosVentas the lPedidosVentas to set
     */
    public void setlPedidosVentas(List<String> lPedidosVentas) {
        this.lPedidosVentas = lPedidosVentas;
    }

    /**
     * @return the lPedidosVentasFin
     */
    public List<String> getlPedidosVentasFin() {
        return lPedidosVentasFin;
    }

    /**
     * @param lPedidosVentasFin the lPedidosVentasFin to set
     */
    public void setlPedidosVentasFin(List<String> lPedidosVentasFin) {
        this.lPedidosVentasFin = lPedidosVentasFin;
    }

    /**
     * @return the mLoteCompraVentas
     */
    public Map<String, List<TVentasVista>> getmLoteCompraVentas() {
        return mLoteCompraVentas;
    }

    /**
     * @param mLoteCompraVentas the mLoteCompraVentas to set
     */
    public void setmLoteCompraVentas(Map<String, List<TVentasVista>> mLoteCompraVentas) {
        this.mLoteCompraVentas = mLoteCompraVentas;
    }

    /**
     * @return the mLoteVentaProd
     */
    public Map<String, List<TVentas>> getmLoteVentaProd() {
        return mLoteVentaProd;
    }

    /**
     * @param mLoteVentaProd the mLoteVentaProd to set
     */
    public void setmLoteVentaProd(Map<String, List<TVentas>> mLoteVentaProd) {
        this.mLoteVentaProd = mLoteVentaProd;
    }

    /**
     * @return the lNombresImportacionesCompras
     */
    public List<String> getlNombresImportacionesCompras() {
        return lNombresImportacionesCompras;
    }

    /**
     * @param lNombresImportacionesCompras the lNombresImportacionesCompras to set
     */
    public void setlNombresImportacionesCompras(List<String> lNombresImportacionesCompras) {
        this.lNombresImportacionesCompras = lNombresImportacionesCompras;
    }

    /**
     * @return the lNombresImportacionesVentas
     */
    public List<String> getlNombresImportacionesVentas() {
        return lNombresImportacionesVentas;
    }

    /**
     * @param lNombresImportacionesVentas the lNombresImportacionesVentas to set
     */
    public void setlNombresImportacionesVentas(List<String> lNombresImportacionesVentas) {
        this.lNombresImportacionesVentas = lNombresImportacionesVentas;
    }

    /**
     * @return the lVariedades
     */
    public List<String> getlVariedades() {
        return lVariedades;
    }

    /**
     * @param lVariedades the lVariedades to set
     */
    public void setlVariedades(List<String> lVariedades) {
        this.lVariedades = lVariedades;
    }

    /**
     * @return the lVariedadesProductos
     */
    public List<String> getlVariedadesProductos() {
        return lVariedadesProductos;
    }

    /**
     * @param lVariedadesProductos the lVariedadesProductos to set
     */
    public void setlVariedadesProductos(List<String> lVariedadesProductos) {
        this.lVariedadesProductos = lVariedadesProductos;
    }

    /**
     * @return the mIdVentasErroneas
     */
    public Map<Integer, TVentas> getmIdVentasErroneas() {
        return mIdVentasErroneas;
    }

    /**
     * @param mIdVentasErroneas the mIdVentasErroneas to set
     */
    public void setmIdVentasErroneas(Map<Integer, TVentas> mIdVentasErroneas) {
        this.mIdVentasErroneas = mIdVentasErroneas;
    }

    /**
     * @return the mIdLineasVentasErroneas
     */
    public Map<Double, List<TLineasVentas>> getmIdLineasVentasErroneas() {
        return mIdLineasVentasErroneas;
    }

    /**
     * @param mIdLineasVentasErroneas the mIdLineasVentasErroneas to set
     */
    public void setmIdLineasVentasErroneas(Map<Double, List<TLineasVentas>> mIdLineasVentasErroneas) {
        this.mIdLineasVentasErroneas = mIdLineasVentasErroneas;
    }

    /**
     * @return the mIdLineasVentas
     */
    public Map<Double, List<TLineasVentas>> getmIdLineasVentas() {
        return mIdLineasVentas;
    }

    /**
     * @param mIdLineasVentas the mIdLineasVentas to set
     */
    public void setmIdLineasVentas(Map<Double, List<TLineasVentas>> mIdLineasVentas) {
        this.mIdLineasVentas = mIdLineasVentas;
    }

    /**
     * Nos comprueba si la sesión del empleado actual es correcta.
     * @param username El empleado.
     * @return true si es válida, en caso contrario false.
     */
    public boolean conexionValida(int idEmpleado, Timestamp fecha, int estadoAplicacion) {
        boolean result = false;

        if (estadoAplicacion == -1) {
            return false;
        } else {

            // Comprobamos el acceso.
            TAcceso acceso = tAccesoMapper.obtenerAccesoEmpleado(idEmpleado);

            String f1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fecha);

            if (acceso != null) {
                String f2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(acceso.getFecha());
                // Comprobamos si la hora es la misma que la del registro de BD.
                result = f1.equals(f2);
            }

            if (result) {
                result = empleadosSetup.obtenerEmpleadoPorId(idEmpleado).getEstado().equals(EmpleadoEnum.ACTIVO.getValue());
            }
        }

        // Retornamos el resultado.
        return result;
    }

    /**
     * Método que nos elimina el acceso existente del empleado.
     * @param idEmpleado El ID del empleado con acceso a eliminar.
     */
    public void eliminaAcceso(Integer idEmpleado) {
        tAccesoMapper.eliminarAccesoEmpleado(idEmpleado);
    }

    /**
     * Método que se encarga de modificar el acceso de empleado.
     * @param acceso El acceso a modificar
     * @return Los registros modificados.
     */
    public int modificarAccesoEmpleado(TAcceso acceso) {
        return tAccesoMapper.updateByPrimaryKey(acceso);
    }

    /**
     * Método que se encarga de crear el acceso de empleado.
     * @param acceso El acceso a crear
     * @return Los registros creados.
     */
    public int crearAccesoEmpleado(TAcceso acceso) {
        return tAccesoMapper.insert(acceso);
    }

    /**
     * Método que se encarga de crear el acceso de empleado.
     * @param acceso El acceso a crear
     * @return Los registros creados.
     */
    public int crearAccesoEmpleadoHis(TAccesoHis acceso) {
        return tAccesoHisMapper.insert(acceso);
    }

    /**
     * Método que nos retorna el acceso del empleado
     * @param idEmpleado El ID del empleado
     * @return El acceso asignado.
     */
    public TAcceso obtenerAccesoEmpleado(Integer idEmpleado) {
        return tAccesoMapper.obtenerAccesoEmpleado(idEmpleado);
    }

    /**
     * Método que nos retorna los permisos a partir del rol.
     * @param idRol El rol
     * @return Los permisos encontrados
     */
    public TPermisos obtenerPermisosPorRol(int idRol) {
        TPermisos permisos = tPermisosMapper.selectByPrimaryKey(idRol);
        // Retnornamos el permiso encontrado.
        return permisos;
    }

    /**
     * Método que nos retorna los permisos a partir del rol.
     * @param idRol El rol
     * @return Los permisos encontrados
     */
    public void crearPermiso(TPermisos record) {
        tPermisosMapper.insert(record);
    }

    /**
     * Método que nos retorna la descripción del código que nos pasan por parámetro.
     * @param codigo El código a buscar.
     * @return La descripción.
     */
    public String obtenerDescripcionCodigo(String codigo) {
        String result = "Error indeterminado.";
        TErrores err = tErroresMapper.selectByPrimaryKey(codigo);
        if (err != null) {
            result = err.getDescripcion();
        }
        // Retornamos la descripción.
        return result;
    }

    /**
     * Métdo para enviar notificaciones vía telegram.
     * @param texto El texto del mensaje.
     * @throws BrosException Si se produce algún error.
     */
    public void enviaNotificacionTelegramMasivo(String texto) throws GenasoftException {
        // Buscamos el empleado de las notificaciones
        TEmpleados empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications));
        telegram.enviarMensaje("+34" + empl.getTelefono(), texto);
        //empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications2));
        //telegram.enviarMensaje("+34" + empl.getTelefono(), texto);
    }

    /**
     * Métdo para enviar notificaciones vía telegram.
     * @param texto El texto del mensaje.
     * @throws BrosException Si se produce algún error.
     */
    public void enviaNotificacionWhatsAppMasivo(String texto) throws GenasoftException {
        // Buscamos el empleado de las notificaciones
        TEmpleados empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications));
        whatsApp.enviarMensaje("+34" + empl.getTelefono(), texto);
        //empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications2));
        //whatsApp.enviarMensaje("+34" + empl.getTelefono(), texto);
    }

    /**
     * Método que nos envía un correo electrónico a la dirección de correo especificado con el adjunto especificado.
     * @param email La dirección (o direcciones separadas por ;) para enviar la notificación.
     * @param titulo El título del correo.
     * @param cuerpo El cuerpo del mensaje.
     * @param adjunto El fichero adjunto (puede ser nulo.)
     * @return El resultado de la operación
     * @throws GenasoftException 
     * @throws NumberFormatException 
     */
    public String enviaNotificacionCorreo(String titulo, String cuerpo, String adjunto, Integer user) throws NumberFormatException, GenasoftException {
        String result = "Notificación enviada correctamente.";
        TEmpleados empl = null;
        try {
            empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(user));
            empl.setEmail("daniel86cr@gmail.com");
            //  if (empl != null && empl.getEmail() != null && !empl.getEmail().isEmpty()) {
            //envioCorreo.enviarCorreo(empl.getEmail(), titulo, cuerpo, adjunto);
            // }
        } catch (MailException e) {
            result = "Error al enviar la notificación, compruebe que la dirección de correo es correcta. Si el problema persiste, contacte con el administrador";
        } catch (Exception e) {

        }

        return result;
    }

    /**
     * Método que nos envía un correo electrónico a la dirección de correo especificado con el adjunto especificado.
     * @param email La dirección (o direcciones separadas por ;) para enviar la notificación.
     * @param titulo El título del correo.
     * @param cuerpo El cuerpo del mensaje.
     * @param adjunto El fichero adjunto (puede ser nulo.)
     * @return El resultado de la operación
     * @throws GenasoftException 
     * @throws NumberFormatException 
     */
    public String enviaNotificacionCorreoMasiva(String titulo, String cuerpo, String adjunto) throws NumberFormatException, GenasoftException {
        String result = "Notificación enviada correctamente.";
        TEmpleados empl = null;
        try {
            empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications));
            if (empl != null && empl.getEmail() != null && !empl.getEmail().isEmpty()) {
                envioCorreo.enviarCorreo(empl.getEmail(), titulo, cuerpo, adjunto);
            }
            empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(userNotifications2));
            if (empl != null && empl.getEmail() != null && !empl.getEmail().isEmpty()) {
                envioCorreo.enviarCorreo(empl.getEmail(), titulo, cuerpo, adjunto);
            }
        } catch (MailException | InterruptedException e) {
            result = "Error al enviar la notificación, compruebe que la dirección de correo es correcta. Si el problema persiste, contacte con el administrador";
        } catch (Exception e) {

        }

        return result;
    }

    /**
     * Método que nos realiza la búsqueda de los pedidos existentes en el sistema externo.
     * @return Lista de pedidos externos {@link Ge07NlvPedidos}
     * @throws ConTransException Si se produce alguna excepción al realizarse la consulta.
     */

    public TTrace obtenerInformacionLicencia(String mac, String nombreCliente) throws GenasoftException {
        log.debug("Debug", " -> Obtener información licencia()");

        TTrace result = null;
        try {
            StringBuffer queryString = new StringBuffer("");
            PreparedStatement selectQuery = null;
            dbConection.getConnection();

            queryString.append("SELECT mac, cliente, last_check, validez, estado FROM t_trace where mac = ? and cliente = ?");

            selectQuery = dbConection.getConection().prepareStatement(queryString.toString());

            selectQuery.setString(1, mac.toUpperCase());
            selectQuery.setString(2, nombreCliente.toUpperCase());

            log.error("La query: " + selectQuery.toString());

            ResultSet rSet = selectQuery.executeQuery();

            // Recorremos los resultados encontrados y los añadimos a la lista final
            if (rSet.next()) {
                result = new TTrace();
                result.setMac(rSet.getString(1));
                result.setCliente(rSet.getString(2));
                result.setLastCheck(rSet.getDate(3));
                result.setValidez(rSet.getDate(4));
                result.setEstado(rSet.getInt(5));
            }

            rSet.close();
            selectQuery.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GenasoftException("SQLException  obtenerPedidosExternos(): " + e.getLocalizedMessage());
        } finally {
            if (dbConection.getConection() != null) {
                dbConection.releaseConnection();
            }
        }

        log.debug("Debug", " <- obtenerPedidosExternos()");

        // La información de la licencia.
        return result;
    }

    /**
     * Método que nos retorna el nº de instentos fallidos para comprobar la licencia.
     * @return El número de intentos fallidos.
     */
    public String guardarIntentos(TTrace2 record) {
        tTrace2Mapper.eliminarRegistros();
        tTrace2Mapper.insert(record);
        return Constants.OPERACION_OK;
    }

    /**
     * Método que nos realiza la búsqueda de los pedidos existentes en el sistema externo.
     * @return Lista de pedidos externos {@link Ge07NlvPedidos}
     * @throws ConTransException Si se produce alguna excepción al realizarse la consulta.
     */

    public void guardarInformacionLicencia(TTrace traza) throws GenasoftException {
        log.debug("Debug", " -> Guardar información licencia()");

        try {
            StringBuffer queryString = new StringBuffer("");
            PreparedStatement selectQuery = null;
            dbConection.getConnection();
            Timestamp t = new Timestamp(traza.getLastCheck().getTime());

            queryString.append("UPDATE t_trace set last_check = ? where mac = ? and cliente = ?");

            selectQuery = dbConection.getConection().prepareStatement(queryString.toString());

            selectQuery.setTimestamp(1, t);
            selectQuery.setString(2, traza.getMac());
            selectQuery.setString(3, traza.getCliente());

            selectQuery.executeUpdate();

            selectQuery.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GenasoftException("SQLException  guardarInformacionLicencia(): " + e.getLocalizedMessage());
        } finally {
            if (dbConection.getConection() != null) {
                dbConection.releaseConnection();
            }
        }

        log.debug("Debug", " <- Guardar información licencia()");
    }

    /**
     * Método que nos retorna el nº de instentos fallidos para comprobar la licencia.
     * @return El número de intentos fallidos.
     */
    public TTrace2 obtenerIntentosFallidosLicencia() {
        TTrace2 result = null;
        result = tTrace2Mapper.obtenerIntentos();
        if (result == null) {
            result = new TTrace2();
            result.setIntentos(0);
        }
        return result;
    }

}
