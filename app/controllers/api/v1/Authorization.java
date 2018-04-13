package controllers.api.v1;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.typesafe.config.ConfigFactory;
import models.BackofficeUser;
import models.FacebookUser;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Calendar;

public class Authorization extends Controller {

    private static Logger.ALogger logger = Logger.of("authorization-api");

    public static Result authenticate(){
        try {
            Form<BackofficeUser> form = Form.form(BackofficeUser.class, BackofficeUser.Creation.class).bindFromRequest();

            if (form.hasErrors()) {
                logger.error("Error intentando autenticarse", form.errorsAsJson());
                return badRequest(form.errorsAsJson());
            }
            //Obtengo el user y pass
            BackofficeUser user = form.get();

            //Busco el usuario y verifico que esten bien los datos
            BackofficeUser dbUser = BackofficeUser.findByUsername(user.getUsername());
            if (dbUser == null) {
                logger.info("Usuario no encontrado");
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Usuario no encontrado"));
            }
            if(!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())){
                logger.info("Intentando loguearse con una contrasena incorrecta");
                return unauthorized(JsonNodeFactory.instance.objectNode().put("message", "Usuario o contraseña incorrectos"));
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 60);
            String token = JWT.create()
                    .withClaim("userId", dbUser.getId())
                    .withExpiresAt(cal.getTime())
                    .sign(Algorithm.HMAC256(ConfigFactory.load().getString("application.secret")));

            response().setHeader("authorization", "Bearer " + token);

            return ok(dbUser.getPanel());
        }catch(Exception e){
            logger.error("Error intentando autenticarse", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando loguearse"));
        }
    }

    public static Result facebookAuthenticate(){
        try{
            Form<FacebookUser> form = Form.form(FacebookUser.class, FacebookUser.Login.class).bindFromRequest();

            if(form.hasErrors()){
                logger.error("Error, token no enviado en la peticion", form.errorsAsJson());
                return badRequest(form.errorsAsJson());
            }

            FacebookUser user = form.get();
            //TO DO - VALIDATE FACEBOOK USER
            user = FacebookUser.createIfNecessary(user);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 60);
            String token = JWT.create()
                    .withClaim("userId", user.getId())
                    .withExpiresAt(cal.getTime())
                    .sign(Algorithm.HMAC256(ConfigFactory.load().getString("application.secret")));

            response().setHeader("authorization", "Bearer " + token);
            return ok();
        }catch(Exception e ){
            logger.error("Error interno intentando autenticarse con facebook", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando loguearse"));
        }

    }
}
