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
 * Created by Saurabh Paliwal on 11/9/14.
 */
public class AutoCompleteServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            Instance clickedInstance = ServletHelper.getInstanceFromServletContext(getServletContext(),(String) request.getSession().getAttribute("clickedInstanceHostPort"));

            String listOfKeys = new Gson().toJson(clickedInstance.myScan(2));
            out.write(listOfKeys);
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
