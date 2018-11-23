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

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"
        integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T"
        crossorigin="anonymous"></script>

<script src="https://cdn.onesignal.com/sdks/OneSignalSDK.js" async=""></script>
<script>
  var OneSignal = window.OneSignal || [];
  OneSignal.push(function() {
    OneSignal.init({
      appId: "10dd1f2b-69a9-4b67-8eae-da3413bd3b70",
      autoRegister: false,
      notifyButton: {
        enable: true,
      },
    });
  });
</script>

    <header class="Header" id="onloadHeaderEco" style="display:none;">
		';
		
		
	
?>