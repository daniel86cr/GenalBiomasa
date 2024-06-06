package com.dina.genasoft.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TRoles;

public interface TRolesMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_roles
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_roles
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insert(TRoles record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_roles
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insertSelective(TRoles record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_roles
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    TRoles selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_roles
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKeySelective(TRoles record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_roles
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKey(TRoles record);

    TRoles obtenerRolPorNombre(@Param("nombre") String nombre);

    List<TRoles> obtenerRolesActivosSinMaster();

    List<TRoles> obtenerTodosRoles();
}