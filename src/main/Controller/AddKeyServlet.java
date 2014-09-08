package Controller;

import Model.Instance;
import Model.InstanceHelper;
import com.google.gson.Gson;
import redis.clients.jedis.HostAndPort;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Saurabh Paliwal on 1/9/14.
 */
public class AddKeyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            String type = (String)request.getParameter("typeOfKey");
            String key = (String)request.getParameter("nameOfKey");
            String value = (String)request.getParameter("valueOfKey");
            String optionalValue = (String)request.getParameter("optionalValueOfKey");

            Instance instance = (Instance)request.getSession().getAttribute("instance");

            if(key != null && value != null && type != null && optionalValue != null && !key.isEmpty() && !value.isEmpty() && !type.isEmpty()&& !optionalValue.isEmpty() ) {
                if (instance.keyExists(key)) {
                    out.write("existsAlready");
                } else if (type.equals("string") || type.equals("set") || type.equals("list")) {
                    instance.addKey(key, type, value);
                    String listOfKeys = new Gson().toJson(instance.getCurrentPage().getKeyList());
                    out.write(listOfKeys);
                } else if (type.equals("sortedSet") || type.equals("hashMap")) {
                    instance.addKey(key, type, value, optionalValue);
                    String listOfKeys = new Gson().toJson(instance.getCurrentPage().getKeyList());
                    out.write(listOfKeys);
                } else
                    out.write("invalidDataStructure");
            }
            else
                out.write("keyNull");
        }
        catch (IOException e) {
            out.write("false");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }

}
