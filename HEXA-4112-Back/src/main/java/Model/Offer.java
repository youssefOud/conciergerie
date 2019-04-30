package Model;

import java.util.Date;
import javax.persistence.Entity;

@Entity
public class Offer extends Service{
    
    private int nbPointMin;
    
    public Offer() {
        
    }
    
    
    public Offer(Person personOffering, String category, String nameObject, Date availabilityDate, Date availabilityTime,
            String localisation, String type, int nbPointMin, String description, String unit, int duration) {
        super(personOffering, null, category, nameObject, availabilityDate,
                availabilityTime, localisation, type, description, unit, duration);
        this.nbPointMin = nbPointMin;
    }
    
    public int getNbPointMin() {
        return nbPointMin;
    }
    
    public void setNbPointMin(int nbPointMin) {
        this.nbPointMin = nbPointMin;
    }
    
}