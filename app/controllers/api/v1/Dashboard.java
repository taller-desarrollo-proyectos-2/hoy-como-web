package controllers.api.v1;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import models.CommerceUser;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.DashBoardServices;
import java.util.Map;

public class Dashboard extends Controller {

    private static final Logger.ALogger logger = Logger.of("dashboard-api");

    @Authenticate(types = "COMMERCE")
    public static Result info(){
        try{
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            DateTime from = DateTime.now().withTimeAtStartOfDay();
            DateTime to = DateTime.now().withTimeAtStartOfDay().plusDays(1);
            //Me fijo si tiene filtros
            Map<String, String[]> queryStrings = request().queryString();
            if(queryStrings.containsKey("from") && queryStrings.containsKey("to")){
                from = DateTime.parse(queryStrings.get("from")[0], DateTimeFormat.forPattern("yyyy-MM-dd"));
                to = DateTime.parse(queryStrings.get("to")[0], DateTimeFormat.forPattern("yyyy-MM-dd"));
            }

            JsonNode info = DashBoardServices.getCommerceInformation(commerceUser.getCommerce(), from, to);

            return ok(info);
        }catch(Exception e){
            logger.error("Error interno intentando obtener datos del dashboard", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno obteniendo datos para el dashboard"));
        }

    }

}
