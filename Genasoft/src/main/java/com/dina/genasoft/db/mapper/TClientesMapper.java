package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TClientes;

public interface TClientesMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insert(TClientes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insertSelective(TClientes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    TClientes selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKeySelective(TClientes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKey(TClientes record);
}