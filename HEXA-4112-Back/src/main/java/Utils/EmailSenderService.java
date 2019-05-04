/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Utils;

import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author olivi
 */
public class EmailSenderService {
    
    private static final String email = "hexa4112@gmail.com";
    private static final String password = "test_Conciergerie";
    private static final String msg = "Bonjour,\nPour finir votre inscription veuillez saisir le code ci-dessous : \n";
    //sendMail("oliviacaraiman@gmail.com","RegistTest", "TEST SUCCESSFUL");
    private static String generateCode() {
        int leftLimit = '0';
        int rightLimit = '9';
        int targetStringLength = 6;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }
    
    public static String sendVerificationEmail(String sendTo){
        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email,password);
                    }
                });
        
        String code = "";
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(sendTo));
            message.setSubject("Bienvenue sur Campus Exchange");
            code = generateCode();
            message.setText(msg + code);
            //send message
            Transport.send(message);
        } catch (MessagingException e) {throw new RuntimeException(e); }
        
        return code;
    }
}