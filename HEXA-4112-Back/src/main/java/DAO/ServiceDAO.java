package DAO;

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
    /*
    public void findAllServicesWithFilter(String category, String localisation, Date date, String duration, String units, String nbPts, String type) {
        EntityManager em = JpaUtil.getEntityManager();
        String request = "select s from Service s where ";
        boolean isFirstCriteria = true;
        if (!category.isEmpty()) {
            request += "s.category = :category ";
            isFirstCriteria = false;
        } 
        if (!localisation.isEmpty()) {
            if (!isFirstCriteria) request += "and "; 
            request += "s.localisation = :localisation ";
            isFirstCriteria = false;
        }
        if (!duration.isEmpty()) {
            if (!isFirstCriteria) request += "and ";
            request += "s.duration <= :duration ";
            isFirstCriteria = false;
        } 
        if (!units.isEmpty()) {
            if (!isFirstCriteria) request += "and ";
            request += "s.units = :units ";
            isFirstCriteria = false;
        } 
        
        Query query = em.createQuery("select v from Voyance v where v.employe = :employe");
        query.setParameter("employe", employe);
        try {
            return (List<Voyance>) query.getResultList();
         }catch(Exception e){
             System.out.println("Aucune voyance touvée pour ce client");
            return null;
        }
    }
    */
    
}