/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.enums;

/**
 * Enumerado para determinar los diferentes campos del operador <br>
 * para ser consultados.
 */
public enum OperadorEnum
{
    /** Operador activo.*/
    ACTIVO(1),
    /** Operador no activo. */
    DESACTIVADO(0);

    /**String data type. */
    private int valor;

    /** Type value.
     * @param pValue value.
     */
    OperadorEnum(final int pValue) {
        valor = pValue;
    }

    public int getValue() {
        return valor;
    }

}
