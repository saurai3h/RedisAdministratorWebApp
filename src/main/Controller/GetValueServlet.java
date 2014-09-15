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
            ServletHelper.redirectIfLoginInvalid(request,response);
            response.setContentType("text/html");
            PrintWriter out= null;
            try {
                out = response.getWriter();
                String key  = request.getParameter("key");
                Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                        (String) request.getSession().getAttribute("clickedInstanceHostPort"));
                if(clickedInstance==null){
                    System.out.println("instance not found!!");
                }
                Map<String,String> map = clickedInstance.getJsonValueOfAKey(key);
                if(map != null)
                    out.write(new Gson().toJson(map));
                else
                    out.write("doesNotExist");
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
