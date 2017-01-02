<?php

require_once 'include/news.php';

$title = "";
$header_image = "";
$text = "";
$type_id = "";



if(isset($_POST['title'])){
	$title = $_POST['title'];
}
if(isset($_POST['header_image'])){
	$header_image = $_POST['header_image'];
}
if(isset($_POST['text'])){
    $text = $_POST['text'];
}
if(isset($_POST['type_id'])){
	$type_id = $_POST['type_id'];
}
$newsObject = new News();

// New post
if(!empty($title) && !empty($header_image) && !empty($text) && !empty($type_id)){

	$json_registration = $newsObject->createNewPost($title, $header_image, $text, $type_id);
	
	echo json_encode($json_registration);
}

?>