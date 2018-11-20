
  <style media="screen">
    .selectParent{
    overflow:hidden;
    }
    .selectParent select{
      font-weight: bold;
      width:100px;
     -webkit-appearance: none;
     -moz-appearance: none;
     appearance: none;
     padding: 2px 2px 2px 20px;
     border: none;
     background: transparent url("http://cdn1.iconfinder.com/data/icons/cc_mono_icon_set/blacks/16x16/br_down.png") no-repeat right center;
    }
  </style>

    <div id="onloadMainEcosystemsPage" class="main" style="display:none">
      <div class="tabs">
        <h5> <button onclick="openTabs('Settings')" class="linkBase openTabs"><?=Settings;?></button></h5>
        <h5><button onclick="openTabs('About')" class="linkBase openTabs"><?=About;?></button></h5>
        <h5><button onclick="openTabs('Contact')" class="linkBase openTabs"><?=Contact;?></button></h5>
      </div>
      <div id="Settings" class="tabName" style="display:block">
	  
		<?php
		$meid = $_SESSION['user']->id;
		$query = $DBFunctions->selectAll("SELECT information FROM users WHERE id = '$meid'");
		
		if(isset($_POST['mobilepush']) && ($_POST['mobilepush'] == 1 || $_POST['mobilepush'] == 0))
		{
			$data = $DBFunctions->PDO_fetch_array($query, 0);
			$datainf = json_decode($data['information'], JSON_UNESCAPED_UNICODE);
			$datainf['mobilepush'] = $_POST['mobilepush'];
			$information = json_encode($datainf);
			$db->query("UPDATE users SET information = '$information' WHERE id = $meid");
			$query = $DBFunctions->selectAll("SELECT information FROM users WHERE id = '$meid'");
		}
		elseif(isset($_POST['emailpush']) && ($_POST['emailpush'] == 1 || $_POST['emailpush'] == 0))
		{
			$data = $DBFunctions->PDO_fetch_array($query, 0);
			$datainf = json_decode($data['information'], JSON_UNESCAPED_UNICODE);
			$datainf['emailpush'] = $_POST['emailpush'];
			$information = json_encode($datainf);
			$db->query("UPDATE users SET information = '$information' WHERE id = $meid");
			$query = $DBFunctions->selectAll("SELECT information FROM users WHERE id = '$meid'");
		}

		$data = $DBFunctions->PDO_fetch_array($query, 0);
		$datainf = json_decode($data['information'], JSON_UNESCAPED_UNICODE);
			
		if(!empty($datainf) > 0)
		{
			$emailpush = $datainf['emailpush'];
			$mobilepush = $datainf['mobilepush'];
		}
		else
		{
			$emailpush = 0;
			$mobilepush = 0;
		}
		if(isset($_POST['lang']))
		{
			$_SESSION['lang'] = $_POST['lang'];
			header("Location: ".(isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? "https" : "http") . "://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]");
			return;
		}
		
		
		?>
	  
        <div class="user">
            <div style="display:grid;margin-bottom:25px;">
                <?=$_SESSION['user']->name." ".$_SESSION['user']->surname;?>
                <a href="logout.html"><?=Logout;?></a>
            </div>
              <div style="margin-bottom:25px;">
                    <img style="border-radius:50px;width:75px;height:75px" src="http://via.placeholder.com/75x75" alt="">
              </div>
            <i><?=Languages;?></i>
            <div class="selectParent">
				<form method="post" action="">
              <select name="lang" style="margin-bottom:25px;" onchange="this.form.submit()">
                    <option value="tr" <?=($_SESSION['lang'] == "tr" ? 'selected="selected"' : '')?>><?=Turkish;?></option>
                    <option value="en" <?=($_SESSION['lang'] == "en" ? 'selected="selected"' : '')?>><?=English;?></option>
              </select>
				</form>
            </div>

        </div>
		
		
		
        <div class="userPanel" style="font-size:1.3rem">
            <i style="margin-right:25px"><?=SendMeMobileNotifications;?></i>
            <form method="post" action="" name="mobile">
              <label class="switch">
					<input type="hidden" id="mobilepush" name="mobilepush" value="<?=$mobilepush;?>">
                    <input type="checkbox" id="mobilecheck" name="mobilecheck" onchange="($(this).prop('checked') ? $('#mobilepush').val(1) : $('#mobilepush').val(0))(this.form.submit() )" <?=($mobilepush == 1 ? 'checked="checked"':'');?>/>
                    <div class="slider round"></div>
                </label>
            </form>
        </div>
        <div class="userPanel" style="font-size:1.3rem">
              <i style="margin-right:25px"><?=SendMeEmailNotifications;?></i>
            <form method="post" action="">
              <label class="switch">
					<input type="hidden" id="emailpush" name="emailpush" value="<?=$emailpush;?>">
                    <input type="checkbox" id="emailcheck" name="emailcheck" onchange="($(this).prop('checked') ? $('#emailpush').val(1) : $('#emailpush').val(0))(this.form.submit() )" <?=($emailpush == 1 ? 'checked="checked"':'');?>/>
                    <div class="slider round"></div>
                </label>
            </form>
        </div>
      </div>
      </div>	  
      <div id="About" class="tabName" style="display:none">
        <div class="main  ">
            <img  class="footer_img" src="img/site-logo-1.png" alt="">
            <h3>Ecosystem Feed</h3>
            <i>by Mentornity</i>
              <div class="devTeam">
                <p style="width:350px;margin-bottom:25px;margin-top:25px;"><?=ABOUTMESSAGE;?></p>
                <p>Web UI Developer:</p>
                <h4><a href="https://www.linkedin.com/in/berk-a%C3%A7%C4%B1kel-83933313b/">Berk Açıkel</a></h4>
                <p>IOS Developer:</p>
                <h4>Ahmet Buğra Peşman</h4>
                <p>Android Developer:</p>
                <h4><a href="https://www.linkedin.com/in/eda-aydin19/">Eda Aydın</a></h4>
                <p>Backend Developers:</p>
                <h4><a href="https://www.linkedin.com/in/berat-kara-889bb5142/">Berat Kara</a> , <a href="https://www.linkedin.com/in/ali-tolak-43b53b12a/">Ali Tolak</a></h4>
              </div>
              <h4 style="margin-top:50px;margin-bottom:25px;">TERMS</h4>
              Copyright © 2018
        </div>
      </div>
      <div id="Contact" class="tabName" style="display:none">
          <div class="main">
                <h3><?=FOLLOWUS;?></h3>
                <div class="">
                  <a  href="facebook.com" style="text-decoration:none">
                    <img class="conctact-icon" src="img/facebook-icon.png" alt="">
                  </a>
                  <a  href="#" ><img class="conctact-icon" src="img/twitter-icon.png" alt=""></a>
                  <a href="#"><img class="conctact-icon" src="img/linkedin-icon.png" alt=""></a>
                </div>
                <h3><?=VISITWEBPAGE;?></h3>
                <a  href="ef.mentornity.com" style="text-decoration:none;">ef.mentornity.com</a>
                <h3><?=WRITEUS;?></h3>
                <a href="ef@mentorniy.com"  style="text-decoration:none;" >ef@mentorniy.com</a>
          </div>
      </div>

  <script>
  (function() {
	  
	  $("#emailcheck").change(function() {
		  debugger;
    if(this.checked) {
        //Do stuff
    }
	});
});
  </script>
  <script>
function openTabs(tabName) {
    var i;
    var x = document.getElementsByClassName("tabName");
    for (i = 0; i < x.length; i++) {
       x[i].style.display = "none";
    }
    document.getElementById(tabName).style.display = "block";
}
</script>
