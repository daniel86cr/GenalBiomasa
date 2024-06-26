package com.dina.genasoft.db.entity;

import java.io.Serializable;

public class TPermisos implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.id_rol
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer idRol;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.descripcion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private String descripcion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.menu_principal
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer menuPrincipal;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.entorno_maestros
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer entornoMaestros;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.entorno_facturacion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer entornoFacturacion;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.entorno_pesajes
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer entornoPesajes;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.crear_pesaje
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer crearPesaje;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.modificar_pesaje
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer modificarPesaje;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.eliminar_pesaje
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer eliminarPesaje;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.crear_materia
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer crearMateria;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.modificar_material
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer modificarMaterial;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.eliminar_material
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer eliminarMaterial;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.crear_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer crearOperador;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.modificar_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer modificarOperador;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.eliminar_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer eliminarOperador;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.crear_transportista
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer crearTransportista;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.modificar_transportista
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer modificarTransportista;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.eliminar_transportista
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer eliminarTransportista;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.crear_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer crearCliente;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.modificar_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer modificarCliente;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.eliminar_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer eliminarCliente;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.crear_factura
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer crearFactura;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_permisos.eliminar_factura
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer eliminarFactura;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_permisos
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.id_rol
     * @return  the value of t_permisos.id_rol
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getIdRol() {
        return idRol;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.id_rol
     * @param idRol  the value for t_permisos.id_rol
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.descripcion
     * @return  the value of t_permisos.descripcion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.descripcion
     * @param descripcion  the value for t_permisos.descripcion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.menu_principal
     * @return  the value of t_permisos.menu_principal
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getMenuPrincipal() {
        return menuPrincipal;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.menu_principal
     * @param menuPrincipal  the value for t_permisos.menu_principal
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setMenuPrincipal(Integer menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.entorno_maestros
     * @return  the value of t_permisos.entorno_maestros
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEntornoMaestros() {
        return entornoMaestros;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.entorno_maestros
     * @param entornoMaestros  the value for t_permisos.entorno_maestros
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEntornoMaestros(Integer entornoMaestros) {
        this.entornoMaestros = entornoMaestros;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.entorno_facturacion
     * @return  the value of t_permisos.entorno_facturacion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEntornoFacturacion() {
        return entornoFacturacion;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.entorno_facturacion
     * @param entornoFacturacion  the value for t_permisos.entorno_facturacion
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEntornoFacturacion(Integer entornoFacturacion) {
        this.entornoFacturacion = entornoFacturacion;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.entorno_pesajes
     * @return  the value of t_permisos.entorno_pesajes
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEntornoPesajes() {
        return entornoPesajes;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.entorno_pesajes
     * @param entornoPesajes  the value for t_permisos.entorno_pesajes
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEntornoPesajes(Integer entornoPesajes) {
        this.entornoPesajes = entornoPesajes;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.crear_pesaje
     * @return  the value of t_permisos.crear_pesaje
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getCrearPesaje() {
        return crearPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.crear_pesaje
     * @param crearPesaje  the value for t_permisos.crear_pesaje
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setCrearPesaje(Integer crearPesaje) {
        this.crearPesaje = crearPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.modificar_pesaje
     * @return  the value of t_permisos.modificar_pesaje
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getModificarPesaje() {
        return modificarPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.modificar_pesaje
     * @param modificarPesaje  the value for t_permisos.modificar_pesaje
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setModificarPesaje(Integer modificarPesaje) {
        this.modificarPesaje = modificarPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.eliminar_pesaje
     * @return  the value of t_permisos.eliminar_pesaje
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEliminarPesaje() {
        return eliminarPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.eliminar_pesaje
     * @param eliminarPesaje  the value for t_permisos.eliminar_pesaje
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEliminarPesaje(Integer eliminarPesaje) {
        this.eliminarPesaje = eliminarPesaje;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.crear_materia
     * @return  the value of t_permisos.crear_materia
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getCrearMateria() {
        return crearMateria;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.crear_materia
     * @param crearMateria  the value for t_permisos.crear_materia
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setCrearMateria(Integer crearMateria) {
        this.crearMateria = crearMateria;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.modificar_material
     * @return  the value of t_permisos.modificar_material
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getModificarMaterial() {
        return modificarMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.modificar_material
     * @param modificarMaterial  the value for t_permisos.modificar_material
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setModificarMaterial(Integer modificarMaterial) {
        this.modificarMaterial = modificarMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.eliminar_material
     * @return  the value of t_permisos.eliminar_material
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEliminarMaterial() {
        return eliminarMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.eliminar_material
     * @param eliminarMaterial  the value for t_permisos.eliminar_material
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEliminarMaterial(Integer eliminarMaterial) {
        this.eliminarMaterial = eliminarMaterial;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.crear_operador
     * @return  the value of t_permisos.crear_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getCrearOperador() {
        return crearOperador;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.crear_operador
     * @param crearOperador  the value for t_permisos.crear_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setCrearOperador(Integer crearOperador) {
        this.crearOperador = crearOperador;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.modificar_operador
     * @return  the value of t_permisos.modificar_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getModificarOperador() {
        return modificarOperador;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.modificar_operador
     * @param modificarOperador  the value for t_permisos.modificar_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setModificarOperador(Integer modificarOperador) {
        this.modificarOperador = modificarOperador;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.eliminar_operador
     * @return  the value of t_permisos.eliminar_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEliminarOperador() {
        return eliminarOperador;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.eliminar_operador
     * @param eliminarOperador  the value for t_permisos.eliminar_operador
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEliminarOperador(Integer eliminarOperador) {
        this.eliminarOperador = eliminarOperador;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.crear_transportista
     * @return  the value of t_permisos.crear_transportista
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getCrearTransportista() {
        return crearTransportista;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.crear_transportista
     * @param crearTransportista  the value for t_permisos.crear_transportista
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setCrearTransportista(Integer crearTransportista) {
        this.crearTransportista = crearTransportista;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.modificar_transportista
     * @return  the value of t_permisos.modificar_transportista
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getModificarTransportista() {
        return modificarTransportista;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.modificar_transportista
     * @param modificarTransportista  the value for t_permisos.modificar_transportista
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setModificarTransportista(Integer modificarTransportista) {
        this.modificarTransportista = modificarTransportista;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.eliminar_transportista
     * @return  the value of t_permisos.eliminar_transportista
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEliminarTransportista() {
        return eliminarTransportista;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.eliminar_transportista
     * @param eliminarTransportista  the value for t_permisos.eliminar_transportista
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEliminarTransportista(Integer eliminarTransportista) {
        this.eliminarTransportista = eliminarTransportista;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.crear_cliente
     * @return  the value of t_permisos.crear_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getCrearCliente() {
        return crearCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.crear_cliente
     * @param crearCliente  the value for t_permisos.crear_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setCrearCliente(Integer crearCliente) {
        this.crearCliente = crearCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.modificar_cliente
     * @return  the value of t_permisos.modificar_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getModificarCliente() {
        return modificarCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.modificar_cliente
     * @param modificarCliente  the value for t_permisos.modificar_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setModificarCliente(Integer modificarCliente) {
        this.modificarCliente = modificarCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.eliminar_cliente
     * @return  the value of t_permisos.eliminar_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEliminarCliente() {
        return eliminarCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.eliminar_cliente
     * @param eliminarCliente  the value for t_permisos.eliminar_cliente
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEliminarCliente(Integer eliminarCliente) {
        this.eliminarCliente = eliminarCliente;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.crear_factura
     * @return  the value of t_permisos.crear_factura
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getCrearFactura() {
        return crearFactura;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.crear_factura
     * @param crearFactura  the value for t_permisos.crear_factura
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setCrearFactura(Integer crearFactura) {
        this.crearFactura = crearFactura;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_permisos.eliminar_factura
     * @return  the value of t_permisos.eliminar_factura
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getEliminarFactura() {
        return eliminarFactura;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_permisos.eliminar_factura
     * @param eliminarFactura  the value for t_permisos.eliminar_factura
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setEliminarFactura(Integer eliminarFactura) {
        this.eliminarFactura = eliminarFactura;
    }
}