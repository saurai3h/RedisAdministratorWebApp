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
public class DeleteFieldServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletHelper.redirectIfLoginInvalid(request,response);
        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            String key = request.getParameter("clickedKey");
            String field = request.getParameter("key");
            String value = request.getParameter("value");
            String type = request.getParameter("type");

            Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                    (String) request.getSession().getAttribute("clickedInstanceHostPort"));
            if(clickedInstance==null){
                System.out.println("instance not found!!");
            }

            if (clickedInstance.deleteField(key,field,value,type)) {
                out.write("success");
            } else {
                out.write("doesNotExist");
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
