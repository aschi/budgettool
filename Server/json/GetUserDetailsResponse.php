<?php
class GetUserDetailsResponse{
	public $status;
	public $statusText;
	public $username;
	public $email;
	
	public function __construct($status, $statusText, $username, $email){
			$this->status = $status;
			$this->statusText = $statusText;
			$this->username = $username;
			$this->email = $email;
	}
}
?>