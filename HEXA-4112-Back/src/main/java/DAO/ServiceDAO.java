package DAO;

import Model.Demand;
import Model.Offer;
import javax.persistence.EntityManager;

import Model.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class ServiceDAO {
    
    public Service findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Service.class, id);
    }
    public void persist(Service service){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(service);
    }
    public void merge(Service service){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(service);
    }
    public void remove(Service service){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(service);
    }
    
    public List<Service> findAllServicesWithFilter(String category, String location, Date startingDate, Date endingDate, String nbPts, String type) {
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select s from Service s where s.availabilityDate <= :startingDate ";
        if (endingDate.compareTo(startingDate) > 0) {
            request += "and s.endOfAvailabilityDate >= :endingDate ";
        }        
                
        if (!category.isEmpty()) {
            request += "and ";
            request += "s.category = :category ";
        }
        if (!location.isEmpty()) {
            request += "and ";
            request += "s.location = :location ";
        }

        if (!type.isEmpty()) {
            request += "and ";
            request += "type(s) = :class";
        }
        
        request += " order by s.publicationDate desc";

        System.out.println("request: " + request);
        System.out.println("startingDate: " + startingDate);
        System.out.println("endingDate:" + endingDate);
        
        Query query = em.createQuery(request);
        query.setParameter("startingDate", startingDate, TemporalType.TIMESTAMP);
         if (endingDate.compareTo(startingDate) > 0) {
            query.setParameter("endingDate", endingDate, TemporalType.TIMESTAMP);
        } 
        
        if (!category.isEmpty()) {
            query.setParameter("category", category);
        }
        if (!location.isEmpty()) {
            query.setParameter("location", location);
        }

        if (!type.isEmpty()) {
            if (type.equals("Demand")) query.setParameter("class", Demand.class);
            else if (type.equals("Offer")) query.setParameter("class", Offer.class);
        }
        List<Service> filteredServices = (List<Service>)query.getResultList();
        System.out.println("filtered: " + filteredServices.size());
        
        if (!nbPts.isEmpty()) {
            List<Service> servicesToRemove = new ArrayList<>();
            for(Service s: filteredServices){
                if(s instanceof Offer){
                    if(s.getNbPoint() > Double.valueOf(nbPts)){
                        servicesToRemove.add(s);
                    }
                }
                else if(s.getNbPoint() < Double.valueOf(nbPts)){
                    servicesToRemove.add(s);
                }
            }
        
            for(Service s: servicesToRemove){
                filteredServices.remove(s);
            }
        }

        return filteredServices;
    }
    
    
}