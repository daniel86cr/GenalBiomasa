package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TPesajesHis implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.numero_albaran
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private String numeroAlbaran;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.id_cliente
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer idCliente;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.id_direccion
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer idDireccion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.fecha_pesaje
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Date fechaPesaje;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.obra
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private String obra;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.origen
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private String origen;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.destino
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private String destino;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.id_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer idMaterial;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.ref_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private String refMaterial;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.ler_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private String lerMaterial;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.desc_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private String descMaterial;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.kgs_bruto
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Double kgsBruto;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.kgs_neto
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Double kgsNeto;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.tara
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Double tara;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.matricula
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private String matricula;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.remolque
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private String remolque;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.usu_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.fecha_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Date fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.usu_modifica
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.fecha_modifica
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Date fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.id_factura
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer idFactura;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_pesajes_his.estado
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private Integer estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_pesajes_his
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.id
     * @return  the value of t_pesajes_his.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.id
     * @param id  the value for t_pesajes_his.id
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.numero_albaran
     * @return  the value of t_pesajes_his.numero_albaran
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public String getNumeroAlbaran() {
        return numeroAlbaran;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.numero_albaran
     * @param numeroAlbaran  the value for t_pesajes_his.numero_albaran
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setNumeroAlbaran(String numeroAlbaran) {
        this.numeroAlbaran = numeroAlbaran;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.id_cliente
     * @return  the value of t_pesajes_his.id_cliente
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getIdCliente() {
        return idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.id_cliente
     * @param idCliente  the value for t_pesajes_his.id_cliente
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.id_direccion
     * @return  the value of t_pesajes_his.id_direccion
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getIdDireccion() {
        return idDireccion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.id_direccion
     * @param idDireccion  the value for t_pesajes_his.id_direccion
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setIdDireccion(Integer idDireccion) {
        this.idDireccion = idDireccion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.fecha_pesaje
     * @return  the value of t_pesajes_his.fecha_pesaje
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Date getFechaPesaje() {
        return fechaPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.fecha_pesaje
     * @param fechaPesaje  the value for t_pesajes_his.fecha_pesaje
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setFechaPesaje(Date fechaPesaje) {
        this.fechaPesaje = fechaPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.obra
     * @return  the value of t_pesajes_his.obra
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public String getObra() {
        return obra;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.obra
     * @param obra  the value for t_pesajes_his.obra
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setObra(String obra) {
        this.obra = obra;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.origen
     * @return  the value of t_pesajes_his.origen
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.origen
     * @param origen  the value for t_pesajes_his.origen
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.destino
     * @return  the value of t_pesajes_his.destino
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public String getDestino() {
        return destino;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.destino
     * @param destino  the value for t_pesajes_his.destino
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setDestino(String destino) {
        this.destino = destino;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.id_material
     * @return  the value of t_pesajes_his.id_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getIdMaterial() {
        return idMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.id_material
     * @param idMaterial  the value for t_pesajes_his.id_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setIdMaterial(Integer idMaterial) {
        this.idMaterial = idMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.ref_material
     * @return  the value of t_pesajes_his.ref_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public String getRefMaterial() {
        return refMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.ref_material
     * @param refMaterial  the value for t_pesajes_his.ref_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setRefMaterial(String refMaterial) {
        this.refMaterial = refMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.ler_material
     * @return  the value of t_pesajes_his.ler_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public String getLerMaterial() {
        return lerMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.ler_material
     * @param lerMaterial  the value for t_pesajes_his.ler_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setLerMaterial(String lerMaterial) {
        this.lerMaterial = lerMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.desc_material
     * @return  the value of t_pesajes_his.desc_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public String getDescMaterial() {
        return descMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.desc_material
     * @param descMaterial  the value for t_pesajes_his.desc_material
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setDescMaterial(String descMaterial) {
        this.descMaterial = descMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.kgs_bruto
     * @return  the value of t_pesajes_his.kgs_bruto
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Double getKgsBruto() {
        return kgsBruto;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.kgs_bruto
     * @param kgsBruto  the value for t_pesajes_his.kgs_bruto
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setKgsBruto(Double kgsBruto) {
        this.kgsBruto = kgsBruto;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.kgs_neto
     * @return  the value of t_pesajes_his.kgs_neto
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Double getKgsNeto() {
        return kgsNeto;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.kgs_neto
     * @param kgsNeto  the value for t_pesajes_his.kgs_neto
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setKgsNeto(Double kgsNeto) {
        this.kgsNeto = kgsNeto;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.tara
     * @return  the value of t_pesajes_his.tara
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Double getTara() {
        return tara;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.tara
     * @param tara  the value for t_pesajes_his.tara
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setTara(Double tara) {
        this.tara = tara;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.matricula
     * @return  the value of t_pesajes_his.matricula
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.matricula
     * @param matricula  the value for t_pesajes_his.matricula
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.remolque
     * @return  the value of t_pesajes_his.remolque
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public String getRemolque() {
        return remolque;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.remolque
     * @param remolque  the value for t_pesajes_his.remolque
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setRemolque(String remolque) {
        this.remolque = remolque;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.usu_crea
     * @return  the value of t_pesajes_his.usu_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.usu_crea
     * @param usuCrea  the value for t_pesajes_his.usu_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.fecha_crea
     * @return  the value of t_pesajes_his.fecha_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.fecha_crea
     * @param fechaCrea  the value for t_pesajes_his.fecha_crea
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.usu_modifica
     * @return  the value of t_pesajes_his.usu_modifica
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getUsuModifica() {
        return usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.usu_modifica
     * @param usuModifica  the value for t_pesajes_his.usu_modifica
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setUsuModifica(Integer usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.fecha_modifica
     * @return  the value of t_pesajes_his.fecha_modifica
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Date getFechaModifica() {
        return fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.fecha_modifica
     * @param fechaModifica  the value for t_pesajes_his.fecha_modifica
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.id_factura
     * @return  the value of t_pesajes_his.id_factura
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getIdFactura() {
        return idFactura;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.id_factura
     * @param idFactura  the value for t_pesajes_his.id_factura
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_pesajes_his.estado
     * @return  the value of t_pesajes_his.estado
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_pesajes_his.estado
     * @param estado  the value for t_pesajes_his.estado
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}