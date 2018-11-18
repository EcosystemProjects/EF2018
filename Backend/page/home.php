    <header class="Header" id="onloadHeaderEco" style="display:none;">
      <div class="container">
        <div class="Header-left">
          <a class="Header-logo">
              <img src="assets/img/site-logo-1.png" alt="">
          </a>
          <div class="Header-left-Text">
            <b>Ecosystem feed</b><b style="font: 400 15px Nunito; color:#6e6e6e"> by Mentornity</b>
          </div>

          </div>
          <div class="Header-Buttons">
                <a href="#"  style="text-decoration: none;">  <img src="assets/img/home-icon.png" alt=""> </a>
                  <a href="#"  style="text-decoration: none;">  <img src="assets/img/createpost-icon.png" alt=""> </a>
                    <a href="#"  style="text-decoration: none;">  <img src="assets/img/ecosystems.png" alt=""> </a>
                      <a href="#"  style="text-decoration: none;">  <img src="assets/img/settings-icon.png" alt=""> </a>
          </div>
        </div>
    </header>
    <div class="main" id="onloadMainEcosystemsPage" style="display:none;">
	
			<?php
			
				if (ob_get_level() == 0)
					ob_start();
				
				$query = $DBFunctions->selectAll("SELECT categoryid,setting FROM follower");
				
				$found = false;
				
				if (count($query) != 0)
				{
					for($i=0; $i<count($query); $i++)
					{
						flush();
						ob_flush();
							
						$data = $DBFunctions->PDO_fetch_array($query, $i);
						$setting = json_decode($data['setting'], JSON_UNESCAPED_UNICODE);
						$categoryid = $data['categoryid'];
						
						
						$regandecoinf = $DBFunctions->selectAll("SELECT c.name as categories,k.name as ecosystem,r.name as region FROM category as c, category as k,category as r WHERE c.type = 'categories' and c.groupid = k.id and k.groupid = r.id");
						$regandecoinfdata = $DBFunctions->PDO_fetch_array($regandecoinf, 0);
						$categories = $regandecoinfdata['categories'];
						$ecosystem = $regandecoinfdata['ecosystem'];
						$region = $regandecoinfdata['region'];
						
						for($j=0; $j<count($setting['follower']); $j++)
						{
							$userId = $setting['follower'][$j]['user'];
							if($userId == $_SESSION['user']->id) //category follower == me id
							{
								$postquery = $DBFunctions->selectAll("SELECT * FROM posts WHERE categoryid = $categoryid ORDER BY id desc");
								for($k=0; $k<count($postquery); $k++)
								{
									$found = true;
									$postdata = $DBFunctions->PDO_fetch_array($postquery, $k);
									$information = json_decode($postdata['information'], JSON_UNESCAPED_UNICODE);
									if($information)
									{
										$title = $information['title'];
										$description = $information['description'];
										$image = $information['image'];
										$date = $information['date'];
										$seourl = $postdata['seourl'];
									}
									
									echo '
									<div class="PicMain">
										<div class="imgM">
											  <img src="assets/img/posts/'.$image.'" alt="">
										</div>
										</br>
										<div class="PicBottom">
											<h6>'.$region.' / '.$ecosystem.' / '.$categories.'</h6>
											<b>'.$title.'  |  '.$date.'</b>
											<div class="">
												<p>'.((strlen($description) > 260) ? substr($description,0,260).'... <a href="/dashboard/posts/'.$seourl.'.html">Devamı</a>' : $description).'</p>
											</div>

										</div>

									</div>
									';
									
								}
							}
						}
						
					}
				  
					ob_end_flush();
			  
				}
				
				if(!$found)
					echo '<tr><td>Henüz Takip Ettiğiniz Kategorilerde Paylaşım Yapılmamış ! </td></tr>';
				
			  ?>
    </div>
