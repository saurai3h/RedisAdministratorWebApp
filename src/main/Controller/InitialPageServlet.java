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
 * Created by Saurabh Paliwal on 28/8/14.
 */
public class InitialPageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println (getServletContext().getAttribute("msg"));
        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();


            String rawHostPort = request.getParameter("hostport");

            String[] hostPort = rawHostPort.split(":");
            try {
                Instance clickedInstance = new Instance(hostPort[0],Integer.parseInt(hostPort[1]));
                request.getSession().setAttribute("instance",clickedInstance);
                String listOfKeys = new Gson().toJson(clickedInstance.getPageAtIndex(0).getKeyList());
                System.out.println(listOfKeys);
                out.write(listOfKeys);
                request.getSession().setAttribute("CurPageIndex",0);
                request.getSession().setAttribute("instance",clickedInstance);
            }
            catch (JedisException e)   {
                out.write("false");
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
