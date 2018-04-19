package services;

import exceptions.CreationException;
import exceptions.UpdateException;
import models.Commerce;
import models.Plate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlatesServices {


    public static void create(Plate plate, Commerce commerce) throws CreationException{
        if(Commerce.findByProperty("id", commerce.getId()) == null){
            throw new CreationException("Comercio con id inexistente");
        }
        if(Plate.findByProperties(Arrays.asList("name", "commerce.id"), Arrays.asList(plate.getName(), commerce.getId())) != null){
            throw new CreationException("Nombre de plato ya utilizado");
        }
        plate.save();
    }

    public static void update(Long id, Plate plate, Commerce commerce) throws UpdateException {
        if(Commerce.findByProperty("id", commerce.getId()) == null){
            throw new UpdateException("Comercio con id inexistente");
        }
        if(Plate.findByProperties(Arrays.asList("name", "commerce.id"), Arrays.asList(plate.getName(), commerce.getId())) != null){
            throw new UpdateException("Nombre de plato ya utilizado");
        }
        Plate dbPlate = Plate.findByProperty("id", id);
        if(dbPlate == null){
            throw new UpdateException("Plato con id inexistente");
        }
        plate.setId(id);
        plate.setCommerce(commerce);
        plate.update();
    }

    public static Map<String, Object> validateQuery(Map<String, String[]> queryParams){
        for(Map.Entry entry: queryParams.entrySet()){

        }
        return new HashMap();
    }
}
