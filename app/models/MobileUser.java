package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("MOBILE")
@Table(name = "user")
public class MobileUser extends User {

    protected static final Finder<Long, MobileUser> FIND = new Finder<>(Long.class, MobileUser.class);

    private String fullName;

    @ManyToMany
    private List<Commerce> favourites;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    private List<Address> addresses;

    private String appToken;

    public List<Commerce> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Commerce> favourites) {
        this.favourites = favourites;
    }

    public static MobileUser findByProperty(String property, Object value){
        return FIND.where().eq(property,value).findUnique();
    }

    @Override
    public void fillRequestMap(Map<String, String[]> map) {
        map.put("user.id", new String[]{this.getId().toString()});
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonIgnore
    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }
}
