package models;

import java.util.List;
import javax.persistence.ManyToMany;

/**
 *
 * @author facundocaldora
 */
public class Combo {
    
    private String name;
    
    private float price;
    
    @ManyToMany
    private List<Plate> plates;

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

    public List<Plate> getPlates() {
        return plates;
    }

    public void setPlates(List<Plate> plates) {
        this.plates = plates;
    }
    
}
