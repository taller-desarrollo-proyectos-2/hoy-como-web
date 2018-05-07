package controllers.api.v1;

import annotations.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.CreationException;
import exceptions.DeleteException;
import exceptions.UpdateException;
import models.Category;
import models.Commerce;
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
import java.util.List;
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
                if(FolderServices.fileExists(FolderServices.getCommerceFolder(commerceUser.getCommerce()) + pictureFilePart.getFilename())){
                    logger.error("Imagen con nombre ya utilizado");
                    return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Nombre de imagen ya utilizado"));
                }
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
            List<Plate> plates = PlatesServices.findFilteredPlates(queryStrings, commerceUser);
            return ok(SerializerService.serializeList(plates));
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
            //Guardo la imagen del plato
            Http.MultipartFormData formData = request().body().asMultipartFormData();
            Plate plate = form.get();

            if(formData != null){
                Http.MultipartFormData.FilePart pictureFilePart = formData.getFile("picture");
                if(FolderServices.fileExists(FolderServices.getCommerceFolder(commerceUser.getCommerce()) + pictureFilePart.getFilename()) && !Plate.findByProperty("id", id).getPictureFileName().equals(pictureFilePart.getFilename())){
                    logger.error("Imagen con nombre ya utilizado");
                    return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Nombre de imagen ya utilizado"));
                }
                pictureFilePart.getFile().renameTo(new File(FolderServices.getCommerceFolder(commerceUser.getCommerce()c) + pictureFilePart.getFilename()));
            }
            //Se actualiza el plato para el comercio especificado
            PlatesServices.update(id, plate, commerceUser.getCommerce());
            Ebean.commitTransaction();
            return ok(Json.toJson(Plate.findByProperty("id", id)));
        }catch(UpdateException e){
            logger.error(e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message",e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno intentando actualizar", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando actualizar el plato"));
        }finally{
            Ebean.endTransaction();
        }
    }

    @Authenticate(types = {"COMMERCE", "FACEBOOK"})
    public static Result getImage(Long id){
        try{
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            Plate dbPlate = Plate.findByProperty("id", id);
            if(dbPlate == null){
                logger.info("Intentando buscar un plato con id inexistente");
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Plato con id no encontrado"));
            }
            if(commerceUser != null){
                if(!commerceUser.getCommerce().getPlates().contains(dbPlate)){
                    logger.error("El id pertenece a un plato que no es de este comercio, o inexistente.");
                    return badRequest(JsonNodeFactory.instance.objectNode().put("message", "El id del plato no es de este comercio, o es inexistente"));
                }
            }
            if(!FolderServices.fileExists(FolderServices.getCommerceFolder(dbPlate.getCommerce()) + dbPlate.getPictureFileName())){
                logger.error("Imagen no encontrada para el comercio: ", dbPlate.getCommerce().getBusinessName());
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Archivo no encontrado"));
            }
            return ok(FolderServices.getFile(FolderServices.getCommerceFolder(dbPlate.getCommerce()) + dbPlate.getPictureFileName()));
        }catch(Exception e){
            logger.error("Error intentando obtener imagen ", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno buscando imagen de plato"));
        }
    }

    @Authenticate(types = "COMMERCE")
    public static Result delete(Long id){
        Ebean.beginTransaction();
        try {
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            PlatesServices.delete(id, commerceUser.getCommerce());
            Ebean.commitTransaction();
            return ok();
        }catch(DeleteException e){
            logger.error("Error borrando el plato", e);
            return badRequest(JsonNodeFactory.instance.objectNode().put("message", e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno borrando el plato", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando borrar el plato"));
        }finally {
            Ebean.endTransaction();
        }
    }
}
