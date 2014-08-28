<%@ page import="Model.Login" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="com.google.gson.JsonArray" %>
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
                <%
                    Gson gson = new Gson();
                    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

                    //Change this according to the database being used.
                    final String DB_URL = "jdbc:mysql://localhost/testJedis";
                    final String USER = "root";
                    final String PASS = "password";
                    Class.forName(JDBC_DRIVER);

                    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    Statement stmt = conn.createStatement();
                    String sql = "SELECT HostName,PortNumber FROM INSTANCES";
                    ResultSet rs = stmt.executeQuery(sql);
                    JsonArray jsonElements = new JsonArray();

                    while(rs.next())    {
                        String res = rs.getString("HostName");
                        res += ":";
                        res += rs.getString("PortNumber");

                        gson.toJson(res);
                    }
                    out.println(gson);
                %>
            <script>

            </script>
        </ul>
    </div>
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row">
                <div class="col-lg-12">
                    <a href="#menu-toggle" class="btn btn-default" id="menu-toggle">Toggle ListView</a>
                </div>
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
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
</script>
</body>

</html>