package services;

import com.avaje.ebean.ExpressionList;
import play.db.ebean.Model;

import java.util.Map;

public class FinderService {

    public static ExpressionList findByMap(ExpressionList<? extends Model> exp, Map<String, String[]> map){
        for(Map.Entry<String, String[]> entry: map.entrySet()){
            exp = exp.conjunction().disjunction();
            for(String value : map.get(entry.getKey())){
                exp.eq(entry.getKey(), value.equals("null") ? null : value);
            }
            exp = exp.endJunction().endJunction();
        }
        return exp;
    }
}
