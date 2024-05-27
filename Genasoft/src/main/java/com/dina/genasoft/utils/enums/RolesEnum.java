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
    /** ROL LIBRO DE TRAZABILIDAD. */
    LIBRO_TRAZABILIDAD(3),
    /** ROL CONTROL PT. */
    CONTROL_PT(4),
    /** ROL TRAZABILIDADES. */
    TRAZABILIDADES(5),
    /** ROL OPERARIO CONTROL PT. */
    OPERARIO_CONTROL_PT(6);

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
