package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TTrace;
import org.apache.ibatis.annotations.Param;

public interface TTraceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trace
     *
     * @mbg.generated Tue May 28 15:15:19 CEST 2024
     */
    int deleteByPrimaryKey(@Param("mac") String mac, @Param("cliente") String cliente);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trace
     *
     * @mbg.generated Tue May 28 15:15:19 CEST 2024
     */
    int insert(TTrace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trace
     *
     * @mbg.generated Tue May 28 15:15:19 CEST 2024
     */
    int insertSelective(TTrace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trace
     *
     * @mbg.generated Tue May 28 15:15:19 CEST 2024
     */
    TTrace selectByPrimaryKey(@Param("mac") String mac, @Param("cliente") String cliente);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trace
     *
     * @mbg.generated Tue May 28 15:15:19 CEST 2024
     */
    int updateByPrimaryKeySelective(TTrace record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_trace
     *
     * @mbg.generated Tue May 28 15:15:19 CEST 2024
     */
    int updateByPrimaryKey(TTrace record);
}