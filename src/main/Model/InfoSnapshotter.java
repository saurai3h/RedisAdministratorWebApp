package Model;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by kartik.k on 9/2/2014.
 */
public class InfoSnapshotter implements Runnable {
    private volatile boolean persistStoredInfo;
    HostAndPort monnitoredInstanceHostPort;
    HostAndPort infoStorageHostPort;
    Jedis monitoredInstance;
    Jedis infoStorage;
    ExecutorService executorService;
    public InfoSnapshotter(HostAndPort infoStorageHostPort, HostAndPort monnitoredInstanceHostPort){
        this.infoStorageHostPort = infoStorageHostPort;
        this.monnitoredInstanceHostPort = monnitoredInstanceHostPort;

        monitoredInstance = new Jedis(monnitoredInstanceHostPort.getHost(),
                monnitoredInstanceHostPort.getPort());
        infoStorage = new Jedis(infoStorageHostPort.getHost(),infoStorageHostPort.getPort());

        persistStoredInfo = false;
    }
    public void run() {
            storeOneInfoSnapshot();
    }

    private synchronized void storeOneInfoSnapshot() {

        Map<String, String> map = getInfoAsMap(monitoredInstance);
        Date date = new Date();

        String key = monnitoredInstanceHostPort.getHost()+":" +
                Integer.toString(monnitoredInstanceHostPort.getPort()) +":" +
                Long.toString(date.getTime());
        infoStorage.hmset(key, map);
        if(!persistStoredInfo)
            infoStorage.expire(key,22);
        else
            System.out.println("stored!! " + monnitoredInstanceHostPort.toString());
    }

    public void startMonitorMode(){persistStoredInfo = true;}

    public void stopMonitorMode(){
        persistStoredInfo = false;
    }

    public Map<String,String> getInfoAsMap(Jedis jedis){
        String infoOutput = jedis.info();
        String[] sections = infoOutput.split("\n#");
        Map<String,String> sectionMap = new HashMap<String, String>();
        for(String section:sections){
            String[] keyValuePairsInInfo = section.split("\n");
            String sectionName = keyValuePairsInInfo[0].replace('#',' ').trim();
//            System.out.println("section is " + sectionName);
            for(String keyValuePair:keyValuePairsInInfo){
                String[] keyValueArray = keyValuePair.split(":");
                if(keyValueArray.length > 2){
                    System.out.println("key-value pair expected, got more than 2 things");
                    return null;
                }
                if(keyValueArray.length == 2){
                    sectionMap.put(
                            keyValueArray[0],keyValueArray[1].trim());
//                    System.out.println(sectionName.trim() +":" +keyValueArray[0] + " == " + keyValueArray[1]);
                }
                if(keyValueArray.length < 1){
                    System.out.println("key-value pair expected, got an empty array");
                }
            }
        }
        return sectionMap;
    }
}


