package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CASH")
public class Cash extends PaymentType {

    private float payWith;

    public Cash(float payWith){
        this.payWith = payWith;
    }

    public float getPayWith() {
        return payWith;
    }

    public void setPayWith(float payWith) {
        this.payWith = payWith;
    }
}
