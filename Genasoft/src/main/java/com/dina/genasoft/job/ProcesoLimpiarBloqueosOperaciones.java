/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dina.genasoft.controller.Controller;

/**
 * Clase que se encarga de limpir las cache que por motivos varios, no se recargan ya que no se hacen inserts/modificaciones desde la aplicación.
 * @author Daniel Carmona Romero
 */
@Component
public class ProcesoLimpiarBloqueosOperaciones {
    /** Inyección de Spring para poder acceder al controlador de la aplicación.*/
    @Autowired
    private Controller controller;

    /** El log de la aplicación*/
    //private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProcesoLimpiarBloqueosOperaciones.class);

    @Scheduled(cron = "${jobs.cache.limp.bloq}")
    public void run() {
        //Calendar cal = Calendar.getInstance();
        //String fechaInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(cal.getTime());
        //log.info("Se ejecuta el proceso limpiar bloequeos de operaciones, hora ejecución: " + fechaInicio);
        //log.info("**- INICIO PROCESO AUTOMÁTICO LIMPIEZA BLOQUEOS DE OPERACIONES. HORA EJECUCIÓN: " + fechaInicio + " -**");
        // Se realiza la carga de avisos para poder presentarlos.
        controller.procesoLimpiezaOperacionesActivas();
        //fechaInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(cal.getTime());
        //log.info("**- FIN PROCESO AUTOMÁTICO LIMPIEZA BLOQUEOS DE OPERACIONES. HORA FIN: " + fechaInicio + " -**");

    }

}
