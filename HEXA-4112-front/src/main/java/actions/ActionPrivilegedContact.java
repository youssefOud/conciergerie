/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import Model.Person;
import Model.Service;
import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author X
 */
public class ActionPrivilegedContact extends Action {
    
    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {

        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        String cellNumber = request.getParameter("numeroTelephone");
        String privilegedContact = request.getParameter("contactPrivilegie");
        
        Services services = new Services();
        boolean saved = services.savePrivilegedContact(idPerson, privilegedContact, cellNumber);
        
        request.setAttribute("saved", saved);
    }
}
