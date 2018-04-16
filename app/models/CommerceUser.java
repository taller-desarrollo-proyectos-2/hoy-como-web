package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.libs.Json;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@DiscriminatorValue("COMMERCE")
@Table(name = "user")
public class CommerceUser extends BackofficeUser {

    protected static final Finder<Long, CommerceUser> FIND = new Finder<>(Long.class, CommerceUser.class);

    @OneToOne
    @Constraints.Required(groups = Creation.class)
    private Commerce commerce;

    public Commerce getCommerce() {
        return commerce;
    }

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
    }

    @Override
    public JsonNode getPanel(){
        return Json.parse("[{\n" +
                "\t\t\"showName\": \"Menu\",\n" +
                "\t\t\"route\": \"/web/plates/commerce/:id\",\n" +
                "\t\t\"icon\": \"glyphicons glyphicons-fast-food\"\n" +
                "\t}]");
    }

    public static List<CommerceUser> findAll(){
        return FIND.all();
    }

    public static CommerceUser findByProperty(String property, Object value){
        return FIND.where().eq(property, value).findUnique();
    }
}
