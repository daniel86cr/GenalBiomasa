/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.exportar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * @author Daniel Carmona Romero
 * Clase que nos realiza la generación de un fichero Excel a partir de los datos pasados por los parámetros.
 */
public class VistaExcel extends AbstractXlsView {

    @SuppressWarnings("unchecked")
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //VARIABLES REQUIRED IN MODEL
        String sheetName = (String) model.get("sheetname");
        List<String> headers = (List<String>) model.get("headers");
        List<List<String>> results = (List<List<String>>) model.get("results");
        List<String> numericColumns = new ArrayList<String>();
        if (model.containsKey("numericcolumns"))
            numericColumns = (List<String>) model.get("numericcolumns");
        //BUILD DOC
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth((short) 12);
        int currentRow = 0;
        short currentColumn = 0;
        //CREATE STYLE FOR HEADER
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);
        //POPULATE HEADER COLUMNS
        Row headerRow = sheet.createRow(currentRow);
        for (String header : headers) {
            HSSFRichTextString text = new HSSFRichTextString(header);
            Cell cell = headerRow.createCell(currentColumn);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(text);
            currentColumn++;
        }
        //POPULATE VALUE ROWS/COLUMNS
        currentRow++;//exclude header
        for (List<String> result : results) {
            currentColumn = 0;
            Row row = sheet.createRow(currentRow);
            for (String value : result) {//used to count number of columns
                Cell cell = row.createCell(currentColumn);
                if (numericColumns.contains(headers.get(currentColumn))) {
                    cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(value);
                } else {
                    HSSFRichTextString text = new HSSFRichTextString(value);
                    cell.setCellValue(text);
                }
                currentColumn++;
            }
            currentRow++;
        }

    }

}
