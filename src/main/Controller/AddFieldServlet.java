package Controller;

import Model.Instance;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Saurabh Paliwal on 9/9/14.
 */
public class AddFieldServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            String clickedKey = request.getParameter("clickedKey");
            String field = request.getParameter("field");
            String value = request.getParameter("value");
            String type = request.getParameter("type");

            Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                    (String) request.getSession().getAttribute("clickedInstanceHostPort"));

            if(clickedInstance==null){
                System.out.println("instance not found!!");
            }
            if(clickedKey != null && value != null && type != null && field != null
                    && !clickedKey.isEmpty() && !value.isEmpty() && !type.isEmpty()&& !field.isEmpty() ) {
                if (!clickedInstance.keyExists(clickedKey)) {
                    out.write("doesNotExist");
                }
                else {
                    if(type.equals("zset")) {
                        try {
                            Double.parseDouble(value);
                        }
                        catch (NumberFormatException e) {
                            out.write("scoreNotDouble");
                            return;
                        }
                    }

                    if(clickedInstance.addField(clickedKey,field,value,type))
                        out.write("success");
                    else
                        out.write("existingFieldUpdated");
                }
            }
            else{
                out.write("KeyNull");
            }

        }
        catch (IOException e) {
            out.write("false");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }
}
