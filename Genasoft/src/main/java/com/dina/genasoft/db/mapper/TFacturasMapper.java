package com.dina.genasoft.db.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TFacturas;

public interface TFacturasMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_facturas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_facturas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int insert(TFacturas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_facturas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int insertSelective(TFacturas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_facturas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    TFacturas selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_facturas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int updateByPrimaryKeySelective(TFacturas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_facturas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int updateByPrimaryKey(TFacturas record);

    /**
     * Método para crear la factura y obtener el ID del registro creado.
     * @param map Los datos de la factura a crear.
     */
    public void insertRecord(Map<String, Object> map);

    TFacturas obtenerFacturaPorNumeroFactura(@Param("numeroFactura") String num);

    List<TFacturas> obtenerFacturasFechas(@Param("fecha1") Date fecha1, @Param("fecha1") Date fecha2);
}