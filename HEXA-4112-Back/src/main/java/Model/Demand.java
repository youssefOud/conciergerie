package Model;

import java.util.Date;
import javax.persistence.Entity;

/**
 * Entity representing information about user demand.
 * Inheriting Class of Service.
 * 
 * @author HEXA-4112
 */
@Entity
public class Demand extends Service {
    
    private int nbPointMax;
    
    public Demand() {
        
    }
    
    public Demand(Person personDemanding, String category, String pictures, String nameObject, Date availabilityDate,
            String localisation, String type, int nbPointMax, String description, String priceUnit, String durationUnit, int duration) {
        super(null, personDemanding, category, pictures, nameObject, availabilityDate, localisation, type, description, priceUnit, durationUnit, duration);
        this.nbPointMax = nbPointMax;
    }
    
    @Override
    public int getNbPoint() {
        return nbPointMax;
    }
    
    public void setNbPointMax(int nbPointMax) {
        this.nbPointMax = nbPointMax;
    }
    
    @Override
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