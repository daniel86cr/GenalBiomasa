package com.dina.genasoft.db.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TPesajes;

public interface TPesajesMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int insert(TPesajes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int insertSelective(TPesajes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    TPesajes selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int updateByPrimaryKeySelective(TPesajes record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_pesajes
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int updateByPrimaryKey(TPesajes record);

    /**
     * Método para crear el pesaje y obtener el ID del registro creado.
     * @param map Los datos del pesaje a crear.
     */
    public void insertRecord(Map<String, Object> map);

    List<TPesajes> obtenerPesajesFactura(@Param("idFactura") Integer idFactura);

    List<TPesajes> obtenerPesajesFechas(@Param("fec1") Date fec1, @Param("fec2") Date fec2);

    TPesajes obtenerPesajePorAlbaran(@Param("numeroAlbaran") String numeroAlbaran);
}