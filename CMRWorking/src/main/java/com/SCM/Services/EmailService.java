package com.SCM.Services;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    public boolean sendEmail(String subj, String msg, String to){

        boolean f = false;

        String from = "shubhamgadhave987@gmail.com";

        final String password = "wkywdnsnnkprkptw";

        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        try {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        session.setDebug(true);

        MimeMessage m = new MimeMessage(session);


            m.setFrom(from);

            m.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

            m.setSubject(subj);

            m.setContent(msg,"text/html");

            Transport.send(m);

            System.out.println("Email sent!!!!!");

            f = true;

        }catch (Exception e){
            e.printStackTrace();
        }

        return f;
    }
    
}
