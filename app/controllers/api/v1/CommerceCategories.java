package controllers.api.v1;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import models.CommerceCategory;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.SerializerService;

public class CommerceCategories extends Controller {

    private static Logger.ALogger logger = Logger.of("commerce-categories");

    @Authenticate(types = {"BACKOFFICE"})
    public static Result list(){
        try{
            return ok(SerializerService.serializeList(CommerceCategory.findAll()));
        }catch(Exception e){
            logger.error("Error intentando listar categorias de comercios");
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando listar categorias de comercios"));
        }
    }
}
