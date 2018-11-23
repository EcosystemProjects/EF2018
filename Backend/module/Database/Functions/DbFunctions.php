<?php

class DBFunctions
{
	protected static $_instance = null;

    public static function instance() {
        
        if ( !isset( self::$_instance ) ) {
            
            self::$_instance = new DBFunctions();
            
        }
        
        return self::$_instance;
    }
    

    protected function __construct() {}
    
    function __destruct(){}
	
	public function selectAll( $query )
	{
		global $db;
		$res = $db->query( $query )->fetchAll();
		return $res;
	}

	public function PDO_fetch_Array( $array, $i ) 
	{
		$res = array();
		foreach( $array[$i] as $key=>$val )
			$res[$key]=$val;
		return $res;    
	}
	
	public function checkLinkedinUser($user)
	{
		$uid = $user->id;
		$email = $user->emailAddress;
		$firstname = $user->firstName;
		$lastname = $user->lastName;
		$linkedinprofile = $user->publicProfileUrl;
		$username = $firstname.$lastname.$uid;
		
		if(empty($uid))
			return NULL;
		elseif(empty($email))
			return NULL;
		elseif(empty($firstname))
			return NULL;
		elseif(empty($lastname))
			return NULL;
		elseif(empty($linkedinprofile))
			return NULL;
		else
		{

			$check = $this->selectAll("SELECT authid,email,phone,username,id,information FROM users WHERE authid = '".$uid."' OR email = '".$email."'");
			if(count($check) > 0){
				
				return $this->PDO_fetch_Array($check, 0);
				
			}else{
				
				global $db;
				global $IpFunction;
				
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

				$information = array(
				"name"=>$firstname,
				"surname"=>$lastname,
				"password"=>"",
				"emailpush"=>1,
				"mobilepush"=>1,
				"onesignalid"=>"",
				"authority"=>0,
				"signup"=>time(),
				"otpCode"=>0,
				"linkedin"=>$linkedinprofile,
				"lastonline"=>time(),
				"location"=>$location
				);
				
				$stmt = $db->prepare ("INSERT INTO users (username,email,authid,information) VALUES (:username,:email,:authid,:information)");
				$stmt->execute(array(
				"username" => $username,
				"email" => $email,
				"authid" => $uid,
				"information" => json_encode($information,JSON_UNESCAPED_UNICODE )
				));
				
				$this->checkLinkedinUser($user);
				/*
				$string =json_encode($input, JSON_UNESCAPED_UNICODE) ; 
				echo $decoded = html_entity_decode( $string );
				*/
			}
		}
	}
	
	public function checkAuthorityUser($user,&$response)
	{
		global $db;
		$check = $this->selectAll("SELECT information FROM users WHERE id = '".$user."'");
		if(count($check) > 0){
			$data = $this->PDO_fetch_Array($check, 0);
			$information = json_decode($data['information'],JSON_UNESCAPED_UNICODE);
			
			$authortiyId = $information['authority'];
			
			$authQuery = $this->selectAll("select name,information FROM authority where auth = $authortiyId");
			if (count($authQuery) > 0) {
				$authData = $this->PDO_fetch_array($authQuery, 0);
				$response = json_decode($authData['information'], JSON_UNESCAPED_UNICODE);
				return $authData['name'];
			}
			else
			{
				$response = array("login"=>0);
				return "authproblem";
			}
		}
	}
	
	function seo($text)
	{
		$find = array('Ç', 'Ş', 'Ğ', 'Ü', 'İ', 'Ö', 'ç', 'ş', 'ğ', 'ü', 'ö', 'ı', '+', '#');
		$replace = array('C', 'S', 'G', 'U', 'I', 'O', 'c', 's', 'g', 'u', 'o', 'i', 'plus', 'sharp');
		$text = str_replace($find, $replace, $text);
		$text = preg_replace("@[^A-Za-z0-9\-_\.\+]@i", ' ', $text);
		$text = trim(preg_replace('/\s+/', ' ', $text));
		$text = str_replace(' ', '-', $text);
		return $text;
	}
	
