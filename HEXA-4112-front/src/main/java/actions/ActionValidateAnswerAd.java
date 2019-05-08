package actions;

import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javafx.util.Pair;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Class linking the front and back to validate the response to one 
 * of the user's ads
 * 
 * @author HEXA-4112
 */
public class ActionValidateAnswerAd extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        String idReservation = request.getParameter("idReservation");
        Long idReservationLong = Long.valueOf(idReservation);
        
        Services services = new Services();
        Map.Entry confirmReservation = services.confirmReservation(idReservationLong);
        
        request.setAttribute("confirmed", confirmReservation.getKey());
        request.setAttribute("message", confirmReservation.getValue());
    }
    
}
