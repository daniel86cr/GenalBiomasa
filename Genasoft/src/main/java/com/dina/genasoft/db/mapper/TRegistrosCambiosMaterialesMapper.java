package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TRegistrosCambiosMateriales;

public interface TRegistrosCambiosMaterialesMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insert(TRegistrosCambiosMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insertSelective(TRegistrosCambiosMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    TRegistrosCambiosMateriales selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKeySelective(TRegistrosCambiosMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_materiales
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKey(TRegistrosCambiosMateriales record);
}