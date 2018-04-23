package models;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Optional extends Model {

    protected static final Finder<Long, Optional> FIND = new Finder<>(Long.class, Optional.class);

    public interface Creation{}

    @Id
    private Long id;

    @Constraints.Required(groups = Creation.class)
    private String name;

    @Constraints.Required(groups = Creation.class)
    private float price;

    @ManyToOne
    private Commerce commerce;

    @ManyToMany
    private List<Plate> plates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @JsonIgnore
    public Commerce getCommerce() {
        return commerce;
    }

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
    }

    public static Optional findByProperty(String property, Object value){
        return FIND.where().eq(property, value).findUnique();
    }
    public static Optional findByProperties(List<String> properties, List<Object> values){
        ExpressionList<Optional> exp = FIND.where();
        for(int i=0; i<properties.size(); i++){
            exp.eq(properties.get(i), values.get(i));
        }
        return exp.findUnique();
    }

    public static List<Optional> findListByProperty(String property, Object value){
        return FIND.where().eq(property, value).findList();
    }

    @JsonIgnore
    public List<Plate> getPlates() {
        return plates;
    }

    public void setPlates(List<Plate> plates) {
        this.plates = plates;
    }
}
