package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TRegistrosCambiosEmpresas;

public interface TRegistrosCambiosEmpresasMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empresas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empresas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insert(TRegistrosCambiosEmpresas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empresas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insertSelective(TRegistrosCambiosEmpresas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empresas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    TRegistrosCambiosEmpresas selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empresas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKeySelective(TRegistrosCambiosEmpresas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empresas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKey(TRegistrosCambiosEmpresas record);
}