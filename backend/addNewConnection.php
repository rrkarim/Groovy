<?php
	require_once 'include/likes.php';
	$uid = "";
	$pid = "";
	if(isset($_POST['uid'])){
		$uid = $_POST['uid'];
	}
	if(isset($_POST['pid'])){
		$pid = $_POST['pid'];
	}
	$likeObject = new Likes();
	
	if(!empty($uid) && !empty($pid)){
		$temp = $likeObject->add($uid, $pid);
		echo $temp;
	}
	else {
		echo "{success: 0}";
	}
?>