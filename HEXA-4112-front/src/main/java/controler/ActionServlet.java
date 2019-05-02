package controler;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.JpaUtil;
import Model.Person;
import actions.ActionConnection;
import actions.ActionCreation;
import actions.ActionRegistration;
import actions.ActionShowTimeline;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import vue.SerialisationJSON;

/**
 * Servlet implementation class ActionServlet
 */

@WebServlet(name="ActionServlet", urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String todo = request.getParameter("todo");
        
        
        HttpSession session = request.getSession(true); 
        SerialisationJSON serialisationJSON = new SerialisationJSON();
        
        switch (todo) {
            case "inscription":
                ActionRegistration ar = new ActionRegistration();
                try {
                    ar.executeAction(request);
                } catch (ParseException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                serialisationJSON.executeInscription(request, response);
                
                break;
                
            case "connexion":
                ActionConnection actionConnection = new ActionConnection();
                try {
                    actionConnection.executeAction(request);
                } catch (ParseException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Person person = (Person) request.getAttribute("person");
                if (person != null){
                    session.setAttribute("idPerson", person.getId());
                }
                
                serialisationJSON.executeConnexion(request,response);
                
                break;
                
            case "deposerAnnonce":
                ActionCreation actionCreation = new ActionCreation();
            
                try {
                    actionCreation.executeAction(request);
                } catch (ParseException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                boolean created = (boolean) request.getAttribute("created");

                serialisationJSON.executeDeposerAnnonce(request, response);

                break;
            
            case "afficherFilActualite":
                ActionShowTimeline astl = new ActionShowTimeline();
                
                try {
                    astl.executeAction(request);
                } catch (ParseException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                serialisationJSON.executeShowTimeline(request, response);
        }
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        JpaUtil.init();
    }
    
    @Override
    public void destroy() {
        super.destroy();
        JpaUtil.destroy();
    }
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Servlet principale faisant office de controler";
    }
    
}