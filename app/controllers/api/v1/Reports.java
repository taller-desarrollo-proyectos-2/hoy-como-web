package controllers.api.v1;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.ExportException;
import models.User;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.ExportService;
import services.ReportsService;

import java.io.File;
import java.util.Map;

public class Reports extends Controller {

    private static final Logger.ALogger logger = Logger.of("reports-api");

    @Authenticate(types = {"COMMERCE", "BACKOFFICE"})
    public static Result list(){
        try{
            //Obtengo los reportes
            User user = User.findByProperty("id", Http.Context.current().args.get("userId"));
            DateTime from = DateTime.now().withTimeAtStartOfDay().minusDays(30);
            DateTime to = DateTime.now().withTimeAtStartOfDay();
            Map<String, String[]> queryStrings = request().queryString();
            if(queryStrings.containsKey("from") && queryStrings.containsKey("to")){
                from = DateTime.parse(queryStrings.get("from")[0], DateTimeFormat.forPattern("yyyy-MM-dd")).withTimeAtStartOfDay();
                to = DateTime.parse(queryStrings.get("to")[0], DateTimeFormat.forPattern("yyyy-MM-dd")).withTimeAtStartOfDay();
            }
            return ok(ReportsService.fillReports(user, from, to));
        }catch(Exception e){
            logger.error("Error interno intentando listar reportes", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando listar reportes"));
        }
    }

    @Authenticate(types = {"COMMERCE", "BACKOFFICE"})
    public static Result export(){
        try{
            //Obtengo los reportes
            User user = User.findByProperty("id", Http.Context.current().args.get("userId"));
            DateTime from = DateTime.now().withTimeAtStartOfDay().minusDays(30);
            DateTime to = DateTime.now().withTimeAtStartOfDay();
            Map<String, String[]> queryStrings = request().queryString();
            if(queryStrings.containsKey("from") && queryStrings.containsKey("to")){
                from = DateTime.parse(queryStrings.get("from")[0], DateTimeFormat.forPattern("yyyy-MM-dd")).withTimeAtStartOfDay();
                to = DateTime.parse(queryStrings.get("to")[0], DateTimeFormat.forPattern("yyyy-MM-dd")).withTimeAtStartOfDay();
            }
            File file;
            try{
                file = ExportService.generateFile((ArrayNode)ReportsService.fillReports(user, from, to), user);
            }catch(ExportException e){
                logger.error("Error generando reporte", e);
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error generando reporte"));
            }
            return ok(file);
        }catch(Exception e){
            logger.error("Error interno intentando exportar reportes", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando exportar reportes"));
        }
    }
}
