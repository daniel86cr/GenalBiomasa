/**
 * Aplicacion Control transporte Brostel.
 * http://www.brostel.net/
 * Copyright (C) 2019
 */
package com.dina.genasoft.utils.exportar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.common.ClientesSetup;
import com.dina.genasoft.common.EmpresasSetup;
import com.dina.genasoft.common.OperadoresSetup;
import com.dina.genasoft.common.PesajesSetup;
import com.dina.genasoft.common.TransportistasSetup;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TDireccionCliente;
import com.dina.genasoft.db.entity.TEmpresas;
import com.dina.genasoft.db.entity.TOperadores;
import com.dina.genasoft.db.entity.TPesajes;
import com.dina.genasoft.db.entity.TTransportistas;
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
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfPage;
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
public class AlbaranPDF extends PdfPageEventHelper {
    /** El log de la aplicación*/
    private static final org.slf4j.Logger log        = org.slf4j.LoggerFactory.getLogger(AlbaranPDF.class);
    /** Inyección de Spring para poder acceder a la capa de datos de empresas.*/
    @Autowired
    private EmpresasSetup                 empresasSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de pesajes.*/
    @Autowired
    private PesajesSetup                  pesajesSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de clientes.*/
    @Autowired
    private ClientesSetup                 clientesSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de transportistas.*/
    @Autowired
    private TransportistasSetup           transportistasSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de operadores.*/
    @Autowired
    private OperadoresSetup               operadoresSetup;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${pdf.temp}")
    private String                        pdfTemp;
    /** Contendrá el ID de la dirección de la empresa.*/
    @Value("${app.id.dir}")
    private String                        appIdDir;
    /** El path del código de barras.*/
    private String                        path1      = null;
    /** El pesaje que se va a exportar a PDF.*/
    private TPesajes                      pesaje;
    /** Fuente de párrafo. */
    private Font                          boldFont   = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont15 = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont12 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          boldFont16 = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    /** Fuente de párrafo. */
    private Font                          cabecera   = new Font(Font.FontFamily.HELVETICA, 5, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          cabecera2  = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          cabecera3  = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          fLinea     = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
    /** Fuente de párrafo. */
    private Font                          error      = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL);
    /** Las columnas del documento. */
    public static final Rectangle[]       COLUMNS    = { new Rectangle(36, 36, 192, 806), new Rectangle(204, 36, 348, 806), new Rectangle(360, 36, 504, 806) };
    /** Color de las celdas sombreadas. */
    private BaseColor                     myColor    = new BaseColor(154, 205, 50);                                                                            // or red, green, blue, alpha
    /** La hoja final.*/
    private Document                      document   = null;
    /** El logo de la emprea.*/
    private Image                         img        = null;
    /** El logo de la emprea.*/
    private Image                         imgFirma   = null;
    /** El cliente del pedido. */
    private TClientes                     cliente;
    private TDireccionCliente             dir;
    private TEmpresas                     empresa;
    private TTransportistas               transportista;
    private TOperadores                   operador;

    public class Rotate extends PdfPageEventHelper {

        protected PdfNumber orientation = PdfPage.PORTRAIT;

