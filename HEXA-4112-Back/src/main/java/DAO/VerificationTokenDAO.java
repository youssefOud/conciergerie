package DAO;

import javax.persistence.EntityManager;

import Model.VerificationToken;
import java.sql.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class VerificationTokenDAO {
    
    public VerificationToken findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(VerificationToken.class, id);
    }
    
    public void persist(VerificationToken verificationToken){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(verificationToken);
    }
    
    public void merge(VerificationToken verificationToken){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(verificationToken);
    }
    
    public void remove(VerificationToken verificationToken){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(verificationToken);
    }
    
    public void removeOldTokens(Long delay){
        EntityManager em = JpaUtil.getEntityManager();
        long currentDateTime = System.currentTimeMillis()-delay;
        Date date = new Date(currentDateTime);
        System.out.println("date: " + date);
        Query query = em.createQuery("DELETE FROM VerificationToken u where :date > u.date").setParameter("date",date,TemporalType.TIMESTAMP);
        query.executeUpdate();
    }
    
    public boolean verificationTokenExists(String mail, String token){
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("SELECT u FROM VerificationToken u where u.email = :mailToVerify and u.token = :tokenToVerify");
        query.setParameter("mailToVerify", mail);
        query.setParameter("tokenToVerify", token);
        return !(((List<VerificationToken>) query.getResultList()).isEmpty());
    }
    
    public VerificationToken findByMail(String mail){
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("SELECT u FROM VerificationToken u where u.email = :mailToVerify").setParameter("mailToVerify", mail);
        if( !(((List<VerificationToken>) query.getResultList()).isEmpty()) ){
            return (VerificationToken)query.getResultList().get(0);
        }
        else{
            return null;
        }
    }
    
}