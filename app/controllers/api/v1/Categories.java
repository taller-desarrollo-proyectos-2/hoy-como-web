package controllers.api.v1;

import annotations.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.CreationException;
import exceptions.UpdateException;
import models.Category;
import models.CommerceUser;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.CategoriesServices;
import services.SerializerService;

public class Categories extends Controller {

    private static Logger.ALogger logger = Logger.of("categories-api");

    @Authenticate(types = "COMMERCE")
    public static Result create(){
        Ebean.beginTransaction();
        try {
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            Form<Category> form = Form.form(Category.class, Category.Creation.class).bindFromRequest();

            if (form.hasErrors()) {
                logger.error("Error en el json de creacion de categorias", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de creacion de la categoria"));
            }

            Category category = form.get();
            //Se crea la categoria para el comercio especificado
            CategoriesServices.create(category, commerceUser.getCommerce());
            Ebean.commitTransaction();
            return ok(Json.toJson(category));
        }catch(CreationException e){
            logger.error(e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message", e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno intentando crear categoria", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando crear categoria"));
        }finally{
            Ebean.endTransaction();
        }

    }

    @Authenticate(types = "COMMERCE")
    public static Result list(){
        try{
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            return ok(SerializerService.serializeList(Category.findListByProperty("commerce.id", commerceUser.getCommerce().getId())));
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
            Form<Category> form = Form.form(Category.class).bindFromRequest();
            if (form.hasErrors()) {
                logger.error("Error en el json de modificacion de categorias", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de modificacion de la categoria"));
            }
            Category category = form.get();
            //Se crea la categoria para el comercio especificado
            CategoriesServices.update(id, category, commerceUser.getCommerce());
            Ebean.commitTransaction();
            return ok(Json.toJson(category));
        }catch(UpdateException e){
            logger.error("Error actualizando la categoria", e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message",e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno intentando actualizar", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando actualizar la categoria"));
        }finally{
            Ebean.endTransaction();
        }
    }

    @Authenticate(types = "COMMERCE")
    public static Result delete(Long id){
        Ebean.beginTransaction();
        try{
            Ebean.commitTransaction();
            return ok();
        }catch(Exception e){
            logger.error("Error interno borrando la categoria", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando borrar la categoria"));
        }finally {
            Ebean.endTransaction();
        }
    }
}
