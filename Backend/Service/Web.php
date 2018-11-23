<?php

/* display error and warning */
/*ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);*/
error_reporting(E_ALL);

$_SERVER['COMSPEC'] = "";//cmd.exe file hide
if (function_exists('header_remove')) { //php header wamp hide
    header_remove('X-Powered-By'); // PHP 5.3+
} else {
    @ini_set('expose_php', 'off');
}
@ini_set('session.cookie_httponly',1);
@ini_set('session.use_only_cookies',1);
@ini_set('default_charset', 'utf-8');
header( 'X-Frame-Options: SAMEORIGIN' );
header('Content-Type: text/html; charset=utf-8');

header("Content-Security-Policy: default-src 'none'; script-src 'self';connect-src 'self'; img-src 'self'; style-src 'self' 'unsafe-inline';");
header("X-XSS-Protection: 1; mode=block");
header('X-Content-Type-Options: nosniff');

include_once($_SERVER['DOCUMENT_ROOT']."/settings/config.php");
include_once($_SERVER['DOCUMENT_ROOT']."/module/Database/Functions/DbFunctions.php");
include_once($_SERVER['DOCUMENT_ROOT']."/module/SqlChecker/SqlChecker.php");
require("Security.php");

Class LinkedinUser {
    public $id;
	public $firstName;
	public $lastName;
	public $emailAddress;
	public $publicProfileUrl;
}

$SqlChecker = SqlChecker::instance();

if(isset($_POST) || isset($_GET)) // sql injection bypass library simple
	$SqlChecker->GetServiceChecker();

