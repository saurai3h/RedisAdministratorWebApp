package Model;

import redis.clients.jedis.HostAndPort;

import java.sql.*;
import java.util.*;

/**
 * Created by kartik.k on 8/25/2014.
 */
public class InstanceHelper {


    public static boolean add(HostAndPort hostAndPort, String userName) {
        boolean success = true;
        success = success && SqlInterface.addVisibility(userName, hostAndPort);
        if(!SqlInterface.isPresentInInstances(hostAndPort)){
            success = success && SqlInterface.addToInstances(hostAndPort);
        }
        return success;
    }
    public static boolean hideHostPort(HostAndPort hostAndPort,String userName){

            boolean success = SqlInterface.deleteFromVisibleInstances(hostAndPort, userName);
            return  success;
    }

    public static ArrayList<HostAndPort> getAllStoredInstances(String userName){
        try {
            ArrayList<HostAndPort> listOfInstances = new ArrayList<HostAndPort>();

            Connection conn = SqlInterface.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT visibleinstances.HostName,visibleinstances.PortNumber  FROM users INNER JOIN visibleinstances ON visibleinstances.UserName = users.UserName WHERE users.UserName = ?"
            );
            stmt.setString(1,userName);
            ResultSet rs = stmt.executeQuery();


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
