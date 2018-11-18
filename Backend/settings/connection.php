<?php

error_reporting(E_ALL ^ E_NOTICE);
session_start();

/* Turkish character utf8 settings */
header("Content-Type: text/html; charset=utf-8");

/* Default Timezone */
date_default_timezone_set('Europe/Istanbul');

/* Log module include */
include_once('logs/logs.php');
/* Ip Module include */
include_once("./module/User/IpInfo.php");
/* Seo Module include */
include_once("./module/Seo/Seo.php");
/* Seo Module include */
include_once("./module/SqlChecker/SqlChecker.php");
/* Db Function Module include */
include_once("./module/Database/Functions/DbFunctions.php");

/* config settings */
include_once('config.php');

function DatabaseConnection()
{
	global $config;
	if($config['dbtype'] == "Mssql")
	{
		include_once('./module/Database/Mssql/mssql.php'); 
		$database = new Mssql($config['host'],$config['port'],$config['dbname'],$config['dbuser'],$config['dbpass']);
		return $database->getDb();
	}
	elseif($config['dbtype'] == "Mysql")
	{
		include_once('./module/Database/Mysql/mysql.php'); 
		$database = new MysqlPdo($config['host'],$config['dbname'],$config['dbuser'],$config['dbpass']);
		return $database->getDb();
	}
	return NULL;
}

/* variable class */
$db = DatabaseConnection();
if($db == NULL)
	die("Database Connection Problem !");

$SqlChecker = SqlChecker::instance();
$IpFunction = IpInfo::instance();
$DBFunctions = DBFunctions::instance();
$SeoFunction = SeoFunction::instance();

$browserlang = $IpFunction->browserLanguage();
if(!file_exists($_SERVER['DOCUMENT_ROOT']."/lang/".$browserlang.".php"))
	include($_SERVER['DOCUMENT_ROOT']."/lang/en.php");
else
	include($_SERVER['DOCUMENT_ROOT']."/lang/".$browserlang.".php");

class User{ 
	public $id;
    public $phone;
    public $name;
    public $surname;
    public $email;
	public $username;
	public $authority;
}


if(empty($_SESSION['user']))
	$isOnline = false;
else
{
	if($_SESSION['user']->authority == "authproblem")
		$isOnline = false;
	elseif($_SESSION['user']->authority == "admin")
		$isAdmin = true;
	
	$isOnline = true;
}

?>