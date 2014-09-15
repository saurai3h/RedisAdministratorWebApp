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
public class DeleteKeyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletHelper.redirectIfLoginInvalid(request,response);
        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            String key = request.getParameter("keyToDelete");
            System.out.println(key);
            Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                    (String) request.getSession().getAttribute("clickedInstanceHostPort"));
            if(clickedInstance==null){
                System.out.println("instance not found!!");
            }
            if(key != null && !key.isEmpty()) {
                if (!clickedInstance.keyExists(key)) {
                    out.write("doesNotExist");
                } else {
                    clickedInstance.deleteKey(key);
                    out.write("success");
                }
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
