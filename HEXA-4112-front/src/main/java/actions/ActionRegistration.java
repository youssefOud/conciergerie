/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import Model.Person;
import Services.Services;
import Utils.EmailSenderService;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author X
 */
public class ActionRegistration extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        String name = request.getParameter("nom");
        String firstName = request.getParameter("prenom");
        String password = request.getParameter("motDePasse");
        String mail = request.getParameter("mail");
        String cellNumber = request.getParameter("numeroTel");
        
        EmailSenderService ems = new EmailSenderService();
        Services services = new Services();
        // TODO : Verifier en envoyant un mail
        boolean verifiedIdentity = ems.sendVerificationEmail(mail);
        
        if (verifiedIdentity) {
            Person person = services.inscription(name, firstName, password, mail, cellNumber);
            // TODO : modifier avec les infos que veulent les filles : directement dans la classe sérialisation
            request.setAttribute("registered", true);
        } else {
            request.setAttribute("registered", false);
        }
    }
    
    
}
