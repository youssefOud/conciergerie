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
import javax.servlet.http.HttpSession;

/**
 *
 * @author X
 */
public class ActionReportAd extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        // TODO : envoyer mail au moderateur (moderateur@gmail.com)
        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        String idAd = request.getParameter("idAnnonce");
        Long idAdLong = Long.valueOf(idAd);
                
        Services services = new Services();
        Person person = services.getPersonById(idPerson);
        boolean reported = services.reportAd(person, idAdLong);
        
        request.setAttribute("reported", reported);
    }
    
}
