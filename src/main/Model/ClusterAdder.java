package Model;

import JedisHelper.RedisClusterForRedisAdmin;
import redis.clients.jedis.HostAndPort;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kartik.k on 8/25/2014.
 */
public class ClusterAdder {
    HostAndPort hostAndPort;
    String clusterName;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://172.16.137.79/testJedis";
    static final String USER = "root";
    static final String PASS = "password";

    public boolean add() {
        try {

            Class.forName(JDBC_DRIVER);

            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO INSTANCES (HostName,PortNumber,ClusterName) VALUES" +
                    "(\"" + hostAndPort.getHost() + "\", \""+
                    Integer.toString(hostAndPort.getPort()) + "\", \""
                     + clusterName + "\")";
            boolean wasQueryExecutedSuccessfully = stmt.execute(sql);
            conn.close();
            stmt.close();

            return wasQueryExecutedSuccessfully;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
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
