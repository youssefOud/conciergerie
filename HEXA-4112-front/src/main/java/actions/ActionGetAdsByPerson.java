/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import Model.Person;
import Model.Reservation;
import Model.Service;
import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author X
 */
public class ActionGetAdsByPerson extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        Services services = new Services();
        Person person = services.getPersonById(idPerson);
        HashMap<Service, ArrayList<Reservation>> ads = services.getAdsByIdPerson(person);
        
        request.setAttribute("ads", ads);
    }
    
}
