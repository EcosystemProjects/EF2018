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
									<a href="dashboard/categoryposts/'.$SeoFunction->seo($name).'.html" style="margin-right:10px;">'.$posts.' Posts</a>
									<a href="dashboard/follower/'.$SeoFunction->seo($name).'.html">'.$follower.' followers</a>
								</div>

								'.(($isOnline) ? '<a href="dashboard/follow/'.$SeoFunction->seo($name).'.html" name="button" value="'.$name.'" class="ecoFollowButton">FOLLOW</a>' : '<a href="homepage.html" name="button" class="ecoFollowButton" disabled="disabled">Disabled</a>').'
							</div>
							';
							
						}
					}
				  
					ob_end_flush();
			  
				}
			  ?>
          </div>
        </div>
