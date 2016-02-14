<?php

$connection = mysql_connect("localhost", "root", "");

if(!$connection) {
	die();
}

mysql_select_db("cometsrv", $connection);

$itemDefinitions = mysql_query("select * from furniture where item_name LIKE '%horse_%dye%';");

while($itemDef = mysql_fetch_assoc($itemDefinitions)) {
	die($itemDef['id']);
}