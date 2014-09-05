package Controller;

import Model.Instance;
import Model.Key;
import com.google.gson.Gson;
import redis.clients.jedis.exceptions.JedisException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by kartik.k on 9/1/2014.
 */
public class GetValueServlet extends HttpServlet{
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) {
            response.setContentType("text/html");
            PrintWriter out= null;
            System.out.println("servletting");
            try {
                out = response.getWriter();
                String key  = (String) request.getParameter("key");
                System.out.println(key + "is our key");
                Instance clickedInstance = (Instance)request.getSession().getAttribute("instance");
                System.out.println(clickedInstance.getHostAndPort().toString());
                Map<String,String> map = clickedInstance.getJsonValueOfAKey(key);
                System.out.println(new Gson().toJson(map));
                out.write(new Gson().toJson(map));
            }
            catch (IOException e) {
                out.write("false");
            }
            catch (JedisException e)   {
                out.write("false");
            }
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp){
            doPost(req, resp);
        }
}
