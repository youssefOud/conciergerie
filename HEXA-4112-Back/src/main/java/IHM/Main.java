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
        
  
       /* Person person = new Person("12345","+12345","fifi@gmail.com","fifi","/url/img.png","Residence A");
                
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        Offer offer = new Offer(person, "Bricolage", null, "marteau",  formatDate.parse("09/05/2019 00:00")
                ,"Residence M", "prêt", 12, "Propose un marteau classique", "heures", "heures", 12);
        Demand demand = new Demand(person, "Bricolage",null, "marteau", formatDate.parse("12/05/2019 19:00")
                ,"Residence M", "prêt", 2, "Recherche marteau classique", "heures", "heures", 2);
        Offer offer2 = new Offer(person, "Bricolage", null, "four",  formatDate.parse("09/05/2019 00:00")
                ,"Residence M", "prêt", 50, "Propose un marteau classique", "heures", "heures", 50);
        
        System.out.println(s.createPerson(person));
        System.out.println(s.createDemand(demand));
        System.out.println(s.createOffer(offer2));
        System.out.println(s.createOffer(offer));
    
        //List<Service> listS = s.findAllServicesWithFilter("Bricolage", "Residence M", "10/05/2019", "19:30", "1", "heures", "3", "Offer");
       // List<Service> listS = s.findAllServicesWithFilter("Marteau","", "", "", "", "","","" ,"");
        //List<Service> listS = s.findAllServicesWithFilter(category, location, date, time, duration, units, nbPts, serviceType)
        System.out.println();
*/
       
       
       
        boolean emailSent = s.sendVerificationEmail("mets ton adresse mail ici");
        System.out.println("--------------------------------"+emailSent+"-------------------");
        
        
        
        //A décommenter quand tu recois le code de vérification (et insérer le code de vérification
        
        //Person p = s.registerPerson("Dupont", "Jean", "Password", "adresseMailDeOlivia@gmail.com", "000000000000", mettre verification code ici);
        JpaUtil.destroy();
        
        
    }
}
