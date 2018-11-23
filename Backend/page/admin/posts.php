            <div>

			<?php
			
			if (isset($_POST['posts'])) 
			{
			
				$postsid = @$_POST['posts'];
				$status = @$_POST['status'];
				
				if($status == "banned")
					$status = 0;
				else
					$status = 1;
				
				$db->query("UPDATE posts SET status = '$status' WHERE id = '$postsid'");
			
			}
			elseif(isset($_GET['mail']) || isset($_GET['app']))
			{
				$postsid = @$_GET['posts'];
				$mail = @$_GET['mail'];
				$app = @$_GET['app'];
				$catidget = @$_GET['catid'];
				
				if($mail && ($mail != "on" && $mail != "off"))
					return;
				if($app && ($app != "on" && $app != "off"))
					return;
				
				$query = $DBFunctions->selectAll("SELECT id,notification FROM posts WHERE id = $postsid");
				if(count($query))
				{
					$data = $DBFunctions->PDO_fetch_array($query, 0);
					$postid = $data['id'];
					$notification = json_decode($data['notification'],JSON_UNESCAPED_UNICODE);
					if($mail)
						if($mail == "on"){
							$notification['mail'] = 1;
							$db->query("INSERT INTO `process` (`id`, `type`, `postid`, `catid`) VALUES (NULL, '1', '$postid', '$catidget')");//type 1 - mail sent 
						}elseif($mail == "off")
							$notification['mail'] = 0;
					if($app)
						if($app == "on"){
							$notification['app'] = 1;
							$db->query("INSERT INTO `process` (`id`, `type`, `postid`, `catid`) VALUES (NULL, '2', '$postid', '$catidget')");//type 2 - application sent
						}elseif($app == "off")
							$notification['app'] = 0;
					
					$notification = json_encode($notification,JSON_UNESCAPED_UNICODE);
					$db->query("UPDATE posts SET notification = '$notification' WHERE id = '$postsid'");
						
				}
				
			}
		  
			?>
			
              <table>
                <tr style="background: #765ed7;    color: white;
              height: 60px;">
                    <th>Title</th>
                    <th>User</th>
					<th>Email</th>
                    <th>Region</th>
                    <th>Ecosystem</th>
                    <th>Category</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Mailing</th>
                    <th>App Notification</th>
                </tr>
				
				<?php
				if (ob_get_level() == 0)
					ob_start();
				
				$query = $DBFunctions->selectAll("SELECT r.name as rname,e.name as ename,c.name as cname,c.id as catid,p.id as id,p.information as information,p.sender as sender,p.categoryid as categoryid,p.notification as notification,p.status as status,p.seourl as seourl FROM posts as p,category as r,category as e,category as c WHERE p.categoryid = c.id and e.id = c.groupid and r.id = e.groupid");
				
				if (count($query) == 0) {
					echo '<tr><td>Henüz Post Yok ! </td></tr>';
				} else {
					for($i=0; $i<count($query); $i++)
					{
						flush();
						ob_flush();
						
						$data = $DBFunctions->PDO_fetch_array($query, $i);
						$postsdatainf = json_decode($data['information'], JSON_UNESCAPED_UNICODE);
						$postsid = $data['id'];
						$postsstatus = $data['status'];
						$postsnotification = json_decode($data['notification'],JSON_UNESCAPED_UNICODE);
						$postsmail = $postsnotification['mail'];
						$postsapp = $postsnotification['app'];
						$catid = $data['catid'];
						$seourl = $data['seourl'];
						
						$userid = $data['sender'];
						$userquery = $DBFunctions->selectAll("SELECT information,email FROM users WHERE id = $userid");
						$userdata = $DBFunctions->PDO_fetch_array($userquery, 0);
						$useremail = $userdata['email'];
						$userdatainf = json_decode($userdata['information'], JSON_UNESCAPED_UNICODE);
						echo "
						<tr>
							<form method=\"post\">
							<input type=\"hidden\" name=\"posts\" value=\"$postsid\" />
							<td><a href=\"dashboard/posts/".$catid."/".$seourl.".html\" target=\"blank\">".$postsdatainf['title']."</a></td>
							<td>".$userdatainf['name']." ".$userdatainf['surname']."</td>
							<td>".$useremail."</td>
							<td>".$data['rname']."</td>
							<td>".$data['ename']."</td>
							<td>".$data['cname']."</td>
							<td>".$postsdatainf['date']."</td>
							<td>
							  <select class=\"\" name=\"status\" onchange='this.form.submit()'>
								<option value=\"live\" ".($postsstatus ? "selected=\"selected\"" : "").">LIVE</option>
								<option value=\"banned\" ".($postsstatus ? "" : "selected=\"selected\"").">BANNED</option>
							  </select>
							</td>
							<td>
								".($postsmail ? "<a class=\"GreyButton\" href=\"admin/posts.html?posts=$postsid&mail=off&catid=$catid\">SEND OFF</a>" : "<a class=\"GreyButton\" href=\"admin/posts.html?posts=$postsid&mail=on&catid=$catid\">SEND ON</a>")."
							</td>
							<td>
								".($postsapp ? "<a class=\"GreyButton\" href=\"admin/posts.html?posts=$postsid&app=off&catid=$catid\">SEND OFF</a>" : "<a class=\"GreyButton\" href=\"admin/posts.html?posts=$postsid&app=on&catid=$catid\">SEND ON</a>")."
							</td>
							<noscript><input type=\"submit\" name=\"editposts\" value=\"Submit\"></noscript>
							</form>
						</tr>
						";
					}
				}
			  
				ob_end_flush();
			  
			  
			  ?>
				
              </table>

            </div>