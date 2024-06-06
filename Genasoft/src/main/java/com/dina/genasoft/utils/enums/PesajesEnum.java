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
    ALBARAN(1),
    /** Pesaje facturado.*/
    FACTURADO(2),
    /** Pesaje anulado. */
    ANULADO(0),
    /** Pesaje sin firma del cliente. */
    SIN_FIRMA(0),
    /** TIPO DE PESAJE GENERICO. */
    TIPO_GENERICO(1);

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
