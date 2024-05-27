/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.dina.genasoft.exception.GenasoftException;

/**
 * Clase donde se definiran las utilidades comunes del applicativo.<br>
 * Funciones como por ejemplo generar fecha nueva, conversion de objetos, etc. <br>
 *
 */
public final class Utils {

    private static List<Integer> lNumeros;

    /**
     * Constructor vacio.
     */
    public Utils() {
    }

    /**
     * Metodo que nos genera una nueva fecha(now)
     * @return Nueva fecha.
     */
    public static Date generarFecha() {
        return new Date();
    }

    /**
     * Metodo que nos genera una lista genetica.
     * @return
     */
    public static <T> List<T> generarListaGenerica() {
        return new ArrayList<T>();
    }

    /**
     * Metodo que nos genera un nuevo map.
     * @return El nuevo map generado de tipo <String, Object>.
     */
    public static Map<String, Object> generarMap() {
        return new HashMap<String, Object>();
    }

    /**
     * Metodo que nos retorna el valor en boleano del valor entero.
     * @param bolInteger true si el valor es diferente de 0, en caso contrario false.
     */
    public static boolean booleanFromInt(int bolInteger) {
        return bolInteger == 1;
    }

    /**
     * Metodo que nos retorna el valor en boleano del valor entero.
     * @param bolInteger true si el valor es diferente de 0 y unulo, en caso contrario false.
     */
    public static boolean booleanFromInteger(Integer bolInteger) {
        return bolInteger != null && bolInteger.equals(1);
    }

    /**
     * Metodo que nos retorna el valor numerico de un boleano
     * @param value El valor boleano.
     * @return 1 si es true, 0 si es false.
     */
    public static int intFromBoolean(boolean value) {
        return (value == false) ? 0 : 1;
    }

    /**
     * Este metodo nos retorna el valor en boleano del string. 
     * @param value El valor a convertir
     * @return Si string es true (obviando mayusculas) retornara true, en caso contrario retornara false.
     */
    public static boolean booleanFromString(String value) {
        boolean result = false;
        if (value != null && !value.isEmpty()) {
            result = value.toLowerCase().equals("true");
        }
        return result;
    }

    /**
     * Este metodo nos retorna el valor en String del booleano. 
     * @param value El valor a convertir
     * @return Si true retornara SI, en caso contrario retornara NO.
     */
    public static String stringFromBoolean(boolean value) {
        String result = "NO";
        if (value) {
            result = "SI";
        }
        return result;
    }

    /**
     * Método que nos calcula la diferencia en minutos entre dos fechas.
     * @param t1 La fecha 1 (mayor)
     * @param t2 La fecha 2 (menor)
     * @return La diferencia entre t1 y t2
     */
    public static long diferenciaEntreFechasEnMinutos(long t1, long t2) {
        long diffInMillis;

        diffInMillis = t1 - t2;

        diffInMillis = Math.abs(diffInMillis / (60 * 1000) % 60);

        return diffInMillis;
    }

    /**
     * Método que nos calcula la diferencia en horas entre dos fechas.
     * @param t1 La fecha 1 (mayor)
     * @param t2 La fecha 2 (menor)
     * @return La diferencia entre t1 y t2
     */
    public static long diferenciaEntreFechasEnHoras(long t1, long t2) {
        long diffInMillis;

        diffInMillis = t1 - t2;

        diffInMillis = Math.abs((diffInMillis / (60 * 60 * 1000)));

        return diffInMillis;
    }

    /**
     * Método que nos calcula la diferencia en horas entre dos fechas.
     * @param t1 La fecha 1 (mayor)
     * @param t2 La fecha 2 (menor)
     * @return La diferencia entre t1 y t2
     */
    public static long diferenciaEntreFechasEnDias(long t1, long t2) {
        long diffInMillis;

        diffInMillis = t1 - t2;

        diffInMillis = (int) ((diffInMillis / (1000 * 60 * 60 * 24)));

        return diffInMillis;
    }

    /**
     * Método que nos retorna la diferencia en días entre d1 y d2.
     * @param d1 La fecha 1
     * @param d2 La fecha 2
     * @return El resultado de restar d1 - d2 en días.
     */
    public static long obtenerDiferenciaEntreFechas(Date d1, Date d2) {
        long l1, l2, diff;
        l1 = d1.getTime();
        l2 = d2.getTime();
        diff = l1 - l2;
        // Calculamos la diferencia en días.
        return diff / (24 * 60 * 60 * 1000);
    }

