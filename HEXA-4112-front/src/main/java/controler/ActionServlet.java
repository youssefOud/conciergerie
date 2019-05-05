package controler;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.JpaUtil;
import Model.Person;
import actions.ActionAnswerAnAd;
import actions.ActionCheckEmail;
import actions.ActionConnection;
import actions.ActionCreation;
import actions.ActionDeconnection;
import actions.ActionGetAdDetails;
import actions.ActionGetInformationPerson;
import actions.ActionRegistration;
import actions.ActionShowTimeline;
import java.util.Enumeration;
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
            case "generationCode":
                ActionCheckEmail actionCheckEmail = new ActionCheckEmail();
                try {
                    actionCheckEmail.executeAction(request);
                } catch (ParseException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                serialisationJSON.executeGenerationCode(request, response);
                
                break;
                
            case "inscription":
                ActionRegistration ar = new ActionRegistration();
                try {
                    ar.executeAction(request);
                } catch (ParseException ex) {
                    Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Person personRegistered = (Person) request.getAttribute("person");
                if (personRegistered != null){
                    request.setAttribute("registered", true);
                    session.setAttribute("idPerson", personRegistered.getId());
                } else {
                    request.setAttribute("registered", false);
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
                
                Person personConnected = (Person) request.getAttribute("person");
                if (personConnected != null){
                    request.setAttribute("connected", true);
                    session.setAttribute("idPerson", personConnected.getId());
                } else {
                    request.setAttribute("connected", false);
                }
                
                serialisationJSON.executeConnexion(request,response);
                
                break;
                
            case "deposerAnnonce":
                System.out.println("idPerson : " + session.getAttribute("idPerson"));
                if (session.getAttribute("idPerson") != null){
                    ActionCreation actionCreation = new ActionCreation();

                    try {
                        actionCreation.executeAction(request);
                    } catch (ParseException ex) {
                        Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    boolean created = (boolean) request.getAttribute("created");

                    serialisationJSON.executeDeposerAnnonce(request, response);
                    
                } else {
                    request.setAttribute("error", false);
                    serialisationJSON.executeErrorNotConnected(request, response);
                }

                break;
            
            case "recupererInfoPersonne":
                if (session.getAttribute("idPerson") != null){
                    ActionGetInformationPerson agip = new ActionGetInformationPerson();

                    try {
                        agip.executeAction(request);
                    } catch (ParseException ex) {
                        Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    serialisationJSON.executeRecupererInfoPersonne(request, response);
                
                } else {
                    request.setAttribute("error", false);
                    serialisationJSON.executeErrorNotConnected(request, response);
                }
                
                break;
                
            case "afficherFilActualite":
                if (session.getAttribute("idPerson") != null){
                    ActionShowTimeline astl = new ActionShowTimeline();

                    try {
                        astl.executeAction(request);
                    } catch (ParseException ex) {
                        Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    serialisationJSON.executeShowTimeline(request, response);
                
                } else {
                    request.setAttribute("error", false);
                    serialisationJSON.executeErrorNotConnected(request, response);
                }
                
                break;
            
            case "repondreAnnonce":
                if (session.getAttribute("idPerson") != null){
                    ActionAnswerAnAd actionAnswerAnAd = new ActionAnswerAnAd();

                    try {
                        actionAnswerAnAd.executeAction(request);
                    } catch (ParseException ex) {
                        Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    serialisationJSON.executeRepondreAnnonce(request, response);
                
                } else {
                    request.setAttribute("error", false);
                    serialisationJSON.executeErrorNotConnected(request, response);
                }
                
                break;
            
            case "detailsAnnonce":
                
                if (session.getAttribute("idPerson") != null){
                    ActionGetAdDetails actionGetAdDetails = new ActionGetAdDetails();

                    try {
                        actionGetAdDetails.executeAction(request);
                    } catch (ParseException ex) {
                        Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    serialisationJSON.executeDetailsAnnonce(request, response);
                
                } else {
                    request.setAttribute("error", false);
                    serialisationJSON.executeErrorNotConnected(request, response);
                }
                
                break;
                
            case "seDeconnecter":
                if (session.getAttribute("idPerson") != null) {
                    ActionDeconnection ad = new ActionDeconnection();
                    
                    try {
                        ad.executeAction(request);
                    } catch (ParseException ex) {
                        Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    serialisationJSON.executeSeDeconnecter(request, response);
                }
                else {
                    request.setAttribute("error", false);
                    serialisationJSON.executeErrorNotConnected(request, response);
                }
                
                break;
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