package Model;
import java.sql.*;

public class Login {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/testJedis";
    static final String USER = "root";
    static final String PASS = "password";

    private String name,password;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean validate(){
        try {

            Class.forName(JDBC_DRIVER);

            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "SELECT PassWord FROM USERS WHERE UserName = \"" + name + "\"";
            ResultSet rs = stmt.executeQuery(sql);

            if(rs.wasNull())    {
                return false;
            }

            while(rs.next())    {
                if(rs.getString("PassWord").equals(password))return true;
            }

            conn.close();
            stmt.close();
            rs.close();

            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
