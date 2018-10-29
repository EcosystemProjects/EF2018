<?php

/* display error and warning */
/*ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);*/
error_reporting(E_ALL);

include_once("../settings/config.php");
include_once("../module/Database/Functions/DbFunctions.php");
require("Security.php");

Class LinkedinUser {
    public $id;
	public $firstName;
	public $lastName;
	public $emailAddress;
	public $publicProfileUrl;
}


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
		header('Content-type: application/json');
		
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
		die(json_encode($getd));
	}

	/* variable class */
	$db = DatabaseConnection();
	if($db == NULL)
		die("Database Connection Problem !");
	
	$process = @$_REQUEST["process"];
	
	if($process == "getCategories")
	{
		$groupid = @$_REQUEST["groupid"];
		getData("SELECT c.seourl as seourl,COALESCE(fw.setting,'{\"follower\":[]}') as follower,COUNT(p.id) as posts, c.id as id,c.name as name,c.orderindex as orderindex,c.groupid as groupid FROM (category as c LEFT JOIN follower as fw ON c.id = fw.categoryid) LEFT JOIN posts as p on c.id = p.categoryid WHERE c.groupid = $groupid GROUP BY c.seourl ",array('id','name','orderindex','groupid','seourl','follower','posts'));
	}
	elseif($process == "getAllData")
		getData("SELECT id,name,orderindex,type,groupid,seourl FROM category",array('id','name','orderindex','groupid','type','seourl'));
	elseif($process == "getRegion")
		getData("SELECT id,name,orderindex,groupid,seourl FROM category WHERE type='region'",array('id','name','orderindex','groupid','seourl'));
	elseif($process == "getEcosystem")
	{
		$groupid = @$_REQUEST["groupid"];
		getData("SELECT id,name,orderindex,groupid,seourl FROM category WHERE type='ecosystems' and groupid = $groupid",array('id','name','orderindex','groupid','seourl'));
	}
	elseif($process == "getFollowers")
	{
		$seourl = @$_REQUEST["seourl"];
		
		$DBFunctions = DBFunctions::instance();
		
		$data = $DBFunctions->getFollower($seourl);
		print_r($data);
	}
	elseif($process == "getPosts")
	{
		$seourl = @$_REQUEST["seourl"];
		
		$DBFunctions = DBFunctions::instance();
		
		$data = $DBFunctions->getPosts($seourl);
		print_r($data);
	}
	elseif($process == "getAllPosts")
	{
		$DBFunctions = DBFunctions::instance();
		
		$data = $DBFunctions->getAllPosts();
		print_r($data);
	}
	elseif($process == "getPostsFollow")
	{
		$authid = @$_REQUEST["authid"];
		
		$DBFunctions = DBFunctions::instance();
		
		$data = $DBFunctions->getPostsFollow($authid);
		print_r($data);
	}
	elseif($process == "isLogin")
	{
		header('Content-type: application/json');
		
		$json = json_decode(@$_REQUEST['json'], true);
		
		$emailAddress = $json["json"]["emailAddress"];
		$firstName = $json["json"]["firstName"];
		$lastName = $json["json"]["lastName"];
		$publicProfileUrl = $json["json"]["publicProfileUrl"];
		$authId = $json["json"]["authId"];
		
		$login = array(
		"isLogin" => 0,
		"authName" => "",
		"authProcess" => "",
		"sessId" => 0,
		);
		
		if(empty($emailAddress) || empty($firstName) || empty($lastName) || empty($publicProfileUrl) || empty($authId))
			die(json_encode($login));
		
		$user = new LinkedinUser();
		$user->id = $authId;
		$user->emailAddress = $emailAddress;
		$user->firstName = $firstName;
		$user->lastName = $lastName;
		$user->publicProfileUrl = $publicProfileUrl;

		$DBFunctions = DBFunctions::instance();
		
		$data = $DBFunctions->checkLinkedinUser($user);
		if(!$data['authid'])
			die(json_encode($login));
		else
		{
			$datainf = json_decode($data['information'], true);
			
			
			
			$authortiyId = $datainf['authority'];
			$loginid = $data['authid'];
			
			$authQuery = $DBFunctions->selectAll("SELECT name,information FROM authority WHERE auth = $authortiyId");
			if (count($authQuery) > 0) {
				$authData = $DBFunctions->PDO_fetch_array($authQuery, 0);
				$authDataInf = json_decode($authData['information'], true);
				if($authData['name'] == "banned")
					$login['isLogin'] = 0;
				else
					$login['isLogin'] = 1;
				
				$login['authName'] = $authData['name'];
				$login['authProcess'] = $authDataInf;
			}
			else
			{
				$login['isLogin'] = 0;
				$login['authName'] = "authproblem";
				$login['authProcess'] = "";
			}
			
			$login['sessId'] = $loginid;
			
			die(json_encode($login));
		}
	}
	else
	{
		header('Content-type: application/json');
		die(json_encode("{}"));
	}
	
}
catch(Exception $e){
	print "Error!: " . $e->getMessage() . "<br/>";
	die();
}
?>
