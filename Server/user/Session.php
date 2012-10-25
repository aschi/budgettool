<?php
class Session {
	private $pdo;

	private $id;
	private $user;
	private $sessionid;
	private $validuntil;

	public function __construct($id){
		$this->pdo = getPDO();
		$sql = "SELECT id, fk_user, sessionid, validuntil from Session where id = '".$id."'";
		$result = $this->pdo->query($sql);
		$row = $result->fetch(PDO::FETCH_ASSOC);

		$this->id = $row['id'];
		$this->user = new User($row['fk_user']);
		$this->sessionid = $row['sessionid'];
		$this->validuntil = $row['validuntil'];
	}

	public function isValid(){
		if(validuntil<=time()){
			return true;
		}else{
			return false;
		}
	}
	
	public function getUser(){
		return $this->user;
	}
	
	public function getSessionid(){
		return $this->sessionid;
	}
	
	public static function createFromSessionId($sessionid){
		$pdo = getPDO();
		
		$sql = "SELECT id from Session where sessionid = '".$sessionid."'";
		$result = $pdo->query($sql);
		$row = $result->fetch(PDO::FETCH_ASSOC);
		
		$s = new Session($row['id']);
	}

	public static function createSession($user){
		$pdo = getPDO();
			
		$fk_user = $user->getId();
		$validuntil = time() + SESSION_TIMEOUT;

		//get a new session id
		do {
			$sessionid = md5(time()+random(0, 32767));
			$sql = "SELECT id from Session where sessionid = '".$sessionid."'";
			$result = $pdo->query($sql);
		} while($result->rowCount() != 0);
		
		$sql = "INSERT into Session (fk_user, sessionid, validuntil) VALUES ('".$fk_user."','".$sessionid."','".$validuntil."')";
		$result = $pdo->query($sql);
		return new Session($pdo->lastInsertId());
	}
}

?>