package services;

import exceptions.CreationException;
import models.BackofficeUser;
import models.Commerce;
import models.CommerceUser;
import org.mindrot.jbcrypt.BCrypt;

public class UsersService {

    public static void create(CommerceUser user) throws CreationException {
        //Me fijo que no exista alguno con ese nombre de usuario.
        if(BackofficeUser.findByUsername(user.getUsername()) != null){
            throw new CreationException("Nombre de usuario ya utilizado.");
        }
        if(CommerceUser.findByProperty("commerce.id", user.getCommerce().getId()) != null){
            throw new CreationException("Ya existe un usuario para ese comercio");
        }
        if(Commerce.findByProperty("id", user.getCommerce().getId()) == null){
            throw new CreationException("Comercio inexistente para asociar al usuario");
        }
        //Seteo la password hasheada
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.save();
    }
}
