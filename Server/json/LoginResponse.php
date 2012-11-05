<?php
class LoginResponse{
	public $status;
	public $username;
	public $sessionid;
	
	public function __construct($status, $username, $sessionid){
			$this->status = $status;
			$this->username = $username;
			$this->sessionid = $sessionid;
	}
}
?>