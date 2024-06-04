package com.dina.genasoft.db.mapper;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TNumeroAlbaran;

public interface TNumeroAlbaranMapper {
    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_numero_albaran
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("yearActual") Integer yearActual, @Param("tipoPesaje") String tipoPesaje);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_numero_albaran
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int insert(TNumeroAlbaran record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_numero_albaran
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int insertSelective(TNumeroAlbaran record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_numero_albaran
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    TNumeroAlbaran selectByPrimaryKey(@Param("id") Integer id, @Param("yearActual") Integer yearActual, @Param("tipoPesaje") String tipoPesaje);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_numero_albaran
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int updateByPrimaryKeySelective(TNumeroAlbaran record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_numero_albaran
     * @mbg.generated  Tue Jun 04 16:18:53 CEST 2024
     */
    int updateByPrimaryKey(TNumeroAlbaran record);

    /**
     * Método que nos retorna el registro de número de albaran a partir del tipo de pedido y el año.
     * @param tipoPesaje El tipo de pesaje
     * @param year El year
     * @return El registro encontrado
     */
    TNumeroAlbaran obtenerNumeroAlbaran(@Param("tipoPesaje") String tipoPesaje, @Param("year") Integer year);
}