package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;
import models.Commerce;
import models.Request;
import models.User;
import org.joda.time.DateTime;
import play.libs.Json;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ReportsService {

    public static JsonNode fillReports(User user, DateTime from, DateTime to){
        ArrayNode node = JsonNodeFactory.instance.arrayNode();
        Double fee = ConfigFactory.load().getDouble("fee");
        while(from.isBefore(to.plusDays(1).withTimeAtStartOfDay())){
            DateTime nextDay = from.plusDays(1).minusSeconds(1);
            for(Commerce commerce: user.myCommerces()) {
                ObjectNode dayNode = JsonNodeFactory.instance.objectNode();
                Map<String, String[]> map = new HashMap<>();
                map.put("from", new String[]{from.toString()});
                map.put("to", new String[]{nextDay.toString()});
                map.put("status", new String[]{Request.Status.DELIVERED.toString()});
                map.put("commerceId", new String[]{commerce.getId().toString()});
                List<Request> requests = RequestsService.findFilteredRequests(map, null);
                dayNode.put("day", from.toString());
                dayNode.put("requests", requests.size());
                dayNode.put("leadTime", commerce.findLeadTimeFromRequests(requests));
                dayNode.put("score", commerce.findScoreFromRequests(requests));
                dayNode.put("billed", requests.stream().map(x -> (x.getTotal())).reduce( (x, y) -> x + y).orElse(0D));
                dayNode.put("fee",  new BigDecimal(dayNode.get("billed").asDouble() *fee).setScale(2, RoundingMode.HALF_UP).doubleValue());
                dayNode.set("commerce", Json.toJson(commerce));
                node.add(dayNode);
            }
            from = nextDay.plusSeconds(1);
        }


        return node;
    }
}
