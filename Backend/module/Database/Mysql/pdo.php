<?php

class PDOConnection {
    
    protected static $_instance = null;

    public static function instance() {
        
        if ( !isset( self::$_instance ) ) {
            
            self::$_instance = new PDOConnection();
            
        }
        
        return self::$_instance;
    }
    

    protected function __construct() {}
    
    function __destruct(){}
    

    public function getConnection($host,$username,$password,$db) {
        
        $conn = null;
        try {

			$conn = new PDO("mysql:host=$host;dbname=$db", $username, $password, array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8"));
			
            return $conn;
            
        } catch (PDOException $e) {
            
            //TODO: flag to disable errors?
            throw $e;
            
        }
        catch(Exception $e) {
            
            //TODO: flag to disable errors?
            throw $e;
            
        }
    }
    
    public function __clone()
    {
        return false;
    }
    public function __wakeup()
    {
        return false;
    }
}
?>