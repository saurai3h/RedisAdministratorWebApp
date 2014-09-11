package Controller;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import Model.*;
import com.google.gson.Gson;

public class ListInstanceServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");
        PrintWriter out= null;
        Login login= (Login) request.getSession().getAttribute("login");
        try {
            String userName = login.getName();
            out = response.getWriter();
            String listOfInstances = new Gson().toJson(InstanceHelper.getAllStoredInstances(userName));
            out.write(listOfInstances);
        }
        catch (IOException e) {
            out.write("false");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }
}
