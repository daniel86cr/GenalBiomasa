/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 * Copyright (C) 2024
 */
package com.dina.genasoft.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dina.genasoft.db.entity.TLineasVentas;
import com.dina.genasoft.db.entity.TLineasVentas2022;
import com.dina.genasoft.db.entity.TLineasVentas2023;
import com.dina.genasoft.db.entity.TLineasVentas2024;
import com.dina.genasoft.db.entity.TLineasVentas2025;
import com.dina.genasoft.db.entity.TLineasVentas2026;
import com.dina.genasoft.db.entity.TLineasVentas2027;
import com.dina.genasoft.db.entity.TLineasVentas2028;
import com.dina.genasoft.db.entity.TLineasVentas2029;
import com.dina.genasoft.db.entity.TLineasVentas2030;
import com.dina.genasoft.db.mapper.TLineasVentas2022Mapper;
import com.dina.genasoft.db.mapper.TLineasVentas2023Mapper;
import com.dina.genasoft.db.mapper.TLineasVentas2024Mapper;
import com.dina.genasoft.db.mapper.TLineasVentas2025Mapper;
import com.dina.genasoft.db.mapper.TLineasVentas2026Mapper;
import com.dina.genasoft.db.mapper.TLineasVentas2027Mapper;
import com.dina.genasoft.db.mapper.TLineasVentas2028Mapper;
import com.dina.genasoft.db.mapper.TLineasVentas2029Mapper;
import com.dina.genasoft.db.mapper.TLineasVentas2030Mapper;
import com.dina.genasoft.exception.GenasoftException;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de líneas de ventas y la BD, aquí se determina qué tabla(s) se van a utilizar.
 */
@Component
@Slf4j
@Data
public class LineasVentasSetup implements Serializable {
    /**
     * 
     */
    private static final long             serialVersionUID = 6960609490441572131L;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(LineasVentasSetup.class);
    /** Inyección por Spring del mapper TLineasVentas2022Mapper.*/
    @Autowired
    private TLineasVentas2022Mapper       tLineasVentas2022Mapper;
    /** Inyección por Spring del mapper TLineasVentas2023Mapper.*/
    @Autowired
    private TLineasVentas2023Mapper       tLineasVentas2023Mapper;
    /** Inyección por Spring del mapper TLineasVentas2024Mapper.*/
    @Autowired
    private TLineasVentas2024Mapper       tLineasVentas2024Mapper;
    /** Inyección por Spring del mapper TLineasVentas2025Mapper.*/
    @Autowired
    private TLineasVentas2025Mapper       tLineasVentas2025Mapper;
    /** Inyección por Spring del mapper TLineasVentas2026Mapper.*/
    @Autowired
    private TLineasVentas2026Mapper       tLineasVentas2026Mapper;
    /** Inyección por Spring del mapper TLineasVentas2027Mapper.*/
    @Autowired
    private TLineasVentas2027Mapper       tLineasVentas2027Mapper;
    /** Inyección por Spring del mapper TLineasVentas2028Mapper.*/
    @Autowired
    private TLineasVentas2028Mapper       tLineasVentas2028Mapper;
    /** Inyección por Spring del mapper TLineasVentas2029Mapper.*/
    @Autowired
    private TLineasVentas2029Mapper       tLineasVentas2029Mapper;
    /** Inyección por Spring del mapper TLineasVentas2030Mapper.*/
    @Autowired
    private TLineasVentas2030Mapper       tLineasVentas2030Mapper;

