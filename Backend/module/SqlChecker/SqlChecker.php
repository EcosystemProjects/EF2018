<?php

class SqlChecker
{
	protected static $_instance = null;

    public static function instance() {
        
        if ( !isset( self::$_instance ) ) {
            
            self::$_instance = new SqlChecker();
            
        }
        
        return self::$_instance;
    }
    

    protected function __construct() {}
    
    function __destruct(){}
	
	public function GetPostChecker()
	{
		/* get , post security */
		foreach($_GET as $check => $value):
			$_GET[$check] = $this->imtsqlclean(htmlspecialchars(strip_tags(addslashes($value))));
		endforeach;

		foreach($_POST as $check => $value):
			$_POST[$check] = $this->imtsqlclean(htmlspecialchars(strip_tags(addslashes($value))),1);
		endforeach;
	}
	
	public function imtsqlclean($deger,$sorgutipi = 0){
		if($sorgutipi == 1){
			$message = "<b>Post Data:</b> ".$deger."\r\n";
			$handle =fopen('./siteyeyapilansaldirilar.php', 'a'); 
			fwrite($handle,$message); 
			fclose($handle);
		}
		$deger = htmlspecialchars(strip_tags(addslashes($deger)));	
		$kelimeler = array("document","location","alert","(",")","script","window","*","+","\'","~","/",":",";",">","<","?","=","&","#","%","{","}","|","\"","select","union","join","where","insert","drop","truncate","table","0x",'"',"'"); 
		$sonuc = str_replace($kelimeler,"",$deger); 
		$kelimeler = array_map('strtoupper', $kelimeler);
		$sonuc2 = str_replace($kelimeler,"",$sonuc);
		if($sorgutipi == 1){
			$message = "<b>Post Data Cleaned:</b> ".$sonuc2."\r\n<br />";
			$handle =fopen('./siteyeyapilansaldirilar.php', 'a'); 
			fwrite($handle,$message); 
			fclose($handle);
		}
		return $sonuc2; 
	} 


	public function CheckGET($text) {

		$words = array("TRUNCATE","truncate","update","UPDATE","drop","DROP","select","SELECT","DELETE","insert into","dete","INSERT INTO","where","WHERE","<",">","script","document",
		"procedure","PROCEDURE","exec","EXEC","'","select","SELECT","DELETE","insert into","dete","INSERT INTO","where","WHERE","%20","beetwen","union","dbo"," or ","%20or%20",'"',"+"," and"," and ");
		$guvenlik = str_replace($words, '*', $text);
		  
		  return $guvenlik;
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