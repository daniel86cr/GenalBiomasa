package com.dina.genasoft.db.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TLineasVentas;

public interface TLineasVentasMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_ventas
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_ventas
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    int insert(TLineasVentas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_ventas
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    int insertSelective(TLineasVentas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_ventas
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    TLineasVentas selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_ventas
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    int updateByPrimaryKeySelective(TLineasVentas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_ventas
     * @mbg.generated  Sat May 04 11:41:18 CEST 2024
     */
    int updateByPrimaryKey(TLineasVentas record);

    /**
     * Método para crear la línea de venta y obtener el ID del registro creado.
     * @param map Los datos de la línea de venta a crear.
     */
    public void insertRecord(Map<String, Object> map);

    /**
     * Método que nos retorna la línea de trazabilidad a partir del id de la venta y el lote, que debe ser único dentro de las trazabilidades
     * @param idVenta
     * @return
     */
    TLineasVentas obtenerLineaVentaBasuraIdVentaLoteFin(@Param("idVenta") Double idVenta, @Param("loteFin") String loteFin);

    TLineasVentas obtenerLineaVentaPorIdExterno(@Param("idExterno") Double idExterno);

    Double obtenerKgsCorrectosVenta(@Param("idVenta") Double idVenta);

    List<TLineasVentas> obtenerLineasVentasIdPedido(@Param("idVenta") Double idVenta);

    List<TLineasVentas> obtenerLineasVentasLotes(@Param("list") List<String> ids);

    int eliminarLineasVentasIdVenta(@Param("list") List<Integer> ids);

    List<TLineasVentas> obtenerLineasVentaSinVenta();

}