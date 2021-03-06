package Controller;

import Model.Instance;
import com.google.gson.Gson;
import com.mysql.jdbc.StringUtils;
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
        ServletHelper.redirectIfLoginInvalid(request,response);


        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            String type = request.getParameter("typeOfKey");
            String key = request.getParameter("nameOfKey");
            String value = request.getParameter("valueOfKey");
            String optionalValue = request.getParameter("optionalValueOfKey");
            String expiry = request.getParameter("expiryOfKey");

            Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                    (String) request.getSession().getAttribute("clickedInstanceHostPort"));
            if(clickedInstance==null){
                System.out.println("instance not found!!");
            }
            if(expiry!= null && key != null && value != null && type != null && optionalValue != null

                    && !expiry.isEmpty() && !key.isEmpty() && !value.isEmpty() && !type.isEmpty()&& !optionalValue.isEmpty() ) {

                try {
                    int val = Integer.parseInt(expiry);

                    if(!(val >=0 || val == -1))
                    {
                        out.write("expiryInvalid");
                        return;
                    }

                }
                catch(NumberFormatException e)    {
                    out.write("expiryInvalid");
                    return;
                }

                if (clickedInstance.keyExists(key)) {
                    //System.out.println("exists");
                    out.write("existsAlready");
                    return;
                }
                else if (type.equals("string") || type.equals("set") || type.equals("list")) {
                    //System.out.println("good");
                    clickedInstance.addKey(key, type, value, expiry);
                    out.write("success");
                } else if (type.equals("zset") || type.equals("hash")) {
                    //System.out.println("goodie");
                    if(type.equals("zset")) {
                        try {
                            Double.parseDouble(optionalValue);
                        }
                        catch (NumberFormatException e) {
                            out.write("scoreNotDouble");
                            return;
                        }
                    }
                    clickedInstance.addKey(key, type, value, optionalValue, expiry);
                    out.write("success");
                } else{
                    //System.out.println(type + "invalid");
                    out.write("invalidDataStructure");
                }

            }
            else{
                //System.out.println("null");
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
