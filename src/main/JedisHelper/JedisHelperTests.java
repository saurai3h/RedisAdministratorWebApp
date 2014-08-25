package JedisHelper;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kartik.k on 8/22/2014.
 */
public class JedisHelperTests {
    RedisClusterForRedisAdmin redisCluster;
    @Before
    public void setup(){
        Set<HostAndPort> instanceSet = new HashSet<HostAndPort>();
        for(int portNo = 7000;portNo<=7005;portNo++)
            instanceSet.add(new HostAndPort("172.16.137.228",portNo));
        redisCluster = new RedisClusterForRedisAdmin(instanceSet);
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
        Page firstPage = new ClusterPage(redisCluster,1000);

        ArrayList<Page> listOfPages = new ArrayList<Page>();
        listOfPages.add(firstPage);
        Set<String> setOfAllKeys = new HashSet<String>();
        setOfAllKeys.addAll(firstPage.getKeyList());
        for(int i=0; i<10;i++){
            Page nextPage = listOfPages.get(i).nextPage();
            Set<String> keySet = nextPage.getKeyList();
            System.out.println("size of list is "+ Integer.toString(keySet.size()));
            setOfAllKeys.addAll(keySet);
            listOfPages.add(nextPage);


        }
        assertEquals("not all/more than all keys were returned",1000,setOfAllKeys.size());
    }
}
