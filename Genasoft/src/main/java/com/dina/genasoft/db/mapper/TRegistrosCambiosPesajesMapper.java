package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TRegistrosCambiosPesajes;

public interface TRegistrosCambiosPesajesMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_pesajes
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_pesajes
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    int insert(TRegistrosCambiosPesajes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_pesajes
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    int insertSelective(TRegistrosCambiosPesajes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_pesajes
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    TRegistrosCambiosPesajes selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_pesajes
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    int updateByPrimaryKeySelective(TRegistrosCambiosPesajes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_pesajes
     * @mbg.generated  Tue Jun 11 10:12:58 CEST 2024
     */
    int updateByPrimaryKey(TRegistrosCambiosPesajes record);
}