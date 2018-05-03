package controllers.api.v1;

import annotations.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.CreationException;
import models.Commerce;
import models.CommerceUser;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.CommerceServices;
import services.FolderServices;
import services.SerializerService;

import java.io.File;
import java.util.List;
import java.util.Map;

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
            //Me fijo si tiene filtros
            Map<String, String[]> queryStrings = request().queryString();
            List<Commerce> filteredCommerces = CommerceServices.findFilteredCommerces(queryStrings);
            return ok(SerializerService.serializeList(filteredCommerces));
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
            //Guardo la imagen del comercio
            Http.MultipartFormData formData = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart pictureFilePart = formData.getFile("picture");
            
            //Obtengo y guardo el comercio
            Commerce commerce = form.get();
            FolderServices.createCommerceFolder(commerce);

            if (pictureFilePart != null) {
                if(FolderServices.fileExists(FolderServices.getCommerceFolder(commerce) + pictureFilePart.getFilename())){
                    logger.error("Imagen con nombre ya utilizado");
                    return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Nombre de imagen ya utilizado"));
                }
                pictureFilePart.getFile().renameTo(new File(FolderServices.getCommerceFolder(commerce) + pictureFilePart.getFilename()));
            }else{
                logger.error("Intentando crear un comercio sin imagen");
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "No es posible crear un comercio sin imagen"));
            }
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

    @Authenticate(types = {"BACKOFFICE", "FACEBOOK"})
    public static Result getImage(Long id){
        try{
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            Commerce commerce = Commerce.findByProperty("id", id);
            if(commerce == null){
                logger.info("Intentando buscar un comercio con id inexistente");
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Comercio con id no encontrado"));
            }
            if(commerceUser != null){
                if(!commerceUser.getCommerce().equals(commerce)){
                    logger.error("El id pertenece a un comercio que no es este comercio, o inexistente.");
                    return badRequest(JsonNodeFactory.instance.objectNode().put("message", "El id del comercio no es este, o es inexistente"));
                }
            }
            if(!FolderServices.fileExists(FolderServices.getCommerceFolder(commerce) + commerce.getPictureFileName())){
                logger.error("Imagen no encontrada para el comercio: ", commerce.getBusinessName());
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Archivo no encontrado"));
            }
            return ok(FolderServices.getFile(FolderServices.getCommerceFolder(commerce) + commerce.getPictureFileName()));
        }catch(Exception e){
            logger.error("Error intentando obtener imagen ", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno buscando imagen de comercio"));
        }
    }
}
