package Controller;

import Model.Constants;
import Model.Instance;
import Model.InstanceHelper;
import Model.Login;
import redis.clients.jedis.HostAndPort;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Created by kartik.k on 9/15/2014.
 */
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {


        response.setContentType("text/html");
        PrintWriter out= null;
        Login login = (Login) request.getSession().getAttribute("login");
        request.getSession().invalidate();
        Map<String, Instance> instanceMap = (Map<String, Instance>) getServletContext().getAttribute("instanceMap");
        List<HostAndPort> visibleInstances = InstanceHelper.getAllVisibleInstances(login.getName());
        for (HostAndPort visibleHostPort:visibleInstances){
            Instance visibleInstance = instanceMap.get(visibleHostPort.toString());
            if(visibleInstance!=null){
                visibleInstance.resetPageList();
            }
        }

        try {
            response.sendRedirect("http://localhost:8080/view/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }
}