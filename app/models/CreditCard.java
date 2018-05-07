package models;


import javax.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorValue("CREDIT_CARD")
public class CreditCard extends PaymentType {

    private String number;

    private String fullName;

    @Transient
    private int code;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    public CreditCard(String number, String fullName, int code, Date date){
        this.number = number;
        this.fullName = fullName;
        this.code = code;
        this.expirationDate = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
