package DAO;

import javax.persistence.EntityManager;

import Model.*;
import java.util.List;
import javax.persistence.Query;

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
    
    public List<Reservation> findAllReservationsByService(Service service){
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select r from Reservation r where r.service = :service ";         
        Query query = em.createQuery(request).setParameter("service", service);
        List<Reservation> reservations = (List<Reservation>)query.getResultList();
        return reservations; 
    }
    
    public List<Reservation> findAllReservationsByReservationOwner(Person p){
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select r from Reservation r where r.reservationOwner = :person OR r.serviceOwner = :person ";         
        Query query = em.createQuery(request).setParameter("person", p);
        List<Reservation> reservations = (List<Reservation>)query.getResultList();
        return reservations; 
    }
   
    
}