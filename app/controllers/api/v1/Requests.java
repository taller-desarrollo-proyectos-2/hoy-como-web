package controllers.api.v1;

import annotations.Authenticate;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import exceptions.CreationException;
import models.*;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.RequestsService;
import services.SerializerService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Requests extends Controller {

    private static Logger.ALogger logger = Logger.of("requests-api");

    @Authenticate(types = {"BACKOFFICE", "COMMERCE", "FACEBOOK"})
    public static Result list(){
        try{
            User user = User.findByProperty("id", Http.Context.current().args.get("userId"));
            Map<String, String[]> queryStrings = request().queryString();
            List<Request> requests = RequestsService.findFilteredRequests(queryStrings, user);
            return ok(SerializerService.serializeList(requests));
        }catch(Exception e){
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando listar pedidos"));
        }
    }


    @Authenticate(types = "FACEBOOK")
    public static Result create(){
        Ebean.beginTransaction();
        try {
            MobileUser mobileUser = MobileUser.findByProperty("id", Http.Context.current().args.get("userId"));
            Form<Request> form = Form.form(Request.class, Request.Creation.class).bindFromRequest();

            if(mobileUser == null){
                logger.info("Usuario no encontrado para hacer el pedido");
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Usuario no encontrado para hacer el pedido"));
            }

            if (form.hasErrors()) {
                logger.error("Error en el json de creacion de pedidos", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de creacion de los pedidos"));
            }

            Request request = form.get();
            PaymentType payment;
            if (form.data().get("paymentType.PAYMENT_TYPE").equals("CREDIT_CARD")){
                payment = new CreditCard(form.data().get("paymentType.number"),
                                            form.data().get("paymentType.fullName"),
                                        Integer.valueOf(form.data().get("paymentType.code")),
                                        new SimpleDateFormat("yyyy-MM-dd").parse(form.data().get("paymentType.expirationDate")));
            }else{
                payment = new Cash(Float.valueOf(form.data().get("paymentType.payWith")));
            }
            request.setPaymentType(payment);
            request.setUser(mobileUser);
            RequestsService.create(request);
            Ebean.commitTransaction();
            return ok(Json.toJson(request));
        }catch(CreationException e){
            logger.error(e.getMessage());
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", e.getMessage()));
        }catch(Exception e){
            logger.error("Error interno intentando crear pedido", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando crear pedido"));
        }finally{
            Ebean.endTransaction();
        }
    }

    @Authenticate(types = "COMMERCE")
    public static Result update(Long id){
        Ebean.beginTransaction();
        try {
            CommerceUser commerceUser = CommerceUser.findByProperty("id", Http.Context.current().args.get("userId"));
            Form<Request> form = Form.form(Request.class).bindFromRequest();

            if(commerceUser == null){
                logger.info("Usuario no encontrado para actualizar el pedido");
                return notFound(JsonNodeFactory.instance.objectNode().put("message", "Usuario no encontrado para actualizar el pedido"));
            }

            if (form.hasErrors()) {
                logger.error("Error en el json de actualizacion de pedidos", form.errorsAsJson());
                return badRequest(JsonNodeFactory.instance.objectNode().put("message", "Error en los parametros de actualizacion de los pedidos"));
            }
            Request request = form.get();
            //Se actualiza el pedido
            RequestsService.update(id, request);
            Ebean.commitTransaction();
            return ok(Json.toJson(Request.findByProperty("id", id)));
        }catch(Exception e){
            logger.error("Error interno intentando actualizar", e);
            return internalServerError(JsonNodeFactory.instance.objectNode().put("message", "Error interno intentando actualizar el pedido"));
        }finally{
            Ebean.endTransaction();
        }
    }
}
