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
        Map<HostAndPort, Instance> instanceMap = new HashMap<HostAndPort, Instance>();
        Connection conn = SqlInterface.getConnection();
        Statement stmt = null;
        List<HostAndPort> hostAndPortList = new ArrayList<HostAndPort>();
        try {
            stmt = conn.createStatement();
            String sql = "SELECT HostName,PortNumber  FROM instances;";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next())    {
                hostAndPortList.add(new HostAndPort(rs.getString("HostName"), Integer.parseInt(rs.getString("PortNumber"))));
            }

            conn.close();
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(HostAndPort hostAndPort:hostAndPortList){
            Instance instance = new Instance(hostAndPort.getHost(),hostAndPort.getPort());
            instanceMap.put(hostAndPort,instance);
        }
        getServletContext().setAttribute("instanceMap",instanceMap);
    }
}
