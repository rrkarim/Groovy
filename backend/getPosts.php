<?php
	require_once 'include/news.php';
	
	//$country_id = "";
	$user_id = "";
	$id = -1;
	$feed = -1;

	if(isset($_POST['user_id'])){
		$user_id = $_POST['user_id'];
	}

	if(isset($_POST['id'])){
		$id = $_POST['id'];
	}

	if(isset($_POST['feed'])){
		$feed = $_POST['feed'];
	}
	
	// Instance of a User class
	$newsObject = new News();
	
	if($feed == 1) {

		$temp = $newsObject->getFeed($id);	
		echo $temp;
		exit;

	}
	
	// Registration of new user
	if(!empty($user_id)){
		$temp = $newsObject->getNews($user_id, -1, $id);	
		echo $temp;
		exit;
	}
	else {
		$temp = $newsObject->getNews(-1, -1, $id);
		echo $temp;
		exit;
	}
	echo "{success: 0}";
?>