    public TLineasVentas obtenerLineaVentaEjercicioPorId(Integer ejercicio, Integer id) throws GenasoftException {
        TLineasVentas result = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");

        } else {

            switch (ejercicio) {
                case 2022:
                    result = tLineasVentas2022Mapper.obtenerLineaVentaPorId(id);
                    break;
                case 2023:
                    result = tLineasVentas2023Mapper.obtenerLineaVentaPorId(id);
                    break;
                case 2024:
                    result = tLineasVentas2024Mapper.obtenerLineaVentaPorId(id);
                    break;
                case 2025:
                    result = tLineasVentas2025Mapper.obtenerLineaVentaPorId(id);
                    break;
                case 2026:
                    result = tLineasVentas2026Mapper.obtenerLineaVentaPorId(id);
                    break;
                case 2027:
                    result = tLineasVentas2027Mapper.obtenerLineaVentaPorId(id);
                    break;
                case 2028:
                    result = tLineasVentas2028Mapper.obtenerLineaVentaPorId(id);
                    break;
                case 2029:
                    result = tLineasVentas2029Mapper.obtenerLineaVentaPorId(id);
                    break;
                case 2030:
                    result = tLineasVentas2030Mapper.obtenerLineaVentaPorId(id);
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return result;
    }

    public TLineasVentas obtenerLineaVentaEjercicioPorIdExterno(Integer ejercicio, Double idExterno) throws GenasoftException {
        TLineasVentas result = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");

        } else {

            switch (ejercicio) {
                case 2022:
                    result = tLineasVentas2022Mapper.obtenerLineaVentaPorIdExterno(idExterno);
                    break;
                case 2023:
                    result = tLineasVentas2023Mapper.obtenerLineaVentaPorIdExterno(idExterno);
                    break;
                case 2024:
                    result = tLineasVentas2024Mapper.obtenerLineaVentaPorIdExterno(idExterno);
                    break;
                case 2025:
                    result = tLineasVentas2025Mapper.obtenerLineaVentaPorIdExterno(idExterno);
                    break;
                case 2026:
                    result = tLineasVentas2026Mapper.obtenerLineaVentaPorIdExterno(idExterno);
                    break;
                case 2027:
                    result = tLineasVentas2027Mapper.obtenerLineaVentaPorIdExterno(idExterno);
                    break;
                case 2028:
                    result = tLineasVentas2028Mapper.obtenerLineaVentaPorIdExterno(idExterno);
                    break;
                case 2029:
                    result = tLineasVentas2029Mapper.obtenerLineaVentaPorIdExterno(idExterno);
                    break;
                case 2030:
                    result = tLineasVentas2030Mapper.obtenerLineaVentaPorIdExterno(idExterno);
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return result;
    }

    public TLineasVentas obtenerLineaVentaBasuraEjercicioIdVentaLoteFin(Integer ejercicio, Double idVentaExt, String loteFin) throws GenasoftException {
        TLineasVentas result = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");

        } else {

            switch (ejercicio) {
                case 2022:
                    result = tLineasVentas2022Mapper.obtenerLineaVentaBasuraIdVentaLoteFin(idVentaExt, loteFin);
                    break;
                case 2023:
                    result = tLineasVentas2023Mapper.obtenerLineaVentaBasuraIdVentaLoteFin(idVentaExt, loteFin);
                    break;
                case 2024:
                    result = tLineasVentas2024Mapper.obtenerLineaVentaBasuraIdVentaLoteFin(idVentaExt, loteFin);
                    break;
                case 2025:
                    result = tLineasVentas2025Mapper.obtenerLineaVentaBasuraIdVentaLoteFin(idVentaExt, loteFin);
                    break;
                case 2026:
                    result = tLineasVentas2026Mapper.obtenerLineaVentaBasuraIdVentaLoteFin(idVentaExt, loteFin);
                    break;
                case 2027:
                    result = tLineasVentas2027Mapper.obtenerLineaVentaBasuraIdVentaLoteFin(idVentaExt, loteFin);
                    break;
                case 2028:
                    result = tLineasVentas2028Mapper.obtenerLineaVentaBasuraIdVentaLoteFin(idVentaExt, loteFin);
                    break;
                case 2029:
                    result = tLineasVentas2029Mapper.obtenerLineaVentaBasuraIdVentaLoteFin(idVentaExt, loteFin);
                    break;
                case 2030:
                    result = tLineasVentas2030Mapper.obtenerLineaVentaBasuraIdVentaLoteFin(idVentaExt, loteFin);
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return result;
    }

    /**
     * Método que nos elimina las  líneas de ventas del ejercicio pasado por parámetro con los ids pasados por parámetro..
     * @param ejercicio El ejercicio a consultar.
     * @param lIdsVentas Los ids de las líneas de ventas a eliminar.
     * @return Los registros eliminados.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer eliminarLineasVentasEjercicioIdsVentas(Integer ejercicio, List<Integer> lIdsVentas) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tLineasVentas2022Mapper.eliminarLineasVentasIdVenta(lIdsVentas);
                break;
            case 2023:
                result = tLineasVentas2023Mapper.eliminarLineasVentasIdVenta(lIdsVentas);
                break;
            case 2024:
                result = tLineasVentas2024Mapper.eliminarLineasVentasIdVenta(lIdsVentas);
                break;
            case 2025:
                result = tLineasVentas2025Mapper.eliminarLineasVentasIdVenta(lIdsVentas);
                break;
            case 2026:
                result = tLineasVentas2026Mapper.eliminarLineasVentasIdVenta(lIdsVentas);
                break;
            case 2027:
                result = tLineasVentas2027Mapper.eliminarLineasVentasIdVenta(lIdsVentas);
                break;
            case 2028:
                result = tLineasVentas2028Mapper.eliminarLineasVentasIdVenta(lIdsVentas);
                break;
            case 2029:
                result = tLineasVentas2029Mapper.eliminarLineasVentasIdVenta(lIdsVentas);
                break;
            case 2030:
                result = tLineasVentas2030Mapper.eliminarLineasVentasIdVenta(lIdsVentas);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos elimina las  líneas de ventas del ejercicio pasado por parámetro con los ids pasados por parámetro..
     * @param ejercicio El ejercicio a consultar.
     * @param lIdsVentas Los ids de las líneas de ventas a eliminar.
     * @return Los registros eliminados.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TLineasVentas> obtenerLineasVentasEjercicioIdsVentas(Integer ejercicio, List<Integer> lIdsVentas) throws GenasoftException {
        List<TLineasVentas> lResult = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tLineasVentas2022Mapper.obtenerLineasVentasIdsVentas(lIdsVentas);
                break;
            case 2023:
                lResult = tLineasVentas2023Mapper.obtenerLineasVentasIdsVentas(lIdsVentas);
                break;
            case 2024:
                lResult = tLineasVentas2024Mapper.obtenerLineasVentasIdsVentas(lIdsVentas);
                break;
            case 2025:
                lResult = tLineasVentas2025Mapper.obtenerLineasVentasIdsVentas(lIdsVentas);
                break;
            case 2026:
                lResult = tLineasVentas2026Mapper.obtenerLineasVentasIdsVentas(lIdsVentas);
                break;
            case 2027:
                lResult = tLineasVentas2027Mapper.obtenerLineasVentasIdsVentas(lIdsVentas);
                break;
            case 2028:
                lResult = tLineasVentas2028Mapper.obtenerLineasVentasIdsVentas(lIdsVentas);
                break;
            case 2029:
                lResult = tLineasVentas2029Mapper.obtenerLineasVentasIdsVentas(lIdsVentas);
                break;
            case 2030:
                lResult = tLineasVentas2030Mapper.obtenerLineasVentasIdsVentas(lIdsVentas);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos elimina las  líneas de ventas del ejercicio pasado por parámetro con los ids pasados por parámetro..
     * @param ejercicio El ejercicio a consultar.
     * @param lIdsVentas Los ids de las líneas de ventas a eliminar.
     * @return Los registros eliminados.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TLineasVentas> obtenerLineasVentasEjercicioLotes(Integer ejercicio, List<String> lIdsVentas) throws GenasoftException {
        List<TLineasVentas> lResult = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tLineasVentas2022Mapper.obtenerLineasVentasLotes(lIdsVentas);
                break;
            case 2023:
                lResult = tLineasVentas2023Mapper.obtenerLineasVentasLotes(lIdsVentas);
                break;
            case 2024:
                lResult = tLineasVentas2024Mapper.obtenerLineasVentasLotes(lIdsVentas);
                break;
            case 2025:
                lResult = tLineasVentas2025Mapper.obtenerLineasVentasLotes(lIdsVentas);
                break;
            case 2026:
                lResult = tLineasVentas2026Mapper.obtenerLineasVentasLotes(lIdsVentas);
                break;
            case 2027:
                lResult = tLineasVentas2027Mapper.obtenerLineasVentasLotes(lIdsVentas);
                break;
            case 2028:
                lResult = tLineasVentas2028Mapper.obtenerLineasVentasLotes(lIdsVentas);
                break;
            case 2029:
                lResult = tLineasVentas2029Mapper.obtenerLineasVentasLotes(lIdsVentas);
                break;
            case 2030:
                lResult = tLineasVentas2030Mapper.obtenerLineasVentasLotes(lIdsVentas);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos retorna las líneas de venta del ejercicio pasado por parámetro y el ID externo de la venta.
     * @param ejercicio El ejercicio de la venta a guardar.
     * @param idExterno El ID externo de la venta.
     * @return Las líneas de venta encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TLineasVentas> obtenerLineasVentasEjercicioIdPedido(Integer ejercicio, Double idExterno) throws GenasoftException {
        List<TLineasVentas> lResult = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tLineasVentas2022Mapper.obtenerLineasVentasIdPedido(idExterno);
                break;
            case 2023:
                lResult = tLineasVentas2023Mapper.obtenerLineasVentasIdPedido(idExterno);
                break;
            case 2024:
                lResult = tLineasVentas2024Mapper.obtenerLineasVentasIdPedido(idExterno);
                break;
            case 2025:
                lResult = tLineasVentas2025Mapper.obtenerLineasVentasIdPedido(idExterno);
                break;
            case 2026:
                lResult = tLineasVentas2026Mapper.obtenerLineasVentasIdPedido(idExterno);
                break;
            case 2027:
                lResult = tLineasVentas2027Mapper.obtenerLineasVentasIdPedido(idExterno);
                break;
            case 2028:
                lResult = tLineasVentas2028Mapper.obtenerLineasVentasIdPedido(idExterno);
                break;
            case 2029:
                lResult = tLineasVentas2029Mapper.obtenerLineasVentasIdPedido(idExterno);
                break;
            case 2030:
                lResult = tLineasVentas2030Mapper.obtenerLineasVentasIdPedido(idExterno);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos retorna las líneas de venta del ejercicio pasado por parámetro y el ID externo de la venta.
     * @param ejercicio El ejercicio de la venta a guardar.
     * @param idExterno El ID externo de la venta.
     * @return Las líneas de venta encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TLineasVentas> obtenerLineasEjercicioVentaSinVenta(Integer ejercicio) throws GenasoftException {
        List<TLineasVentas> lResult = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tLineasVentas2022Mapper.obtenerLineasVentaSinVenta();
                break;
            case 2023:
                lResult = tLineasVentas2023Mapper.obtenerLineasVentaSinVenta();
                break;
            case 2024:
                lResult = tLineasVentas2024Mapper.obtenerLineasVentaSinVenta();
                break;
            case 2025:
                lResult = tLineasVentas2025Mapper.obtenerLineasVentaSinVenta();
                break;
            case 2026:
                lResult = tLineasVentas2026Mapper.obtenerLineasVentaSinVenta();
                break;
            case 2027:
                lResult = tLineasVentas2027Mapper.obtenerLineasVentaSinVenta();
                break;
            case 2028:
                lResult = tLineasVentas2028Mapper.obtenerLineasVentaSinVenta();
                break;
            case 2029:
                lResult = tLineasVentas2029Mapper.obtenerLineasVentaSinVenta();
                break;
            case 2030:
                lResult = tLineasVentas2030Mapper.obtenerLineasVentaSinVenta();
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos crea la línea de venta del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la venta a crear.
     * @param id El id de la línea de venta a crear.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer crearLineaVentaEjercicio(Integer ejercicio, TLineasVentas linea) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                TLineasVentas2022 l22 = new TLineasVentas2022(linea);
                result = tLineasVentas2022Mapper.insert(l22);
                break;
            case 2023:
                TLineasVentas2023 l23 = new TLineasVentas2023(linea);
                result = tLineasVentas2023Mapper.insert(l23);
                break;
            case 2024:
                TLineasVentas2024 l24 = new TLineasVentas2024(linea);
                result = tLineasVentas2024Mapper.insert(l24);
                break;
            case 2025:
                TLineasVentas2025 l25 = new TLineasVentas2025(linea);
                result = tLineasVentas2025Mapper.insert(l25);
                break;
            case 2026:
                TLineasVentas2026 l26 = new TLineasVentas2026(linea);
                result = tLineasVentas2026Mapper.insert(l26);
                break;
            case 2027:
                TLineasVentas2027 l27 = new TLineasVentas2027(linea);
                result = tLineasVentas2027Mapper.insert(l27);
                break;
            case 2028:
                TLineasVentas2028 l28 = new TLineasVentas2028(linea);
                result = tLineasVentas2028Mapper.insert(l28);
                break;
            case 2029:
                TLineasVentas2029 l29 = new TLineasVentas2029(linea);
                result = tLineasVentas2029Mapper.insert(l29);
                break;
            case 2030:
                TLineasVentas2030 l30 = new TLineasVentas2030(linea);
                result = tLineasVentas2030Mapper.insert(l30);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos modifica la línea de venta del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la venta a modificar.
     * @param id El id de la línea de venta a modificar.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer modificarLineaVentaEjercicio(Integer ejercicio, TLineasVentas linea) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                TLineasVentas2022 l22 = new TLineasVentas2022(linea);
                result = tLineasVentas2022Mapper.updateByPrimaryKey(l22);
                break;
            case 2023:
                TLineasVentas2023 l23 = new TLineasVentas2023(linea);
                result = tLineasVentas2023Mapper.updateByPrimaryKey(l23);
                break;
            case 2024:
                TLineasVentas2024 l24 = new TLineasVentas2024(linea);
                result = tLineasVentas2024Mapper.updateByPrimaryKey(l24);
                break;
            case 2025:
                TLineasVentas2025 l25 = new TLineasVentas2025(linea);
                result = tLineasVentas2025Mapper.updateByPrimaryKey(l25);
                break;
            case 2026:
                TLineasVentas2026 l26 = new TLineasVentas2026(linea);
                result = tLineasVentas2026Mapper.updateByPrimaryKey(l26);
                break;
            case 2027:
                TLineasVentas2027 l27 = new TLineasVentas2027(linea);
                result = tLineasVentas2027Mapper.updateByPrimaryKey(l27);
                break;
            case 2028:
                TLineasVentas2028 l28 = new TLineasVentas2028(linea);
                result = tLineasVentas2028Mapper.updateByPrimaryKey(l28);
                break;
            case 2029:
                TLineasVentas2029 l29 = new TLineasVentas2029(linea);
                result = tLineasVentas2029Mapper.updateByPrimaryKey(l29);
                break;
            case 2030:
                TLineasVentas2030 l30 = new TLineasVentas2030(linea);
                result = tLineasVentas2030Mapper.updateByPrimaryKey(l30);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos elimina la línea de venta del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la venta a eliminar.
     * @param id El id de la línea de venta a eliminar.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer eliminarLineaVentaEjercicio(Integer ejercicio, Integer id) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tLineasVentas2022Mapper.deleteByPrimaryKey(id);
                break;
            case 2023:
                result = tLineasVentas2023Mapper.deleteByPrimaryKey(id);
                break;
            case 2024:
                result = tLineasVentas2024Mapper.deleteByPrimaryKey(id);
                break;
            case 2025:
                result = tLineasVentas2025Mapper.deleteByPrimaryKey(id);
                break;
            case 2026:
                result = tLineasVentas2026Mapper.deleteByPrimaryKey(id);
                break;
            case 2027:
                result = tLineasVentas2027Mapper.deleteByPrimaryKey(id);
                break;
            case 2028:
                result = tLineasVentas2028Mapper.deleteByPrimaryKey(id);
                break;
            case 2029:
                result = tLineasVentas2029Mapper.deleteByPrimaryKey(id);
                break;
            case 2030:
                result = tLineasVentas2030Mapper.deleteByPrimaryKey(id);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos guarda la línea venta del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la venta a guardar.
     * @param compra La línea de venta a guardar.
     * @return El ID generado.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer insertRecordEjercicioLinea(Integer ejercicio, Map<String, Object> map) throws GenasoftException {

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                tLineasVentas2022Mapper.insertRecord(map);
                break;
            case 2023:
                tLineasVentas2023Mapper.insertRecord(map);
                break;
            case 2024:
                tLineasVentas2024Mapper.insertRecord(map);
                break;
            case 2025:
                tLineasVentas2025Mapper.insertRecord(map);
                break;
            case 2026:
                tLineasVentas2026Mapper.insertRecord(map);
                break;
            case 2027:
                tLineasVentas2027Mapper.insertRecord(map);
                break;
            case 2028:
                tLineasVentas2028Mapper.insertRecord(map);
                break;
            case 2029:
                tLineasVentas2029Mapper.insertRecord(map);
                break;
            case 2030:
                tLineasVentas2030Mapper.insertRecord(map);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }
        Integer id = (Integer) map.get("id");
        return id;
    }

}
