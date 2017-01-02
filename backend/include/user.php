<?php

include_once 'db.php';

class User{
	
	private $db;
	private $db_table = "users";
	
	public function __construct(){
		$this->db = new DbConnect();
	}
	
	public function isLoginExist($email, $password){		
				
		$query = "select id,name,surname,image,image_small,date,fol_count from " . $this->db_table . " where email = '$email' AND password = '$password' Limit 1";
		$result = mysqli_query($this->db->getDb(), $query);


		if(mysqli_num_rows($result) > 0){
			$r = mysqli_fetch_assoc($result);
			$json = $r;		
			$json['success'] = 1;			
		}else{
			$json['success'] = 0;
		}
		mysqli_close($this->db->getDb());
		
		return json_encode($json);		
	}
	
	public function createNewRegisterUser($username, $password, $email){
		
		$query = "INSERT INTO USERS (username, password, email) values ('$username', '$password', '$email')";
		$inserted = mysqli_query($this->db->getDb(), $query);
		
		if($inserted == 1){
			$json['success'] = 1;									
		}else{
			$json['success'] = 0;
		}
		mysqli_close($this->db->getDb());
		
		return $json;
	}
	
	public function loginUsers($username, $password){
			
		$json = array();
		$canUserLogin = $this->isLoginExist($username, $password);
		if($canUserLogin){
			$json['success'] = 1;
		}else{
			$json['success'] = 0;
		}
		return $json;
	}

}


?>