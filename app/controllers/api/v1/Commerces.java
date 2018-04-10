package controllers.api.v1;

import annotations.Authenticate;
import models.BackofficeUser;
import models.Commerce;
import models.User;
import play.Logger;
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

    @Authenticate(types = {"BACKOFFICE", "MOBILE"})
    public static Result create(){
        try{

            BackofficeUser user = (BackofficeUser) Http.Context.current().args.get("user");

            return ok();
        }catch(Exception e){
            return internalServerError();
        }

    }
}
