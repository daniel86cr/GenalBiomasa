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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.common.BancosSetup;
import com.dina.genasoft.common.ClientesSetup;
import com.dina.genasoft.common.EmpresasSetup;
import com.dina.genasoft.common.FacturasSetup;
import com.dina.genasoft.common.MonedasSetup;
import com.dina.genasoft.common.PesajesSetup;
import com.dina.genasoft.db.entity.TBancos;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TDireccionCliente;
import com.dina.genasoft.db.entity.TEmpresas;
import com.dina.genasoft.db.entity.TIva;
import com.dina.genasoft.db.entity.TLineasFactura;
import com.dina.genasoft.db.entity.TLineasFacturaVista;
import com.dina.genasoft.db.entity.TPesajes;
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
import com.itextpdf.text.pdf.BaseFont;
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
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
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
    /** Inyección de Spring para poder acceder a la capa de datos de bancos.*/
    @Autowired
    private BancosSetup                   bancosSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de pesajes.*/
    @Autowired
    private PesajesSetup                  pesajesSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de clientes.*/
    @Autowired
    private ClientesSetup                 clientesSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de ivass.*/
    @Autowired
    private MonedasSetup                  ivasSetup;
    /** Inyección de Spring para poder acceder a la capa de datos de facturación.*/
    @Autowired
    private FacturasSetup                 facturasSetup;
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
    private Font                          boldFont9  = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
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
    /** El cliente del pedido. */
    private TClientes                     cliente;
    /** Diccionario para los diferentes tipos de IVA del sistema. */
    private Map<Integer, TIva>            mIvas;
    /** Diccionario para los importes de iva. */
    private Map<Integer, Double>          mIvasImporteAlbaran;
    /** Diccionario para las bases de los importes del iva (importe de líneas). */
    private Map<Integer, Double>          mIvasBaseAlbaran;
    /** El total del albarán. */
    private Double                        totalAlbaran;
    /** El total del albarán. */
    private Double                        totalAlbaranSinDesc;
    /** El total del IVA del albarán. */
    private Double                        totalAlbaranIVA;
    /** El método de pago seleccionado. */
    private String                        metodoPago;
    /** La moneda que se va a utilizar en el documento. */
    private String                        simboloMoneda;
    private TEmpresas                     empresa;

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

        // Obtenemos la hoja de ruta para generar el fichero PDF.
        pesaje = pesajesSetup.obtenerPesajePorId(idPesaje);

        if (pesaje != null) {

            cliente = clientesSetup.obtenerClientePorId(pesaje.getIdCliente());

            document.open();

            try {

                informacionInicial2();
                // Info adicional cabecera.
                Paragraph preface = new Paragraph();
                preface.add(new Paragraph("  ", boldFont15));

                document.add(preface);

                totalAlbaran = Double.valueOf(0);
                totalAlbaranSinDesc = Double.valueOf(0);

                simboloMoneda = "€";

                List<TIva> lIvas = ivasSetup.obtenerTodosIva();

                mIvasImporteAlbaran = new HashMap<Integer, Double>();
                mIvasBaseAlbaran = new HashMap<Integer, Double>();
                mIvas = new HashMap<Integer, TIva>();
                for (TIva iva : lIvas) {
                    mIvas.put(iva.getId(), iva);
                }
                // La tabla de la cabecera.
                document.add(crearTablaCabecera1(writer));
                // Info adicional cabecera.
                Paragraph preface2 = new Paragraph();
                preface2.add(new Paragraph("  ", fLinea));

                // La tabla con los datos del pedido.
                document.add(crearTablaCabecera2(writer, cabecera));
                // La información del IVA y TOTAL.
                informacionIvaYTotales();
                // Pintamos línea divisoria.
                /**PdfConte ntByte canvas = writer.getDirectContent();
                CMYKColor magentaColor = new CMYKColor(1.f, 1.f, 1.f, 1.f);
                canvas.setColorStroke(magentaColor);
                canvas.moveTo(10, 170);
                canvas.lineTo(580, 170);
                canvas.closePathStroke();
                */

                // La parte final del albarán.                    
                document.add(crearTablaInformacionFinal1());

                /**   if (cabecera) {
                    Paragraph preface4 = new Paragraph();
                    preface4.add(
                                 new Paragraph("\n\n\n\n\n\n                                                                                                                                         Responsable del Tratamiento: BROSTEL S.L - CIF: B84315431 \r"
                                         + "\n                                                                                                                            Dirección postal: ANTONIO LOPEZ AGUADO 9-2H, MADRID 28029, MADRID"
                                         + "\n \t                                                                                                                                     TELÉFONO: +34 902 196 715 - Correo electrónico: gdpr@brostel.es ", fLinea2));
                    document.add(preface4);
                }
                */
                /**  if (!mGgnLineas.isEmpty()) {
                    Paragraph preface5 = new Paragraph();
                    preface5.add(new Paragraph("\n\n *GGN 000000000000 Producto GlobalGap", boldFont7));
                    document.add(preface5);
                }
                */
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

    private PdfPTable crearTablaIvas() throws GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        LineDash solid = new SolidLine();
        try {

            cell = new PdfPCell(new Phrase("Desglose del I.V.A.", boldFont12));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(crearTablaDetalleIva());
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

    private PdfPTable crearTablaTotalesAlbaran() throws GenasoftException {
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        LineDash solid = new SolidLine();
        try {
            // SubTotal
            cell = new PdfPCell(new Phrase("Total Neto", boldFont9));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            String campo = df.format(totalAlbaranSinDesc);
            cell = new PdfPCell(new Phrase(campo, cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            // Total dto.
            cell = new PdfPCell(new Phrase("Total Dto.", boldFont9));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(df.format(totalAlbaranSinDesc - totalAlbaran), cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            // Total dto.
            cell = new PdfPCell(new Phrase("SubTotal", boldFont9));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(df.format(totalAlbaran), cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            // Portes
            cell = new PdfPCell(new Phrase("Portes", boldFont9));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("0,00", cabecera2));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            // Gastos
            cell = new PdfPCell(new Phrase("Gastos", boldFont9));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", fLinea));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            // Total IVA  + Recargo equivalencia.
            cell = new PdfPCell(new Phrase("Total IVA+Rec.", boldFont9));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("       " + df.format(totalAlbaranIVA), cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            // TOTAL
            cell = new PdfPCell(new Phrase("TOTAL", boldFont9));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            //cell = new PdfPCell(new Phrase(df.format(totalAlbaran + totalAlbaranIVA) + " " + simboloMoneda, cabecera2));
            cell = new PdfPCell(new Phrase(df.format(Utils.redondeoDecimales(2, totalAlbaran) + Utils.redondeoDecimales(2, totalAlbaranIVA)) + " " + simboloMoneda, cabecera2));
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

    private PdfPTable crearTablaRetencion() throws GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        LineDash solid = new SolidLine();
        try {

            cell = new PdfPCell(new Phrase("Retención", boldFont12));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(crearTablaDetalleRetencion());
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

    private PdfPTable crearTablaDetalleRetencion() throws GenasoftException {
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell;
        LineDash solid = new SolidLine();
        try {

            cell = new PdfPCell(new Phrase("% Ret", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Base", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Importe", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }
        return table;
    }

    private PdfPTable crearTablaFormaPago() throws GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        LineDash solid = new SolidLine();
        try {

            cell = new PdfPCell(new Phrase("Forma de pago y entidad bancaria", boldFont9));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(metodoPago, boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);

        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el documento, faltan datos:  " + npe.getMessage());
        }
        return table;
    }

    private PdfPTable crearTablaDetalleIva() throws GenasoftException {
        PdfPTable table = new PdfPTable(5);
        PdfPCell cell;
        LineDash solid = new SolidLine();
        try {

            cell = new PdfPCell(new Phrase("Base", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("%IVA", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Cuota IVA", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("% R.E", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Cuota R.E", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(myColor);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            // Los datos de IVA.
            Set<Integer> lIdsIvas = mIvasImporteAlbaran.keySet();
            int cont = 0;
            TIva iva = null;
            DecimalFormat df = new DecimalFormat("#,##0.00");
            totalAlbaranIVA = Double.valueOf(0);
            Double aux = Double.valueOf(0);
            Double aux2 = Double.valueOf(0);
            for (int id : lIdsIvas) {
                iva = mIvas.get(id);
                if (iva == null) {
                    cell = new PdfPCell(new Phrase(df.format(Utils.redondeoDecimales(2, mIvasBaseAlbaran.get(id))), fLinea));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setBackgroundColor(myColor);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", fLinea));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", fLinea));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(myColor);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);
                    aux = mIvasBaseAlbaran.get(id) * (aux2 / 100);
                    totalAlbaranIVA = totalAlbaranIVA + aux;
                    cell = new PdfPCell(new Phrase(df.format(Utils.redondeoDecimales(2, aux)), fLinea));
                    cell.setBackgroundColor(myColor);
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    table.addCell(cell);

                } else {
                    //totalAlbaranIVA = totalAlbaranIVA + mIvasImporteAlbaran.get(iva.getId());
                    if (iva.getDescripcion().toLowerCase().contains("equivalencia")) {
                        cell = new PdfPCell(new Phrase(df.format(Utils.redondeoDecimales(2, mIvasBaseAlbaran.get(iva.getId()))), boldFont));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setBackgroundColor(myColor);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", fLinea));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", fLinea));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("" + iva.getImporte(), fLinea));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        aux = mIvasBaseAlbaran.get(iva.getId()) * (iva.getImporte() / 100);
                        totalAlbaranIVA = totalAlbaranIVA + Utils.redondeoDecimales(2, aux);
                        cell = new PdfPCell(new Phrase(df.format(Utils.redondeoDecimales(2, aux)), fLinea));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Phrase(df.format(Utils.redondeoDecimales(2, mIvasBaseAlbaran.get(iva.getId()))), fLinea));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setBackgroundColor(myColor);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("" + iva.getImporte(), fLinea));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        aux = mIvasBaseAlbaran.get(iva.getId()) * (iva.getImporte() / 100);
                        totalAlbaranIVA = totalAlbaranIVA + Utils.redondeoDecimales(2, aux);
                        cell = new PdfPCell(new Phrase(df.format(Utils.redondeoDecimales(2, aux)), fLinea));
                        cell.setBackgroundColor(myColor);
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", fLinea));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase(" ", fLinea));
                        cell.setBorder(PdfPCell.NO_BORDER);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBackgroundColor(myColor);
                        cell.setCellEvent(new CustomBorder(null, null, null, null));
                        table.addCell(cell);
                    }
                }
                cont++;
            }
            while (cont < 3) {
                cell = new PdfPCell(new Phrase(" ", boldFont));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setBackgroundColor(myColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", boldFont15));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", boldFont));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", boldFont));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" ", boldFont));
                cell.setBackgroundColor(myColor);
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                cont++;
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
    public PdfPTable crearTablaConsignatario() throws IOException, GenasoftException {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        //LineDash solid = new SolidLine();

        try {

            TDireccionCliente dir = clientesSetup.obtenerDireccionClientePorId(pesaje.getIdDireccion());

            String cif = "";
            String campo = "";

            campo = dir.getCodigoPostal() != null ? dir.getCodigoPostal() : "CP no indicado";
            if (!campo.isEmpty()) {
                while (campo.length() < 5) {
                    campo = "0" + campo;
                }
            }
            campo = campo + " " + (dir.getPoblacion() != null ? dir.getPoblacion() : "Ciudad no indicada");

            cif = "CIF/NIF: ";
            cif = cif + (cliente.getCif() != null ? cliente.getCif() : "");

            table.setWidthPercentage(40);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            //cell = new PdfPCell(new Phrase("FACTURA CLIENTE", boldFont16));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cell.setBackgroundColor(myColor);
            //cell.setCellEvent(new CustomBorder(null, null, null, null));
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            //cell = new PdfPCell(new Phrase(cliente.getNombre() != null ? cliente.getNombre() : cliente.getNombre(), boldFont12));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            //cell = new PdfPCell(new Phrase(direccion, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            //cell = new PdfPCell(new Phrase(campo, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            //cell = new PdfPCell(new Phrase(provincia, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", boldFont));
            //cell = new PdfPCell(new Phrase(cif, cabecera3));
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

        float[] columnWidths = { 1, 1, 3, 1, 1, };

        table = new PdfPTable(columnWidths);

        try {

            if (cliente == null) {
                throw new GenasoftException("No se ha encontrado al cliente de la factura.");
            }

            table.setWidthPercentage(100);
            cell = new PdfPCell(new Phrase("Nº Factura", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Fecha factura", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Delegación", boldFont));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Departamento", boldFont));
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
            String numFactura = pesaje.getNumeroAlbaran();

            cell = new PdfPCell(new Phrase("" + numFactura, cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(new SimpleDateFormat("dd/MM/yyyy").format(pesaje.getFechaPesaje()), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(empresa.getDelegacion(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(empresa.getDepartamento(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("" + writer.getCurrentPageNumber(), cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
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
     */
    public PdfPTable crearTablaCabecera2(PdfWriter writer, Boolean cab) throws IOException, GenasoftException {
        PdfPTable table;
        PdfPCell cell;
        LineDash solid = new SolidLine();

        float[] columnWidths = { 2, 2, 5, 2, 1, 1, 1, new Float(1.5) };
        table = new PdfPTable(columnWidths);

        int contador = 0;

        try {

            table.setWidthPercentage(100);
            cell = new PdfPCell(new Phrase("Nº Albarán", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Referencia material", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Descripción material", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("LER Material", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Precio kilo", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Kgs netos", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("% Dto", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Base", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            //table.addCell(cell);
            cell = new PdfPCell(new Phrase("% Impuesto", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            //table.addCell(cell);
            cell = new PdfPCell(new Phrase("Importe neto", cabecera2));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            cell.setBackgroundColor(myColor);
            table.addCell(cell);

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
            //table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            //table.addCell(cell);

            // DATOS
            Double kgs = Double.valueOf("0");
            DecimalFormat df = new DecimalFormat("#,##0.00");
            Double importeLinea = Double.valueOf("0");
            Double aux = Double.valueOf(0);
            //TLineasPedido l = null;
            //TLineasPedidoVista l3 = null;
            TLineasFactura l4 = null;

            Double kgsTotales = Double.valueOf(0);
            TIva iva = null;
            TPesajes pesaje = null;
            List<TLineasFacturaVista> lLineas = Utils.generarListaGenerica();
            for (TLineasFacturaVista linea : lLineas) {
                l4 = facturasSetup.obtenerLineaFacturaPorId(Integer.valueOf(linea.getId()));
                pesaje = pesajesSetup.obtenerPesajePorAlbaran(l4.getNumeroAlbaran());

                // Es una línea del albarán
                if (l4.getKgsNetos().equals(Double.valueOf(0))) {
                    continue;
                }

                kgs = l4.getKgsNetos();

                importeLinea = l4.getImporte();

                kgsTotales = kgsTotales + kgs;

                // Quitamos el porcentaje de descuento, si lo tiene
                if (!l4.getDescuento().equals(Double.valueOf(0))) {
                    totalAlbaranSinDesc += importeLinea / (1 - (l4.getDescuento() / 100));
                } else {
                    totalAlbaranSinDesc += importeLinea;
                }

                totalAlbaran = totalAlbaran + importeLinea;

                iva = mIvas.get(l4.getIdIva());

                // Calculamos el importe del iva
                if (iva != null) {

                    aux = (iva.getImporte() * importeLinea) / 100;

                    if (mIvasBaseAlbaran.get(l4.getIdIva()) == null) {
                        mIvasBaseAlbaran.put(l4.getIdIva(), importeLinea);
                    } else {
                        mIvasBaseAlbaran.put(l4.getIdIva(), mIvasBaseAlbaran.get(l4.getIdIva()) + importeLinea);
                    }
                    if (mIvasImporteAlbaran.get(l4.getIdIva()) == null) {
                        mIvasImporteAlbaran.put(l4.getIdIva(), aux);
                    } else {
                        mIvasImporteAlbaran.put(l4.getIdIva(), mIvasImporteAlbaran.get(l4.getIdIva()) + aux);
                    }

                }

                // Número albarán
                cell = new PdfPCell(new Phrase(linea.getNumeroAlbaran(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                // Referencia material
                cell = new PdfPCell(new Phrase(linea.getRefMaterial(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                table.addCell(cell);
                // Descripción material
                cell = new PdfPCell(new Phrase(linea.getDescMaterial(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                // LER material
                cell = new PdfPCell(new Phrase(linea.getLerMaterial(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                // Precio kg.
                cell = new PdfPCell(new Phrase(df.format(pesaje.getPrecioKg()), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                // Kilos netos
                cell = new PdfPCell(new Phrase(linea.getKgsNetos(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                // Descuento
                cell = new PdfPCell(new Phrase(df.format(Double.valueOf(0)), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                // Base
                cell = new PdfPCell(new Phrase(linea.getImporte(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                //table.addCell(cell);
                // IVA
                cell = new PdfPCell(new Phrase(linea.getIva(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                //table.addCell(cell);
                // Total
                cell = new PdfPCell(new Phrase(linea.getTotal(), fLinea));
                cell.setBorder(PdfPCell.NO_BORDER);
                cell.setCellEvent(new CustomBorder(null, null, null, null));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);

                contador++;
                if (contador == 20) {
                    nuevaPagina(table);
                    if (!cab) {
                        informacionInicial();
                    } else {
                        informacionInicial2();
                    }

                    table = new PdfPTable(columnWidths);

                    // La tabla de la cabecera.
                    document.add(crearTablaCabecera1(writer));

                    table.setWidthPercentage(100);
                    cell = new PdfPCell(new Phrase("Referencia albarán", cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Referencia material", cabecera2));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Descripción material", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("LER Material", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Precio kilo", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Kilos netos", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("% Dto", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Base", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    //table.addCell(cell);
                    cell = new PdfPCell(new Phrase("% Impuesto", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    //table.addCell(cell);
                    cell = new PdfPCell(new Phrase("Importe neto", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
                    cell.setBackgroundColor(myColor);
                    table.addCell(cell);

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
                    //table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    //table.addCell(cell);

                    contador = 0;
                }

            }

            if (contador > 22) {
                // Líneas en blanco
                for (int i = contador; i < 18; i++) {
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
                    //table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    //table.addCell(cell);

                }
            } else {
                // Líneas en blanco
                for (int i = contador; i < 50; i++) {
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
                    //table.addCell(cell);
                    cell = new PdfPCell(new Phrase(" ", cabecera));
                    cell.setBorder(PdfPCell.NO_BORDER);
                    cell.setCellEvent(new CustomBorder(null, null, null, null));
                    //table.addCell(cell);

                }
            }

            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            //table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, solid));
            //table.addCell(cell);

        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el albarán, faltan datos:  " + npe.getMessage());
        }
        return table;
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
        PdfPCell cell;
        LineDash solid = new SolidLine();
        try {

            String nombreBanco = " - ";
            String numCuenta = "";
            TBancos banco = bancosSetup.obtenerBancoPorId(cliente.getIdBanco());

            if (banco != null) {
                nombreBanco = banco.getNombre();
            }

            if (cliente.getNumCuenta() != null) {
                numCuenta = cliente.getNumCuenta();
            } else {
                numCuenta = " - ";
            }

            cell = new PdfPCell(new Phrase("DATOS BANCARIOS " + empresa.getNombre(), boldFont12));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Banco: " + nombreBanco, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nº de cuenta: " + numCuenta, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el albarán, faltan datos:  " + npe.getMessage());
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

        //document.add(img);
        float[] columnWidths = { 1, 2, 3 };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        //table.setTableEvent(new ImageContent(img));

        table.addCell(crearTablaVaciaLogo());
        // Insertamos la tabla con la información de la empresa.
        table.addCell(crearTablaEmpresa());
        // Insertamos la tabla con la información del consignatario.
        table.addCell(crearTablaConsignatario());

        document.add(table);

        // Info adicional cabecera.
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("  ", boldFont15));
        preface.add(new Paragraph(" ", boldFont15));

        document.add(preface);

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

    private PdfPTable crearTablaInformacionFinal1() throws IOException, GenasoftException, DocumentException {

        // Info adicional cabecera.
        Paragraph preface3 = new Paragraph();
        preface3.add(new Paragraph(" ", fLinea));

        document.add(preface3);

        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;
        LineDash solid = new SolidLine();
        try {

            String nombreBanco = " - ";
            String numCuenta = "";
            TBancos banco = bancosSetup.obtenerBancoPorId(cliente.getIdBanco());

            if (banco != null) {
                nombreBanco = banco.getNombre();
            }

            if (cliente.getNumCuenta() != null) {
                numCuenta = cliente.getNumCuenta();
            } else {
                numCuenta = " - ";
            }

            cell = new PdfPCell(new Phrase("DATOS BANCARIOS " + empresa.getNombre(), boldFont12));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(myColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setCellEvent(new CustomBorder(solid, solid, solid, solid));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Banco: " + nombreBanco, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Nº de cuenta: " + numCuenta, cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(" ", cabecera3));
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setCellEvent(new CustomBorder(null, null, null, null));
            table.addCell(cell);
        } catch (Exception npe) {
            log.error("Error:");
            npe.printStackTrace();
            throw new GenasoftException("No se puede generar el albarán, faltan datos:  " + npe.getMessage());
        }

        table.setWidthPercentage(100);

        return table;
    }

    /**
     * Método que nos crea la tabla con la información del consignatario
     * @return La tabla con los datos
     * @throws DocumentException 
     */
    private void informacionIvaYTotales() throws IOException, GenasoftException, DocumentException {

        float f = new Float(4);
        float f2 = new Float(1.85);
        float[] columnWidths = { f, 2, f2 };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(crearTablaIvas());
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);
        // Insertamos la tabla con la información de la empresa.
        //table.addCell(crearTablaRetencion());
        cell = new PdfPCell(crearTablaRetencion());
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);
        // Insertamos la tabla con la información del consignatario.
        //table.addCell(crearTablaTotalesAlbaran());
        cell = new PdfPCell(crearTablaTotalesAlbaran());
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setCellEvent(new CustomBorder(null, null, null, null));
        table.addCell(cell);

        document.add(table);
    }

    /** 
     * Agregar marca de agua en archivo pdf 
    * 
    * @param inputFile 
     * Archivo original 
    * @param outputFile 
     * Archivo de salida de marca de agua 
    * @param waterMarkName 
     * Nombre de marca de agua 
    */
    public static void waterMark(String inputFile, String outputFile, String waterMarkName, String pathMarcaAgua) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));

            // establecer contraseña   
            //stamper.setEncryption(userPassWord.getBytes(), ownerPassWord.getBytes(), permission, false);
            BaseFont base = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            int total = reader.getNumberOfPages() + 1;
            Image image = Image.getInstance(pathMarcaAgua);
            image.setAbsolutePosition(100, 100);
            PdfContentByte under;
            int j = waterMarkName.length();
            char c = 0;
            int rise = 0;
            for (int i = 1; i < total; i++) {
                rise = 500;
                under = stamper.getUnderContent(i);
                // agregar imagen 
                // under.addImage(image); 
                under.beginText();

                under.setFontAndSize(base, 30);
                // Establece la fuente de texto de marca de agua para comenzar 
                if (j >= 15) {
                    under.setTextMatrix(200, 120);
                    for (int k = 0; k < j; k++) {
                        under.setTextRise(rise);
                        c = waterMarkName.charAt(k);
                        under.showText(c + "");
                        rise -= 20;
                    }
                } else {
                    under.setTextMatrix(180, 100);
                    for (int k = 0; k < j; k++) {
                        under.setTextRise(rise);
                        c = waterMarkName.charAt(k);
                        under.showText(c + "");
                        rise -= 18;
                    }
                }
                // Fin de la configuración de fuente 
                under.endText();
                // dibuja un círculo 
                // under.ellipse(250, 450, 350, 550); 
                // under.setLineWidth(1f); 
                // under.stroke(); 
            }
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void waterMark2(String origen, String fin, String imagen) {
        try {
            PdfReader reader = new PdfReader(origen);
            int n = reader.getNumberOfPages();

            // Create a stamper that will copy the document to a new file
            PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(fin));
            int i = 0;
            PdfContentByte under;
            PdfContentByte over;

            Image img = Image.getInstance(imagen);
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);

            img.setAbsolutePosition(230, 350);
            img.scaleAbsolute(150f, 150f);

            while (i < n) {
                // Watermark under the existing page
                under = stamp.getUnderContent(i + 1);
                under.addImage(img);

                // Text over the existing page
                over = stamp.getOverContent(i + 1);
                over.beginText();
                over.setFontAndSize(bf, 18);
                //over.showText("page " + i + 1);
                over.endText();

                i++;
            }

            stamp.close();
        } catch (Exception e) {

        }

    }

}
