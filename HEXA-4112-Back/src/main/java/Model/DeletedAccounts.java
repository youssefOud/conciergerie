package Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity representing the information of unsubscribed users
 * 
 * @author HEXA-4112
 */
@Entity
public class DeletedAccounts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String mail;
    
    private Integer pointBalance;
    
    private Double rating;
    
    public DeletedAccounts() {}

    public DeletedAccounts(String mail, Integer pointBalance, Double rating) {
        this.mail = mail;
        this.pointBalance = pointBalance;
        this.rating = rating;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(Integer pointBalance) {
        this.pointBalance = pointBalance;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
    
    
}
