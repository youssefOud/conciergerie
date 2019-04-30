package Servlet;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import Model.Demand;
import Model.Offer;
import Model.User;
import Services.Services;

public class ActionCreation extends Action {

	@Override
	public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {

		// On recupere le parametre du bouton radio pour savoir
				// si c'est une demande ou une offre
				String typeService = request.getParameter("typeService");
				
				// TODO : Modifier le nom des param et eventuellement le type
				// quand on aura les specs côté front
				String category = request.getParameter("category");
				String nameObject = request.getParameter("nameObject");
				// Pour date voir si on met une date ou un string
				// Pareil pour la durée
				String availabilityDate = request.getParameter("availabilityDate");
				String availabilityTime = request.getParameter("availabilityTime");
				String localisation = request.getParameter("localisation");
				// type indique si c'est un prêt, une donation, un service rendu ...
				//String type = request.getParameter("type");
				
				Services services = new Services();
			
				// A modifier quand on aura implémenté la connection avec les sessions
				// En attendant, passer l'id du user.
				String idUser = request.getParameter("idUser"); 
				Long idUserLong = Long.valueOf(idUser);
				User user = services.getUserById(idUserLong);
				// TODO : A modifier quand on connait la valeur du type (demander au front)
				boolean created = false;
				if (typeService == "demand") {
					Demand demand = new Demand(category, nameObject, availabilityDate, 
							availabilityTime, localisation/*, type*/);
					created = services.createDemand(user, demand);
				} else if (typeService == "offer") {
					Offer offer = new Offer(category, nameObject, availabilityDate, 
							availabilityTime, localisation/*, type*/);
					created = services.createOffer(user, offer);
				}
				
				request.setAttribute("created", created);
		
	}
}
