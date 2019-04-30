package DAO;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import Model.Person;
import java.util.List;

public class PersonDAO {
    
    public Person findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(Person.class, id);
    }
    public void persist(Person person){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(person);
    }
    public void merge(Person person){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(person);
    }
    public void remove(Person person){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(person);
    }
    
    public Person verifyPersonAccount(String login, String password) {
        EntityManager em = JpaUtil.getEntityManager();
        Query query = em.createQuery("select u from Person u where u.login=:loginToVerify"
                + " AND u.password=:passwordToVerify");
        query.setParameter("loginToVerify", login);
        query.setParameter("passwordToVerify", password);
        
        if(((List<Person>) query.getResultList()).isEmpty()){
            return null;
        }
        return ((List<Person>) query.getResultList()).get(0); 
    }
    
}