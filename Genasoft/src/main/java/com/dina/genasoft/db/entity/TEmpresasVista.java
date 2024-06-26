package com.dina.genasoft.db.entity;

import java.io.Serializable;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.enums.EmpresaEnum;

public class TEmpresasVista implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empresas.id
     * @mbg.generated  Wed May 29 11:51:53 CEST 2024
     */
    private String            id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empresas.nombre
     * @mbg.generated  Wed May 29 11:51:53 CEST 2024
     */
    private String            nombre;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empresas.estado
     * @mbg.generated  Wed May 29 11:51:53 CEST 2024
     */
    private String            estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_empresas
     * @mbg.generated  Wed May 29 11:51:53 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empresas.id
     * @return  the value of t_empresas.id
     * @mbg.generated  Wed May 29 11:51:53 CEST 2024
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empresas.id
     * @param id  the value for t_empresas.id
     * @mbg.generated  Wed May 29 11:51:53 CEST 2024
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empresas.nombre
     * @return  the value of t_empresas.nombre
     * @mbg.generated  Wed May 29 11:51:53 CEST 2024
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empresas.nombre
     * @param nombre  the value for t_empresas.nombre
     * @mbg.generated  Wed May 29 11:51:53 CEST 2024
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empresas.estado
     * @return  the value of t_empresas.estado
     * @mbg.generated  Wed May 29 11:51:53 CEST 2024
     */
    public String getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empresas.estado
     * @param estado  the value for t_empresas.estado
     * @mbg.generated  Wed May 29 11:51:53 CEST 2024
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Constructor a partir de TEmpresas
     * @param e El objeto con los datos a nutrir.
     */
    public TEmpresasVista(TEmpresas e) {
        this.estado = e.getEstado().equals(EmpresaEnum.ACTIVO.getValue()) ? Constants.ACTIVO : Constants.DESACTIVADO;
        this.id = "" + e.getId();
        this.nombre = Utils.formatearValorString(e.getNombre());
    }
}