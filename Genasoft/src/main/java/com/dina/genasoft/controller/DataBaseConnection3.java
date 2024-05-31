/**
 * Aplicacion Transportes TRAZABILIDADES[Natural TROPIC].
 * http://www.TRAZABILIDADES[Natural TROPIC].net/
 * Copyright (C) 2016
 */
package com.dina.genasoft.controller;

/**
 * @author Daniel Carmona Romero
 * En esta clase se establecen los parámetros de conexión con la base de datos externa.
 *
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dina.genasoft.exception.GenasoftException;

/**
 * Este clase se encarga de operar con la base de datos, estas operaciones son: <br>
 * - Crear conexion. <br>
 * - Retornar conexion activa. <br>
 * - Liberar conexion. <br>
 */
@Component
public class DataBaseConnection3 {
    /** Propiedades de base de datos, que se leen automaticamente del properties.*/
    @Value("${data.base2.ssource}")
    private String     ssource;
    @Value("${data.base2.suser}")
    // LOCAL
    private String     suser;
    //private String     spassword = "dataFood";
    // REMOTO
    private String     spassword = "D4t4F00d";
    @Value("${data.base2.sdriverclassname}")
    private String     sdriverclassname;
    /** Variable que contiene la conexion con la base de datos.*/
    private Connection conection;

    /**
     * @return the conection
     */
    public Connection getConection() {
        return conection;
    }

    /**
     * @param conection the conection to set
     */
    public void setConection(Connection conection) {
        this.conection = conection;
    }

    /**
     * Constructor vacio.
     * En el constructor se establece la conexion con la base de datos.
     * @throws TimeLaborException Si no se puede establecer conexion con la base de datos.
     */
    public DataBaseConnection3() throws GenasoftException {

    }

    /**
     * este metodo nos retorna la conexion con la base de datos.
     * Si no hay conexion se crea.
     * @return La conexion con la base de datos.
     * @throws TimeLaborException Si no se puede establecer la conexion.
     */
    public Connection getConnection() throws GenasoftException {
        if (getConection() == null) {
            establishConnection();
        }
        return getConection();
    }

    /**
     * Metodo para establecer la conexion con la base de datos.
     */
    private void establishConnection() throws GenasoftException {
        try {
            Class.forName(sdriverclassname);
            setConection(DriverManager.getConnection(ssource, suser, spassword));
        } catch (SQLException e) {
            throw new GenasoftException("No se ha podido conectar con la base de datos. SQLException in DataBaseConnection establishConnection()", e);
        } catch (ClassNotFoundException e) {
            throw new GenasoftException("No se ha podido establecer conexion con la base de datos. DRIVER. SQLException in DataBaseConnection establishConnection()", e);
        }
    }

    /**
     * este metodo nos libera la conexion de la base de datos.
     * @throws TimeLaborException Si se produce un error al liberar la conexion con la base de datos.
     */
    public void releaseConnection() throws GenasoftException {
        if (getConection() != null) {
            try {
                // Cerramos la conexion.
                getConection().close();
                // Inicializamos el objeto a null, para determinar que la conexion con la base de datos no se ha establecido.
                setConection(null);
            } catch (SQLException e) {
                throw new GenasoftException("No se ha podido establecer conexion con la base de datos. DRIVER. SQLException in DataBaseConnection establishConnection()", e);
            }
        }
    }

}
