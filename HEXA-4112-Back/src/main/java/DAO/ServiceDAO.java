package DAO;

import javax.persistence.EntityManager;
import Model.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class ServiceDAO {

    public Service findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Service.class, id);
    }

    public void persist(Service service) {
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(service);
    }

    public void merge(Service service) {
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(service);
    }

    public void remove(Service service) {
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(service);
    }

    public List<Service> findAllServicesWithFilter(String object, String category, String location, Date startingDate, Date endingDate, String nbPts, String paymentUnit, String type) {
        EntityManager em = JpaUtil.getEntityManager();
        String request;
        if (startingDate == null) {
            request = "select s from Service s where s.endOfAvailabilityDate >= :endingDate and s.serviceState = :validState ";
        } else {
            request = "select s from Service s where s.availabilityDate <= :startingDate and s.endOfAvailabilityDate >= :endingDate and s.serviceState = :validState ";
        }

        if (!object.isEmpty()) {
            request += "and lower(s.nameObject) like concat('%',:object,'%') or lower(s.description) like concat('%',:object,'%')  ";
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
        if (startingDate != null) {
            query.setParameter("startingDate", startingDate, TemporalType.TIMESTAMP);
        }
        query.setParameter("endingDate", endingDate, TemporalType.TIMESTAMP);
        query.setParameter("validState", 0);
        

        if (!object.isEmpty()) {
            query.setParameter("object", object.toLowerCase());
        }
        if (!category.isEmpty()) {
            query.setParameter("category", category);
        }
        if (!location.isEmpty()) {
            query.setParameter("location", location);
        }

        if (!type.isEmpty()) {
            if (type.equals("demande")) {
                query.setParameter("class", Demand.class);
            } else if (type.equals("offre")) {
                query.setParameter("class", Offer.class);
            }
        }
        List<Service> filteredServices = (List<Service>) query.getResultList();

        if (!nbPts.isEmpty()) {
            int nbPtsPerDay;
            if (paymentUnit.equals("minutes")) {
                nbPtsPerDay = Integer.valueOf(nbPts) * 60 * 24;
            } else if (paymentUnit.equals("heures")) {
                nbPtsPerDay = Integer.valueOf(nbPts) * 24;
            } else {
                nbPtsPerDay = Integer.valueOf(nbPts);
            }
            List<Service> servicesToRemove = new ArrayList<>();
            for (Service s : filteredServices) {
                if (s instanceof Offer) {
                    if (s.getNbPointPerDay() > nbPtsPerDay) {
                        servicesToRemove.add(s);
                    }
                } else if (s.getNbPointPerDay() < nbPtsPerDay) {
                    servicesToRemove.add(s);
                }
            }

            for (Service s : servicesToRemove) {
                filteredServices.remove(s);
            }
        }

        return filteredServices;
    }
    
     public List<Service> findAllServicesByPerson(Person person) {
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select s from Service s where s.personOffering = :person or s.personDemanding = :person order by s.serviceState asc";          
        Query query = em.createQuery(request).setParameter("person", person);
        List<Service> services = (List<Service>)query.getResultList();
        return services; 
    } 
     
    public List<Object[]> findInterestsByPerson(Person person){
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select r,r.service from Reservation r where r.reservationOwner = :person order by r.reservationRequestDate desc";      
        Query query = em.createQuery(request).setParameter("person", person);
        List<Object[]> services = (List<Object[]>)query.getResultList();
        
        System.out.println("services: " + services.size());
        return services; 
    }
     
}
