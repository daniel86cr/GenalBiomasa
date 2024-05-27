package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TDireccionCliente implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.id
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.id_cliente
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer idCliente;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.cod_direccion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private String codDireccion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.direccion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private String direccion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.poblacion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private String poblacion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.provincia
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private String provincia;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.pais
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private String pais;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.usu_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.fecha_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Date fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.usu_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Integer usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.fecha_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private Date fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_direccion_cliente.estado
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private String estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_direccion_cliente
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.id
     * @return  the value of t_direccion_cliente.id
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.id
     * @param id  the value for t_direccion_cliente.id
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.id_cliente
     * @return  the value of t_direccion_cliente.id_cliente
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getIdCliente() {
        return idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.id_cliente
     * @param idCliente  the value for t_direccion_cliente.id_cliente
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.cod_direccion
     * @return  the value of t_direccion_cliente.cod_direccion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public String getCodDireccion() {
        return codDireccion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.cod_direccion
     * @param codDireccion  the value for t_direccion_cliente.cod_direccion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setCodDireccion(String codDireccion) {
        this.codDireccion = codDireccion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.direccion
     * @return  the value of t_direccion_cliente.direccion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.direccion
     * @param direccion  the value for t_direccion_cliente.direccion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.poblacion
     * @return  the value of t_direccion_cliente.poblacion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public String getPoblacion() {
        return poblacion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.poblacion
     * @param poblacion  the value for t_direccion_cliente.poblacion
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.provincia
     * @return  the value of t_direccion_cliente.provincia
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.provincia
     * @param provincia  the value for t_direccion_cliente.provincia
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.pais
     * @return  the value of t_direccion_cliente.pais
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public String getPais() {
        return pais;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.pais
     * @param pais  the value for t_direccion_cliente.pais
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.usu_crea
     * @return  the value of t_direccion_cliente.usu_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.usu_crea
     * @param usuCrea  the value for t_direccion_cliente.usu_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.fecha_crea
     * @return  the value of t_direccion_cliente.fecha_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.fecha_crea
     * @param fechaCrea  the value for t_direccion_cliente.fecha_crea
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.usu_modifica
     * @return  the value of t_direccion_cliente.usu_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Integer getUsuModifica() {
        return usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.usu_modifica
     * @param usuModifica  the value for t_direccion_cliente.usu_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setUsuModifica(Integer usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.fecha_modifica
     * @return  the value of t_direccion_cliente.fecha_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public Date getFechaModifica() {
        return fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.fecha_modifica
     * @param fechaModifica  the value for t_direccion_cliente.fecha_modifica
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_direccion_cliente.estado
     * @return  the value of t_direccion_cliente.estado
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public String getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_direccion_cliente.estado
     * @param estado  the value for t_direccion_cliente.estado
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}