/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.configuration;

/**
 * Constantes de la aplicacion.
 */
public final class Constants {

    private Constants() {
    }

    /** Constante que indica que la operación se completó con éxito.*/
    public static String OPERACION_OK                           = "GBE-000";
    /** Constante de error que nos indica que el empleado tiene sesión activa en otro dispositivo.*/
    public static String EMPLEADO_EN_USO                        = "GBE-001";
    /** Constante de error que indica que se ha producido un error al crear al empleado. */
    public static String BD_KO_CREA_EMPL                        = "GBE-002";
    /** Constante de error que indica que no existe el empleado. */
    public static String EMPL_NO_EXISTE                         = "GBE-003";
    /** Constante de error que indica que el empleado existe con el nombre de usaurio indicado. */
    public static String EMPL_EXISTE_NOMBRE_USUARIO             = "GBE-004";
    /** Constante de error que indica que el empleado existe con el nombre indicado. */
    public static String EMPL_EXISTE_NOMBRE                     = "GBE-005";
    /** Constante de error que indica que se ha producido un error al modificar al empleado. */
    public static String BD_KO_MODIF_EMPL                       = "GBE-006";
    /** Constante de error que indica que se ha producido un error al crear el cliente. */
    public static String BD_KO_CREA_CLIENTE                     = "GBE-007";
    /** Constante de error que indica que se ha producido un error al modificar el cliente. */
    public static String BD_KO_MODIF_CLIENTE                    = "GBE-008";
    /** Constante de error que nos indica que el cliente ya existe con el nombre indicado.*/
    public static String CLIENTE_EXISTE_NOMBRE                  = "GBE-009";
    /** Constante de error que nos indica que la contraseña introducida no es correcta.*/
    public static String EMPLADO_CONTRASEÑA_INCORRECTA          = "GBE-010";
    /** Constante de error que nos indica que el empleado no existe.*/
    public static String BD_KO_EMPLEADO_NO_EXISTE               = "GBE-011";
    /** Constante de error que nos indica que existe un empleado con el código de acceso indicado.*/
    public static String BD_KO_CREAR_EMPLEADO_EXISTE_COD_ACCESO = "GBE-012";
    /** Constante de error que nos indica que existe un empleado con el código externo indicado.*/
    public static String EMPL_EXISTE_COD_EXTERNO                = "GBE-013";
}
