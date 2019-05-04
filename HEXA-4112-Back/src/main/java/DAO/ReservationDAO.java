package DAO;

import javax.persistence.EntityManager;

import Model.Reservation;

public class ReservationDAO {
    
    public Reservation findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Reservation.class, id);
    }
    
    public void persist(Reservation reservation){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(reservation);
    }
    
    public void merge(Reservation reservation){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(reservation);
    }
    
    public void remove(Reservation reservation){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(reservation);
    }
   
    
}