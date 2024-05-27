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

import com.dina.genasoft.db.entity.TCompras;
import com.dina.genasoft.db.entity.TCompras2022;
import com.dina.genasoft.db.entity.TCompras2023;
import com.dina.genasoft.db.entity.TCompras2024;
import com.dina.genasoft.db.entity.TCompras2025;
import com.dina.genasoft.db.entity.TCompras2026;
import com.dina.genasoft.db.entity.TCompras2027;
import com.dina.genasoft.db.entity.TCompras2028;
import com.dina.genasoft.db.entity.TCompras2029;
import com.dina.genasoft.db.entity.TCompras2030;
import com.dina.genasoft.db.mapper.TCompras2022Mapper;
import com.dina.genasoft.db.mapper.TCompras2023Mapper;
import com.dina.genasoft.db.mapper.TCompras2024Mapper;
import com.dina.genasoft.db.mapper.TCompras2025Mapper;
import com.dina.genasoft.db.mapper.TCompras2026Mapper;
import com.dina.genasoft.db.mapper.TCompras2027Mapper;
import com.dina.genasoft.db.mapper.TCompras2028Mapper;
import com.dina.genasoft.db.mapper.TCompras2029Mapper;
import com.dina.genasoft.db.mapper.TCompras2030Mapper;
import com.dina.genasoft.exception.GenasoftException;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que hace de 'facade' entre la lógica de compras y la BD, aquí se determina qué tabla(s) se van a utilizar.
 */
@Component
@Slf4j
@Data
public class ComprasSetup implements Serializable {
    /**
     * 
     */
    private static final long             serialVersionUID = 6960609490441572131L;
    /** El log de la aplicación.*/
    private static final org.slf4j.Logger log              = org.slf4j.LoggerFactory.getLogger(ComprasSetup.class);
    /** Inyección por Spring del mapper TCompras2022Mapper.*/
    @Autowired
    private TCompras2022Mapper            tCompras2022Mapper;
    /** Inyección por Spring del mapper TCompras2023Mapper.*/
    @Autowired
    private TCompras2023Mapper            tCompras2023Mapper;
    /** Inyección por Spring del mapper TCompras2024Mapper.*/
    @Autowired
    private TCompras2024Mapper            tCompras2024Mapper;
    /** Inyección por Spring del mapper TCompras2025Mapper.*/
    @Autowired
    private TCompras2025Mapper            tCompras2025Mapper;
    /** Inyección por Spring del mapper TCompras2026Mapper.*/
    @Autowired
    private TCompras2026Mapper            tCompras2026Mapper;
    /** Inyección por Spring del mapper TCompras2027Mapper.*/
    @Autowired
    private TCompras2027Mapper            tCompras2027Mapper;
    /** Inyección por Spring del mapper TCompras2028Mapper.*/
    @Autowired
    private TCompras2028Mapper            tCompras2028Mapper;
    /** Inyección por Spring del mapper TCompras2029Mapper.*/
    @Autowired
    private TCompras2029Mapper            tCompras2029Mapper;
    /** Inyección por Spring del mapper TCompras2030Mapper.*/
    @Autowired
    private TCompras2030Mapper            tCompras2030Mapper;

