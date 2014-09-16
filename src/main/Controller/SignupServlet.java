package Controller;

import Model.Login;
import Model.SqlInterface;
import com.mysql.jdbc.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Saurabh Paliwal on 16/9/14.
 */
public class SignupServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out=response.getWriter();

        String name=request.getParameter("User");
        String password=request.getParameter("Pass");

        if(StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(password))  {
            RequestDispatcher rd = request.getRequestDispatcher("SignupError.jsp");
            rd.forward(request,response);
        }
        else {
            try {
                Connection conn = SqlInterface.getConnection();
                Statement stmt = conn.createStatement();

                String sql = "INSERT INTO users (UserName,PassWord) VALUES" +
                        "(\"" + name + "\", \"" +
                        password + "\")";

                boolean wasQueryExecutedSuccessfully = stmt.execute(sql);
                conn.close();
                stmt.close();
                RequestDispatcher rd = request.getRequestDispatcher("SignupSuccessful.jsp");
                rd.forward(request,response);
            }
            catch (SQLException e)  {
                e.printStackTrace();
                RequestDispatcher rd = request.getRequestDispatcher("UserExists.jsp");
                rd.forward(request,response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
