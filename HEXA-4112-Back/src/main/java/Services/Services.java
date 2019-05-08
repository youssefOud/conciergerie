package Services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.RollbackException;

import DAO.*;
import Model.DeletedAccounts;
import Model.Demand;
import Model.Offer;
import Model.Person;
import Model.Reservation;
import Model.Service;
import Model.VerificationToken;
import Utils.EmailSenderService;
import Utils.Moderation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Services {
    
    final private DemandDAO demandDAO;
    final private OfferDAO offerDAO;
    final private PersonDAO personDAO;
    final private ServiceDAO serviceDAO;
    final private VerificationTokenDAO verificationTokenDAO;
    final private ReservationDAO reservationDAO;
    final private DeletedAccountsDAO deletedAccountsDAO;
    
    Comparator<Service> compareByServiceState = new Comparator<Service>() {
        @Override
        public int compare(Service s1, Service s2) {
            return s1.getServiceState().compareTo(s2.getServiceState());
        }
    };
    
    public Services(){
        this.demandDAO = new DemandDAO();
        this.offerDAO = new OfferDAO();
        this.personDAO = new PersonDAO();
        this.serviceDAO = new ServiceDAO();
        this.verificationTokenDAO = new VerificationTokenDAO();
        this.reservationDAO = new ReservationDAO();
        this.deletedAccountsDAO = new DeletedAccountsDAO();
    }
    
    /**
     * Sets privileged contact(email or phone, that will be lately used to contact the user) according to user's preferences
     * @param idPerson
     * @param privilegedContact
     * @param cellNumber
     * @return true if operation is successful, false if not
     */
    public boolean savePrivilegedContact(Long idPerson, String privilegedContact, String cellNumber) {
        JpaUtil.createEntityManager();
        
        Person person = personDAO.findById(idPerson);
        
        if (!cellNumber.equals("")) {
            person.setCellNumber(cellNumber);
        }
        
        if (privilegedContact.equals("email")) {
            person.setPrivilegedContact(privilegedContact);
        } else {
            person.setPrivilegedContact("cellphone");
        }
        
        try {
            JpaUtil.openTransaction();
            personDAO.merge(person);
            JpaUtil.validateTransaction();
        }
        catch(RollbackException e){
            JpaUtil.cancelTransaction();
            JpaUtil.closeEntityManager();
            return false;
        }
        
        JpaUtil.closeEntityManager();
        return true;
    }
    
    
    public Person connectPerson (String mail, String mdp) {
        JpaUtil.createEntityManager();
        Person person = personDAO.verifyPersonAccount(mail, mdp);
        JpaUtil.closeEntityManager();        
        return person;
    }
    
    /**
     * Sends an email with a verification code during inscription procedure
     * @param mail
     * @return true if operation is successful, false if not
     */
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
    
    /**
     * Inserts the person passed in parameter in the database.
     * @return true if operation is successful, false if not
     */
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
    
    /**
     * Inserts the demand passed in parameter in the database. If the demand contains obscene words, it will not be inserted.
     * @return true if operation is successful, false if not
     */
    public boolean createDemand (Demand demand) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();

        String word = Moderation.checkObsceneWords(demand);
        if (word.equals("")) {
            demandDAO.persist(demand);
        } else {
            JpaUtil.cancelTransaction();
            JpaUtil.closeEntityManager();
            return false;
        }
        
        try {
            JpaUtil.validateTransaction();
        } catch (RollbackException e) {
            JpaUtil.cancelTransaction();
            JpaUtil.closeEntityManager();
            return false;
        }
        
        this.matchMakingForDemand(demand.getId());
        
        JpaUtil.closeEntityManager();
        return true;
    }
    
    /**
     * Inserts the offer passed in parameter in the database. If the offer contains obscene words, it will not be inserted.
     * @return true if operation is successful, false if not
     */
    public boolean createOffer (Offer offer) {
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        String word = Moderation.checkObsceneWords(offer);
        if (word.equals("")) {
            offerDAO.persist(offer);
        } else {
            JpaUtil.cancelTransaction();
            JpaUtil.closeEntityManager();
            return false;
        }
        
        try {
            JpaUtil.validateTransaction();
        } catch (RollbackException e) {
            JpaUtil.cancelTransaction();
        }
        this.matchMakingForOffer(offer.getId());
        
        JpaUtil.closeEntityManager();
        return true;
    }
    
     /**
     * Inserts the demand passed in parameter in the database.
     * @return true if operation is successful, false if not
     */
   
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
    
    /**
     * Registers a new user. Checks if the email does not already exist in the database.
     * @return the user if the operation is successful, null if operation fails
     */    
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
    
   
    /**
     * Searches the list of services corresponding to the criteria passed in parameters. The first elements of the list represent services that could interest the user.
     * @return a pair : the key is the list of services; the value is the number of services that could interest the user
     */
    
    public Map.Entry findAllServicesWithFilter(Long idPerson, String object, String category, String location, String date, String time, String duration, String timeUnit, String nbPts, String paymentUnit, String serviceType) throws ParseException {
        JpaUtil.createEntityManager();
        
        //checks if starting date and time are not empty and parse the strings to a date
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

        // conversion of the service's duration in millis
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
        
        //checks if ending date and time are not empty and parse the strings to a date
        Date endingDate;
        if(startingDate != null){
            endingDate = formatNormal.parse( formatNormal.format(startingDate.getTime() + durationInMillis) );
        }
        else{
            endingDate = formatNormal.parse( formatNormal.format(today.getTime() + durationInMillis) );
        }

        List<Service> filteredServices = serviceDAO.findAllServicesWithFilter(object, category, location, startingDate, endingDate, nbPts, paymentUnit, serviceType);
        
        Person user = personDAO.findById(idPerson);
        
        int nbPropositions = 0;
        //Find the services that could interest the user from the previous list (filteredServices) and put them on top of the list.
        if(!serviceType.equals("demande")){
            List<Service> supposedlyInterestingOffers = user.getSupposedlyInterestingOffers();
            List<Service> offersToRemove = new ArrayList<>();
            for(Service s : supposedlyInterestingOffers){
                updateServiceState(s);
                if(s.getServiceState() != 0){
                    offersToRemove.add(s);
                }
            }
            for(Service s : offersToRemove){
                supposedlyInterestingOffers.remove(s);
            }
            user.setSupposedlyInterestingOffers(supposedlyInterestingOffers);
            try{
            JpaUtil.openTransaction();
            personDAO.merge(user);
            JpaUtil.validateTransaction();
            }
            catch (Exception e){
                JpaUtil.cancelTransaction();
                JpaUtil.closeEntityManager();
                return null;
            }
            
            for(Service s : supposedlyInterestingOffers){
                if(filteredServices.contains(s)){
                    filteredServices.remove(s);
                    filteredServices.add(0,s);
                    nbPropositions++;
                }
            }
        }
        
        if(!serviceType.equals("offre")){
            List<Service> supposedlyInterestingDemands = user.getSupposedlyInterestingDemands();
            List<Service> demandsToRemove = new ArrayList<>();
            for(Service s : supposedlyInterestingDemands){
                updateServiceState(s);
                if(s.getServiceState() != 0){
                    demandsToRemove.add(s);
                }
            }
            for(Service s : demandsToRemove){
                supposedlyInterestingDemands.remove(s);
            }
            user.setSupposedlyInterestingDemands(supposedlyInterestingDemands);
            try{
            JpaUtil.openTransaction();
            personDAO.merge(user);
            JpaUtil.validateTransaction();
            }
            catch (Exception e){
                JpaUtil.cancelTransaction();
                JpaUtil.closeEntityManager();
                return null;
            }
            
            for(Service s : supposedlyInterestingDemands){
                if(filteredServices.contains(s)){
                    filteredServices.remove(s);
                    filteredServices.add(0,s);
                    nbPropositions++;
                }
            }
        } 
        
        JpaUtil.closeEntityManager();
        return new AbstractMap.SimpleEntry(filteredServices,nbPropositions);
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
        updateServiceState(service); 
        JpaUtil.closeEntityManager();
        return service;
    }
    
    /**
     * Delete verification tokens from database after a certain period.
     * @param delay : specifies the period 
     * @return true if operation is successful, false if not
     */
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
    
    /**
     * Creates a reservation with the information provided by the user responding to a service.
     * Checks if dates are available and if the point balance is sufficient to make the transaction. Sends emails to the offer and the demand makers.
     * @return a pair with a boolean as a key, indicating if operation was successful and a string containing an informative message as a value
     */
    public Map.Entry createReservation(Long idReservationOwner, Long idService, String date, String time, int reservationDuration, String durationUnit){
        JpaUtil.createEntityManager();
        Person serviceOwner = null; 
        Person reservationOwner = personDAO.findById(idReservationOwner);
        Service service = serviceDAO.findById(idService);
        if (service != null) serviceOwner = service.getPerson();
        if (serviceOwner.getId() == idReservationOwner) {
            JpaUtil.closeEntityManager();
            return new AbstractMap.SimpleEntry(false, "Vous ne pouvez pas répondre à votre propre annonce.");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date reservationStartingDate;
        try{
            reservationStartingDate = dateFormat.parse(date + " " + time);
        }
        catch (Exception e){
            JpaUtil.closeEntityManager();
            return new AbstractMap.SimpleEntry(false, "La requête n'a pas pu aboutir. Veuillez réessayer ultérieurement.");
        }
        Date reservationRequestDate = new Date();
        
        if(serviceOwner != null && reservationOwner != null && service != null){
            try {
                JpaUtil.openTransaction();
                Reservation reservation = new Reservation(reservationOwner, service, reservationStartingDate, reservationDuration, durationUnit, reservationRequestDate);
                //Dates validity checking
                if(reservation.getReservationStartingDate().getTime() < service.getAvailabilityDate().getTime() || reservation.getReservationEndingDate().getTime() > service.getEndOfAvailabilityDate().getTime()){
                    JpaUtil.validateTransaction();
                    JpaUtil.closeEntityManager();
                    return new AbstractMap.SimpleEntry(false, "Les dates saisies ne sont pas valides. Veuillez réessayer.");
                }
                
                //Point balance checking
                Person offerOwner;
                Person demandOwner;
                if(reservation.getService() instanceof Offer){
                    offerOwner = reservation.getService().getPerson();
                    demandOwner = reservation.getReservationOwner();
                }
                else {
                    offerOwner = reservation.getReservationOwner();
                    demandOwner = reservation.getService().getPerson();
                }

                if(demandOwner.getPointBalance() < reservation.getReservationPrice()){
                    JpaUtil.cancelTransaction();
                    JpaUtil.closeEntityManager();
                    return new AbstractMap.SimpleEntry (false,"Votre solde est insuffisant pour réaliser cette opération.");
                }
                
                String demandOwnerContact = ((demandOwner.getPrivilegedContact().equals("email")) ? demandOwner.getMail() : demandOwner.getCellNumber());
                String offerOwnerContact = ((offerOwner.getPrivilegedContact().equals("email")) ? offerOwner.getMail() : offerOwner.getCellNumber());
                
                EmailSenderService.sendDemandReservationEmail(demandOwner.getMail(), reservation.getService().getNameObject(), dateFormat.format(reservation.getReservationStartingDate()), dateFormat.format(reservation.getReservationEndingDate()), offerOwner.getFirstName(), offerOwnerContact, reservation.getReservationPrice());
                EmailSenderService.sendOfferReservationEmail(offerOwner.getMail(), reservation.getService().getNameObject(), dateFormat.format(reservation.getReservationStartingDate()), dateFormat.format(reservation.getReservationEndingDate()), demandOwner.getFirstName(), demandOwnerContact, reservation.getReservationPrice());
                
                reservationDAO.persist(reservation);
                JpaUtil.validateTransaction();
            }
            catch(Exception e){
                JpaUtil.cancelTransaction();
                JpaUtil.closeEntityManager();
                return new AbstractMap.SimpleEntry(false, "La requête n'a pas pu aboutir. Veuillez réessayer ultérieurement.");
            }
        }
        else{
            JpaUtil.closeEntityManager();
            return new AbstractMap.SimpleEntry(false, "La requête n'a pas pu aboutir. Veuillez réessayer ultérieurement.");
        }
        JpaUtil.closeEntityManager();
        return new AbstractMap.SimpleEntry(true, "Votre demande a bien été prise en compte");
    }
    
    /**
     * Updates service state. 
     * Available states : 0 if valid; 1 if expired; 2 if closed
     * @return true if operation is successful, false if not
     */
    public boolean updateServiceState(Service service) {
        JpaUtil.createEntityManager();
        if (service == null) return false;
        Date now  = new Date();
        if (now.compareTo(service.getEndOfAvailabilityDate()) > 0) {
            try {
                JpaUtil.openTransaction();
                service.setServiceState(1);
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
    
    /**
     * Updates reservation state. 
     * Available states : 0 for pending; 1 for accepted; 2 for refused; 3 for expired; 4 for rated by service owner; 
     * 5 for rated by reservation owner; 6 for rated by both
     * @return true if operation is successful, false if not
     */
    public boolean updateReservationState(Reservation res) {
        JpaUtil.createEntityManager();
        if (res == null) return false;
        Date now  = new Date();
        if (res.getReservationOwnerRating() != -1 && res.getServiceOwnerRating() != -1) {
            try {
                JpaUtil.openTransaction();
                res.setReservationState(6);
                reservationDAO.merge(res);
                JpaUtil.validateTransaction();
            }
            catch(Exception e){
                JpaUtil.cancelTransaction();
                JpaUtil.closeEntityManager();
                return false;
            }
        } 
        else if (res.getReservationOwnerRating() != -1 || res.getServiceOwnerRating() != -1) {
            try {
                JpaUtil.openTransaction();
                if (res.getReservationOwnerRating() != -1) 
                    res.setReservationState(5);
                else 
                    res.setReservationState(4);
                reservationDAO.merge(res);
                JpaUtil.validateTransaction();
            }
            catch(Exception e){
                JpaUtil.cancelTransaction();
                JpaUtil.closeEntityManager();
                return false;
            }
        } 
        else if (now.compareTo(res.getReservationEndingDate()) > 0) {
            try {
                JpaUtil.openTransaction();
                res.setReservationState(3);
                reservationDAO.merge(res);
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
    
    /**
     * Gets a list of services proposed by the user. For each service, gets a list of associated reservations, sorted by date.
     * The list of services is sorted: valid services come before expired ones.
     * @param person 
     * @return a hashmap containing all services for the specified user
     */
    public HashMap<Service, List<Reservation>> getAdsByPerson(Person person) {
        if (person == null) return null;
        
        JpaUtil.createEntityManager();
        JpaUtil.openTransaction();
        
        List<Service> services = serviceDAO.findAllServicesByPerson(person);
        HashMap<Service,List<Reservation>> hm = new HashMap<>();
        for (Service serv :services) {
            updateServiceState(serv);
        }
        Collections.sort(services,compareByServiceState);
        for (Service serv :services) {
            List<Reservation> reservations = reservationDAO.findAllReservationsByService(serv);
            for (Reservation res :reservations) {
                updateReservationState(res);
            }
            hm.put(serv,reservations);
        }
        JpaUtil.closeEntityManager();
        return hm;
    }
    
    /**
     * Deletes a service and the associated reservations
     * @param serviceId : id of the service to delete
     */
    public boolean deleteService(Long serviceId){
        JpaUtil.createEntityManager();
        try {
            JpaUtil.openTransaction();            
            List<Reservation> reservations = reservationDAO.findAllReservationsByService(serviceDAO.findById(serviceId));
            
            for(Reservation r: reservations){
                reservationDAO.remove(r);
            }
            
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
    
    /**
     * Calculates reservation price using the duration and durationUnit
     * @param idService
     * @param reservationDuration : number of duration units
     * @param durationUnit : jours, heures ou minutes
     * @return 
     */
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
        // units conversion to minutes
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
    
    /**
     * Checks if a reservation can be confirmed. If so, updates reservation state to accepted and service state to closed. Makes the points transaction between the offer and demand makers.
     * @return a pair with a boolean as a key, indicating if operation was successful and a string containing an informative message as a value
     */
    public Map.Entry confirmReservation(Long idReservation){ 
        JpaUtil.createEntityManager();
        Reservation reservation = reservationDAO.findById(idReservation);
        try{
            JpaUtil.openTransaction();
            
            //Check point balance
            Person offerOwner;
            Person demandOwner;
            if(reservation.getService() instanceof Offer){
                offerOwner = reservation.getService().getPerson();
                demandOwner = reservation.getReservationOwner();
                
            }
            else{
                offerOwner = reservation.getReservationOwner();
                demandOwner = reservation.getService().getPerson();
            }
            
            if(demandOwner.getPointBalance() >= reservation.getReservationPrice()){
                offerOwner.setPointBalance(offerOwner.getPointBalance() +  reservation.getReservationPrice());
                demandOwner.setPointBalance(demandOwner.getPointBalance() -  reservation.getReservationPrice());
            }
            else{
                JpaUtil.cancelTransaction();
                JpaUtil.closeEntityManager();
                return new AbstractMap.SimpleEntry (true,"Erreur : Solde insuffisant pour réaliser cette demande");
            }
            
            reservation.getService().setServiceState(2);
            reservation.setReservationState(1);
            serviceDAO.merge(reservation.getService());
            reservationDAO.merge(reservation);
            
            //Email sending
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            EmailSenderService.sendDemandConfirmationEmail(demandOwner.getMail(), reservation.getService().getNameObject(), dateFormat.format(reservation.getReservationStartingDate()), dateFormat.format(reservation.getReservationEndingDate()));
            
            EmailSenderService.sendOfferConfirmationEmail(offerOwner.getMail(), reservation.getService().getNameObject(), dateFormat.format(reservation.getReservationStartingDate()), dateFormat.format(reservation.getReservationEndingDate()), demandOwner.getFirstName(), demandOwner.getPrivilegedContact());
            JpaUtil.validateTransaction();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            JpaUtil.cancelTransaction();
            JpaUtil.closeEntityManager();
            return new AbstractMap.SimpleEntry (false,"Erreur : Une erreure s'est produite");
        }
        JpaUtil.closeEntityManager();
        return new AbstractMap.SimpleEntry (true,"Demande validée !");
    }
    
    /**
     * Updates reservation state to 2(declined)
     * @param idReservation
     * @return 
     */
    public boolean declineReservation(Long idReservation){
        JpaUtil.createEntityManager();
        Reservation reservation = reservationDAO.findById(idReservation);
        try{
            JpaUtil.openTransaction();
            reservation.setReservationState(2);
            reservationDAO.merge(reservation);
            JpaUtil.validateTransaction();
        }
        catch (Exception e){
            JpaUtil.cancelTransaction();
            JpaUtil.closeEntityManager();
            return false;
        }
        JpaUtil.closeEntityManager();
        return true;
    }
    
    /**
     * Deletes a reservation done by the connected user
     * @param idReservation
     * @return 
     */
    public boolean deleteInterest(Long idReservation) {
        JpaUtil.createEntityManager();
        Reservation reservation = reservationDAO.findById(idReservation);
        if (reservation == null) return false;
        try{
            JpaUtil.openTransaction();
            reservationDAO.remove(reservation);
            JpaUtil.validateTransaction();
        }
        catch (Exception e){
            JpaUtil.cancelTransaction();
            JpaUtil.closeEntityManager();
            return false;
        }
        JpaUtil.closeEntityManager();
        return true;
    }
    
    /**
     * Gets a list of services and, for each service, the associated reservation done by the connected user
     * @param person : connected user
     * @return a hashmap with service as key and reservation as value
     */
    public HashMap<Service,Reservation> getInterests(Person person) {
        if (person == null) return null;
        JpaUtil.createEntityManager();
        List<Object[]> interests = serviceDAO.findInterestsByPerson(person);
        HashMap<Service,Reservation> hm= new HashMap<Service, Reservation>();
        for (Object[] i :interests) {
            updateServiceState((Service)i[1]);
            updateReservationState((Reservation)i[0]);
            
        }
        for (Object[] i :interests) {
            hm.put((Service)i[1], (Reservation)i[0]);
        }
        JpaUtil.closeEntityManager();
        return hm;
        
    }
    
    /**
     * Creates a new user with unknown identity that will replace all deleted users. Replaces the user to delete with the user with unknown identity.
     * Updates all services and reservations created by the deleted user.
     * @param idPerson
     * @return 
     */
    public boolean deletePerson(Long idPerson){
        JpaUtil.createEntityManager();
        try{
            
            addToDeletedAccounts(personDAO.findById(idPerson));
            //Creer utilisateur supprimé si existe pas déjà
            Person deletedPerson;
            if(!personDAO.personExists("Utilisateur de Campus Exchange")){
                deletedPerson = new Person("", "", "", "", "Utilisateur de Campus Exchange");
                JpaUtil.openTransaction();
                personDAO.persist(deletedPerson);
                JpaUtil.validateTransaction();
            }
            else{
                deletedPerson = personDAO.findByMail("Utilisateur de Campus Exchange");
            }
            
            //Pour toutes les annonces remplacer l'ancien utilisateur par le nouveau (qui correspon à un compte supprimé)
            Person personToDelete = (personDAO.findById(idPerson));
            
            List<Service> services = serviceDAO.findAllServicesByPerson(personToDelete);
            for(Service s : services){
                s.setPerson(deletedPerson);
                try {
                    JpaUtil.openTransaction();
                    serviceDAO.merge(s);
                    JpaUtil.validateTransaction();
                    System.out.println("");
                } catch (Exception e){
                    JpaUtil.cancelTransaction();
                    JpaUtil.closeEntityManager();
                    return false;
                }
            }
            
            List<Reservation> reservations = reservationDAO.findAllReservationsByReservationOwner(personToDelete);
            
            for(Reservation r : reservations){
                if(r.getServiceOwner().getId().equals(idPerson)){
                    r.setServiceOwner(deletedPerson);
                }
                else{
                    r.setReservationOwner(deletedPerson);
                }
                try {
                    JpaUtil.openTransaction();
                    reservationDAO.merge(r);
                    JpaUtil.validateTransaction();
                }catch (Exception e){
                    JpaUtil.cancelTransaction();
                    JpaUtil.closeEntityManager();
                    return false;
                }
            }
            
            try {
                JpaUtil.openTransaction();
                personDAO.remove(personToDelete);
                JpaUtil.validateTransaction();
            } catch (Exception e){
                JpaUtil.cancelTransaction();
                JpaUtil.closeEntityManager();
                return false;
            }
            
        }
        catch (Exception e){
            JpaUtil.cancelTransaction();
            JpaUtil.closeEntityManager();
            return false;
        }
        JpaUtil.closeEntityManager();
        return true;
    }
    
    /**
     * Adds the mail, the point balance and the rating of the person that should be deleted to a table in the database, containing all deleted accounts.
     * @param person : user to be deleted
     * @return 
     */
    public boolean addToDeletedAccounts(Person person) {
        if (person != null) {
            DeletedAccounts da = new DeletedAccounts(person.getMail(), person.getPointBalance(), person.getRating());
            try{
                JpaUtil.openTransaction();
                deletedAccountsDAO.persist(da);
                JpaUtil.validateTransaction();
            }
            catch (Exception e){
                JpaUtil.cancelTransaction();
                JpaUtil.closeEntityManager();
                return false;
            }
        }
        return true;
    }
    
    /**
     * Sends an email to the moderator to report an ad (containing obscene words, pictures, etc.)
     * @param person : user reporting the ad
     * @param idAd
     * @return 
     */
    public boolean reportAd(Person person, Long idAd) {
        Service ad = getServiceById(idAd);
        Long idPerson;
        if (ad instanceof Demand) {
            if (ad.getPersonDemanding() != null) {
                idPerson = ad.getPersonDemanding().getId();
            } else {
                return false;
            }
        } else {
            if (ad.getPersonOffering() != null) {
                idPerson = ad.getPersonOffering().getId();
            } else {
                return false;
            }
        }
        // Verification que la personne qui signale n'est pas celle qui a posté l'annonce
        if (idPerson != person.getId()) {
            boolean reported = EmailSenderService.sendEmailModeratorReportAd(idAd, person);
            return reported;
        }
        
        return false;
    }
    
    /**
     * Creates a list of services matching to an offer 
     * @param idService: id of the offer
     * @return list of services matching to the offer
     */
    public List<Service> matchMakingForOffer(Long idService){
      
        Service service = serviceDAO.findById(idService);
        List<Service> services = serviceDAO.matchMaking(service, 0);
        
        Person person = service.getPerson();
        person.addSSupposedlyInterestingDemands(services);

        JpaUtil.openTransaction();
        personDAO.merge(person);
        JpaUtil.validateTransaction();
        
        return services;
    }
    
    /**
     * Creates a list of services matching to a demand 
     * @param idService: id of the demand
     * @return list of services matching to the demand
     */
    public List<Service> matchMakingForDemand(Long idService){
        
        Service service = serviceDAO.findById(idService);
        List<Service> services = serviceDAO.matchMaking(service, 1);
        
        Person person = service.getPerson();
        person.addSSupposedlyInterestingOffers(services);

        JpaUtil.openTransaction();
        personDAO.merge(person);
        JpaUtil.validateTransaction();
        
        return services;
    }
    
    /**
     * Updates reservation by taking into account the rating given by the service owner. Updates reservation owner's rating.
     * @param reservationId
     * @param rating
     * @return 
     */
    public boolean rateReservationByServiceOwner(Long reservationId, int rating) {
        JpaUtil.createEntityManager();
        
        Reservation r = getReservationById(reservationId);
        if (r != null) {
            Person reservationOwner = r.getReservationOwner();
            
            if (reservationOwner != null ){
                r.setServiceOwnerRating(rating);
                int nbRatings = reservationOwner.getNbRatings();
                double avg;
                if ((int)reservationOwner.getRating() == -1)
                    avg = (double)(rating)/(double)(nbRatings + 1);
                else
                    avg = (double)((double)nbRatings*(double)reservationOwner.getRating()+(double)rating)/(double)(nbRatings + 1);
                reservationOwner.setRating(avg);
                reservationOwner.setNbRatings(nbRatings + 1);
                try {
                    JpaUtil.openTransaction();
                    reservationDAO.merge(r);
                    personDAO.merge(reservationOwner);
                    JpaUtil.validateTransaction();
                }
                catch(Exception e){
                    JpaUtil.cancelTransaction();
                    JpaUtil.closeEntityManager();
                    return false;
                }
            }
            updateReservationState(r);
        }
        
        JpaUtil.closeEntityManager();
        return true;
    }
    
    public Reservation getReservationById(Long id) {
        Reservation res = reservationDAO.findById(id);
        return res;
    }
    
    /**
     * Updates reservation by taking into account the rating given by reservation owner. Updates service owner's rating.
     * @param reservationId
     * @param rating
     * @return 
     */
    public boolean rateReservationByReservationOwner(Long reservationId, int rating) {
        JpaUtil.createEntityManager();
        Reservation r = getReservationById(reservationId);
        if (r != null) {
            Person serviceOwner = r.getServiceOwner();
            if (serviceOwner != null ){
                r.setReservationOwnerRating(rating);
                int nbRatings = serviceOwner.getNbRatings();
                double avg;
                if ((int)serviceOwner.getRating() == -1)
                    avg = (double)(rating)/(double)(nbRatings + 1);
                else
                    avg = (double) ((double)nbRatings*(double)serviceOwner.getRating()+(double)rating)/(double)(nbRatings + 1);
                serviceOwner.setRating(avg);
                serviceOwner.setNbRatings(nbRatings + 1);
                try {
                    JpaUtil.openTransaction();
                    reservationDAO.merge(r);
                    personDAO.merge(serviceOwner);
                    JpaUtil.validateTransaction();
                }
                catch(Exception e){
                    JpaUtil.cancelTransaction();
                    JpaUtil.closeEntityManager();
                    return false;
                }
            }
            updateReservationState(r);
        }
        
        JpaUtil.closeEntityManager();
        return true;
    }
}

