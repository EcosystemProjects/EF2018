    <div class="main" id="onloadMainEcosystemsPage" style="display:none;">
		
		<b style="color:#cfcfcf;">Region / Ecosystem  </b>
        
		<h1>CATEGORY</h1>
        <div id="cat" class="CategoryMenu">
            
			<?php
			
				$inpage = $SqlChecker->imtsqlclean(@$_GET["inpage"]);
				$inpage = $SqlChecker->CheckGET(htmlspecialchars($inpage));
				
				if(empty($inpage))
					header("location:dashboard.html");
				else
				{
					function followed($followjson,$id)
					{
						for($i = 0; $i < count($followjson); $i++){
							if($followjson[$i]['id'] == $id)
								return true;
						}
						return false;
					}
					
					if(isset($_SESSION['user']))
						$userid = $_SESSION['user']->id;

					if(isset($_POST['follow']))
					{
						if(!isset($_SESSION['user'])){
							header("location:".$_SERVER['HTTP_REFERER']);
							return;
						}
						
						$catid = $_POST['catid'];
						$queryfollower = $DBFunctions->selectAll("SELECT id,setting FROM follower WHERE categoryid=$catid");
						$datafol = $DBFunctions->PDO_fetch_array($queryfollower, 0);

						if(count($datafol) > 0){
							$id = $datafol['id'];
							$setting = json_decode($datafol['setting'],JSON_UNESCAPED_UNICODE);
							for($i=0; $i<count($setting['follower']); $i++){
								if($setting['follower'][$i]['user'] == $userid){
									echo '<div class="alert alert-info" role="alert">'.existsfollowuser.'div>';
									return;
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
							
						echo '<div class="alert alert-success" role="alert">'.followsuccess.'</div>';
					}
					elseif(isset($_POST['unfollow']))
					{
						if(!isset($_SESSION['user'])){
							header("location:".$_SERVER['HTTP_REFERER']);
							return;
						}
						
						$catid = $_POST['catid'];
						$queryfollower = $DBFunctions->selectAll("SELECT id,setting FROM follower WHERE categoryid=$catid");
						$datafol = $DBFunctions->PDO_fetch_array($queryfollower, 0);
						
						$searchingexists = false;
						if(count($datafol) > 0)
						{
							$id = $datafol['id'];
							$setting = json_decode($datafol['setting'],JSON_UNESCAPED_UNICODE);
							for($i=0; $i<count($setting['follower']); $i++){
								if($setting['follower'][$i]['user'] == $userid){
									$searchingexists = true;
									unset($setting['follower'][$i]);
									$setting['follower'] = array_values($setting['follower']);
									break;
								}
							}
							if($searchingexists)
							{
								$setting = json_encode($setting,JSON_UNESCAPED_UNICODE);
								$db->query("UPDATE follower SET setting = '$setting' WHERE id = '$id'");
								echo '<div class="alert alert-success" role="alert">'.unfollowsuccess.'</div>';
							}
						}
					}
					
					if(!empty($_SESSION['user']))
					{
						$auth = $DBFunctions->getExistUserAuthById($_SESSION['user']->id);
						$followcat = $DBFunctions->getMeFollowCategories($auth);
						$followjson = json_decode($followcat,JSON_UNESCAPED_UNICODE);
					}
					
					if (ob_get_level() == 0)
						ob_start();
				
					$query = $DBFunctions->selectAll("SELECT k.name,k.id from category as c,category as k where c.type='ecosystems' and c.seourl='$inpage' and k.type='categories' and k.groupid=c.id");
					
					if (count($query) == 0) {
						echo '<h3>Henüz Category Yok ! </h3>';
					} else {
						
						for($i=0; $i<count($query); $i++)
						{
							flush();
							ob_flush();
							
							$data = $DBFunctions->PDO_fetch_array($query, $i);
							$name = $data['name'];
							$id = $data['id'];
							$followerquery = $DBFunctions->selectAll("SELECT setting from follower where categoryid=$id");
							
							if(count($followerquery) == 0)
								$follower = 0;
							else
							{
								$followerdata = $DBFunctions->PDO_fetch_array($followerquery, 0);
								$setting = json_decode($followerdata['setting'], true);
								if(empty($setting['follower']))
									$follower = 0;
								else
									$follower = count($setting['follower']);
							}
							
							$postsquery = $DBFunctions->selectAll("SELECT id from posts where categoryid=$id and status=1");
							
							if(count($postsquery) == 0)
								$posts = 0;
							else
								$posts = count($postsquery);
							
							echo '
							</br>
							<h3>'.$name.'</h3>
							<div class="CategoryDetail">
								<div class="CategoryDetailTagA">
									<a href="dashboard/categoryposts/'.$id.'/'.$SeoFunction->seo($name).'.html" style="margin-right:10px;">'.$posts.' Posts</a>
									<a href="dashboard/follower/'.$SeoFunction->seo($name).'.html">'.$follower.' followers</a>
								</div>
								<form action="" method="post">
									<input type="hidden" name="catid" id="catid" value="'.$id.'">
									'.($isOnline ? (followed($followjson['categories'],$id) ? '<button type="submit" name="unfollow" class="ecoFollowButton">'.Unfollow.'</button>' : '<button type="submit" name="follow" class="ecoFollowButton">'.Follow.'</button>')   : '').'
								</form>
							</div>
							';
							
						}
					}
				  
					ob_end_flush();
			  
				}
			  ?>
          </div>
        </div>
