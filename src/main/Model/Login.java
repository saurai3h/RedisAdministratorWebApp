package Model;
import java.sql.*;

public class Login {


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
        if(name == null || password == null){
            return false;
        }
        try {
            Connection conn = SqlInterface.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT PassWord FROM users WHERE UserName = \"" + name + "\"";
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
        }
    }
}