        public void setOrientation(PdfNumber orientation) {
            this.orientation = orientation;
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            writer.addPageDictEntry(PdfName.ROTATE, orientation);
        }
    }

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
     * @param idPesaje El Identificador del pedido para realizar el albarán.
     * @param nombre El path donde se genera de forma temporal en disco el albarán.
     * @param pathLogo El path donde está el logo.
     * @throws IOException Si se produce alguna excepción a la hora de generar la documentación
     * @throws DocumentException Si se produce alguna excepción a la hora de generar la documentación
     */
    public void createPdf(int idPesaje, String nombre, String pathLogo, Boolean cabecera) throws IOException, DocumentException, GenasoftException {

        // Establecemos los márgenes del documento.
        float left = 10;
        float right = 10;
        float top = 0;
        float bottom = 0;

        if (cabecera) {
            top = 10;
            // img3 = Image.getInstance(pdfTemp + "/cabeceraAlbaranes.png");
        } else {
            top = 10;
        }

        document = new Document(PageSize.A4.rotate(), left, right, top, bottom);

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nombre + ".pdf"));

        path1 = pathLogo;

        img = Image.getInstance(path1);

        try {
            imgFirma = Image.getInstance(pdfTemp + "/Firmas/Firma_" + idPesaje + "_1.png");
        } catch (Exception e) {
            imgFirma = null;
        }

        // Obtenemos la hoja de ruta para generar el fichero PDF.
        pesaje = pesajesSetup.obtenerPesajePorId(idPesaje);

        if (pesaje != null) {

            cliente = clientesSetup.obtenerClientePorId(pesaje.getIdCliente());

            transportista = transportistasSetup.obtenerTransportistaPorId(pesaje.getIdTransportista());

            operador = operadoresSetup.obtenerOperadorPorId(pesaje.getIdOperador());

            document.open();

            try {

                informacionInicial2();
                // Info adicional cabecera.
                Paragraph preface = new Paragraph();
                preface.add(new Paragraph("  ", boldFont15));

                document.add(preface);

                // La tabla de la cabecera.
                document.add(crearTablaCabecera1(writer));
                // Info adicional cabecera.
                Paragraph preface2 = new Paragraph();
                preface2.add(
                             new Paragraph("                El cliente,                                                                                                                                 Conductor                                                                                                                        Conforme: Por la planta", fLinea));

                //  document.add(preface2);

                document.add(crearTablaInformacionFinal1());

                Paragraph preface3 = new Paragraph();
                preface3.add(
                             new Paragraph("Los partes se someten para la resolución de cualquier controersia relativa a la interpretación y ejecución del contrato de transporte al que se refiere el presente documento a la junta arbitral de transporte que proceda a lo establecido en la ley 16/85 de ordenación de los transportes terrestres y sus normas de desarrollo.", fLinea));

                document.add(preface3);

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

            document.close();

        } else {
            document.open();
            document.resetPageCount();
            PdfContentByte cb1 = writer.getDirectContent();
            Phrase header31 = new Phrase("Se ha producido el siguiente error: ", error);
            Phrase header41 = null;
            if (pesaje == null) {
                header41 = new Phrase("No se ha encontrado el pedido", error);
            } else if (cliente == null) {
                header41 = new Phrase("No se ha encontrado el cliente", error);
            } else {
                header41 = new Phrase("El pedido indicado está anulado, no se muestran datos", error);
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

            // Obtenemos la información de la empresa.
            empresa = empresasSetup.obtenerEmpresaPorId(1);

            table.setWidthPercentage(60);

            cell = new PdfPCell(new Phrase(empresa.getNombre(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(empresa.getDireccion(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(empresa.getCp() + " - " + empresa.getCiudad(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(empresa.getProvincia(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(empresa.getPais(), boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("C.I.F: " + empresa.getCif(), boldFont));
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
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
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

            String direccion = "";
            String provincia = "";
            String cif = "";
            String campo = "";

            TDireccionCliente dir = clientesSetup.obtenerDireccionClientePorId(pesaje.getIdDireccion());

            // Miramos si tiene EDI pagador, si lo tiene, cogemos la dirección, sino, cogemos la dirección del cliente.

            direccion = dir.getDireccion() != null ? dir.getDireccion() : "Dirección no indicada.";
            campo = dir.getCodigoPostal() != null ? dir.getCodigoPostal() : "CP no indicado";
            if (!campo.isEmpty()) {
                while (campo.length() < 5) {
                    campo = "0" + campo;
                }
            }
            campo = campo + " " + (dir.getPoblacion() != null ? dir.getPoblacion() : "Ciudad no indicada");
            provincia = dir.getProvincia() != null ? dir.getProvincia() : "Provincia no indicada";
            cif = "C.I.F: ";
            cif = cif + (cliente.getCif() != null ? cliente.getCif() : "");

            table.setWidthPercentage(40);
            cell = new PdfPCell(new Phrase("Factura", boldFont16));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(cliente.getNombre() != null ? cliente.getNombre() : cliente.getNombre(), boldFont12));;
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(direccion, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(campo, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(provincia, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(cif, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, null, solid));
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
     */
    public PdfPTable crearTablaConsignatario22() throws IOException, GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        LineDash solid = new SolidLine();

        try {

            table.setWidthPercentage(80);
            cell = new PdfPCell(new Phrase("Nº Albarán: " + pesaje.getNumeroAlbaran(), boldFont12));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Fecha: " + new SimpleDateFormat("dd/MMM/yyyy").format(pesaje.getFechaPesaje()), boldFont12));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Hora: " + new SimpleDateFormat("HH:mm:ss").format(pesaje.getFechaPesaje()), boldFont12));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont12));
            cell.setBorder(PdfPCell.NO_BORDER);
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
     */
    public PdfPTable crearTablaCabecera1(PdfWriter writer) throws IOException, GenasoftException {
        PdfPTable table;
        PdfPCell cell;
        LineDash solid = new SolidLine();
        DecimalFormat df = new DecimalFormat("#,##0.000");
        table = new PdfPTable(2);

        empresa = empresasSetup.obtenerEmpresaPorId(1);
        dir = clientesSetup.obtenerDireccionClientePorId(pesaje.getIdDireccion());

        try {

            if (cliente == null) {
                throw new GenasoftException("No se ha encontrado al cliente del pesaje.");
            }

            table.setWidthPercentage(100);
            cell = crearCeldaColspanRowspan("CENTRO DE PRODUCCIÓN: " + empresa.getDireccion() + "                                                                        GRU02199 NIMA2900009081", 2, 1, null);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = crearCeldaColspanRowspan("EXPEDIDOR Y CARGADOR: " + empresa.getNombre() + "  DOMICILIO: " + empresa.getDireccion() + "(" + empresa.getProvincia() + ")", 2, 1, null);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            // CLIENTE
            cell = new PdfPCell(new Phrase("", cabecera));
            cell.addElement(new Phrase("CLIENTE: " + cliente.getCif(), boldFont));
            cell.addElement(new Phrase(cliente.getNombre(), cabecera2));
            cell.addElement(new Phrase(dir.getDireccion(), cabecera2));
            cell.addElement(new Phrase(dir.getCodigoPostal() + " - " + dir.getPoblacion(), cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            // TRANSPORTISTA
            cell = new PdfPCell(new Phrase("", cabecera));
            cell.addElement(new Phrase("TRANSPORTISTA: " + transportista.getCif(), boldFont));
            cell.addElement(new Phrase(transportista.getNombre(), cabecera2));
            cell.addElement(new Phrase(transportista.getDireccion(), cabecera2));
            cell.addElement(new Phrase(transportista.getCodigoPostal() + " - " + transportista.getCiudad(), cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            // OBRA
            cell = new PdfPCell(new Phrase(" ", cabecera2));
            cell.addElement(new Phrase("OBRA: " + pesaje.getObra(), cabecera2));
            cell.addElement(new Phrase(" ", cabecera2));
            cell.addElement(new Phrase(" ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            // OPERADOR
            cell = new PdfPCell(new Phrase("", cabecera));
            cell.addElement(new Phrase("OPERADOR TRANSPORTE: " + operador.getCif(), boldFont));
            cell.addElement(new Phrase(operador.getNombre(), cabecera2));
            cell.addElement(new Phrase(operador.getDireccion(), cabecera2));
            cell.addElement(new Phrase(operador.getCodigoPostal() + " - " + operador.getCiudad(), cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setRowspan(2);
            table.addCell(cell);
            // ORIGEN
            cell = new PdfPCell(new Phrase("ORIGEN: " + pesaje.getOrigen(), cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            // DESTINO
            cell = new PdfPCell(new Phrase("DESTINO: " + pesaje.getDestino(), cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            table.addCell(tabla2Columnas("MATRÍCULA: " + pesaje.getMatricula(), "REMOLQUE: " + pesaje.getRemolque()));
            // MATERIAL
            cell = new PdfPCell(new Phrase("", cabecera2));
            cell.addElement(new Phrase("MATERIAL: " + pesaje.getRefMaterial() + " " + pesaje.getDescMaterial(), boldFont));
            cell.addElement(new Phrase("Ler: " + pesaje.getLerMaterial(), cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            // PESOS
            cell = new PdfPCell(new Phrase("", cabecera));
            cell.addElement(new Phrase("BRUTO: " + df.format(pesaje.getKgsBruto()), cabecera2));
            cell.addElement(new Phrase("TARA: : " + df.format(pesaje.getTara()), cabecera2));
            cell.addElement(new Phrase("NETO: : " + df.format(pesaje.getKgsNeto()), cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            // TEXTO
            cell = new PdfPCell(new Phrase("BÁSCULA VERIFICADA DE ACUERDO CON LA LEGISLACIÓN DE CONTROL METEOROLÓGICO VIGENTE", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            // OBSERVACIONES
            cell = new PdfPCell(new Phrase("", cabecera));
            cell.addElement(new Phrase("OBSERVACIONES: ", cabecera2));
            cell.addElement(new Phrase(" ", cabecera2));
            cell.addElement(new Phrase(" ", cabecera2));
            cell.addElement(new Phrase(pesaje.getObservaciones() != null ? pesaje.getObservaciones() : "", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);

        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar la factura, faltan datos:  " + npe.getMessage());
        }
        return table;
    }

    private void informacionInicial2() throws IOException, GenasoftException, DocumentException {
        img.scaleAbsolute(80f, 80f);

        //img3.scalePercent(38f);

        //document.add(img3);

        //document.add(img);
        float[] columnWidths = { 1, 2, 2 };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setTableEvent(new ImageContent(img));

        table.addCell(crearTablaVaciaLogo());
        // Insertamos la tabla con la información de la empresa.
        table.addCell(crearTablaEmpresa());
        // Insertamos la tabla con la información del consignatario.
        table.addCell(crearTablaConsignatario22());
        // Insertamos la tabla con la información del consignatario.
        // Insertamos la tabla con la información del consignatario.
        //table.addCell(crearTablaConsignatario2());

        document.add(table);

    }

    private PdfPCell crearCeldaColspanRowspan(String content, int colspan, int rowspan, BaseColor color) {
        PdfPCell cell = new PdfPCell(new Phrase(content, cabecera2));
        cell.setColspan(colspan);
        if (color != null) {
            cell.setBackgroundColor(color);
        }
        cell.setRowspan(rowspan);
        cell.setBorder(PdfPCell.NO_BORDER);

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    /**
     * Nos genera la parte final del albarán
     * @return La tabla que corresponde con el final del albarán.
     * @throws IOException
     * @throws BrosException
     */
    public PdfPTable tabla2Columnas(String col1, String col2) throws IOException {
        PdfPCell cell;

        // EL pie.
        PdfPTable table2;
        LineDash solid = new SolidLine();

        table2 = new PdfPTable(2);

        table2.setWidthPercentage(100);
        cell = new PdfPCell(new Phrase(col1, cabecera2));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setCellEvent(new CustomBorder(null, solid, null, null));
        //cell.setFixedHeight(36f);        
        table2.addCell(cell);
        cell = new PdfPCell(new Phrase(col2, cabecera2));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setCellEvent(new CustomBorder(null, solid, null, null));
        //cell.setFixedHeight(36f);
        table2.addCell(cell);

        return table2;
    }

    private PdfPTable crearTablaInformacionFinal1() throws GenasoftException {
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell;
        try {

            cell = new PdfPCell(new Phrase("El cliente ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Conductor ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Conforme: Por la planta ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
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
            // Añadimos la firma del transportista, si la tiene.
            if (imgFirma != null) {
                imgFirma.scalePercent(30f);
                imgFirma.setScaleToFitHeight(false);
                cell = new PdfPCell(imgFirma);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
            } else {
                cell = new PdfPCell(new Phrase(" ", cabecera2));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
            }
            cell = new PdfPCell(new Phrase(" ", cabecera2));
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
            cell = new PdfPCell(new Phrase(" ", cabecera2));
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
            cell = new PdfPCell(new Phrase(" ", cabecera2));
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
            cell = new PdfPCell(new Phrase(" ", cabecera2));
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
            cell = new PdfPCell(new Phrase(" ", cabecera2));
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
            cell = new PdfPCell(new Phrase(" ", cabecera2));
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

}
