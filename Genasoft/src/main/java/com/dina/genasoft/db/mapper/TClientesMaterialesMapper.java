package com.dina.genasoft.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TClientesMateriales;

public interface TClientesMaterialesMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_materiales
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int deleteByPrimaryKey(@Param("idCliente") Integer idCliente, @Param("idMaterial") Integer idMaterial);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_materiales
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int insert(TClientesMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_materiales
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int insertSelective(TClientesMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_materiales
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    TClientesMateriales selectByPrimaryKey(@Param("idCliente") Integer idCliente, @Param("idMaterial") Integer idMaterial);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_materiales
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int updateByPrimaryKeySelective(TClientesMateriales record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_materiales
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int updateByPrimaryKey(TClientesMateriales record);

    List<TClientesMateriales> obtenerMaterialesAsociadosCliente(@Param("idCliente") Integer idCliente);
}