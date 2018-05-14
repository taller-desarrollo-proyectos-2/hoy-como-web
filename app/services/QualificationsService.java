package services;

import models.MobileUser;
import models.Qualification;

public class QualificationsService {

    public static void create(Qualification qualification, MobileUser user){
        Qualification dbQualif = Qualification.findByProperty("request.id", qualification.getRequest().getId());
        if(dbQualif != null){
            // TODO - Restarle el puntaje de la calificacion de db.
            dbQualif.delete();
        }
        qualification.setUser(user);
        qualification.save();
        // TODO - Agregar al comercio el total de puntaje y sumarle uno a la cantidad de calificaciones.
    }
}
