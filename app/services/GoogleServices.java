package services;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.maps.GeoApiContext;
import com.typesafe.config.ConfigFactory;
import models.MobileUser;
import models.Request;
import models.Request.Status;
import play.libs.WS;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class GoogleServices {

    private static GeoApiContext context = null;

    protected GoogleServices(GeoApiContext context){
        this.context = context;
    }

    public static GeoApiContext getContext(){
        if(context == null){
            context = new GeoApiContext.Builder()
                    .apiKey(ConfigFactory.load().getString("google.api.key"))
                    .build();
        }
        return context;
    }

    private static String getAccessToken() throws IOException {
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new FileInputStream("hoycomo-fcm.json"))
                .createScoped(Arrays.asList());
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
                break;
            case CANCELLED_BY_USER:
                break;
            case CANCELLED_BY_COMMERCE:
                message = "Su pedido ha sido cancelado por el comercio";
                break;
        }
        if(!message.isEmpty()) {
            ObjectNode body = JsonNodeFactory.instance.objectNode();
            body.put("to", destinationUser.getAppToken());
            body.set("data", JsonNodeFactory.instance.objectNode().put("message", message));
            WS.url("https://fcm.googleapis.com/v1/projects/hoycomo-201312/messages:send")
                    .setHeader("Authorization", "Bearer " + getAccessToken())
                    .setContentType("application/json")
                    .post(body);
        }
    }
}
