package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TPesajes;

public interface TPesajesMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insert(TPesajes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insertSelective(TPesajes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    TPesajes selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKeySelective(TPesajes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKey(TPesajes record);
}