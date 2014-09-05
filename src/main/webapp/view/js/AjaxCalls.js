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
$(document).off('click', '#addInstance').on('click', '#addInstance', function(){
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
$(document).off('click', '#deleteInstance').on('click', '#deleteInstance', function() {
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
$(document).off('click', '.sidebar-nav').on('click', '.sidebar-nav', function() {
        var ajaxCallForAddKey = function(buttonNo){

            var key = document.getElementById("keyAdd"+buttonNo.toString()).value.toString();
            var value = document.getElementById("valueAdd"+buttonNo.toString()).value.toString();
            var optionalInputBox = document.getElementById("optionalValueAdd"+buttonNo.toString());
            var optionalValue;
            if(optionalInputBox === null){
                optionalValue = "dummy";
            }
            else{
                optionalValue= optionalInputBox.value.toString();
            }
            var type;
            if(buttonNo === 1){
                type = "string";
            }
            else if(buttonNo === 2){
                type = "list";
            }
            else if(buttonNo === 3){
                type = "set";
            }else if(buttonNo === 4){
                type = "hash";
            }
            else if(buttonNo === 5){
                type = "zset";
            }


            alertify.alert(key + " " + value + " " + optionalValue);
            $.ajax(
                {
                    url: "/view/AddKey",
                    type: "POST",
                    data: "typeOfKey="+ type +"&nameOfKey="+key+
                        "&valueOfKey="+value+"&optionalValueOfKey="+optionalValue,
                    success: function (strData) {

                        if(strData === "existsAlready") {
                            alertify.alert("This key already exists.");
                        }
                        else if(strData === "invalidDataStructure") {
                            alertify.alert("Redis doesn't support this data structure");
                        }
                        else if(strData === "KeyNull")  {
                            alertify.alert("Redis entries not filled." + buttonNo);
                        }
                        else if(strData === "false")   {
                            alertify.alert("Sorry! Couldn't add. The server must be down.");
                        }
                        else if(strData === "success"){
                            alertify.alert("Success!!");
                        }
                        else{
                            alertify.alert("Some strange error!");
                        }
                    }
                }
            );
            document.getElementById("keyAdd3").value = "";
            document.getElementById("valueAdd3").value = "";
        };
        var populateKeyListFromJson = function(strData){
            $("#list-content").remove();
            var ul = document.createElement("ul");
            $(ul).attr("id","list-content");
            if (strData !== "false") {
                var jsonData = jQuery.parseJSON(strData);
                for (var x in jsonData) {

                    var li = document.createElement("li");
                    var link = document.createElement("a");
                    $(link).html("name: " + jsonData[x]["keyName"] + "; type: "
                        +jsonData[x]["type"]+ "; expiry date: "+jsonData[x]["expiryTime"]);
                    $(link).attr("id", jsonData[x]["keyName"]);
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
        var hostPort = event.target.id.toString();

        $.ajax(
            {
                url: "/view/RedisApplication2",
                type: "POST",
                data: "hostport=" + hostPort,
                success: function (strData) {
                    populateKeyListFromJson(strData);
                }
            }
        );

        $(document).off('click', '#Add1').on('click', '#Add1', function(){
                ajaxCallForAddKey(1);
            }
        );

        $(document).off('click', '#Add2').on('click', '#Add2', function(){
                ajaxCallForAddKey(2);
            }
        );

        $(document).off('click', '#Add3').on('click', '#Add3', function(){
                ajaxCallForAddKey(3);
            }
        );

        $(document).off('click', '#Add4').on('click', '#Add4',function(){
            ajaxCallForAddKey(4);
        } );

        $(document).off('click', '#Add5').on('click', '#Add5', function() {
                ajaxCallForAddKey(5);
            });



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
                            else  if(strData === "doesNotExist")  {
                                alertify.alert("Success!!");
                            }
                            else{
                                alertify.alert("Strange Error!! Aliens hacked into your DB!");
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
                                populateKeyListFromJson(strData);
                            }
                        }
                    }
                );
                document.getElementById("keyDeleteSearch6").value = "";
            }
        );

        $(document).off('click', '#prev').on('click', '#prev', function(){
                $.ajax(
                    {
                        url: "/view/Previous",
                        type: "POST",
                        data: "hostport=" + hostPort,
                        success: function (strData) {populateKeyListFromJson(strData);
                        }
                    }
                );

            }
        );

        $(document).off('click', '#next').on('click', '#next', function() {
                $.ajax(
                    {
                        url: "/view/Next",
                        type: "POST",
                        data: "hostport=" + hostPort,
                        success: function(strData) {
                            populateKeyListFromJson(strData);
                        }
                    }
                );

            }
        );
    }
);
$(document).off('click', '#list-content').on('click', '#list-content', function(){
        var clickedKey = event.target.id.toString();
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
$(document).off('click', '#start-monitor').on('click', '#start-monitor', function(){
    alertify.alert("starting");
    $.ajax(
        {
            url: "/view/monitor",
            type: "POST",
            data: "shouldStartMonitor="+true

        }
    );
});

$(document).off('click', '#stop-monitor').on('click', '#stop-monitor', function(){
    alertify.alert("stopping");
    $.ajax(
        {
            url: "/view/monitor",
            type: "POST",
            data: "shouldStartMonitor="+false
        }
    );
});