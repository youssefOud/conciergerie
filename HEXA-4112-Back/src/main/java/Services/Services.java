package Services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.RollbackException;

import DAO.*;
import Model.Demand;
import Model.Offer;
import Model.Person;
import Model.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author B3427
 */
public class Services {
    public Services(){
        
    }
    
    // TODO : compléter
    public Person connexion (String mail, String mdp) {
        JpaUtil.createEntityManager();
        PersonDAO personDAO = new PersonDAO();
        Person person = personDAO.verifyPersonAccount(mail, mdp);
        JpaUtil.closeEntityManager();
        
        return person;
    }
    
    // TODO : A completer : Dans l'ActionServlet, le bouton radio
    // permet de savoir si c'est une demande ou une offre
    public boolean createDemand (Demand demand) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        DemandDAO demandDAO = new DemandDAO();
        demandDAO.persist(demand);
        
        try {
            JpaUtil.validateTransaction();
        } catch (RollbackException e) {
            JpaUtil.cancelTransaction();
            return false;
        }
        
        JpaUtil.closeEntityManager();
        return true;
    }
    
    
    
    // TODO : A completer
    public boolean createOffer (Offer offer) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        // Traitement sur offer ? Date de début ?
        
        OfferDAO offerDAO = new OfferDAO();
        offerDAO.persist(offer);
        
        try {
            JpaUtil.validateTransaction();
        } catch (RollbackException e) {
            JpaUtil.cancelTransaction();
        }
        
        JpaUtil.closeEntityManager();
        return true;
    }
    
    public boolean createPerson (Person person) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        PersonDAO personDAO = new PersonDAO();
        personDAO.persist(person);
        
        try {
            JpaUtil.validateTransaction();
        } catch (RollbackException e) {
            JpaUtil.cancelTransaction();
            return false;
        }
        
        JpaUtil.closeEntityManager();
        return true;
    }
    
    // TODO : A completer : permet de retourner toutes les demandes
    // en cours
    public List<Demand> findAllDemands() {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        List<Demand> listDemand = new ArrayList<>();
        
        JpaUtil.closeEntityManager();
        return listDemand;
    }
    
    // Ajouter en parametre tous les critères des filtres afin de faire nos
    // comparaison
    // TODO : A completer : permet de retourner toutes les demandes
    // en cours avec les filtres mis
    public List<Service> findAllServicesWithFilter(String category, String location, String date, String time, String duration, String units, String nbPts, String type) throws ParseException {
        JpaUtil.createEntityManager();
        ServiceDAO serviceDao = new ServiceDAO();
        
        Date today = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
        Date startingDate = formatTime.parse(formatTime.format(today)) ;
        
        if (!date.isEmpty()) {
            if (time.isEmpty()) time="00:00";
            
            startingDate = formatTime.parse(date + " " + time);
        } else if (!time.isEmpty()) {
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy"); 
            startingDate = formatTime.parse(formatDate.format(today) + " " + time) ;
        } 
        
        Long durationInMillis = Long.valueOf(duration);
        if (units.equals("jours")) {
            durationInMillis *= 24*60*60*1000;
        } else if (units.equals("heures")) {
            durationInMillis *= 60*60*1000;
        }  else if (units.equals("minutes")) {
            durationInMillis *= 60*1000;
        }
                      
        List<Service> listServices = serviceDao.findAllServicesWithFilter(category, location, startingDate, durationInMillis, nbPts, type);
        
        JpaUtil.closeEntityManager();
        return listServices;
    }
    
    // TODO : A completer : permet de retourner toutes les offres
    // en cours
    public List<Offer> findAllOffers() {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        List<Offer> listOffer = new ArrayList<>();
        
        
        
        JpaUtil.closeEntityManager();
        return listOffer;
    }
    
    // Ajouter en parametre tous les critères des filtres afin de faire nos
    // comparaison
    // TODO : A completer : permet de retourner toutes les offres
    // en cours avec les filtres mis
    public List<Offer> findAllOffersWithFilters(/*Add Filter*/) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        List<Offer> listOffer = new ArrayList<>();
        
        
        
        JpaUtil.closeEntityManager();
        return listOffer;
    }
    
    public Person getPersonById(Long idPerson) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        PersonDAO personDAO = new PersonDAO();
        Person person = personDAO.findById(idPerson);
        
        JpaUtil.closeEntityManager();
        return person;
    }
    
    public Service getServiceById(Long idService) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        ServiceDAO serviceDAO = new ServiceDAO();
        Service service = serviceDAO.findById(idService);
        
        JpaUtil.closeEntityManager();
        return service;
    }
}
