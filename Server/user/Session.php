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
		
		if($result->rowCount() !=1){
			throw new NotFoundException();
		}
		
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
		if($result->rowCount() != 1){
			$row = $result->fetch(PDO::FETCH_ASSOC);
			try{
				return new Session($row['id']);
			} catch (NotFoundException $unfe) {
            	return NULL;
        	}
		}else{
			return NULL;
		}
	}

	public static function createFromUser($user){
		$pdo = getPDO();
			
		$fk_user = $user->getId();
		$validuntil = time() + SESSION_TIMEOUT;

		//get a new session id
		do {
			$sessionid = md5(time()+rand(0, 32767));
			$sql = "SELECT id from Session where sessionid = '".$sessionid."'";
			$result = $pdo->query($sql);
		} while($result->rowCount() != 0);
		
		$sql = "INSERT into Session (fk_user, sessionid, validuntil) VALUES ('".$fk_user."','".$sessionid."','".$validuntil."')";
		$result = $pdo->query($sql);
		try{
			return new Session($pdo->lastInsertId());
		} catch (NotFoundException $unfe) {
			return NULL;
		}
	}
}

?>