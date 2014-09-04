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

    public static boolean add(HostAndPort hostAndPort) {

        try {
            Class.forName(JDBC_DRIVER);

            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "insert into instances (HostName,PortNumber,IsMonitored) VALUES" +
                    "(\"" + hostAndPort.getHost() + "\", \"" +
                    Integer.toString(hostAndPort.getPort()) + "\"," +
                    Integer.toString(0) + ");" ;
            stmt.executeUpdate(sql);
            conn.close();
            stmt.close();
            return true;

       } catch (SQLException e) {
            return false;

        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean delete(HostAndPort hostAndPort) {

        try {
            Class.forName(JDBC_DRIVER);

            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "delete from instances where HostName = " +
                    "\"" + hostAndPort.getHost() + "\"" + "and PortNumber = " +
                    "\"" + Integer.toString(hostAndPort.getPort()) + "\";";
            stmt.executeUpdate(sql);
            conn.close();
            stmt.close();
            return true;
        } catch (SQLException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
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
