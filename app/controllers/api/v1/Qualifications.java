package controllers.api.v1;

import annotations.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import models.MobileUser;
import models.Qualification;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.QualificationsService;

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
}
