<?php

require_once 'include/user.php';

$username = "";
$password = "";
$email = "";

if(isset($_POST['username'])){
	$username = $_POST['username'];
}
if(isset($_POST['password'])){
    $password = $_POST['password'];
}
if(isset($_POST['email'])){
	$email = $_POST['email'];
}

// Instance of a User class
$userObject = new User();

// Registration of new user
if(!empty($username) && !empty($password) && !empty($email)){
	$hashed_password = md5($password);
	$json_registration = $userObject->createNewRegisterUser($username, $hashed_password, $email);
	
	echo json_encode($json_registration);
}

// User Login
if(!empty($username) && !empty($password) && empty($email)){
	$hashed_password = md5($password);	
    $json_array = $userObject->loginUsers($username, $hashed_password);

    echo json_encode($json_array);
}
?>