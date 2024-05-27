package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TVariedades implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_variedades.descripcion
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    private String descripcion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_variedades.usu_crea
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_variedades.fecha_crea
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    private Date fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_variedades.estado
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    private Integer estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_variedades
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_variedades.descripcion
     * @return  the value of t_variedades.descripcion
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_variedades.descripcion
     * @param descripcion  the value for t_variedades.descripcion
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_variedades.usu_crea
     * @return  the value of t_variedades.usu_crea
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_variedades.usu_crea
     * @param usuCrea  the value for t_variedades.usu_crea
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_variedades.fecha_crea
     * @return  the value of t_variedades.fecha_crea
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_variedades.fecha_crea
     * @param fechaCrea  the value for t_variedades.fecha_crea
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_variedades.estado
     * @return  the value of t_variedades.estado
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_variedades.estado
     * @param estado  the value for t_variedades.estado
     * @mbg.generated  Fri Mar 19 13:26:28 CET 2021
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}