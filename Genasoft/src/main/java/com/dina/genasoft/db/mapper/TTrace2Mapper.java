package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TTrace2;

public interface TTrace2Mapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_trace_2
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int deleteByPrimaryKey(Integer intentos);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_trace_2
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insert(TTrace2 record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_trace_2
     * @mbg.generated  Thu Jun 06 19:19:25 CEST 2024
     */
    int insertSelective(TTrace2 record);

    TTrace2 obtenerIntentos();

    int eliminarRegistros();
}