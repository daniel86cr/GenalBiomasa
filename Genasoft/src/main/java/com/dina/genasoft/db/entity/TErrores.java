package com.dina.genasoft.db.entity;

import java.io.Serializable;

public class TErrores implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_errores.codigo
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String codigo;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_errores.descripcion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String descripcion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_errores.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_errores
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_errores.codigo
     * @return  the value of t_errores.codigo
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_errores.codigo
     * @param codigo  the value for t_errores.codigo
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_errores.descripcion
     * @return  the value of t_errores.descripcion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_errores.descripcion
     * @param descripcion  the value for t_errores.descripcion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_errores.estado
     * @return  the value of t_errores.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_errores.estado
     * @param estado  the value for t_errores.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}