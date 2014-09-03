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
$("#addInstance").click(function () {
    var host = document.getElementById("host").value.toString();
    var port = document.getElementById("port").value.toString();
    $.ajax(
        {
            url: "/view/Add",
            type: "POST",
            data: "addThisHost="+host+"&addThisPort="+port,
            success: function (strData)    {
                if(strData === "true")
                var li = document.createElement("li");
                var link = document.createElement("a");
                $(link).html(host+":"+port);
                $(link).attr("id", host+":"+port);
                $(link).attr("href", "#");
                $(li).append(link);
                $("#sidebar-wrapper").find("ul").append(li);
            }
        }
    );
});
$("#deleteInstance").click(function () {
    var host = document.getElementById("host").value.toString();
    var port = document.getElementById("port").value.toString();
    $.ajax(
        {
            url: "/view/Delete",
            type: "POST",
            data: "deleteThisHost="+host+"&deleteThisPort="+port,
            success: function (strData)    {
                if(strData === "true")

                var id = host + ":" + port;
                var toRemove = document.getElementById(id);

                $(toRemove).parent().remove();
            }
        }
    );
});
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

        $(document).off('click', '#Add1').on('click', '#Add1', function(){

                var key = document.getElementById("keyAdd1").value.toString();
                var value = document.getElementById("valueAdd1").value.toString();

                $.ajax(
                    {
                        url: "/view/AddKey",
                        type: "POST",
                        data: "typeOfKey=string"+"&nameOfKey="+key+"&valueOfKey="+value+"&optionalValueOfKey=dummy",

                        success: function (strData) {

                            if (strData === "existsAlready") {
                                alertify.alert("This key already exists.");
                            }
                            else if (strData === "invalidDataStructure") {
                                alertify.alert("Redis doesn't support this data structure");
                            }
                            else if (strData === "KeyNull") {
                                alertify.alert("Redis entries not filled.");
                            }
                            else if (strData === "false") {
                                alertify.alert("Sorry! Couldn't add. The server must be down.");
                            }
                            else    {
                                $("#list-content").remove();
                                var ul = document.createElement("ul");
                                $(ul).attr("id","list-content");
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
                        }
                    }
                );
                document.getElementById("keyAdd1").value = "";
                document.getElementById("valueAdd1").value = "";
            }
        );

        $(document).off('click', '#Add2').on('click', '#Add2', function(){

                var key = document.getElementById("keyAdd2").value.toString();
                var value = document.getElementById("valueAdd2").value.toString();

                $.ajax(
                    {
                        url: "/view/AddKey",
                        type: "POST",
                        data: "typeOfKey=list"+"&nameOfKey="+key+"&valueOfKey="+value+"&optionalValueOfKey=dummy",
                        success: function (strData) {

                            if(strData === "existsAlready") {
                                alertify.alert("This key already exists.");
                            }
                            else if(strData === "invalidDataStructure") {
                                alertify.alert("Redis doesn't support this data structure");
                            }
                            else if(strData === "KeyNull")  {
                                alertify.alert("Redis entries not filled.");
                            }
                            else if(strData === "false")   {
                                alertify.alert("Sorry! Couldn't add. The server must be down.");
                            }
                            else    {
                                $("#list-content").remove();
                                var ul = document.createElement("ul");
                                $(ul).attr("id","list-content");
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
                        }
                    }
                );
                document.getElementById("keyAdd2").value = "";
                document.getElementById("valueAdd2").value = "";
            }
        );

        $(document).off('click', '#Add3').on('click', '#Add3', function() {

                var key = document.getElementById("keyAdd3").value.toString();
                var value = document.getElementById("valueAdd3").value.toString();

                $.ajax(
                    {
                        url: "/view/AddKey",
                        type: "POST",
                        data: "typeOfKey=set"+"&nameOfKey="+key+"&valueOfKey="+value+"&optionalValueOfKey=dummy",
                        success: function (strData) {

                            if(strData === "existsAlready") {
                                alertify.alert("This key already exists.");
                            }
                            else if(strData === "invalidDataStructure") {
                                alertify.alert("Redis doesn't support this data structure");
                            }
                            else if(strData === "KeyNull")  {
                                alertify.alert("Redis entries not filled.");
                            }
                            else if(strData === "false")   {
                                alertify.alert("Sorry! Couldn't add. The server must be down.");
                            }
                            else    {
                                $("#list-content").remove();
                                var ul = document.createElement("ul");
                                $(ul).attr("id","list-content");
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
                        }
                    }
                );
                document.getElementById("keyAdd3").value = "";
                document.getElementById("valueAdd3").value = "";
            }
        );

        $(document).off('click', '#Add4').on('click', '#Add4', function(){

                var key = document.getElementById("keyAdd4").value.toString();
                var value = document.getElementById("valueAdd4").value.toString();
                var field = document.getElementById("fieldAdd4").value.toString();

                $.ajax(
                    {
                        url: "/view/AddKey",
                        type: "POST",
                        data: "typeOfKey=hashMap"+"&nameOfKey="+key+"&valueOfKey="+field+"&optionalValueOfKey="+value,
                        success: function (strData) {

                            if(strData === "existsAlready") {
                                alertify.alert("This key already exists.");
                            }
                            else if(strData === "invalidDataStructure") {
                                alertify.alert("Redis doesn't support this data structure");
                            }
                            else if(strData === "KeyNull")  {
                                alertify.alert("Redis entries not filled.");
                            }
                            else if(strData === "false")   {
                                alertify.alert("Sorry! Couldn't add. The server must be down.");
                            }
                            else    {
                                $("#list-content").remove();
                                var ul = document.createElement("ul");
                                $(ul).attr("id","list-content");
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
                        }
                    }
                );
                document.getElementById("keyAdd4").value = "";
                document.getElementById("valueAdd4").value = "";
                document.getElementById("fieldAdd4").value = "";
            }
        );

        $(document).off('click', '#Add5').on('click', '#Add5', function() {

                var key = document.getElementById("keyAdd5").value.toString();
                var value = document.getElementById("valueAdd5").value.toString();
                var score = document.getElementById("scoreAdd5").value.toString();

                $.ajax(
                    {
                        url: "/view/AddKey",
                        type: "POST",
                        data: "typeOfKey=sortedSet"+"&nameOfKey="+key+"&valueOfKey="+score+"&optionalValueOfKey="+value,
                        success: function (strData) {

                            if(strData === "existsAlready") {
                                alertify.alert("This key already exists.");
                            }
                            else if(strData === "invalidDataStructure") {
                                alertify.alert("Redis doesn't support this data structure");
                            }
                            else if(strData === "KeyNull")  {
                                alertify.alert("Redis entries not filled.");
                            }
                            else if(strData === "false")   {
                                alertify.alert("Sorry! Couldn't add. The server must be down.");
                            }
                            else    {
                                $("#list-content").remove();
                                var ul = document.createElement("ul");
                                $(ul).attr("id","list-content");
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
                        }
                    }
                );
                document.getElementById("keyAdd5").value = "";
                document.getElementById("valueAdd5").value = "";
                document.getElementById("scoreAdd5").value = "";
            }
        );

        $(document).off('click', '#Delete6').on('click', '#Delete6', function() {

                var key = document.getElementById("keyDeleteSearch6").value.toString();

                $.ajax(
                    {
                        url: "/view/DeleteKey",
                        type: "POST",
                        data: "keyToDelete="+key,
                        success: function (strData) {

                            if(strData === "doesNotExist") {
                                alertify.alert("The key does not exist!");
                            }
                            else if(strData === "keyNull")  {
                                alertify.alert("Redis entries not filled.")
                            }
                            else if(strData === "false")    {
                                alertify.alert("Sorry! Couldn't add. The server must be down.");
                            }
                            else    {
                                $("#list-content").remove();
                                var ul = document.createElement("ul");
                                $(ul).attr("id","list-content");
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
                        }
                    }
                );
                document.getElementById("keyDeleteSearch6").value = "";
            }
        );

        $(document).off('click', '#Search6').on('click', '#Search6', function() {

                var key = document.getElementById("keyDeleteSearch6").value.toString();

                $.ajax(
                    {
                        url: "/view/SearchKey",
                        type: "POST",
                        data: "keyToSearch="+key,
                        success: function (strData) {

                            if(strData === "doesNotExist") {
                                alertify.alert("The key does not exist!");
                            }
                            else if(strData === "keyNull")  {
                                alertify.alert("Redis entries not filled.");
                            }
                            else if(strData === "false")   {
                                alertify.alert("Sorry! Couldn't add. The server must be down.");
                            }
                            else    {
                                $("#list-content").remove();
                                var ul = document.createElement("ul");
                                $(ul).attr("id","list-content");
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
                        }
                    }
                );
                document.getElementById("keyDeleteSearch6").value = "";
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



