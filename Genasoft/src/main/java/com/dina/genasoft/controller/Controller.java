/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dina.genasoft.common.CommonSetup;
import com.dina.genasoft.common.ImportadorSetup;
import com.dina.genasoft.db.entity.TTrace;
import com.dina.genasoft.db.entity.TTrace2;
import com.dina.genasoft.db.entity.TVentasVista;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.EnvioCorreo;
import com.dina.genasoft.utils.GeneradorZip;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.exportar.AlbaranFictPDF;
import com.dina.genasoft.utils.exportar.AlbaranPDF;
import com.dina.genasoft.utils.exportar.BalanceMasasPDF;
import com.dina.genasoft.utils.exportar.Excel;
import com.dina.genasoft.utils.exportar.InformeControlPDF;
import com.ibm.icu.util.Calendar;

/**
 * @author Daniel Carmona Romero
 * El controlador de la aplicacion
 */
@RestController
@Configurable
public class Controller {
    /** El log de la aplicación*/
    private static final org.slf4j.Logger log                 = org.slf4j.LoggerFactory.getLogger(Controller.class);
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private ControladorVistas             contrVista;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private ImportadorSetup               importadorSetup;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private CommonSetup                   commonSetup;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private GeneradorZip                  generadorZip;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private BalanceMasasPDF               balanceMasasPDF;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private AlbaranPDF                    albaranPDF;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private AlbaranFictPDF                albaranFictPDF;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private InformeControlPDF             informeControlPtPDF;
    /** Inyección de Spring para poder generar ficheros Excel.*/
    @Autowired
    private Excel                         excel;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${pdf.temp}")
    private String                        pdfTemp;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.name}")
    private String                        appName;
    /** Contendrá el directorio con los ficheros de compras a importar.*/
    @Value("${path.compras}")
    private String                        pathCompras;
    /** Contendrá el directorio con los ficheros de ventas a importar.*/
    @Value("${path.ventas}")
    private String                        pathVentas;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${datos.antiguos.dias}")
    private Integer                       pedidosAntiguosDias;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.int.interval}")
    private Integer                       appIntInterval;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.int.max}")
    private Integer                       appIntMax;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${name.client}")
    private String                        nameClient;
    @Autowired
    private EnvioCorreo                   envioCorreo;
    /** Contendrá el ID del usuario del administrador para recibir notificaciones.*/
    @Value("${user.notificacions}")
    private String                        userNotifications;
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.informe}")
    private Integer                       appInforme;
    private final Integer                 USUARIO_IMPORTACION = 0;
    // Necesario para migración de pedidos.

    /**
     * Para generar el documento de exportación en formato EXCEL.
     * @param request La petición que nos llega del cliente
     * @param response La respuesta al cliente
     * @throws SQLException Si se produce alguna excepción.
     */
    @RequestMapping(value = "/exportarTrazabilidadesExcel", method = RequestMethod.GET, produces = "application/ms-excel")
    public ModelAndView exportarTrazabilidadesExcel(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        response.setContentType("application/ms-excel");
        ModelAndView result = null;

        try {
            result = excel.exportarTrazabilidades(request, response);
            return result;
        } catch (Exception e) {
            log.error("Se ha producido el siguiente error en la generación del fichero excel de trazabilidades.");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Para generar el documento de exportación en formato EXCEL.
     * @param request La petición que nos llega del cliente
     * @param response La respuesta al cliente
     * @throws SQLException Si se produce alguna excepción.
     */
    @RequestMapping(value = "/exportarComprasVentasExcel", method = RequestMethod.GET, produces = "application/ms-excel")
    public ModelAndView exportarComprasVentasExcel(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        response.setContentType("application/ms-excel");
        ModelAndView result = null;

        try {
            result = excel.exportarComprasVentasExcel(request, response);
            return result;
        } catch (Exception e) {
            log.error("Se ha producido el siguiente error en la generación del fichero excel de trazabilidades.");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Para generar el documento de exportación en formato EXCEL.
     * @param request La petición que nos llega del cliente
     * @param response La respuesta al cliente
     * @throws SQLException Si se produce alguna excepción.
     */
    @RequestMapping(value = "/exportarLibroMasasExcel", method = RequestMethod.GET, produces = "application/ms-excel")
    public ModelAndView exportarLibroMasasExcel(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        response.setContentType("application/ms-excel");
        ModelAndView result = null;

        try {
            result = excel.exportarBalanceMasas(request, response);
            return result;
        } catch (Exception e) {
            log.error("Se ha producido el siguiente error en la generación del fichero excel de trazabilidades.");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Para generar el documento de exportación en formato EXCEL.
     * @param request La petición que nos llega del cliente
     * @param response La respuesta al cliente
     * @throws SQLException Si se produce alguna excepción.
     */
    @RequestMapping(value = "/exportarLibroMasasPdf", method = RequestMethod.GET, produces = "application/pdf")
    public void exportarLibroMasasPdf(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        response.setContentType("application/pdf");

        try {

            String idEmpleado = !request.getParameter("idEmpleado").equals("null") ? request.getParameter("idEmpleado") : null;

            String nombreLogo = pdfTemp + "lnT.png";

            List<String> lFiltros = Utils.generarListaGenerica();

            lFiltros.addAll(contrVista.obtenerFiltrosBalanceMasasExportar(Integer.valueOf(idEmpleado)));

            List<TVentasVista> lMasas = Utils.generarListaGenerica();

            lMasas.addAll(contrVista.obtenerBalanceMasasExportar(Integer.valueOf(idEmpleado)));

            List<TVentasVista> lTotalesMasas = Utils.generarListaGenerica();
            lTotalesMasas.addAll(contrVista.obtenerTotalesBalanceMasasExportar(Integer.valueOf(idEmpleado)));

            String nombreDocumento = "balanceMasas" + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Utils.generarFecha());
            balanceMasasPDF.createPdf(lMasas, lTotalesMasas, lFiltros, nombreDocumento, nombreLogo);

            nombreDocumento = nombreDocumento.concat(".pdf");

            if (!lMasas.isEmpty()) {

                if (!nombreDocumento.isEmpty()) {

                    ServletOutputStream outputStream;

                    outputStream = response.getOutputStream();

                    FileInputStream in = new FileInputStream(nombreDocumento);
                    byte[] b = new byte[10240];
                    int count;
                    while ((count = in.read(b)) >= 0) {
                        outputStream.write(b, 0, count);
                    }

                    in.close();
                    outputStream.flush();
                    outputStream.close();

                    // Eliminamos informes y ficheros generados.
                    File f = new File(nombreDocumento);
                    f.delete();

                }
            }
        } catch (Exception e) {
            log.error("Se ha producido el siguiente error en la generación del documento");
            e.printStackTrace();

        }
    }

    /**
     * Para generar el documento de exportación en formato EXCEL.
     * @param request La petición que nos llega del cliente
     * @param response La respuesta al cliente
     * @throws SQLException Si se produce alguna excepción.
     */
    @RequestMapping(value = "/exportarControlPtPdf", method = RequestMethod.GET, produces = "application/zip")
    public void exportarControlPtPdf(HttpServletRequest request, HttpServletResponse response) throws SQLException {

        try {

            String idEmpleado = !request.getParameter("idEmpleado").equals("null") ? request.getParameter("idEmpleado") : null;
            String idsControlPt = !request.getParameter("idSeleccionado").equals("null") ? request.getParameter("idSeleccionado") : null;
            response.setContentType("application/zip");

            if (idsControlPt != null && !idsControlPt.isEmpty() && !idsControlPt.equals("null")) {

                String nombreLogo = pdfTemp + "lnT.png";

                List<String> lNombres = Utils.generarListaGenerica();

                if (!idsControlPt.contains(",")) {
                    idsControlPt = idsControlPt.concat(",");
                }

                String[] values = idsControlPt.split(",");

                int size = idsControlPt.split(",").length;
                String nombreZip = "";
                String nombreTemporal = "";
                List<Integer> lIds = Utils.generarListaGenerica();
                for (int i = 0; i < size; i++) {
                    lIds.add(Integer.valueOf(values[i]));

                }

                nombreTemporal = pdfTemp + "/" + "informeControlPt" + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Utils.generarFecha());
                informeControlPtPDF.createPdf(lIds, nombreTemporal, nombreLogo);

                nombreZip = nombreTemporal + ".pdf";

                lNombres.add(nombreZip);

                String ficheroComprimido = pdfTemp + generadorZip.comprimirServicios(lNombres, "Controles_PT");

                // Comprimimos los albaranes.

                if (!ficheroComprimido.isEmpty()) {

                    ServletOutputStream outputStream;

                    outputStream = response.getOutputStream();

                    FileInputStream in = new FileInputStream(ficheroComprimido);
                    byte[] b = new byte[10240];
                    int count;
                    while ((count = in.read(b)) >= 0) {
                        outputStream.write(b, 0, count);
                    }

                    in.close();
                    outputStream.flush();
                    outputStream.close();

                    // Eliminamos informes y ficheros generados.
                    File f = new File(ficheroComprimido);
                    f.delete();

                }
                File f2 = null;
                // Ahora eiminamos los PDFs creados.
                for (String path : lNombres) {
                    f2 = new File(path);
                    f2.delete();
                }

            }

        } catch (Exception e) {
            log.error("Se ha producido el siguiente error en la generación del documento");
            e.printStackTrace();

        }
    }

    /**
     * Para generar la hoja de ruta en formato PDF.
     * @param request La petición que nos llega del cliente
     * @param response La respuesta al cliente
     * @throws SQLException Si se produce alguna excepción.
     */
    @RequestMapping(value = "/exportarTrazabilidadesPdf", method = RequestMethod.GET, produces = "application/zip")
    public void getAlbaran(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String parametros = !request.getParameter("idPedidos").equals("null") ? (String) request.getParameter("idPedidos") : null;
        response.setContentType("application/zip");

        if (parametros != null) {

            try {

                List<String> lNombres = Utils.generarListaGenerica();

                String nombreZip = "";

                if (!parametros.contains(",")) {
                    parametros = parametros.concat(",");
                }
                int size = parametros.split(",").length;

                String[] values = parametros.split(",");

                List<Integer> lIds = Utils.generarListaGenerica();
                for (int cnt = 0; cnt < size; cnt++) {
                    lIds.add(Integer.valueOf(values[cnt]));
                }

                List<String> lAlbaranes = importadorSetup.obtenerNumAlbaranIdsPedidos(lIds);

                String nombre = "";
                for (String nAlbaran : lAlbaranes) {

                    nombre = nAlbaran;
                    if (nombre.contains(" ")) {
                        nombre = nombre.replaceAll(" ", "_");
                    }
                    if (nombre.contains("/")) {
                        nombre = nombre.replaceAll("/", "_");
                    }
                    if (nombre.contains("\\")) {
                        nombre = nombre.replaceAll("\\", "_");
                    }
                    if (nombre.contains("\\.")) {
                        nombre = nombre.replaceAll(".", "_");
                    }
                    if (nombre.contains(",")) {
                        nombre = nombre.replaceAll(",", "-");
                    }
                    String nombreTemporal = pdfTemp + "/" + "Albaran_Venta_" + nombre + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Utils.generarFecha());

                    // Generamos el fichero PDF con los datos del pedido.
                    albaranPDF.createPdf(nAlbaran, nombreTemporal);

                    nombreZip = nombreTemporal + ".pdf";

                    lNombres.add(nombreZip);

                }

                String ficheroComprimido = pdfTemp + generadorZip.comprimirServicios(lNombres, "Albaranes_ventas");

                // Comprimimos los albaranes.

                if (!ficheroComprimido.isEmpty()) {

                    ServletOutputStream outputStream;

                    outputStream = response.getOutputStream();

                    FileInputStream in = new FileInputStream(ficheroComprimido);
                    byte[] b = new byte[10240];
                    int count;
                    while ((count = in.read(b)) >= 0) {
                        outputStream.write(b, 0, count);
                    }

                    in.close();
                    outputStream.flush();
                    outputStream.close();

                    // Eliminamos informes y ficheros generados.
                    File f = new File(ficheroComprimido);
                    f.delete();

                }
            } catch (Exception e) {
                log.error("Se ha producido el siguiente error en la generación del albarán");
                e.printStackTrace();
            }
        }
    }

    /**
     * Para generar la hoja de ruta en formato PDF.
     * @param request La petición que nos llega del cliente
     * @param response La respuesta al cliente
     * @throws SQLException Si se produce alguna excepción.
     */
    @RequestMapping(value = "/exportarTrazabilidadesFictPdf", method = RequestMethod.GET, produces = "application/zip")
    public void getAlbaranFict(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String parametros = !request.getParameter("idPedidos").equals("null") ? (String) request.getParameter("idPedidos") : null;
        response.setContentType("application/zip");

        if (parametros != null) {

            try {

                List<String> lNombres = Utils.generarListaGenerica();

                String nombreZip = "";

                if (!parametros.contains(",")) {
                    parametros = parametros.concat(",");
                }
                int size = parametros.split(",").length;

                String[] values = parametros.split(",");

                List<Integer> lIds = Utils.generarListaGenerica();
                for (int cnt = 0; cnt < size; cnt++) {
                    lIds.add(Integer.valueOf(values[cnt]));
                }

                List<String> lAlbaranes = importadorSetup.obtenerNumAlbaranIdsPedidosFict(lIds);

                String nombre = "";
                for (String nAlbaran : lAlbaranes) {

                    nombre = nAlbaran;
                    if (nombre.contains(" ")) {
                        nombre = nombre.replaceAll(" ", "_");
                    }
                    if (nombre.contains("/")) {
                        nombre = nombre.replaceAll("/", "_");
                    }
                    if (nombre.contains("\\")) {
                        nombre = nombre.replaceAll("\\", "_");
                    }
                    if (nombre.contains("\\.")) {
                        nombre = nombre.replaceAll(".", "_");
                    }
                    if (nombre.contains(",")) {
                        nombre = nombre.replaceAll(",", "-");
                    }
                    String nombreTemporal = pdfTemp + "/" + "Albaran_Venta_" + nombre + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Utils.generarFecha());

                    // Generamos el fichero PDF con los datos del pedido.
                    albaranFictPDF.createPdf(nAlbaran, nombreTemporal);

                    nombreZip = nombreTemporal + ".pdf";

                    lNombres.add(nombreZip);

                }

                String ficheroComprimido = pdfTemp + generadorZip.comprimirServicios(lNombres, "Albaranes_ventas");

                // Comprimimos los albaranes.

                if (!ficheroComprimido.isEmpty()) {

                    ServletOutputStream outputStream;

                    outputStream = response.getOutputStream();

                    FileInputStream in = new FileInputStream(ficheroComprimido);
                    byte[] b = new byte[10240];
                    int count;
                    while ((count = in.read(b)) >= 0) {
                        outputStream.write(b, 0, count);
                    }

                    in.close();
                    outputStream.flush();
                    outputStream.close();

                    // Eliminamos informes y ficheros generados.
                    File f = new File(ficheroComprimido);
                    f.delete();

                }
            } catch (Exception e) {
                log.error("Se ha producido el siguiente error en la generación del albarán");
                e.printStackTrace();
            }
        }
    }

    /**
     * Método que se encarga de comprobar la validez de la licencia.
     */
    public String controlLicencia() {

        InetAddress ip;

        String result = "OK";

        try {

            Calendar cal = Calendar.getInstance();
            cal.setTime(Utils.generarFecha());

            cargarEjerciciosDisponibles(cal.get(Calendar.YEAR));
            commonSetup.setEjercicio(cal.get(Calendar.YEAR));

            Process result2;
            /** try {
            
               ip = InetAddress.getLocalHost();
                String hostname = ip.getHostName();
                System.out.println("Your current IP address : " + ip.getCanonicalHostName());
                System.out.println("Your current IP address2 : " + ip.getHostAddress());
                System.out.println("Your current Hostname : " + hostname);
                result2 = Runtime.getRuntime().exec("traceroute -m 1 www.amazon.com");
            
                BufferedReader output = new BufferedReader(new InputStreamReader(result2.getInputStream()));
                String thisLine = output.readLine();
                StringTokenizer st = new StringTokenizer(thisLine);
                st.nextToken();
                String gateway = st.nextToken();
                System.out.printf("The gateway is %s\n", gateway);
                InetAddress inetAddress = InetAddress.getLocalHost();
                System.out.println("IP Address:- " + inetAddress.getHostAddress());
                System.out.println("-----");
                Enumeration e = NetworkInterface.getNetworkInterfaces();
            
                while (e.hasMoreElements()) {
                    NetworkInterface n = (NetworkInterface) e.nextElement();
                    Enumeration ee = n.getInetAddresses();
                    while (ee.hasMoreElements()) {
                        InetAddress i = (InetAddress) ee.nextElement();
                        System.out.println(i.getHostAddress());
                    }
                }
            
                System.out.println("-----");
                System.out.println("-----");
                try (final DatagramSocket socket = new DatagramSocket()) {
                    socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                    System.out.println(socket.getLocalAddress().getHostAddress());
                }
            } catch (IOException e3) {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            }
            */

            if (appInforme.equals(1)) {
                contrVista.estadoAplicacion = 1;
                return result;
            }

            ip = InetAddress.getLocalHost();

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

            if (mac != null) {

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }

                result = sb.toString();

                System.out.println("************************** LA MAC: " + result);
                System.out.println("EL NOMBRE DE NAMECLIENT: " + nameClient);

                // Comprobamos si existe el registro 
                try {
                    TTrace trace = commonSetup.obtenerInformacionLicencia(sb.toString(), nameClient);

                    // No existe licencia
                    if (trace == null) {
                        commonSetup.enviaNotificacionTelegramMasivo("SE ESTA EJECUTANDO LA APLICACION GENASOFT SIN LICENCIA \\n NOMBRE QUE SE ESTA UTILIZANDO EN LA APLICACIÓN: " + appName + " CLIENTE PROCEDENCIA: GENASOFT");
                        commonSetup.enviaNotificacionWhatsAppMasivo("SE ESTA EJECUTANDO LA APLICACION GENASOFT SIN LICENCIA \\n NOMBRE QUE SE ESTA UTILIZANDO EN LA APLICACIÓN: " + appName + " CLIENTE PROCEDENCIA: GENASOFT");
                        commonSetup.enviaNotificacionCorreoMasiva(
                                                                  "*** APLICACIÓN UTILIZÁNDOSE SIN LICENCIA ***",
                                                                      "SE ESTÁ EJECUTANDO LA APLICACIÓN GENASOFT SIN LICENCIA \n NOMBRE QUE SE ESTÁ UTILIZANDO EN LA APLICACIÓN: " + appName + " CLIENTE PROCEDENCIA: GENASOFT",
                                                                      null);

                        contrVista.estadoAplicacion = -1;
                    }
                    if (trace != null) {
                        // Si existe, comprobamos la validez.
                        if (Utils.generarFecha().after(trace.getValidez()) || trace.getEstado().equals(0) || !trace.getCliente().equals(nameClient)) {
                            contrVista.estadoAplicacion = -1;
                            commonSetup.enviaNotificacionTelegramMasivo("SE ESTÁ EJECUTANDO LA APLICACION GENASOFT SIN LICENCIA \\n NOMBRE QUE SE ESTA UTILIZANDO EN LA APLICACION: " + appName + " CLIENTE PROCEDENCIA: " + nameClient);
                            commonSetup.enviaNotificacionWhatsAppMasivo("SE ESTÁ EJECUTANDO LA APLICACION GENASOFT SIN LICENCIA \\n NOMBRE QUE SE ESTA UTILIZANDO EN LA APLICACION: " + appName + " CLIENTE PROCEDENCIA: " + nameClient);
                            commonSetup.enviaNotificacionCorreoMasiva(
                                                                      "*** APLICACIÓN UTILIZÁNDOSE SIN LICENCIA ***",
                                                                          "SE ESTÁ EJECUTANDO LA APLICACIÓN GENASOFT SIN LICENCIA \n NOMBRE QUE SE ESTÁ UTILIZANDO EN LA APLICACIÓN: " + appName + " CLIENTE PROCEDENCIA: " + nameClient,
                                                                          null);
                        } else {
                            TTrace2 traza = new TTrace2();
                            traza.setIntentos(0);
                            commonSetup.guardarIntentos(traza);
                            // Guardamos la última comprobación de la licencia.
                            trace.setLastCheck(Utils.generarFecha());
                            commonSetup.guardarInformacionLicencia(trace);
                            contrVista.estadoAplicacion = 1;
                        }
                    }
                } catch (GenasoftException e) {
                    // Ha dado error la consulta, puede ser por no tener conexíon a internet.
                    TTrace2 traza = commonSetup.obtenerIntentosFallidosLicencia();
                    traza.setIntentos(traza.getIntentos() + 1);
                    Integer max = appIntMax;
                    commonSetup.guardarIntentos(traza);
                    if (max == null || max > 90) {
                        max = 0;
                    }
                    if (traza.getIntentos() >= appIntInterval && traza.getIntentos() < max) {
                        contrVista.estadoAplicacion = 1;
                        try {
                            commonSetup.enviaNotificacionTelegramMasivo("SE ESTÁ EJECUTANDO LA APLICACION GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DIAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos());
                            commonSetup.enviaNotificacionWhatsAppMasivo("SE ESTÁ EJECUTANDO LA APLICACION GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DIAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos());
                            commonSetup.enviaNotificacionCorreoMasiva(
                                                                      "*** APLICACIÓN SIN VALIDAR LICENCIA ***",
                                                                          "SE ESTÁ EJECUTANDO LA APLICACIÓN GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DÍAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos(),
                                                                          null);
                        } catch (GenasoftException e1) {
                            contrVista.estadoAplicacion = -1;
                        }
                    } else if (traza.getIntentos().equals(max) || traza.getIntentos() > max) {
                        contrVista.estadoAplicacion = -1;
                        try {
                            commonSetup.enviaNotificacionTelegramMasivo(
                                                                        "SE ESTA EJECUTANDO LA APLICACION GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DIAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos()
                                                                                + " AL LLEGAR AL MAXIMO DE INTENTOS, SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA");
                            commonSetup.enviaNotificacionWhatsAppMasivo(
                                                                        "SE ESTA EJECUTANDO LA APLICACION GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DIAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos()
                                                                                + " AL LLEGAR AL MAXIMO DE INTENTOS, SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA");
                            commonSetup.enviaNotificacionCorreoMasiva(
                                                                      "*** APLICACIÓN SIN VALIDAR LICENCIA ***",
                                                                          "SE ESTÁ EJECUTANDO LA APLICACIÓN GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DÍAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos()
                                                                                  + " AL LLEGAR AL MÁXIMO DE INTENTOS, SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA",
                                                                          null);
                            try {
                                commonSetup.guardarIntentos(traza);
                            } catch (Exception e2) {
                                contrVista.estadoAplicacion = -1;
                            }

                        } catch (GenasoftException e1) {

                        }
                    }

                }
            } else {
                // No tiene conexión a internet.
                TTrace2 traza = commonSetup.obtenerIntentosFallidosLicencia();
                traza.setIntentos(traza.getIntentos() + 1);
                Integer max = appIntMax;
                commonSetup.guardarIntentos(traza);
                if (max == null || max > 90) {
                    max = 0;
                }
                if (traza.getIntentos() >= appIntInterval && traza.getIntentos() < max) {
                    contrVista.estadoAplicacion = 1;
                    try {
                        commonSetup.enviaNotificacionTelegramMasivo("SE ESTA EJECUTANDO LA APLICACION GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DIAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos());
                        commonSetup.enviaNotificacionWhatsAppMasivo("SE ESTA EJECUTANDO LA APLICACION GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DIAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos());
                        commonSetup.enviaNotificacionCorreoMasiva(
                                                                  "*** APLICACIÓN SIN VALIDAR LICENCIA ***",
                                                                      "SE ESTÁ EJECUTANDO LA APLICACIÓN GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DÍAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos(),
                                                                      null);
                    } catch (GenasoftException e1) {
                        contrVista.estadoAplicacion = -1;
                    }
                } else if (traza.getIntentos().equals(max) || traza.getIntentos() > max) {
                    contrVista.estadoAplicacion = -1;
                    try {
                        commonSetup.enviaNotificacionTelegramMasivo(
                                                                    "SE ESTA EJECUTANDO LA APLICACION GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DIAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos()
                                                                            + " AL LLEGAR AL MAXIMO DE INTENTOS, SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA");
                        commonSetup.enviaNotificacionWhatsAppMasivo(
                                                                    "SE ESTA EJECUTANDO LA APLICACION GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DIAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos()
                                                                            + " AL LLEGAR AL MAXIMO DE INTENTOS, SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA");
                        commonSetup.enviaNotificacionCorreoMasiva(
                                                                  "*** APLICACIÓN SIN VALIDAR LICENCIA ***",
                                                                      "SE ESTÁ EJECUTANDO LA APLICACIÓN GENASOFT SIN VALIDAR LICENCIA CLIENTE PROCEDENCIA: " + nameClient + "; DÍAS SIN PODER CONSULTAR LICENCIA: " + traza.getIntentos()
                                                                              + " AL LLEGAR AL MÁXIMO DE INTENTOS, SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA",
                                                                      null);
                        try {
                            commonSetup.guardarIntentos(traza);
                        } catch (Exception e2) {
                            contrVista.estadoAplicacion = -1;
                        }

                    } catch (GenasoftException e1) {

                    }
                } else {
                    contrVista.estadoAplicacion = 1;
                }

            }

        } catch (UnknownHostException e) {
            try {
                commonSetup.enviaNotificacionTelegramMasivo("NO SE HA PODIDO DETERMINAR LA MAC DEL DISPOSITIVO DONDE ESTA ALOJADA LA APLICACION GENASOFT. CLIENTE PROCEDENCIA: " + nameClient + ", SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA");
                commonSetup.enviaNotificacionWhatsAppMasivo("NO SE HA PODIDO DETERMINAR LA MAC DEL DISPOSITIVO DONDE ESTA ALOJADA LA APLICACION GENASOFT. CLIENTE PROCEDENCIA: " + nameClient + ", SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA");
                commonSetup.enviaNotificacionCorreoMasiva(
                                                          "*** APLICACIÓN INOPERATIVA ***",
                                                              "NO SE HA PODIDO DETERMINAR LA MAC DEL DISPOSITIVO DONDE ESTÁ ALOJADA LA APLICACIÓN GENASOFT. CLIENTE PROCEDENCIA: " + nameClient + ", SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA",
                                                              null);

            } catch (GenasoftException e1) {

            }
            contrVista.estadoAplicacion = -1;

        } catch (SocketException e) {
            try {
                commonSetup.enviaNotificacionTelegramMasivo("NO SE HA PODIDO DETERMINAR LA MAC DEL DISPOSITIVO DONDE ESTA ALOJADA LA APLICACIÓN GENASOFT. CLIENTE PROCEDENCIA: " + nameClient + ", SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA");
                commonSetup.enviaNotificacionWhatsAppMasivo("NO SE HA PODIDO DETERMINAR LA MAC DEL DISPOSITIVO DONDE ESTA ALOJADA LA APLICACIÓN GENASOFT. CLIENTE PROCEDENCIA: " + nameClient + ", SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA");
                commonSetup.enviaNotificacionCorreoMasiva(
                                                          "*** APLICACIÓN INOPERATIVA ***",
                                                              "NO SE HA PODIDO DETERMINAR LA MAC DEL DISPOSITIVO DONDE ESTÁ ALOJADA LA APLICACIÓN GENASOFT. CLIENTE PROCEDENCIA: " + nameClient + ", SE PROCEDE A DEJAR LA HERRAMIENTA INOPERATIVA",
                                                              null);

            } catch (GenasoftException e1) {

            }
            contrVista.estadoAplicacion = -1;
        }

        return result;
    }

    /**
     * Método que se encarga de comprobar si existen ficheros a importar de compra y venta en el directorio correspondiente
     * @throws GenasoftException 
     */
    public void cargaFicherosComprasVentasTrazabilidades() throws GenasoftException {

        String leyenda = "";
        String leyendaCorreo = "";
        Integer cntComprasOk = 0;
        Integer cntComprasKo = 0;
        Integer cntVentasOk = 0;
        Integer cntVentasKo = 0;

        // Cargamos los ficheros de compras que puedan haber.
        try (Stream<Path> walk = Files.walk(Paths.get(pathCompras))) {

            List<String> lResult = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());

            // Ordenamos la lista por orden alfabético.
            Collections.sort(lResult);
            for (String path : lResult) {
                if (path.endsWith(".txt") && !path.endsWith(".csv")) {
                    importadorSetup.importarFicheroCompras(path, "IMPORTACIÓN AUTOMÁTICA FECHA: " + new SimpleDateFormat("dd/MM/yyyy").format(Utils.generarFecha()), USUARIO_IMPORTACION);
                    cntComprasOk++;
                    // Eliminamos el fichero para no dejar morraya.
                    File f = new File(path);
                    f.delete();
                } else {
                    // Eliminamos el fichero para no dejar morraya.
                    File f = new File(path);
                    f.delete();
                    leyenda = leyenda + "El fichero de compras con nombre " + path + ", no se ha tenido en cuenta, ya que solo se admiten ficheros con extension '.txt' o '.csv'";
                    leyendaCorreo = leyendaCorreo + "El fichero de compras con nombre " + path + ", no se ha tenido en cuenta, ya que solo se admiten ficheros con extensión '.txt' o '.csv' + \n\n";
                    cntComprasKo++;
                }

            }

        } catch (IOException e) {
            leyendaCorreo = leyendaCorreo + "Se ha producido un error al intentar leer del directorio de ficheros de compras. \n\n";
            cntComprasKo++;
        }

        // Cargamos los ficheros de ventas que puedan haber.
        try (Stream<Path> walk = Files.walk(Paths.get(pathVentas))) {

            List<String> lResult = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
            // Ordenamos la lista por orden alfabético.
            Collections.sort(lResult);
            for (String path : lResult) {
                if (path.endsWith(".txt") && !path.endsWith(".csv")) {
                    importadorSetup.importarFicheroVentas(path, "IMPORTACIÓN AUTOMÁTICA FECHA: " + new SimpleDateFormat("dd/MM/yyyy").format(Utils.generarFecha()), USUARIO_IMPORTACION);
                    cntVentasOk++;
                    // Eliminamos el fichero para no dejar morraya.
                    File f = new File(path);
                    f.delete();
                } else {
                    // Eliminamos el fichero para no dejar morraya.
                    File f = new File(path);
                    f.delete();
                    leyenda = leyenda + "El fichero de ventas con nombre " + path + ", no se ha tenido en cuenta, ya que solo se admiten ficheros con extension '.txt' o '.csv'";
                    leyendaCorreo = leyendaCorreo + "El fichero de ventas con nombre " + path + ", no se ha tenido en cuenta, ya que solo se admiten ficheros con extensión '.txt' o '.csv' + \n\n";
                    cntVentasKo++;
                }
            }

        } catch (IOException e) {
            leyendaCorreo = leyendaCorreo + "Se ha producido un error al intentar leer del directorio de ficheros de ventas. \n\n";
            cntComprasKo++;
        }

        System.out.println(leyendaCorreo);

        //  FALTARÍA COMPROBAR SI ES IMPORTACIÓN, QUE RETORNE EL TEXTO Y SE ENVIE EL DETALLE POR CORREO, SI ES MANUAL, QUE LO MUESTRE EN PANTALLA. (PASARLE UN BOOLEAN)
        //  TAMBIÉN FALTA REINICIAR INDICES Y DICCIONARIOS DESPUES DE CADA IMPORTACIÓN EN LA QUE HAYA AL MENOS 1 FICHERO DE COMPRAS Y/O VENTAS.
    }

    private void cargarEjerciciosDisponibles(Integer ejercicio) {
        importadorSetup.obtenerEjerciciosDisponibles(ejercicio);
    }

}
// Process process = new ProcessBuilder("C:\\PathToExe\\MyExe.exe","param1","param2").start();
