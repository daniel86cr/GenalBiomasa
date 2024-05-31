/**
ç * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.configuration;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

import lombok.Data;

/**
 * Clase que se encarga de establecer los parametros de conexion con la base de datos.
 * @author Daniel Carmona Romero
 */

@Configuration
@Data
@ConfigurationProperties(locations = "classpath:database.properties", ignoreUnknownFields = false, prefix = "data.base")
public class DatabaseConfiguration {
    public static final String DATASOURCE_SAMPLE = "DATASOURCE_SAMPLE";
    /** Propiedades de base de datos, que se leen automaticamente del properties.*/
    @NotNull
    @NotBlank
    private String             source;
    private String             user;
    // LOCAL
    //private String             password          = "root";
    // IDEAL TROPIC
    //private String             password          = "u#L_ñ_?98kFg";
    // WEB GIRES
    private String             password          = "LbB_B#qj";

    @Primary
    @Bean(name = DATASOURCE_SAMPLE, destroyMethod = "")
    public DataSource getDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(source);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    /**
     * @return the datasourceSample
     */
    public static String getDatasourceSample() {
        return DATASOURCE_SAMPLE;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
