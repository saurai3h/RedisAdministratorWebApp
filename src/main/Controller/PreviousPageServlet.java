package Controller;

import Model.Instance;
import Model.Page;
import com.google.gson.Gson;
import redis.clients.jedis.exceptions.JedisException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Saurabh Paliwal on 28/8/14.
 */
public class PreviousPageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();

            String rawHostPort = request.getParameter("hostport");

            String[] hostPort = rawHostPort.split(":");
            try {
                System.out.println (getServletContext().getAttribute("msg"));

                Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                        (String) request.getSession().getAttribute("clickedInstanceHostPort"));
                Integer curPageIndex = (Integer) request.getSession().getAttribute("CurPageIndex");
                curPageIndex--;
                Page curPage =clickedInstance.getPageAtIndex(curPageIndex);
                if(curPage==null) {
                    curPageIndex++;
                    curPage = clickedInstance.getPageAtIndex(curPageIndex);
                }
                String listOfKeys = new Gson().toJson(curPage.getKeyList());
                out.write(listOfKeys);
                request.getSession().setAttribute("CurPageIndex",curPageIndex);
                System.out.println(curPageIndex);
            }
            catch (JedisException e)   {
                out.write("false");
            }
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
