package DAO;

import javax.persistence.EntityManager;

import Model.VerificationToken;

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
    
}