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
    public static String OPERACION_OK                             = "NSE-000";
    /** Constante de error que nos indica que el empleado tiene sesión activa en otro dispositivo.*/
    public static String EMPLEADO_EN_USO                          = "NSE-001";
    /** Constante de error que indica que se ha producido un error al crear al empleado. */
    public static String BD_KO_CREA_EMPL                          = "NSE-002";
    /** Constante de error que indica que no existe el empleado. */
    public static String EMPL_NO_EXISTE                           = "NSE-003";
    /** Constante de error que indica que el empleado existe con el nombre de usaurio indicado. */
    public static String EMPL_EXISTE_NOMBRE_USUARIO               = "NSE-004";
    /** Constante de error que indica que el empleado existe con el nombre indicado. */
    public static String EMPL_EXISTE_NOMBRE                       = "NSE-005";
    /** Constante de error que indica que se ha producido un error al modificar al empleado. */
    public static String BD_KO_MODIF_EMPL                         = "NSE-006";
    /** Constante de error que indica que se ha producido un error al crear el cliente. */
    public static String BD_KO_CREA_CLIENTE                       = "NSE-007";
    /** Constante de error que indica que se ha producido un error al modificar el cliente. */
    public static String BD_KO_MODIF_CLIENTE                      = "NSE-008";
    /** Constante de error que nos indica que el cliente ya existe con el nombre indicado.*/
    public static String CLIENTE_EXISTE_NOMBRE                    = "NSE-009";
    /** Constante de error que indica que el proveedor existe con el nombre indicado. */
    public static String PROVEEDOR_EXISTE_NOMBRE                  = "NSE-010";
    /** Constante de error que nos indica que se ha producido un error en la creación del proveedor. */
    public static String BD_KO_CREAR_PROVEEDOR                    = "NSE-011";
    /** Constante de error que nos indica que se ha producido un error en la modificación del proveedor. */
    public static String BD_KO_MODIFICAR_PROVEEDOR                = "NSE-012";
    /** Constante de error que nos indica que se ha iniciado sesión en otro dispositivo.*/
    public static String LICENCIA_NO_VALIDA                       = "NSE-013";
    /** Constante de error que nos indica que se ha iniciado sesión en otro dispositivo.*/
    public static String SESION_INVALIDA                          = "NSE-014";
    /** Constante de error que nos indica que se ha producido un error en el envío del mensaje por Telegram.*/
    public static String ERROR_TELEGRAM                           = "NSE-015";
    /** Constante de error que indica que se ha producido un error al crear la compra. */
    public static String BD_KO_CREA_COMPRA                        = "NSE-016";
    /** Constante de error que indica que se ha producido un error al crear la venta. */
    public static String BD_KO_CREA_VENTA                         = "NSE-017";
    /** Constante de error que indica que el empleado ya está desactivado. */
    public static String EMPL_DESACTIVADO                         = "NSE-018";
    /** Constante de error que indica que existe un empleado con el código de acceso indicado. */
    public static String BD_KO_CREAR_EMPLEADO_EXISTE_COD_ACCESO   = "NSE-019";
    /** Constante de error que nos indica que el producto ya existe con el nombre indicado.*/
    public static String PRODUCTO_EXISTE_NOMBRE                   = "NSE-020";
    /** Constante de error que indica que se ha producido un error al crear el producto. */
    public static String BD_KO_CREA_PRODUCTO                      = "NSE-021";
    /** Constante de error que indica que se ha producido un error al modificar el producto. */
    public static String BD_KO_MODIF_PRODUCTO                     = "NSE-022";
    /** Constante de error que indica que el producto ya está desactivado. */
    public static String PRODUCTO_DESACTIVADO                     = "NSE-023";
    /** Constante de error que indica que no existe el producto. */
    public static String PRODUCTO_NO_EXISTE                       = "NSE-024";
    /** Constante de error que indica que la contraseña introducida no es correcta. */
    public static String EMPLADO_CONTRASEÑA_INCORRECTA            = "NSE-025";
    /** Constante de error que indica que el usuario introducido no es correcto. */
    public static String BD_KO_EMPLEADO_NO_EXISTE                 = "NSE-026";
    /** Constante de error que indica que existe un control de PT con el número de pedido indicado. */
    public static String BD_KO_CREAR_CONTROL_PT_EXISTE_NUM_PEDIDO = "NSE-027";
    /** Constante de error que indica que se ha producido un error al crear el control de PT. */
    public static String BD_KO_CREA_CONTROL_PT                    = "NSE-028";
    /** Constante de error que indica que se ha producido un error al modificar el control de PT. */
    public static String BD_KO_MODIF_CONTROL_PT                   = "NSE-029";
    /** Constante de error que indica que no existe el control de PT. */
    public static String CONTROL_PT_NO_EXISTE                     = "NSE-030";
    /** Constante de error que indica que se ha producido un error al crear la imagen del control de PT. */
    public static String BD_KO_CREA_FOTO_CONTROL_PT               = "NSE-031";
    /** Constante de error que indica que se ha producido un error al crear el pesaje de caja del control de PT. */
    public static String BD_KO_CREA_CONTROL_PT_PESAJE_CAJA        = "NSE-032";
    /** Constante de error que indica que se ha producido un error al crear el pesaje de confección del control de PT. */
    public static String BD_KO_CREA_CONTROL_PT_PESAJE_CONFECCION  = "NSE-033";
    /** Constante de error que indica que se ha producido un error al crear el pesaje de calibre del control de PT. */
    public static String BD_KO_CREA_CONTROL_PT_PESAJE_CALIBRE     = "NSE-034";
    /** Constante de error que indica que se ha producido un error al crear el pesaje de penetromía del control de PT. */
    public static String BD_KO_CREA_CONTROL_PT_PESAJE_PENETROMIA  = "NSE-035";
    /** Constante de error que indica que se ha producido un error al crear el pesaje de BRIX del control de PT. */
    public static String BD_KO_CREA_CONTROL_PT_PESAJE_BRIX        = "NSE-036";
    /** Constante que nos indica que el lote indicado no tiene ventas. */
    public static String NO_VENTAS_LOTE                           = "NSE-037";
}
