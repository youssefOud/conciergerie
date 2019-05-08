package actions;

import Services.Services;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Class linking the front and back to send a verification 
 * email before registration
 * 
 * @author HEXA-4112
 */
public class ActionCheckEmail extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        
        String mail = request.getParameter("mail");
        
        Services services = new Services();
        
        boolean emailSent = services.sendVerificationEmail(mail);
        
        request.setAttribute("emailSent", emailSent);
    }
}