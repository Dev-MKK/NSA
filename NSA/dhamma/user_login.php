<?php 
	$message = 'Incorrect login details! Try again.';
	if ( !isset($_POST['phone'], $_POST['password']) ) {
		die ('Access denied!');
	}
	include 'settings.php';
	$setting = new Setting();
	$jsonFile = $setting->userJsonFile;
	
	$current_data = file_get_contents($jsonFile);  
	$data= json_decode($current_data, true);
	$count = count($data);
	$found = false;
	for($x = 0; $x < $count; $x++) {
		if( $_POST['phone'] === $data[$x]["phone"]) {
			if($_POST['password'] === $data[$x]["password"] ) {
				$arr = array(
					'phone' => $data[$x]["phone"],
					'credits' => $data[$x]["credits"]
				);
				$message = json_encode($arr);
				break;
			}
		}
	}
	echo $message;
?>
