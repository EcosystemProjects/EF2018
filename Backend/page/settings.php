
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
        <h5> <button onclick="openTabs('Settings')" class="linkBase openTabs">SETTINGS</button></h5>
        <h5><button onclick="openTabs('About')" class="linkBase openTabs">ABOUT</button></h5>
        <h5><button onclick="openTabs('Contact')" class="linkBase openTabs">CONTACT</button></h5>
      </div>
      <div id="Settings" class="tabName" style="display:block">
        <div class="user">
            <div style="display:grid;margin-bottom:25px;">
                <?=$_SESSION['user']->name." ".$_SESSION['user']->surname;?>
                <a href="logout.html">Log Out</a>
            </div>
              <div style="margin-left:10%; margin-bottom:25px;">
                    <img style="border-radius:50px;width:75px;height:75px" src="http://via.placeholder.com/75x75" alt="">
              </div>
            <i>Languages</i>
            <div class="selectParent">
              <select name="" style="margin-bottom:25px;">

                    <option value="English">English</option>
                    <option value="Turkish">Turkish</option>
              </select>
            </div>

        </div>
        <div class="userPanel" style="font-size:1.3rem">
            <i style="margin-right:25px">Send me mobile notifications about featued post</i>
            <form>
              <label class="switch">
                    <input type="checkbox" id="toggle" checked="false" />
                    <div class="slider round"></div>
                </label>
            </form>
        </div>
        <div class="userPanel" style="font-size:1.3rem">
              <i style="margin-right:25px">Send me e-mail about featued post</i>
            <form>
              <label class="switch">
                    <input type="checkbox" id="toggle1" checked="false" />
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
                <p style="width:350px;margin-bottom:25px;margin-top:25px;">This is an Internall Project that developed by great intern teams of Mentornity, Inc. Meet them and visit their Linkedin profiles.</p>
                <p>Web UI Developer:</p>
                <h4>Berk Açıkel</h4>
                <p>IOS Developer:</p>
                <h4>Ahmet Buğra Peşman</h4>
                <p>Android Developer:</p>
                <h4>Eda Aydın</h4>
                <p>Backend Developers:</p>
                <h4>Ali Tolak , Berat Kara</h4>
              </div>
              <h4 style="margin-top:50px;margin-bottom:25px;">TERMS</h4>
              Copyright © 2018
        </div>
      </div>
      <div id="Contact" class="tabName" style="display:none">
          <div class="main">
                <h3>FOLLOW US</h3>
                <div class="">
                  <a  href="facebook.com" style="text-decoration:none">
                    <img class="conctact-icon" src="img/facebook-icon.png" alt="">
                  </a>
                  <a  href="#" ><img class="conctact-icon" src="img/twitter-icon.png" alt=""></a>
                  <a href="#"><img class="conctact-icon" src="img/linkedin-icon.png" alt=""></a>
                </div>
                <h3>VISIT OUR WEB SITE</h3>
                <a  href="ef.mentornity.com" style="text-decoration:none;">ef.mentornity.com</a>
                <h3>WRITE US</h3>
                <a href="ef@mentorniy.com"  style="text-decoration:none;" >ef@mentorniy.com</a>
          </div>
      </div>

  <script type="text/javascript">
  (function() {
$("#toggle").click(function() {
  if ($(this).is(":checked")) {
    console.log("checked");
  } else {
  console.log("faasfa");
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
