package DAO;

import javax.persistence.EntityManager;
import Model.Offer;

/**
 * Class linking the data access layer to the offer business layer
 * 
 * @author HEXA-4112
 */
public class OfferDAO {
    
    /**
     * Gets the offer thanks to the id
     * 
     * @param id
     * @return the Offer corresponding to the id
     */
    public Offer findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Offer.class, id);
    }
    
    /**
     * Persists the parameter offer in the database
     * 
     * @param offer 
     */
    public void persist(Offer offer){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(offer);
    }
    
    /**
     * Merges the parameter offer in the database
     * 
     * @param offer 
     */
    public void merge(Offer offer){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(offer);
    }
    
    /**
     * Removes the parameter offer in the database 
     * 
     * @param offer 
     */
    public void remove(Offer offer){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(offer);
    }
    
}