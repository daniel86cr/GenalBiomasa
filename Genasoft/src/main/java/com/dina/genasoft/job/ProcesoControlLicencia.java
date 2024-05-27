/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dina.genasoft.controller.Controller;

/**
 * Clase que se encarga de identificar los procesos que se van a ejecutar.
 * @author Daniel Carmona Romero
 */
@Component
public class ProcesoControlLicencia {
    /** Inyección de Spring para poder acceder al controlador de la aplicación.*/
    @Autowired
    private Controller                    controller;
    /** El log de la aplicación*/
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProcesoControlLicencia.class);

    @Scheduled(cron = "${jobs.check}")
    public void run() {
        Calendar cal = Calendar.getInstance();
        String fechaInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(cal.getTime());
        log.info(" ");
        log.info(" ");
        log.info("Se ejecuta el proceso para el control de la aplicacion: " + fechaInicio);
        log.info("**- INICIO PROCESO AUTOMÁTICO CONTROL DE LA APLICACION. HORA EJECUCIÓN: " + fechaInicio + " -**");
        // Se realiza la carga de avisos para poder presentarlos.
        String result = controller.controlLicencia();
        fechaInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(cal.getTime());
        log.info("ESTADO LICENCIA: " + result);
        log.info("**- FIN PROCESO AUTOMÁTICO CONTROL APLICACION. HORA FIN: " + fechaInicio + " -**");
        log.info(" ");
        log.info(" ");
    }

}
