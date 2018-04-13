package models;

import play.data.validation.Constraints;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BACKOFFICE")
public class BackofficeUser extends User {

    protected static final Finder<Long, BackofficeUser> FIND = new Finder<>(Long.class, BackofficeUser.class);

    public interface Creation{
    }

    @Constraints.Required(groups = Creation.class)
    private String username;

    @Constraints.Required(groups = Creation.class)
    private String password;

    public BackofficeUser(){ super(); }

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

    public static BackofficeUser findByUsername(String username){
        return FIND.where().eq("username", username).findUnique();
    }
}
