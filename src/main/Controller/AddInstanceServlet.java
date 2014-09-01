package Controller;

import Model.Instance;
import Model.InstanceHelper;
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

            InstanceHelper.add(new HostAndPort(host,Integer.parseInt(port)));

            RequestDispatcher rd = request.getRequestDispatcher("RedisApplication.jsp");
            try {
                rd.forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
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
