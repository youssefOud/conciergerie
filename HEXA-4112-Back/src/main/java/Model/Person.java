package Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Person{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String firstName;
    
    private String lastName;
    
    private String password;
    
    private String cellNumber;
    
    private String mail;
    
    private Integer pointBalance;
    
    private Double rating;
    
    private String picture;
    
    private String location;
    
    private String privilegedContact;
    
    public Person() {
        
    }
    
    public Person(String firstName, String lastName, String password, String cellNumber, String mail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.cellNumber = cellNumber;
        this.mail = mail;
        this.pointBalance = 100;
        this.rating = -1.0; // Par défaut, si aucune annonce d'offre terminée
        this.privilegedContact = "email";
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
    
    public double getPointBalance() {
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
    
}