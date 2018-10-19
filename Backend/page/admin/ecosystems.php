  <?php
  
  $seoregion = @$_GET['region'];
  $query = $DBFunctions->selectAll("select id from category where type = 'region' and seourl = '$seoregion'");
  if (count($query) == 0) {
	  header("location: /index.html");
  }
  else
  {
	  $data = $DBFunctions->PDO_fetch_array($query, 0);
	  $regionid = $data['id'];
  }
  ?>
  <div>
    <table>
      <tr style="background: #765ed7;    color: white;
    height: 60px;">
          <th>Region/Ecosystems</th>
          <th>Color</th>
          <th>Categories</th>
		  
		  <?php
	
		  if (isset($_POST['newecosystems'])) {
			
			$name = @$_POST['ecosystems'];
			
			$query = $DBFunctions->selectAll("select orderindex from category where type = 'ecosystems' and groupid in (select id from category where name = '$region') order by orderindex desc");
			if (count($query) == 0) {
				$index = 1; // ekosistem yok
			} else {
				$data = $DBFunctions->PDO_fetch_array($query, 0);
				$index = $data['orderindex'] + 1;
			}
			
			$stmt = $db->prepare ("INSERT INTO category (name,type,orderindex,groupid,seourl) VALUES (:name,:type,:orderindex,:groupid,:seourl)");
			$stmt->execute(array(
			"name" => $name,
			"type" => "ecosystems",
			"orderindex" => $index,
			"groupid" => $regionid,
			"seourl" => $SeoFunction->seo($name)
			));
			
			if($stmt)
				echo '<div class="alert alert-success"><strong>Kayıt Başarılı !</strong></div>';
			else
				echo '<div class="alert alert-danger"><strong>Kayıt Başarısız !</strong></div>';
			
		  }
		  
		  ?>
		  
		  <form action="" method="post">
			  <th style="text-align:right; width:150px;">
				  <input class="BorderRadiusİnput" type="text" name="ecosystems" id="ecosystems" placeholder="New Ecosystems">
			  </th>
			  <th style="text-align:center;">
				<input role="button" type="submit" name="newecosystems" id="newecosystems" class="orangeButton" value="SAVE">
			  </th>
		  </form>
      </tr>
	  
	  
	  <?php
		if (ob_get_level() == 0)
			ob_start();
		
	    $query = $DBFunctions->selectAll("SELECT * FROM category where type='ecosystems' and groupid = '$regionid' ORDER BY orderindex ASC");
		
		if (count($query) == 0) {
			echo '<tr><td>Sistemde Kayıtlı Ekosistem Yok ! </td></tr>';
		} else {
			for($i=0; $i<count($query); $i++)
			{
				flush();
				ob_flush();
				
				$data = $DBFunctions->PDO_fetch_array($query, $i);
				echo '
				<tr>
					<td>'.$seoregion." / <b>".$data['name'].'</b></td>
					<td>#'.$data['color'].'</a></td>
					<td><a href="admin/categories/'.$seoregion.'/'.$SeoFunction->seo($data['name']).'.html">'.$data['name'].'</a></td>
					<td style="text-align:right;width: 150px;">
					   <button class="EditDelete" type="button" name="button">EDIT</button>
					   <button class="EditDelete" type="button" name="button">DELETE</button>
				    </td>
				</tr>
				';
			}
		}
	  
		ob_end_flush();
	  
	  
	  ?>
	  
	  
    </table>
  </div>
