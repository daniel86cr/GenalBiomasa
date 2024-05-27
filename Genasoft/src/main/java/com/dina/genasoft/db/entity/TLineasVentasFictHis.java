package com.dina.genasoft.db.entity;

import java.io.Serializable;

public class TLineasVentasFictHis implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.id
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.id_externo
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private Double idExterno;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.id_venta
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private Integer idVenta;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.proveedor_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private String proveedorIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.proveedor_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private String proveedorFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.certificacion_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private String certificacionIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.certificacion_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private String certificacionFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.referencia_compra_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private String referenciaCompraIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.referencia_compra_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private String referenciaCompraFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.kgs_trazados_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private Double kgsTrazadosIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.kgs_trazados_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private Double kgsTrazadosFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.kgs_trazabilidad_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private Double kgsTrazabilidadIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.kgs_trazabilidad_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private Double kgsTrazabilidadFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.albaran_compra_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private String albaranCompraIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.albaran_compra_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private String albaranCompraFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.lote_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private String loteIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.lote_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private String loteFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.bultos_pale_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private Integer bultosPaleIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.bultos_pale_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private Integer bultosPaleFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_fict_his.ggn
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private String ggn;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_lineas_ventas_fict_his
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.id
     * @return  the value of t_lineas_ventas_fict_his.id
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.id
     * @param id  the value for t_lineas_ventas_fict_his.id
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.id_externo
     * @return  the value of t_lineas_ventas_fict_his.id_externo
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public Double getIdExterno() {
        return idExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.id_externo
     * @param idExterno  the value for t_lineas_ventas_fict_his.id_externo
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setIdExterno(Double idExterno) {
        this.idExterno = idExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.id_venta
     * @return  the value of t_lineas_ventas_fict_his.id_venta
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public Integer getIdVenta() {
        return idVenta;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.id_venta
     * @param idVenta  the value for t_lineas_ventas_fict_his.id_venta
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.proveedor_ini
     * @return  the value of t_lineas_ventas_fict_his.proveedor_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public String getProveedorIni() {
        return proveedorIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.proveedor_ini
     * @param proveedorIni  the value for t_lineas_ventas_fict_his.proveedor_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setProveedorIni(String proveedorIni) {
        this.proveedorIni = proveedorIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.proveedor_fin
     * @return  the value of t_lineas_ventas_fict_his.proveedor_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public String getProveedorFin() {
        return proveedorFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.proveedor_fin
     * @param proveedorFin  the value for t_lineas_ventas_fict_his.proveedor_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setProveedorFin(String proveedorFin) {
        this.proveedorFin = proveedorFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.certificacion_ini
     * @return  the value of t_lineas_ventas_fict_his.certificacion_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public String getCertificacionIni() {
        return certificacionIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.certificacion_ini
     * @param certificacionIni  the value for t_lineas_ventas_fict_his.certificacion_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setCertificacionIni(String certificacionIni) {
        this.certificacionIni = certificacionIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.certificacion_fin
     * @return  the value of t_lineas_ventas_fict_his.certificacion_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public String getCertificacionFin() {
        return certificacionFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.certificacion_fin
     * @param certificacionFin  the value for t_lineas_ventas_fict_his.certificacion_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setCertificacionFin(String certificacionFin) {
        this.certificacionFin = certificacionFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.referencia_compra_ini
     * @return  the value of t_lineas_ventas_fict_his.referencia_compra_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public String getReferenciaCompraIni() {
        return referenciaCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.referencia_compra_ini
     * @param referenciaCompraIni  the value for t_lineas_ventas_fict_his.referencia_compra_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setReferenciaCompraIni(String referenciaCompraIni) {
        this.referenciaCompraIni = referenciaCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.referencia_compra_fin
     * @return  the value of t_lineas_ventas_fict_his.referencia_compra_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public String getReferenciaCompraFin() {
        return referenciaCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.referencia_compra_fin
     * @param referenciaCompraFin  the value for t_lineas_ventas_fict_his.referencia_compra_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setReferenciaCompraFin(String referenciaCompraFin) {
        this.referenciaCompraFin = referenciaCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.kgs_trazados_ini
     * @return  the value of t_lineas_ventas_fict_his.kgs_trazados_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public Double getKgsTrazadosIni() {
        return kgsTrazadosIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.kgs_trazados_ini
     * @param kgsTrazadosIni  the value for t_lineas_ventas_fict_his.kgs_trazados_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setKgsTrazadosIni(Double kgsTrazadosIni) {
        this.kgsTrazadosIni = kgsTrazadosIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.kgs_trazados_fin
     * @return  the value of t_lineas_ventas_fict_his.kgs_trazados_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public Double getKgsTrazadosFin() {
        return kgsTrazadosFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.kgs_trazados_fin
     * @param kgsTrazadosFin  the value for t_lineas_ventas_fict_his.kgs_trazados_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setKgsTrazadosFin(Double kgsTrazadosFin) {
        this.kgsTrazadosFin = kgsTrazadosFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.kgs_trazabilidad_ini
     * @return  the value of t_lineas_ventas_fict_his.kgs_trazabilidad_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public Double getKgsTrazabilidadIni() {
        return kgsTrazabilidadIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.kgs_trazabilidad_ini
     * @param kgsTrazabilidadIni  the value for t_lineas_ventas_fict_his.kgs_trazabilidad_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setKgsTrazabilidadIni(Double kgsTrazabilidadIni) {
        this.kgsTrazabilidadIni = kgsTrazabilidadIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.kgs_trazabilidad_fin
     * @return  the value of t_lineas_ventas_fict_his.kgs_trazabilidad_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public Double getKgsTrazabilidadFin() {
        return kgsTrazabilidadFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.kgs_trazabilidad_fin
     * @param kgsTrazabilidadFin  the value for t_lineas_ventas_fict_his.kgs_trazabilidad_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setKgsTrazabilidadFin(Double kgsTrazabilidadFin) {
        this.kgsTrazabilidadFin = kgsTrazabilidadFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.albaran_compra_ini
     * @return  the value of t_lineas_ventas_fict_his.albaran_compra_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public String getAlbaranCompraIni() {
        return albaranCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.albaran_compra_ini
     * @param albaranCompraIni  the value for t_lineas_ventas_fict_his.albaran_compra_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setAlbaranCompraIni(String albaranCompraIni) {
        this.albaranCompraIni = albaranCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.albaran_compra_fin
     * @return  the value of t_lineas_ventas_fict_his.albaran_compra_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public String getAlbaranCompraFin() {
        return albaranCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.albaran_compra_fin
     * @param albaranCompraFin  the value for t_lineas_ventas_fict_his.albaran_compra_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setAlbaranCompraFin(String albaranCompraFin) {
        this.albaranCompraFin = albaranCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.lote_ini
     * @return  the value of t_lineas_ventas_fict_his.lote_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public String getLoteIni() {
        return loteIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.lote_ini
     * @param loteIni  the value for t_lineas_ventas_fict_his.lote_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setLoteIni(String loteIni) {
        this.loteIni = loteIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.lote_fin
     * @return  the value of t_lineas_ventas_fict_his.lote_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public String getLoteFin() {
        return loteFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.lote_fin
     * @param loteFin  the value for t_lineas_ventas_fict_his.lote_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setLoteFin(String loteFin) {
        this.loteFin = loteFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.bultos_pale_ini
     * @return  the value of t_lineas_ventas_fict_his.bultos_pale_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public Integer getBultosPaleIni() {
        return bultosPaleIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.bultos_pale_ini
     * @param bultosPaleIni  the value for t_lineas_ventas_fict_his.bultos_pale_ini
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setBultosPaleIni(Integer bultosPaleIni) {
        this.bultosPaleIni = bultosPaleIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.bultos_pale_fin
     * @return  the value of t_lineas_ventas_fict_his.bultos_pale_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public Integer getBultosPaleFin() {
        return bultosPaleFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.bultos_pale_fin
     * @param bultosPaleFin  the value for t_lineas_ventas_fict_his.bultos_pale_fin
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setBultosPaleFin(Integer bultosPaleFin) {
        this.bultosPaleFin = bultosPaleFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_fict_his.ggn
     * @return  the value of t_lineas_ventas_fict_his.ggn
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public String getGgn() {
        return ggn;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_fict_his.ggn
     * @param ggn  the value for t_lineas_ventas_fict_his.ggn
     * @mbg.generated  Thu Oct 07 10:43:08 CEST 2021
     */
    public void setGgn(String ggn) {
        this.ggn = ggn;
    }
}