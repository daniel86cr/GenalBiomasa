/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dina.genasoft.common.ClientesSetup;
import com.dina.genasoft.common.CommonSetup;
import com.dina.genasoft.common.FacturasSetup;
import com.dina.genasoft.db.entity.TClientes;
import com.dina.genasoft.db.entity.TFacturas;
import com.dina.genasoft.db.entity.TLineasFactura;
import com.dina.genasoft.db.entity.TOperacionActual;
import com.dina.genasoft.db.entity.TTrace;
import com.dina.genasoft.db.entity.TTrace2;
import com.dina.genasoft.exception.GenasoftException;
import com.dina.genasoft.utils.EnvioCorreo;
import com.dina.genasoft.utils.GeneradorZip;
import com.dina.genasoft.utils.Utils;
import com.dina.genasoft.utils.exportar.AlbaranPDF;
import com.dina.genasoft.utils.exportar.FacturaPDF;

/**
 * @author Daniel Carmona Romero
 * El controlador de la aplicacion
 */
@RestController
@Configurable
public class Controller {
    /** El log de la aplicación*/
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Controller.class);
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private ControladorVistas             contrVista;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private ClientesSetup                 clientesSetup;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private CommonSetup                   commonSetup;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private FacturasSetup                 facturasSetup;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private GeneradorZip                  generadorZip;
    /** Inyección de Spring para poder acceder a la lógica de exportación de datos en Excel.*/
    @Autowired
    private AlbaranPDF                    albaranPDF;
    /** Inyección de Spring para poder acceder a la lógica de exportación facturas en formato PDF.*/
    @Autowired
    private FacturaPDF                    facturaPDF;
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
    /** Contendrá el nombre de la aplicación.*/
    @Value("${app.min.bloqueo}")
    private Integer                       appMinBloqueo;

    /**
     * Para generar la hoja de ruta en formato PDF.
     * @param request La petición que nos llega del cliente
     * @param response La respuesta al cliente
     * @throws SQLException Si se produce alguna excepción.
     */
    @RequestMapping(value = "/albaranPesajes", method = RequestMethod.GET, produces = "application/zip")
    public void getAlbaranPesaje(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String parametros = !request.getParameter("idPesaje").equals("null") ? (String) request.getParameter("idPesaje") : null;
        response.setContentType("application/zip");

        if (parametros != null) {

            try {

                List<String> lNombres = Utils.generarListaGenerica();

                String nombreZip = "";
                TFacturas factura = null;

                if (!parametros.contains(",")) {
                    parametros = parametros.concat(",");
                }
                int size = parametros.split(",").length;

                String[] values = parametros.split(",");
                String nombreLogo = pdfTemp + "/logo_albaran.png";
                Integer idFactura = 0;
                TClientes cl = null;
                String nombre = null;
                List<TLineasFactura> lineas = null;
                for (int i = 0; i < size; i++) {
                    idFactura = Integer.valueOf(values[i]);
                    factura = facturasSetup.obtenerFacturaPorId(idFactura);
                    cl = clientesSetup.obtenerClientePorId(factura.getIdCliente());
                    nombre = factura.getNumeroFactura();

                    nombre = nombre + "_" + cl.getNombre();

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
                    lineas = facturasSetup.obtenerLineasFacturaPorIdFactura(idFactura);

                    String nombreTemporal = "";

                    nombreTemporal = pdfTemp + "/" + "Albaran_pesaje" + nombre + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Utils.generarFecha()) + "_" + i;

                    nombreTemporal = nombreTemporal + "_tmp";
                    // Generamos el fichero PDF con los datos del pedido.
                    albaranPDF.createPdf(idFactura, nombreTemporal, nombreLogo, false);

                    nombreZip = nombreTemporal + ".pdf";

                    lNombres.add(nombreZip);
                    //nombreZip = nombreTemporal + "W" + ".pdf";
                    //lNombres.add(nombreZip);

                }

                String ficheroComprimido = pdfTemp + generadorZip.comprimirServicios(lNombres, "Facturas_");

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
            } catch (Exception e) {
                log.error("Se ha producido el siguiente error en la generación de la factura");
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
    @RequestMapping(value = "/facturaVentas", method = RequestMethod.GET, produces = "application/zip")
    public void getFacturaVenta(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String parametros = !request.getParameter("idFactura").equals("null") ? (String) request.getParameter("idFactura") : null;
        response.setContentType("application/zip");

        if (parametros != null) {

            try {

                List<String> lNombres = Utils.generarListaGenerica();

                String nombreZip = "";
                TFacturas factura = null;

                if (!parametros.contains(",")) {
                    parametros = parametros.concat(",");
                }
                int size = parametros.split(",").length;

                String[] values = parametros.split(",");
                String nombreLogo = pdfTemp + "/logo_albaran.png";
                Integer idFactura = 0;
                TClientes cl = null;
                String nombre = null;
                List<TLineasFactura> lineas = null;
                for (int i = 0; i < size; i++) {
                    idFactura = Integer.valueOf(values[i]);
                    factura = facturasSetup.obtenerFacturaPorId(idFactura);
                    cl = clientesSetup.obtenerClientePorId(factura.getIdCliente());
                    nombre = factura.getNumeroFactura();

                    nombre = nombre + "_" + cl.getNombre();

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
                    lineas = facturasSetup.obtenerLineasFacturaPorIdFactura(idFactura);

                    String nombreTemporal = "";
                    if (lineas.size() == 1) {
                        nombreTemporal = pdfTemp + "/" + "Factura_ventas_" + nombre + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Utils.generarFecha()) + "_" + i;
                    } else {
                        nombreTemporal = pdfTemp + "/" + "Multi_Factura_ventas_" + nombre + "_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Utils.generarFecha()) + "_" + i;
                    }

                    nombreTemporal = nombreTemporal + "_tmp";
                    // Generamos el fichero PDF con los datos del pedido.
                    facturaPDF.createPdf(idFactura, nombreTemporal, nombreLogo, false);

                    nombreZip = nombreTemporal + ".pdf";

                    lNombres.add(nombreZip);
                    //nombreZip = nombreTemporal + "W" + ".pdf";
                    //lNombres.add(nombreZip);

                }

                String ficheroComprimido = pdfTemp + generadorZip.comprimirServicios(lNombres, "Facturas_");

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
            } catch (Exception e) {
                log.error("Se ha producido el siguiente error en la generación de la factura");
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
     * Proceso que se encarga de limpiar aquellos bloquedos que se den cuando un usuario no cierre sesión y cierre la ventana del navegador.
     */
    public void procesoLimpiezaOperacionesActivas() {

        Calendar cal = Calendar.getInstance();

        cal.setTime(Utils.generarFecha());
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.MINUTE, -appMinBloqueo);

        Date max = cal.getTime();

        List<TOperacionActual> lOperaciones = commonSetup.obtenerOperacionesPorTiempo(max);

        // Eliminamos los bloqueos.

        for (TOperacionActual op : lOperaciones) {
            commonSetup.eliminarOperacionEmpleado(op.getIdEmpleado());
        }

    }

}
// Process process = new ProcessBuilder("C:\\PathToExe\\MyExe.exe","param1","param2").start();
