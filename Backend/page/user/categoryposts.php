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

					if(empty($twopage))
						header("location:home.html");
					else
					{
						if (ob_get_level() == 0)
							ob_start();
					
						$query = $DBFunctions->selectAll("SELECT p.information as inf,p.seourl as seourl,c.name as cat FROM posts as p,category as c WHERE p.categoryid = '$inpage' and c.seourl = '$twopage' and p.status = 1 ORDER BY p.id desc Limit 0,50");
						
						if($isPublisher)
							echo '<br/ ><a href="dashboard/createpost/'.$inpage.'/'.$twopage.'.html" class="transparentButton followePannelButton" type="button" name="createpost">'.Createpost.'</a><br/ ><br/ >';
						
						if (count($query) == 0) {
							echo '<tr><td>'.NotFoundPost.'</td></tr>';
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
									
									$seourl = $data['seourl'];
									
									echo '
										<div class="PicMain">
											<div class="imgM">
												  <img src="assets/img/posts/'.$image.'" alt="">
											</div>
											</br>
											<div class="PicBottom">
												<h6>'.$cat.'</h6>
												<b>'.$title.'  |  '.$date.'</b>
												<div class="">
													<p>'.((strlen($description) > 250) ? '<a href="/dashboard/posts/'.$inpage.'/'.$seourl.'.html">'.utf8_decode(substr(utf8_encode($description),0,250)).' ... <b style="color:black;">'.More.'</b></a>' : '<a href="/dashboard/posts/'.$inpage.'/'.$seourl.'.html">'.$description.'</a>').'</p>
												</div>

											</div>

										</div>
										';
								}
							}
						}
					  
						ob_end_flush();
					}
				}
			  ?>
    </div>
