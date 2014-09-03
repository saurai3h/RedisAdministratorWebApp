/**
 * Created by saurabh on 1/9/14.
 */
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

$(".sidebar-nav").click(function(event) {

        var hostPort = event.target.id.toString();

        $.ajax(
            {
                url: "/view/RedisApplication2",
                type: "POST",
                data: "hostport=" + hostPort,
                success: function (strData) {
                    $("#list-content").remove();
                    var ul = document.createElement("ul");
                    $(ul).attr("id","list-content");
                    if (strData !== "false") {
                        var jsonData = jQuery.parseJSON(strData);
                        for (var x in jsonData) {

                            var li = document.createElement("li");
                            var link = document.createElement("a");
                            $(link).html(jsonData[x]);
                            $(link).attr("id", jsonData[x]);
                            $(link).attr("href", "#");
                            $(li).append(link);
                            $(ul).append(li);
                        }
                        $("#list-display").append(ul);
                    }
                    else {
                        console.log("No page to display");
                    }
                }
            }
        );

        $("#prev").click(function () {

                $.ajax(
                    {
                        url: "/view/Previous",
                        type: "POST",
                        data: "hostport=" + hostPort,
                        success: function (strData) {
                            $("#list-content").remove();
                            var ul = document.createElement("ul");
                            $(ul).attr("id","list-content");
                            if (strData !== "false") {
                                var jsonData = jQuery.parseJSON(strData);
                                for (var x in jsonData) {

                                    var li = document.createElement("li");
                                    var link = document.createElement("a");
                                    $(link).html(jsonData[x]);
                                    $(link).attr("id", jsonData[x]);
                                    $(link).attr("href", "#");
                                    $(li).append(link);
                                    $(ul).append(li);
                                }
                                $("#list-display").append(ul);
                            }
                            else {
                                console.log("No page to display");
                            }
                        }
                    }
                );

            }
        );

        $("#next").click(function () {

                $.ajax(
                    {
                        url: "/view/Next",
                        type: "POST",
                        data: "hostport=" + hostPort,
                        success: function (strData) {
                            $("#list-content").remove();
                            var ul = document.createElement("ul");
                            $(ul).attr("id","list-content");
                            if (strData !== "false") {
                                var jsonData = jQuery.parseJSON(strData);
                                for (var x in jsonData) {

                                    var li = document.createElement("li");
                                    var link = document.createElement("a");
                                    $(link).html(jsonData[x]);
                                    $(link).attr("id", jsonData[x]);
                                    $(link).attr("href", "#");
                                    $(li).append(link);
                                    $(ul).append(li);
                                }
                                $("#list-display").append(ul);
                            }
                            else {
                                console.log("No page to display");
                            }
                        }
                    }
                );

            }
        );
    }
);

$("#list-content").click(function(event) {
        Console.log("herr");
        var clickedKey = event.target.id.toString();
        alertify.alert("are you sure?");
        $.ajax(
            {

                url: "/view/getValueForAKey",
                type: "POST",
                dataType : "json",
                data: "key=" + clickedKey,
                success: function (jsonData) {
                    var type = jsonData["type"];
                    $("#value-container").remove();
                    var json = jQuery.parseJSON(jsonData["json"]);
                    var container;
                    var appendToContainer;
                    if(type ==="zset" || type ==="hash"){
                        container = document.createElement("table");
                        container.setAttribute("border","1");
                        container.setAttribute("style","width:100%");
                        var tr = document.createElement("tr");
                        var keyHead = document.createElement("th");
                        var valueHead = document.createElement("th");
                        if(type==="zset"){
                            $(keyHead).html("Member");
                            $(valueHead).html("Score");
                        }
                        else{
                            $(keyHead).html("Key");
                            $(valueHead).html("Value");
                        }
                        $(tr).append(keyHead);
                        $(tr).append(valueHead);
                        $(container).append(tr);

                        appendToContainer = function(key,value){
                            var tr = document.createElement("tr");
                            var keyCell = document.createElement("td");
                            var valueCell = document.createElement("td");
                            $(keyCell).html(key);
                            $(valueCell).html(value);
                            $(tr).append(keyCell);
                            $(tr).append(valueCell);
                            $(container).append(tr);
                        }
                    }
                    else{
                        container = document.createElement("ul");
                        appendToContainer = function(key,value){
                           var li = document.createElement("li");
                            $(li).html(value);
                            $(li).attr("id", value);
                            $(container).append(li);
                        }
                    }
                    container.setAttribute("id","value-container");
                    for (var x in json) {
                        appendToContainer(x,json[x]);
                    }

                    $("#keys-details").append(container);
                }
            }
        );


    }

);

$("#start-monitor").click()(function(){
    alertify.alert("starting");
    $.ajax(
        {
            url: "/view/monitor",
            type: "POST",
            data: "shouldStartMonitor="+true

        }
    );
});

$("#stop-monitor").click()(function(){
    alertify.alert("stopping");
    $.ajax(
        {
            url: "/view/monitor",
            type: "POST",
            data: "shouldStartMonitor="+false
        }
    );
});