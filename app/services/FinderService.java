package services;

import com.avaje.ebean.ExpressionList;

import java.util.Map;

public class FinderService {

    public static ExpressionList findByMap(ExpressionList<?> exp, Map<String, String[]> map){
        for(Map.Entry entry: map.entrySet()){
            exp = exp.conjunction().disjunction();
            for(String value : map.get(entry.getKey())){
                exp.eq(entry.getKey().toString(), value.equals("null") ? null : value);
            }
            exp = exp.endJunction().endJunction();
        }
        return exp;
    }
}
