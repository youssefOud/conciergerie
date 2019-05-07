/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.DeletedAccounts;
import javax.persistence.EntityManager;

/**
 *
 * @author olivi
 */
public class DeletedAccountsDAO {
      public DeletedAccounts findById(Long id){
        EntityManager em = JpaUtil.getEntityManager();
        return em.find(DeletedAccounts.class, id);
    }
    
    public void persist(DeletedAccounts delAc){
        EntityManager em = JpaUtil.getEntityManager();
        em.persist(delAc);
    }
    
    public void merge(DeletedAccounts delAc){
        EntityManager em = JpaUtil.getEntityManager();
        em.merge(delAc);
    }
    
    public void remove(DeletedAccounts delAc){
        EntityManager em = JpaUtil.getEntityManager();
        em.remove(delAc);
    }
    
}
