package com.dina.genasoft.db.entity;

import java.io.Serializable;

public class TFacturaPesaje implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_factura_pesaje.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_factura_pesaje.id_factura
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer idFactura;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_factura_pesaje.id_pesaje
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer idPesaje;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_factura_pesaje
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_factura_pesaje.id
     * @return  the value of t_factura_pesaje.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_factura_pesaje.id
     * @param id  the value for t_factura_pesaje.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_factura_pesaje.id_factura
     * @return  the value of t_factura_pesaje.id_factura
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getIdFactura() {
        return idFactura;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_factura_pesaje.id_factura
     * @param idFactura  the value for t_factura_pesaje.id_factura
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_factura_pesaje.id_pesaje
     * @return  the value of t_factura_pesaje.id_pesaje
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getIdPesaje() {
        return idPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_factura_pesaje.id_pesaje
     * @param idPesaje  the value for t_factura_pesaje.id_pesaje
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setIdPesaje(Integer idPesaje) {
        this.idPesaje = idPesaje;
    }
}