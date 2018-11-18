<?php 
ob_start(); 
//php security
$_SERVER['COMSPEC'] = "";//cmd.exe file hide
if (function_exists('header_remove')) { //php header wamp hide
    header_remove('X-Powered-By'); // PHP 5.3+
} else {
    @ini_set('expose_php', 'off');
}
@ini_set('session.cookie_httponly',1);
@ini_set('session.use_only_cookies',1);
header( 'X-Frame-Options: SAMEORIGIN' );

/* Connection module include */
include_once("./settings/connection.php");

if($isOnline) /* get Authority process */
{
	$auth = $DBFunctions->checkAuthorityUser($_SESSION['user']->id,$response);
	if($_SESSION['user']->authority != $auth)
		header("location: /logout.html");

	//print_r($response); user settings show
	
	if($response && ($response['login'] == 0))
		header("location: /logout.html");
	
	if($response && ($response['createpost'] == 1)) //publisher skill
		$isPublisher = true;
}

?>
<!DOCTYPE html>
<html lang="en" dir="ltr">
	
	