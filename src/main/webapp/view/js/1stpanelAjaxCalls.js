/**
 * Created by saurabh on 1/9/14.
 */
var arr = new Array();
var repopulateInstanceList = function(){
    var listOfInstances = $("#listOfInstances");
    if(listOfInstances!==null) {
        listOfInstances.remove();
    }
    listOfInstances = document.createElement("ul");
    $(listOfInstances).attr("id","listOfInstances");
    $(listOfInstances).attr("class","sidebar-nav");
    var header = document.createElement("li");
    $(header).attr("class","sidebar-brand");
    var link = document.createElement("a");
    $(link).attr("href","#");
    $(link).html("Listing Instances");
    $(header).append(link);
    $(listOfInstances).append(header);
    var sidebarWrapper = $("#sidebar-wrapper");
    sidebarWrapper.append(listOfInstances);

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
                        var deleteLink = document.createElement("button");
                        var monitorLink = document.createElement("img");

                        $(link).html(arr[x]);
                        $(link).attr("id",arr[x]);
                        $(link).attr("href","#");
                        $(link).css("color","#3B5998");
                        $(link).css("display","inline-block");
                        $(link).css("width","65%");
                        $(link).css("text-decoration","none");

                        $(monitorLink).attr("src","css/24DC.gif");
                        $(monitorLink).attr("style","width:10%;height:10%;margin-left:-10%;");
                        $(monitorLink).attr("id",arr[x]+":monitorImage");

                        if(jsonData[x].isMonitor === true)
                            $(monitorLink).show();
                        else
                            $(monitorLink).hide();

                        $(deleteLink).addClass("btn btn-danger deletingInstances");
                        $(deleteLink).html("Delete");
                        $(deleteLink).css("margin-left","5%");
                        $(deleteLink).attr("id",arr[x]+":deleteButton");

                        $(li).append(link);
                        $(li).append(monitorLink);
                        $(li).append(deleteLink);
                        $("#sidebar-wrapper").find("ul").append(li);
                    }
                }
            }
        }
    );
}
repopulateInstanceList();
$(document).off('click', ".btn.btn-danger.deletingInstances").on('click', ".btn.btn-danger.deletingInstances", function()
{

    var clickedDelete = event.target.id.split(":");

    var prompt = confirm("Are you sure you want to delete this instance?");

    if(prompt === true) {
        var host = clickedDelete[0];
        var port = clickedDelete[1];
        $.ajax(
            {
                url: "/view/Delete",
                type: "POST",
                data: "deleteThisHost=" + host + "&deleteThisPort=" + port,
                success: function (strData) {
                    if (strData === "true") {
                        repopulateInstanceList();
                    }
                    else {
                        alertify.alert("Cannot delete.");
                    }
                }
            }
        );
    }
});

$(document).off('click', '#addInstance').on('click', '#addInstance', function(){
    var host = document.getElementById("host").value.toString();
    var port = document.getElementById("port").value.toString();
    var visibleTo = document.getElementById("visibleTo").value.toString();
    $.ajax(
        {
            url: "/view/Add",
            type: "POST",
            data: "addThisHost="+host+"&addThisPort="+port+"&visibleTo="+visibleTo,
            success: function (strData)    {
                repopulateInstanceList();
                alertify.alert(strData);
            }
        }
    );
    document.getElementById("host").value = "";
    document.getElementById("port").value = "";
    document.getElementById("visibleTo").value = "";
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
                if(strData === "true") {
                    repopulateInstanceList();
                }
                else    {
                    alertify.alert("Cannot delete.");
                }
            }
        }
    );
    document.getElementById("host").value = "";
    document.getElementById("port").value = "";
    document.getElementById("visibleTo").value = "";
});



