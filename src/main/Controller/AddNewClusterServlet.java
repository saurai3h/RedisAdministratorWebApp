package Controller;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import JedisClusterHelper.RedisClusterForRedisAdmin;
import Model.*;
import redis.clients.jedis.HostAndPort;

public class AddNewClusterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        String address=request.getParameter("Host Address");
        String port=request.getParameter("Port");
        String clusterName=request.getParameter("ClusterName");

        HostAndPort hostAndPort = new HostAndPort(address,Integer.parseInt(port));
        Set<HostAndPort> instanceSet = new HashSet<HostAndPort>();
        instanceSet.add(hostAndPort);
        RedisClusterForRedisAdmin redisClusterForRedisAdmin = new RedisClusterForRedisAdmin(instanceSet);
        boolean addClusterSuccessful;
        addClusterSuccessful = (redisClusterForRedisAdmin.getSetOfAllInstances().size() > 0);
        for(HostAndPort hostAndPortOfAll : redisClusterForRedisAdmin.getSetOfAllInstances()){
            InstanceAdder instanceAdder = new InstanceAdder(hostAndPortOfAll,clusterName);
            if(!instanceAdder.add()) {
                addClusterSuccessful = false;
                break;
            }
        }
        if(addClusterSuccessful)
            request.setAttribute("message","Cluster successfully added.");
        else
            request.setAttribute("message","Could not add cluster, maybe the cluster isn't up and running or maybe it already exists in our database!");
        RequestDispatcher rd=request.getRequestDispatcher("RedisApplication.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
