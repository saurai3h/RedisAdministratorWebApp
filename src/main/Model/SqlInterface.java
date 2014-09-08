package Model;

import java.sql.*;

/**
 * Created by kartik.k on 9/4/2014.
 */
public class SqlInterface {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //Change this according to the database being used.
    static final String DB_URL = "jdbc:mysql://172.16.137.79/testjedis";

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

}
