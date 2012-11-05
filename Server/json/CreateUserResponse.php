<?php
class CreateUserResponse{
	public $status;
	public $statusText;
	public $username;
	
	public function __construct($status, $statusText, $username){
			$this->status = $status;
			$this->statusText = $statusText;
			$this->username = $username;
	}
}
?>