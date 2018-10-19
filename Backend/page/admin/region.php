  <div>
    <table>
      <tr style="background: #765ed7;    color: white;
    height: 60px;">
          <th style="width: 500px;" >Regions</th>
          <th style="width:600px;" >Ecosystems</th>
		  <th style="width: 200px;" >Index</th>

		  <?php
	
		  if (isset($_POST['newregion'])) {
			
			$region = @$_POST['NewRegion'];
			
			$query = $DBFunctions->selectAll("SELECT orderindex FROM category where type = 'region' ORDER BY orderindex DESC");
			if (count($query) == 0) {
				$index = 1; // region yok
			} else {
				$data = $DBFunctions->PDO_fetch_array($query, 0);
				$index = $data['orderindex'] + 1;
			}
			
			$stmt = $db->prepare ("INSERT INTO category (name,type,orderindex,groupid,seourl) VALUES (:name,:type,:orderindex,:groupid,:seourl)");
			$stmt->execute(array(
			"name" => $region,
			"type" => "region",
			"orderindex" => $index,
			"groupid" => 0,
			"seourl" => $SeoFunction->seo($region)
			));
			
			if($stmt)
				echo '<div class="alert alert-success"><strong>Kayıt Başarılı !</strong></div>';
			else
				echo '<div class="alert alert-danger"><strong>Kayıt Başarısız !</strong></div>';
			
		  }
		  
		  ?>
		  
		  <form action="" method="post">
			  <th style="text-align:right;">
				  <input class="BorderRadiusİnput" type="text" name="NewRegion" placeholder="New Region">
			  </th>
			  <th>
				  <input role="button" type="submit" name="newregion" id="newregion" class="orangeButton" value="SAVE">
			  </th>
		  </form>
      </tr>
	  
	  <?php
		if (ob_get_level() == 0)
			ob_start();
		
	    $query = $DBFunctions->selectAll("SELECT * FROM category where type = 'region' ORDER BY orderindex");
		
		if (count($query) == 0) {
			echo '<tr><td>Sistemde Kayıtlı Ülke Yok ! </td></tr>';
		} else {
			for($i=0; $i<count($query); $i++)
			{
				flush();
				ob_flush();
				
				$data = $DBFunctions->PDO_fetch_array($query, $i);
				echo '
				<tr>
					<td>'.$data['name'].'</td>
					<td><a href="admin/ecosystems/'.$SeoFunction->seo($data['name']).'.html">'.$data['name'].'</a></td>
					<td>'.$data['orderindex'].'</td>
					<td style="text-align: right;"><input id="hiddenTextBox" class="hiddenTextBox" type="text" name="region" value="'.$data['id'].'"></td>
					<td>
						<input id="editButton" type="button" name="edit" class="EditDelete" value="EDIT">
						<input id="deleteButton"  class="EditDelete" type="button" name="DeleteandSavebutton" value="DELETE">
					</td>
				</tr>
				';
			}
		}
	  
		ob_end_flush();
	  
	  
	  ?>
	 
    </table>
	
	<script>
          /* not ready */
          jQuery(document).ready(function($) {
            $("#editButton").click(function(e){
                    e.preventDefault();
                if ( $("#hiddenTextBox").css('visibility') === 'visible') {
                      $("#deleteButton").attr("value","DELETE");
                        $("#hiddenTextBox").css("visibility",'hidden');
                           $("#deleteButton").removeClass("GreyButton");
                          $("#deleteButton").addClass("EditDelete");
                    }else {
                    $("#hiddenTextBox").css("visibility","visible");
                       $("#deleteButton").addClass("GreyButton");
                        $("#deleteButton").attr("value","SAVE");
                          $("#editButton").css("visibility","hidden");

                  }
                  });
          });
		  

    /* not ready */
    jQuery(document).ready(function($) {

      $("#deleteButton").click(function(e){



        if ( $('#deleteButton').val('DELETE')) {
          $("#hiddenTextBox").addClass("visibleTextBox");
           $("#hiddenTextBox").attr("readonly","readonly");
             $("#deleteButton").addClass("GreyButton");
                 $("#deleteButton").attr("value","SAVE");
                     $("#hiddenTextBox").attr("value","Are you sure ?");
                        $("#editButton").css("visibility","hidden");
        }
          else {
               SAVE

          }





      });
    });
    </script>

  </div>