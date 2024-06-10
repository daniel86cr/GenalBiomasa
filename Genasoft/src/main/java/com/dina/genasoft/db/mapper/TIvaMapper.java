package com.dina.genasoft.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TIva;

public interface TIvaMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int insert(TIva record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int insertSelective(TIva record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    TIva selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int updateByPrimaryKeySelective(TIva record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_iva
     * @mbg.generated  Mon Jun 10 10:49:02 CEST 2024
     */
    int updateByPrimaryKey(TIva record);

    /**
     * Método que nos busca el IVA a partir de la descripción.
     * @param descripcion La descripción por la cual realizar la búsqueda.
     * @return El IVA encontrado.
     */
    TIva obtenerIvaPorDescripcion(@Param("descripcion") String descripcion);

    /**
     * Método que nos retorna los diferentes IVA activos en el sistema.
     * @return Los IVA encontrados.
     */
    List<TIva> obtenerIvaActivos();

    String obtenerValorIva(@Param("id") Integer id);

    /**
     * Método que nos retorna los diferentes IVA existentes en el sistema.
     * @return Los IVA encontrados.
     */
    List<TIva> obtenerTodosIva();
}