package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Commerce;
import models.Qualification;
import models.Request;
import org.joda.time.DateTime;

import java.util.Arrays;

public class DashBoardServices {

    public static JsonNode getCommerceInformation(Commerce commerce, DateTime from, DateTime to){

        Commerce dbCommerce = Commerce.findByProperty("id", commerce.getId());

        ObjectNode commerceInfo = JsonNodeFactory.instance.objectNode();
        ObjectNode requests = JsonNodeFactory.instance.objectNode();
        ObjectNode qualifications = JsonNodeFactory.instance.objectNode();
        // ## REQUESTS ##
        requests.put(Request.Status.WAITING_CONFIRMATION.toString(), Request.countByPropertiesAt(Arrays.asList("singleRequests.plate.commerce.id", "status"), Arrays.asList(commerce.getId(), Request.Status.WAITING_CONFIRMATION), from, to));
        requests.put(Request.Status.ON_PREPARATION.toString(), Request.countByPropertiesAt(Arrays.asList("singleRequests.plate.commerce.id", "status"), Arrays.asList(commerce.getId(), Request.Status.ON_PREPARATION), from, to));
        requests.put(Request.Status.ON_THE_WAY.toString(), Request.countByPropertiesAt(Arrays.asList("singleRequests.plate.commerce.id", "status"), Arrays.asList(commerce.getId(), Request.Status.ON_THE_WAY), from, to));
        requests.put(Request.Status.DELIVERED.toString(), Request.countByPropertiesAt(Arrays.asList("singleRequests.plate.commerce.id", "status"), Arrays.asList(commerce.getId(), Request.Status.DELIVERED), from, to));
        requests.put(Request.Status.CANCELLED_BY_COMMERCE.toString(), Request.countByPropertiesAt(Arrays.asList("singleRequests.plate.commerce.id", "status"), Arrays.asList(commerce.getId(), Request.Status.CANCELLED_BY_COMMERCE), from, to));
        requests.put(Request.Status.CANCELLED_BY_USER.toString(), Request.countByPropertiesAt(Arrays.asList("singleRequests.plate.commerce.id", "status"), Arrays.asList(commerce.getId(), Request.Status.CANCELLED_BY_USER), from, to));
        commerceInfo.set("requests", requests);

        // ## QUALIFICATIONS ##
        qualifications.put("score", dbCommerce.findScoreBetweenDates(from, to));
        qualifications.put("withoutResponse", Qualification.countByPropertiesEQAndNEAt(Arrays.asList("request.singleRequests.plate.commerce.id", "response"),
                                                                                                Arrays.asList(commerce.getId(), "NULL"),
                                                                                                Arrays.asList("comment"), Arrays.asList("NULL"), from, to));
        commerceInfo.set("qualifications", qualifications);

        // ## LEAD TIME AND MONEY ##
        commerceInfo.put("leadTime", dbCommerce.findLeadTimeBetweenDates(from, to));
        commerceInfo.put("money", Request.countMoneyByCommerceAndDate(commerce.getId(), from, to));

        return commerceInfo;
    }
}
