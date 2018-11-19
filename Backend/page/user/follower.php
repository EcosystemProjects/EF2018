    <style media="screen">
      .followePannelButton{
        margin-top: auto;
        margin-left: auto;
        height: 35px;
      }
      .abc{
        width: 250px;
        text-align: left;
    }
      .tableİmg{
        border-radius: 50%;
        height: 60px;
        width: 60px;
      }
      table {
        border-collapse: collapse;
      }
      td th {
        text-align: left;
      }
      tr:nth-child(even) {background-color: #f2f2f2;}
    </style>

	<div id="onloadMainEcosystemsPage"  class="main" style="display:none">

			<?php

				$inpage = $SqlChecker->imtsqlclean(@$_GET["inpage"]);
				$inpage = $SqlChecker->CheckGET(htmlspecialchars($inpage));

				if(empty($inpage))
					header("location:".$_SERVER['HTTP_REFERER']);
				else
				{
					if (ob_get_level() == 0)
						ob_start();

					$querycat = $DBFunctions->selectAll("SELECT id,name FROM category WHERE type='categories' and seourl='$inpage'");
					if(count($querycat) == 0){
						header("location:".$_SERVER['HTTP_REFERER']);
						return;
					}

					$datacat = $DBFunctions->PDO_fetch_array($querycat, 0);
					$catid = $datacat['id'];
					$catname = $datacat['name'];

					$queryfollower = $DBFunctions->selectAll("SELECT id,setting FROM follower WHERE categoryid=$catid");
					$datafol = $DBFunctions->PDO_fetch_array($queryfollower, 0);

					$userid = $_SESSION['user']->id;

					if(isset($_POST['follow']))
					{
						if(!isset($_SESSION['user'])){
							header("location:".$_SERVER['HTTP_REFERER']);
							return;
						}

						if(count($datafol) > 0){
							$id = $datafol['id'];
							$setting = json_decode($datafol['setting'],JSON_UNESCAPED_UNICODE);
							for($i=0; $i<count($setting['follower']); $i++){
								if($setting['follower'][$i]['user'] == $userid){
									echo '<div class="alert alert-info" role="alert">Zaten Takiptesin !</div>';
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
							
						echo '<div class="alert alert-success" role="alert">Başarılı bir şekilde takip edildi !</div>';
						$queryfollower = $DBFunctions->selectAll("SELECT id,setting FROM follower WHERE categoryid=$catid");
						$datafol = $DBFunctions->PDO_fetch_array($queryfollower, 0);
					}
					elseif(isset($_POST['unfollow']))
					{
						if(!isset($_SESSION['user'])){
							header("location:".$_SERVER['HTTP_REFERER']);
							return;
						}
						
						$searchingexists = false;
						if(count($datafol) > 0)
						{
							$id = $datafol['id'];
							$setting = json_decode($datafol['setting'],JSON_UNESCAPED_UNICODE);
							for($i=0; $i<count($setting['follower']); $i++){
								if($setting['follower'][$i]['user'] == $userid){
									$searchingexists = true;
									unset($setting['follower'][$i]);
									break;
								}
							}
							if($searchingexists)
							{
								$setting = json_encode($setting,JSON_UNESCAPED_UNICODE);
								$db->query("UPDATE follower SET setting = '$setting' WHERE id = '$id'");
								echo '<div class="alert alert-success" role="alert">Başarılı bir şekilde takipten çıkıldı !</div>';
								$queryfollower = $DBFunctions->selectAll("SELECT id,setting FROM follower WHERE categoryid=$catid");
								$datafol = $DBFunctions->PDO_fetch_array($queryfollower, 0);
							}
						}
					}
					
					$searching = false;

					flush();
					ob_flush();

					$setting = json_decode($datafol['setting'],JSON_UNESCAPED_UNICODE);
					
					
					for($i=0; $i<count($setting['follower']); $i++){
						if($setting['follower'][$i]['user'] == $userid){
							$searching = true;
							break;
						}
					}

					$queryposts = $DBFunctions->selectAll("SELECT id FROM posts WHERE categoryid=$catid");

					echo '
							<div class="followerPannel">
							<div class="abc">
							  <h4>'.$catname.'</h4>
							  <div style="display:flex">
								<i style="margin-right: 25px;" >'.count($queryposts).' Post</i>
								 <i>'.count($setting['follower']).' Followers</i>
							  </div>
							</div>
							<form style="margin:auto" action="" method="post">
								'.($searching ? '<button class="transparentButton followePannelButton" type="submit" name="unfollow">Unfollow</button>' : '<button class="transparentButton followePannelButton" type="submit" name="follow">Follow</button>').'
							</form>
						  </div>
						  <div class="main">
							<table>
					';

					for($j = 0; $j < count($setting['follower']); $j++)
					{
						$user = $setting['follower'][$j]['user'];
						$userquery = $DBFunctions->selectAll("SELECT * FROM users WHERE id=$user");
						if (count($userquery) == 0)
							continue;
						else
						{
							$userdata = $DBFunctions->PDO_fetch_array($userquery, 0);
							$information = json_decode($userdata['information'],JSON_UNESCAPED_UNICODE);
							echo '<tr>';
							echo '<td><img src="http://via.placeholder.com/75x75" alt="" class="tableİmg" /></td>';
							echo '<td>'.$information['name']." ".$information['surname'].'</td>';
							echo '</tr>';
						}
					}

				ob_end_flush();

				}
			  ?>

        </table>
    </div>
    </div>
