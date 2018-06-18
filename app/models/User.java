package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE")
public abstract class User extends Model {

    protected static final Finder<Long, User> FIND = new Finder<>(Long.class, User.class);

    @Id
    private Long id;

    public User(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static User findByIdAndType(Long id, String[] types){
        // CAST to Object[] to suppress warning.
        return FIND.where().eq("id", id).in("USER_TYPE", (Object[])types).findUnique();
    }

    public static User findByProperty(String property, Object value){
        return FIND.where().eq(property,value).findUnique();
    }

    public abstract void fillRequestMap(Map<String, String[]> map);

    public abstract List<Commerce> myCommerces();

    @JsonIgnore
    public abstract String getHeaderForReport();

    @JsonIgnore
    public abstract String[] getReportColumns();
}
