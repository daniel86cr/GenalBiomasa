package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TRegistrosCambiosEmpleados implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_empleados.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_empleados.cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private String cambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_empleados.id_empleado
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer idEmpleado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_empleados.usu_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_registros_cambios_empleados.fecha_cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Date fechaCambio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_registros_cambios_empleados
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_empleados.id
     * @return  the value of t_registros_cambios_empleados.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_empleados.id
     * @param id  the value for t_registros_cambios_empleados.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_empleados.cambio
     * @return  the value of t_registros_cambios_empleados.cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public String getCambio() {
        return cambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_empleados.cambio
     * @param cambio  the value for t_registros_cambios_empleados.cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setCambio(String cambio) {
        this.cambio = cambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_empleados.id_empleado
     * @return  the value of t_registros_cambios_empleados.id_empleado
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_empleados.id_empleado
     * @param idEmpleado  the value for t_registros_cambios_empleados.id_empleado
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_empleados.usu_crea
     * @return  the value of t_registros_cambios_empleados.usu_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_empleados.usu_crea
     * @param usuCrea  the value for t_registros_cambios_empleados.usu_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_registros_cambios_empleados.fecha_cambio
     * @return  the value of t_registros_cambios_empleados.fecha_cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Date getFechaCambio() {
        return fechaCambio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_registros_cambios_empleados.fecha_cambio
     * @param fechaCambio  the value for t_registros_cambios_empleados.fecha_cambio
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}