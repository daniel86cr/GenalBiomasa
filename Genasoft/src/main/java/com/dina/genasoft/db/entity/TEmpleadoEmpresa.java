package com.dina.genasoft.db.entity;

import java.io.Serializable;

public class TEmpleadoEmpresa implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleado_empresa.id_empleado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer idEmpleado;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column t_empleado_empresa.id_empresa
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private Integer idEmpresa;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table t_empleado_empresa
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleado_empresa.id_empleado
     * @return  the value of t_empleado_empresa.id_empleado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleado_empresa.id_empleado
     * @param idEmpleado  the value for t_empleado_empresa.id_empleado
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column t_empleado_empresa.id_empresa
     * @return  the value of t_empleado_empresa.id_empresa
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column t_empleado_empresa.id_empresa
     * @param idEmpresa  the value for t_empleado_empresa.id_empresa
     * @mbg.generated  Tue Jun 11 19:48:23 CEST 2024
     */
    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
}