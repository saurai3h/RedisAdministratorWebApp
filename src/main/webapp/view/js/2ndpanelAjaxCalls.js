var typeOfKeys = [];

editOuter = function() {

    var clickedArray = event.target.id.split(":");
    var key = clickedArray[3];
    var inputBoxID = clickedArray[0] + ":" + clickedArray[1] + ":optionalInput:" + key;

    var box = document.getElementById(inputBoxID);
    var value;

    if($(box).is(":hidden")) {
        $(box).show();
    }
    else    {
        value = box.value;
        if (value !== null && value.length > 0) {
            $.ajax(
                {
                    url: "/view/EditKey",
                    type: "POST",
                    data: "keyToEdit=" + key + "&valueToEdit=" + value,
                    success: function (strData) {

                        var aChange = document.getElementById(key);
                        var delChange = document.getElementById(clickedArray[0] + ":" + clickedArray[1] + ":deleteButton:" + key);
                        var editChange = document.getElementById(clickedArray[0] + ":" + clickedArray[1] + ":editButton:" + key);
                        var spanChange = document.getElementById(clickedArray[0] + ":" + clickedArray[1] + ":typeSpan:" + key);


                        $(aChange).html(value);
                        $(aChange).attr("id", value);
                        $(delChange).attr("id", clickedArray[0] + ":" + clickedArray[1] + ":deleteButton:" + value);
                        $(editChange).attr("id", clickedArray[0] + ":" + clickedArray[1] + ":editButton:" + value);
                        $(spanChange).attr("id", clickedArray[0] + ":" + clickedArray[1] + ":typeSpan:" + value);

                    }
                });
        }
        else {
            alertify.alert("Hey! You didn't enter anything.");
        }
        $(box).hide();
    }
};

