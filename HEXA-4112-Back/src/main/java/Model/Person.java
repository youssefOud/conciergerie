package Model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
    
    public Person() {
        
    }
    
    public Person(String firstName, String lastName, String password, String cellNumber, String mail) {
        this.password = password;
        this.cellNumber = cellNumber;
        this.mail = mail;
        this.pointBalance = 100;
        this.location = location;
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
    
    
    
}