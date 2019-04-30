package DAO;

import javax.persistence.EntityManager;

import Model.Market;

public class MarketDAO {

	public MarketDAO() {
		
	}
	
	public Market findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Market.class, id);
    }
    public void persist(Market market){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(market);
    }
    public void merge(Market market){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(market);
    }
    public void remove(Market market){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(market);
    }
	
}