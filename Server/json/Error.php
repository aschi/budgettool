<?php

class Error{
	public $action = "Error";
	public $errormsg;
	
	public function __construct($errormsg) {
		$this->errormsg = $errormsg;
	}
}

?>