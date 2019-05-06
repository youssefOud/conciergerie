/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author X
 */
public class ActionRateBidder extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        String reservationId = request.getParameter("reservationId");
        Long reservationIdLong = Long.valueOf(reservationId);
        
        String rating = request.getParameter("rating");
        int ratingInt = Integer.valueOf(rating);
        
        Services services = new Services();
        boolean rated = services.rateReservationByReservationOwner(reservationIdLong, ratingInt);
        
        request.setAttribute("rated", rated);
    }
    
}
