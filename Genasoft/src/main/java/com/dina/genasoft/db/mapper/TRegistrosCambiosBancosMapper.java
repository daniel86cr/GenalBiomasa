package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TRegistrosCambiosBancos;

public interface TRegistrosCambiosBancosMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_bancos
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_bancos
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int insert(TRegistrosCambiosBancos record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_bancos
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int insertSelective(TRegistrosCambiosBancos record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_bancos
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    TRegistrosCambiosBancos selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_bancos
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int updateByPrimaryKeySelective(TRegistrosCambiosBancos record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_bancos
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int updateByPrimaryKey(TRegistrosCambiosBancos record);
}