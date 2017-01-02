<?php

include_once 'db.php';

class Country {
	
	private $db;
	private $db_table = "countries";
	
	public function __construct(){
		$this->db = new DbConnect();
	}

	public function getAllCountries() {
		$sample = $this->db_table;
		$query = "select * from `$sample`";
		
		$result = mysqli_query($this->db->getDb(), $query);

		$rows = array();
		$len = 0;
		$rows['success'] = 1;
		while($r = mysqli_fetch_assoc($result)) {
		    $rows[] = $r;
		    $len += 1;
		}
		$rows['success'] = $len;
		mysqli_close($this->db->getDb());

		return ($rows);
	}

	
	public function getCountry($country_id) {
		$sample = $this->db_table;
		$query = "select * from  `$sample` where id = '$country_id'";
		
		$result = mysqli_query($this->db->getDb(), $query);

		$rows = array();
		$len = 0;
		while($r = mysqli_fetch_assoc($result)) {
		    $rows[] = $r;
		    $len += 1;
		}
		$rows['success'] = $len;
		mysqli_close($this->db->getDb());

		return ($rows);
	}	

}


?>