<?php
error_reporting(E_ALL);

if(!isset($HTTP_RAW_POST_DATA)) {
    die("1");
}

$fileData = $HTTP_RAW_POST_DATA;

if(strlen($fileData) < 50) {
    die("2");
}

$imageName = substr(md5(time() . rand(1, 9999)), 0, 16);
file_put_contents("photos/" . $imageName . ".png", $fileData);
file_put_contents("photos/" . $imageName . "_small.png", $fileData);

$ch = curl_init();

curl_setopt($ch, CURLOPT_URL, "http://localhost:30001/camera/purchase");
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

curl_setopt($ch, CURLOPT_HTTPHEADER, ['authToken: matouisgay', 'ssoTicket: ' . $_GET['ssoTicket'], 'photoId: ' . $imageName]);

$server_output = curl_exec($ch);
die($server_output);

curl_close ($ch);


echo $imageName;