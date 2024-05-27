package com.dina.genasoft.db.mapper;

import com.dina.genasoft.db.entity.TClientesTransportistas;
import org.apache.ibatis.annotations.Param;

public interface TClientesTransportistasMapper {

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_transportistas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int deleteByPrimaryKey(@Param("idCliente") Integer idCliente, @Param("idTransportista") Integer idTransportista);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_transportistas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insert(TClientesTransportistas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_transportistas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int insertSelective(TClientesTransportistas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_transportistas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    TClientesTransportistas selectByPrimaryKey(@Param("idCliente") Integer idCliente, @Param("idTransportista") Integer idTransportista);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_transportistas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKeySelective(TClientesTransportistas record);

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table t_clientes_transportistas
     * @mbg.generated  Mon May 27 18:53:23 CEST 2024
     */
    int updateByPrimaryKey(TClientesTransportistas record);
}