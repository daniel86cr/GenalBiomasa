/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.exportar;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

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

import com.dina.genasoft.db.entity.TVentasVista;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero
 * Clase que nos realiza la generación de un fichero Excel a partir de los datos pasados por los parámetros.
 */
@Component
@Slf4j
@Data
public class BalanceMasasExcel extends AbstractXlsView {
    /** Los colores. */
    private Short[]            colores                   = new Short[11];
    /** Las líneas del balance de masas que se incluirán en el fichero Excel. */
    private List<TVentasVista> lMasas;
    /** Los totales de las masas se incluirán en el fichero Excel. */
    private List<TVentasVista> lTotalesMasas;
    /** Los filtros aplicados para mostrar el balance de masas. */
    private List<String>       lFiltros;
    private final short        VERTICAL_ALIGNMENT_CENTER = 4;
    private String             nombreLogo;

    @SuppressWarnings("unchecked")
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //VARIABLES REQUIRED IN MODEL
        String sheetName = (String) model.get("sheetname");
        lMasas = (List<TVentasVista>) model.get("masas");
        lFiltros = (List<String>) model.get("filtros");
        lTotalesMasas = (List<TVentasVista>) model.get("totales");
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
        sheet.setColumnWidth(0, 777 * (74 / 7));
        sheet.setColumnWidth(1, 777 * (74 / 7));
        // DOC.PAGO
        sheet.setColumnWidth(2, 777 * (74 / 7));
        // NETO
        sheet.setColumnWidth(3, 777 * (74 / 7));
        // IMPUESTOS
        sheet.setColumnWidth(4, 777 * (74 / 7));
        // TOTAL
        sheet.setColumnWidth(5, 777 * (74 / 7));
        // SITUACIÓN
        sheet.setColumnWidth(6, 777 * (74 / 7));
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

        // Creamos el título del documento
        crearTituloDocumento(sheet, workbook, (short) 2, (short) 3);

        // MOSTRAMOS LOS FILTROS APLICADOS, SI LOS HAY
        Short currentRow = mostrarFiltros(sheet, workbook, (short) 8, (short) 0);

        currentRow++;

        // Mostramos los totales
        if (!lFiltros.get(0).equals("Por ggn")) {
            crearTablaTotales(workbook, sheet, currentRow);
        }

        currentRow++;
        currentRow++;
        currentRow++;

