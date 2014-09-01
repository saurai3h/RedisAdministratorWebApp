<%@ page import="java.util.List" %>
<%@ page import="Model.InstanceAdder" %>
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
<div id="newClusterModal" class="modal show" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="position: relative; top: 100px">

            <div class="modal-header">
                <h1 class="text-center">Add New Cluster Here</h1>
            </div>

            <div class="modal-body">

                <form class="form col-md-12 center-block" method="POST" action="addCluster">
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" placeholder="Host Address" name="Host Address">
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" placeholder="Port" name="Port">
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" placeholder="ClusterName"
                               name="ClusterName">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" type="submit">Click to add this cluster to
                            your favorite list
                        </button>
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





</div>
<div id="wrapper">

    <div id="sidebar-wrapper">
        <ul class="sidebar-nav">
            <li class="sidebar-brand">
                <a href="#">
                    Listing Instances
                </a>
            </li>
            <%
                List<String> listOfClusters = InstanceAdder.getAllStoredClusters();
                for(String clusterName:listOfClusters){
            %>
            <li id = "172.16.137.228:7000">
                <a href="#"><%
                    out.println(clusterName);
                %></a>;
            </li>
            <%
                }
            %>
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

<script>
    $(document).ready(function(){
        $("#driver").click(function(){
            $("#stage").load("<jsp:include page="/Controller/FetchPageServlet.java" flush="true" />");
        });
    });
</script>

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

<script>
    var ul = document.getElementsByTagName('ul');

</script>
</body>

</html>