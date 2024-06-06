package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TFacturas implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.id
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.numero_factura
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private String numeroFactura;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.fecha_factura
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Date fechaFactura;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.empresa
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Integer empresa;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.obra
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private String obra;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.id_cliente
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Integer idCliente;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.id_direccion
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Integer idDireccion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.base
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Double base;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.descuento
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Double descuento;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.subtotal
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Double subtotal;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.total_neto
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Double totalNeto;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.total
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Double total;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.usu_crea
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.fecha_crea
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Date fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.usu_modifica
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Integer usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_facturas.fecha_modifica
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Date fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_facturas
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.id
     * @return  the value of t_facturas.id
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.id
     * @param id  the value for t_facturas.id
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.numero_factura
     * @return  the value of t_facturas.numero_factura
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public String getNumeroFactura() {
        return numeroFactura;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.numero_factura
     * @param numeroFactura  the value for t_facturas.numero_factura
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.fecha_factura
     * @return  the value of t_facturas.fecha_factura
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Date getFechaFactura() {
        return fechaFactura;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.fecha_factura
     * @param fechaFactura  the value for t_facturas.fecha_factura
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.empresa
     * @return  the value of t_facturas.empresa
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Integer getEmpresa() {
        return empresa;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.empresa
     * @param empresa  the value for t_facturas.empresa
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.obra
     * @return  the value of t_facturas.obra
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public String getObra() {
        return obra;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.obra
     * @param obra  the value for t_facturas.obra
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setObra(String obra) {
        this.obra = obra;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.id_cliente
     * @return  the value of t_facturas.id_cliente
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Integer getIdCliente() {
        return idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.id_cliente
     * @param idCliente  the value for t_facturas.id_cliente
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.id_direccion
     * @return  the value of t_facturas.id_direccion
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Integer getIdDireccion() {
        return idDireccion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.id_direccion
     * @param idDireccion  the value for t_facturas.id_direccion
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setIdDireccion(Integer idDireccion) {
        this.idDireccion = idDireccion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.base
     * @return  the value of t_facturas.base
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Double getBase() {
        return base;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.base
     * @param base  the value for t_facturas.base
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setBase(Double base) {
        this.base = base;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.descuento
     * @return  the value of t_facturas.descuento
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Double getDescuento() {
        return descuento;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.descuento
     * @param descuento  the value for t_facturas.descuento
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.subtotal
     * @return  the value of t_facturas.subtotal
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Double getSubtotal() {
        return subtotal;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.subtotal
     * @param subtotal  the value for t_facturas.subtotal
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.total_neto
     * @return  the value of t_facturas.total_neto
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Double getTotalNeto() {
        return totalNeto;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.total_neto
     * @param totalNeto  the value for t_facturas.total_neto
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setTotalNeto(Double totalNeto) {
        this.totalNeto = totalNeto;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.total
     * @return  the value of t_facturas.total
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Double getTotal() {
        return total;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.total
     * @param total  the value for t_facturas.total
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.usu_crea
     * @return  the value of t_facturas.usu_crea
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.usu_crea
     * @param usuCrea  the value for t_facturas.usu_crea
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.fecha_crea
     * @return  the value of t_facturas.fecha_crea
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.fecha_crea
     * @param fechaCrea  the value for t_facturas.fecha_crea
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.usu_modifica
     * @return  the value of t_facturas.usu_modifica
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Integer getUsuModifica() {
        return usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.usu_modifica
     * @param usuModifica  the value for t_facturas.usu_modifica
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setUsuModifica(Integer usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_facturas.fecha_modifica
     * @return  the value of t_facturas.fecha_modifica
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Date getFechaModifica() {
        return fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_facturas.fecha_modifica
     * @param fechaModifica  the value for t_facturas.fecha_modifica
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    @Override
    public String toString() {
        return numeroFactura + ", " + obra;
    }

    public String toString2() {
        return "TFacturas [id=" + id + ", numeroFactura=" + numeroFactura + ", fechaFactura=" + fechaFactura + ", empresa=" + empresa + ", obra=" + obra + ", idCliente=" + idCliente + ", idDireccion=" + idDireccion + ", base=" + base + ", descuento="
                + descuento + ", subtotal=" + subtotal + ", totalNeto=" + totalNeto + ", total=" + total + "]";
    }

}