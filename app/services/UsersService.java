package services;

import exceptions.CreationException;
import exceptions.DeleteException;
import exceptions.UpdateException;
import models.BackofficeUser;
import models.Commerce;
import models.CommerceUser;
import models.MobileUser;
import org.mindrot.jbcrypt.BCrypt;

public class UsersService {

    public static void create(CommerceUser user) throws CreationException {
        //Me fijo que no exista alguno con ese nombre de usuario.
        if(BackofficeUser.findByUsername(user.getUsername()) != null){
            throw new CreationException("Nombre de usuario ya utilizado.");
        }
        //if(CommerceUser.findByProperty("commerce.id", user.getCommerce().getId()) != null){
        //    throw new CreationException("Ya existe un usuario para ese comercio");
        //}
        if(Commerce.findByProperty("id", user.getCommerce().getId()) == null){
            throw new CreationException("Comercio inexistente para asociar al usuario");
        }
        if(user.getPassword().length() < 10){
            throw new CreationException("Contraseña muy corta, minimo 10 caracteres");
        }
        //Seteo la password hasheada
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.save();
    }

    public static void update(Long id, CommerceUser user) throws UpdateException{
        CommerceUser dbUser = CommerceUser.findByProperty("id", id);
        if(dbUser == null){
            throw new UpdateException("Usuario con id inexistente");
        }
        if(!dbUser.getUsername().equals(user.getUsername()) && CommerceUser.findByProperty("username", user.getUsername()) != null){
            throw new UpdateException("Nombre de usuario ya utilizado");
        }
        user.setId(id);
        user.update();
    }

    public static void update(Long id, MobileUser user){
        user.setId(id);
        user.update();
    }

    public static void addToFavourite(Long commerceId, MobileUser user) throws UpdateException{
        Commerce dbCommerce = Commerce.findByProperty("id", commerceId);
        if(dbCommerce == null){
            throw new UpdateException("Comercio inexistente para asociar al usuario");
        }
        user.getFavourites().add(dbCommerce);
        user.update();
    }

    public static void removeFromFavourite(Long commerceId, MobileUser user){
        for(Commerce commerce : user.getFavourites()){
            if(commerce.getId().equals(commerceId)){
                user.getFavourites().remove(commerce);
                break;
            }
        }
        user.update();
    }

    public static void delete(Long id) throws DeleteException {
        CommerceUser dbUser = CommerceUser.findByProperty("id", id);
        if(dbUser == null){
            throw new DeleteException("Usuario con id inexistente");
        }
        dbUser.delete();
    }
}
