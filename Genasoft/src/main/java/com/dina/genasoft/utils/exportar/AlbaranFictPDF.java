/**
 * Aplicacion Control transporte Brostel.
 * http://www.brostel.net/
 * Copyright (C) 2020
 */
package com.dina.genasoft.utils.exportar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.common.ImportadorSetup;
import com.dina.genasoft.common.ProveedoresSetup;
import com.dina.genasoft.db.entity.TProveedores;
import com.dina.genasoft.db.entity.TVentasFictVista;
import com.dina.genasoft.exception.GenasoftException;
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
public class AlbaranFictPDF extends PdfPageEventHelper {
    /** Inyección de Spring para poder acceder a la capa de datos de proveedores.*/
    @Autowired
    private ProveedoresSetup              proveedoresSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de importación.*/
    @Autowired
    private ImportadorSetup               importadorSetup;
    /** El log de la aplicación*/
    private static final org.slf4j.Logger log         = org.slf4j.LoggerFactory.getLogger(AlbaranFictPDF.class);
    /** Contendrá el nombre de la aplicación.*/
    @Value("${pdf.temp}")
    private String                        pdfTemp;
    /** Contendrá el ID de la dirección de la empresa.*/
    @Value("${app.id.dir}")
    private String                        appIdDir;
    /** Fuente de párrafo. */
    private Font                          boldFont    = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont7   = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont120 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont12  = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont16  = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont162 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont19  = new Font(Font.FontFamily.HELVETICA, 19, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          cabecera    = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          cabecera2   = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          cabecera3   = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          fLineaNeg   = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          fLinea      = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          fLinea2     = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          error       = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL);
    /** Las columnas del documento. */
    public static final Rectangle[]       COLUMNS     = { new Rectangle(36, 36, 192, 806), new Rectangle(204, 36, 348, 806), new Rectangle(360, 36, 504, 806) };
    /** Color de las celdas sombreadas. */
    private BaseColor                     myColor     = new BaseColor(226, 226, 226);                                                                           // or red, green, blue, alpha
    private BaseColor                     myColor2    = new BaseColor(232, 232, 232);                                                                           // or red, green, blue, alpha
    private BaseColor                     myColor3    = new BaseColor(255, 255, 255);
    /** La hoja final.*/
    private Document                      document    = null;
    /** El logo de la emprea.*/
    private Image                         img         = null;
    /** Listado de líneas que componen la venta. */
    private List<TVentasFictVista>        lLineas;
    /** Nos indica si en alguna línea hay GGN. */
    private Boolean                       indBio;
    private TProveedores                  empresa;

    interface LineDash {
        public void applyLineDash(PdfContentByte canvas);
    }

    class SolidLine implements LineDash {
        public void applyLineDash(PdfContentByte canvas) {
            canvas.setLineWidth(0.3);
        }
    }

    class SolidLineBlanco implements LineDash {
        public void applyLineDash(PdfContentByte canvas) {
            canvas.setColorStroke(myColor3);
            canvas.setLineWidth(3);
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
    public void createPdf(String numAlbaran, String nombre) throws IOException, DocumentException, GenasoftException {

        // Establecemos los márgenes del documento.
        float left = 20;
        float right = 10;
        float top = 30;
        float bottom = 0;

        indBio = false;
        img = Image.getInstance(pdfTemp + "/logoDocumentos.png");

        document = new Document(PageSize.A4, left, right, top, bottom);

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombre + ".pdf"));

        lLineas = importadorSetup.obtenerTodasLineasVentaFictAlbaran(numAlbaran);

