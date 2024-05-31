/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.enums;

/**
 * Enumerado para determinar los diferentes campos de la empresa <br>
 * para ser consultados.
 */
public enum EmpresaEnum
{
    /** Empresa activa.*/
    ACTIVO(1),
    /** Empresa no activa. */
    DESACTIVADO(0);

    /**String data type. */
    private int valor;

    /** Type value.
     * @param pValue value.
     */
    EmpresaEnum(final int pValue) {
        valor = pValue;
    }

    public int getValue() {
        return valor;
    }

}
