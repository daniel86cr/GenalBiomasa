/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.exportar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.common.ControlPtSetup;
import com.dina.genasoft.db.entity.TControlProductoTerminado;
import com.dina.genasoft.db.entity.TControlProductoTerminadoFotos;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesBrix;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCaja;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesCalibre;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesConfeccion;
import com.dina.genasoft.db.entity.TControlProductoTerminadoPesajesPenetromia;
import com.dina.genasoft.db.entity.TControlProductoTerminadoVista;
import com.dina.genasoft.db.entity.TLineaControlProductoTerminadoVista;
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
public class InformeControlPDF extends PdfPageEventHelper {
    /** El log de la aplicación*/
    private static final org.slf4j.Logger log        = org.slf4j.LoggerFactory.getLogger(BalanceMasasPDF.class);
    /** Contendrá el nombre de la aplicación.*/
    @Value("${pdf.temp}")
    private String                        pdfTemp;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${pdf.temp2}")
    private String                        pdfTemp2;
    /** Inyección de Spring para poder acceder a la capa de datos de controles de PT.*/
    @Autowired
    private ControlPtSetup                controlPTSetup;
    /** El path del código de barras.*/
    private String                        path1      = null;
    /** Fuente de párrafo. */
    private Font                          boldFont   = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont15 = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont10 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont7  = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont6  = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont4  = new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont9  = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont17 = new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          cabecera   = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          cabecera2  = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          cabecera3  = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          cabecera10 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          fLinea     = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          fLinea6    = new Font(Font.FontFamily.TIMES_ROMAN, new Float(6.5), Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          fLinea7    = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          fLinea2    = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          error      = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
    /** Las columnas del documento. */
    public static final Rectangle[]       COLUMNS    = { new Rectangle(36, 36, 192, 806), new Rectangle(204, 36, 348, 806), new Rectangle(360, 36, 504, 806) };
    /** Color de las celdas sombreadas. */
    private BaseColor                     myColor    = new BaseColor(102, 255, 102);                                                                           // or red, green, blue, alpha
    /** La hoja final.*/
    private Document                      document   = null;
    /** El logo de la emprea.*/
    private Image                         img        = null;
    /** La firma del operario, si existe.*/
    private Image                         img2       = null;
    private PdfWriter                     writer;

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
    public void createPdf(List<Integer> lIdsControlPT, String nombre, String pathLogo) throws IOException, DocumentException, GenasoftException {

        // Establecemos los márgenes del documento.
        float left = 20;
        float right = 10;
        float top = 5;
        float bottom = 0;

        path1 = pathLogo;

        img = Image.getInstance(path1);

        document = new Document(PageSize.A4, left, right, top, bottom);

        writer = PdfWriter.getInstance(document, new FileOutputStream(nombre + ".pdf"));

        path1 = pathLogo;

        if (!lIdsControlPT.isEmpty()) {

            document.open();

            try {

                List<TControlProductoTerminado> lControles = controlPTSetup.obtenerControlPtIds(lIdsControlPT);

                List<TControlProductoTerminadoVista> lControlesVista = controlPTSetup.convertirProductoTerminadoVista(lControles);

                List<TLineaControlProductoTerminadoVista> lLineas = null;

                int cnt = 0;

                for (TControlProductoTerminadoVista cPt : lControlesVista) {

                    lLineas = controlPTSetup.obtenerLineasControlProductoTerminadoVista(Integer.valueOf(cPt.getId()));

                    for (TLineaControlProductoTerminadoVista linea : lLineas) {
                        // Mostramos el logo y el título del documento.
                        informacionInicial();

                        try {
                            img2 = Image.getInstance(pdfTemp2 + "/Firma_" + linea.getIdControlPt() + ".png");
                        } catch (Exception e) {
                            img2 = null;
                        }

                        // Pintamos la información referente a la línea de envasado
                        crearLineaEnvasado(cPt);

                        crearContenidoControlPt(cPt, linea);

                        //crearLineaControlPt(cPt);

                        nuevaPagina();

                        tituloControlEtiquetado(linea.getIdProdudcto());
                        incluirImagenesControlPt(Integer.valueOf(linea.getId()));

                        cnt++;

                        if (cnt != lControles.size()) {
                            nuevaPagina();
                        }
                    }

                }

                // Info adicional cabecera.
                Paragraph preface2 = new Paragraph();
                preface2.add(new Paragraph("  ", boldFont15));
                preface2.add(new Paragraph(" ", boldFont15));

                document.add(preface2);

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

        } else

        {
            document.open();
            document.resetPageCount();
            PdfContentByte cb = writer.getDirectContent();
            Phrase header3 = new Phrase("Se ha producido el siguiente error: ", error);
            Phrase header4 = null;

            header4 = new Phrase("No se han encontrado el registros a mostrar", error);

            // Albarán nº
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header3, 300f, 500f, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header4, 300f, 485f, 0);

            document.newPage();

            document.close();
        }

    }

    /**
     * Método que nos añade al documento la cabecera inicial del documento.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException
     */
    private void informacionInicial() throws IOException, GenasoftException, DocumentException {
        img.scalePercent(50f);
        img.setScaleToFitHeight(false);
        //document.add(img);

        PdfPCell cell;

        float[] columnWidths = { 2, 5, 2 };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);

        // IMAGEN
        cell = new PdfPCell(img);
        cell.setFixedHeight(55f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // TÍTULO
        cell = new PdfPCell(new Phrase("CONTROL DE PRODUCTO TERMINADO", boldFont15));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // TABLA CON EL CÓDIGO, REVISIÓN Y FECHA

        // CÓDIGO
        PdfPTable table2 = new PdfPTable(1);
        cell = new PdfPCell(new Phrase("Código: M06R04", boldFont10));
        table2.addCell(cell);
        // REVISIÓN
        cell = new PdfPCell(new Phrase("REV: 7", boldFont10));
        table2.addCell(cell);
        // FECHA
        cell = new PdfPCell(new Phrase("Fecha: May. 2021", boldFont10));
        table2.addCell(cell);

        table.addCell(table2);
        document.add(table);

        // Info adicional cabecera.
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("  ", boldFont15));

        document.add(preface);

    }

