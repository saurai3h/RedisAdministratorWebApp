<%--
  Created by IntelliJ IDEA.
  User: saurabh
  Date: 21/8/14
  Time: 3:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="Model.Login" %>
<%@ page import="java.util.Date" %>
<%@ page import="Model.ClusterAdder" %>
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


<div id="newClusterModal" class="modal show" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="position: relative; top: 100px">

            <div class="modal-header">
                <h1 class="text-center">Add New Cluster Here</h1>
            </div>

            <div class="modal-body">

                <form class="form col-md-12 center-block"  method="POST" action="addCluster">
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" placeholder="Host Address" name = "Host Address">
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" placeholder="Port" name = "Port">
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" placeholder="ClusterName"
                               name = "ClusterName">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" type="submit">Click to add this cluster to
                        your favorite list</button>
                    </div>
                    <%
                        session.setAttribute("hasLoadedLoginSuccessBefore",true);
                    %>
                </form>

            </div>
            <div class="modal-header">
                <h4 class="text-center">
                </h4>
            </div>

            <div class="modal-footer">
                <div class="col-md-12"></div>
            </div>
        </div>
    </div>

    <div class="col-md-offset-1">
        <table>
            <thead>List of clusters</thead>
            <tbody>
            <%
                List<String> listOfClusters = ClusterAdder.getAllStoredClusters();
                for(String clusterName:listOfClusters){
            %>
                <tr>
                    <td>
                        <%
                            out.println(clusterName);
                        %>
                    </td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>

</div>
<!-- script references -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap.js"></script>
<script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>

</body>
</html>
