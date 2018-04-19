package controllers.api.v1;

import annotations.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.CreationException;
import exceptions.UpdateException;
import models.Category;
import models.CommerceUser;
import models.Plate;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.FolderServices;
import services.PlatesServices;
import services.SerializerService;

import java.io.File;
import java.util.Map;

public class Plates extends Controller {

    private static Logger.ALogger logger = Logger.of("plates-api");

    @Authenticate(types = "COMMERCE")
    public static Result create(){
        Ebean.beginTransaction();
        try {
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            Form<Plate> form = Form.form(Plate.class, Plate.Creation.class).bindFromRequest();

            if (form.hasErrors()) {
                logger.error("Error en el json de creacion de platos", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de creacion del plato"));
            }
            //Guardo la imagen del plato
            Http.MultipartFormData formData = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart pictureFilePart = formData.getFile("picture");

            if (pictureFilePart != null) {
                pictureFilePart.getFile().renameTo(new File(FolderServices.getCommerceFolder(commerceUser.getCommerce()) + pictureFilePart.getFilename()));
            }else{
                logger.error("Intentando crear un plato sin imagen");
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "No es posible crear un plato sin imagen"));
            }

            Plate plate = form.get();
            //Se crea el plato para el comercio especificado
            PlatesServices.create(plate, commerceUser.getCommerce());
            Ebean.commitTransaction();
            return ok(Json.toJson(plate));
        }catch(CreationException e){
            logger.error(e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message", e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno intentando crear plato", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando crear plato"));
        }finally{
            Ebean.endTransaction();
        }
    }

    @Authenticate(types = {"COMMERCE", "FACEBOOK"})
    public static Result list(){
        try{
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            Map<String, String[]> queryStrings = request().queryString();
            Map<String, Object> validatedQuery = PlatesServices.validateQuery(queryStrings);
            if(commerceUser == null){
                return ok(SerializerService.serializeList(Plate.findListByMap(validatedQuery)));
            }else{
                return ok(SerializerService.serializeList(Plate.findListByProperty("commerce.id", commerceUser.getCommerce().getId())));
            }
        }catch(Exception e){
            logger.error("Error listando categorias", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando listar categorias"));
        }
    }

    @Authenticate(types = "COMMERCE")
    public static Result update(Long id){
        Ebean.beginTransaction();
        try {
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            Form<Plate> form = Form.form(Plate.class).bindFromRequest();
            if (form.hasErrors()) {
                logger.error("Error en el json de modificacion de platos", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de modificacion del plato"));
            }
            Plate plate = form.get();
            //Se actualiza el plato para el comercio especificado
            PlatesServices.update(id, plate, commerceUser.getCommerce());
            Ebean.commitTransaction();
            return ok(Json.toJson(plate));
        }catch(UpdateException e){
            logger.error("Error actualizando el plato", e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message",e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno intentando actualizar", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando actualizar el plato"));
        }finally{
            Ebean.endTransaction();
        }
    }
}
