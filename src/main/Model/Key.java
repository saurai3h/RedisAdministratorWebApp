package Model;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by kartik.k on 9/5/2014.
 */
public class Key {
    String keyName;

    public String getName() {
        return keyName;
    }

    public String getType() {
        return type;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    String type;
    String expiryTime;
    Key(Jedis jedis,String keyName){
        this.keyName = keyName;
        type = jedis.type(keyName);
        Long ttl = jedis.ttl(keyName);
        Date date = new Date(new Date().getTime()+1000*ttl);
        if(ttl == -1){
            expiryTime = "expiry time not set";
        }
        else if(ttl == -2){
            expiryTime = "probably key got deleted";
        }
        else if(ttl>0) {
            expiryTime = "Expires at "+ new SimpleDateFormat("HH:mm:ss").format(date);
        }
        else {
            expiryTime = "strange ttl!";
        }
    }

    public Map<String,String> getJsonValueOfAKey(Jedis jedis){
        String JsonOfValue = "";
        Object value;
        if (type.equals("zset")) {
            value = jedis.zrange(keyName, 0, -1);
            HashMap<String,Double> map = new HashMap<String, Double>();
            for (String element:(Set<String>)value){
                Double score = jedis.zscore(keyName,element);
                map.put(element,score);
            }
            value = map;
        } else if (type.equals("hash")) {
            value = jedis.hgetAll(keyName);

        } else if (type.equals("list")) {
            value = jedis.lrange(keyName,0,-1);

        } else if (type.equals("string")) {
            value = jedis.get(keyName);
            JsonOfValue = "[\"" + value + "\"]";

        } else if (type.equals("set")) {
            value = jedis.smembers(keyName);

        } else {
            System.out.println("Invalid Data Structure " + keyName);
            return new HashMap<String, String>();
        }

        if(!type.equals("string")) {
            JsonOfValue = new Gson().toJson(value);
        }
        System.out.println(JsonOfValue);
        Map<String,String> map = new HashMap<String, String>();
        map.put("json",JsonOfValue);
        map.put("type",type);
        return map;
    }
}
