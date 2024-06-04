package com.dina.genasoft.db.mapper;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TAcceso;

public interface TAccesoMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_acceso
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int deleteByPrimaryKey(Integer idAcceso);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_acceso
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int insert(TAcceso record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_acceso
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int insertSelective(TAcceso record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_acceso
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    TAcceso selectByPrimaryKey(Integer idAcceso);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_acceso
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int updateByPrimaryKeySelective(TAcceso record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_acceso
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int updateByPrimaryKey(TAcceso record);

    int eliminarAccesoEmpleado(@Param("idEmpleado") Integer idEmpleado);

    TAcceso obtenerAccesoEmpleado(@Param("idEmpleado") Integer idEmpleado);
}