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

public class ActionCreation extends Action {
    
    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        
        List<String> pictures = new ArrayList<>();
        /*String picturesArray = request.getParameter("pictures");
        JsonParser jsonParser = new JsonParser();
        JsonArray picturesFromString = jsonParser.parse(picturesArray).getAsJsonArray();
        Iterator it = picturesFromString.iterator();
        while(it.hasNext())
        {
            Object indexPicture = (String) it.next(); // get key
            Object o = picturesFromString.get(indexPicture); // get value
            System.out.println(indexPicture + " : " +  o); // print the key and value
        }*/
        
        // On recupere le parametre du bouton radio pour savoir
        // si c'est une demande ou une offre
        String typeService = request.getParameter("type");
        
        String category = request.getParameter("categorie");
        String nameObject = request.getParameter("objet");
        String description = request.getParameter("description");
        
        String date = request.getParameter("date");
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");  
        Date availabilityDate = formatDate.parse(date);
        
        String time = request.getParameter("time");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");  
        Date availabilityTime = formatTime.parse(time);
        
        // On combine les deux dates ensemble
        Date availabilityDateComplete = new Date(availabilityDate.getTime() + availabilityTime.getTime());
        
        String localisation = request.getParameter("localisation");
        // type indique si c'est un prêt, une donation, un service rendu ...
        // String type = request.getParameter("type");
        String dur = request.getParameter("duree");
        int duration = Integer.valueOf(dur);
        String pts = request.getParameter("nbPts");
        int nbPts = Integer.valueOf(pts);
        String priceUnit = request.getParameter("unitePrix");
        String durationUnit = request.getParameter("uniteDuree");
        
        Services services = new Services();
        
        // TODO : A modifier quand on aura implémenté la connection avec les sessions
        // En attendant, passer l'id du person.
        String idPerson = request.getParameter("idPerson");
        Long idPersonLong = 1L; //Long.valueOf(idPerson);
        Person person = services.getPersonById(idPersonLong);
        // TODO : A modifier quand on connait la valeur du type (demander au front)
        boolean created = false;
        if (typeService.equals("demande")) {
            Demand demand = new Demand(person, category, pictures, nameObject, availabilityDateComplete, localisation, ""/*, type*/, nbPts, description, priceUnit, durationUnit, duration);
            created = services.createDemand(demand);
            System.out.println("Value de la demande : " + demand);
        } else if (typeService.equals("offre")) {
            Offer offer = new Offer(person, category, pictures, nameObject, availabilityDateComplete, localisation, ""/*, type*/, nbPts, description, priceUnit, durationUnit, duration);
            created = services.createOffer(offer);
            System.out.println("Value de la offre : " + offer);
        }
        System.out.println("Value : " + created);
        
        request.setAttribute("created", created);
        
    }
}