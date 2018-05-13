package controllers.api.v1;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import models.MobileUser;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class MobileUsers extends Controller {

    private static Logger.ALogger logger = Logger.of("mobile-users-api");

    @Authenticate(types = "FACEBOOK")
    public static Result myAddresses(){
        try{
            MobileUser mobileUser = MobileUser.findByProperty("id", Http.Context.current().args.get("userId"));
            if(mobileUser == null){
                logger.info("Usuario mobile no encontrado");
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Usuario no encontrado"));
            }
            return ok(Json.toJson(mobileUser.getAddresses()));// == null ? new ArrayList() : mobileUser.getAddresses()));
        }catch(Exception e){
            logger.error("Error intentando listar direcciones", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando listar direcciones"));
        }
    }

    @Authenticate(types = "FACEBOOK")
    public static Result addAddress(){
        try{
            return ok();
        }catch(Exception e){
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando crear direccion "));
        }
    }
}
