package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TClientesTransportistas implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.id_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Integer idCliente;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.id_transportista
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Integer idTransportista;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.impuesto
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Double impuesto;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.precio_kg
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Double precioKg;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.usu_crea
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.fecha_crea
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Date fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.usu_modifica
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Integer usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.fecha_modifica
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Date fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.estado
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Integer estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_clientes_transportistas
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_transportistas.id_cliente
     * @return  the value of t_clientes_transportistas.id_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Integer getIdCliente() {
        return idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_transportistas.id_cliente
     * @param idCliente  the value for t_clientes_transportistas.id_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_transportistas.id_transportista
     * @return  the value of t_clientes_transportistas.id_transportista
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Integer getIdTransportista() {
        return idTransportista;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_transportistas.id_transportista
     * @param idTransportista  the value for t_clientes_transportistas.id_transportista
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setIdTransportista(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_transportistas.impuesto
     * @return  the value of t_clientes_transportistas.impuesto
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Double getImpuesto() {
        return impuesto;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_transportistas.impuesto
     * @param impuesto  the value for t_clientes_transportistas.impuesto
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_transportistas.precio_kg
     * @return  the value of t_clientes_transportistas.precio_kg
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Double getPrecioKg() {
        return precioKg;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_transportistas.precio_kg
     * @param precioKg  the value for t_clientes_transportistas.precio_kg
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setPrecioKg(Double precioKg) {
        this.precioKg = precioKg;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_transportistas.usu_crea
     * @return  the value of t_clientes_transportistas.usu_crea
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_transportistas.usu_crea
     * @param usuCrea  the value for t_clientes_transportistas.usu_crea
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_transportistas.fecha_crea
     * @return  the value of t_clientes_transportistas.fecha_crea
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_transportistas.fecha_crea
     * @param fechaCrea  the value for t_clientes_transportistas.fecha_crea
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_transportistas.usu_modifica
     * @return  the value of t_clientes_transportistas.usu_modifica
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Integer getUsuModifica() {
        return usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_transportistas.usu_modifica
     * @param usuModifica  the value for t_clientes_transportistas.usu_modifica
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setUsuModifica(Integer usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_transportistas.fecha_modifica
     * @return  the value of t_clientes_transportistas.fecha_modifica
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Date getFechaModifica() {
        return fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_transportistas.fecha_modifica
     * @param fechaModifica  the value for t_clientes_transportistas.fecha_modifica
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_transportistas.estado
     * @return  the value of t_clientes_transportistas.estado
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_transportistas.estado
     * @param estado  the value for t_clientes_transportistas.estado
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}