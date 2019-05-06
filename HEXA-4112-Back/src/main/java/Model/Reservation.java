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
    
    protected int reservationState; //0 for pending / 1 for accepted / 2 for refused / 3 for ended / 4 for evaluated
    
    
    public Reservation() {
        
    }
    
    public Reservation(Person reservationOwner, Service service, Date reservationStartingDate, int reservationDuration, String durationUnit, Date reservationRequestDate){
        if (service != null) this.serviceOwner = service.getPerson();
        this.reservationOwner = reservationOwner;
        this.service = service;
        this.reservationStartingDate = reservationStartingDate;
        this.reservationDuration = reservationDuration;
        this.durationUnit = durationUnit;
        this.reservationRequestDate = reservationRequestDate;
        this.reservationState = 0;
        
        
         Long durationInMillis = Long.valueOf(reservationDuration);
        if (durationUnit.equals("jours")) {
            durationInMillis *= 24*60*60*1000;
        } else if (durationUnit.equals("heures")) {
            durationInMillis *= 60*60*1000;
        }  else if (durationUnit.equals("minutes")) {
            durationInMillis *= 60*1000;
        }
        reservationEndingDate = new Date(this.reservationStartingDate.getTime() + durationInMillis);
        
        Long durationInMinutes = durationInMillis/(1000*60);
        
        String priceUnit = service.getPriceUnit();
        
        double nbPts = service.getNbPoint();
        double nbPtsInMinutes;
        
        if(priceUnit.equals("jours")){
            nbPtsInMinutes = nbPts/(60*24);
        }
        else if (priceUnit.equals("heures")){
            nbPtsInMinutes = nbPts/(60);
        }
        else{
            nbPtsInMinutes = nbPts;
        }
        
        reservationPrice = (int)Math.ceil(durationInMinutes * nbPtsInMinutes);
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getServiceOwner() {
        return serviceOwner;
    }

    public void setServiceOwner(Person serviceOwner) {
        this.serviceOwner = serviceOwner;
    }

    public Person getReservationOwner() {
        return reservationOwner;
    }

    public void setReservationOwner(Person reservationOwner) {
        this.reservationOwner = reservationOwner;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Date getReservationStartingDate() {
        return reservationStartingDate;
    }

    public void setReservationStartingDate(Date reservationStartingDate) {
        this.reservationStartingDate = reservationStartingDate;
    }

    public int getReservationDuration() {
        return reservationDuration;
    }

    public void setReservationDuration(int reservationDuration) {
        this.reservationDuration = reservationDuration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public Date getReservationEndingDate() {
        return reservationEndingDate;
    }

    public void setReservationEndingDate(Date reservationEndingDate) {
        this.reservationEndingDate = reservationEndingDate;
    }

    public Date getReservationRequestDate() {
        return reservationRequestDate;
    }

    public void setReservationRequestDate(Date reservationRequestDate) {
        this.reservationRequestDate = reservationRequestDate;
    }

    public int getServiceOwnerRating() {
        return serviceOwnerRating;
    }

    public int getReservationOwnerRating() {
        return reservationOwnerRating;
    }

    public int getReservationPrice() {
        return reservationPrice;
    }

    public int getReservationState() {
        return reservationState;
    }

    public void setServiceOwnerRating(int serviceOwnerRating) {
        this.serviceOwnerRating = serviceOwnerRating;
    }

    public void setReservationOwnerRating(int reservationOwnerRating) {
        this.reservationOwnerRating = reservationOwnerRating;
    }


    public void setReservationPrice(int reservationPrice) {
        this.reservationPrice = reservationPrice;
    }


    public void setReservationState(int reservationState) {
        this.reservationState = reservationState;
    }
    
    
}