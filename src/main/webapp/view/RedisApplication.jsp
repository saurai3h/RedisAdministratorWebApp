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
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row">
                <div class="col-lg-12">
                    <a href="#menu-toggle" class="btn btn-default" id="menu-toggle">Toggle ListView</a>
                </div>
            </div>
            <div id = "list-display">
                <ul>

                </ul>
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

<script>

    var arr = new Array();

    $.ajax(
            {
                url: "/view/RedisApplication",
                type: "POST",
                success: function( strData ){

                    if(strData !== "false") {

                        var jsonData = jQuery.parseJSON(strData);

                        for (var i = 0; i < jsonData.length; ++i) {
                            arr[i] = jsonData[i].host + ":" + jsonData[i].port;
                        }
                        var counter = 0;
                        for(var x in arr)   {
                            var li = document.createElement("li");
                            var link = document.createElement("a");
                            $(link).html(arr[x]);
                            $(link).attr("id",arr[x]);
                            $(link).attr("href","#");
                            $(li).append(link);
                            $("#sidebar-wrapper").find("ul").append(li);
                        }
                    }
                }
            }
    );

    $(".sidebar-nav").click(function() {

                $.ajax(
                        {
                            url: "/view/RedisApplication2",
                            type: "POST",
                            data: "hostport="+event.target.id.toString(),
                            success: function( strData ){
                                $("#page-content-wrapper").find("ul").remove();
                                $("#page-content-wrapper").append(document.createElement("ul"));
                                if(strData !== "false") {
                                    var counter = 0;
                                    var jsonData = jQuery.parseJSON(strData);
                                    for(var x in jsonData)   {

                                        var li = document.createElement("li");
                                        var link = document.createElement("a");
                                        $(link).html(jsonData[x]);
                                        $(link).attr("id",jsonData[x]);
                                        $(link).attr("href","#");
                                        $(li).append(link);
                                        $("#page-content-wrapper").find("ul").append(li);
                                    }
                                }
                                else
                                    console.log("No page to display");
                            }
                        }
                );

            }
    );

    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
</script>

</body>

</html>