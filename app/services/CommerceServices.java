package services;

import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import exceptions.CreationException;
import exceptions.UpdateException;
import models.Address;
import models.Commerce;
import models.Location;
import models.Phone;
import play.Logger;

import java.util.ArrayList;
import java.util.Arrays;
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
        commerce.save();
    }

    public static void update(Long id, Commerce commerce) throws UpdateException {
        Commerce dbCommerce = Commerce.findByProperty("id", id);
        if(dbCommerce == null){
            throw new UpdateException("Comercio con id inexistente");
        }
        if(!dbCommerce.getBusinessName().equals(commerce.getBusinessName()) && Commerce.findByProperty("businessName", commerce.getBusinessName()) != null){
            throw new UpdateException("Razon social de comercio ya utilizada");
        }
        List<Phone> newAndOldPhones = new ArrayList();
        if(!dbCommerce.getPhones().isEmpty()) {
            dbCommerce.getPhones().get(0).delete();
        }
        for(Phone phone: commerce.getPhones()){
            if(phone.getId() != null){
                newAndOldPhones.add(Phone.findByProperty("id", phone.getId()));
            }else{
                newAndOldPhones.add(phone);
            }
        }
        if(commerce.getLocation().getId() != null){
            commerce.setLocation(Location.findByProperty("id", commerce.getLocation().getId()));
        }
        if(commerce.getAddress().getId() != null){
            commerce.setAddress(Address.findByProperties(Arrays.asList("id"), Arrays.asList(commerce.getAddress().getId())));
        }
        commerce.setPhones(newAndOldPhones);
        commerce.setId(id);
        commerce.update();
    }

    public static List<Commerce> findFilteredCommerces(Map<String, String[]> queryStrings){
        Map<String,String[]> validatedQuery = Commerce.validateQuery(queryStrings);
        return Commerce.findByMap(validatedQuery);
    }
}
