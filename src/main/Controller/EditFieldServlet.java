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
public class EditFieldServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            out.write("success");

        } catch (IOException e) {
            out.write("false");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }
}
