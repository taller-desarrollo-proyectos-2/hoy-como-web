package services;

import com.avaje.ebean.ExpressionList;

import java.util.Map;

public class FinderService {

    public static ExpressionList findByMap(ExpressionList<?> exp, Map<String, String[]> map){
        for(Map.Entry<String, String[]> entry: map.entrySet()){
            exp = exp.conjunction().disjunction();
            for(String value : map.get(entry.getKey())){
                if(entry.getKey().startsWith("from")) {
                    exp.ge(entry.getKey().split("_")[1], value);
                }else if(entry.getKey().startsWith("to")) {
                    exp.le(entry.getKey().split("_")[1], value);
                }else{
                    exp.eq(entry.getKey(), value.equals("null") ? null : value);
                }
            }
            exp = exp.endJunction().endJunction();
        }
        return exp;
    }
}
