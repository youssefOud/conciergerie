package actions;

import Model.Person;
import Model.Reservation;
import Model.Service;
import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class linking the front and back to get the ads of a person
 * 
 * @author HEXA-4112
 */
public class ActionGetAdsByPerson extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        Services services = new Services();
        Person person = services.getPersonById(idPerson);
        HashMap<Service, List<Reservation>> ads = services.getAdsByPerson(person);
        
        request.setAttribute("ads", ads);
    }    
}
