package DAO;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import Model.User;

public class UserDAO {

	public UserDAO() {
		
	}
	
	public User findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(User.class, id);
    }
    public void persist(User user){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(user);
    }
    public void merge(User user){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(user);
    }
    public void remove(User user){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(user);
    }
    
    public User verifyUserAccount(String login, String password) {
    	EntityManager em = JpaUtil.getEntityManager();
    	Query query = em.createQuery("select u from User u where u.login=:loginToVerify"
    			+ " AND u.password=:passwordToVerify");
    	query.setParameter("loginToVerify", login);
    	query.setParameter("passwordToVerify", password);
		
    	
    	// TODO : change when javax.persistence marche
    	// User user = query.getResult();
    	// return user;
    	return null;
    }
	
}