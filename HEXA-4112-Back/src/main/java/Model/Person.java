package Model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Person{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="PERSON_ID")
    private Long id;
    
    private String firstName;
    
    private String lastName;
    
    private String password;
    
    private String cellNumber;
    
    private String mail;
    
    private Integer pointBalance;
    
    private Double rating;
    
    private int nbRatings;
    
    private String picture;
    
    private String location;
    
    private String privilegedContact; // Value : "email" or "cellphone"
    

    @ManyToMany(cascade={CascadeType.ALL} ) 
    @JoinTable(name="PERSON_OFFER", joinColumns=@JoinColumn(name="PERSON_ID"),
    inverseJoinColumns=@JoinColumn(name="SERVICE_ID"))//@JoinTable is used to map Join table in database
    private List<Service> supposedlyInterestingOffers;

    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="PERSON_DEMAND", joinColumns=@JoinColumn(name="PERSON_ID"),
    inverseJoinColumns=@JoinColumn(name="SERVICE_ID"))//@JoinTable is used to map Join table in database
    private List<Service> supposedlyInterestingDemands;
    
    
    public Person() {
        
    }
    
    public Person(String firstName, String lastName, String password, String cellNumber, String mail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.cellNumber = cellNumber;
        this.mail = mail;
        this.pointBalance = 50;
        this.rating = -1.0; // Default value while no rating has been done
        this.nbRatings = 0;
        this.privilegedContact = "email"; // Default value
        this.supposedlyInterestingDemands = new ArrayList<>();
        this.supposedlyInterestingOffers = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getNbRatings() {
        return nbRatings;
    }

    public void setNbRatings(int nbRatings) {
        this.nbRatings = nbRatings;
    }
    
    

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getCellNumber() {
        return cellNumber;
    }
    
    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }
    
    public String getMail() {
        return mail;
    }
    
    public void setMail(String mail) {
        this.mail = mail;
    }
    
    public int getPointBalance() {
        return pointBalance;
    }
    
    public void setPointBalance(int pointBalance) {
        this.pointBalance = pointBalance;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public String getPicture() {
        return picture;
    }
    
    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrivilegedContact() {
        return privilegedContact;
    }
    
    public void setPrivilegedContact(String privilegedContact) {
        this.privilegedContact = privilegedContact;
    }

    public List<Service> getSupposedlyInterestingOffers() {
        return supposedlyInterestingOffers;
    }

    public List<Service> getSupposedlyInterestingDemands() {
        return supposedlyInterestingDemands;
    }

    public void setSupposedlyInterestingOffers(List<Service> supposedlyInterestingOffers) {
        this.supposedlyInterestingOffers = supposedlyInterestingOffers;
    }

    public void setSupposedlyInterestingDemands(List<Service> supposedlyInterestingDemands) {
        this.supposedlyInterestingDemands = supposedlyInterestingDemands;
    }
    
    public void addSSupposedlyInterestingOffers(List<Service> supposedlyInterestingOffers) {
        this.supposedlyInterestingOffers.addAll(supposedlyInterestingOffers);
    }
    
    public void addSSupposedlyInterestingDemands(List<Service> supposedlyInterestingDemands) {
        this.supposedlyInterestingDemands.addAll(supposedlyInterestingDemands);
    }
    
     public void deleteSupposedlyInterestingOffers(Service supposedlyInterestingOffer) {
        this.supposedlyInterestingOffers.remove(supposedlyInterestingOffer);
    }
     
    public void deleteSupposedlyInterestingDemands(Service supposedlyInterestingDemand) {
        this.supposedlyInterestingDemands.remove(supposedlyInterestingDemand);
    }
}
