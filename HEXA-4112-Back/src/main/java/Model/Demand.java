package Model;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;

@Entity
public class Demand extends Service {
    
    // TODO : Verifier float ou pas
    private int nbPointMax;
    
    public Demand() {
        
    }
    
    public Demand(Person personDemanding, String category, String pictures, String nameObject, Date availabilityDate,
            String localisation, String type, int nbPointMax, String description, String priceUnit, String durationUnit, int duration) {
        // TODO A implementer
        super(null, personDemanding, category, pictures, nameObject, availabilityDate, localisation, type, description, priceUnit, durationUnit, duration);
        this.nbPointMax = nbPointMax;
    }
    
    public int getNbPoint() {
        return nbPointMax;
    }
    
    public void setNbPointMax(int nbPointMax) {
        this.nbPointMax = nbPointMax;
    }
    
    public int getNbPointPerDay() {
        if(this.getPriceUnit().equals("minutes")){
            return nbPointMax*60*24;
        }
        else if(this.getPriceUnit().equals("heures")){
            return nbPointMax*24;
        }
        else{
            return nbPointMax;
        }
    
    }
}