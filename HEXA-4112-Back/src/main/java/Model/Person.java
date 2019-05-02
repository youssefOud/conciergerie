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
    
    private String password;
    
    private String cellNumber;
    
    private String mail;
    
    private Integer pointBalance;
    
    private String pseudo;
    
    private Double rating;
    
    private String picture;
    
    private String location;
    
    public Person() {
        
    }
    // Pas de rating ni de solde de points dans le contructeur
    public Person(String login, String password, String cellNumber, String mail, String pseudo,
            String picture, String location) {
        //this.servicesOffered = new ArrayList<>();
        //this.servicesDemanded = new ArrayList<>();
        this.password = password;
        this.cellNumber = cellNumber;
        this.mail = mail;
        this.pointBalance = 50;
        this.pseudo = pseudo;
        this.rating = -1.0; // Par défaut, si aucune annonce d'offre terminée
        this.picture = picture;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    
    public String getPseudo() {
        return pseudo;
    }
    
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
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