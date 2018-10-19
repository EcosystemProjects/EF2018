<?php

include_once("pdo_odbc.php");

class Mssql 
{
	private $db;
	
	function __construct($host,$port,$dbname,$dbuser,$dbpass){
		$this->dbConnect($host,$port,$dbname,$dbuser,$dbpass);
	}
	
	public function getDb()
	{
		return $this->db;
	}
	
	function dbConnect($host,$port,$dbname,$dbuser,$dbpass){
		try {
			$pdo = PDOConnection::instance();
			$this->db = $pdo->getConnection("odbc:Driver=SQL Server Native Client 11.0; Server=$host; Port=$port; Database=$dbname; UID=$dbuser; PWD=$dbpass; charset=UTF8");
		} catch ( PDOException $e ){
			print "Mssql Connection Error : ".$e->getMessage();
			die();
		}
	}
}




?>