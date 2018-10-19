<?php 
ob_start(); 
session_start(); 

$_SESSION['user'] = '';
$_SESSION['token'] = '';
$_SESSION['OAUTH_ACCESS_TOKEN'] = '';

if($_SESSION['user'])
	unset($_SESSION['user']);

if($_SESSION['token'])
	unset($_SESSION['token']);

if($_SESSION['OAUTH_ACCESS_TOKEN'])
	unset($_SESSION['OAUTH_ACCESS_TOKEN']);

if(!empty($_SESSION))
	unset($_SESSION);
	
session_destroy();
setcookie("token","",time()-1);

header("location:/index.html");

ob_end_flush();

?>