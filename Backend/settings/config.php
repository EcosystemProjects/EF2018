<?php

	/* Linkedin settings */
	$config['url']             		  =   'http://ecosystemfeed.com/'; // Default System Address
	$config['base_url']             =   'http://ecosystemfeed.com/homepage.html'; // First Page
	$config['callback_url']         =   'http://ecosystemfeed.com/linkedin.html'; // Linkedin Api Information Return Page
	$config['linkedin_access']      =   '';//Linkedin api access key
	$config['linkedin_secret']      =   '';//Linkedin api secret key
	$config['linkedin_scope']       =   'r_basicprofile r_emailaddress w_share';

	/* Database settings */
	$config['host']					=	'localhost'; // Default : localhost , or Ip Address
	$config['port']					=	'';// Mssql 1433 etc.
	$config['dbname']				=	'';// Db Name
	$config['dbuser']				=	'';// Db Username
	$config['dbpass']				=	'';// Db Password
	$config['dbtype']				=	'Mysql'; // Db Query Type Mssql, Mysql, Mysqli, Mongodb, Mariadb

?>
