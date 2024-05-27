/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.enums;

/**
 * Enumerado para determinar los diferentes campos del producto <br>
 * para ser consultados.
 */
public enum CertificacionesEnum
{
    /** CALIDAD BIO. */
    BIO("BIO"),
    /** CALIDAD CONVENCIONAL. */
    CONVENCIONAL("CONVENCIONAL");

    /** String data type. */
    private String valor;

    /** Type value.
     * @param pValue value.
     */
    CertificacionesEnum(final String pValue) {
        valor = pValue;
    }

    public String getValue() {
        return valor;
    }

}
