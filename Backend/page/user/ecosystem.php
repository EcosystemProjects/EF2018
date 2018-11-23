    <div class="main" id="onloadMainEcosystemsPage" style="display:none;">
      <h1 style="font-size:50px;margin-top:20px;"><?=Ecosystem;?></h1>
      <h2 style="font-size:12px;color:#b1b1b1;margin-bottom:20px;" ><?=SeeCategoryWithEcosystem;?></h2>
          <div class="EcoButton">
			<?php

				$inpage = $SqlChecker->imtsqlclean(@$_GET["inpage"]);
				$inpage = $SqlChecker->CheckGET(htmlspecialchars($inpage));

				if(empty($inpage))
					header("location:dashboard.html");
				else
				{
					if (ob_get_level() == 0)
						ob_start();

					$colors = array('#28BF8B', '#BF2884', '#FF9C00', '#187ADF', '#52A854','#D41B36','#20AB9C','#4E07EE','#A513D5','#FF5A00','#C6C414');

					$query = $DBFunctions->selectAll("SELECT k.name from category as c,category as k where c.type='region' and c.seourl='$inpage' and k.type='ecosystems' and k.groupid=c.id");

					if (count($query) == 0) {
						echo '<tr><td>'.NotFoundEcosystem.'</td></tr>';
					} else {
						for($i=0; $i<count($query); $i++)
						{
							flush();
							ob_flush();

							$data = $DBFunctions->PDO_fetch_array($query, $i);
							$name = $data['name'];
							$color = $data['color'];
							echo '
							<a href="dashboard/category/'.$SeoFunction->seo($name).'.html" style="background-color:'.$colors[$i%11].'" name="button" value="'.$name.'">'.$name.'</a>
							';
						}
					}

					ob_end_flush();

				}
			  ?>
          </div>
        </div>
