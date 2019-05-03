/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import Model.Offer;
import Model.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author X
 */
public class SerialisationJSON {
    
    public void executeDeposerAnnonce(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();
        
        boolean created = (boolean) request.getAttribute("created");
        jo.addProperty("created", created);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    public void executeShowTimeline(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        JsonObject container = new JsonObject();
        JsonArray jsonList = new JsonArray();
        
        List<Service> listOfServices = (List<Service>) request.getAttribute("listOfServices");
        
        for (Service s : listOfServices) {
            JsonObject jo = new JsonObject();
            
            jo.addProperty("categorie", s.getCategory());
            jo.addProperty("localisation", s.getLocation());
            jo.addProperty("nomObjet", s.getNameObject());
            jo.addProperty("nbPts", s.getNbPoint());
            jo.addProperty("typeService", s.getType());
            if (s instanceof Offer) {
                jo.addProperty("typeAnnonce", "offre");
            } else {
                jo.addProperty("typeAnnonce", "demande");
            }
            
            Date date = s.getAvailabilityDate();
            Date datePublication = s.getPublicationDate();
            String pattern = "MM/dd/yyyy HH:mm:ss";
            DateFormat df = new SimpleDateFormat(pattern);  
            String dateAsString = df.format(date);
            String datePublicationAsString = df.format(datePublication);
            
            String theDate = dateAsString.substring(0,11);
            String theTime = dateAsString.substring(11);
            
            String theDateOfPublication = datePublicationAsString.substring(0,11);
            String theTimeOfPublication = datePublicationAsString.substring(11);
            
            jo.addProperty("date", theDate);
            jo.addProperty("time", theTime);
            jo.addProperty("datePublication", theDateOfPublication);
            jo.addProperty("timePublication", theTimeOfPublication);
            
            int duration = s.getDuration();
            String theDuration = Integer.toString(duration);
            jo.addProperty("duree", theDuration);
            
            jo.addProperty("unitePrix", s.getPriceUnit());
            jo.addProperty("uniteDuree", s.getDurationUnit());

            // TODO : A changer quand l'attribut preferences de contact sera mis en place
            
            if(s.getPersonDemanding() != null){
                jo.addProperty("auteur", s.getPersonDemanding().getMail());
            }
            else if( s.getPersonOffering()!= null){
                jo.addProperty("auteur", s.getPersonOffering().getMail());
            }
            
            // pictures aussi a mettre
            /*JsonObject containerPictures = new JsonObject();
            JsonArray jsonListPictures = new JsonArray();
            for (String picture : s.getPictures()) {
                jsonListPictures.add(picture);
            }
            containerPictures.add("images", jsonListPictures);*/
            
            jsonList.add(jo);
        }
        
        container.add("Annonces", jsonList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(container));
        out.close();
    }

    public void executeGenerationCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();
        
        boolean emailSent = (boolean) request.getAttribute("emailSent");
        jo.addProperty("emailSent", emailSent);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }
     
    public void executeInscription(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();
        
        if (request.getAttribute("idPerson") != null) {
            jo.addProperty("registered", true);
        } else {
            jo.addProperty("registered", false);
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    public void executeConnexion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();
        
        if (request.getAttribute("person") != null) {
            jo.addProperty("connected", true);
        } else {
            jo.addProperty("connected", false);
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    // TODO : A voir avec les filles
    public void executeErrorNotConnected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();
        
        if (request.getAttribute("idPerson") != null) {
            jo.addProperty("error", false);
        } else {
            jo.addProperty("error", true);
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }
}
