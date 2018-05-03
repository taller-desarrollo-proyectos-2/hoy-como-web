package models;

import javax.persistence.*;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import services.FinderService;

import java.util.*;

/**
 *
 * @author facundocaldora
 */
@Entity
public class  Plate extends Model{

    private static final Map<String, String> attributeMap;
    static {
        Map<String, String> map = new HashMap();
        map.put("name", "name");
        map.put("commerce", "commerce.name");
        map.put("price", "price");
        map.put("active", "active");
        map.put("glutenFree", "glutenFree");
        map.put("deletedAt", "deletedAt");
        attributeMap = Collections.unmodifiableMap(map);
    }
    protected static final Finder<Long, Plate> FIND = new Finder<>(Long.class, Plate.class);

    public interface Creation{}
    
    @Id
    private Long id;

    @Constraints.Required(groups = Creation.class)
    private String name;

    @ManyToOne
    private Commerce commerce;

    @ManyToOne
    @Constraints.Required(groups = Creation.class)
    private Category category;

    @ManyToMany
    private List<Optional> optionals;
    
    private Float price;

    private Promo promo;

    private Boolean active;

    private Boolean glutenFree;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Constraints.Required(groups = Creation.class)
    private String pictureFileName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Optional> getOptionals() {
        return optionals;
    }

    public void setOptionals(List<Optional> optionals) {
        this.optionals = optionals;
    }

    public float getPrice() {
        return price;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(Boolean glutenFree) {
        this.glutenFree =  glutenFree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public Commerce getCommerce() {
        return commerce;
    }

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }

    public static Plate findByProperty(String property, Object value){
        return FIND.where().eq(property, value).findUnique();
    }
    public static Plate findByProperties(List<String> properties, List<Object> values){
        ExpressionList<Plate> exp = FIND.where();
        for(int i=0; i<properties.size(); i++){
            exp.eq(properties.get(i), values.get(i));
        }
        return exp.findUnique();
    }

    public static List<Plate> findListByProperty(String property, Object value){
        return FIND.where().eq(property, value).findList();
    }

    public static List<Plate> findListByMap(Map<String, Object> map){
        return FIND.where().allEq(map).findList();
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
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

    public static List<Plate> findByMap(Map<String, String[]> map){
        return FinderService.findByMap(FIND.where(), map).findList();
    }

}