	function random_string($length){
		return substr(str_repeat(md5(rand()), ceil($length/32)), 0, $length);
	}
	
	public function setPosts($post)
	{
		global $db;
		global $SeoFunction;
		
		$post = json_decode($post);
		
		$authid = $post->authid;
		$image = $post->image;
		$title = $post->title;
		$description = $post->description;
		$seourl = $post->seourl;
		
		if(empty($authid))
			return json_encode(array("Function"=>"DbFunctions->setPosts","status"=>"authid empty"));
		elseif(empty($image))
			return json_encode(array("Function"=>"DbFunctions->setPosts","status"=>"image empty"));
		elseif(empty($title))
			return json_encode(array("Function"=>"DbFunctions->setPosts","status"=>"title empty"));
		elseif(empty($description))
			return json_encode(array("Function"=>"DbFunctions->setPosts","status"=>"description empty"));
		elseif(empty($seourl))
			return json_encode(array("Function"=>"DbFunctions->setPosts","status"=>"seourl empty"));
		
		$titleseo = $this->seo($title)."-".$this->random_string(5);
		
		$userid = $this->getExistUser($authid);
		if($userid != -1)
		{
			$images;
			if(isset($image)){
				$hata = $image->error;
					if($hata == 0){
						$boyut = $image->size;
						if($boyut > (1024*1024*5)){
							return json_encode(array("Function"=>"DbFunctions->setPosts","status"=>"Image Size > 5MB"));
						}else{
							$tip = $image->type;
							$isim = $image->name;
							$uzanti = explode('.', $isim);
							$uzanti = $uzanti[count($uzanti)-1];
							if(($tip != 'image/jpeg' || $uzanti != 'jpg') && ($tip != 'image/png' || $uzanti != 'png')) {
								return json_encode(array("Function"=>"DbFunctions->setPosts","status"=>"Unsupported picture type"));
							} else {
								$images = $titleseo."-".$this->random_string(50).".png";
								copy($image->tmp_name, $_SERVER['DOCUMENT_ROOT'].'/assets/img/posts/'.$images);
							}
						}
					}
			}
				
			$date = date('d-m-Y H:i:s');
					
			$information = array(
				'title' => $title,
				'description' => $description,
				'image' => (empty($images) ? '' : $images),
				'date' => $date
			);
					
			$notification = array(
				'mail' => 0,
				'app' => 0
			);
					
			$catquery = $this->selectAll("SELECT id FROM category WHERE type='categories' and seourl='$seourl'");

			$catid = -1;
			if (count($catquery) == 0) {
				return json_encode(array("Function"=>"DbFunctions->setPosts","status"=>"Category Not Exists !"));
			} else {
				$catdata = $this->PDO_fetch_array($catquery, 0);
				$catid = $catdata['id'];
			}
			
			$stmt = $db->prepare ("INSERT INTO posts (sender,categoryid,information,notification,seourl,status) VALUES (:sender,:categoryid,:information,:notification,:seourl,:status)");
			$stmt->execute(array(
			"sender" => $userid,
			"categoryid" => $catid,
			"information" => json_encode($information,JSON_UNESCAPED_UNICODE),
			"notification" => json_encode($notification,JSON_UNESCAPED_UNICODE),
			"seourl" => $titleseo,
			"status" => 1
			));
					
			if($stmt)
				return json_encode(array("Function"=>"DbFunctions->setPosts","status"=>"Create Post Success !"));
			else
				return json_encode(array("Function"=>"DbFunctions->setPosts","status"=>"Create Post Error !"));
		}
		else
			return json_encode(array("Function"=>"DbFunctions->setPosts","status"=>"User not found"));
	}
	
