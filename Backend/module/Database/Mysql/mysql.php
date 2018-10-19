<?php

include_once("pdo.php");

class MysqlPdo
{
	private $db;

	function __construct($host,$dbname,$dbuser,$dbpass){
		$this->dbConnect($host,$dbname,$dbuser,$dbpass);
	}
	
	public function getDb()
	{
		return $this->db;
	}

	function dbConnect($host,$dbname,$dbuser,$dbpass){
		try {
			$pdo = PDOConnection::instance();
			$this->db = $pdo->getConnection($host,$dbuser,$dbpass,$dbname);
		} catch ( PDOException $e ){
			print "Mysql Connection Error : ".$e->getMessage();
			die();
		}
	}
}




?>