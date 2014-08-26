<%--
  Created by IntelliJ IDEA.
  User: saurabh
  Date: 21/8/14
  Time: 4:01 PM
  To change this template use File | Settings | File Templates.
--%>
<html>
<%@page import="Model.Login"%>
<%@ page import="java.util.Date" %>
<head>
    <title>
        Error in Login.
    </title>
    <link rel = "stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <link rel="stylesheet" type="text/css" href="css/alertify.default.css">
    <link rel="stylesheet" type="text/css" href="css/alertify.core.css">
    <link rel="stylesheet" type="text/css" href="css/alertify.bootstrap.css">
</head>
<body>

<div class = "jumbotron">
    <p>Sorry! Username or Password Error</p>
</div>

<%@ include file ="index.jsp" %>

<script src="js/alertify.js"></script>
<script src="js/alertify.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
</body>
</html>