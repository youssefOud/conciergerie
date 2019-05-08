package DAO;

import javax.persistence.EntityManager;

import Model.*;
import java.util.List;
import javax.persistence.Query;

/**
 * Class linking the data access layer to the reservation business layer
 * 
 * @author HEXA-4112
 */
public class ReservationDAO {
    
    /**
     * Gets the reservation thanks to the id
     * 
     * @param id
     * @return the Reservation corresponding to the id
     */
    public Reservation findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Reservation.class, id);
    }
    
    /**
     * Persists the parameter reservation in the database
     * 
     * @param reservation 
     */
    public void persist(Reservation reservation){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(reservation);
    }
    
    /**
     * Merges the parameter reservation in the database
     * 
     * @param reservation 
     */
    public void merge(Reservation reservation){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(reservation);
    }
    
    /**
     * Removes the parameter reservation in the database
     * 
     * @param reservation 
     */
    public void remove(Reservation reservation){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(reservation);
    }
    
    /**
     * Gets all reservations associated with the parameter service
     * 
     * @param service
     * @return a List of Reservation
     */
    public List<Reservation> findAllReservationsByService(Service service){
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select r from Reservation r where r.service = :service order by r.reservationRequestDate desc";         
        Query query = em.createQuery(request).setParameter("service", service);
        List<Reservation> reservations = (List<Reservation>)query.getResultList();
        return reservations; 
    }
    
    /**
     * Gets all reservations associated with the parameter person
     * 
     * @param p
     * @return a List of Reservation
     */
    public List<Reservation> findAllReservationsByReservationOwner(Person p){
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select r from Reservation r where r.reservationOwner = :person OR r.serviceOwner = :person ";         
        Query query = em.createQuery(request).setParameter("person", p);
        List<Reservation> reservations = (List<Reservation>)query.getResultList();
        return reservations; 
    }
   
    
}