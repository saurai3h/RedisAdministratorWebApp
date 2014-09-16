package Model;

import redis.clients.jedis.HostAndPort;

import java.sql.*;

/**
 * Created by kartik.k on 9/4/2014.
 */
public class SqlInterface {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://" + Constants.SQL_DB_HOST + "/testjedis";

    static final String USER = "root";
    static final String PASS = "password";
    public static Connection getConnection(){
        try {

            Class.forName(JDBC_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            return connection;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean addToInstances(HostAndPort hostAndPort) {
        try {
            Connection conn = getConnection();
            PreparedStatement addInstanceStatement;
            addInstanceStatement = conn.prepareStatement(
                    "insert into instances (HostName,PortNumber,IsMonitored) VALUES(?,?,?);");
            addInstanceStatement.setString(1, hostAndPort.getHost());
            addInstanceStatement.setInt(2, hostAndPort.getPort());
            addInstanceStatement.setBoolean(3, false);
            addInstanceStatement.execute();
            conn.close();
            addInstanceStatement.close();
            return true;

       } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFromInstances(HostAndPort hostAndPort) {

        try {

            Connection conn = getConnection();
            String sql = "delete from instances where HostName = ? and PortNumber = ?;";
            PreparedStatement deleteFromInstances = conn.prepareStatement(sql);
            deleteFromInstances.setString(1,hostAndPort.getHost());
            deleteFromInstances.setInt(2,hostAndPort.getPort());
            deleteFromInstances.execute();
            deleteFromInstances.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println("sql exception in delete from instance");
            return false;
        }
    }

    public static boolean deleteFromVisibleInstances(HostAndPort hostAndPort, String userName){
        try {
            Connection connection = getConnection();
            PreparedStatement deleteFromVisibleInstance = connection.prepareStatement(
                    "delete from visibleinstances where HostName = ? and PortNumber = ? and UserName = ?;");
            deleteFromVisibleInstance.setString(1,hostAndPort.getHost());
            deleteFromVisibleInstance.setInt(2,hostAndPort.getPort());
            deleteFromVisibleInstance.setString(3,userName);
            deleteFromVisibleInstance.execute();
            deleteFromVisibleInstance.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addVisibility(String userName, HostAndPort instanceHostPort){
        Connection connection = getConnection();
        try {
            PreparedStatement addVisibilityStmt = connection.prepareStatement(
                    "insert into visibleinstances (UserName,HostName,PortNumber) VALUES(?,?,?)");
            addVisibilityStmt.setString(1,userName);
            addVisibilityStmt.setString(2,instanceHostPort.getHost());
            addVisibilityStmt.setInt(3, instanceHostPort.getPort());
            addVisibilityStmt.execute();
            addVisibilityStmt.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isVisibleToAnyUser(HostAndPort hostAndPort){

        try {
            Connection connection = getConnection();
            PreparedStatement checkIfThisWasLastObserver = connection.prepareStatement
                    ("SELECT * FROM visibleinstances WHERE HostName = ? AND PortNumber = ?;");
            checkIfThisWasLastObserver.setString(1,hostAndPort.getHost());
            checkIfThisWasLastObserver.setInt(2,hostAndPort.getPort());
            ResultSet resultSet =  checkIfThisWasLastObserver.executeQuery();
            boolean isVisible = resultSet.next();
            resultSet.close();
            checkIfThisWasLastObserver.close();
            connection.close();
            return isVisible;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean isPresentInInstances(HostAndPort hostAndPort){
        try {
            Connection connection = getConnection();
            PreparedStatement occurrencesOfHostPortInInstance = connection.prepareStatement
                    ("SELECT * FROM instances WHERE HostName = ? AND PortNumber = ?;");
            occurrencesOfHostPortInInstance.setString(1, hostAndPort.getHost());
            occurrencesOfHostPortInInstance.setInt(2, hostAndPort.getPort());
            ResultSet resultSet =  occurrencesOfHostPortInInstance.executeQuery();
            boolean isPresent = resultSet.next();
            resultSet.close();
            occurrencesOfHostPortInInstance.close();
            connection.close();
            return isPresent;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
