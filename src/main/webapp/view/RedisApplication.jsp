<%@ page import="Model.Login" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.google.gson.JsonArray" %>
<%@ page import="Model.InstanceHelper" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="redis.clients.jedis.HostAndPort" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Redis Application</title>
    <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">

    <link href="css/simple-sidebar.css" rel="stylesheet">

</head>

<body>
<%
    if(!(Boolean) session.getAttribute("hasLoadedLoginSuccessBefore")) {
        Login login = (Login) request.getAttribute("login");
        session.setAttribute("login", login);
    }
    else {
        out.println(request.getAttribute("message"));
    }
    Login login = (Login) session.getAttribute("login");
%>

<div id="wrapper">

    <div id="sidebar-wrapper">
        <ul class="sidebar-nav">
            <li class="sidebar-brand">
                <a href="#">
                    Listing Instances
                </a>
            </li>
        </ul>
    </div>
    <div class="sidebar-footer">
        <div class="input-group">
            <input type="text" class="form-control" id="host" onchange="return hostValid()">
            <span class="input-group-btn">
            <button class="btn btn-default" type="button">Add host</button>
            </span>
        </div>
    </div>
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div id = "list-display" class="col-lg-6">

                <div class="row">
                    <a href="#menu-toggle" class="btn btn-default" id="menu-toggle">Toggle ListView</a>
                </div>

                <ul id="list-content">

                </ul>
                <ul id="list-footer" class="pager">
                    <li class="previous"><a id="prev" href="#">&larr; Older</a></li>
                    <li class="next"><a id="next" href="#">Newer &rarr;</a></li>
                </ul>
            </div>
            <div id ="keys-details" class="col-lg-6">

            </div>
        </div>
    </div>

</div>

<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src="js/AjaxCalls.js"></script>
<script>
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
</script>
</body>

</html>