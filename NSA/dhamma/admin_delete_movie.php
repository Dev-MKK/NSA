<?php 
	$message = 'Something went wrong';
	$date = '';
	$date = isset($_POST['date']) ? $_POST['date'] : '';
	if(empty($date)) {
		die('Access denied!');
	}
	include 'settings.php';
	$setting = new Setting();
	$jsonFile = $setting->jsonFile;
	
	$current_data = file_get_contents($jsonFile);  
	$data= json_decode($current_data, true);
	$count = count($data);
	$found = false;
	for($x = 0; $x < $count; $x++) {
		if($data[$x]["date"] === $date) {
			$found = true;
			$count = $x;
			break;
		}
	}
	if($found) {
		unset($data[$count]);
		$data =json_encode(array_values($data));
		if(file_put_contents($jsonFile, $data)) {
			$message = 'Video deleted successfully.';
		}
	}
	echo $message;
?>
