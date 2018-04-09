package models;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

import java.util.List;

/**
 *
 * @author facundocaldora
 */
public class Plate extends Model{
    
    
    @Id
    private Long id;
    
    private String name;

    @ManyToMany
    private List<Category> categories;

    @OneToMany
    private List<Optional> optionals;
    
    private float price;

    private Promo promo;

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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Optional> getOptionals() {
        return optionals;
    }

    public void setOptionals(List<Optional> optionals) {
        this.optionals = optionals;
    }

    public float getPrice() {
        return price;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    
}
