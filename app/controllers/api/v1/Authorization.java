package controllers.api.v1;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.typesafe.config.ConfigFactory;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Calendar;
import java.util.Date;

public class Authorization extends Controller {

    private static Logger.ALogger logger = Logger.of("authorization-api");

    public static Result authenticate(){
        try {
            Form<User> form = Form.form(User.class, User.Creation.class).bindFromRequest();

            if (form.hasErrors()) {
                logger.error("Error intentando autenticarse", form.errorsAsJson());
                return badRequest(form.errorsAsJson());
            }
            //Obtengo el user y pass
            User user = form.get();

            //Busco el usuario y verifico que esten bien los datos
            User dbUser = User.findByUsername(user.getUsername());
            if(!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())){
                logger.error("Intentando loguearse con una contrasena incorrecta");
                return unauthorized(JsonNodeFactory.instance.objectNode().put("message", "Not a valid username or password"));
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 60);
            String token = JWT.create()
                    .withClaim("userId", dbUser.getId())
                    .withExpiresAt(cal.getTime())
                    .sign(Algorithm.HMAC256(ConfigFactory.load().getString("application.secret")));

            response().setHeader("authorization", "Bearer " + token);
            return ok();
        }catch(Exception e){
            logger.error("Error intentando autenticarse", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Internal error attempting to log in"));
        }
    }
}