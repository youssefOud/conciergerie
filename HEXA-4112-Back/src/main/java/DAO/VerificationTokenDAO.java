package DAO;

import javax.persistence.EntityManager;

import Model.VerificationToken;
import java.sql.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Class linking the data access layer to the verification token business layer
 * 
 * @author HEXA-4112
 */
public class VerificationTokenDAO {
    
    /**
     * Gets the verification token thanks to the id 
     * 
     * @param id
     * @return the VerificationToken corresponding to the id
     */
    public VerificationToken findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(VerificationToken.class, id);
    }
    
    /**
     * Persists the parameter verificationToken in the database
     * 
     * @param verificationToken 
     */
    public void persist(VerificationToken verificationToken){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(verificationToken);
    }
    
    /**
     * Merges the parameter verificationToken in the database
     * 
     * @param verificationToken 
     */
    public void merge(VerificationToken verificationToken){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(verificationToken);
    }
    
    /**
     * Removes the parameter verificationToken in the database
     * 
     * @param verificationToken 
     */
    public void remove(VerificationToken verificationToken){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(verificationToken);
    }
    
    /**
     * Removes the old tokens e.g. when the time limit is exceeded
     * 
     * @param delay 
     */
    public void removeOldTokens(Long delay){
        EntityManager em = JpaUtil.getEntityManager();
        long currentDateTime = System.currentTimeMillis()-delay;
        Date date = new Date(currentDateTime);
        Query query = em.createQuery("DELETE FROM VerificationToken u where :date > u.date").setParameter("date",date,TemporalType.TIMESTAMP);
        query.executeUpdate();
    }
    
    /**
     * Verifies that the token attached to the mail exists 
     * 
     * @param mail
     * @param token
     * @return true if it exists, false otherwise
     */
    public boolean verificationTokenExists(String mail, String token){
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("SELECT u FROM VerificationToken u where u.email = :mailToVerify and u.token = :tokenToVerify");
        query.setParameter("mailToVerify", mail);
        query.setParameter("tokenToVerify", token);
        return !(((List<VerificationToken>) query.getResultList()).isEmpty());
    }
    
    /**
     * Gets the verification token thanks to the mail 
     * 
     * @param mail
     * @return the VerificationToken attached to the mail, null if it
     * doesn't exist
     */
    public VerificationToken findByMail(String mail){
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("SELECT u FROM VerificationToken u where u.email = :mailToVerify").setParameter("mailToVerify", mail);
        if( !(((List<VerificationToken>) query.getResultList()).isEmpty()) ){
            return ((List<VerificationToken>)query.getResultList()).get(0);
        }
        else{
            return null;
        }
    }
    
}