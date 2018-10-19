<?php
	ob_start();
	session_start();
	if($isOnline)
		header("Location:/index.html");
?>

<div class="login">
    <div class="text" style="font: 700 15px Nunito; margin-left: 10px; margin-right:15px;">Welcome!</div>
	<div class="text">
	<?php
	
	if (isset($_POST['login'])) {
		
		$login = @$_POST['username'];
		$password     = @$_POST['password'];
		
		if (empty($login) || empty($password))
			echo '<div class="alert alert-danger"><strong>Hata !</strong> Tüm alanları doldurunuz.</div>';
		else if (strlen($password) < 3)
			echo '<div class="alert alert-danger"><strong>Hata !</strong> Geçersiz parola ( En az 3 karakter gerekli ) .</div>';
		elseif (preg_match('/[çğşöıİÇĞŞÖüÜ]/', $login))
			echo '<div class="alert alert-danger"><strong>Hata !</strong> Email adresinizde Türkçe karakter kullanamazsınız.</div>';
		elseif (preg_match('/[?|!|*|~|:|;|=|(|)|{|}|&|%|+|^]/', $login))
			echo '<div class="alert alert-danger"><strong>Hata !</strong> Email adresinizde geçersiz karakterler içeremez.</div>';
		elseif (preg_match('/[çğşöıİÇĞŞÖüÜ]/', $password))
			echo '<div class="alert alert-danger"><strong>Hata !</strong> Şifrenizde türkçe karakter kullanamazsınız.</div>';
		elseif (preg_match('/[?|!|*|~|:|;|=|(|)|{|}|&|%|+|^]/', $password))
			echo '<div class="alert alert-danger"><strong>Hata !</strong> Şifreniz geçersiz karakterler içeremez.</div>';
		else {
			
			$password = md5($password);
			
			$query = selectAll("SELECT * FROM users WHERE username='$login' or email='$login' or phone='$login'");
			if (count($query) == 0) {
				echo '<div class="alert alert-danger"><strong>Hata !</strong> Sistemde böyle bir kullanıcı bulunmamaktadır .</div>';
			} else {
				$data = PDO_fetch_array($query, 0);
				$datainf = json_decode($data['information'], true);
				
				if (empty($datainf)) {
					echo '<div class="alert alert-danger"><strong>Hata !</strong> Sistem Hatası .</div>';
				} else if ($datainf['password'] != $password) {
					echo '<div class="alert alert-danger"><strong>Hata !</strong> Şifre Yanlış .</div>';
				} else if ($datainf['authority'] == -1) {
					echo '<div class="alert alert-danger"><strong>Hata !</strong> Hesabınızın Girişi Yasaklanmıştır .</div>';
				} else if ($datainf['authority'] != 2) { /* -1 = banned , 0 = user , 1 = moderator , 2 = admin */
					echo '<div class="alert alert-danger"><strong>Hata !</strong> Bu Sayfaya Erişim Yetkiniz Yoktur .</div>';
				} else {
					$_SESSION['token'] = sha1(time() . rand() . ip_bul() . $_SERVER['SERVER_NAME']);
					setcookie("token", $_SESSION['token'], time() + (60 * 60 * 24));
					$user            = new User();
					$user->id        = $data['id'];
					$user->name      = $datainf['name'];
					$user->surname   = $datainf['surname'];
					$user->email     = $data['email'];
					$user->phone     = $data['phone'];
					$user->username  = $data['username'];
					
					$userid = $data['id'];
					
					$authortiyid = $datainf['authority'];
					$authquery = selectAll("select name,information FROM authority where auth = '$authortiyid'");
					if (count($authquery) > 0) {
						$authdata = PDO_fetch_array($authquery, 0);
						$authdatainf = json_decode($authdata['information'], true);
						$user->authority = $authdata['name']; // auth yetkilerini alıp kontrolleri gerçekleştirilecek
					}
					else
						$user->authority = "authproblem";
					
					$dataip               = array(
						"ip" => ip_bul(),
						"time" => time()
					);
					$dataip["lastonline"] = time();
					$datainf['lastip']    = array(
						$datainf['lastip'],
						$dataip
					);
					$dataencode           = json_encode($datainf);

					global $db;
					$db->query("UPDATE users SET information = '$dataencode' WHERE id = '$userid'");
					
					$_SESSION['user'] = $user;
					
					if($user->authority == "admin")
						header("Location: admin.html");
					else if($user->authority == "moderator")
						header("Location: moderator.html");
					else if($user->authority == "normal")
						header("Location: dashboard.html");
					else if($user->authority == "authproblem")
						header("Location: logout.html");
				}
			}
			
		}
	}

	?>
	
    </div>
    <form class="LoginForm" action="" method="post">
		<input id="username" type="text" name="username" placeholder="Email or Phone or Username">
		<input id="password" type="password" name="password" placeholder="Password">
		<input id="login" type="submit" name="login" value="Login">
	</form>
</div>