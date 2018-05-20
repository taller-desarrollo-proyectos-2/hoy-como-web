package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.libs.Json;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("COMMERCE")
@Table(name = "user")
public class CommerceUser extends BackofficeUser {

    protected static final Finder<Long, CommerceUser> FIND = new Finder<>(Long.class, CommerceUser.class);

    @ManyToOne
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
                "\t\t\"showName\": \"Categorías\",\n" +
                "\t\t\"route\": \"/web/categories\",\n" +
                "\t\t\"icon\": \"glyphicon glyphicon-cutlery\"\n" +
                "\t},{\n" +
                "\t\t\"showName\": \"Platos\",\n" +
                "\t\t\"route\": \"/web/plates\",\n" +
                "\t\t\"icon\": \"glyphicon glyphicon-cutlery\"\n" +
                "\t},{\n" +
                "\t\t\"showName\": \"Opcionales\",\n" +
                "\t\t\"route\": \"/web/optionals\",\n" +
                "\t\t\"icon\": \"glyphicon glyphicon-cutlery\"\n" +
                "\t},{\n" +
                "\t\t\"showName\": \"Pedidos\",\n" +
                "\t\t\"route\": \"/web/commerce/requests\",\n" +
                "\t\t\"icon\": \"fa fa-list-alt\"\n" +
                "\t},{\n" +
                "\t\t\"showName\": \"Calificaciones\",\n" +
                "\t\t\"route\": \"/web/commerce/qualifications\",\n" +
                "\t\t\"icon\": \"fa fa-star-o\"\n" +
                "\t}]");
    }

    public static List<CommerceUser> findAll(){
        return FIND.all();
    }

    public static CommerceUser findByProperty(String property, Object value){
        return FIND.where().eq(property, value).findUnique();
    }

    @Override
    public void fillRequestMap(Map<String, String[]> map) {
        map.put("singleRequests.plate.commerce.id", new String[]{this.getCommerce().getId().toString()});
    }

    public int getLeadTime(){
        List<Request> commerceRequests = Request.findListByProperty("singleRequests.plate.commerce.id", this.getId());
        int total = 0;
        for(Request req : commerceRequests){
            total += req.getLeadTime();
        }
        return commerceRequests.isEmpty() ? 30 : total/commerceRequests.size();
    }
}
