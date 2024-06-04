package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TMateriales implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.id
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.referencia
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private String referencia;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.descripcion
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private String descripcion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.ler
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private String ler;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.id_familia
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private Integer idFamilia;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.precio
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private Double precio;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.iva
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private Integer iva;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.usu_crea
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.fecha_crea
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private Date fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.usu_modifica
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private Integer usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.fecha_modifica
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private Date fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_materiales.estado
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private Integer estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_materiales
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.id
     * @return  the value of t_materiales.id
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.id
     * @param id  the value for t_materiales.id
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.referencia
     * @return  the value of t_materiales.referencia
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.referencia
     * @param referencia  the value for t_materiales.referencia
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.descripcion
     * @return  the value of t_materiales.descripcion
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.descripcion
     * @param descripcion  the value for t_materiales.descripcion
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.ler
     * @return  the value of t_materiales.ler
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public String getLer() {
        return ler;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.ler
     * @param ler  the value for t_materiales.ler
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setLer(String ler) {
        this.ler = ler;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.id_familia
     * @return  the value of t_materiales.id_familia
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public Integer getIdFamilia() {
        return idFamilia;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.id_familia
     * @param idFamilia  the value for t_materiales.id_familia
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setIdFamilia(Integer idFamilia) {
        this.idFamilia = idFamilia;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.precio
     * @return  the value of t_materiales.precio
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.precio
     * @param precio  the value for t_materiales.precio
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.iva
     * @return  the value of t_materiales.iva
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public Integer getIva() {
        return iva;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.iva
     * @param iva  the value for t_materiales.iva
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setIva(Integer iva) {
        this.iva = iva;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.usu_crea
     * @return  the value of t_materiales.usu_crea
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.usu_crea
     * @param usuCrea  the value for t_materiales.usu_crea
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.fecha_crea
     * @return  the value of t_materiales.fecha_crea
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.fecha_crea
     * @param fechaCrea  the value for t_materiales.fecha_crea
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.usu_modifica
     * @return  the value of t_materiales.usu_modifica
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public Integer getUsuModifica() {
        return usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.usu_modifica
     * @param usuModifica  the value for t_materiales.usu_modifica
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setUsuModifica(Integer usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.fecha_modifica
     * @return  the value of t_materiales.fecha_modifica
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public Date getFechaModifica() {
        return fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.fecha_modifica
     * @param fechaModifica  the value for t_materiales.fecha_modifica
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_materiales.estado
     * @return  the value of t_materiales.estado
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_materiales.estado
     * @param estado  the value for t_materiales.estado
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return referencia + ", " + descripcion + ", " + ler;
    }

    public String toString2() {
        return "TMateriales [id=" + id + ", referencia=" + referencia + ", descripcion=" + descripcion + ", ler=" + ler + ", idFamilia=" + idFamilia + ", precio=" + precio + ", iva=" + iva + ", usuCrea=" + usuCrea + ", fechaCrea=" + fechaCrea
                + ", usuModifica=" + usuModifica + ", fechaModifica=" + fechaModifica + ", estado=" + estado + "]";
    }

}