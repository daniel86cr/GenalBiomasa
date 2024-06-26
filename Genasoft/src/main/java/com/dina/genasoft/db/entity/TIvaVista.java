package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.utils.enums.IvaEnum;

public class TIvaVista implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_iva.id
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_iva.cod_externo
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            codExterno;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_iva.descripcion
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            descripcion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_iva.importe
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            importe;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_iva.estado
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_iva
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_iva.id
     * @return  the value of t_iva.id
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_iva.id
     * @param id  the value for t_iva.id
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_iva.cod_externo
     * @return  the value of t_iva.cod_externo
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    public String getCodExterno() {
        return codExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_iva.cod_externo
     * @param codExterno  the value for t_iva.cod_externo
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    public void setCodExterno(String codExterno) {
        this.codExterno = codExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_iva.descripcion
     * @return  the value of t_iva.descripcion
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_iva.descripcion
     * @param descripcion  the value for t_iva.descripcion
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_iva.importe
     * @return  the value of t_iva.importe
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    public String getImporte() {
        return importe;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_iva.importe
     * @param importe  the value for t_iva.importe
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    public void setImporte(String importe) {
        this.importe = importe;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_iva.estado
     * @return  the value of t_iva.estado
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    public String getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_iva.estado
     * @param estado  the value for t_iva.estado
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Constructor a partir de TIva
     * @param co El objeto con los datos a nutrir.
     */
    public TIvaVista(TIva iva) {
        DecimalFormat df = new DecimalFormat("#,##0.000");
        this.descripcion = iva.getDescripcion();
        this.estado = iva.getEstado().equals(IvaEnum.ACTIVO.getValue()) ? Constants.ACTIVO : Constants.DESACTIVADO;
        this.id = "" + iva.getId();
        this.importe = df.format(iva.getImporte());
    }
}