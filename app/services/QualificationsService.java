package services;

import models.Commerce;
import models.MobileUser;
import models.Qualification;
import models.Request;

public class QualificationsService {

    public static void create(Qualification qualification, MobileUser user){
        qualification.setUser(user);
        qualification.save();
        //Busco el comerico al que corresponde la calificacion.
        Commerce commerce = Commerce.findByProperty("plates.id", Request.findByProperty("id", qualification.getRequest().getId()).getSingleRequests().get(0).getPlate().getId());
        commerce.setScoreCount(commerce.getScoreCount() + qualification.getScore());
        commerce.update();
    }
}
