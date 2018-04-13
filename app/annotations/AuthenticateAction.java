package annotations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.typesafe.config.ConfigFactory;
import models.User;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;

public class AuthenticateAction extends Action<Authenticate> {

    @Override
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {

        try{
            String token;
            if(ctx.request().headers().containsKey("authorization")){
                token = ctx.request().headers().get("authorization")[0];
            }else{
                return F.Promise.pure(unauthorized());
            }
            token = token.split(" ")[1];

            Long userId = JWT.require(Algorithm.HMAC256(ConfigFactory.load().getString("application.secret")))
                                .build()
                                .verify(token)
                                .getClaim("userId").asLong();

            User user = User.findByIdAndType(userId, this.configuration.types());

            if(user == null){
                return F.Promise.pure(unauthorized());
            }

            ctx.args.put("user", user);

            return delegate.call(ctx);
        }catch(Exception e){
            return F.Promise.pure(unauthorized());
        }

    }
}
