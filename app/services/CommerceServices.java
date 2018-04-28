package services;

import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import exceptions.CreationException;
import models.Commerce;
import models.Location;
import play.Logger;

import java.util.List;
import java.util.Map;


public class CommerceServices {

    private static Logger.ALogger logger = Logger.of("commerce-services");

    public static void create(Commerce commerce) throws CreationException {
        if(Commerce.findByProperty("name", commerce.getName()) != null){
            throw new CreationException("Comercio con nombre ya creado");
        }
        GeocodingApiRequest req =  GeocodingApi.newRequest(GoogleServices.getContext())
                .address(commerce.getAddress().getNumber() + " " +  commerce.getAddress().getStreet() + "Argentina");

        // Async
        try {
            GeocodingResult[] results = req.await();
            commerce.setLocation(new Location(results[0].geometry.location.lat, results[0].geometry.location.lng));
            // Handle successful request.
        } catch (Exception e) {
            // Handle error
            logger.error("Error comunicandose con los servidores de google", e);
            throw new CreationException("Error validando direccion del comercio");
        }
        FolderServices.createCommerceFolder(commerce);
        commerce.save();
    }

    public static List<Commerce> findFilteredCommerces(Map<String, String[]> queryStrings){
        Map<String,String[]> validatedQuery = Commerce.validateQuery(queryStrings);
        return Commerce.findByMap(validatedQuery);
    }
}
