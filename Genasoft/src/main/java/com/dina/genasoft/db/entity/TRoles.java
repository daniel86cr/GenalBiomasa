package com.dina.genasoft.db.entity;

import java.io.Serializable;

public class TRoles implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_roles.id
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_roles.descripcion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private String descripcion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_roles.estado
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_roles
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_roles.id
     * @return  the value of t_roles.id
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_roles.id
     * @param id  the value for t_roles.id
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_roles.descripcion
     * @return  the value of t_roles.descripcion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_roles.descripcion
     * @param descripcion  the value for t_roles.descripcion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_roles.estado
     * @return  the value of t_roles.estado
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_roles.estado
     * @param estado  the value for t_roles.estado
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}