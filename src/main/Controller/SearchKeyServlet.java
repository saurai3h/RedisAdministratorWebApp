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
public class SearchKeyServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        ServletHelper.redirectIfLoginInvalid(request,response);
        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            String key = request.getParameter("keyToSearch");
            Instance instance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                    (String) request.getSession().getAttribute("clickedInstanceHostPort"));

            if(key != null && !key.isEmpty()) {
                if (!instance.keyExists(key)) {
                    out.write("doesNotExist");
                } else {
                    instance.search(key);
                    String listOfKeys = new Gson().toJson(instance.getSearchPage().getKeyList());
                    out.write(listOfKeys);
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
