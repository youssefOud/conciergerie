package actions;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import Model.Demand;
import Model.Offer;
import Model.Person;
import Services.Services;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;

public class ActionCreation extends Action {
    
    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        
        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        Services services = new Services();
        Person person = services.getPersonById(idPerson);
        
        String pictures = request.getParameter("pictures");

        // On recupere le parametre du bouton radio pour savoir
        // si c'est une demande ou une offre
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
            System.out.println("Value de la demande : " + demand);
        } else if (typeAnnonce.equals("offre")) {
            Offer offer = new Offer(person, category, pictures, nameObject, availabilityDateComplete, localisation, ""/*, type*/, nbPts, description, priceUnit, durationUnit, duration);

            created = services.createOffer(offer);
            System.out.println("Value de l'offre : " + offer);
        }
        System.out.println("Value : " + created);
        
        request.setAttribute("created", created);
        
    }
}
