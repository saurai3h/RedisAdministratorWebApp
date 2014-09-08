package Controller;

import Model.Instance;
import Model.Page;
import com.google.gson.Gson;
import redis.clients.jedis.exceptions.JedisException;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kartik.k on 9/8/2014.
 */
public class ResetInstanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");
        PrintWriter out= null;
        try {
            String clickedInstanceHostPort = (String) request.getSession().getAttribute("clickedInstanceHostPort");
            Map<String,Instance> instanceMap = (HashMap<String, Instance>)getServletContext().getAttribute("instanceMap");
            if(instanceMap==null){
                System.out.println("instance map not found");
            }
            Instance instanceToBeReset = instanceMap.get(clickedInstanceHostPort);
            instanceToBeReset.resetPageList();
            instanceMap.put(clickedInstanceHostPort,instanceToBeReset);
            Page curPage  = instanceToBeReset.getPageAtIndex((Integer) request.getSession().getAttribute("CurPageIndex"));
            String listOfKeys = new Gson().toJson(curPage.getKeyList());
            out.write(listOfKeys);
        }
        catch (JedisException e)   {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        doPost(req, resp);
    }
}
