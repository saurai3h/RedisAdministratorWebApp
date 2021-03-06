package Model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import redis.clients.jedis.HostAndPort;

import java.sql.*;
import java.util.*;

/**
 * Created by kartik.k on 8/25/2014.
 */
public class InstanceHelper {


    public static String add(HostAndPort hostAndPort, String addeeUserName, String adderUserName) {
        boolean success = true;
        String status;
        boolean isPresentInInstances = SqlInterface.isPresentInInstances(hostAndPort);
        boolean hasPermissionToAdd;
        if(!isPresentInInstances){
            success = success && SqlInterface.addToInstances(hostAndPort);
            success = success && SqlInterface.addVisibility(adderUserName, hostAndPort);
            hasPermissionToAdd = true;
        }
        else {
            ArrayList<HostAndPort> visibleInstances = getAllVisibleInstances(adderUserName);
            hasPermissionToAdd = visibleInstances.contains(hostAndPort);
        }
        if(hasPermissionToAdd) {
            success = success && SqlInterface.addVisibility(addeeUserName, hostAndPort);
        }
        if(hasPermissionToAdd && success){
            if(isPresentInInstances){

                status = Constants.ALREADY_PRESENT_IN_INSTANCES;
            }
            else {
                status = Constants.SUCCESS_STATUS_CODE;
            }
        }
        else if(!success){
            status = Constants.SQL_ERROR_STATUS_CODE;
        }
        else if(!hasPermissionToAdd){
            status = Constants.PERMISSION_DENIED_STATUS_CODE;
        }
        else {
            status = Constants.UNKNOWN_ERROR;
        }
        return status;
    }
    public static boolean hideHostPort(HostAndPort hostAndPort,String userName){

            boolean success = SqlInterface.deleteFromVisibleInstances(hostAndPort, userName);
            return  success;
    }

    public static ArrayList<HostAndPort> getAllVisibleInstances(String userName){
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

    public static ArrayList<Boolean> getIsMonitored(ArrayList<HostAndPort> listOfHostAndPort){
        try {
            ArrayList<Boolean> isMonitoredList = new ArrayList<Boolean>();

            Connection conn = SqlInterface.getConnection();

            for(HostAndPort hostAndPort : listOfHostAndPort) {
                String host = hostAndPort.getHost();
                String port = String.valueOf(hostAndPort.getPort());

                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT IsMonitored from instances where HostName = ? and PortNumber = ?"
                );
                stmt.setString(1, host);
                stmt.setString(2, port);

                ResultSet rs = stmt.executeQuery();
                if (rs.wasNull()) {
                    isMonitoredList.add(false);
                }
                while (rs.next()) {
                    Boolean isMonitoring;
                    if(rs.getString("IsMonitored").equals("1"))
                        isMonitoring = true;
                    else
                        isMonitoring = false;

                    isMonitoredList.add(isMonitoring);
                }
                stmt.close();
                rs.close();
            }
            conn.close();
            return isMonitoredList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
