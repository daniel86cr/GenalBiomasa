/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.exception;

public class GenasoftException extends Exception {

    /**
     * Constant serialVersionUID.
     */
    private static final long serialVersionUID = 4716598419461073308L;

    /**
     * Constructor de clase.
     * 
     * @param msg String
     */
    public GenasoftException(String msg) {

        super(msg);
    }

    /**
     * Constructor de clase.
     * 
     * @param msg String
     * @param e Throwable
     */
    public GenasoftException(String msg, Throwable e) {

        super(msg, e);
    }
}