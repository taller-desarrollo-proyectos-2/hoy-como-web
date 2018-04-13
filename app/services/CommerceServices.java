package services;

import exceptions.CreationException;
import models.Commerce;

public class CommerceServices {

    public static void create(Commerce commerce) throws CreationException {
        if(Commerce.findByProperty("name", commerce.getName()) != null){
            throw new CreationException("Comercio con nombre ya creado");
        }
        commerce.save();
    }
}
