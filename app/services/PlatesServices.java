package services;

import exceptions.CreationException;
import exceptions.DeleteException;
import exceptions.UpdateException;
import models.Commerce;
import models.Plate;

import java.io.File;
import java.util.ArrayList;
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
        plate.setCommerce(commerce);
        plate.save();
    }

    public static void update(Long id, Plate plate, Commerce commerce) throws UpdateException {
        if(Commerce.findByProperty("id", commerce.getId()) == null){
            throw new UpdateException("Comercio con id inexistente");
        }
        Plate dbPlate = Plate.findByProperties(Arrays.asList("id", "commerce.id"), Arrays.asList(id, commerce.getId()));
        if(dbPlate == null){
            throw new UpdateException("Plato con id inexistente o de comercio incorrecto");
        }
        if(!dbPlate.getName().equals(plate.getName())) {
            if (Plate.findByProperties(Arrays.asList("name", "commerce.id"), Arrays.asList(plate.getName(), commerce.getId())) != null) {
                throw new UpdateException("Nombre de plato ya utilizado");
            }
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

    public static void delete(Long id, Commerce commerce) throws DeleteException{
        Plate dbPlate = Plate.findByProperties(Arrays.asList("id", "commerce.id"), Arrays.asList(id, commerce.getId()));
        if(dbPlate == null){
            throw new DeleteException("Plato de id inexistente o perteneciente a otro comercio");
        }
        //Elimino el archivo imagen
        new File(FolderServices.getCommerceFolder(commerce) + dbPlate.getPictureFileName()).delete();
        for(Plate plate : commerce.getPlates()){
            if(plate.getName().equals(dbPlate.getName())){
                commerce.getPlates().remove(plate);
                break;
            }
        }
        //dbPlate.setCommerce(Commerce.findByProperty("id", dbPlate.getCommerce().getId()));
        dbPlate.deleteManyToManyAssociations("optionals");
        dbPlate.delete();
    }
}
