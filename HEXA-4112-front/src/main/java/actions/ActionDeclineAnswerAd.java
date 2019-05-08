package actions;

import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Class linking the front and back for the declination of a
 * response to one of its ads
 * 
 * @author HEXA-4112
 */
public class ActionDeclineAnswerAd extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        
        String idReservation = request.getParameter("idReservation");
        Long idReservationLong = Long.valueOf(idReservation);
        
        Services services = new Services();
        boolean declineReservation = services.declineReservation(idReservationLong);
        
        request.setAttribute("declined", declineReservation);
    }
    
}
