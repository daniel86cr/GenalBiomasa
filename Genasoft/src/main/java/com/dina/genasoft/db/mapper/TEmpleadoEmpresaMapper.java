package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TEmpleadoEmpresa;
import org.apache.ibatis.annotations.Param;

public interface TEmpleadoEmpresaMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empleado_empresa
     * @mbg.generated  Sun Jun 02 08:56:59 CEST 2024
     */
    int deleteByPrimaryKey(@Param("idEmpleado") Integer idEmpleado, @Param("idEmpresa") Integer idEmpresa);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empleado_empresa
     * @mbg.generated  Sun Jun 02 08:56:59 CEST 2024
     */
    int insert(TEmpleadoEmpresa record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empleado_empresa
     * @mbg.generated  Sun Jun 02 08:56:59 CEST 2024
     */
    int insertSelective(TEmpleadoEmpresa record);
}