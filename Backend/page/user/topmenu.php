<?php 

	echo '
		<head>
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
		<base href="'.$config['url'].'" />
		
		<title>Ecosystem Feed</title>
		
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		
		<link rel="stylesheet" href="assets/css/web-ui/main.css?id='.time().'">
		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.1.0/css/all.css" integrity="sha384-lKuwvrZot6UHsBSfcMvOkWwlCMgc0TaWr+30HWe3a4ltaBwTZhyTEggF5tJv8tbt" crossorigin="anonymous">
		<link rel="stylesheet" href="assets/css/boostrap/bootstrap.css">
		<link rel="stylesheet" href="assets/css/style.css">
		<link href="https://fonts.googleapis.com/css?family=Nunito:300i,400,700" rel="stylesheet">

		  <body onload="myFunction()">
    <!-- loader -->
     <img id="loader"src="assets/img/ef-loading-gif.gif" alt="">
    <script>
    var myVar;
    /*i want to see the how loader working so time variable is made-up we can change it later */
    function myFunction() {
        myVar = setTimeout(showPage, 1000);
    }

    function showPage() {
      document.getElementById("loader").style.display = "none";
      document.getElementById("onloadMainEcosystemsPage").style.display = "block";
        document.getElementById("onloadHeaderEco").style.display = "block";
		document.getElementById("onloadBottom").style.display = "block";
    }
    </script>
    <!-- End -->

    <header class="Header" id="onloadHeaderEco" style="display:none;">
		';
	
?>