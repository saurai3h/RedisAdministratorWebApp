package Controller;

import Model.Instance;
import Model.Login;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Saurabh Paliwal on 9/9/14.
 */
public class EditFieldServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            String clickedKey = request.getParameter("clickedKey");
            String key = request.getParameter("key");
            String value = request.getParameter("value");
            String type = request.getParameter("type");
            String newKey = request.getParameter("newKey");
            String newValue = request.getParameter("newValue");

            //System.out.println(clickedKey + key + value + type);

            Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                    (String) request.getSession().getAttribute("clickedInstanceHostPort"));

            if(clickedInstance==null){
                System.out.println("instance not found!!");
            }
            if(clickedKey!= null && value != null && type != null && key != null && newKey != null && newKey != null
                    && !clickedKey.isEmpty() && !value.isEmpty() && !type.isEmpty()&& !key.isEmpty() && !newKey.isEmpty() && !newValue.isEmpty() ) {

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

                    clickedInstance.editField(clickedKey, key, value, newKey, newValue, type);
                    out.write("success");
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }
}