        if (!lLineas.isEmpty()) {

            document.open();

            if (!lLineas.isEmpty()) {

                try {
                    empresa = proveedoresSetup.obtenerProveedorPorId(Integer.valueOf(appIdDir));
                    informacionInicial();

                    PdfPTable table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                    // Insertamos la tabla con la información del consignatario.
                    table.addCell(crearTablaDireccionDescarga());
                    // Insertamos la tabla con la información del consignatario.
                    table.addCell(crearTablaDatosCliente());

                    document.add(table);

                    Paragraph preface3 = new Paragraph();
                    preface3.add(new Paragraph(" ", fLinea2));
                    preface3.add(new Paragraph(" ", fLinea2));
                    document.add(preface3);

                    // La tabla con los datos del pedido.
                    document.add(crearTablaCabecera1(writer));

                    // La tabla con los datos del pedido.
                    document.add(crearTablaCabecera2(writer));

                    if (indBio) {
                        Paragraph preface5 = new Paragraph();
                        preface5.add(
                                     new Paragraph("\n\n *Productos procedentes de agricultura ecologica conforme a la reglamentación CE n  834/2007 sobre producción y etiquetado de los productos ecológicos.\n"
                                             + "Certificado por ES-ECO-01-AN Nº Inscrip. 14085", boldFont7));
                        document.add(preface5);
                    }

                    // La parte final del albarán.                    
                    document.add(pieAlbaran());

                    Paragraph preface4 = new Paragraph();
                    preface4.add(new Paragraph("Revisión estado higiénico del transporte correcto", fLinea2));
                    preface4.add(new Paragraph("No se admiten reclamaciones pasadas 24h de la recepción de la mercancía ", fLinea2));
                    document.add(preface4);

                } catch (GenasoftException e) {
                    document.resetPageCount();
                    PdfContentByte cb1 = writer.getDirectContent();
                    Phrase header31 = new Phrase("Se ha producido el siguiente error: ", error);
                    Phrase header41 = new Phrase(e.getLocalizedMessage(), error);

                    // Albaran nº
                    ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, header31, 300f, 500f, 0);
                    ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, header41, 300f, 485f, 0);

                    document.newPage();
                }
            }

