package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TRegistrosCambiosDireccionCliente implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_direccion_cliente.id
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_direccion_cliente.cambio
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String cambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_direccion_cliente.id_direccion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer idDireccion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_direccion_cliente.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_direccion_cliente.fecha_cambio
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Date fechaCambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_registros_cambios_direccion_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_direccion_cliente.id
     * @return  the value of t_registros_cambios_direccion_cliente.id
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_direccion_cliente.id
     * @param id  the value for t_registros_cambios_direccion_cliente.id
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_direccion_cliente.cambio
     * @return  the value of t_registros_cambios_direccion_cliente.cambio
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getCambio() {
        return cambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_direccion_cliente.cambio
     * @param cambio  the value for t_registros_cambios_direccion_cliente.cambio
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_direccion_cliente.id_direccion
     * @return  the value of t_registros_cambios_direccion_cliente.id_direccion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getIdDireccion() {
        return idDireccion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_direccion_cliente.id_direccion
     * @param idDireccion  the value for t_registros_cambios_direccion_cliente.id_direccion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setIdDireccion(Integer idDireccion) {
        this.idDireccion = idDireccion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_direccion_cliente.usu_crea
     * @return  the value of t_registros_cambios_direccion_cliente.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_direccion_cliente.usu_crea
     * @param usuCrea  the value for t_registros_cambios_direccion_cliente.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_direccion_cliente.fecha_cambio
     * @return  the value of t_registros_cambios_direccion_cliente.fecha_cambio
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Date getFechaCambio() {
        return fechaCambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_direccion_cliente.fecha_cambio
     * @param fechaCambio  the value for t_registros_cambios_direccion_cliente.fecha_cambio
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}