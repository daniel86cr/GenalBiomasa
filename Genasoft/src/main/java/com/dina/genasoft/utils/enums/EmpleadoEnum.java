/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.enums;

/**
 * Enumerado para determinar los diferentes campos del empleado <br>
 * para ser consultados.
 */
public enum EmpleadoEnum
{
    /** Empleado activo.*/
    ACTIVO(1),
    /** Empleado no activo. */
    DESACTIVADO(0),
    /** Contraseña incorrecta. */
    PASS_INCORRECT(-1),
    /** Empleado existente. */
    EMPLEADO_EXISTENTE(-2);

    /**String data type. */
    private int valor;

    /** Type value.
     * @param pValue value.
     */
    EmpleadoEnum(final int pValue) {
        valor = pValue;
    }

    public int getValue() {
        return valor;
    }

}
