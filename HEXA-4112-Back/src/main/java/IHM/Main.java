/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IHM;

import DAO.JpaUtil;
import Services.Services;

/**
 *
 * @author youss
 */
public class Main {
    public static void main (String[] args){
        //On initialise
        JpaUtil.init();
        Services s = new Services();
        
        //s.initialisation();
       
        
        JpaUtil.destroy();
        
        
    }
}
