package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Phone extends Model {

    protected static final Finder<Long, Phone> FIND = new Finder<>(Long.class, Phone.class);

    @Id
    private Long id;

    private String number;

    @ManyToOne
    private Commerce commerce;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public static Phone findByProperty(String property, Object value){
        return FIND.where().eq(property,value).findUnique();
    }

    @JsonIgnore
    public Commerce getCommerce() {
        return commerce;
    }

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
    }
}
