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
                    var deleteLink = document.createElement("button");
                    $(link).html(arr[x]);
                    $(link).attr("id",arr[x]);
                    $(link).attr("href","#");
                    $(link).css("color","#3B5998");
                    $(link).css("display","inline-block");
                    $(link).css("width","65%");
                    $(link).css("text-decoration","none");

                    $(deleteLink).addClass("btn btn-danger deletingInstances");
                    $(deleteLink).html("Delete");
                    $(deleteLink).css("margin-left","5%");
                    $(deleteLink).attr("id",arr[x]+":deleteButton");
                    $(li).append(link);
                    $(li).append(deleteLink);
                    $("#sidebar-wrapper").find("ul").append(li);
                }
            }
        }
    }
);
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
                        var id = host + ":" + port;
                        var toRemove = document.getElementById(id);
                        $(toRemove).parent().remove();
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
    $.ajax(
        {
            url: "/view/Add",
            type: "POST",
            data: "addThisHost="+host+"&addThisPort="+port,
            success: function (strData)    {
                if(strData === "true") {
                    var li = document.createElement("li");
                    var link = document.createElement("a");
                    var deleteLink = document.createElement("button");
                    $(link).html(host + ":" + port);
                    $(link).attr("id", host + ":" + port);
                    $(link).attr("href", "#");
                    $(link).css("color","#3B5998");
                    $(link).css("text-decoration","none");
                    $(link).css("display","inline-block");
                    $(link).css("width","65%");

                    $(deleteLink).addClass("btn btn-danger");
                    $(deleteLink).html("Delete");
                    $(deleteLink).attr("id",host + ":" + port +":button");
                    $(deleteLink).css("margin-left","5%");
                    $(li).append(link);
                    $(li).append(deleteLink);
                    $("#sidebar-wrapper").find("ul").append(li);
                }
                else    {
                    alertify.alert("Cannot add.");
                }
            }
        }
    );
    document.getElementById("host").value = "";
    document.getElementById("port").value = "";
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
                    var id = host + ":" + port;
                    var toRemove = document.getElementById(id);
                    $(toRemove).parent().remove();
                }
                else    {
                    alertify.alert("Cannot delete.");
                }
            }
        }
    );
    document.getElementById("host").value = "";
    document.getElementById("port").value = "";
});



