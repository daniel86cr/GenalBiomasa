package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.ClienteEnum;

public class TClientesVista implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.id
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private String            id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.nombre
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private String            nombre;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.razon_social
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private String            razonSocial;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.cif
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private String            cif;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.usu_crea
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private String            usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.fecha_crea
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private String            fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.usu_modifica
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private String            usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.fecha_modifica
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private String            fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.estado
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private String            estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.estado
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private String            matricula;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes.estado
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private String            remolque;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_clientes
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.id
     * @return  the value of t_clientes.id
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.id
     * @param id  the value for t_clientes.id
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.nombre
     * @return  the value of t_clientes.nombre
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.nombre
     * @param nombre  the value for t_clientes.nombre
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.razon_social
     * @return  the value of t_clientes.razon_social
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.razon_social
     * @param razonSocial  the value for t_clientes.razon_social
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.cif
     * @return  the value of t_clientes.cif
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public String getCif() {
        return cif;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.cif
     * @param cif  the value for t_clientes.cif
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public void setCif(String cif) {
        this.cif = cif;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.usu_crea
     * @return  the value of t_clientes.usu_crea
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public String getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.usu_crea
     * @param usuCrea  the value for t_clientes.usu_crea
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public void setUsuCrea(String usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.fecha_crea
     * @return  the value of t_clientes.fecha_crea
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public String getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.fecha_crea
     * @param fechaCrea  the value for t_clientes.fecha_crea
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public void setFechaCrea(String fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.usu_modifica
     * @return  the value of t_clientes.usu_modifica
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public String getUsuModifica() {
        return usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.usu_modifica
     * @param usuModifica  the value for t_clientes.usu_modifica
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public void setUsuModifica(String usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.fecha_modifica
     * @return  the value of t_clientes.fecha_modifica
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public String getFechaModifica() {
        return fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.fecha_modifica
     * @param fechaModifica  the value for t_clientes.fecha_modifica
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public void setFechaModifica(String fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_clientes.estado
     * @return  the value of t_clientes.estado
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public String getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_clientes.estado
     * @param estado  the value for t_clientes.estado
     * @mbg.generated  Mon May 27 18:48:23 CEST 2024
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the remolque
     */
    public String getRemolque() {
        return remolque;
    }

    /**
     * @param remolque the remolque to set
     */
    public void setRemolque(String remolque) {
        this.remolque = remolque;
    }

    /**
     * Constructor a partir de TClientes
     * @param co El objeto con los datos a nutrir.
     */
    public TClientesVista(TClientes cl) {
        this.cif = Utils.formatearValorString(cl.getCif());
        this.estado = cl.getEstado().equals(ClienteEnum.ACTIVO.getValue()) ? Constants.ACTIVO : Constants.DESACTIVADO;
        this.fechaCrea = new SimpleDateFormat("dd/MM/yyyy hh:mm").format(cl.getFechaCrea());
        this.fechaModifica = cl.getFechaModifica() != null ? new SimpleDateFormat("dd/MM/yyyy hh:mm").format(cl.getFechaModifica()) : "-";
        this.id = "" + cl.getId();
        this.nombre = Utils.formatearValorString(cl.getNombre());
        this.razonSocial = Utils.formatearValorString(cl.getRazonSocial());
        this.usuCrea = "" + cl.getUsuCrea();
        this.usuModifica = "" + cl.getUsuModifica();
        this.matricula = Utils.formatearValorString(cl.getMatricula());
        this.remolque = Utils.formatearValorString(cl.getRemolque());
    }

}