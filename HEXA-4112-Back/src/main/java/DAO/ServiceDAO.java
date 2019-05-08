package DAO;

import javax.persistence.EntityManager;
import Model.*;
import Utils.SynonymsFinder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Class linking the data access layer to the service business layer
 * 
 * @author HEXA-4112
 */
public class ServiceDAO {

    /**
     * Gets the service thanks to the id
     * 
     * @param id
     * @return the Service corresponding to the id
     */
    public Service findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Service.class, id);
    }

    /**
     * Persists the parameter service in the database
     * 
     * @param service 
     */
    public void persist(Service service) {
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(service);
    }

    /**
     * Merges the parameter service in the database
     * 
     * @param service 
     */
    public void merge(Service service) {
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(service);
    }

    /**
     * Removes the parameter service in the database
     * 
     * @param service 
     */
    public void remove(Service service) {
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(service);
    }

    /**
     * Gets all services that match the filters
     * 
     * @param service 
     */
    public List<Service> findAllServicesWithFilter(String object, String category, String location, Date startingDate, Date endingDate, String nbPts, String paymentUnit, String type) {
        EntityManager em = JpaUtil.getEntityManager();
        String request;
        if (startingDate == null) {
            request = "select s from Service s where s.endOfAvailabilityDate >= :endingDate and s.serviceState = :validState ";
        } else {
            request = "select s from Service s where s.availabilityDate <= :startingDate and s.endOfAvailabilityDate >= :endingDate and s.serviceState = :validState ";
        }


        if (!category.isEmpty()) {
            request += "and ";
            request += "s.category = :category ";
        }
        if (!location.isEmpty()) {
            request += "and ";
            request += "s.location = :location ";
        }

        if (!type.isEmpty()) {
            request += "and ";
            request += "type(s) = :class";
        }

        request += " order by s.publicationDate desc";

        System.out.println("request: " + request);
        System.out.println("startingDate: " + startingDate);
        System.out.println("endingDate:" + endingDate);

        Query query = em.createQuery(request);
        if (startingDate != null) {
            query.setParameter("startingDate", startingDate, TemporalType.TIMESTAMP);
        }
        query.setParameter("endingDate", endingDate, TemporalType.TIMESTAMP);
        query.setParameter("validState", 0);
       
      
        if (!category.isEmpty()) {
            query.setParameter("category", category);
        }
        if (!location.isEmpty()) {
            query.setParameter("location", location);
        }

        if (!type.isEmpty()) {
            if (type.equals("demande")) {
                query.setParameter("class", Demand.class);
            } else if (type.equals("offre")) {
                query.setParameter("class", Offer.class);
            }
        }
        List<Service> filteredServices = (List<Service>) query.getResultList();

        System.out.println("filtered: " + filteredServices.size());
        
        //Analyse sémantique sur le nom de l'objet
        if (!object.isEmpty()) {
            List<Service> filteredServicesWithName = new ArrayList<>();
            SynonymsFinder sf = new SynonymsFinder();

            String uselessWords = "un une le la ce cette ma ta les des mon ton mes tes ses sa son notre votre leur à électrique machine appareil automatique";

            String [] objectNameWords = object.toLowerCase().split(" ");
            List<String> objectNameWordsWithoutPronouns = new ArrayList<>();

            for(String word : objectNameWords){
                if(!uselessWords.contains(word)){
                    objectNameWordsWithoutPronouns.add(word);
                }
            }

            String synonyms = "";
            for(String word : objectNameWordsWithoutPronouns){
                synonyms += sf.SendRequest(word, "fr_FR", "H8H9E1QqrjX7noHE7aJq", "json") + "|"+ word + "|";
            }


            for(Service s : filteredServices){
                String[] words = s.getNameObject().split(" ");
                for(String word : words){
                    if ( !uselessWords.contains(word.toLowerCase()) && synonyms.contains(word.toLowerCase()) ){
                        filteredServicesWithName.add(s);
                    }
                }
            }
            filteredServices = filteredServicesWithName;
        }
        
        //Tri sur le nombre de points

        if (!nbPts.isEmpty()) {
            int nbPtsPerDay;
            if (paymentUnit.equals("minutes")) {
                nbPtsPerDay = Integer.valueOf(nbPts) * 60 * 24;
            } else if (paymentUnit.equals("heures")) {
                nbPtsPerDay = Integer.valueOf(nbPts) * 24;
            } else {
                nbPtsPerDay = Integer.valueOf(nbPts);
            }
            List<Service> servicesToRemove = new ArrayList<>();
            for (Service s : filteredServices) {
                if (s instanceof Offer) {
                    if (s.getNbPointPerDay() > nbPtsPerDay) {
                        servicesToRemove.add(s);
                    }
                } else if (s.getNbPointPerDay() < nbPtsPerDay) {
                    servicesToRemove.add(s);
                }
            }

            for (Service s : servicesToRemove) {
                filteredServices.remove(s);
            }
        }

        return filteredServices;
    }
    
    /**
     * Gets all the services that the person has published
     * 
     * @param person
     * @return a List of Service
     */
     public List<Service> findAllServicesByPerson(Person person) {
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select s from Service s where s.personOffering = :person or s.personDemanding = :person order by s.serviceState asc";          
        Query query = em.createQuery(request).setParameter("person", person);
        List<Service> services = (List<Service>)query.getResultList();
        return services; 
    } 
     
    /**
    * Gets all the interests that the person has made
    * 
    * @param person
    * @return a List of Service
    */
    public List<Object[]> findInterestsByPerson(Person person){
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select r,r.service from Reservation r where r.reservationOwner = :person order by r.reservationRequestDate desc";      
        Query query = em.createQuery(request).setParameter("person", person);
        List<Object[]> services = (List<Object[]>)query.getResultList();
        
        System.out.println("services: " + services.size());
        return services; 
    }
     
    /**
     * Gets all services that match the user's request
     * 
     * @param service
     * @param serviceClass 0 for offer, 1 for demand
     * @return a List of Service
     */
    public List<Service> matchMaking(Service service, int serviceClass){
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select s from Service s where "
                + "type(s) = :class "
                + "and s.serviceState = :validState "
                + "and s.availabilityDate <= :startingDate "
                + "and s.category = :category ";
        request += " order by s.publicationDate desc";
        
        Query query = em.createQuery(request);
        if(serviceClass == 0){
            query.setParameter("class", Demand.class);
        }
        else{
            query.setParameter("class", Offer.class);
        }
        query.setParameter("validState", 0);
        query.setParameter("startingDate", new Date(service.getAvailabilityDate().getTime() + 2*24*60*60*1000), TemporalType.TIMESTAMP);
        query.setParameter("category", service.getCategory());
 
        List<Service> servicesWithoutNameFilter = (List<Service>) query.getResultList();
        
        List<Service> res = new ArrayList<>();
        SynonymsFinder sf = new SynonymsFinder();
        
        String uselessWords = "un une le la ce cette ma ta les des mon ton mes tes ses sa son notre votre leur à électrique machine appareil automatique";
        
        String [] objectNameWords = service.getNameObject().toLowerCase().split(" ");
        List<String> objectNameWordsWithoutPronouns = new ArrayList<>();
        
        for(String word : objectNameWords){
            if(!uselessWords.contains(word)){
                objectNameWordsWithoutPronouns.add(word);
            }
        }
        
        String synonyms = "";
        for(String word : objectNameWordsWithoutPronouns){
            synonyms += sf.SendRequest(word, "fr_FR", "H8H9E1QqrjX7noHE7aJq", "json") + "|" + word + "|";
        }
        
        for(Service s : servicesWithoutNameFilter){
            String[] words = s.getNameObject().split(" ");
            for(String word : words){
                if ( !uselessWords.contains(word.toLowerCase()) && synonyms.contains(word.toLowerCase())){
                    res.add(s);
                }
            }
        }
        
        return res;
    }
}
