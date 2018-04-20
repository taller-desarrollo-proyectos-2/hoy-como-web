package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE")
public class User extends Model {

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
}