$(document).off('click', '.sidebar-nav a').on('click', '.sidebar-nav a', function() {

        var clicked = event.target;
        var hostPort = clicked.id.toString();


        $(".sidebar-nav a").css("color","#3B5998");

        $(clicked).css("color","#3B0909");

        $.ajax(
            {
                url: "/view/RedisApplication2",
                type: "POST",
                data: "hostport=" + hostPort,
                success: function (strData) {


                    $.ajax(
                        {
                            url: "/view/RedisApplication3",
                            type: "POST",
                            data: "hostport=" + hostPort,
                            success: function (data) {
                                var jData = jQuery.parseJSON(data);
                                for (i = 0; i < jData.length; ++i) {
                                    typeOfKeys[i] = jData[i];
                                }

                                $("#list-content").remove();
                                var ul = document.createElement("ul");

                                $(ul).attr("id","list-content");
                                if (strData !== "false") {
                                    var jsonData = jQuery.parseJSON(strData);

                                    for (x = 0 ; x < jsonData.length ; ++x) {
                                        var li = document.createElement("li");
                                        var link = document.createElement("a");
                                        var deleteLink = document.createElement("button");
                                        var editLink = document.createElement("button");
                                        var editInputBox = document.createElement("input");
                                        var typeOfKey = document.createElement("span");

                                        $(link).html(jsonData[x]);
                                        $(link).attr("id", jsonData[x]);
                                        $(link).attr("href", "#");
                                        $(link).css("display","inline-block");
                                        $(link).css("width","40%");
                                        $(li).append(link);

                                        $(deleteLink).addClass("btn btn-danger deletingKeys");
                                        $(deleteLink).html("Delete");
                                        $(deleteLink).css("margin-left","1%");
                                        $(deleteLink).attr("id",arr[x]+":deleteButton"+":"+jsonData[x]);
                                        $(li).append(deleteLink);

                                        $(editLink).addClass("btn btn-info");
                                        $(editLink).html("Edit");
                                        $(editLink).css("margin-left","1%");
                                        $(editLink).attr("id",arr[x]+":editButton"+":"+jsonData[x]);
                                        $(editLink).attr("onclick","editOuter()");
                                        $(li).append(editLink);

                                        $(editInputBox).css("display","none");
                                        $(editInputBox).css("width","15%");
                                        $(editInputBox).attr("id",arr[x]+":optionalInput:"+jsonData[x]);
                                        $(editInputBox).attr("placeholder",jsonData[x]);
                                        $(editInputBox).attr("type","text");
//                                        $(editInputBox).attr("onchange",function(value){this.value = value;});
                                        $(li).append(editInputBox);

                                        $(typeOfKey).html(typeOfKeys[x]);
                                        $(typeOfKey).css("margin-left","1%");
                                        $(typeOfKey).css("font-style","italic");
                                        $(typeOfKey).css("font-family","cursive");
                                        $(typeOfKey).css("text-transform","uppercase");
                                        $(typeOfKey).attr("id",arr[x]+":typeSpan:"+jsonData[x]);
                                        $(li).append(typeOfKey);

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

                            $.ajax(
                                {
                                    url: "/view/RedisApplication3",
                                    type: "POST",
                                    data: "hostport=" + hostPort,
                                    success: function (data) {
                                        var jData = jQuery.parseJSON(data);
                                        for (i = 0; i < jData.length; ++i) {
                                            typeOfKeys[i] = jData[i];
                                        }

                                        $("#list-content").remove();
                                        var ul = document.createElement("ul");

                                        $(ul).attr("id","list-content");
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
                                            var jsonData = jQuery.parseJSON(strData);

                                            for (x = 0 ; x < jsonData.length ; ++x) {
                                                var li = document.createElement("li");
                                                var link = document.createElement("a");
                                                var deleteLink = document.createElement("button");
                                                var editLink = document.createElement("button");
                                                var editInputBox = document.createElement("input");
                                                var typeOfKey = document.createElement("span");

                                                $(link).html(jsonData[x]);
                                                $(link).attr("id", jsonData[x]);
                                                $(link).attr("href", "#");
                                                $(link).css("display","inline-block");
                                                $(link).css("width","40%");
                                                $(li).append(link);

                                                $(deleteLink).addClass("btn btn-danger deletingKeys");
                                                $(deleteLink).html("Delete");
                                                $(deleteLink).css("margin-left","1%");
                                                $(deleteLink).attr("id",arr[x]+":deleteButton"+":"+jsonData[x]);
                                                $(li).append(deleteLink);

                                                $(editLink).addClass("btn btn-info");
                                                $(editLink).html("Edit");
                                                $(editLink).css("margin-left","1%");
                                                $(editLink).attr("id",arr[x]+":editButton"+":"+jsonData[x]);
                                                $(editLink).attr("onclick","editOuter()");
                                                $(li).append(editLink);

                                                $(editInputBox).css("display","none");
                                                $(editInputBox).css("width","15%");
                                                $(editInputBox).attr("id",arr[x]+":optionalInput:"+jsonData[x]);
                                                $(editInputBox).attr("placeholder",jsonData[x]);
                                                $(editInputBox).attr("type","text");
//                                                $(editInputBox).attr("onchange",function(value){this.value = value;});
                                                $(li).append(editInputBox);

                                                $(typeOfKey).html(typeOfKeys[x]);
                                                $(typeOfKey).css("margin-left","1%");
                                                $(typeOfKey).css("font-style","italic");
                                                $(typeOfKey).css("font-family","cursive");
                                                $(typeOfKey).css("text-transform","uppercase");
                                                $(typeOfKey).attr("id",arr[x]+":typeSpan:"+jsonData[x]);
                                                $(li).append(typeOfKey);

                                                $(ul).append(li);
                                            }
                                            $("#list-display").append(ul);
                                        }
                                    }
                                }
                            );
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

                            $.ajax(
                                {
                                    url: "/view/RedisApplication3",
                                    type: "POST",
                                    data: "hostport=" + hostPort,
                                    success: function (data) {
                                        var jData = jQuery.parseJSON(data);
                                        for (i = 0; i < jData.length; ++i) {
                                            typeOfKeys[i] = jData[i];
                                        }

                                        $("#list-content").remove();
                                        var ul = document.createElement("ul");

                                        $(ul).attr("id","list-content");
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
                                            var jsonData = jQuery.parseJSON(strData);

                                            for (x = 0 ; x < jsonData.length ; ++x) {
                                                var li = document.createElement("li");
                                                var link = document.createElement("a");
                                                var deleteLink = document.createElement("button");
                                                var editLink = document.createElement("button");
                                                var editInputBox = document.createElement("input");
                                                var typeOfKey = document.createElement("span");

                                                $(link).html(jsonData[x]);
                                                $(link).attr("id", jsonData[x]);
                                                $(link).attr("href", "#");
                                                $(link).css("display","inline-block");
                                                $(link).css("width","40%");
                                                $(li).append(link);

                                                $(deleteLink).addClass("btn btn-danger deletingKeys");
                                                $(deleteLink).html("Delete");
                                                $(deleteLink).css("margin-left","1%");
                                                $(deleteLink).attr("id",arr[x]+":deleteButton"+":"+jsonData[x]);
                                                $(li).append(deleteLink);

                                                $(editLink).addClass("btn btn-info");
                                                $(editLink).html("Edit");
                                                $(editLink).css("margin-left","1%");
                                                $(editLink).attr("id",arr[x]+":editButton"+":"+jsonData[x]);
                                                $(editLink).attr("onclick","editOuter()");
                                                $(li).append(editLink);

                                                $(editInputBox).css("display","none");
                                                $(editInputBox).css("width","15%");
                                                $(editInputBox).attr("id",arr[x]+":optionalInput:"+jsonData[x]);
                                                $(editInputBox).attr("placeholder",jsonData[x]);
                                                $(editInputBox).attr("type","text");
//                                                $(editInputBox).attr("onchange",function(value){this.value = value;});
                                                $(li).append(editInputBox);

                                                $(typeOfKey).html(typeOfKeys[x]);
                                                $(typeOfKey).css("margin-left","1%");
                                                $(typeOfKey).css("font-style","italic");
                                                $(typeOfKey).css("font-family","cursive");
                                                $(typeOfKey).css("text-transform","uppercase");
                                                $(typeOfKey).attr("id",arr[x]+":typeSpan:"+jsonData[x]);
                                                $(li).append(typeOfKey);

                                                $(ul).append(li);
                                            }
                                            $("#list-display").append(ul);
                                        }
                                    }
                                }
                            );
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

                            $.ajax(
                                {
                                    url: "/view/RedisApplication3",
                                    type: "POST",
                                    data: "hostport=" + hostPort,
                                    success: function (data) {
                                        var jData = jQuery.parseJSON(data);
                                        for (i = 0; i < jData.length; ++i) {
                                            typeOfKeys[i] = jData[i];
                                        }

                                        $("#list-content").remove();
                                        var ul = document.createElement("ul");

                                        $(ul).attr("id","list-content");
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
                                            var jsonData = jQuery.parseJSON(strData);

                                            for (x = 0 ; x < jsonData.length ; ++x) {
                                                var li = document.createElement("li");
                                                var link = document.createElement("a");
                                                var deleteLink = document.createElement("button");
                                                var editLink = document.createElement("button");
                                                var editInputBox = document.createElement("input");
                                                var typeOfKey = document.createElement("span");

                                                $(link).html(jsonData[x]);
                                                $(link).attr("id", jsonData[x]);
                                                $(link).attr("href", "#");
                                                $(link).css("display","inline-block");
                                                $(link).css("width","40%");
                                                $(li).append(link);

                                                $(deleteLink).addClass("btn btn-danger deletingKeys");
                                                $(deleteLink).html("Delete");
                                                $(deleteLink).css("margin-left","1%");
                                                $(deleteLink).attr("id",arr[x]+":deleteButton"+":"+jsonData[x]);
                                                $(li).append(deleteLink);

                                                $(editLink).addClass("btn btn-info");
                                                $(editLink).html("Edit");
                                                $(editLink).css("margin-left","1%");
                                                $(editLink).attr("id",arr[x]+":editButton"+":"+jsonData[x]);
                                                $(editLink).attr("onclick","editOuter()");
                                                $(li).append(editLink);


                                                $(editInputBox).css("display","none");
                                                $(editInputBox).css("width","15%");
                                                $(editInputBox).attr("id",arr[x]+":optionalInput:"+jsonData[x]);
                                                $(editInputBox).attr("placeholder",jsonData[x]);
                                                $(editInputBox).attr("type","text");
//                                                $(editInputBox).attr("onchange",function(value){this.value = value;});
                                                $(li).append(editInputBox);

                                                $(typeOfKey).html(typeOfKeys[x]);
                                                $(typeOfKey).css("margin-left","1%");
                                                $(typeOfKey).css("font-style","italic");
                                                $(typeOfKey).css("font-family","cursive");
                                                $(typeOfKey).css("text-transform","uppercase");
                                                $(typeOfKey).attr("id",arr[x]+":typeSpan:"+jsonData[x]);
                                                $(li).append(typeOfKey);

                                                $(ul).append(li);
                                            }
                                            $("#list-display").append(ul);
                                        }
                                    }
                                }
                            );
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

                            $.ajax(
                                {
                                    url: "/view/RedisApplication3",
                                    type: "POST",
                                    data: "hostport=" + hostPort,
                                    success: function (data) {
                                        var jData = jQuery.parseJSON(data);
                                        for (i = 0; i < jData.length; ++i) {
                                            typeOfKeys[i] = jData[i];
                                        }

                                        $("#list-content").remove();
                                        var ul = document.createElement("ul");

                                        $(ul).attr("id","list-content");
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
                                            var jsonData = jQuery.parseJSON(strData);

                                            for (x = 0 ; x < jsonData.length ; ++x) {
                                                var li = document.createElement("li");
                                                var link = document.createElement("a");
                                                var deleteLink = document.createElement("button");
                                                var editLink = document.createElement("button");
                                                var editInputBox = document.createElement("input");
                                                var typeOfKey = document.createElement("span");

                                                $(link).html(jsonData[x]);
                                                $(link).attr("id", jsonData[x]);
                                                $(link).attr("href", "#");
                                                $(link).css("display","inline-block");
                                                $(link).css("width","40%");
                                                $(li).append(link);

                                                $(deleteLink).addClass("btn btn-danger deletingKeys");
                                                $(deleteLink).html("Delete");
                                                $(deleteLink).css("margin-left","1%");
                                                $(deleteLink).attr("id",arr[x]+":deleteButton"+":"+jsonData[x]);
                                                $(li).append(deleteLink);

                                                $(editLink).addClass("btn btn-info");
                                                $(editLink).html("Edit");
                                                $(editLink).css("margin-left","1%");
                                                $(editLink).attr("id",arr[x]+":editButton"+":"+jsonData[x]);
                                                $(editLink).attr("onclick","editOuter()");
                                                $(li).append(editLink);


                                                $(editInputBox).css("display","none");
                                                $(editInputBox).css("width","15%");
                                                $(editInputBox).attr("id",arr[x]+":optionalInput:"+jsonData[x]);
                                                $(editInputBox).attr("placeholder",jsonData[x]);
                                                $(editInputBox).attr("type","text");
//                                                $(editInputBox).attr("onchange",function(value){this.value = value;});
                                                $(li).append(editInputBox);

                                                $(typeOfKey).html(typeOfKeys[x]);
                                                $(typeOfKey).css("margin-left","1%");
                                                $(typeOfKey).css("font-style","italic");
                                                $(typeOfKey).css("font-family","cursive");
                                                $(typeOfKey).css("text-transform","uppercase");
                                                $(typeOfKey).attr("id",arr[x]+":typeSpan:"+jsonData[x]);
                                                $(li).append(typeOfKey);

                                                $(ul).append(li);
                                            }
                                            $("#list-display").append(ul);
                                        }
                                    }
                                }
                            );
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
                            $.ajax(
                                {
                                    url: "/view/RedisApplication3",
                                    type: "POST",
                                    data: "hostport=" + hostPort,
                                    success: function (data) {
                                        var jData = jQuery.parseJSON(data);
                                        for (i = 0; i < jData.length; ++i) {
                                            typeOfKeys[i] = jData[i];
                                        }

                                        $("#list-content").remove();
                                        var ul = document.createElement("ul");

                                        $(ul).attr("id","list-content");
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
                                            var jsonData = jQuery.parseJSON(strData);

                                            for (x = 0 ; x < jsonData.length ; ++x) {
                                                var li = document.createElement("li");
                                                var link = document.createElement("a");
                                                var deleteLink = document.createElement("button");
                                                var editLink = document.createElement("button");
                                                var editInputBox = document.createElement("input");
                                                var typeOfKey = document.createElement("span");

                                                $(link).html(jsonData[x]);
                                                $(link).attr("id", jsonData[x]);
                                                $(link).attr("href", "#");
                                                $(link).css("display","inline-block");
                                                $(link).css("width","40%");
                                                $(li).append(link);

                                                $(deleteLink).addClass("btn btn-danger deletingKeys");
                                                $(deleteLink).html("Delete");
                                                $(deleteLink).css("margin-left","1%");
                                                $(deleteLink).attr("id",arr[x]+":deleteButton"+":"+jsonData[x]);
                                                $(li).append(deleteLink);

                                                $(editLink).addClass("btn btn-info");
                                                $(editLink).html("Edit");
                                                $(editLink).css("margin-left","1%");
                                                $(editLink).attr("id",arr[x]+":editButton"+":"+jsonData[x]);
                                                $(editLink).attr("onclick","editOuter()");
                                                $(li).append(editLink);


                                                $(editInputBox).css("display","none");
                                                $(editInputBox).css("width","15%");
                                                $(editInputBox).attr("id",arr[x]+":optionalInput:"+jsonData[x]);
                                                $(editInputBox).attr("placeholder",jsonData[x]);
                                                $(editInputBox).attr("type","text");
//                                                $(editInputBox).attr("onchange",function(value){this.value = value;});
                                                $(li).append(editInputBox);



                                                $(typeOfKey).html(typeOfKeys[x]);
                                                $(typeOfKey).css("margin-left","1%");
                                                $(typeOfKey).css("font-style","italic");
                                                $(typeOfKey).css("font-family","cursive");
                                                $(typeOfKey).css("text-transform","uppercase");
                                                $(typeOfKey).attr("id",arr[x]+":typeSpan:"+jsonData[x]);
                                                $(li).append(typeOfKey);

                                                $(ul).append(li);
                                            }
                                            $("#list-display").append(ul);
                                        }
                                    }
                                }
                            );
                        }
                    }
                );
                document.getElementById("keyAdd5").value = "";
                document.getElementById("valueAdd5").value = "";
                document.getElementById("scoreAdd5").value = "";
            }
        );

        $(document).off('click','.btn.btn-danger.deletingKeys').on('click', '.btn.btn-danger.deletingKeys',function(){

            var key = event.target.id.split(":")[3];

            var prompt = confirm("Sure you want to delete this key?");
            if(prompt == true) {
                $.ajax(
                    {
                        url: "/view/DeleteKeyThatPage",
                        type: "POST",
                        data: "keyToDelete=" + key,
                        success: function (strData) {
                            if(strData === "true") {
                                var link = document.getElementById(key);
                                $(link).parent().remove();
                            }
                            else    {
                                alertify.alert("Sorry, could not delete! Server may be down.")
                            }
                        }
                    }
                );
            }
        });

        $(document).off('click', '#Delete6').on('click', '#Delete6', function() {

                var key = document.getElementById("keyDeleteSearch6").value.toString();

                $.ajax(
                    {
                        url: "/view/DeleteKey",
                        type: "POST",
                        data: "keyToDelete="+key,
                        success: function (strData) {

                        $.ajax(
                                {
                                    url: "/view/RedisApplication3",
                                    type: "POST",
                                    data: "hostport=" + hostPort,
                                    success: function (data) {
                                        var jData = jQuery.parseJSON(data);
                                        for (i = 0; i < jData.length; ++i) {
                                            typeOfKeys[i] = jData[i];
                                        }

                                        $("#list-content").remove();
                                        var ul = document.createElement("ul");

                                        $(ul).attr("id","list-content");
                                        if(strData === "doesNotExist") {
                                            alertify.alert("The key does not exist!");
                                        }
                                        else if(strData === "keyNull")  {
                                            alertify.alert("Redis entries not filled.")
                                        }
                                        else if(strData === "false")    {
                                            alertify.alert("Sorry! Couldn't delete. The server must be down.");
                                        }
                                        else    {
                                            var jsonData = jQuery.parseJSON(strData);

                                            for (x = 0 ; x < jsonData.length ; ++x) {
                                                var li = document.createElement("li");
                                                var link = document.createElement("a");
                                                var deleteLink = document.createElement("button");
                                                var editLink = document.createElement("button");
                                                var editInputBox = document.createElement("input");
                                                var typeOfKey = document.createElement("span");

                                                $(link).html(jsonData[x]);
                                                $(link).attr("id", jsonData[x]);
                                                $(link).attr("href", "#");
                                                $(link).css("display","inline-block");
                                                $(link).css("width","40%");
                                                $(li).append(link);

                                                $(deleteLink).addClass("btn btn-danger deletingKeys");
                                                $(deleteLink).html("Delete");
                                                $(deleteLink).css("margin-left","1%");
                                                $(deleteLink).attr("id",arr[x]+":deleteButton"+":"+jsonData[x]);
                                                $(li).append(deleteLink);

                                                $(editLink).addClass("btn btn-info");
                                                $(editLink).html("Edit");
                                                $(editLink).css("margin-left","1%");
                                                $(editLink).attr("id",arr[x]+":editButton"+":"+jsonData[x]);
                                                $(editLink).attr("onclick","editOuter()");
                                                $(li).append(editLink);


                                                $(editInputBox).css("display","none");
                                                $(editInputBox).css("width","15%");
                                                $(editInputBox).attr("id",arr[x]+":optionalInput:"+jsonData[x]);
                                                $(editInputBox).attr("placeholder",jsonData[x]);
                                                $(editInputBox).attr("type","text");
//                                                $(editInputBox).attr("onchange",function(value){this.value = value;});
                                                $(li).append(editInputBox);

                                                $(typeOfKey).html(typeOfKeys[x]);
                                                $(typeOfKey).css("margin-left","1%");
                                                $(typeOfKey).css("font-style","italic");
                                                $(typeOfKey).css("font-family","cursive");
                                                $(typeOfKey).css("text-transform","uppercase");
                                                $(typeOfKey).attr("id",arr[x]+":typeSpan:"+jsonData[x]);
                                                $(li).append(typeOfKey);

                                                $(ul).append(li);
                                            }
                                            $("#list-display").append(ul);
                                        }
                                    }
                                }
                            );
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

                            $.ajax(
                                {
                                    url: "/view/RedisApplication3",
                                    type: "POST",
                                    data: "hostport=" + hostPort,
                                    success: function (data) {
                                        var jData = jQuery.parseJSON(data);
                                        for (i = 0; i < jData.length; ++i) {
                                            typeOfKeys[i] = jData[i];
                                        }

                                        $("#list-content").remove();
                                        var ul = document.createElement("ul");

                                        $(ul).attr("id","list-content");
                                        if(strData === "doesNotExist") {
                                            alertify.alert("The key does not exist!");
                                        }
                                        else if(strData === "keyNull")  {
                                            alertify.alert("Redis entries not filled.")
                                        }
                                        else if(strData === "false")    {
                                            alertify.alert("Sorry! Couldn't find. The server must be down.");
                                        }
                                        else    {
                                            var jsonData = jQuery.parseJSON(strData);

                                            for (x = 0 ; x < jsonData.length ; ++x) {
                                                var li = document.createElement("li");
                                                var link = document.createElement("a");
                                                var deleteLink = document.createElement("button");
                                                var editLink = document.createElement("button");
                                                var editInputBox = document.createElement("input");
                                                var typeOfKey = document.createElement("span");

                                                $(link).html(jsonData[x]);
                                                $(link).attr("id", jsonData[x]);
                                                $(link).attr("href", "#");
                                                $(link).css("display","inline-block");
                                                $(link).css("width","40%");
                                                $(li).append(link);

                                                $(deleteLink).addClass("btn btn-danger deletingKeys");
                                                $(deleteLink).html("Delete");
                                                $(deleteLink).css("margin-left","1%");
                                                $(deleteLink).attr("id",arr[x]+":deleteButton"+":"+jsonData[x]);
                                                $(li).append(deleteLink);

                                                $(editLink).addClass("btn btn-info");
                                                $(editLink).html("Edit");
                                                $(editLink).css("margin-left","1%");
                                                $(editLink).attr("id",arr[x]+":editButton"+":"+jsonData[x]);
                                                $(editLink).attr("onclick","editOuter()");
                                                $(li).append(editLink);


                                                $(editInputBox).css("display","none");
                                                $(editInputBox).css("width","15%");
                                                $(editInputBox).attr("id",arr[x]+":optionalInput:"+jsonData[x]);
                                                $(editInputBox).attr("placeholder",jsonData[x]);
                                                $(editInputBox).attr("type","text");
//                                                $(editInputBox).attr("onchange",function(value){this.value = value;});
                                                $(li).append(editInputBox);

                                                $(typeOfKey).html(typeOfKeys[x]);
                                                $(typeOfKey).css("margin-left","1%");
                                                $(typeOfKey).css("font-style","italic");
                                                $(typeOfKey).css("font-family","cursive");
                                                $(typeOfKey).css("text-transform","uppercase");
                                                $(typeOfKey).attr("id",arr[x]+":typeSpan:"+jsonData[x]);
                                                $(li).append(typeOfKey);

                                                $(ul).append(li);
                                            }
                                            $("#list-display").append(ul);
                                        }
                                    }
                                }
                            );
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
                        success: function (strData) {

                            $.ajax(
                                {
                                    url: "/view/RedisApplication3",
                                    type: "POST",
                                    data: "hostport=" + hostPort,
                                    success: function(data) {
                                        var jData = jQuery.parseJSON(data);
                                        for(i = 0 ; i < jData.length ; ++i)    {
                                            typeOfKeys[i] = jData[i];
                                        }

                                        $("#list-content").remove();
                                        var ul = document.createElement("ul");
                                        $(ul).attr("id","list-content");
                                        if (strData !== "false") {
                                            var jsonData = jQuery.parseJSON(strData);
                                            var counter = 0;
                                            for (var x in jsonData) {

                                                var li = document.createElement("li");
                                                var link = document.createElement("a");
                                                var deleteLink = document.createElement("button");
                                                var editLink = document.createElement("button");
                                                var editInputBox = document.createElement("input");
                                                var typeOfKey = document.createElement("span");

                                                $(link).html(jsonData[x]);
                                                $(link).attr("id", jsonData[x]);
                                                $(link).attr("href", "#");
                                                $(link).css("display","inline-block");
                                                $(link).css("width","40%");
                                                $(li).append(link);

                                                $(deleteLink).addClass("btn btn-danger deletingKeys");
                                                $(deleteLink).html("Delete");
                                                $(deleteLink).css("margin-left","1%");
                                                $(deleteLink).attr("id",arr[x]+":deleteButton"+":"+jsonData[x]);
                                                $(li).append(deleteLink);

                                                $(editLink).addClass("btn btn-info");
                                                $(editLink).html("Edit");
                                                $(editLink).css("margin-left","1%");
                                                $(editLink).attr("id",arr[x]+":editButton"+":"+jsonData[x]);
                                                $(editLink).attr("onclick","editOuter()");
                                                $(li).append(editLink);


                                                $(editInputBox).css("display","none");
                                                $(editInputBox).css("width","15%");
                                                $(editInputBox).attr("id",arr[x]+":optionalInput:"+jsonData[x]);
                                                $(editInputBox).attr("placeholder",jsonData[x]);
                                                $(editInputBox).attr("type","text");
//                                                $(editInputBox).attr("onchange",function(value){this.value = value;});
                                                $(li).append(editInputBox);

                                                $(typeOfKey).html(typeOfKeys[counter]);
                                                $(typeOfKey).css("margin-left","1%");
                                                $(typeOfKey).css("font-style","italic");
                                                $(typeOfKey).css("font-family","cursive");
                                                $(typeOfKey).css("text-transform","uppercase");
                                                $(typeOfKey).attr("id",arr[x]+":typeSpan:"+jsonData[x]);
                                                $(li).append(typeOfKey);

                                                $(ul).append(li);
                                                ++counter;
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
                        success: function (strData) {


                            $.ajax(
                                {
                                    url: "/view/RedisApplication3",
                                    type: "POST",
                                    data: "hostport=" + hostPort,
                                    success: function(data) {
                                        var jData = jQuery.parseJSON(data);
                                        for(i = 0 ; i < jData.length ; ++i)    {
                                            typeOfKeys[i] = jData[i];
                                        }

                                        $("#list-content").remove();
                                        var ul = document.createElement("ul");
                                        $(ul).attr("id","list-content");
                                        if (strData !== "false") {
                                            var jsonData = jQuery.parseJSON(strData);
                                            var counter = 0;
                                            for (var x in jsonData) {

                                                var li = document.createElement("li");
                                                var link = document.createElement("a");
                                                var deleteLink = document.createElement("button");
                                                var editLink = document.createElement("button");
                                                var editInputBox = document.createElement("input");
                                                var typeOfKey = document.createElement("span");

                                                $(link).html(jsonData[x]);
                                                $(link).attr("id", jsonData[x]);
                                                $(link).attr("href", "#");
                                                $(link).css("display","inline-block");
                                                $(link).css("width","40%");
                                                $(li).append(link);

                                                $(deleteLink).addClass("btn btn-danger deletingKeys");
                                                $(deleteLink).html("Delete");
                                                $(deleteLink).css("margin-left","1%");
                                                $(deleteLink).attr("id",arr[x]+":deleteButton"+":"+jsonData[x]);
                                                $(li).append(deleteLink);

                                                $(editLink).addClass("btn btn-info");
                                                $(editLink).html("Edit");
                                                $(editLink).css("margin-left","1%");
                                                $(editLink).attr("id",arr[x]+":editButton"+":"+jsonData[x]);
                                                $(editLink).attr("onclick","editOuter()");
                                                $(li).append(editLink);


                                                $(editInputBox).css("display","none");
                                                $(editInputBox).css("width","15%");
                                                $(editInputBox).attr("id",arr[x]+":optionalInput:"+jsonData[x]);
                                                $(editInputBox).attr("placeholder",jsonData[x]);
                                                $(editInputBox).attr("type","text");
//                                                $(editInputBox).attr("onchange",function(value){this.value = value;});
                                                $(li).append(editInputBox);

                                                $(typeOfKey).html(typeOfKeys[counter]);
                                                $(typeOfKey).css("margin-left","1%");
                                                $(typeOfKey).css("font-style","italic");
                                                $(typeOfKey).css("font-family","cursive");
                                                $(typeOfKey).css("text-transform","uppercase");
                                                $(typeOfKey).attr("id",arr[x]+":typeSpan:"+jsonData[x]);
                                                $(li).append(typeOfKey);

                                                $(ul).append(li);
                                                ++counter;
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
                    }
                );

            }
        );
    }
);

$(document).off('click', 'ul#list-content li a').on('click', "ul#list-content li a", function(){

        var clickedKey = event.target.id.toString();
        console.log(clickedKey);
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
                        $(container).addClass("table table-responsive");
                        $(container).attr("border","1");
                        $(container).attr("style","width:100%;margin-left:5%;margin-top:5%;margin-bottom:5%;");
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