<?php

$subpage = $SqlChecker->imtsqlclean(@$_GET["subpage"]);
$subpage = $SqlChecker->CheckGET(htmlspecialchars($subpage));

include("admin/topmenu.php");

$incsubpage = "page/admin/";

switch($subpage) {
	case "region":
		$incsubpage .= "region.php";
		break;
	case "users":
		$incsubpage .= "users.php";
		break;
	case "posts":
		$incsubpage .= "posts.php";
		break;
	case "ecosystems":
		$incsubpage .= "ecosystems.php";
		break;
	case "categories":
		$incsubpage .= "categories.php";
		break;
		
	default:
		$incsubpage .= "home.php";
		break;
		
} // switch end

if($redirect)
	header("Location: ".$incsubpage);
else if (file_exists($incsubpage))
	include($incsubpage);
else	 
	include("page/404.php");

?>