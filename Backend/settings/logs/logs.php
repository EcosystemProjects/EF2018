<?php
$text = $_SERVER['QUERY_STRING'];
$words = array("TRUNCATE","truncate","update","UPDATE","drop","DROP","select","SELECT","DELETE","insert into","dete","INSERT INTO","where","WHERE","procedure","PROCEDURE","exec","EXEC","'","select","SELECT","DELETE","insert into","dete","INSERT INTO","where","WHERE","union","UNION","*");
$guvenlik = str_replace($words, '*', $text);
$tarih = date("Y-m-d H:i:s");
function FixWords($text) {
    $text = htmlspecialchars($text);
    $text = str_replace("%27", "'", $text);
    $text = str_replace("%20", " ", $text);
    $text = str_replace("act=", "Page: ", $text);
    $text = str_replace("&amp;", " QueryString: ", $text);
    $text = str_replace("='", " Query: ", $text);
    return $text;}
if ($text != $guvenlik) {
    $words = FixWords($text);
    $dosya = "logs/Logs2.txt";
    touch("$dosya");
    $dosyam = fopen($dosya, 'aw');
    $yazi = "Inject Code: [$words]";
    $yazi.= "IP Adress: $_SERVER[REMOTE_ADDR]";
    $yazi.= "Date: $tarih";
    $yazi.= "------------------------------------------------------------------------------\n";
    fwrite($dosyam, $yazi);
    fclose($dosyam);
	};
 ?>