<?php
class User{
	private $pdo;
	
	private $id;
	private $user;
	private $budget;
	
	
	public function __construct($id) {
		$this->pdo = getPDO();
		$sql = "SELECT id, fk_user, group_name, budget from Group where id = '".$id."'";
		$result = $this->pdo->query($sql);
		if($result->rowCount() != 1){
			throw new NotFoundException();
		}else{
			$row = $result->fetch(PDO::FETCH_ASSOC);
			
			$this->id = $row['id'];
			
			try{
				$this->user = new User($row['fk_user']);	
			}catch (NotFoundException $unfe) {
            	$this->user = NULL;
			}
			$this->budget = $row['budget'];
        }		
	}
	
	public function getId(){
		return $this->id;
	}
	
	public function getUser(){
		return $this->user;
	}
		
	public static function create($user, $groupName, $password, $budget){
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
