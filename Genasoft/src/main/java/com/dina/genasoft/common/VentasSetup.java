/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 * Copyright (C) 2024
 */
package com.dina.genasoft.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dina.genasoft.db.entity.TVentas;
import com.dina.genasoft.db.entity.TVentas2022;
import com.dina.genasoft.db.entity.TVentas2023;
import com.dina.genasoft.db.entity.TVentas2024;
import com.dina.genasoft.db.entity.TVentas2025;
import com.dina.genasoft.db.entity.TVentas2026;
import com.dina.genasoft.db.entity.TVentas2027;
import com.dina.genasoft.db.entity.TVentas2028;
import com.dina.genasoft.db.entity.TVentas2029;
import com.dina.genasoft.db.entity.TVentas2030;
import com.dina.genasoft.db.mapper.TVentas2022Mapper;
import com.dina.genasoft.db.mapper.TVentas2023Mapper;
import com.dina.genasoft.db.mapper.TVentas2024Mapper;
import com.dina.genasoft.db.mapper.TVentas2025Mapper;
import com.dina.genasoft.db.mapper.TVentas2026Mapper;
import com.dina.genasoft.db.mapper.TVentas2027Mapper;
import com.dina.genasoft.db.mapper.TVentas2028Mapper;
import com.dina.genasoft.db.mapper.TVentas2029Mapper;
import com.dina.genasoft.db.mapper.TVentas2030Mapper;
import com.dina.genasoft.exception.GenasoftException;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de ventas y la BD, aquí se determina qué tabla(s) se van a utilizar.
 */
@Component
@Slf4j
@Data
public class VentasSetup implements Serializable {
    /**
     * 
     */
    private static final long             serialVersionUID = 6960609490441572131L;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(VentasSetup.class);
    /** Inyección por Spring del mapper TVentas2022Mapper.*/
    @Autowired
    private TVentas2022Mapper             tVentas2022Mapper;
    /** Inyección por Spring del mapper TVentas2023Mapper.*/
    @Autowired
    private TVentas2023Mapper             tVentas2023Mapper;
    /** Inyección por Spring del mapper TVentas2024Mapper.*/
    @Autowired
    private TVentas2024Mapper             tVentas2024Mapper;
    /** Inyección por Spring del mapper TVentas2025Mapper.*/
    @Autowired
    private TVentas2025Mapper             tVentas2025Mapper;
    /** Inyección por Spring del mapper TVentas2026Mapper.*/
    @Autowired
    private TVentas2026Mapper             tVentas2026Mapper;
    /** Inyección por Spring del mapper TVentas2027Mapper.*/
    @Autowired
    private TVentas2027Mapper             tVentas2027Mapper;
    /** Inyección por Spring del mapper TVentas2028Mapper.*/
    @Autowired
    private TVentas2028Mapper             tVentas2028Mapper;
    /** Inyección por Spring del mapper TVentas2029Mapper.*/
    @Autowired
    private TVentas2029Mapper             tVentas2029Mapper;
    /** Inyección por Spring del mapper TVentas2030Mapper.*/
    @Autowired
    private TVentas2030Mapper             tVentas2030Mapper;

