/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.enums;

/**
 * Enumerado para determinar los diferentes campos del pesaje <br>
 * para ser consultados.
 */
public enum PesajesEnum
{
    /** Pesaje activo.*/
    ACTIVO(1),
    /** Pesaje no activo. */
    DESACTIVADO(0);

    /**String data type. */
    private int valor;

    /** Type value.
     * @param pValue value.
     */
    PesajesEnum(final int pValue) {
        valor = pValue;
    }

    public int getValue() {
        return valor;
    }

}
