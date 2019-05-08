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
import com.sun.media.sound.EmergencySoundbank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import javafx.util.Pair;

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
//            if (s1.getServiceState()>s2.getServiceState()) return s2.getServiceState();
//            return s1.getServiceState();
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
    
    public Person inscription(String name, String firstName, String password, String mail, String cellNumber) {
        // TODO : to implement : persist la personne en base de données
        return new Person();
    }
    
    public boolean verifyEmailAdress(String mail) {
        // TODO : to implement : envoyer un mail et attendre la validation de celui-ci
        // Voir comment faire cela
        return false;
    }
    
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
        
        // TODO : checker mot obscene
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
    
    public Pair<List<Service>,Integer> findAllServicesWithFilter(Long idPerson, String object, String category, String location, String date, String time, String duration, String timeUnit, String nbPts, String paymentUnit, String serviceType) throws ParseException {
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
        
        List<Service> filteredServices = serviceDAO.findAllServicesWithFilter(object, category, location, startingDate, endingDate, nbPts, paymentUnit, serviceType);
        
        Person user = personDAO.findById(idPerson);
        
        int nbPropositions = 0;
        //On retrouve les éléments susceptibles d'intéresser l'utilisateur et on les met en début de liste
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
        return new Pair(filteredServices,nbPropositions);
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
  
     public Pair<Boolean, String> createReservation(Long idReservationOwner, Long idService, String date, String time, int reservationDuration, String durationUnit, 
                                                    String pictures, String description, String location){
       JpaUtil.createEntityManager();
        Person serviceOwner = null; // = personDAO.findById(idServiceOwner); 
        Person reservationOwner = personDAO.findById(idReservationOwner);
        Service service = serviceDAO.findById(idService);
        if (service != null) serviceOwner = service.getPerson();
        if (serviceOwner.getId() == idReservationOwner) {
            JpaUtil.closeEntityManager();
            return new Pair<>(false, "Vous ne pouvez pas répondre à votre propre annonce.");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date reservationStartingDate;
        try{
            reservationStartingDate = dateFormat.parse(date + " " + time);
        }
        catch (Exception e){
            JpaUtil.closeEntityManager();
            return new Pair<>(false, "La requête n'a pas pu aboutir. Veuillez réessayer ultérieurement.");
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
                    return new Pair<>(false, "Les dates saisies ne sont pas valides. Veuillez réessayer.");
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
                    if (pictures != null) reservation.setPictures(pictures);
                    if (description != null) reservation.setPictures(description);
                    if (location != null) reservation.setLocation(location);
                }

                if(demandOwner.getPointBalance() >= reservation.getReservationPrice()){
                    offerOwner.setPointBalance(offerOwner.getPointBalance() +  reservation.getReservationPrice());
                    demandOwner.setPointBalance(demandOwner.getPointBalance() -  reservation.getReservationPrice());
                    System.out.println("");
                }
                else{
                    JpaUtil.cancelTransaction();
                    JpaUtil.closeEntityManager();
                    return new Pair<> (false,"Votre solde est insuffisant pour réaliser cette opération.");
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
                return new Pair<>(false, "La requête n'a pas pu aboutir. Veuillez réessayer ultérieurement.");
            }
        }
        else{
            JpaUtil.closeEntityManager();
            return new Pair<>(false, "La requête n'a pas pu aboutir. Veuillez réessayer ultérieurement.");
        }
        JpaUtil.closeEntityManager();
        return new Pair<>(true, "Votre demande a bien été prise en compte");
    
    }
    
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
    
    public boolean deleteService(Long serviceId){
        JpaUtil.createEntityManager();
        try {
            JpaUtil.openTransaction();
            ///////////////////////////////////////  Supprimer aussi toutes les réservations qui sont en lien avec le service
            
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
    
    public Pair<Boolean, String> confirmReservation(Long idReservation){ //On retourne le message d'erreur ou de confirmation
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
                return new Pair<> (true,"Erreur : Solde insuffisant pour réaliser cette demande");
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
            return new Pair<> (false,"Erreur : Une erreure s'est produite");
        }
        JpaUtil.closeEntityManager();
        return new Pair<> (true,"Demande validée !");
    }
    
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
    
    public HashMap<Service,Reservation> getInterests(Person person) {
        if (person == null) return null;
        JpaUtil.createEntityManager();
        // JpaUtil.openTransaction();
        
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
    
    public List<Service> matchMakingForOffer(Long idService){
        JpaUtil.createEntityManager();
        
        Service service = serviceDAO.findById(idService);
        List<Service> services = serviceDAO.matchMaking(service, 0);
        
        Person person = service.getPerson();
        person.addSSupposedlyInterestingDemands(services);
        
       
        JpaUtil.openTransaction();
        personDAO.merge(person);
        JpaUtil.validateTransaction();
        
        
        JpaUtil.closeEntityManager();
        return services;
    }
    
    public List<Service> matchMakingForDemand(Long idService){
        JpaUtil.createEntityManager();
        
        Service service = serviceDAO.findById(idService);
        List<Service> services = serviceDAO.matchMaking(service, 1);
        
        Person person = service.getPerson();
        List<Service> supposedlyInterestingOffers = person.getSupposedlyInterestingOffers();
        supposedlyInterestingOffers.addAll(services);
        person.setSupposedlyInterestingOffers(supposedlyInterestingOffers);
        personDAO.merge(person);
        
        JpaUtil.closeEntityManager();
        return services;
    }
    
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
                    System.out.println("avg" + avg);
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
        //JpaUtil.createEntityManager();
        Reservation res = reservationDAO.findById(id);
        //JpaUtil.closeEntityManager();
        return res;
    }
    
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
                    System.out.println("avg : " + avg);
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

