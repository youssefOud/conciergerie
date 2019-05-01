package Model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Inheritance (strategy = InheritanceType.JOINED)
public abstract class Service implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected Person personOffering;

    protected Person personDemanding;
    
    protected String category;
    
    protected String nameObject;
    
    @Temporal(TemporalType.TIMESTAMP)
    protected Date availabilityDate;
    
    protected String localisation;
    
    protected String type;

    protected String description;
    
    protected int duration;
    
    protected String unit;
    
    public Service() {
        
    }

    
    public Service(Person personOffering, Person personDemanding, String category, String nameObject, Date availabilityDate,
            String localisation, String type,String description, String unit, int duration) {

        this.personOffering = personOffering;
        this.personDemanding = personDemanding;
        this.category = category;
        this.nameObject = nameObject;
        this.availabilityDate = availabilityDate;
        this.localisation = localisation;
        this.type = type;
        this.description = description;
        this.unit = unit;
        this.duration = duration;
    }
    
    public Person getPersonOffering() {
        return personOffering;
    }
    
    public void setPersonOffering(Person personOffering) {
        this.personOffering = personOffering;
    }
    
    public Person getPersonDemanding() {
        return personDemanding;
    }
    
    public void setPersonDemanding(Person personDemanding) {
        this.personDemanding = personDemanding;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getNameObject() {
        return nameObject;
    }
    
    public void setNameObject(String nameObject) {
        this.nameObject = nameObject;
    }
    
    public Date getAvailabilityDate() {
        return availabilityDate;
    }
    
    public void setAvailabilityDate(Date availabilityDate) {
        this.availabilityDate = availabilityDate;
    }
    
    public String getLocalisation() {
        return localisation;
    }
    
    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String serviceString = "Service : " + personOffering + " propose un " + nameObject;
        return serviceString;
    }
    
}