	public function getFollower($seourl)
	{
		if(empty($seourl))
			return json_encode("{}");
		else
		{
			global $db;
			
			$query = $this->selectAll("SELECT f.setting FROM category as c,follower as f WHERE c.type='categories' and c.seourl='$seourl' and f.categoryid=c.id");

			if (count($query) == 0) {
				return json_encode("{}");
			} else {
				for($i=0; $i<count($query); $i++)
				{
					$data = $this->PDO_fetch_array($query, $i);
					$setting = json_decode($data['setting'],JSON_UNESCAPED_UNICODE);
						
					$jsondata = array();
					for($j = 0; $j < count($setting['follower']); $j++)
					{
						$user = $setting['follower'][$j]['user'];
						$userquery = $this->selectAll("SELECT username,information FROM users WHERE id=$user");
						if (count($userquery) == 0)
							continue;
						else
						{
							$userdata = $this->PDO_fetch_array($userquery, 0);
							$information = json_decode($userdata['information'],JSON_UNESCAPED_UNICODE);
							$ret = array(
								"name" => $information['name'],
								"surname" => $information['surname'],
								"username" => $userdata['username']
							);
							array_push($jsondata, $ret);
						}
					}
							
				}
			}
				  
			return json_encode($jsondata,JSON_UNESCAPED_UNICODE);
		}
		
	}
	
	public function getMeFollowCategories($authid)
	{
		global $db;
		
		if(empty($authid))
			return json_encode(array("Function"=>"DbFunctions->getMeFollowCategories","status"=>"Authid Empty!"));
		else
		{
			$query = $this->selectAll("SELECT id FROM users WHERE authid = '$authid'");

			if (count($query) == 0) {
				return json_encode(array("Function"=>"DbFunctions->getMeFollowCategories","status"=>"User Not Found!"));
			} else {
				$userdata = $this->PDO_fetch_array($query, 0);
				$userid = $userdata['id'];
				
				$categories = array();
				$queryfollower = $this->selectAll("SELECT categoryid,setting FROM follower WHERE LENGTH(setting) > 15"); //follow user exist query
				for($i = 0; $i < count($queryfollower); $i++)
				{
					$followerdata = $this->PDO_fetch_array($queryfollower, $i);
					$catid = $followerdata['categoryid'];
					$followeruser = json_decode($followerdata['setting']);
					for($j = 0; $j < count($followeruser->follower); $j++){
						if($followeruser->follower[$j]->user == $userid){
							$catret = $this->getCategoriesById($catid,array("id","name","seourl"));
							$categoryinfo = json_decode($catret);
							if(!empty($categoryinfo[0]->name))
								array_push($categories,$categoryinfo[0]);
						}
					}
				}
				return json_encode(array("Function"=>"DbFunctions->getMeFollowCategories","status"=>"Success Get Follower!","categories"=>$categories));
			}
		}
	}
	
	public function getCategoriesById($id,$parameter = array())
	{
		global $db;
		
		if(empty($id))
			return json_encode(array("Function"=>"DbFunctions->getCategoriesById","status"=>"Id Empty!"));
		else
		{
			$query = $this->selectAll("SELECT * FROM category WHERE id = '$id'");
			if (count($query) == 0) {
				return -1;
			} else {
				$data = $this->PDO_fetch_array($query, 0);
				$getd = array();

				for($i = 0; $i < count($parameter); $i++)
				{
					$val = $data[$parameter[$i]];
					$ret[$parameter[$i]] = $val;
				}
				array_push($getd, $ret);
				
				return json_encode($getd);
			}
		}
	}
	
	
	public function getPosts($seourl)
	{
		if(empty($seourl))
			return json_encode("{}");
		else
		{
			global $db;
			
			$query = $this->selectAll("SELECT p.seourl as pseo,p.information as inf,c.seourl as cat,e.seourl as eco,r.seourl as reg FROM posts as p,category as c,category as e,category as r WHERE c.seourl = '$seourl' and p.categoryid = c.id and c.groupid = e.id and e.groupid = r.id ORDER BY p.id desc");

			if (count($query) == 0) {
				return json_encode("{}");
			} else {
				$jsondata = array();
				for($i=0; $i<count($query); $i++){
					$data = $this->PDO_fetch_array($query, $i);
					$information = json_decode($data['inf'],JSON_UNESCAPED_UNICODE);
					if($information)
					{
						$title = $information['title'];
						$description = $information['description'];
						$image = $information['image'];
						$date = $information['date'];
						$category = $data['cat'];
						$ecosystem = $data['eco'];
						$region = $data['reg'];
						$pseo = $data['pseo'];
						$ret = array(
						"title" => $title,
						"description" => $description,
						"image" => "/assets/img/posts/".$image,
						"date" => $date,
						"category" => $category,
						"ecosystem" => $ecosystem,
						"region" => $region,
						"shareurl" => "/dashboard/posts/".$pseo.".html"
						);
						array_push($jsondata, $ret);
					}
				}
				return json_encode($jsondata);
			}
		}
		
	}
	
