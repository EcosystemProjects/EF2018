<head>
<meta charset="utf-8">
<base href="<?=$config['url'];?>">
<link rel="stylesheet" href="assets/css/admin-ui/ADMIN-panel.css">
<link rel="stylesheet" href="assets/css/admin-ui/web-ADMIN.css">
<link href="https://fonts.googleapis.com/css?family=Nunito:300i,400,700" rel="stylesheet">
<title>Admin Panel</title>
</head>
<body>
<div class="container">

<div class="PanelMenu">

          <a <?=(($subpage == "users") ? "id=\"CategoriesMenuButton\" style=\"color:#765ed7\"" : ""); ?> class="buttonMenuUsers" href="admin/users.html"><?=(($subpage == "users") ? "<b>Users</b>" : "Users"); ?></a>
          <a <?=(($subpage == "region") ? "id=\"CategoriesMenuButton\" style=\"color:#765ed7\"" : ""); ?> class="buttonMenuUsers" href="admin/region.html"><?=(($subpage == "region") ? "<b>Region</b>" : "Region"); ?></a>
          <a <?=(($subpage == "posts") ? "id=\"CategoriesMenuButton\" style=\"color:#765ed7\"" : ""); ?> class="buttonMenuUsers" href="admin/posts.html"><?=(($subpage == "posts") ? "<b>Posts</b>" : "Posts"); ?></a>
		  <a style="color:#a25555" class="buttonMenuUsers" href="http://<?=$_SERVER['SERVER_NAME']."/dashboard.html";?>"><b>Siteyi Görüntüle : <?=$_SERVER['SERVER_NAME'];?></b></a>
		  
            <div class="UsersLogoutPanel">
                <b style="margin-right: 10px;"><?php echo $_SESSION['user']->email; ?></b>
                <a class="buttonLogoutPanel" href="logout.html">Logout</a>
            </div>
  </div>
    <script type="text/javascript" src="assets/js/jquery-2.2.0.min.js"></script>

    <!-- Script for hide text on click     -->
    <script>

    jQuery(document).ready(function($) {
      $("#inputBox").click(function(){
          $('#inputBox').val('');
      });
        });
    </script>