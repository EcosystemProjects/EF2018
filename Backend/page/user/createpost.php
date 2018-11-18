    <div id="onloadMainEcosystemsPage" style="display:none;" class="main">
	<?php
	if(!$isPublisher)
		return;
	?>
	<br />
      <div class="sendTitle">
          <h4 id="sendTitle_CreatePost">Create New Post</h3>
          <a href="mypost.html">Show My Post</a>
      </div>
        <div class="Linkbox">
			<form method="post" action="">
				<div class="form-group">
				  <input type="text" class="form-control" id="pastelink" placeholder="Paste link to import post title, description and featured image.">
				</div>
				<div class="form-group">
					<button class="whiteButton" id="import" name="import"><b>Import</b></button>
				</div>
			</form>
        </div>
		<br />
            <h4>Or Write Yourself</h3>
			<?php
				
			$cid = $SqlChecker->imtsqlclean(@$_GET["inpage"]);
			$cid = $SqlChecker->CheckGET(htmlspecialchars($cid));
			
			if (isset($_POST['createpost'])) {
				
				$image;
				
				if(isset($_FILES['image'])){
				   $hata = $_FILES['image']['error'];
				   if($hata == 0){
					  $boyut = $_FILES['image']['size'];
					  if($boyut > (1024*1024*5)){
						echo 'Resim boyutu 5MB den büyük olamaz.';
					  }else{
						$tip = $_FILES['image']['type'];
						$isim = $_FILES['image']['name'];
						$uzanti = explode('.', $isim);
						$uzanti = $uzanti[count($uzanti)-1];
						if($tip != 'image/jpeg' || $uzanti != 'jpg') {
							echo 'Yanlızca JPG dosyaları gönderebilirsiniz.';
							die();
						} else {
							$image = $_FILES['image']['name'];
							copy($_FILES['image']['tmp_name'], 'assets/img/posts/'.$image);
						}
					  }
				   }
				}
			
				$title = @$_POST['title'];
				$description = @$_POST['description'];
				$date = date('d-m-Y H:i:s');
				$senderid = $_SESSION['user']->id;
				
				$information = array(
					'title' => $title,
					'description' => $description,
					'image' => $image,
					'date' => $date
				);
				
				$notification = array(
					'mail' => 1,
					'app' => 1
				);
				
				$stmt = $db->prepare ("INSERT INTO posts (sender,categoryid,information,notification,seourl,status) VALUES (:sender,:categoryid,:information,:notification,:seourl,:status)");
				$stmt->execute(array(
				"sender" => $senderid,
				"categoryid" => $cid,
				"information" => json_encode($information,JSON_UNESCAPED_UNICODE),
				"notification" => json_encode($notification,JSON_UNESCAPED_UNICODE),
				"seourl" => $SeoFunction->seo($title),
				"status" => 1
				));
				
				if($stmt)
					echo '<div class="alert alert-success"><strong>Post Oluşturuldu !</strong></div>';
				else
					echo '<div class="alert alert-danger"><strong>Post Oluşturma Başarısız !</strong></div>';
				
			}
			
			if(!empty($cid))
			{
				$query = $DBFunctions->selectAll("SELECT c.name as category,e.name as ecosystem,r.name as region FROM category as c,category as e,category as r WHERE c.id = $cid and e.id = c.groupid and r.id = e.groupid");
						
				if (count($query) == 0) {
					echo 'Böyle bir Kategori Bulunmamaktadır !';
					return;
				} else {
					$data = $DBFunctions->PDO_fetch_array($query, 0);
					$category = $data['category'];
					$ecosystem = $data['ecosystem'];
					$region = $data['region'];
				}
			}
			
			?>
                <form class="formListSend" method="post" action="" enctype="multipart/form-data">
					<div class="form-group">
						<label for="title">Title</label>
						<input type="text" class="form-control" id="title" name="title" placeholder="">
					</div>
					<div class="form-group">
						<label for="description">Description</label>
						<textarea class="form-control" rows="5" id="description" name="description"></textarea>
					</div>
					<div class="form-group">
						<label for="image">Featured Image</label>
						<div class="uploadDiv">
							<input type="file" name="image" id="image" />
                        </div>
					</div>
                <div class="main">
                  <div class="SendPanel">
                      <div class="SendPanel_Inner">
                          <b>REGION</b>
                            <div class="SendPanel_icon selectParent">
                              <select  class="" name="region" id="region">
                                  <option value="<?=$region;?>"><?=$region;?></option>
                              </select>
                            </div>
                          </div>
                        <div class="SendPanel_Inner">
                            <b>ECOSYSTEM</b>
                            <div class="SendPanel_icon selectParent">
                              <select class="SendPanel_icon selectParent" name="ecosystem" id="ecosystem" >
                                  <option value="<?=$ecosystem;?>"><?=$ecosystem;?></option>
                              </select>
                            </div>
                        </div>
                        <div class="SendPanel_Inner">
                            <b>CATEGORY</b>
                            <div class="SendPanel_icon selectParent">
                              <select class="" name="category" id="category" >
                                  <option value="<?=$category;?>"><?=$category;?></option>
                              </select>
                            </div>
                        </div>
                    </div>
                    <div class="SendPanel_Inner_send">
                        <input role="button" class="SendPanel_Sen_btn" type="submit" name="createpost" id="createpost" value="SEND"></input>
                    </div>
                </div>
                </form>
    </div>