/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.exportar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
public class InformeControlPDFOld extends PdfPageEventHelper {
    /** El log de la aplicación*/
    private static final org.slf4j.Logger           log        = org.slf4j.LoggerFactory.getLogger(BalanceMasasPDF.class);
    /** Contendrá el nombre de la aplicación.*/
    @Value("${pdf.temp}")
    private String                                  pdfTemp;
    /** Inyección de Spring para poder acceder a la capa de datos de controles de PT.*/
    @Autowired
    private ControlPtSetup                          controlPTSetup;
    /** El path del código de barras.*/
    private String                                  path1      = null;
    /** Fuente de párrafo. */
    private Font                                    boldFont   = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                                    boldFont15 = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                                    boldFont10 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                                    boldFont7  = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                                    boldFont6  = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                                    boldFont4  = new Font(Font.FontFamily.HELVETICA, 4, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                                    boldFont9  = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                                    boldFont17 = new Font(Font.FontFamily.HELVETICA, 17, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                                    cabecera   = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                                    cabecera2  = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                                    cabecera3  = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                                    cabecera10 = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                                    fLinea     = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                                    fLinea6    = new Font(Font.FontFamily.HELVETICA, new Float(6.5), Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                                    fLinea7    = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                                    fLinea2    = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                                    error      = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL);
    /** Las columnas del documento. */
    public static final Rectangle[]                 COLUMNS    = { new Rectangle(36, 36, 192, 806), new Rectangle(204, 36, 348, 806), new Rectangle(360, 36, 504, 806) };
    /** Color de las celdas sombreadas. */
    private BaseColor                               myColor    = new BaseColor(217, 217, 217);                                                                           // or red, green, blue, alpha
    /** La hoja final.*/
    private Document                                document   = null;
    /** El logo de la emprea.*/
    private Image                                   img        = null;
    /** El control de producto terminado a exportar. */
    private TControlProductoTerminado               controlPt;
    /** Diccionario con los Controles de PT*/
    private Map<Integer, TControlProductoTerminado> mControl;

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

        document = new Document(PageSize.A4.rotate(), left, right, top, bottom);

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombre + ".pdf"));

        path1 = pathLogo;

