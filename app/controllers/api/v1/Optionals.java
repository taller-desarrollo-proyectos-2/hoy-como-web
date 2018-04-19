package controllers.api.v1;

import annotations.Authenticate;
import play.mvc.Controller;
import play.mvc.Result;

public class Optionals extends Controller {

    @Authenticate(types = "COMMERCE")
    public static Result create(){
        return ok();
    }
}
