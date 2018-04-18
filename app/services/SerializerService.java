package services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.libs.Json;

import java.util.List;

public class SerializerService {

    public static ArrayNode serializeList(List<?> list){
        ArrayNode node = JsonNodeFactory.instance.arrayNode();
        for(Object element: list){
            node.add(Json.toJson(element));
        }
        return node;
    }
}
