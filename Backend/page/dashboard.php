<?php

$subpage = $SqlChecker->imtsqlclean(@$_GET["subpage"]);
$subpage = $SqlChecker->CheckGET(htmlspecialchars($subpage));

$incsubpage = "page/user/";

switch($subpage) {
	case "region":
		$incsubpage .= "region.php";
		break;
	case "ecosystem":
		$incsubpage .= "ecosystem.php";
		break;
	case "category":
		$incsubpage .= "category.php";
		break;
	case "categoryposts":
		$incsubpage .= "categoryposts.php";
		break;
	case "posts":
		$incsubpage .= "posts.php";
		break;
	case "follower":
		$incsubpage .= "follower.php";
		break;
		
	default:
		$incsubpage .= "region.php";
		break;
		
} // switch end

if($redirect)
	header("Location: ".$incsubpage);
else if (file_exists($incsubpage))
	include($incsubpage);
else	 
	include("page/404.php");

?>