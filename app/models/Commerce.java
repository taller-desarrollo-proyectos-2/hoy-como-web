package models;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import services.FinderService;

/**
 *
 * @author facundocaldora
 */
@Entity
public class Commerce extends Model{

    private static float LATITUDE_DISTANCE = 0.00002713F;
    private static float LONGITUDE_DISTANCE = 0.00002695F;

    private static final Map<String, String> attributeMap;
    static {
        Map<String, String> map = new HashMap();
        map.put("name", "name");
        map.put("categories", "categories.name");
        map.put("lat", "lat");
        map.put("lng", "lng");
        attributeMap = Collections.unmodifiableMap(map);
    }

    protected static final Finder<Long, Commerce> FIND = new Finder<>(Long.class, Commerce.class);

    public interface Creation{}

    @Id
    private Long id;

    private String name;

    @ManyToOne
    private Company company;

    @Constraints.Required(groups = {Commerce.Creation.class})
    private String businessName;

    @ManyToMany
    private List<CommerceCategory> categories;
    
    @OneToMany
    private List<Plate> plates;

    @OneToOne
    private License license;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Phone> phones;

    private String email;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OpeningTime> times;

    @OneToOne(cascade = CascadeType.ALL)
    private Location location;

    @Constraints.Required(groups = Commerce.Creation.class)
    private String pictureFileName;

    private long scoreCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Plate> getPlates() {
        return plates;
    }

    public void setPlates(List<Plate> plates) {
        this.plates = plates;
    }

    public List<CommerceCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<CommerceCategory> categories) {
        this.categories = categories;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Commerce findByProperty(String property, Object value){
        return FIND.where().eq(property, value).findUnique();
    }

    public static List<Commerce> findAll(){
        return FIND.all();
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OpeningTime> getTimes() {
        return times;
    }

    public void setTimes(List<OpeningTime> times) {
        this.times = times;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonIgnore
    public long getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(long scoreCount) {
        this.scoreCount = scoreCount;
    }

    public double getScore(){
        return (this.scoreCount / Qualification.countByCommerce(this.getId()));
    }

    public static Map<String, String[]> validateQuery(Map<String, String[]> query){
        Map<String, String[]> validatedQuery = new HashMap();
        for(Map.Entry entry : query.entrySet()){
            if(attributeMap.containsKey(entry.getKey())){
                validatedQuery.put(attributeMap.get(entry.getKey()), query.get(entry.getKey()));
            }
        }
        return validatedQuery;
    }

    public static List<Commerce> findByMap(Map<String, String[]> map){
        ExpressionList<Commerce> exp = FIND.where();
        if(map.containsKey("lat") && map.containsKey("lng")){
            exp = exp.ge("location.lat", Float.valueOf(map.get("lat")[0]) - LATITUDE_DISTANCE)
                .le("location.lat", Float.valueOf(map.get("lat")[0]) + LATITUDE_DISTANCE)
                .ge("location.lng", Float.valueOf(map.get("lng")[0]) - LONGITUDE_DISTANCE)
                .lt("location.lng", Float.valueOf(map.get("lat")[0]) + LONGITUDE_DISTANCE);
            map.remove("lat");
            map.remove("lng");
        }
        return FinderService.findByMap(exp, map).findList();
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }

    public static Commerce findByProperties(List<String> properties, List<Object> values){
        ExpressionList<Commerce> exp = FIND.where();
        for(int i=0; i<properties.size(); i++){
            exp.eq(properties.get(i), values.get(i));
        }
        return exp.findUnique();
    }
}
