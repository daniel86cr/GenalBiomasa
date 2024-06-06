package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TAcceso implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_acceso.id_acceso
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Integer idAcceso;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_acceso.id_empleado
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Integer idEmpleado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_acceso.fecha
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Date fecha;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_acceso.fecha_ult_acceso
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private Date fechaUltAcceso;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_acceso
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_acceso.id_acceso
     * @return  the value of t_acceso.id_acceso
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Integer getIdAcceso() {
        return idAcceso;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_acceso.id_acceso
     * @param idAcceso  the value for t_acceso.id_acceso
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setIdAcceso(Integer idAcceso) {
        this.idAcceso = idAcceso;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_acceso.id_empleado
     * @return  the value of t_acceso.id_empleado
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_acceso.id_empleado
     * @param idEmpleado  the value for t_acceso.id_empleado
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_acceso.fecha
     * @return  the value of t_acceso.fecha
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_acceso.fecha
     * @param fecha  the value for t_acceso.fecha
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_acceso.fecha_ult_acceso
     * @return  the value of t_acceso.fecha_ult_acceso
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public Date getFechaUltAcceso() {
        return fechaUltAcceso;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_acceso.fecha_ult_acceso
     * @param fechaUltAcceso  the value for t_acceso.fecha_ult_acceso
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    public void setFechaUltAcceso(Date fechaUltAcceso) {
        this.fechaUltAcceso = fechaUltAcceso;
    }
}