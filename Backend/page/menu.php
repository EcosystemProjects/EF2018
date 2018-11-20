<?php

echo '
<nav class="navbar navbar-light navbar-expand-lg">
        <div class="container">
            <h5><a class="navbar-brand mr-2" href="#">Ecosystem Feed</a><span class="d-none d-sm-inline-block d-lg-inline-block d-xl-inline-block">by Mentornity</span></h5>
            <button class="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="navbar-collapse collapse" id="navbarCollapse" style="">
                <ul class="navbar-nav ml-auto">
					'.(($isOnline && $isAdmin) ? '
					<li class="nav-item active">
                        <a class="nav-link" href="admin.html"><img src="assets/img/admin-icon.png" width="22px" height="24px" alt=""> '.ADMINPAGE.' <span class="sr-only">(current)</span></a>
                    </li>
					' : '').'
					'.(($isOnline) 
					? '
					<li class="nav-item active">
                        <a class="nav-link" href="home.html"><img src="assets/img/home-icon.png" alt=""> '.HOMEPAGE.' <span class="sr-only">(current)</span></a>
                    </li>
					<li class="nav-item ml-2 mr-2">
                        <a class="nav-link" href="createpost.html"><img src="assets/img/createpost-icon.png" alt=""> '.Createpost.'</a>
                    </li>
					<li class="nav-item ml-2 mr-2">
                        <a class="nav-link" href="dashboard.html"><img src="assets/img/ecosystems.png" alt=""> '.Ecosystems.'</a>
                    </li>
					<li class="nav-item ml-2 mr-2">
                        <a class="nav-link" href="settings.html"><img src="assets/img/settings-icon.png" alt=""> '.Settings.'</a>
                    </li>
					<li class="nav-item ml-2 mr-2">
                        <a class="nav-link" href="logout.html"><img src="assets/img/logout-icon.png" width="22px" height="24px" alt=""> '.Logout.'</a>
                    </li>
					' : '
					<li class="nav-item active">
                        <a class="nav-link" href="homepage.html">Feed <span class="sr-only">(current)</span></a>
                    </li>
                    <li class="nav-item ml-2 mr-2">
                        <a class="nav-link" href="dashboard.html">'.Ecosystems.'</a>
                    </li>
                    <li class="nav-item ml-2 mr-2">
                        <a class="nav-link" href="about.html">'.About.'</a>
                    </li>
                    <div class="dropdown">
                        <button class="btn pl-4 pr-4 btn-custom-transparent" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <img src="assets/img/language.png" alt="">
                        </button>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenu2">
                            <button class="dropdown-item" type="button">'.Turkish.'</button>
                            <button class="dropdown-item" type="button">'.English.'</button>
                        </div>
                    </div>
                    <li class="nav-item ml-2">
                        <a class="btn pl-4 pr-4 btn-custom-blue" href="linkedin.html" style="font-size: 1rem">'.ConnectLinkedin.'</a>
                    </li>
					').'
                </ul>

            </div>
        </div>
    </nav>
		</header>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"
        integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T"
        crossorigin="anonymous"></script>
';

?>
