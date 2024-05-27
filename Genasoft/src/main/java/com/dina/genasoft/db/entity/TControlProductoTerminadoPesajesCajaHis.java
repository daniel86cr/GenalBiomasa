package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TControlProductoTerminadoPesajesCajaHis implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_control_producto_terminado_pesajes_caja_his.id
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    private Integer           id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_control_producto_terminado_pesajes_caja_his.id_control_producto_terminado
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    private Integer           idControlProductoTerminado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_control_producto_terminado_pesajes_caja_his.id_linea
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    private Integer           idLinea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_control_producto_terminado_pesajes_caja_his.valor
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    private Double            valor;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_control_producto_terminado_pesajes_caja_his.orden
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    private Integer           orden;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_control_producto_terminado_pesajes_caja_his.usu_crea
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    private Integer           usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_control_producto_terminado_pesajes_caja_his.fecha_crea
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    private Date              fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_control_producto_terminado_pesajes_caja_his.usu_modifica
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    private Integer           usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_control_producto_terminado_pesajes_caja_his.fecha_modifica
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    private Date              fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_control_producto_terminado_pesajes_caja_his
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_control_producto_terminado_pesajes_caja_his.id
     * @return  the value of t_control_producto_terminado_pesajes_caja_his.id
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_control_producto_terminado_pesajes_caja_his.id
     * @param id  the value for t_control_producto_terminado_pesajes_caja_his.id
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_control_producto_terminado_pesajes_caja_his.id_control_producto_terminado
     * @return  the value of t_control_producto_terminado_pesajes_caja_his.id_control_producto_terminado
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public Integer getIdControlProductoTerminado() {
        return idControlProductoTerminado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_control_producto_terminado_pesajes_caja_his.id_control_producto_terminado
     * @param idControlProductoTerminado  the value for t_control_producto_terminado_pesajes_caja_his.id_control_producto_terminado
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public void setIdControlProductoTerminado(Integer idControlProductoTerminado) {
        this.idControlProductoTerminado = idControlProductoTerminado;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_control_producto_terminado_pesajes_caja_his.id_linea
     * @return  the value of t_control_producto_terminado_pesajes_caja_his.id_linea
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public Integer getIdLinea() {
        return idLinea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_control_producto_terminado_pesajes_caja_his.id_linea
     * @param idLinea  the value for t_control_producto_terminado_pesajes_caja_his.id_linea
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public void setIdLinea(Integer idLinea) {
        this.idLinea = idLinea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_control_producto_terminado_pesajes_caja_his.valor
     * @return  the value of t_control_producto_terminado_pesajes_caja_his.valor
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public Double getValor() {
        return valor;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_control_producto_terminado_pesajes_caja_his.valor
     * @param valor  the value for t_control_producto_terminado_pesajes_caja_his.valor
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_control_producto_terminado_pesajes_caja_his.orden
     * @return  the value of t_control_producto_terminado_pesajes_caja_his.orden
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public Integer getOrden() {
        return orden;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_control_producto_terminado_pesajes_caja_his.orden
     * @param orden  the value for t_control_producto_terminado_pesajes_caja_his.orden
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_control_producto_terminado_pesajes_caja_his.usu_crea
     * @return  the value of t_control_producto_terminado_pesajes_caja_his.usu_crea
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_control_producto_terminado_pesajes_caja_his.usu_crea
     * @param usuCrea  the value for t_control_producto_terminado_pesajes_caja_his.usu_crea
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_control_producto_terminado_pesajes_caja_his.fecha_crea
     * @return  the value of t_control_producto_terminado_pesajes_caja_his.fecha_crea
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_control_producto_terminado_pesajes_caja_his.fecha_crea
     * @param fechaCrea  the value for t_control_producto_terminado_pesajes_caja_his.fecha_crea
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_control_producto_terminado_pesajes_caja_his.usu_modifica
     * @return  the value of t_control_producto_terminado_pesajes_caja_his.usu_modifica
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public Integer getUsuModifica() {
        return usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_control_producto_terminado_pesajes_caja_his.usu_modifica
     * @param usuModifica  the value for t_control_producto_terminado_pesajes_caja_his.usu_modifica
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public void setUsuModifica(Integer usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_control_producto_terminado_pesajes_caja_his.fecha_modifica
     * @return  the value of t_control_producto_terminado_pesajes_caja_his.fecha_modifica
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public Date getFechaModifica() {
        return fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_control_producto_terminado_pesajes_caja_his.fecha_modifica
     * @param fechaModifica  the value for t_control_producto_terminado_pesajes_caja_his.fecha_modifica
     * @mbg.generated  Fri May 14 18:38:04 CEST 2021
     */
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    /**
     * Método que nos realiza la copia de los campos pasado por parametro a formato String.
     * @param pesCaja El objeto con los datos a copiar.
     */
    public void copiaDesdePesajeCaja(TControlProductoTerminadoPesajesCaja pesCaja) {
        this.fechaCrea = pesCaja.getFechaCrea();
        this.fechaModifica = pesCaja.getFechaModifica();
        this.id = pesCaja.getId();
        this.idControlProductoTerminado = pesCaja.getIdControlProductoTerminado();
        this.orden = pesCaja.getOrden();
        this.usuCrea = pesCaja.getUsuCrea();
        this.usuModifica = pesCaja.getUsuModifica();
        this.valor = pesCaja.getValor();
    }
}