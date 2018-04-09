package models;

import java.util.List;
import javax.persistence.*;

import play.db.ebean.Model;

/**
 *
 * @author facundocaldora
 */
@Entity
public class Commerce extends Model{
    
    @Id
    private Long id;
    
    @ManyToOne
    private Company company;

    @OneToMany
    private List<Category> categories;
    
    @OneToMany
    private List<Plate> plates;

    @OneToOne
    private License license;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Plate> getPlates() {
        return plates;
    }

    public void setPlates(List<Plate> plates) {
        this.plates = plates;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }
}
