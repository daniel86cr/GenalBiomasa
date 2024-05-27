/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.db.entity;

import java.util.Date;

/**
 * Clase que modela el producto que con la calidad y Kgs disponbibles 
 *
 */
public class Producto {
    /** El ID de la compra para saber los datos, como por ejemplo el lote.*/
    private Integer id;
    /** La calidad del producto. */
    private String  calidad;
    /** Los Kgs disponibles. */
    private Double  kgs;
    /** La fecha de compra. */
    private Date    fechaCompra;

    /**
     * Constructor sin parámetros.
     */
    public Producto() {
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the calidad
     */
    public String getCalidad() {
        return calidad;
    }

    /**
     * @param calidad the calidad to set
     */
    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }

    /**
     * @return the kgs
     */
    public Double getKgs() {
        return kgs;
    }

    /**
     * @param kgs the kgs to set
     */
    public void setKgs(Double kgs) {
        this.kgs = kgs;
    }

    /**
     * @return the fechaCompra
     */
    public Date getFechaCompra() {
        return fechaCompra;
    }

    /**
     * @param fechaCompra the fechaCompra to set
     */
    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    @Override
    public String toString() {
        return "Producto [id=" + id + ", calidad=" + calidad + ", kgs=" + kgs + "]";
    }

}
