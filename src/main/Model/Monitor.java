package Model;

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
    HostAndPort monnitoredInstanceHostPort;
    HostAndPort infoStorageHostPort;
    private double interval;
    Jedis monitoredInstance;
    Jedis infoStorage;
    public Monitor(HostAndPort infoStorageHostPort,HostAndPort monnitoredInstanceHostPort,
                   double snapshottingIntervalInSeconds){
        this.infoStorageHostPort = infoStorageHostPort;
        this.monnitoredInstanceHostPort = monnitoredInstanceHostPort;

        monitoredInstance = new Jedis(monnitoredInstanceHostPort.getHost(),
                monnitoredInstanceHostPort.getPort());
        infoStorage = new Jedis(infoStorageHostPort.getHost(),infoStorageHostPort.getPort());

        terminateSignalNotReceived = true;
        this.interval = snapshottingIntervalInSeconds;
    }
    public void run() {
        System.out.println("starting to run..");

        while (terminateSignalNotReceived) {
            System.out.println("running..");
            storeOneInfoSnapshot(monitoredInstance, infoStorage);
            try {
                Thread.sleep((long) interval*1000);
            } catch (InterruptedException e) {
                System.out.println("what creature woke me up??");
            }
        }
    }

    private synchronized void storeOneInfoSnapshot(Jedis monitoredInstance, Jedis infoStorage) {

        Map<String, String> map = getInfoAsMap(monitoredInstance);
        Date date = new Date();
        System.out.println(infoStorage.ping());
        infoStorage.hmset(monnitoredInstanceHostPort.getHost()+":" +
                Integer.toString(monnitoredInstanceHostPort.getPort()) +":" +
                Long.toString(date.getTime()), map);
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


