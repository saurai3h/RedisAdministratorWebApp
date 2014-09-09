package Controller;

import Model.Instance;
import Model.InstanceHelper;
import redis.clients.jedis.HostAndPort;

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

            boolean didAdd = InstanceHelper.add(new HostAndPort(host,Integer.parseInt(port)));
            if(didAdd) {
                out.write("true");
                Map<String, Instance> instanceMap = (Map<String, Instance>) getServletContext().getAttribute("instanceMap");
                instanceMap.put((host+":"+port),new Instance(host,Integer.parseInt(port),false));
                getServletContext().setAttribute("instanceMap",instanceMap);
            }
            else
                out.write("false");
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
