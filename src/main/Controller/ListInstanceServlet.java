package Controller;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import Model.*;
import com.google.gson.Gson;
import redis.clients.jedis.HostAndPort;

public class ListInstanceServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletHelper.redirectIfLoginInvalid(request,response);
        response.setContentType("text/html");
        PrintWriter out= null;
        Login login= (Login) request.getSession().getAttribute("login");
        try {
            String userName = login.getName();
            out = response.getWriter();

            ArrayList<HostAndPort> listOfHostAndPorts = InstanceHelper.getAllStoredInstances(userName);
            ArrayList<Boolean> listOfMonitored = InstanceHelper.getIsMonitored(listOfHostAndPorts);
            ArrayList<InstanceWrapper> instances = new ArrayList<InstanceWrapper>();

            int length = listOfHostAndPorts.size();

            for(int i = 0 ; i < length ; ++i){
                instances.add(new InstanceWrapper(listOfHostAndPorts.get(i),listOfMonitored.get(i)));
            }

            Gson gson = new Gson();
            String listOfInstances = gson.toJson(instances);
            out.write(listOfInstances);
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
