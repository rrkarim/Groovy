<?php
	for($i = 0; $i < 3500000; $i++) {

	}
	require_once 'include/news.php';
	$id = "";
	$uid = "";

	if(isset($_POST['id'])){
		$id = $_POST['id'];
	}
	if(isset($_POST['uid'])){
		$uid = $_POST['uid'];
	}

	$newsObject = new News();
	if(!empty($id) && !empty($uid)){
		$temp = $newsObject->postPost($uid, $id);
		echo $temp;
	}
	else {
		echo "{success: 0}";
	}
?>