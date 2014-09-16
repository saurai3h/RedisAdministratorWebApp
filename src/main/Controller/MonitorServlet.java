package Controller;

import Model.Instance;
import redis.clients.jedis.exceptions.JedisException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by kartik.k on 9/3/2014.
 */
public class MonitorServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        PrintWriter out= null;
        try {
            out = response.getWriter();
            boolean shouldStartMonitor = Boolean.parseBoolean(request.getParameter("shouldStartMonitor"));
            Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                    (String) request.getSession().getAttribute("clickedInstanceHostPort"));

            if(shouldStartMonitor) {
                clickedInstance.startMonitor();
                String hostAndPort = clickedInstance.getHostAndPort().getHost() + ":" + String.valueOf(clickedInstance.getHostAndPort().getPort());
                out.write(hostAndPort);
            }
            else {
                clickedInstance.stopMonitor();
                String hostAndPort = clickedInstance.getHostAndPort().getHost() + ":" + String.valueOf(clickedInstance.getHostAndPort().getPort());
                out.write(hostAndPort);
            }
        }
        catch (JedisException e)   {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }
}
