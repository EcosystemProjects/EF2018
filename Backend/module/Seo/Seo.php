<?php

class SeoFunction
{
	protected static $_instance = null;

    public static function instance() {
        
        if ( !isset( self::$_instance ) ) {
            
            self::$_instance = new SeoFunction();
            
        }
        
        return self::$_instance;
    }
    

    protected function __construct() {}
    
    function __destruct(){}
	
	function seo($text)
	{
	$find = array('Ç', 'Ş', 'Ğ', 'Ü', 'İ', 'Ö', 'ç', 'ş', 'ğ', 'ü', 'ö', 'ı', '+', '#');
	$replace = array('C', 'S', 'G', 'U', 'I', 'O', 'c', 's', 'g', 'u', 'o', 'i', 'plus', 'sharp');
	$text = str_replace($find, $replace, $text);
	$text = preg_replace("@[^A-Za-z0-9\-_\.\+]@i", ' ', $text);
	$text = trim(preg_replace('/\s+/', ' ', $text));
	$text = str_replace(' ', '-', $text);
	return $text;
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