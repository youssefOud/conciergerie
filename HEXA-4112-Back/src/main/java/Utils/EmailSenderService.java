/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Utils;

import Model.Person;
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
 * Class allowing to send an email to users or the moderator
 * 
 * @author HEXA-4112
 */
public class EmailSenderService {
    
    private static final String email = "hexa4112@gmail.com";
    private static final String password = "test_Conciergerie";
    private static final String msg = "Bonjour,\nPour finaliser votre inscription veuillez saisir le code ci-dessous : \n";
    
    /**
     * Generates a code to be sent to the user in the verification email 
     * 
     * @return 
     */
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
    
    /**
     * Sends verification email to the parameter sendTo
     * 
     * @param sendTo
     * @return the code generated in the mail
     */
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
    
    /**
     * Sends an email to confirm the user's demand
     * 
     * @param sendTo
     * @param object
     * @param startingDate
     * @param endingDate
     * @return an empty String (not used)
     */
    public static String sendDemandConfirmationEmail(String sendTo, String object, String startingDate, String endingDate){
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
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(sendTo));
            message.setSubject("Confirmation de demande : "+ object);
            message.setText("Bonjour,\r\n"
                    +       "Votre réponse pour :\r\n"
                    +       object + " du " + startingDate + " au " + endingDate + "\r\n"
                    +       "a bien été confirmée");
            //send message
            Transport.send(message);
        } catch (MessagingException e) {throw new RuntimeException(e); }
        
        return "";
    }
    
    /**
     * Sends an email to confirm the user's offer
     * 
     * @param sendTo
     * @param object
     * @param startingDate
     * @param endingDate
     * @param firstName
     * @param privilegedContact
     * @return an empty String (not used)
     */
    public static String sendOfferConfirmationEmail(String sendTo, String object, String startingDate, String endingDate, String firstName, String privilegedContact){
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
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(sendTo));
            message.setSubject("Confirmation de prêt de : "+ object);
            message.setText("Bonjour,\r\n"
                    +       "Votre prêt de :\r\n"
                    +       object + " du " + startingDate + " au " + endingDate + " à " + firstName + " ( " + privilegedContact + ")" + "\r\n"
                    +       "a bien été confirmé");
            //send message
            Transport.send(message);
        } catch (MessagingException e) {throw new RuntimeException(e); }
        
        return "";
    }
    
    /**
     * Sends an email to confirm the response to a demand from the user
     * 
     * @param sendTo
     * @param object
     * @param startingDate
     * @param endingDate
     * @param firstName
     * @param privilegedContact
     * @param reservationPrice
     * @return an empty String (not used)
     */
    public static String sendDemandReservationEmail(String sendTo, String object, String startingDate, String endingDate, String firstName, String privilegedContact, int reservationPrice){
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
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(sendTo));
            message.setSubject("Résumé de votre réponse à l'annonce pour : "+ object);
            message.setText("Bonjour,\r\n"
                    +       "Vous avez proposé l'offre suivante pour l'annonce de : " + firstName + " ( " + privilegedContact + ") :" + "\r\n"
                    +       object + " du " + startingDate + " au " + endingDate + " pour " + reservationPrice +" points \r\n");
            //send message
            Transport.send(message);
        } catch (MessagingException e) {throw new RuntimeException(e); }
        
        return "";
    }
    
    /**
     * Sends an email to notify the user of a new response to his 
     * or her ad
     * 
     * @param sendTo
     * @param object
     * @param startingDate
     * @param endingDate
     * @param firstName
     * @param privilegedContact
     * @param reservationPrice
     * @return 
     */
    public static String sendOfferReservationEmail(String sendTo, String object, String startingDate, String endingDate, String firstName, String privilegedContact, int reservationPrice){
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
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(sendTo));
            message.setSubject("Nouvelle proposition pour votre offre de : "+ object);
            message.setText("Bonjour,\r\n"
                    +       "Votre offre  de : " + object + " a reçu une nouvelle demande de " + firstName + " ( " + privilegedContact + ") :" + "\r\n"
                    +       object + " du " + startingDate + " au " + endingDate + " pour " + reservationPrice +" points \r\n");
            //send message
            Transport.send(message);
        } catch (MessagingException e) {throw new RuntimeException(e); }
        
        return "";
    }
    
    /**
     * Sends an email to notify the moderator of the alert of an ad
     * 
     * @param idAd the ad that was reported
     * @param person who reports the ad
     * @return true when the mail has been sent
     */
    public static boolean sendEmailModeratorReportAd(Long idAd, Person person){
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
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
            message.setSubject("Annonce signalée par un utilisateur");
            message.setText("L'annonce d'identifiant " + idAd + " a été signalée par l'utilisateur " + person.getFirstName() + " " + person.getLastName() +".");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        
        return true;
    }
}
