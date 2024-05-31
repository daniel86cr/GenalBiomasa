/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que se encarga de comprimir en formato .ZIP ficheros.
 * @author Daniel Carmona Romero
 *
 */
@Component
@Slf4j
@Data
public class GeneradorZip {

    /** El log de la aplicación*/
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GeneradorZip.class);
    /** Contendrá el nombre de la aplicación.*/
    @Value("${pdf.temp}")
    private String                        pdfTemp;

    /**
     * Constructor vacío
     */
    public GeneradorZip() {

    }

    /**
     * Método que nos crea en un fichero .zip los ficheros que nos pasan por parámetro (lista de path)
     * @param lInformes Los ficheros a comprimir.
     */
    public String comprimirServicios(List<String> lInformes, String nombre) {
        log.debug("Debug", " -> GeneradorZip exportarServicios(lInformes={" + lInformes.size() + " informes a comprimir})");
        int i = 0;
        String nombreFichero = "";
        try {
            nombreFichero = "TRAZABILIDADES[Natural TROPIC]_" + nombre + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Utils.generarFecha()) + ".zip";
            FileOutputStream fos = new FileOutputStream(pdfTemp + nombreFichero);
            ZipOutputStream zos = new ZipOutputStream(fos);

            // Por cada path que exista en la lista, vamos comprimiendo el fichero.
            for (String path : lInformes) {

                addToZipFile(path, zos);
                i++;
                // Eliminamos informes y ficheros generados.
                File f = new File(path);
                f.delete();
            }

            zos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            log.error("Error", " Se ha producido un error al intentar comprimir el fichero: " + lInformes.get(i) + " no existe", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Error", " Se ha producido un error al intentar comprimir el fichero IOException: " + lInformes.get(i) + " no existe", e);
            e.printStackTrace();
        }

        log.debug("Debug", " <- GeneradorZip exportarServicios()");

        // Retornamos el nombre del fichero generado.
        return nombreFichero;
    }

    /**
     * M�todo que nos añade el fichero pasado por parámetro dentro de un fichero .zip.
     * @param fileName El path del fichero a comprimir.
     * @param zos El ZipOutputStream del fichero zip.
     * @throws FileNotFoundException Si no se encuentra el fichero a comprimir.
     * @throws IOException Si se produce alg�n error de L/E.
     */
    private void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {
        log.debug("Debug", " -> GeneradorZip addToZipFile(fileName={" + fileName + "})");

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();

        log.debug("Debug", " <- GeneradorZip addToZipFile()");
    }

}
