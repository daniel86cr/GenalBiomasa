package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TRegistrosCambiosClientes;

public interface TRegistrosCambiosClientesMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insert(TRegistrosCambiosClientes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insertSelective(TRegistrosCambiosClientes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    TRegistrosCambiosClientes selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKeySelective(TRegistrosCambiosClientes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKey(TRegistrosCambiosClientes record);
}