package Controller;

import Model.Instance;
import Model.Login;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kartik.k on 9/8/2014.
 */
public class ServletHelper {
    public static void redirectIfLoginInvalid(HttpServletRequest request,HttpServletResponse response){
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        boolean loginInvalid = false;
        try {
            Login login = (Login) request.getSession().getAttribute("login");
            if(login==null){
                loginInvalid = true;
            }
            else {
                loginInvalid = !login.validate();
            }
        }
        catch (IllegalStateException loggedOut){
            loginInvalid = true;
        }

        if(loginInvalid){
            try {
                RequestDispatcher rd=request.getRequestDispatcher("index.jsp");
                rd.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Instance getInstanceFromServletContext(ServletContext servletContext,String curInstanceHostPort) {

        Map<String,Instance> instanceMap = (HashMap<String, Instance>)servletContext.getAttribute("instanceMap");
        if(instanceMap==null){
            System.out.println("instance map not found");
        }
        return instanceMap.get(curInstanceHostPort);
    }
}
