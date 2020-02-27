<?php

	class Setting {
	
		public $jsonFile;

		public function __construct() {
			$this->setup();
		}
		public function setup() {
			$this->jsonFile = 'movies.json';
		}
	}
	
?>
