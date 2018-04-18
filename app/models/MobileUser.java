package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("MOBILE")
public class MobileUser extends User {

    @ManyToMany
    private List<Commerce> favourites;

    public List<Commerce> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<Commerce> favourites) {
        this.favourites = favourites;
    }

}
