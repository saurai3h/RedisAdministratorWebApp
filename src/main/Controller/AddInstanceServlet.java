package Controller;

import Model.Constants;
import Model.Instance;
import Model.InstanceHelper;
import Model.Login;
import redis.clients.jedis.HostAndPort;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by Saurabh Paliwal on 1/9/14.
 */
public class AddInstanceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            String host = request.getParameter("addThisHost");
            String port = request.getParameter("addThisPort");
            String userNameOfAdder = request.getParameter("visibleTo");
            Login login = (Login) request.getSession().getAttribute("login");
            String status = InstanceHelper.add(new HostAndPort(host,
                    Integer.parseInt(port)),userNameOfAdder,login.getName() );
            if(status.equals(Constants.SUCCESS_STATUS_CODE)) {
                Map<String, Instance> instanceMap = (Map<String, Instance>) getServletContext().getAttribute("instanceMap");
                instanceMap.put((host+":"+port),new Instance(host,Integer.parseInt(port),false));
                getServletContext().setAttribute("instanceMap",instanceMap);
            }

            out.write(status);
        }
        catch (IOException e) {
            System.out.println("weird exception in out.write");
            out.write(Constants.SERVLET_ERROR_CODE);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }
}
