package actions;

import Model.Person;
import Model.Service;
import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class linking the front and back to change a user's privileged contact and 
 * add his phone number
 * 
 * @author HEXA-4112
 */
public class ActionChangePrivilegedContact extends Action {
    
    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {

        HttpSession session = request.getSession();
        Long idPerson = (Long) session.getAttribute("idPerson");
        
        String cellNumber = request.getParameter("numeroTelephone");
        String privilegedContact = request.getParameter("contactPrivilegie");
        
        Services services = new Services();
        boolean saved = services.savePrivilegedContact(idPerson, privilegedContact, cellNumber);
        
        request.setAttribute("saved", saved);
    }
}
