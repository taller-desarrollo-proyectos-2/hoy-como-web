package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("MOBILE")
@Table(name = "user")
public class MobileUser extends User {

    protected static final Finder<Long, MobileUser> FIND = new Finder<>(Long.class, MobileUser.class);

    @ManyToMany
    private List<Commerce> favourites;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    private List<Address> addresses;

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
}
