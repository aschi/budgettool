<?php
class User{
	private $pdo;
	
	private $id;
	private $username;
	private $group;
	
	
	public function __construct($id) {
		$this->pdo = getPDO();
		$sql = "SELECT id, username, group_id from User where id = '".$id."'";
		$row = $this->pdo->query($sql)->fetch(PDO::FETCH_ASSOC);
		
		$this->id = $row['id'];
		$this->username = $row['username'];
		
		//$this->group = new Group($row['group_id']);
		
	}
	
	public function getId(){
		return $this->id;
	}
	
	public function getUsername(){
		return $this->username;
	}
	
	public static function login($username, $password){
		$pdo = getPDO();
		$sql = "SELECT id, password from User where username = '".htmlspecialchars($username)."' and password = '".md5($password)."'";
		$result = $pdo->query($sql);
		
		if($result->rowCount() == 1){
			$session = Session::createFromUser(new User($result->fetchColumn(0)));
			return new LoginResponse("success", $session->getUser()->getUsername(), $session->getSessionid());
		}else{
			return new LoginRespone("failed", $username, "");
		}		
	}
	
	public static function create($username, $password, $email){
		$pdo = getPDO();
		
		//check if username is unique
		$sql = "SELECT id from User where username = '".$username."'";
		$result = $pdo->query($sql);
		if($result->rowCount() != 0){
			return new CreateUserResponse("failed", "duplicate username", $username);
		}

		//check if email is unique
		$sql = "SELECT id from User where email = '".$email."'";
		$result = $pdo->query($sql);
		if($result->rowCount() != 0){
			return new CreateUserResponse("failed", "duplicate email", $username);
		}
		
		//create user
		$sql = "INSERT into User (username, password, email) VALUES ('".$username."','".md5($password)."','".$email."')";
		return new CreateUserResponse("success", "successfully created", $username);
	}
}

?>
