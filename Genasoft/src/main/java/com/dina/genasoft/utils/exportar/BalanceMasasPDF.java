/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.exportar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.db.entity.TVentasVista;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.Utils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Daniel Carmona Romero.
 * Clase que se encarga de generar PDF con los datos solicitados.
 */
@Component
@Slf4j
@Data
public class BalanceMasasPDF extends PdfPageEventHelper {
    /** El log de la aplicación*/
    private static final org.slf4j.Logger log        = org.slf4j.LoggerFactory.getLogger(BalanceMasasPDF.class);
    /** Contendrá el nombre de la aplicación.*/
    @Value("${pdf.temp}")
    private String                        pdfTemp;
    /** El path del código de barras.*/
    private String                        path1      = null;
    /** Fuente de párrafo. */
    private Font                          boldFont   = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont15 = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont10 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont7  = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont9  = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont17 = new Font(Font.FontFamily.HELVETICA, 17, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          cabecera   = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          cabecera2  = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          cabecera3  = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          cabecera10 = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          fLinea     = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          fLinea6    = new Font(Font.FontFamily.HELVETICA, new Float(6.5), Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          fLinea7    = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          fLinea2    = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          error      = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL);
    /** Las columnas del documento. */
    public static final Rectangle[]       COLUMNS    = { new Rectangle(36, 36, 192, 806), new Rectangle(204, 36, 348, 806), new Rectangle(360, 36, 504, 806) };
    /** Color de las celdas sombreadas. */
    private BaseColor                     myColor    = new BaseColor(217, 217, 217);                                                                           // or red, green, blue, alpha
    /** La hoja final.*/
    private Document                      document   = null;
    /** El logo de la emprea.*/
    private Image                         img        = null;
    /** Las líneas del balance de masas que se incluirán en el fichero Excel. */
    private List<TVentasVista>            lMasas;
    /** Los totales de las masas se incluirán en el fichero Excel. */
    private List<TVentasVista>            lTotalesMasas;
    /** Los filtros aplicados para mostrar el balance de masas. */
    private List<String>                  lFiltros;

    interface LineDash {
        public void applyLineDash(PdfContentByte canvas);
    }

    class SolidLine implements LineDash {
        public void applyLineDash(PdfContentByte canvas) {
        }
    }

    class DottedLine implements LineDash {
        public void applyLineDash(PdfContentByte canvas) {
            canvas.setLineCap(PdfContentByte.LINE_CAP_ROUND);
            canvas.setLineDash(0, 4, 2);
        }
    }

    class DashedLine implements LineDash {
        public void applyLineDash(PdfContentByte canvas) {
            canvas.setLineDash(3, 3);
        }
    }

    class CustomBorder implements PdfPCellEvent {
        protected LineDash left;
        protected LineDash right;
        protected LineDash top;
        protected LineDash bottom;

        public CustomBorder(LineDash left, LineDash right, LineDash top, LineDash bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }

        public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            if (top != null) {
                canvas.saveState();
                top.applyLineDash(canvas);
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getTop());
                canvas.stroke();
                canvas.restoreState();
            }
            if (bottom != null) {
                canvas.saveState();
                bottom.applyLineDash(canvas);
                canvas.moveTo(position.getRight(), position.getBottom());
                canvas.lineTo(position.getLeft(), position.getBottom());
                canvas.stroke();
                canvas.restoreState();
            }
            if (right != null) {
                canvas.saveState();
                right.applyLineDash(canvas);
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getRight(), position.getBottom());
                canvas.stroke();
                canvas.restoreState();
            }
            if (left != null) {
                canvas.saveState();
                left.applyLineDash(canvas);
                canvas.moveTo(position.getLeft(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getBottom());
                canvas.stroke();
                canvas.restoreState();
            }
        }
    }

    public class ImageContent implements PdfPTableEvent {
        protected Image content;

        public ImageContent(Image content) {
            this.content = content;
        }

        public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
            try {
                PdfContentByte canvas = canvases[PdfPTable.TEXTCANVAS];
                float x = widths[0][1] - 80;
                float y = heights[0] - 0 - content.getScaledHeight();
                content.setAbsolutePosition(x, y);
                canvas.addImage(content);
            } catch (DocumentException e) {
                throw new ExceptionConverter(e);
            }
        }
    }

    /**
     * Lógica para crear el documento PDF con los datos solicitados.
     * @param idPedido El Identificador del pedido para realizar el albarán.
     * @param nombre El path donde se genera de forma temporal en disco el albarán.
     * @param pathLogo El path donde está el logo.
     * @throws IOException Si se produce alguna excepción a la hora de generar la documentación
     * @throws DocumentException Si se produce alguna excepción a la hora de generar la documentación
     */
    public void createPdf(List<TVentasVista> lRegistros, List<TVentasVista> lTotales, List<String> lFiltr, String nombre, String pathLogo) throws IOException, DocumentException, GenasoftException {

        // Establecemos los márgenes del documento.
        float left = 20;
        float right = 10;
        float top = 10;
        float bottom = 0;

        path1 = pathLogo;

        top = 100;
        img = Image.getInstance(path1);

        document = new Document(PageSize.A4, left, right, top, bottom);

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombre + ".pdf"));

        path1 = pathLogo;

        lMasas = Utils.generarListaGenerica();

        lMasas.addAll(lRegistros);

        lTotalesMasas = Utils.generarListaGenerica();

        lTotalesMasas.addAll(lTotales);

        lFiltros = Utils.generarListaGenerica();

        lFiltros.addAll(lFiltr);

        if (!lMasas.isEmpty()) {

            document.open();

            try {

                // Mostramos el logo y el título del documento.
                informacionInicial();
                if (!lFiltros.get(0).equals("Por ggn")) {
                    // Mostramos los filtros aplicados.
                    mostrarFiltros();
                    Paragraph preface = new Paragraph();
                    preface.add(new Paragraph("                                                                                           TOTALES", boldFont10));
                    preface.add(new Paragraph(" ", boldFont15));
                    document.add(preface);
                    document.add(crearTablaTotales(writer));
                }
                // Info adicional cabecera.
                Paragraph preface2 = new Paragraph();
                preface2.add(new Paragraph("  ", boldFont15));
                preface2.add(new Paragraph(" ", boldFont15));

                document.add(preface2);

                // La tabla con los datos de las masas.
                document.add(crearTablaContenidoMasas(writer));

            } catch (GenasoftException e) {
                document.resetPageCount();
                PdfContentByte cb = writer.getDirectContent();
                Phrase header3 = new Phrase("Se ha producido el siguiente error: ", error);
                Phrase header4 = new Phrase(e.getLocalizedMessage(), error);

                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header3, 300f, 500f, 0);
                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header4, 300f, 485f, 0);

                document.newPage();
            }

            document.close();

        } else {
            document.open();
            document.resetPageCount();
            PdfContentByte cb = writer.getDirectContent();
            Phrase header3 = new Phrase("Se ha producido el siguiente error: ", error);
            Phrase header4 = null;

            header4 = new Phrase("No se han identificado registros a mostrar", error);

            // Albarán nº
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header3, 300f, 500f, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header4, 300f, 485f, 0);

            document.newPage();

            document.close();
        }

    }

    private PdfPTable crearTablaVaciaLogo() throws GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;

        try {
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);

        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }
        return table;
    }

    /**
     * Método que nos crea la tabla con la información del consignatario
     * @return La tabla con los datos
     */
    private PdfPTable crearTablaTitulo() throws IOException, GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;

        try {

            table.setWidthPercentage(100);
            cell = new PdfPCell(new Phrase(" ", boldFont10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Balance de masas (" + lFiltros.get(0) + ")", boldFont10));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", fLinea));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", fLinea));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);

        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }
        return table;
    }

    /**
     * Método que nos crea la tabla con la información del consignatario
     * @return La tabla con los datos
     */
    private PdfPTable crearTablaTituloVacia() throws IOException, GenasoftException {
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;

        try {

            table.setWidthPercentage(100);
            cell = new PdfPCell(new Phrase(" ", boldFont10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", fLinea));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", fLinea));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);

        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }
        return table;
    }

    /**
     * Método que nos crea la tabla con la información del consignatario
     * @return La tabla con los datos
     */
    private PdfPTable crearTablaTotales(PdfWriter writer) throws IOException, GenasoftException {
        PdfPTable table;
        PdfPCell cell;
        LineDash solid = new SolidLine();

        if (lFiltros.get(0).equals("Nacional/No nacional") || lFiltros.get(0).equals("Por paises")) {
            table = new PdfPTable(3);
        } else {
            table = new PdfPTable(6);
        }

        try {
            table.setWidthPercentage(100);
            if (lFiltros.get(0).equals("Nacional/No nacional") || lFiltros.get(0).equals("Por paises")) {
                cell = new PdfPCell(new Phrase("Compras(kg)", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Ventas(kg)", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Stock(kg)", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase("Compras(kg) (Calidad B)", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Compras(kg) (Calidad C)", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Ventas(kg) (Calidad B)", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Ventas(kg) (Calidad C)", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Stock(kg) (Calidad B)", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Stock(kg) (Calidad B)", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
            }

            // Datos
            DecimalFormat df = new DecimalFormat("#,##0.00");

            for (TVentasVista venta : lTotalesMasas) {
                if (lFiltros.get(0).equals("Nacional/No nacional") || lFiltros.get(0).equals("Por paises")) {
                    cell = new PdfPCell(new Phrase(df.format(venta.getKgs()), cabecera2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(df.format(venta.getKgsFin()), cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(venta.getKgsEnvase(), cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase(venta.getLote(), cabecera2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(venta.getLoteFin(), cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(venta.getAlbaran(), cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(venta.getAlbaranFin(), cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(venta.getCalibre(), cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(venta.getCalibreFin(), cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                }

            }

        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }
        return table;
    }

    /**
     * Método que nos crea la tabla con la información del consignatario
     * @return La tabla con los datos
     */
    private PdfPTable crearTablaContenidoMasas(PdfWriter writer) throws IOException, GenasoftException {
        PdfPTable table;
        PdfPCell cell;
        LineDash solid = new SolidLine();

        if (lFiltros.get(0).equals("Por ggn")) {
            table = new PdfPTable(6);
        } else {
            table = new PdfPTable(5);
        }

        try {
            table.setWidthPercentage(100);
            cell = new PdfPCell(new Phrase("Producto", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            if (lFiltros.get(0).equals("Nacional/No nacional") || lFiltros.get(0).equals("Por paises")) {
                cell = new PdfPCell(new Phrase("Origen", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
            } else if (lFiltros.get(0).equals("Por calidad") || lFiltros.get(0).equals("Por ggn")) {
                cell = new PdfPCell(new Phrase("Calidad", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
            }
            if (lFiltros.get(0).equals("Por ggn")) {
                cell = new PdfPCell(new Phrase("GGN", cabecera2));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
            }
            cell = new PdfPCell(new Phrase("Compras(kg)", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Ventas(kg)", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Stock(kg)", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);

            // Datos
            DecimalFormat df = new DecimalFormat("#,##0.00");

            for (TVentasVista venta : lMasas) {
                cell = new PdfPCell(new Phrase(venta.getVariedad(), cabecera2));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                if (lFiltros.get(0).equals("Nacional/No nacional") || lFiltros.get(0).equals("Por paises")) {
                    cell = new PdfPCell(new Phrase(venta.getOrigen(), cabecera2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                } else if (lFiltros.get(0).equals("Por calidad") || lFiltros.get(0).equals("Por ggn")) {
                    cell = new PdfPCell(new Phrase(venta.getCalidadCompra(), cabecera2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                }
                if (lFiltros.get(0).equals("Por ggn")) {
                    cell = new PdfPCell(new Phrase(venta.getAlbaran(), cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Phrase(df.format(venta.getKgs()), cabecera2));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(df.format(venta.getKgsFin()), cabecera2));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(venta.getKgsEnvase(), cabecera2));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
            }

        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }
        return table;

    }

    private void informacionInicial() throws IOException, GenasoftException, DocumentException {
        img.scaleAbsolute(100f, 75f);

        //document.add(img);
        float[] columnWidths = { 1, 5, 1 };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setTableEvent(new ImageContent(img));

        table.addCell(crearTablaVaciaLogo());
        table.addCell(crearTablaTitulo());
        table.addCell(crearTablaTituloVacia());

        document.add(table);

        // Info adicional cabecera.
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("  ", boldFont15));
        preface.add(new Paragraph(" ", boldFont15));

        document.add(preface);

    }

    private void mostrarFiltros() throws DocumentException {
        int cnt = 0;
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("Filtros aplicados: \n", fLinea7));

        while (cnt < lFiltros.size() - 1) {
            if (cnt == 0 && !lFiltros.get(cnt + 1).trim().isEmpty()) {
                preface.add(new Paragraph("PRODUCTO: " + lFiltros.get(cnt + 1) + "\n"));
            } else if (cnt == 1 && !lFiltros.get(cnt + 1).trim().isEmpty()) {
                preface.add(new Paragraph("CALIDAD: " + lFiltros.get(cnt + 1) + "\n"));
            } else if (cnt == 3 && !lFiltros.get(cnt + 1).trim().isEmpty()) {
                preface.add(new Paragraph("GGN: " + lFiltros.get(cnt + 1) + "\n"));
            } else if (cnt == 4 && !lFiltros.get(cnt + 1).trim().isEmpty()) {
                preface.add(new Paragraph("FECHA DESDE: " + lFiltros.get(cnt + 1) + "\n"));
            } else if (cnt == 5 && !lFiltros.get(cnt + 1).trim().isEmpty()) {
                preface.add(new Paragraph("FECHA HASTA: " + lFiltros.get(cnt + 1) + "\n"));
            }
            cnt++;
        }

        preface.add(new Paragraph("  ", boldFont15));
        preface.add(new Paragraph("  ", boldFont15));

        document.add(preface);

    }

}
