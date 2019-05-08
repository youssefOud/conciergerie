package actions;

import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Class linking the front and back to rate the bidder of an ad
 * 
 * @author HEXA-4112
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
