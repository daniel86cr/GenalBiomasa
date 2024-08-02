/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.enums;

/**
 * Enumerado para determinar los diferentes campos del banco <br>
 * para ser consultados.
 */
public enum BancosEnum
{
    /** Banco activo.*/
    ACTIVO(1),
    /** Banco no activo. */
    DESACTIVADO(0);

    /**String data type. */
    private int valor;

    /** Type value.
     * @param pValue value.
     */
    BancosEnum(final int pValue) {
        valor = pValue;
    }

    public int getValue() {
        return valor;
    }

}
