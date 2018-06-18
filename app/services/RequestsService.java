package services;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.Unit;
import exceptions.CreationException;
import models.*;
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
        request.addTotal();
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
            Commerce destination = Commerce.findByProperty("id", dbRequest.getSingleRequests().get(0).getPlate().getCommerce().getId());
            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(GoogleServices.getContext("matrix"))
                    .origins(destination.getLocation().getLat() + "," + destination.getLocation().getLng())
                    .destinations(dbRequest.getDestination().getStreet() + " " + dbRequest.getDestination().getNumber() + " Argentina")
                    .units(Unit.METRIC);
            // Async
            long timeToDestination;
            try {
                DistanceMatrix results = req.await();
                long timeInSec = results.rows[0].elements[0].duration.inSeconds;
                timeToDestination = timeInSec/60;
                Logger.of("app-notifications").error("Tiempo hasta pedido listo + Tiempo a destino: " + minutes + "+" + timeToDestination);
                // Handle successful request.
            } catch (Exception e) {
                // Handle error
                Logger.of("app-notifications").error("Error comunicandose con los servidores de google", e);
                timeToDestination = 15;
            }
            request.setLeadTime(minutes + timeToDestination);
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
                Logger.of("app-notifications").error("Error enviando notificacion de cambio de estado", e);
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