        if (!lMasas.isEmpty()) {
            if (!lFiltros.get(0).equals("Por producto")) {
                // Pintamos la cabecera de la tabla
                crearCabeceraTablaMasas(workbook, sheet, currentRow);
            } else {
                crearCabeceraTablaMasasPorProducto(workbook, sheet, currentRow);
            }

            currentRow++;

            if (lFiltros.get(0).equals("Por producto")) {
                rellenarTablaDatosMasasPorProducto(workbook, sheet, currentRow);
            } else {
                // Rellenamos la tabla de contenido.
                rellenarTablaDatosMasas(workbook, sheet, currentRow);
            }
        }

    }

    /**
     * Método que nos crea la cabecera de la tabla
     * @param workbook El fichero Excel
     * @param sheet La hoja.
     */
    private void crearCabeceraTablaMasas(Workbook workbook, Sheet sheet, int currentRow) {

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

        // PRODUCTO
        text = new HSSFRichTextString("Producto");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        cell2.setCellValue(text);
        currentColumn++;

        if (lFiltros.get(0).equals("Nacional/No nacional") || lFiltros.get(0).equals("Por paises")) {
            // Orígen
            text = new HSSFRichTextString("Orígen");
        } else if (lFiltros.get(0).equals("Por calidad") || lFiltros.get(0).equals("Por ggn")) {
            text = new HSSFRichTextString("Calidad");
        }
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        if (lFiltros.get(0).equals("Por ggn")) {
            text = new HSSFRichTextString("GGN");

            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloCeldaCabecera);
            cell2.setCellValue(text);
            currentColumn++;
        }

        // Compras(kg)
        text = new HSSFRichTextString("Compras(kg)");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        // Ventas(kg)
        text = new HSSFRichTextString("Ventas(kg)");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        // Stock(kg)
        text = new HSSFRichTextString("Stock(kg)");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

    }

    /**
     * Método que nos crea la cabecera de la tabla
     * @param workbook El fichero Excel
     * @param sheet La hoja.
     */
    private void crearCabeceraTablaMasasPorProducto(Workbook workbook, Sheet sheet, int currentRow) {

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

        // PRODUCTO
        text = new HSSFRichTextString("Producto");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        // VARIEDAD
        text = new HSSFRichTextString("Variedad");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        // CERTIFICACIÓN
        text = new HSSFRichTextString("Certificación");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        // ORÍGEN
        text = new HSSFRichTextString("Orígen");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        // GGN
        text = new HSSFRichTextString("GGN");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        // Compras(kg)
        text = new HSSFRichTextString("Compras(kg)");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        // Ventas(kg)
        text = new HSSFRichTextString("Ventas(kg)");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

        // Stock(kg)
        text = new HSSFRichTextString("Stock(kg)");
        cell2 = headerRow.createCell(currentColumn);
        cell2.setCellStyle(estiloCeldaCabecera);
        cell2.setCellValue(text);
        CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
        currentColumn++;

    }

    /**
     * Método que nos rellena la tabla de datos.
     */
    private void crearTablaTotales(Workbook workbook, Sheet sheet, short currentRow) {

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

        // CABECERA TABLA TOTALES
        headerRow = sheet.createRow(currentRow);
        // Variables necesarias para mostrar los datos
        HSSFRichTextString text = null;
        Cell cell2 = null;
        estiloContenidoTabla.setFillForegroundColor(colores[1]);
        if (lFiltros.get(0).equals("Nacional/No nacional") || lFiltros.get(0).equals("Por paises") || lFiltros.get(0).equals("Por producto")) {
            // Compras(kg)
            text = new HSSFRichTextString("Compras(kg)");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;
            // Ventas(kg)
            text = new HSSFRichTextString("Ventas(kg)");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // Stock(kg)
            text = new HSSFRichTextString("Stock(kg)");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;
        } else {
            // Compras(kg) (Calidad B)
            text = new HSSFRichTextString("Compras(kg) (Calidad B)");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;
            // Compras(kg) (Calidad C)
            text = new HSSFRichTextString("Compras(kg) (Calidad C)");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;
            // Ventas(kg) (Calidad B)
            text = new HSSFRichTextString("Ventas(kg) (Calidad B)");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;
            // Ventas(kg) (Calidad C)
            text = new HSSFRichTextString("Ventas(kg) (Calidad C)");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // Stock(kg) (Calidad B)
            text = new HSSFRichTextString("Stock(kg) (Calidad B)");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // Stock(kg) (Calidad C)
            text = new HSSFRichTextString("Stock(kg) (Calidad B)");
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;
        }

        DecimalFormat df = new DecimalFormat("#,##0.00");
        estiloContenidoTabla.setFillForegroundColor(colores[0]);
        currentRow++;
        for (TVentasVista venta : lTotalesMasas) {
            headerRow = sheet.createRow(currentRow);
            currentColumn = 0;
            if (lFiltros.get(0).equals("Nacional/No nacional") || lFiltros.get(0).equals("Por paises") || lFiltros.get(0).equals("Por producto")) {
                text = new HSSFRichTextString(df.format(venta.getKgs()));
                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;

                text = new HSSFRichTextString(df.format(venta.getKgsFin()));
                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;

                text = new HSSFRichTextString(venta.getKgsEnvase());
                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;
            } else {
                text = new HSSFRichTextString(venta.getLote());
                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;

                text = new HSSFRichTextString(venta.getLoteFin());
                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;

                text = new HSSFRichTextString(venta.getAlbaran());
                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;

                text = new HSSFRichTextString(venta.getAlbaranFin());
                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;

                text = new HSSFRichTextString(venta.getCalibre());
                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;

                text = new HSSFRichTextString(venta.getCalibreFin());
                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;
            }

            currentRow++;
        }

    }

    /**
     * Método que nos rellena la tabla de datos.
     */
    private void rellenarTablaDatosMasas(Workbook workbook, Sheet sheet, short currentRow) {

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
        DecimalFormat df = new DecimalFormat("#,##0.00");
        int i = 0;
        for (TVentasVista venta : lMasas) {
            headerRow = sheet.createRow(currentRow);
            currentColumn = 0;

            estiloContenidoTabla.setFillForegroundColor(colores[i % 2]);

            // PRODUCTO
            text = new HSSFRichTextString(venta.getVariedad());
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            if (lFiltros.get(0).equals("Nacional/No nacional") || lFiltros.get(0).equals("Por paises")) {
                // Orígen
                text = new HSSFRichTextString(venta.getOrigen());
            } else if (lFiltros.get(0).equals("Por calidad") || lFiltros.get(0).equals("Por ggn")) {
                text = new HSSFRichTextString(venta.getCalidadCompra());
            }
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            if (lFiltros.get(0).equals("Por ggn")) {
                text = new HSSFRichTextString(venta.getAlbaran());
                cell2 = headerRow.createCell(currentColumn);
                cell2.setCellStyle(estiloContenidoTabla);
                cell2.setCellValue(text);
                CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
                currentColumn++;
            }

            // COMPRAS(kg)
            text = new HSSFRichTextString(df.format(venta.getKgsNetos()));
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // Ventas(kg)
            text = new HSSFRichTextString(df.format(venta.getKgsNetosFin()));
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // Stock(kg)
            text = new HSSFRichTextString(venta.getKgsEnvase());
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            i++;
            currentRow++;
        }

    }

    /**
     * Método que nos rellena la tabla de datos.
     */
    private void rellenarTablaDatosMasasPorProducto(Workbook workbook, Sheet sheet, short currentRow) {

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
        DecimalFormat df = new DecimalFormat("#,##0.00");
        int i = 0;
        for (TVentasVista venta : lMasas) {
            headerRow = sheet.createRow(currentRow);
            currentColumn = 0;

            estiloContenidoTabla.setFillForegroundColor(colores[i % 2]);

            // PRODUCTO
            text = new HSSFRichTextString(venta.getFamilia());
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // VARIEDAD
            text = new HSSFRichTextString(venta.getVariedad());
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // CERTIFICACIÓN
            text = new HSSFRichTextString(venta.getCalidadCompra());
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // ORÍGEN
            text = new HSSFRichTextString(venta.getOrigen());
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // GGN
            text = new HSSFRichTextString(venta.getAlbaran());
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // COMPRAS(kg)
            text = new HSSFRichTextString(df.format(venta.getKgsNetos()));
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // Ventas(kg)
            text = new HSSFRichTextString(df.format(venta.getKgsNetosFin()));
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

            // Stock(kg)
            text = new HSSFRichTextString(venta.getKgsEnvase());
            cell2 = headerRow.createCell(currentColumn);
            cell2.setCellStyle(estiloContenidoTabla);
            cell2.setCellValue(text);
            CellUtil.setAlignment(cell2, workbook, CellStyle.ALIGN_CENTER);
            currentColumn++;

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
    private void crearTituloDocumento(Sheet sheet, Workbook workbook, short currentRow, short currentColumn) {

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
        HSSFRichTextString text = new HSSFRichTextString("BALANCE DE MASAS" + " (" + lFiltros.get(0).toUpperCase() + ")");
        Cell cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);
        CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);

        // ORDEN DE TRANSPORTE
        CellRangeAddress regionTitulo = CellRangeAddress.valueOf("D3:H5");

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
    private short mostrarFiltros(Sheet sheet, Workbook workbook, short currentRow, short currentColumn) {

        //CREATE STYLE FOR HEADER
        CellStyle estiloCeldaCabecera = workbook.createCellStyle();
        Font cabeceraFuente = workbook.createFont();
        cabeceraFuente.setFontHeightInPoints((short) 10);
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
        HSSFRichTextString text = new HSSFRichTextString("Filtros aplicados: ");
        Cell cell = headerRow.createCell(currentColumn);
        cell.setCellStyle(estiloCeldaCabecera);
        cell.setCellValue(text);

        currentRow++;

        int cnt = 0;
        currentColumn = 0;
        Boolean entra = false;
        while (cnt < lFiltros.size() - 1) {
            entra = false;
            if (cnt == 0 && !lFiltros.get(cnt + 1).trim().isEmpty()) {
                headerRow = sheet.createRow(currentRow);
                cell = headerRow.createCell(currentColumn);
                cell.setCellStyle(estiloCeldaCabecera);
                cell.setCellValue("PRODUCTO: " + lFiltros.get(cnt + 1));
                CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
                entra = true;
            } else if (cnt == 1 && !lFiltros.get(cnt + 1).trim().isEmpty()) {
                headerRow = sheet.createRow(currentRow);
                cell = headerRow.createCell(currentColumn);
                cell.setCellStyle(estiloCeldaCabecera);
                cell.setCellValue("CALIDAD: " + lFiltros.get(cnt + 1));
                CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
                entra = true;
            } else if (cnt == 3 && !lFiltros.get(cnt + 1).trim().isEmpty()) {
                headerRow = sheet.createRow(currentRow);
                cell = headerRow.createCell(currentColumn);
                cell.setCellStyle(estiloCeldaCabecera);
                cell.setCellValue("GGN: " + lFiltros.get(cnt + 1));
                CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
                entra = true;
            } else if (cnt == 4 && !lFiltros.get(cnt + 1).trim().isEmpty()) {
                headerRow = sheet.createRow(currentRow);
                cell = headerRow.createCell(currentColumn);
                cell.setCellStyle(estiloCeldaCabecera);
                cell.setCellValue("FECHA DESDE: " + lFiltros.get(cnt + 1));
                CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
                entra = true;
            } else if (cnt == 5 && !lFiltros.get(cnt + 1).trim().isEmpty()) {
                headerRow = sheet.createRow(currentRow);
                cell = headerRow.createCell(currentColumn);
                cell.setCellStyle(estiloCeldaCabecera);
                cell.setCellValue("FECHA HASTA: " + lFiltros.get(cnt + 1));
                CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
                entra = true;
            }
            if (entra) {
                currentRow++;
            }
            cnt++;
        }
        return currentRow;
    }

}
