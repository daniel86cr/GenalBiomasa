package com.dina.genasoft.db.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TProductoVariedad;

public interface TProductoVariedadMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_producto_variedad
     *
     * @mbg.generated Fri Mar 19 13:25:12 CET 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_producto_variedad
     *
     * @mbg.generated Fri Mar 19 13:25:12 CET 2021
     */
    int insert(TProductoVariedad record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_producto_variedad
     *
     * @mbg.generated Fri Mar 19 13:25:12 CET 2021
     */
    int insertSelective(TProductoVariedad record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_producto_variedad
     *
     * @mbg.generated Fri Mar 19 13:25:12 CET 2021
     */
    TProductoVariedad selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_producto_variedad
     *
     * @mbg.generated Fri Mar 19 13:25:12 CET 2021
     */
    int updateByPrimaryKeySelective(TProductoVariedad record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_producto_variedad
     *
     * @mbg.generated Fri Mar 19 13:25:12 CET 2021
     */
    int updateByPrimaryKey(TProductoVariedad record);

    List<TProductoVariedad> obtenerVariedadesProducto(@Param("idProducto") Integer idProducto);
}