package global;

import models.BackofficeUser;
import models.CommerceCategory;
import org.mindrot.jbcrypt.BCrypt;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import services.FolderServices;

public class Global extends GlobalSettings {

    private static Logger.ALogger logger = Logger.of("global");

    @Override
    public void onStart(Application app){
        try {
            if (BackofficeUser.findByUsername("root") == null) {
                BackofficeUser rootUser = new BackofficeUser();
                rootUser.setUsername("root");
                rootUser.setPassword(BCrypt.hashpw("root", BCrypt.gensalt()));
                rootUser.save();
            }
            FolderServices.createMediaFolder();
            CommerceCategory.initializeCategories();
        }catch(Exception e){
            logger.error("Error inicializando el usuario root", e);
        }
    }
}
