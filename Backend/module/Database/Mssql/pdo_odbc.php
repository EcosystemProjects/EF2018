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
    

    public function getConnection($dsn) {
        
        $conn = null;
        try {
            
            $conn = new \PDO($dsn);
            $conn->query("SET CHARACTER SET utf8");
            //Set common attributes
            $conn->setAttribute(\PDO::ATTR_ERRMODE, \PDO::ERRMODE_EXCEPTION);
            
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