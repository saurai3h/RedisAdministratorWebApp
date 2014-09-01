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

$(".sidebar-nav").click(function() {

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



