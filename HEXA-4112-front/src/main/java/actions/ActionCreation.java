package actions;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import Model.Demand;
import Model.Offer;
import Model.Person;
import Services.Services;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.servlet.http.HttpSession;

/**
 * Class linking the front and back the creation of an ad
 * 
 * @author HEXA-4112
 */
public class ActionCreation extends Action {
    
    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        
        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        Services services = new Services();
        Person person = services.getPersonById(idPerson);
        
        String pictures = request.getParameter("pictures");

        String typeAnnonce = request.getParameter("type");
        
        String category = request.getParameter("categorie");
        String nameObject = request.getParameter("objet");
        String description = request.getParameter("description");
        
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
        formatDate.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        Date availabilityDateComplete = formatDate.parse(date + " " + time);
        
        String localisation = request.getParameter("localisation");
        // type indique si c'est un prêt, une donation, un service rendu ...
        // String type = request.getParameter("type");
        String dur = request.getParameter("duree");
        int duration = Integer.valueOf(dur);
        String pts = request.getParameter("nbPts");
        int nbPts = Integer.valueOf(pts);
        String priceUnit = request.getParameter("unitePrix");
        String durationUnit = request.getParameter("uniteDuree");
        
        boolean created = false;

        if (typeAnnonce.equals("demande")) {
            Demand demand = new Demand(person, category, pictures, nameObject, availabilityDateComplete, localisation, ""/*, type*/, nbPts, description, priceUnit, durationUnit, duration);
            created = services.createDemand(demand);
        } else if (typeAnnonce.equals("offre")) {
            Offer offer = new Offer(person, category, pictures, nameObject, availabilityDateComplete, localisation, ""/*, type*/, nbPts, description, priceUnit, durationUnit, duration);

            created = services.createOffer(offer);
        }
        
        request.setAttribute("created", created);
        
    }
}
