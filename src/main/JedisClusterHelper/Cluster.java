package JedisClusterHelper;
import redis.clients.jedis.*;

import java.util.HashSet;

/**
 * Created by kartik.k on 8/21/2014.
 */
public class Cluster extends Page {
    private RedisClusterForRedisAdmin jedisCluster;
    private String[] hostPortArrayList;
    private int curHostPortBeingScannedPointer;

    public Cluster(RedisClusterForRedisAdmin jedisCluster, int pagesize){
        this("0",0,jedisCluster,pagesize);
    }

    private Cluster(String cursor, int curHostPortBeingScannedPointer, RedisClusterForRedisAdmin jedisCluster, int pagesize){
        super(pagesize,cursor);
        this.jedisCluster = jedisCluster;
        hostPortArrayList = jedisCluster.getClusterNodes().keySet().toArray(new String[jedisCluster.getClusterNodes().size()]);


        this.curHostPortBeingScannedPointer = curHostPortBeingScannedPointer;
        fillKeySet();
    }

    Page nextPage(){
        Page nextPage = new Cluster(cursorForScan,curHostPortBeingScannedPointer,jedisCluster,pageSize);
        return nextPage;
    }

    void close(){
        jedisCluster = null;
    }

    protected void fillKeySet(){
        int remainingSpaceOnPage = pageSize;
        keySet = new HashSet<String>();
        String newCursor = cursorForScan;
        while (curHostPortBeingScannedPointer < hostPortArrayList.length && remainingSpaceOnPage > 0) {
            JedisPool curJedisPool = jedisCluster.getClusterNodes().get(hostPortArrayList[curHostPortBeingScannedPointer]);
            Jedis curNodeBeingScanned = curJedisPool.getResource();
            try {
                if(!RedisClusterForRedisAdmin.isNodeMaster(curNodeBeingScanned)){
                    curHostPortBeingScannedPointer++;
                    continue;
                }
            } catch (InfoFormatException e) {
                System.out.println("JedisInfoFormatException");
            }
            while (remainingSpaceOnPage > 0) {
                ScanParams scanParams = new ScanParams();
                scanParams.count(pageSize);
                ScanResult<String> scanResult = curNodeBeingScanned.scan(newCursor,scanParams);
                keySet.addAll(scanResult.getResult());
                newCursor = scanResult.getStringCursor();
                remainingSpaceOnPage -= keySet.size();
                if(newCursor.equals("0"))
                    break;
            }
            if(remainingSpaceOnPage>0){
                curJedisPool.returnBrokenResource(curNodeBeingScanned);
                curHostPortBeingScannedPointer++;
                newCursor = "0";
            }
        }
        cursorForScan = newCursor;

        if(keySet.size()<pageSize)
            System.out.println("returning half filled page of size "+ Integer.toString(keySet.size()));
    }
}
