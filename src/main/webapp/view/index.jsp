<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="generator" content="Bootply" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>Redis Administrator</title>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap-theme.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap-theme.min.css">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
</head>

<body>

<div id="loginModal" class="modal show" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="position: relative; top: 100px">

            <div class="modal-header">
                <h1 class="text-center">Redis Administrator Login</h1>
            </div>

            <div class="modal-body">

                <form class="form col-md-12 center-block"  method="POST" action="view/Log.in">
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" placeholder="Username" name = "Username">
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control input-lg" placeholder="Password" name = "Password">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" type="submit">Sign In</button>
                    </div>
                </form>

            </div>

            <div class="modal-footer">
                <div class="col-md-12"></div>
            </div>
        </div>
    </div>
</div>

<!-- script references -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.0.2/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap.js"></script>
<script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>

</body>
</html>