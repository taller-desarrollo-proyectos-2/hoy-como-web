package services;

import exceptions.CreationException;
import models.BackofficeUser;
import org.mindrot.jbcrypt.BCrypt;

public class UsersService {

    public static void create(BackofficeUser user) throws CreationException {
        //Me fijo que no exista alguno con ese nombre de usuario.
        if(BackofficeUser.findByUsername(user.getUsername()) != null){
            throw new CreationException("Username already taken.");
        }
        //Seteo la password hasheada
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.save();
    }
}
