/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author X
 */
public class ActionAnswerAnAd extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        String idAds = request.getParameter("idAnnonce");
        Long idAdsLong = Long.valueOf(idAds);
        String durationString = request.getParameter("duree");
        int duration = Integer.valueOf(durationString);
        String durationUnit = request.getParameter("uniteDuree");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String pictures = request.getParameter("pictures");
        String description = request.getParameter("description");
        String location = request.getParameter("localisation");
        
        Services services = new Services();

        Map.Entry created = services.createReservation(idPerson, idAdsLong, date, time, duration, durationUnit);
        
        request.setAttribute("created", created.getKey());
        request.setAttribute("message", created.getValue());
        
    }
    
}
