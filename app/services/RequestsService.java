package services;

import exceptions.CreationException;
import models.Address;
import models.MobileUser;
import models.Request;
import models.User;
import play.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RequestsService {

    public static void create(Request request) throws CreationException {
        //Chequeos previos a la creacion
        if(request.getDestination().getId() == null){
            AddressServices.create(request.getDestination(), request.getUser());
        }
        request.setStatus(Request.Status.WAITING_CONFIRMATION);
        request.setInitAt(new Date());
        request.save();
    }

    public static void update(Long id, Request request){
        Request dbRequest = Request.findByProperty("id", id);
        Request.Status previousStatus = dbRequest.getStatus();
        request.setId(id);
        if(!previousStatus.equals(Request.Status.ON_THE_WAY) && request.getStatus().equals(Request.Status.ON_THE_WAY)){
            Date now = new Date();
            long diff = now.getTime() - dbRequest.getInitAt().getTime();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            request.setLeadTime(minutes); //TODO - ADD TIME TO DESTINATION ESTIMATED BY GOOGLE API.
        }
        if(!previousStatus.equals(Request.Status.DELIVERED) &&
                request.getStatus() != null && request.getStatus().equals(Request.Status.DELIVERED)){
            request.setFinishedAt(new Date());
        }
        request.update();
        MobileUser destinationUser = MobileUser.findByProperty("id", dbRequest.getUser().getId());
        if(destinationUser.getAppToken() != null){
            try {
                GoogleServices.sendNotification(request, destinationUser);
            }catch(IOException e){
                Logger.of("app-notifications").error("Error enviando notificacion de cambio de estado");
            }
        }
    }

    public static List<Request> findFilteredRequests(Map<String, String[]> map, User user){
        Map<String,String[]> validatedQuery = Request.validateQuery(map);
        if(user != null){
            user.fillRequestMap(validatedQuery);
        }
        return Request.findByMap(validatedQuery);
    }
}
