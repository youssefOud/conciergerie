package Model;

import java.util.Date;
import javax.persistence.Entity;

@Entity
public class Demand extends Service {
    
    // TODO : Verifier float ou pas
    private double nbPointMax;
    
    public Demand() {
        
    }
    
    public Demand(Person personDemanding, String category, String nameObject, Date availabilityDate, Date availabilityTime,
            String localisation, String type, double nbPointMax, String description, String unit, int duration) {
        // TODO A implementer
        super(null, personDemanding, category, nameObject, availabilityDate,
                availabilityTime, localisation, type, description, unit, duration);
        this.nbPointMax = nbPointMax;
    }
    
    public double getNbPointMax() {
        return nbPointMax;
    }
    
    public void setNbPointMax(double nbPointMax) {
        this.nbPointMax = nbPointMax;
    }
}