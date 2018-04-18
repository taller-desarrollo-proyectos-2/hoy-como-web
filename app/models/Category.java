package models;

import com.avaje.ebean.ExpressionList;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 *
 * @author facundocaldora
 */
@Entity
public class Category extends Model {

    protected static final Finder<Long, Category> FIND = new Finder<>(Long.class, Category.class);

    public interface Creation{}

    @Id
    private Long id;

    @Constraints.Required(groups = Creation.class)
    private String name;

    @ManyToOne
    private Commerce commerce;

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

    public static Category findByProperty(String property, Object value){
        return FIND.where().eq(property, value).findUnique();
    }

    public Commerce getCommerce() {
        return commerce;
    }

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
    }

    public static Category findByProperties(List<String> properties, List<Object> values){
        ExpressionList<Category> exp = FIND.where();
        for(int i=0; i<properties.size(); i++){
            exp.eq(properties.get(i), values.get(i));
        }
        return exp.findUnique();
    }
}
