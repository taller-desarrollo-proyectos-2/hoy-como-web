package controllers.api.v1;

import annotations.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.CreationException;
import exceptions.DeleteException;
import exceptions.UpdateException;
import models.CommerceUser;
import models.Optional;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.OptionalsService;
import services.SerializerService;

public class Optionals extends Controller {

    private static Logger.ALogger logger = Logger.of("optionals-api");

    @Authenticate(types = "COMMERCE")
    public static Result create(){
        Ebean.beginTransaction();
        try {
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            Form<Optional> form = Form.form(Optional.class, Optional.Creation.class).bindFromRequest();

            if (form.hasErrors()) {
                logger.error("Error en el json de creacion de opcionales", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de creacion de opcionales"));
            }

            Optional optional = form.get();
            //Se crea el opcional para el comercio especificado
            OptionalsService.create(optional, commerceUser.getCommerce());
            Ebean.commitTransaction();
            return ok(Json.toJson(optional));
        }catch(CreationException e){
            logger.error(e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message", e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno intentando crear opcional", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando crear opcional"));
        }finally{
            Ebean.endTransaction();
        }
    }

    @Authenticate(types = "COMMERCE")
    public static Result list(){
        try{
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            return ok(SerializerService.serializeList(Optional.findListByProperty("commerce.id", commerceUser.getCommerce().getId())));
        }catch(Exception e){
            logger.error("Error listando categorias", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando listar opcionales"));
        }
    }

    @Authenticate(types = "COMMERCE")
    public static Result update(Long id){
        Ebean.beginTransaction();
        try {
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            Form<Optional> form = Form.form(Optional.class).bindFromRequest();
            if (form.hasErrors()) {
                logger.error("Error en el json de modificacion de opcionales", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de modificacion del opcional"));
            }
            Optional optional = form.get();

            OptionalsService.update(id, optional, commerceUser.getCommerce());
            Ebean.commitTransaction();
            return ok(Json.toJson(optional));
        }catch(UpdateException e){
            logger.error("Error actualizando el opcional", e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message",e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno intentando actualizar", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando actualizar el opcional"));
        }finally{
            Ebean.endTransaction();
        }
    }

    @Authenticate(types = "COMMERCE")
    public static Result delete(Long id){
        Ebean.beginTransaction();
        try {
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            OptionalsService.delete(id, commerceUser.getCommerce());
            Ebean.commitTransaction();
            return ok();
        }catch(DeleteException e){
            logger.error("Error borrando el opcional", e);
            return badRequest(JsonNodeFactory.instance.objectNode().put("message", e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno borrando el pocional", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando borrar el opcional"));
        }finally {
            Ebean.endTransaction();
        }
    }
}
