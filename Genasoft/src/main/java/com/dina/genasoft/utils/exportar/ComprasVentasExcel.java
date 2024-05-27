/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.exportar;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.dina.genasoft.db.entity.TColumnasTablasEmpleado;
import com.dina.genasoft.db.entity.TComprasFict;
import com.dina.genasoft.db.entity.TVentasFict;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero
 * Clase que nos realiza la generación de un fichero Excel a partir de los datos pasados por los parámetros.
 */
@Component
@Slf4j
@Data
public class ComprasVentasExcel extends AbstractXlsView {
    /** Los colores. */
    private Short[]                       colores                   = new Short[11];
    /** Las líneas de compras que se incluirán en el fichero Excel. */
    private List<TComprasFict>            lCompras;
    /** Las líneas de ventas que se incluirán en el fichero Excel. */
    private List<TVentasFict>             lVentas;
    /** Las líneas de compras que se incluirán en el fichero Excel. */
    private List<TColumnasTablasEmpleado> lColsCompra;
    /** Las líneas de ventas que se incluirán en el fichero Excel. */
    private List<TColumnasTablasEmpleado> lColsVenta;
    /** Las líneas de compras que se incluirán en el fichero Excel. */
    private List<Double>                  lTotalCompras;
    /** Las líneas de ventas que se incluirán en el fichero Excel. */
    private List<Double>                  lTotalVenta;
    /** Diccionario con las columnas que se van a mostrar en la tabla de compras. */
    private Map<String, String>           mColumnasIdsCompras;
    /** Diccionario con las columnas que se van a mostrar en la tabla de ventas. */
    private Map<String, String>           mColumnasIdsVentas;
    /** Diccionario que contendrá los filros aplicados. */
    private Map<Integer, List<String>>    mFiltros;
    private final short                   VERTICAL_ALIGNMENT_CENTER = 4;
    /** Variables que nos indica la posición del diccionario de filtros donde se encuentra el valor a filtrar.*/
    private final Integer                 ALBARAN_COMPRA            = 1;
    private final Integer                 PARTIDA_COMPRA            = 2;
    private final Integer                 PEDIDO_VENTA              = 3;
    private final Integer                 ALBARAN_VENTA             = 4;
    private final Integer                 ARTICULO_COMPRA           = 5;
    private final Integer                 ARTICULO_VENTA            = 6;
    private final Integer                 FAMILIA_COMPRA            = 7;
    private final Integer                 FAMILIA_VENTA             = 8;
    private final Integer                 ORIGEN_COMPRA             = 9;
    private final Integer                 ORIGEN_VENTA              = 10;
    private final Integer                 PROVEEDOR_COMPRA          = 11;
    private final Integer                 PROVEEDOR_VENTA           = 12;
    private final Integer                 CLIENTE                   = 13;
    private final Integer                 LOTE_COMPRA               = 14;
    private final Integer                 LOTE_VENTA                = 15;
    private final Integer                 FECHA_DESDE_COMPRA        = 16;
    private final Integer                 FECHA_HASTA_COMPRA        = 17;
    private final Integer                 FECHA_DESDE_VENTA         = 18;
    private final Integer                 FECHA_HASTA_VENTA         = 19;
    private final Integer                 GGN                       = 20;
    private final Integer                 CALIDAD_COMPRA            = 21;
    private final Integer                 CALIDAD_VENTA             = 22;
    private String                        nombreLogo;

    @SuppressWarnings("unchecked")
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //VARIABLES REQUIRED IN MODEL
        String sheetName = (String) model.get("sheetname");
        lCompras = (List<TComprasFict>) model.get("compras");
        lVentas = (List<TVentasFict>) model.get("ventas");
        lColsCompra = (List<TColumnasTablasEmpleado>) model.get("colsCompras");
        lColsVenta = (List<TColumnasTablasEmpleado>) model.get("colsVentas");
        lTotalCompras = (List<Double>) model.get("totCompras");
        lTotalVenta = (List<Double>) model.get("totVentas");
        mFiltros = (Map<Integer, List<String>>) model.get("filtros");
        nombreLogo = (String) model.get("nombreLogo");

        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFPalette palette = hwb.getCustomPalette();

        // color cabecera
        HSSFColor myColor = palette.findSimilarColor(119, 162, 205);
        // get the palette index of that color 
        short palIndex = myColor.getIndex();

        colores[0] = IndexedColors.LIGHT_GREEN.getIndex();
        colores[1] = IndexedColors.GREY_25_PERCENT.getIndex();
        colores[2] = IndexedColors.WHITE.getIndex();
        colores[3] = IndexedColors.BRIGHT_GREEN.getIndex();

        //BUILD DOC
        Sheet sheet = workbook.createSheet(sheetName);
        //sheet.setDefaultRowHeight((short) 15);
        sheet.setFitToPage(true);

