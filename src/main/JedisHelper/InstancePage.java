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
    private Jedis jedis;
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


}
