package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TMateriales;

public interface TMaterialesMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insert(TMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insertSelective(TMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    TMateriales selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKeySelective(TMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKey(TMateriales record);
}