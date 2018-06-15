package models;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.CreationException;
import play.data.validation.Constraints;
import services.FacebookServices;

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

    public static FacebookUser createIfNecessary(FacebookUser user) throws CreationException {
        FacebookUser dbUser = FIND.where().eq("token", user.getToken()).findUnique();
        if(dbUser == null) {
            JsonNode fbResponse = FacebookServices.validateUser(user);
            if(fbResponse.has("error")){
                throw new CreationException("ID de usuario invalido");
            }
            user.setFullName(fbResponse.get("name").asText());
            user.save();
            dbUser = new FacebookUser();
            dbUser.setId(user.getId());
        }
        return dbUser;
    }
}
