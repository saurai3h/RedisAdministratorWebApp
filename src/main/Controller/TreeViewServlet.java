package Controller;

import Model.Instance;
import Model.MakeTree;
import com.google.gson.Gson;
import redis.clients.jedis.exceptions.JedisException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Created by Saurabh Paliwal on 12/9/14.
 */
public class TreeViewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletHelper.redirectIfLoginInvalid(request,response);
        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            Instance clickedInstance = ServletHelper.getInstanceFromServletContext(getServletContext(),(String) request.getSession().getAttribute("clickedInstanceHostPort"));
            Set<String> keys = clickedInstance.allKeys();
            out.write(new Gson().toJson(MakeTree.makeTree(keys)));
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
