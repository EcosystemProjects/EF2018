    <div class="main" id="onloadMainEcosystemsPage" style="display:none;">
      
			<?php
			
				$inpage = $SqlChecker->imtsqlclean(@$_GET["inpage"]);
				$inpage = $SqlChecker->CheckGET(htmlspecialchars($inpage));
				
				if(empty($inpage))
					header("location:home.html");
				else
				{
					if (ob_get_level() == 0)
						ob_start();
				
					$query = $DBFunctions->selectAll("SELECT p.information as inf,c.name as cat,e.name as eco,r.name as reg FROM posts as p,category as c,category as e,category as r WHERE p.seourl = '$inpage' and p.status=1 and p.categoryid = c.id and c.groupid = e.id and e.groupid = r.id ORDER BY p.id desc");
					
					if (count($query) == 0) {
						echo '<tr><td>Henüz Paylaşım Yok ! </td></tr>';
					} else {

						for($i=0; $i<count($query); $i++)
						{
							flush();
							ob_flush();
							
							$data = $DBFunctions->PDO_fetch_array($query, $i);
							$information = json_decode($data['inf'],true);
							if($information)
							{
								$title = $information['title'];
								$description = $information['description'];
								$image = $information['image'];
								$date = $information['date'];
								$category = $data['cat'];
								$ecosystem = $data['eco'];
								$region = $data['reg'];
							}
							else
								header("location: home.html");
									
							echo '
							</br>
							<p>'.$region.' / '.$ecosystem.' / '.$category.' </p>
							<a href="'.$_SERVER['REQUEST_URI'].'"><img width="800px;" src="assets/img/posts/'.$image.'"></a>
							
							<h3 style="width: 600px; margin-top:20px;">'.$title.'</h3>
							<div class="PostText">
								<p style="text-align:justify" >'.$description.'</p>
							</div>
							<div class="a">';
								echo "<a href=\"javascript:void(0)\" onclick=\"window.open('http://www.linkedin.com/shareArticle?mini=true&url=http://".$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI']."', 'sharer', 'toolbar=0, status=0, width=626, height=436');return false;\" style=\"width:500px;\" class=\"LinkedinButton\" type=\"button\" name=\"button\">Share On Linkedin</a>";
								echo '
								<div  class="inner">
								  <div class="">
									<b>Stephen Garza</b>
									<div style="font-size:13px;">
										<i>4 days ago</i> <a href="#">Report</a>
									</div>
								  </div>
									<div  style="margin-left:auto;">
									  <img  class="logo_img_post" src="http://via.placeholder.com/50x50" alt="">
									</div>
								</div>
								<div class="sıra">
									<i>Sharing Link</i>
									<a class="linkBase"  style="color:#2c83bf" href="#">https://ef.mentornity.com/Milenial-invest-fasas</a>
								</div>
								<div class="sıra">
									<i>Resource Link</i>
									<a  class="linkBase" style="color:#2c83bf" href="#">https://cnn.com/milenial-invest-asfasf</a>
								</div>
							</div>
							';
						}
					}
				  
					ob_end_flush();
			  
				}
			  ?>
    </div>
