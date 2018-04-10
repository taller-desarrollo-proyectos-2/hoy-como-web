package controllers.api.v1;

import annotations.Authenticate;
import models.BackofficeUser;
import models.Commerce;
import models.User;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

public class Commerces extends Controller {

    private static Logger.ALogger logger = Logger.of("companies-api");

    public static Result list(){
        try {
            User user = (User) Http.Context.current().args.get("user");
            //List<Commerce> companies = Commerce.findByUser(user);

            return ok();
        }catch(Exception e){
            return internalServerError();
        }
    }

    @Authenticate(types = "BACKOFFICE")
    public static Result create(){
        try{

            BackofficeUser user = (BackofficeUser) Http.Context.current().args.get("user");

            Form<Commerce> form = Form.form(Commerce.class).bindFromRequest();

            if(form.hasErrors()){
                logger.info("Parametros incorrectos para la creacion del comercio", form.errorsAsJson());
                return badRequest(form.errorsAsJson());
            }

            Commerce commerce = form.get();

            commerce.save();
            return ok(Json.toJson(commerce));
        }catch(Exception e){
            return internalServerError();
        }

    }
}
