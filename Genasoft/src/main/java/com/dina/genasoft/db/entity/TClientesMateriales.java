package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TClientesMateriales implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_materiales.id_cliente
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer idCliente;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_materiales.id_material
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer idMaterial;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_materiales.iva
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer iva;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_materiales.precio_kg
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Double precioKg;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_materiales.usu_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_materiales.fecha_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Date fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_materiales.usu_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_materiales.fecha_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Date fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_materiales.estado
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_clientes_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_materiales.id_cliente
     * @return  the value of t_clientes_materiales.id_cliente
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getIdCliente() {
        return idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_materiales.id_cliente
     * @param idCliente  the value for t_clientes_materiales.id_cliente
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_materiales.id_material
     * @return  the value of t_clientes_materiales.id_material
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getIdMaterial() {
        return idMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_materiales.id_material
     * @param idMaterial  the value for t_clientes_materiales.id_material
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setIdMaterial(Integer idMaterial) {
        this.idMaterial = idMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_materiales.iva
     * @return  the value of t_clientes_materiales.iva
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getIva() {
        return iva;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_materiales.iva
     * @param iva  the value for t_clientes_materiales.iva
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setIva(Integer iva) {
        this.iva = iva;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_materiales.precio_kg
     * @return  the value of t_clientes_materiales.precio_kg
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Double getPrecioKg() {
        return precioKg;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_materiales.precio_kg
     * @param precioKg  the value for t_clientes_materiales.precio_kg
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setPrecioKg(Double precioKg) {
        this.precioKg = precioKg;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_materiales.usu_crea
     * @return  the value of t_clientes_materiales.usu_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_materiales.usu_crea
     * @param usuCrea  the value for t_clientes_materiales.usu_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_materiales.fecha_crea
     * @return  the value of t_clientes_materiales.fecha_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_materiales.fecha_crea
     * @param fechaCrea  the value for t_clientes_materiales.fecha_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_materiales.usu_modifica
     * @return  the value of t_clientes_materiales.usu_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getUsuModifica() {
        return usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_materiales.usu_modifica
     * @param usuModifica  the value for t_clientes_materiales.usu_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setUsuModifica(Integer usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_materiales.fecha_modifica
     * @return  the value of t_clientes_materiales.fecha_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Date getFechaModifica() {
        return fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_materiales.fecha_modifica
     * @param fechaModifica  the value for t_clientes_materiales.fecha_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_materiales.estado
     * @return  the value of t_clientes_materiales.estado
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_materiales.estado
     * @param estado  the value for t_clientes_materiales.estado
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}