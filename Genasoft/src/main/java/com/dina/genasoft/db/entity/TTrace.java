package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TTrace implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trace.mac
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    private String mac;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trace.cliente
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    private String cliente;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trace.last_check
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    private Date lastCheck;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trace.validez
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    private Date validez;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trace.estado
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    private Integer estado;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_trace.descripcion
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    private String descripcion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_trace
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trace.mac
     *
     * @return the value of t_trace.mac
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public String getMac() {
        return mac;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trace.mac
     *
     * @param mac the value for t_trace.mac
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trace.cliente
     *
     * @return the value of t_trace.cliente
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trace.cliente
     *
     * @param cliente the value for t_trace.cliente
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trace.last_check
     *
     * @return the value of t_trace.last_check
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public Date getLastCheck() {
        return lastCheck;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trace.last_check
     *
     * @param lastCheck the value for t_trace.last_check
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public void setLastCheck(Date lastCheck) {
        this.lastCheck = lastCheck;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trace.validez
     *
     * @return the value of t_trace.validez
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public Date getValidez() {
        return validez;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trace.validez
     *
     * @param validez the value for t_trace.validez
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public void setValidez(Date validez) {
        this.validez = validez;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trace.estado
     *
     * @return the value of t_trace.estado
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trace.estado
     *
     * @param estado the value for t_trace.estado
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_trace.descripcion
     *
     * @return the value of t_trace.descripcion
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_trace.descripcion
     *
     * @param descripcion the value for t_trace.descripcion
     *
     * @mbg.generated Wed Mar 17 17:28:03 CET 2021
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}