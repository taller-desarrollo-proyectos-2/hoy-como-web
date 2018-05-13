package controllers.api.v1;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class Users extends Controller {

    private static Logger.ALogger logger = Logger.of("users-api");

    @Authenticate(types = {"COMMERCE", "BACKOFFICE", "MOBILE", "FACEBOOK"})
    public static Result myInfo(){
        try{
            User user = User.findByProperty("id", Http.Context.current().args.get("userId"));
            if(user == null){
                logger.info("Buscando un usuario no encontrado");
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Usuario no encontrado"));
            }
            return ok(Json.toJson(user));
        }catch(Exception e){
            logger.error("Error interno obteniendo informacion del usuario");
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno obteniendo informacion del usuario"));
        }
    }
}
