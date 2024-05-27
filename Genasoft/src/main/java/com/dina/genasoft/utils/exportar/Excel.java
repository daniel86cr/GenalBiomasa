/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 * Copyright (C) 2018
 */
package com.dina.genasoft.utils.exportar;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.dina.genasoft.common.EmpleadosSetup;
import com.dina.genasoft.common.ImportadorSetup;
import com.dina.genasoft.controller.ControladorVistas;
import com.dina.genasoft.db.entity.TColumnasTablasEmpleado;
import com.dina.genasoft.db.entity.TCompras;
import com.dina.genasoft.db.entity.TComprasFict;
import com.dina.genasoft.db.entity.TLineasVentas;
import com.dina.genasoft.db.entity.TVentas;
import com.dina.genasoft.db.entity.TVentasFict;
import com.dina.genasoft.db.entity.TVentasVista;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.vistas.comprasVentas.VistaTrazabilidades;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero
 * Clase que se generar un fichero Excel con datos solicitados.
 */
@Component
@Slf4j
@Data
public class Excel {
    /** Inyección de Spring para poder acceder a la capa del controlador de vistas.*/
    @Autowired
    private ControladorVistas contrVista;
    /** Inyección de Spring para poder acceder a la capa de empleados.*/
    @Autowired
    private EmpleadosSetup    empleadosSetup;
    /** Inyección de Spring para poder acceder a la capa de importación.*/
    @Autowired
    private ImportadorSetup   importadorSetup;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String            appName;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${pdf.temp}")
    private String            pdfTemp;

    /**
     * Método que nos exporta a Excel los datos que se están mostrando en la pantalla de trazabilidades.
     * @param idPedidoCompra El ID del empleado que realiza la petición
     * @param filename El nombre del fichero, el path para generar el fichero.
     * @throws BrosException 
     */
    public ModelAndView exportarTrazabilidades(HttpServletRequest request, HttpServletResponse response) throws GenasoftException {
        // Obtenemos los parámetros del filtro para exportar a Excel

        String idEmpleado = !request.getParameter("idEmpleado").equals("null") ? request.getParameter("idEmpleado") : null;

        Map<String, Object> model = new HashMap<String, Object>();
        //Sheet Name
        model.put("sheetname", "Compras Vs Ventas");

        //TEmpleados empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(idEmpleado));

        List<Integer> lIdsCompras = Utils.generarListaGenerica();

        lIdsCompras.addAll(contrVista.obtenerListadoComprasTrazabilidadesIdEmpleado(Integer.valueOf(idEmpleado)));

        List<Integer> lIdsVentas = Utils.generarListaGenerica();
        lIdsVentas.addAll(contrVista.obtenerListadoVentasTrazabilidadesIdEmpleado(Integer.valueOf(idEmpleado)));

        List<TCompras> lCompras = Utils.generarListaGenerica();

        List<TVentas> lVentas = Utils.generarListaGenerica();
        List<TLineasVentas> lLineas = Utils.generarListaGenerica();

        List<Double> lTotalesCompras = Utils.generarListaGenerica();
        lTotalesCompras.addAll(contrVista.obtenerTotalesListadoComprasTrazabilidadesIdEmpleado(Integer.valueOf(idEmpleado)));
        List<Double> lTotalesVentas = Utils.generarListaGenerica();
        lTotalesVentas.addAll(contrVista.obtenerTotalesListadoVentasTrazabilidadesIdEmpleado(Integer.valueOf(idEmpleado)));
        List<TColumnasTablasEmpleado> lColumnasCompras = empleadosSetup.obtenerCamposPantallaTablaEmpleado(Integer.valueOf(idEmpleado), VistaTrazabilidades.NAME, 1);
        List<TColumnasTablasEmpleado> lColumnasVentas = empleadosSetup.obtenerCamposPantallaTablaEmpleado(Integer.valueOf(idEmpleado), VistaTrazabilidades.NAME, 2);

        Map<Integer, List<String>> mFiltros = new HashMap<Integer, List<String>>(contrVista.obtenerFiltroTrazabilidades(Integer.valueOf(idEmpleado)));

        if (lIdsCompras.isEmpty() && lIdsVentas.isEmpty()) {
            return null;
        } else {
            if (!lIdsCompras.isEmpty()) {

                lCompras = importadorSetup.obtenerComprasIds(lIdsCompras);
            }

            if (!lIdsVentas.isEmpty()) {
                lVentas = importadorSetup.obtenerVentasIds(lIdsVentas);
            }

            List<String> lLotes = Utils.generarListaGenerica();
            // Buscamos las líneas de venta en función de las compras (lote)
            for (TCompras compra : lCompras) {
                if (!lLotes.contains(compra.getLoteFin())) {
                    lLotes.add(compra.getLoteFin());
                }
            }

            if (!lIdsVentas.isEmpty()) {
                lLineas = importadorSetup.obtenerLineasVentasEjercicioIdsVentas(lIdsVentas);
            }

            String nombreLogo = pdfTemp + "lnT.png";

            model.put("compras", lCompras);
            model.put("ventas", lVentas);
            model.put("lineas", lLineas);
            model.put("colsCompras", lColumnasCompras);
            model.put("colsVentas", lColumnasVentas);
            model.put("totCompras", lTotalesCompras);
            model.put("totVentas", lTotalesVentas);
            model.put("filtros", mFiltros);
            model.put("nombreLogo", nombreLogo);

            /**
            // Ordenamos por el campo nombreArticulo
            Collections.sort(lLineasPedidoPtVista, new Comparator<TLineasPedidoPtVista>() {
                @Override
                public int compare(TLineasPedidoPtVista record1, TLineasPedidoPtVista record2) {
            
                    return record1.getIdPedido().compareTo(record2.getIdPedido());
                }
            });
            */
            String nombre = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Utils.generarFecha());
            nombre = appName.replaceAll(" ", "_") + "_TRAZABILIDADES_" + nombre + ".xls";
            response.setContentType("application/ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + nombre);

            // Limpiamos los diccionarios
            contrVista.mTrazabilidadesComprasEmpleado.get(Integer.valueOf(idEmpleado)).clear();
            contrVista.mTotalesTrazabilidadesVentasEmpleado.get(Integer.valueOf(idEmpleado)).clear();
            contrVista.mTotalesTrazabilidadesComprasEmpleado.get(Integer.valueOf(idEmpleado)).clear();
            contrVista.mTotalesTrazabilidadesVentasEmpleado.get(Integer.valueOf(idEmpleado)).clear();

            return new ModelAndView(new TrazabilidadesExcel(), model);

        }
    }

