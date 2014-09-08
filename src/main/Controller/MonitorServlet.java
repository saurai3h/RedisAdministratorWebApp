package Controller;

import Model.Instance;
import com.google.gson.Gson;
import redis.clients.jedis.exceptions.JedisException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by kartik.k on 9/3/2014.
 */
public class MonitorServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        PrintWriter out= null;
        System.out.print("monitor mode is ");
        try {
            boolean shouldStartMonitor = Boolean.parseBoolean(request.getParameter("shouldStartMonitor"));
            Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                    (String) request.getSession().getAttribute("clickedInstanceHostPort"));
            System.out.println(shouldStartMonitor);
            if(shouldStartMonitor) {
                clickedInstance.startMonitor(1);
            }
            else {
                clickedInstance.stopMonitor();
            }
        }
        catch (JedisException e)   {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }
}