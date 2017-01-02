<?php
	require_once 'include/user.php';
	$password = "";
	$email = "";
	if(isset($_POST['email'])){
		$email = $_POST['email'];
	}
	if(isset($_POST['password'])){
	    $password = $_POST['password'];
	}
	$userObject = new User(); //Instance of a User class
	if(!empty($password) && !empty($email)) { // User Login
		$hashed_password = md5($password);	
	    $json = $userObject->isLoginExist($email, $hashed_password);
	    echo $json;
	}
	else {
		echo "{success: 0}";
	}
?>