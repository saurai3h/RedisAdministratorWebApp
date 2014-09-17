package Controller;

import Model.Constants;
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
 * Created by kartik.k on 9/17/2014.
 */
public class GetCurrentInfoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletHelper.redirectIfLoginInvalid(request,response);
        response.setContentType("text/html");
        PrintWriter out= null;
        try {
            out = response.getWriter();
            Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                    (String) request.getSession().getAttribute("clickedInstanceHostPort"));
            if(clickedInstance==null){
                out.write(Constants.INSTANCE_NOT_FOUND_STATUS_CODE);
            }
            else {
                Map<String, String> map = clickedInstance.getCurrentInfo();
                if (map != null)
                    out.write(new Gson().toJson(map));
                else
                    out.write("doesNotExist");
            }
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
