package Model;

import JedisClusterHelper.RedisClusterForRedisAdmin;
import redis.clients.jedis.HostAndPort;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by kartik.k on 8/25/2014.
 */
public class ClusterAdder {
    private HostAndPort hostAndPort;
    private String clusterName;

    public boolean add() {
        Set<HostAndPort> instanceSet = new HashSet<HostAndPort>();
        instanceSet.add(hostAndPort);
        RedisClusterForRedisAdmin cluster = new RedisClusterForRedisAdmin(instanceSet);
        instanceSet = cluster.getSetOfAllInstances();
        try {
            boolean wasQueryExecutedSuccessfully = (instanceSet.size() > 0);
            for(HostAndPort instance:instanceSet) {
                Connection conn = SqlInterface.getConnection();
                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO INSTANCES (HostName,PortNumber,ClusterName) VALUES" +
                        "(\"" + instance.getHost() + "\", \"" +
                        Integer.toString(instance.getPort()) + "\", \""
                        + clusterName + "\")";
                wasQueryExecutedSuccessfully &= stmt.execute(sql);
                conn.close();
                stmt.close();
            }
            return wasQueryExecutedSuccessfully;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ClusterAdder(HostAndPort hostAndPort, String clusterName){
        this.clusterName = clusterName;
        this.hostAndPort = hostAndPort;
    }

    public static List<String> getAllStoredClusters(){
        try {
            List<String> listOfClusters = new LinkedList<String>();

            Connection conn = SqlInterface.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT ClusterName  FROM instances GROUP by ClusterName;";
            ResultSet rs = stmt.executeQuery(sql);

            if(rs.wasNull())    {
                return null;
            }

            while(rs.next())    {
                listOfClusters.add(rs.getString("ClusterName"));
            }

            conn.close();
            stmt.close();
            rs.close();
            return listOfClusters;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }
}

