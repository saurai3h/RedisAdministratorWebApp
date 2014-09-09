package Controller;

import Model.Instance;
import com.google.gson.Gson;
import redis.clients.jedis.HostAndPort;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

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
            String type = request.getParameter("typeOfKey");
            String key = request.getParameter("nameOfKey");
            String value = request.getParameter("valueOfKey");
            String optionalValue = request.getParameter("optionalValueOfKey");
            Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                    (String) request.getSession().getAttribute("clickedInstanceHostPort"));
            if(clickedInstance==null){
                System.out.println("instance not found!!");
            }
            if(key != null && value != null && type != null && optionalValue != null
                    && !key.isEmpty() && !value.isEmpty() && !type.isEmpty()&& !optionalValue.isEmpty() ) {
                System.out.println("valid");
                if (clickedInstance.keyExists(key)) {
                    //System.out.println("exists");
                    out.write("existsAlready");
                } else if (type.equals("string") || type.equals("set") || type.equals("list")) {
                    //System.out.println("good");
                    clickedInstance.addKey(key, type, value);
                    out.write("success");
                } else if (type.equals("zset") || type.equals("hash")) {
                    //System.out.println("goodie");
                    clickedInstance.addKey(key, type, value, optionalValue);
                    out.write("success");
                } else{
                    System.out.println(type + "invalid");
                    out.write("invalidDataStructure");
                }

            }
            else{
                System.out.println("null");
                out.write("KeyNull");
            }

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
