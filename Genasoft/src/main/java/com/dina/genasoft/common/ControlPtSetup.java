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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TControlProductoTerminado;
import com.dina.genasoft.db.entity.TControlProductoTerminadoFotos;
import com.dina.genasoft.db.entity.TControlProductoTerminadoFotosHis;
import com.dina.genasoft.db.entity.TControlProductoTerminadoHis;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesBrix;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesBrixHis;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesBrixVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCaja;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCajaHis;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCajaVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCalibre;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCalibreHis;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCalibreVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesConfeccion;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesConfeccionHis;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesConfeccionVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesPenetromia;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesPenetromiaHis;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesPenetromiaVista;
import com.dina.genasoft.db.entity.TControlProductoTerminadoVista;
import com.dina.genasoft.db.entity.TDiametrosProducto;
import com.dina.genasoft.db.entity.TEmpleados;
import com.dina.genasoft.db.entity.TLineaControlProductoTerminado;
import com.dina.genasoft.db.entity.TLineaControlProductoTerminadoHis;
import com.dina.genasoft.db.entity.TLineaControlProductoTerminadoVista;
import com.dina.genasoft.db.entity.TProductoCalibre;
import com.dina.genasoft.db.entity.TProductos;
import com.dina.genasoft.db.entity.TRegistrosCambiosControlProductoTerminado;
import com.dina.genasoft.db.entity.TVariedad;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoFotosHisMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoFotosMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoHisMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoPesajesBrixHisMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoPesajesBrixMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoPesajesCajaHisMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoPesajesCajaMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoPesajesCalibreHisMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoPesajesCalibreMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoPesajesConfeccionHisMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoPesajesConfeccionMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoPesajesPenetromiaHisMapper;
import com.dina.genasoft.db.mapper.TControlProductoTerminadoPesajesPenetromiaMapper;
import com.dina.genasoft.db.mapper.TLineaControlProductoTerminadoHisMapper;
import com.dina.genasoft.db.mapper.TLineaControlProductoTerminadoMapper;
import com.dina.genasoft.db.mapper.TRegistrosCambiosControlProductoTerminadoMapper;
import com.dina.genasoft.utils.Utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de control de producto terminado y la BD.
 */
@Component
@Slf4j
@Data
public class ControlPtSetup implements Serializable {
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger                       log              = org.slf4j.LoggerFactory.getLogger(ControlPtSetup.class);
    /** Inyección de Spring para poder acceder a la capa de datos de empleados.*/
    @Autowired
    private EmpleadosSetup                                      empleadosSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de clientes.*/
    @Autowired
    private ClientesSetup                                       clientesSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de productos.*/
    @Autowired
    private ProductosSetup                                      productosSetup;
    /** Inyección por Spring del mapper TControlProductoTerminadoMapper.*/
    @Autowired
    private TControlProductoTerminadoMapper                     tControlProductoTerminadoMapper;
    /** Inyección por Spring del mapper TLineasControlProductoTerminadoMapper.*/
    @Autowired
    private TLineaControlProductoTerminadoMapper                tLineaControlProductoTerminadoMapper;
    /** Inyección por Spring del mapper TLineasControlProductoTerminadoMapper.*/
    @Autowired
    private TLineaControlProductoTerminadoHisMapper             tLineaControlProductoTerminadoHisMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoHisMapper.*/
    @Autowired
    private TControlProductoTerminadoHisMapper                  tControlProductoTerminadoHisMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoFotosMapper.*/
    @Autowired
    private TControlProductoTerminadoFotosMapper                tControlProductoTerminadoFotosMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoFotosHisMapper.*/
    @Autowired
    private TControlProductoTerminadoFotosHisMapper             tControlProductoTerminadoFotosHisMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoPesajesBrixMapper.*/
    @Autowired
    private TControlProductoTerminadoPesajesBrixMapper          tControlProductoTerminadoPesajesBrixMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoPesajesBrixHisMapper.*/
    @Autowired
    private TControlProductoTerminadoPesajesBrixHisMapper       tControlProductoTerminadoPesajesBrixHisMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoPesajesCajaMapper.*/
    @Autowired
    private TControlProductoTerminadoPesajesCajaMapper          tControlProductoTerminadoPesajesCajaMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoPesajesCajaHisMapper.*/
    @Autowired
    private TControlProductoTerminadoPesajesCajaHisMapper       tControlProductoTerminadoPesajesCajaHisMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoPesajesCalibreMapper.*/
    @Autowired
    private TControlProductoTerminadoPesajesCalibreMapper       tControlProductoTerminadoPesajesCalibreMapper;
    /** Inyección por Spring del mapper tControlProductoTerminadoPesajesCalibreHisMapper.*/
    @Autowired
    private TControlProductoTerminadoPesajesCalibreHisMapper    tControlProductoTerminadoPesajesCalibreHisMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoPesajesConfeccionMapper.*/
    @Autowired
    private TControlProductoTerminadoPesajesConfeccionMapper    tControlProductoTerminadoPesajesConfeccionMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoPesajesConfeccionHisMapper.*/
    @Autowired
    private TControlProductoTerminadoPesajesConfeccionHisMapper tControlProductoTerminadoPesajesConfeccionHisMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoPesajesPenetromiaMapper.*/
    @Autowired
    private TControlProductoTerminadoPesajesPenetromiaMapper    tControlProductoTerminadoPesajesPenetromiaMapper;
    /** Inyección por Spring del mapper TControlProductoTerminadoPesajesPenetromiaHisMapper.*/
    @Autowired
    private TControlProductoTerminadoPesajesPenetromiaHisMapper tControlProductoTerminadoPesajesPenetromiaHisMapper;
    /** Inyección por Spring del mapper TRegistrosCambiosControlProductoTerminadoMapper.*/
    @Autowired
    private TRegistrosCambiosControlProductoTerminadoMapper     tRegistrosCambiosControlProductoTerminadoMapper;
    private static final long                                   serialVersionUID = 5701299788812594642L;
    /** Contendrá el número de fotos que debe tener de palet. */
    @Value("${app.pt.fotos.pale}")
    private Integer                                             appPtFotosPale;
    /** Contendrá el número de fotos que debe tener de cajas .*/
    @Value("${app.pt.fotos.cajas}")
    private Integer                                             appPtFotosCajas;
    /** Contendrá el número de fotos que debe tener de etiquetas internas .*/
    @Value("${app.pt.fotos.etiqueta1}")
    private Integer                                             appPtFotosEtiqueta1;
    /** Contendrá el número de fotos que debe tener de etiquetas externas .*/
    @Value("${app.pt.fotos.etiqueta2}")
    private Integer                                             appPtFotosEtiqueta2;
    /** Contendrá el número de pesajes que se deben realizar en las confecciones .*/
    @Value("${app.pt.pesajes.confeccion}")
    private Integer                                             appPtPesajesConfeccion;
    /** Contendrá el número de pesajes que se deben realizar en las cajas .*/
    @Value("${app.pt.pesajes.caja}")
    private Integer                                             appPtPesajesCaja;
    /** Contendrá el número de pesajes que se deben realizar en los calibres .*/
    @Value("${app.pt.pesajes.calibre}")
    private Integer                                             appPtPesajesCalibre;

