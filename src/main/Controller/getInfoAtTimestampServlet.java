package Controller;

import Model.Constants;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by kartik.k on 9/15/2014.
 */
public class getInfoAtTimestampServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletHelper.redirectIfLoginInvalid(request,response);
        String hostPort = request.getSession().getAttribute("clickedInstanceHostPort") + ":";
        String timeStamp = request.getParameter("timeStamp");
        response.setContentType("text/html");
        PrintWriter out = null;
        try {
            out = response.getWriter();

            Jedis jedis = new Jedis(Constants.INFO_STORE.getHost(), Constants.INFO_STORE.getPort());
            Map<String,String> infoAtTimestamp = jedis.hgetAll(hostPort+timeStamp);
            String infoJson = new Gson().toJson(infoAtTimestamp);
            out.write(infoJson);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }
}
