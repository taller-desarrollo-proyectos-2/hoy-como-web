package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.data.validation.Constraints;
import play.libs.Json;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("BACKOFFICE")
public class BackofficeUser extends User {

    protected static final Finder<Long, BackofficeUser> FIND = new Finder<>(Long.class, BackofficeUser.class);

    public interface Creation{
    }

    @Constraints.Required(groups = Creation.class)
    private String username;

    @Constraints.Required(groups = Creation.class)
    private String password;

    public BackofficeUser(){ super(); }

    @Override
    public void fillRequestMap(Map<String, String[]> map) {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static BackofficeUser findByUsername(String username){
        return FIND.where().eq("username", username).findUnique();
    }

    public JsonNode getPanel(){
        return Json.parse("[{\n" +
                "\t\t\"showName\": \"Comercios\",\n" +
                "\t\t\"route\": \"/web/commerces\",\n" +
                "\t\t\"icon\": \"glyphicon glyphicon-cutlery\"\n" +
                "\t},{\n" +
                "\t\t\"showName\": \"Usuarios\",\n" +
                "\t\t\"route\": \"/web/commerce/users\",\n" +
                "\t\t\"icon\": \"glyphicon glyphicon-user\"\n" +
                "\t},{\n" +
                "\t\t\"showName\": \"Pedidos\",\n" +
                "\t\t\"route\": \"/web/root/requests\",\n" +
                "\t\t\"icon\": \"fa fa-list-alt\"\n" +
                "\t}]");
    }
}
