package actions;

import Model.Person;
import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Class linking the front and back to register a person
 * 
 * @author HEXA-4112
 */
public class ActionRegistration extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        String name = request.getParameter("nom");
        String firstName = request.getParameter("prenom");
        String password = request.getParameter("motDePasse");
        String mail = request.getParameter("mail");
        String verificationCode = request.getParameter("code");
        
        Services services = new Services();
        
        Person person = services.registerPerson(name, firstName, password, mail, "", verificationCode);
        
        request.setAttribute("person", person);
    }
}
