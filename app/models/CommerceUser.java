package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;
import play.libs.Json;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("COMMERCE")
@Table(name = "user")
public class CommerceUser extends BackofficeUser {

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
}
