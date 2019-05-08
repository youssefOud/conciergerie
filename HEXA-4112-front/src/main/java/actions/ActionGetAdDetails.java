package actions;

import Model.Service;
import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class linking the front and back to get the details of an ad
 * 
 * @author HEXA-4112
 */
public class ActionGetAdDetails extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        
        String idAds = request.getParameter("idAnnonce");
        Long idAdsLong = Long.valueOf(idAds);
        
        Services services = new Services();

        Service service = services.getServiceById(idAdsLong);
        
        request.setAttribute("service", service);
    }
    
}
