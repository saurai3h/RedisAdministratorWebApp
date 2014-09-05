package Controller;

import Model.Instance;
import com.google.gson.Gson;

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
        System.out.println("adding keys..");
        try {
            out = response.getWriter();
            String type = (String)request.getParameter("typeOfKey");
            String key = (String)request.getParameter("nameOfKey");
            String value = (String)request.getParameter("valueOfKey");
            String optionalValue = (String)request.getParameter("optionalValueOfKey");

            System.out.println("type,key,val,optval = "+
            type+"," +key+","+value+","+optionalValue+",");
            Instance instance = (Instance)request.getSession().getAttribute("instance");

            if(key != null && value != null && type != null && optionalValue != null && !key.isEmpty() && !value.isEmpty() && !type.isEmpty()&& !optionalValue.isEmpty() ) {
                System.out.println("valid");
                if (instance.keyExists(key)) {
                    System.out.println("exists");
                    out.write("existsAlready");
                } else if (type.equals("string") || type.equals("set") || type.equals("list")) {
                    System.out.println("good");
                    instance.addKey(key, type, value);
                    out.write("success");
                } else if (type.equals("zset") || type.equals("hash")) {
                    System.out.println("goodie");
                    instance.addKey(key, type, value, optionalValue);
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
            System.out.println("exception");
            out.write("false");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }

}
