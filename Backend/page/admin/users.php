  <div>
  
	<?php
			
			if (isset($_POST['users'])) 
			{
			
				$usersid = @$_POST['users'];
				$authority = @$_POST['authority'];
				
				if($authority == "publisher")
					$authority = 1;
				elseif($authority == "reader")
					$authority = 0;
				elseif($authority == "banned")
					$authority = -1;
				elseif($authority == "admin")
					$authority = 2;
					
				$query = $DBFunctions->selectAll("SELECT information FROM users WHERE id = $usersid");
				if(count($query))
				{
					$data = $DBFunctions->PDO_fetch_array($query, 0);
					$information = json_decode($data['information'],JSON_UNESCAPED_UNICODE);
					$information['authority'] = $authority;
					$information = json_encode($information,JSON_UNESCAPED_UNICODE);
					$db->query("UPDATE users SET information = '$information' WHERE id = '$usersid'");
				}
				
			}
		  
			?>
  
    <table>
      <tr style="background: #765ed7; color: white; height: 60px;">
          <th>Name</th>
          <th>Linkedin</th>
          <th>E-mail</th>
          <th>Signup Date</th>
          <th>Location</th>
          <th>Posts</th>
          <th>Status</th>
      </tr>
	  
	  <?php
	  
		if (ob_get_level() == 0)
			ob_start();
		
	    $query = $DBFunctions->selectAll("SELECT information,id,email FROM users");
		
		if (count($query) == 0) {
			echo '<tr><td>Sistemde Kayıtlı Üye Yok ! </td></tr>';
		} else {
			for($i=0; $i<count($query); $i++)
			{
				flush();
				ob_flush();
				
				$data = $DBFunctions->PDO_fetch_array($query, $i);
				$datainf = json_decode($data['information'], JSON_UNESCAPED_UNICODE);
				$lastip = $datainf['location']['ip'];
				
				$sender = $data['id'];
				$authority = $datainf['authority'];
				$postsquery = $DBFunctions->selectAll("select id from posts where sender = '$sender'");
				echo "
				<tr>
					<form method=\"post\">
					<input type=\"hidden\" name=\"users\" value=\"$sender\" />
					<td>".$datainf['name']."</td>
					<td><a href=".$datainf['linkedin']." target=\"_blank\">GO</a></td>
					<td>".$data['email']."</td>
					<td>".date('d/m/Y H:i:s', $datainf['signup'])."</td>
					<td>".$datainf['location']['city'].",".$datainf['location']['country']."</td>
					<td>".count($postsquery)."</td>
					<td>
						<select class=\"\" name=\"authority\" onchange='this.form.submit()'>
						  <option value=\"publisher\" ".(($authority == 1) ? "selected=\"selected\"" : "").">PUBLISHER</option>
						  <option value=\"reader\" ".(($authority == 0) ? "selected=\"selected\"" : "").">READER</option>
						  <option value=\"banned\" ".(($authority == -1) ? "selected=\"selected\"" : "").">BANNED</option>
						  <option value=\"admin\" ".(($authority == 2) ? "selected=\"selected\"" : "").">ADMIN</option>
						</select>
					</td>
					<noscript><input type=\"submit\" name=\"editusers\" value=\"Submit\"></noscript>
					</form>
				</tr>
				";
			}
		}
	  
		ob_end_flush();
	  
	  
	  ?>
	  
    </table>
  </div>