package models;

import com.avaje.ebean.annotation.EnumValue;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CommerceCategory extends Model {

    private enum Category{
        @EnumValue("MILAS")
        MILAS
    }

    @Id
    private Long id;

    private Category name;

    public Category getName() {
        return name;
    }

    public void setName(Category name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
