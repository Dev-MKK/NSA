<?php 
	$message = 'Server error. Try again';
	if ( !isset($_POST['phone']) || !isset($_POST['amount']) ) {
		die ('Access denied!');
	}
	include 'settings.php';
	$setting = new Setting();
	$jsonFile = $setting->userJsonFile;
	
	$current_data = file_get_contents($jsonFile);  
	$data= json_decode($current_data, true);
	$count = count($data);
	foreach($data as $k => $v) {
		if($v["phone"] == $_POST['phone']) {
			$data[$k]["credits"] = $v["credits"] + $_POST['amount'];
			$data =json_encode(array_values($data));
			if(file_put_contents($jsonFile, $data)) {
				$message = 'topupok';
				break;
			}
		}
	}
	echo $message;
?>
