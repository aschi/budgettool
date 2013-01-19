<?php
require_once 'config.php';
require_once 'user/User.php';
require_once 'user/Session.php';
require_once 'json/LoginResponse.php';
require_once 'json/CreateUserResponse.php';

/*
 * $pdo->query($sql) 
 */

//$body = http_get_request_body();
$body = $HTTP_RAW_POST_DATA;
$decoded = json_decode($body);

switch($decoded->action){
	case "LoginRequest":
			print json_encode(User::login($decoded->username, $decoded->password));
		break;
	case "CreateUserRequest":
			print json_encode(User::create($decoded->username, $decoded->password, $decoded->email));
		break;
	case "GetUserDetailsRequest":
		try{
			$s = Session::createFromSessionId($decoded->sessionid);
			if($s != NULl && $s->isValid()){
				print json_encode($s->getUser()->getUserDetails());
			}else{
				print json_encode(new GetUserDetailsResponse("failed", "session invalid", "", ""));
			}
		}catch(NotFoundException $unfe) {
            print json_encode(new GetUserDetailsResponse("failed", "exception", "", ""));;
        }
			
		break;
	case "CreateGroupRequest":
		try{
			$s = Session::createFromSessionId($decoded->sessionid);
			if($s != NULL && $s->isValid()){
				print json_encode($s->getUser()->getUserDetails());
			}else{
				print json_encode(new GetUserDetailsResponse("failed", "session invalid", "", ""));
			}
		}catch(NotFoundException $unfe) {
            print json_encode(new GetUserDetailsResponse("failed", "exception", "", ""));;
        }
		break;
}
?>