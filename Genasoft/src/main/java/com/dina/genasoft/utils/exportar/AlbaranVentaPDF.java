/**
 * Aplicacion Control transporte Brostel.
 * http://www.brostel.net/
 * Copyright (C) 2018
 */
package com.dina.genasoft.utils.exportar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.common.ClientesSetup;
import com.dina.genasoft.common.ImportadorSetup;
import com.dina.genasoft.common.ProveedoresSetup;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TProveedores;
import com.dina.genasoft.db.entity.TVentasVista;
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
public class AlbaranVentaPDF extends PdfPageEventHelper {
    /** El log de la aplicación*/
    private static final org.slf4j.Logger log        = org.slf4j.LoggerFactory.getLogger(AlbaranVentaPDF.class);
    /** Inyección de Spring para poder acceder a la capa de datos de cajas.*/
    /** Contendrá el nombre de la aplicación.*/
    @Value("${pdf.temp}")
    private String                        pdfTemp;
    /** Contendrá el ID de la dirección de la empresa.*/
    @Value("${app.id.dir}")
    private String                        appIdDir;
    /** El path del código de barras.*/
    private String                        path1      = null;
    /** Fuente de párrafo. */
    private Font                          boldFont   = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont6  = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont15 = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont12 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
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
    private Font                          fLinea21   = new Font(Font.FontFamily.HELVETICA, 4, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          error      = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL);
    /** Las columnas del documento. */
    public static final Rectangle[]       COLUMNS    = { new Rectangle(36, 36, 192, 806), new Rectangle(204, 36, 348, 806), new Rectangle(360, 36, 504, 806) };
    /** Color de las celdas sombreadas. */
    private BaseColor                     myColor    = new BaseColor(220, 220, 220);                                                                           // or red, green, blue, alpha
    /** La hoja final.*/
    private Document                      document   = null;
    /** El logo de la emprea.*/
    private Image                         img        = null;
    /** El logo de la emprea.*/
    private Image                         img3       = null;
    /** El cliente del pedido. */
    private TClientes                     cliente;
    /** El proveedor del pedido. */
    private TProveedores                  proveedor;
    /** Listado de líneas que componen el pedido. */
    private List<TVentasVista>            lLineas;
    /** La empresa que emite los albaranes. */
    private TProveedores                  empresa;
    /** Para acceder a la capa de clientes. */
    @Autowired
    private ClientesSetup                 clientesSetup;
    /** Para acceder a la capa de proveedores. */
    @Autowired
    private ProveedoresSetup              proveedoresSetup;
    /** Para acceder a la capa de importación. */
    @Autowired
    private ImportadorSetup               importadorSetup;
    private Boolean                       indGgn;

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
    public void createPdf(String numAlbaran, String nombre, String pathLogo) throws IOException, DocumentException, GenasoftException {

        // Establecemos los márgenes del documento.
        float left = 20;
        float right = 10;
        float top = 30;
        float bottom = 0;

        document = new Document(PageSize.A4, left, right, top, bottom);

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombre + ".pdf"));

        path1 = pathLogo;

        img = Image.getInstance(path1);

        if (numAlbaran != null) {

            lLineas = importadorSetup.obtenerLineasVentaAlbaran(numAlbaran);

            document.open();

            indGgn = false;

            if (!lLineas.isEmpty()) {

                try {

                    informacionInicial2();

                    cliente = clientesSetup.obtenerClientePorNombre(lLineas.get(0).getClienteFin());

                    // La tabla de la cabecera.
                    document.add(crearTablaCabecera1(writer));
                    // Info adicional cabecera.
                    Paragraph preface = new Paragraph();
                    preface.add(new Paragraph("  ", fLinea));

                    document.add(preface);
                    // La tabla con los datos del pedido.
                    document.add(crearTablaCabecera2(writer));

                    // Pintamos línea divisoria.
                    /** PdfContentByte canvas = writer.getDirectContent();
                    CMYKColor magentaColor = new CMYKColor(1.f, 1.f, 1.f, 1.f);
                    canvas.setColorStroke(magentaColor);
                    canvas.moveTo(10, 170);
                    canvas.lineTo(580, 170);
                    canvas.closePathStroke();
                    */
                    // La parte final del albarán.
                    //pieAlbaran();
                    document.add(crearTablaInformacionFinal1());

                    /**  if (proveedor != null) {
                        document.add(pieAlbaran2(proveedor));
                        // Info adicional cabecera.
                        Paragraph preface2 = new Paragraph();
                        preface2.add(new Paragraph(pdfAlbaran, boldFont8It));
                        document.add(preface2);
                    
                        Paragraph preface3 = new Paragraph();
                        preface3.add(new Paragraph(pdfAlbaran2, cabecera3));
                        document.add(preface3);
                        Paragraph preface4 = new Paragraph();
                        preface4.add(new Paragraph(pdfAlbaran, cabecera3));
                        document.add(preface4);
                    }
                    */

                    if (indGgn) {
                        Paragraph preface5 = new Paragraph();
                        preface5.add(
                                     new Paragraph("\n\n *Productos procedentes de agricultura ecologica conforme a la reglamentación CE nº 834/2007 sobre producción y etiquetado de los productos ecológicos.\r\n"
                                             + "Certificado por ES-ECO-01-AN Nº Inscrip. 14085", boldFont7));
                        document.add(preface5);
                    }

                    Paragraph preface4 = new Paragraph();
                    preface4.add(new Paragraph("\n\nRevisión estado higiénico del transporte correcto \n\n No se admiten reclamaciones pasadas 24h de la recepción de la mercancía                                         ", fLinea2));
                    document.add(preface4);

                } catch (GenasoftException e) {
                    document.resetPageCount();
                    PdfContentByte cb = writer.getDirectContent();
                    Phrase header3 = new Phrase("Se ha producido el siguiente error: ", error);
                    Phrase header4 = new Phrase(e.getLocalizedMessage(), error);

                    // Albaran nº
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header3, 300f, 500f, 0);
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header4, 300f, 485f, 0);

                    document.newPage();
                }
            }

            document.close();

        } else {
            document.open();
            document.resetPageCount();
            PdfContentByte cb = writer.getDirectContent();
            Phrase header3 = new Phrase("Se ha producido el siguiente error: ", error);
            Phrase header4 = null;

            // Albarán nº
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header3, 300f, 500f, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header4, 300f, 485f, 0);

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
            /**
            // Obtenemos la información de la empresa.
            empresa = proveedoresSetup.obtenerProveedorPorId(Integer.valueOf(appIdDir));
            TPais pais = paisesSetup.obtenerPaisPorCodigo(empresa.getPais());
            
            table.setWidthPercentage(60);
            
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell = new PdfPCell(new Phrase(empresa.getContacto(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell = new PdfPCell(new Phrase(empresa.getNombre(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell = new PdfPCell(new Phrase(empresa.getDireccion(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell = new PdfPCell(new Phrase(empresa.getCp() + " " + empresa.getCiudad(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("CIF/NIF: " + empresa.getCif(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell = new PdfPCell(new Phrase(empresa.getContacto(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            */
        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }
        return table;
    }

    /**
     * Método que nos crea la tabla con la información del remitente
     * @return La tabla con los datos
     */
    public PdfPTable crearTablaEmpresa2() throws IOException, GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        LineDash solid = new SolidLine();
        try {

            // Obtenemos la información de la empresa.
            empresa = proveedoresSetup.obtenerProveedorPorId(Integer.valueOf(appIdDir));

            table.setWidthPercentage(60);

            BaseColor color = new BaseColor(154, 158, 154);

            boldFont15.setColor(myColor);

            cell = new PdfPCell(new Phrase(empresa.getDescripcion(), boldFont15));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(empresa.getDireccion(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(empresa.getDireccion2(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("e-mail: " + empresa.getEmail(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Tfno: : " + empresa.getTelf(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Fax: " + empresa.getFax(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("C.I.F.:" + empresa.getCif(), boldFont));
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
    public PdfPTable crearTablaConsignatario() throws IOException, GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        LineDash solid = new SolidLine();

        try {
            /*
            TDireccionCliente dir = clientesSetup.obtenerDireccionClientePorId(pedido.getDireccionDescarga());
            
            if (dir == null) {
                throw new GenasoftException("No se ha encontrado la dirección de entrega del pedido.");
            }
            table.setWidthPercentage(40);
            //cell = new PdfPCell(new Phrase(" ", boldFont17));
            cell = new PdfPCell(new Phrase("ALBARÁN DE VENTA", boldFont17));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, null));
            //cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            //cell = new PdfPCell(new Phrase(cliente.getNombre() != null ? cliente.getNombre() : cliente.getNombre(), boldFont12));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            //cell = new PdfPCell(new Phrase(dir.getDireccion(), cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            String campo = dir.getCp() != null ? dir.getCp() : "";
            if (!campo.isEmpty()) {
                while (campo.length() < 5) {
                    campo = "0" + campo;
                }
            }
            campo = campo + " " + (dir.getCiudad() != null ? dir.getCiudad() : "");
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            //cell = new PdfPCell(new Phrase(campo, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            //cell = new PdfPCell(new Phrase(" ", cabecera3));
            //cell = new PdfPCell(new Phrase(dir.getProvincia() != null ? dir.getProvincia() : "", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            campo = "";
            campo = campo + (cliente.getCif() != null ? cliente.getCif() : "");
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            //cell = new PdfPCell(new Phrase(campo, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            */
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
    public PdfPTable crearTablaConsignatario2() throws IOException, GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        LineDash solid = new SolidLine();
        try {
            /**
            // Obtenemos la información de la empresa.
            empresa = proveedoresSetup.obtenerProveedorPorId(Integer.valueOf(appIdDir));
            
            table.setWidthPercentage(60);
            
            cell = new PdfPCell(new Phrase("Datos cliente", boldFont12));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell = new PdfPCell(new Phrase(empresa.getContacto(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell = new PdfPCell(new Phrase(empresa.getNombre(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell = new PdfPCell(new Phrase(empresa.getDireccion(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell = new PdfPCell(new Phrase(empresa.getCp() + " " + empresa.getPoblacion() + " " + empresa.getCiudad(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("CIF/NIF: " + empresa.getCif(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell = new PdfPCell(new Phrase(empresa.getContacto(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            */
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

        float[] columnWidths = { 2, new Float(1.25), 3, 2, 5, 1 };

        table = new PdfPTable(columnWidths);

        try {

            if (cliente == null) {
                throw new GenasoftException("No se ha encontrado al cliente del pedido.");
            }
            /**
            table.setWidthPercentage(100);
            cell = new PdfPCell(new Phrase("Nº Documento", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Fecha", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nº Cliente", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Transportista", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase("Referencia", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase("Pág.", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            
            // Datos
            cell = new PdfPCell(new Phrase(pedido.getNumAlbaran(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            if (pedido.getFechaExpedicion() != null) {
                cell = new PdfPCell(new Phrase(new SimpleDateFormat("dd/MM/yyyy").format(pedido.getFechaExpedicion()), cabecera));
            } else {
                cell = new PdfPCell(new Phrase("-", cabecera));
            }
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("" + (cliente.getCodExterno() != null && !cliente.getCodExterno().isEmpty() ? cliente.getCodExterno().trim().toUpperCase() : cliente.getId()), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(proveedor != null ? proveedor.getNombre() : "Transporte propio", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(pedido.getReferencia() != null ? pedido.getReferencia() : " - ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("" + writer.getCurrentPageNumber(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            */
        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el albarán, faltan datos:  " + npe.getMessage());
        }
        return table;
    }

    private PdfPTable crearTablaInformacionFinal1() throws GenasoftException {
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell;
        try {

            cell = new PdfPCell(new Phrase(" ", cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("RECIBÍ", cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("N.I.F", cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("MATRÍCULA TRANSPORTISTA", cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera10));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera10));
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
    public PdfPTable crearTablaCabecera2(PdfWriter writer) throws IOException, GenasoftException {
        PdfPTable table = null;
        PdfPCell cell;
        LineDash solid = new SolidLine();
        boolean entra = false;
        float[] columnWidths2 = { new Float(1.5), 10, 1, 1, new Float(0.75), 1 };
        /**
        if (tipoAlbaran == 1) {
            if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                entra = true;
                float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, new Float(1.5), 1, 1, new Float(1.5), 1, new Float(0.75), 1 };
                table = new PdfPTable(columnWidths);
            } else {
                float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, new Float(1.5), 1, 1, 1, 1, new Float(0.75), 1 };
                table = new PdfPTable(columnWidths);
            }
            if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                if (entra) {
                    float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, new Float(0.75), 1 };
                    table = new PdfPTable(columnWidths);
                } else {
                    float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, 1, 1, 1, 1, 1, new Float(0.75), 1 };
                    table = new PdfPTable(columnWidths);
                }
            }
        } else {
            table = new PdfPTable(columnWidths2);
        }
        
        int max = 0;
        if (tipoAlbaran == 0) {
            max = 28;
        } else {
            max = 27;
        }
        int contador = 0;
        mCajasAlbaran = new HashMap<String, Integer>();
        try {
        
            table.setWidthPercentage(100);
            cell = new PdfPCell(new Phrase("Ref.Client", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Descripción", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Org", cabecera2));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Cajas", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            // Línea en blanco
            cell = new PdfPCell(new Phrase("Unids", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            table.addCell(cell);
            if (tipoAlbaran == 1) {
                cell = new PdfPCell(new Phrase("Bruto", cabecera));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("TARA", cabecera));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
            }
            cell = new PdfPCell(new Phrase("Neto", cabecera));
            cell.setBackgroundColor(myColor);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            if (tipoAlbaran == 1) {
                cell = new PdfPCell(new Phrase("Precio UD/kg", cabecera));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                    cell = new PdfPCell(new Phrase("Precio Kg", cabecera));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                }
                //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                cell = new PdfPCell(new Phrase("U.M", cabecera));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                //}
                cell = new PdfPCell(new Phrase("R", cabecera));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Importe", cabecera));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                table.addCell(cell);
            }
            //            
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            if (tipoAlbaran == 1) {
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
            }
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            if (tipoAlbaran == 1) {
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                }
                //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                // }
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
            }
        
            // Datos
            Integer unidadesCaja = 0;
            Double kgsBrutos = Double.valueOf("0");
            Double tara = Double.valueOf(0);
            Double kgs = Double.valueOf("0");
            DecimalFormat df = new DecimalFormat("#,##0.00");
            DecimalFormat df2 = new DecimalFormat("#,##0.000");
            Double importeLinea = Double.valueOf("0");
            Double aux = Double.valueOf(0);
            TLineasPedido l = null;
            Double kilosTramitados = Double.valueOf(0);
            TLineasPedidoPtTramitado lTram = null;
            List<TLineasPedidoPtTramitado> lLTram = null;
            TProveedores prv = null;
            TPais p = null;
            int cajasFinales = 0;
            int unidadesFinales = 0;
            Double kgsTotales = Double.valueOf(0);
            TIva iva = null;
            String descCaja = null;
            Boolean entra2 = false;
            for (TLineasPedidoVista linea : lLineas) {
                p = null;
                l = pedidosSetup.obtenerLineaPedidoPorId(Integer.valueOf(linea.getId()));
                kilosTramitados = pedidosSetup.obtenerKgsLineasTramitadas(l.getId());
                if (l.getEstado().equals(LineasPedidoEnum.ANULADO_CLIENTE.getValue()) || l.getEstado().equals(LineasPedidoEnum.ANULADO_PRODUCCION.getValue())) {
                    continue;
                }
        
                if (l.getUnidadesFinales().equals(0) && l.getNumCajasFinales().equals(0) && l.getEstado().equals(LineasPedidoEnum.FINALIZADA.getValue())) {
                    continue;
                }
        
                // Comprobamos si la caja se contabiliza
                if (mCajas.get(l.getIdCaja()) != null && mCajas.get(l.getIdCaja()).equals(1)) {
                    if (mCajasAlbaran.get((linea.getIdCaja())) == null) {
                        mCajasAlbaran.put((linea.getIdCaja()), Integer.valueOf(linea.getUnidades()));
                    } else {
                        mCajasAlbaran.put((linea.getIdCaja()), mCajasAlbaran.get((linea.getIdCaja())) + Integer.valueOf(linea.getUnidades()));
                    }
                }
        
                if (pedido.getEstado().equals(PedidosEnum.FINALIZADO.getValue())) {
                    unidadesCaja = Integer.valueOf(linea.getUnidadesFinales()) / Integer.valueOf(linea.getNumCajasFinales());
                } else {
                    unidadesCaja = Integer.valueOf(linea.getUnidades()) / Integer.valueOf(linea.getNumCajas());
                }
        
                if (pedido.getEstado().equals(PedidosEnum.FINALIZADO.getValue())) {
                    // Cogemos los kilos que nos han indicado en el pedido.
                    String kilos = linea.getLote();
                    kilos = kilos.replaceAll("\\.", ",");
                    kgs = l.getKgsFinales();
        
                    // kgs = mArticulosKgs.get(Integer.valueOf(l.getIdArticulo())) * Integer.valueOf(linea.getUnidadesFinales());
                } else {
                    //kgs = mArticulosKgs.get(Integer.valueOf(l.getIdArticulo())) * Integer.valueOf(linea.getUnidades());
                    String kilos = linea.getLote();
                    kilos = kilos.replaceAll("\\.", ",");
                    kgs = Utils.formatearValorDouble(kilos);
                    kgs = l.getKgs();
                }
        
                kgsTotales = kgsTotales + kgs;
        
                descCaja = mCajas2.get(l.getIdCaja()) != null ? mCajas2.get(l.getIdCaja()).getReferencia() : "N/D";
        
                if (tipoAlbaran == 1 && cliente.getModeloAlbaran().equals(ModelosAlbaranEnum.GENERICO.getValue())) {
                    // El precio por Kg.
                    if (pedido.getEstado().equals(PedidosEnum.FINALIZADO.getValue())) {
        
                        importeLinea = l.getImporteLinea();
                        //if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb()) && cliente.getImporteDescuento() != null && !cliente.getImporteDescuento().equals(Double.valueOf(0))) {
                        aux = (cliente.getImporteDescuento() * importeLinea) / 100;
                        totalDescuentos = totalDescuentos + aux;
                        //importeLinea = importeLinea - aux;
        
                    } else {
        
                        importeLinea = l.getImporteLinea();
                    }
                    totalAlbaran = totalAlbaran + importeLinea;
        
                    iva = mIvas.get(l.getTipoIva());
                    // Calculamos el importe del iva
                    if (iva != null) {
                        if (cliente.getTipoOperacion().equals(28)) {
                            if (iva.getId().equals(12)) {
                                aux = Utils.redondeoDecimales(2, (iva.getImporte() * (importeLinea)) / 100);
                                if (mIvasBaseAlbaran.get(l.getTipoIva()) == null) {
                                    mIvasBaseAlbaran.put(l.getTipoIva(), importeLinea);
                                } else {
                                    mIvasBaseAlbaran.put(l.getTipoIva(), mIvasBaseAlbaran.get(l.getTipoIva()) + importeLinea);
                                }
                                if (mIvasImporteAlbaran.get(l.getTipoIva()) == null) {
                                    mIvasImporteAlbaran.put(l.getTipoIva(), aux);
                                } else {
                                    mIvasImporteAlbaran.put(l.getTipoIva(), mIvasImporteAlbaran.get(l.getTipoIva()) + aux);
                                }
        
                                aux = (TIPO_IVA_12 * (importeLinea)) / 100;
                                if (mIvasBaseAlbaran.get(912) == null) {
                                    mIvasBaseAlbaran.put(912, importeLinea);
                                } else {
                                    mIvasBaseAlbaran.put(912, mIvasBaseAlbaran.get(912) + importeLinea);
                                }
                                if (mIvasImporteAlbaran.get(912) == null) {
                                    mIvasImporteAlbaran.put(912, aux);
                                } else {
                                    mIvasImporteAlbaran.put(912, mIvasImporteAlbaran.get(912) + aux);
                                }
                            } else if (iva.getId().equals(23)) {
                                aux = Utils.redondeoDecimales(2, (iva.getImporte() * (importeLinea)) / 100);
                                if (mIvasBaseAlbaran.get(l.getTipoIva()) == null) {
                                    mIvasBaseAlbaran.put(l.getTipoIva(), importeLinea);
                                } else {
                                    mIvasBaseAlbaran.put(l.getTipoIva(), mIvasBaseAlbaran.get(l.getTipoIva()) + importeLinea);
                                }
                                if (mIvasImporteAlbaran.get(l.getTipoIva()) == null) {
                                    mIvasImporteAlbaran.put(l.getTipoIva(), aux);
                                } else {
                                    mIvasImporteAlbaran.put(l.getTipoIva(), mIvasImporteAlbaran.get(l.getTipoIva()) + aux);
                                }
        
                                aux = (TIPO_IVA_23 * (importeLinea)) / 100;
                                if (mIvasBaseAlbaran.get(923) == null) {
                                    mIvasBaseAlbaran.put(923, importeLinea);
                                } else {
                                    mIvasBaseAlbaran.put(923, mIvasBaseAlbaran.get(923) + importeLinea);
                                }
                                if (mIvasImporteAlbaran.get(923) == null) {
                                    mIvasImporteAlbaran.put(923, aux);
                                } else {
                                    mIvasImporteAlbaran.put(923, mIvasImporteAlbaran.get(923) + aux);
                                }
                            } else if (iva.getId().equals(21)) {
                                aux = (iva.getImporte() * (importeLinea)) / 100;
                                if (mIvasBaseAlbaran.get(l.getTipoIva()) == null) {
                                    mIvasBaseAlbaran.put(l.getTipoIva(), importeLinea);
                                } else {
                                    mIvasBaseAlbaran.put(l.getTipoIva(), mIvasBaseAlbaran.get(l.getTipoIva()) + importeLinea);
                                }
                                if (mIvasImporteAlbaran.get(l.getTipoIva()) == null) {
                                    mIvasImporteAlbaran.put(l.getTipoIva(), aux);
                                } else {
                                    mIvasImporteAlbaran.put(l.getTipoIva(), mIvasImporteAlbaran.get(l.getTipoIva()) + aux);
                                }
        
                                aux = (TIPO_IVA_21 * (importeLinea)) / 100;
                                if (mIvasBaseAlbaran.get(921) == null) {
                                    mIvasBaseAlbaran.put(921, importeLinea);
                                } else {
                                    mIvasBaseAlbaran.put(921, mIvasBaseAlbaran.get(921) + importeLinea);
                                }
                                if (mIvasImporteAlbaran.get(921) == null) {
                                    mIvasImporteAlbaran.put(921, aux);
                                } else {
                                    mIvasImporteAlbaran.put(921, mIvasImporteAlbaran.get(921) + aux);
                                }
        
                            } else {
                                aux = (iva.getImporte() * importeLinea) / 100;
                            }
                        } else {
                            aux = (iva.getImporte() * importeLinea) / 100;
        
                            if (l != null) {
                                if (mIvasBaseAlbaran.get(l.getTipoIva()) == null) {
                                    mIvasBaseAlbaran.put(l.getTipoIva(), importeLinea);
                                } else {
                                    mIvasBaseAlbaran.put(l.getTipoIva(), mIvasBaseAlbaran.get(l.getTipoIva()) + importeLinea);
                                }
                                if (mIvasImporteAlbaran.get(l.getTipoIva()) == null) {
                                    mIvasImporteAlbaran.put(l.getTipoIva(), aux);
                                } else {
                                    mIvasImporteAlbaran.put(l.getTipoIva(), mIvasImporteAlbaran.get(l.getTipoIva()) + aux);
                                }
                            }
                        }
        
                    }
                } else if (tipoAlbaran == 1 && cliente.getModeloAlbaran().equals(ModelosAlbaranEnum.DESGLOSE_GGN.getValue())) {
                    // Calculamos el conversor a partir de los kilos introducidos en el pedido.                    
                    String kilosLineaPedido = linea.getLote();
                    kilosLineaPedido = kilosLineaPedido.replaceAll("\\.", ",");
        
                    // Mostramos línea por cada lote registrado en producción.
                    lLTram = pedidosSetup.obtenerLineasPedidoPtLineaTramitado(l.getId());
                    String texto = "";
                    for (TLineasPedidoPtTramitado tram : lLTram) {
                        if (mFamilias.get(mArticulos.get(tram.getIdArticulo()).getIdFamilia()).getMix() != null) {
                            entra2 = true;
                        }
        
                        prv = mProveedores.get(tram.getIdProveedor());
                        // Cogemos el país del lote, sino lo encontramos, lo cogemos del proveedor.
                        if (tram.getLoteEntrada().contains("GGN")) {
                            texto = tram.getLoteEntrada().split("GGN")[0];
                            p = paisesSetup.obtenerPaisPorCodigo(texto.substring(texto.length() - 2, texto.length()));
                        } else {
                            p = paisesSetup.obtenerPaisPorCodigo(tram.getLoteEntrada().substring(tram.getLoteEntrada().length() - 2, tram.getLoteEntrada().length()));
                        }
        
                        if (p == null) {
                            if (prv != null) {
                                p = paisesSetup.obtenerPaisPorCodigo(prv.getPais());
                            } else {
                                p = null;
                            }
                        }
                        String campo = l.getRefArticulo();
                        if (campo == null) {
                            campo = "";
                        }
                        cell = new PdfPCell(new Phrase(campo, fLinea));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        String lote = ""; //mTramitadosLotes.get(Integer.valueOf(linea.getId()));
                        //if (lote == null) {
                        //    lote = "";
                        // }
        
                        //lote = (mArticulosCodBarras.get(Integer.valueOf(linea.getId())) != null ? "- EAN: " + (mArticulosCodBarras.get(Integer.valueOf(linea.getId()))) : "");
        
                        if (mFamilias.get(mArticulos.get(tram.getIdArticulo()).getIdFamilia()).getMix() != null) {
                            lote = lote + " Lote: ";
                            for (TLineasPedidoPtTramitado tram2 : lLTram) {
                                lote = lote + " " + tram2.getLote() + ", ";
                            }
                            lote = lote.substring(0, lote.length() - 2);
                        } else {
                            lote = lote + " Lote: " + tram.getLote();
                        }
        
                        String camp = l.getDescArticulo();
                        if (tipoAlbaran == 1) {
                            if (camp.length() > 29) {
                                camp = camp.substring(0, 29) + ".";
                            }
                        }
                        if (tram.getGgn() != null) {
                            camp = camp + "*";
                        }
                        cell = new PdfPCell(new Phrase(camp + "\n" + lote, fLinea6));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(p != null ? p.getCodigo() : " ", fLinea));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        if (tram.getIdMateriaPrima() == null) {
                            cell = new PdfPCell(new Phrase("" + tram.getNumCajas().intValue(), fLinea));
                        } else {
                            int numCajas = l.getUnidadesFinales() / l.getNumCajasFinales();
                            cell = new PdfPCell(new Phrase("" + tram.getUnidadesPt() / numCajas, fLinea));
                        }
        
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        if (tram.getIdMateriaPrima() == null) {
                            cell = new PdfPCell(new Phrase("" + tram.getUnidades(), fLinea));
                        } else {
                            cell = new PdfPCell(new Phrase("" + tram.getUnidadesPt(), fLinea));
                        }
        
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        if (mFamilias.get(mArticulos.get(tram.getIdArticulo()).getIdFamilia()).getMix() != null) {
                            cell = new PdfPCell(new Phrase(df.format(l.getKgsFinales()), fLinea));
                        } else {
                            // Los Kilos.
                            if (l.getKgsFinales().equals(kilosTramitados)) {
                                // Cogemos los kilos del pedido.
                                Double kilosReales = l.getKgsFinales();
                                // Cogemos los kilos tramitados
                                Double kgsTram = kilosTramitados;
                                Double aux3 = Double.valueOf(0);
                                // Hacemos la ponderación.
                                if (tram.getMateriaPrima() == null) {
                                    // Los Kilos.
                                    if (l.getConversor().equals(Double.valueOf(0))) {
                                        if (!l.getConversor2().equals(Double.valueOf(0))) {
                                            aux3 = kilosReales * (l.getConversor2() * tram.getUnidades());
                                        } else {
                                            cell = new PdfPCell(new Phrase(df.format(tram.getKgs()), fLinea));
                                        }
                                    } else {
                                        if (tram.getKgs().equals(Double.valueOf(0))) {
                                            //txtKgs.setValue(df.format(tram.getPesoUnitario() * tram.getUnidades()));
                                            aux3 = kilosReales * (tram.getPesoUnitario() * tram.getUnidades());
                                            aux3 = aux3 / kgsTram;
                                            cell = new PdfPCell(new Phrase(df.format(aux3), fLinea));
                                        } else {
                                            aux3 = (kilosReales * tram.getKgs()) / kgsTram;
                                            cell = new PdfPCell(new Phrase(df.format(aux3), fLinea));
                                        }
                                    }
                                } else {
                                    aux3 = (kilosReales * (tram.getPesoPt() * tram.getUnidadesPt()));
                                    aux3 = aux3 / kgsTram;
                                    cell = new PdfPCell(new Phrase(df.format(aux3), fLinea));
                                }
        
                            } else {
                                if (l.getConversor().equals(Double.valueOf(0))) {
                                    if (!l.getConversor2().equals(Double.valueOf(0))) {
                                        cell = new PdfPCell(new Phrase(df.format(tram.getUnidades() * l.getConversor2()), fLinea));
                                    } else {
                                        cell = new PdfPCell(new Phrase(df.format(tram.getKgs()), fLinea));
                                    }
                                } else {
                                    // Cogemos los kilos del pedido.
                                    Double kilosReales = l.getKgsFinales();
                                    // Cogemos los kilos tramitados
                                    Double kgsTram = kilosTramitados;
                                    Double aux3 = Double.valueOf(0);
                                    // Hacemos la ponderación.
                                    if (tram.getMateriaPrima() == null) {
                                        // Los Kilos.
                                        if (tram.getKgs().equals(Double.valueOf(0))) {
                                            //txtKgs.setValue(df.format(tram.getPesoUnitario() * tram.getUnidades()));
                                            aux3 = kilosReales * (tram.getPesoUnitario() * tram.getUnidades());
                                            aux3 = aux3 / kgsTram;
                                            cell = new PdfPCell(new Phrase(df.format(aux3), fLinea));
                                        } else {
                                            //aux3 = (kilosReales * tram.getKgs()) / kgsTram;
                                            aux3 = tram.getKgs();
                                            cell = new PdfPCell(new Phrase(df.format(aux3), fLinea));
                                        }
        
                                    } else {
                                        aux3 = (kilosReales * (tram.getPesoPt() * tram.getUnidadesPt()));
                                        aux3 = aux3 / kgsTram;
                                        cell = new PdfPCell(new Phrase(df.format(aux3), fLinea));
                                    }
                                }
                            }
                        }
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        table.addCell(cell);
                        if (tipoAlbaran == 1) {
                            if (!tram.getIdCaja().equals(-1)) {
                                descCaja = mCajas2.get(tram.getIdCaja()) != null ? mCajas2.get(tram.getIdCaja()).getReferencia() : "N/D";
                            } else {
                                descCaja = mCajas2.get(tram.getIdCaja()) != null ? mCajas2.get(tram.getIdCaja()).getReferencia() : "N/D";
                            }
                            if (descCaja.length() > 12) {
                                descCaja = descCaja.substring(0, 11).concat(".");
                            }
                            cell = new PdfPCell(new Phrase(df.format(l.getPrecio()), fLinea));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
        
                            //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                            cell = new PdfPCell(new Phrase(l.getUnidadMedida(), fLinea2));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                            //}
                            Double descuento = (cliente.getImporteDescuento() / 100);
                            //  if (!Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                            //      descuento = Double.valueOf(0);
                            //  }
                            Double impLinea = Double.valueOf(0);
        
                            descuento = cliente.getImporteDescuento();
                            impLinea = l.getImporteLinea();
                            descuento = (l.getPrecio() * descuento) / 100;
                            totalDescuentos = totalDescuentos + descuento;
        
                            cell = new PdfPCell(new Phrase(df.format(pedido.getDescuentoCliente()), fLinea));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
        
                            cell = new PdfPCell(new Phrase(df.format(impLinea), fLinea7));
                            /**if (mFamilias.get(mArticulos.get(tram.getIdArticulo()).getIdFamilia()).getMix() != null) {
                                descuento = l.getPrecio() * l.getUnidadesFinales() * descuento;
                                totalDescuentos = totalDescuentos + descuento;
                                if (art2.getConversor().equals(Double.valueOf(0)) ) {
                                    if (!art2.getConversor2().equals(Double.valueOf(0))) {
                                        impLinea = (l.getPrecio() * (l.getUnidadesFinales() * art2.getConversor2())) - descuento;
                                        cell = new PdfPCell(new Phrase(df.format(l.getPrecio() * tram.getKgs()), fLinea));
                                    } else {
                                        impLinea = l.getPrecio() * l.getKgsFinales() - descuento;
                                        cell = new PdfPCell(new Phrase(df.format(l.getPrecio() * tram.getKgs()), fLinea));
                                    }
                                } else {
                                    impLinea = l.getPrecio() * l.getUnidadesFinales() - descuento;
                                    cell = new PdfPCell(new Phrase(df.format(l.getPrecio() * l.getUnidadesFinales()), fLinea));
                                }
                            } else {
                                if (tram.getMateriaPrima() == null) {
                                    // if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                                    descuento = l.getPrecio() * tram.getUnidades() * descuento;
                                    totalDescuentos = totalDescuentos + descuento;
                                    if (art2.getConversor().equals(Double.valueOf(0))) {
                                        if (art2.getConversor2().equals(Double.valueOf(0))) {
                                            impLinea = l.getPrecio() * tram.getKgs() - descuento;
                                        } else {
                                            if (Utils.booleanFromInteger(art.getIndPrecioUnif())) {
                                                impLinea = l.getPrecio() * (tram.getUnidades() * art2.getConversor2()) - descuento;
                                            } else {
                                                impLinea = l.getPrecio() * (tram.getUnidades()) - descuento;
                                            }
                                        }
                                    } else {
                                       impLinea = l.getImporteLinea();
                                    }
                                    cell = new PdfPCell(new Phrase(df.format(impLinea), fLinea));
                                    // }
                                } else {
                                    //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                                    descuento = l.getPrecio() * tram.getUnidadesPt() * descuento;
                                    totalDescuentos = totalDescuentos + descuento;
                                    impLinea = l.getPrecio() * tram.getUnidadesPt() - descuento;
                                    cell = new PdfPCell(new Phrase(df.format(l.getPrecio() * tram.getUnidadesPt()), fLinea));
                                    //} else {
                                    //    impLinea = l.getPrecio() * tram.getUnidadesPt() - descuento;
                                    //    cell = new PdfPCell(new Phrase(df.format(l.getPrecio() * tram.getUnidadesPt()), fLinea));
                                    // }
                                }
                            }
                            */
        /**
                            impLinea = impLinea + l.getImporteTasa();
                            iva = mIvas.get(l.getTipoIva());
                            // Calculamos el importe del iva
                            if (iva != null) {
                                if (cliente.getTipoOperacion().equals(28)) {
                                    if (iva.getId().equals(12)) {
                                        aux = (iva.getImporte() * (impLinea + TIPO_IVA_12)) / 100;
                                    } else if (iva.getId().equals(23)) {
                                        aux = (iva.getImporte() * (impLinea + TIPO_IVA_23)) / 100;
                                    } else if (iva.getId().equals(21)) {
                                        aux = (iva.getImporte() * (impLinea + TIPO_IVA_21)) / 100;
                                    } else {
                                        aux = (iva.getImporte() * impLinea) / 100;
                                    }
                                } else {
                                    aux = (iva.getImporte() * impLinea) / 100;
                                }
                                if (l != null) {
                                    if (mIvasBaseAlbaran.get(l.getTipoIva()) == null) {
                                        mIvasBaseAlbaran.put(l.getTipoIva(), impLinea);
                                    } else {
                                        mIvasBaseAlbaran.put(l.getTipoIva(), mIvasBaseAlbaran.get(l.getTipoIva()) + impLinea);
                                    }
                                    if (mIvasImporteAlbaran.get(l.getTipoIva()) == null) {
                                        mIvasImporteAlbaran.put(l.getTipoIva(), aux);
                                    } else {
                                        mIvasImporteAlbaran.put(l.getTipoIva(), mIvasImporteAlbaran.get(l.getTipoIva()) + aux);
                                    }
                                }
                            }
        
                            totalAlbaran = totalAlbaran + impLinea;// - descuento;
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            table.addCell(cell);
                        }
        
                        contador++;
        
                        if (contador == max) {
                            nuevaPagina(table);
        
                            if (tipoAlbaran == 1) {
                                if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                                    entra = true;
                                    float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, new Float(1.5), 1, 1, new Float(1.5), 1, 1, new Float(0.75), 1 };
                                    table = new PdfPTable(columnWidths);
                                } else {
                                    float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, new Float(1.5), 1, 1, 1, 1, new Float(0.75), 1 };
                                    table = new PdfPTable(columnWidths);
                                }
                                if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                                    if (entra) {
                                        float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, new Float(0.75), 1 };
                                        table = new PdfPTable(columnWidths);
                                    } else {
                                        float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, 1, 1, 1, 1, 1, new Float(0.75), 1 };
                                        table = new PdfPTable(columnWidths);
                                    }
                                }
                            } else {
                                table = new PdfPTable(columnWidths2);
                            }
                            if (!cab) {
                                informacionInicial();
                            } else {
                                informacionInicial2();
                            }
        
                            // La tabla de la cabecera.
                            document.add(crearTablaCabecera1(writer));
                            table.setWidthPercentage(100);
                            cell = new PdfPCell(new Phrase("Ref.Client", cabecera2));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            cell.setBackgroundColor(myColor);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("Descripción", cabecera2));
                            cell.setBackgroundColor(myColor);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("Org", cabecera2));
                            cell.setBackgroundColor(myColor);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("Cajas", cabecera2));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setBackgroundColor(myColor);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            table.addCell(cell);
                            // Línea en blanco
                            cell = new PdfPCell(new Phrase("Unids", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            cell.setBackgroundColor(myColor);
                            table.addCell(cell);
                            if (tipoAlbaran == 1) {
                                cell = new PdfPCell(new Phrase("Bruto", cabecera));
                                cell.setBackgroundColor(myColor);
                                cell.setBorder(PdfPCell.NO_BORDER);
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase("TARA", cabecera));
                                cell.setBackgroundColor(myColor);
                                cell.setBorder(PdfPCell.NO_BORDER);
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                                table.addCell(cell);
                            }
                            cell = new PdfPCell(new Phrase("Neto", cabecera));
                            cell.setBackgroundColor(myColor);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            table.addCell(cell);
                            if (tipoAlbaran == 1) {
                                cell = new PdfPCell(new Phrase("Precio UD/kg", cabecera));
                                cell.setBackgroundColor(myColor);
                                cell.setBorder(PdfPCell.NO_BORDER);
                                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                                table.addCell(cell);
                                if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                                    cell = new PdfPCell(new Phrase("Precio Kg", cabecera));
                                    cell.setBackgroundColor(myColor);
                                    cell.setBorder(PdfPCell.NO_BORDER);
                                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                                    table.addCell(cell);
                                }
                                //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                                cell = new PdfPCell(new Phrase("U.M", cabecera));
                                cell.setBackgroundColor(myColor);
                                cell.setBorder(PdfPCell.NO_BORDER);
                                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                                table.addCell(cell);
                                //}
                                cell = new PdfPCell(new Phrase("R", cabecera));
                                cell.setBackgroundColor(myColor);
                                cell.setBorder(PdfPCell.NO_BORDER);
                                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase("Importe", cabecera));
                                cell.setBackgroundColor(myColor);
                                cell.setBorder(PdfPCell.NO_BORDER);
                                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                                table.addCell(cell);
                            }
                            contador = 0;
                        }
        
                        if (mFamilias.get(mArticulos.get(tram.getIdArticulo()).getIdFamilia()).getMix() != null) {
                            break;
                        }
                    }
        
                    continue;
                }
        
                lLTram = pedidosSetup.obtenerLineasPedidoPtLineaTramitado(l.getId());
                lTram = null;
                if (!lLTram.isEmpty()) {
                    lTram = lLTram.get(0);
                }
                String texto = "";
                if (lTram != null) {
                    if (lTram.getLoteEntrada().contains("GGN")) {
                        texto = lTram.getLoteEntrada().split("GGN")[0];
                        try {
                            p = paisesSetup.obtenerPaisPorIntrastat(Integer.valueOf(texto.substring(texto.length() - 2, texto.length())));
                        } catch (NumberFormatException e) {
                            p = paisesSetup.obtenerPaisPorCodigo(texto.substring(texto.length() - 2, texto.length()));
                        }
                    } else {
                        try {
                            p = paisesSetup.obtenerPaisPorIntrastat(Integer.valueOf(lTram.getLoteEntrada().substring(lTram.getLoteEntrada().length() - 2, lTram.getLoteEntrada().length())));
                        } catch (NumberFormatException e) {
                            p = paisesSetup.obtenerPaisPorCodigo(lTram.getLoteEntrada().substring(lTram.getLoteEntrada().length() - 2, lTram.getLoteEntrada().length()));
                        }
                    }
        
                    if (p == null) {
                        prv = mProveedores.get(lTram.getIdProveedor());
                        if (prv != null) {
                            p = paisesSetup.obtenerPaisPorCodigo(prv.getPais());
                        }
                    }
        
                }
        
                if (l.getPais() != null) {
                    p = paisesSetup.obtenerPaisPorCodigo(l.getPais());
                }
        
                // Las cajas del albarán.
                cajasFinales = cajasFinales + (pedido.getEstado().equals(PedidosEnum.FINALIZADO.getValue()) ? Integer.valueOf(linea.getNumCajasFinales()) : Integer.valueOf(linea.getNumCajas()));
                // Las unidades del albarán.
                unidadesFinales = unidadesFinales + (pedido.getEstado().equals(PedidosEnum.FINALIZADO.getValue()) ? Integer.valueOf(linea.getUnidadesFinales()) : Integer.valueOf(linea.getUnidades()));
        
                String campo = l.getRefArticulo();
                if (campo == null) {
                    campo = "";
                }
                cell = new PdfPCell(new Phrase(campo, fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                String lote = mTramitadosLotes.get(Integer.valueOf(linea.getId()));
                if (lote == null) {
                    lote = "";
                }
        
                // lote = (mArticulosCodBarras.get(Integer.valueOf(linea.getId())) != null ? "- EAN: " + (mArticulosCodBarras.get(Integer.valueOf(linea.getId()))) : "");
        
                // lote = lote + " Lote: " + linea.getLote();
        
                String camp = l.getDescArticulo();
        
                if (tipoAlbaran == 1) {
                    if (camp.length() > 30) {
                        camp = camp.substring(0, 28) + ".";
                    }
                }
                if (mGgnLineas.get(Integer.valueOf(linea.getId())) != null) {
                    camp = camp + "*";
                }
                cell = new PdfPCell(new Phrase(camp + "\n" + lote, fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                String pais = (p != null ? p.getCodigo() : "-");
                cell = new PdfPCell(new Phrase(pais, fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(pedido.getEstado().equals(PedidosEnum.FINALIZADO.getValue()) ? linea.getNumCajasFinales() : linea.getNumCajas(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                Integer unidades = Integer.valueOf(pedido.getEstado().equals(PedidosEnum.FINALIZADO.getValue()) ? linea.getUnidadesFinales() : linea.getUnidades());
                cell = new PdfPCell(new Phrase(pedido.getEstado().equals(PedidosEnum.FINALIZADO.getValue()) ? linea.getUnidadesFinales() : linea.getUnidades(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                if (tipoAlbaran == 1) {
                    // KGS Brutos
                    if (l.getConversor().equals(Double.valueOf(0))) {
                        if (!l.getConversor2().equals(Double.valueOf(0))) {
                            kgsBrutos = l.getConversor2() * unidades;
                        } else {
                            kgsBrutos = pedido.getEstado().equals(PedidosEnum.FINALIZADO.getValue()) ? l.getKgsBrutos() : l.getKgsBrutos();
                        }
                        cell = new PdfPCell(new Phrase(df.format(kgsBrutos), fLinea));
                    } else {
                        //kgsBrutos = art2.getConversor() * unidades;
                        cell = new PdfPCell(new Phrase("-", fLinea));
                    }
        
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    // TARA
                    if (l.getConversor().equals(Double.valueOf(0))) {
                        if (!l.getConversor2().equals(Double.valueOf(0))) {
                            tara = Double.valueOf(0);
                        } else {
                            tara = kgsBrutos - (pedido.getEstado().equals(PedidosEnum.FINALIZADO.getValue()) ? l.getKgsFinales() : l.getKgs());
                        }
                        cell = new PdfPCell(new Phrase(df.format(tara), fLinea));
                    } else {
                        cell = new PdfPCell(new Phrase("-", fLinea));
                    }
                    /**  if (art2.getConversor2().equals(Double.valueOf(0))) {
                        cell = new PdfPCell(new Phrase(df.format(tara), fLinea));
                    } else {
                        cell = new PdfPCell(new Phrase("-", fLinea));
                    }
                    */

        /**
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
                // KGS Netos
                if (l.getConversor().equals(Double.valueOf(0))) {
                    if (!l.getConversor2().equals(Double.valueOf(0))) {
                        kgs = l.getConversor2() * unidades;
                    } else {
                        kgs = pedido.getEstado().equals(PedidosEnum.FINALIZADO.getValue()) ? l.getKgsFinales() : l.getKgs();
                    }
                    cell = new PdfPCell(new Phrase(df.format(kgs), fLinea));
                } else {
                    kgs = l.getConversor() * unidades;
                    cell = new PdfPCell(new Phrase(df.format(kgs), fLinea));
        
                }
        
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                if (tipoAlbaran == 1) {
                    cell = new PdfPCell(new Phrase(df.format(l.getPrecio()), fLinea));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                        cell = new PdfPCell(new Phrase(df.format(importeLinea / kgs), cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                    }
                    //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                    cell = new PdfPCell(new Phrase(l.getUnidadMedida(), fLinea2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    //}
                    cell = new PdfPCell(new Phrase(df.format(pedido.getDescuentoCliente()), fLinea));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(df.format(importeLinea), fLinea7));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
        
                contador++;
        
                if (contador == max) {
                    nuevaPagina(table);
        
                    if (tipoAlbaran == 1) {
                        if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                            entra = true;
                            float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, new Float(1.5), 1, 1, new Float(1.5), 1, 1, new Float(0.75), 1 };
                            table = new PdfPTable(columnWidths);
                        } else {
                            float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, new Float(1.5), 1, 1, 1, 1, new Float(0.75), 1 };
                            table = new PdfPTable(columnWidths);
                        }
                        if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                            if (entra) {
                                float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, new Float(0.75), 1 };
                                table = new PdfPTable(columnWidths);
                            } else {
                                float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, 1, 1, 1, 1, 1, new Float(0.75), 1 };
                                table = new PdfPTable(columnWidths);
                            }
                        }
                    } else {
                        table = new PdfPTable(columnWidths2);
                    }
                    if (!cab) {
                        informacionInicial();
                    } else {
                        informacionInicial2();
                    }
        
                    // La tabla de la cabecera.
                    document.add(crearTablaCabecera1(writer));
                    table.setWidthPercentage(100);
                    cell = new PdfPCell(new Phrase("Ref.Client", cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Descripción", cabecera2));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Org", cabecera2));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Cajas", cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    // Línea en blanco
                    cell = new PdfPCell(new Phrase("Unids", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);
                    if (tipoAlbaran == 1) {
                        cell = new PdfPCell(new Phrase("Bruto", cabecera));
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("TARA", cabecera));
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        table.addCell(cell);
                    }
                    cell = new PdfPCell(new Phrase("Neto", cabecera));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    table.addCell(cell);
                    if (tipoAlbaran == 1) {
                        cell = new PdfPCell(new Phrase("Precio UD/kg", cabecera));
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        table.addCell(cell);
                        if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                            cell = new PdfPCell(new Phrase("Precio Kg", cabecera));
                            cell.setBackgroundColor(myColor);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            table.addCell(cell);
                        }
                        //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                        cell = new PdfPCell(new Phrase("U.M", cabecera));
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        table.addCell(cell);
                        //}
                        cell = new PdfPCell(new Phrase("R", cabecera));
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Importe", cabecera));
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        table.addCell(cell);
                    }
                    contador = 0;
                }
        
            }
        
            if (!mCajasAlbaran.isEmpty()) {
                Set<String> keys = mCajasAlbaran.keySet();
                for (String kCaja : keys) {
                    cell = new PdfPCell(new Phrase(kCaja, fLinea2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(mCajasDescripcion.get(kCaja), fLinea2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", fLinea));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("" + mCajasAlbaran.get(kCaja) + ",00", fLinea));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", fLinea));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    if (tipoAlbaran == 1) {
                        cell = new PdfPCell(new Phrase("", fLinea));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                        if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                        }
                        //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        //}
                        cell = new PdfPCell(new Phrase("", fLinea));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("", fLinea));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                    }
                    contador++;
        
                    if (contador == max) {
                        nuevaPagina(table);
                        if (tipoAlbaran == 1) {
                            if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                                entra = true;
                                float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, new Float(1.5), 1, 1, new Float(1.5), 1, 1, new Float(0.75), 1 };
                                table = new PdfPTable(columnWidths);
                            } else {
                                float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, new Float(1.5), 1, 1, 1, 1, new Float(0.75), 1 };
                                table = new PdfPTable(columnWidths);
                            }
                            if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                                if (entra) {
                                    float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, new Float(0.75), 1 };
                                    table = new PdfPTable(columnWidths);
                                } else {
                                    float[] columnWidths = { new Float(1.5), 5, 1, 1, 1, 1, 1, 1, 1, 1, new Float(0.75), 1 };
                                    table = new PdfPTable(columnWidths);
                                }
                            }
                        } else {
                            table = new PdfPTable(columnWidths2);
                        }
        
                        if (!cab) {
                            informacionInicial();
                        } else {
                            informacionInicial2();
                        }
        
                        // La tabla de la cabecera.
                        document.add(crearTablaCabecera1(writer));
                        table.setWidthPercentage(100);
                        cell = new PdfPCell(new Phrase("Ref.Client", cabecera2));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        cell.setBackgroundColor(myColor);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Descripción", cabecera2));
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Org", cabecera2));
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("Cajas", cabecera2));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setBackgroundColor(myColor);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        table.addCell(cell);
                        // Línea en blanco
                        cell = new PdfPCell(new Phrase("Unids", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        cell.setBackgroundColor(myColor);
                        table.addCell(cell);
                        if (tipoAlbaran == 1) {
                            cell = new PdfPCell(new Phrase("Bruto", cabecera));
                            cell.setBackgroundColor(myColor);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("TARA", cabecera));
                            cell.setBackgroundColor(myColor);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            table.addCell(cell);
                        }
                        cell = new PdfPCell(new Phrase("Neto", cabecera));
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                        table.addCell(cell);
                        if (tipoAlbaran == 1) {
                            cell = new PdfPCell(new Phrase("Precio UD/kg", cabecera));
                            cell.setBackgroundColor(myColor);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            table.addCell(cell);
                            if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                                cell = new PdfPCell(new Phrase("Precio Kg", cabecera));
                                cell.setBackgroundColor(myColor);
                                cell.setBorder(PdfPCell.NO_BORDER);
                                cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                                table.addCell(cell);
                            }
                            //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                            cell = new PdfPCell(new Phrase("U.M", cabecera));
                            cell.setBackgroundColor(myColor);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            table.addCell(cell);
                            //}
                            cell = new PdfPCell(new Phrase("R", cabecera));
                            cell.setBackgroundColor(myColor);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase("Importe", cabecera));
                            cell.setBackgroundColor(myColor);
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                            table.addCell(cell);
                        }
        
                        contador = 0;
        
                    }
        
                }
            }
        
            if (contador > 15) {
                if (tipoAlbaran == 0) {
                    // Líneas en blanco
                    for (int i = contador; i < max; i++) {
                        // Línea en blanco
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        if (tipoAlbaran == 1) {
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                        }
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        if (tipoAlbaran == 1) {
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                            if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                                cell = new PdfPCell(new Phrase(" ", cabecera));
                                cell.setBorder(PdfPCell.NO_BORDER);
                                cell.setCellEvent(new CustomBorder(null, null, null, null));
                                table.addCell(cell);
                            }
                            //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                            //}
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                        }
                    }
                } else {
                    // Líneas en blanco
                    for (int i = contador; i < max; i++) {
                        // Línea en blanco
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        if (tipoAlbaran == 1) {
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                        }
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        if (tipoAlbaran == 1) {
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                            if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                                cell = new PdfPCell(new Phrase(" ", cabecera));
                                cell.setBorder(PdfPCell.NO_BORDER);
                                cell.setCellEvent(new CustomBorder(null, null, null, null));
                                table.addCell(cell);
                            }
                            //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                            //}
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                        }
        
                    }
                }
                // Línea con los totales.
                cell = new PdfPCell(new Phrase("", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(pedido.getObservaciones() != null ? pedido.getObservaciones() : "", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                if (tipoAlbaran == 1) {
                    cell = new PdfPCell(new Phrase("", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Phrase("", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                if (tipoAlbaran == 1) {
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
                    if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        //cell.setBackgroundColor(myColor);
                        cell.setCellEvent(new CustomBorder(null, null, null, solid));
                        table.addCell(cell);
                    }
                    //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
                    //}
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
        
                }
        
            } else {
        
                // Líneas en blanco
                for (int i = contador; i < max; i++) {
                    // Línea en blanco
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    if (tipoAlbaran == 1) {
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                    }
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    if (tipoAlbaran == 1) {
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                            cell = new PdfPCell(new Phrase(" ", cabecera));
                            cell.setBorder(PdfPCell.NO_BORDER);
                            cell.setCellEvent(new CustomBorder(null, null, null, null));
                            table.addCell(cell);
                        }
                        //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        //}
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                    }
        
                }
                // Línea con los totales.
                cell = new PdfPCell(new Phrase("", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(pedido.getObservaciones() != null ? pedido.getObservaciones() : "", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                if (tipoAlbaran == 1) {
                    cell = new PdfPCell(new Phrase("", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Phrase("", cabecera));
                cell.setBorder(PdfPCell.NO_BORDER);
                //cell.setBackgroundColor(myColor);
                cell.setCellEvent(new CustomBorder(null, null, null, solid));
                table.addCell(cell);
                if (tipoAlbaran == 1) {
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
                    if (Utils.booleanFromInteger(cliente.getIndPrecKgAlb())) {
                        cell = new PdfPCell(new Phrase(" ", cabecera));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        //cell.setBackgroundColor(myColor);
                        cell.setCellEvent(new CustomBorder(null, null, null, solid));
                        table.addCell(cell);
                    }
                    //if (Utils.booleanFromInteger(cliente.getIndDescAlb())) {
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
                    //}
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    //cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, solid));
                    table.addCell(cell);
        
                }
            }
        
        } catch (
        
        Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el albarán, faltan datos:  " + npe.getMessage());
        }
        */
        return table;
    }

    /**
     * Nos genera la parte final del albarán
     * @return La tabla que corresponde con el final del albarán.
     * @throws IOException
     * @throws GenasoftException
     */
    public PdfPTable pieAlbaran2(TProveedores proveedor) throws IOException, GenasoftException {
        PdfPCell cell;

        // EL pie.
        PdfPTable table2;
        LineDash solid = new SolidLine();
        float[] columnWidths2 = { 7, 2 };

        table2 = new PdfPTable(columnWidths2);

        table2.setWidthPercentage(100);
        cell = new PdfPCell(new Phrase("Transportado por:", cabecera2));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, null));
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase(" ", cabecera2));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, solid, null));
        table2.addCell(cell);

        //
        //  if (proveedor.getNombre() != null) {
        //      cell = new PdfPCell(new Phrase("" + proveedor.getNombre() + ", " + proveedor.getDireccion() + ", " + proveedor.getCp() + ", " + proveedor.getCiudad(), cabecera2));
        //  } else {
        cell = new PdfPCell(new Phrase("", cabecera2));
        //  }
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, null, solid));
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase("Firma transportado ", cabecera2));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, solid, null, solid));
        table2.addCell(cell);

        cell = new PdfPCell(new Phrase("Observaciones ", cabecera2));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, null, null, null));
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase(" ", cabecera2));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, solid, null, null));
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase(" ", cabecera2));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(solid, null, null, solid));
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase(" ", cabecera2));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, solid, null, solid));
        table2.addCell(cell);

        return table2;
    }

    private void nuevaPagina(PdfPTable table) throws DocumentException {
        document.add(table);
        document.newPage();
    }

    private void informacionInicial() throws IOException, GenasoftException, DocumentException {
        img.scaleAbsolute(75f, 75f);
        //img3.scalePercent(38f);

        //document.add(img3);

        //document.add(img);
        float[] columnWidths = { 1, 1, 2 };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        //table.setTableEvent(new ImageContent(img));

        table.addCell(crearTablaVaciaLogo());
        // Insertamos la tabla con la información de la empresa.
        table.addCell(crearTablaEmpresa2());
        // Insertamos la tabla con la información del consignatario.
        //table.addCell(crearTablaConsignatario());
        // Insertamos la tabla con la información del consignatario.
        table.addCell(crearTablaConsignatario2());

        document.add(table);

        // Info adicional cabecera.
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("  ", boldFont15));

        document.add(preface);

    }

    private void informacionInicial2() throws IOException, GenasoftException, DocumentException {
        img.scaleAbsolute(80f, 80f);
        //img3.scalePercent(38f);

        //document.add(img3);

        //document.add(img);
        float[] columnWidths = { 1, 1, 2 };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setTableEvent(new ImageContent(img));

        table.addCell(crearTablaVaciaLogo());
        // Insertamos la tabla con la información de la empresa.
        table.addCell(crearTablaEmpresa2());
        // Insertamos la tabla con la información del consignatario.
        table.addCell(crearTablaConsignatario2());

        document.add(table);

    }

}
