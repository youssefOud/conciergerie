/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package IHM;

import DAO.JpaUtil;
import Model.Demand;
import Model.Offer;
import Model.Person;
import Services.Services;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author youss
 */
public class Main {
    public static void main (String[] args) throws ParseException{
        //Test de création de demande
        //On initialise
        JpaUtil.init();
        Services s = new Services();
        
  
        Person person = new Person("fifi","12345","+12345","fifi@gmail.com",5,"fifi",4.5,"/url/img.png","Residence A");
                
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        
        Offer offer = new Offer(person, "Bricolage", "marteau",  formatDate.parse("10/05/2019"), formatTime.parse("19:00")
                ,"Résidence M", "prêt", 2, "Propose un marteau classique", "heures", 2);
        Demand demand = new Demand(person, "Bricolage", "marteau", formatDate.parse("12/05/2019"), formatTime.parse("19:00")
                ,"Résidence M", "prêt", 2, "Recherche marteau classique", "heures", 2);
        
        System.out.println(s.createPerson(person));
        System.out.println(s.createDemand(demand));
        System.out.println(s.createOffer(offer));
    
        JpaUtil.destroy();
        
        
    }
}
