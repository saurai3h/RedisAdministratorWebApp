package Controller;

import Model.Instance;
import Model.InstanceHelper;
import Model.Login;
import Model.SqlInterface;
import com.google.gson.Gson;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.exceptions.JedisException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by Saurabh Paliwal on 1/9/14.
 */
public class DeleteInstanceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletHelper.redirectIfLoginInvalid(request,response);
        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            String host = request.getParameter("deleteThisHost");
            String port = request.getParameter("deleteThisPort");


            Login login = (Login) request.getSession().getAttribute("login");
            String userName = login.getName();
            boolean didDelete = InstanceHelper.hideHostPort(
                    new HostAndPort(host, Integer.parseInt(port)),userName);
            if(!SqlInterface.isVisibleToAnyUser(new HostAndPort(host,Integer.parseInt(port)))) {
                SqlInterface.deleteFromInstances(new HostAndPort(host,Integer.parseInt(port)));
                String hostPort = host + ":" + port;
                Map<String, Instance> instanceMap = (Map<String, Instance>) getServletContext().getAttribute("instanceMap");
                Instance instanceBeingDeleted = instanceMap.get(hostPort);
                instanceBeingDeleted.close();
                instanceMap.remove(hostPort);
                getServletContext().setAttribute("instanceMap", instanceMap);
            }

                out.write(Boolean.toString(didDelete));
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
