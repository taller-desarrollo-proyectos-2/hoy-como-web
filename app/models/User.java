package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE")
public class User extends Model {

    protected static final Finder<Long, User> FIND = new Finder<>(Long.class, User.class);

    public interface Creation{
    }

    @Id
    private Long id;

    @Constraints.Required(groups = Creation.class)
    private String username;

    @Constraints.Required(groups = Creation.class)
    private String password;

    public User(){
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User findByUsername(String username){
        return FIND.where().eq("username", username).findUnique();
    }

    public static User findByIdAndType(Long id, String[] types){
        return FIND.where().eq("id", id).in("USER_TYPE", types).findUnique();
    }
}
