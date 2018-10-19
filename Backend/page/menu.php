<?php

echo '
<div class="container">
        <div class="Header-left">
          <a class="Header-logo">
              <img src="assets/img/site-logo-1.png" alt="">
          </a>
          <div class="Header-left-Text">
            <b>Ecosystem feed</b><b style="font: 400 15px Nunito; color:#6e6e6e"> by Mentornity</b>
          </div>

          </div>
          <div class="Header-Buttons">
                <a href="'.(($isOnline) ? 'home.html' : 'homepage.html').'"  style="text-decoration: none;">  <img src="assets/img/home-icon.png" alt=""> </a>
                  '.(($isOnline) ? '<a href="createpost.html"  style="text-decoration: none;">  <img src="assets/img/createpost-icon.png" alt=""> </a>' : '').'
                    '.(($isOnline) ? '<a href="dashboard.html"  style="text-decoration: none;">  <img src="assets/img/ecosystems.png" alt=""> </a>' : '').'
                      '.(($isOnline) ? '<a href="settings.html"  style="text-decoration: none;">  <img src="assets/img/settings-icon.png" alt=""> </a>' : '').'
					  '.(($isOnline && $isAdmin) ? '<a href="admin.html"  style="text-decoration: none;">  <img src="assets/img/admin-icon.png" alt=""> </a>' : '').'
						'.(($isOnline) ? '<a href="logout.html"  style="text-decoration: none;">  <img src="assets/img/logout-icon.png" alt=""> </a>' : '').'
		  </div>
        </div>
		</header>
';

?>