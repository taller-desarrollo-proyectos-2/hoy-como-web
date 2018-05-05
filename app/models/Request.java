package models;

import com.avaje.ebean.annotation.EnumValue;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import services.FinderService;

import javax.persistence.*;
import java.util.*;

@Entity
public class Request extends Model {

    public enum Status{
        @EnumValue("WAITING_CONFIRMATION")
        WAITING_CONFIRMATION,
        @EnumValue("ON_PREPARATION")
        ON_PREPARATION,
        @EnumValue("ON_THE_WAY")
        ON_THE_WAY,
        @EnumValue("DELIVERED")
        DELIVERED,
        @EnumValue("CANCELLED_BY_USER")
        CANCELED_BY_USER,
        @EnumValue("CANCELLED_BY_COMMERCE")
        CANCELED_BY_COMMERCE
    }

    private static final Map<String, String> attributeMap;
    static {
        Map<String, String> map = new HashMap();
        map.put("status", "status");
        map.put("userId", "user.id");
        map.put("plateId", "singleRequests.plate.id");
        attributeMap = Collections.unmodifiableMap(map);
    }

    protected static final Finder<Long, Request> FIND = new Finder<>(Long.class, Request.class);

    public interface Creation{}

    @Id
    private Long id;

    @Constraints.Required(groups = Creation.class)
    @OneToMany(cascade = CascadeType.ALL)
    private List<SingleRequest> singleRequests;

    @ManyToOne
    private MobileUser user;

    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date initAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedAt;

    @Constraints.Required(groups = Creation.class)
    @ManyToOne
    private Address destination;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SingleRequest> getSingleRequests() {
        return singleRequests;
    }

    public void setSingleRequests(List<SingleRequest> singleRequests) {
        this.singleRequests = singleRequests;
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

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public static Map<String, String[]> validateQuery(Map<String,String[]> map){
        Map<String, String[]> validatedQuery = new HashMap();
        for(Map.Entry entry : map.entrySet()){
            if(attributeMap.containsKey(entry.getKey())){
                validatedQuery.put(attributeMap.get(entry.getKey()), map.get(entry.getKey()));
            }
        }
        return validatedQuery;
    }
    public static List<Request> findByMap(Map<String, String[]> map){
        return FinderService.findByMap(FIND.where(), map).findList();
    }

    public static Request findByProperty(String property, Object value){
        return FIND.where().eq(property,value).findUnique();
    }

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }
}
