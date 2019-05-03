/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

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
public class ActionShowTimeline extends Action {
    
    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        
        String objectName = request.getParameter("objet");
        String priceUnit = request.getParameter("uniteePrix"); 
        String category = request.getParameter("categorie");
        String location = request.getParameter("localisation");
        String availabilityDate = request.getParameter("date");
        String availabilityTime = request.getParameter("time");
        String duration = request.getParameter("duree");
        String durationUnit = request.getParameter("uniteDuree");
        String nbPts = request.getParameter("nbPts");
        String typeService = request.getParameter("type");
        
        Services services = new Services(); 
        List<Service> listOfServices = services.findAllServicesWithFilter(objectName, category, location,availabilityDate,
                availabilityTime, duration, durationUnit, nbPts, priceUnit, typeService);
        
        request.setAttribute("listOfServices", listOfServices);
    }
}
