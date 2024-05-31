/**
 * Genasoft - Daniel Carmona 47637680B.
 *  
 *  Copyright (C) 2024
 */
package com.dina.genasoft.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Daniel Carmona Romero
 * Clase útil para enviar correos.
 */
@Component
public class EnvioCorreo {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    public EnvioCorreo(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /** El log de la aplicación*/
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EnvioCorreo.class);

    /**
     * Método que se encarga de enviar correos
     * @param email Dirección de correo de destino, puede estar separada por ";"
     * @param titulo El titulo del correo
     * @param cuerpo El cuerpo del correo
     * @throws MailException Si se produce un error en el envío del correo
     * @throws InterruptedException Si se produce una interrupción en el envío de correo.
     */
    @Async
    public void enviarCorreo(String email, String titulo, String cuerpo, String adjunto) throws MailException, InterruptedException {

        // NOTIFICACIONES TRAZABILIDADES[Natural TROPIC]
        System.setProperty("spring.mail.host", "smtp.gmail.com");
        System.setProperty("spring.mail.username", "dcarmonanotificaciones@gmail.com");
        System.setProperty("spring.mail.password", "N0t1f1c4c10n3s");
        System.setProperty("mail.smtp.starttls.enable", "true");
        System.setProperty("spring.mail.port", "465");

        String[] correos = email.split(";");

        for (int i = 0; i < correos.length; i++) {
            log.error("ENVANDO CORREO: " + correos[i]);
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setTo("daniel86cr@gmail.com");
            mail.setFrom("dcarmonanotificaciones@gmail.com");
            mail.setSubject(titulo);
            mail.setText(cuerpo);
            MimeMessage message = javaMailSender.createMimeMessage();
            try {

                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setFrom(mail.getFrom());
                helper.setTo(mail.getTo());
                helper.setSubject(mail.getSubject());
                helper.setText(mail.getText());

                if (adjunto != null) {
                    FileSystemResource file = new FileSystemResource(adjunto);
                    helper.addAttachment(file.getFilename(), file);
                }

                javaMailSender.send(message);
                //log.error("ENVÍO OK ");
            } catch (MessagingException e) {
                log.error("ERROR AL ENVIAR EL CORREO: ");
                e.printStackTrace();
                throw new MailParseException(e);
            } catch (Exception e) {
                log.error("ERROR AL ENVIAR EL CORREO: ");
                e.printStackTrace();
            }
        }
    }

    /**
     * Método que se encarga de enviar correos.
     * @param email Dirección de correo de destino, puede estar separada por ";"
     * @param titulo El titulo del correo.
     * @param cuerpo El cuerpo del correo.
     * @param adjunto El fichero que se adjunta al correo.
     * @throws MailException Si se produce un error en el envío del correo.
     * @throws InterruptedException Si se produce una interrupción en el envío de correo.
     */
    @Async
    public void enviarCorreo2(String email, String titulo, String cuerpo, String adjunto) throws MailException, InterruptedException {

        String[] correos = email.split(";");

        // NOTIFICACIONES TRAZABILIDADES[Natural TROPIC]
        System.setProperty("spring.mail.host", "smtp.gmail.com");
        System.setProperty("spring.mail.username", "daniel86cr@gmail.com");
        System.setProperty("spring.mail.password", "C4lcul4d0r41?");
        System.setProperty("mail.smtp.starttls.enable", "true");
        System.setProperty("spring.mail.port", "587");

        // NI PUTA IDEA
        //System.setProperty("spring.mail.smtp.ssl.trust", "*");
        //System.setProperty("spring.mail.properties.mail.smtp.starttls.enable", "true");
        //System.setProperty("spring.mail.host", "rs11.websitehostserver.net");
        //System.setProperty("spring.mail.username", "albaran@glamour-fresh.com");
        //System.setProperty("spring.mail.password", "4CAEZCjokv");
        //System.setProperty("spring.mail.port", "465");
        //System.setProperty("spring.mail.smtp.ssl.trust", "*");
        //System.setProperty("spring.mail.properties.mail.smtp.starttls.enable", "true");
        //System.setProperty("spring.mail.properties.mail.smtp.starttls.enable", "true");

        for (int i = 0; i < correos.length; i++) {

            log.error("ENVANDO CORREO: " + correos[i]);
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setTo(correos[i]);
            //mail.setTo("daniel.carmona@TRAZABILIDADES[Natural TROPIC].es");
            mail.setFrom("daniel86cr@gmail.com");
            mail.setSubject(titulo);
            mail.setText(cuerpo);

            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                /**
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                
                helper.setFrom(mail.getFrom());
                helper.setTo(mail.getTo());
                helper.setSubject(mail.getSubject());
                helper.setText(mail.getText());
                
                if (adjunto != null) {
                    FileSystemResource file = new FileSystemResource(adjunto);
                    helper.addAttachment(file.getFilename(), file);
                }
                
                javaMailSender.send(message);
                log.error("ENVÍO OK ");
                */

                MimeMultipart mp = new MimeMultipart();
                MimeBodyPart mbp1 = new MimeBodyPart();

                //helper.setText(mail.getText());

                //String htmlText = "<b> This is formatted</b>" + "<font size =\"5\" face=\"arial\" >This paragraph is in Arial, size 5</font>";
                mbp1.setContent(cuerpo, "text/html");
                mp.addBodyPart(mbp1);
                message.setContent(mp);

                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setFrom(mail.getFrom());
                helper.setTo(mail.getTo());
                helper.setSubject(mail.getSubject());
                helper.setText("", cuerpo);

                if (adjunto != null) {
                    FileSystemResource file = new FileSystemResource(adjunto);
                    helper.addAttachment(file.getFilename(), file);
                }

                javaMailSender.send(message);
                log.error("ENVÍO OK ");

            } catch (MessagingException e) {
                log.error("ERROR AL ENVIAR EL CORREO: ");
                e.printStackTrace();
                throw new MailParseException(e);
            } catch (Exception e) {
                log.error("ERROR AL ENVIAR EL CORREO: ");
                e.printStackTrace();
            }
        }

    }
}
