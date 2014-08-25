package JedisHelper;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisClusterException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by kartik.k on 8/20/2014.
 */
public class RedisClusterForRedisAdmin extends JedisCluster {
    public RedisClusterForRedisAdmin(Set<HostAndPort> instanceSet) {
        super(instanceSet);
    }

    public void pingAll(){
/*
* Even when cluster is down, pinging individual nodes gets a response from these clusters
* But the cluster cannot accept get or set requsts and gives a clusterDown exception
* so pinging all nodes in a cluster can be helpful to determine which all nodes failed
* */

        for (String hostPort : getClusterNodes().keySet()) {
            try {
                JedisPool jedisPool = getClusterNodes().get(hostPort);
                Jedis jedis = jedisPool.getResource();
                jedis.close();
                jedisPool.returnBrokenResource(jedis);
            } catch (JedisException JE) {
                System.out.println("Jedis exception occured for " + hostPort);
            }

        }
    }

    public boolean isClusterAlive(){
        boolean isClusterAliveAndWell;
        try {
            boolean wasTryAlreadyPresent = exists("try");
            setnx("try", "toSetSomething");
            String getAnswer = get("try");
            isClusterAliveAndWell = wasTryAlreadyPresent|getAnswer.equals("toSetSomething");
            if(!wasTryAlreadyPresent)
                del("try");
        }
        catch (JedisClusterException jedisClusterException){
            return false;
        }

        return isClusterAliveAndWell;
    }

    public Map<String,String> getAllNodesInfo(){
        Map<String,String> infoMap =
                new HashMap<String, String>(getClusterNodes().keySet().size());
        for (String hostPort : getClusterNodes().keySet()) {
            try {
                JedisPool jedisPool = getClusterNodes().get(hostPort);
                Jedis jedis = jedisPool.getResource();
                String info = jedis.info();
                jedis.close();
                jedisPool.returnBrokenResource(jedis);
                infoMap.put(hostPort,info);
            } catch (JedisException JE) {
                System.out.println("Jedis exception occured!! :(");
            }

        }
        return infoMap;
    }

    public Set<HostAndPort> getSetOfAllInstances(){
        Set<HostAndPort> setOfInstances = new HashSet<HostAndPort>();
        for (String hostPort : getClusterNodes().keySet()) {
            try {
                JedisPool jedisPool = getClusterNodes().get(hostPort);
                Jedis jedis = jedisPool.getResource();
                String host = hostPort.split(":")[0];
                int port = Integer.parseInt(hostPort.split(":")[1]);
                HostAndPort instance = new HostAndPort(host,port);
                setOfInstances.add(instance);
                jedis.close();
                jedisPool.returnBrokenResource(jedis);
            }catch (JedisException JE) {
                System.out.println("Jedis exception occured!! :(");
            }

        }
        return setOfInstances;
    }

    public static boolean isNodeMaster(Jedis jedis) throws InfoFormatException {
        String info = jedis.info("Replication");
        int indexOfRoleLabel =  info.indexOf("role:");
        String slaveForSlaveNodes = info.substring(indexOfRoleLabel+5);
        if(slaveForSlaveNodes.startsWith("master"))
            return true;
        if(slaveForSlaveNodes.startsWith("slave"))
            return false;
        throw new InfoFormatException();

    }

    //need to check



}
