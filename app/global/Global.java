package global;

import models.BackofficeUser;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app){
        if(User.findByUsername("root") == null) {
            BackofficeUser rootUser = new BackofficeUser();
            rootUser.setUsername("root");
            rootUser.setPassword(BCrypt.hashpw("root", BCrypt.gensalt()));
            rootUser.save();
        }
    }
}
