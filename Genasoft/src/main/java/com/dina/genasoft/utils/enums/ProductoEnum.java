/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils.enums;

/**
 * Enumerado para determinar los diferentes campos del producto <br>
 * para ser consultados.
 */
public enum ProductoEnum
{
    /** Producto activo. */
    ACTIVO(1),
    /** Producto no activo. */
    DESACTIVADO(0),
    /** Producto por calibre. */
    CALIBRE(1),
    /**Producto por diámetro. */
    DIAMETRO(2);

    /**String data type. */
    private int valor;

    /** Type value.
     * @param pValue value.
     */
    ProductoEnum(final int pValue) {
        valor = pValue;
    }

    public int getValue() {
        return valor;
    }

}
