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
    
    private String login;
    
    private String password;
    
    private String numeroTel;
    
    private String mail;
    
    private double pointBalance;
    
    private String pseudo;
    
    private double rating;
    
    private String picture;
    
    private String location;
    
    public Person() {
        
    }
    
    public Person(String login, String password, String numeroTel, String mail, double pointBalance, String pseudo,
            double rating, String picture, String location) {
        //this.servicesOffered = new ArrayList<>();
        //this.servicesDemanded = new ArrayList<>();
        this.login = login;
        this.password = password;
        this.numeroTel = numeroTel;
        this.mail = mail;
        this.pointBalance = pointBalance;
        this.pseudo = pseudo;
        this.rating = rating;
        this.picture = picture;
        this.location = location;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNumeroTel() {
        return numeroTel;
    }
    
    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
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
    
    public void setPointBalance(double pointBalance) {
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