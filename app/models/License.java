package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class License extends Model {

    @Id
    private Long id;

    private Date expirationDate;

    private float feeToPay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public float getFeeToPay() {
        return feeToPay;
    }

    public void setFeeToPay(float feeToPay) {
        this.feeToPay = feeToPay;
    }
}
