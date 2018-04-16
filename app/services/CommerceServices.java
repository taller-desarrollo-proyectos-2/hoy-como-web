package services;

import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import exceptions.CreationException;
import models.Commerce;

import java.io.IOException;

public class CommerceServices {

    public static void create(Commerce commerce) throws CreationException {
        if(Commerce.findByProperty("name", commerce.getName()) != null){
            throw new CreationException("Comercio con nombre ya creado");
        }
        GeocodingApiRequest req =  GeocodingApi.newRequest(GoogleServices.getContext())
                .address(commerce.getAddress().getNumber() + " " +  commerce.getAddress().getNumber());

        // Async
        try {
            GeocodingResult[] results = req.await();
            if(results.length > 0){

            }
            // Handle successful request.
        } catch (Exception e) {
            // Handle error
        }
        commerce.save();
    }
}
