/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.enums;

/**
 * Enumerado para determinar los diferentes campos del transportista <br>
 * para ser consultados.
 */
public enum TransportistaEnum
{
    /** Transportista activo.*/
    ACTIVO(1),
    /** Transportista no activo. */
    DESACTIVADO(0);

    /**String data type. */
    private int valor;

    /** Type value.
     * @param pValue value.
     */
    TransportistaEnum(final int pValue) {
        valor = pValue;
    }

    public int getValue() {
        return valor;
    }

}
