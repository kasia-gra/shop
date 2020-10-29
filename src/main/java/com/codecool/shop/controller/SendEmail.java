package com.codecool.shop.controller;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {

    public static void sendEmail(String to, String messageBodyHtml) {


        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("coolshopforcoders@gmail.com", "yzvrzbrmxuafurcz");
            }
        });

        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("coolshopforcoders@gmail.com"));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            MimeBodyPart wrap = new MimeBodyPart();

            String messageBody = "test";

            //Text
            MimeMultipart cover = new MimeMultipart("alternative");
            BodyPart textPart = new MimeBodyPart();
            textPart.setContent(messageBody, "text/plain; charset=utf-8");
            textPart.setDisposition(Part.INLINE);
            cover.addBodyPart(textPart);

            //HTML
            BodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(messageBodyHtml, "text/html; charset=utf-8");
            htmlPart.setDisposition(Part.INLINE);
            cover.addBodyPart(htmlPart);

            wrap.setContent(cover);

            MimeMultipart content = new MimeMultipart("related");
            message.setContent(content);
            content.addBodyPart(wrap);

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
//            message.setText("This is actual message");


            // Send message
            Transport.send(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

}