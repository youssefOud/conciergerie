package DAO;

import javax.persistence.EntityManager;

import Model.VerificationToken;
import java.util.List;
import javax.persistence.Query;

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
    
    public boolean verificationTokenExists(String mail, String token){
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("SELECT u FROM VerificationToken u where u.email = :mailToVerify and u.token = :tokenToVerify");
        query.setParameter("mailToVerify", mail);
        query.setParameter("tokenToVerify", token);
        return !(((List<VerificationToken>) query.getResultList()).isEmpty());
    }
    
}