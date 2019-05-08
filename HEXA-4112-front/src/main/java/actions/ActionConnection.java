/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import Model.Person;
import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Class linking the front and back to connect the user
 * 
 * @author HEXA-4112
 */
public class ActionConnection extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        String mail = request.getParameter("mail");
        String password = request.getParameter("motDePasse");
        
        Services services = new Services();
        Person person = services.connectPerson(mail, password);
        
        request.setAttribute("person", person);
    }
    
}
