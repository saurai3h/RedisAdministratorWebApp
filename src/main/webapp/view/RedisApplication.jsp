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
    <link type="text/css" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">
    <link type="text/css" href="http://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css" rel="stylesheet">
    <link type="text/css" href="css/simple-sidebar.css" rel="stylesheet">
    <link type="text/css" href="css/simple-footer.css" rel="stylesheet">
    <link type="text/css" href="css/alertify.default.css" rel="stylesheet" >
    <link type="text/css" href="css/alertify.core.css" rel="stylesheet">
    <link type="text/css" href="css/alertify.bootstrap.css" rel="stylesheet">
    <link type="text/css" href="css/styles.css" rel="stylesheet">
    <link type="text/css" href="css/jquery.bonsai.css" rel="stylesheet">
    <link type="text/css"
          href="https://rawgit.com/Eonasdan/bootstrap-datetimepicker/master/build/css/bootstrap-datetimepicker.min.css" rel="stylesheet">

</head>

<body  style="width: 100%; height:100%">
<%
    response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    Login login = (Login) session.getAttribute("login");
    if(login == null){
        RequestDispatcher rd=request.getRequestDispatcher("login-error.jsp");
        rd.forward(request, response);
    }
%>

<div id="wrapper">
    <div id="sidebar-wrapper">
        <ul class="sidebar-nav" id = "listOfInstances">

        </ul>
    </div>
    <div class="sidebar-footer">
            <div class="input-group">
                <input type="text" class="form-control" id="host" placeholder="hostname" onchange="return hostValid()">
                <input type="text" class="form-control" id="port" placeholder="portnumber" onchange="return portValid()">
                <input type="text" class="form-control" id="visibleTo" placeholder="visible to">
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
                <ul id="list-header" class="pager" style="display:none">
                    <li class="previous"><a id="prev" href="#">&larr; Older</a></li>
                    <li class="next"><a id="next" href="#">Newer &rarr;</a></li>
                    <li><a id="reset-page-list" href="#">Reset &olarr;</a></li>
                    <li><a id="start-infoSnapshotter" href="#">Start Monitor</a></li>
                    <li>
                        <a id="show-info-button" href="#infoModal" data-toggle="modal">See Info</a>
                    </li>
                    <li><a id="stop-infoSnapshotter" href="#">Stop Monitor</a></li>
                    <li><a id = "treeView" data-toggle = "modal" href="#treeViewModal" style = "outline-style:none" >TreeView</a></li>
                </ul>

                <ul id="list-content">

                </ul>
            </div>

            <div id ="keys-details" class="col-lg-5" style="height: 100%;overflow-y: auto">
                <div id="keys-details-content">

                </div>
            </div>
        </div>

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
                            <input type="text" class="form-control" id="expiryAdd1" placeholder="enter expiry in seconds(-1 if not interested).">
                        </div>
                        <div class="btn-group">
                            <button style="font-size: 12px" id="Add1" type="button" class="btn btn-primary">Add</button>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab2">
                        <div class="input-group">
                            <input type="text" class="form-control" id="keyAdd2" placeholder="key">
                            <input type="text" class="form-control" id="valueAdd2" placeholder="value">
                            <input type="text" class="form-control" id="expiryAdd2" placeholder="enter expiry in seconds(-1 if not interested).">
                        </div>
                        <div class="btn-group">
                            <button style="font-size: 12px" id="Add2" type="button" class="btn btn-primary">Add</button>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab3">
                        <div class="input-group">
                            <input type="text" class="form-control" id="keyAdd3" placeholder="key">
                            <input type="text" class="form-control" id="valueAdd3" placeholder="value">
                            <input type="text" class="form-control" id="expiryAdd3" placeholder="enter expiry in seconds(-1 if not interested).">
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
                            <input type="text" class="form-control" id="expiryAdd4" placeholder="enter expiry in seconds(-1 if not interested).">
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
                            <input type="text" class="form-control" id="expiryAdd5" placeholder="enter expiry in seconds(-1 if not interested).">
                        </div>
                        <div class="btn-group">
                            <button style="font-size: 12px" id="Add5" type="button" class="btn btn-primary">Add</button>
                        </div>
                    </div>
                </div>
            </div>
            <div style="width:30%" id="searchOrDeleteKey" class="col-lg-2">
                <div class="input-group" style="width:100%">
                    <input type="text" title = "Search a key you want to be deleted or searched in the redis instance." class="form-control" id="keyDeleteSearch6" placeholder="key">
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

<div class="modal" id="stringModal" tabindex="-1" role="dialog" aria-labelledby="stringLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="stringModalLabel">Edit String</h4>
            </div>
            <div class="modal-body">
                Key : <span id = "stringKey" ></span> <br>
                Value : <textarea id = "stringValue"></textarea>
            </div>
            <div class="modal-footer">
                <button id = "stringClose" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary finalEditingString">Save changes</button>
            </div>
        </div>
    </div>
