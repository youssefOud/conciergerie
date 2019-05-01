package Model;

import java.util.Date;
import javax.persistence.Entity;

@Entity
public class Demand extends Service {
    
    // TODO : Verifier float ou pas
    private double nbPointMax;
    
    public Demand() {
        
    }
    
    public Demand(Person personDemanding, String category, String nameObject, Date availabilityDate,
            String localisation, String type, double nbPointMax, String description, String priceUnit, String durationUnit, int duration) {
        // TODO A implementer
        super(null, personDemanding, category, nameObject, availabilityDate, localisation, type, description, priceUnit, durationUnit, duration);
        this.nbPointMax = nbPointMax;
    }
    
    public double getNbPointMax() {
        return nbPointMax;
    }
    
    public void setNbPointMax(double nbPointMax) {
        this.nbPointMax = nbPointMax;
    }
}