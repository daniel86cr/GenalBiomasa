package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TLineasVentas2023 implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.id
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Integer           id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.id_externo
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Double            idExterno;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.id_venta_externo
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Double            idVentaExterno;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.id_venta
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Integer           idVenta;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.detalle_error
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            detalleError;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.proveedor_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            proveedorIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.proveedor_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            proveedorFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.certificacion_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            certificacionIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.certificacion_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            certificacionFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.referencia_compra_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            referenciaCompraIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.referencia_compra_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            referenciaCompraFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.kgs_trazados_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Double            kgsTrazadosIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.kgs_trazados_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Double            kgsTrazadosFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.kgs_trazabilidad_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Double            kgsTrazabilidadIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.kgs_trazabilidad_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Double            kgsTrazabilidadFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.albaran_compra_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            albaranCompraIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.albaran_compra_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            albaranCompraFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.lote_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            loteIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.lote_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            loteFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.bultos_pale_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Integer           bultosPaleIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.bultos_pale_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Integer           bultosPaleFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.ind_basura
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Integer           indBasura;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.ind_error
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Integer           indError;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.ggn
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            ggn;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.ind_cambio
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Integer           indCambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.ind_cerrada
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Integer           indCerrada;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.variedad_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            variedadIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.variedad_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            variedadFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.familia_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            familiaIni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.familia_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private String            familiaFin;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.usu_crea
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Integer           usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_lineas_ventas_2023.fecha_crea
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private Date              fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_lineas_ventas_2023
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.id
     * @return  the value of t_lineas_ventas_2023.id
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.id
     * @param id  the value for t_lineas_ventas_2023.id
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.id_externo
     * @return  the value of t_lineas_ventas_2023.id_externo
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Double getIdExterno() {
        return idExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.id_externo
     * @param idExterno  the value for t_lineas_ventas_2023.id_externo
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setIdExterno(Double idExterno) {
        this.idExterno = idExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.id_venta_externo
     * @return  the value of t_lineas_ventas_2023.id_venta_externo
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Double getIdVentaExterno() {
        return idVentaExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.id_venta_externo
     * @param idVentaExterno  the value for t_lineas_ventas_2023.id_venta_externo
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setIdVentaExterno(Double idVentaExterno) {
        this.idVentaExterno = idVentaExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.id_venta
     * @return  the value of t_lineas_ventas_2023.id_venta
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Integer getIdVenta() {
        return idVenta;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.id_venta
     * @param idVenta  the value for t_lineas_ventas_2023.id_venta
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.detalle_error
     * @return  the value of t_lineas_ventas_2023.detalle_error
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getDetalleError() {
        return detalleError;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.detalle_error
     * @param detalleError  the value for t_lineas_ventas_2023.detalle_error
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setDetalleError(String detalleError) {
        this.detalleError = detalleError;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.proveedor_ini
     * @return  the value of t_lineas_ventas_2023.proveedor_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getProveedorIni() {
        return proveedorIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.proveedor_ini
     * @param proveedorIni  the value for t_lineas_ventas_2023.proveedor_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setProveedorIni(String proveedorIni) {
        this.proveedorIni = proveedorIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.proveedor_fin
     * @return  the value of t_lineas_ventas_2023.proveedor_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getProveedorFin() {
        return proveedorFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.proveedor_fin
     * @param proveedorFin  the value for t_lineas_ventas_2023.proveedor_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setProveedorFin(String proveedorFin) {
        this.proveedorFin = proveedorFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.certificacion_ini
     * @return  the value of t_lineas_ventas_2023.certificacion_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getCertificacionIni() {
        return certificacionIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.certificacion_ini
     * @param certificacionIni  the value for t_lineas_ventas_2023.certificacion_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setCertificacionIni(String certificacionIni) {
        this.certificacionIni = certificacionIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.certificacion_fin
     * @return  the value of t_lineas_ventas_2023.certificacion_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getCertificacionFin() {
        return certificacionFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.certificacion_fin
     * @param certificacionFin  the value for t_lineas_ventas_2023.certificacion_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setCertificacionFin(String certificacionFin) {
        this.certificacionFin = certificacionFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.referencia_compra_ini
     * @return  the value of t_lineas_ventas_2023.referencia_compra_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getReferenciaCompraIni() {
        return referenciaCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.referencia_compra_ini
     * @param referenciaCompraIni  the value for t_lineas_ventas_2023.referencia_compra_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setReferenciaCompraIni(String referenciaCompraIni) {
        this.referenciaCompraIni = referenciaCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.referencia_compra_fin
     * @return  the value of t_lineas_ventas_2023.referencia_compra_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getReferenciaCompraFin() {
        return referenciaCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.referencia_compra_fin
     * @param referenciaCompraFin  the value for t_lineas_ventas_2023.referencia_compra_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setReferenciaCompraFin(String referenciaCompraFin) {
        this.referenciaCompraFin = referenciaCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.kgs_trazados_ini
     * @return  the value of t_lineas_ventas_2023.kgs_trazados_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Double getKgsTrazadosIni() {
        return kgsTrazadosIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.kgs_trazados_ini
     * @param kgsTrazadosIni  the value for t_lineas_ventas_2023.kgs_trazados_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setKgsTrazadosIni(Double kgsTrazadosIni) {
        this.kgsTrazadosIni = kgsTrazadosIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.kgs_trazados_fin
     * @return  the value of t_lineas_ventas_2023.kgs_trazados_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Double getKgsTrazadosFin() {
        return kgsTrazadosFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.kgs_trazados_fin
     * @param kgsTrazadosFin  the value for t_lineas_ventas_2023.kgs_trazados_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setKgsTrazadosFin(Double kgsTrazadosFin) {
        this.kgsTrazadosFin = kgsTrazadosFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.kgs_trazabilidad_ini
     * @return  the value of t_lineas_ventas_2023.kgs_trazabilidad_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Double getKgsTrazabilidadIni() {
        return kgsTrazabilidadIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.kgs_trazabilidad_ini
     * @param kgsTrazabilidadIni  the value for t_lineas_ventas_2023.kgs_trazabilidad_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setKgsTrazabilidadIni(Double kgsTrazabilidadIni) {
        this.kgsTrazabilidadIni = kgsTrazabilidadIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.kgs_trazabilidad_fin
     * @return  the value of t_lineas_ventas_2023.kgs_trazabilidad_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Double getKgsTrazabilidadFin() {
        return kgsTrazabilidadFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.kgs_trazabilidad_fin
     * @param kgsTrazabilidadFin  the value for t_lineas_ventas_2023.kgs_trazabilidad_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setKgsTrazabilidadFin(Double kgsTrazabilidadFin) {
        this.kgsTrazabilidadFin = kgsTrazabilidadFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.albaran_compra_ini
     * @return  the value of t_lineas_ventas_2023.albaran_compra_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getAlbaranCompraIni() {
        return albaranCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.albaran_compra_ini
     * @param albaranCompraIni  the value for t_lineas_ventas_2023.albaran_compra_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setAlbaranCompraIni(String albaranCompraIni) {
        this.albaranCompraIni = albaranCompraIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.albaran_compra_fin
     * @return  the value of t_lineas_ventas_2023.albaran_compra_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getAlbaranCompraFin() {
        return albaranCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.albaran_compra_fin
     * @param albaranCompraFin  the value for t_lineas_ventas_2023.albaran_compra_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setAlbaranCompraFin(String albaranCompraFin) {
        this.albaranCompraFin = albaranCompraFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.lote_ini
     * @return  the value of t_lineas_ventas_2023.lote_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getLoteIni() {
        return loteIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.lote_ini
     * @param loteIni  the value for t_lineas_ventas_2023.lote_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setLoteIni(String loteIni) {
        this.loteIni = loteIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.lote_fin
     * @return  the value of t_lineas_ventas_2023.lote_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getLoteFin() {
        return loteFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.lote_fin
     * @param loteFin  the value for t_lineas_ventas_2023.lote_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setLoteFin(String loteFin) {
        this.loteFin = loteFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.bultos_pale_ini
     * @return  the value of t_lineas_ventas_2023.bultos_pale_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Integer getBultosPaleIni() {
        return bultosPaleIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.bultos_pale_ini
     * @param bultosPaleIni  the value for t_lineas_ventas_2023.bultos_pale_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setBultosPaleIni(Integer bultosPaleIni) {
        this.bultosPaleIni = bultosPaleIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.bultos_pale_fin
     * @return  the value of t_lineas_ventas_2023.bultos_pale_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Integer getBultosPaleFin() {
        return bultosPaleFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.bultos_pale_fin
     * @param bultosPaleFin  the value for t_lineas_ventas_2023.bultos_pale_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setBultosPaleFin(Integer bultosPaleFin) {
        this.bultosPaleFin = bultosPaleFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.ind_basura
     * @return  the value of t_lineas_ventas_2023.ind_basura
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Integer getIndBasura() {
        return indBasura;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.ind_basura
     * @param indBasura  the value for t_lineas_ventas_2023.ind_basura
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setIndBasura(Integer indBasura) {
        this.indBasura = indBasura;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.ind_error
     * @return  the value of t_lineas_ventas_2023.ind_error
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Integer getIndError() {
        return indError;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.ind_error
     * @param indError  the value for t_lineas_ventas_2023.ind_error
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setIndError(Integer indError) {
        this.indError = indError;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.ggn
     * @return  the value of t_lineas_ventas_2023.ggn
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getGgn() {
        return ggn;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.ggn
     * @param ggn  the value for t_lineas_ventas_2023.ggn
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setGgn(String ggn) {
        this.ggn = ggn;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.ind_cambio
     * @return  the value of t_lineas_ventas_2023.ind_cambio
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Integer getIndCambio() {
        return indCambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.ind_cambio
     * @param indCambio  the value for t_lineas_ventas_2023.ind_cambio
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setIndCambio(Integer indCambio) {
        this.indCambio = indCambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.ind_cerrada
     * @return  the value of t_lineas_ventas_2023.ind_cerrada
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Integer getIndCerrada() {
        return indCerrada;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.ind_cerrada
     * @param indCerrada  the value for t_lineas_ventas_2023.ind_cerrada
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setIndCerrada(Integer indCerrada) {
        this.indCerrada = indCerrada;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.variedad_ini
     * @return  the value of t_lineas_ventas_2023.variedad_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getVariedadIni() {
        return variedadIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.variedad_ini
     * @param variedadIni  the value for t_lineas_ventas_2023.variedad_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setVariedadIni(String variedadIni) {
        this.variedadIni = variedadIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.variedad_fin
     * @return  the value of t_lineas_ventas_2023.variedad_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getVariedadFin() {
        return variedadFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.variedad_fin
     * @param variedadFin  the value for t_lineas_ventas_2023.variedad_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setVariedadFin(String variedadFin) {
        this.variedadFin = variedadFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.familia_ini
     * @return  the value of t_lineas_ventas_2023.familia_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getFamiliaIni() {
        return familiaIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.familia_ini
     * @param familiaIni  the value for t_lineas_ventas_2023.familia_ini
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setFamiliaIni(String familiaIni) {
        this.familiaIni = familiaIni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.familia_fin
     * @return  the value of t_lineas_ventas_2023.familia_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public String getFamiliaFin() {
        return familiaFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.familia_fin
     * @param familiaFin  the value for t_lineas_ventas_2023.familia_fin
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setFamiliaFin(String familiaFin) {
        this.familiaFin = familiaFin;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.usu_crea
     * @return  the value of t_lineas_ventas_2023.usu_crea
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.usu_crea
     * @param usuCrea  the value for t_lineas_ventas_2023.usu_crea
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_lineas_ventas_2023.fecha_crea
     * @return  the value of t_lineas_ventas_2023.fecha_crea
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_lineas_ventas_2023.fecha_crea
     * @param fechaCrea  the value for t_lineas_ventas_2023.fecha_crea
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * Constructor a partir del objecto líneas ventas, se setean todos los valores de la línea de venta.
     * @param v La línea de venta con los datos a copiar
     */
    public TLineasVentas2023(TLineasVentas linea) {
        this.albaranCompraFin = linea.getAlbaranCompraFin();
        this.albaranCompraIni = linea.getAlbaranCompraIni();
        this.bultosPaleFin = linea.getBultosPaleFin();
        this.bultosPaleIni = linea.getBultosPaleIni();
        this.certificacionFin = linea.getCertificacionFin();
        this.certificacionIni = linea.getCertificacionIni();
        this.detalleError = linea.getDetalleError();
        this.familiaFin = linea.getFamiliaFin();
        this.familiaIni = linea.getFamiliaIni();
        this.ggn = linea.getGgn();
        this.id = linea.getId();
        this.idExterno = linea.getIdExterno();
        this.idVenta = linea.getIdVenta();
        this.idVentaExterno = linea.getIdVentaExterno();
        this.indBasura = linea.getIndBasura();
        this.indCambio = linea.getIndCambio();
        this.indCerrada = linea.getIndCerrada();
        this.indError = linea.getIndError();
        this.kgsTrazabilidadFin = linea.getKgsTrazabilidadFin();
        this.kgsTrazabilidadIni = linea.getKgsTrazabilidadIni();
        this.kgsTrazadosFin = linea.getKgsTrazadosFin();
        this.kgsTrazadosIni = linea.getKgsTrazadosIni();
        this.loteFin = linea.getLoteFin();
        this.loteIni = linea.getLoteIni();
        this.proveedorFin = linea.getProveedorFin();
        this.proveedorIni = linea.getProveedorIni();
        this.referenciaCompraFin = linea.getReferenciaCompraFin();
        this.referenciaCompraIni = linea.getReferenciaCompraIni();
        this.variedadFin = linea.getVariedadFin();
        this.variedadIni = linea.getVariedadIni();
        this.usuCrea = linea.getUsuCrea();
        this.fechaCrea = linea.getFechaCrea();
    }
}