package com.dina.genasoft.db.entity;

import java.io.Serializable;

public class TVariedad implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_variedad.id
     * @mbg.generated  Tue Apr 20 17:55:47 CEST 2021
     */
    private Integer           id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_variedad.descripcion
     * @mbg.generated  Tue Apr 20 17:55:47 CEST 2021
     */
    private String            descripcion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_variedad
     * @mbg.generated  Tue Apr 20 17:55:47 CEST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_variedad.id
     * @return  the value of t_variedad.id
     * @mbg.generated  Tue Apr 20 17:55:47 CEST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_variedad.id
     * @param id  the value for t_variedad.id
     * @mbg.generated  Tue Apr 20 17:55:47 CEST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_variedad.descripcion
     * @return  the value of t_variedad.descripcion
     * @mbg.generated  Tue Apr 20 17:55:47 CEST 2021
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_variedad.descripcion
     * @param descripcion  the value for t_variedad.descripcion
     * @mbg.generated  Tue Apr 20 17:55:47 CEST 2021
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }

}