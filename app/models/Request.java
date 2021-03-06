package models;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.annotation.EnumValue;
import org.joda.time.DateTime;
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
        CANCELLED_BY_USER,
        @EnumValue("CANCELLED_BY_COMMERCE")
        CANCELLED_BY_COMMERCE
    }

    private static final Map<String, String> attributeMap;
    static {
        Map<String, String> map = new HashMap();
        map.put("status", "status");
        map.put("userId", "user.id");
        map.put("plateId", "singleRequests.plate.id");
        map.put("commerceId", "singleRequests.plate.commerce.id");
        map.put("from", "from_initAt");
        map.put("to", "to_initAt");
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

    @OneToOne(cascade = CascadeType.ALL)
    private PaymentType paymentType;

    private Long leadTime;

    private String rejectedReason;

    private Double total;

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
        Map<String, String[]> validatedQuery = new HashMap<>();
        for(Map.Entry<String, String[]> entry : map.entrySet()){
            if(attributeMap.containsKey(entry.getKey())){
                validatedQuery.put(attributeMap.get(entry.getKey()), map.get(entry.getKey()));
            }
        }
        return validatedQuery;
    }
    public static List<Request> findByMap(Map<String, String[]> map){
        return FinderService.findByMap(FIND.where(), map).orderBy("initAt DESC").findList();
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

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Long getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Long leadTime) {
        this.leadTime = leadTime;
    }

    public static List<Request> findListByProperty(String property, Object value){
        return FIND.where().eq(property, value).findList();
    }

    public Long getCommerceId(){
        if(this.singleRequests.isEmpty()){
            return null;
        }
        return Plate.findByProperty("id", this.singleRequests.get(0).getPlate().getId()).getCommerce().getId();
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public double getTotal(){
        return total;
    }

    public boolean isQualified() {
        return (Qualification.findByProperty("request.id", this.getId()) != null);
    }
    public static int countByPropertiesAt(List<String> properties, List<Object> values, DateTime from, DateTime to){
        ExpressionList<Request> exp = FIND.where();
        for(int i=0; i<properties.size(); i++){
            exp.eq(properties.get(i), values.get(i));
        }
        exp.ge("initAt",from).le("initAt", to);
        return exp.findRowCount();
    }

    public static double countMoneyByCommerceAndDate(Long commerceId, DateTime from, DateTime to){
        List<Request> requests = FIND.where().eq("singleRequests.plate.commerce.id", commerceId)
                                                .ge("initAt", from)
                                                .le("initAt", to).findList();
        double total = 0;
        for(Request req: requests){
            total += req.getTotal();
        }
        return total;
    }

    public static List<Request> findListByPropertyAt(String property, Object value, DateTime from, DateTime to){
        return FIND.where().eq(property, value).ge("initAt", from).le("initAt", to).findList();
    }

    public void addTotal(){
        double total = 0;
        for(SingleRequest req: this.getSingleRequests()){
            Plate plate = Plate.findByProperty("id", req.getPlate().getId());
            total+= (plate.getPrice()*req.getQuantity() - plate.getPrice()*req.getQuantity()*(plate.getDiscount()/100D));
            for(Optional opt: req.getOptionals()){
                Optional dbOpt = Optional.findByProperty("id", opt.getId());
                total+= (dbOpt.getPrice()*req.getQuantity() - dbOpt.getPrice()*req.getQuantity()*(plate.getDiscount()/100D));
            }
        }
        this.total = total;
    }
}
