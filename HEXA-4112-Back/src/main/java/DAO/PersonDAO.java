package DAO;

import Model.Demand;
import Model.Offer;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import Model.Person;
import java.util.List;

/**
 * Class linking the data access layer to the person business layer
 * 
 * @author HEXA-4112
 */
public class PersonDAO {
    
    /**
     * Gets the person thanks to the id
     * 
     * @param id
     * @return the Person corresponding to the id
     */
    public Person findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Person.class, id);
    }
    
    /**
     * Persists the parameter person in the database
     * 
     * @param person 
     */
    public void persist(Person person){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(person);
    }
    
    /**
     * Merges the parameter person in the database
     * 
     * @param person 
     */
    public void merge(Person person){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(person);
    }
    
    /**
     * Removes the parameter person in the database
     * 
     * @param person 
     */
    public void remove(Person person){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(person);
    }
    
    
    /**
     * Verifies that the person exists in the database thanks to his email
     * 
     * @param mail
     * @return true if the person exists, false otherwises
     */
    public boolean personExists(String mail){
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("SELECT u FROM Person u where u.mail = :mailToVerify").setParameter("mailToVerify", mail);
        return !(((List<Person>) query.getResultList()).isEmpty());
    }
    
    /**
     * Verifies that the person exists in the database thanks to his email and 
     * the corresponding password
     * 
     * @param mail
     * @param password
     * @return the Person whom the mail belongs, null otherwise
     */
    public Person verifyPersonAccount(String mail, String password) {
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("select u from Person u where u.mail=:mailToVerify"
                + " AND u.password=:passwordToVerify");
        query.setParameter("mailToVerify", mail);
        query.setParameter("passwordToVerify", password);
        
        if(((List<Person>) query.getResultList()).isEmpty()){
            return null;
        }
        return ((List<Person>) query.getResultList()).get(0); 
    }
    
    /**
     * Finds the Person thanks to his mail
     * 
     * @param mail
     * @return the Person 
     */
    public Person findByMail(String mail) {
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("select u from Person u where u.mail=:mailToVerify");
        query.setParameter("mailToVerify", mail);
        
        if(((List<Person>) query.getResultList()).isEmpty()){
            return null;
        }
        return ((List<Person>) query.getResultList()).get(0); 
    }
    
    public List<Person> findBySupposedlyInterestingDemand(Demand d){
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("select u from Person u where :demand MEMBER OF u.supposedlyInterestingDemands");
        
        //SELECT d FROM Document AS d WHERE :user MEMBER OF d.accessors
        query.setParameter("demand", d);
        
        return (List<Person>) query.getResultList();
    }
    
    public List<Person> findBySupposedlyInterestingOffer(Offer o){
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("select u from Person u where :offer MEMBER OF u.supposedlyInterestingOffers");
        
        //SELECT d FROM Document AS d WHERE :user MEMBER OF d.accessors
        query.setParameter("offer", o);
        
        return (List<Person>) query.getResultList();
    }
    
}