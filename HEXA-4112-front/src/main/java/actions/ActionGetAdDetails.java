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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author X
 */
public class ActionGetAdDetails extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        String idAds = request.getParameter("idAnnonce");
        Long idAdsLong = Long.valueOf(idAds);
        
        Services services = new Services();

        Service service = services.getServiceById(idAdsLong);
        
        request.setAttribute("service", service);
    }
    
}
