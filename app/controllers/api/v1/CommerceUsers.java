package controllers.api.v1;

import annotations.Authenticate;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.CreationException;
import models.BackofficeUser;
import models.CommerceUser;
import models.User;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.UsersService;

public class CommerceUsers extends Controller {

    private static Logger.ALogger logger = Logger.of("backoffice-users-api");

    @Authenticate(types = "BACKOFFICE")
    public static Result create(){
        try {
            Form<CommerceUser> form = Form.form(CommerceUser.class, BackofficeUser.Creation.class).bindFromRequest();
            //Chequeo que se hayan enviado los parametros necesarios
            if (form.hasErrors()) {
                logger.info("Intento de creacion de usuario de comercio con parametros incorrectos", form.errorsAsJson());
                return badRequest(form.errorsAsJson());
            }

            CommerceUser user = form.get();

            UsersService.create(user);

            return ok(Json.toJson(user));
        }catch(CreationException e){
            logger.error(e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message", e.getMessage()));
        }catch(Exception e){
            logger.error("Error while creating backoffice user", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error while creating backoffice user"));
        }
    }
}