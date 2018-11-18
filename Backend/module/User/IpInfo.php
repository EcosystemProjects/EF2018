<?php

class IpInfo
{
	protected static $_instance = null;

    public static function instance() {
        
        if ( !isset( self::$_instance ) ) {
            
            self::$_instance = new IpInfo();
            
        }
        
        return self::$_instance;
    }
    

    protected function __construct() {}
    
    function __destruct(){}
	
	public function browserLanguage(){
		return substr($_SERVER['HTTP_ACCEPT_LANGUAGE'], 0, 2);
	}
	
	function getIp(){ 
		if(getenv("HTTP_CLIENT_IP"))
		{ 
			$ip = getenv("HTTP_CLIENT_IP"); 
		}
		elseif(getenv("HTTP_X_FORWARDED_FOR"))
		{ 
			$ip = getenv("HTTP_X_FORWARDED_FOR"); 
			if (strstr($ip, ',')) { 
				$tmp = explode (',', $ip); 
				$ip = trim($tmp[0]); 
			} 
		}
		else
			$ip = getenv("REMOTE_ADDR"); 

		return $ip; 
	}


	function getIpInfo($ip = NULL, $purpose = "location", $deep_detect = TRUE) {
		/* advanced geoplugin api
		echo getIpInfo("Visitor", "Country"); // Turkey
		echo getIpInfo("Visitor", "Country Code"); // TR
		echo getIpInfo("Visitor", "State"); // Kadikoy
		echo getIpInfo("Visitor", "City"); // Istanbul
		echo getIpInfo("Visitor", "Address"); // Kadikoy Istanbul Turkey 34
		*/
		$output = NULL;
		if($ip == NULL || $ip == "Visitor")
			$ip = $this->getIp();
		else if($ip == "::1")
			return "local";
		
		$purpose    = str_replace(array("name", "\n", "\t", " ", "-", "_"), NULL, strtolower(trim($purpose)));
		$support    = array("country", "countrycode", "state", "region", "city", "location", "address", "geolocation");
		$continents = array(
			"AF" => "Africa",
			"AN" => "Antarctica",
			"AS" => "Asia",
			"EU" => "Europe",
			"OC" => "Australia (Oceania)",
			"NA" => "North America",
			"SA" => "South America"
		);
		if (filter_var($ip, FILTER_VALIDATE_IP) && in_array($purpose, $support)) {
			$ipdat = @json_decode(file_get_contents("http://www.geoplugin.net/json.gp?ip=" . $ip));
			if (@strlen(trim($ipdat->geoplugin_countryCode)) == 2) {
				switch ($purpose) {
					case "location":
						$output = array(
							"city"           => @$ipdat->geoplugin_city,
							"state"          => @$ipdat->geoplugin_regionName,
							"country"        => @$ipdat->geoplugin_countryName,
							"country_code"   => @$ipdat->geoplugin_countryCode,
							"continent"      => @$continents[strtoupper($ipdat->geoplugin_continentCode)],
							"continent_code" => @$ipdat->geoplugin_continentCode
						);
						break;
					case "address":
						$address = array($ipdat->geoplugin_countryName);
						if (@strlen($ipdat->geoplugin_city) >= 1)
							$address[] = $ipdat->geoplugin_city;
						$output = implode(", ", array_reverse($address));
						break;
					case "city":
						$output = @$ipdat->geoplugin_city;
						break;
					case "state":
						$output = @$ipdat->geoplugin_regionName;
						break;
					case "region":
						$output = @$ipdat->geoplugin_regionName;
						break;
					case "country":
						$output = @$ipdat->geoplugin_countryName;
						break;
					case "countrycode":
						$output = @$ipdat->geoplugin_countryCode;
						break;
					case "geolocation":
						$latitude = @$ipdat->geoplugin_latitude;
						$longitude = @$ipdat->geoplugin_longitude;
						$geolocation = $latitude.','.$longitude;
						$request ='http://maps.googleapis.com/maps/api/geocode/json?latlng='.$geolocation.'&sensor=false';
						
						$curl = curl_init();
						curl_setopt($curl, CURLOPT_URL, $request);
						curl_setopt($curl, CURLOPT_HEADER, false);
						curl_setopt($curl, CURLOPT_FOLLOWLOCATION, true);
						curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
						curl_setopt($curl, CURLOPT_ENCODING, "");
						$curlData = curl_exec($curl);
						curl_close($curl);

						$address = json_decode($curlData);
						$results  = $address->results[0];
						$addr = $results->formatted_address;
						$comp = $results->address_components;
						if(!empty($comp))
						{
							$response=array();
							
							foreach( $comp as $i => $obj ){
								if( in_array( 'administrative_area_level_2', $obj->types ) || in_array( 'administrative_area_level_1', $obj->types ) ) $response[]=$obj->long_name;
							}
							$output .= implode(',',$response);
						}
						else
						{
							$output = $this->getIpInfo($ip,"address");
						}
						break;
				}
			}
		}
		return $output;
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