    /**
     * Método que nos añade al documento la cabecera inicial del documento.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException
     */
    private void tituloControlEtiquetado(String titulo) throws IOException, GenasoftException, DocumentException {
        // Info adicional cabecera.
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("  ", boldFont15));
        preface.add(new Paragraph(" ", boldFont15));

        document.add(preface);

        PdfPCell cell;

        float[] columnWidths = { 1 };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);

        // TÍTULO
        cell = new PdfPCell(new Phrase("CONTROL DE ETIQUETADO PRODUCTO " + titulo, boldFont15));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        document.add(table);

        // Info adicional cabecera.
        Paragraph preface2 = new Paragraph();
        preface2.add(new Paragraph("  ", boldFont15));

        document.add(preface2);

    }

    /**
     * Método que nos añade al documento la cabecera de línea de envasado.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException
     */
    private void crearLineaEnvasado(TControlProductoTerminadoVista controlPt) throws IOException, GenasoftException, DocumentException {

        Integer numCols = 3;
        LineDash solid = new SolidLine();
        PdfPCell cell;

        PdfPTable table = new PdfPTable(numCols);
        table.setWidthPercentage(100);

        // TEXTO LÍNEA DE ENVASADO
        cell = new PdfPCell(new Phrase("LÍNEA DE ENVASADO", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setColspan(3);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // Pintamos la nave.
        if (controlPt.getIndNave1().equals("Sí")) {
            // NAVE 1
            cell = new PdfPCell(new Phrase("NAVE 1", boldFont10));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            table.addCell(cell);
        } else if (controlPt.getIndNave2().equals("Sí")) {
            // NAVE 2
            cell = new PdfPCell(new Phrase("NAVE 2", boldFont10));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            table.addCell(cell);
        } else if (controlPt.getIndNave3().equals("Sí")) {
            // NAVE 3
            cell = new PdfPCell(new Phrase("NAVE 3", boldFont10));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            table.addCell(cell);
        }

        // PINTAMOS LA LÍNEA
        cell = new PdfPCell(new Phrase(controlPt.getLinea().trim().toUpperCase(), boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, solid));
        table.addCell(cell);

        // PINTAMOS SI ES BIO O NO
        cell = new PdfPCell(new Phrase("BIO: " + controlPt.getIndBio(), boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, solid));
        table.addCell(cell);

        document.add(table);

    }

    /**
     * Método que nos añade al documento la cabecera de línea de envasado.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException
     */
    private void crearContenidoControlPt(TControlProductoTerminadoVista controlPt, TLineaControlProductoTerminadoVista linea) throws IOException, GenasoftException, DocumentException {

        PdfPCell cell;
        LineDash solid = new SolidLine();
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        // TEXTO IDENTIFICACIÓN
        cell = new PdfPCell(new Phrase("IDENTIFICACIÓN", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(95f);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        cell.setColspan(4);
        table.addCell(cell);

        // TEXTO FECHA
        cell = new PdfPCell(new Phrase("Fecha", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // FECHA
        cell = new PdfPCell(new Phrase(controlPt.getFecha(), cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // TEXTO CLIENTE
        cell = new PdfPCell(new Phrase("Cliente", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // CLIENTE
        cell = new PdfPCell(new Phrase(controlPt.getIdCliente(), cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // TEXTO Nº PEDIDO
        cell = new PdfPCell(new Phrase("Nº pedido", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // Nº PEDIDO
        cell = new PdfPCell(new Phrase(controlPt.getNumeroPedido(), cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // TEXTO Nº CAJAS PEDIDO
        cell = new PdfPCell(new Phrase("Nº Cajas pedido", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // Nº CAJAS PEDIDO
        cell = new PdfPCell(new Phrase(linea.getNumCajasPedido(), cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // TEXTO Nº CAJAS REAL
        cell = new PdfPCell(new Phrase("Nº Cajas real", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // Nº CAJAS REAL
        cell = new PdfPCell(new Phrase(linea.getNumCajasReal(), cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // TEXTO PRODUCTO
        cell = new PdfPCell(new Phrase("Producto", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // PRODUCTO
        cell = new PdfPCell(new Phrase(linea.getIdProdudcto(), cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // TEXTO VARIEDAD Y CALIBRE/DIÁMETRO
        cell = new PdfPCell(new Phrase("Variedad y calibre/diámetro", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        String texto = "-";

        if (linea.getIdVariedad() != null && !linea.getIdVariedad().isEmpty() && !linea.getIdVariedad().equals("-")) {
            texto = controlPt.getIdCalibre();
        }
        if (linea.getIdDiametro() != null && !linea.getIdDiametro().isEmpty() && !linea.getIdDiametro().equals("-")) {
            texto = linea.getIdDiametro();
        }

        // VARIEDAD Y CALIBRE/DIÁMETRO
        cell = new PdfPCell(new Phrase(linea.getIdVariedad() + ", " + texto, cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        if (controlPt.getIndMallas().equals("Sí") || (!controlPt.getIndFlowPack().isEmpty() && !controlPt.getIndFlowPack().equals("0"))) {
            texto = "Peso confección teórico";
        } else {
            texto = "Peso caja teórico";
        }
        // TEXTO PESO CAJA/CONFECCIÓN TEÓRICO
        cell = new PdfPCell(new Phrase(texto, boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // PESO CAJA/CONFECCIÓN TEÓRICO
        cell = new PdfPCell(new Phrase(linea.getPesoCaja(), cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // TEXTO CONTAMINACIÓN FÍSICA
        cell = new PdfPCell(new Phrase("Contaminación física", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // CONTAMINACIÓN FÍSICA
        cell = new PdfPCell(new Phrase(linea.getIndContaminaFisica(), cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // TEXTO CONTAMINACIÓN QUÍMICA
        cell = new PdfPCell(new Phrase("Contaminación química", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // CONTAMINACIÓN QUÍMICA
        cell = new PdfPCell(new Phrase(linea.getIndContaminaQuimica(), cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // TEXTO CONTAMINACIÓN BIOLÓGICA
        cell = new PdfPCell(new Phrase("Contaminación biológica", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // CONTAMINACIÓN BIOLÓGICA
        cell = new PdfPCell(new Phrase(linea.getIndContaminaBiologica(), cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // LIBRE
        cell = new PdfPCell(new Phrase(" ", cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setColspan(2);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // TEXTO DAÑOS INTERNOS
        cell = new PdfPCell(new Phrase("Daños internos", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // DAÑOS INTERNOS        
        cell = new PdfPCell(new Phrase("<" + linea.getdInternos() + "%", cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // TEXTO DAÑOS INTERNOS
        cell = new PdfPCell(new Phrase("Daños externos", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        // DAÑOS EXTERNOS
        cell = new PdfPCell(new Phrase("<" + linea.getdExternos() + "%", cabecera10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        document.add(table);

        PdfPTable table2 = new PdfPTable(4);
        table2.setWidthPercentage(100);

        // TEXTO CALIDAD
        cell = new PdfPCell(new Phrase("CALIDAD", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setFixedHeight(95f);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        cell.setColspan(4);
        table2.addCell(cell);

        if (controlPt.getIndMallas().equals("Sí") || (!controlPt.getIndFlowPack().isEmpty() && !controlPt.getIndFlowPack().equals("0"))) {
            // PESAJES CONFECCIÓN        
            table2.addCell(crearTablaPesajesConfeccion(Integer.valueOf(linea.getId()), Utils.formatearValorDouble2(linea.getPesoCaja())));
        } else {
            // PESAJES CAJA        
            table2.addCell(crearTablaPesajesCajas(Integer.valueOf(linea.getId()), Utils.formatearValorDouble2(linea.getPesoCaja())));
        }

        String[] cal = controlPt.getIdCalibre().split(",");
        Integer min;
        Integer max;
        try {
            min = Integer.valueOf(cal[0].split("\\(")[1].trim());
            max = Integer.valueOf(cal[1].split("\\)")[0].trim());
        } catch (Exception e) {
            min = 0;
            max = 0;
        }

        // CONTROL CALIBRE
        table2.addCell(crearTablaPesajesCalibres(Integer.valueOf(linea.getId()), min, max));
        // PENETROMÍA
        table2.addCell(crearTablaPesajesPenetromia(Integer.valueOf(linea.getId())));
        // BRIX
        table2.addCell(crearTablaPesajesBrix(Integer.valueOf(linea.getId())));

        document.add(table2);

        if (img2 != null) {
            PdfPTable table3 = new PdfPTable(2);
            table3.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("FIRMA OPERARIO:  " + controlPt.getUsuFoto() + ", Fecha: " + controlPt.getFechaFoto(), cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            cell.setColspan(2);
            table3.addCell(cell);
            img2.scalePercent(20f);
            img2.setScaleToFitHeight(false);
            cell = new PdfPCell(img2);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setFixedHeight(100f);
            table3.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table3.addCell(cell);
            document.add(table3);
        }
    }

    /**
     * Método que nos añade al documento la cabecera de línea de envasado.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException
     */
    private void incluirImagenesControlPt(Integer idLinea) throws IOException, GenasoftException, DocumentException {

        // Info adicional cabecera.
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("  ", boldFont15));
        preface.add(new Paragraph(" ", boldFont15));
        preface.add(new Paragraph(" ", boldFont15));
        preface.add(new Paragraph(" ", boldFont15));

        document.add(preface);

        PdfPCell cell;

        PdfPTable tableCajas = new PdfPTable(1);
        tableCajas.setWidthPercentage(100);

        PdfPTable tableResto = new PdfPTable(5);
        tableResto.setWidthPercentage(100);

        List<TControlProductoTerminadoFotos> lFotos = controlPTSetup.obtenerImagenesIdLinea(idLinea);

        // Integer max = 5;
        Integer cnt = 0;
        Image foto;
        Integer cnt2 = 0;
        // Incluimos las fotos de cajas
        for (TControlProductoTerminadoFotos fotoCaja : lFotos) {
            try {
                foto = Image.getInstance(fotoCaja.getDescripcionFoto());
                foto.scalePercent(35f);
                foto.setScaleToFitHeight(true);
                cell = new PdfPCell(foto);
                cell.setFixedHeight(75f);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                tableResto.addCell(cell);
                cnt++;
                cnt2++;
                if (cnt2.equals(5)) {
                    cnt2 = 0;
                }
            } catch (Exception e) {
            }
        }

        while (cnt2 < 5) {
            cell = new PdfPCell(new Phrase(" ", cabecera10));
            cell.setFixedHeight(75f);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            tableResto.addCell(cell);
            cnt2++;
        }

        document.add(tableResto);

    }

    /**
     * Método que nos añade al documento la cabecera de línea de envasado.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException
     */
    private void incluirImagenesControlPtOLD(Integer idLinea) throws IOException, GenasoftException, DocumentException {

        // Info adicional cabecera.
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("  ", boldFont15));
        preface.add(new Paragraph(" ", boldFont15));
        preface.add(new Paragraph(" ", boldFont15));
        preface.add(new Paragraph(" ", boldFont15));

        document.add(preface);

        PdfPCell cell;

        LineDash solid = new SolidLine();

        PdfPTable tableCajas = new PdfPTable(5);
        tableCajas.setWidthPercentage(100);

        PdfPTable tableResto = new PdfPTable(3);
        tableResto.setWidthPercentage(100);

        List<TControlProductoTerminadoFotos> lFotos = controlPTSetup.obtenerFotosCajasLinea(idLinea);

        Integer max = 5;
        Integer cnt = 0;
        Image foto;
        // Incluimos las fotos de cajas
        for (TControlProductoTerminadoFotos fotoCaja : lFotos) {
            if (cnt.equals(max)) {
                break;
            }
            try {
                foto = Image.getInstance(fotoCaja.getDescripcionFoto());
                foto.scalePercent(35f);
                foto.setScaleToFitHeight(true);
                cell = new PdfPCell(foto);
                cell.setFixedHeight(75f);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                tableCajas.addCell(cell);
            } catch (Exception e) {
                cell = new PdfPCell(new Phrase(" ", cabecera10));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                tableCajas.addCell(cell);
            }
            cnt++;
        }

        while (cnt < max) {
            cell = new PdfPCell(new Phrase(" ", cabecera10));
            cell.setFixedHeight(75f);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            tableCajas.addCell(cell);
            cnt++;
        }

        // Añadimos el texto a pie de foto
        // FOTO 1
        cell = new PdfPCell(new Phrase("FOTO CAJAS 1", cabecera10));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        tableCajas.addCell(cell);
        // FOTO 2
        cell = new PdfPCell(new Phrase("FOTO CAJAS 2", cabecera10));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        tableCajas.addCell(cell);
        // FOTO 3
        cell = new PdfPCell(new Phrase("FOTO CAJAS 3", cabecera10));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        tableCajas.addCell(cell);
        // FOTO 4
        cell = new PdfPCell(new Phrase("FOTO CAJAS 4", cabecera10));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        tableCajas.addCell(cell);
        // FOTO 5
        cell = new PdfPCell(new Phrase("FOTO CAJAS 5", cabecera10));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        tableCajas.addCell(cell);

        document.add(tableCajas);

        // Info adicional cabecera.
        Paragraph preface2 = new Paragraph();
        preface2.add(new Paragraph("  ", boldFont15));
        preface2.add(new Paragraph(" ", boldFont15));

        document.add(preface2);

        // Cargamos la foto de palé
        List<TControlProductoTerminadoFotos> lFotosPale = controlPTSetup.obtenerFotosPaleLinea(idLinea);

        if (!lFotosPale.isEmpty()) {
            try {
                foto = Image.getInstance(lFotosPale.get(0).getDescripcionFoto());
                foto.scalePercent(35f);
                foto.setScaleToFitHeight(true);
                cell = new PdfPCell(foto);
                cell.setFixedHeight(75f);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                tableResto.addCell(cell);
            } catch (Exception e) {
                cell = new PdfPCell(new Phrase(" ", cabecera10));
                cell.setFixedHeight(75f);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                tableResto.addCell(cell);
            }
        } else {
            cell = new PdfPCell(new Phrase(" ", cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setFixedHeight(75f);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            tableResto.addCell(cell);
        }

        // Cargamos la foto de etiqueta interior.
        List<TControlProductoTerminadoFotos> lFotosEtInt = controlPTSetup.obtenerFotosEtiquetasIntLinea(idLinea);

        if (!lFotosEtInt.isEmpty()) {
            try {
                foto = Image.getInstance(lFotosEtInt.get(0).getDescripcionFoto());
                foto.scalePercent(35f);
                foto.setScaleToFitHeight(true);
                cell = new PdfPCell(foto);
                cell.setFixedHeight(75f);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                tableResto.addCell(cell);
            } catch (Exception e) {
                cell = new PdfPCell(new Phrase(" ", cabecera10));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                tableResto.addCell(cell);
            }
        } else {
            cell = new PdfPCell(new Phrase(" ", cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setFixedHeight(75f);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            tableResto.addCell(cell);
        }

        // Cargamos la foto de etiqueta exterior.
        List<TControlProductoTerminadoFotos> lFotosEtExt = controlPTSetup.obtenerFotosEtiquetasExtLinea(idLinea);

        if (!lFotosEtExt.isEmpty()) {
            try {
                foto = Image.getInstance(lFotosEtExt.get(0).getDescripcionFoto());
                foto.scalePercent(35f);
                foto.setScaleToFitHeight(true);
                cell = new PdfPCell(foto);
                cell.setFixedHeight(75f);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                tableResto.addCell(cell);
            } catch (Exception e) {
                cell = new PdfPCell(new Phrase(" ", cabecera10));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                tableResto.addCell(cell);
            }
        } else {
            cell = new PdfPCell(new Phrase(" ", cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setFixedHeight(75f);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            tableResto.addCell(cell);
        }

        // Añadimos el texto a pie de foto
        // FOTO PALÉ.
        cell = new PdfPCell(new Phrase("FOTOS PALET", cabecera10));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        tableResto.addCell(cell);
        // FOTO ETIQUETA INTERIOR.
        cell = new PdfPCell(new Phrase("FOTOS ETIQUETA INTERIOR", cabecera10));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        tableResto.addCell(cell);
        // FOTO ETIQUETA EXTERIOR.
        cell = new PdfPCell(new Phrase("FOTOS ETIQUETA EXTERIOR", cabecera10));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        tableResto.addCell(cell);

        document.add(tableResto);

    }

    private void nuevaPagina() throws DocumentException {
        document.newPage();
        document.setPageSize(PageSize.A4);

    }

    private void nuevaPaginaHorizontal() throws DocumentException {
        document.newPage();
        document.setPageSize(PageSize.A4.rotate());
        document.left(10f);
        document.right(10f);
        document.top(60f);
        document.bottom(0f);
    }

    private PdfPTable crearTablaPesajesCajas(Integer idCPt, Double pesoCaja) {
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;
        List<TControlProductoTerminadoPesajesCaja> lPes = controlPTSetup.obtenerPesajesCajaIdLinea(idCPt);
        cell = new PdfPCell(new Phrase("#", boldFont));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Peso caja", boldFont));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        Integer cnt = 1;
        if (!lPes.isEmpty()) {
            for (TControlProductoTerminadoPesajesCaja pes : lPes) {
                cell = new PdfPCell(new Phrase("" + cnt, boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + pes.getValor(), boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                if (pes.getValor() > pesoCaja) {
                    cell.setBackgroundColor(myColor);
                }
                table.addCell(cell);
                cnt++;
            }
        }
        while (cnt < 11) {
            cell = new PdfPCell(new Phrase("" + cnt, boldFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("-", boldFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cnt++;
        }

        return table;
    }

    private PdfPTable crearTablaPesajesConfeccion(Integer idCPt, Double pesoConf) {
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;
        List<TControlProductoTerminadoPesajesConfeccion> lPes = controlPTSetup.obtenerPesajesConfeccionIdLinea(idCPt);
        cell = new PdfPCell(new Phrase("#", boldFont));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Peso Confección", boldFont));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        Integer cnt = 1;
        if (!lPes.isEmpty()) {
            for (TControlProductoTerminadoPesajesConfeccion pes : lPes) {
                cell = new PdfPCell(new Phrase("" + cnt, boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + pes.getValor(), boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                if (pes.getValor() > pesoConf) {
                    cell.setBackgroundColor(myColor);
                }
                table.addCell(cell);
                cnt++;
            }
        }
        while (cnt < 11) {
            cell = new PdfPCell(new Phrase("" + cnt, boldFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("-", boldFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cnt++;
        }

        return table;
    }

    private PdfPTable crearTablaPesajesCalibres(Integer idCPt, Integer min, Integer max) {
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;
        List<TControlProductoTerminadoPesajesCalibre> lPes = controlPTSetup.obtenerPesajesCalibreIdLinea(idCPt);
        cell = new PdfPCell(new Phrase("#", boldFont));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Valor calibre", boldFont));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        Integer cnt = 1;
        if (!lPes.isEmpty()) {
            for (TControlProductoTerminadoPesajesCalibre pes : lPes) {
                cell = new PdfPCell(new Phrase("" + cnt, boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + pes.getValor(), boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                if (pes.getValor() >= min && pes.getValor() <= max) {
                    cell.setBackgroundColor(myColor);
                }
                table.addCell(cell);
                cnt++;
            }
        }
        while (cnt < 11) {
            cell = new PdfPCell(new Phrase("" + cnt, boldFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("-", boldFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cnt++;
        }

        return table;
    }

    private PdfPTable crearTablaPesajesPenetromia(Integer idCPt) {
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;
        List<TControlProductoTerminadoPesajesPenetromia> lPes = controlPTSetup.obtenerPesajesPenetromiaIdLinea(idCPt);
        cell = new PdfPCell(new Phrase("#", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Valor Penetromía", boldFont));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        Integer cnt = 1;
        if (!lPes.isEmpty()) {
            for (TControlProductoTerminadoPesajesPenetromia pes : lPes) {
                cell = new PdfPCell(new Phrase("" + cnt, boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + pes.getValor(), boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cnt++;
            }
        }
        while (cnt < 11) {
            cell = new PdfPCell(new Phrase("" + cnt, boldFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("-", boldFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cnt++;
        }

        return table;
    }

    private PdfPTable crearTablaPesajesBrix(Integer idCPt) {
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;
        List<TControlProductoTerminadoPesajesBrix> lPes = controlPTSetup.obtenerPesajesBrixIdLinea(idCPt);
        cell = new PdfPCell(new Phrase("#", boldFont));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("ºBrix", boldFont));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        Integer cnt = 1;
        if (!lPes.isEmpty()) {
            for (TControlProductoTerminadoPesajesBrix pes : lPes) {
                cell = new PdfPCell(new Phrase("" + cnt, boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + pes.getValor(), boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cnt++;
            }
        }
        while (cnt < 11) {
            cell = new PdfPCell(new Phrase("" + cnt, boldFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("-", boldFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cnt++;
        }

        return table;
    }

}
