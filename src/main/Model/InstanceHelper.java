package Model;

import redis.clients.jedis.HostAndPort;

import java.sql.*;
import java.util.*;

/**
 * Created by kartik.k on 8/25/2014.
 */
public class InstanceHelper {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://172.16.137.79/testJedis";
    static final String USER = "root";
    static final String PASS = "password";

    public static void add(HostAndPort hostAndPort) {

        try {
            Class.forName(JDBC_DRIVER);

            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "insert into instances (HostName,PortNumber) VALUES" +
                    "(\"" + hostAndPort.getHost() + "\", \"" +
                    Integer.toString(hostAndPort.getPort()) + "\");" ;

            conn.close();
            stmt.close();

       } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<HostAndPort> getAllStoredInstances(){
        try {
            ArrayList<HostAndPort> listOfInstances = new ArrayList<HostAndPort>();


            Class.forName(JDBC_DRIVER);

            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "SELECT HostName,PortNumber  FROM instances;";
            ResultSet rs = stmt.executeQuery(sql);

            if(rs.wasNull())    {
                return null;
            }

            while(rs.next())    {
                listOfInstances.add(new HostAndPort(rs.getString("HostName"),Integer.parseInt(rs.getString("PortNumber"))));
            }

            conn.close();
            stmt.close();
            rs.close();
            return listOfInstances;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
