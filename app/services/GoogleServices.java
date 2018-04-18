package services;

import com.google.maps.GeoApiContext;
import com.typesafe.config.ConfigFactory;

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
}
