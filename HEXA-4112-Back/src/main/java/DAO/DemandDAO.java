package DAO;

import javax.persistence.EntityManager;

import Model.Demand;

public class DemandDAO {

	public DemandDAO() {
		
	}
	
	public Demand findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Demand.class, id);
    }
    public void persist(Demand demand){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(demand);
    }
    public void merge(Demand demand){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(demand);
    }
    public void remove(Demand demand){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(demand);
    }
		
}