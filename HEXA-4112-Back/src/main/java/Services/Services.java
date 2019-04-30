package Services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.RollbackException;

import DAO.*;
import Model.Demand;
import Model.Offer;
import Model.User;

/**
 * 
 * @author B3427
 */
public class Services {
    public Services(){
        
    }
    
    // TODO : compléter
    public User connexion (String mail, String mdp) {
    	JpaUtil.createEntityManager();
    	UserDAO userDAO = new UserDAO();
    	User user = userDAO.verifyUserAccount(mail, mdp);
    	JpaUtil.closeEntityManager();
    	
    	return user;
    }
    
    // TODO : A completer : Dans l'ActionServlet, le bouton radio
    // permet de savoir si c'est une demande ou une offre
    public boolean createDemand (User user, Demand demand) {
    	JpaUtil.createEntityManager();
    	JpaUtil.openTransaction();
    	
    	// Traitement sur demand ? Date de début ?
    	
    	DemandDAO demandDAO = new DemandDAO();
    	demandDAO.persist(demand);
    	
    	try {
    		JpaUtil.validateTransaction();
    	} catch (RollbackException e) {
    		JpaUtil.cancelTransaction();
    	}
    	
    	JpaUtil.closeEntityManager();
    	return true;
    }
    
 // TODO : A completer
    public boolean createOffer (User user, Offer offer) {
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
    public List<Demand> findAllDemandsWithFilter(/*Add Filter*/) {
    	JpaUtil.createEntityManager();
    	JpaUtil.openTransaction();
    	
    	List<Demand> listDemand = new ArrayList<>();
    	
    	
    	
    	JpaUtil.closeEntityManager();
    	return listDemand;
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
    
    public User getUserById(Long idUser) {
    	JpaUtil.createEntityManager();
    	JpaUtil.openTransaction();
    	
    	UserDAO userDAO = new UserDAO();
    	User user = userDAO.findById(idUser);
    	
    	JpaUtil.closeEntityManager();
    	return user;
    }
}
