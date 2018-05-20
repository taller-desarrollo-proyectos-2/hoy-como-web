package controllers.api.v1;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.UpdateException;
import models.MobileUser;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.UsersService;

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

    @Authenticate(types = "FACEBOOK")
    public static Result update(){
        try{
            MobileUser mobileUser = MobileUser.findByProperty("id", Http.Context.current().args.get("userId"));
            if(mobileUser == null){
                logger.info("Usuario mobile no encontrado");
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Usuario no encontrado"));
            }
            Form<MobileUser> form = Form.form(MobileUser.class).bindFromRequest();
            //Chequeo que se hayan enviado los parametros necesarios
            if (form.hasErrors()) {
                logger.info("Intento de actualizacion de usuario mobile con parametros incorrectos", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Errores en los campos de actualizacion"));
            }
            MobileUser userToUpdate = form.get();
            UsersService.update(mobileUser.getId(), userToUpdate);
            return ok(Json.toJson(mobileUser));
        }catch(Exception e) {
            logger.error("Error intentando actualizar el usuario", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando actualizar informacion del usuario"));
        }
    }

    @Authenticate(types = "FACEBOOK")
    public static Result addToFavourite(Long id){
        try{
            MobileUser mobileUser = MobileUser.findByProperty("id", Http.Context.current().args.get("userId"));
            if(mobileUser == null){
                logger.info("Usuario mobile no encontrado");
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Usuario no encontrado"));
            }
            UsersService.addToFavourite(id, mobileUser);
            return ok();
        }catch(UpdateException e){
            logger.error(e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Id de comercio no utilizado"));
        }catch(Exception e){
            logger.error("Error interno intenando agregar a favorito",e );
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error intentando agregar a favorito"));
        }
    }

    @Authenticate(types = "FACEBOOK")
    public static Result removeFromFavourite(Long id){
        try{
            MobileUser mobileUser = MobileUser.findByProperty("id", Http.Context.current().args.get("userId"));
            if(mobileUser == null){
                logger.info("Usuario mobile no encontrado");
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Usuario no encontrado"));
            }
            UsersService.removeFromFavourite(id, mobileUser);
            return ok();
        }catch(Exception e){
            logger.error("Error interno intenando remover comercio favorito",e );
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error intentando remover comercio favorito"));
        }
    }
}