	public function getPostsMe($authid)
	{
		global $db;
		
		$userid = $this->getExistUser($authid);
		if($userid != -1)
		{
			$query = $this->selectAll("SELECT seourl,information,categoryid FROM posts WHERE sender = $userid");

			if (count($query) == 0) {
				return json_encode(array("Function"=>"DbFunctions->getPostsMe","status"=>"Post not found"));
			} else {
				$jsondata = array();
				for($i=0; $i<count($query); $i++){
					$data = $this->PDO_fetch_array($query, $i);
					$information = json_decode($data['information'],JSON_UNESCAPED_UNICODE);
					if($information)
					{
						$catid = $data['categoryid'];
						$regandecoinf = $this->selectAll("SELECT c.name as categories,k.name as ecosystem,r.name as region FROM category as c, category as k,category as r WHERE c.type = 'categories' and c.id = $catid and c.groupid = k.id and k.groupid = r.id");
						$regandecoinfdata = $this->PDO_fetch_array($regandecoinf, 0);
						$categories = $regandecoinfdata['categories'];
						$ecosystem = $regandecoinfdata['ecosystem'];
						$region = $regandecoinfdata['region'];
				
						$title = $information['title'];
						$description = $information['description'];
						$image = $information['image'];
						$date = $information['date'];
						$pseo = $data['seourl'];
						$ret = array(
						"title" => $title,
						"description" => $description,
						"image" => "/assets/img/posts/".$image,
						"date" => $date,
						"shareurl" => "/dashboard/posts/".$pseo.".html",
						"category" => $categories,
						"ecosystem" => $ecosystem,
						"region" => $region,
						"seourl" => $pseo
						);
						array_push($jsondata, $ret);
					}
				}
				return json_encode($jsondata,JSON_UNESCAPED_UNICODE);
			}
		}
		else
		{
			return json_encode(array("Function"=>"DbFunctions->getPostsMe","status"=>"User not found"));
		}
	}
	
	public function deletePostsMe($authid,$seourl)
	{
		global $db;
		
		$userid = $this->getExistUser($authid);
		if($userid != -1)
		{
			$query = $this->selectAll("SELECT seourl,information FROM posts WHERE sender = $userid AND seourl = '$seourl'");

			if (count($query) == 0) {
				return json_encode(array("Function"=>"DbFunctions->getPostsMe","status"=>"Post not found"));
			} else {
				//$query = $this->selectAll("Delete from posts WHERE sender = $userid and seourl = '$seourl'");
				$query = $this->selectAll("Update from posts status = 0 WHERE sender = $userid and seourl = '$seourl'");
				return json_encode(array("Function"=>"DbFunctions->getPostsMe","status"=>"Delete Operation Change Update Success"));
			}
		}
		else
			return json_encode(array("Function"=>"DbFunctions->getPostsMe","status"=>"User not found"));
	}
	
