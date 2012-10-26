<?php
require_once 'config.php';
require_once 'user/User.php';
require_once 'user/Session.php';
require_once 'json/Error.php';
require_once 'json/LoginSuccessfull.php';

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
}
?>