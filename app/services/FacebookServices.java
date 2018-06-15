package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.ConfigFactory;
import models.FacebookUser;
import play.Logger;
import play.libs.F;
import play.libs.WS;

public class FacebookServices {

    private static final Logger.ALogger logger = Logger.of("facebookservices");

    public static JsonNode validateUser(FacebookUser user){
        String fbToken = ConfigFactory.load().getString("facebook.api.token");
        F.Promise<WS.Response> fbUser = WS.url("https://graph.facebook.com/v3.0/" + user.getToken()).setQueryParameter("access_token", fbToken).get();

        return fbUser.flatMap(response -> F.Promise.pure(response.asJson())).get(10000);
    }
}
