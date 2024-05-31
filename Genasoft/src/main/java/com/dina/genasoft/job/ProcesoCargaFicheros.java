/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Clase que se encarga de identificar los procesos que se van a ejecutar.
 * @author Daniel Carmona Romero
 */
@Component
public class ProcesoCargaFicheros {
    /** Inyección de Spring para poder acceder al controlador de la aplicación.*/
    @Autowired
    //private Controller                    controller;
    /** El log de la aplicación*/
    //private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProcesoCargaFicheros.class);

    @Scheduled(cron = "${jobs.file.upload}")
    public void run() {
        // Se realiza la carga de avisos para poder presentarlos.
        //controller.cargaFicherosComprasVentasTrazabilidades();
    }
}
