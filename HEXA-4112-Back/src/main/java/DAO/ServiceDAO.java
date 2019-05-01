package DAO;

import Model.Demand;
import Model.Offer;
import javax.persistence.EntityManager;

import Model.Service;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

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
    
    public List<Service> findAllServicesWithFilter(String category, String location, Date date, Long duration, String nbPts, String type) {
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select s from Service s where ";
        boolean isFirstCriteria = true;
        if (!category.isEmpty()) {
            request += "s.category = :category ";
            isFirstCriteria = false;
        }
        if (!location.isEmpty()) {
            if (!isFirstCriteria) request += "and ";
            request += "s.location = :location ";
            isFirstCriteria = false;
        }
//        if () {
//            if (!isFirstCriteria) request += "and ";
//            request += "s.location = :location ";
//            isFirstCriteria = false;
//        }
//        if (!duration.isEmpty()) {
//            if (!isFirstCriteria) request += "and ";
//            request += "s.duration <= :duration ";
//            isFirstCriteria = false;
//        }
if (!type.isEmpty()) {
    if (!isFirstCriteria) request += "and ";
    request += "type(s) = :class";
    isFirstCriteria = false;
}

Query query = em.createQuery(request);
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

return filteredServices;
    }
    
    
}