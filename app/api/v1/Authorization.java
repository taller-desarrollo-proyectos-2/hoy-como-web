package api.v1;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Authorization extends Controller {

    public static Result authenticate(){
        try {
            Form<User> form = Form.form(User.class, User.Creation.class).bindFromRequest();

            if (form.hasErrors()) {
                Logger.of("authorization-api").error("Error intentando autenticarse", form.errorsAsJson());
                return badRequest(form.errorsAsJson());
            }
            //Obtengo el user y pass
            User user = form.get();

            //Hasheo la contrasena y verifico que exista en la db.
            User dbUser = User.findByUsername(user.getUsername());
            return ok();
        }catch(Exception e){
            Logger.of("authorization-api").error("Error intentando autenticarse", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Internal error attempting to log in"));
        }
    }
}
