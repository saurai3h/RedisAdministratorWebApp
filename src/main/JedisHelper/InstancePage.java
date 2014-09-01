package JedisHelper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kartik.k on 8/27/2014.
 */
public class InstancePage extends Page {
    Jedis jedis;
    protected InstancePage(String cursor, Jedis jedis,int pageSize){
        super(pageSize,cursor);
        this.jedis = jedis;
    }

    public InstancePage(Jedis jedis, int pageSize){
        this("0",jedis,pageSize);
    }

    protected void fillKeySet(){
        ScanParams scanParams = new ScanParams();
        scanParams.count(pageSize);
        ScanResult<String> result = jedis.scan(cursorForScan, scanParams);
        String nextCursor = result.getStringCursor();
        keySet.addAll(result.getResult());
        cursorForScan = nextCursor;
    }

    public Page nextPage(){
        return new InstancePage(cursorForScan,jedis,pageSize);
    }

    public Map<String,Map<String,String>> getInfoAsMap(){
        String infoOutput = jedis.info();
        String[] sections = infoOutput.split("\n#");
        Map<String,Map<String,String>> mappedInfo = new HashMap<String, Map<String, String>>();
        for(String section:sections){
            Map<String,String> sectionMap = new HashMap<String, String>();
            String[] keyValuePairsInInfo = section.split("\n");
            String sectionName = keyValuePairsInInfo[0];
            for(String keyValuePair:keyValuePairsInInfo){
                String[] keyValueArray = keyValuePair.split(":");
                if(keyValueArray.length > 2){
                    System.out.println("key-value pair expected, got more than 2 things");
                    return null;
                }
                if(keyValueArray.length == 1){
                    if(!sectionName.equals(keyValueArray[0])){
                        System.out.println("key-value pair expected, got only one thing");
                    };
                }
                if(keyValueArray.length == 2){
                    sectionMap.put(keyValueArray[0],keyValueArray[1]);
                }
                if(keyValueArray.length < 1){
                    System.out.println("key-value pair expected, got an empty array");
                }
            }
            mappedInfo.put(sectionName,sectionMap);
        }
        return mappedInfo;
    }
}
