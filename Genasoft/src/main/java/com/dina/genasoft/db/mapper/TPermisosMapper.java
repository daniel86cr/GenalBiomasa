package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TPermisos;

public interface TPermisosMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_permisos
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int deleteByPrimaryKey(Integer idRol);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_permisos
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insert(TPermisos record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_permisos
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insertSelective(TPermisos record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_permisos
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    TPermisos selectByPrimaryKey(Integer idRol);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_permisos
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKeySelective(TPermisos record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_permisos
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKey(TPermisos record);
}