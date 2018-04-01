package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
    
    @ManyToOne
    private List<Plate> plates;

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
    
}
