package JedisHelper;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kartik.k on 9/2/2014.
 */
public class Monitor extends Thread {
    private volatile boolean terminateSignalNotReceived;
    private Jedis monitoredInstance;
    private Jedis infoStorage;

    public Monitor(HostAndPort infoStorage,HostAndPort monitoredInstance){
        terminateSignalNotReceived = true;
        this.monitoredInstance = new Jedis(monitoredInstance.getHost(),monitoredInstance.getPort());
        this.infoStorage = new Jedis(infoStorage.getHost(),infoStorage.getPort());
    }
    public void run() {


        while (terminateSignalNotReceived) {
            storeOneInfoSnapshot(monitoredInstance, infoStorage);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("what creature woke me up??");
            }
        }
    }

    private synchronized void storeOneInfoSnapshot(Jedis monitoredInstance, Jedis infoStorage) {

        Map<String, String> map = getInfoAsMap(monitoredInstance);
        Date date = new Date();
        System.out.println(infoStorage.ping());
        infoStorage.hmset(Long.toString(date.getTime()), map);

    }

    public void terminate(){
        terminateSignalNotReceived = false;
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
                if(keyValueArray.length == 1){
                    if(!sectionName.equals(keyValueArray[0])){
                        //System.out.println("key-value pair expected, got only one thing");
                    };
                }
                if(keyValueArray.length == 2){
                    sectionMap.put(sectionName +":" +keyValueArray[0],keyValueArray[1].trim());
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


