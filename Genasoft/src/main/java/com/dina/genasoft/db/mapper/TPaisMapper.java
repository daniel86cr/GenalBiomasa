package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TPais;

public interface TPaisMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pais
     * @mbg.generated  Tue Feb 16 11:44:14 CET 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pais
     * @mbg.generated  Tue Feb 16 11:44:14 CET 2021
     */
    int insert(TPais record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pais
     * @mbg.generated  Tue Feb 16 11:44:14 CET 2021
     */
    int insertSelective(TPais record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pais
     * @mbg.generated  Tue Feb 16 11:44:14 CET 2021
     */
    TPais selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pais
     * @mbg.generated  Tue Feb 16 11:44:14 CET 2021
     */
    int updateByPrimaryKeySelective(TPais record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pais
     * @mbg.generated  Tue Feb 16 11:44:14 CET 2021
     */
    int updateByPrimaryKey(TPais record);
}