package Controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JedisHelper.InstancePage;
import JedisHelper.Page;
import Model.Login;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by kartik.k on 8/27/2014.
 */
public class FetchPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Jedis jedis = (Jedis) request.getAttribute("jedis");
        Page page = new InstancePage(jedis,25);

        request.setAttribute("page",page);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}

