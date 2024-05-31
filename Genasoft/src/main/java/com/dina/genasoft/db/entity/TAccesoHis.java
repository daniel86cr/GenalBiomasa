package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TAccesoHis implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_acceso_his.id_acceso
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer idAcceso;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_acceso_his.id_empleado
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer idEmpleado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_acceso_his.fecha
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Date fecha;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_acceso_his.fecha_acceso
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Date fechaAcceso;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_acceso_his
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_acceso_his.id_acceso
     * @return  the value of t_acceso_his.id_acceso
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getIdAcceso() {
        return idAcceso;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_acceso_his.id_acceso
     * @param idAcceso  the value for t_acceso_his.id_acceso
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setIdAcceso(Integer idAcceso) {
        this.idAcceso = idAcceso;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_acceso_his.id_empleado
     * @return  the value of t_acceso_his.id_empleado
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_acceso_his.id_empleado
     * @param idEmpleado  the value for t_acceso_his.id_empleado
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_acceso_his.fecha
     * @return  the value of t_acceso_his.fecha
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_acceso_his.fecha
     * @param fecha  the value for t_acceso_his.fecha
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_acceso_his.fecha_acceso
     * @return  the value of t_acceso_his.fecha_acceso
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Date getFechaAcceso() {
        return fechaAcceso;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_acceso_his.fecha_acceso
     * @param fechaAcceso  the value for t_acceso_his.fecha_acceso
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setFechaAcceso(Date fechaAcceso) {
        this.fechaAcceso = fechaAcceso;
    }
}