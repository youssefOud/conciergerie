package Services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.RollbackException;

import DAO.*;
import Model.Demand;
import Model.Offer;
import Model.Person;
import Model.Reservation;
import Model.Service;
import Model.VerificationToken;
import Utils.EmailSenderService;
import com.sun.media.sound.EmergencySoundbank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
    final private ReservationDAO reservationDAO;
    
    public Services(){
        this.demandDAO = new DemandDAO();
        this.offerDAO = new OfferDAO();
        this.personDAO = new PersonDAO();
        this.serviceDAO = new ServiceDAO();
        this.verificationTokenDAO = new VerificationTokenDAO();
        this.reservationDAO = new ReservationDAO();
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
        else if (verificationTokenDAO.findByMail(mail) != null){
            VerificationToken vt = verificationTokenDAO.findByMail(mail);
            String verifCode = EmailSenderService.sendVerificationEmail(mail);
            if(!verifCode.isEmpty()){
                try {
                    JpaUtil.openTransaction();
                    vt.setToken(verifCode);
                    vt.setDate(new Date());
                    verificationTokenDAO.merge(vt);
                    JpaUtil.validateTransaction();
                }
                catch(Exception e){
                    JpaUtil.cancelTransaction();
                    JpaUtil.closeEntityManager();
                    return false;
                }
            }
            else{
                JpaUtil.closeEntityManager();
                return false;
            }
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
            else{
                JpaUtil.closeEntityManager();
                return false;
            }
            
        }
        JpaUtil.closeEntityManager();
        return true;
    }
    
    //FOR TEST PURPOSES
      public boolean createPerson(Person p) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        personDAO.persist(p);
        
        try {
            JpaUtil.validateTransaction();
        } catch (RollbackException e) {
            JpaUtil.cancelTransaction();
            return false;
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
    
    public boolean createToken (VerificationToken vt) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        verificationTokenDAO.persist(vt);
        
        try {
            JpaUtil.validateTransaction();
        } catch (RollbackException e) {
            JpaUtil.cancelTransaction();
            return false;
        }
        
        JpaUtil.closeEntityManager();
        return true;
    }
    
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
                JpaUtil.validateTransaction();
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
    
    
    public boolean deleteOldTokens(Long delay) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        verificationTokenDAO.removeOldTokens(delay);
        try {
            JpaUtil.validateTransaction();
        } catch (RollbackException e) {
            JpaUtil.cancelTransaction();
            return false;
        }
        JpaUtil.closeEntityManager();
        return true;
    }
    
    public boolean createReservation(Long idReservationOwner, Long idService, String date, String time, int reservationDuration, String durationUnit){
        JpaUtil.createEntityManager();
        Person serviceOwner = null; // = personDAO.findById(idServiceOwner); 
        Person reservationOwner = personDAO.findById(idReservationOwner);
        Service service = serviceDAO.findById(idService);
        if (service != null) serviceOwner = service.getPerson();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date reservationStartingDate;
        try{
        reservationStartingDate = dateFormat.parse(date + " " + time);
        }
        catch (Exception e){
            JpaUtil.closeEntityManager();
            return false;
        }
        Date reservationRequestDate = new Date();
        
        if(serviceOwner != null && reservationOwner != null && service != null){
            try {
                JpaUtil.openTransaction();
                Reservation reservation = new Reservation(reservationOwner, service, reservationStartingDate, reservationDuration, durationUnit, reservationRequestDate);
                if(reservation.getReservationStartingDate().getTime() < service.getAvailabilityDate().getTime() || reservation.getReservationEndingDate().getTime() > service.getEndOfAvailabilityDate().getTime()){
                    JpaUtil.validateTransaction();
                    JpaUtil.closeEntityManager();
                    return false;
                }
                reservationDAO.persist(reservation);
                JpaUtil.validateTransaction();
            }
            catch(Exception e){
                JpaUtil.cancelTransaction();
                JpaUtil.closeEntityManager();
                return false;
            }
        }
        else{
            JpaUtil.closeEntityManager();
            return false;
        }
        JpaUtil.closeEntityManager();
        return true;
    }
    
    public List<Reservation> getReservationByPersonId(Long personId){
        //todo
        return null;
    }

    public boolean updateServiceState(Service service) {
        JpaUtil.createEntityManager();
        if (service == null) return false;
        Date now  = new Date();
        if (now.compareTo(service.getEndOfAvailabilityDate()) > 0) {
            try {
                JpaUtil.openTransaction();
                service.setServiceState("expired");
                serviceDAO.merge(service);
                JpaUtil.validateTransaction();
            }
            catch(Exception e){
                JpaUtil.cancelTransaction();
                JpaUtil.closeEntityManager();
                return false;
            }
        }
        return true;
    }
    
    public HashMap<Service, List<Reservation>> getAdsByPerson(Person person) {
        if (person == null) return null;
        
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
                
        List<Service> services = serviceDAO.findAllServicesByPerson(person);
        HashMap<Service,List<Reservation>> hm = new HashMap<>();
        for (Service serv :services) {
            updateServiceState(serv);
            List<Reservation> reservations = reservationDAO.findAllReservationsByService(serv);
            hm.put(serv,reservations);
        }
        JpaUtil.closeEntityManager();       
        return hm;
    }
    
    public boolean deleteService(Long serviceId){
        JpaUtil.createEntityManager();
        try {
            JpaUtil.openTransaction();
            Service serviceToRemove = serviceDAO.findById(serviceId);
            serviceDAO.remove(serviceToRemove);
            JpaUtil.validateTransaction();
        }
        catch(Exception e){
            JpaUtil.cancelTransaction();
            JpaUtil.closeEntityManager();
            return false;
        }
        JpaUtil.closeEntityManager();
        return true;
    }
    
    public int calculateReservationPrice(Long idService, int reservationDuration, String durationUnit){
        Long durationInMinutes = Long.valueOf(reservationDuration);
        if (durationUnit.equals("jours")) {
            durationInMinutes *= 24*60;
        } else if (durationUnit.equals("heures")) {
            durationInMinutes *= 60;
        }
        
        JpaUtil.createEntityManager();
        Service service = serviceDAO.findById(idService);
        String priceUnit = service.getPriceUnit();
        
        double nbPts = service.getNbPoint();
        double nbPtsInMinutes;
        
        if(priceUnit.equals("jours")){
            nbPtsInMinutes = nbPts/(60*24);
        }
        else if (priceUnit.equals("heures")){
            nbPtsInMinutes = nbPts/(60);
        }
        else{
            nbPtsInMinutes = nbPts;
        }
        JpaUtil.closeEntityManager();
        
        return (int)Math.ceil(durationInMinutes * nbPtsInMinutes);
    }
    
    
}
