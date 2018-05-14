package controllers.api.v1;

import annotations.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.UpdateException;
import models.*;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.QualificationsService;
import services.SerializerService;

import java.util.ArrayList;
import java.util.List;

public class Qualifications extends Controller {

    private static Logger.ALogger logger = Logger.of("qualifications-api");

    @Authenticate(types = "FACEBOOK")
    public static Result create(){
        Ebean.beginTransaction();
        try{
            MobileUser user = MobileUser.findByProperty("id", Http.Context.current().args.get("userId"));
            Form<Qualification> form = Form.form(Qualification.class, Qualification.Creation.class).bindFromRequest();

            if (form.hasErrors()) {
                logger.error("Error en el json de creacion de calificacion", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de creacion de la calificacion"));
            }

            Qualification qualification = form.get();
            //Se crea la calificacion para el usuario
            QualificationsService.create(qualification, user);
            Ebean.commitTransaction();
            return ok(Json.toJson(qualification));
        }catch(Exception e){
            logger.error("Error creando calificacion", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error intentando crear la calificacion"));
        }finally{
            Ebean.endTransaction();
        }
    }

    @Authenticate(types = {"FACEBOOK", "COMMERCE"})
    public static Result list(){
        try{
            List<Qualification> qualifications = new ArrayList();
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            if(commerceUser == null){
                MobileUser mobileUser = MobileUser.findByProperty("id", Http.Context.current().args.get("userId"));
                qualifications = QualificationsService.list(mobileUser);
            }else{
                qualifications = QualificationsService.list(commerceUser);
            }
            return ok(SerializerService.serializeList(qualifications));
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
            //Solo me quedo con la response que puso el administrador de comercio.
            Form<Qualification> form = Form.form(Qualification.class).bindFromRequest("response");
            if (form.hasErrors()) {
                logger.error("Error en el json de modificacion de calificacion", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de modificacion de la calificacion"));
            }
            Qualification qualification = form.get();

            QualificationsService.update(id, qualification);
            Ebean.commitTransaction();
            return ok(Json.toJson(qualification));
        }catch(UpdateException e){
            logger.error("Error actualizando la calificacion", e.getMessage());
            return badRequest(JsonNodeFactory.instance.objectNode().put("message",e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno intentando actualizar", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando actualizar la calificacion"));
        }finally{
            Ebean.endTransaction();
        }
    }
}
