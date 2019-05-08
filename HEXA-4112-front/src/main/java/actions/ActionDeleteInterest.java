package actions;

import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Class linking the front and back for deleting an interest
 * 
 * @author HEXA-4112
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
