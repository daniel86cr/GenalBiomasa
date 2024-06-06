package com.dina.genasoft.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TEmpleados;

public interface TEmpleadosMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insert(TEmpleados record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insertSelective(TEmpleados record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    TEmpleados selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKeySelective(TEmpleados record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empleados
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKey(TEmpleados record);

    TEmpleados obtenerEmpleadoPorNombreUsuario(@Param("nombreUsuario") String nombreUsuario);

    TEmpleados obtenerEmpleadoPorCodAcceso(@Param("codAcceso") String codAcceso);

    TEmpleados obtenerEmpleadoPorNombre(@Param("nombre") String nombre);

    TEmpleados obtenerEmpleadoPorIdExterno(@Param("idExterno") String idExterno);

    List<TEmpleados> obtenerTodosEmpleados();

    List<TEmpleados> obtenerEmpleadosPorRolActivos(@Param("idRol") Integer idRol);
}