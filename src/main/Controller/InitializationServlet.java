package Controller;

import Model.Instance;
import Model.SqlInterface;
import redis.clients.jedis.HostAndPort;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kartik.k on 9/5/2014.
 */
public class InitializationServlet extends HttpServlet{
    @Override
    public void init() throws ServletException {
        super.init();
        Map<String, Instance> instanceMap = new HashMap<String, Instance>();
        Connection conn = SqlInterface.getConnection();
        Statement stmt = null;
        Map<String,Boolean> hostAndPortMonitorMap = new HashMap<String, Boolean>();
        try {
            stmt = conn.createStatement();
            String sql = "SELECT HostName,PortNumber,IsMonitored  FROM instances;";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next())    {
                hostAndPortMonitorMap.put(rs.getString("HostName") + ":"
                        + rs.getString("PortNumber"),rs.getBoolean("IsMonitored"));
            }

            conn.close();
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(String hostAndPort:hostAndPortMonitorMap.keySet()){
            String host = hostAndPort.split(":")[0];
            int port = Integer.parseInt(hostAndPort.split(":")[1]);
            Instance instance = new Instance(host,port,hostAndPortMonitorMap.get(hostAndPort));
            instanceMap.put(hostAndPort,instance);
            System.out.println(host+"--"+Integer.toString(port));
        }
        getServletContext().setAttribute("instanceMap",instanceMap);
        System.out.println("initialization completed.");
    }


}
