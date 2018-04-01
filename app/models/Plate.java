package models;

import javax.persistence.Id;
import play.db.ebean.Model;

/**
 *
 * @author facundocaldora
 */
public class Plate extends Model{
    
    
    @Id
    private Long id;
    
    private String name;
    
    private float price;
    
    private float promoPrice;
    
    private boolean isOnPromo;

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
        if(this.isOnPromo) return this.getPromoPrice();
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(float promoPrice) {
        this.promoPrice = promoPrice;
    }

    public boolean isIsOnPromo() {
        return isOnPromo;
    }

    public void setIsOnPromo(boolean isOnPromo) {
        this.isOnPromo = isOnPromo;
    }
    
    
}
