package com.dina.genasoft.db.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TControlProductoTerminado;

public interface TControlProductoTerminadoMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_control_producto_terminado
     * @mbg.generated  Wed May 26 19:42:13 CEST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_control_producto_terminado
     * @mbg.generated  Wed May 26 19:42:13 CEST 2021
     */
    int insert(TControlProductoTerminado record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_control_producto_terminado
     * @mbg.generated  Wed May 26 19:42:13 CEST 2021
     */
    int insertSelective(TControlProductoTerminado record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_control_producto_terminado
     * @mbg.generated  Wed May 26 19:42:13 CEST 2021
     */
    TControlProductoTerminado selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_control_producto_terminado
     * @mbg.generated  Wed May 26 19:42:13 CEST 2021
     */
    int updateByPrimaryKeySelective(TControlProductoTerminado record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_control_producto_terminado
     * @mbg.generated  Wed May 26 19:42:13 CEST 2021
     */
    int updateByPrimaryKey(TControlProductoTerminado record);

    /**
     * Método para crear el control de producto terminado y obtener el ID del registro creado.
     * @param map Los datos del producto terminado a crear.
     */
    public void insertRecord(Map<String, Object> map);

    /**
     * Método que nos busca en el sistema el control de producto terminado a partir del número de pedido.
     * @param @numeroPedido El número de pedido por el que realizar la búsqueda.
     * @return El control de producto terminado encontrado.
     */
    TControlProductoTerminado obtenerControlPtPorNumPedido(@Param("numeroPedido") String numeroPedido);

    List<TControlProductoTerminado> obtenerControlPtPorFechas(@Param("f1") Date f1, @Param("f2") Date f2);

    List<TControlProductoTerminado> obtenerControlPtCreados(@Param("user") Integer user, @Param("f1") Date f1, @Param("f2") Date f2);

    List<TControlProductoTerminado> obtenerControlPtIds(@Param("list") List<Integer> ids);

}