    /**
     * Método que nos retorna las compras del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio a consultar, si ejercicio is null, retorna las compras de todos los ejercicios.
     * @return Las compras encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TCompras> obtenerTodasComprasEjercicio(Integer ejercicio) throws GenasoftException {
        List<TCompras> lResult = null;

        if (ejercicio.equals(-1)) {
            lResult = tCompras2022Mapper.obtenerTodasCompras();
            lResult.addAll(tCompras2023Mapper.obtenerTodasCompras());
            lResult.addAll(tCompras2024Mapper.obtenerTodasCompras());
            lResult.addAll(tCompras2025Mapper.obtenerTodasCompras());
            lResult.addAll(tCompras2026Mapper.obtenerTodasCompras());
            lResult.addAll(tCompras2027Mapper.obtenerTodasCompras());
            lResult.addAll(tCompras2028Mapper.obtenerTodasCompras());
            lResult.addAll(tCompras2029Mapper.obtenerTodasCompras());
            lResult.addAll(tCompras2030Mapper.obtenerTodasCompras());

        } else {

            switch (ejercicio) {
                case 2022:
                    lResult = tCompras2022Mapper.obtenerTodasCompras();
                    break;
                case 2023:
                    lResult = tCompras2023Mapper.obtenerTodasCompras();
                    break;
                case 2024:
                    lResult = tCompras2024Mapper.obtenerTodasCompras();
                    break;
                case 2025:
                    lResult = tCompras2025Mapper.obtenerTodasCompras();
                    break;
                case 2026:
                    lResult = tCompras2026Mapper.obtenerTodasCompras();
                    break;
                case 2027:
                    lResult = tCompras2027Mapper.obtenerTodasCompras();
                    break;
                case 2028:
                    lResult = tCompras2028Mapper.obtenerTodasCompras();
                    break;
                case 2029:
                    lResult = tCompras2029Mapper.obtenerTodasCompras();
                    break;
                case 2030:
                    lResult = tCompras2030Mapper.obtenerTodasCompras();
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return lResult;
    }

    /**
     * Método que nos retorna las compras del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio a consultar
     * @return Las compras encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TCompras> obtenerTodasComprasEjercicio2(Integer ejercicio) throws GenasoftException {
        List<TCompras> lResult = null;

        if (ejercicio.equals(-1)) {
            lResult = tCompras2022Mapper.obtenerTodasCompras2();
            lResult.addAll(tCompras2023Mapper.obtenerTodasCompras2());
            lResult.addAll(tCompras2024Mapper.obtenerTodasCompras2());
            lResult.addAll(tCompras2025Mapper.obtenerTodasCompras2());
            lResult.addAll(tCompras2026Mapper.obtenerTodasCompras2());
            lResult.addAll(tCompras2027Mapper.obtenerTodasCompras2());
            lResult.addAll(tCompras2028Mapper.obtenerTodasCompras2());
            lResult.addAll(tCompras2029Mapper.obtenerTodasCompras2());
            lResult.addAll(tCompras2030Mapper.obtenerTodasCompras2());

        } else {

            switch (ejercicio) {
                case 2022:
                    lResult = tCompras2022Mapper.obtenerTodasCompras2();
                    break;
                case 2023:
                    lResult = tCompras2023Mapper.obtenerTodasCompras2();
                    break;
                case 2024:
                    lResult = tCompras2024Mapper.obtenerTodasCompras2();
                    break;
                case 2025:
                    lResult = tCompras2025Mapper.obtenerTodasCompras2();
                    break;
                case 2026:
                    lResult = tCompras2026Mapper.obtenerTodasCompras2();
                    break;
                case 2027:
                    lResult = tCompras2027Mapper.obtenerTodasCompras2();
                    break;
                case 2028:
                    lResult = tCompras2028Mapper.obtenerTodasCompras2();
                    break;
                case 2029:
                    lResult = tCompras2029Mapper.obtenerTodasCompras2();
                    break;
                case 2030:
                    lResult = tCompras2030Mapper.obtenerTodasCompras2();
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return lResult;
    }

    /**
     * Método que nos realiza la búsqueda de las compras a partir del ejercicio, fechas, variedades, orígen y calidad.
     * @param ejercicio El ejercicio.
     * @param f1 La fecha desde.
     * @param f2 La fecha hasta.
     * @param lVariedades Lista de variedades
     * @param origen El orígem
     * @param calidad La calidad
     * @return Las compras encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TCompras> obtenerComprasEjercicioParametros2(Integer ejercicio, Date f1, Date f2, List<String> lVariedades, String origen, String calidad) throws GenasoftException {
        List<TCompras> lResult = null;

        if (ejercicio.equals(-1)) {
            lResult = tCompras2022Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad);
            lResult.addAll(tCompras2023Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad));
            lResult.addAll(tCompras2024Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad));
            lResult.addAll(tCompras2025Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad));
            lResult.addAll(tCompras2026Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad));
            lResult.addAll(tCompras2027Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad));
            lResult.addAll(tCompras2028Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad));
            lResult.addAll(tCompras2029Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad));
            lResult.addAll(tCompras2030Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad));

        } else {

            switch (ejercicio) {
                case 2022:
                    lResult = tCompras2022Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad);
                    break;
                case 2023:
                    lResult = tCompras2023Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad);
                    break;
                case 2024:
                    lResult = tCompras2024Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad);
                    break;
                case 2025:
                    lResult = tCompras2025Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad);
                    break;
                case 2026:
                    lResult = tCompras2026Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad);
                    break;
                case 2027:
                    lResult = tCompras2027Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad);
                    break;
                case 2028:
                    lResult = tCompras2028Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad);
                    break;
                case 2029:
                    lResult = tCompras2029Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad);
                    break;
                case 2030:
                    lResult = tCompras2030Mapper.obtenerComprasParametros2(f1, f2, lVariedades, origen, calidad);
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return lResult;
    }

    /**
     * Método que nos retorna la compra del ejercicio pasado por parámetro y el cód externo.
     * @param ejercicio El ejercicio a consultar.
     * @param codExterno El código externo por el que realizar la consulta.
     * @return La compra encontrada.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public TCompras obtenerCompraPorEjercicioIdExterno(Integer ejercicio, Double codExterno) throws GenasoftException {
        TCompras result = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tCompras2022Mapper.obtenerCompraPorIdExterno(codExterno);
                break;
            case 2023:
                result = tCompras2023Mapper.obtenerCompraPorIdExterno(codExterno);
                break;
            case 2024:
                result = tCompras2024Mapper.obtenerCompraPorIdExterno(codExterno);
                break;
            case 2025:
                result = tCompras2025Mapper.obtenerCompraPorIdExterno(codExterno);
                break;
            case 2026:
                result = tCompras2026Mapper.obtenerCompraPorIdExterno(codExterno);
                break;
            case 2027:
                result = tCompras2027Mapper.obtenerCompraPorIdExterno(codExterno);
                break;
            case 2028:
                result = tCompras2028Mapper.obtenerCompraPorIdExterno(codExterno);
                break;
            case 2029:
                result = tCompras2029Mapper.obtenerCompraPorIdExterno(codExterno);
                break;
            case 2030:
                result = tCompras2030Mapper.obtenerCompraPorIdExterno(codExterno);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos retorna la compra del ejercicio pasado por parámetro y lote.
     * @param ejercicio El ejercicio a consultar.
     * @param lote El lote por el que realizar la consulta.
     * @return La compra encontrada.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public TCompras obtenerCompraPorEjercicioLote(Integer ejercicio, String lote) throws GenasoftException {
        TCompras result = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tCompras2022Mapper.obtenerCompraPorLote(lote);
                break;
            case 2023:
                result = tCompras2023Mapper.obtenerCompraPorLote(lote);
                break;
            case 2024:
                result = tCompras2024Mapper.obtenerCompraPorLote(lote);
                break;
            case 2025:
                result = tCompras2025Mapper.obtenerCompraPorLote(lote);
                break;
            case 2026:
                result = tCompras2026Mapper.obtenerCompraPorLote(lote);
                break;
            case 2027:
                result = tCompras2027Mapper.obtenerCompraPorLote(lote);
                break;
            case 2028:
                result = tCompras2028Mapper.obtenerCompraPorLote(lote);
                break;
            case 2029:
                result = tCompras2029Mapper.obtenerCompraPorLote(lote);
                break;
            case 2030:
                result = tCompras2030Mapper.obtenerCompraPorLote(lote);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos guarda la compra del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la compra a guardar.
     * @param compra La compra a guardar.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer modificarCompraEjercicio(Integer ejercicio, TCompras compra) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                TCompras2022 c22 = new TCompras2022(compra);
                result = tCompras2022Mapper.updateByPrimaryKey(c22);
                break;
            case 2023:
                TCompras2023 c23 = new TCompras2023(compra);
                result = tCompras2023Mapper.updateByPrimaryKey(c23);
                break;
            case 2024:
                TCompras2024 c24 = new TCompras2024(compra);
                result = tCompras2024Mapper.updateByPrimaryKey(c24);
                break;
            case 2025:
                TCompras2025 c25 = new TCompras2025(compra);
                result = tCompras2025Mapper.updateByPrimaryKey(c25);
                break;
            case 2026:
                TCompras2026 c26 = new TCompras2026(compra);
                result = tCompras2026Mapper.updateByPrimaryKey(c26);
                break;
            case 2027:
                TCompras2027 c27 = new TCompras2027(compra);
                result = tCompras2027Mapper.updateByPrimaryKey(c27);
                break;
            case 2028:
                TCompras2028 c28 = new TCompras2028(compra);
                result = tCompras2028Mapper.updateByPrimaryKey(c28);
                break;
            case 2029:
                TCompras2029 c29 = new TCompras2029(compra);
                result = tCompras2029Mapper.updateByPrimaryKey(c29);
                break;
            case 2030:
                TCompras2030 c30 = new TCompras2030(compra);
                result = tCompras2030Mapper.updateByPrimaryKey(c30);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos guarda la compra del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la compra a guardar.
     * @param compra La compra a guardar.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer crearCompraEjercicio(Integer ejercicio, TCompras compra) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                TCompras2022 c22 = new TCompras2022(compra);
                result = tCompras2022Mapper.insert(c22);
                break;
            case 2023:
                TCompras2023 c23 = new TCompras2023(compra);
                result = tCompras2023Mapper.insert(c23);
                break;
            case 2024:
                TCompras2024 c24 = new TCompras2024(compra);
                result = tCompras2024Mapper.insert(c24);
                break;
            case 2025:
                TCompras2025 c25 = new TCompras2025(compra);
                result = tCompras2025Mapper.insert(c25);
                break;
            case 2026:
                TCompras2026 c26 = new TCompras2026(compra);
                result = tCompras2026Mapper.insert(c26);
                break;
            case 2027:
                TCompras2027 c27 = new TCompras2027(compra);
                result = tCompras2027Mapper.insert(c27);
                break;
            case 2028:
                TCompras2028 c28 = new TCompras2028(compra);
                result = tCompras2028Mapper.insert(c28);
                break;
            case 2029:
                TCompras2029 c29 = new TCompras2029(compra);
                result = tCompras2029Mapper.insert(c29);
                break;
            case 2030:
                TCompras2030 c30 = new TCompras2030(compra);
                result = tCompras2030Mapper.insert(c30);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos guarda la compra del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la compra a guardar.
     * @param compra La compra a guardar.
     * @return El ID generado.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer insertRecordEjercicio(Integer ejercicio, Map<String, Object> map) throws GenasoftException {

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                tCompras2022Mapper.insertRecord(map);
                break;
            case 2023:
                tCompras2023Mapper.insertRecord(map);
                break;
            case 2024:
                tCompras2024Mapper.insertRecord(map);
                break;
            case 2025:
                tCompras2025Mapper.insertRecord(map);
                break;
            case 2026:
                tCompras2026Mapper.insertRecord(map);
                break;
            case 2027:
                tCompras2027Mapper.insertRecord(map);
                break;
            case 2028:
                tCompras2028Mapper.insertRecord(map);
                break;
            case 2029:
                tCompras2029Mapper.insertRecord(map);
                break;
            case 2030:
                tCompras2030Mapper.insertRecord(map);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }
        Integer id = (Integer) map.get("id");
        return id;
    }

    /**
     * Método que nos guarda la compra del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio de la compra a guardar.
     * @param compra La compra a guardar.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer eliminarCompraEjercicio(Integer ejercicio, Integer id) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tCompras2022Mapper.deleteByPrimaryKey(id);
                break;
            case 2023:
                result = tCompras2023Mapper.deleteByPrimaryKey(id);
                break;
            case 2024:
                result = tCompras2024Mapper.deleteByPrimaryKey(id);
                break;
            case 2025:
                result = tCompras2025Mapper.deleteByPrimaryKey(id);
                break;
            case 2026:
                result = tCompras2026Mapper.deleteByPrimaryKey(id);
                break;
            case 2027:
                result = tCompras2027Mapper.deleteByPrimaryKey(id);
                break;
            case 2028:
                result = tCompras2028Mapper.deleteByPrimaryKey(id);
                break;
            case 2029:
                result = tCompras2029Mapper.deleteByPrimaryKey(id);
                break;
            case 2030:
                result = tCompras2030Mapper.deleteByPrimaryKey(id);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos retorna la compra del ejercicio pasado por parámetro  partir del albarán y lote.
     * @param ejercicio El ejercicio a consultar.
     * @param numAlbaran El número de albarán.
     * @param lote El lote
     * @return La compra encontrada.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public TCompras obtenerCompraPorAlbaranLoteEjercicio(Integer ejercicio, String numAlbaran, String lote) throws GenasoftException {
        TCompras result = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tCompras2022Mapper.obtenerCompraPorAlbaranLote(numAlbaran, lote);
                break;
            case 2023:
                result = tCompras2023Mapper.obtenerCompraPorAlbaranLote(numAlbaran, lote);
                break;
            case 2024:
                result = tCompras2024Mapper.obtenerCompraPorAlbaranLote(numAlbaran, lote);
                break;
            case 2025:
                result = tCompras2025Mapper.obtenerCompraPorAlbaranLote(numAlbaran, lote);
                break;
            case 2026:
                result = tCompras2026Mapper.obtenerCompraPorAlbaranLote(numAlbaran, lote);
                break;
            case 2027:
                result = tCompras2027Mapper.obtenerCompraPorAlbaranLote(numAlbaran, lote);
                break;
            case 2028:
                result = tCompras2028Mapper.obtenerCompraPorAlbaranLote(numAlbaran, lote);
                break;
            case 2029:
                result = tCompras2029Mapper.obtenerCompraPorAlbaranLote(numAlbaran, lote);
                break;
            case 2030:
                result = tCompras2030Mapper.obtenerCompraPorAlbaranLote(numAlbaran, lote);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos elimina las compras del ejercicio pasado por parámetro con los ids pasados por parámetro..
     * @param ejercicio El ejercicio a consultar.
     * @param lIdsCompras Los ids de las compras a eliminar.
     * @return Los registros eliminados.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer eliminarComprasEjercicioIds(Integer ejercicio, List<Integer> lIdsCompras) throws GenasoftException {
        Integer result = -1;
        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tCompras2022Mapper.eliminarComprasIds(lIdsCompras);
                break;
            case 2023:
                result = tCompras2023Mapper.eliminarComprasIds(lIdsCompras);
                break;
            case 2024:
                result = tCompras2024Mapper.eliminarComprasIds(lIdsCompras);
                break;
            case 2025:
                result = tCompras2025Mapper.eliminarComprasIds(lIdsCompras);
                break;
            case 2026:
                result = tCompras2026Mapper.eliminarComprasIds(lIdsCompras);
                break;
            case 2027:
                result = tCompras2027Mapper.eliminarComprasIds(lIdsCompras);
                break;
            case 2028:
                result = tCompras2028Mapper.eliminarComprasIds(lIdsCompras);
                break;
            case 2029:
                result = tCompras2029Mapper.eliminarComprasIds(lIdsCompras);
                break;
            case 2030:
                result = tCompras2030Mapper.eliminarComprasIds(lIdsCompras);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return result;
    }

    /**
     * Método que nos cierra las compras del ejercicio pasado por parámetro con los ids pasado por parámetro.
     * @param ejercicio El ejericio
     * @param lIdsCompras Los ids de las compras a cerrar
     * @return El nº de compras cerradas
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public Integer cerrarComprasEjercicioKgsDisponiblesCero(Integer ejercicio, List<Integer> lIdsCompras) throws GenasoftException {
        Integer result = -1;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                result = tCompras2022Mapper.cerrarComprasKgsDisponiblesCero(lIdsCompras);
                break;
            case 2023:
                result = tCompras2023Mapper.cerrarComprasKgsDisponiblesCero(lIdsCompras);
                break;
            case 2024:
                result = tCompras2024Mapper.cerrarComprasKgsDisponiblesCero(lIdsCompras);
                break;
            case 2025:
                result = tCompras2025Mapper.cerrarComprasKgsDisponiblesCero(lIdsCompras);
                break;
            case 2026:
                result = tCompras2026Mapper.cerrarComprasKgsDisponiblesCero(lIdsCompras);
                break;
            case 2027:
                result = tCompras2027Mapper.cerrarComprasKgsDisponiblesCero(lIdsCompras);
                break;
            case 2028:
                result = tCompras2028Mapper.cerrarComprasKgsDisponiblesCero(lIdsCompras);
                break;
            case 2029:
                result = tCompras2029Mapper.cerrarComprasKgsDisponiblesCero(lIdsCompras);
                break;
            case 2030:
                result = tCompras2030Mapper.cerrarComprasKgsDisponiblesCero(lIdsCompras);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }
        return result;
    }

    /**
     * Método que nos retorna las compras del ejercicio indicado y los ids pasado por parámetro.
     * @param ejercicio El ejericio para realizar la consulta.
     * @param lIdsCompras Los Ids de las compras a buscar
     * @return Las compras encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TCompras> obtenerComprasEjercicioIds(Integer ejercicio, List<Integer> lIdsCompras) throws GenasoftException {
        List<TCompras> lResult = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tCompras2022Mapper.obtenerComprasIds(lIdsCompras);
                break;
            case 2023:
                lResult = tCompras2023Mapper.obtenerComprasIds(lIdsCompras);
                break;
            case 2024:
                lResult = tCompras2024Mapper.obtenerComprasIds(lIdsCompras);
                break;
            case 2025:
                lResult = tCompras2025Mapper.obtenerComprasIds(lIdsCompras);
                break;
            case 2026:
                lResult = tCompras2026Mapper.obtenerComprasIds(lIdsCompras);
                break;
            case 2027:
                lResult = tCompras2027Mapper.obtenerComprasIds(lIdsCompras);
                break;
            case 2028:
                lResult = tCompras2028Mapper.obtenerComprasIds(lIdsCompras);
                break;
            case 2029:
                lResult = tCompras2029Mapper.obtenerComprasIds(lIdsCompras);
                break;
            case 2030:
                lResult = tCompras2030Mapper.obtenerComprasIds(lIdsCompras);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }

        return lResult;
    }

    /**
     * Método que nos retorna las compras del ejercicio indicado para el balance de masas.
     * @param ejercicio El ejericio para realizar la consulta.
     * @return Las compras encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TCompras> obtenerComprasBalanceMasasEjercicioTodosGroupBy(Integer ejercicio) throws GenasoftException {
        List<TCompras> lResult = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tCompras2022Mapper.obtenerComprasBalanceMasasTodosGroupBy();
                break;
            case 2023:
                lResult = tCompras2023Mapper.obtenerComprasBalanceMasasTodosGroupBy();
                break;
            case 2024:
                lResult = tCompras2024Mapper.obtenerComprasBalanceMasasTodosGroupBy();
                break;
            case 2025:
                lResult = tCompras2025Mapper.obtenerComprasBalanceMasasTodosGroupBy();
                break;
            case 2026:
                lResult = tCompras2026Mapper.obtenerComprasBalanceMasasTodosGroupBy();
                break;
            case 2027:
                lResult = tCompras2027Mapper.obtenerComprasBalanceMasasTodosGroupBy();
                break;
            case 2028:
                lResult = tCompras2028Mapper.obtenerComprasBalanceMasasTodosGroupBy();
                break;
            case 2029:
                lResult = tCompras2029Mapper.obtenerComprasBalanceMasasTodosGroupBy();
                break;
            case 2030:
                lResult = tCompras2030Mapper.obtenerComprasBalanceMasasTodosGroupBy();
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }
        return lResult;
    }

    /**
     * Método que nos retorna las compras del ejercicio indicado para el balance de masas.
     * @param ejercicio El ejericio para realizar la consulta.
     * @return Las compras encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TCompras> obtenerComprasEjercicioFechas(Integer ejercicio, Date f1, Date f2) throws GenasoftException {
        List<TCompras> lResult = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tCompras2022Mapper.obtenerComprasFechas(f1, f2);
                break;
            case 2023:
                lResult = tCompras2023Mapper.obtenerComprasFechas(f1, f2);
                break;
            case 2024:
                lResult = tCompras2024Mapper.obtenerComprasFechas(f1, f2);
                break;
            case 2025:
                lResult = tCompras2025Mapper.obtenerComprasFechas(f1, f2);
                break;
            case 2026:
                lResult = tCompras2026Mapper.obtenerComprasFechas(f1, f2);
                break;
            case 2027:
                lResult = tCompras2027Mapper.obtenerComprasFechas(f1, f2);
                break;
            case 2028:
                lResult = tCompras2028Mapper.obtenerComprasFechas(f1, f2);
                break;
            case 2029:
                lResult = tCompras2029Mapper.obtenerComprasFechas(f1, f2);
                break;
            case 2030:
                lResult = tCompras2030Mapper.obtenerComprasFechas(f1, f2);
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }
        return lResult;
    }

    /**
     * Método que nos retorna las compras del ejercicio indicado para el balance de masas.
     * @param ejercicio El ejericio para realizar la consulta.
     * @return Las compras encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TCompras> obtenerComprasCertificacionNacionalImportacionEjercicio(Integer ejercicio) throws GenasoftException {
        List<TCompras> lResult = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tCompras2022Mapper.obtenerComprasCertificacionNacionalImportacion();
                break;
            case 2023:
                lResult = tCompras2023Mapper.obtenerComprasCertificacionNacionalImportacion();
                break;
            case 2024:
                lResult = tCompras2024Mapper.obtenerComprasCertificacionNacionalImportacion();
                break;
            case 2025:
                lResult = tCompras2025Mapper.obtenerComprasCertificacionNacionalImportacion();
                break;
            case 2026:
                lResult = tCompras2026Mapper.obtenerComprasCertificacionNacionalImportacion();
                break;
            case 2027:
                lResult = tCompras2027Mapper.obtenerComprasCertificacionNacionalImportacion();
                break;
            case 2028:
                lResult = tCompras2028Mapper.obtenerComprasCertificacionNacionalImportacion();
                break;
            case 2029:
                lResult = tCompras2029Mapper.obtenerComprasCertificacionNacionalImportacion();
                break;
            case 2030:
                lResult = tCompras2030Mapper.obtenerComprasCertificacionNacionalImportacion();
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }
        return lResult;
    }

    /**
     * Método que nos retorna las compras del ejercicio indicado para el balance de masas por global gap.
     * @param ejercicio El ejericio para realizar la consulta.
     * @return Las compras encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TCompras> obtenerComprasGlobalGapEjercicio(Integer ejercicio) throws GenasoftException {
        List<TCompras> lResult = null;

        if (ejercicio.equals(-1)) {
            throw new GenasoftException("Se debe indicar un ejercicio.");
        }

        switch (ejercicio) {
            case 2022:
                lResult = tCompras2022Mapper.obtenerComprasGlobalGap();
                break;
            case 2023:
                lResult = tCompras2023Mapper.obtenerComprasGlobalGap();
                break;
            case 2024:
                lResult = tCompras2024Mapper.obtenerComprasGlobalGap();
                break;
            case 2025:
                lResult = tCompras2025Mapper.obtenerComprasGlobalGap();
                break;
            case 2026:
                lResult = tCompras2026Mapper.obtenerComprasGlobalGap();
                break;
            case 2027:
                lResult = tCompras2027Mapper.obtenerComprasGlobalGap();
                break;
            case 2028:
                lResult = tCompras2028Mapper.obtenerComprasGlobalGap();
                break;
            case 2029:
                lResult = tCompras2029Mapper.obtenerComprasGlobalGap();
                break;
            case 2030:
                lResult = tCompras2030Mapper.obtenerComprasGlobalGap();
                break;
            default:
                throw new GenasoftException("El ejercicio indicado no es correcto.");
        }
        return lResult;
    }

    /**
     * Método que nos retorna las compras del ejercicio pasado por parámetro.
     * @param ejercicio El ejercicio a consultar
     * @return Las compras encontradas.
     * @throws GenasoftException Si el ejercicio indicado no es correcto.
     */
    public List<TCompras> obtenerComprasAlbaranesLotesPartidasEjercicio(Integer ejercicio, List<String> lAlbaranes, List<String> lLotes, List<String> lPartidas) throws GenasoftException {
        List<TCompras> lResult = null;

        if (ejercicio.equals(-1)) {
            lResult = tCompras2022Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
            lResult.addAll(tCompras2023Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas));
            lResult.addAll(tCompras2024Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas));
            lResult.addAll(tCompras2025Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas));
            lResult.addAll(tCompras2026Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas));
            lResult.addAll(tCompras2027Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas));
            lResult.addAll(tCompras2028Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas));
            lResult.addAll(tCompras2029Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas));
            lResult.addAll(tCompras2030Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas));

        } else {

            switch (ejercicio) {
                case 2022:
                    lResult = tCompras2022Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
                    break;
                case 2023:
                    lResult = tCompras2023Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
                    break;
                case 2024:
                    lResult = tCompras2024Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
                    break;
                case 2025:
                    lResult = tCompras2025Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
                    break;
                case 2026:
                    lResult = tCompras2026Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
                    break;
                case 2027:
                    lResult = tCompras2027Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
                    break;
                case 2028:
                    lResult = tCompras2028Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
                    break;
                case 2029:
                    lResult = tCompras2029Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
                    break;
                case 2030:
                    lResult = tCompras2030Mapper.obtenerComprasAlbaranesLotesPartidas(lAlbaranes, lLotes, lPartidas);
                    break;
                default:
                    throw new GenasoftException("El ejercicio indicado no es correcto.");
            }
        }

        return lResult;
    }

}
