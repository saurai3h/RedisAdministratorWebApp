package Controller;

import Model.Instance;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Saurabh Paliwal on 4/9/14.
 */
public class keyTypeServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();


            String rawHostPort = request.getParameter("hostport");

            String[] hostPort = rawHostPort.split(":");
            try {
                Jedis jedis = new Jedis(hostPort[0],Integer.parseInt(hostPort[1]));

                Instance clickedInstance = (Instance)request.getSession().getAttribute("instance");
                ArrayList<String> keyList = clickedInstance.getCurrentPage().getKeyList();
                ArrayList<String> keyType = new ArrayList<String>();

                for(String key:keyList) {
                    String typeOfKey = jedis.type(key);
                    if(typeOfKey.equals("none"))
                        keyType.add("Deleted!!");
                    else keyType.add(typeOfKey);
                }

                String typeOfKeys = new Gson().toJson(keyType);
                out.write(typeOfKeys);
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
