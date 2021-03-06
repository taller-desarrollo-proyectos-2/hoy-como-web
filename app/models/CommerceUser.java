package models;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import play.data.validation.Constraints;
import play.libs.Json;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Arrays;
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
                "\t\t\"showName\": \"Dashboard\",\n" +
                "\t\t\"route\": \"/web/monitor\",\n" +
                "\t\t\"icon\": \"fa fa-tachometer\"\n" +
                "\t},{\n" +
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
                "\t},{\n" +
                "\t\t\"showName\": \"Reportes\",\n" +
                "\t\t\"route\": \"/web/commerce/reports\",\n" +
                "\t\t\"icon\": \"fa fa-bar-chart\"\n" +
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

    @Override
    public List<Commerce> myCommerces(){
        return Arrays.asList(this.getCommerce());
    }

    @Override
    public String getHeaderForReport() {
        return "REPORTE DE COMERCIO" +  " - " +  this.getCommerce().getBusinessName();
    }

    @Override
    public String[] getReportColumns(){
        return new String[]{"Día", "Pedidos Entregados", "Lead Time", "Calificación", "Facturado", "Fee"};
    }
}
