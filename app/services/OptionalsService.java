package services;

import exceptions.CreationException;
import exceptions.DeleteException;
import exceptions.UpdateException;
import models.Commerce;
import models.Optional;

import java.util.Arrays;

public class OptionalsService {

    public static void create(Optional optional, Commerce commerce) throws CreationException {
        if(Commerce.findByProperty("id", commerce.getId()) == null){
            throw new CreationException("Comercio con id inexistente");
        }
        if(Optional.findByProperties(Arrays.asList("name", "commerce.id"), Arrays.asList(optional.getName(), commerce.getId())) != null){
            throw new CreationException("Nombre de opcional ya utilizado");
        }
        optional.setCommerce(commerce);
        optional.save();
    }

    public static void update(Long id, Optional optional, Commerce commerce) throws UpdateException{
        if(Commerce.findByProperty("id", commerce.getId()) == null){
            throw new UpdateException("Comercio con id inexistente");
        }
        if(Optional.findByProperties(Arrays.asList("name", "commerce.id"), Arrays.asList(optional.getName(), commerce.getId())) != null){
            throw new UpdateException("Nombre de categoria ya utilizado");
        }
        Optional dbOptional = Optional.findByProperty("id", id);
        if(dbOptional == null){
            throw new UpdateException("Categoria con id inexistente");
        }
        optional.setId(id);
        optional.setCommerce(commerce);
        optional.update();
    }

    public static void delete(Long id, Commerce commerce) throws DeleteException{
        if(Optional.findByProperties(Arrays.asList("id", "commerce.id"), Arrays.asList(id, commerce.getId())) == null){
            throw new DeleteException("Opcional de id inexistente o perteneciente a otro comercio");
        }
        Optional.findByProperty("id", id).delete();
    }
}
