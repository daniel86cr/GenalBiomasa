package com.dina.genasoft.db.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TTransportistas;

public interface TTransportistasMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_transportistas
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_transportistas
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    int insert(TTransportistas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_transportistas
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    int insertSelective(TTransportistas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_transportistas
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    TTransportistas selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_transportistas
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    int updateByPrimaryKeySelective(TTransportistas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_transportistas
     * @mbg.generated  Thu Jun 06 15:59:32 CEST 2024
     */
    int updateByPrimaryKey(TTransportistas record);

    /**
     * Método para crear el transportista y obtener el ID del registro creado.
     * @param map Los datos del transportista a crear.
     */
    public void insertRecord(Map<String, Object> map);

    TTransportistas obtenerTransportistaPorNombre(@Param("nombre") String nombre);

    TTransportistas obtenerTransportistaPorCif(@Param("cif") String cif);

    List<TTransportistas> obtenerTodosTransportistas();

    List<TTransportistas> obtenerTransportistasActivos();

    List<TTransportistas> obtenerTransportistasAsignadosCliente(@Param("idCliente") Integer idCliente);
}