    /**
     * Método que nos retorna las ventas del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio a consultar, si ejercicio is null, retorna las ventas de todos los ejercicios.
     * @return Las ventas encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerTodasVentasEjercicio(Integer ejercicio) throws GenasoftException {
        List<TVentas> lResult = null;

        if (ejercicio.equals(-1)) {
            lResult = tVentas2022Mapper.obtenerTodasVentas();
            lResult.addAll(tVentas2023Mapper.obtenerTodasVentas());
            lResult.addAll(tVentas2024Mapper.obtenerTodasVentas());
            lResult.addAll(tVentas2025Mapper.obtenerTodasVentas());
            lResult.addAll(tVentas2026Mapper.obtenerTodasVentas());
            lResult.addAll(tVentas2027Mapper.obtenerTodasVentas());
            lResult.addAll(tVentas2028Mapper.obtenerTodasVentas());
            lResult.addAll(tVentas2029Mapper.obtenerTodasVentas());
            lResult.addAll(tVentas2030Mapper.obtenerTodasVentas());

        } else {

            switch (ejercicio) {
                case 2022:
                    lResult = tVentas2022Mapper.obtenerTodasVentas();
                    break;
                case 2023:
                    lResult = tVentas2023Mapper.obtenerTodasVentas();
                    break;
                case 2024:
                    lResult = tVentas2024Mapper.obtenerTodasVentas();
                    break;
                case 2025:
                    lResult = tVentas2025Mapper.obtenerTodasVentas();
                    break;
                case 2026:
                    lResult = tVentas2026Mapper.obtenerTodasVentas();
                    break;
                case 2027:
                    lResult = tVentas2027Mapper.obtenerTodasVentas();
                    break;
                case 2028:
                    lResult = tVentas2028Mapper.obtenerTodasVentas();
                    break;
                case 2029:
                    lResult = tVentas2029Mapper.obtenerTodasVentas();
                    break;
                case 2030:
                    lResult = tVentas2030Mapper.obtenerTodasVentas();
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return lResult;
    }

    /**
     * Método que nos retorna las ventas del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio a consultar, si ejercicio is null, retorna las ventas de todos los ejercicios.
     * @return Las ventas encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerVentasEjercicioFechas(Integer ejercicio, Date f1, Date f2) throws GenasoftException {
        List<TVentas> lResult = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");

        } else {

            switch (ejercicio) {
                case 2022:
                    lResult = tVentas2022Mapper.obtenerVentasFechas(f1, f2);
                    break;
                case 2023:
                    lResult = tVentas2023Mapper.obtenerVentasFechas(f1, f2);
                    break;
                case 2024:
                    lResult = tVentas2024Mapper.obtenerVentasFechas(f1, f2);
                    break;
                case 2025:
                    lResult = tVentas2025Mapper.obtenerVentasFechas(f1, f2);
                    break;
                case 2026:
                    lResult = tVentas2026Mapper.obtenerVentasFechas(f1, f2);
                    break;
                case 2027:
                    lResult = tVentas2027Mapper.obtenerVentasFechas(f1, f2);
                    break;
                case 2028:
                    lResult = tVentas2028Mapper.obtenerVentasFechas(f1, f2);
                    break;
                case 2029:
                    lResult = tVentas2029Mapper.obtenerVentasFechas(f1, f2);
                    break;
                case 2030:
                    lResult = tVentas2030Mapper.obtenerVentasFechas(f1, f2);
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return lResult;
    }

    /**
     * Método que nos retorna las ventas del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio a consultar
     * @return Las ventas encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerTodasVentasEjercicio2(Integer ejercicio) throws GenasoftException {
        List<TVentas> lResult = null;

        if (ejercicio.equals(-1)) {
            lResult = tVentas2022Mapper.obtenerTodasVentas2();
            lResult.addAll(tVentas2023Mapper.obtenerTodasVentas2());
            lResult.addAll(tVentas2024Mapper.obtenerTodasVentas2());
            lResult.addAll(tVentas2025Mapper.obtenerTodasVentas2());
            lResult.addAll(tVentas2026Mapper.obtenerTodasVentas2());
            lResult.addAll(tVentas2027Mapper.obtenerTodasVentas2());
            lResult.addAll(tVentas2028Mapper.obtenerTodasVentas2());
            lResult.addAll(tVentas2029Mapper.obtenerTodasVentas2());
            lResult.addAll(tVentas2030Mapper.obtenerTodasVentas2());

        } else {

            switch (ejercicio) {
                case 2022:
                    lResult = tVentas2022Mapper.obtenerTodasVentas2();
                    break;
                case 2023:
                    lResult = tVentas2023Mapper.obtenerTodasVentas2();
                    break;
                case 2024:
                    lResult = tVentas2024Mapper.obtenerTodasVentas2();
                    break;
                case 2025:
                    lResult = tVentas2025Mapper.obtenerTodasVentas2();
                    break;
                case 2026:
                    lResult = tVentas2026Mapper.obtenerTodasVentas2();
                    break;
                case 2027:
                    lResult = tVentas2027Mapper.obtenerTodasVentas2();
                    break;
                case 2028:
                    lResult = tVentas2028Mapper.obtenerTodasVentas2();
                    break;
                case 2029:
                    lResult = tVentas2029Mapper.obtenerTodasVentas2();
                    break;
                case 2030:
                    lResult = tVentas2030Mapper.obtenerTodasVentas2();
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return lResult;
    }

    /**
     * Método que nos retorna las ventas del ejercicio pasado por parámetro y el lote fin.
     * @param ejercicio El ejercicio a consultar.
     * @param loteFin El lote fin por el que realizar la consulta.
     * @return Las ventas encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerVentasEjercicioLoteFin(Integer ejercicio, String loteFin) throws GenasoftException {
        List<TVentas> lResult = null;

        if (ejercicio.equals(-1)) {
            lResult = tVentas2022Mapper.obtenerVentasLoteFin(loteFin);
            lResult.addAll(tVentas2023Mapper.obtenerVentasLoteFin(loteFin));
            lResult.addAll(tVentas2024Mapper.obtenerVentasLoteFin(loteFin));
            lResult.addAll(tVentas2025Mapper.obtenerVentasLoteFin(loteFin));
            lResult.addAll(tVentas2026Mapper.obtenerVentasLoteFin(loteFin));
            lResult.addAll(tVentas2027Mapper.obtenerVentasLoteFin(loteFin));
            lResult.addAll(tVentas2028Mapper.obtenerVentasLoteFin(loteFin));
            lResult.addAll(tVentas2029Mapper.obtenerVentasLoteFin(loteFin));
            lResult.addAll(tVentas2030Mapper.obtenerVentasLoteFin(loteFin));

        } else {

            switch (ejercicio) {
                case 2022:
                    lResult = tVentas2022Mapper.obtenerVentasLoteFin(loteFin);
                    break;
                case 2023:
                    lResult = tVentas2023Mapper.obtenerVentasLoteFin(loteFin);
                    break;
                case 2024:
                    lResult = tVentas2024Mapper.obtenerVentasLoteFin(loteFin);
                    break;
                case 2025:
                    lResult = tVentas2025Mapper.obtenerVentasLoteFin(loteFin);
                    break;
                case 2026:
                    lResult = tVentas2026Mapper.obtenerVentasLoteFin(loteFin);
                    break;
                case 2027:
                    lResult = tVentas2027Mapper.obtenerVentasLoteFin(loteFin);
                    break;
                case 2028:
                    lResult = tVentas2028Mapper.obtenerVentasLoteFin(loteFin);
                    break;
                case 2029:
                    lResult = tVentas2029Mapper.obtenerVentasLoteFin(loteFin);
                    break;
                case 2030:
                    lResult = tVentas2030Mapper.obtenerVentasLoteFin(loteFin);
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return lResult;
    }

    /**
     * Método que nos retorna las ventas del ejercicio pasado por parámetro y el lote fin.
     * @param ejercicio El ejercicio a consultar.
     * @param numPedido El número de pedido por el que realizar la consulta.
     * @param albaranCompra El albarán de pedido por el que realizar la consulta.
     * @param loteFin El lote fin por el que realizar la consulta.
     * @return Las ventas encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public TVentas obtenerVentaEjercicioNumPedidoAlbaranCompraLote(Integer ejercicio, String numPedido, String albaranCompra, String loteFin) throws GenasoftException {
        TVentas result = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");

        } else {

            switch (ejercicio) {
                case 2022:
                    result = tVentas2022Mapper.obtenerVentaNumPedidoAlbaranCompraLote(numPedido, albaranCompra, loteFin);
                    break;
                case 2023:
                    result = tVentas2023Mapper.obtenerVentaNumPedidoAlbaranCompraLote(numPedido, albaranCompra, loteFin);
                    break;
                case 2024:
                    result = tVentas2024Mapper.obtenerVentaNumPedidoAlbaranCompraLote(numPedido, albaranCompra, loteFin);
                    break;
                case 2025:
                    result = tVentas2025Mapper.obtenerVentaNumPedidoAlbaranCompraLote(numPedido, albaranCompra, loteFin);
                    break;
                case 2026:
                    result = tVentas2026Mapper.obtenerVentaNumPedidoAlbaranCompraLote(numPedido, albaranCompra, loteFin);
                    break;
                case 2027:
                    result = tVentas2027Mapper.obtenerVentaNumPedidoAlbaranCompraLote(numPedido, albaranCompra, loteFin);
                    break;
                case 2028:
                    result = tVentas2028Mapper.obtenerVentaNumPedidoAlbaranCompraLote(numPedido, albaranCompra, loteFin);
                    break;
                case 2029:
                    result = tVentas2029Mapper.obtenerVentaNumPedidoAlbaranCompraLote(numPedido, albaranCompra, loteFin);
                    break;
                case 2030:
                    result = tVentas2030Mapper.obtenerVentaNumPedidoAlbaranCompraLote(numPedido, albaranCompra, loteFin);
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return result;
    }

    /**
     * Método que nos abre las ventas del ejercicio pasado por parámetro con los ids pasados por parámetro..
     * @param ejercicio El ejercicio a consultar.
     * @param lIdsVentas Los ids de las ventas a abrir.
     * @return Los registros abiertos.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer abrirVentasEjercicioIdsVentas(Integer ejercicio, List<Integer> lIdsVentas) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tVentas2022Mapper.abrirVentasIdsVentas(lIdsVentas);
                break;
            case 2023:
                result = tVentas2023Mapper.abrirVentasIdsVentas(lIdsVentas);
                break;
            case 2024:
                result = tVentas2024Mapper.abrirVentasIdsVentas(lIdsVentas);
                break;
            case 2025:
                result = tVentas2025Mapper.abrirVentasIdsVentas(lIdsVentas);
                break;
            case 2026:
                result = tVentas2026Mapper.abrirVentasIdsVentas(lIdsVentas);
                break;
            case 2027:
                result = tVentas2027Mapper.abrirVentasIdsVentas(lIdsVentas);
                break;
            case 2028:
                result = tVentas2028Mapper.abrirVentasIdsVentas(lIdsVentas);
                break;
            case 2029:
                result = tVentas2029Mapper.abrirVentasIdsVentas(lIdsVentas);
                break;
            case 2030:
                result = tVentas2030Mapper.abrirVentasIdsVentas(lIdsVentas);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos elimina las ventas del ejercicio pasado por parámetro con los ids pasados por parámetro..
     * @param ejercicio El ejercicio a consultar.
     * @param lIdsVentas Los ids de las ventas a eliminar.
     * @return Los registros eliminados.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer eliminarVentasEjercicioIdsVentas(Integer ejercicio, List<Integer> lIdsVentas) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tVentas2022Mapper.eliminarVentasIds(lIdsVentas);
                break;
            case 2023:
                result = tVentas2023Mapper.eliminarVentasIds(lIdsVentas);
                break;
            case 2024:
                result = tVentas2024Mapper.eliminarVentasIds(lIdsVentas);
                break;
            case 2025:
                result = tVentas2025Mapper.eliminarVentasIds(lIdsVentas);
                break;
            case 2026:
                result = tVentas2026Mapper.eliminarVentasIds(lIdsVentas);
                break;
            case 2027:
                result = tVentas2027Mapper.eliminarVentasIds(lIdsVentas);
                break;
            case 2028:
                result = tVentas2028Mapper.eliminarVentasIds(lIdsVentas);
                break;
            case 2029:
                result = tVentas2029Mapper.eliminarVentasIds(lIdsVentas);
                break;
            case 2030:
                result = tVentas2030Mapper.eliminarVentasIds(lIdsVentas);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos retorna los número de albarán del ejercicio e Ids pasados por parámetro.
     * @param ejercicio El ejercicio a consultar.
     * @param lIdsVentas Los ids de las ventas a consultar.
     * @return Los nº de albarán encontrados..
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<String> obtenerNumAlbaranEjercicioIdsPedidos(Integer ejercicio, List<Integer> lIdsVentas) throws GenasoftException {
        List<String> lResult = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tVentas2022Mapper.obtenerNumAlbaranIdsPedidos(lIdsVentas);
                break;
            case 2023:
                lResult = tVentas2023Mapper.obtenerNumAlbaranIdsPedidos(lIdsVentas);
                break;
            case 2024:
                lResult = tVentas2024Mapper.obtenerNumAlbaranIdsPedidos(lIdsVentas);
                break;
            case 2025:
                lResult = tVentas2025Mapper.obtenerNumAlbaranIdsPedidos(lIdsVentas);
                break;
            case 2026:
                lResult = tVentas2026Mapper.obtenerNumAlbaranIdsPedidos(lIdsVentas);
                break;
            case 2027:
                lResult = tVentas2027Mapper.obtenerNumAlbaranIdsPedidos(lIdsVentas);
                break;
            case 2028:
                lResult = tVentas2028Mapper.obtenerNumAlbaranIdsPedidos(lIdsVentas);
                break;
            case 2029:
                lResult = tVentas2029Mapper.obtenerNumAlbaranIdsPedidos(lIdsVentas);
                break;
            case 2030:
                lResult = tVentas2030Mapper.obtenerNumAlbaranIdsPedidos(lIdsVentas);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos guarda la venta del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la venta a guardar.
     * @param venta La venta a guardar.
     * @return El nº de registros modificados.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer modificarVentaEjercicio(Integer ejercicio, TVentas venta) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                TVentas2022 v22 = new TVentas2022(venta);
                result = tVentas2022Mapper.updateByPrimaryKey(v22);
                break;
            case 2023:
                TVentas2023 v23 = new TVentas2023(venta);
                result = tVentas2023Mapper.updateByPrimaryKey(v23);
                break;
            case 2024:
                TVentas2024 v24 = new TVentas2024(venta);
                result = tVentas2024Mapper.updateByPrimaryKey(v24);
                break;
            case 2025:
                TVentas2025 v25 = new TVentas2025(venta);
                result = tVentas2025Mapper.updateByPrimaryKey(v25);
                break;
            case 2026:
                TVentas2026 v26 = new TVentas2026(venta);
                result = tVentas2026Mapper.updateByPrimaryKey(v26);
                break;
            case 2027:
                TVentas2027 v27 = new TVentas2027(venta);
                result = tVentas2027Mapper.updateByPrimaryKey(v27);
                break;
            case 2028:
                TVentas2028 v28 = new TVentas2028(venta);
                result = tVentas2028Mapper.updateByPrimaryKey(v28);
                break;
            case 2029:
                TVentas2029 v29 = new TVentas2029(venta);
                result = tVentas2029Mapper.updateByPrimaryKey(v29);
                break;
            case 2030:
                TVentas2030 v30 = new TVentas2030(venta);
                result = tVentas2030Mapper.updateByPrimaryKey(v30);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos retorna la venta del ejercicio pasado por parámetro y los diferentes parametros.
     * @param ejercicio El ejercicio de la venta a guardar.
     * @param idPedido El ID del pedido.
     * @param albaran el albarán.
     * @param lote el lote.
     * @param lote el lote fin.
     * @param idPale el ID del palé.
     * @return La venta encontrada.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public TVentas obtenerVentaEjercicioNumPedidoAlbaranLoteIdPale(Integer ejercicio, String idPedido, String albaran, String lote, String loteFin, String idPale) throws GenasoftException {
        TVentas result = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tVentas2022Mapper.obtenerVentaNumPedidoAlbaranLoteIdPale(idPedido, albaran, lote, loteFin, idPale);
                break;
            case 2023:
                result = tVentas2023Mapper.obtenerVentaNumPedidoAlbaranLoteIdPale(idPedido, albaran, lote, loteFin, idPale);
                break;
            case 2024:
                result = tVentas2024Mapper.obtenerVentaNumPedidoAlbaranLoteIdPale(idPedido, albaran, lote, loteFin, idPale);
                break;
            case 2025:
                result = tVentas2025Mapper.obtenerVentaNumPedidoAlbaranLoteIdPale(idPedido, albaran, lote, loteFin, idPale);
                break;
            case 2026:
                result = tVentas2026Mapper.obtenerVentaNumPedidoAlbaranLoteIdPale(idPedido, albaran, lote, loteFin, idPale);
                break;
            case 2027:
                result = tVentas2027Mapper.obtenerVentaNumPedidoAlbaranLoteIdPale(idPedido, albaran, lote, loteFin, idPale);
                break;
            case 2028:
                result = tVentas2028Mapper.obtenerVentaNumPedidoAlbaranLoteIdPale(idPedido, albaran, lote, loteFin, idPale);
                break;
            case 2029:
                result = tVentas2029Mapper.obtenerVentaNumPedidoAlbaranLoteIdPale(idPedido, albaran, lote, loteFin, idPale);
                break;
            case 2030:
                result = tVentas2030Mapper.obtenerVentaNumPedidoAlbaranLoteIdPale(idPedido, albaran, lote, loteFin, idPale);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos retorna la venta del ejercicio pasado por parámetro y el ID externo.
     * @param ejercicio El ejercicio de la venta a guardar.
     * @param idExterno El ID externo del pedido.
     * @return La venta encontrada.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public TVentas obtenerVentaEjercicioPorIdExterno(Integer ejercicio, Double idExterno) throws GenasoftException {
        TVentas result = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tVentas2022Mapper.obtenerVentaPorIdExterno(idExterno);
                break;
            case 2023:
                result = tVentas2023Mapper.obtenerVentaPorIdExterno(idExterno);
                break;
            case 2024:
                result = tVentas2024Mapper.obtenerVentaPorIdExterno(idExterno);
                break;
            case 2025:
                result = tVentas2025Mapper.obtenerVentaPorIdExterno(idExterno);
                break;
            case 2026:
                result = tVentas2026Mapper.obtenerVentaPorIdExterno(idExterno);
                break;
            case 2027:
                result = tVentas2027Mapper.obtenerVentaPorIdExterno(idExterno);
                break;
            case 2028:
                result = tVentas2028Mapper.obtenerVentaPorIdExterno(idExterno);
                break;
            case 2029:
                result = tVentas2029Mapper.obtenerVentaPorIdExterno(idExterno);
                break;
            case 2030:
                result = tVentas2030Mapper.obtenerVentaPorIdExterno(idExterno);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos guarda la venta del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la venta a guardar.
     * @param venta La venta a guardar.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer crearVentaEjercicio(Integer ejercicio, TVentas venta) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                TVentas2022 v22 = new TVentas2022(venta);
                result = tVentas2022Mapper.insert(v22);
                break;
            case 2023:
                TVentas2023 v23 = new TVentas2023(venta);
                result = tVentas2023Mapper.insert(v23);
                break;
            case 2024:
                TVentas2024 v24 = new TVentas2024(venta);
                result = tVentas2024Mapper.insert(v24);
                break;
            case 2025:
                TVentas2025 v25 = new TVentas2025(venta);
                result = tVentas2025Mapper.insert(v25);
                break;
            case 2026:
                TVentas2026 v26 = new TVentas2026(venta);
                result = tVentas2026Mapper.insert(v26);
                break;
            case 2027:
                TVentas2027 v27 = new TVentas2027(venta);
                result = tVentas2027Mapper.insert(v27);
                break;
            case 2028:
                TVentas2028 v28 = new TVentas2028(venta);
                result = tVentas2028Mapper.insert(v28);
                break;
            case 2029:
                TVentas2029 v29 = new TVentas2029(venta);
                result = tVentas2029Mapper.insert(v29);
                break;
            case 2030:
                TVentas2030 v30 = new TVentas2030(venta);
                result = tVentas2030Mapper.insert(v30);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos retorna las ventas del ejercicio indicado y los ids pasado por parámetro.
     * @param ejercicio El ejericio para realizar la consulta.
     * @param lIdsVentas Los Ids de las ventas a buscar
     * @return Las ventas encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerVentasEjercicioIds(Integer ejercicio, List<Integer> lIdsVentas) throws GenasoftException {
        List<TVentas> lResult = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tVentas2022Mapper.obtenerVentasIds(lIdsVentas);
                break;
            case 2023:
                lResult = tVentas2023Mapper.obtenerVentasIds(lIdsVentas);
                break;
            case 2024:
                lResult = tVentas2024Mapper.obtenerVentasIds(lIdsVentas);
                break;
            case 2025:
                lResult = tVentas2025Mapper.obtenerVentasIds(lIdsVentas);
                break;
            case 2026:
                lResult = tVentas2026Mapper.obtenerVentasIds(lIdsVentas);
                break;
            case 2027:
                lResult = tVentas2027Mapper.obtenerVentasIds(lIdsVentas);
                break;
            case 2028:
                lResult = tVentas2028Mapper.obtenerVentasIds(lIdsVentas);
                break;
            case 2029:
                lResult = tVentas2029Mapper.obtenerVentasIds(lIdsVentas);
                break;
            case 2030:
                lResult = tVentas2030Mapper.obtenerVentasIds(lIdsVentas);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos retorna las ventas del ejercicio indicado y el albarán.
     * @param ejercicio El ejericio para realizar la consulta.
     * @param albaran El albarán de la venta a buscar
     * @return Las ventas encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerLineasVentasEjercicio(Integer ejercicio, String albaran) throws GenasoftException {
        List<TVentas> lResult = null;

        if (ejercicio.equals(-1)) {
            lResult = tVentas2022Mapper.obtenerLineasVentaAlbaran(albaran);
            lResult.addAll(tVentas2023Mapper.obtenerLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2024Mapper.obtenerLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2025Mapper.obtenerLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2026Mapper.obtenerLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2027Mapper.obtenerLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2028Mapper.obtenerLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2029Mapper.obtenerLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2030Mapper.obtenerLineasVentaAlbaran(albaran));

        }

        switch (ejercicio) {
            case 2022:
                lResult = tVentas2022Mapper.obtenerLineasVentaAlbaran(albaran);
                break;
            case 2023:
                lResult = tVentas2023Mapper.obtenerLineasVentaAlbaran(albaran);
                break;
            case 2024:
                lResult = tVentas2024Mapper.obtenerLineasVentaAlbaran(albaran);
                break;
            case 2025:
                lResult = tVentas2025Mapper.obtenerLineasVentaAlbaran(albaran);
                break;
            case 2026:
                lResult = tVentas2026Mapper.obtenerLineasVentaAlbaran(albaran);
                break;
            case 2027:
                lResult = tVentas2027Mapper.obtenerLineasVentaAlbaran(albaran);
                break;
            case 2028:
                lResult = tVentas2028Mapper.obtenerLineasVentaAlbaran(albaran);
                break;
            case 2029:
                lResult = tVentas2029Mapper.obtenerLineasVentaAlbaran(albaran);
                break;
            case 2030:
                lResult = tVentas2030Mapper.obtenerLineasVentaAlbaran(albaran);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos retorna las ventas del ejercicio indicado y el albarán.
     * @param ejercicio El ejericio para realizar la consulta.
     * @param albaran El albarán de la venta a buscar
     * @return Las líneas de venta encontradas. Éstas se hace un GROUP BY de familia_fin, variedad_fin, calibre_fin
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerTodasLineasVentasEjercicio(Integer ejercicio, String albaran) throws GenasoftException {
        List<TVentas> lResult = null;

        if (ejercicio.equals(-1)) {
            lResult = tVentas2022Mapper.obtenerTodasLineasVentaAlbaran(albaran);
            lResult.addAll(tVentas2023Mapper.obtenerTodasLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2024Mapper.obtenerTodasLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2025Mapper.obtenerTodasLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2026Mapper.obtenerTodasLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2027Mapper.obtenerTodasLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2028Mapper.obtenerTodasLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2029Mapper.obtenerTodasLineasVentaAlbaran(albaran));
            lResult.addAll(tVentas2030Mapper.obtenerTodasLineasVentaAlbaran(albaran));

        }

        switch (ejercicio) {
            case 2022:
                lResult = tVentas2022Mapper.obtenerTodasLineasVentaAlbaran(albaran);
                break;
            case 2023:
                lResult = tVentas2023Mapper.obtenerTodasLineasVentaAlbaran(albaran);
                break;
            case 2024:
                lResult = tVentas2024Mapper.obtenerTodasLineasVentaAlbaran(albaran);
                break;
            case 2025:
                lResult = tVentas2025Mapper.obtenerTodasLineasVentaAlbaran(albaran);
                break;
            case 2026:
                lResult = tVentas2026Mapper.obtenerTodasLineasVentaAlbaran(albaran);
                break;
            case 2027:
                lResult = tVentas2027Mapper.obtenerTodasLineasVentaAlbaran(albaran);
                break;
            case 2028:
                lResult = tVentas2028Mapper.obtenerTodasLineasVentaAlbaran(albaran);
                break;
            case 2029:
                lResult = tVentas2029Mapper.obtenerTodasLineasVentaAlbaran(albaran);
                break;
            case 2030:
                lResult = tVentas2030Mapper.obtenerTodasLineasVentaAlbaran(albaran);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos guarda la venta del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la venta a guardar.
     * @param compra La venta a guardar.
     * @return El ID generado.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer insertRecordEjercicioVenta(Integer ejercicio, Map<String, Object> map) throws GenasoftException {

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                tVentas2022Mapper.insertRecord(map);
                break;
            case 2023:
                tVentas2023Mapper.insertRecord(map);
                break;
            case 2024:
                tVentas2024Mapper.insertRecord(map);
                break;
            case 2025:
                tVentas2025Mapper.insertRecord(map);
                break;
            case 2026:
                tVentas2026Mapper.insertRecord(map);
                break;
            case 2027:
                tVentas2027Mapper.insertRecord(map);
                break;
            case 2028:
                tVentas2028Mapper.insertRecord(map);
                break;
            case 2029:
                tVentas2029Mapper.insertRecord(map);
                break;
            case 2030:
                tVentas2030Mapper.insertRecord(map);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }
        Integer id = (Integer) map.get("id");
        return id;
    }

    /**
     * Método que nos elimina la venta del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la venta a eliminar.
     * @param id El id de la venta a eliminar.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer eliminarVentaEjercicio(Integer ejercicio, Integer id) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tVentas2022Mapper.deleteByPrimaryKey(id);
                break;
            case 2023:
                result = tVentas2023Mapper.deleteByPrimaryKey(id);
                break;
            case 2024:
                result = tVentas2024Mapper.deleteByPrimaryKey(id);
                break;
            case 2025:
                result = tVentas2025Mapper.deleteByPrimaryKey(id);
                break;
            case 2026:
                result = tVentas2026Mapper.deleteByPrimaryKey(id);
                break;
            case 2027:
                result = tVentas2027Mapper.deleteByPrimaryKey(id);
                break;
            case 2028:
                result = tVentas2028Mapper.deleteByPrimaryKey(id);
                break;
            case 2029:
                result = tVentas2029Mapper.deleteByPrimaryKey(id);
                break;
            case 2030:
                result = tVentas2030Mapper.deleteByPrimaryKey(id);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos busca la venta del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la venta a consultar.
     * @param id El id de la venta a buscar.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public TVentas obtenerVentaEjercicioPorId(Integer ejercicio, Integer id) throws GenasoftException {
        TVentas result = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tVentas2022Mapper.obtenerVentaPorId(id);
                break;
            case 2023:
                result = tVentas2023Mapper.obtenerVentaPorId(id);
                break;
            case 2024:
                result = tVentas2024Mapper.obtenerVentaPorId(id);
                break;
            case 2025:
                result = tVentas2025Mapper.obtenerVentaPorId(id);
                break;
            case 2026:
                result = tVentas2026Mapper.obtenerVentaPorId(id);
                break;
            case 2027:
                result = tVentas2027Mapper.obtenerVentaPorId(id);
                break;
            case 2028:
                result = tVentas2028Mapper.obtenerVentaPorId(id);
                break;
            case 2029:
                result = tVentas2029Mapper.obtenerVentaPorId(id);
                break;
            case 2030:
                result = tVentas2030Mapper.obtenerVentaPorId(id);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos busca la ventas para mostrar el balance de masas.
     * @param ejercicio El ejercicio de la venta a consultar.
     * @return Las ventas para mostrar el balance de masas
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerVentasEjercicioBalanceMasasTodosGroupBy(Integer ejercicio) throws GenasoftException {
        List<TVentas> lResult = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tVentas2022Mapper.obtenerVentasBalanceMasasTodosGroupBy();
                break;
            case 2023:
                lResult = tVentas2023Mapper.obtenerVentasBalanceMasasTodosGroupBy();
                break;
            case 2024:
                lResult = tVentas2024Mapper.obtenerVentasBalanceMasasTodosGroupBy();
                break;
            case 2025:
                lResult = tVentas2025Mapper.obtenerVentasBalanceMasasTodosGroupBy();
                break;
            case 2026:
                lResult = tVentas2026Mapper.obtenerVentasBalanceMasasTodosGroupBy();
                break;
            case 2027:
                lResult = tVentas2027Mapper.obtenerVentasBalanceMasasTodosGroupBy();
                break;
            case 2028:
                lResult = tVentas2028Mapper.obtenerVentasBalanceMasasTodosGroupBy();
                break;
            case 2029:
                lResult = tVentas2029Mapper.obtenerVentasBalanceMasasTodosGroupBy();
                break;
            case 2030:
                lResult = tVentas2030Mapper.obtenerVentasBalanceMasasTodosGroupBy();
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos busca la ventas para mostrar el balance de masas.
     * @param ejercicio El ejercicio de la venta a consultar.
     * @return Las ventas para mostrar el balance de masas
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerVentasEjercicioCertificacionNacionalImportacion(Integer ejercicio) throws GenasoftException {
        List<TVentas> lResult = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tVentas2022Mapper.obtenerVentasCertificacionNacionalImportacion();
                break;
            case 2023:
                lResult = tVentas2023Mapper.obtenerVentasCertificacionNacionalImportacion();
                break;
            case 2024:
                lResult = tVentas2024Mapper.obtenerVentasCertificacionNacionalImportacion();
                break;
            case 2025:
                lResult = tVentas2025Mapper.obtenerVentasCertificacionNacionalImportacion();
                break;
            case 2026:
                lResult = tVentas2026Mapper.obtenerVentasCertificacionNacionalImportacion();
                break;
            case 2027:
                lResult = tVentas2027Mapper.obtenerVentasCertificacionNacionalImportacion();
                break;
            case 2028:
                lResult = tVentas2028Mapper.obtenerVentasCertificacionNacionalImportacion();
                break;
            case 2029:
                lResult = tVentas2029Mapper.obtenerVentasCertificacionNacionalImportacion();
                break;
            case 2030:
                lResult = tVentas2030Mapper.obtenerVentasCertificacionNacionalImportacion();
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos busca la ventas para mostrar el balance de masas.
     * @param ejercicio El ejercicio de la venta a consultar.
     * @return Las ventas para mostrar el balance de masas
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerVentasEjercicioGlobalGap(Integer ejercicio) throws GenasoftException {
        List<TVentas> lResult = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tVentas2022Mapper.obtenerVentasGlobalGap();
                break;
            case 2023:
                lResult = tVentas2023Mapper.obtenerVentasGlobalGap();
                break;
            case 2024:
                lResult = tVentas2024Mapper.obtenerVentasGlobalGap();
                break;
            case 2025:
                lResult = tVentas2025Mapper.obtenerVentasGlobalGap();
                break;
            case 2026:
                lResult = tVentas2026Mapper.obtenerVentasGlobalGap();
                break;
            case 2027:
                lResult = tVentas2027Mapper.obtenerVentasGlobalGap();
                break;
            case 2028:
                lResult = tVentas2028Mapper.obtenerVentasGlobalGap();
                break;
            case 2029:
                lResult = tVentas2029Mapper.obtenerVentasGlobalGap();
                break;
            case 2030:
                lResult = tVentas2030Mapper.obtenerVentasGlobalGap();
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos busca los kgs de las ventas.
     * @param ejercicio El ejercicio de la ventas ventas a consultar.
     * @return Los kgs de ventas encontrados.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Double obtenerKgsLoteEjercicio(Integer ejercicio, String lote) throws GenasoftException {
        Double result = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tVentas2022Mapper.obtenerKgsLote(lote);
                break;
            case 2023:
                result = tVentas2023Mapper.obtenerKgsLote(lote);
                break;
            case 2024:
                result = tVentas2024Mapper.obtenerKgsLote(lote);
                break;
            case 2025:
                result = tVentas2025Mapper.obtenerKgsLote(lote);
                break;
            case 2026:
                result = tVentas2026Mapper.obtenerKgsLote(lote);
                break;
            case 2027:
                result = tVentas2027Mapper.obtenerKgsLote(lote);
                break;
            case 2028:
                result = tVentas2028Mapper.obtenerKgsLote(lote);
                break;
            case 2029:
                result = tVentas2029Mapper.obtenerKgsLote(lote);
                break;
            case 2030:
                result = tVentas2030Mapper.obtenerKgsLote(lote);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos busca la ventas para mostrar el balance de masas.
     * @param ejercicio El ejercicio de la venta a consultar.
     * @return Las ventas para mostrar el balance de masas
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerVentasEjercicioProductoBultosKg(Integer ejercicio, String producto, String variedad, Integer bultos, Double kgs, String calidad, Date fecha, Date fecha2) throws GenasoftException {
        List<TVentas> lResult = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tVentas2022Mapper.obtenerVentasProductoBultosKg(producto, variedad, bultos, kgs, calidad, fecha, fecha2);
                break;
            case 2023:
                lResult = tVentas2023Mapper.obtenerVentasProductoBultosKg(producto, variedad, bultos, kgs, calidad, fecha, fecha2);
                break;
            case 2024:
                lResult = tVentas2024Mapper.obtenerVentasProductoBultosKg(producto, variedad, bultos, kgs, calidad, fecha, fecha2);
                break;
            case 2025:
                lResult = tVentas2025Mapper.obtenerVentasProductoBultosKg(producto, variedad, bultos, kgs, calidad, fecha, fecha2);
                break;
            case 2026:
                lResult = tVentas2026Mapper.obtenerVentasProductoBultosKg(producto, variedad, bultos, kgs, calidad, fecha, fecha2);
                break;
            case 2027:
                lResult = tVentas2027Mapper.obtenerVentasProductoBultosKg(producto, variedad, bultos, kgs, calidad, fecha, fecha2);
                break;
            case 2028:
                lResult = tVentas2028Mapper.obtenerVentasProductoBultosKg(producto, variedad, bultos, kgs, calidad, fecha, fecha2);
                break;
            case 2029:
                lResult = tVentas2029Mapper.obtenerVentasProductoBultosKg(producto, variedad, bultos, kgs, calidad, fecha, fecha2);
                break;
            case 2030:
                lResult = tVentas2030Mapper.obtenerVentasProductoBultosKg(producto, variedad, bultos, kgs, calidad, fecha, fecha2);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos busca la ventas para mostrar el balance de masas.
     * @param ejercicio El ejercicio de la venta a consultar.
     * @return Las ventas para mostrar el balance de masas
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TVentas> obtenerVentasProductoKg(Integer ejercicio, String producto, String variedad, Double kgs, String calidad, Date fecha, Date fecha2) throws GenasoftException {
        List<TVentas> lResult = null;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tVentas2022Mapper.obtenerVentasProductoKg(producto, variedad, kgs, calidad, fecha, fecha2);
                break;
            case 2023:
                lResult = tVentas2023Mapper.obtenerVentasProductoKg(producto, variedad, kgs, calidad, fecha, fecha2);
                break;
            case 2024:
                lResult = tVentas2024Mapper.obtenerVentasProductoKg(producto, variedad, kgs, calidad, fecha, fecha2);
                break;
            case 2025:
                lResult = tVentas2025Mapper.obtenerVentasProductoKg(producto, variedad, kgs, calidad, fecha, fecha2);
                break;
            case 2026:
                lResult = tVentas2026Mapper.obtenerVentasProductoKg(producto, variedad, kgs, calidad, fecha, fecha2);
                break;
            case 2027:
                lResult = tVentas2027Mapper.obtenerVentasProductoKg(producto, variedad, kgs, calidad, fecha, fecha2);
                break;
            case 2028:
                lResult = tVentas2028Mapper.obtenerVentasProductoKg(producto, variedad, kgs, calidad, fecha, fecha2);
                break;
            case 2029:
                lResult = tVentas2029Mapper.obtenerVentasProductoKg(producto, variedad, kgs, calidad, fecha, fecha2);
                break;
            case 2030:
                lResult = tVentas2030Mapper.obtenerVentasProductoKg(producto, variedad, kgs, calidad, fecha, fecha2);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }
}
