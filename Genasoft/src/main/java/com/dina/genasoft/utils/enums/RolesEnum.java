/**

 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.enums;

/**
 * Enumerado para determinar los diferentes campos del proveedor <br>
 * para ser consultados.
 */
public enum RolesEnum
{
    /** ROL MÁSTER.*/
    MASTER(1),
    /** ROL ADMINISTRADOR. */
    ADMINISTRADOR(2),
    /** ROL ADMINISTRATIVO. */
    ADMINISTRATIVO(3);

    /**String data type. */
    private int valor;

    /** Type value.
     * @param pValue value.
     */
    RolesEnum(final int pValue) {
        valor = pValue;
    }

    public int getValue() {
        return valor;
    }

}