            document.close();

        } else {
            document.open();
            document.resetPageCount();
            PdfContentByte cb1 = writer.getDirectContent();
            Phrase header31 = new Phrase("Se ha producido el siguiente error: ", error);
            Phrase header41 = null;
            if (lLineas.isEmpty()) {
                header41 = new Phrase("No se ha encontrado la venta", error);
            }

            // Albarán nº
            ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, header31, 300f, 500f, 0);
            ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, header41, 300f, 485f, 0);

            document.newPage();

            document.close();
        }

    }

    /**
     * Método que nos crea la tabla con la información del remitente
     * @return La tabla con los datos
     */
    public PdfPTable crearTablaEmpresa() throws IOException, GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;

        try {

            boldFont120.setColor(103, 103, 103);

            table.setWidthPercentage(60);
            cell = new PdfPCell(new Phrase(empresa.getDescripcion(), boldFont120));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(empresa.getDireccion(), fLinea));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(empresa.getDireccion2(), fLinea));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("e-mail: " + empresa.getEmail(), fLinea));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Tfno: " + empresa.getTelf(), fLinea));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Fax: " + empresa.getFax(), fLinea));
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

    public PdfPTable crearTablaVaciaLogo() throws GenasoftException {
        PdfPTable table = new PdfPTable(4);
        PdfPCell cell;

        try {

            boldFont19.setColor(103, 103, 103);

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
            throw new GenasoftException("No se puede generar el albarán, faltan datos:  " + npe.getMessage());
        }
        return table;
    }

    /**
     * Método que nos crea la tabla con la información del consignatario
     * @return La tabla con los datos
     */
    public PdfPTable crearTablaTitulo() throws IOException, GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;

        try {

            boldFont19.setColor(103, 103, 103);

            table.setWidthPercentage(40);

            cell = new PdfPCell(new Phrase("ALBARÁN VENTA", boldFont19));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(lLineas.get(0).getAlbaranFin(), boldFont162));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
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
    private PdfPTable crearTablaDireccionDescarga() throws IOException, GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        LineDash solid = new SolidLine();

        try {

            boldFont19.setColor(103, 103, 103);

            table.setWidthPercentage(40);

            cell = new PdfPCell(new Phrase("          C.I.F.: " + empresa.getCif(), fLinea));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            cell.setPaddingRight(100);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont162));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            cell.setPaddingRight(100);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Dirección de entrega", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setPaddingRight(100);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(lLineas.get(0).getCentroDescargaFin() + "\n\n\n", boldFont162));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setPaddingRight(100);
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
    private PdfPTable crearTablaDatosCliente() throws IOException, GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        LineDash solid = new SolidLine();

        try {

            table.setWidthPercentage(40);
            cell = new PdfPCell(new Phrase("Datos cliente", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(lLineas.get(0).getDireccionClienteFin() + "\n\n\n", boldFont162));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
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
    public PdfPTable crearTablaCabecera1(PdfWriter writer) throws IOException, GenasoftException {
        PdfPTable table;
        PdfPCell cell;
        LineDash solid = new SolidLine();

        table = new PdfPTable(8);

        try {

            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            cell = new PdfPCell(new Phrase("Cod. Cliente", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nº Albarán", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Fecha Salida", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nº Pedido", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setColspan(4);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);

            // Datos
            cell = new PdfPCell(new Phrase(lLineas.get(0).getCodClienteFin(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(lLineas.get(0).getAlbaranFin(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(new SimpleDateFormat("dd/MM/yyyy").format(lLineas.get(0).getFechaSalidaFin()), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(lLineas.get(0).getPedidoFin(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Referencia Cliente: ", boldFont12));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(lLineas.get(0).getReferenciaFin(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setColspan(2);
            cell.setBackgroundColor(myColor2);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setColspan(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);

        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar la factura, faltan datos:  " + npe.getMessage());
        }
        return table;
    }

    /**
     * Método que nos crea la tabla con la información del consignatario
     * @return La tabla con los datos
     * @throws DocumentException 
     */
    public PdfPTable crearTablaCabecera2(PdfWriter writer) throws IOException, GenasoftException, DocumentException {
        PdfPTable table;
        PdfPCell cell;
        LineDash solid = new SolidLine();

        float[] columnWidths = { 3, 1, 2, 2, 3, 2, 2, 2, 2 };
        table = new PdfPTable(columnWidths);

        int contador = 0;
        int totalCajas = 0;
        int totalPales = 0;
        try {

            table.setWidthPercentage(100);
            cell = new PdfPCell(new Phrase("Producto", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Cat.", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Calibre", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Marca", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Tipo caja", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nº cajas", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Unidades", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Tipo palet", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nº palets", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            // DATOS

            String bio = "BIO";
            for (TVentasFictVista linea : lLineas) {
                bio = "";
                // Las cajas del albarán.
                totalCajas = totalCajas + Integer.valueOf(linea.getNumBultosFin());

                // Los palés totales.
                totalPales = totalPales + Integer.valueOf(linea.getnPaleFin());

                if (!indBio) {
                    if (linea.getCalidadVentaFin().equals("BIO")) {
                        indBio = true;
                    }
                }

                if (linea.getCalidadVentaFin().equals("BIO")) {
                    bio = "BIO";
                }

                // PRODUCTO.
                cell = new PdfPCell(new Phrase(linea.getFamiliaFin() + " " + linea.getVariedadFin() + " " + bio, fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                // CAT.
                cell = new PdfPCell(new Phrase(linea.getCategoriaFin(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                // CALIBRE.
                cell = new PdfPCell(new Phrase(linea.getCalibreFin(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                // MARCA.
                cell = new PdfPCell(new Phrase(linea.getMarcaFin(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                // TIPO CAJA.
                cell = new PdfPCell(new Phrase(linea.getUndConsumoFin(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                // Nº CAJAS.
                cell = new PdfPCell(new Phrase(linea.getNumBultosFin(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                // UNIDADES.
                cell = new PdfPCell(new Phrase(linea.getUnidadesFin(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                // TIPO PALET.
                cell = new PdfPCell(new Phrase(linea.getPaleFin(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                // Nº PALETS.
                cell = new PdfPCell(new Phrase(linea.getnPaleFin(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                contador++;
                if (contador == 20) {
                    nuevaPagina(table);

                    informacionInicial();

                    table = new PdfPTable(columnWidths);

                    document.add(crearTablaCabecera1(writer));

                    table.setWidthPercentage(100);
                    cell = new PdfPCell(new Phrase("Producto", cabecera2));
                    cell.setBackgroundColor(myColor);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Cat.", cabecera2));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Calibre", cabecera2));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Marca", cabecera));
                    cell.setBackgroundColor(myColor);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Tipo caja", cabecera));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Nº cajas", cabecera));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Unidades", cabecera));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Tipo palet", cabecera));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Nº palets", cabecera));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    contador = 0;
                }

            }

            if (contador > 19) {
                // Líneas en blanco
                for (int i = contador; i < 20; i++) {
                    // Línea en blanco                    
                    cell = new PdfPCell(new Phrase(" ", cabecera2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            } else {
                // Líneas en blanco
                for (int i = contador; i < 20; i++) {
                    cell = new PdfPCell(new Phrase(" ", cabecera2));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                }
            }

        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }

        document.add(table);

        // La tabla con los totales

        PdfPTable table2 = new PdfPTable(8);
        table2.setWidthPercentage(100);
        cell = new PdfPCell(new Phrase(" ", cabecera2));
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase("Total cajas", cabecera2));
        cell.setBackgroundColor(myColor2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase("Total palets", cabecera2));
        cell.setBackgroundColor(myColor2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        table2.addCell(cell);
        // TOTALES
        cell = new PdfPCell(new Phrase(" ", cabecera2));
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase(" " + totalCajas, cabecera2));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase(" " + totalPales, cabecera2));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
        table2.addCell(cell);

        return table2;
    }

    /**
     * Nos genera la parte final del albarán
     * @return La tabla que corresponde con el final del albarán.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException 
     */
    public PdfPTable pieAlbaran() throws IOException, GenasoftException, DocumentException {

        // Info adicional cabecera.
        Paragraph preface3 = new Paragraph();
        preface3.add(new Paragraph(" ", fLinea));

        document.add(preface3);

        PdfPTable table = new PdfPTable(2);

        LineDash solid = new SolidLine();
        try {

            table.addCell(pieAlbaran1());
            table.addCell(pieAlbaran2());
        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }

        table.setWidthPercentage(100);

        return table;
    }

    /**
     * Nos genera la parte final del albarán
     * @return La tabla que corresponde con el final del albarán.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException 
     */
    public PdfPTable pieAlbaran1() throws IOException, GenasoftException, DocumentException {

        // Info adicional cabecera.
        Paragraph preface3 = new Paragraph();
        preface3.add(new Paragraph(" ", fLinea));

        document.add(preface3);

        PdfPTable table = new PdfPTable(4);
        PdfPCell cell;
        SolidLineBlanco solid = new SolidLineBlanco();
        try {

            cell = new PdfPCell(new Phrase("Transportista: ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(lLineas.get(0).getTransportistaFin().toUpperCase() + "\n", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setColspan(3);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Matrícula: ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Remolque: ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }

        table.setWidthPercentage(100);

        return table;
    }

    /**
     * Nos genera la parte final del albarán
     * @return La tabla que corresponde con el final del albarán.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException 
     */
    public PdfPTable pieAlbaran2() throws IOException, GenasoftException, DocumentException {

        // Info adicional cabecera.
        Paragraph preface3 = new Paragraph();
        preface3.add(new Paragraph(" ", fLinea));

        document.add(preface3);

        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;
        SolidLineBlanco solid = new SolidLineBlanco();
        try {

            cell = new PdfPCell(new Phrase("Pers. contact.: ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Telf.: ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(lLineas.get(0).getTelefonoClienteFin(), cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("e-mail: ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }

        table.setWidthPercentage(100);

        return table;
    }

    private void nuevaPagina(PdfPTable table) throws DocumentException {
        document.add(table);
        document.newPage();
    }

    private void informacionInicial() throws IOException, GenasoftException, DocumentException {
        //img.scaleAbsolute(80f, 80f);

        float[] columnWidths = { 1, 2, 2 };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell cell;
        if (img != null) {
            img.scalePercent(13f);
            cell = new PdfPCell(img);
        } else {
            cell = new PdfPCell(new Phrase(" - ", boldFont12));
        }

        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(10f);
        table.addCell(cell);

        table.addCell(crearTablaEmpresa());
        table.addCell(crearTablaTitulo());

        document.add(table);

    }

    private PdfPCell createCell(String content, int colspan, int rowspan, int border, BaseColor color, Font f) {
        PdfPCell cell = new PdfPCell(new Phrase(content, f));
        cell.setColspan(colspan);
        if (color != null) {
            cell.setBackgroundColor(color);
        }
        cell.setRowspan(rowspan);
        cell.setBorder(PdfPCell.NO_BORDER);
        //cell.setFixedHeight(36f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

}
