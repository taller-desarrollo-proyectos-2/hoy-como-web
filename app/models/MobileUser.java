package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@DiscriminatorValue("MOBILE")
public class MobileUser extends User {

    @ManyToMany
    private List<Commerce> favourites;

    public MobileUser(){ super (); }

    public MobileUser(String username, String password) {
        super(username, password);
    }

    public List<Commerce> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Commerce> favourites) {
        this.favourites = favourites;
    }


}
