package controler;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.JpaUtil;
import actions.ActionCreation;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import vue.SerialisationJSON;

/**
 * Servlet implementation class ActionServlet
 */

@WebServlet(name="ActionServlet", urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {
    
    // A modifier l'annotation en fonction du nom de la servlet choisie
    private static final long serialVersionUID = 1L;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String todo = request.getParameter("todo");
        
        SerialisationJSON serialisationJSON = new SerialisationJSON();
        
        if ("deposerAnnonce".equals(todo)) {
            ActionCreation ac = new ActionCreation();
            
            try {
                ac.executeAction(request);
            } catch (ParseException ex) {
                Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            boolean created = (boolean) request.getAttribute("created");
            
            serialisationJSON.executeDeposerAnnonce(request, response);
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