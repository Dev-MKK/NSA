<?php

	class Setting {
	
		public $jsonFile;
		public $userJsonFile;

		public function __construct() {
			$this->setup();
		}
		public function setup() {
			$this->jsonFile = 'movies.json';
			$this->userJsonFile = 'users.json';
		}
	}
	
?>
