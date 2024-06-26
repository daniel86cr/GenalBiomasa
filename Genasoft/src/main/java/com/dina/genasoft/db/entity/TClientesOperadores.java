package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TClientesOperadores implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.id_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer idCliente;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.id_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer idOperador;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.impuesto
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Double impuesto;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.precio_kg
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Double precioKg;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.fecha_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Date fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.usu_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.fecha_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Date fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_clientes_operadores
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_operadores.id_cliente
     * @return  the value of t_clientes_operadores.id_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getIdCliente() {
        return idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_operadores.id_cliente
     * @param idCliente  the value for t_clientes_operadores.id_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_operadores.id_operador
     * @return  the value of t_clientes_operadores.id_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getIdOperador() {
        return idOperador;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_operadores.id_operador
     * @param idOperador  the value for t_clientes_operadores.id_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setIdOperador(Integer idOperador) {
        this.idOperador = idOperador;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_operadores.impuesto
     * @return  the value of t_clientes_operadores.impuesto
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Double getImpuesto() {
        return impuesto;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_operadores.impuesto
     * @param impuesto  the value for t_clientes_operadores.impuesto
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_operadores.precio_kg
     * @return  the value of t_clientes_operadores.precio_kg
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Double getPrecioKg() {
        return precioKg;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_operadores.precio_kg
     * @param precioKg  the value for t_clientes_operadores.precio_kg
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setPrecioKg(Double precioKg) {
        this.precioKg = precioKg;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_operadores.usu_crea
     * @return  the value of t_clientes_operadores.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_operadores.usu_crea
     * @param usuCrea  the value for t_clientes_operadores.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_operadores.fecha_crea
     * @return  the value of t_clientes_operadores.fecha_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_operadores.fecha_crea
     * @param fechaCrea  the value for t_clientes_operadores.fecha_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_operadores.usu_modifica
     * @return  the value of t_clientes_operadores.usu_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getUsuModifica() {
        return usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_operadores.usu_modifica
     * @param usuModifica  the value for t_clientes_operadores.usu_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setUsuModifica(Integer usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_operadores.fecha_modifica
     * @return  the value of t_clientes_operadores.fecha_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Date getFechaModifica() {
        return fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_operadores.fecha_modifica
     * @param fechaModifica  the value for t_clientes_operadores.fecha_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes_operadores.estado
     * @return  the value of t_clientes_operadores.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes_operadores.estado
     * @param estado  the value for t_clientes_operadores.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}