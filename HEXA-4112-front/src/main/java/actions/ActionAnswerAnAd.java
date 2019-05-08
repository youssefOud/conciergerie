package actions;

import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javafx.util.Pair;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class linking the front and back to respond to an ad
 * 
 * @author HEXA-4112
 */
public class ActionAnswerAnAd extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        String idAds = request.getParameter("idAnnonce");
        Long idAdsLong = Long.valueOf(idAds);
        String durationString = request.getParameter("duree");
        int duration = Integer.valueOf(durationString);
        String durationUnit = request.getParameter("uniteDuree");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String pictures = request.getParameter("pictures");
        String description = request.getParameter("description");
        String location = request.getParameter("localisation");
        
        Services services = new Services();

        Pair<Boolean,String> created = services.createReservation(idPerson, idAdsLong, date, time, duration, durationUnit,pictures,description,location);
        
        request.setAttribute("created", created.getKey());
        request.setAttribute("message", created.getValue());
        
    }
    
}
