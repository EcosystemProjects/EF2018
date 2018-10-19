  <?php
  
  $seoregion = @$_GET['region'];
  $seoecosystems = @$_GET['ecosystems'];
  $query = $DBFunctions->selectAll("select c.name as regname,k.id as ecoid,k.name as econame from category as c,category as k where c.seourl='$seoregion' and k.groupid = c.id and k.type = 'ecosystems' and k.seourl='$seoecosystems'");
  if (count($query) == 0) {
	  header("location: /index.html");
  }
  else
  {
	  $data = $DBFunctions->PDO_fetch_array($query, 0);
	  $ecoid = $data['ecoid'];
	  $regname = $data['regname'];
	  $econame = $data['econame'];
  }
  ?>
<table>
  <tr style="background: #765ed7;    color: white;
height: 60px;">
      <th>Region/Ecosystems/Categories</th>
      <th style="width:300px;" >Post</th>
      <th></th>
	  
		<?php
	
		  if (isset($_POST['newcategories'])) {
			
			$name = @$_POST['categories'];
			
			$query = $DBFunctions->selectAll("select orderindex from category where type = 'categories' and groupid in (select id from category where name = '$econame') order by orderindex desc");
			if (count($query) == 0) {
				$index = 1; // kategori yok
			} else {
				$data = $DBFunctions->PDO_fetch_array($query, 0);
				$index = $data['orderindex'] + 1;
			}
			
			$stmt = $db->prepare ("INSERT INTO category (name,type,orderindex,groupid,seourl) VALUES (:name,:type,:orderindex,:groupid,:seourl)");
			$stmt->execute(array(
			"name" => $name,
			"type" => "categories",
			"orderindex" => $index,
			"groupid" => $ecoid,
			"seourl" => $SeoFunction->seo($name)
			));
			
			
			if($stmt)
				echo '<div class="alert alert-success"><strong>Kayıt Başarılı !</strong></div>';
			else
				echo '<div class="alert alert-danger"><strong>Kayıt Başarısız !</strong></div>';
			
		  }
		  
		  ?>
		  <form action="" method="post">
			<th style="text-align: right" >
				<input class="BorderRadiusİnput" type="text" name="categories" id="categories" placeholder="New Categories">
			</th>

		   <th style="text-align:center; width:150px;">
			  <input role="button" type="submit" name="newcategories" id="newcategories" class="orangeButton" value="SAVE">
		  </th>
	    </form>

  </tr>
  
  <?php
		if (ob_get_level() == 0)
			ob_start();
		
	    $query = $DBFunctions->selectAll("SELECT name,id FROM category where type='categories' and groupid = '$ecoid' ORDER BY orderindex ASC");
		
		if (count($query) == 0) {
			echo '<tr><td>Sistemde Kayıtlı Kategori Yok ! </td></tr>';
		} else {
			for($i=0; $i<count($query); $i++)
			{
				flush();
				ob_flush();
				
				$data = $DBFunctions->PDO_fetch_array($query, $i);
				$categoryid = $data['id'];
				$totalposts = $DBFunctions->selectAll("SELECT * FROM posts where categoryid='$categoryid' ORDER BY id ASC");
				
				echo '
				<tr>
					<td>'.$regname." / ".$econame." / <b>".$data['name'].'</b></td>
					<td>'.count($totalposts).'</a></td>
				</tr>
				';
			}
		}
	  
		ob_end_flush();
	  
	  
	  ?>
	  
</table>
