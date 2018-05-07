package controllers.api.v1;

import annotations.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.CreationException;
import exceptions.UpdateException;
import models.BackofficeUser;
import models.Commerce;
import models.CommerceUser;
import models.User;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.SerializerService;
import services.UsersService;

public class CommerceUsers extends Controller {

    private static Logger.ALogger logger = Logger.of("backoffice-users-api");

    @Authenticate(types = "BACKOFFICE")
    public static Result create(){
        Ebean.beginTransaction();
        try {
            Form<CommerceUser> form = Form.form(CommerceUser.class, BackofficeUser.Creation.class).bindFromRequest();
            //Chequeo que se hayan enviado los parametros necesarios
            if (form.hasErrors()) {
                logger.info("Intento de creacion de usuario de comercio con parametros incorrectos", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Errores en los campos de creacion"));
            }

            CommerceUser user = form.get();

            UsersService.create(user);
            Ebean.commitTransaction();
            return ok(Json.toJson(user));
        }catch(CreationException e){
            logger.error(e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message", e.getMessage()));
        }catch(Exception e){
            logger.error("Error while creating backoffice user", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando crear usuario"));
        }finally{
            Ebean.endTransaction();
        }
    }

    @Authenticate(types = "BACKOFFICE")
    public static Result update(Long id){
        Ebean.beginTransaction();
        try{
            Form<CommerceUser> form = Form.form(CommerceUser.class).bindFromRequest();
            if (form.hasErrors()) {
                logger.error("Error en el json de modificacion de usuarios de comercio", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de modificacion del usuario de comercio"));
            }
            CommerceUser user = form.get();

            UsersService.update(id, user);
            Ebean.commitTransaction();
            return ok(Json.toJson(user));
        }catch(UpdateException e){
            logger.error("Error actualizando el usuario de comercio", e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message",e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno intentando actualizar el usuario de comercio", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando actualizar el usuario de comercio"));
        }finally{
            Ebean.endTransaction();
        }
    }

    @Authenticate(types = "BACKOFFICE")
    public static Result list(){
        try{
            return ok(SerializerService.serializeList(CommerceUser.findAll()));
        }catch(Exception e){
            logger.error("Error interno intentando listar usuarios", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando listar usuarios"));
        }
    }
}
