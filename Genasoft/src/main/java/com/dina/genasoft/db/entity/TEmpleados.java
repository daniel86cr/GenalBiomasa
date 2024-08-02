package com.dina.genasoft.db.entity;

import java.io.Serializable;
import java.util.Date;

public class TEmpleados implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.id
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.nombre_usuario
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String nombreUsuario;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.id_externo
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String idExterno;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.password
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String password;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.codigo_acceso
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String codigoAcceso;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.nombre
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String nombre;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.dni
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String dni;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.telefono
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String telefono;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.pais
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String pais;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.email
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String email;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.id_rol
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer idRol;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer usuCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.fecha_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Date fechaCrea;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.usu_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer usuModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.fecha_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Date fechaModifica;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleados.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer estado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_empleados
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.id
     * @return  the value of t_empleados.id
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.id
     * @param id  the value for t_empleados.id
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.nombre_usuario
     * @return  the value of t_empleados.nombre_usuario
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.nombre_usuario
     * @param nombreUsuario  the value for t_empleados.nombre_usuario
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.id_externo
     * @return  the value of t_empleados.id_externo
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getIdExterno() {
        return idExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.id_externo
     * @param idExterno  the value for t_empleados.id_externo
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setIdExterno(String idExterno) {
        this.idExterno = idExterno;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.password
     * @return  the value of t_empleados.password
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.password
     * @param password  the value for t_empleados.password
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.codigo_acceso
     * @return  the value of t_empleados.codigo_acceso
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getCodigoAcceso() {
        return codigoAcceso;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.codigo_acceso
     * @param codigoAcceso  the value for t_empleados.codigo_acceso
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setCodigoAcceso(String codigoAcceso) {
        this.codigoAcceso = codigoAcceso;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.nombre
     * @return  the value of t_empleados.nombre
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.nombre
     * @param nombre  the value for t_empleados.nombre
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.dni
     * @return  the value of t_empleados.dni
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getDni() {
        return dni;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.dni
     * @param dni  the value for t_empleados.dni
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.telefono
     * @return  the value of t_empleados.telefono
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.telefono
     * @param telefono  the value for t_empleados.telefono
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.pais
     * @return  the value of t_empleados.pais
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getPais() {
        return pais;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.pais
     * @param pais  the value for t_empleados.pais
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.email
     * @return  the value of t_empleados.email
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.email
     * @param email  the value for t_empleados.email
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.id_rol
     * @return  the value of t_empleados.id_rol
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getIdRol() {
        return idRol;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.id_rol
     * @param idRol  the value for t_empleados.id_rol
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.usu_crea
     * @return  the value of t_empleados.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getUsuCrea() {
        return usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.usu_crea
     * @param usuCrea  the value for t_empleados.usu_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setUsuCrea(Integer usuCrea) {
        this.usuCrea = usuCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.fecha_crea
     * @return  the value of t_empleados.fecha_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Date getFechaCrea() {
        return fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.fecha_crea
     * @param fechaCrea  the value for t_empleados.fecha_crea
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.usu_modifica
     * @return  the value of t_empleados.usu_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getUsuModifica() {
        return usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.usu_modifica
     * @param usuModifica  the value for t_empleados.usu_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setUsuModifica(Integer usuModifica) {
        this.usuModifica = usuModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.fecha_modifica
     * @return  the value of t_empleados.fecha_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Date getFechaModifica() {
        return fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.fecha_modifica
     * @param fechaModifica  the value for t_empleados.fecha_modifica
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleados.estado
     * @return  the value of t_empleados.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleados.estado
     * @param estado  the value for t_empleados.estado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombreUsuario + ": " + nombre;
    }

    public String toString2() {
        return "TEmpleados [id=" + id + ", nombreUsuario=" + nombreUsuario + ", idExterno=" + idExterno + ", password=" + password + ", codigoAcceso=" + codigoAcceso + ", nombre=" + nombre + ", dni=" + dni + ", telefono=" + telefono + ", pais="
                + pais + ", email=" + email + ", idRol=" + idRol + ", usuCrea=" + usuCrea + ", fechaCrea=" + fechaCrea + ", usuModifica=" + usuModifica + ", fechaModifica=" + fechaModifica + ", estado=" + estado + "]";
    }

}