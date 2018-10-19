    <style media="screen">
      .followerPannel{
        width: 500px;
        display: flex;
        margin: 25px;
        height: 100px;
      }
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
					header("location:/dashboard.html");
				else
				{
					if (ob_get_level() == 0)
						ob_start();
				
					$querycat = $DBFunctions->selectAll("SELECT id,name FROM category WHERE type='categories' and seourl='$inpage'");
					if(count($querycat) == 0)
						header("location:/dashboard.html");
					
					$datacat = $DBFunctions->PDO_fetch_array($querycat, 0);
					$catid = $datacat['id'];
					$catname = $datacat['name'];
					
					$queryfollower = $DBFunctions->selectAll("SELECT id,setting FROM follower WHERE categoryid=$catid");
					$datafol = $DBFunctions->PDO_fetch_array($queryfollower, 0);
					
					if(isset($_POST['follow']))
					{
						if(empty($_SESSION['user']))
							header("location:/dashboard.html");
						
						$id = $datafol['id'];
						$setting = json_decode($datafol['setting'],true);
						for($i=0; $i<count($setting['follower']); $i++)
							if($setting['follower'][$i]['user'] == $_SESSION['user']->id) //ben takip ediyorsam geri dön
								return;
					
						$users = array(
						'user' => intval($_SESSION['user']->id),
						'time' => date("d.m.Y G:i:s")
						);
						if(count($datafol['setting']) == 0)
							$setting['follower'] = array($users);
						else
							array_push($setting['follower'], $users);
						
						$setting = json_encode($setting,JSON_UNESCAPED_UNICODE);

						if(count($datafol['setting']) == 0)
						{
							$stmt = $db->prepare ("INSERT INTO follower (categoryid,setting) VALUES (:categoryid,:setting)");
							$stmt->execute(array(
							"categoryid" => $catid,
							"setting" => $setting
							));
						}
						else
							$db->query("UPDATE follower SET setting = '$setting' WHERE id = '$id'");
						
						$url = $_SERVER['REQUEST_URI'];
						header("location: $url");
					}
					
					flush();
					ob_flush();
							
					$setting = json_decode($datafol['setting'],true);
							
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
							<form action="" method="post">
								<button class="transparentButton followePannelButton" type="submit" name="follow">Follow</button>
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
							$information = json_decode($userdata['information'],true);
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
	