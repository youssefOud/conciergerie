package actions;

import Model.Person;
import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class linking the front and back to report an ad
 * 
 * @author HEXA-4112
 */
public class ActionReportAd extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        
        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        String idAd = request.getParameter("idAnnonce");
        Long idAdLong = Long.valueOf(idAd);
                
        Services services = new Services();
        Person person = services.getPersonById(idPerson);
        boolean reported = services.reportAd(person, idAdLong);
        
        request.setAttribute("reported", reported);
    }
    
}
