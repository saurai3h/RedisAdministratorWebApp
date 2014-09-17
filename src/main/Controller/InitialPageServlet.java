package Controller;

import Model.Instance;
import Model.Page;
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

        String curInstanceHostPort = request.getParameter("hostport");

        try {
            out = response.getWriter();
            Instance clickedInstance = ServletHelper.getInstanceFromServletContext(getServletContext(),curInstanceHostPort);
            Page firstPage = clickedInstance.getPageAtIndex(0);
            if(firstPage!=null) {
                String listOfKeys = new Gson().toJson(firstPage.getKeyList());
                out.write(listOfKeys);
            }
            else out.write("false");
        }
        catch (JedisException e)   {
            out.write("false");
        }
        catch (IOException e) {
            out.write("false");
        }
        finally {

            request.getSession().setAttribute("CurPageIndex", 0);
            request.getSession().setAttribute("clickedInstanceHostPort", curInstanceHostPort);
        }
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }
}
