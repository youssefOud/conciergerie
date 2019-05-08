package DAO;

import Model.DeletedAccounts;
import javax.persistence.EntityManager;

/**
 * Class linking the data access layer to the delete accounts business layer
 * 
 * @author HEXA-4112
 */
public class DeletedAccountsDAO {
    
    /**
     * Gets the deleted reservation thanks to the id
     * 
     * @param id
     * @return the DeletedAccounts to the id
     */
    public DeletedAccounts findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(DeletedAccounts.class, id);
    }
    
    /**
     * Persists the parameter delAc in the database
     * 
     * @param delAc 
     */
    public void persist(DeletedAccounts delAc){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(delAc);
    }
    
    /**
     * Merges the parameter delAc in the database
     * 
     * @param delAc 
     */
    public void merge(DeletedAccounts delAc){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(delAc);
    }
    
    /**
     * Removes from the database the parameter delAc
     * 
     * @param delAc 
     */
    public void remove(DeletedAccounts delAc){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(delAc);
    }
    
}
