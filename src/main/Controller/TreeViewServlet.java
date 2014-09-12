package Controller;

import Model.Instance;
import com.google.gson.Gson;
import redis.clients.jedis.exceptions.JedisException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Saurabh Paliwal on 12/9/14.
 */
public class TreeViewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            Instance clickedInstance = ServletHelper.getInstanceFromServletContext(getServletContext(),(String) request.getSession().getAttribute("clickedInstanceHostPort"));

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
