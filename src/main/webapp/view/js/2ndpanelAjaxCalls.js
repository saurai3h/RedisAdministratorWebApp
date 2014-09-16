
var splitString = "#$@!^~%&$#~!#@#~";

var autoCompleteArray = [];

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
            var expiryTime = document.createElement("span");

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

            $(expiryTime).html(jsonData[x]["expiryTime"]);
            $(expiryTime).css("margin-left","3%");
            $(expiryTime).css("font-family","cursive");
            $(expiryTime).attr("id","expiryTimeSpan:"+jsonData[x]["expiryTime"]);
            $(li).append(expiryTime);

            $(ul).append(li);
        }
        $("#list-display").append(ul);
    }
    else {
        $("#treeViewContent").empty();
    }
};
var charCodeArrToString = function toBinString (charCodeArr) {
    var concatenatedString = "";
    for(var char in charCodeArr){
        concatenatedString += String.fromCharCode(charCodeArr[char]);
    }
    return concatenatedString;
}
var editOuter = function() {

    var key = event.target.id.toString().substring(11);
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
var ajaxCallForAddKey = function(buttonNo){

    var key = document.getElementById("keyAdd"+buttonNo.toString()).value.toString();
    var value = document.getElementById("valueAdd"+buttonNo.toString()).value.toString();
    var expiry = document.getElementById("expiryAdd"+buttonNo.toString()).value.toString();

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
                "&valueOfKey="+value+"&optionalValueOfKey="+optionalValue+"&expiryOfKey="+expiry,
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
                else if(strData === "scoreNotDouble")   {
                    alertify.alert("The entry you filled for score should be of Double type.");
                }
                else if(strData === "expiryInvalid")    {
                    alertify.alert("The expiry added is invalid. Please enter non-negative(except -1) values.");
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
    document.getElementById("keyAdd"+buttonNo.toString()).value = "";
    document.getElementById("valueAdd"+buttonNo.toString()).value = "";
    document.getElementById("expiryAdd"+buttonNo.toString()).value = "";
    var optionalValue = document.getElementById("optionalValueAdd"+buttonNo.toString());
    if(optionalValue !== null)optionalValue.value = "";
};
var fieldAdder = function(clickedKey,field,value,type) {
    $.ajax(
        {
            url: "/view/AddField",
            type: "POST",
            data: "clickedKey=" + clickedKey +"&field=" + field + "&value=" + value + "&type=" +type,
            success: function (strData) {

                if(strData === "doesNotExist") {
                    alertify.alert("The key does not exist anymore");
                }
                else if(strData === "existingFieldUpdated")    {
                    alertify.alert("The key existed already and is updated.");
                    var keyFromWhichAdded = document.getElementById(clickedKey);
                    if(keyFromWhichAdded)
                        keyFromWhichAdded.click();
                }
                else if(strData === "scoreNotDouble")   {
                    alertify.alert("The entry you filled for score should be of Double type.");
                }
                else  if(strData === "success")  {
                    var keyFromWhichAdded = document.getElementById(clickedKey);
                    if(keyFromWhichAdded)
                        keyFromWhichAdded.click();
                }
                else if(strData === "keyNull") {
                    alertify.alert("Entries not filled.");
                }
                else if(strData === "false")    {
                    alertify.alert("Field could not be added. The server must be down.")
                }
                else{
                    alertify.alert("Strange Error!! Aliens hacked into your DB!");
                }
            }
        }
    );
};
var deleteField = function(clickedKey,key,value,type)  {
    $.ajax(
        {
            url: "/view/DeleteField",
            type: "POST",
            data: "clickedKey="+ clickedKey +"&key="+key + "&value=" + value + "&type=" + type,
            success: function (strData) {

                if(strData === "doesNotExist") {
                    alertify.alert("The field does not exist!");
                }
                else if(strData === "false")    {
                    alertify.alert("Sorry! Couldn't delete. The server must be down.");
                }
                else  if(strData === "success")  {
                    var keyFromWhichDeleted = document.getElementById(clickedKey);
                    if(keyFromWhichDeleted)
                        keyFromWhichDeleted.click();
                }
                else{
                    alertify.alert("Strange Error!! Aliens hacked into your DB!");
                }
            }
        }
    );
};
var editField = function(clickedKey,key,value,newKey,newValue,type)  {

    $.ajax(
        {
            url: "/view/EditField",
            type: "POST",
            data: "clickedKey="+ clickedKey +"&key="+ key + "&value=" + value + "&type=" + type + "&newKey=" + newKey + "&newValue=" + newValue,
            success: function (strData) {

                if(strData === "doesNotExist") {
                    alertify.alert("The field does not exist!");
                }
                else if(strData === "keyNull")  {
                    alertify.alert("Redis entries not filled.")
                }
                else if(strData === "scoreNotDouble")   {
                    alertify.alert("The entry you filled for score should be of Double type.");
                }
                else if(strData === "false")    {
                    alertify.alert("Sorry! Couldn't edit. The server must be down.");
                }
                else  if(strData === "success")  {

                    var keyFromWhichEdited = document.getElementById(clickedKey);
                    if(keyFromWhichEdited)
                        keyFromWhichEdited.click();
                }
                else{
                    alertify.alert("Strange Error!! Aliens hacked into your DB!");
                }
            }
        }
    );
}

$(document).off('click', '.sidebar-nav a').on('click', '.sidebar-nav a', function() {

        $("#keys-details").hide();
        $("#thirdPanel").hide();
        $("#list-header").show();

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

                    $.ajax(
                        {
                            url: "/view/AutoComplete",
                            type: "POST",
                            success: function (data) {

                                autoCompleteArray = jQuery.parseJSON(data);

                                $("#keyDeleteSearch6").autocomplete(
                                    {
                                        source: function (request,response) {
                                            var results = $.ui.autocomplete.filter(autoCompleteArray, request.term);

                                            response(results.slice(0, 10));
                                        },
                                        minLength: 2
                                    });
                            }
                        }
                    );
                }
            }
        );
});

