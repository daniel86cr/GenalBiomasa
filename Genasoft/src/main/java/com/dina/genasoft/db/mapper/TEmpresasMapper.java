package com.dina.genasoft.db.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TEmpresas;

public interface TEmpresasMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empresas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empresas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int insert(TEmpresas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empresas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int insertSelective(TEmpresas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empresas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    TEmpresas selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empresas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int updateByPrimaryKeySelective(TEmpresas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_empresas
     * @mbg.generated  Wed May 29 13:39:06 CEST 2024
     */
    int updateByPrimaryKey(TEmpresas record);

    /**
     * Método para crear la empresa y obtener el ID del registro creado.
     * @param map Los datos de la empresa a crear.
     */
    public void insertRecord(Map<String, Object> map);

    TEmpresas obtenerEmpresaPorNombre(@Param("nombre") String nombre);

    List<TEmpresas> obtenerTodasEmpresas();

    List<TEmpresas> obtenerEmpresasActivas();
}