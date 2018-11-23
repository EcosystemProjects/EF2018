	<div class="main" id="onloadMainEcosystemsPage" style="display:none;">

			<?php

				$inpage = $SqlChecker->imtsqlclean(@$_GET["inpage"]);
				$inpage = $SqlChecker->CheckGET(htmlspecialchars($inpage));

				if(empty($inpage))
					header("location:home.html");
				else
				{
					$twopage = $SqlChecker->imtsqlclean(@$_GET["twopage"]);
					$twopage = $SqlChecker->CheckGET(htmlspecialchars($twopage));

						function getRemainingTime($times){
							$times = strtotime($times);
							$timesdiff = time() - $times;
							$second = $timesdiff;
							$minute = round($timesdiff/60);
							$hour	= round($timesdiff/3600);
							$day	= round($timesdiff/86400);
							$week	= round($timesdiff/604800);
							$month	= round($timesdiff/2419200);
							$year	= round($timesdiff/29030400);
							
							if($second < 60 ){
								if($second == 0){
									return "az önce";
								}else {
									return $second .' saniye önce';
								}
							}else if($minute < 60){
								return $minute.' dakika önce';
							}else if($hour < 24){
								return $hour.' saat önce';
							}else if($day < 7){
								return $day.' gün önce';
							}else if($week < 4){
								return $week.' hafta önce';
							}else if($month < 12){
								return $month.' ay önce';
							}else{
								return $year.' yıl önce';
							}
						}
					
					if(empty($twopage))
						header("location:home.html");
					else
					{
						if (ob_get_level() == 0)
							ob_start();

						$query = $DBFunctions->selectAll("SELECT p.information as inf,c.name as cat,e.name as eco,r.name as reg,u.information as userinf FROM posts as p,category as c,category as e,category as r,users as u WHERE p.categoryid = $inpage and p.seourl = '$twopage' and p.status=1 and c.id = p.categoryid and c.groupid = e.id and e.groupid = r.id and u.id = p.sender ORDER BY p.id desc");

						if (count($query) == 0) {
							echo '<tr><td>Henüz Paylaşım Yok ! </td></tr>';
						} else {

							for($i=0; $i<count($query); $i++)
							{
								flush();
								ob_flush();

								$data = $DBFunctions->PDO_fetch_array($query, $i);
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
									$userinf = json_decode($data['userinf'],JSON_UNESCAPED_UNICODE);
								}
								else
									header("location: home.html");

								echo '
								</br>
								<p>'.$region.' / '.$ecosystem.' / '.$category.' </p>
								'.(!empty($image) ? '<a href="'.$_SERVER['REQUEST_URI'].'"><img width="800px;" src="assets/img/posts/'.$image.'"></a>' : '').'

								<h3 style="margin-top:20px;">'.$title.'</h3>
								<div class="PostText">
									<p style="text-align:justify" >'.$description.'</p>
								</div>
								<div class="a">';
									echo "<a href=\"javascript:void(0)\" onclick=\"window.open('https://www.linkedin.com/shareArticle?mini=true&url=http://".$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI']."', 'sharer', 'toolbar=0, status=0, width=626, height=436');return false;\" style=\"width:500px;\" class=\"LinkedinButton\" type=\"button\" name=\"button\">Share On Linkedin</a>";
									echo '
									<div  class="inner">
									  <div class="">
										<b>'.$userinf['name']." ".$userinf['surname'].'</b>
										<div style="font-size:13px;">
											<i>'.getRemainingTime($date).'</i> <a href="#">Report</a>
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

				}
			  ?>
    </div>
