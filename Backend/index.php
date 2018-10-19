<?php

/* *******************************
**** beratkarabusra@gmail.com ****
*********** BERAT KARA ***********
************** 2018 **************
*************** _ ****************
******************************* */

include_once("page/header.php");

$page = $SqlChecker->imtsqlclean(@$_GET["page"]);
$page = $SqlChecker->CheckGET(htmlspecialchars($page));

if($page != "admin")
	include_once("page/user/topmenu.php");

$redirect = false;

$incpage = "page/";

switch($page) {  
	case "linkedin":
		if($isOnline)
		{
			$incpage = "dashboard.html";
			$redirect = true;
		}
		else
			$incpage .= "linkedin.php";
		break;
	case "logout":
		if(!$isOnline)
		{
			$incpage = "homepage.html";
			$redirect = true;
		}
		else
			$incpage .= "logout.php";
		break;
	case "dashboard":
			$incpage .= "dashboard.php";
		break;
	case "homepage":
		if($isOnline)
		{
			$incpage = "home.html";
			$redirect = true;
		}
		else
			$incpage .= "homepage.php";
		break;
	case "home":
		if(!$isOnline)
		{
			$incpage = "homepage.html";
			$redirect = true;
		}
		else
			$incpage .= "home.php";
		break;
	case "settings":
		if(!$isOnline)
		{
			$incpage = "homepage.html";
			$redirect = true;
		}
		else
			$incpage .= "settings.php";
		break;
	case "createpost":
		if(!$isOnline)
		{
			$incpage = "homepage.html";
			$redirect = true;
		}
		else if($isPublisher)
			$incpage .= "createpost.php";
		else
		{
			$incpage = "home.html";
			$redirect = true;
		}
		break;
	case "admin":
		if($isOnline && $isAdmin)
			$incpage .= "admin.php";
		else
			$incpage .= "linkedin.php";
		break;
		
	default:
		if($isOnline)
		{
			if($_SESSION["user"]->authority == "admin")
				$incpage .= "admin.php";
			elseif($_SESSION["user"]->authority == "authproblem")
				$redirect = "index.html";
			else
				$incpage .= "home.php";
		}
		else
		{
			$incpage = "homepage.html"; //login.html admin girişi için
			$redirect = true;
		}
		break;
		
} // switch end

if($page != "homepage" && $page != "admin")
	include("page/menu.php");

if($redirect)
	header("Location: ".$incpage);
else if (file_exists($incpage))
	include($incpage);
else	 
	include("page/404.php");

include_once("page/footer.php");

?>	  
