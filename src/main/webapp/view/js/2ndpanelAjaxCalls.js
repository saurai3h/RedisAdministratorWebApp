var typeOfKeys = [];

var populateKeyListFromJson = function(strData){

    $("#list-content").remove();
    var ul = document.createElement("ul");
    $(ul).attr("id","list-content");
    if (strData !== "false") {
        var jsonData = jQuery.parseJSON(strData);
        for (var x in jsonData) {

            var li = document.createElement("li");
            var link = document.createElement("a");
            var deleteLink = document.createElement("button");
            var editLink = document.createElement("button");
            var editInputBox = document.createElement("input");
            var typeOfKey = document.createElement("span");

            $(link).html(jsonData[x]["keyName"]);
            $(link).attr("id", jsonData[x]["keyName"]);
            $(link).attr("href", "#");
            $(link).css("display","inline-block");
            $(link).css("width","40%");
            $(li).append(link);

            $(deleteLink).addClass("btn btn-danger deletingKeys");
            $(deleteLink).html("Delete");
            $(deleteLink).css("margin-left","1%");
            $(deleteLink).attr("id","deleteButton"+":"+jsonData[x]["keyName"]);
            $(li).append(deleteLink);

            $(editLink).addClass("btn btn-info");
            $(editLink).html("Edit");
            $(editLink).css("margin-left","1%");
            $(editLink).attr("id","editButton"+":"+jsonData[x]["keyName"]);
            $(editLink).attr("onclick","editOuter()");
            $(li).append(editLink);


            $(editInputBox).css("display","none");
            $(editInputBox).css("width","15%");
            $(editInputBox).attr("id","optionalInput:"+jsonData[x]["keyName"]);
            $(editInputBox).attr("placeholder",jsonData[x]["keyName"]);
            $(editInputBox).attr("type","text");
//                                                $(editInputBox).attr("onchange",function(value){this.value = value;});
            $(li).append(editInputBox);

            $(typeOfKey).html(jsonData[x]["type"]);
            $(typeOfKey).css("margin-left","1%");
            $(typeOfKey).css("font-style","italic");
            $(typeOfKey).css("font-family","cursive");
            $(typeOfKey).css("text-transform","uppercase");
            $(typeOfKey).attr("id","typeSpan:"+jsonData[x]["keyName"]);
            $(li).append(typeOfKey);

            $(ul).append(li);
        }
        $("#list-display").append(ul);
    }
    else {
        console.log("No page to display");
    }
};
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
    }
    else if(buttonNo === 4){
        type = "hash";
    }
    else if(buttonNo === 5){
        type = "zset";
    }

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
editOuter = function() {

    var key = event.target.id.substring(11);
    var inputBoxID = "optionalInput:" + key;

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
                        var delChange = document.getElementById("deleteButton:" + key);
                        var editChange = document.getElementById("editButton:" + key);
                        var editBox = document.getElementById("optionalInput:",key);
                        var spanChange = document.getElementById("typeSpan:" + key);


                        $(aChange).html(value);
                        $(aChange).attr("id", value);
                        $(delChange).attr("id","deleteButton:" + value);
                        $(editChange).attr("id","editButton:" + value);
                        $(editBox).attr("id","optionalInput:" + value);
                        $(spanChange).attr("id","typeSpan:" + value);

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


        $(".sidebar-nav a").css("color", "#3B5998");

        $(clicked).css("color", "#3B0909");

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
    }
        );

        $(document).off('click','.btn.btn-danger.deletingKeys').on('click', '.btn.btn-danger.deletingKeys',function(){

            var key = event.target.id.substring(13);

            var prompt = confirm("Sure you want to delete this key?");
            if(prompt == true) {
                $.ajax(
                    {
                        url: "/view/DeleteKey",
                        type: "POST",
                        data: "keyToDelete=" + key,
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
                            else  if(strData === "success")  {
                                alertify.alert("Success!!");
                            }
                            else{
                                alertify.alert("Strange Error!! Aliens hacked into your DB!");
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

                            if(strData === "doesNotExist") {
                                alertify.alert("The key does not exist!");
                            }
                            else if(strData === "keyNull")  {
                                alertify.alert("Redis entries not filled.")
                            }
                            else if(strData === "false")    {
                                alertify.alert("Sorry! Couldn't add. The server must be down.");
                            }
                            else  if(strData === "success")  {
                                alertify.alert("Success!!");
                            }
                            else{
                                alertify.alert("Strange Error!! Probably aliens hacked into your DB!");
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
                        success: function (strData) {
                            populateKeyListFromJson(strData);}
                    }
                );

            }
        );

        $(document).off('click', '#next').on('click', '#next', function() {

                $.ajax(
                    {
                        url: "/view/Next",
                        type: "POST",
                        success: function (strData) {
                            populateKeyListFromJson(strData);
                        }
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

$(document).off('click', '#reset-page-list').on('click', '#reset-page-list', function(){

    $.ajax(
        {
            url: "/view/resetPageList",
            type: "POST",
            success: function (strData) {
                populateKeyListFromJson(strData);}
        }
    );
});


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