    /**
     * Método que nos retorna en formato legible el número pasado por parámetro.
     * @param numero El número a formatear
     * @return El número formateado.
     * Ejemplo: <br>
     * numero = 1000
     * resultado 1.000
     */
    public static String obtenerNumeroLegible(int numero) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(numero);
    }

    /**
     * Método que nos rertorna la parte decimal del número pasado por parámetro.
     * @param value El valor a consultar la parte decimal.
     * @return La parte decimal.
     */
    public static Double obtenerParteDecimal(double value) {
        long entero = (long) value;
        Double result = value - entero;
        return result;
    }

    /**
     * Métod que nos hace el redondeo de decimales que nos pasan por parámetro.
     * @param decimales Los decimales a redondear.
     * @param valor El valor a redondear
     * @return El valor redondeado con 'decimales'
     * @throws BrosException Si el número de decimales no es correcto (menor de cero.).
     */
    public static Double redondeoDecimales(int decimales, double valor) {

        //BigDecimal bd = new BigDecimal(valor);
        //bd = bd.setScale(decimales, RoundingMode.HALF_UP);

        /**   Double aux = valor;
        if (valor < Double.valueOf(0)) {
            valor = -valor;
        }
        //Double res = Math.round(valor * 10000.0) / 10000.0;
        //res = Math.round(res * 1000.0) / 1000.0;
        Double res = Math.round(valor * 100.0) / 100.0;
        res = Math.round(res * 100.0) / 100.0;
        if (aux < Double.valueOf(0)) {
            res = -res;
        }
        */
        if (Utils.obtenerNumeroDecimales(valor) > 2) {
            Double aux = valor;
            if (valor < Double.valueOf(0)) {
                valor = -valor;
            }
            //Double res = Math.round(valor * 10000.0) / 10000.0;
            //res = Math.round(res * 1000.0) / 1000.0;
            Double res = Math.round(valor * 10000.0) / 10000.0;
            res = Math.round(res * 1000.0) / 1000.0;
            //res = Math.round(valor * 100.0) / 100.0;
            if (aux < Double.valueOf(0)) {
                res = -res;
            }
            if (Utils.obtenerNumeroDecimales(res) > 2) {
                BigDecimal aux2 = new BigDecimal(res).setScale(decimales, RoundingMode.HALF_UP);
                return aux2.doubleValue();
            } else {
                return res;
            }
        } else {
            return valor;
        }

    }

    /**
     * Métod que nos hace el redondeo de decimales que nos pasan por parámetro.
     * @param decimales Los decimales a redondear.
     * @param valor El valor a redondear
     * @return El valor redondeado con 'decimales'
     * @throws GenasoftException Si el número de decimales no es correcto (menor de cero.).
     */
    public static double redondeo4Decimales(double valor) throws GenasoftException {
        //BigDecimal bd = new BigDecimal(valor);
        //bd = bd.setScale(decimales, RoundingMode.HALF_UP);

        Double aux = valor;
        if (valor < Double.valueOf(0)) {
            valor = -valor;
        }
        Double res = Math.round(valor * 10000.0) / 10000.0;
        res = Math.round(res * 1000.0) / 1000.0;
        res = Math.round(valor * 100.0) / 100.0;
        if (aux < Double.valueOf(0)) {
            res = -res;
        }
        return res;
    }

    public static int obtenerNumeroDecimales(Double valor) {
        if (valor == null || valor.equals(Double.valueOf(0))) {
            return 1;
        }
        String text = Double.toString(Math.abs(valor));
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;
        if (decimalPlaces == 1) {
            decimalPlaces = 2;
        }
        return decimalPlaces;
    }

    /**
     * Método que nos formatea el valor pasado por parámetro,
     * @param valor El valor a formatear 
     * @return El valor formateado.
     */
    public static String formatearValorNumericoString(String valor) {
        return valor.replace(".", "").replace(";", "").replace("-", "").replace("_", "").replace(" ", "").replace(",", "");
    }

    public static Double formatearValorDouble(String value) {
        if (value == null || value.isEmpty()) {
            value = "0";
        } else {
            if (value.contains(".")) {
                value = value.replaceAll("\\.", "");
            }
            if (value.contains(",")) {
                value = value.replaceAll(",", ".");
            }
        }

        return Double.valueOf(value);
    }

    public static Double formatearValorDouble2(String value) {
        if (value == null || value.isEmpty()) {
            value = "0";
        } else {
            if (value.contains(".")) {
                value = value.replaceAll("\\,", "");
            }
            if (value.contains(",")) {
                value = value.replaceAll(",", ".");
            }
        }

        return Double.valueOf(value);
    }

    /**
     * Método que nos retorna el primer día del mes en curso
     * @return Por ejemplo, si hoy es 18/12/20 nos retorna 01/12/20.
     */
    public static Date obtenerPrimerDiaMesEnCurso() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * Método que nos retorna el primer día del mes en curso
     * @return Por ejemplo, si hoy es 18/12/20 nos retorna 01/12/20.
     */
    public static Date obtenerPrimerDiaYearEnCurso() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.DAY_OF_YEAR));
        return cal.getTime();
    }

    /**
     * Método que nos retorna el primer día del mes en curso
     * @return Por ejemplo, si hoy es 18/12/20 nos retorna 01/12/20.
     */
    public static Date obtenerUltimoDiaMesEnCurso() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * Método que nos retorna el primer día del mes en curso
     * @return Por ejemplo, si hoy es 18/12/20 nos retorna 01/12/20.
     */
    public static Date obtenerUltimoDiaYearEnCurso() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        return cal.getTime();
    }

    /**
     * Método que nos valida si el texto pasado por parámetro es una dirección de correo electrónico válida.
     * @param email El texto a validar
     * @return true si es válido, false en caso contrario
     */
    public static Boolean comprobarDireccionCorreo(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }

    /**
     * Método que nos valida si el texto pasado por parámetro es un DNI correcto.
     * @param dni El texto a validar
     * @return true si es válido, false en caso contrario.
     */
    public static Boolean comprobarDNI(String dni) {
        String letraMayuscula = ""; //Guardaremos la letra introducida en formato mayúscula

        // Aquí excluimos cadenas distintas a 9 caracteres que debe tener un dni y también si el último caracter no es una letra
        if (dni.length() != 9 || Character.isLetter(dni.charAt(8)) == false) {
            return false;
        }

        // Al superar la primera restricción, la letra la pasamos a mayúscula
        letraMayuscula = (dni.substring(8)).toUpperCase();

        int miDNI = Integer.parseInt(dni.substring(0, 8));
        int resto = 0;
        String miLetra = "";
        String[] asignacionLetra = { "T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E" };

        resto = miDNI % 23;

        miLetra = asignacionLetra[resto];

        // Por último validamos que sólo tengo 8 dígitos entre los 8 primeros caracteres y que la letra introducida es igual a la de la ecuación
        // Llamamos a los métodos privados de la clase soloNumeros() y letraDNI()
        if (soloNumeros(dni) == true && miLetra.equals(letraMayuscula)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Método que nos valida si el texto pasado por parámetro es un CIF correcto.
     * @param cif El texto a validar
     * @return true si es válido, false en caso contrario.
     */
    public static Boolean comprobarCIF(String cif) {
        if (cif != null) {
            String aux = cif.substring(0, 8);
            aux = calculaCif(aux);

            return cif.equals(aux);
        } else {
            return false;
        }

    }

    public static Boolean soloNumeros(String texto) {
        int i, j = 0;
        String numero = ""; // Es el número que se comprueba uno a uno por si hay alguna letra entre los 8 primeros dígitos
        String miDNI = ""; // Guardamos en una cadena los números para después calcular la letra
        String[] unoNueve = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

        for (i = 0; i < texto.length() - 1; i++) {
            numero = texto.substring(i, i + 1);

            for (j = 0; j < unoNueve.length; j++) {
                if (numero.equals(unoNueve[j])) {
                    miDNI += unoNueve[j];
                }
            }
        }

        if (miDNI.length() != 8) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Método que nos convierte un valor Integer a String a pelo
     * @param valor El valor a convertir
     * @return El valor en formato Strong
     * <li> Si @param es nulo retorna "". </li>
     * <li> Si @param es valor numérico, p.e 1 retorna "1". </li>
     */
    public static String convertirIntegerStringVista(Object valor) {
        String result = "";
        if (valor != null) {
            result = "" + valor;
            result = result.trim().toUpperCase();
        }
        return result;
    }

    /**
     * Método que nos convierte un valor Integer a String a pelo
     * @param valor El valor a convertir
     * @return El valor en formato Strong
     * <li> Si @param es nulo retorna "". </li>
     * <li> Si @param es valor numérico, p.e 1 retorna "1". </li>
     */
    public static String convertirIntegerBooleanStringVista(Integer valor) {
        String result = "No";
        if (valor != null) {
            result = valor.equals(1) ? "Sí" : "No";
        }
        return result;
    }

    private static String calculaCif(String cif) {
        return cif + calculaDigitoControl(cif);
    }

    /**
     * Método que nos calcula el dígito de control del CIF
     * @param cif El cif para comprobar el dígito de control
     * @return El dígito de control.
     */
    private static String calculaDigitoControl(String cif) {
        String str = cif.substring(1, 8);
        String cabecera = cif.substring(0, 1);
        String digitoControlCif = "JABCDEFGHI";
        String cifLetra = "KPQRSNW";
        int sumaPar = 0;
        int sumaImpar = 0;
        int sumaTotal;

        for (int i = 1; i < str.length(); i += 2) {
            int aux = Integer.parseInt("" + str.charAt(i));
            sumaPar += aux;
        }

        for (int i = 0; i < str.length(); i += 2) {
            sumaImpar += posicionImpar("" + str.charAt(i));
        }

        sumaTotal = sumaPar + sumaImpar;
        sumaTotal = 10 - (sumaTotal % 10);

        if (sumaTotal == 10) {
            sumaTotal = 0;
        }

        if (cifLetra.contains(cabecera)) {
            str = "" + digitoControlCif.charAt(sumaTotal);
        } else {
            str = "" + sumaTotal;
        }

        return str;
    }

    private static int posicionImpar(String str) {
        int aux = Integer.parseInt(str);
        aux = aux * 2;
        aux = (aux / 10) + (aux % 10);

        return aux;
    }

    public static List<Integer> obtenerListaNumeros() {
        if (lNumeros == null) {
            lNumeros = Utils.generarListaGenerica();
            for (int i = 0; i < 10000; i++) {
                lNumeros.add(i);
            }
        }
        return lNumeros;
    }

}