package com.dina.genasoft.db.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TClientes;

public interface TClientesMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insert(TClientes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insertSelective(TClientes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    TClientes selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKeySelective(TClientes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int updateByPrimaryKey(TClientes record);

    /**
     * Método para crear el cliente y obtener el ID del registro creado.
     * @param map Los datos del cliente a crear.
     */
    public void insertRecord(Map<String, Object> map);

    TClientes obtenerClientePorNombre(@Param("nombre") String nombre);

    TClientes obtenerClientePorCif(@Param("cif") String cif);

    List<TClientes> obtenerTodosClientes();

    List<TClientes> obtenerClientesActivos();
}