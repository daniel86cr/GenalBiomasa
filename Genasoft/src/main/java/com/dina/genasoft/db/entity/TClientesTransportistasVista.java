package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.dina.genasoft.configuration.Constants;
import com.dina.genasoft.utils.enums.ClienteEnum;

public class TClientesTransportistasVista implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.id_cliente
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private Integer           idCliente;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.id_cliente
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            nombreCliente;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.id_transportista
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private Integer           idTransportista;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.id_transportista
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            nombreTransportista;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.impuesto
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            impuesto;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.precio_kg
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            precioKg;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.usu_crea
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.usu_crea
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            usuarioCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.fecha_crea
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.usu_modifica
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_operadores.usu_crea
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            usuarioModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.fecha_modifica
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_clientes_transportistas.estado
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private String            estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_clientes_transportistas
     * @mbg.generated  Tue May 28 16:07:02 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return the idCliente
     */
    public Integer getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @return the nombreCliente
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * @param nombreCliente the nombreCliente to set
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /**
     * @return the idTransportista
     */
    public Integer getIdTransportista() {
        return idTransportista;
    }

    /**
     * @param idTransportista the idTransportista to set
     */
    public void setIdTransportista(Integer idTransportista) {
        this.idTransportista = idTransportista;
    }

    /**
     * @return the nombreTransportista
     */
    public String getNombreTransportista() {
        return nombreTransportista;
    }

    /**
     * @param nombreTransportista the nombreTransportista to set
     */
    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
    }

    /**
     * @return the impuesto
     */
    public String getImpuesto() {
        return impuesto;
    }

    /**
     * @param impuesto the impuesto to set
     */
    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    /**
     * @return the precioKg
     */
    public String getPrecioKg() {
        return precioKg;
    }

    /**
     * @param precioKg the precioKg to set
     */
    public void setPrecioKg(String precioKg) {
        this.precioKg = precioKg;
    }

    /**
     * @return the usuCrea
     */
    public String getUsuCrea() {
        return usuCrea;
    }

    /**
     * @param usuCrea the usuCrea to set
     */
    public void setUsuCrea(String usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * @return the usuarioCrea
     */
    public String getUsuarioCrea() {
        return usuarioCrea;
    }

    /**
     * @param usuarioCrea the usuarioCrea to set
     */
    public void setUsuarioCrea(String usuarioCrea) {
        this.usuarioCrea = usuarioCrea;
    }

    /**
     * @return the fechaCrea
     */
    public String getFechaCrea() {
        return fechaCrea;
    }

    /**
     * @param fechaCrea the fechaCrea to set
     */
    public void setFechaCrea(String fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * @return the usuModifica
     */
    public String getUsuModifica() {
        return usuModifica;
    }

    /**
     * @param usuModifica the usuModifica to set
     */
    public void setUsuModifica(String usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * @return the usuarioModifica
     */
    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    /**
     * @param usuarioModifica the usuarioModifica to set
     */
    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    /**
     * @return the fechaModifica
     */
    public String getFechaModifica() {
        return fechaModifica;
    }

    /**
     * @param fechaModifica the fechaModifica to set
     */
    public void setFechaModifica(String fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TClientesTransportistasVista(TClientesTransportistas ct) {
        DecimalFormat df = new DecimalFormat("#,##0.000");
        this.estado = ct.getEstado().equals(ClienteEnum.ACTIVO.getValue()) ? Constants.ACTIVO : Constants.DESACTIVADO;
        this.fechaCrea = new SimpleDateFormat("dd/MM/yyyy hh:mm").format(ct.getFechaCrea());
        this.fechaModifica = ct.getFechaModifica() != null ? new SimpleDateFormat("dd/MM/yyyy hh:mm").format(ct.getFechaModifica()) : "-";
        this.idCliente = ct.getIdCliente();
        this.idTransportista = ct.getIdTransportista();
        this.nombreCliente = "";
        this.nombreTransportista = "";
        this.precioKg = df.format(ct.getPrecioKg());
        this.usuarioCrea = "";
        this.usuarioModifica = "";
        this.usuCrea = "" + ct.getUsuCrea();
        this.usuModifica = "" + ct.getUsuModifica();
    }
}