    /**
     * Método que nos exporta a Excel los datos que se están mostrando en la pantalla de trazabilidades.
     * @param idPedidoCompra El ID del empleado que realiza la petición
     * @param filename El nombre del fichero, el path para generar el fichero.
     * @throws BrosException 
     */
    public ModelAndView exportarComprasVentasExcel(HttpServletRequest request, HttpServletResponse response) throws GenasoftException {
        // Obtenemos los parámetros del filtro para exportar a Excel

        String idEmpleado = !request.getParameter("idEmpleado").equals("null") ? request.getParameter("idEmpleado") : null;

        Map<String, Object> model = new HashMap<String, Object>();
        //Sheet Name
        model.put("sheetname", "Compras Vs Ventas");

        //TEmpleados empl = empleadosSetup.obtenerEmpleadoPorId(Integer.valueOf(idEmpleado));

        List<Integer> lIdsCompras = Utils.generarListaGenerica();

        lIdsCompras.addAll(contrVista.obtenerListadoComprasComprasVentasIdEmpleado(Integer.valueOf(idEmpleado)));

        List<Integer> lIdsVentas = Utils.generarListaGenerica();
        lIdsVentas.addAll(contrVista.obtenerListadoVentasComprasVentasIdEmpleado(Integer.valueOf(idEmpleado)));

        List<TComprasFict> lCompras = Utils.generarListaGenerica();

        List<TVentasFict> lVentas = Utils.generarListaGenerica();

        List<Double> lTotalesCompras = Utils.generarListaGenerica();
        lTotalesCompras.addAll(contrVista.obtenerTotalesListadoComprasComprasVentasIdEmpleado(Integer.valueOf(idEmpleado)));
        List<Double> lTotalesVentas = Utils.generarListaGenerica();
        lTotalesVentas.addAll(contrVista.obtenerTotalesListadoVentasComprasVentasIdEmpleado(Integer.valueOf(idEmpleado)));
        List<TColumnasTablasEmpleado> lColumnasCompras = empleadosSetup.obtenerCamposPantallaTablaEmpleado(Integer.valueOf(idEmpleado), VistaTrazabilidades.NAME, 1);
        List<TColumnasTablasEmpleado> lColumnasVentas = empleadosSetup.obtenerCamposPantallaTablaEmpleado(Integer.valueOf(idEmpleado), VistaTrazabilidades.NAME, 2);

        Map<Integer, List<String>> mFiltros = new HashMap<Integer, List<String>>();

        if (lIdsCompras.isEmpty() && lIdsVentas.isEmpty()) {
            return null;
        } else {
            if (!lIdsCompras.isEmpty()) {

                lCompras = importadorSetup.obtenerComprasFictIds(lIdsCompras);
            }

            if (!lIdsVentas.isEmpty()) {
                lVentas = importadorSetup.obtenerVentasFictIds(lIdsVentas);
            }

            String nombreLogo = pdfTemp + "lnT.png";

            model.put("compras", lCompras);
            model.put("ventas", lVentas);
            model.put("colsCompras", lColumnasCompras);
            model.put("colsVentas", lColumnasVentas);
            model.put("totCompras", lTotalesCompras);
            model.put("totVentas", lTotalesVentas);
            model.put("filtros", mFiltros);
            model.put("nombreLogo", nombreLogo);

            /**
            // Ordenamos por el campo nombreArticulo
            Collections.sort(lLineasPedidoPtVista, new Comparator<TLineasPedidoPtVista>() {
                @Override
                public int compare(TLineasPedidoPtVista record1, TLineasPedidoPtVista record2) {
            
                    return record1.getIdPedido().compareTo(record2.getIdPedido());
                }
            });
            */
            String nombre = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Utils.generarFecha());
            nombre = appName.replaceAll(" ", "_") + "_COMPRAS_VENTAS_" + nombre + ".xls";
            response.setContentType("application/ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + nombre);

            // Limpiamos los diccionarios
            contrVista.mComprasComprasVentasEmpleado.get(Integer.valueOf(idEmpleado)).clear();
            contrVista.mTotalesComprasComprasVentasEmpleado.get(Integer.valueOf(idEmpleado)).clear();
            contrVista.mComprasVentasVentasEmpleado.get(Integer.valueOf(idEmpleado)).clear();
            contrVista.mTotalesVentasComprasVentasEmpleado.get(Integer.valueOf(idEmpleado)).clear();

            return new ModelAndView(new ComprasVentasExcel(), model);

        }
    }

