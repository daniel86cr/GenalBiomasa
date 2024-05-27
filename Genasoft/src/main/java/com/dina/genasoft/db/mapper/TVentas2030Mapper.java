package com.dina.genasoft.db.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dina.genasoft.db.entity.TVentas;
import com.dina.genasoft.db.entity.TVentas2030;

public interface TVentas2030Mapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ventas_2030
     *
     * @mbg.generated Wed Apr 24 13:50:28 CEST 2024
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ventas_2030
     *
     * @mbg.generated Wed Apr 24 13:50:28 CEST 2024
     */
    int insert(TVentas2030 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ventas_2030
     *
     * @mbg.generated Wed Apr 24 13:50:28 CEST 2024
     */
    int insertSelective(TVentas2030 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ventas_2030
     *
     * @mbg.generated Wed Apr 24 13:50:28 CEST 2024
     */
    TVentas2030 selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ventas_2030
     *
     * @mbg.generated Wed Apr 24 13:50:28 CEST 2024
     */
    int updateByPrimaryKeySelective(TVentas2030 record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_ventas_2030
     *
     * @mbg.generated Wed Apr 24 13:50:28 CEST 2024
     */
    int updateByPrimaryKey(TVentas2030 record);

    /**
     * Método para crear la venta y obtener el ID del registro creado.
     * @param map Los datos de la venta a crear.
     */
    public void insertRecord(Map<String, Object> map);

    TVentas obtenerVentaPorId(@Param("id") Integer id);

    TVentas obtenerVentaNumPedidoAlbaranLoteIdPale(@Param("pedido") String idPedido, @Param("albaran") String albaran, @Param("lote") String lote, @Param("loteFin") String loteFin, @Param("idPale") String idPale);

    TVentas obtenerVentaPorIdExterno(@Param("idExterno") Double idExterno);

    TVentas obtenerVentaNumPedidoAlbaranCompraLote(@Param("pedido") String idPedido, @Param("albaran") String albaran, @Param("lote") String lote);

    TVentas obtenerVentaNumPedidoAlbaranCompraLoteIdPale(@Param("pedido") String idPedido, @Param("albaran") String albaran, @Param("lote") String lote, @Param("idPale") String idPale);

    List<TVentas> obtenerTodasVentas();

    List<TVentas> obtenerTodasVentas2();

    List<TVentas> obtenerVentasIds(@Param("list") List<Integer> ids);

    List<String> obtenerNumAlbaranIdsPedidos(@Param("list") List<Integer> ids);

    int abrirVentasIdsVentas(@Param("list") List<Integer> ids);

    int eliminarVentasIds(@Param("list") List<Integer> ids);

    List<TVentas> obtenerVentasCreadasIdLineaVenta(@Param("idVenta") Integer idVenta);

    List<TVentas> obtenerVentasLoteFin(@Param("loteFin") String loteFin);

    List<TVentas> obtenerLineasVentaAlbaran(@Param("albaran") String albaran);

    List<TVentas> obtenerTodasLineasVentaAlbaran(@Param("albaran") String albaran);

    List<TVentas> obtenerVentasParametros(@Param("producto") String producto, @Param("calidad") String calidad, @Param("origen") String origen, @Param("ggn") String ggn, @Param("f1") Date f1, @Param("f2") Date f2, @Param("origen2") String origen2,
            @Param("calidad2") String calidad2);

    List<TVentas> obtenerVentasParametrosGgn(@Param("producto") String producto, @Param("calidad") String calidad, @Param("origen") String origen, @Param("ggn") String ggn, @Param("f1") Date f1, @Param("f2") Date f2, @Param("origen2") String origen2,
            @Param("calidad2") String calidad2);

    int insertarVentasMasivo(@Param("list") List<TVentas> ids);

    Double obtenerKgsCreadas(@Param("idVenta") Integer idVenta);

    Double obtenerKgsLote(@Param("lote") String lote);

    List<TVentas> obtenerVentasProductoBultosKg(@Param("producto") String producto, @Param("variedad") String variedad, @Param("bultos") Integer bultos, @Param("kgs") Double kgs, @Param("calidad") String calidad, @Param("fecha") Date fecha,
            @Param("fecha2") Date fecha2);

    List<TVentas> obtenerVentasProductoKg(@Param("producto") String producto, @Param("variedad") String variedad, @Param("kgs") Double kgs, @Param("calidad") String calidad, @Param("fecha") Date fecha, @Param("fecha2") Date fecha2);

    List<TVentas> obtenerVentasBalanceMasasTodosGroupBy();

    List<TVentas> obtenerVentasCertificacionNacionalImportacion();

    List<TVentas> obtenerVentasGlobalGap();

    List<TVentas> obtenerVentasEjercicio(@Param("year1") Integer year1, @Param("year2") Integer year2);

    List<TVentas> obtenerVentasEjercicio2(@Param("year1") Integer year1, @Param("year2") Integer year2);

    List<TVentas> obtenerVentasFechas(@Param("f1") Date fecha, @Param("f2") Date fecha2);

}