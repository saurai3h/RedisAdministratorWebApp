package Controller;

import Model.Instance;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Saurabh Paliwal on 5/9/14.
 */
public class EditKeyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletHelper.redirectIfLoginInvalid(request,response);
        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            String oldName = (String)request.getParameter("keyToEdit");
            String newName = (String)request.getParameter("valueToEdit");

            Instance instance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                    (String) request.getSession().getAttribute("clickedInstanceHostPort"));
            if(oldName!=null && newName!=null && !oldName.isEmpty() && !newName.isEmpty()) {
                instance.renameKey(oldName,newName);
                out.write("true");
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
