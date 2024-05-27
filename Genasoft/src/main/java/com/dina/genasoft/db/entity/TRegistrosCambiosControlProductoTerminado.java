package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TRegistrosCambiosControlProductoTerminado implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_registros_cambios_control_producto_terminado.id
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    private Integer           id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_registros_cambios_control_producto_terminado.id_control_pt
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    private Integer           idControlPt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_registros_cambios_control_producto_terminado.fecha
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    private Date              fecha;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_registros_cambios_control_producto_terminado.descripcion
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    private String            descripcion;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_registros_cambios_control_producto_terminado.usu_registro
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    private Integer           usuRegistro;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_registros_cambios_control_producto_terminado
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_registros_cambios_control_producto_terminado.id
     *
     * @return the value of t_registros_cambios_control_producto_terminado.id
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_registros_cambios_control_producto_terminado.id
     *
     * @param id the value for t_registros_cambios_control_producto_terminado.id
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_registros_cambios_control_producto_terminado.id_control_pt
     *
     * @return the value of t_registros_cambios_control_producto_terminado.id_control_pt
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    public Integer getIdControlPt() {
        return idControlPt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_registros_cambios_control_producto_terminado.id_control_pt
     *
     * @param idControlPt the value for t_registros_cambios_control_producto_terminado.id_control_pt
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    public void setIdControlPt(Integer idControlPt) {
        this.idControlPt = idControlPt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_registros_cambios_control_producto_terminado.fecha
     *
     * @return the value of t_registros_cambios_control_producto_terminado.fecha
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_registros_cambios_control_producto_terminado.fecha
     *
     * @param fecha the value for t_registros_cambios_control_producto_terminado.fecha
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_registros_cambios_control_producto_terminado.descripcion
     *
     * @return the value of t_registros_cambios_control_producto_terminado.descripcion
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_registros_cambios_control_producto_terminado.descripcion
     *
     * @param descripcion the value for t_registros_cambios_control_producto_terminado.descripcion
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_registros_cambios_control_producto_terminado.usu_registro
     *
     * @return the value of t_registros_cambios_control_producto_terminado.usu_registro
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    public Integer getUsuRegistro() {
        return usuRegistro;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_registros_cambios_control_producto_terminado.usu_registro
     *
     * @param usuRegistro the value for t_registros_cambios_control_producto_terminado.usu_registro
     *
     * @mbg.generated Tue Apr 06 16:35:25 CEST 2021
     */
    public void setUsuRegistro(Integer usuRegistro) {
        this.usuRegistro = usuRegistro;
    }

    @Override
    public String toString() {
        return "TRegistrosCambiosControlProductoTerminado [id=" + id + ", idControlPt=" + idControlPt + ", fecha=" + fecha + ", descripcion=" + descripcion + ", usuRegistro=" + usuRegistro + "]";
    }

}