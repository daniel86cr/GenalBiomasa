package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TFacturaPesaje;

public interface TFacturaPesajeMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_factura_pesaje
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_factura_pesaje
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int insert(TFacturaPesaje record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_factura_pesaje
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int insertSelective(TFacturaPesaje record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_factura_pesaje
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    TFacturaPesaje selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_factura_pesaje
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int updateByPrimaryKeySelective(TFacturaPesaje record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_factura_pesaje
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int updateByPrimaryKey(TFacturaPesaje record);
}