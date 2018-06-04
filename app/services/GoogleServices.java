package services;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.maps.GeoApiContext;
import com.typesafe.config.ConfigFactory;
import models.MobileUser;
import models.Request;
import models.Request.Status;
import play.Logger;
import play.libs.F;
import play.libs.WS;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class GoogleServices {

    private static GeoApiContext contextCoord = null;
    private static GeoApiContext contextMatrix = null;

    public static GeoApiContext getContext(String type){
        switch(type) {
            case "matrix":
                if (contextMatrix == null) {
                    contextMatrix = new GeoApiContext.Builder()
                            .apiKey(ConfigFactory.load().getString("google.distance.key"))
                            .build();
                }
                return contextMatrix;
            case "coordinates":
                if (contextCoord == null) {
                    contextCoord = new GeoApiContext.Builder()
                            .apiKey(ConfigFactory.load().getString("google.api.key"))
                            .build();
                }
                return contextCoord;
        }
        return contextCoord;
    }

    private static String getAccessToken() throws IOException {
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new FileInputStream("hoycomo-fcm.json"))
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }

    public static void sendNotification(Request request, MobileUser destinationUser) throws IOException{
        String message = "";
        switch(request.getStatus()){
            case WAITING_CONFIRMATION:
                break;
            case ON_PREPARATION:
                message = "Su pedido ya está preparándose!";
                break;
            case ON_THE_WAY:
                message = "Su pedido ya está en camino!";
                break;
            case DELIVERED:
                message = "Su pedido ya fué entregado! ¿Está todo en orden?";
                break;
            case CANCELLED_BY_USER:
                break;
            case CANCELLED_BY_COMMERCE:
                message = "Su pedido ha sido cancelado por el comercio, razón: "+ request.getRejectedReason();
                break;
        }
        if(!message.isEmpty()) {
            ObjectNode body = JsonNodeFactory.instance.objectNode();
            body.put("validate_only", false)
                                .set("message", JsonNodeFactory.instance.objectNode()
                                                .put("token", destinationUser.getAppToken())
                                                .set("notification", JsonNodeFactory.instance.objectNode().put("body", message)));
            WS.url("https://fcm.googleapis.com/v1/projects/hoycomo-201312/messages:send")
                    .setHeader("Authorization", "Bearer " + getAccessToken())
                    .setContentType("application/json; UTF-8")
                    .post(body).flatMap(new F.Function<WS.Response, F.Promise<ObjectNode>>() {
                @Override
                public F.Promise<ObjectNode> apply(WS.Response response) throws Throwable {
                    Logger.of("googleservices-sendfcm").error("response: " + response.asJson().toString());
                    return F.Promise.pure(JsonNodeFactory.instance.objectNode());
                }
            });
        }
    }
}
