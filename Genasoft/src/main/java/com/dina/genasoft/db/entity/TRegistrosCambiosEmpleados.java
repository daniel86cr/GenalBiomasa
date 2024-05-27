package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TRegistrosCambiosEmpleados implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_registros_cambios_empleados.id
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    private Integer           id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_registros_cambios_empleados.id_empleado
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    private Integer           idEmpleado;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_registros_cambios_empleados.fecha
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    private Date              fecha;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_registros_cambios_empleados.descripcion
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    private String            descripcion;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_registros_cambios_empleados.usu_registro
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    private Integer           usuRegistro;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_registros_cambios_empleados
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_registros_cambios_empleados.id
     *
     * @return the value of t_registros_cambios_empleados.id
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_registros_cambios_empleados.id
     *
     * @param id the value for t_registros_cambios_empleados.id
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_registros_cambios_empleados.id_empleado
     *
     * @return the value of t_registros_cambios_empleados.id_empleado
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_registros_cambios_empleados.id_empleado
     *
     * @param idEmpleado the value for t_registros_cambios_empleados.id_empleado
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_registros_cambios_empleados.fecha
     *
     * @return the value of t_registros_cambios_empleados.fecha
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_registros_cambios_empleados.fecha
     *
     * @param fecha the value for t_registros_cambios_empleados.fecha
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_registros_cambios_empleados.descripcion
     *
     * @return the value of t_registros_cambios_empleados.descripcion
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_registros_cambios_empleados.descripcion
     *
     * @param descripcion the value for t_registros_cambios_empleados.descripcion
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_registros_cambios_empleados.usu_registro
     *
     * @return the value of t_registros_cambios_empleados.usu_registro
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    public Integer getUsuRegistro() {
        return usuRegistro;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_registros_cambios_empleados.usu_registro
     *
     * @param usuRegistro the value for t_registros_cambios_empleados.usu_registro
     *
     * @mbg.generated Wed Mar 17 18:24:26 CET 2021
     */
    public void setUsuRegistro(Integer usuRegistro) {
        this.usuRegistro = usuRegistro;
    }

    @Override
    public String toString() {
        return "TRegistrosCambiosEmpleados [id=" + id + ", idEmpleado=" + idEmpleado + ", fecha=" + fecha + ", descripcion=" + descripcion + ", usuRegistro=" + usuRegistro + "]";
    }

}