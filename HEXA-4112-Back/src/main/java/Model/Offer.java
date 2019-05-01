package Model;

import java.util.Date;
import javax.persistence.Entity;

@Entity
public class Offer extends Service{
    
    private double nbPointMin;
    
    public Offer() {
        
    }
    
    
    public Offer(Person personOffering, String category, String nameObject, Date availabilityDate,
            String localisation, String type, double nbPointMin, String description, String priceUnit, String durationUnit, int duration) {
        super(personOffering, null, category, nameObject, availabilityDate, localisation, type, description, priceUnit, durationUnit, duration);
        this.nbPointMin = nbPointMin;
    }
    
    public double getNbPointMin() {
        return nbPointMin;
    }
    
    public void setNbPointMin(double nbPointMin) {
        this.nbPointMin = nbPointMin;
    }
    
}