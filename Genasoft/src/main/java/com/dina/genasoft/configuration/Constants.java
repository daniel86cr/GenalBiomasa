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
    /** Constante de error que nos indica que el cliente ya existe con el nombre indicado. */
    public static String CLIENTE_EXISTE_NOMBRE                  = "GBE-009";
    /** Constante de error que nos indica que la contraseña introducida no es correcta. */
    public static String EMPLADO_CONTRASEÑA_INCORRECTA          = "GBE-010";
    /** Constante de error que nos indica que el empleado no existe.*/
    public static String BD_KO_EMPLEADO_NO_EXISTE               = "GBE-011";
    /** Constante de error que nos indica que existe un empleado con el código de acceso indicado. */
    public static String BD_KO_CREAR_EMPLEADO_EXISTE_COD_ACCESO = "GBE-012";
    /** Constante de error que nos indica que existe un empleado con el código externo indicado. */
    public static String EMPL_EXISTE_COD_EXTERNO                = "GBE-013";
    /** Constante de error que nos indica que la licencia no es válida.*/
    public static String LICENCIA_NO_VALIDA                     = "GBE-014";
    /** Constante de error que nos indica que se ha iniciado sesión en otro dispositivo. */
    public static String SESION_INVALIDA                        = "GBE-015";
    /** Constante de error que nos indica que el empleado ya está desactivado .*/
    public static String EMPL_DESACTIVADO                       = "GBE-016";
    /** Constante con el texto activo para que sea uniforme en toda la aplicación. */
    public static String ACTIVO                                 = "Activo";
    /** Constante con el texto activo para que sea uniforme en toda la aplicación. */
    public static String DESACTIVADO                            = "Desactivado";
    /** Constante de error que nos indica que el cliente ya existe con el CIF indicado. */
    public static String CLIENTE_EXISTE_CIF                     = "GBE-017";
    /** Constante de error que indica que no existe el cliente. */
    public static String CLIENTE_NO_EXISTE                      = "GBE-018";
    /** Constante de error que nos indica que el cliente ya está desactivado .*/
    public static String CLIENTE_DESACTIVADO                    = "GBE-019";
    /** Constante de error que nos indica que hay un error general en la aplicación .*/
    public static String ERROR_GENERAL                          = "GBE-020";
    /** Constante de error que indica que se ha producido un error al registrar el pesaje. */
    public static String BD_KO_CREA_PESAJE                      = "GBE-021";
    /** Constante de error que indica que se ha producido un error al modificar el pesaje. */
    public static String BD_KO_MODIF_PESAJE                     = "GBE-022";
    /** Constante de error que indica que se ha producido un error al registrar el operador. */
    public static String BD_KO_CREA_OPERADOR                    = "GBE-023";
    /** Constante de error que indica que se ha producido un error al modificar el operador. */
    public static String BD_KO_MODIF_OPERADOR                   = "GBE-024";
    /** Constante de error que nos indica que el operador ya existe con el CIF indicado. */
    public static String OPERADOR_EXISTE_CIF                    = "GBE-025";
    /** Constante de error que indica que no existe el operador. */
    public static String OPERADOR_NO_EXISTE                     = "GBE-026";
    /** Constante de error que nos indica que el operador ya está desactivado .*/
    public static String OPERADOR_DESACTIVADO                   = "GBE-027";
    /** Constante de error que nos indica que el operador ya existe con el nombre indicado. */
    public static String OPERADOR_EXISTE_NOMBRE                 = "GBE-028";
    /** Constante de error que indica que se ha producido un error al registrar el transportista. */
    public static String BD_KO_CREA_TRANSPORTISTA               = "GBE-029";
    /** Constante de error que indica que se ha producido un error al modificar el transportista. */
    public static String BD_KO_MODIF_TRANSPORTISTA              = "GBE-030";
    /** Constante de error que nos indica que el transportista ya existe con el CIF indicado. */
    public static String TRANSPORTISTA_EXISTE_CIF               = "GBE-031";
    /** Constante de error que indica que no existe el transportista. */
    public static String TRANSPORTISTA_NO_EXISTE                = "GBE-032";
    /** Constante de error que nos indica que el transportista ya está desactivado .*/
    public static String TRANSPORTISTA_DESACTIVADO              = "GBE-033";
    /** Constante de error que nos indica que el transportista ya existe con el nombre indicado. */
    public static String TRANSPORTISTA_EXISTE_NOMBRE            = "GBE-034";
    /** Constante de error que indica que se ha producido un error al registrar la factura. */
    public static String BD_KO_CREA_FACTURA                     = "GBE-035";
    /** Constante de error que indica que se ha producido un error al modificar la factura. */
    public static String BD_KO_MODIF_FACTURA                    = "GBE-036";

}
