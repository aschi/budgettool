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
			$session = Session::createSession(new User($result->fetchColumn(0)));
			return new LoginSuccessfull($session->getUser()->getUsername(), $session->getSessionid());
		}else{
			return new Error("Login failed!");
		}		
	}
}

?>
