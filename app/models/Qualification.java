package models;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.annotation.CreatedTimestamp;
import org.joda.time.DateTime;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Qualification extends Model {

    protected static final Finder<Long, Qualification> FIND = new Finder<>(Long.class, Qualification.class);

    public interface Creation{}

    public interface Update{}

    @Id
    private Long id;

    @OneToOne
    @Constraints.Required(groups = Creation.class)
    private Request request;

    @ManyToOne
    private MobileUser user;

    @Constraints.Required(groups = Creation.class)
    private int score;

    private String comment;

    @Constraints.Required(groups = Update.class)
    private String response;

    @CreatedTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date qualifiedAt;

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

    public static int countByCommerce(Long commerceId){
        return FIND.where().eq("request.singleRequests.plate.commerce.id", commerceId).findRowCount();
    }

    public static List<Qualification> findListByProperty(String property, Object value){
        return FIND.where().eq(property, value).orderBy("qualifiedAt DESC").findList();
    }

    public Date getQualifiedAt() {
        return qualifiedAt;
    }

    public void setQualifiedAt(Date qualifiedAt) {
        this.qualifiedAt = qualifiedAt;
    }

    public static int countByPropertiesEQAndNEAt(List<String> propertiesE, List<Object> valuesE,List<String> propertiesNE, List<Object> valuesNE, DateTime from, DateTime to){
        ExpressionList<Qualification> exp = FIND.where();
        for(int i=0; i<propertiesE.size(); i++){
            exp.eq(propertiesE.get(i), valuesE.get(i));
        }
        for(int i=0; i<propertiesNE.size(); i++){
            exp.ne(propertiesNE.get(i), valuesNE.get(i));
        }
        exp.ge("qualifiedAt", from).le("qualifiedAt", to);
        return exp.findRowCount();
    }

    public static List<Qualification> findListByPropertyAt(String property, Object value, DateTime from, DateTime to){
        return FIND.where().eq(property, value).ge("qualifiedAt", from).le("qualifiedAt", to).findList();
    }

    public static List<Qualification> findFromRequests(List<Request> requests){
        Set<Long> reqIds = new HashSet<>();
        for(Request req : requests){
            reqIds.add(req.getId());
        }
        return FIND.where().in("request.id", reqIds).findList();
    }
}