$(document).off('click','.btn.btn-danger.deletingKeys').on('click', '.btn.btn-danger.deletingKeys',function(){

    var key = event.target.id.toString().substring(13);

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
                        alertify.alert("Sorry! Couldn't delete. The server must be down.");
                    }
                    else  if(strData === "success")  {
                        alertify.alert("Success!!");
                    }
                    else    {
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
                        alertify.alert("Sorry! Couldn't delete. The server must be down.");
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
                        alertify.alert("Sorry! Couldn't search. The server must be down.");
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


$(document).off('click', '#reset-page-list').on('click', '#reset-page-list', function(){

        $("#keys-details").hide();
        $("#thirdPanel").hide();

        $.ajax(
            {
                url: "/view/resetPageList",
                type: "POST",
                success: function (strData) {
                    populateKeyListFromJson(strData);
                    $.ajax(
                        {
                            url: "/view/AutoComplete",
                            type: "POST",
                            success: function (data) {

                                autoCompleteArray = jQuery.parseJSON(data);

                                $("#keyDeleteSearch6").autocomplete(
                                    {
                                        source: function (request,response) {
                                            var results = $.ui.autocomplete.filter(autoCompleteArray, request.term);

                                            response(results.slice(0, 10));
                                        },
                                        minLength: 2
                                    });
                            }
                        }
                    );
                }
            }
        );
    }
);

recursePopulate = function(root,li)    {

    li.innerHTML = root["label"];

    var ulInner = document.createElement("ul");

    var isLeaf = 1;

    for(var child in root["children"])  {

        isLeaf = 0;
        var liInner = document.createElement("li");

        liInner.innerHTML = root["children"][child]["label"];

        recursePopulate(root["children"][child],liInner);
        $(ulInner).append(liInner);
    }

    if(isLeaf === 0)
        $(li).append(ulInner);
};

$(document).off('click', '#treeView').on('click', '#treeView', function(){

        $.ajax(
            {
                url: "/view/TreeView",
                type: "POST",
                success: function (treeData) {
                    $("#treeViewContent").empty();
                    var everything = jQuery.parseJSON(treeData);

                    var li = document.createElement("li");

                    recursePopulate(everything,li);

                    $("#treeViewContent").append(li);

                    $("#treeViewContent").bonsai();

                }
            }
        );

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
});

$(document).off('click', '#next').on('click', '#next', function(){

    $.ajax(
        {
            url: "/view/Next",
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

$(document).off('click', 'ul#list-content li a').on('click', "ul#list-content li a", function(){

        $("#keys-details").show();
        $("#thirdPanel").show();

        var clickedKey = event.target.id.toString();
        $.ajax(
            {

                url: "/view/getValueForAKey",
                type: "POST",
                dataType : "json",
                data: "key=" + clickedKey,
                success: function (jsonData) {

                    if(jsonData === "doesNotExist")
                        alertify.alert("The key does not exist anymore.");
                    else
                    {
                        var valueFooter = document.createElement("input");
                        var addFooter = document.createElement("button");

                        $(addFooter).addClass("btn btn-primary");
                        $(valueFooter).attr("placeholder", "value");
                        $(addFooter).html("Add");

                        $("#thirdPanel").empty();


                        var type = jsonData["type"];
                        $("#value-container").remove();
                        var json = jQuery.parseJSON(jsonData["json"]);
                        var len = json.length;
                        var container;
                        var appendToContainer;
                        if (type === "zset" || type === "hash") {


                            container = document.createElement("table");
                            $(container).addClass("table table-responsive");
                            $(container).attr("border", "1");
                            $(container).attr("style", "width:100%;margin-left:5%;margin-top:5%;margin-bottom:5%;");
                            var tr = document.createElement("tr");
                            var keyHead = document.createElement("th");
                            var valueHead = document.createElement("th");

                            if (type === "zset") {
                                $(keyHead).html("Member");
                                $(valueHead).html("Score");

                                var scoreFooter = document.createElement("input");
                                $(scoreFooter).attr("placeholder", "score");
                                $(valueFooter).attr("id", "zsetValue:" + clickedKey);
                                $(scoreFooter).attr("id", "zsetScore:" + clickedKey);
                                $(addFooter).attr("id", "zsetAdd:" + clickedKey);
                                $(addFooter).addClass("addingZsetFields");

                                $("#thirdPanel").append(scoreFooter);
                                $("#thirdPanel").append(valueFooter);
                                $("#thirdPanel").append(addFooter);

                            }
                            else {
                                $(keyHead).html("Field");
                                $(valueHead).html("Value");

                                var fieldFooter = document.createElement("input");
                                $(fieldFooter).attr("placeholder", "field");
                                $(fieldFooter).attr("id", "hashField:" + clickedKey);
                                $(valueFooter).attr("id", "hashValue:" + clickedKey);
                                $(addFooter).attr("id", "hashAdd:" + clickedKey);
                                $(addFooter).addClass("addingHashFields");

                                $("#thirdPanel").append(fieldFooter);
                                $("#thirdPanel").append(valueFooter);
                                $("#thirdPanel").append(addFooter);
                            }

                            $(tr).append(keyHead);
                            $(tr).append(valueHead);
                            $(container).append(tr);

                            appendToContainer = function (key, value) {
                                var tr = document.createElement("tr");
                                var keyCell = document.createElement("td");
                                $(keyCell).css("width", "30%");
                                var valueCell = document.createElement("td");
                                $(valueCell).css("width", "30%");

                                var deleteCell = document.createElement("button");
                                $(deleteCell).addClass("btn btn-danger deletingFields");
                                $(deleteCell).html("Delete");
                                $(deleteCell).css("width", "40%");
                                $(deleteCell).click(function (e) {
                                    e.stopPropagation();
                                    deleteField(clickedKey, key, value, type)
                                });


                                var editCell = document.createElement("button");
                                $(editCell).addClass("btn btn-info editingFields");
                                $(editCell).html("Edit");
                                $(editCell).css("width", "40%");
                                $(editCell).attr("data-toggle","modal");
                                $(editCell).attr("id",clickedKey+splitString+key+splitString+value+splitString+type);

                                if(type==="zset")
                                $(editCell).attr("data-target","#zsetModal");
                                else
                                $(editCell).attr("data-target","#hashModal");

                                $(keyCell).html(key);
                                $(valueCell).html(value);
                                $(tr).append(keyCell);
                                $(tr).append(valueCell);
                                $(tr).append(deleteCell);
                                $(tr).append(editCell);
                                $(container).append(tr);
                            }

                        }
                        else {
                            container = document.createElement("ul");
                            $(container).attr("style", "list-style-type:none;width:100%;margin-left:5%;margin-top:5%;margin-bottom:5%;");

                            if (type === "list") {
                                $("#thirdPanel").append(valueFooter);
                                $(valueFooter).attr("id", "listValue:" + clickedKey);
                                $(addFooter).attr("id", "listAdd:" + clickedKey);
                                $(addFooter).addClass("addingListFields");
                                $("#thirdPanel").append(addFooter);
                            }
                            else if (type === "set") {
                                $("#thirdPanel").append(valueFooter);
                                $(valueFooter).attr("id", "setValue:" + clickedKey);
                                $(addFooter).attr("id", "setAdd:" + clickedKey);
                                $(addFooter).addClass("addingSetFields");
                                $("#thirdPanel").append(addFooter);
                            }

                            appendToContainer = function (key, value) {
                                var li = document.createElement("li");

                                var text = document.createElement("span");
                                $(text).html(value);
                                $(text).css("width", "30%");
                                $(text).css("float", "left");
                                $(text).attr("id", value);

                                var deleteCell = document.createElement("button");
                                $(deleteCell).addClass("btn btn-danger deletingFields");
                                $(deleteCell).html("Delete");
                                $(deleteCell).css("width", "15%");

                                var editCell = document.createElement("button");
                                $(editCell).addClass("btn btn-info editingFields");
                                $(editCell).html("Edit");
                                $(editCell).css("width", "15%");
                                $(editCell).attr("data-toggle","modal");
                                $(editCell).attr("id",clickedKey+splitString+key+splitString+value+splitString+type);


                                if (type === "string") {
                                    $(deleteCell).click(function (e) {
                                        e.stopPropagation();
                                        deleteField(clickedKey, key, value , "string")
                                    });
                                    $(editCell).attr("data-target","#stringModal");
                                }
                                else if (type === "list") {
                                    $(deleteCell).click(function (e) {
                                        e.stopPropagation();
                                        deleteField(clickedKey, key, value, "list")
                                    });
                                    $(editCell).attr("data-target","#listModal");
                                }
                                else {
                                    $(deleteCell).click(function (e) {
                                        e.stopPropagation();
                                        deleteField(clickedKey,key, value, "set")
                                    });
                                    $(editCell).attr("data-target","#setModal");
                                }

                                $(li).append(text);
                                $(li).append(deleteCell);
                                $(li).append(editCell);
                                $(container).append(li);
                            }
                        }
                        container.setAttribute("id", "value-container");
                        for (var key in json) {
                            var value = json[key];
                            if(type === "zset"){

                                appendToContainer(charCodeArrToString(value["element"]),value["score"]);
                            }
                            else
                                appendToContainer(key,value);
                        }

                        $("#keys-details-content").append(container);
                    }
                }
            }
        );
    }

);

$(document).off('click', '#start-infoSnapshotter').on('click', '#start-infoSnapshotter', function(){
    $.ajax(
        {
            url: "/view/infoSnapshotter",
            type: "POST",
            data: "shouldStartMonitor="+true,
            success : function(strData)
            {
                var monitorImage = document.getElementById(strData + ":monitorImage");
                $(monitorImage).show();
            }

        }
    );
});
$(document).off('click', '#stop-infoSnapshotter').on('click', '#stop-infoSnapshotter', function(){
    $.ajax(
        {
            url: "/view/infoSnapshotter",
            type: "POST",
            data: "shouldStartMonitor="+false,
            success : function(strData)
            {
                var monitorImage = document.getElementById(strData + ":monitorImage");
                $(monitorImage).hide();
            }
        }
    );
});


$(document).off('click', '.addingHashFields').on('click', '.addingHashFields', function() {

    var clickedKey = event.target.id.toString().substring(8);
    var field = document.getElementById("hashField:"+clickedKey).value;
    var value = document.getElementById("hashValue:"+clickedKey).value;

    fieldAdder(clickedKey,field,value,"hash");
    document.getElementById("hashField:"+clickedKey).value = "";
    document.getElementById("hashValue:"+clickedKey).value = "";
});

$(document).off('click', '.addingListFields').on('click', '.addingListFields', function() {

    var clickedKey = event.target.id.toString().substring(8);
    var field = "dummy";
    var value = document.getElementById("listValue:"+clickedKey).value;

    fieldAdder(clickedKey,field,value,"list");

    document.getElementById("listValue:"+clickedKey).value = "";
});

$(document).off('click', '.addingSetFields').on('click', '.addingSetFields', function() {

    var clickedKey = event.target.id.toString().substring(7);
    var field = "dummy";
    var value = document.getElementById("setValue:"+clickedKey).value;

    fieldAdder(clickedKey,field,value,"set");
    document.getElementById("setValue:"+clickedKey).value = "";
});

$(document).off('click', '.addingZsetFields').on('click', '.addingZsetFields', function() {

    var clickedKey = event.target.id.toString().substring(8);
    var score = document.getElementById("zsetScore:"+clickedKey).value;
    var value = document.getElementById("zsetValue:"+clickedKey).value;

    fieldAdder(clickedKey,value,score,"zset");

    document.getElementById("zsetScore:"+clickedKey).value = "";
    document.getElementById("zsetValue:"+clickedKey).value = "";
});

$(document).off("click","#show-info-button").on("click","#show-info-button",function(){
    var plot = function(pointArray){
    $('#info-body').highcharts({
        chart: {
            type: 'area'
        },
        series: [{
            data: mergeArrays(xArray,yArray),
            point: {
                events: {
                    click: function() {
                        x = this;
                        var timeStamp = this.x; // prachand bakchodi, dont know how it works
                        $.ajax(
                            {
                                url: "/view/infoAtTime",
                                type: "POST",
                                data: "timeStamp="+timeStamp,
                                success: function (strData) {
                                    var jsonData = jQuery.parseJSON(strData);
                                    container = $("#singleInfoSnapshot");
                                    if(container!==null){
                                        container.remove();
                                    }
                                    container = document.createElement("table");
                                    $(container).addClass("table table-responsive");
                                    $(container).attr("border", "1");
                                    $(container).css("overflow-y", "scroll");
                                    $(container).css("float", "right");
                                    $(container).css("margin-left", "5%");
                                    $(container).css("width", "25%");
                                    $(container).css("height", "25%");

                                    $(container).attr("id","singleInfoSnapshot");
                                    var tr = document.createElement("tr");
                                    var keyHead = document.createElement("th");
                                    var valueHead = document.createElement("th");
                                    $(keyHead).html("Field");
                                    $(valueHead).html("Value");
                                    $(tr).append(keyHead);
                                    $(tr).append(valueHead);
                                    $(container).append(tr);
                                    for(var field in jsonData){
                                        tr = document.createElement("tr");
                                        var keyCell = document.createElement("td");
                                        $(keyCell).css("width", "30%");
                                        $(keyCell).html(field);
                                        var valueCell = document.createElement("td");
                                        $(valueCell).css("width", "30%");
                                        $(valueCell).html(jsonData[field]);
                                        $(tr).append(keyCell);
                                        $(tr).append(valueCell);
                                        $(container).append(tr);
                                    }
                                    $("#info-body").append(container);
                                }

                            }
                        );
                    }
                }
            }
        }
        ],


        xAxis: [{
            type: 'datetime'
        }]
    });
}
    function mergeArrays(arr1,arr2){
        var mergedArray = new Array();
        for (index in arr1) {
            var point = [];
            point.push(arr1[index]);
            point.push(arr2[index]);
            mergedArray.push(point);
        }
        return mergedArray;
    }
    var fetchOneSeriesOfDataPoints = function(field) {
        var infoField = [];
        $.ajax(
            {
                url: "/view/infoPlotter",
                type: "POST",
                data: "field="+field,
                success: function (strData) {
                    infoField = jQuery.parseJSON(strData);
                }
            });
        return infoField;
    }
    var timeStampArray = fetchOneSeriesOfDataPoints("timeStamp");
    $("#chartTabList").each()(function() {
        var id = $(this).attr("id");
        var fieldToBePlotted = id.substr(0,id.size-"-chart".size());
        plot(timeStampArray,fetchOneSeriesOfDataPoints(fieldToBePlotted));
    });

});

$(document).off("click", ".btn.btn-info.editingFields").on("click", ".btn.btn-info.editingFields", function () {

    var myVar = $(this).attr("id").split(splitString);
    var clickedKey = myVar[0];
    var key = myVar[1];
    var value = myVar[2];
    var type = myVar[3];
    var Val1;
    var Val2;

    if(type === "string") {
        document.getElementById("stringKey").innerHTML = clickedKey;
        document.getElementById("stringValue").setAttribute("placeholder",value);

        $(document).off("click",".btn.btn-primary.finalEditingString").on("click",".btn.btn-primary.finalEditingString", function (e) {
            e.stopPropagation();
            Val1 = document.getElementById("stringValue").value;
            editField(clickedKey,key,value,key,Val1,type);
            document.getElementById("stringClose").click();
        });
//        this.setAttribute("id",clickedKey+splitString+key+splitString+Val1+splitString+type);
        document.getElementById("stringValue").value = "";
    }
    else if(type === "list")    {
        document.getElementById("listKey").innerHTML = clickedKey;
        document.getElementById("listValue").setAttribute("placeholder",value);
        $(document).off("click",".btn.btn-primary.finalEditingList").on("click",".btn.btn-primary.finalEditingList", function (e) {
            e.stopPropagation();
            Val1 = document.getElementById("listValue").value;
            editField(clickedKey,key,value,key,Val1,type);
            document.getElementById("listClose").click();
        });
//        this.setAttribute("id",clickedKey+splitString+key+splitString+Val1+splitString+type);
        document.getElementById("listValue").value = "";
    }
    else if(type === "set") {
        document.getElementById("setKey").innerHTML = clickedKey;
        document.getElementById("setValue").setAttribute("placeholder",value);

        $(document).off("click",".btn.btn-primary.finalEditingSet").on("click",".btn.btn-primary.finalEditingSet", function (e) {
            e.stopPropagation();
            Val1 = document.getElementById("setValue").value;
            editField(clickedKey,key,value,key,Val1,type);
            document.getElementById("setClose").click();
        });
//        this.setAttribute("id",clickedKey+splitString+key+splitString+Val1+splitString+type);
        document.getElementById("setValue").value = "";
    }
    else if(type === "zset")    {
        document.getElementById("zsetKey").innerHTML = clickedKey;
        document.getElementById("zsetScore").setAttribute("placeholder",value);
        document.getElementById("zsetValue").setAttribute("placeholder",key);

        $(document).off("click",".btn.btn-primary.finalEditingZset").on("click",".btn.btn-primary.finalEditingZset", function (e) {
            Val1 = document.getElementById("zsetValue").value;
            Val2 = document.getElementById("zsetScore").value;
            editField(clickedKey,key,value,Val1,Val2,type);
            document.getElementById("zsetClose").click();
        });
//        this.setAttribute("id",clickedKey+splitString+Val1+splitString+Val2+splitString+type);
        document.getElementById("zsetScore").value = "";
        document.getElementById("zsetValue").value = "";
    }
    else if(type === "hash")    {
        document.getElementById("hashKey").innerHTML = clickedKey;
        document.getElementById("hashField").setAttribute("placeholder",key);
        document.getElementById("hashValue").setAttribute("placeholder",value);
        $(document).off("click",".btn.btn-primary.finalEditingHash").on("click",".btn.btn-primary.finalEditingHash", function (e) {
            e.stopPropagation();
            Val1 = document.getElementById("hashField").value;
            Val2 = document.getElementById("hashValue").value;
            editField(clickedKey,key,value,Val1,Val2,type);
            document.getElementById("hashClose").click();
        });
//        this.setAttribute("id",clickedKey+splitString+Val1+splitString+Val2+splitString+type);
        document.getElementById("hashValue").value = "";
        document.getElementById("hashField").value = "";
    }
});

