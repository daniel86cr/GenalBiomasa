package com.dina.genasoft.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TDireccionCliente;

public interface TDireccionClienteMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int insert(TDireccionCliente record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int insertSelective(TDireccionCliente record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    TDireccionCliente selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int updateByPrimaryKeySelective(TDireccionCliente record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_direccion_cliente
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int updateByPrimaryKey(TDireccionCliente record);

    List<TDireccionCliente> obtenerDireccionesClientePorIdCliente(@Param("idCliente") Integer idCliente);

    List<TDireccionCliente> obtenerTodasDireccionesCliente();
}