    /**
     * Método que nos retorna el control de PT a partir del ID.
     * @param id El ID del control de PT
     * @return El control de PT encontrado.
     */
    public TControlProductoTerminado obtenerControlPtPorId(Integer id) {
        return tControlProductoTerminadoMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el control de PT a partir del ID.
     * @param id El ID del control de PT
     * @return El control de PT encontrado.
     */
    public TControlProductoTerminadoHis obtenerControlPtHisPorId(Integer id) {
        return tControlProductoTerminadoHisMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna la línea del control de PT a partir del ID.
     * @param id El ID de la línea del control de PT
     * @return La línea del control de PT encontrado.
     */
    public TLineaControlProductoTerminado obtenerLineaControlPtPorId(Integer idLinea) {
        return tLineaControlProductoTerminadoMapper.selectByPrimaryKey(idLinea);
    }

    /**
     * Método que nos retorna la línea del control de PT a partir del ID.
     * @param id El ID de la línea del control de PT
     * @return La línea del control de PT encontrado.
     */
    public TLineaControlProductoTerminadoHis obtenerLineaHisControlPtPorId(Integer idLinea) {
        return tLineaControlProductoTerminadoHisMapper.selectByPrimaryKey(idLinea);
    }

    /**
     * Método que nos retorna la línea del control de PT a partir del ID.
     * @param id El ID de la línea del control de PT
     * @return La línea del control de PT encontrado.
     */
    public List<TLineaControlProductoTerminado> obtenerLineasControlPtPorId(Integer idControlPt) {
        return tLineaControlProductoTerminadoMapper.obtenerLineasControlPtPorId(idControlPt);
    }

    /**
     * Método que nos retorna la imagen del control de PT a partir del ID.
     * @param id El ID de la imagen de control de PT
     * @return El control de PT encontrado.
     */
    public TControlProductoTerminadoFotos obtenerImagenControlPtPorId(Integer id) {
        return tControlProductoTerminadoFotosMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna la imagen del control de PT a partir del ID.
     * @param id El ID de la imagen de control de PT
     * @return El control de PT encontrado.
     */
    public TControlProductoTerminadoFotos obtenerImagenIdControlPtDescripcionOrden(Integer idControlPt, String descripcion, Integer orden) {
        return tControlProductoTerminadoFotosMapper.obtenerImagenIdControlPtDescripcionOrden(idControlPt, descripcion, orden);
    }

    /**
     * Método que nos retorna la imagen del control de PT a partir del ID.
     * @param id El ID de la imagen de control de PT
     * @return El control de PT encontrado.
     */
    public TControlProductoTerminadoFotos obtenerImagenIdLineaDescripcionOrden(Integer idLinea, String descripcion, Integer orden) {
        return tControlProductoTerminadoFotosMapper.obtenerImagenIdLineaDescripcionOrden(idLinea, descripcion, orden);
    }

    /**
     * Método que nos retorna el control de PT pesajes de brix a partir del ID.
     * @param id El ID del control de pt de pesaje de brix
     * @return El control de PT de pesaje de brix encontrado.
     */
    public TControlProductoTerminadoPesajesBrix obtenerControlPtPesajesBrixPorId(Integer id) {
        return tControlProductoTerminadoPesajesBrixMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el control de PT pesajes de brix a partir del ID.
     * @param id El ID del control de pt de pesaje de brix
     * @return El control de PT de pesaje de brix encontrado.
     */
    public TControlProductoTerminadoPesajesBrixHis obtenerControlPtPesajesBrixHisPorId(Integer id) {
        return tControlProductoTerminadoPesajesBrixHisMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el control de PT pesajes de brix a partir del ID.
     * @param id El ID del control de pt de pesaje de brix
     * @return El control de PT de pesaje de brix encontrado.
     */
    public TControlProductoTerminadoFotosHis obtenerFotoPorId(Integer id) {
        return tControlProductoTerminadoFotosHisMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el control de PT pesajes de caja a partir del ID.
     * @param id El ID del control de pt de pesaje de caja
     * @return El control de PT de pesaje de caja encontrado.
     */
    public TControlProductoTerminadoPesajesCaja obtenerControlPtPesajesCajaPorId(Integer id) {
        return tControlProductoTerminadoPesajesCajaMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el control de PT pesajes de caja a partir del ID.
     * @param id El ID del control de pt de pesaje de caja
     * @return El control de PT de pesaje de caja encontrado.
     */
    public TControlProductoTerminadoPesajesCajaHis obtenerControlPtPesajesCajaHisPorId(Integer id) {
        return tControlProductoTerminadoPesajesCajaHisMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el control de PT pesajes de caja a partir del ID.
     * @param id El ID del control de pt de pesaje de caja
     * @return El control de PT de pesaje de caja encontrado.
     */
    public TControlProductoTerminadoPesajesCalibreHis obtenerControlPtPesajesCalibreHisPorId(Integer id) {
        return tControlProductoTerminadoPesajesCalibreHisMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el control de PT pesajes de calibre a partir del ID.
     * @param id El ID del control de pt de pesaje de calibre
     * @return El control de PT de pesaje de calibre encontrado.
     */
    public TControlProductoTerminadoPesajesCalibre obtenerControlPtPesajesCalibrePorId(Integer id) {
        return tControlProductoTerminadoPesajesCalibreMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el control de PT pesajes de confección a partir del ID.
     * @param id El ID del control de pt de pesaje de confección
     * @return El control de PT de pesaje de confección encontrado.
     */
    public TControlProductoTerminadoPesajesConfeccion obtenerControlPtPesajesConfeccionPorId(Integer id) {
        return tControlProductoTerminadoPesajesConfeccionMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el control de PT pesajes de confección a partir del ID.
     * @param id El ID del control de pt de pesaje de confección
     * @return El control de PT de pesaje de confección encontrado.
     */
    public TControlProductoTerminadoPesajesConfeccionHis obtenerControlPtPesajesConfeccionHisPorId(Integer id) {
        return tControlProductoTerminadoPesajesConfeccionHisMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el control de PT pesajes de penetromia a partir del ID.
     * @param id El ID del control de pt de pesaje de penetromia
     * @return El control de PT de pesaje de penetromia encontrado.
     */
    public TControlProductoTerminadoPesajesPenetromia obtenerControlPtPesajesPenetromiaPorId(Integer id) {
        return tControlProductoTerminadoPesajesPenetromiaMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos retorna el control de PT pesajes de penetromia a partir del ID.
     * @param id El ID del control de pt de pesaje de penetromia
     * @return El control de PT de pesaje de penetromia encontrado.
     */
    public TControlProductoTerminadoPesajesPenetromiaHis obtenerControlPtPesajesPenetromiaHisPorId(Integer id) {
        return tControlProductoTerminadoPesajesPenetromiaHisMapper.selectByPrimaryKey(id);
    }

    /**
     * Método que nos busca en el sistema el control de producto terminado a partir del número de pedido.
     * @param @numeroPedido El número de pedido por el que realizar la búsqueda.
     * @return El control de producto terminado encontrado.
     */
    public TControlProductoTerminado obtenerControlPtPorNumPedido(String numeroPedido) {
        return tControlProductoTerminadoMapper.obtenerControlPtPorNumPedido(numeroPedido);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminado> obtenerControlPtIds(List<Integer> lIds) {
        return tControlProductoTerminadoMapper.obtenerControlPtIds(lIds);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminado> obtenerControlPtPorFechas(Date f1, Date f2) {
        return tControlProductoTerminadoMapper.obtenerControlPtPorFechas(f1, f2);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TLineaControlProductoTerminado> obtenerLineasControlProductoTerminado(Integer idControlPt) {
        return tLineaControlProductoTerminadoMapper.obtenerLineasControlPtPorId(idControlPt);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TLineaControlProductoTerminadoVista> obtenerLineasControlProductoTerminadoVista(Integer idControlPt) {
        return convertirLineaProductoTerminadoVista(obtenerLineasControlPtPorId(idControlPt));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminado> obtenerControlPtCreados(Integer user, Date f1, Date f2) {
        return tControlProductoTerminadoMapper.obtenerControlPtCreados(user, f1, f2);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoFotos> obtenerImagenesIdControlPtDescripcion(Integer idControlPt, String descripcion) {
        return tControlProductoTerminadoFotosMapper.obtenerImagenesIdControlPtDescripcion(idControlPt, descripcion);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoFotos> obtenerImagenesIdLineaDescripcion(Integer idLinea, String descripcion) {
        return tControlProductoTerminadoFotosMapper.obtenerImagenesIdLineaDescripcion(idLinea, descripcion);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoFotos> obtenerImagenesIdControlPt(Integer idControlPt) {
        return tControlProductoTerminadoFotosMapper.obtenerImagenesIdControlPt(idControlPt);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoFotos> obtenerImagenesIdLinea(Integer idControlPt) {
        return tControlProductoTerminadoFotosMapper.obtenerImagenesIdLinea(idControlPt);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesBrix> obtenerPesajesBrixIdControlPt(Integer idControlPt) {
        return tControlProductoTerminadoPesajesBrixMapper.obtenerPesajesBrixIdControlPt(idControlPt);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesBrix> obtenerPesajesBrixIdLinea(Integer idLinea) {
        return tControlProductoTerminadoPesajesBrixMapper.obtenerPesajesBrixIdLinea(idLinea);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesBrixVista> obtenerPesajesBrixIdLineaVista(Integer idLinea) {
        return convertirControlProductoTerminadoPesajeBrixVista(obtenerPesajesBrixIdLinea(idLinea));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesCaja> obtenerPesajesCajaIdControlPt(Integer idControlPt) {
        return tControlProductoTerminadoPesajesCajaMapper.obtenerPesajesCajaIdControlPt(idControlPt);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesCaja> obtenerPesajesCajaIdLinea(Integer idLinea) {
        return tControlProductoTerminadoPesajesCajaMapper.obtenerPesajesCajaIdLinea(idLinea);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesCajaVista> obtenerPesajesCajaIdLineaVista(Integer idLinea) {
        return convertirControlProductoTerminadoPesajeCajaVista(obtenerPesajesCajaIdLinea(idLinea));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesCalibre> obtenerPesajesCalibreIdControlPt(Integer idControlPt) {
        return tControlProductoTerminadoPesajesCalibreMapper.obtenerPesajesCalibreIdControlPt(idControlPt);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesCalibre> obtenerPesajesCalibreIdLinea(Integer idLinea) {
        return tControlProductoTerminadoPesajesCalibreMapper.obtenerPesajesCalibreIdLinea(idLinea);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesCalibreVista> obtenerPesajesCalibreIdLineaVista(Integer idLinea) {
        return convertirControlProductoTerminadoPesajeCalibreVista(obtenerPesajesCalibreIdLinea(idLinea));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesConfeccion> obtenerPesajesConfeccionIdControlPt(Integer idControlPt) {
        return tControlProductoTerminadoPesajesConfeccionMapper.obtenerPesajesConfeccionIdControlPt(idControlPt);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesConfeccion> obtenerPesajesConfeccionIdLinea(Integer idLinea) {
        return tControlProductoTerminadoPesajesConfeccionMapper.obtenerPesajesConfeccionIdLinea(idLinea);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesConfeccionVista> obtenerPesajesConfeccionIdLineaVista(Integer idLinea) {
        return convertirControlProductoTerminadoPesajeConfeccionVista(obtenerPesajesConfeccionIdLinea(idLinea));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesPenetromia> obtenerPesajesPenetromiaIdControlPt(Integer idControlPt) {
        return tControlProductoTerminadoPesajesPenetromiaMapper.obtenerPesajesPenetromiaIdControlPt(idControlPt);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesPenetromia> obtenerPesajesPenetromiaIdLinea(Integer idLinea) {
        return tControlProductoTerminadoPesajesPenetromiaMapper.obtenerPesajesPenetromiaIdLinea(idLinea);
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesPenetromiaVista> obtenerPesajesPenetromiaIdLineaVista(Integer idLinea) {
        return convertirControlProductoTerminadoPesajePenetromiaVista(obtenerPesajesPenetromiaIdLinea(idLinea));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoVista> obtenerControlPtPorFechasVista(Date f1, Date f2) {
        return convertirProductoTerminadoVista(obtenerControlPtPorFechas(f1, f2));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoVista> obtenerControlPtCreadosVista(Integer user, Date f1, Date f2) {
        return convertirProductoTerminadoVista(obtenerControlPtCreados(user, f1, f2));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesBrixVista> obtenerPesajesBrixIdControlPtVista(Integer idControlPt) {
        return convertirControlProductoTerminadoPesajeBrixVista(obtenerPesajesBrixIdControlPt(idControlPt));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesCajaVista> obtenerPesajesCajaIdControlPtVista(Integer idControlPt) {
        return convertirControlProductoTerminadoPesajeCajaVista(obtenerPesajesCajaIdControlPt(idControlPt));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesCalibreVista> obtenerPesajesCalibreIdControlPtVista(Integer idControlPt) {
        return convertirControlProductoTerminadoPesajeCalibreVista(obtenerPesajesCalibreIdControlPt(idControlPt));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesConfeccionVista> obtenerPesajesConfeccionIdControlPtVista(Integer idControlPt) {
        return convertirControlProductoTerminadoPesajeConfeccionVista(obtenerPesajesConfeccionIdControlPt(idControlPt));
    }

    /**
     * Método que nos retorna los controles de PT con las fechas comprendidas entre las pasadas por parámetro.
     * @param f1 El La fecha desde.
     * @param f2 El La fecha hasta.
     * @return Los controles de PT que cumplen la condición.
     */
    public List<TControlProductoTerminadoPesajesPenetromiaVista> obtenerPesajesPenetromiaIdControlPtVista(Integer idControlPt) {
        return convertirControlProductoTerminadoPesajePenetromiaVista(obtenerPesajesPenetromiaIdControlPt(idControlPt));
    }

    /**
     * Método que nos crea el control de producto terminado y nos retorna el ID generado.
     * @param record El control de producto terminado a crear.
     * @return El ID generado.
     */
    public int crearControlPtRetornaId(TControlProductoTerminado record) {

        Integer id = -1;

        try {
            // Rellenamos los datos necesarios para crear la familia.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("indNave1", record.getIndNave1());
            map.put("indNave2", record.getIndNave2());
            map.put("indMesa1", record.getIndMesa1());
            map.put("indMesa2", record.getIndMesa2());
            map.put("indFlowPack", record.getIndFlowPack());
            map.put("indMaduracion", record.getIndMaduracion());
            map.put("indRepaso", record.getIndRepaso());
            map.put("indMallas", record.getIndMallas());
            map.put("indMango", record.getIndMango());
            map.put("indNave3", record.getIndNave3());
            map.put("indBio", record.getIndBio());
            map.put("calibrador", record.getCalibrador());
            map.put("mesaConfeccion", record.getMesaConfeccion());
            map.put("fecha", record.getFecha());
            map.put("idCliente", record.getIdCliente());
            map.put("numeroPedido", record.getNumeroPedido());
            map.put("numCajasPedido", record.getNumCajasPedido());
            map.put("numCajasReal", record.getNumCajasReal());
            map.put("idProdudcto", record.getIdProdudcto());
            map.put("idVariedad", record.getIdVariedad());
            map.put("idCalibre", record.getIdCalibre());
            map.put("idDiametro", record.getIdDiametro());
            map.put("pesoCaja", record.getPesoCaja());
            map.put("observaciones", record.getObservaciones());
            map.put("usuCrea", record.getUsuCrea());
            map.put("fechaCrea", record.getFechaCrea());
            map.put("usuModifica", record.getUsuModifica());
            map.put("fechaModifica", record.getFechaModifica());
            map.put("indFotoEtiqueta", record.getIndFotoEtiqueta());
            map.put("indFotoConfeccion", record.getIndFotoConfeccion());
            map.put("indFotoCaja", record.getIndFotoCaja());
            map.put("indFotoPale", record.getIndFotoPale());
            map.put("numPesajesConfeccion", record.getNumPesajesConfeccion());
            map.put("numPesajesCajas", record.getNumPesajesCajas());
            map.put("numPesajesCalibres", record.getNumPesajesCalibres());
            map.put("indContaminaFisica", record.getIndContaminaFisica());
            map.put("indContaminaQuimica", record.getIndContaminaQuimica());
            map.put("indContaminaBiologica", record.getIndContaminaBiologica());
            map.put("linea", record.getLinea());
            map.put("dInternos", record.getdInternos());
            map.put("dExternos", record.getdExternos());

            tControlProductoTerminadoMapper.insertRecord(map);

            id = (Integer) map.get("id");
        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_CONTROL_PT + ", Error al crear el control de producto terminado: " + record.toString2() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return id;
    }

    /**
     * Método que nos crea el control de producto terminado y nos retorna el ID generado.
     * @param record El control de producto terminado a crear.
     * @return El ID generado.
     */
    public int crearLineaControlPtRetornaId(TLineaControlProductoTerminado record) {

        Integer id = -1;

        try {
            // Rellenamos los datos necesarios para crear la familia.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", 0);
            map.put("idControlPt", record.getIdControlPt());
            map.put("numCajasPedido", record.getNumCajasPedido());
            map.put("numCajasReal", record.getNumCajasReal());
            map.put("idProdudcto", record.getIdProdudcto());
            map.put("idVariedad", record.getIdVariedad());
            map.put("idCalibre", record.getIdCalibre());
            map.put("idDiametro", record.getIdDiametro());
            map.put("pesoCaja", record.getPesoCaja());
            map.put("origen", record.getOrigen());
            map.put("observaciones", record.getObservaciones());
            map.put("usuCrea", record.getUsuCrea());
            map.put("fechaCrea", record.getFechaCrea());
            map.put("usuModifica", record.getUsuModifica());
            map.put("fechaModifica", record.getFechaModifica());
            map.put("indFotoEtiqueta", record.getIndFotoEtiqueta());
            map.put("indFotoConfeccion", record.getIndFotoConfeccion());
            map.put("indFotoCaja", record.getIndFotoCaja());
            map.put("indFotoPale", record.getIndFotoPale());
            map.put("numPesajesConfeccion", record.getNumPesajesConfeccion());
            map.put("numPesajesCajas", record.getNumPesajesCajas());
            map.put("numPesajesCalibres", record.getNumPesajesCalibres());
            map.put("indContaminaFisica", record.getIndContaminaFisica());
            map.put("indContaminaQuimica", record.getIndContaminaQuimica());
            map.put("indContaminaBiologica", record.getIndContaminaBiologica());
            map.put("linea", record.getLinea());
            map.put("dInternos", record.getdInternos());
            map.put("dExternos", record.getdExternos());
            map.put("estado", record.getEstado());

            tLineaControlProductoTerminadoMapper.insertRecord(map);

            id = (Integer) map.get("id");
        } catch (Exception e) {
            id = -1;
            log.error(Constants.BD_KO_CREA_CONTROL_PT + ", Error al crear la línea de control de producto terminado: " + record.toString2() + ", ", e);
        }

        // Retornamos el resultado de la operación.
        return id;
    }

    /**
     * Método que se encarga de crear un nuevo control de producto terminado en el sistema.
     * @param record El control de producto terminado a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearControlPt(TControlProductoTerminado record) {
        record.setNumeroPedido(record.getNumeroPedido().toUpperCase());

        // El resultado de la operación.
        String result = Constants.OPERACION_OK;

        // Comprobamos si existe el control de producto terminado
        //TControlProductoTerminado aux = obtenerControlPtPorNumPedido(record.getNumeroPedido());

        //if (aux != null) {
        //    return Constants.BD_KO_CREAR_CONTROL_PT_EXISTE_NUM_PEDIDO;
        //}
        try {

            result = tControlProductoTerminadoMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_CONTROL_PT;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_CONTROL_PT;
            log.error(Constants.BD_KO_CREA_CONTROL_PT + ", Error al crear el control de producto terminado: " + record.toString2() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo control de producto terminado en el sistema.
     * @param record El control de producto terminado a crear.     
     * @return El código del resultado de la operación.
     */
    public void crearControlPtHis(TControlProductoTerminadoHis record) {
        tControlProductoTerminadoHisMapper.insert(record);
    }

    /**
     * Método que se encarga de crear un nuevo control de producto terminado en el sistema.
     * @param record El control de producto terminado a crear.     
     * @return El código del resultado de la operación.
     */
    public void modificarControlPtHis(TControlProductoTerminadoHis record) {
        tControlProductoTerminadoHisMapper.updateByPrimaryKey(record);
    }

    public void guardarPesajesCajaControlPt(Integer idControlPt, Integer idLinea, List<Double> lPesajes, Integer user) {

        eliminarPesajesCajaControlPt(idLinea);

        TControlProductoTerminadoPesajesCaja record = null;
        Integer orden = 1;
        for (Double val : lPesajes) {
            record = new TControlProductoTerminadoPesajesCaja();
            record.setFechaCrea(Utils.generarFecha());
            record.setIdControlProductoTerminado(idControlPt);
            record.setIdLinea(idLinea);
            record.setOrden(orden);
            record.setUsuCrea(user);
            record.setValor(val);
            crearPesajeCajaControlPt(record);
            orden++;
        }
    }

    public void crearPesajeCajaHis(TControlProductoTerminadoPesajesCajaHis record) {
        tControlProductoTerminadoPesajesCajaHisMapper.insert(record);
    }

    public void crearPesajeCalibreHis(TControlProductoTerminadoPesajesCalibreHis record) {
        tControlProductoTerminadoPesajesCalibreHisMapper.insert(record);
    }

    public void crearPesajeConfeccionHis(TControlProductoTerminadoPesajesConfeccionHis record) {
        tControlProductoTerminadoPesajesConfeccionHisMapper.insert(record);
    }

    public void crearPesajePenetromiaHis(TControlProductoTerminadoPesajesPenetromiaHis record) {
        tControlProductoTerminadoPesajesPenetromiaHisMapper.insert(record);
    }

    public void crearPesajeBrixHis(TControlProductoTerminadoPesajesBrixHis record) {
        tControlProductoTerminadoPesajesBrixHisMapper.insert(record);
    }

    public void crearFotoHis(TControlProductoTerminadoFotosHis record) {
        tControlProductoTerminadoFotosHisMapper.insert(record);
    }

    public void crearLineaControlProductoTerminadoHis(TLineaControlProductoTerminadoHis record) {
        tLineaControlProductoTerminadoHisMapper.insert(record);
    }

    public void modificarPesajeCajaHis(TControlProductoTerminadoPesajesCajaHis record) {
        tControlProductoTerminadoPesajesCajaHisMapper.updateByPrimaryKey(record);
    }

    public void modificarPesajeCalibreHis(TControlProductoTerminadoPesajesCalibreHis record) {
        tControlProductoTerminadoPesajesCalibreHisMapper.updateByPrimaryKey(record);
    }

    public void modificarPesajeConfeccionHis(TControlProductoTerminadoPesajesConfeccionHis record) {
        tControlProductoTerminadoPesajesConfeccionHisMapper.updateByPrimaryKey(record);
    }

    public void modificarPesajePenetromiaHis(TControlProductoTerminadoPesajesPenetromiaHis record) {
        tControlProductoTerminadoPesajesPenetromiaHisMapper.updateByPrimaryKey(record);
    }

    public void modificarPesajeBrixHis(TControlProductoTerminadoPesajesBrixHis record) {
        tControlProductoTerminadoPesajesBrixHisMapper.updateByPrimaryKey(record);
    }

    public void modificarFotoHis(TControlProductoTerminadoFotosHis record) {
        tControlProductoTerminadoFotosHisMapper.updateByPrimaryKey(record);
    }

    public void modificarLineaControlProductoTerminadoHis(TLineaControlProductoTerminadoHis record) {
        tLineaControlProductoTerminadoHisMapper.updateByPrimaryKey(record);
    }

    public void guardarPesajesConfeccionControlPt(Integer idControlPt, Integer idLinea, List<Double> lPesajes, Integer user) {

        eliminarPesajesConfeccionControlPt(idLinea);

        TControlProductoTerminadoPesajesConfeccion record = null;
        Integer orden = 1;
        for (Double val : lPesajes) {
            record = new TControlProductoTerminadoPesajesConfeccion();
            record.setFechaCrea(Utils.generarFecha());
            record.setIdControlProductoTerminado(idControlPt);
            record.setIdLinea(idLinea);
            record.setOrden(orden);
            record.setUsuCrea(user);
            record.setValor(val);
            crearPesajeConfeccionControlPt(record);
            orden++;

        }

    }

    public void guardarPesajesCalibreControlPt(Integer idControlPt, Integer idLinea, List<Double> lPesajes, Integer user) {

        eliminarPesajesCalibreControlPt(idLinea);

        TControlProductoTerminadoPesajesCalibre record = null;
        Integer orden = 1;
        for (Double val : lPesajes) {
            record = new TControlProductoTerminadoPesajesCalibre();
            record.setFechaCrea(Utils.generarFecha());
            record.setIdControlProductoTerminado(idControlPt);
            record.setIdLinea(idLinea);
            record.setOrden(orden);
            record.setUsuCrea(user);
            record.setValor(val);
            crearPesajeCalibreControlPt(record);
            orden++;

        }

    }

    public void guardarPesajesPenetromiaControlPt(Integer idControlPt, Integer idLinea, List<Double> lPesajes, Integer user) {

        eliminarPesajesPenetromiaControlPt(idLinea);

        TControlProductoTerminadoPesajesPenetromia record = null;
        Integer orden = 1;
        for (Double val : lPesajes) {
            record = new TControlProductoTerminadoPesajesPenetromia();
            record.setFechaCrea(Utils.generarFecha());
            record.setIdControlProductoTerminado(idControlPt);
            record.setIdLinea(idLinea);
            record.setOrden(orden);
            record.setUsuCrea(user);
            record.setValor(val);
            crearPesajePenetromiaControlPt(record);
            orden++;

        }

    }

    public void guardarPesajesBrixControlPt(Integer idControlPt, Integer idLinea, List<Double> lPesajes, Integer user) {

        eliminarPesajesBrixControlPt(idLinea);

        TControlProductoTerminadoPesajesBrix record = null;
        Integer orden = 1;
        for (Double val : lPesajes) {
            record = new TControlProductoTerminadoPesajesBrix();
            record.setFechaCrea(Utils.generarFecha());
            record.setIdControlProductoTerminado(idControlPt);
            record.setIdLinea(idLinea);
            record.setOrden(orden);
            record.setUsuCrea(user);
            record.setValor(val);
            crearPesajeBrixControlPt(record);
            orden++;

        }

    }

    public void eliminarPesajesCajaControlPtPorId(Integer id) {
        tControlProductoTerminadoPesajesCajaMapper.deleteByPrimaryKey(id);
    }

    public void eliminarPesajesCalibreControlPtPorId(Integer id) {
        tControlProductoTerminadoPesajesCalibreMapper.deleteByPrimaryKey(id);
    }

    public void eliminarPesajesCajaControlPt(Integer idControlPt) {
        tControlProductoTerminadoPesajesCajaMapper.eliminarPesajesIdControlPt(idControlPt);
    }

    public void eliminarPesajesConfeccionControlPt(Integer idControlPt) {
        tControlProductoTerminadoPesajesConfeccionMapper.eliminarPesajesConfeccionControlPt(idControlPt);
    }

    public void eliminarPesajesConfeccionControlPtPorId(Integer id) {
        tControlProductoTerminadoPesajesConfeccionMapper.deleteByPrimaryKey(id);
    }

    public void eliminarPesajesCalibreControlPt(Integer idControlPt) {
        tControlProductoTerminadoPesajesCalibreMapper.eliminarPesajesCalibreControlPt(idControlPt);
    }

    public void eliminarPesajesPenetromiaControlPt(Integer idControlPt) {
        tControlProductoTerminadoPesajesPenetromiaMapper.eliminarPesajesPenetromiaControlPt(idControlPt);
    }

    public void eliminarPesajesPenetromiaControlPtPorId(Integer id) {
        tControlProductoTerminadoPesajesPenetromiaMapper.deleteByPrimaryKey(id);
    }

    public void eliminarPesajesBrixControlPt(Integer idControlPt) {
        tControlProductoTerminadoPesajesBrixMapper.eliminarPesajesBrixControlPt(idControlPt);
    }

    public void eliminarPesajesBrixControlPtPorId(Integer id) {
        tControlProductoTerminadoPesajesBrixMapper.deleteByPrimaryKey(id);
    }

    public void eliminarFotoControlPtPorId(Integer id) {
        tControlProductoTerminadoFotosMapper.deleteByPrimaryKey(id);
    }

    public void eliminarLineaControlPtPorId(Integer id) {
        tLineaControlProductoTerminadoMapper.deleteByPrimaryKey(id);
    }

    /**
     * Método que se encarga de crear un nuevo control de producto terminado en el sistema.
     * @param record El control de producto terminado a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearPesajeCajaControlPt(TControlProductoTerminadoPesajesCaja record) {

        // El resultado de la operación.
        String result = Constants.OPERACION_OK;

        try {

            result = tControlProductoTerminadoPesajesCajaMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_CONTROL_PT_PESAJE_CAJA;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_CONTROL_PT_PESAJE_CAJA;
            log.error(Constants.BD_KO_CREA_CONTROL_PT_PESAJE_CAJA + ", Error al crear el peaje de caja del control de producto terminado: " + record.toString2() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo control de producto terminado en el sistema.
     * @param record El control de producto terminado a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearPesajeConfeccionControlPt(TControlProductoTerminadoPesajesConfeccion record) {

        // El resultado de la operación.
        String result = Constants.OPERACION_OK;

        try {

            result = tControlProductoTerminadoPesajesConfeccionMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_CONTROL_PT_PESAJE_CONFECCION;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_CONTROL_PT_PESAJE_CONFECCION;
            log.error(Constants.BD_KO_CREA_CONTROL_PT_PESAJE_CONFECCION + ", Error al crear el pesaje de confección del control de producto terminado: " + record.toString2() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo control de producto terminado en el sistema.
     * @param record El control de producto terminado a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearPesajeCalibreControlPt(TControlProductoTerminadoPesajesCalibre record) {

        // El resultado de la operación.
        String result = Constants.OPERACION_OK;

        try {

            result = tControlProductoTerminadoPesajesCalibreMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_CONTROL_PT_PESAJE_CALIBRE;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_CONTROL_PT_PESAJE_CALIBRE;
            log.error(Constants.BD_KO_CREA_CONTROL_PT_PESAJE_CALIBRE + ", Error al crear el pesaje de calibre del control de producto terminado: " + record.toString2() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo control de producto terminado en el sistema.
     * @param record El control de producto terminado a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearPesajePenetromiaControlPt(TControlProductoTerminadoPesajesPenetromia record) {

        // El resultado de la operación.
        String result = Constants.OPERACION_OK;

        try {

            result = tControlProductoTerminadoPesajesPenetromiaMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_CONTROL_PT_PESAJE_PENETROMIA;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_CONTROL_PT_PESAJE_PENETROMIA;
            log.error(Constants.BD_KO_CREA_CONTROL_PT_PESAJE_PENETROMIA + ", Error al crear el pesaje de penetromía del control de producto terminado: " + record.toString2() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de crear un nuevo control de producto terminado en el sistema.
     * @param record El control de producto terminado a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearPesajeBrixControlPt(TControlProductoTerminadoPesajesBrix record) {

        // El resultado de la operación.
        String result = Constants.OPERACION_OK;

        try {

            result = tControlProductoTerminadoPesajesBrixMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_CONTROL_PT_PESAJE_BRIX;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_CONTROL_PT_PESAJE_BRIX;
            log.error(Constants.BD_KO_CREA_CONTROL_PT_PESAJE_BRIX + ", Error al crear el pesaje de BRIX del control de producto terminado: " + record.toString2() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de modificar el control de producto terminado en el sistema.
     * @param record El control de producto terminado a modificar.     
     * @return El código del resultado de la operación.
     */
    public String modificarControlPt(TControlProductoTerminado record) {
        record.setNumeroPedido(record.getNumeroPedido().toUpperCase());

        // El resultado de la operación.
        String result = Constants.OPERACION_OK;

        // Comprobamos si existe el control de producto terminado
        TControlProductoTerminado aux = obtenerControlPtPorId(record.getId());

        if (aux == null) {
            result = Constants.CONTROL_PT_NO_EXISTE;
        } else {
            try {

                //         aux = obtenerControlPtPorNumPedido(record.getNumeroPedido());
                //        if (aux != null && !aux.getId().equals(record.getId())) {
                //           return Constants.BD_KO_CREAR_CONTROL_PT_EXISTE_NUM_PEDIDO;
                //       }

                result = tControlProductoTerminadoMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_CONTROL_PT;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_CONTROL_PT;
                log.error(Constants.BD_KO_MODIF_CONTROL_PT + ", Error al modificar el control de producto terminado: " + record.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    /**
     * Método que se encarga de modificar el control de producto terminado en el sistema.
     * @param record El control de producto terminado a modificar.     
     * @return El código del resultado de la operación.
     */
    public String modificarLineaControlPt(TLineaControlProductoTerminado record) {

        // El resultado de la operación.
        String result = Constants.OPERACION_OK;

        // Comprobamos si existe el control de producto terminado
        TLineaControlProductoTerminado aux = obtenerLineaControlPtPorId(record.getId());

        if (aux == null) {
            result = Constants.CONTROL_PT_NO_EXISTE;
        } else {
            try {

                //         aux = obtenerControlPtPorNumPedido(record.getNumeroPedido());
                //        if (aux != null && !aux.getId().equals(record.getId())) {
                //           return Constants.BD_KO_CREAR_CONTROL_PT_EXISTE_NUM_PEDIDO;
                //       }

                result = tLineaControlProductoTerminadoMapper.updateByPrimaryKey(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_MODIF_CONTROL_PT;
            } catch (Exception e) {
                result = Constants.BD_KO_MODIF_CONTROL_PT;
                log.error(Constants.BD_KO_MODIF_CONTROL_PT + ", Error al modificar el control de producto terminado: " + record.toString2() + ", ", e);
            }
        }
        // Retnornamos el resultado de la operación.
        return result;
    }

    public void borrarControlProductoTerminado(Integer id) {
        tControlProductoTerminadoMapper.deleteByPrimaryKey(id);
    }

    /**
     * Método que se encarga de crear un nuevo control de producto terminado en el sistema.
     * @param record El control de producto terminado a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearFotoControlPt(TControlProductoTerminadoFotos record) {

        // El resultado de la operación.
        String result = Constants.OPERACION_OK;

        // Comprobamos si existe la foto con la descripción y orden indicado
        TControlProductoTerminadoFotos aux = null;
        aux = obtenerImagenIdControlPtDescripcionOrden(record.getIdControlProductoTerminado(), record.getDescripcionFoto().trim().toUpperCase(), record.getOrdenFoto());
        while (aux != null) {
            record.setOrdenFoto(record.getOrdenFoto() + 1);
            aux = obtenerImagenIdControlPtDescripcionOrden(record.getIdControlProductoTerminado(), record.getDescripcionFoto().trim().toUpperCase(), record.getOrdenFoto());
        }

        //if (aux != null) {
        //    return Constants.BD_KO_CREAR_CONTROL_PT_EXISTE_NUM_PEDIDO;
        //}
        try {

            result = tControlProductoTerminadoFotosMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_FOTO_CONTROL_PT;

            TControlProductoTerminado cp = obtenerControlPtPorId(record.getIdControlProductoTerminado());
            if (record.getDescripcionFoto().toLowerCase().contains("caja")) {
                cp.setIndFotoCaja(cp.getIndFotoCaja() + 1);
            } else if (record.getDescripcionFoto().toLowerCase().contains("pal")) {
                cp.setIndFotoPale(cp.getIndFotoPale() + 1);
            } else if (record.getDescripcionFoto().toLowerCase().contains("int")) {
                cp.setIndFotoEtiqueta(cp.getIndFotoPale() + 1);
            } else if (record.getDescripcionFoto().toLowerCase().contains("ext")) {
                cp.setIndFotoEtiqueta(cp.getIndFotoPale() + 1);
            }
            modificarControlPt(cp);

        } catch (Exception e) {
            result = Constants.BD_KO_CREA_FOTO_CONTROL_PT;
            log.error(Constants.BD_KO_CREA_FOTO_CONTROL_PT + ", Error al crear la foto del control de producto terminado: " + record.toString2() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    public int eliminarFotoControlProductoTerminadoIdFoto(Integer idFoto) {
        return tControlProductoTerminadoFotosMapper.deleteByPrimaryKey(idFoto);
    }

    public List<TControlProductoTerminadoFotos> obtenerFotosCajas(Integer idControlPt) {
        List<TControlProductoTerminadoFotos> lResult = null;
        lResult = tControlProductoTerminadoFotosMapper.obtenerImagenesIdControlPtDescripcionLike(idControlPt, "FOTO CAJA");
        if (lResult == null) {
            lResult = Utils.generarListaGenerica();
        }
        return lResult;
    }

    public List<TControlProductoTerminadoFotos> obtenerFotosPale(Integer idControlPt) {
        List<TControlProductoTerminadoFotos> lResult = null;
        lResult = tControlProductoTerminadoFotosMapper.obtenerImagenesIdControlPtDescripcionLike(idControlPt, "FOTO PALET");
        if (lResult == null) {
            lResult = Utils.generarListaGenerica();
        }
        return lResult;
    }

    public List<TControlProductoTerminadoFotos> obtenerFotosEtiquetasInt(Integer idControlPt) {
        List<TControlProductoTerminadoFotos> lResult = null;
        lResult = tControlProductoTerminadoFotosMapper.obtenerImagenesIdControlPtDescripcionLike(idControlPt, "FOTO ETIQUETA INTERIOR");
        if (lResult == null) {
            lResult = Utils.generarListaGenerica();
        }
        return lResult;
    }

    public List<TControlProductoTerminadoFotos> obtenerFotosEtiquetasExt(Integer idControlPt) {
        List<TControlProductoTerminadoFotos> lResult = null;
        lResult = tControlProductoTerminadoFotosMapper.obtenerImagenesIdControlPtDescripcionLike(idControlPt, "FOTO ETIQUETA EXTERIOR");
        if (lResult == null) {
            lResult = Utils.generarListaGenerica();
        }
        return lResult;
    }

    public List<TControlProductoTerminadoFotos> obtenerFotosCajasLinea(Integer idLinea) {
        List<TControlProductoTerminadoFotos> lResult = null;
        lResult = tControlProductoTerminadoFotosMapper.obtenerImagenesIdLineaDescripcionLike(idLinea, "FOTO CAJA");
        if (lResult == null) {
            lResult = Utils.generarListaGenerica();
        }
        return lResult;
    }

    public List<TControlProductoTerminadoFotos> obtenerFotosPaleLinea(Integer idLinea) {
        List<TControlProductoTerminadoFotos> lResult = null;
        lResult = tControlProductoTerminadoFotosMapper.obtenerImagenesIdLineaDescripcionLike(idLinea, "FOTO PALET");
        if (lResult == null) {
            lResult = Utils.generarListaGenerica();
        }
        return lResult;
    }

    public List<TControlProductoTerminadoFotos> obtenerFotosEtiquetasIntLinea(Integer idLinea) {
        List<TControlProductoTerminadoFotos> lResult = null;
        lResult = tControlProductoTerminadoFotosMapper.obtenerImagenesIdLineaDescripcionLike(idLinea, "FOTO ETIQUETA INTERIOR");
        if (lResult == null) {
            lResult = Utils.generarListaGenerica();
        }
        return lResult;
    }

    public List<TControlProductoTerminadoFotos> obtenerFotosEtiquetasExtLinea(Integer idLinea) {
        List<TControlProductoTerminadoFotos> lResult = null;
        lResult = tControlProductoTerminadoFotosMapper.obtenerImagenesIdLineaDescripcionLike(idLinea, "FOTO ETIQUETA EXTERIOR");
        if (lResult == null) {
            lResult = Utils.generarListaGenerica();
        }
        return lResult;
    }

    public List<TControlProductoTerminadoVista> convertirProductoTerminadoVista(List<TControlProductoTerminado> lControlProductoTerminado) {

        // Los controles de producto terminado con formato vista.
        List<TControlProductoTerminadoVista> lResult = Utils.generarListaGenerica();
        TControlProductoTerminadoVista aux = null;

        String campo = null;

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();
        List<TProductos> lProductos = productosSetup.obtenerTodosProductos();
        List<TVariedad> lVariedades = productosSetup.obtenerTodasVariedades();
        List<TClientes> lClientes = clientesSetup.obtenerTodosClientes();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));
        // Nutrimos el diccionario con los productos
        Map<Integer, String> mProductos = lProductos.stream().collect(Collectors.toMap(TProductos::getId, TProductos::getDescripcion));
        // Nutrimos el diccionario con las variedades
        Map<Integer, String> mVariedades = lVariedades.stream().collect(Collectors.toMap(TVariedad::getId, TVariedad::getDescripcion));
        // Nutrimos el diccionario con los clientes
        Map<Integer, String> mClientes = lClientes.stream().collect(Collectors.toMap(TClientes::getId, TClientes::getDescripcion));

        Boolean completo = true;
        String estado = "";
        TProductoCalibre cal = null;

        for (TControlProductoTerminado cPt : lControlProductoTerminado) {

            completo = true;
            aux = new TControlProductoTerminadoVista();

            // Realizamos la copia de los campos
            aux.copiaDesdeControlPt(cPt);

            campo = mEmpleados.get(cPt.getUsuCrea());
            aux.setUsuCrea(campo != null ? campo : "Empleado no encontrado; ID:" + cPt.getUsuCrea());
            if (cPt.getUsuModifica() != null) {
                campo = mEmpleados.get(cPt.getUsuModifica());
                aux.setUsuModifica(campo != null ? campo : "Empleado no encontrado; ID: " + cPt.getUsuModifica());
            }
            if (cPt.getUsuFoto() != null) {
                campo = mEmpleados.get(cPt.getUsuFoto());
                aux.setUsuFoto(campo != null ? campo : "Empleado no encontrado; ID: " + cPt.getUsuFoto());
            }
            // PRODUCTO
            campo = mProductos.get(cPt.getIdProdudcto());
            aux.setIdProdudcto(campo != null ? campo : "Producto no encontrado; ID: " + cPt.getIdProdudcto());
            // VARIEDAD
            campo = mVariedades.get(cPt.getIdVariedad());
            aux.setIdVariedad(campo != null ? campo : "Variedad no encontrada; ID: " + cPt.getIdVariedad());

            // CLIENTE
            campo = mClientes.get(cPt.getIdCliente());
            aux.setIdCliente(campo != null ? campo : "Cliente no encontrado; ID: " + cPt.getIdCliente());

            // CALIBRE
            if (cPt.getIdCalibre() != null) {
                cal = productosSetup.obtenerCalibrePorId(cPt.getIdProdudcto(), cPt.getIdCalibre());
                aux.setIdCalibre(cal != null ? cal.toString() : "N/D; CAL: " + cPt.getIdCalibre());
            } else {
                aux.setIdCalibre("CAL: -");
            }

            // Añadimos a la lista final.
            lResult.add(aux);

            if (aux.getFecha().equals("-")) {
                completo = false;
            }

            if (completo) {
                // Comprobamos si el control de PT está completo o no
                // FOTOS DE PALET
                if (cPt.getIndFotoPale().equals(appPtFotosPale) || cPt.getIndFotoPale() > appPtFotosPale) {
                    completo = true;
                } else {
                    completo = false;
                }

                if (!completo) {
                    estado = "Incompleto";
                } else {
                    if (cPt.getIndFotoCaja().equals(appPtFotosCajas) || cPt.getIndFotoCaja() > appPtFotosCajas) {
                        completo = true;
                    } else {
                        completo = false;
                    }

                    if (!completo) {
                        estado = "Incompleto";
                    } else {
                        if (cPt.getIndFotoEtiqueta().equals(appPtFotosEtiqueta1) || cPt.getIndFotoEtiqueta() > appPtFotosEtiqueta1) {
                            completo = true;
                        } else {
                            completo = false;
                        }

                        if (!completo) {
                            estado = "Incompleto";
                        } else {
                            if (cPt.getIndFotoConfeccion().equals(appPtFotosEtiqueta2) || cPt.getIndFotoConfeccion() > appPtFotosEtiqueta2) {
                                completo = true;
                            } else {
                                completo = false;
                            }

                            if (!completo) {
                                estado = "Incompleto";
                            } else {
                                if (Utils.booleanFromInteger(cPt.getIndFlowPack()) || Utils.booleanFromInteger(cPt.getIndMango())) {
                                    if (cPt.getNumPesajesConfeccion().equals(appPtPesajesConfeccion) || cPt.getNumPesajesConfeccion() > appPtPesajesConfeccion) {
                                        completo = true;
                                    } else {
                                        completo = false;
                                    }
                                }

                                if (!completo) {
                                    estado = "Incompleto";
                                } else {
                                    if (cPt.getNumPesajesCajas().equals(appPtPesajesCaja) || cPt.getNumPesajesCajas() > appPtPesajesCaja) {
                                        completo = true;
                                    } else {
                                        completo = false;
                                    }

                                    if (!completo) {
                                        estado = "Incompleto";
                                    } else {
                                        if (cPt.getNumPesajesCalibres().equals(appPtPesajesCalibre) || cPt.getNumPesajesCalibres() > appPtPesajesCalibre) {
                                            completo = true;
                                        } else {
                                            completo = false;
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }

            if (!completo) {
                estado = "Incompleto";
            } else {
                estado = "Completo";
            }

            aux.setEstado(estado);

        }
        // Retnornamos los empleados en formato vista.
        return lResult;
    }

    public List<TLineaControlProductoTerminadoVista> convertirLineaProductoTerminadoVista(List<TLineaControlProductoTerminado> lControlProductoTerminado) {

        // Los controles de producto terminado con formato vista.
        List<TLineaControlProductoTerminadoVista> lResult = Utils.generarListaGenerica();
        TLineaControlProductoTerminadoVista aux = null;

        String campo = null;

        List<TEmpleados> lEmpl = empleadosSetup.obtenerTodosEmpleados();
        List<TProductos> lProductos = productosSetup.obtenerTodosProductos();
        List<TVariedad> lVariedades = productosSetup.obtenerTodasVariedades();
        List<TDiametrosProducto> lDiametros = productosSetup.obtenerTodosDimaetros();

        // Nutrimos el diccionario con los empleados
        Map<Integer, String> mEmpleados = lEmpl.stream().collect(Collectors.toMap(TEmpleados::getId, TEmpleados::getNombre));
        // Nutrimos el diccionario con los productos
        Map<Integer, String> mProductos = lProductos.stream().collect(Collectors.toMap(TProductos::getId, TProductos::getDescripcion));
        // Nutrimos el diccionario con las variedades
        Map<Integer, String> mVariedades = lVariedades.stream().collect(Collectors.toMap(TVariedad::getId, TVariedad::getDescripcion));
        // Nutrimos el diccionario con las variedades
        Map<Integer, String> mDiametros = lDiametros.stream().collect(Collectors.toMap(TDiametrosProducto::getId, TDiametrosProducto::toString));

        Boolean completo = true;
        String estado = "";
        TProductoCalibre cal = null;

        for (TLineaControlProductoTerminado cPt : lControlProductoTerminado) {

            completo = true;
            aux = new TLineaControlProductoTerminadoVista();

            // Realizamos la copia de los campos
            aux.copiaDesdeLinea(cPt);

            campo = mEmpleados.get(cPt.getUsuCrea());
            aux.setUsuCrea(campo != null ? campo : "Empleado no encontrado; ID:" + cPt.getUsuCrea());
            if (cPt.getUsuModifica() != null) {
                campo = mEmpleados.get(cPt.getUsuModifica());
                aux.setUsuModifica(campo != null ? campo : "Empleado no encontrado; ID: " + cPt.getUsuModifica());
            }
            // PRODUCTO
            campo = mProductos.get(cPt.getIdProdudcto());
            aux.setIdProdudcto(campo != null ? campo : "Producto no encontrado; ID: " + cPt.getIdProdudcto());

            if (cPt.getIdVariedad() != null) {
                // VARIEDAD
                campo = mVariedades.get(cPt.getIdVariedad());
                aux.setIdVariedad(campo != null ? campo : "Variedad no encontrada; ID: " + cPt.getIdVariedad());
            }

            if (cPt.getIdCalibre() != null) {
                // CALIBRE
                cal = productosSetup.obtenerCalibrePorId(cPt.getIdProdudcto(), cPt.getIdCalibre());

                aux.setIdCalibre(cal != null ? cal.toString() : "N/D; CAL: " + cPt.getIdCalibre());
            }

            if (cPt.getIdDiametro() != null) {
                // DIAMETRO
                campo = mDiametros.get(cPt.getIdDiametro());
                aux.setIdDiametro(campo != null ? campo : "Diámetro no encontrado; ID: " + cPt.getIdDiametro());
            }
            // Añadimos a la lista final.
            lResult.add(aux);

            if (completo) {
                // Comprobamos si el control de PT está completo o no
                // FOTOS DE PALET
                if (cPt.getIndFotoPale().equals(appPtFotosPale) || cPt.getIndFotoPale() > appPtFotosPale) {
                    completo = true;
                } else {
                    completo = false;
                }

                if (!completo) {
                    estado = "Incompleto";
                } else {
                    if (cPt.getIndFotoCaja().equals(appPtFotosCajas) || cPt.getIndFotoCaja() > appPtFotosCajas) {
                        completo = true;
                    } else {
                        completo = false;
                    }

                    if (!completo) {
                        estado = "Incompleto";
                    } else {
                        if (cPt.getIndFotoEtiqueta().equals(appPtFotosEtiqueta1) || cPt.getIndFotoEtiqueta() > appPtFotosEtiqueta1) {
                            completo = true;
                        } else {
                            completo = true;
                        }

                        if (!completo) {
                            estado = "Incompleto";
                        } else {
                            if (cPt.getIndFotoConfeccion().equals(appPtFotosEtiqueta2) || cPt.getIndFotoConfeccion() > appPtFotosEtiqueta2) {
                                completo = true;
                            } else {
                                completo = false;
                            }

                            if (!completo) {
                                estado = "Incompleto";
                            } else {

                                if (cPt.getNumPesajesCajas().equals(appPtPesajesCaja) || cPt.getNumPesajesCajas() > appPtPesajesCaja) {
                                    completo = true;
                                } else {
                                    completo = false;
                                }

                                if (!completo) {
                                    estado = "Incompleto";
                                } else {
                                    if (cPt.getNumPesajesCalibres().equals(appPtPesajesCalibre) || cPt.getNumPesajesCalibres() > appPtPesajesCalibre) {
                                        completo = true;
                                    } else {
                                        completo = false;
                                    }
                                }

                            }

                        }
                    }
                }
            }

            if (!completo) {
                estado = "Incompleto";
            } else {
                estado = "Completo";
            }

            aux.setEstado(estado);

        }
        // Retnornamos los empleados en formato vista.
        return lResult;
    }

    /**
     * Método que se encarega de convertir los datos de control de producto terminado de pesaje de caja {@link TControlProductoTerminadoPesajesBrix} a formato {@link TControlProductoTerminadoPesajesBrixVista}
     * @param lPesajesCaja Lista de control de producto terminado de pesaje de caja a convertir.
     * @return Lista de empleados convertida a {@link TControlProductoTerminadoPesajesBrixVista}
     */
    private List<TControlProductoTerminadoPesajesBrixVista> convertirControlProductoTerminadoPesajeBrixVista(List<TControlProductoTerminadoPesajesBrix> lPesajesCaja) {
        // Los empleados con formato vista.
        List<TControlProductoTerminadoPesajesBrixVista> lResult = Utils.generarListaGenerica();
        TControlProductoTerminadoPesajesBrixVista aux = null;

        for (TControlProductoTerminadoPesajesBrix pesCaja : lPesajesCaja) {

            aux = new TControlProductoTerminadoPesajesBrixVista();
            // Realizamos la copia de los campos
            aux.copiaDesdePesajeBrix(pesCaja);

            // Añadimos a la lista final.
            lResult.add(aux);
        }
        // Retnornamos los pesajes en formato vista.
        return lResult;

    }

    /**
     * Método que se encarega de convertir los datos de control de producto terminado de pesaje de caja {@link TControlProductoTerminadoPesajesCaja} a formato {@link TControlProductoTerminadoPesajesCajaVista}
     * @param lPesajesCaja Lista de control de producto terminado de pesaje de caja a convertir.
     * @return Lista de empleados convertida a {@link TControlProductoTerminadoPesajesCajaVista}
     */
    private List<TControlProductoTerminadoPesajesCajaVista> convertirControlProductoTerminadoPesajeCajaVista(List<TControlProductoTerminadoPesajesCaja> lPesajesCaja) {
        // Los empleados con formato vista.
        List<TControlProductoTerminadoPesajesCajaVista> lResult = Utils.generarListaGenerica();
        TControlProductoTerminadoPesajesCajaVista aux = null;

        for (TControlProductoTerminadoPesajesCaja pesCaja : lPesajesCaja) {

            aux = new TControlProductoTerminadoPesajesCajaVista();
            // Realizamos la copia de los campos
            aux.copiaDesdePesajeCaja(pesCaja);

            // Añadimos a la lista final.
            lResult.add(aux);
        }
        // Retnornamos los pesajes en formato vista.
        return lResult;

    }

    /**
     * Método que se encarega de convertir los datos de control de producto terminado de pesaje de calibre {@link TControlProductoTerminadoPesajesCalibre} a formato {@link TControlProductoTerminadoPesajesCalibreVista}
     * @param lPesajesCal Lista de control de producto terminado de pesaje de calibre a convertir.
     * @return Lista de empleados convertida a {@link TControlProductoTerminadoPesajesCalibreVista}
     */
    private List<TControlProductoTerminadoPesajesCalibreVista> convertirControlProductoTerminadoPesajeCalibreVista(List<TControlProductoTerminadoPesajesCalibre> lPesajesCal) {
        // Los empleados con formato vista.
        List<TControlProductoTerminadoPesajesCalibreVista> lResult = Utils.generarListaGenerica();
        TControlProductoTerminadoPesajesCalibreVista aux = null;

        for (TControlProductoTerminadoPesajesCalibre pesCal : lPesajesCal) {

            aux = new TControlProductoTerminadoPesajesCalibreVista();
            // Realizamos la copia de los campos
            aux.copiaDesdePesajeCalibre(pesCal);

            // Añadimos a la lista final.
            lResult.add(aux);
        }
        // Retnornamos los pesajes en formato vista.
        return lResult;

    }

    /**
     * Método que se encarega de convertir los datos de control de producto terminado de pesaje de confeccion {@link TControlProductoTerminadoPesajesConfeccion} a formato {@link TControlProductoTerminadoPesajesConfeccionVista}
     * @param lPesajesConf Lista de control de producto terminado de pesaje de confeccion a convertir.
     * @return Lista de empleados convertida a {@link TControlProductoTerminadoPesajesConfeccionVista}
     */
    private List<TControlProductoTerminadoPesajesConfeccionVista> convertirControlProductoTerminadoPesajeConfeccionVista(List<TControlProductoTerminadoPesajesConfeccion> lPesajesConf) {
        // Los empleados con formato vista.
        List<TControlProductoTerminadoPesajesConfeccionVista> lResult = Utils.generarListaGenerica();
        TControlProductoTerminadoPesajesConfeccionVista aux = null;

        for (TControlProductoTerminadoPesajesConfeccion pesConf : lPesajesConf) {

            aux = new TControlProductoTerminadoPesajesConfeccionVista();
            // Realizamos la copia de los campos
            aux.copiaDesdePesajeConfeccion(pesConf);

            // Añadimos a la lista final.
            lResult.add(aux);
        }
        // Retnornamos los pesajes en formato vista.
        return lResult;

    }

    /**
     * Método que se encarega de convertir los datos de control de producto terminado de pesaje de caja {@link TControlProductoTerminadoPesajesPenetromia} a formato {@link TControlProductoTerminadoPesajesPenetromiaVista}
     * @param lPesajesCaja Lista de control de producto terminado de pesaje de caja a convertir.
     * @return Lista de empleados convertida a {@link TControlProductoTerminadoPesajesPenetromiaVista}
     */
    private List<TControlProductoTerminadoPesajesPenetromiaVista> convertirControlProductoTerminadoPesajePenetromiaVista(List<TControlProductoTerminadoPesajesPenetromia> lPesajesCaja) {
        // Los empleados con formato vista.
        List<TControlProductoTerminadoPesajesPenetromiaVista> lResult = Utils.generarListaGenerica();
        TControlProductoTerminadoPesajesPenetromiaVista aux = null;

        for (TControlProductoTerminadoPesajesPenetromia pesCaja : lPesajesCaja) {

            aux = new TControlProductoTerminadoPesajesPenetromiaVista();
            // Realizamos la copia de los campos
            aux.copiaDesdePesajePenetromia(pesCaja);

            // Añadimos a la lista final.
            lResult.add(aux);
        }
        // Retnornamos los pesajes en formato vista.
        return lResult;

    }

    /**
     * Método que se encarga de crear un nuevo registro de cambio de empleado en el sistema.
     * @param record El registro de cambio de empleado a crear.     
     * @return El código del resultado de la operación.
     */
    public String crearRegistroCambioControlPt(TRegistrosCambiosControlProductoTerminado record) {

        String result = Constants.OPERACION_OK;

        try {
            result = tRegistrosCambiosControlProductoTerminadoMapper.insert(record) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_CONTROL_PT;
        } catch (Exception e) {
            result = Constants.BD_KO_CREA_CONTROL_PT;
            log.error(Constants.BD_KO_CREA_CONTROL_PT + ", Error al crear el registro de modificación del control de producto terminado: " + record.toString() + ", ", e);
        }

        // Retnornamos el resultado de la operación.
        return result;
    }

    public String eliminarLineaControlPt(Integer idLinea) {
        return tLineaControlProductoTerminadoMapper.deleteByPrimaryKey(idLinea) == 1 ? Constants.OPERACION_OK : Constants.BD_KO_CREA_CONTROL_PT;
    }

    /**
     * Método que nos eliminar el control de producto terminado y nos crea en las tablas *_his el registo eliminado.
     * @param id El ID del control de producto terminado a eliminar.
     */
    public void eliminarControlProductoTerminado(Integer id) {
        TControlProductoTerminado cPt = obtenerControlPtPorId(id);

        if (cPt != null) {
            // Obtenemos las líneas de control de Pt para migrarlas
            List<TLineaControlProductoTerminado> lLineas = obtenerLineasControlPtPorId(id);

            List<TControlProductoTerminadoPesajesCaja> lPesajesC;
            List<TControlProductoTerminadoPesajesCalibre> lPesajesCal;
            List<TControlProductoTerminadoPesajesConfeccion> lPesajesConf;
            List<TControlProductoTerminadoPesajesPenetromia> lPesajesPen;
            List<TControlProductoTerminadoPesajesBrix> lPesajesB;

            TControlProductoTerminadoPesajesCajaHis pesCHis;
            TControlProductoTerminadoPesajesCalibreHis pesCalHis;
            TControlProductoTerminadoPesajesConfeccionHis pesConfHis;
            TControlProductoTerminadoPesajesPenetromiaHis pesPenHis;
            TControlProductoTerminadoPesajesBrixHis pesBrixHis;
            TControlProductoTerminadoFotosHis fotHis;

            List<TControlProductoTerminadoFotos> lFotos;

            TLineaControlProductoTerminadoHis lHis;
            TControlProductoTerminadoHis cptHis;
            // Vamos migrando las líneas una a una.
            for (TLineaControlProductoTerminado linea : lLineas) {
                // Buscamos los pesajes.
                lPesajesC = obtenerPesajesCajaIdLinea(linea.getId());

                /// PESAJES DE CAJA
                // Por cada pesaje de caja, vamos migrandolo al his.
                for (TControlProductoTerminadoPesajesCaja pesC : lPesajesC) {
                    pesCHis = obtenerControlPtPesajesCajaHisPorId(pesC.getId());
                    if (pesCHis == null) {
                        pesCHis = new TControlProductoTerminadoPesajesCajaHis();
                        pesCHis.copiaDesdePesajeCaja(pesC);
                        crearPesajeCajaHis(pesCHis);
                    } else {
                        pesCHis.copiaDesdePesajeCaja(pesC);
                        modificarPesajeCajaHis(pesCHis);
                    }
                    // Eliminamos el pesaje de caja.
                    eliminarPesajesCajaControlPtPorId(pesC.getId());
                }

                /// PESAJES DE CALIBRE 
                lPesajesCal = obtenerPesajesCalibreIdLinea(linea.getId());
                // Por cada pesaje de calibre, vamos migrandolo al his.
                for (TControlProductoTerminadoPesajesCalibre pesC : lPesajesCal) {
                    pesCalHis = obtenerControlPtPesajesCalibreHisPorId(pesC.getId());
                    if (pesCalHis == null) {
                        pesCalHis = new TControlProductoTerminadoPesajesCalibreHis();
                        pesCalHis.copiaDesdePesajeCalibre(pesC);
                        crearPesajeCalibreHis(pesCalHis);
                    } else {
                        pesCalHis.copiaDesdePesajeCalibre(pesC);
                        modificarPesajeCalibreHis(pesCalHis);
                    }
                    // Eliminamos el pesaje de calibre.
                    eliminarPesajesCalibreControlPtPorId(pesC.getId());
                }

                /// PESAJES DE CONFECCIÓN 
                lPesajesConf = obtenerPesajesConfeccionIdLinea(linea.getId());
                // Por cada pesaje de confección, vamos migrandolo al his.
                for (TControlProductoTerminadoPesajesConfeccion pesC : lPesajesConf) {
                    pesConfHis = obtenerControlPtPesajesConfeccionHisPorId(pesC.getId());
                    if (pesConfHis == null) {
                        pesConfHis = new TControlProductoTerminadoPesajesConfeccionHis();
                        pesConfHis.copiaDesdePesajeConfeccion(pesC);
                        crearPesajeConfeccionHis(pesConfHis);
                    } else {
                        pesConfHis.copiaDesdePesajeConfeccion(pesC);
                        modificarPesajeConfeccionHis(pesConfHis);
                    }
                    // Eliminamos el pesaje de confección.
                    eliminarPesajesConfeccionControlPtPorId(pesC.getId());
                }

                /// PESAJES DE PENETROMÍA 
                lPesajesPen = obtenerPesajesPenetromiaIdLinea(linea.getId());
                // Por cada pesaje de penetromía, vamos migrandolo al his.
                for (TControlProductoTerminadoPesajesPenetromia pesP : lPesajesPen) {
                    pesPenHis = obtenerControlPtPesajesPenetromiaHisPorId(pesP.getId());
                    if (pesPenHis == null) {
                        pesPenHis = new TControlProductoTerminadoPesajesPenetromiaHis();
                        pesPenHis.copiaDesdePesajePenetromia(pesP);
                        crearPesajePenetromiaHis(pesPenHis);
                    } else {
                        pesPenHis.copiaDesdePesajePenetromia(pesP);
                        modificarPesajePenetromiaHis(pesPenHis);
                    }
                    // Eliminamos el pesaje de penetromía.
                    eliminarPesajesPenetromiaControlPtPorId(pesP.getId());
                }

                /// PESAJES DE BRIX 
                lPesajesB = obtenerPesajesBrixIdLinea(linea.getId());
                // Por cada pesaje de brix, vamos migrandolo al his.
                for (TControlProductoTerminadoPesajesBrix pesB : lPesajesB) {
                    pesBrixHis = obtenerControlPtPesajesBrixHisPorId(pesB.getId());
                    if (pesBrixHis == null) {
                        pesBrixHis = new TControlProductoTerminadoPesajesBrixHis();
                        pesBrixHis.copiaDesdePesajeBrix(pesB);
                        crearPesajeBrixHis(pesBrixHis);
                    } else {
                        pesBrixHis.copiaDesdePesajeBrix(pesB);
                        modificarPesajeBrixHis(pesBrixHis);
                    }
                    // Eliminamos el pesaje de brix.
                    eliminarPesajesBrixControlPtPorId(pesB.getId());
                }

                // FOTOS
                lFotos = obtenerImagenesIdLinea(linea.getId());

                for (TControlProductoTerminadoFotos foto : lFotos) {
                    fotHis = obtenerFotoPorId(foto.getId());
                    if (fotHis == null) {
                        fotHis = new TControlProductoTerminadoFotosHis();
                        fotHis.copiaDesdeFoto(foto);
                        crearFotoHis(fotHis);
                    } else {
                        fotHis.copiaDesdeFoto(foto);
                        modificarFotoHis(fotHis);
                    }
                    eliminarFotoControlPtPorId(foto.getId());
                }

                // Migramos la línea de control de PT
                lHis = obtenerLineaHisControlPtPorId(id);
                if (lHis == null) {
                    lHis = new TLineaControlProductoTerminadoHis();
                    lHis.copiaDesdeLinea(linea);
                    crearLineaControlProductoTerminadoHis(lHis);
                } else {
                    lHis.copiaDesdeLinea(linea);
                    modificarLineaControlProductoTerminadoHis(lHis);
                }

                // ELiminamos la línea
                eliminarLineaControlPtPorId(linea.getId());
            }

            // Migramos el control de producto terminado.
            cptHis = obtenerControlPtHisPorId(id);

            if (cptHis == null) {
                cptHis = new TControlProductoTerminadoHis();
                cptHis.copiaDesdeControlPt(cPt);
                crearControlPtHis(cptHis);
            } else {
                cptHis.copiaDesdeControlPt(cPt);
                modificarControlPtHis(cptHis);
            }

            // Eliminamos el registro.
            borrarControlProductoTerminado(id);
        }
    }

}
