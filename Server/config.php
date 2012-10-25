<?php
define(MYSQL_DSN, 'mysql:dbname=usr_web419_10;host=localhost');
define(MYSQL_USER, 'web419');
define(MYSQL_PASSWD, '.xDNr%.uzW');

define(SESSION_TIMEOUT, 3600); //3600s

function getPDO(){
	return new PDO(MYSQL_DSN, MYSQL_USER, MYSQL_PASSWD);
}
?>