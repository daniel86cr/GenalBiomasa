package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TRegistrosCambiosOperadores implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_operadores.id
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_operadores.cambio
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    private String cambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_operadores.id_operador
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    private Integer idOperador;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_operadores.usu_crea
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_operadores.fecha_cambio
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    private Date fechaCambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_registros_cambios_operadores
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_operadores.id
     * @return  the value of t_registros_cambios_operadores.id
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_operadores.id
     * @param id  the value for t_registros_cambios_operadores.id
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_operadores.cambio
     * @return  the value of t_registros_cambios_operadores.cambio
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    public String getCambio() {
        return cambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_operadores.cambio
     * @param cambio  the value for t_registros_cambios_operadores.cambio
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_operadores.id_operador
     * @return  the value of t_registros_cambios_operadores.id_operador
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    public Integer getIdOperador() {
        return idOperador;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_operadores.id_operador
     * @param idOperador  the value for t_registros_cambios_operadores.id_operador
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    public void setIdOperador(Integer idOperador) {
        this.idOperador = idOperador;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_operadores.usu_crea
     * @return  the value of t_registros_cambios_operadores.usu_crea
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_operadores.usu_crea
     * @param usuCrea  the value for t_registros_cambios_operadores.usu_crea
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_operadores.fecha_cambio
     * @return  the value of t_registros_cambios_operadores.fecha_cambio
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    public Date getFechaCambio() {
        return fechaCambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_operadores.fecha_cambio
     * @param fechaCambio  the value for t_registros_cambios_operadores.fecha_cambio
     * @mbg.generated  Mon Jun 10 16:23:22 CEST 2024
     */
    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}