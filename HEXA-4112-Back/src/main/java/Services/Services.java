package Services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.RollbackException;

import DAO.*;
import Model.Demand;
import Model.Offer;
import Model.Person;
import Model.Service;
import Model.VerificationToken;
import Utils.EmailSenderService;
import com.sun.media.sound.EmergencySoundbank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author B3427
 */

public class Services {
    final private DemandDAO demandDAO;
    final private OfferDAO offerDAO;
    final private PersonDAO personDAO;
    final private ServiceDAO serviceDAO;
    final private VerificationTokenDAO verificationTokenDAO;
    
    public Services(){
        this.demandDAO = new DemandDAO();
        this.offerDAO = new OfferDAO();
        this.personDAO = new PersonDAO();
        this.serviceDAO = new ServiceDAO();   
        this.verificationTokenDAO = new VerificationTokenDAO();   
    }
    
    
    // TODO : compléter
    public Person connectPerson (String mail, String mdp) {
        JpaUtil.createEntityManager();
        Person person = personDAO.verifyPersonAccount(mail, mdp);
        JpaUtil.closeEntityManager();
        
        return person;
    }
    
    public boolean sendVerificationEmail (String mail){
        JpaUtil.createEntityManager();
        
        if( personDAO.personExists(mail) ){
            
            JpaUtil.closeEntityManager();
            return false;
        }
        else{
            String verifCode = EmailSenderService.sendVerificationEmail(mail);
            if(!verifCode.isEmpty()){
                VerificationToken vt = new VerificationToken(verifCode, mail);
                try {
                    JpaUtil.openTransaction();
                    verificationTokenDAO.persist(vt);
                    JpaUtil.validateTransaction();
                }
                catch(Exception e){
                    JpaUtil.cancelTransaction();
                    JpaUtil.closeEntityManager();
                    return false;
                }
            }
            
        }
        JpaUtil.closeEntityManager();
        return true;
    }
    
    // TODO : A completer : Dans l'ActionServlet, le bouton radio
    // permet de savoir si c'est une demande ou une offre
    public boolean createDemand (Demand demand) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
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
        
        offerDAO.persist(offer);
        
        try {
            JpaUtil.validateTransaction();
        } catch (RollbackException e) {
            JpaUtil.cancelTransaction();
        }
        
        JpaUtil.closeEntityManager();
        return true;
    }
    
    public Person registerPerson (String lastName, String firstName, String password, String mail, String cellNumber, String verificationCode) {
        JpaUtil.createEntityManager();
        
        boolean tokenExists = verificationTokenDAO.verificationTokenExists(mail, verificationCode);
        
        if( !tokenExists || personDAO.personExists(mail) ){ 
            JpaUtil.closeEntityManager();
            return null;
        }
        
        else{
            Person p = new Person(firstName, lastName, password, cellNumber, mail);
            try {
                JpaUtil.openTransaction();
                //Create person plutot que de le prendre en paramètre
                personDAO.persist(p);
                /////////////////////////////////////////Ajouter si possible la suppression du verification token utilisé
                JpaUtil.validateTransaction();
                p = personDAO.verifyPersonAccount(mail, password);  
            }

             catch (Exception e) {
                JpaUtil.cancelTransaction();
                JpaUtil.closeEntityManager();
                return null;
            }
            
            JpaUtil.closeEntityManager();
            return p;
            }
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

    public List<Service> findAllServicesWithFilter(String object, String category, String location, String date, String time, String duration, String timeUnit, String nbPts, String paymentUnit, String serviceType) throws ParseException {
        JpaUtil.createEntityManager();
        
        Date today = new Date();
        SimpleDateFormat formatNormal = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy"); 
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm"); 
        Date startingDate;
        
        if (!date.isEmpty()) {
            if (time.isEmpty()) {
                if (date.equals(formatDate.format(today))) time = formatTime.format(today);
                else  time="00:00";
                
            startingDate = formatNormal.parse(date + " " + time);
            }
            else{
                startingDate = formatNormal.parse(date + " " + time);
            }
        } else if (!time.isEmpty()) {
            startingDate = formatNormal.parse(formatDate.format(today) + " " + time) ;
        } else{
            startingDate = null;
        }
        

        Long durationInMillis = 0L;
        if (!duration.isEmpty()) {
            durationInMillis = Long.valueOf(duration);
            if (timeUnit.equals("jours")) {
                durationInMillis *= 24*60*60*1000;
            } else if (timeUnit.equals("heures")) {
                durationInMillis *= 60*60*1000;
            }  else if (timeUnit.equals("minutes")) {
                durationInMillis *= 60*1000;
            }
        }
        else{
            durationInMillis = new Long(0);
        }
        
        Date endingDate;
        if(startingDate != null){
            endingDate = formatNormal.parse( formatNormal.format(startingDate.getTime() + durationInMillis) );
        }
        else{
            endingDate = formatNormal.parse( formatNormal.format(today.getTime() + durationInMillis) );
        }
              
        List<Service> listServices = serviceDAO.findAllServicesWithFilter(object, category, location, startingDate, endingDate, nbPts, paymentUnit, serviceType);
        
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
        
        Person person = personDAO.findById(idPerson);
        
        JpaUtil.closeEntityManager();
        return person;
    }
    
    public Service getServiceById(Long idService) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        Service service = serviceDAO.findById(idService);
        
        JpaUtil.closeEntityManager();
        return service;
    }

    public Person inscription(String name, String firstName, String password, String mail, String cellNumber) {
        // TODO : to implement : persist la personne en base de données
        return new Person();
    }
    
    public boolean verifyEmailAdress(String mail) {
        // TODO : to implement : envoyer un mail et attendre la validation de celui-ci
        // Voir comment faire cela
        return false;
    }

}
