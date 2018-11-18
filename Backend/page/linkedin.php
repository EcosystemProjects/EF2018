<?php
ob_start(); 
session_start();
if($isOnline)
	header("Location:/index.html");

/* Linkedin module include */
include_once("./module/LinkedIn/http.php");
include_once("./module/LinkedIn/oauth_client.php");

session_regenerate_id();

if (isset($_GET["oauth_problem"]) && $_GET["oauth_problem"] <> "") {
  $_SESSION["err_msg"] = $_GET["zoauth_problem"];
  header("location:/index.html");
  exit;
}

$client = new oauth_client_class;

$client->debug = false;
$client->debug_http = true;
$client->redirect_uri = $config['callback_url'];

$client->client_id = $config['linkedin_access'];
$application_line = __LINE__;
$client->client_secret = $config['linkedin_secret'];

if (strlen($client->client_id) == 0 || strlen($client->client_secret) == 0)
  die('Please go to LinkedIn Apps page https://www.linkedin.com/secure/developer?newapp= , '.
			'create an application, and in the line '.$application_line.
			' set the client_id to Consumer key and client_secret with Consumer secret. '.
			'The Callback URL must be '.$client->redirect_uri).' Make sure you enable the '.
			'necessary permissions to execute the API calls your application needs.';

$client->scope = $config['linkedin_scope'];
if (($success = $client->Initialize())) {
  if (($success = $client->Process())) {
    if (strlen($client->authorization_error)) {
      $client->error = $client->authorization_error;
      $success = false;
    } elseif (strlen($client->access_token)) {
      $success = $client->CallAPI(
					'http://api.linkedin.com/v1/people/~:(id,email-address,first-name,last-name,location,picture-url,public-profile-url,formatted-name)', 
					'GET', array(
						'format'=>'json'
					), array('FailOnAccessError'=>true), $getUser);
    }
  }
  $success = $client->Finalize($success);
}

if ($client->exit) exit;

if ($success)
{
	if($_SESSION['OAUTH_ACCESS_TOKEN'])
		unset($_SESSION['OAUTH_ACCESS_TOKEN']);
	
	if(!empty($getUser))
	{
		global $DBFunctions;
		$data = $DBFunctions->checkLinkedinUser($getUser);
		if($data['authid'])
		{
			global $IpFunction;
			global $db;
			
			$datainf = json_decode($data['information'], JSON_UNESCAPED_UNICODE);
				
			if (empty($datainf)) {
				echo '<div class="alert alert-danger"><strong>Hata !</strong> Sistem Hatası .</div>';
			} else if ($datainf['authority'] == -1) {
				echo '<div class="alert alert-danger"><strong>Hata !</strong> Hesabınızın Girişi Yasaklanmıştır .</div>';
			} else {
				$_SESSION['token'] = sha1(time() . rand() . $IpFunction->getIp() . $_SERVER['SERVER_NAME']);
				setcookie("token", $_SESSION['token'], time() + (60 * 60 * 24));
				
				$userId = $data['id'];
				
				$UserInformation = new User();
				
				$UserInformation->id        = $userId;
				$UserInformation->name      = $datainf['name'];
				$UserInformation->surname   = $datainf['surname'];
				$UserInformation->email     = $data['email'];
				$UserInformation->phone     = $data['phone'];
				$UserInformation->username  = $data['username'];
					
				$authortiyId = $datainf['authority'];
				
					$authQuery = $DBFunctions->selectAll("select name,information FROM authority where auth = $authortiyId");
					if (count($authQuery) > 0) {
						$authData = $DBFunctions->PDO_fetch_array($authQuery, 0);
						$authDataInf = json_decode($authData['information'], JSON_UNESCAPED_UNICODE);
						$UserInformation->authority = $authData['name']; // auth yetkilerini alıp kontrolleri gerçekleştirilecek
					}
					else
					{
						$UserInformation->authority = "authproblem";
					}
						
				$getIp = $IpFunction->getIp();
				$getCountry = $IpFunction->getIpInfo("Visitor", "Country");
				if(empty($getCountry))
					$getCountry = "Unknown";
				
				$getCity = $IpFunction->getIpInfo("Visitor", "City");
				if(empty($getCity))
				{
					$getCity = $IpFunction->getIpInfo($getIp, "geolocation");
					if(empty($getCity))
						$getCity = "Unknown";
				}
				
				$getLanguage = $IpFunction->getIpInfo("Visitor", "Country Code");
				if(empty($getLanguage))
					$getLanguage = "default";
				
				$location = array(
				"ip"=>$getIp,
				"country"=>$getCountry,
				"city"=>$getCity,
				"language"=>$getLanguage,
				"time"=>time()
				);
				
				$dataip["lastonline"] = time();
				$datainf['location'] = $location;
				$dataencode = json_encode($datainf,JSON_UNESCAPED_UNICODE);

				$db->query("UPDATE users SET information = '$dataencode' WHERE id = '$userId'");

				$_SESSION['user'] = $UserInformation;
				
				if($_SESSION['OAUTH_ACCESS_TOKEN'])
					unset($_SESSION['OAUTH_ACCESS_TOKEN']);
				
				if($UserInformation->authority == "admin")
				{
					$isAdmin = true;
					$isOnline = true;
					header("Location: admin.html");
				}
				else if($UserInformation->authority == "publisher" || $UserInformation->authority == "reader")
				{
					header("Location: home.html");
				}
				else if($UserInformation->authority == "authproblem")
				{
					header("Location: logout.html");
				}
			}
			
		}
	}
}

header("location:/homepage.html");
?>