package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TPesajesHis;

public interface TPesajesHisMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes_his
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes_his
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int insert(TPesajesHis record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes_his
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int insertSelective(TPesajesHis record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes_his
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    TPesajesHis selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes_his
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int updateByPrimaryKeySelective(TPesajesHis record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes_his
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int updateByPrimaryKey(TPesajesHis record);
}