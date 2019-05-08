package actions;

import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Class linking the front and back for deleting an ad
 * 
 * @author HEXA-4112
 */
public class ActionDeleteAd extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        
        String idAnnonce  = request.getParameter("idAnnonce");
        Long idAnnonceLong = Long.valueOf(idAnnonce);
        
        Services services = new Services();
        boolean deleted = services.deleteService(idAnnonceLong);
        
        request.setAttribute("deleted", deleted);
    }
    
}
