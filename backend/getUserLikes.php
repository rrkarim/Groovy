<?php
	require_once 'include/likes.php';
	$uid = "";
	if(isset($_POST['uid'])){
		$uid = $_POST['uid'];
	}

	$likeObject = new Likes();
	if(!empty($uid)){
		$temp = $likeObject->getLikes($uid);
		echo $temp;
	}
	else {
		echo "{success: 0}";
	}
?>