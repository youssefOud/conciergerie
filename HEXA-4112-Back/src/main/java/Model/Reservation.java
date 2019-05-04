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
public class Reservation implements Serializable{  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Person serviceOwner;

    @ManyToOne
    private Person reservationOwner;
    
    @ManyToOne
    private Service service;
    
    //Terms of reservation
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationStartingDate;
    
    private int reservationDuration;
    
    protected String durationUnit;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationEndingDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationRequestDate;
    
    private int serviceOwnerRating;
    
    private int reservationOwnerRating;
    
    protected int reservationPrice;
    
    protected int state; //0 for pending / 1 for accepted / 2 for refused 
    
    
    public Reservation() {
        
    }
    
    public Reservation(Person serviceOwner, Person reservationOwner, Service service, Date reservationStartingDate, int reservationDuration, String durationUnit, Date reservationRequestDate){
        this.serviceOwner = serviceOwner;
        this.reservationOwner = reservationOwner;
        this.service = service;
        this.reservationStartingDate = reservationStartingDate;
        this.reservationDuration = reservationDuration;
        this.durationUnit = durationUnit;
        this.reservationRequestDate = reservationRequestDate;
        this.state = 0;
        
        
         Long durationInMillis = Long.valueOf(reservationDuration);
        if (durationUnit.equals("jours")) {
            durationInMillis *= 24*60*60*1000;
        } else if (durationUnit.equals("heures")) {
            durationInMillis *= 60*60*1000;
        }  else if (durationUnit.equals("minutes")) {
            durationInMillis *= 60*1000;
        }
        reservationEndingDate = new Date(this.reservationStartingDate.getTime() + durationInMillis);
        
        Long durationInMinutes = durationInMillis/1000;
        
        String priceUnit = service.getPriceUnit();
        
        double nbPts = service.getNbPoint();
        double nbPtsInMinutes;
        
        if(priceUnit.equals("jours")){
            nbPtsInMinutes = nbPts/(60*24);
        }
        else if (priceUnit.equals("heures")){
            nbPtsInMinutes = nbPts/(60*24);
        }
        else{
            nbPtsInMinutes = nbPts;
        }
        
        reservationPrice = (int)Math.ceil(durationInMinutes * nbPtsInMinutes);
        
    }
}
