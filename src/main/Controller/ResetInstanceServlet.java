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
 * Created by kartik.k on 9/8/2014.
 */
public class ResetInstanceServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("text/html");
        PrintWriter out= null;

        try {
            out = response.getWriter();
            try {
                Instance clickedInstance =ServletHelper.getInstanceFromServletContext(getServletContext(),
                        (String) request.getSession().getAttribute("clickedInstanceHostPort"));
                clickedInstance.resetPageList();
                Integer curPageIndex = (Integer) request.getSession().getAttribute("CurPageIndex");
                Page curPage =clickedInstance.getPageAtIndex(curPageIndex);
                while(curPage==null && curPageIndex>=0) {
                    curPageIndex--;
                    curPage = clickedInstance.getPageAtIndex(curPageIndex);
                }
                String listOfKeys = new Gson().toJson(curPage.getKeyList());
                out.write(listOfKeys);
                System.out.println(listOfKeys);
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
