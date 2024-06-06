package com.dina.genasoft.db.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TMateriales;

public interface TMaterialesMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insert(TMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insertSelective(TMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    TMateriales selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKeySelective(TMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_materiales
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKey(TMateriales record);

    /**
     * Método para crear el material y obtener el ID del registro creado.
     * @param map Los datos del material a crear.
     */
    public void insertRecord(Map<String, Object> map);

    TMateriales obtenerMaterialPorNombre(@Param("nombre") String nombre);

    List<TMateriales> obtenerTodosMateriales();

    List<TMateriales> obtenerMaterialesActivos();

    List<TMateriales> obtenerMaterialesAsignadosCliente(@Param("idCliente") Integer idCliente);
}