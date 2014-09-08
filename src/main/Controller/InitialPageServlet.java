package Controller;

import Model.Instance;
import Model.InstanceHelper;
import com.google.gson.Gson;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.exceptions.JedisException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

/**
 * Created by Saurabh Paliwal on 28/8/14.
 */
public class InitialPageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();


            String rawHostPort = request.getParameter("hostport");

            String[] hostPort = rawHostPort.split(":");
            try {
                Instance clickedInstance = new Instance(hostPort[0],Integer.parseInt(hostPort[1]));
                //if(request.getSession().getAttribute("instance") == null)
                request.getSession().setAttribute("instance",clickedInstance);
                String listOfKeys = new Gson().toJson(clickedInstance.getCurrentPage().getKeyList());
                out.write(listOfKeys);
            }
            catch (JedisException e)   {
                out.write("false");
            }
        }
        catch (IOException e) {
            out.write("false");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }
}
