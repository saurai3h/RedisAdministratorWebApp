package JedisClusterHelper;

import Model.InfoSnapshotter;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by kartik.k on 8/22/2014.
 */
public class JedisHelperTests {
    RedisClusterForRedisAdmin redisCluster;
    Jedis jedis;
    @Before
    public void setupInstance(){
        jedis = new Jedis("localhost",7004);
    }

//    public void setupCluster(){
//        Set<HostAndPort> instanceSet = new HashSet<HostAndPort>();
//        for(int portNo = 7000;portNo<=7000;portNo++)
//            instanceSet.add(new HostAndPort("172.16.137.228",portNo));
//        redisCluster = new RedisClusterForRedisAdmin(instanceSet);
//    }



    @Test
    public void shouldGiveAllInstances(){
        System.out.println(redisCluster.getSetOfAllInstances().toString());
    }
    @Test
    public void clusterShouldBeUp(){
        assertTrue("Cluster should be up and running",redisCluster.isClusterAlive());
    }

    @Test
    public void shouldAllowAddingIntegersToRedis(){
        int noOfValues = 1000;
        int offset = 109;
        for(int i = 0;i<noOfValues;i++){
            redisCluster.set(Integer.toString(i), Integer.toString(i + offset));
            if(i%1000 == 0){
                System.out.println(Integer.toString(i / 1000) + "k writes completed..");
            }
        }

    }

    @Test
    public void shouldAllowScanningAllNodes(){
        redisCluster.pingAll();
    }

    @Test
    public void shouldPrintPagesProperly(){
        Page firstPage = new Cluster(redisCluster,1000);

        ArrayList<Page> listOfPages = new ArrayList<Page>();
        listOfPages.add(firstPage);
        Set<String> setOfAllKeys = new HashSet<String>();
        setOfAllKeys.addAll(firstPage.getKeySet());
        for(int i=0; i<10;i++){
            Page nextPage = listOfPages.get(i).nextPage();
            Set<String> keySet = nextPage.getKeySet();
            System.out.println("size of list is "+ Integer.toString(keySet.size()));
            setOfAllKeys.addAll(keySet);
            listOfPages.add(nextPage);


        }
        assertEquals("not all/more than all keys were returned",1000,setOfAllKeys.size());
    }


    @Test
    public void shouldStoreInfo() {
        HostAndPort instance = new HostAndPort("172.16.137.228",6379);
        HostAndPort infoStore = new HostAndPort("172.16.137.228",7000);
        InfoSnapshotter infoSnapshotter = new InfoSnapshotter(infoStore,instance);
        Thread t = new Thread(infoSnapshotter);
        t.run();

    }

    @Test
    public void shouldAllowAddingIntegersToRedisInstance(){
        int noOfValues = 1000;
        int noOfDataStructsOfEachType = 3;
        for(int i = 0;i<noOfValues;i++){
            String value = "value" + Integer.toString(i);
            String key = "key" + Integer.toString(i);
            String dataStructSuffix = Integer.toString(noOfDataStructsOfEachType);
            switch (i%5){
                case 0:
                    jedis.set(key, value);
                    break;
                case 1:
                    jedis.lpush("l"+dataStructSuffix,value);
                    break;
                case 2:
                    jedis.hset("hash"+dataStructSuffix,key,value);
                    break;
                case 3:
                    jedis.sadd("set"+dataStructSuffix,value);
                    break;
                case 4:
                    jedis.zadd("zset"+dataStructSuffix,i%11,value);
                    break;
                default:
                    fail();
            }
        }

    }

}
