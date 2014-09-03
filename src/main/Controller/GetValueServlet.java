package Controller;

import Model.Instance;
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

            try {
                out = response.getWriter();
                String key  = request.getParameter("key");
                Instance clickedInstance = (Instance)request.getSession().getAttribute("instance");
                Map<String,String> map = clickedInstance.getJsonValueOfAKey(key);
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