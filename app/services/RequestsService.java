package services;

import exceptions.CreationException;
import models.Address;
import models.Request;
import models.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
        request.setId(id);
        if(request.getStatus().equals(Request.Status.DELIVERED)) request.setFinishedAt(new Date());
        request.update();
    }

    public static List<Request> findFilteredRequests(Map<String, String[]> map, User user){
        Map<String,String[]> validatedQuery = Request.validateQuery(map);
        if(user != null){
            user.fillRequestMap(validatedQuery);
        }
        return Request.findByMap(validatedQuery);
    }
}
