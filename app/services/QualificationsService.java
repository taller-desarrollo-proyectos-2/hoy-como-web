package services;

import exceptions.UpdateException;
import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QualificationsService {

    public static void create(Qualification qualification, MobileUser user){
        qualification.setUser(user);
        qualification.save();
        //Busco el comerico al que corresponde la calificacion.
        Commerce commerce = Commerce.findByProperty("plates.id", Request.findByProperty("id", qualification.getRequest().getId()).getSingleRequests().get(0).getPlate().getId());
        commerce.setScoreCount(commerce.getScoreCount() + qualification.getScore());
        commerce.update();
    }

    public static List<Qualification> list(MobileUser user, Map<String, String[]> queryString){
        if(queryString.isEmpty()) {
            return Qualification.findListByProperty("user.id", user.getId());
        }else{
            if(queryString.containsKey("commerceId")) {
                return Qualification.findListByProperty("request.singleRequests.plate.commerce.id", queryString.get("commerceId")[0]);
            }else{
                return new ArrayList();
            }
        }
    }

    public static List<Qualification> list(CommerceUser user){
        return Qualification.findListByProperty("request.singleRequests.plate.commerce.id", user.getCommerce().getId());
    }

    public static void update(Long id, Qualification qualification) throws UpdateException{
        Qualification dbQualification = Qualification.findByProperty("id", id);
        if(dbQualification == null){
            throw new UpdateException("Calificacion de id inexistente");
        }
        qualification.setId(id);
        qualification.setScore(dbQualification.getScore());
        qualification.update();
    }
}
