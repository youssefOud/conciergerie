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
import Model.Service;
import Services.Services;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

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
                
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        Offer offer = new Offer(person, "Bricolage", "010010100110", "marteau",  formatDate.parse("10/04/2019 19:00")
                ,"Residence M", "prêt", 2, "Propose un marteau classique", "heures", "heures", 2);
        Demand demand = new Demand(person, "Bricolage", "010010100110", "marteau", formatDate.parse("12/05/2019 19:00")
                ,"Residence M", "prêt", 2, "Recherche marteau classique", "heures", "heures", 2);
        Offer offer2 = new Offer(person, "Bricolage", "010010100110", "four",  formatDate.parse("10/04/2019 19:00")
                ,"Residence M", "prêt", 2, "Propose un marteau classique", "heures", "heures", 2);
        
        System.out.println(s.createPerson(person));
        System.out.println(s.createDemand(demand));
        System.out.println(s.createOffer(offer2));
        System.out.println(s.createOffer(offer));
    
        //List<Service> listS = s.findAllServicesWithFilter("Bricolage", "Residence M", "10/05/2019", "19:30", "1", "heures", "3", "Offer");
        List<Service> listS = s.findAllServicesWithFilter("Bricolage", "Residence M", "", "", "","","" ,"Offer");
        
        System.out.println();

        JpaUtil.destroy();
        
        
    }
}
