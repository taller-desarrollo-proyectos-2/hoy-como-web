package services;

import exceptions.CreationException;
import models.User;
import org.mindrot.jbcrypt.BCrypt;

public class UsersService {

    public static void create(User user) throws CreationException {
        //Me fijo que no exista alguno con ese nombre de usuario.
        if(User.findByUsername(user.getUsername()) != null){
            throw new CreationException("Username already taken.");
        }
        //Seteo la password hasheada
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.save();
    }
}
