package DAO;

import javax.persistence.EntityManager;
import Model.Demand;

/**
 * Class linking the data access layer to the demand business layer
 * 
 * @author HEXA-4112
 */
public class DemandDAO {
    
    /**
     * Gets the demand thanks to the id
     * 
     * @param id
     * @return the Demand corresponding to the id
     */
    public Demand findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Demand.class, id);
    }
    
    /**
     * Persists the parameter demand in the database
     * 
     * @param demand 
     */
    public void persist(Demand demand){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(demand);
    }
    
    /**
     * Merges the parameter demand in the database
     * 
     * @param demand 
     */
    public void merge(Demand demand){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(demand);
    }
    
    /**
     * Removes the parameter demand in the database
     * 
     * @param demand 
     */
    public void remove(Demand demand){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(demand);
    }
    
}