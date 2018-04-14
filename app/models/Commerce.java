package models;

import java.util.List;
import javax.persistence.*;

import play.data.validation.Constraints;
import play.db.ebean.Model;

/**
 *
 * @author facundocaldora
 */
@Entity
public class Commerce extends Model{

    protected static final Finder<Long, Commerce> FIND = new Finder<>(Long.class, Commerce.class);

    public interface Creation{}

    @Id
    private Long id;

    private String name;

    @ManyToOne
    private Company company;

    @Constraints.Required(groups = {Commerce.Creation.class})
    private String businessName;

    @ManyToMany
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Commerce findByProperty(String property, Object value){
        return FIND.where().eq(property, value).findUnique();
    }

    public static List<Commerce> findAll(){
        return FIND.all();
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
