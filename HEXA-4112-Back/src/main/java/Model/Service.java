
package Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
    
    @Temporal(TemporalType.TIMESTAMP)
    protected Date endOfAvailabilityDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    protected Date publicationDate;
    
    protected String location;
    
    protected String type;

    protected String description;
    
    protected int duration;
    
    @javax.persistence.Lob
    protected String pictures;
    
    protected String priceUnit;
    
    protected String durationUnit;
    
    protected String state; // valid or expired
    
    public Service() {
        
    }

    public Service(Person personOffering, Person personDemanding, String category, String pictures, String nameObject, Date availabilityDate,
            String localisation, String type, String description, String priceUnit, String durationUnit, int duration) {

        this.personOffering = personOffering;
        this.personDemanding = personDemanding;
        this.category = category;
        this.pictures = pictures;
        this.nameObject = nameObject;
        this.availabilityDate = availabilityDate;
        this.location = localisation;
        this.type = type;
        this.description = description;
        this.priceUnit = priceUnit;
        this.durationUnit = durationUnit;
        this.duration = duration;
        this.publicationDate = new Date();
        this.state = "valid";
        
        Long durationInMillis = Long.valueOf(duration);
        if (durationUnit.equals("jours")) {
            durationInMillis *= 24*60*60*1000;
        } else if (durationUnit.equals("heures")) {
            durationInMillis *= 60*60*1000;
        }  else if (durationUnit.equals("minutes")) {
            durationInMillis *= 60*1000;
        }
        
        endOfAvailabilityDate = new Date(availabilityDate.getTime() + durationInMillis);
    }
    
     public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEndOfAvailabilityDate() {
        return endOfAvailabilityDate;
    }

    public void setEndOfAvailabilityDate(Date endOfAvailabilityDate) {
        this.endOfAvailabilityDate = endOfAvailabilityDate;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
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
    
    public String getLocation() {
        return location;
    }
    
    public void setLocalisation(String localisation) {
        this.location = localisation;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public Person getPerson(){
        if(personOffering != null){
            return personOffering;
        }
        return personDemanding;
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String serviceString = "Service : " + personOffering + " propose un " + nameObject;
        return serviceString;
    }
    
    public abstract int getNbPoint();
    public abstract int getNbPointPerDay();
}