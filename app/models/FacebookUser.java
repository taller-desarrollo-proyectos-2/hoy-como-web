package models;

import play.data.validation.Constraints;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("FACEBOOK")
@Table(name = "user")
public class FacebookUser extends MobileUser {

    protected static final Finder<Long, FacebookUser> FIND = new Finder<>(Long.class, FacebookUser.class);

    public interface Login{}

    @Constraints.Required(groups = Login.class)
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static FacebookUser createIfNecessary(FacebookUser user){
        FacebookUser dbUser = FIND.where().eq("token", user.getToken()).findUnique();
        if(dbUser == null) {
            user.save();
            return user;
        }
        return dbUser;
    }
}
