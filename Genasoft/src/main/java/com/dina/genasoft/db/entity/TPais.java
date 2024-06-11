package com.dina.genasoft.db.entity;

import java.io.Serializable;

public class TPais implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pais.codigo
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    private String codigo;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pais.nombre
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    private String nombre;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pais.prefijo_teléfono
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    private String prefijoTeléfono;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pais.estado
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    private Integer estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pais.intrastat
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    private Integer intrastat;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_pais
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pais.codigo
     * @return  the value of t_pais.codigo
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pais.codigo
     * @param codigo  the value for t_pais.codigo
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pais.nombre
     * @return  the value of t_pais.nombre
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pais.nombre
     * @param nombre  the value for t_pais.nombre
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pais.prefijo_teléfono
     * @return  the value of t_pais.prefijo_teléfono
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    public String getPrefijoTeléfono() {
        return prefijoTeléfono;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pais.prefijo_teléfono
     * @param prefijoTeléfono  the value for t_pais.prefijo_teléfono
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    public void setPrefijoTeléfono(String prefijoTeléfono) {
        this.prefijoTeléfono = prefijoTeléfono;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pais.estado
     * @return  the value of t_pais.estado
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pais.estado
     * @param estado  the value for t_pais.estado
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pais.intrastat
     * @return  the value of t_pais.intrastat
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    public Integer getIntrastat() {
        return intrastat;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pais.intrastat
     * @param intrastat  the value for t_pais.intrastat
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    public void setIntrastat(Integer intrastat) {
        this.intrastat = intrastat;
    }
}