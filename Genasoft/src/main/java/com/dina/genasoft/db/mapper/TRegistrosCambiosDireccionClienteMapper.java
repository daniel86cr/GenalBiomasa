package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TRegistrosCambiosDireccionCliente;

public interface TRegistrosCambiosDireccionClienteMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int insert(TRegistrosCambiosDireccionCliente record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int insertSelective(TRegistrosCambiosDireccionCliente record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    TRegistrosCambiosDireccionCliente selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int updateByPrimaryKeySelective(TRegistrosCambiosDireccionCliente record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int updateByPrimaryKey(TRegistrosCambiosDireccionCliente record);
}