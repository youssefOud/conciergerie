package actions;

import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Class linking the front and back to compute the price of a user's
 * response to an ad
 * 
 * @author HEXA-4112
 */
public class ActionComputePrice extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        
        String idAds = request.getParameter("idAnnonce");
        Long idAdsLong = Long.valueOf(idAds);
          
        String durationString = request.getParameter("duree");
        int duration = Integer.valueOf(durationString);
        
        String durationUnit = request.getParameter("uniteDuree");
        
        Services services = new Services();
        int price = services.calculateReservationPrice(idAdsLong, duration, durationUnit);
        
        request.setAttribute("price", price);
        
    }
    
}
