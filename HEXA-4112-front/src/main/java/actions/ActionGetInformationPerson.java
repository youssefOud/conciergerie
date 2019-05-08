package actions;

import Model.Person;
import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class linking the front and back to get the information of a person
 * 
 * @author HEXA-4112
 */
public class ActionGetInformationPerson extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        Services services = new Services();
        Person person = services.getPersonById(idPerson);
        
        if (person != null) {
            request.setAttribute("session", true);
            request.setAttribute("prenom", person.getFirstName());
            request.setAttribute("nom", person.getLastName());
            request.setAttribute("nbPoint", person.getPointBalance());
            request.setAttribute("email", person.getMail());
            request.setAttribute("numTel", person.getCellNumber());
            request.setAttribute("contactPrefere", person.getPrivilegedContact());
            request.setAttribute("note", person.getRating());
        } else {
            request.setAttribute("session", false);
        }
    }
    
}