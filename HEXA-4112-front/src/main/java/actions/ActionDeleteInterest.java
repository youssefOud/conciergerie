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
public class ActionDeleteInterest extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        String idReservation  = request.getParameter("idReservation");
        Long idReservationLong = Long.valueOf(idReservation);

        Services services = new Services();
        boolean deleted = services.deleteInterest(idReservationLong);

        request.setAttribute("deleted", deleted);
    }
    
}
