package Model;

import redis.clients.jedis.HostAndPort;

import java.sql.*;
import java.util.*;

/**
 * Created by kartik.k on 8/25/2014.
 */
public class InstanceHelper {

    public static boolean add(HostAndPort hostAndPort) {
        try {
            Connection conn = SqlInterface.getConnection();
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

        }
    }

    public static boolean delete(HostAndPort hostAndPort) {

        try {

            Connection conn = SqlInterface.getConnection();
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
        }
    }

    public static ArrayList<HostAndPort> getAllStoredInstances(){
        try {
            ArrayList<HostAndPort> listOfInstances = new ArrayList<HostAndPort>();

            Connection conn = SqlInterface.getConnection();
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
        }
    }
}
