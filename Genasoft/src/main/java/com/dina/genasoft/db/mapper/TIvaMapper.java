package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TIva;

public interface TIvaMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insert(TIva record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insertSelective(TIva record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    TIva selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKeySelective(TIva record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKey(TIva record);
}