        sheet.setMargin(Sheet.RightMargin, 0.1);
        sheet.setMargin(Sheet.LeftMargin, 0.1);
        sheet.setMargin(Sheet.TopMargin, 0.1);
        sheet.setMargin(Sheet.BottomMargin, 0.1);

        // ESTABLECEMOS EL ANCHO DE LA COLUMNA A. (25 píxeles)
        // DOCUMENTO/REF
        sheet.setColumnWidth(0, 580 * (74 / 7));
        sheet.setColumnWidth(1, 580 * (74 / 7));
        // DOC.PAGO
        sheet.setColumnWidth(2, 777 * (74 / 7));
        // NETO
        sheet.setColumnWidth(3, 580 * (74 / 7));
        // IMPUESTOS
        sheet.setColumnWidth(4, 580 * (74 / 7));
        // TOTAL
        sheet.setColumnWidth(5, 580 * (74 / 7));
        // SITUACIÓN
        sheet.setColumnWidth(6, 580 * (74 / 7));
        sheet.setColumnWidth(7, 580 * (74 / 7));
        sheet.setColumnWidth(8, 580 * (74 / 7));
        sheet.setColumnWidth(9, 580 * (74 / 7));
        sheet.setColumnWidth(10, 580 * (74 / 7));
        sheet.setColumnWidth(11, 580 * (74 / 7));
        sheet.setColumnWidth(12, 580 * (74 / 7));
        sheet.setColumnWidth(13, 580 * (74 / 7));
        sheet.setColumnWidth(14, 580 * (74 / 7));
        sheet.setColumnWidth(15, 580 * (74 / 7));
        sheet.setColumnWidth(16, 580 * (74 / 7));
        sheet.setColumnWidth(17, 580 * (74 / 7));
        sheet.setColumnWidth(18, 580 * (74 / 7));
        sheet.setColumnWidth(19, 580 * (74 / 7));
        sheet.setColumnWidth(20, 580 * (74 / 7));
        sheet.setColumnWidth(21, 580 * (74 / 7));
        sheet.setColumnWidth(22, 580 * (74 / 7));
        sheet.setColumnWidth(23, 580 * (74 / 7));
        sheet.setColumnWidth(24, 580 * (74 / 7));
        sheet.setColumnWidth(25, 580 * (74 / 7));
        sheet.setColumnWidth(26, 580 * (74 / 7));
        sheet.setColumnWidth(27, 580 * (74 / 7));
        sheet.setColumnWidth(28, 580 * (74 / 7));
        sheet.setColumnWidth(29, 580 * (74 / 7));
        sheet.setColumnWidth(30, 580 * (74 / 7));
        sheet.setColumnWidth(31, 580 * (74 / 7));
        sheet.setColumnWidth(32, 580 * (74 / 7));

        // Añadimos el logo al documento.

        // read the image to the stream
        //final FileInputStream stream = new FileInputStream("/Volumes/Datos/natural_tropic/logo_nTropic.png");
        final FileInputStream stream = new FileInputStream(nombreLogo);
        final CreationHelper helper = workbook.getCreationHelper();
        final Drawing drawing = sheet.createDrawingPatriarch();

