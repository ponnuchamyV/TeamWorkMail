package com.sam.sidTask.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MailUtil {

    private static final String PROPS = "/application.properties";
    private static Properties mailProps = new Properties();
    private static String username;
    private static String password;
    private static String from;

    static {
        try (InputStream in = MailUtil.class.getResourceAsStream(PROPS)) {
            Properties p = new Properties();
            p.load(in);
            // copy mail properties
            mailProps.put("mail.smtp.auth", p.getProperty("mail.smtp.auth", "true"));
            mailProps.put("mail.smtp.starttls.enable", p.getProperty("mail.smtp.starttls.enable", "true"));
            mailProps.put("mail.smtp.host", p.getProperty("mail.smtp.host"));
            mailProps.put("mail.smtp.port", p.getProperty("mail.smtp.port"));
            username = p.getProperty("mail.username");
            password = p.getProperty("mail.password");
            from = p.getProperty("mail.from", username);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load mail config: " + e.getMessage());
        }
    }

    public static void sendEmail(String to, String subject, String body) throws MessagingException {
        Session session = Session.getInstance(mailProps, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
