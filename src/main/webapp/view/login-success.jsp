<%--
  Created by IntelliJ IDEA.
  User: saurabh
  Date: 21/8/14
  Time: 3:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="Model.Login" %>
<%@ page import="java.util.Date" %>
<%@ page import="Model.InstanceAdder" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="generator" content="Bootply" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>Redis Administrator</title>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap-theme.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap-theme.min.css">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>

<body>


<p>You are successfully logged in! at <%= new Date() %></p>

<%
    if(!(Boolean) session.getAttribute("hasLoadedLoginSuccessBefore")) {
        Login login = (Login) request.getAttribute("login");
        session.setAttribute("login", login);
    }
    else {
        out.println(request.getAttribute("message"));
    }
    Login login = (Login) session.getAttribute("login");
    out.println("Welcome, " + login.getName());
    out.println("Your password is, " + login.getPassword());
%>

<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap.js"></script>
<script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
</body>
</html>
