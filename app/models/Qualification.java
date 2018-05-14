package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Qualification extends Model {

    protected static final Finder<Long, Qualification> FIND = new Finder<>(Long.class, Qualification.class);

    public interface Creation{}

    @Id
    private Long id;

    @OneToOne
    @Constraints.Required(groups = Creation.class)
    private Request request;

    @ManyToOne
    @Constraints.Required(groups = Creation.class)
    private MobileUser user;

    private int score;

    private String comment;

    private String response;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public MobileUser getUser() {
        return user;
    }

    public void setUser(MobileUser user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static Qualification findByProperty(String property, Object value){
        return FIND.where().eq(property,value).findUnique();
    }
}
