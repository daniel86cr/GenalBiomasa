package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TErrores;

public interface TErroresMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_errores
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    int deleteByPrimaryKey(String codigo);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_errores
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    int insert(TErrores record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_errores
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    int insertSelective(TErrores record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_errores
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    TErrores selectByPrimaryKey(String codigo);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_errores
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    int updateByPrimaryKeySelective(TErrores record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_errores
     * @mbg.generated  Fri May 31 17:18:41 CEST 2024
     */
    int updateByPrimaryKey(TErrores record);
}