<?php 
	$message = 'Something went wrong';
	$title = '';
	$type = '';
	$vlink = '';
	$mlink = '';
	$title = isset($_POST['title']) ? $_POST['title'] : '';
	$type = isset($_POST['type']) ? $_POST['type'] : '';
	$vlink = isset($_POST['vlink']) ? $_POST['vlink'] : '';
	$mlink = isset($_POST['mlink']) ? $_POST['mlink'] : '';
	
	if(empty($title)||empty($type)||empty($vlink)||empty($mlink)) {
		die('Access denied!');
	}
	include 'settings.php';
	$setting = new Setting();
	$jsonFile = $setting->jsonFile;
	
	$current_data = file_get_contents($jsonFile);  
	$array_data = json_decode($current_data, true);
	$array_data[] = array(  
		'title' => $title,
		'type' => $type,
		'vlink' => $vlink,
		'mlink' => $mlink,
		'date' => '' . date("hisadmY")
	);
	$final_data =json_encode($array_data);
	if(file_put_contents($jsonFile, $final_data)) {
		$message = 'Success!';
	}
	echo $message;
?>
