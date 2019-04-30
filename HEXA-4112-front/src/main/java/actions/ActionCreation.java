package actions;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import Model.Demand;
import Model.Offer;
import Model.Person;
import Services.Services;

public class ActionCreation extends Action {
    
    //@Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        
        // On recupere le parametre du bouton radio pour savoir
        // si c'est une demande ou une offre
        String typeService = request.getParameter("type");
        
        // TODO : Modifier le nom des param et eventuellement le type
        // quand on aura les specs côté front
        String category = request.getParameter("category");
        String nameObject = request.getParameter("objet");
        String description = request.getParameter("description");
        // Pour date voir si on met une date ou un string
        // Pareil pour la durée
        String availabilityDate = request.getParameter("date");
        String availabilityTime = request.getParameter("time");
        String localisation = request.getParameter("localisation");
        // type indique si c'est un prêt, une donation, un service rendu ...
        //String type = request.getParameter("type");
        String dur = request.getParameter("duree");
        int duration = Integer.valueOf(dur);
        String pts = request.getParameter("nbPts");
        int nbPts = Integer.valueOf(pts);
        String unit = request.getParameter("units");
        
        Services services = new Services();
        
        // A modifier quand on aura implémenté la connection avec les sessions
        // En attendant, passer l'id du person.
        String idPerson = request.getParameter("idPerson");
        Long idPersonLong = Long.valueOf(idPerson);
        Person person = services.getPersonById(idPersonLong);
        // TODO : A modifier quand on connait la valeur du type (demander au front)
        boolean created = false;
        if (typeService == "demande") {
            
            Demand demand = new Demand(person, category, nameObject, availabilityDate,
                    availabilityTime, localisation, ""/*, type*/, nbPts, description, unit, duration);
            created = services.createDemand(person, demand);
        } else if (typeService == "offre") {
            Offer offer = new Offer(person, category, nameObject, availabilityDate,
                    availabilityTime, localisation, ""/*, type*/, nbPts, description, unit, duration);
            created = services.createOffer(person, offer);
        }
        
        request.setAttribute("created", created);
        
    }
}
