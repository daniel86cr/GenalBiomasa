package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TRegistrosCambiosEmpleados;

public interface TRegistrosCambiosEmpleadosMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insert(TRegistrosCambiosEmpleados record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insertSelective(TRegistrosCambiosEmpleados record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    TRegistrosCambiosEmpleados selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKeySelective(TRegistrosCambiosEmpleados record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_registros_cambios_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKey(TRegistrosCambiosEmpleados record);
}