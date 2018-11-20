    <div class="main" id="onloadMainEcosystemsPage" style="display:none;">
      <h1 style="font-size:50px;"><?=Region;?></h1>
      <h2 style="font-size:15px;color:#b1b1b1;" ><?=SeeEcosystemWithRegion;?></h2>
          <div class="RegionButton">
			<?php
			
				if (ob_get_level() == 0)
					ob_start();
				
					$query = $DBFunctions->selectAll("SELECT name from category where type='region' order by orderindex asc");
					
					if (count($query) == 0) {
						echo '<tr><td>'.NotFoundRegion.'</td></tr>';
					} else {
						for($i=0; $i<count($query); $i++)
						{
							flush();
							ob_flush();
							
							$data = $DBFunctions->PDO_fetch_array($query, $i);
							$name = $data['name'];
							echo '
							<a href="dashboard/ecosystem/'.$SeoFunction->seo($name).'.html" name="button" value="'.$name.'">'.$name.'</a>
							';
						}
					}
				  
				ob_end_flush();
			  ?>
          </div>
        </div>
