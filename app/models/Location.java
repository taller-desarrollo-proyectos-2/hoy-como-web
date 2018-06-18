package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Location extends Model {

    protected static final Finder<Long, Location> FIND = new Finder<>(Long.class, Location.class);

    @Id
    private Long id;

    private Double lat;

    private Double lng;

    public Location(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static Location findByProperty(String property, Object value){
        return FIND.where().eq(property, value).findUnique();
    }
}
