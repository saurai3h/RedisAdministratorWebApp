<%@ page import="Model.Login" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en" style="width: 100%;height: 100%">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Redis Application</title>
    <link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/simple-sidebar.css" rel="stylesheet">
    <link href="css/simple-footer.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/alertify.default.css">
    <link rel="stylesheet" type="text/css" href="css/alertify.core.css">
    <link rel="stylesheet" type="text/css" href="css/alertify.bootstrap.css">
    <link rel="stylesheet" type="text/css" href="css/styles.css">

</head>

<body  style="width: 100%; height:100%">
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
                <input type="text" class="form-control" id="host" placeholder="hostname" onchange="return hostValid()">
                <input type="text" class="form-control" id="port" placeholder="portnumber" onchange="return portValid()">
            </div>
            <div class="btn-group" style="left:25%">
                <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
                    Add or Delete <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                    <li><a id = "addInstance" href="#" onclick="return validator()">Add</a></li>
                    <li class="divider"></li>
                    <li><a id = "deleteInstance" href="#" onclick="return validator()">Delete</a></li>
                </ul>
            </div>
    </div>
    <div id="page-content-wrapper">
        <div class="container-fluid" style="width:100%;height:100%">
            <div id = "list-display" class="col-lg-7" style="height:100%;overflow-y:auto">
                <ul id="list-header" class="pager">
                    <li class="previous"><a id="prev" href="#">&larr; Older</a></li>
                    <li class="next"><a id="next" href="#">Newer &rarr;</a></li>
                    <li><a id="reset-page-list" href="#">Reset &olarr;</a></li>
                    <li><a id="start-monitor" href="#">Start Monitor</a></li>
                    <li><a id="stop-monitor" href="#">Stop Monitor</a></li>
                </ul>

                <ul id="list-content">

                </ul>
            </div>

            <div id ="keys-details" class="col-lg-5" style="height: 100%;overflow-y: auto">

            </div>
        </div>

        <%--To be later uncommented--%>
        <%--<button class="btn btn-default" type="button" id="start-monitor">Start Monitor</button>--%>
        <%--<button class="btn btn-default" type="button" id="stop-monitor">Stop Monitor</button>--%>

    </div>
    <div class="list-footer">
        <div id="secondPanel" class="col-lg-7">
            <div style="width:65%" id="addKey" class="col-lg-10">
                <ul id = "listOfDataStructures" class="nav nav-tabs" role="tablist">
                    <li><a style = "font-size: 12px;" href="#tab1" data-toggle="tab">String</a></li>
                    <li><a style = "font-size: 12px;" href="#tab2" data-toggle="tab">List</a></li>
                    <li><a style = "font-size: 12px;" href="#tab3" data-toggle="tab">Set</a></li>
                    <li><a style = "font-size: 12px;" href="#tab4" data-toggle="tab">Hash</a></li>
                    <li><a style = "font-size: 12px;" href="#tab5" data-toggle="tab">SortSet</a></li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane" id="tab1">
                        <div class="input-group">
                            <input type="text" class="form-control" id="keyAdd1" placeholder="key">
                            <input type="text" class="form-control" id="valueAdd1" placeholder="value">
                        </div>
                        <div class="btn-group">
                            <button style="font-size: 12px" id="Add1" type="button" class="btn btn-primary">Add</button>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab2">
                        <div class="input-group">
                            <input type="text" class="form-control" id="keyAdd2" placeholder="key">
                            <input type="text" class="form-control" id="valueAdd2" placeholder="value">
                        </div>
                        <div class="btn-group">
                            <button style="font-size: 12px" id="Add2" type="button" class="btn btn-primary">Add</button>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab3">
                        <div class="input-group">
                            <input type="text" class="form-control" id="keyAdd3" placeholder="key">
                            <input type="text" class="form-control" id="valueAdd3" placeholder="value">
                        </div>
                        <div class="btn-group">
                            <button style="font-size: 12px" id="Add3" type="button" class="btn btn-primary">Add</button>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab4">
                        <div class="input-group">
                            <input type="text" class="form-control" id="keyAdd4" placeholder="key">
                            <input type="text" class="form-control" id="optionalValueAdd4" placeholder="field">
                            <input type="text" class="form-control" id="valueAdd4" placeholder="value">
                        </div>
                        <div class="btn-group">
                            <button style="font-size: 12px" id="Add4" type="button" class="btn btn-primary">Add</button>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab5">
                        <div class="input-group">
                            <input type="text" class="form-control" id="keyAdd5" placeholder="key">
                            <input type="text" class="form-control" id="optionalValueAdd5" placeholder="score">
                            <input type="text" class="form-control" id="valueAdd5" placeholder="value">
                        </div>
                        <div class="btn-group">
                            <button style="font-size: 12px" id="Add5" type="button" class="btn btn-primary">Add</button>
                        </div>
                    </div>
                </div>
            </div>
            <div style="width:30%" id="searchOrDeleteKey" class="col-lg-2">
                <div class="input-group" style="width:100%">
                    <input type="text" class="form-control" id="keyDeleteSearch6" placeholder="key">
                </div>
                <div class="btn-group">
                    <button style="width: 50%;font-size: 12px;" id="Delete6" type="button" class="btn btn-primary">Delete</button>

                    <button style="width: 50%;font-size: 12px;" id="Search6" type="button" class="btn btn-primary">Search</button>
                </div>
            </div>
        </div>
        <div id="thirdPanel" class="col-lg-5">
        </div>
    </div>
</div>

<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<script src="js/alertify.js"></script>
<script src="js/alertify.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src="js/1stpanelAjaxCalls.js"></script>
<script src="js/2ndpanelAjaxCalls.js"></script>
<script type="text/javascript">
    hostValid = function hostValidator()    {

        var validHostNameRegex = /^(?=.{1,255}$)[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?(?:\.[0-9A-Za-z](?:(?:[0-9A-Za-z]|-){0,61}[0-9A-Za-z])?)*\.?$/;

        var hostEntered = document.getElementById("host");

        if(hostEntered.value.match(validHostNameRegex))   {
            alertify.success("Allowed input");
            return true;
        }
        else
        {
            alertify.error("Invalid Hostname");
            return false;
        }
    };

    portValid = function portValidator()    {
        var validPortNumber = /^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$/;

        var portEntered = document.getElementById("port");

        if(portEntered.value.match(validPortNumber))   {
            alertify.success("Allowed input");
            return true;
        }
        else
        {
            alertify.error("Invalid Portnumber");
            return false;
        }
    };

    function validator()    {
        if(hostValid() && portValid())
            return true;
        else {
            alertify.alert("Invalid Server instance added or deleted.");
            return false;
        }
    }

</script>
</body>

</html>