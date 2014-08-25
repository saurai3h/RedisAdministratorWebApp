package JedisHelper;
import redis.clients.jedis.*;

import java.util.HashSet;

/**
 * Created by kartik.k on 8/21/2014.
 */
public class ClusterPage extends Page {
    JedisCluster jedisCluster;
    String[] hostPortArrayList;
    int curHostPortBeingScannedPointer;

    public ClusterPage(JedisCluster jedisCluster, int pagesize){
        this("0",0,jedisCluster,pagesize);
    }

    private ClusterPage(String cursor, int curHostPortBeingScannedPointer, JedisCluster jedisCluster, int pagesize){
        super(pagesize,cursor);
        this.jedisCluster = jedisCluster;
        hostPortArrayList = jedisCluster.getClusterNodes().keySet().toArray(new String[jedisCluster.getClusterNodes().size()]);
        this.curHostPortBeingScannedPointer = curHostPortBeingScannedPointer;
        fillKeyList();
    }

    Page nextPage(){
        Page nextPage = new ClusterPage(cursorForScan,curHostPortBeingScannedPointer,jedisCluster,pageSize);
        return nextPage;
    }

    void close(){
        jedisCluster = null;
    }

    private void fillKeyList(){
        int remainingSpaceOnPage = pageSize;
        keyList = new HashSet<String>();
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
            //System.out.println("scanning " + hostPortArrayList[curHostPortBeingScannedPointer] + "....");
            while (remainingSpaceOnPage > 0) {
                ScanParams scanParams = new ScanParams();
                scanParams.count(pageSize);
                ScanResult<String> scanResult = curNodeBeingScanned.scan(newCursor,scanParams);
                keyList.addAll(scanResult.getResult());
                newCursor = scanResult.getStringCursor();
                remainingSpaceOnPage -= keyList.size();
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

        if(keyList.size()<pageSize)
            System.out.println("returning half filled page of size "+ Integer.toString(keyList.size()));
    }

}
