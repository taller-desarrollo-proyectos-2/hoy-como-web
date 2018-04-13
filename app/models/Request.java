package models;

import com.avaje.ebean.annotation.EnumValue;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

@Entity
public class Request extends Model {

    public enum Status{
        @EnumValue("ON_PREPARATION")
        ON_PREPARATION,
        @EnumValue("ON_THE_WAY")
        ON_THE_WAY,
        @EnumValue("DELIVERED")
        DELIVERED,
        @EnumValue("CANCELED_BY_USER")
        CANCELED_BY_USER,
        @EnumValue("CANCELED_BY_COMMERCE")
        CANCELED_BY_COMMERCE
    }

    @Id
    private Long id;

    @ManyToMany
    private List<Plate> plates;

    @ManyToOne
    private MobileUser user;

    private Status status;

    private Date initAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Plate> getPlates() {
        return plates;
    }

    public void setPlates(List<Plate> plates) {
        this.plates = plates;
    }

    public MobileUser getUser() {
        return user;
    }

    public void setUser(MobileUser user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getInitAt() {
        return initAt;
    }

    public void setInitAt(Date initAt) {
        this.initAt = initAt;
    }
}