try{
	
	function DatabaseConnection()
	{
		global $config;
		if($config['dbtype'] == "Mssql")
		{
			include_once('../module/Database/Mssql/mssql.php'); 
			$database = new Mssql($config['host'],$config['port'],$config['dbname'],$config['dbuser'],$config['dbpass']);
			return $database->getDb();
		}
		elseif($config['dbtype'] == "Mysql")
		{
			include_once('../module/Database/Mysql/mysql.php'); 
			$database = new MysqlPdo($config['host'],$config['dbname'],$config['dbuser'],$config['dbpass']);
			return $database->getDb();
		}
		return NULL;
	}
	
	function getData($sql,$parameter)
	{
		$DBFunctions = DBFunctions::instance();
		
		$getd = array();
		$query = $DBFunctions->selectAll($sql);
		if (count($query) == 0)
			die(json_encode("{}"));
		else
		{
			for($i=0; $i<count($query); $i++)
			{
				$data = $DBFunctions->PDO_fetch_array($query, $i);
				for($j = 0; $j < count($parameter); $j++)
				{
					$val = $data[$parameter[$j]];
					$ret[$parameter[$j]] = $val;
				}
				
				array_push($getd, $ret);
			}
		}
		die(json_encode($getd,JSON_UNESCAPED_UNICODE));
	}

	/* variable class */
	$db = DatabaseConnection();
	if($db == NULL)
		die("Database Connection Problem !");
	
	$process = @$_GET["process"];
	
	header('Content-type: application/json');
	
	if($process == "getCategories")
	{
		$groupid = @$_GET["groupid"];
		if(empty($groupid))
			print_r(json_encode(array("Function"=>"Service->getCategories","status"=>"groupid Null variable")));
		else
			getData("SELECT c.seourl as seourl,COALESCE(fw.setting,'{\"follower\":[]}') as follower,COUNT(p.id) as posts, c.id as id,c.name as name,c.orderindex as orderindex,c.groupid as groupid FROM (category as c LEFT JOIN follower as fw ON c.id = fw.categoryid) LEFT JOIN posts as p on c.id = p.categoryid WHERE c.groupid = $groupid GROUP BY c.seourl ",array('id','name','orderindex','groupid','seourl','follower','posts'));
	}
	elseif($process == "getAllData")
		getData("SELECT id,name,orderindex,type,groupid,seourl FROM category",array('id','name','orderindex','groupid','type','seourl'));
	elseif($process == "getRegion")
		getData("SELECT id,name,orderindex,groupid,seourl FROM category WHERE type='region'",array('id','name','orderindex','groupid','seourl'));
	elseif($process == "getEcosystem")
	{
		$groupid = @$_GET["groupid"];
		if(empty($groupid))
			print_r(json_encode(array("Function"=>"Service->getEcosystem","status"=>"groupid Null variable")));
		else
		getData("SELECT id,name,orderindex,groupid,seourl FROM category WHERE type='ecosystems' and groupid = $groupid",array('id','name','orderindex','groupid','seourl'));
	}
	elseif($process == "getFollowers")
	{
		$seourl = @$_GET["seourl"];
		if(empty($seourl))
			print_r(json_encode(array("Function"=>"Service->getFollowers","status"=>"seourl Null variable")));
		else
		{
			$DBFunctions = DBFunctions::instance();
			$data = $DBFunctions->getFollower($seourl);
			print_r($data);
		}
	}
	elseif($process == "getPosts")
	{
		$seourl = @$_GET["seourl"];
		if(empty($seourl))
			print_r(json_encode(array("Function"=>"Service->getPosts","status"=>"seourl Null variable")));
		else
		{
			$DBFunctions = DBFunctions::instance();
			$data = $DBFunctions->getPosts($seourl);
			print_r($data);
		}
	}
	elseif($process == "getPostsMe")
	{
		$authid = @$_GET["authid"];
		if(empty($authid))
			print_r(json_encode(array("Function"=>"Service->getPostsMe","status"=>"authid Null variable")));
		else
		{
			$DBFunctions = DBFunctions::instance();
			$data = $DBFunctions->getPostsMe($authid);
			print_r($data);
		}
	}
	elseif($process == "deletePostsMe")
	{
		$authid = @$_GET["authid"];
		$seourl = @$_GET["seourl"];
		
		if(empty($authid))
			print_r(json_encode(array("Function"=>"Service->deletePostsMe","status"=>"authid Null variable")));
		elseif(empty($seourl))
			print_r(json_encode(array("Function"=>"Service->deletePostsMe","status"=>"seourl Null variable")));
		else
		{
			$DBFunctions = DBFunctions::instance();
			$data = $DBFunctions->deletePostsMe($authid,$seourl);
			print_r($data);
		}
	}
	elseif($process == "getAllPosts")
	{
		$DBFunctions = DBFunctions::instance();
		$data = $DBFunctions->getAllPosts();
		print_r($data);
	}
	elseif($process == "getMeFollowCategories")
	{
		$authid = @$_GET["authid"];
		if(empty($authid))
			print_r(json_encode(array("Function"=>"Service->getMeFollowCategories","status"=>"authid Null variable")));
		else
		{
			$DBFunctions = DBFunctions::instance();
			$data = $DBFunctions->getMeFollowCategories($authid);
			print_r($data);
		}
	}
	elseif($process == "getPostsFollow")
	{
		$authid = @$_GET["authid"];
		if(empty($authid))
			print_r(json_encode(array("Function"=>"Service->getPostsFollow","status"=>"authid Null variable")));
		else
		{
			$DBFunctions = DBFunctions::instance();
			$data = $DBFunctions->getPostsFollow($authid);
			print_r($data);
		}
	}
	elseif($process == "setOneSignalUser")
	{
		$authid = @$_GET["authid"];
		$userid = @$_GET["userid"];
		if(empty($authid))
			print_r(json_encode(array("Function"=>"Service->setOneSignalUser","status"=>"authid Null variable")));
		elseif(empty($userid))
			print_r(json_encode(array("Function"=>"Service->setOneSignalUser","status"=>"userid Null variable")));
		else
		{
			$DBFunctions = DBFunctions::instance();
			$data = $DBFunctions->setOneSignalUser($authid,$userid);
			print_r($data);
		}
	}
	elseif($process == "setPosts")
	{
		$authid = @$_POST["authid"];
		$image = @$_FILES["image"];
		$title = @$_POST["title"];
		$description = @$_POST["description"];
		$seourl = @$_POST["seourl"];

		if(empty($authid))
			print_r(json_encode(array("Function"=>"Service->setPosts","status"=>"authid Null variable")));
		elseif(empty($image))
			print_r(json_encode(array("Function"=>"Service->setPosts","status"=>"image Null variable")));
		elseif(empty($title))
			print_r(json_encode(array("Function"=>"Service->setPosts","status"=>"title Null variable")));
		elseif(empty($description))
			print_r(json_encode(array("Function"=>"Service->setPosts","status"=>"description Null variable")));
		elseif(empty($seourl))
			print_r(json_encode(array("Function"=>"Service->setPosts","status"=>"seourl Null variable")));
		else
		{
			$DBFunctions = DBFunctions::instance();
			$post = json_encode(array("authid" => $authid,"image" => $image,"title" => $title,"description" => $description,"seourl" => $seourl),JSON_UNESCAPED_UNICODE);
			print_r($DBFunctions->setPosts($post));
		}
	}
	elseif($process == "categoriesFollow")
	{
		$authid = @$_GET["authid"];
		$catid = @$_GET["catid"];
		$type = @$_GET["type"];
		
		if(empty($authid))
			print_r(json_encode(array("Function"=>"Service->categoriesFollow","status"=>"authid Null variable")));
		elseif(empty($catid))
			print_r(json_encode(array("Function"=>"Service->categoriesFollow","status"=>"catid Null variable")));
		elseif(empty($type))
			print_r(json_encode(array("Function"=>"Service->categoriesFollow","status"=>"type Null variable")));
		else
		{
			$DBFunctions = DBFunctions::instance();
			print_r($DBFunctions->categoriesFollow($authid,$catid,$type));
		}
	}
	elseif($process == "isLogin")
	{
		if(empty(@$_GET['json']))
			die(print_r(json_encode(array("Function"=>"Service->isLogin","status"=>"json Null variable"))));
		
		$json = json_decode(@$_GET['json'], JSON_UNESCAPED_UNICODE);

		if(empty($json["json"]["emailAddress"]))
			print_r(json_encode(array("Function"=>"Service->isLogin","status"=>"json->emailAddress Null variable")));
		elseif(empty($json["json"]["firstName"]))
			print_r(json_encode(array("Function"=>"Service->isLogin","status"=>"json->firstName Null variable")));
		elseif(empty($json["json"]["lastName"]))
			print_r(json_encode(array("Function"=>"Service->isLogin","status"=>"json->lastName Null variable")));
		elseif(empty($json["json"]["publicProfileUrl"]))
			print_r(json_encode(array("Function"=>"Service->isLogin","status"=>"json->publicProfileUrl Null variable")));
		elseif(empty($json["json"]["authId"]))
			print_r(json_encode(array("Function"=>"Service->isLogin","status"=>"json->authId Null variable")));
		else
		{
			$emailAddress = $json["json"]["emailAddress"];
			$firstName = $json["json"]["firstName"];
			$lastName = $json["json"]["lastName"];
			$publicProfileUrl = $json["json"]["publicProfileUrl"];
			$authId = $json["json"]["authId"];
			
			$user = new LinkedinUser();
			$user->id = $authId;
			$user->emailAddress = $emailAddress;
			$user->firstName = $firstName;
			$user->lastName = $lastName;
			$user->publicProfileUrl = $publicProfileUrl;

			$DBFunctions = DBFunctions::instance();
			
			$data = $DBFunctions->checkLinkedinUser($user);
			if(!$data['authid'])
				print_r(json_encode(array("Function"=>"Service->isLogin","status"=>"DbFunctions->checkLinkedinUser->authId Null variable")));
			else
			{
				$datainf = json_decode($data['information'], JSON_UNESCAPED_UNICODE);
				
				$authortiyId = $datainf['authority'];
				$loginid = $data['authid'];
				
				$authQuery = $DBFunctions->selectAll("SELECT name,information FROM authority WHERE auth = $authortiyId");
				if (count($authQuery) > 0) {
					$authData = $DBFunctions->PDO_fetch_array($authQuery, 0);
					$authDataInf = json_decode($authData['information'], JSON_UNESCAPED_UNICODE);
					if($authData['name'] == "banned")
						die(print_r(json_encode(array("Function"=>"Service->isLogin","status"=>false,"message"=>"banned user"))));
					
					$login['authName'] = $authData['name'];
					$login['authProcess'] = $authDataInf;
				}
				else
					die(print_r(json_encode(array("Function"=>"Service->isLogin","status"=>false,"message"=>"unknown authority"))));
				
				print_r(json_encode(array("Function"=>"Service->isLogin","status"=>true,"message"=>"success login","sessId"=>$loginid,"authName"=>$authData['name'],"authProcess"=>$authDataInf)));
			}
		}
	}
	else
		die(json_encode(array("Function"=>"Unknown Function")));
	
}
catch(Exception $e){
	print "Error!: " . $e->getMessage() . "<br/>";
	die();
}
?>