package actions;

import java.io.IOException;
import java.text.ParseException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class allowing to disable the session and disconnect the user
 * 
 * @author HEXA-4112
 */
public class ActionDeconnection extends Action {

    @Override
    public void executeAction(HttpServletRequest request) throws ServletException, IOException, ParseException {
        HttpSession session = request.getSession();
        session.invalidate();
    }
    
}
