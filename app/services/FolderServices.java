package services;

import exceptions.CreationException;
import models.Commerce;
import play.Logger;

import java.io.File;

public class FolderServices {

    private static Logger.ALogger logger = Logger.of("folder-services");

    public static String getCommerceFolder(Commerce commerce){
        return "media/" + commerce.getName() + "/";
    }

    public static void createCommerceFolder(Commerce commerce) throws CreationException{
        File file = new File("media/" + commerce.getName());
        if(!file.mkdir()){
            throw new CreationException("Error creando carpeta de imagenes de comercio");
        }
    }

    public static void createMediaFolder(){
        File file = new File("media");
        if(!file.exists()){
            if(!file.mkdir()) {
                logger.error("Error intentando crear carpeta medias");
            }
        }
    }
}
