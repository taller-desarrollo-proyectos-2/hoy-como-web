package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("BACKOFFICE")
public class BackofficeUser extends User {

    @OneToOne
    private Commerce commerce;

    public BackofficeUser(){ super(); }

    public BackofficeUser(String username, String password) {
        super(username, password);
    }

    public Commerce getCommerce() {
        return commerce;
    }

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
    }



}
