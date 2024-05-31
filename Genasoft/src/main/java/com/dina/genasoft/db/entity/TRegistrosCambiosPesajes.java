package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TRegistrosCambiosPesajes implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_pesajes.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_pesajes.cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private String cambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_pesajes.id_pesaje
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer idPesaje;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_pesajes.usu_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_pesajes.fecha_cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Date fechaCambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_registros_cambios_pesajes
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_pesajes.id
     * @return  the value of t_registros_cambios_pesajes.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_pesajes.id
     * @param id  the value for t_registros_cambios_pesajes.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_pesajes.cambio
     * @return  the value of t_registros_cambios_pesajes.cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public String getCambio() {
        return cambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_pesajes.cambio
     * @param cambio  the value for t_registros_cambios_pesajes.cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_pesajes.id_pesaje
     * @return  the value of t_registros_cambios_pesajes.id_pesaje
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getIdPesaje() {
        return idPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_pesajes.id_pesaje
     * @param idPesaje  the value for t_registros_cambios_pesajes.id_pesaje
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setIdPesaje(Integer idPesaje) {
        this.idPesaje = idPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_pesajes.usu_crea
     * @return  the value of t_registros_cambios_pesajes.usu_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_pesajes.usu_crea
     * @param usuCrea  the value for t_registros_cambios_pesajes.usu_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_pesajes.fecha_cambio
     * @return  the value of t_registros_cambios_pesajes.fecha_cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Date getFechaCambio() {
        return fechaCambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_pesajes.fecha_cambio
     * @param fechaCambio  the value for t_registros_cambios_pesajes.fecha_cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}