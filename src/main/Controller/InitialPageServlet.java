package Controller;

import Model.Instance;
import com.google.gson.Gson;
import redis.clients.jedis.exceptions.JedisException;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saurabh Paliwal on 28/8/14.
 */
public class InitialPageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletHelper.redirectIfLoginInvalid(request,response);
        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            String curInstanceHostPort = request.getParameter("hostport");
            Instance clickedInstance = ServletHelper.getInstanceFromServletContext(getServletContext(),curInstanceHostPort);
            String listOfKeys = new Gson().toJson(clickedInstance.getPageAtIndex(0).getKeyList());
            out.write(listOfKeys);
            request.getSession().setAttribute("CurPageIndex",0);
            request.getSession().setAttribute("clickedInstanceHostPort",curInstanceHostPort);
        }
        catch (JedisException e)   {
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
