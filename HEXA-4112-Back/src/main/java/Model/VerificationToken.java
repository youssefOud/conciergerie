package Model;

import java.util.Date;
import javax.persistence.*;

/**
 * Entity that represents the verification token sent to a user at registration
 * 
 * @author HEXA-4112
 */
@Entity
public class VerificationToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String token;
    
    private String email;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    public VerificationToken(){
    }

    public VerificationToken(String token, String email) {
        this.token = token;
        this.email = email;
        this.date = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
 
}
