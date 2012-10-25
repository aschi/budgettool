<?php

//$body = http_get_request_body();
$body = $HTTP_RAW_POST_DATA;
var_dump($body);
$decoded = json_decode($body);
var_dump($decoded);
?>