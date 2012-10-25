<?php
class LoginSuccessfull{
	public $action = "Login Successfull";
	public $username;
	public $sessionid;
	
	public function __construct($username, $sessionid){
			$this->username = $username;
			$this->sessionid = $sessionid;
	}
}
?>