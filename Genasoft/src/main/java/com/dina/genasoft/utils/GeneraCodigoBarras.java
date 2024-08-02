/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aspose.barcode.BarCodeBuilder;
import com.aspose.barcode.CodeLocation;
import com.aspose.barcode.Symbology;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que contiene la lógica necesaria para generar códigos de barra tipo DUN-14
 * @author Daniel Carmona Romero
 *
 */
@Component
@Slf4j
@Data
public class GeneraCodigoBarras {
    /** El log de la aplicación*/
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GeneraCodigoBarras.class);
    /** Contendrá el nombre de la aplicación.*/
    @Value("${pdf.temp}")
    private String                        pdfTemp;

    /**
     * Método que nos genera en la ruta pasada por parámetro (nombreFichero) el código de barras con el código pasado por parámetro.
     * @param idExpedicion El código de barras
     * @param nombreFichero El nombre de la imagen generada
     * @return Nos retorna un int con el resultado: 1--> Generado correctamente 0--> Se produjeron errores.
     */
    public int generaCodigoBarras(String codBarras, String nombreFichero) {

        nombreFichero = nombreFichero + ".png";
        int result = 1;

        // Create la instancia de BarCodeBuilder
        BarCodeBuilder b = new BarCodeBuilder();
        // Establecemos el tipo de código de barras.
        b.setSymbologyType(Symbology.Code128);
        // Especificamos el número del código de barras
        b.setCodeText(codBarras);
        // Establecemos la localización
        b.setCodeLocation(CodeLocation.None);

        // Generamos el código de barras
        BufferedImage image = b.getBarCodeImage();
        // Recortamos la imágen para quitar el nombre de la librería
        image = image.getSubimage(0, 10, image.getWidth(), image.getHeight() - 10);

        // Lógica para guardar en disco.
        String path = nombreFichero;
        File imgOutFile = new File(pdfTemp + "/" + path);
        try {
            ImageIO.write(image, "png", imgOutFile);
        } catch (IOException e) {
            log.error("Se ha producido el siguiente error al generar el código de barras con el número: " + codBarras);
            e.printStackTrace();
            result = 0;
        }
        // Retornamos el resultado de la generación del código de barras.
        return result;

    }

}