</div>
<div class="modal" id="setModal" tabindex="-1" role="dialog" aria-labelledby="setLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="setModalLabel">Edit Set</h4>
            </div>
            <div class="modal-body">
                Key : <span id = "setKey"></span> <br>
                Value : <textarea id = "setValue"></textarea>
            </div>
            <div class="modal-footer">
                <button id = "setClose" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary finalEditingSet">Save changes</button>
            </div>
        </div>
    </div>
</div>
<div class="modal" id="listModal" tabindex="-1" role="dialog" aria-labelledby="listLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="listModalLabel">Edit List</h4>
            </div>
            <div class="modal-body">
                Key : <span id = "listKey"></span> <br>
                Value : <textarea id = "listValue"></textarea>
            </div>
            <div class="modal-footer">
                <button id = "listClose" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary finalEditingList">Save changes</button>
            </div>
        </div>
    </div>
</div>
<div class="modal" id="hashModal" tabindex="-1" role="dialog" aria-labelledby="hashLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="hashModalLabel">Edit Hash</h4>
            </div>
            <div class="modal-body">
                Key : <span id = "hashKey"></span> <br>
                Field : <textarea id = "hashField"></textarea><br>
                Value : <textarea id = "hashValue"></textarea>
            </div>
            <div class="modal-footer">
                <button id = "hashClose" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary finalEditingHash">Save changes</button>
            </div>
        </div>
    </div>
</div>
<div class="modal" id="zsetModal" tabindex="-1" role="dialog" aria-labelledby="zsetLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="zsetModalLabel">Edit Zset</h4>
            </div>
            <div class="modal-body">
                Key : <span id = "zsetKey"></span> <br>
                Score : <textarea id = "zsetScore"></textarea><br>
                Value : <textarea id = "zsetValue"></textarea>
            </div>
            <div class="modal-footer">
                <button id = "zsetClose" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary finalEditingZset">Save changes</button>
            </div>
        </div>
    </div>
</div>

<div class="modal" id="infoModal" tabindex="-1" role="dialog" aria-labelledby="info" aria-hidden="true">
    <div class="modal-dialog" style = "width: 50%;height: 100%">
        <div class="modal-content" style="width: 100%;height: 100%">
            <div class="modal-header">
                <h4 class="modal-title" id="infoModelHeader">Info</h4>
            </div>
            <div class="modal-body" style="overflow-y:scroll; height:400px"  id = "info-body">

                        <div class='col-sm-12'>
                            <div class="form-group">
                                <div class='input-group date' id="fromDatePicker">
                                    <input type='text' class="form-control" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class='col-sm-12'>
                            <div class="form-group">
                                <div class='input-group date' id="toDatePicker">
                                    <input type='text' class="form-control" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </div>
                        </div>

                            

                <ul id = "chartTabList" class="nav nav-tabs" role="tablist">
                    <li><a style = "font-size: 12px;" href="#no_of_keys_chart" data-toggle="tab">No. of Keys</a></li>
                    <li><a style = "font-size: 12px;" href="#connected_clients_chart" data-toggle="tab">No. of clients</a></li>
                    <li><a style = "font-size: 12px;" href="#used_memory_chart" data-toggle="tab">Memory Used</a></li>
                    <li><a style = "font-size: 12px;" href="#no_of_expirable_keys_chart" data-toggle="tab">Expirable Keys</a></li>
                    <li><a style = "font-size: 12px;" href="#used_cpu_user_chart" data-toggle="tab">CPU Used</a></li>
                </ul>
                <div class="tab-content" id = "chart-tab-contents">
                    <div class="tab-pane" id="no_of_keys_chart"> </div>
                    <div class="tab-pane" id="connected_clients_chart"></div>
                    <div class="tab-pane" id="used_memory_chart"></div>
                    <div class="tab-pane" id="no_of_expirable_keys_chart"></div>
                    <div class="tab-pane" id="used_cpu_user_chart"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal" id="treeViewModal" tabindex="-1" role="dialog" aria-labelledby="treeViewLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="treeViewModalLabel">Tree view of keys in this instance.</h4>
            </div>
            <div class="modal-body">
                <ul id="treeViewContent">

                </ul>
            </div>
            <div class="modal-footer">
                <button id = "treeViewClose" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src="js/alertify.min.js"></script>
<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="js/jquery.bonsai.js"></script>
<script src = "http://eonasdan.github.io/bootstrap-datetimepicker/scripts/moment.js"></script>
<script src = "https://rawgit.com/Eonasdan/bootstrap-datetimepicker/master/src/js/bootstrap-datetimepicker.js"></script>
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
<script type="text/javascript">
    $(function () {
        $('#fromDatePicker').datetimepicker();
        $('#toDatePicker').datetimepicker();
        var elem = document.getElementsByClassName("bootstrap-datetimepicker-widget");
        $(elem).attr("style","margin-left:-62%");
    });
</script>
</body>

</html>