    /**
     * Método que nos exporta a Excel los datos que se están mostrando en la pantalla de balance de masas.
     * @param idPedidoCompra El ID del empleado que realiza la petición
     * @param filename El nombre del fichero, el path para generar el fichero.
     * @throws BrosException 
     */
    public ModelAndView exportarBalanceMasas(HttpServletRequest request, HttpServletResponse response) throws GenasoftException {
        // Obtenemos los parámetros del filtro para exportar a Excel

        String idEmpleado = !request.getParameter("idEmpleado").equals("null") ? request.getParameter("idEmpleado") : null;

        Map<String, Object> model = new HashMap<String, Object>();
        //Sheet Name
        model.put("sheetname", "Balance de masas");

        List<String> lFiltros = Utils.generarListaGenerica();

        lFiltros.addAll(contrVista.obtenerFiltrosBalanceMasasExportar(Integer.valueOf(idEmpleado)));

        List<TVentasVista> lMasas = Utils.generarListaGenerica();

        lMasas.addAll(contrVista.obtenerBalanceMasasExportar(Integer.valueOf(idEmpleado)));

        List<TVentasVista> lTotalesMasas = Utils.generarListaGenerica();
        lTotalesMasas.addAll(contrVista.obtenerTotalesBalanceMasasExportar(Integer.valueOf(idEmpleado)));

        if (lMasas.isEmpty()) {
            return null;
        } else {

            String nombreLogo = pdfTemp + "lnT.png";

            model.put("masas", lMasas);
            model.put("filtros", lFiltros);
            model.put("totales", lTotalesMasas);
            model.put("nombreLogo", nombreLogo);

            // Ordenamos por el campo Producto, que en ventas es Familia.
            Collections.sort(lMasas, new Comparator<TVentasVista>() {
                @Override
                public int compare(TVentasVista record1, TVentasVista record2) {

                    return record1.getFamiliaFin().compareTo(record2.getFamiliaFin());
                }
            });

            String nombre = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Utils.generarFecha());
            nombre = appName.replaceAll(" ", "_") + "_BALANCE_MASAS_" + nombre + ".xls";
            response.setContentType("application/ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + nombre);

            // Limpiamos los diccionarios
            contrVista.lFiltrosBalanceMasas.clear();
            contrVista.mBalanceMasasExportar.get(Integer.valueOf(idEmpleado)).clear();

            return new ModelAndView(new BalanceMasasExcel(), model);

        }
    }

}
