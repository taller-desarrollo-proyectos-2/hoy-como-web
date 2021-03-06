package services;

import exceptions.CreationException;
import exceptions.DeleteException;
import exceptions.UpdateException;
import models.Commerce;
import models.CommerceUser;
import models.Plate;

import java.io.File;
import java.util.*;

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
        if(plate.getPrice() == 0){
            plate.setPrice(dbPlate.getPrice());
        }
        plate.setId(id);
        plate.setCommerce(commerce);
        plate.update();
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
        dbPlate.deleteManyToManyAssociations("optionals");
        dbPlate.setActive(false);
        dbPlate.setDeletedAt(new Date());
        dbPlate.update();
    }

    public static List<Plate> findFilteredPlates(Map<String, String[]> map, CommerceUser user){
        Map<String,String[]> validatedQuery = Plate.validateQuery(map);
        if(user != null){
            validatedQuery.put("commerce.id", new String[]{user.getCommerce().getId().toString()});
        }
        return Plate.findByMap(validatedQuery);
    }
}
