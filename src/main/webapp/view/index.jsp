<!DOCTYPE html>
<html style="width: 100%; height: 100%">
<head lang="en">
    <meta charset="UTF-8">
    <meta name="generator" content="Bootply" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>Redis Administrator</title>
    <link rel="stylesheet" type="text/css" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <link rel="stylesheet" type="text/css" href="css/alertify.default.css">
    <link rel="stylesheet" type="text/css" href="css/alertify.core.css">
    <link rel="stylesheet" type="text/css" href="css/alertify.bootstrap.css">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

</head>

<body style="width: 100%; height: 100%">
<div style="width: 100%; height: 100%" >
<div id="loginModal" style="height:100%" class="col-lg-7" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" style="width: 100%;height: 100%;bottom:20%">
        <div class="modal-content" style="position: absolute; top: 40%;left: 25%">

            <div class="modal-header">
                <h1 class="text-center">Redis Administrator Login</h1>
            </div>

            <div class="modal-body">

                <form name = "loginForm" class="form col-md-12 center-block"  method="POST" action="Log.in">
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" placeholder="Username" name = "Username" onchange="return nameValid()">
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control input-lg" placeholder="Password" name = "Password">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" type="submit" onclick="return formValidator()">Sign In</button>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <div class="col-md-12"></div>
            </div>
        </div>

    </div>
</div>

<div id="signUpModal" style="height:100%" class="col-lg-5" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" style="width: 100%;height: 100%;bottom:20%">
        <div class="modal-content" style="position: absolute; top: 40%;left: 25%">

            <div class="modal-header">
                <h1 class="text-center">Signup</h1>
            </div>

            <div class="modal-body">

                <form name = "signupForm" class="form col-md-12 center-block"  method="POST" action="Sign.up">
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" placeholder="Username" name = "User">
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control input-lg" placeholder="Password" name = "Pass">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" type="submit">Sign Up</button>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <div class="col-md-12"></div>
            </div>
        </div>

    </div>
</div>
</div>
<!-- script references -->

<script type="text/javascript">
nameValid = function nameValidator()    {

    var regexAlphaNumeric = /^[0-9a-zA-Z]+$/;
    var nameField = document.loginForm.Username;

    if(nameField.value.match(regexAlphaNumeric))   {
        alertify.success("Allowed input");
        return true;
    }
    else
    {
        alertify.error("Only alphanumeric allowed");
        return false;
    }
}
function formValidator()    {
    if(nameValid())
        return true;
    else {
        alertify.alert("Form not filled correctly.");
        return false;
    }
}

</script>
<!-- script references -->
<script src="js/alertify.js"></script>
<script src="js/alertify.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>

</body>
</html>