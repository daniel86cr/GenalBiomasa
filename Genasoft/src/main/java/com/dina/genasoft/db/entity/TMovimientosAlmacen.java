package com.dina.genasoft.db.entity;

import java.io.Serializable;

public class TMovimientosAlmacen implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_movimientos_almacen.id
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    private Double            id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_movimientos_almacen.id_palet
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    private Double            idPalet;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_movimientos_almacen.id_movimeinto_venta
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    private Double            idMovimeintoVenta;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_movimientos_almacen.id_venta
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    private Integer           idVenta;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_movimientos_almacen
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_movimientos_almacen.id
     * @return  the value of t_movimientos_almacen.id
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    public Double getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_movimientos_almacen.id
     * @param id  the value for t_movimientos_almacen.id
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    public void setId(Double id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_movimientos_almacen.id_palet
     * @return  the value of t_movimientos_almacen.id_palet
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    public Double getIdPalet() {
        return idPalet;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_movimientos_almacen.id_palet
     * @param idPalet  the value for t_movimientos_almacen.id_palet
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    public void setIdPalet(Double idPalet) {
        this.idPalet = idPalet;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_movimientos_almacen.id_movimeinto_venta
     * @return  the value of t_movimientos_almacen.id_movimeinto_venta
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    public Double getIdMovimeintoVenta() {
        return idMovimeintoVenta;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_movimientos_almacen.id_movimeinto_venta
     * @param idMovimeintoVenta  the value for t_movimientos_almacen.id_movimeinto_venta
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    public void setIdMovimeintoVenta(Double idMovimeintoVenta) {
        this.idMovimeintoVenta = idMovimeintoVenta;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_movimientos_almacen.id_venta
     * @return  the value of t_movimientos_almacen.id_venta
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    public Integer getIdVenta() {
        return idVenta;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_movimientos_almacen.id_venta
     * @param idVenta  the value for t_movimientos_almacen.id_venta
     * @mbg.generated  Wed Jul 20 22:50:48 CEST 2022
     */
    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    @Override
    public String toString() {
        return "TMovimientosAlmacen [idPalet=" + idPalet + ", idMovimeintoVenta=" + idMovimeintoVenta + ", id=" + id + "]";
    }

}