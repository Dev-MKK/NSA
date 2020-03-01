<?php 
	$message = 'Server error updating credits!';
	if ( !isset($_POST['phone']) ) {
		die ('Access denied!');
	}
	include 'settings.php';
	$setting = new Setting();
	$jsonFile = $setting->userJsonFile;
	
	$current_data = file_get_contents($jsonFile);  
	$data= json_decode($current_data, true);
	$count = count($data);
	foreach($data as $k => $v) {
			if($v["phone"] ==  $_POST['phone']) {
				$ks = $v["credits"] - 50;
				$ks = $ks < 0 ? 0 : $ks;
				$data[$k]["credits"] = $ks;
				$data =json_encode(array_values($data));
				if(file_put_contents($jsonFile, $data)) {
					$message = $ks;
					break;
				}
			}
	}
	echo $message;
?>
