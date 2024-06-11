package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TClientes implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.id
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.nombre
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String nombre;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.razon_social
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String razonSocial;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.cif
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String cif;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.fecha_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Date fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.usu_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.fecha_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Date fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.matricula
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String matricula;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.remolque
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String remolque;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.id_banco
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer idBanco;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.num_cuenta
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String numCuenta;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_clientes
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.id
     * @return  the value of t_clientes.id
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.id
     * @param id  the value for t_clientes.id
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.nombre
     * @return  the value of t_clientes.nombre
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.nombre
     * @param nombre  the value for t_clientes.nombre
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.razon_social
     * @return  the value of t_clientes.razon_social
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.razon_social
     * @param razonSocial  the value for t_clientes.razon_social
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.cif
     * @return  the value of t_clientes.cif
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getCif() {
        return cif;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.cif
     * @param cif  the value for t_clientes.cif
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setCif(String cif) {
        this.cif = cif;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.usu_crea
     * @return  the value of t_clientes.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.usu_crea
     * @param usuCrea  the value for t_clientes.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.fecha_crea
     * @return  the value of t_clientes.fecha_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.fecha_crea
     * @param fechaCrea  the value for t_clientes.fecha_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.usu_modifica
     * @return  the value of t_clientes.usu_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getUsuModifica() {
        return usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.usu_modifica
     * @param usuModifica  the value for t_clientes.usu_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setUsuModifica(Integer usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.fecha_modifica
     * @return  the value of t_clientes.fecha_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Date getFechaModifica() {
        return fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.fecha_modifica
     * @param fechaModifica  the value for t_clientes.fecha_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.estado
     * @return  the value of t_clientes.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.estado
     * @param estado  the value for t_clientes.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.matricula
     * @return  the value of t_clientes.matricula
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.matricula
     * @param matricula  the value for t_clientes.matricula
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.remolque
     * @return  the value of t_clientes.remolque
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getRemolque() {
        return remolque;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.remolque
     * @param remolque  the value for t_clientes.remolque
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setRemolque(String remolque) {
        this.remolque = remolque;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.id_banco
     * @return  the value of t_clientes.id_banco
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getIdBanco() {
        return idBanco;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.id_banco
     * @param idBanco  the value for t_clientes.id_banco
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setIdBanco(Integer idBanco) {
        this.idBanco = idBanco;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.num_cuenta
     * @return  the value of t_clientes.num_cuenta
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getNumCuenta() {
        return numCuenta;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.num_cuenta
     * @param numCuenta  the value for t_clientes.num_cuenta
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setNumCuenta(String numCuenta) {
        this.numCuenta = numCuenta;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String toString2() {
        return "TClientes [id=" + id + ", nombre=" + nombre + ", razonSocial=" + razonSocial + ", cif=" + cif + ", usuCrea=" + usuCrea + ", fechaCrea=" + fechaCrea + ", usuModifica=" + usuModifica + ", fechaModifica=" + fechaModifica + ", estado="
                + estado + "]";
    }

}