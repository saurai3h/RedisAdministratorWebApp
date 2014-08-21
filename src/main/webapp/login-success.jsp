<%--
  Created by IntelliJ IDEA.
  User: saurabh
  Date: 21/8/14
  Time: 3:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page import="RedisAdministratorClasses.LoginBean"%>
<%@ page import="java.util.Date" %>

<p>You are successfully logged in! at <%= new Date() %></p>

<%
    LoginBean bean=(LoginBean)request.getAttribute("bean");
    out.println("Welcome, " + bean.getName());
%>
