package DAO;

import javax.persistence.EntityManager;

import Model.Offer;

public class OfferDAO {

	public OfferDAO() {
		
	}
	
	public Offer findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Offer.class, id);
    }
    public void persist(Offer offer){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(offer);
    }
    public void merge(Offer offer){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(offer);
    }
    public void remove(Offer offer){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(offer);
    }
	
}