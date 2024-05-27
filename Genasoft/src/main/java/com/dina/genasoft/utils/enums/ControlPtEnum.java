/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.enums;

/**
 * Enumerado para determinar los diferentes campos del control de producto terminado <br>
 * para ser consultados.
 */
public enum ControlPtEnum
{
    /** Control de producto determinado con todos los campos correctamente informados .*/
    COMPLETO(1),
    /** Control de producto determinado con algún campo sin informar .*/
    INCOMPLETO(0);

    /**String data type. */
    private int valor;

    /** Type value.
     * @param pValue value.
     */
    ControlPtEnum(final int pValue) {
        valor = pValue;
    }

    public int getValue() {
        return valor;
    }

}
