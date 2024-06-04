package com.dina.genasoft.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TLineasFactura;

public interface TLineasFacturaMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_factura
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_factura
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int insert(TLineasFactura record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_factura
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int insertSelective(TLineasFactura record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_factura
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    TLineasFactura selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_factura
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int updateByPrimaryKeySelective(TLineasFactura record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_lineas_factura
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int updateByPrimaryKey(TLineasFactura record);

    List<TLineasFactura> obtenerLineasFacturaPorIdFactura(@Param("idFactura") Integer idFactura);
}