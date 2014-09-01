package Model;

import JedisHelper.RedisClusterForRedisAdmin;
import redis.clients.jedis.HostAndPort;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by kartik.k on 8/25/2014.
 */
public class InstanceAdder {
    private HostAndPort hostAndPort;
    private String clusterName;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://172.16.137.79/testJedis";
    static final String USER = "root";
    static final String PASS = "password";

    public boolean add() {
        Set<HostAndPort> instanceSet = new HashSet<HostAndPort>();
        instanceSet.add(hostAndPort);
        RedisClusterForRedisAdmin cluster = new RedisClusterForRedisAdmin(instanceSet);
        instanceSet = cluster.getSetOfAllInstances();
        try {
            boolean wasQueryExecutedSuccessfully = (instanceSet.size() > 0);
            Class.forName(JDBC_DRIVER);
            for(HostAndPort instance:instanceSet) {
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public InstanceAdder(HostAndPort hostAndPort, String clusterName){
        this.clusterName = clusterName;
        this.hostAndPort = hostAndPort;
    }

    public static List<String> getAllStoredClusters(){
        try {
            List<String> listOfClusters = new LinkedList<String>();

            Class.forName(JDBC_DRIVER);

            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