	public function getAllPosts()
	{
		global $db;
			
		$query = $this->selectAll("SELECT seourl,information FROM posts");

		if (count($query) == 0) {
			return json_encode("{}");
		} else {
			$jsondata = array();
			for($i=0; $i<count($query); $i++){
				$data = $this->PDO_fetch_array($query, $i);
				$information = json_decode($data['information'],JSON_UNESCAPED_UNICODE);
				if($information)
				{
					$title = $information['title'];
					$description = $information['description'];
					$image = $information['image'];
					$date = $information['date'];
					$pseo = $data['seourl'];
					$ret = array(
					"title" => $title,
					"description" => $description,
					"image" => "/assets/img/posts/".$image,
					"date" => $date,
					"shareurl" => "/dashboard/posts/".$pseo.".html"
					);
					array_push($jsondata, $ret);
				}
			}
			return json_encode($jsondata);
		}
	}
	
	public function getExistUser($authid)
	{
		$query = $this->selectAll("SELECT id FROM users WHERE authid = '$authid'");
		$data = $this->PDO_fetch_array($query, 0);
		if(!empty($data))
			return $data['id'];
		return -1;
	}
	
	public function getExistUserAuthById($id)
	{
		$query = $this->selectAll("SELECT authid FROM users WHERE id = '$id'");
		$data = $this->PDO_fetch_array($query, 0);
		if(!empty($data))
			return $data['authid'];
		return -1;
	}
	
	public function setOneSignalUser($authid,$userid)
	{
		global $db;

		$check = $this->selectAll("SELECT information FROM users WHERE authid = '".$authid."'");
		if(count($check) > 0){
			$data = $this->PDO_fetch_Array($check, 0);
			$information = json_decode($data['information'],JSON_UNESCAPED_UNICODE);
			$information['onesignalid'] = $userid;
			$updatejson = json_encode($information);
			$this->selectAll("UPDATE users SET information = '$updatejson' WHERE authid = '$authid'");
			return json_encode(array("Function"=>"DbFunctions->setOneSignalUser","status"=>"Set OneSignalID"));
		}
		else
			return json_encode(array("Function"=>"DbFunctions->setOneSignalUser","status"=>"User not found"));
	}
	
