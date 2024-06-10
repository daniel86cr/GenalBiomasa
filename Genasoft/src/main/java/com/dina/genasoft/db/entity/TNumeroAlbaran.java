package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TNumeroAlbaran implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_numero_albaran.id
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_numero_albaran.year_actual
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Integer yearActual;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_numero_albaran.tipo_pesaje
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private String tipoPesaje;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_numero_albaran.ultimo_numero
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private String ultimoNumero;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_numero_albaran.fecha_ultima_consulta
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private Date fechaUltimaConsulta;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_numero_albaran
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_numero_albaran.id
     * @return  the value of t_numero_albaran.id
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_numero_albaran.id
     * @param id  the value for t_numero_albaran.id
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_numero_albaran.year_actual
     * @return  the value of t_numero_albaran.year_actual
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Integer getYearActual() {
        return yearActual;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_numero_albaran.year_actual
     * @param yearActual  the value for t_numero_albaran.year_actual
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setYearActual(Integer yearActual) {
        this.yearActual = yearActual;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_numero_albaran.tipo_pesaje
     * @return  the value of t_numero_albaran.tipo_pesaje
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public String getTipoPesaje() {
        return tipoPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_numero_albaran.tipo_pesaje
     * @param tipoPesaje  the value for t_numero_albaran.tipo_pesaje
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setTipoPesaje(String tipoPesaje) {
        this.tipoPesaje = tipoPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_numero_albaran.ultimo_numero
     * @return  the value of t_numero_albaran.ultimo_numero
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public String getUltimoNumero() {
        return ultimoNumero;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_numero_albaran.ultimo_numero
     * @param ultimoNumero  the value for t_numero_albaran.ultimo_numero
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setUltimoNumero(String ultimoNumero) {
        this.ultimoNumero = ultimoNumero;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_numero_albaran.fecha_ultima_consulta
     * @return  the value of t_numero_albaran.fecha_ultima_consulta
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public Date getFechaUltimaConsulta() {
        return fechaUltimaConsulta;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_numero_albaran.fecha_ultima_consulta
     * @param fechaUltimaConsulta  the value for t_numero_albaran.fecha_ultima_consulta
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    public void setFechaUltimaConsulta(Date fechaUltimaConsulta) {
        this.fechaUltimaConsulta = fechaUltimaConsulta;
    }
}