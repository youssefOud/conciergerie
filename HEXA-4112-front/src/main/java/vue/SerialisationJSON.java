/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import Model.Offer;
import Model.Reservation;
import Model.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that serializes the information to be sent in JSON format
 * 
 * @author HEXA-4112
 */
public class SerialisationJSON {

    /**
     * Serializes information in JSON format to respond to the creation of an ad
     * @param request
     * @param response
     * @throws IOException 
     */
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

    /**
     * Serializes information in JSON format to respond to the timeline request
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeShowTimeline(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject container = new JsonObject();
        JsonArray jsonList = new JsonArray();

        List<Service> listOfServices = (List<Service>) request.getAttribute("listOfServices");
        container.addProperty("nbPropositions", (int) request.getAttribute("nbPropositions"));
        for (Service s : listOfServices) {
            if (s.getServiceState() == 0) {
                JsonObject jo = new JsonObject();

                jo.addProperty("categorie", s.getCategory());
                jo.addProperty("localisation", s.getLocation());
                jo.addProperty("nomObjet", s.getNameObject());
                jo.addProperty("nbPts", s.getNbPoint());
                jo.addProperty("typeService", s.getType());
                jo.addProperty("description", s.getDescription());
                if (s instanceof Offer) {
                    jo.addProperty("typeAnnonce", "offre");
                    if (s.getPersonOffering() != null) {
                        if (s.getPersonOffering().getId().equals(request.getAttribute("idPerson"))) {
                            jo.addProperty("memePersonne", true);
                        } else {
                            jo.addProperty("memePersonne", false);
                        }
                        if (s.getPersonOffering().getPrivilegedContact().equals("email")) {
                            jo.addProperty("auteur", s.getPersonOffering().getMail());
                        } else {
                            jo.addProperty("auteur", s.getPersonOffering().getCellNumber());
                        }
                    }
                } else {
                    jo.addProperty("typeAnnonce", "demande");
                    if (s.getPersonDemanding() != null) {
                        if (s.getPersonDemanding().getId().equals(request.getAttribute("idPerson"))) {
                            jo.addProperty("memePersonne", true);
                        } else {
                            jo.addProperty("memePersonne", false);
                        }
                        if (s.getPersonDemanding().getPrivilegedContact().equals("email")) {
                            jo.addProperty("auteur", s.getPersonDemanding().getMail());
                        } else {
                            jo.addProperty("auteur", s.getPersonDemanding().getCellNumber());
                        }
                    }
               }
                Date date = s.getAvailabilityDate();
                Date datePublication = s.getPublicationDate();
                String pattern = "dd/MM/yyyy HH:mm";
                DateFormat df = new SimpleDateFormat(pattern);
                df.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
                String dateAsString = df.format(date);
                String datePublicationAsString = df.format(datePublication);

                String theDate = dateAsString.substring(0, 11);
                String theTime = dateAsString.substring(11);

                String theDateOfPublication = datePublicationAsString.substring(0, 11);
                String theTimeOfPublication = datePublicationAsString.substring(11);

                jo.addProperty("date", theDate);
                jo.addProperty("time", theTime);
                jo.addProperty("datePublication", theDateOfPublication);
                jo.addProperty("timePublication", theTimeOfPublication);

                int duration = s.getDuration();
                String theDuration = Integer.toString(duration);
                jo.addProperty("duree", theDuration);
                String unitePrix = s.getPriceUnit();
                if(unitePrix.equals("heures")){
                    jo.addProperty("unitePrix", "heure");
                }
                else if(unitePrix.equals("minutes")){
                    jo.addProperty("unitePrix", "minute");
                }
                else if(unitePrix.equals("jours")){
                    jo.addProperty("unitePrix", "jour");
                }
                
                jo.addProperty("uniteDuree", s.getDurationUnit());
                jo.addProperty("idAnnonce", s.getId());
                // TODO : A changer quand l'attribut preferences de contact sera mis en place

                
                if (s.getPersonDemanding() != null) {
                    jo.addProperty("auteur", s.getPersonDemanding().getMail());
                    jo.addProperty("noteMoyenneAuteur", (double) roundRating(s.getPersonDemanding().getRating()));
                } else if (s.getPersonOffering() != null) {
                    jo.addProperty("auteur", s.getPersonOffering().getMail());
                    jo.addProperty("noteMoyenneAuteur", (double) roundRating(s.getPersonOffering().getRating()));
                }
                JsonArray jsonListPictures = new JsonArray();
                if (s.getPictures() != null && s.getPictures() != "") {
                    String pictures = s.getPictures();
                    String[] picturesArray = pictures.split("-");
                    System.out.println("array string " + picturesArray.length);
                    for (int i = 0; i < picturesArray.length; i++) {
                        jsonListPictures.add(picturesArray[i]);
                        System.out.println(i + ": pic");
                    }
                    jo.add("images", jsonListPictures);
                }
                jsonList.add(jo);
            }
        }

        container.add("Annonces", jsonList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(container));
        out.close();
    }

    /**
     * Serializes information in JSON format to respond to the request to generate a code
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeGenerationCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("emailSent", (boolean) request.getAttribute("emailSent"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to respond to a user's registration request
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeInscription(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("registered", (boolean) request.getAttribute("registered"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to respond to a user's connexion request
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeConnexion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("connected", (boolean) request.getAttribute("connected"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to warn that the user is not logged in
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeErrorNotConnected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("error", (boolean) request.getAttribute("error"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to return a person's ads
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeGetAnnoncesPersonne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject container = new JsonObject();
        JsonArray jsonList = new JsonArray();

        HashMap<Service, ArrayList<Reservation>> ads = (HashMap<Service, ArrayList<Reservation>>) request.getAttribute("ads");
        if (ads != null) {
            container.addProperty("error", false);
            Set<Entry<Service, ArrayList<Reservation>>> setAds = ads.entrySet();
            Iterator<Entry<Service, ArrayList<Reservation>>> it = setAds.iterator();
            while (it.hasNext()) {
                JsonObject jo = new JsonObject();
                Entry<Service, ArrayList<Reservation>> e = it.next();
                jo.addProperty("etat", (int) e.getKey().getServiceState());
                jo.addProperty("categorie", (String) e.getKey().getCategory());
                jo.addProperty("description", (String) e.getKey().getDescription());
                jo.addProperty("duree", (int) e.getKey().getDuration());
                jo.addProperty("uniteDuree", (String) e.getKey().getDurationUnit());
                jo.addProperty("objet", (String) e.getKey().getNameObject());
                jo.addProperty("nbPts", (int) e.getKey().getNbPoint());
                jo.addProperty("unitePrix", (String) e.getKey().getPriceUnit());
                jo.addProperty("localisation", (String) e.getKey().getLocation());
                jo.addProperty("typeService", (String) e.getKey().getType());
                jo.addProperty("idAnnonce", (Long) e.getKey().getId());
                if (e.getKey() instanceof Offer) {
                    jo.addProperty("typeAnnonce", "offre");
                    if (e.getKey().getPersonOffering() != null) {
                        if (e.getKey().getPersonOffering().getPrivilegedContact().equals("email")) {
                            jo.addProperty("auteur", e.getKey().getPersonOffering().getMail());
                        } else {
                            jo.addProperty("auteur", e.getKey().getPersonOffering().getCellNumber());
                        }
                    }
                } else {
                    jo.addProperty("typeAnnonce", "demande");
                    if (e.getKey().getPersonDemanding() != null) {
                        if (e.getKey().getPersonDemanding().getPrivilegedContact().equals("email")) {
                            jo.addProperty("auteur", e.getKey().getPersonDemanding().getMail());
                        } else {
                            jo.addProperty("auteur", e.getKey().getPersonDemanding().getCellNumber());
                        }
                    }
                }

                Date date = e.getKey().getAvailabilityDate();
                Date datePublication = e.getKey().getPublicationDate();
                String pattern = "dd/MM/yyyy HH:mm";
                DateFormat df = new SimpleDateFormat(pattern);
                df.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
                String dateAsString = df.format(date);
                String datePublicationAsString = df.format(datePublication);

                String theDate = dateAsString.substring(0, 11);
                String theTime = dateAsString.substring(11);

                String theDateOfPublication = datePublicationAsString.substring(0, 11);
                String theTimeOfPublication = datePublicationAsString.substring(11);

                jo.addProperty("date", theDate);
                jo.addProperty("time", theTime);
                jo.addProperty("datePublication", theDateOfPublication);
                jo.addProperty("timePublication", theTimeOfPublication);

                JsonArray jsonListPictures = new JsonArray();
                if (e.getKey().getPictures() != null && e.getKey().getPictures() != "") {
                    String pictures = e.getKey().getPictures();
                    String[] picturesArray = pictures.split("-");
                    for (int i = 0; i < picturesArray.length; i++) {
                        jsonListPictures.add(picturesArray[i]);
                    }
                    jo.add("pictures", jsonListPictures);
                }

                JsonArray jsonListReponses = new JsonArray();
                List<Reservation> reservations = e.getValue();

                if (!reservations.isEmpty() && reservations != null) {
                    for (Reservation r : reservations) {
                        JsonObject joReponse = new JsonObject();
                        joReponse.addProperty("idReponse", r.getId());
                        joReponse.addProperty("duree", r.getReservationDuration());
                        joReponse.addProperty("uniteDuree", r.getDurationUnit());
                        joReponse.addProperty("etat", r.getReservationState());
                        if (r.getReservationOwner() != null) {
                            if (r.getReservationOwner().getPrivilegedContact().equals("email")) {
                                joReponse.addProperty("auteur", r.getReservationOwner().getMail());
                            } else {
                                joReponse.addProperty("auteur", r.getReservationOwner().getCellNumber());
                            }
                        }
                        joReponse.addProperty("prix", r.getReservationPrice());
                        joReponse.addProperty("note", (double) roundRating(r.getReservationOwner().getRating()));
                        Date dateWanted = r.getReservationStartingDate();
                        String dateWantedAsString = df.format(dateWanted);
                        
                        String theDateWanted = dateWantedAsString.substring(0, 11);
                        String theTimeWanted = dateWantedAsString.substring(11);
                        
                        joReponse.addProperty("date", theDateWanted);
                        joReponse.addProperty("time", theTimeWanted);

                        jsonListReponses.add(joReponse);
                    }
                    jo.add("reponses", jsonListReponses);
                } else {
                    jo.add("reponses", jsonListReponses);
                }
                jsonList.add(jo);
            }
        } else {
            container.addProperty("error", true);
        }

        container.add("Annonces", jsonList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(container));
        out.close();
    }

    /**
     * Serializes information in JSON format to confirm logout
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeSeDeconnecter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("deconnexion", true);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to return a person's information
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeRecupererInfoPersonne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        if ((boolean) request.getAttribute("session")) {
            jo.addProperty("session", true);
            jo.addProperty("prenom", (String) request.getAttribute("prenom"));
            jo.addProperty("nom", (String) request.getAttribute("nom"));
            jo.addProperty("nbPoint", (int) request.getAttribute("nbPoint"));
            jo.addProperty("email", (String) request.getAttribute("email"));
            jo.addProperty("numTel", (String) request.getAttribute("numTel"));
            jo.addProperty("contactPrefere", (String) request.getAttribute("contactPrefere"));
            jo.addProperty("note", (double) request.getAttribute("note"));
        } else {
            jo.addProperty("session", false);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to confirm the registration of the privileged contact
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeEnregistreContactPrivilegie(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("saved", (boolean) request.getAttribute("saved"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to confirm the answer of an ad
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeRepondreAnnonce(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("creationReponse", (Boolean) request.getAttribute("created"));
        jo.addProperty("messageErreur", (String) request.getAttribute("message"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to give details of an ad
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeDetailsAnnonce(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        Service service = (Service) request.getAttribute("service");
        if (service != null && service.getServiceState() == 0) {
            jo.addProperty("annonce", true);
            jo.addProperty("categorie", (String) service.getCategory());
            jo.addProperty("duree", (int) service.getDuration());
            jo.addProperty("localisation", (String) service.getLocation());
            jo.addProperty("nbPts", (double) service.getNbPoint());
            jo.addProperty("objet", (String) service.getNameObject());
            jo.addProperty("uniteDuree", (String) service.getDurationUnit());
            jo.addProperty("unitePrix", (String) service.getPriceUnit());
            Date date = service.getAvailabilityDate();
            Date datePublication = service.getPublicationDate();
            String pattern = "dd/MM/yyyy HH:mm";
            DateFormat df = new SimpleDateFormat(pattern);
            df.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
            String dateAsString = df.format(date);
            String datePublicationAsString = df.format(datePublication);

            String theDate = dateAsString.substring(0, 11);
            String theTime = dateAsString.substring(11);

            String theDateOfPublication = datePublicationAsString.substring(0, 11);
            String theTimeOfPublication = datePublicationAsString.substring(11);

            jo.addProperty("date", theDate);
            jo.addProperty("time", theTime);
            jo.addProperty("datePublication", theDateOfPublication);
            jo.addProperty("timePublication", theTimeOfPublication);

            JsonArray jsonListPictures = new JsonArray();
            if (service.getPictures() != null && service.getPictures() != "") {
                String pictures = service.getPictures();
                String[] picturesArray = pictures.split("-");
                System.out.println("array string " + picturesArray.length);
                for (int i = 0; i < picturesArray.length; i++) {
                    jsonListPictures.add(picturesArray[i]);
                    System.out.println(i + ": pic");
                }
                jo.add("images", jsonListPictures);
            }

            if (service instanceof Offer) {
                jo.addProperty("typeAnnonce", "offre");
                if (service.getPersonOffering() != null) {
                    jo.addProperty("auteur", (String) service.getPersonOffering().getPrivilegedContact());
                }
            } else {
                jo.addProperty("typeAnnonce", "demande");
                if (service.getPersonDemanding() != null) {
                    jo.addProperty("auteur", (String) service.getPersonDemanding().getPrivilegedContact());
                }
            }
            jo.addProperty("idAnnonce", (Long) service.getId());
            jo.addProperty("typeService", (String) service.getType());
        } else {
            jo.addProperty("annonce", false);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to give the price of the user's request
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeCalculPrix(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();
        
        jo.addProperty("calcule", true);
        jo.addProperty("prix", (int) request.getAttribute("price"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to give the user's interests
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeGetInteretsPersonne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject container = new JsonObject();
        JsonArray jsonList = new JsonArray();

        HashMap<Service, Reservation> services = (HashMap<Service, Reservation>) request.getAttribute("listOfServices");
        if (services != null && !services.isEmpty()) {
            container.addProperty("error", false);
            Set<Entry<Service, Reservation>> setServices = services.entrySet();
            Iterator<Entry<Service, Reservation>> it = setServices.iterator();
            while (it.hasNext()) {
                JsonObject jo = new JsonObject();
                Entry<Service, Reservation> e = it.next();
                jo.addProperty("etat", (int) e.getKey().getServiceState());
                jo.addProperty("categorie", (String) e.getKey().getCategory());
                jo.addProperty("description", (String) e.getKey().getDescription());
                jo.addProperty("duree", (int) e.getKey().getDuration());
                jo.addProperty("uniteDuree", (String) e.getKey().getDurationUnit());
                jo.addProperty("objet", (String) e.getKey().getNameObject());
                jo.addProperty("nbPts", (int) e.getKey().getNbPoint());
                jo.addProperty("unitePrix", (String) e.getKey().getPriceUnit());
                jo.addProperty("localisation", (String) e.getKey().getLocation());
                jo.addProperty("typeService", (String) e.getKey().getType());
                jo.addProperty("idAnnonce", e.getKey().getId());
                if (e.getKey() instanceof Offer) {
                    jo.addProperty("typeAnnonce", "offre");
                    if (e.getKey().getPersonOffering() != null) {
                        jo.addProperty("noteMoyenneAuteur", (double) roundRating(e.getKey().getPersonOffering().getRating()));
                        if (e.getKey().getPersonOffering().getPrivilegedContact().equals("email")) {
                            jo.addProperty("auteur", e.getKey().getPersonOffering().getMail());
                        } else {
                            jo.addProperty("auteur", e.getKey().getPersonOffering().getCellNumber());
                        }
                    }
                } else {
                    jo.addProperty("typeAnnonce", "demande");
                    if (e.getKey().getPersonDemanding() != null) {
                        jo.addProperty("noteMoyenneAuteur",(double) roundRating(e.getKey().getPersonDemanding().getRating()));
                        if (e.getKey().getPersonDemanding().getPrivilegedContact().equals("email")) {
                            jo.addProperty("auteur", e.getKey().getPersonDemanding().getMail());
                        } else {
                            jo.addProperty("auteur", e.getKey().getPersonDemanding().getCellNumber());
                        }
                    }
                }

                Date date = e.getKey().getAvailabilityDate();
                Date datePublication = e.getKey().getPublicationDate();
                String pattern = "dd/MM/yyyy HH:mm";
                DateFormat df = new SimpleDateFormat(pattern);
                df.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
                String dateAsString = df.format(date);
                String datePublicationAsString = df.format(datePublication);

                String theDate = dateAsString.substring(0, 11);
                String theTime = dateAsString.substring(11);

                String theDateOfPublication = datePublicationAsString.substring(0, 11);
                String theTimeOfPublication = datePublicationAsString.substring(11);

                jo.addProperty("date", theDate);
                jo.addProperty("time", theTime);
                jo.addProperty("datePublication", theDateOfPublication);
                jo.addProperty("timePublication", theTimeOfPublication);
                

                JsonArray jsonListPictures = new JsonArray();
                if (e.getKey().getPictures() != null && e.getKey().getPictures() != "") {
                    String pictures = e.getKey().getPictures();
                    String[] picturesArray = pictures.split("-");
                    for (int i = 0; i < picturesArray.length; i++) {
                        jsonListPictures.add(picturesArray[i]);
                    }
                    jo.add("pictures", jsonListPictures);
                }

                Reservation reservation = e.getValue();

                if (reservation != null) {
                    jo.addProperty("idReponse", reservation.getId());
                    jo.addProperty("dureeReponse", reservation.getReservationDuration());
                    jo.addProperty("uniteDureeReponse", reservation.getDurationUnit());
                    jo.addProperty("etatReponse", reservation.getReservationState());
                    if (reservation.getReservationOwner() != null) {
                        if (reservation.getReservationOwner().getPrivilegedContact().equals("email")) {
                            jo.addProperty("auteurReponse", reservation.getReservationOwner().getMail());
                        } else {
                            jo.addProperty("auteurReponse", reservation.getReservationOwner().getCellNumber());
                        }
                    }
                    Date dateWanted = reservation.getReservationStartingDate();
                    String dateWantedAsString = df.format(dateWanted);

                    String theDateWanted = dateWantedAsString.substring(0, 11);
                    String theTimeWanted = dateWantedAsString.substring(11);

                    jo.addProperty("dateReponse", theDateWanted);
                    jo.addProperty("timeReponse", theTimeWanted);
                    jo.addProperty("prixPropose", reservation.getReservationPrice());
                    jo.addProperty("noteAuteurAnnonce", (double) roundRating(reservation.getServiceOwnerRating()));
                    jo.addProperty("noteReponseAnnonce", (double) roundRating(reservation.getReservationOwnerRating()));
                    
                }
                jsonList.add(jo);
            }
        } else {
            container.addProperty("error", true);
        }

        container.add("Annonces", jsonList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(container));
        out.close();
    }

    /**
     * Serializes information in JSON format to confirm the deletion of an interest
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeSupprimerInteret(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("deleted", (boolean) request.getAttribute("deleted"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to confirm the deletion of a person
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeSupprimerPersonne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();
        
        jo.addProperty("deleted", (boolean) request.getAttribute("deleted"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to confirm the validation of the response to an ad
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeValiderReponseAnnonce(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("confirmationDone", (boolean) request.getAttribute("confirmed"));
        jo.addProperty("message", (String) request.getAttribute("message"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to confirm the declination of a 
     * response to the user's announcement
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeDeclinerReponseAnnonce(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("declined", (boolean) request.getAttribute("declined"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to confirm the deletion of an ad
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeSupprimerAnnonce(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("supprime", (boolean) request.getAttribute("deleted"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to confirm the beneficiary's rating
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeNoterBeneficiaire(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("note", (boolean) request.getAttribute("rated"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to confirm the bidder's rating
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeNoterOffrant(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("note", (boolean) request.getAttribute("rated"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }

    /**
     * Serializes information in JSON format to confirm notification of an ad alert
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
    public void executeSignalerAnnonce(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = new JsonObject();

        jo.addProperty("signale", (boolean) request.getAttribute("reported"));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.println(gson.toJson(jo));
        out.close();
    }
    
    /**
     * Method for rounding the user's note to one digit after the decimal point
     * 
     * @param numberToRound
     * @return 
     */
    private double roundRating (double numberToRound) {
        DecimalFormat decimalFormat = new DecimalFormat("#,#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        String doubleString = decimalFormat.format(numberToRound).replace(",",".");
        return Double.valueOf(doubleString);
    }
}