        final ClientAnchor anchor = helper.createClientAnchor();
        anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);

        final int pictureIndex = workbook.addPicture(IOUtils.toByteArray(stream), Workbook.PICTURE_TYPE_PNG);

        anchor.setCol1(0);
        anchor.setRow1(0); // same row is okay
        anchor.setRow2(3);
        //anchor.setCol2(1);
        final Picture pict = drawing.createPicture(anchor, pictureIndex);
        pict.resize();

        // Nutricmos los diccionarios para mostrar el nombre de la columna
        nutrirDiccionarioCabecerasColumnaCompras();
        nutrirDiccionarioCabeceraColumnaVentas();

        if (!lCompras.isEmpty() && !lVentas.isEmpty()) {
            crearTituloDocumento2Tablas(sheet, workbook, 2, 8);
        } else {
            // Creamos el título del documento
            crearTituloDocumento1Tabla(sheet, workbook, 2, 3);
        }

        Integer currentRow = 0;
        if (!mFiltros.isEmpty()) {
            currentRow = crearContenidoFiltro(workbook, sheet, 7);
        }

        currentRow++;
        currentRow++;

        // MOSTRAMOS LOS TOTALES , SI LOS HAY
        //if (!lTotalCompras.isEmpty()) {
        //    crearTotalesCompras(workbook, sheet, currentRow);
        //}
        //if (!lTotalVenta.isEmpty()) {
        //    crearTotalesVentas(workbook, sheet, currentRow);
        //}

        currentRow++;
        currentRow++;
        currentRow++;

        currentRow++;

        Short currentColumn = 0;
        if (!lCompras.isEmpty()) {
            // Pintamos la cabecera de la tabla
            crearCabeceraTablaCompras(workbook, sheet, currentRow);
            currentColumn = (short) (lColsCompra.size());
        }
        if (!lVentas.isEmpty()) {
            // Pintamos la cabecera de la tabla
            crearCabeceraTablaVentas(workbook, sheet, currentRow, currentColumn);
        }
        currentRow++;

        // Rellenamos la tabla de contenido.
        rellenarTablaDatosCompras(workbook, sheet, currentRow);

        // Rellenamos la tabla de contenido.
        rellenarTablaDatosVentas(workbook, sheet, currentRow);

    }

    /**
     * Método que nos crea la cabecera de la tabla
     * @param workbook El fichero Excel
     * @param sheet La hoja.
     */
    private void crearCabeceraTablaCompras(Workbook workbook, Sheet sheet, int currentRow) {

        short currentColumn = 0;
        //CREATE STYLE FOR HEADER
        CellStyle estiloCeldaCabecera = workbook.createCellStyle();
        Font cabeceraFuente = workbook.createFont();
        cabeceraFuente.setFontHeightInPoints((short) 12);
        cabeceraFuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cabeceraFuente.setFontName("VERDANA");
        //cabeceraFuente.setColor(HSSFColor.WHITE.index);
        estiloCeldaCabecera.setFont(cabeceraFuente);

        estiloCeldaCabecera.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        estiloCeldaCabecera.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        estiloCeldaCabecera.setTopBorderColor(IndexedColors.BLACK.getIndex());
        estiloCeldaCabecera.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        // Pintamos las celdas de la cabecera.
        Row headerRow = sheet.createRow(currentRow);

        currentColumn = 0;

        // Variables necesarias para mostrar las columnas
        HSSFRichTextString text = null;;
        Cell cell2 = null;
        // Miramos las columnas que tiene guardada en BD para mostrar las mismas columnas y en el mismo orden.
        for (TColumnasTablasEmpleado col : lColsCompra) {
            text = new HSSFRichTextString(mColumnasIdsCompras.get(col.getCampo()));
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;
        }

    }

    /**
     * Método que nos crea la cabecera de la tabla
     * @param workbook El fichero Excel
     * @param sheet La hoja.
     */
    private void crearCabeceraTablaVentas(Workbook workbook, Sheet sheet, int currentRow, int currentColumn) {

        //CREATE STYLE FOR HEADER
        CellStyle estiloCeldaCabecera = workbook.createCellStyle();
        Font cabeceraFuente = workbook.createFont();
        cabeceraFuente.setFontHeightInPoints((short) 12);
        cabeceraFuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cabeceraFuente.setFontName("VERDANA");
        //cabeceraFuente.setColor(HSSFColor.WHITE.index);
        estiloCeldaCabecera.setFont(cabeceraFuente);

        estiloCeldaCabecera.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        estiloCeldaCabecera.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        estiloCeldaCabecera.setTopBorderColor(IndexedColors.BLACK.getIndex());
        estiloCeldaCabecera.setBottomBorderColor(IndexedColors.BLACK.getIndex());

        Row headerRow = sheet.getRow(currentRow);

        if (lCompras.isEmpty()) {
            // Pintamos las celdas de la cabecera.
            headerRow = sheet.createRow(currentRow);
        }

        // Variables necesarias para mostrar las columnas
        HSSFRichTextString text = null;
        Cell cell2 = null;
        // Miramos las columnas que tiene guardada en BD para mostrar las mismas columnas y en el mismo orden.
        for (TColumnasTablasEmpleado col : lColsVenta) {
            text = new HSSFRichTextString(mColumnasIdsVentas.get(col.getCampo()));
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;
        }
    }

    /**
     * Método que nos rellena la tabla de datos.
     */
    private void rellenarTablaDatosCompras(Workbook workbook, Sheet sheet, Integer currentRow) {

        short currentColumn = 0;
        //CREATE STYLE FOR HEADER
        CellStyle estiloContenidoTablaCliente = workbook.createCellStyle();
        Font cabeceraFuente = workbook.createFont();
        cabeceraFuente.setFontHeightInPoints((short) 10);
        cabeceraFuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cabeceraFuente.setFontName("VERDANA");
        estiloContenidoTablaCliente.setFont(cabeceraFuente);

        CellStyle estiloContenidoTabla = workbook.createCellStyle();
        Font cabeceraFuente2 = workbook.createFont();
        cabeceraFuente2.setFontHeightInPoints((short) 11);
        cabeceraFuente2.setFontName("VERDANA");
        estiloContenidoTabla.setFont(cabeceraFuente2);
        estiloContenidoTabla.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        estiloContenidoTabla.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // PARA EL TOTAL DEL CLIENTE
        //CREATE STYLE FOR HEADER
        CellStyle estiloCeldaCabecera = workbook.createCellStyle();
        Font cabeceraFuente3 = workbook.createFont();
        cabeceraFuente3.setFontHeightInPoints((short) 8);
        cabeceraFuente3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cabeceraFuente3.setFontName("VERDANA");
        estiloCeldaCabecera.setFont(cabeceraFuente3);
        estiloCeldaCabecera.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        estiloCeldaCabecera.setTopBorderColor(IndexedColors.BLACK.getIndex());

        CellStyle mergenSuperior = workbook.createCellStyle();
        mergenSuperior.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        mergenSuperior.setTopBorderColor(IndexedColors.BLACK.getIndex());

        // Pintamos las celdas de la cabecera.
        Row headerRow = null;

        // Variables necesarias para mostrar los datos
        HSSFRichTextString text = null;
        Cell cell2 = null;
        String campo = null;
        // Nutrimos el diccionario con los pedidos.
        Map<Integer, String> mColumnnas = lColsCompra.stream().collect(Collectors.toMap(TColumnasTablasEmpleado::getOrdenCampo, TColumnasTablasEmpleado::getCampo));
        int i = 0;
        for (TComprasFict compra : lCompras) {
            headerRow = sheet.createRow(currentRow);
            currentColumn = 0;

            // Vamos mostrando los datos.
            while (true) {
                if (currentColumn >= lColsCompra.size()) {
                    break;
                }
                campo = mColumnnas.get(new Short(currentColumn).intValue());

                campo = obtenerDatoMostrarCompra(compra, campo);

                estiloContenidoTabla.setFillForegroundColor(colores[i % 2]);

                // El campo que nos indica el orden de columna de empleado.
                text = new HSSFRichTextString(campo);
                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;
            }
            i++;
            currentRow++;
        }

    }

    /**
     * Método que nos rellena la tabla de datos.
     */
    private void rellenarTablaDatosVentas(Workbook workbook, Sheet sheet, Integer currentRow) {

        //CREATE STYLE FOR HEADER
        CellStyle estiloContenidoTablaCliente = workbook.createCellStyle();
        Font cabeceraFuente = workbook.createFont();
        cabeceraFuente.setFontHeightInPoints((short) 10);
        cabeceraFuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cabeceraFuente.setFontName("VERDANA");
        estiloContenidoTablaCliente.setFont(cabeceraFuente);

        CellStyle estiloContenidoTabla = workbook.createCellStyle();
        Font cabeceraFuente2 = workbook.createFont();
        cabeceraFuente2.setFontHeightInPoints((short) 11);
        cabeceraFuente2.setFontName("VERDANA");
        estiloContenidoTabla.setFont(cabeceraFuente2);
        estiloContenidoTabla.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        estiloContenidoTabla.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // PARA EL TOTAL DEL CLIENTE
        //CREATE STYLE FOR HEADER
        CellStyle estiloCeldaCabecera = workbook.createCellStyle();
        Font cabeceraFuente3 = workbook.createFont();
        cabeceraFuente3.setFontHeightInPoints((short) 8);
        cabeceraFuente3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cabeceraFuente3.setFontName("VERDANA");
        estiloCeldaCabecera.setFont(cabeceraFuente3);
        estiloCeldaCabecera.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        estiloCeldaCabecera.setTopBorderColor(IndexedColors.BLACK.getIndex());

        CellStyle mergenSuperior = workbook.createCellStyle();
        mergenSuperior.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        mergenSuperior.setTopBorderColor(IndexedColors.BLACK.getIndex());

        // Pintamos las celdas de la cabecera.
        Row headerRow = null;

        // Variables necesarias para mostrar los datos
        HSSFRichTextString text = null;
        Cell cell2 = null;
        String campo = null;
        short currentColumn = 0;
        int contadorColumna = 0;
        // Nutrimos el diccionario con los pedidos.
        Map<Integer, String> mColumnnas = lColsVenta.stream().collect(Collectors.toMap(TColumnasTablasEmpleado::getOrdenCampo, TColumnasTablasEmpleado::getCampo));
        int i = 0;
        if (!lCompras.isEmpty()) {
            i = 1;
        }
        for (TVentasFict venta : lVentas) {
            contadorColumna = 0;
            if (lCompras.isEmpty()) {
                headerRow = sheet.createRow(currentRow);
            } else {
                headerRow = sheet.getRow(currentRow);
            }
            //System.out.println(currentRow);
            if (headerRow == null) {
                headerRow = sheet.createRow(currentRow);
            }
            if (!lCompras.isEmpty()) {
                currentColumn = (short) (lColsCompra.size());
            } else {
                currentColumn = 0;
            }

            // Vamos mostrando los datos.
            while (true) {
                if (contadorColumna >= lColsVenta.size()) {
                    break;
                }
                campo = mColumnnas.get(contadorColumna);

                campo = obtenerDatoMostrarVenta(venta, campo);

                estiloContenidoTabla.setFillForegroundColor(colores[i % 2]);

                // El campo que nos indica el orden de columna de empleado.
                text = new HSSFRichTextString(campo);

                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;
                contadorColumna++;
            }
            i++;
            currentRow++;
        }

    }

    /**
     * Título a pintar cuando se muestren 2 tablas
     * @param sheet
     * @param workbook
     * @param currentRow
     * @param currentColumn
     */
    private void crearTituloDocumento2Tablas(Sheet sheet, Workbook workbook, Integer currentRow, Integer currentColumn) {

        //CREATE STYLE FOR HEADER
        CellStyle estiloCeldaCabecera = workbook.createCellStyle();
        Font cabeceraFuente = workbook.createFont();
        cabeceraFuente.setFontHeightInPoints((short) 16);
        cabeceraFuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cabeceraFuente.setFontName("VERDANA");
        cabeceraFuente.setColor(HSSFColor.BLACK.index);
        estiloCeldaCabecera.setFont(cabeceraFuente);
        //estiloCeldaCabecera.setFillForegroundColor(colores[0]);
        //estiloCeldaCabecera.setFillPattern(CellStyle.SOLID_FOREGROUND);
        estiloCeldaCabecera.setVerticalAlignment(VERTICAL_ALIGNMENT_CENTER);

        // Pintamos las celdas de la cabecera.
        Row headerRow = sheet.createRow(currentRow);
        // El nombre del transportista
        HSSFRichTextString text = new HSSFRichTextString("Compras Vs Ventas");
        Cell cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);

        // ORDEN DE TRANSPORTE
        CellRangeAddress regionTitulo = CellRangeAddress.valueOf("I3:L5");

        currentColumn = 4;

        sheet.addMergedRegion(regionTitulo);
    }

    /**
     * Título a pintar cuando se muestren 2 tablas
     * @param sheet
     * @param workbook
     * @param currentRow
     * @param currentColumn
     */
    private void crearTituloDocumento1Tabla(Sheet sheet, Workbook workbook, Integer currentRow, Integer currentColumn) {

        //CREATE STYLE FOR HEADER
        CellStyle estiloCeldaCabecera = workbook.createCellStyle();
        Font cabeceraFuente = workbook.createFont();
        cabeceraFuente.setFontHeightInPoints((short) 16);
        cabeceraFuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cabeceraFuente.setFontName("VERDANA");
        cabeceraFuente.setColor(HSSFColor.BLACK.index);
        estiloCeldaCabecera.setFont(cabeceraFuente);
        //estiloCeldaCabecera.setFillForegroundColor(colores[0]);
        //estiloCeldaCabecera.setFillPattern(CellStyle.SOLID_FOREGROUND);
        estiloCeldaCabecera.setVerticalAlignment(VERTICAL_ALIGNMENT_CENTER);

        // Pintamos las celdas de la cabecera.
        Row headerRow = sheet.createRow(currentRow);
        // El nombre del transportista
        HSSFRichTextString text = new HSSFRichTextString("Compras Vs Ventas");
        Cell cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);

        // ORDEN DE TRANSPORTE
        CellRangeAddress regionTitulo = CellRangeAddress.valueOf("D3:H5");

        currentColumn = 4;

        sheet.addMergedRegion(regionTitulo);
    }

    private void nutrirDiccionarioCabecerasColumnaCompras() {
        mColumnasIdsCompras = new HashMap<String, String>();
        mColumnasIdsCompras.put("nombreDescriptivo", "Descripción importación");
        mColumnasIdsCompras.put("albaranFin", "Albarán");
        mColumnasIdsCompras.put("fechaFin", "Fecha");
        mColumnasIdsCompras.put("partidaFin", "Partida");
        mColumnasIdsCompras.put("cajasFin", "Nº de cajas");
        mColumnasIdsCompras.put("kgsBrutoFin", "Peso bruto");
        mColumnasIdsCompras.put("loteFin", "Trazabilidad");
        mColumnasIdsCompras.put("pesoNetoFin", "Peso neto");
        mColumnasIdsCompras.put("kgsDisponiblesFin", "Kgs disponibles");
        mColumnasIdsCompras.put("productoFin", "Producto");
        mColumnasIdsCompras.put("proveedorFin", "Entidad");
        mColumnasIdsCompras.put("origenFin", "Orígen");
        mColumnasIdsCompras.put("familiaFin", "Plantilla de producto");
        mColumnasIdsCompras.put("ggnFin", "Global Gap");
        mColumnasIdsCompras.put("calidadFin", "Calidad");
    }

    private void nutrirDiccionarioCabeceraColumnaVentas() {
        mColumnasIdsVentas = new HashMap<String, String>();
        mColumnasIdsVentas.put("nombreDescriptivo", "Descripción importación");
        mColumnasIdsVentas.put("pedidoFin", "Pedido de venta");
        mColumnasIdsVentas.put("albaranFin", "Albarán de venta");
        mColumnasIdsVentas.put("calibreFin", "Calibre");
        mColumnasIdsVentas.put("loteFin", "Trazabilidad");
        mColumnasIdsVentas.put("idPaleFin", "ID de palé");
        mColumnasIdsVentas.put("numBultosFin", "Bultos mov. venta");
        mColumnasIdsVentas.put("numBultosPaleFin", "Bultos por palé");
        mColumnasIdsVentas.put("proveedorFin", "Productor");
        mColumnasIdsVentas.put("clienteFin", "Entidad");
        mColumnasIdsVentas.put("fechaVentaFin", "Fecha salida");
        mColumnasIdsVentas.put("kgsFin", "Kilos");
        mColumnasIdsVentas.put("kgsNetosFin", "Peso neto teo. venta");
        mColumnasIdsVentas.put("variedadFin", "Variedad");
        mColumnasIdsVentas.put("origenFin", "Orígen venta");
        mColumnasIdsVentas.put("calidadVentaFin", "Calidad venta");
        mColumnasIdsVentas.put("confeccionFin", "Confección");
        mColumnasIdsVentas.put("familiaFin", "Producto");
    }

    /**
     * Método que nos determina cuál es el campo que tenemos que coger el objeto compra a partir de la clave de nombre de columna pasada por prámetro.
     * @param compra La compa con los datos
     * @param campo El identificador del campo
     * @return El valor del campo
     */
    private String obtenerDatoMostrarCompra(TComprasFict compra, String campo) {
        String result = null;

        if (campo.equals("albaranFin")) {
            result = compra.getAlbaranFin();
        } else if (campo.equals("nombreDescriptivo")) {
            result = compra.getNombreDescriptivo();
        } else if (campo.equals("fechaFin")) {
            result = new SimpleDateFormat("dd/MM/yyyy").format(compra.getFechaFin());
        } else if (campo.equals("partidaFin")) {
            result = compra.getPartidaFin();
        } else if (campo.equals("cajasFin")) {
            result = "" + compra.getCajasFin();
        } else if (campo.equals("kgsBrutoFin")) {
            result = "" + compra.getKgsBrutoFin();
        } else if (campo.equals("loteFin")) {
            result = compra.getLoteFin();
        } else if (campo.equals("pesoNetoFin")) {
            result = "" + compra.getPesoNetoFin();
        } else if (campo.equals("kgsDisponibles")) {
            result = "" + compra.getPesoNetoDisponible();
        } else if (campo.equals("productoFin")) {
            result = compra.getProductoFin();
        } else if (campo.equals("proveedorFin")) {
            result = compra.getProveedorFin();
        } else if (campo.equals("origenFin")) {
            result = compra.getOrigenFin();
        } else if (campo.equals("familiaFin")) {
            result = compra.getFamiliaFin();
        } else if (campo.equals("ggnFin")) {
            result = compra.getGgnFin();
        } else if (campo.equals("calidadFin")) {
            result = compra.getCalidadFin();
        }

        return result;
    }

    /**
     * Método que nos determina cuál es el campo que tenemos que coger el objeto compra a partir de la clave de nombre de columna pasada por prámetro.
     * @param venta La compa con los datos
     * @param campo El identificador del campo
     * @return El valor del campo
     */
    private String obtenerDatoMostrarVenta(TVentasFict venta, String campo) {
        String result = null;

        if (campo.equals("albaranFin")) {
            result = venta.getAlbaranFin();
        } else if (campo.equals("nombreDescriptivo")) {
            result = venta.getNombreDescriptivo();
        } else if (campo.equals("pedidoFin")) {
            result = venta.getPedidoFin();
        } else if (campo.equals("calibreFin")) {
            result = venta.getCalibreFin();
        } else if (campo.equals("loteFin")) {
            result = venta.getLoteFin();
        } else if (campo.equals("idPaleFin")) {
            result = venta.getIdPaleFin();
        } else if (campo.equals("numBultosFin")) {
            result = "" + venta.getNumBultosFin();
        } else if (campo.equals("numBultosPaleFin")) {
            result = "" + venta.getNumBultosPaleFin();
        } else if (campo.equals("proveedorFin")) {
            result = venta.getProveedorFin();
        } else if (campo.equals("clienteFin")) {
            result = venta.getClienteFin();
        } else if (campo.equals("fechaVentaFin")) {
            result = new SimpleDateFormat("dd/MM/yyyy").format(venta.getFechaVentaFin());
        } else if (campo.equals("kgsFin")) {
            result = "" + venta.getKgsFin();
        } else if (campo.equals("kgsNetosFin")) {
            result = "" + venta.getKgsNetosFin();
        } else if (campo.equals("variedadFin")) {
            result = venta.getVariedadFin();
        } else if (campo.equals("origenFin")) {
            result = venta.getOrigenFin();
        } else if (campo.equals("calidadVentaFin")) {
            result = venta.getCalidadVentaFin();
        } else if (campo.equals("confeccionFin")) {
            result = venta.getConfeccionFin();
        } else if (campo.equals("familiaFin")) {
            result = venta.getFamiliaFin();
        }

        return result;
    }

    private void crearTotalesCompras(Workbook workbook, Sheet sheet, int currentRow) {

        short currentColumn = 2;
        //CREATE STYLE FOR HEADER
        CellStyle estiloCeldaCabecera = workbook.createCellStyle();
        Font cabeceraFuente = workbook.createFont();
        cabeceraFuente.setFontHeightInPoints((short) 16);
        cabeceraFuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cabeceraFuente.setFontName("VERDANA");
        cabeceraFuente.setColor(HSSFColor.BLACK.index);
        estiloCeldaCabecera.setFont(cabeceraFuente);
        //estiloCeldaCabecera.setFillForegroundColor(colores[0]);
        //estiloCeldaCabecera.setFillPattern(CellStyle.SOLID_FOREGROUND);
        estiloCeldaCabecera.setVerticalAlignment(VERTICAL_ALIGNMENT_CENTER);

        // Pintamos las celdas de la cabecera.
        Row headerRow = sheet.createRow(currentRow);
        // El nombre del transportista
        HSSFRichTextString text = new HSSFRichTextString("Total compras");
        Cell cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);

        // ORDEN DE TRANSPORTE
        CellRangeAddress regionTitulo = CellRangeAddress.valueOf("C8:D8");

        currentRow++;
        headerRow = sheet.createRow(currentRow);
        DecimalFormat df = new DecimalFormat("#,##0.000");

        cabeceraFuente.setFontHeightInPoints((short) 12);

        // Mostramos los totales
        text = new HSSFRichTextString("Kilos totales");
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        text = new HSSFRichTextString("Kilos disponibles");
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);

        currentRow++;
        headerRow = sheet.createRow(currentRow);
        currentColumn = 2;

        // Mostramos los totales
        text = new HSSFRichTextString(df.format(lTotalCompras.get(0)));
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        cabeceraFuente.setFontHeightInPoints((short) 12);

        text = new HSSFRichTextString(df.format(lTotalCompras.get(1)));
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);

        //sheet.addMergedRegion(regionTitulo);

    }

    private void crearTotalesVentas(Workbook workbook, Sheet sheet, int currentRow) {

        short currentColumn = 19;

        if (lCompras.isEmpty()) {
            currentColumn = 2;
        }
        //CREATE STYLE FOR HEADER
        CellStyle estiloCeldaCabecera = workbook.createCellStyle();
        Font cabeceraFuente = workbook.createFont();
        cabeceraFuente.setFontHeightInPoints((short) 16);
        cabeceraFuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cabeceraFuente.setFontName("VERDANA");
        cabeceraFuente.setColor(HSSFColor.BLACK.index);
        estiloCeldaCabecera.setFont(cabeceraFuente);
        //estiloCeldaCabecera.setFillForegroundColor(colores[0]);
        //estiloCeldaCabecera.setFillPattern(CellStyle.SOLID_FOREGROUND);
        estiloCeldaCabecera.setVerticalAlignment(VERTICAL_ALIGNMENT_CENTER);

        // Pintamos las celdas de la cabecera.
        Row headerRow;

        if (!lCompras.isEmpty()) {
            // ORDEN DE TRANSPORTE

            headerRow = sheet.getRow(currentRow);
        } else {

            headerRow = sheet.createRow(currentRow);
        }

        // El nombre del transportista
        HSSFRichTextString text = new HSSFRichTextString("Total ventas");
        Cell cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);

        CellRangeAddress regionTitulo = null;

        DecimalFormat df = new DecimalFormat("#,##0.000");

        if (!lCompras.isEmpty()) {
            // ORDEN DE TRANSPORTE
            regionTitulo = CellRangeAddress.valueOf("T8:W8");
        } else {
            regionTitulo = CellRangeAddress.valueOf("C8:D8");
        }

        currentRow++;
        if (lCompras.isEmpty()) {
            headerRow = sheet.createRow(currentRow);
        } else {
            headerRow = sheet.getRow(currentRow);
        }

        cabeceraFuente.setFontHeightInPoints((short) 12);
        // Mostramos los totales
        /**
        text = new HSSFRichTextString("Bultos mov. venta totales");
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;
        
        text = new HSSFRichTextString("Bultos por palé totales");
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;
        */
        text = new HSSFRichTextString("Kilos totales");
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        /** text = new HSSFRichTextString("Kilos netos teo. venta totales");
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;
        */
        currentRow++;
        if (lCompras.isEmpty()) {
            headerRow = sheet.createRow(currentRow);
            currentColumn = 2;
        } else {
            headerRow = sheet.getRow(currentRow);
            currentColumn = 19;
        }
        /**
        // Mostramos los totales
        text = new HSSFRichTextString(df.format(lTotalVenta.get(0)));
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;
        
        text = new HSSFRichTextString(df.format(lTotalVenta.get(1)));
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;
        */
        text = new HSSFRichTextString(df.format(lTotalVenta.get(2)));
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;
        /*
        text = new HSSFRichTextString(df.format(lTotalVenta.get(3)));
        cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;
        */
        //sheet.addMergedRegion(regionTitulo);

    }

    private Integer crearContenidoFiltro(Workbook workbook, Sheet sheet, Integer currentRow) {
        Integer currentColumn = 0;
        //CREATE STYLE FOR HEADER
        CellStyle estiloCeldaCabecera = workbook.createCellStyle();
        Font cabeceraFuente = workbook.createFont();
        cabeceraFuente.setFontHeightInPoints((short) 10);
        cabeceraFuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cabeceraFuente.setFontName("VERDANA");
        //cabeceraFuente.setColor(HSSFColor.WHITE.index);
        estiloCeldaCabecera.setFont(cabeceraFuente);

        // Pintamos las celdas de la cabecera.
        Row headerRow;

        currentColumn = 0;

        // Variables necesarias para mostrar las columnas
        HSSFRichTextString text = null;;
        Cell cell2 = null;

        List<String> lFiltros;
        String texto = "";
        // Vamos mirando a ver qué filtros a aplicado.

        headerRow = sheet.createRow(currentRow);
        text = new HSSFRichTextString("FILTROS APLICADOS ");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        currentColumn = 0;;
        currentRow++;

        // PARTIDA COMPRA
        if (mFiltros.get(PARTIDA_COMPRA) != null) {
            lFiltros = mFiltros.get(PARTIDA_COMPRA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("PARTIDA COMPRA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // ALBRAÁN COMPRA
        if (mFiltros.get(ALBARAN_COMPRA) != null) {
            lFiltros = mFiltros.get(ALBARAN_COMPRA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("ALBARÁN COMPRA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // PEDIDO VENTA
        if (mFiltros.get(PEDIDO_VENTA) != null) {
            lFiltros = mFiltros.get(PEDIDO_VENTA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("PEDIDO VENTA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // ALBARÁN VENTA
        if (mFiltros.get(ALBARAN_VENTA) != null) {
            lFiltros = mFiltros.get(ALBARAN_VENTA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("ALBARÁN VENTA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // ARTÍCULO COMPRA
        if (mFiltros.get(ARTICULO_COMPRA) != null) {
            lFiltros = mFiltros.get(ARTICULO_COMPRA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("ARTÍCULO COMPRA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // ARTÍCULO VENTA
        if (mFiltros.get(ARTICULO_VENTA) != null) {
            lFiltros = mFiltros.get(ARTICULO_VENTA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("ARTÍCULO VENTA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // FAMILIA COMPRA
        if (mFiltros.get(FAMILIA_COMPRA) != null) {
            lFiltros = mFiltros.get(FAMILIA_COMPRA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("FAMILIA COMPRA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // FAMILIA VENTA
        if (mFiltros.get(FAMILIA_VENTA) != null) {
            lFiltros = mFiltros.get(FAMILIA_VENTA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("FAMILIA VENTA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // ORIGEN COMPRA
        if (mFiltros.get(ORIGEN_COMPRA) != null) {
            lFiltros = mFiltros.get(ORIGEN_COMPRA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("PAÍS COMPRA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // ORIGEN VENTA
        if (mFiltros.get(ORIGEN_VENTA) != null) {
            lFiltros = mFiltros.get(ORIGEN_VENTA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("PAÍS VENTA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // PROVEEDOR COMPRA
        if (mFiltros.get(PROVEEDOR_COMPRA) != null) {
            lFiltros = mFiltros.get(PROVEEDOR_COMPRA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("PROVEEDOR COMPRA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // PROVEEDOR VENTA
        if (mFiltros.get(PROVEEDOR_VENTA) != null) {
            lFiltros = mFiltros.get(PROVEEDOR_VENTA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("PROVEEDOR VENTA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // CLIENTE
        if (mFiltros.get(CLIENTE) != null) {
            lFiltros = mFiltros.get(CLIENTE);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("CLIENTE: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // LOTE COMPRA
        if (mFiltros.get(LOTE_COMPRA) != null) {
            lFiltros = mFiltros.get(LOTE_COMPRA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("TRAZABILIDAD COMPRA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // LOTE VENTA
        if (mFiltros.get(LOTE_VENTA) != null) {
            lFiltros = mFiltros.get(LOTE_VENTA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("TRAZABILIDAD VENTA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // GGN
        if (mFiltros.get(GGN) != null) {
            lFiltros = mFiltros.get(GGN);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("GGN: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // CALIDAD COMPRA
        if (mFiltros.get(CALIDAD_COMPRA) != null) {
            lFiltros = mFiltros.get(CALIDAD_COMPRA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("CERTIFICACIÓN COMPRA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        // CALIDAD VENTA
        if (mFiltros.get(CALIDAD_VENTA) != null) {
            lFiltros = mFiltros.get(CALIDAD_VENTA);
            headerRow = sheet.createRow(currentRow);

            text = new HSSFRichTextString("CERTIFICACIÓN VENTA: ");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;

            for (String filtr : lFiltros) {
                texto = texto + filtr + ",";
            }
            texto = texto.substring(0, texto.length() - 1);
            text = new HSSFRichTextString(texto);
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentRow++;
            currentColumn = 0;
        }

        return currentRow;

    }

}
