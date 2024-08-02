/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils;

import com.dina.genasoft.exception.GenasoftException;
import com.vaadin.spring.annotation.SpringComponent;

/**
 * @author Daniel Carmona Romero
 * Clase que se encarga de enviar mensajes via Telegram.
 */
@SpringComponent
public class WhatsApp {
    /** El valor del Instance.*/
    //@Value("${app.property.telegram.instance}")
    private String INSTANCE_ID   = "22";
    /** El valor del client ID.*/
    //@Value("${app.property.telegram.client.id}")
    private String CLIENT_ID     = "daniel.carmona@brostel.es";
    /** El valor del client secret.*/
    //@Value("${app.property.telegram.client.secret}")
    private String CLIENT_SECRET = "fd4d6304d2a44eb1b75171182a0fd7d1";
    /** El valor del client secret.*/
    //@Value("${app.property.telegram.gateway}")
    private String GATEWAY_URL   = "http://api.whatsmate.net/v3/whatsapp/single/text/message/";

    /**
     * Entry Point
     */
    /**
    public static void main(String[] args) throws Exceptxion {
        String number = "+34625436572";
        String message = "Hola Dani, ¿Cómo estás?";
        
        INSTANCE_ID   = "0";        
        CLIENT_ID     = "daniel.carmona@TRAZABILIDADES[Natural TROPIC].es";
        CLIENT_SECRET = "fd4d6304d2a44eb1b75171182a0fd7d1";
        GATEWAY_URL   = "http://api.whatsmate.net/v1/telegram/single/message/" + INSTANCE_ID;
        Telegram.enviarMensaje(number, message);    
    }
    */
    /**
     * Método que nos envía un mensaje via Telegram
     * @param number El número de teléfono dónde se va a enviar la notificación (incluido el código del país) Ej. España +34
     * @param message El mensaje que se va a enviar.
     * @throws GenasoftException Si se produce alguna excepcion.
     */
    public void enviarMensaje(String number, String message) throws GenasoftException {
        /** try {
        
            String jsonPayload = new StringBuilder().append("{").append("\"number\":\"").append(number).append("\",").append("\"message\":\"").append(message).append("\"").append("}").toString();
        
            URL url = new URL(GATEWAY_URL + INSTANCE_ID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-WM-CLIENT-ID", CLIENT_ID);
            conn.setRequestProperty("X-WM-CLIENT-SECRET", CLIENT_SECRET);
            conn.setRequestProperty("Content-Type", "application/json");
        
            OutputStream os = conn.getOutputStream();
            os.write(jsonPayload.getBytes());
            os.flush();
            os.close();
        
            int statusCode = conn.getResponseCode();
            System.out.println("Response from WA Gateway: \n");
            System.out.println("Status Code: " + statusCode);
            BufferedReader br = new BufferedReader(new InputStreamReader((statusCode == 200) ? conn.getInputStream() : conn.getErrorStream()));
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();
        
        } catch (Exception e) {
            e.printStackTrace();
            throw new GenasoftException(Constants.ERROR_TELEGRAM);
        }
        */
    }
}
