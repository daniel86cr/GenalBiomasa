package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TRegistrosCambiosTransportistas implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_transportistas.id
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_transportistas.cambio
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private String cambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_transportistas.id_transportista
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Integer idTransportista;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_transportistas.usu_crea
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_transportistas.fecha_cambio
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Date fechaCambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_registros_cambios_transportistas
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_transportistas.id
     * @return  the value of t_registros_cambios_transportistas.id
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_transportistas.id
     * @param id  the value for t_registros_cambios_transportistas.id
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_transportistas.cambio
     * @return  the value of t_registros_cambios_transportistas.cambio
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public String getCambio() {
        return cambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_transportistas.cambio
     * @param cambio  the value for t_registros_cambios_transportistas.cambio
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_transportistas.id_transportista
     * @return  the value of t_registros_cambios_transportistas.id_transportista
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Integer getIdTransportista() {
        return idTransportista;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_transportistas.id_transportista
     * @param idTransportista  the value for t_registros_cambios_transportistas.id_transportista
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setIdTransportista(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_transportistas.usu_crea
     * @return  the value of t_registros_cambios_transportistas.usu_crea
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_transportistas.usu_crea
     * @param usuCrea  the value for t_registros_cambios_transportistas.usu_crea
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_transportistas.fecha_cambio
     * @return  the value of t_registros_cambios_transportistas.fecha_cambio
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Date getFechaCambio() {
        return fechaCambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_transportistas.fecha_cambio
     * @param fechaCambio  the value for t_registros_cambios_transportistas.fecha_cambio
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}