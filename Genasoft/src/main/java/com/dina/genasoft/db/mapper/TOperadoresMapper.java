package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TOperadores;

public interface TOperadoresMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_operadores
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_operadores
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insert(TOperadores record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_operadores
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insertSelective(TOperadores record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_operadores
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    TOperadores selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_operadores
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKeySelective(TOperadores record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_operadores
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKey(TOperadores record);
}