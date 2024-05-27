package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.dina.genasoft.utils.Utils;

public class TLineasVentasVista implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.id
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.id_externo
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            idExterno;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.id_venta
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            idVenta;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.id_venta
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            detalleError;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.proveedor_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            proveedorIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.proveedor_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            proveedorFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.certificacion_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            certificacionIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.certificacion_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            certificacionFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.referencia_compra_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            referenciaCompraIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.referencia_compra_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            referenciaCompraFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.kgs_trazados_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private Double            kgsTrazadosIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.kgs_trazados_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private Double            kgsTrazadosFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.kgs_trazabilidad_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private Double            kgsTrazabilidadIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.kgs_trazabilidad_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private Double            kgsTrazabilidadFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.albaran_compra_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            albaranCompraIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.albaran_compra_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            albaranCompraFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.lote_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            loteIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.lote_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            loteFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.bultos_pale_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            bultosPaleIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.bultos_pale_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            bultosPaleFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.ind_basura
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            indBasura;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.ind_error
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            indError;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.ggn
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            ggn;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.ind_cambio
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            indCambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.ind_cerrada
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            indCerrada;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.ind_cambio
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.ind_cerrada
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private String            fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.familia_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            familiaIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.familia_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            familiaFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.variedad_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            variedadIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.variedad_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            variedadFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas.id_venta_externo
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Double            idVentaExterno;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_lineas_ventas
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.id
     * @return  the value of t_lineas_ventas.id
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.id
     * @param id  the value for t_lineas_ventas.id
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.id_externo
     * @return  the value of t_lineas_ventas.id_externo
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getIdExterno() {
        return idExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.id_externo
     * @param idExterno  the value for t_lineas_ventas.id_externo
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setIdExterno(String idExterno) {
        this.idExterno = idExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.id_venta
     * @return  the value of t_lineas_ventas.id_venta
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getIdVenta() {
        return idVenta;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.id_venta
     * @param idVenta  the value for t_lineas_ventas.id_venta
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    /**
     * @return the detalleError
     */
    public String getDetalleError() {
        return detalleError;
    }

    /**
     * @param detalleError the detalleError to set
     */
    public void setDetalleError(String detalleError) {
        this.detalleError = detalleError;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.proveedor_ini
     * @return  the value of t_lineas_ventas.proveedor_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getProveedorIni() {
        return proveedorIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.proveedor_ini
     * @param proveedorIni  the value for t_lineas_ventas.proveedor_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setProveedorIni(String proveedorIni) {
        this.proveedorIni = proveedorIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.proveedor_fin
     * @return  the value of t_lineas_ventas.proveedor_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getProveedorFin() {
        return proveedorFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.proveedor_fin
     * @param proveedorFin  the value for t_lineas_ventas.proveedor_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setProveedorFin(String proveedorFin) {
        this.proveedorFin = proveedorFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.certificacion_ini
     * @return  the value of t_lineas_ventas.certificacion_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getCertificacionIni() {
        return certificacionIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.certificacion_ini
     * @param certificacionIni  the value for t_lineas_ventas.certificacion_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setCertificacionIni(String certificacionIni) {
        this.certificacionIni = certificacionIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.certificacion_fin
     * @return  the value of t_lineas_ventas.certificacion_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getCertificacionFin() {
        return certificacionFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.certificacion_fin
     * @param certificacionFin  the value for t_lineas_ventas.certificacion_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setCertificacionFin(String certificacionFin) {
        this.certificacionFin = certificacionFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.referencia_compra_ini
     * @return  the value of t_lineas_ventas.referencia_compra_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getReferenciaCompraIni() {
        return referenciaCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.referencia_compra_ini
     * @param referenciaCompraIni  the value for t_lineas_ventas.referencia_compra_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setReferenciaCompraIni(String referenciaCompraIni) {
        this.referenciaCompraIni = referenciaCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.referencia_compra_fin
     * @return  the value of t_lineas_ventas.referencia_compra_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getReferenciaCompraFin() {
        return referenciaCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.referencia_compra_fin
     * @param referenciaCompraFin  the value for t_lineas_ventas.referencia_compra_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setReferenciaCompraFin(String referenciaCompraFin) {
        this.referenciaCompraFin = referenciaCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.kgs_trazados_ini
     * @return  the value of t_lineas_ventas.kgs_trazados_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public Double getKgsTrazadosIni() {
        return kgsTrazadosIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.kgs_trazados_ini
     * @param kgsTrazadosIni  the value for t_lineas_ventas.kgs_trazados_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setKgsTrazadosIni(Double kgsTrazadosIni) {
        this.kgsTrazadosIni = kgsTrazadosIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.kgs_trazados_fin
     * @return  the value of t_lineas_ventas.kgs_trazados_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public Double getKgsTrazadosFin() {
        return kgsTrazadosFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.kgs_trazados_fin
     * @param kgsTrazadosFin  the value for t_lineas_ventas.kgs_trazados_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setKgsTrazadosFin(Double kgsTrazadosFin) {
        this.kgsTrazadosFin = kgsTrazadosFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.kgs_trazabilidad_ini
     * @return  the value of t_lineas_ventas.kgs_trazabilidad_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public Double getKgsTrazabilidadIni() {
        return kgsTrazabilidadIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.kgs_trazabilidad_ini
     * @param kgsTrazabilidadIni  the value for t_lineas_ventas.kgs_trazabilidad_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setKgsTrazabilidadIni(Double kgsTrazabilidadIni) {
        this.kgsTrazabilidadIni = kgsTrazabilidadIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.kgs_trazabilidad_fin
     * @return  the value of t_lineas_ventas.kgs_trazabilidad_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public Double getKgsTrazabilidadFin() {
        return kgsTrazabilidadFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.kgs_trazabilidad_fin
     * @param kgsTrazabilidadFin  the value for t_lineas_ventas.kgs_trazabilidad_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setKgsTrazabilidadFin(Double kgsTrazabilidadFin) {
        this.kgsTrazabilidadFin = kgsTrazabilidadFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.albaran_compra_ini
     * @return  the value of t_lineas_ventas.albaran_compra_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getAlbaranCompraIni() {
        return albaranCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.albaran_compra_ini
     * @param albaranCompraIni  the value for t_lineas_ventas.albaran_compra_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setAlbaranCompraIni(String albaranCompraIni) {
        this.albaranCompraIni = albaranCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.albaran_compra_fin
     * @return  the value of t_lineas_ventas.albaran_compra_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getAlbaranCompraFin() {
        return albaranCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.albaran_compra_fin
     * @param albaranCompraFin  the value for t_lineas_ventas.albaran_compra_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setAlbaranCompraFin(String albaranCompraFin) {
        this.albaranCompraFin = albaranCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.lote_ini
     * @return  the value of t_lineas_ventas.lote_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getLoteIni() {
        return loteIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.lote_ini
     * @param loteIni  the value for t_lineas_ventas.lote_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setLoteIni(String loteIni) {
        this.loteIni = loteIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.lote_fin
     * @return  the value of t_lineas_ventas.lote_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getLoteFin() {
        return loteFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.lote_fin
     * @param loteFin  the value for t_lineas_ventas.lote_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setLoteFin(String loteFin) {
        this.loteFin = loteFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.bultos_pale_ini
     * @return  the value of t_lineas_ventas.bultos_pale_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getBultosPaleIni() {
        return bultosPaleIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.bultos_pale_ini
     * @param bultosPaleIni  the value for t_lineas_ventas.bultos_pale_ini
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setBultosPaleIni(String bultosPaleIni) {
        this.bultosPaleIni = bultosPaleIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.bultos_pale_fin
     * @return  the value of t_lineas_ventas.bultos_pale_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getBultosPaleFin() {
        return bultosPaleFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.bultos_pale_fin
     * @param bultosPaleFin  the value for t_lineas_ventas.bultos_pale_fin
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setBultosPaleFin(String bultosPaleFin) {
        this.bultosPaleFin = bultosPaleFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.ind_basura
     * @return  the value of t_lineas_ventas.ind_basura
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getIndBasura() {
        return indBasura;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.ind_basura
     * @param indBasura  the value for t_lineas_ventas.ind_basura
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setIndBasura(String indBasura) {
        this.indBasura = indBasura;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.ind_error
     * @return  the value of t_lineas_ventas.ind_error
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getIndError() {
        return indError;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.ind_error
     * @param indError  the value for t_lineas_ventas.ind_error
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setIndError(String indError) {
        this.indError = indError;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.ggn
     * @return  the value of t_lineas_ventas.ggn
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getGgn() {
        return ggn;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.ggn
     * @param ggn  the value for t_lineas_ventas.ggn
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setGgn(String ggn) {
        this.ggn = ggn;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.ind_cambio
     * @return  the value of t_lineas_ventas.ind_cambio
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getIndCambio() {
        return indCambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.ind_cambio
     * @param indCambio  the value for t_lineas_ventas.ind_cambio
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setIndCambio(String indCambio) {
        this.indCambio = indCambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas.ind_cerrada
     * @return  the value of t_lineas_ventas.ind_cerrada
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public String getIndCerrada() {
        return indCerrada;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas.ind_cerrada
     * @param indCerrada  the value for t_lineas_ventas.ind_cerrada
     * @mbg.generated  Thu Oct 07 14:42:51 CEST 2021
     */
    public void setIndCerrada(String indCerrada) {
        this.indCerrada = indCerrada;
    }

    /**
     * @return the usuCrea
     */
    public String getUsuCrea() {
        return usuCrea;
    }

    /**
     * @param usuCrea the usuCrea to set
     */
    public void setUsuCrea(String usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * @return the fechaCrea
     */
    public String getFechaCrea() {
        return fechaCrea;
    }

    /**
     * @param fechaCrea the fechaCrea to set
     */
    public void setFechaCrea(String fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * @return the familiaIni
     */
    public String getFamiliaIni() {
        return familiaIni;
    }

    /**
     * @param familiaIni the familiaIni to set
     */
    public void setFamiliaIni(String familiaIni) {
        this.familiaIni = familiaIni;
    }

    /**
     * @return the familiaFin
     */
    public String getFamiliaFin() {
        return familiaFin;
    }

    /**
     * @param familiaFin the familiaFin to set
     */
    public void setFamiliaFin(String familiaFin) {
        this.familiaFin = familiaFin;
    }

    /**
     * @return the variedadIni
     */
    public String getVariedadIni() {
        return variedadIni;
    }

    /**
     * @param variedadIni the variedadIni to set
     */
    public void setVariedadIni(String variedadIni) {
        this.variedadIni = variedadIni;
    }

    /**
     * @return the variedadFin
     */
    public String getVariedadFin() {
        return variedadFin;
    }

    /**
     * @param variedadFin the variedadFin to set
     */
    public void setVariedadFin(String variedadFin) {
        this.variedadFin = variedadFin;
    }

    /**
     * @return the idVentaExterno
     */
    public Double getIdVentaExterno() {
        return idVentaExterno;
    }

    /**
     * @param idVentaExterno the idVentaExterno to set
     */
    public void setIdVentaExterno(Double idVentaExterno) {
        this.idVentaExterno = idVentaExterno;
    }

    public TLineasVentasVista() {
        this.albaranCompraFin = "";
        this.albaranCompraIni = "";
        this.bultosPaleFin = "0";
        this.bultosPaleIni = "0";
        this.certificacionFin = "";
        this.certificacionIni = "";
        this.ggn = "";
        this.id = "";
        this.idExterno = null;
        this.idVenta = null;
        this.indBasura = "No";
        this.indCambio = "Sí";
        this.indCerrada = "Sí";
        this.indError = "No";
        this.kgsTrazabilidadFin = Double.valueOf(0);
        this.kgsTrazabilidadIni = Double.valueOf(0);
        this.kgsTrazadosFin = Double.valueOf(0);
        this.kgsTrazadosIni = Double.valueOf(0);
        this.loteFin = "";
        this.loteIni = "";
        this.proveedorFin = "";
        this.proveedorIni = "";
        this.referenciaCompraFin = "";
        this.referenciaCompraIni = "";
        this.detalleError = "";
        this.usuCrea = null;
        this.fechaCrea = null;
        this.familiaFin = "";
        this.familiaIni = "";
        this.variedadFin = "";
        this.variedadIni = "";
        this.idVentaExterno = Double.valueOf(0);
    }

    /**
     * Método que nos realiza la copiade los valores del objeto pasado por parámetro.
     * @param linea El objeto con los datos a copiar y convertirlos a formato String
     */
    public void copiaDesdeLinea(TLineasVentas linea) {
        this.albaranCompraFin = Utils.convertirIntegerStringVista(linea.getAlbaranCompraFin());
        this.albaranCompraIni = Utils.convertirIntegerStringVista(linea.getAlbaranCompraIni());
        this.bultosPaleFin = Utils.convertirIntegerStringVista(linea.getBultosPaleFin());
        this.bultosPaleIni = Utils.convertirIntegerStringVista(linea.getBultosPaleIni());
        this.certificacionFin = Utils.convertirIntegerStringVista(linea.getCertificacionFin());
        this.certificacionIni = Utils.convertirIntegerStringVista(linea.getCertificacionIni());
        this.ggn = Utils.convertirIntegerStringVista(linea.getGgn());
        this.id = Utils.convertirIntegerStringVista(linea.getId());
        this.idExterno = linea.getIdExterno() != null ? Utils.convertirIntegerStringVista(linea.getIdExterno().intValue()) : "-";
        this.idVenta = linea.getIdVenta() != null ? Utils.convertirIntegerStringVista(linea.getIdVenta().intValue()) : "-";
        this.indBasura = linea.getIndBasura() != null && linea.getIndBasura().equals(1) ? "Sí" : "No";
        this.indCambio = linea.getIndCambio() != null && linea.getIndCambio().equals(1) ? "Sí" : "No";
        this.indCerrada = linea.getIndCerrada() != null && linea.getIndCerrada().equals(1) ? "Sí" : "No";
        this.indError = linea.getIndError() != null && linea.getIndError().equals(1) ? "Sí" : "No";
        this.kgsTrazabilidadFin = linea.getKgsTrazabilidadFin();
        this.kgsTrazabilidadIni = linea.getKgsTrazabilidadIni();
        this.kgsTrazadosFin = linea.getKgsTrazadosFin();
        this.kgsTrazadosIni = linea.getKgsTrazadosIni();
        this.loteFin = Utils.convertirIntegerStringVista(linea.getLoteFin());
        this.loteIni = Utils.convertirIntegerStringVista(linea.getLoteIni());
        this.proveedorFin = Utils.convertirIntegerStringVista(linea.getProveedorFin());
        this.proveedorIni = Utils.convertirIntegerStringVista(linea.getProveedorIni());
        this.referenciaCompraFin = Utils.convertirIntegerStringVista(linea.getReferenciaCompraFin());
        this.referenciaCompraIni = Utils.convertirIntegerStringVista(linea.getReferenciaCompraIni());
        this.detalleError = Utils.convertirIntegerStringVista(linea.getDetalleError());
        this.usuCrea = Utils.convertirIntegerStringVista(linea.getUsuCrea());
        this.fechaCrea = linea.getFechaCrea() != null ? new SimpleDateFormat("dd/MM/yyyy").format(linea.getFechaCrea()) : "-";
        this.familiaFin = Utils.convertirIntegerStringVista(linea.getFamiliaFin());
        this.familiaIni = Utils.convertirIntegerStringVista(linea.getFamiliaIni());
        this.variedadFin = Utils.convertirIntegerStringVista(linea.getVariedadFin());
        this.variedadIni = Utils.convertirIntegerStringVista(linea.getVariedadIni());
        this.idVentaExterno = linea.getIdVentaExterno();
    }

}