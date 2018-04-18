package controllers.api.v1;

import annotations.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.CreationException;
import models.Commerce;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.CommerceServices;
import services.SerializerService;
/*
API Comercios
 */
public class Commerces extends Controller {

    private static Logger.ALogger logger = Logger.of("companies-api");

    /*
     *  Lista todos los comercios.
     */
    @Authenticate(types = { "BACKOFFICE", "FACEBOOK" })
    public static Result list(){
        try {
            return ok(SerializerService.serializeList(Commerce.findAll()));
        }catch(Exception e){
            logger.error("Error interno intentando listar comercios", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando listar comercios"));
        }
    }

    /*
     *  Crea un comercio con nombre y licencia.
     */
    @Authenticate(types = "BACKOFFICE")
    public static Result create(){
        Ebean.beginTransaction();
        try {
            Form<Commerce> form = Form.form(Commerce.class, Commerce.Creation.class).bindFromRequest();
            //Chequeo que el json tenga errores
            if (form.hasErrors()) {
                logger.error("Parametros incorrectos para la creacion del comercio", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de la creacion"));
            }
            //Obtengo y guardo el comercio
            Commerce commerce = form.get();
            CommerceServices.create(commerce);
            Ebean.commitTransaction();
            return ok(Json.toJson(commerce));
        }catch(CreationException e){
            logger.error(e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message", e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno intentando crear comercio", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando crear comercio"));
        }finally{
            Ebean.endTransaction();
        }

    }

    @Authenticate(types = {"BACKOFFICE", "FACEBOOK"})
    public static Result get(Long id){
        try{
            Commerce dbCommerce = Commerce.findByProperty("id", id);
            if(dbCommerce == null){
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Comercio con id "+ id + " no encontrado"));
            }
            return ok(Json.toJson(dbCommerce));
        }catch(Exception e){
            logger.error("Error interno intentando borrar usuarios", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando eliminar usuario"));
        }
    }
}