        if (!lIdsControlPT.isEmpty()) {

            document.open();

            try {

                controlPt = controlPTSetup.obtenerControlPtPorId(3);

                List<TControlProductoTerminado> lControles = controlPTSetup.obtenerControlPtIds(lIdsControlPT);

                List<TControlProductoTerminadoVista> lControlesVista = controlPTSetup.convertirProductoTerminadoVista(lControles);

                // Diccionario con los pedidos que se van a tener en cuenta.
                mControl = lControles.stream().collect(Collectors.toMap(TControlProductoTerminado::getId, Function.identity()));

                controlPt = lControles.get(0);

                Integer cnt = 1;

                Integer total = 0;
                for (TControlProductoTerminadoVista cPt : lControlesVista) {

                    if (total.equals(5) || cnt.equals(1)) {
                        if (total.equals(5)) {
                            nuevaPagina();
                            total = 0;
                        }
                        // Mostramos el logo y el título del documento.
                        informacionInicial();

                        crearLineaEnvasado();

                        crearCabeceraLineaControlPt();

                    }
                    crearLineaControlPt(cPt, cnt);
                    cnt++;
                    total++;
                }

                // Mostramos las imágenes, las imagenes de cada control de PT se mostrará en una página
                for (TControlProductoTerminadoVista cPt : lControlesVista) {
                    nuevaPagina();
                    tituloControlEtiquetado(cPt.getId());
                    incluirImagenesControlPt(Integer.valueOf(cPt.getId()));
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

        } else {
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
        cell = new PdfPCell(new Phrase("Fecha: May. 2019", boldFont10));
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
        cell = new PdfPCell(new Phrase("CONTROL DE ETIQUETADO CONTROL PRODUCTO TEMRINADO " + titulo, boldFont15));
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
    private void crearLineaEnvasado() throws IOException, GenasoftException, DocumentException {

        Integer numCols = 1;

        if (Utils.booleanFromInteger(controlPt.getIndNave1())) {
            numCols++; // PINTAR NAVE 1
            numCols++; // PINTAR LÍNEA 1/"
        } else if (Utils.booleanFromInteger(controlPt.getIndNave2())) {
            numCols++; // PINTAR NAVE 2
            if (Utils.booleanFromInteger(controlPt.getIndMaduracion())) {
                numCols++;
            }
            if (controlPt.getIndRepaso() != null) {
                numCols++;
            }
            if (controlPt.getIndFlowPack() > 0) {
                numCols++;
            }
            if (Utils.booleanFromInteger(controlPt.getIndMango())) {
                numCols++;
            }
            if (Utils.booleanFromInteger(controlPt.getIndMallas())) {
                numCols++;
            }
        } else {
            numCols++; // PINTAR NAVE 3
            if (Utils.booleanFromInteger(controlPt.getIndMallas())) {
                numCols++;
            }
            if (controlPt.getCalibrador() != null) {
                numCols++;
            }
            if (controlPt.getMesaConfeccion() != null) {
                numCols++;
            }
        }

        PdfPCell cell;

        PdfPTable table = new PdfPTable(numCols);
        table.setWidthPercentage(100);

        // TEXTO LÍNEA DE ENVASADO
        cell = new PdfPCell(new Phrase("LÍNEA DE ENVASADO", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // En función de lo que se haya rellenado, añadimos al documento.
        if (Utils.booleanFromInteger(controlPt.getIndNave1())) {
            // MESA 1
            cell = new PdfPCell(new Phrase("NAVE 1", boldFont10));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            // LINEA
            cell = new PdfPCell(new Phrase(Utils.booleanFromInteger(controlPt.getIndMesa1()) ? "Línea 1" : "Línea 2", boldFont10));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        } else if (Utils.booleanFromInteger(controlPt.getIndNave2())) {
            cell = new PdfPCell(new Phrase("NAVE 2", boldFont10));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            if (Utils.booleanFromInteger(controlPt.getIndMaduracion())) {
                cell = new PdfPCell(new Phrase("MADURACIÓN", boldFont10));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            if (controlPt.getIndRepaso() != null) {
                cell = new PdfPCell(new Phrase(controlPt.getIndRepaso().equals(1) ? "REPASO 1" : "REPASO 2", boldFont10));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            if (controlPt.getIndFlowPack() > 0) {
                String flowPack = "FLOWPACK " + controlPt.getIndFlowPack();
                cell = new PdfPCell(new Phrase(flowPack, boldFont10));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            if (Utils.booleanFromInteger(controlPt.getIndMango())) {
                cell = new PdfPCell(new Phrase("MANGO", boldFont10));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            if (Utils.booleanFromInteger(controlPt.getIndMallas())) {
                cell = new PdfPCell(new Phrase("MALLA", boldFont10));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        } else {
            cell = new PdfPCell(new Phrase("NAVE 3", boldFont10));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            if (Utils.booleanFromInteger(controlPt.getIndMallas())) {
                cell = new PdfPCell(new Phrase("MALLAS", boldFont10));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            if (controlPt.getCalibrador() != null) {
                cell = new PdfPCell(new Phrase(controlPt.getCalibrador().trim().toUpperCase(), boldFont10));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
            if (controlPt.getMesaConfeccion() != null) {
                cell = new PdfPCell(new Phrase("MESA CONFECCIÓN", boldFont10));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }

        document.add(table);

        // Info adicional cabecera.
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("  ", boldFont15));
        preface.add(new Paragraph(" ", boldFont15));

        document.add(preface);

    }

    /**
     * Método que nos añade al documento la cabecera de línea de envasado.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException
     */
    private void crearCabeceraLineaControlPt() throws IOException, GenasoftException, DocumentException {

        PdfPCell cell;

        float[] columnWidths = { 3, 2, 2, 2, 2, 2, 1, 1, 1, 3, 3, 2, 2, 4 };

        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);

        // TEXTO IDENTIFICACIÓN
        cell = new PdfPCell(new Phrase("IDENTIFICACIÓN", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(6);
        table.addCell(cell);

        // TEXTO CALIDAD
        cell = new PdfPCell(new Phrase("CALIDAD", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(7);
        table.addCell(cell);

        // TEXTO OBSERVACIONES
        cell = new PdfPCell(new Phrase("OBSERVACIONES", boldFont10));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRowspan(3);
        table.addCell(cell);

        // CLIENTE/FECHA DE ENVASADO
        cell = new PdfPCell(new Phrase("CLIENTE/FECHA DE ENVASADO", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);

        // Nº PEDIDO
        cell = new PdfPCell(new Phrase("Nº PEDIDO", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);

        // Nº CAJAS
        cell = new PdfPCell(new Phrase("Nº CAJAS PEDIDO/REAL", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);

        // PRODUCTO, VARIEDAD Y CALIBRE
        cell = new PdfPCell(new Phrase("PRODUCTO, VARIEDAD Y CALIBRE", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);

        // PESO CAJA TEÓRICO
        cell = new PdfPCell(new Phrase("PESO CAJA TEÓRICO", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);

        // PESO CONFECCIÓN TEÓRICO
        cell = new PdfPCell(new Phrase("PESO CONFECCIÓN TEÓRICO", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);

        // CONTAMINACIÓN
        cell = new PdfPCell(new Phrase("CONTAMINACIÓN", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(3);
        table.addCell(cell);

        // PESADO CAJAS REAL/PESO CONFECCIÓN REAL
        cell = new PdfPCell(new Phrase("PESADO CAJAS REAL / PESO CONFECCIÓN REAL", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);

        // CONTROL CALIBRE
        cell = new PdfPCell(new Phrase("CONTROL CALIBRE", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);

        // PENETROMÍA
        cell = new PdfPCell(new Phrase("PENETROMÍA", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);

        // ºBRIX
        cell = new PdfPCell(new Phrase("ºBRIX", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRowspan(2);
        table.addCell(cell);

        // CONTAMINACIÓN FÍSICA
        cell = new PdfPCell(new Phrase("Fª", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // CONTAMINACIÓN QUÍMICA
        cell = new PdfPCell(new Phrase("Qª", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // CONTAMINACIÓN BIOLÓGICA
        cell = new PdfPCell(new Phrase("Bª", boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        document.add(table);

    }

    /**
     * Método que nos añade al documento la cabecera de línea de envasado.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException
     */
    private void incluirImagenesControlPt(Integer idControlPt) throws IOException, GenasoftException, DocumentException {

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

        List<TControlProductoTerminadoFotos> lFotos = controlPTSetup.obtenerFotosCajas(idControlPt);

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
        List<TControlProductoTerminadoFotos> lFotosPale = controlPTSetup.obtenerFotosPale(idControlPt);

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
        List<TControlProductoTerminadoFotos> lFotosEtInt = controlPTSetup.obtenerFotosEtiquetasInt(idControlPt);

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
        List<TControlProductoTerminadoFotos> lFotosEtExt = controlPTSetup.obtenerFotosEtiquetasExt(idControlPt);

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

    /**
     * Método que nos añade al documento la cabecera de línea de envasado.
     * @throws IOException
     * @throws GenasoftException
     * @throws DocumentException
     */
    private void crearLineaControlPt(TControlProductoTerminadoVista cpt, Integer cnt) throws IOException, GenasoftException, DocumentException {

        PdfPCell cell;
        LineDash solid = new SolidLine();
        float[] columnWidths = { 3, 2, 2, 2, 2, 2, 1, 1, 1, 3, 3, 2, 2, 4 };

        float[] columnWidths2 = { 1, 7 };

        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);

        // CLIENTE / FECHA
        PdfPTable table2 = new PdfPTable(columnWidths2);
        table2.setWidthPercentage(100);
        cell = new PdfPCell(new Phrase("" + cnt, boldFont15));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.addCell(cell);

        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100);
        // FECHA
        cell = new PdfPCell(new Phrase("FECHA: \n" + cpt.getFecha(), boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table1.addCell(cell);

        // CLIENTE
        cell = new PdfPCell(new Phrase("CLIENTE: \n" + cpt.getIdCliente(), boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table1.addCell(cell);

        table2.addCell(table1);
        table.addCell(table2);

        // Nº PEDIDO
        cell = new PdfPCell(new Phrase(cpt.getNumeroPedido(), boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // Nº CAJAS
        cell = new PdfPCell(new Phrase(cpt.getNumCajasPedido() + "/" + cpt.getNumCajasReal(), boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // PRODUCTO, VARIEDAD Y CALIBRE
        cell = new PdfPCell(new Phrase(cpt.getIdProdudcto() + ", " + cpt.getIdVariedad() + ", " + cpt.getIdCalibre(), boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        if (cpt.getIndMallas().equals("Sí") || !cpt.getIndFlowPack().isEmpty()) {
            // PESO CAJA TEÓRICO
            cell = new PdfPCell(new Phrase(" - ", boldFont6));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);

            // PESO CONFECCIÓN TEÓRICO
            cell = new PdfPCell(new Phrase(cpt.getPesoCaja(), boldFont6));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(2);
            table.addCell(cell);
        } else {
            // PESO CONFECCIÓN TEÓRICO
            cell = new PdfPCell(new Phrase(cpt.getPesoCaja(), boldFont6));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" - ", boldFont6));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        // CONTAMINACIÓN FÍSICA
        cell = new PdfPCell(new Phrase(cpt.getIndContaminaFisica(), boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // CONTAMINACIÓN QUÍMICA
        cell = new PdfPCell(new Phrase(cpt.getIndContaminaQuimica(), boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // CONTAMINACIÓN BIOLÓGICA
        cell = new PdfPCell(new Phrase(cpt.getIndContaminaBiologica(), boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // OBTENEMOS LOS PESADOS DE CAJAS
        List<TControlProductoTerminadoPesajesCaja> lPesCaj = controlPTSetup.obtenerPesajesCajaIdControlPt(Integer.valueOf(cpt.getId()));
        List<TControlProductoTerminadoPesajesConfeccion> lPesConf = controlPTSetup.obtenerPesajesConfeccionIdControlPt(Integer.valueOf(cpt.getId()));
        List<TControlProductoTerminadoPesajesCalibre> lPesCal = controlPTSetup.obtenerPesajesCalibreIdControlPt(Integer.valueOf(cpt.getId()));
        List<TControlProductoTerminadoPesajesPenetromia> lPesPen = controlPTSetup.obtenerPesajesPenetromiaIdControlPt(Integer.valueOf(cpt.getId()));
        List<TControlProductoTerminadoPesajesBrix> lPesBrix = controlPTSetup.obtenerPesajesBrixIdControlPt(Integer.valueOf(cpt.getId()));

        // PESAJES CAJAS/CONFECCIÓN
        PdfPTable table3 = new PdfPTable(2);
        if (!lPesCaj.isEmpty()) {
            for (TControlProductoTerminadoPesajesCaja pes : lPesCaj) {
                cell = new PdfPCell(new Phrase("" + pes.getValor(), boldFont6));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cell);
            }
            table.addCell(table3);
        } else {
            for (TControlProductoTerminadoPesajesConfeccion pes : lPesConf) {
                cell = new PdfPCell(new Phrase("" + pes.getValor(), boldFont6));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table3.addCell(cell);
            }
            table.addCell(table3);
        }

        // PESAJES CALIBRE
        PdfPTable table4 = new PdfPTable(2);
        for (TControlProductoTerminadoPesajesCalibre pes : lPesCal) {
            cell = new PdfPCell(new Phrase("" + pes.getValor(), boldFont6));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell);
        }
        table.addCell(table4);

        // PENETROMÍA
        PdfPTable table5 = new PdfPTable(2);
        for (TControlProductoTerminadoPesajesPenetromia pes : lPesPen) {
            cell = new PdfPCell(new Phrase("" + pes.getValor(), boldFont6));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table5.addCell(cell);
        }
        table.addCell(table5);

        // ºBRIX
        PdfPTable table6 = new PdfPTable(2);
        for (TControlProductoTerminadoPesajesBrix pes : lPesBrix) {
            cell = new PdfPCell(new Phrase("" + pes.getValor(), boldFont6));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table6.addCell(cell);
        }
        table.addCell(table6);

        // OBSERVACIONES
        cell = new PdfPCell(new Phrase(cpt.getObservaciones(), boldFont6));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        document.add(table);
    }

    private void nuevaPagina() throws DocumentException {
        document.newPage();
    }

}