	public function categoriesFollow($authid,$catid,$type)
	{
		global $db;
		
		$querycat = $this->selectAll("SELECT id FROM category WHERE id=$catid");
		$datacat = $this->PDO_fetch_array($querycat, 0);
		if(count($datacat) == 0)
			return json_encode(array("type"=>$type,"status"=>"categories not found"));
		else
		{
			$queryfollower = $this->selectAll("SELECT id,setting FROM follower WHERE categoryid=$catid");
			$datafol = $this->PDO_fetch_array($queryfollower, 0);
			
			$userid = $this->getExistUser($authid);
			if($userid == -1){
				return json_encode(array("type"=>$type,"status"=>"user not found"));
			}
			else
			{
				if($type == "follow")
				{
					if(count($datafol) > 0){
						$id = $datafol['id'];
						$setting = json_decode($datafol['setting'],JSON_UNESCAPED_UNICODE);
						for($i=0; $i<count($setting['follower']); $i++){
							if($setting['follower'][$i]['user'] == $userid){ //ben takip ediyorsam geri dön
								return json_encode(array("type"=>$type,"status"=>"exists user"));
							}
						}
					}
					else
						$setting = array();
					
					$users = array(
						'user' => intval($userid),
						'time' => date("d.m.Y G:i:s")
					);	
						
					if(count($datafol) == 0)
						$setting['follower'] = array($users);
					else
						array_push($setting['follower'], $users);

					$setting = json_encode($setting,JSON_UNESCAPED_UNICODE);

					if(count($datafol) == 0)
					{
						$stmt = $db->prepare ("INSERT INTO follower (categoryid,setting) VALUES (:categoryid,:setting)");
						$stmt->execute(array(
							"categoryid" => $catid,
							"setting" => $setting
						));
					}
					else
						$db->query("UPDATE follower SET setting = '$setting' WHERE id = '$id'");
						
					return json_encode(array("type"=>$type,"status"=>"success follow"));
				}
				elseif($type == "unfollow")
				{
					$searching = false;
					if(count($datafol) > 0)
					{
						$id = $datafol['id'];
						$setting = json_decode($datafol['setting'],JSON_UNESCAPED_UNICODE);
						for($i=0; $i<count($setting['follower']); $i++){
							if($setting['follower'][$i]['user'] == $userid){ //ben takip ediyorsam geri dön
								$searching = true;
								unset($setting['follower'][$i]);
								break;
							}
						}
						if($searching)
						{
							$setting = json_encode($setting,JSON_UNESCAPED_UNICODE);
							$db->query("UPDATE follower SET setting = '$setting' WHERE id = '$id'");
							return json_encode(array("type"=>$type,"status"=>"success unfollow"));
						}
						else
							return json_encode(array("type"=>$type,"status"=>"not found unfollow"));
					}
					else
						return json_encode(array("type"=>$type,"status"=>"error unfollow"));
				}
				else
					return json_encode(array("type"=>$type,"status"=>"unknown operations.."));
			}
		}
	}
	
	
	public function getPostsFollow($auth = null)
	{
		global $db;
		
		$authid = -1;
		if(!empty($auth))
		{
			$querys = $this->selectAll("SELECT id FROM users WHERE authid = '$auth'");
			if(count($querys) > 0)
			{
				$datas = $this->PDO_fetch_array($querys, 0);
				$authid = $datas['id'];
			}
			else
				return json_encode("{}");
		}
		else
			return json_encode("{}");
		
		$jsondata = array();
		
		$query = $this->selectAll("SELECT categoryid,setting FROM follower");
				
		$found = false;
				
		if (count($query) != 0)
		{
			for($i=0; $i<count($query); $i++)
			{
				$data = $this->PDO_fetch_array($query, $i);
				$setting = json_decode($data['setting'], JSON_UNESCAPED_UNICODE);
				$categoryid = $data['categoryid'];
						
				$regandecoinf = $this->selectAll("SELECT c.name as categories,k.name as ecosystem,r.name as region FROM category as c, category as k,category as r WHERE c.type = 'categories' and c.groupid = k.id and k.groupid = r.id");
				$regandecoinfdata = $this->PDO_fetch_array($regandecoinf, 0);
				$categories = $regandecoinfdata['categories'];
				$ecosystem = $regandecoinfdata['ecosystem'];
				$region = $regandecoinfdata['region'];
						
				for($j=0; $j<count($setting['follower']); $j++)
				{
					$userId = $setting['follower'][$j]['user'];
					
					if($userId != $authid)
						continue;
					
					$postquery = $this->selectAll("SELECT * FROM posts WHERE categoryid = $categoryid ORDER BY id desc");
					for($k=0; $k<count($postquery); $k++)
					{
						$found = true;
						$postdata = $this->PDO_fetch_array($postquery, $k);
						$information = json_decode($postdata['information'], JSON_UNESCAPED_UNICODE);
						if($information)
						{
							$title = $information['title'];
							$description = $information['description'];
							$image = $information['image'];
							$date = $information['date'];
							$seourl = $postdata['seourl'];
							$ret = array(
							"title" => $title,
							"description" => $description,
							"image" => "/assets/img/posts/".$image,
							"date" => $date,
							"shareurl" => "/dashboard/posts/".$seourl.".html",
							"category" => $categories,
							"ecosystem" => $ecosystem,
							"region" => $region,
							"seourl" => $seourl
							);
							array_push($jsondata, $ret);
						}
					}
				}		
			}
			return json_encode($jsondata);
		}
		else
			return json_encode("{}");
	}
	
	
	public function __clone()
    {
        return false;
    }
    public function __wakeup()
    {
        return false;
    }
}

?>